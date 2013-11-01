package com.copyright.ccc.web.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.articlesearch.ArticleSearchResultsPaging;
import com.copyright.ccc.business.services.articlesearch.ArticleSearchState;
import com.copyright.ccc.business.services.rlnk.RlnkRightsServices;
import com.copyright.ccc.business.services.search.SearchState;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.ArticleSearchActionForm;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.util.ArticleDisplay;
import com.copyright.ccc.web.util.RightsLnkQPriceUtil;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;
import com.copyright.svc.searchRetrieval.api.data.ArticleSearch;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalResult;

public class ArticleSearchAction extends CCAction {
    
    private static final String ARTICLE_SEARCH_RESULTS = "articleSearchResults";
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String RLQUICKPRICE = "rlQuickPrice";
    protected static final Logger _logger = Logger.getLogger( ArticleSearchAction.class );
    
    

    protected static final String PAGESELECTED = "pageSelected";
    
    protected static final String ARTICLEINDEX = "articleIndex";
    
    protected static final int MAXRESULTSREQUEST=100;
    private static final boolean PREFETCHING_ENABLED = CC2Configuration.getInstance().isRightslinkLinkPrefetchEnabled();
    
    
	public ActionForward articleSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
	{
		ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
		/*
		 * if we don't have a selectedPublication then the session likely expired,
		 * redirect back to the permission summary page
		 */
		if (asaForm.getSelectedPublication()==null) {
			return CCAction.sessionDataNotFound(mapping,request,String.valueOf(asaForm.getSelectedPublicationWrkInst()));
		}
		
		if (asaForm.getArticleAuthor().isEmpty() &&
			asaForm.getArticleIDno().isEmpty() &&
			asaForm.getArticleTitle().isEmpty() &&
			asaForm.getItemVolume().isEmpty() &&
			asaForm.getItemIssue().isEmpty() &&
			asaForm.getItemStartPage().isEmpty() &&
			asaForm.getArticleStartMonth().isEmpty() &&
			asaForm.getArticleStartYear().isEmpty() &&
			asaForm.getArticleEndMonth().isEmpty() &&
			asaForm.getArticleEndYear().isEmpty())
		{
			addError(request,"Article Search failed.  Reason:  Search form was null.");
			return mapping.findForward("failure");
		}
		/*
		 * validate for numeric values only. Ensures cross-site scripting strings
		 * are ignored. Jira item CC-1670
		 */
		if (!StringUtils.isEmpty(asaForm.getArticleStartMonth()) &&
				!StringUtils.isNumeric(asaForm.getArticleStartMonth())) {
			throw new IllegalArgumentException("publication date start month must be numeric");
		}		
		if (!StringUtils.isEmpty(asaForm.getArticleEndMonth()) &&
				!StringUtils.isNumeric(asaForm.getArticleEndMonth())) {
			throw new IllegalArgumentException("publication date end month must be numeric");
		}			
		
		ArticleSearch articleSearch = new ArticleSearch();

		articleSearch.setArtTitle(asaForm.getArticleTitle());
		articleSearch.setAuthor(asaForm.getArticleAuthor());
		articleSearch.setArtIdno(asaForm.getArticleIDno());
		articleSearch.setItemVolume(asaForm.getItemVolume());
		articleSearch.setItemIssue(asaForm.getItemIssue());
		articleSearch.setItemStartPage(asaForm.getItemStartPage());
		
		//put together date from separate fields
		String startMonth="00";
		String startDay="00";
		String endMonth="12";
		String endDay="31";
		StringBuffer dateRange = new StringBuffer();
		
		// Start year not Empty  OR End Year not Empty
		if((!asaForm.getArticleStartYear().isEmpty() &&
				 WebUtils.isAllDigit(asaForm.getArticleStartYear()))  ||  (!asaForm.getArticleEndYear().isEmpty() &&
				 WebUtils.isAllDigit(asaForm.getArticleEndYear()))) {
			
			
			// if start year Not Empty
			if(!asaForm.getArticleStartYear().isEmpty() &&
			 WebUtils.isAllDigit(asaForm.getArticleStartYear())){
					if (!asaForm.getArticleStartMonth().isEmpty()&& Integer.parseInt(asaForm.getArticleStartMonth())>0 ){
						if (Integer.parseInt(asaForm.getArticleStartMonth())<10){
							startMonth="0"+ asaForm.getArticleStartMonth();
						} else {
							startMonth=asaForm.getArticleStartMonth();
						}
					}
					dateRange.append(asaForm.getArticleStartYear());
					dateRange.append(startMonth);
					dateRange.append(startDay);
					
				
			}else{
				dateRange.append(" * TO ");
			}
			
			// add TO condition if both are not empty
			if((!asaForm.getArticleStartYear().isEmpty() &&
					 WebUtils.isAllDigit(asaForm.getArticleStartYear()))  &&  (!asaForm.getArticleEndYear().isEmpty() &&
					 WebUtils.isAllDigit(asaForm.getArticleEndYear()))) {
				dateRange.append(" TO ");
			}
		
			// IF end year not empty
			if(!asaForm.getArticleEndYear().isEmpty() &&
					 WebUtils.isAllDigit(asaForm.getArticleEndYear())){
				if (!asaForm.getArticleEndMonth().isEmpty()&& Integer.parseInt(asaForm.getArticleEndMonth())>0 ){
					if (Integer.parseInt(asaForm.getArticleEndMonth())<10){
						endMonth="0"+ asaForm.getArticleEndMonth();
					} else {
						endMonth=asaForm.getArticleEndMonth();
					}
				}
				dateRange.append(asaForm.getArticleEndYear());
				dateRange.append(endMonth);
				dateRange.append(endDay);
				
			}else{
				dateRange.append(" TO * ");
			}
				
			_logger.info("Article Search Date Range :" + dateRange.toString());
			
			articleSearch.setDate(dateRange.toString());
		}

		List <String> sortList = new ArrayList<String>();
		int sortOption = 0;
		if (asaForm.getSort() != null && !asaForm.getSort().isEmpty()) {
			sortOption = Integer.parseInt(asaForm.getSort());
		}
        switch (sortOption) {
          case 0:
			sortList.add(PublicationSearch.SORT_RELEVANCE);
			break;
          case 1:
            sortList.add(PublicationSearch.SORT_TITLE);
            break;
          case 2:
        	sortList.add(PublicationSearch.SORT_DATE);
        }
        
        //new search set back to page 1
        int startPage=1;
        asaForm.setPageNo(Integer.toString(1));
        

        //get the search fields and perform the search
        // commented out the line below to fix CC-1825 - this limit is preventing
        // works other than articles (like Chapter or Other) to be found
        //articleSearch.setPubType("article");

     
        //clear cached search results first so only the latest results show
        WorkExternal selectedWork = ((WorkRightsAdapter)asaForm.getSelectedPublication()).getWork();
    
        SearchRetrievalResult result = ArticleSearchResultsPaging.search(articleSearch, selectedWork,sortList, startPage);
        asaForm.setArticles(ArticleSearchResultsPaging.copyPage(0, Integer.parseInt(asaForm.getPageSize())));
        if (PREFETCHING_ENABLED) {
         	//pre-fetch links for the works about to be displayed
            List<ArticleDisplay> articles = asaForm.getArticles();
         	Long selectedTouId = UserContextService.getSearchState().getSelectedTouId();
         	List<WorkExternal> displayableSubWorks = new ArrayList<WorkExternal>();
         	for(ArticleDisplay article: articles) {
         		displayableSubWorks.add(article.getWork());
         	}
    		Integer selectedPubYear=CCAction.getSelectedPubYear(request);
        	RlnkRightsServices.prefetchLinks(selectedTouId, selectedWork, displayableSubWorks, selectedPubYear);
        }
        asaForm.setCount(Integer.toString(result.getResultCount()));
        asaForm.clearPageRange();
        //calculate the number of pages based on the selected page size and number of results
        asaForm.calculatePageRange();
        return mapping.findForward(SUCCESS);
	}
	/**
	 * 
	 * @param form
	 */
	public void setRightInstinUserContext( ActionForm          form)
	{
		ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
		asaForm.setSelectedTou(asaForm.getSelectedRRTou());
		UserContextService.getSearchState().setSelectedTou(asaForm.getSelectedRRTou());
		UserContextService.getSearchState().setSelectedTouId(asaForm.getSelectedRightInst());
		UserContextService.getSearchState().setSelectedRrTouId(Long.parseLong(asaForm.getSelectedRRTouId()));
		UserContextService.getSearchState().setSelectedCategoryId(asaForm.getSelectedCategoryId());
		UserContextService.getSearchState().setSelectedOfferChannel(asaForm.getSelectedOfferChannel());
	}
    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward initSearchParms(ActionMapping       mapping
                               , ActionForm          form
                               , HttpServletRequest  request
                               , HttpServletResponse response )
    {
		ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
		asaForm.clearState();
		if (!StringUtils.isNumeric(request.getParameter(WebConstants.RequestKeys.ITEM))) {
			throw new IllegalArgumentException("parameter 'item' must be numeric");
		}
		if (!StringUtils.isNumeric(request.getParameter(WebConstants.RequestKeys.IDX))) {
			throw new IllegalArgumentException("parameter 'idx' must be numeric");			
		}
		if (!StringUtils.isNumeric(request.getParameter("selectedRRTouId"))) {
			throw new IllegalArgumentException("parameter 'selectedRRTouId' must be numeric");			
		}

		long item = new Long((String) request.getParameter( WebConstants.RequestKeys.ITEM )).longValue();

		/*
		 * if we don't have the SearchState in session then the session probably expired.
		 * Redirect back to permission summary
		 */
		if (UserContextService.getSearchState()==null) {
			return CCAction.sessionDataNotFound(mapping,request,String.valueOf(item));
		}

		//save the selected permission type and id of the selected work
		setRightInstinUserContext(form);

        request.getSession().setAttribute(WebConstants.RequestKeys.ITEM, item);
        Publication publication = RightsLnkQPriceUtil.getSelectedPublication(item);
        asaForm.setSelectedPublication(publication);
        //get the selected tou which was put in cache by an AJAX action when the user selected the radio
        //button tou on the Permission Summary Page
        asaForm.setSelectedTou(UserContextService.getSearchState().getSelectedTou());

        /*
         * added by jra to change the way we carry around the PublicationPermission that was
         * chosen by the user
         */
        /*
         * get the index number of the category the user clicked on
         */
        SearchForm searchForm = (SearchForm) request.getSession().getAttribute( WebConstants.SessionKeys.SEARCH_FORM );
        
        //added for pub to pub (clear out values if we have done a title level search in the past it will still be in the session objects)
        searchForm.setSelectedOfferChannel("");
        searchForm.setSelectedRlPermissionType("");
        searchForm.setSelectedRlPubCode("");
        
        
        String selectedCategoryIndex = request.getParameter(WebConstants.RequestKeys.IDX);
        PermissionSummaryCategory selectedCategory = null;
        if (selectedCategoryIndex != null) {
        	searchForm.setSelectedPermCatDisplayIndex(Integer.valueOf(selectedCategoryIndex));
        	selectedCategory = searchForm.getSelectedPermCatDisplay().getPermissionSummaryCategory();
        } else {
        	selectedCategory = null;
        }
        
        if (selectedCategory==null) {
        	throw new CCCRuntimeException("cannot determine which PermissionSummaryCategory was chosen by the user");
        }
        
        SearchState searchState = UserContextService.getSearchState();  
        
        //added for pub to pub (clear out values if we have done a title level search in the past it will still be in the session objects) 
        searchState.setSelectedRlPermissionType(searchForm.getSelectedRlPermissionType());
        //searchState.setSelectedOfferChannel(searchForm.getSelectedOfferChannel());
        searchState.setSelectedRlPubCode(searchForm.getSelectedRlPubCode());
        
        
    	if (searchState != null) {
            searchState.setSelectedPermissionType(searchForm.getSelectedPermCatDisplay().getCategoryDescription());
     		asaForm.setSelectedCategory(searchForm.getSelectedPermCatDisplay().getCategoryDescription());
     		asaForm.setBiactive(searchForm.getIsBiactive());
     		if (!searchForm.getSelectedPubYear().isEmpty()){
     			asaForm.setArticleStartMonth("1");
     			asaForm.setArticleStartYear(searchForm.getSelectedPubYear());
     			asaForm.setArticleEndMonth("12");
     			asaForm.setArticleEndYear(searchForm.getSelectedPubYear());
     		}
    	} else {
    		asaForm.setSelectedCategory("");
    	}
        
        request.getSession().setAttribute(WebConstants.RequestKeys.PERM, asaForm.getSelectedCategory());
        return mapping.findForward( ARTICLE_SEARCH_RESULTS);
    }
	
	public ActionForward clearParameters(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response ){
		ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
		/*
		 * if we don't have a selectedPublication then the session likely expired,
		 * redirect back to the permission summary page
		 */
		if (asaForm.getSelectedPublication()==null) {
			return CCAction.sessionDataNotFound(mapping,request,String.valueOf(request.getParameter(WebConstants.RequestKeys.ITEM)));
		}
		asaForm.clearSearchParameters();
		return mapping.findForward( ARTICLE_SEARCH_RESULTS);
		
	}
	
    //******************************************************************
    //  The following two methods are for the "more" and "less" links
    //  on the search results page.
    
    public ActionForward next( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
        ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
        asaForm.incrPageNo();
        
        if (Integer.parseInt(asaForm.getCount()) > 0)
        {
            getResults(asaForm);
        }
        
        return mapping.findForward( SUCCESS );
    }
    
    public ActionForward prev( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
    	ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
        asaForm.decrPageNo();
        
        if (Integer.parseInt(asaForm.getCount()) > 0)
        {
            getResults(asaForm);
        }
        
        return mapping.findForward(SUCCESS );
    }
	
    //*******************************************************************
    //  The following two methods have to do with the sort and page
    //  size.  The action delivers us back to the same page we were on.
    
    public ActionForward sort( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
        ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
        
        if (asaForm != null)
        {
        	return articleSearch(mapping,asaForm,request,response);
        }
        else
        {
        	return mapping.findForward( FAILURE);
        }

    }
    
    //*******************************************************************
    //  How many items to show per page.
    
    public ActionForward perPage( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
        ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
        
        WorkExternal work = ((WorkRightsAdapter)asaForm.getSelectedPublication()).getWork();
    	asaForm.setArticles(ArticleSearchResultsPaging.getResultsWindow(work,1, Integer.parseInt(asaForm.getPageSize())));

    	asaForm.clearPageRange();
    	asaForm.calculatePageRange();
    	asaForm.setPageNo("1");
        return mapping.findForward( SUCCESS );
    }
    
    public ActionForward toPage( ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response )
    {
    	ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
    	//get next page from saved search results
    	String pageNo = request.getParameter( WebConstants.RequestKeys.PNO );
    	int pageNum = Integer.parseInt(pageNo);
    	asaForm.setPageNo(pageNo);
    	WorkExternal work = ((WorkRightsAdapter)asaForm.getSelectedPublication()).getWork();
    	asaForm.setArticles(ArticleSearchResultsPaging.getResultsWindow(work,pageNum, Integer.parseInt(asaForm.getPageSize())));
    	
    	return mapping.findForward(SUCCESS);
    }
    
    public ActionForward continuePurchase(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response ) {
    	ArticleSearchActionForm asaForm = castForm( ArticleSearchActionForm.class, form );
    	String indexNo = request.getParameter( ARTICLEINDEX );
		if (!StringUtils.isNumeric(indexNo)) {
			throw new IllegalArgumentException("parameter '" + ARTICLEINDEX + "' must be numeric");
		}
    	int selectedIndex = Integer.parseInt(indexNo);
    	List<ArticleDisplay> subWorks = asaForm.getArticles();
    	/*
    	 * if subWorks is empty the session probably expired, go back to permission
    	 * summary page.
    	 */
		if (subWorks.size()==0) {
			return CCAction.sessionDataNotFound(mapping,request,request.getParameter(WebConstants.RequestKeys.ITEM));
		}
    	ArticleDisplay articleDisplay = subWorks.get(selectedIndex);
    	//get index to which indicates the selected subWork (article)
    	ArticleSearchState articleSearchState = ArticleSearchResultsPaging.getArticleSearchStateFromUserContext();
    	
    	articleSearchState.setSelectedSubWork(articleDisplay.getWork());
    	return mapping.findForward( RLQUICKPRICE );
    }
    
    //  Helper method to grab our result window and stuff
    //  it into our search form.

    protected void getResults(ArticleSearchActionForm searchForm)
    {
        int pageNo = Integer.parseInt( searchForm.getPageNo() );
       int pageSize = Integer.parseInt( searchForm.getPageSize() );
        WorkExternal work = ((WorkRightsAdapter)searchForm.getSelectedPublication()).getWork();
        searchForm.setArticles(ArticleSearchResultsPaging.getResultsWindow(work, pageNo, pageSize));
    }
    
    private void addError(HttpServletRequest r, String s)
    {
        ActionErrors errors = new ActionErrors();

        errors.add(
            ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage(s)
        );
        r.setAttribute(Globals.ERROR_KEY, errors);
    }

}
