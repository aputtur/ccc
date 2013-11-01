package com.copyright.ccc.web.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.oue.client.OpenUrlExtensionUtils;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.LandingForm;
import com.copyright.ccc.web.forms.LoginForm;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.transaction.TransactionConstants;

/**
 * Accept and process OpenURL requests.  OpenURL more-or-less replaces PD.
 *
 * @author Michael Jessop &lt;&gt;
 * @version $Rev: 110553 $
 *
 * Design:          This class should endeavor to accept and process
 *                  OpenURL requests to search and
 *                  display results.  Because I am not sure whether or not
 *                  this class will handle incoming requests directly, or
 *                  secondarily I decided to have it make use of the
 *                  http parameter string instead of forms.  It will create
 *                  the SearchForm and set it up appropriately, search,
 *                  then pass it onto the landing page.
 *
 * Note:            I decided to keep this module separate from SearchAction
 *                  because a) SearchAction is becoming unruly and needs to
 *                  be refactored and b) this code bypasses pages in favor
 *                  of results.
 *
 * Joke:            A string walks into a bar and tries to order a drink.
 *                  The bartender says "Sorry, we don't serve strings here."
 *                  The string leaves, disappointed.  A few days later he
 *                  comes in again looking tangled and worn.  He sidles
 *                  up to the bar and asks for a drink.  The bartender says
 *                  "Hey, aren't you that string that came in a few days ago?"
 *                  The string answers "No, I'm a frayed knot!"
 */
public final class OpenURLAction extends CCAction
{
    //  OpenURL constants.  These should possibly go in
    //  the WebConstants class.
    
    private static final String EMPTY_STRING  = "";
    private static final String SERVICE_NAME  = "servicename";
    
    //  Keywords for the servicename parameter.  Grouped items
    //  show pay-per-and annual options.  The new PPU parameters
    //  show only the pay-per-use options.
    
    private static final String SVC_PHOTOCOPY       = "photocopy";
    private static final String SVC_ILL             = "ill";
    private static final String SVC_COURSEPACK      = "coursepack";
    private static final String SVC_ECOURSEPACK     = "ecoursepack";
    private static final String SVC_ECOURSEPACK_ALT = "e-coursepack";
    private static final String SVC_DIGITAL         = "digital";
    private static final String SVC_REPUBLICATION   = "republication";
    private static final String SVC_ACADEMIC        = "academic";
    private static final String SVC_BUSINESS        = "business";
    private static final String SVC_ALL             = "all";
    private static final String SVC_PPU             = "ppu";
    private static final String SVC_PPU_ACADEMIC    = "ppuacademic";
    private static final String SVC_PPU_BUSINESS    = "ppubusiness";
    
    //  Keywords for the criteria parameter.
    
    private static final String CRI_ALWAYS    = "always";
    private static final String CRI_EXISTS    = "exists";
    private static final String CRI_AVAILABLE = "available";
    private static final String CRI_GRANTED   = "granted";
    
    //  PD constants.
    
    private static final String SPAGE           = "spage";
    private static final String EPAGE           = "epage";
    
    //  Shared constants.
    private static final String ON        = "on";
    private static final String OFF       = "off";
    private static final String DELIMITER = ",";
    
    //  Flow control constants.
    
    private static final String FWD_SUCCESS  = "success";
    private static final String FWD_FAILURE  = "failure";
    private static final String FORM_SEARCH  = "searchForm";
    private static final String FORM_LANDING = "landingForm";
    
    //  This parameter was added to restrict the method of payment for
    //  the customer.  It can have one of three values:  CC, INV or ALL.
    //  If CC is the value, only credit card options will show up during
    //  the purchase process.  If INV, only INV, if ALL, both will show.
    
    private static final String METHOD_OF_PMT_RESTRICTOR = "mpr";

    //  2013-08-28  MSJ
    //  No longer using WebTrends, shifting to Google Analytics.
    //  WT.mc_id out, utm_source in.

    private static final String UTM_SOURCE = "utm_source";

    //  2012-06-08  MSJ
    //  SID hasn't been used here, it has been in the image server only,
    //  for being able to customize images for the client.

    private static final String PARM_SID = "sid";
    
    //  Local variables.
    
    private String      _destination = null;
    private SearchForm  _form        = null;
    private LandingForm _landing     = null;
    private boolean     _autologin   = false;
    
    /**
     * What to do if no "operation" is specified.
     */
    public ActionForward defaultOperation( ActionMapping       mapping
                                         , ActionForm          form
                                         , HttpServletRequest  request
                                         , HttpServletResponse response )
    {
        return query(mapping, form, request, response);
    }
     
    // OpenURL and PermissionsDirect have ever-so-slightly different
    // behavior.  Both services provide querystring-based login and
    // search behavior.  PD also automates handling the search results
    // and proceeding to the order process.  We are moving to handle 
    // them identically, since we eventually want to replace our PD
    // querystring parameters with the OpenURL standard.  It should
    // also be noted that while we accept any and all OpenURL 
    // parameters, we do not HANDLE all OpenURL parameters, only a
    // subset with additional keywords of our own.
    // 
    // (See bottom of sources for unsupported parameters.)
    //
    // http://www.exlibrisgroup.com/sfx_openurl_syntax.htm
    //
    // The aforementioned link is very useful.  Now here is the
    // breakdown of the parameters used by both OpenURL and PD:
    //
    // OpenURL                     PD
    // -------------------------   ---------------------------------
    // servicename                 prdabrv
    // issn                        stdnum
    // isbn                        title
    // date                        copies          --+
    //                             firstpage         |
    //                             lastpage          +- Order parms
    //                             pages             |
    //                             chapterarticle  --+
    // 
    // Shared by OpenURL and PD
    // -------------------------
    // username
    // password
    //
    // As you can see above, the major difference is that PD continues
    // on (automatically, if everything goes smoothly) to the order
    // process once the search is complete.  OpenURL stops at the
    // landing page.
     
    public ActionForward query( ActionMapping       mapping
                              , ActionForm          form
                              , HttpServletRequest  request
                              , HttpServletResponse response )
    {
        String pName;
        String serviceName = null;
        String issn = null;
        String isbn = null;
        String date = null;
        String title = null;
        String copies = null;
        String fpage = null;
        String lpage = null;
        String pages = null;
        String usr = null;
        String pwd = null;
        String pmt = null;
        String prd = null;
        String utm_source = null;
        String sid = null;
        
        // We need a search form to work with.
         
        _form = new SearchForm();
        _destination = FWD_SUCCESS;
        
        UserContextService.setSessionInitiator( TransactionConstants.PERMISSIONS_DIRECT_ORDER_SOURCE_CODE );

//		KM: Yanking this so we don't create carts so early in the session       
/*      if ( UserContextService.getCOICart() == null ) {
        	UserContextService.setCOICart(CartServices.getCart());
       	}
        
        if ( UserContextService.getCOICart() != null ) {
        	if ( !UserContextService.getCOICart().getCartSource().equals(OrderSource.PERMISSIONS_DIRECT) ) {
        		UserContextService.getCOICart().setCartSource(OrderSource.PERMISSIONS_DIRECT);
        		CartServices.updateCart(UserContextService.getCOICart());
        	}
        }
        
        _logger.debug("Cart Source is: " + UserContextService.getCOICart().getCartSource().name()+ "=" + UserContextService.getCOICart().getCartSource().getCode());
*/
        // Pull out our querystring parameters, stuff into form
        // as appropriate.
        @SuppressWarnings("unchecked")
        Enumeration<String> pKeys = request.getParameterNames();
        
        while (pKeys.hasMoreElements())
        {
            pName = pKeys.nextElement();
            
            if (pName.equalsIgnoreCase( SERVICE_NAME )) serviceName = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.ISSN )) issn = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.ISBN )) isbn = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.DATE )) date = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.TITLE )) title = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.COPIES )) copies = request.getParameter( pName );
            if (pName.equalsIgnoreCase( SPAGE )) fpage = request.getParameter( pName );
            if (pName.equalsIgnoreCase( EPAGE )) lpage = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.PAGES )) pages = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.USERNAME )) usr = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.PASSWORD )) pwd = request.getParameter( pName );
            if (pName.equalsIgnoreCase( METHOD_OF_PMT_RESTRICTOR )) pmt = request.getParameter( pName );
            if (pName.equalsIgnoreCase( UTM_SOURCE )) utm_source = request.getParameter( pName );
            if (pName.equalsIgnoreCase( PARM_SID )) sid = request.getParameter( pName );
        }
        
        if (serviceName != null && serviceName.length() > 0) 
            serviceName = serviceName.trim();
        
        //  This stinks, but we need to clean up the parameters when
        //  they come from the apache server, for some reason.  I am
        //  not even sure if this will fix the problem.
        
        try
        {
            if (!isNull( title )) title = URLDecoder.decode( title, "UTF-8" );
            if (!isNull( date )) date = URLDecoder.decode( date, "UTF-8" );
            if (!isNull( issn )) issn = URLDecoder.decode( issn, "UTF-8" );
            if (!isNull( isbn )) isbn = URLDecoder.decode( isbn, "UTF-8" );
            if (!isNull( serviceName )) serviceName = URLDecoder.decode( serviceName, "UTF-8" );
            if (!isNull( copies )) copies = URLDecoder.decode( copies, "UTF-8" );
            if (!isNull( fpage )) fpage = URLDecoder.decode( fpage, "UTF-8" );
            if (!isNull( lpage )) lpage = URLDecoder.decode( lpage, "UTF-8" );
            if (!isNull( pages )) pages = URLDecoder.decode( pages, "UTF-8" );
            if (!isNull( usr )) usr = URLDecoder.decode( usr, "UTF-8" );
            if (!isNull( pwd )) pwd = URLDecoder.decode( pwd, "UTF-8" );
            if (!isNull( pmt )) pmt = URLDecoder.decode( pmt, "UTF-8" );
            if (!isNull( utm_source)) utm_source = URLDecoder.decode( utm_source, "UTF-8" );
            if (!isNull( sid )) sid = URLDecoder.decode( sid, "UTF-8" );
        }
        catch(UnsupportedEncodingException e) 
        {
            //  Do nothing for now.
            
            _logger.error("Error decoding URL text.", e);
        }

        //  If we have a Google Analytics source we want to stuff that sucker into the
        //  session.  It will be used later on, in ConfirmCartPurchaseAction.

        if (isNull( utm_source ) && !isNull( sid )) utm_source = sid;

        if (!isNull( utm_source )) {
            request.getSession()
                .setAttribute( WebConstants.SessionKeys.UTM_SOURCE, utm_source );

            if (_logger.isDebugEnabled()) {
                _logger.info("\nUTM_SOURCE = " + utm_source);
            }
        }
        
        _autologin = ((usr != null) && !usr.equals( "" ) &&
                      (pwd != null) && !pwd.equals( "" ));
        
        _form.setSource( SearchForm.OPENURL_SOURCE );
         
        //  Only one value should ever be specified, so this should
        //  be valid, but if more than one is specified, I'd rather
        //  grab the isbn.  If neither of specified, that's a
        //  problem.
         
        if (!isNull( isbn ))
        {
            _form.setTitleOrStdNo( isbn );
        }
        else
        {
            if (!isNull( issn )) 
            {
                _form.setTitleOrStdNo( issn );
                _form.setIsIssnSearch( true );  //  Flag ISSN for result filtering.
            }
            else 
            {
                if (!isNull( title )) 
                {
                    _form.setTitleOrStdNo( title );
                }
                else
                {
                    _destination = FWD_FAILURE;
                }
            }
        }
        
        //  Servicename can have multiple parameters.  Valid parameter
        //  values are:
        //
        //  photocopy           TRS
        //  coursepack          APS
        //  ecoursepack         ECC
        //  e-coursepack        ECC
        //  digital             DPS
        //  republication       RLS
        //  academic            TRS, APS and ECC + Academic Annual
        //  business            TRS, DPS and RLS + Business Annual
        //  all                 TRS, APS, ECC, DPS, RLS + Annual
        //  ppuacademic         TRS, APS and ECC
        //  ppubusiness         TRS, DPS and RLS
        //  ppu                 TRS, APS, ECC, DPS, RLS
        //
        //  We need to extract the keywords, interpret them and set
        //  the appropriate values on our search form.  If nothing was
        //  specified, default to "all."
        
        if (isNull( serviceName )) serviceName = SVC_ALL;
        serviceName = serviceName.toLowerCase();
        String[] products = serviceName.split( DELIMITER );
        
        //  O.K.  This is not super efficient, but hopefully it is
        //  readable.  The caller can specify ANY COMBINATION of
        //  products.  Some will overlap, some seem contrary but
        //  that's how we decided to do it...  there are no OFFs,
        //  only ONs...  :)  Unless of course YOUR cup is always
        //  half empty and never half full.
        
        _form.setAll( OFF );
        
        for (int i = 0; i < products.length; i++)
        {
            prd = products[i];
            if (prd != null && prd.length() > 0) 
            {
                prd = prd.trim();
            
            	if (prd.equals( SVC_PHOTOCOPY )) {       _form.setTrsIll( ON ); _form.setTrsPhoto( ON ); }
            	if (prd.equals( SVC_COURSEPACK ))       _form.setAps( ON );
            	if (prd.equals( SVC_ECOURSEPACK ))      _form.setEcc( ON );
            	if (prd.equals( SVC_ECOURSEPACK_ALT ))  _form.setEcc( ON );
            	if (prd.equals( SVC_DIGITAL ))          _form.setDps( ON );
            	if (prd.equals( SVC_REPUBLICATION ))    _form.setRls( ON );
            
            	if (prd.equals( SVC_ACADEMIC ))
            	{
                	_form.setTrsIll( ON );
                	_form.setTrsPhoto( ON );
                	_form.setAps( ON );
                	_form.setEcc( ON );
                	_form.setArs( ON );
            	}
            
            	if (prd.equals( SVC_BUSINESS ))
            	{
                	_form.setTrsIll( ON );
                	_form.setTrsPhoto( ON );
                	_form.setDps( ON );
                	_form.setRls( ON );
                	_form.setAas( ON );
                	_form.setDra( ON );
            	}
            	if (prd.equals( SVC_ALL ))
            	{
                	_form.setTrsIll( ON );
                	_form.setTrsPhoto( ON );
                	_form.setAps( ON );
                	_form.setEcc( ON );
                	_form.setDps( ON );
                	_form.setRls( ON );
                	_form.setAas( ON );
                	_form.setDra( ON );
                	_form.setArs( ON );
            	}
            	if (prd.equals( SVC_PPU_ACADEMIC ))
            	{
                	_form.setTrsIll( ON );
                	_form.setTrsPhoto( ON );
                	_form.setAps( ON );
                	_form.setEcc( ON );
            	}
            
            	if (prd.equals( SVC_PPU_BUSINESS ))
            	{
                	_form.setTrsIll( ON );
                	_form.setTrsPhoto( ON );
                	_form.setDps( ON );
                	_form.setRls( ON );
            	}
            
            	if (prd.equals( SVC_PPU ))
            	{
                	_form.setTrsIll( ON );
                	_form.setTrsPhoto( ON );
                	_form.setAps( ON );
                	_form.setEcc( ON );
                	_form.setDps( ON );
                	_form.setRls( ON );
            	}
        	}
        }
        //  Save off the types of use!
        
        _form.persistTypesOfUse();
        if (_logger.isDebugEnabled()) _logger.info(_form.toString());
        
        //  Set the publication date filter.  Date is a string
        //  with the format of:  YYYY[-MM[-DD]]
        
        if (!isNull( date ) && (date.length() >= 4))
        {
            String tmp = date.substring( 0, 4 );
            _form.setYear( tmp );
        }
        else _form.setYear( null );
        
        request.getSession().removeAttribute( WebConstants.SessionKeys.COPIES );
        request.getSession().removeAttribute( WebConstants.SessionKeys.FIRST_PAGE );
        request.getSession().removeAttribute( WebConstants.SessionKeys. LAST_PAGE );
        request.getSession().removeAttribute( WebConstants.SessionKeys.PAGES );
        request.getSession().removeAttribute( WebConstants.SessionKeys.CHAPTER_ARTICLE );
         
        request.getSession().setAttribute( WebConstants.SessionKeys.COPIES, copies );
        request.getSession().setAttribute( WebConstants.SessionKeys.FIRST_PAGE, fpage );
        request.getSession().setAttribute( WebConstants.SessionKeys.LAST_PAGE, lpage );
        request.getSession().setAttribute( WebConstants.SessionKeys.PAGES, pages );
        request.getSession().setAttribute( WebConstants.SessionKeys.CHAPTER_ARTICLE, null );

        request.getSession().setAttribute( FORM_SEARCH, _form );
        
        //  Payment method needs to be checked and stuffed into
        //  our session for future use.
        
        UserContextService.getUserContext().setMethodOfPmtRestrictor( pmt );

        //  The following bit redirects to a protected action
        //  which prefills the login form with the u/p, auto
        //  submits it and redirects to the _destination I
        //  also provided.
        
        if (_autologin)
        {
            LoginForm loginForm = new LoginForm();
            
            loginForm.setAutoLoginUsername(usr);
            loginForm.setAutoLoginPassword(pwd);
            loginForm.setAutoLoginForward("autoSearch.do");
            
            request.getSession().setAttribute("loginForm", loginForm);
            request.setAttribute("loginForm", loginForm);
            
            String autoLoginPath = "/autoLogin.do";
                
            if(_logger.isDebugEnabled())
            {
                _logger.debug( "OURLAction: Auto login redirect!" );
                _logger.debug( "OURLAction: Path = " + autoLoginPath );
            }
            request.getSession().setAttribute( FORM_SEARCH, _form );
            return new ActionForward( autoLoginPath );
        }
        
        //  Go to autosearch.

        if (_logger.isDebugEnabled()) _logger.info(_form.toString());
        request.getSession().setAttribute( FORM_SEARCH, _form );
        if (_destination == FWD_SUCCESS)
        {
            OpenUrlExtensionUtils.updateSession(request);
        }
        return mapping.findForward( _destination );
    }
          
    private boolean isNull( String s )
    {
        return (s == null || s.equals( EMPTY_STRING ));
    }
    
     // *****************************************************************
     // These are the additional (unsupported) OpenURL querystring
     // parameters.  Some of them might be implemented in the future.
     //
     // sid
     // id
     // pid
     // genre
     // aulast
     // aufirst
     // auinit
     // auinitl
     // auinitm
     // eissn
     // coden
     // sici
     // bici
     // title
     // stitle
     // atitle
     // volume
     // part
     // issue
     // spage
     // epage
     // pages
     // artnum
     // ssn
     // quarter
     //
     // Many of them would map directly to our current Permissions
     // Direct parameters (spage, epage, pages, title, etc.)  We have
     // not, however, defined the ongoing usage of this product just
     // yet so until then, they will be ignored.
     // *****************************************************************
}
