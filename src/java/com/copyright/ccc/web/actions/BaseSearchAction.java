package com.copyright.ccc.web.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.services.PermissionCategoryEnum;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.util.LimiterComparator;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.LandingForm;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalConsumerContext;
import com.copyright.svc.searchRetrieval.api.data.SrsLimiter;

/*
    Change Log
    
    when        who         what
    ----------  ----------  --------------------------------------------------------
    2012-28-03  mjessop     Added code to test for potential ISSN, ISBN values.  
                            Added test and strip of dashes to secondary search.
                            CC-3372.
    2011-XX-07  mjessop     Stopwords, auto-suggest, etc.
    2008-13-03  mjessop     Added code to recover from a loss of context when adding
                            to an existing order after going back to select a dif-
                            erent type of use.  Also did a little cleanup.
*/

/**
 * Provides basic functionality for search action[s].
 *
 * @author Michael Jessop &lt;&gt;
 * @version $Rev: 110543 $
 */
public class BaseSearchAction extends CCAction
{
    //  Class constants.
    //  URL querystring parameters.
    
    protected static final String URL_PARM_PAGENO   = "pno";
    protected static final String URL_PARM_START    = "start";
    protected static final String URL_PARM_PAGE     = "page";
    protected static final String URL_PARM_ITEMNO   = "item";
    protected static final String URL_PARM_DETAILTYPE   = "detailType";
    protected static final String URL_PARM_PERM     = "perm";
    protected static final String URL_PARM_IDX      = "idx";
    protected static final String URL_PARM_EXISTING = "existing";
    
    /*
     * Cempro related properties
     */
    protected static final String URL_PARM_IS_CEMPRO	= "isCempro";
    protected static final String IS_NOT_CEMPRO			= "0";

    
    //  Destinations (maps to struts-config.xml forwards.)
    
    protected static final String FWD_SUCCESS        = "success";
    protected static final String FWD_SUCCESS_SIMPLE = "simplesuccess";
    protected static final String FWD_FAILURE        = "failure";
    protected static final String FWD_NO_RESULTS     = "noresults";
    protected static final String FWD_LANDING        = "landing";
    protected static final String FWD_CART           = "cartadd";
    protected static final String FWD_CART_SPECIAL   = "cartaddspecial";
    protected static final String FWD_CART_COI           = "cartaddcoi";
    protected static final String FWD_CART_SPECIAL_COI   = "cartaddspecialcoi";
    protected static final String FWD_CART_RLNK      = "cartaddrlnk";
    protected static final String FWD_ORDER          = "orderadd";
    protected static final String FWD_ORDER_SPECIAL  = "orderaddspecial";
    protected static final String FWD_ORDER_SPECIAL_COI  = "orderaddspecialcoi";
    protected static final String FWD_CART_SCRATCH   = "cartaddscratch";
    protected static final String FWD_ORDER_SCRATCH  = "orderaddscratch";
    protected static final String FWD_CART_SCRATCH_COI   = "cartaddscratchcoi";
    protected static final String FWD_ORDER_SCRATCH_COI  = "orderaddscratchcoi";
    
    protected static final String FWD_SEARCH_SIMPLE   = "simple";
    protected static final String FWD_SEARCH_ADVANCED = "advanced";
    protected static final String FWD_SEARCH_ANNUAL   = "annual";
    protected static final String FWD_SEARCH_ACADEMIC = "academic";
    
    protected static final String FWD_TOU_DETAIL     = "touDetail";
    protected static final String PERM_OPTIONS_PAGE  = "permOptionsPage";
    protected static final String PERM_OPTIONS_PAGE_PPU_ONLY = "permOptionsPagePPUOnly";

    // Permissions Direct - redirect landing
    protected static final String FWD_PERMISSIONS_DIRECT  = "permissionsdirect";

    //  Some state-related constants.
    
    protected static final String SIMPLE_SEARCH  = "0";
    protected static final String COMPLEX_SEARCH = "1";
    
    //  Response variables (Jarbo replaced the class variables).
    
    protected static final String ORIGIN = "_origin";
    protected static final String DESTINATION = "_destination";
    protected static final String PAGE = "_page";
    protected static final String ITEM = "_item";
    protected static final String DETAILTYPE = "_detailType";
    protected static final String PNO = "_pno";
    protected static final String FORM = "_form";
    protected static final String LANDING = "_landing";
    protected static final String COUNT = "_count";
    protected static final String PERM = "_perm";
    protected static final String IDX = "_idx";
    protected static final String PURCHASE_ID = "_purchaseId";
    protected static final String REFRESH = "_refresh";
    protected static final String EXISTING_ORDER = "_existingOrder";
    protected static final String RESET = "_reset";
    
    //  Class data.
    //  Updated to use new web services

    protected static final WebServiceSearch _WebSearchService;
    
    // Dropdown content.
    // Updated to use new web services
    // Package protected

    static  String[] _pubTypes;

    // Pub types that shouldn't show up as adv search limiters

    private static final String[] IGNORE_PUB_TYPES = {"article","chapter"};
    static  String[] _languages;
    static  String[] _countries;
    private static String USA = "united states of america";
    private static String ENGLISH = "english";
    static final Logger _logger = Logger.getLogger(BaseSearchAction.class );

    //  Used in confirming mapping operations.

    protected static final Vector<String> _search_forwards;
    protected static final Vector<String> _simple_forwards;
    protected static final Vector<String> _result_forwards;
    
    //  Some things are better private.
    
    private static final HashMap<String,Integer> _pmap;
    
    //  Constructor...
    static {
        _search_forwards = new Vector<String>();
        _search_forwards.add( 0, FWD_SEARCH_SIMPLE );
        _search_forwards.add( 1, FWD_SEARCH_ADVANCED );
        _simple_forwards = new Vector<String>();
        _simple_forwards.add( 0, FWD_SEARCH_SIMPLE );
        
        _result_forwards = new Vector<String>();
        _result_forwards.add( 0, FWD_SUCCESS );
        _result_forwards.add( 1, FWD_SUCCESS_SIMPLE );
        _result_forwards.add( 2, FWD_NO_RESULTS );
        
        _pmap = new HashMap<String,Integer>();
        _pmap.put( new UsageDescriptor( UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY ).getDescription(), UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY );
        _pmap.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY ).getDescription(), UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY );
        _pmap.put( new UsageDescriptor( UsageDescriptor.ACADEMIC_TRX_SCAN ).getDescription(), UsageDescriptor.ACADEMIC_TRX_SCAN ) ;
        _pmap.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_EMAIL ).getDescription(), UsageDescriptor.NON_ACADEMIC_TRX_EMAIL );
        _pmap.put( new UsageDescriptor( UsageDescriptor.NON_ACADEMIC_TRX_REPUB_EMAIL ).getDescription(), UsageDescriptor.NON_ACADEMIC_TRX_REPUB_EMAIL );
        
        _WebSearchService = new WebServiceSearch();    
    }
    
    static{
    	if (_pubTypes == null) {
    		List<SrsLimiter> limiters = null;
    		int nn = 0;


    		//  Load a limiter comparator.  We use this to
    		//  sort our pub types, and any other type of
    		//  limiter if necessary.
    		LimiterComparator countryComparator = new LimiterComparator();
    		countryComparator.setValueThatRanksFirst(USA);

    		//  Get our list of countries.

    		limiters = ServiceLocator.getSearchRetrievalService().itemsForField(new SearchRetrievalConsumerContext(), "country_facet");

    		Collections.sort(limiters, countryComparator);

    		_countries = new String[limiters.size() + 1];
    		_countries[nn++] = "all";

    		for (SrsLimiter limiter : limiters) {
    				_countries[nn++] = limiter.getItem();
    		}

    		//  Get our list of languages.
    		LimiterComparator langComparator = new LimiterComparator();
    		langComparator.setValueThatRanksFirst(ENGLISH);

    		limiters = ServiceLocator.getSearchRetrievalService().itemsForField(new SearchRetrievalConsumerContext(),  "lang_facet");

    		Collections.sort(limiters, langComparator);

    		nn = 0;
    		_languages = new String[limiters.size() + 1];
    		_languages[nn++] = "all";

    		for (SrsLimiter limiter : limiters) {
    				_languages[nn++] = limiter.getItem();
    		}

    		//  Get our list of publication types.

    		limiters =  ServiceLocator.getSearchRetrievalService().itemsForField(new SearchRetrievalConsumerContext(),  "pub_type_facet");

    		LimiterComparator pubTypeComparator = new LimiterComparator();
    	
    		Collections.sort(limiters, pubTypeComparator);

    		List<String> pTypes = new ArrayList<String>();
    		pTypes.add("all");
            String [] dupCheckArray = new String [limiters.size()];
            dupCheckArray[0]="all";
            int index=1;
    		for (SrsLimiter limiter : limiters) {
    			//eliminate a pre-determined set of options from pub type list
    			if (! ArrayUtils.contains(IGNORE_PUB_TYPES, limiter.getItem().toLowerCase()) &&
    					!ArrayUtils.contains (dupCheckArray, limiter.getItem().toLowerCase())) {
        			pTypes.add(limiter.getItem());
        			dupCheckArray[index++]=limiter.getItem().toLowerCase();
    			}
    		}
    		_pubTypes=pTypes.toArray(new String[] {});
    	}
    }

    public BaseSearchAction()
    {
        super();  
    }
    
    //  defaultOperation is called when no operation was
    //  specified for this class to perform.
    
    public ActionForward defaultOperation( ActionMapping       mapping
                                         , ActionForm          form
                                         , HttpServletRequest  request
                                         , HttpServletResponse response )
    {
        return show(mapping, form, request, response);
    }
    
    //  Shared operations.
    
    public ActionForward show( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {    
        String destination;
        
        if (_logger.isDebugEnabled())
        {
            _logger.debug("**** BaseSearchAction.show() ****");
        }
        
        //  Grab our form.
        LandingForm lform = null;
        
        // pull the searchForm from session
        SearchForm sform = getSearchForm(request);
        
        // if we didn't find one in session, use the one passed to this method
        if (sform == null) sform = castForm( SearchForm.class, form );
        
        String saveLastSearch = sform.getLastSearch();
        
        //update the form with the selected tpus from the user record
        sform.synchronizeTypesOfUse();
        
        String page = request.getParameter("page");
        
        // if the user specified specific tou permission categories either
        // by providing specific tou's or a page parameter in the url
        // we update the SearchForm with these so that the advanced search page will get painted
        // with appropriate categories checked.
		String permsSelected = request.getParameter("permSel"); //user provided specific tou's in url
		String[] permsSelectedArr = null;
		if (permsSelected!=null && permsSelected.length()>0) {
			sform.unsetAll();
			permsSelectedArr = permsSelected.split(",");
		} else if(page!=null && page.length()>0) { //user provided a page parameter in the url
			if (page.equalsIgnoreCase("annual")) {
				permsSelectedArr=new String[] {PermissionCategoryEnum.AAS_CATAGORYID.getCategoryCode(), 
											   PermissionCategoryEnum.DRA_CATAGORYID.getCategoryCode()};
			} else if (page.equalsIgnoreCase("academic")) {
				permsSelectedArr=new String[] {PermissionCategoryEnum.ARS_CATAGORYID.getCategoryCode()};				
			} else if (page.equalsIgnoreCase("ppu")) {
				permsSelectedArr=new String[] {PermissionCategoryEnum.APS_CATAGORYID.getCategoryCode(),
												PermissionCategoryEnum.DPS_CATAGORYID.getCategoryCode(),
												PermissionCategoryEnum.ECC_CATAGORYID.getCategoryCode(),
												PermissionCategoryEnum.RLR_CATAGORYID.getCategoryCode(),
												PermissionCategoryEnum.RLS_CATAGORYID.getCategoryCode(),
												PermissionCategoryEnum.TRSILL_CATAGORYID.getCategoryCode(),
												PermissionCategoryEnum.TRSPHOTO_CATAGORYID.getCategoryCode()};								
			}
		}

		if (permsSelectedArr!=null) {
			sform.unsetAll();
			for(String categoryCode: permsSelectedArr) {
				PermissionCategoryEnum category = PermissionCategoryEnum.valueOfCategoryCode(categoryCode);
				if (category!=null) {
					sform.setPermissionTypeDisplay(category.getSearchPermissionTypeEnum(), "on");
				}
			}
		}

        sform.setSelectedPubYear("");

        //  We need to see if our form is brand new and needs to
        //  have its dropdown box data loaded.

        if (sform.needsDropdownData()) {
            //  Fill in our list of countries, languages
            //  and publication types.

            sform.setCountries(_countries);
            sform.setPublicationTypes(_pubTypes);
            sform.setLanguages(_languages);
        }

        //  The show operation has two parameters:  page and start.
        //  It expects the page parameter, the start parameter is
        //  optional.
        //
        //  eg. search?operation=show&page=simple&start=1

        /*
         * In an effort to remove the instance level variables from this action,
         * the analyzeParameters method has been changed to write its variables into 
         * the request objects attribute. All reads of this variables will be 
         * changed to read from the request object as well. [jra]
         */
         
        destination = analyzeParameters( request );
        sform.setOrderExists(((Boolean) request.getAttribute("_existingOrder")).booleanValue());
        sform.setPurchaseId( ((Long) request.getAttribute("_purchaseId")).longValue());

        /* get the query string parameter isCempro and set it
         * in session so that other pages have access to
         * the value
         */
		String isCempro = (String) request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO);
        if (StringUtils.isEmpty(isCempro)){
	        if (request.getParameter(URL_PARM_IS_CEMPRO) == null){
	        	isCempro = IS_NOT_CEMPRO;
	        } else {
	        	isCempro = request.getParameter(URL_PARM_IS_CEMPRO);
	        }
	        
        	String[] countryArray = new String []{"all"};
            sform.setSelectedCountries(countryArray);
			request.getSession().setAttribute(WebConstants.SessionKeys.IS_CEMPRO, isCempro);

			if(isCempro.equals("1")){
	        	request.getSession().setAttribute( WebConstants.SessionKeys.UTM_SOURCE, "Cempro" );
			}
        }
        
        if (destination == null) {
            if (sform.getLastSearch() != null && !sform.getLastSearch().equals(""))
                destination = sform.getLastSearch();
            else
                destination = SearchForm.ADVANCED_SEARCH;
        }            
            
        if (destination.equals( "last" ))
        {
            //  The user wants to go back to the last valid
            //  page in this process.  First, synchronize
            //  with the user context.  If no context existed
            //  it defaults to simple search.
            
            if (saveLastSearch == null) saveLastSearch = sform.getLastSearch();
            destination = saveLastSearch;
            sform.setSearchFlags(saveLastSearch);
            sform.persistTypesOfUse();
        }
        
        if (destination.equals( "back" ))
        {
            //  The user wants to go back to the last valid
            //  page in this process.
            
            if (saveLastSearch == null) saveLastSearch = sform.getLastSearch();
            destination = sform.getLastPage();
            sform.setSearchFlags(saveLastSearch);
            sform.persistTypesOfUse();
        }
        
        if (_search_forwards.contains( destination ))
        {
            //  We have a request for some form of search page.
            //  It is possible the user wants a clean slate, here, 
            //  so we check the reset value set in analyzeParameters.
            
            if (!destination.equals( sform.getLastSearch() ))
            {
                sform.setLastSearch( destination );
            }
            
           // if (_reset)
           if (((Boolean) request.getAttribute("_reset")).booleanValue() ==  true)
            {
                sform = new SearchForm();
                lform = new LandingForm();
                sform.setLastSearch( destination );
                sform.setLastPage( destination );
                sform.setSearchFlags( destination );
                
                sform.setCountries(_countries);
                sform.setPublicationTypes(_pubTypes);
                sform.setLanguages(_languages);
                
                if (_simple_forwards.contains( destination ))
                    sform.setSearchType( SIMPLE_SEARCH );
                else
                    sform.setSearchType( COMPLEX_SEARCH );
            } 
        }
        if (_logger.isDebugEnabled())
        {
            _logger.debug("destination: " + destination);
        }
        putForms( request, sform, lform );
        return mapping.findForward( destination );
    }
    
    //  Methods to get and put our search-related forms into the session.
    
    
    protected void putForms(HttpServletRequest r, SearchForm searchForm, LandingForm landingForm)
    {
        r.getSession().setAttribute( WebConstants.SessionKeys.SEARCH_FORM, searchForm );
        
        if (landingForm != null)
            r.getSession().setAttribute( WebConstants.SessionKeys.LANDING_FORM, landingForm );
    }

    /**
     * Returns the searchForm from the http session. Can return null if no searchForm
     * is found in the session.
     * 
     * @param r the servlet request object
     * @return the SearchForm from session if present, null otherwise.
     */
    protected SearchForm getSearchForm(HttpServletRequest r) {
        return (SearchForm) r.getSession().getAttribute( WebConstants.SessionKeys.SEARCH_FORM );
    }

    protected LandingForm getLandingForm(HttpServletRequest r)
    {
        return (LandingForm) r.getSession().getAttribute( WebConstants.SessionKeys.LANDING_FORM );
    }
    
    protected String analyzeParameters(HttpServletRequest r)
    {
        /*
         * In an effort to remove the instance level variables from this action,
         * this method has been changed to write its variables into 
         * the request objects attribute. All reads of these variables will be 
         * changed to read from the request object as well. [jra]
         */
        String destination = null;
        String startParameter = WebUtils.escapeBrackets(r.getParameter( URL_PARM_START ));
        String page = WebUtils.escapeBrackets(r.getParameter( URL_PARM_PAGE ));
        String perm = WebUtils.escapeBrackets(r.getParameter( URL_PARM_PERM ));
        String index = WebUtils.escapeBrackets(r.getParameter( URL_PARM_IDX ));
        String existing = WebUtils.escapeBrackets(r.getParameter( URL_PARM_EXISTING ));
        String purchaseId = WebUtils.escapeBrackets(r.getParameter( WebConstants.RequestKeys.PURCHASE_ID ));
        String pno = WebUtils.escapeBrackets(r.getParameter( URL_PARM_PAGENO ));
        String item = WebUtils.escapeBrackets(r.getParameter( URL_PARM_ITEMNO ));
        String detailType = WebUtils.escapeBrackets(r.getParameter( URL_PARM_DETAILTYPE ));
        
        /*
         * initialize all request level variables so we don't end up with
         * nullPointerExceptions. Since these were formerly instance variables
         * they would have automatically init'd.
         */
        r.setAttribute( RESET, Boolean.FALSE );
        r.setAttribute( IDX, Integer.valueOf( "0" ) );
        r.setAttribute( PURCHASE_ID, Long.valueOf( "0" ) );
        r.setAttribute( EXISTING_ORDER, Boolean.FALSE );
        r.setAttribute( PERM, perm);

    	if ((item==null||item.equals("")) && r.getAttribute("item")!=null) {
    		item = (String) r.getAttribute("item");
    	}
    	

        if (isNull( pno )) pno = "1";
        if (isNull( item )) {
        	item = "0";
        }
        r.setAttribute( ITEM, item );
        r.setAttribute( PNO, pno );
        r.setAttribute(DETAILTYPE, detailType);
        
        if (notNull( startParameter )) {
            startParameter = startParameter.toLowerCase();
            
            if (startParameter.equals( "1" ) ||
                startParameter.equals( "yes" ) ||
                startParameter.equals( "true" )) {
                r.setAttribute( RESET, Boolean.TRUE );
            }
            else {
                r.setAttribute( RESET, Boolean.FALSE );
            }
        }
        if (notNull( page )) {
            page = page.toLowerCase();
            if ("ppu".equals(page) || "annual".equals(page) || "academic".equals(page))
            {
                //  2010-02-02  MSJ
                //  Quickie fix to protect against old links that people
                //  have bookmarked.  This should be removed at some future
                //  date, after users have been forewarned that links are
                //  changing.
                
                page = FWD_SEARCH_ADVANCED;
            }
            destination = page;
        }
        if (notNull( index )) {
            r.setAttribute( IDX, Integer.valueOf( index ) );
        }
        else {
            r.setAttribute( IDX, Integer.valueOf( "0" ) );
        }
        if (notNull( purchaseId )) {
            r.setAttribute( PURCHASE_ID, Long.valueOf( purchaseId ) );
        } 
        else {
            r.setAttribute( PURCHASE_ID, Long.valueOf( "0" ) );
        }

        boolean tmpExistingOrder = (notNull( existing ) || notNull(purchaseId));
        r.setAttribute( EXISTING_ORDER, Boolean.valueOf( tmpExistingOrder ) );
        
        if (tmpExistingOrder) {
            Long tmp = (Long) r.getAttribute( PURCHASE_ID );
            r.getSession().setAttribute( WebConstants.SessionKeys.PARM_PURCHASE_ID, tmp );
            r.getSession().setAttribute( WebConstants.SessionKeys.PARM_EXISTING_ORDER, Boolean.TRUE );
        }
        
        return destination;
    }
    
    //  Helper method to grab our result window and stuff
    //  it into our search form.
    
    protected void getResults(SearchForm searchForm)
    {
        int pageNo = Integer.parseInt( searchForm.getPageNo() );
        int pageSize = Integer.parseInt( searchForm.getPageSize() );
        
        if (_logger.isDebugEnabled()) {
            _logger.info("\n\nGET RESULTS PARMS: num=" 
                + searchForm.getPageNo() + ", siz=" 
                + searchForm.getPageSize() + "\n\n");
        }
        searchForm.setResults( 
            _WebSearchService.getResultsWindow( pageNo, pageSize )
        );
    }
    
    //  Perform the actual search.  Simple or ... not simple.
    
    protected int doSearch(SearchForm searchForm)
    {
        String searchType = searchForm.getSearchType();
        searchForm.setPageNo("1");
        int count = 0;
        
        if (searchType.equals( SIMPLE_SEARCH ))
        {
                 //count = _WebSearchService.performBasicSearch( searchForm.getTitleOrStdNo() );
                 CCSearchCriteria criteria = searchForm.getSearchCriteria();
                 count = _WebSearchService.performBasicSearch( criteria, new Integer(searchForm.getPageSize()) );
        }
        else
        {
            if (_logger.isDebugEnabled()) {
                _logger.info("\nSearch Criteria in SearchForm: \n");
                _logger.info("\n" + searchForm.getSearchCriteria().toString());
            }
            CCSearchCriteria criteria = searchForm.getSearchCriteria();
            count = _WebSearchService.performAdvancedSearch( criteria, new Integer(searchForm.getPageSize()) );
            searchForm.setPageNo("1");
            
            if (_logger.isDebugEnabled()) {
                _logger.info("\nSearch yielded result count of: " + Integer.toString(count) + "\n");
            }
        }
        return count;
    }
    
    protected int doSecondarySearch(SearchForm searchForm, String altTitle, Boolean basic)
    {
        int count = 0;
        int displayPageSize = 0;

        if (isNumericPlusX( altTitle )) {
            //  If we have what appears to be an ISSN, ISBN or WRK_INST
            //  we want to strip it of dashes.  People seem to enter dashes
            //  in all the wrong places.  In case you were wondering.

            altTitle = altTitle.replaceAll("-", "");
        }
        
        if (searchForm.getPageSize() != null && !searchForm.getPageSize().equals("")) {
            displayPageSize = (new Integer(searchForm.getPageSize())).intValue();
            displayPageSize = displayPageSize > 0 ? displayPageSize : 25;
        }
        
        CCSearchCriteria criteria = searchForm.getSearchCriteria();
        
        count = _WebSearchService.performSecondarySearch( 
            criteria, displayPageSize, altTitle, basic
        );
        searchForm.setPageNo( String.valueOf(1) );
        
        return count;
    }
    
    protected int doComplexSearch(SearchForm searchForm)
    {
        //  Force matters.
        
        String tmp = searchForm.getSearchType();
        int count;
        
        searchForm.setSearchType( COMPLEX_SEARCH );
        count = doSearch(searchForm);
        searchForm.setSearchType( tmp );
        
        return count;
    }
    
    protected List<String> getSuggestions(SearchForm searchForm)
    {
        int itemCount;
        int weight;
        
        //  Pull back a list of strings that contain titles from
        //  a fuzzy search.
        
        CCSearchCriteria searchCriteria = searchForm.getSearchCriteria();
        
        try
        {
            itemCount = CC2Configuration.getInstance().getSearchInsteadItems();
            weight = CC2Configuration.getInstance().getSearchInsteadCount();
        }
        catch (Exception e)
        {
            itemCount = 10;
            weight = 3;
        }
        
        //  The first parameter is self explanatory.  The second and third
        //  represent (in order) the number of results we want back, and
        //  the number of times a search must have yielded valid results.
        
        return _WebSearchService.getSuggestions(searchCriteria, itemCount, weight);
    }
    
    //  If this is called, we are on our way to displaying some
    //  search results.  Make sure the appropriate forward is
    //  set, update our state variables and stuff the form back
    //  into our session.
    
    protected void updateFormValues(HttpServletRequest request, SearchForm searchForm, int count)
    { 
        searchForm.setCount( Integer.valueOf( count ).toString() );
        //searchForm.setLastPage( _destination );
    }
    
    //  Null string tests.
    
    protected boolean isNull(String s)
    {
        return ((s == null) || s.equals(""));
    }
    
    protected boolean notNull(String s)
    {
        return ((s != null) && !s.equals(""));
    }
    
    protected void addError(HttpServletRequest r, String s)
    {
        ActionErrors errors = new ActionErrors();

        errors.add(
            ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage(s)
        );
        r.setAttribute(Globals.ERROR_KEY, errors);
    }
    
    //  Little ditty I ripped from Lech to apply stopwords
    //  to our title field.
    
    protected String stripStopwords(String title)
    {
        String titleAfterChange = "";
        String[] stopwords = CC2Configuration.getInstance().getManualStopwords();
		
		String[] tokens = title.trim().split(" ");
		boolean found = false;
		for(String token : tokens)
		{
		    found = false;
			for(String stopword : stopwords)
			{
				if(token.equalsIgnoreCase(stopword))
				{
					found = true;
					break;
				}
			}
			if(!found)
			{
				titleAfterChange += token + " ";
			}
		}
		return titleAfterChange.trim();
    }

    //  Do we have a potential ISBN?  An ISBN consists
    //  of digits, hyphens and the letter 'X', but only
    //  as the last digit.  Hyphens do not count toward
    //  length in an ISBN.  ISBNs can be 9, 10 or 13 digits
    //  in length.  The only problem is that hyphens are
    //  frequently misplaced or incomplete causing a
    //  malformed ISBN.  We use this to check before we
    //  strip hyphens in a search.  We check for a length
    //  of 10 or greater because if the length is 9 and
    //  there is a hyphen, we have an ISSN... if it is 9
    //  and an ISBN, there could be no hyphen.

    protected boolean mightBeISBN(String s)
    {
        //  ^[xX0-9\-]{10,}$
        //  At least 10 characters long, consisting
        //  of digits 0123456789, 'x', 'X' or '-'.

        boolean isMatchSoFar = s.matches("[xX0-9\\-]{10,}");

        //  An ISBN (or ISSN for that matter) will only
        //  have an X as the LAST digit, if there is one
        //  at all.

        if (isMatchSoFar && (
            s.toLowerCase().indexOf('x') == -1 ||
            s.toLowerCase().indexOf('x') == (s.length() - 1)))
        {
            return true;
        }

        return false;
    }

    //  Slightly modified version of the above, accepts value of
    //  8 or greated to cover ISSNs and WRK_INSTs.  We are using
    //  the code in this, and not the above routine after much
    //  discussion, but for now I'd like to leave the aforementioned
    //  code in place.

    protected boolean isNumericPlusX(String s)
    {
         //  ^[xX0-9\-]{8,}$
        //  At least 10 characters long, consisting
        //  of digits 0123456789, 'x', 'X' or '-'.

        boolean isMatchSoFar = s.matches("[xX0-9\\-]{8,}");

        //  An ISBN (or ISSN for that matter) will only
        //  have an X as the LAST digit, if there is one
        //  at all.

        if (isMatchSoFar && (
            s.toLowerCase().indexOf('x') == -1 ||
            s.toLowerCase().indexOf('x') == (s.length() - 1)))
        {
            return true;
        }

        return false;       
    }
}