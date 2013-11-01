package com.copyright.ccc.web.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.LoginForm;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.transaction.TransactionConstants;

/**
 * Accept and process Permissions Direct requests.
 *
 * @author Michael Jessop &lt;&gt;
 * @version $Rev: 110553 $
 *
 */
public final class PermissionsDirectAction extends CCAction
{
    //  Permissions Direct constants.
    
    private static final String EMPTY_STRING         = "";    
    private static final String PRODUCT_ABBREVIATION = "prdabrv";
    private static final String STANDARD_NUMBER      = "stdnum";
  
    private static final String PUBLISHER_NAME       = "publishername";
    
    // Permissions direct parameters that are for internal use only.
    // These are not intended to be part of the publicly documented
    // api.
    private static final String WR_WRK_INST			 = "wrWrkInstInternal";
    
    //  Shared constants.
    
    private static final String USERNAME  = "username";
    private static final String PASSWORD  = "password";
    private static final String ON        = "on";
    private static final String OFF       = "off";
    private static final String FWD_SUCCESS  = "success";
    private static final String FORM_SEARCH  = "searchForm";
    
    //  This parameter was added to restrict the method of payment for
    //  the customer.  It can have one of three values:  CC, INV or ALL.
    //  If CC is the value, only credit card options will show up during
    //  the purchase process.  If INV, only INV, if ALL, both will show.
    
    private static final String METHOD_OF_PMT_RESTRICTOR = "mpr";

    //  2013-08-28  MSJ
    //  No longer using WebTrends, shifting to Google Analytics.
    //  WT.mc_id out, utm_source in.

    private static final String UTM_SOURCE = "utm_source";
    private static final String LINK_FROM_RS = "rs_ccom";
    
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
    // and proceeding to the order process.
     
    public ActionForward query( ActionMapping       mapping
                              , ActionForm          form
                              , HttpServletRequest  request
                              , HttpServletResponse response )
    {
    	String destination;
    	SearchForm searchForm;
    	boolean autologin = false;
        String pName;
        String prdabrv = null;
        String pubname = null;
        String stdnum = null;
        String title = null;
        String copies = null;
        String fpage = null;
        String lpage = null;
        String pages = null;
        String chapter = null;
        String usr = null;
        String pwd = null;
        String pmt = null;
        String wrWrkInst = null;
        String utm_source = null;
    
        // We need a search form to work with.
         
        searchForm = new SearchForm();
        destination = FWD_SUCCESS;
        
        UserContextService.setSessionInitiator( TransactionConstants.PERMISSIONS_DIRECT_ORDER_SOURCE_CODE );  
        
        // Pull out our querystring parameters, stuff into form
        // as appropriate.  Since parameters are case sensitive and
        // we are mandated to maintain backward compatibility (with ASP)
        // we need to handle it differently...

        @SuppressWarnings("unchecked")
        Enumeration<String> pKeys = request.getParameterNames();
        
        while (pKeys.hasMoreElements())
        {
            pName = pKeys.nextElement();
         
            if (pName.equalsIgnoreCase( PRODUCT_ABBREVIATION )) prdabrv = request.getParameter( pName );
            if (pName.equalsIgnoreCase( STANDARD_NUMBER )) stdnum = request.getParameter( pName );
            if (pName.equalsIgnoreCase( PUBLISHER_NAME )) pubname = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.TITLE )) title = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.COPIES )) copies = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.FIRST_PAGE )) fpage = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.LAST_PAGE )) lpage = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.PAGES )) pages = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WebConstants.RequestKeys.CHAPTER_ARTICLE )) chapter = request.getParameter( pName );
            if (pName.equalsIgnoreCase( USERNAME )) usr = request.getParameter( pName );
            if (pName.equalsIgnoreCase( PASSWORD )) pwd = request.getParameter( pName );
            if (pName.equalsIgnoreCase( METHOD_OF_PMT_RESTRICTOR )) pmt = request.getParameter( pName );
            if (pName.equalsIgnoreCase( WR_WRK_INST )) wrWrkInst = request.getParameter( pName );
            if (pName.equalsIgnoreCase( UTM_SOURCE )) utm_source = request.getParameter( pName );
        }
        
        //  This stinks, but we need to clean up the parameters when
        //  they come from the apache server, for some reason.  I am
        //  not even sure if this will fix the problem.
        
        try
        {
            if (!isNull( title )) title = URLDecoder.decode( title, "UTF-8" );
            if (!isNull( stdnum )) stdnum = URLDecoder.decode( stdnum, "UTF-8" );
            if (!isNull( prdabrv )) prdabrv = URLDecoder.decode( prdabrv, "UTF-8" );
            if (!isNull( pubname )) pubname = URLDecoder.decode( pubname, "UTF-8" );
            if (!isNull( copies )) copies = URLDecoder.decode( copies, "UTF-8" );
            if (!isNull( fpage )) fpage = URLDecoder.decode( fpage, "UTF-8" );
            if (!isNull( lpage )) lpage = URLDecoder.decode( lpage, "UTF-8" );
            if (!isNull( pages )) pages = URLDecoder.decode( pages, "UTF-8" );
            if (!isNull( chapter )) chapter = URLDecoder.decode( chapter, "UTF-8" );
            if (!isNull( usr )) usr = URLDecoder.decode( usr, "UTF-8" );
            if (!isNull( pwd )) pwd = URLDecoder.decode( pwd, "UTF-8" );
            if (!isNull( pmt )) pmt = URLDecoder.decode( pmt, "UTF-8" );
            if (!isNull( wrWrkInst )) wrWrkInst = URLDecoder.decode( wrWrkInst, "UTF-8" );
            if (!isNull( utm_source )) utm_source = URLDecoder.decode( utm_source, "UTF-8" );
        }
        catch(UnsupportedEncodingException e) 
        {      
            _logger.error( "Error URL decoding request parameters " + LogUtil.appendableStack( e ) );
            throw new CCCRuntimeException( e );
        }
        
        //  If we have a Google Analytics source we want to stuff that sucker into the
        //  session.  It will be used later on, in ConfirmCartPurchaseAction.

        if (!isNull( utm_source )) {
            request.getSession()
                .setAttribute( WebConstants.SessionKeys.UTM_SOURCE, utm_source );

            if (utm_source.equals(LINK_FROM_RS)) {
            	//eliminate session expired message RSPH-497 when linking from RightSphere
            	UserContextService.setSessionNewlyCreated(false);
            }
            if (_logger.isDebugEnabled()) {
                _logger.info("\nUTM_SOURCE = " + utm_source);
            }
        }

        autologin = ((usr != null) && !usr.equals( "" ) &&
                      (pwd != null) && !pwd.equals( "" ));
        
        searchForm.setSource( SearchForm.PD_SOURCE );
        
        if ( StringUtils.isNotEmpty( prdabrv ) )
        {
            searchForm.setAll( OFF );
            searchForm.setAps( OFF );
            searchForm.setDps( OFF );
            searchForm.setEcc( OFF );
            searchForm.setRls( OFF );
            searchForm.setRlr( OFF );
            searchForm.setTrsIll( OFF );
            searchForm.setTrsPhoto( OFF );
            if (prdabrv.equalsIgnoreCase( "aps" )) searchForm.setAps( ON ); 
            else if (prdabrv.equalsIgnoreCase( "dps" )) searchForm.setDps( ON ); 
            else if (prdabrv.equalsIgnoreCase( "ecc" )) searchForm.setEcc( ON ); 
            else if (prdabrv.equalsIgnoreCase( "rls" )) searchForm.setRls( ON );
            else if (prdabrv.equalsIgnoreCase( "rlr" )) searchForm.setRlr( ON ); 
            else if (prdabrv.equalsIgnoreCase( "trsIll" )) searchForm.setTrsIll( ON ); 
            else if (prdabrv.equalsIgnoreCase( "trsPhoto" )) searchForm.setTrsPhoto( ON ); 
            else if (prdabrv.equalsIgnoreCase( "reprint")) searchForm.setRlr( ON );
            else if (prdabrv.equalsIgnoreCase( "all" )) searchForm.setAll( ON ); 
            else { 
                searchForm.setAll( ON ); 
            }  
        } 
        else { 
            searchForm.setAll( ON );
        }
        
        if (!isNull( pubname )) searchForm.setPublisher( pubname );
        
        //  We need to pass along our order parameters.
        
        request.getSession().removeAttribute( WebConstants.SessionKeys.COPIES );
        request.getSession().removeAttribute( WebConstants.SessionKeys.FIRST_PAGE );
        request.getSession().removeAttribute( WebConstants.SessionKeys.LAST_PAGE );
        request.getSession().removeAttribute( WebConstants.SessionKeys.PAGES );
        request.getSession().removeAttribute( WebConstants.SessionKeys.CHAPTER_ARTICLE );
         
        request.getSession().setAttribute( WebConstants.SessionKeys.COPIES, copies );
        request.getSession().setAttribute( WebConstants.SessionKeys.FIRST_PAGE, fpage );
        request.getSession().setAttribute( WebConstants.SessionKeys.LAST_PAGE, lpage );
        request.getSession().setAttribute( WebConstants.SessionKeys.PAGES, pages );
        request.getSession().setAttribute( WebConstants.SessionKeys.CHAPTER_ARTICLE, chapter );
        
        //  Push them (types of use) back into the session for use later.
        
        searchForm.persistTypesOfUse();

        //  Only one value should ever be specified, so this should
        //  be valid, but if more than one is specified, I'd rather
        //  grab the isbn.  If neither of specified, that's a
        //  problem.
         
        if (!isNull( stdnum )) {
            searchForm.setTitleOrStdNo( stdnum );
        }
        else if (!isNull( title )) {
            searchForm.setTitleOrStdNo( title );
        }
        else if (!isNull( wrWrkInst )) {
        	searchForm.setWrWrkInst( wrWrkInst );
        }

        //  Payment method needs to be checked and stuffed into
        //  our session for future use.
        
        UserContextService.getUserContext().setMethodOfPmtRestrictor( pmt );

        //  The following bit redirects to a protected action
        //  which prefills the login form with the u/p, auto
        //  submits it and redirects to the _destination I
        //  also provided.
        
        if (autologin)
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
                _logger.debug( "PDAction: Auto login redirect!" );
                _logger.debug( "PDAction: Path = " + autoLoginPath );
            }
            request.getSession().setAttribute( FORM_SEARCH, searchForm );
            return new ActionForward( autoLoginPath );
        }
        
        //  Go to autosearch.

        searchForm.setIsPermissionDirectAction(false);
        searchForm.setPermissionDirectProduct("NONE");

        if ( StringUtils.defaultString(searchForm.getEcc()).equalsIgnoreCase( ON ) ) { searchForm.setPermissionDirectProduct("0"); }
        if ( StringUtils.defaultString(searchForm.getAps()).equalsIgnoreCase( ON ) ) { searchForm.setPermissionDirectProduct("0"); }
        if ( StringUtils.defaultString(searchForm.getTrsIll()).equalsIgnoreCase( ON ) ) { searchForm.setPermissionDirectProduct("0"); }
        if ( StringUtils.defaultString(searchForm.getTrsPhoto()).equalsIgnoreCase( ON ) ) { searchForm.setPermissionDirectProduct("0"); }
        if ( StringUtils.defaultString(searchForm.getDps()).equalsIgnoreCase( ON ) ) { searchForm.setPermissionDirectProduct("0"); }
        if ( StringUtils.defaultString(searchForm.getRls()).equalsIgnoreCase( ON ) ) { searchForm.setPermissionDirectProduct("0"); }
        if ( StringUtils.defaultString(searchForm.getRlr()).equalsIgnoreCase( ON ) ) { searchForm.setPermissionDirectProduct("0"); }
               
        searchForm.setIsPermissionDirectAction(!searchForm.getPermissionDirectProduct().equalsIgnoreCase("NONE"));
        
        request.getSession().setAttribute( FORM_SEARCH, searchForm );
        return mapping.findForward( destination );
    }
          
    private boolean isNull( String s )
    {
        return (s == null || s.equals( EMPTY_STRING ));
    }
}
