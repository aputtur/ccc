package com.copyright.ccc.web.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.copyright.ccc.business.services.search.SearchState;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.ChapterSearchActionForm;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.util.ChapterDisplay;
import com.copyright.ccc.web.util.RightsLnkQPriceUtil;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.rightsResolver.api.data.PermissionSummaryCategory;
import com.copyright.svc.searchRetrieval.api.data.ArticleSearch;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalResult;

public class ChapterSearchAction extends CCAction {
    
    private static final String CHAPTER_SEARCH_RESULTS = "chapterSearchResults";
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String RLQUICKPRICE = "rlQuickPrice";
    protected static final Logger _logger = Logger.getLogger( ChapterSearchAction.class );
    protected static final String PAGESELECTED = "pageSelected";
    
    protected static final String CHAPTERINDEX = "chapterIndex";
    
    protected static final int MAXRESULTSREQUEST=100;
   
    
	public ActionForward chapterSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
	{
		ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
		
		if (csaForm.getChapterAuthor().isEmpty() &&
				csaForm.getChapterIDno().isEmpty() &&
				csaForm.getChapterTitle().isEmpty())
		{
			addError(request,"Chapter Search failed.  Reason:  Search form was null.");
			return mapping.findForward("failure");
		}
		ArticleSearch articleSearch = new ArticleSearch();
		

		articleSearch.setArtTitle(csaForm.getChapterTitle());
		articleSearch.setAuthor(csaForm.getChapterAuthor());
		articleSearch.setArtIdno(csaForm.getChapterIDno());
		
		
//		HttpSession httpSession = request.getSession();
//		String item = (String)httpSession.getAttribute(ITEM);
	//	String perm = (String)httpSession.getAttribute(PERM);
		//ChapterSearch.setHostIdno((String)(request.getSession().getAttribute(ITEM)));
		
		List <String> sortList = new ArrayList<String>();
		int sortOption = 0;
		if (csaForm.getSort() != null && !csaForm.getSort().isEmpty()) {
			sortOption = Integer.parseInt(csaForm.getSort());
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
        csaForm.setPageNo(Integer.toString(1));
    
        
		int pageSize=25;
        if (Integer.parseInt(csaForm.getPageSize())>0)
        {
        	pageSize = Integer.parseInt(csaForm.getPageSize());
        }
     
        articleSearch.setPubType("chapter");
        //clear cached search results first so only the latest results show
        WorkExternal selectedWork = ((WorkRightsAdapter)csaForm.getSelectedPublication()).getWork();
        SearchRetrievalResult result = ArticleSearchResultsPaging.search(articleSearch, selectedWork,sortList, startPage);
        csaForm.setChapters(ArticleSearchResultsPaging.copyPage(0, Integer.parseInt(csaForm.getPageSize())));
        csaForm.setCount(Integer.toString(result.getResultCount()));
        csaForm.clearPageRange();
        //calculate the number of pages based on the selected page size and number of results
        csaForm.calculatePageRange();
        return mapping.findForward(SUCCESS);
	}
    public void setRightInstinUserContext( ActionForm          form)
   {
   	
    	ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
    	csaForm.setSelectedTou(csaForm.getSelectedRRTou());
   	UserContextService.getSearchState().setSelectedTou(csaForm.getSelectedRRTou());
   	UserContextService.getSearchState().setSelectedTouId(csaForm.getSelectedRightInst());
   	UserContextService.getSearchState().setSelectedRrTouId(Long.parseLong(csaForm.getSelectedRRTouId()));
   	UserContextService.getSearchState().setSelectedCategoryId(csaForm.getSelectedCategoryId());
   	UserContextService.getSearchState().setSelectedOfferChannel(csaForm.getSelectedOfferChannel());

   	
   	
   }
	
	public ActionForward initSearchParms(ActionMapping       mapping
                               , ActionForm          form
                               , HttpServletRequest  request
                               , HttpServletResponse response )
    {
		ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
		csaForm.clearState();
        //save the selected permission type and id of the selected work
		setRightInstinUserContext(form);
		
		long item = new Long(request.getParameter( WebConstants.RequestKeys.ITEM )).longValue();
        request.getSession().setAttribute(WebConstants.SessionKeys.ITEM, item);
        Publication publication = RightsLnkQPriceUtil.getSelectedPublication(item);
        csaForm.setSelectedPublication(publication);
        //get the selected tou which was put in cache by an AJAX action when the user selected the radio
        //button tou on the Permission Summary Page
        csaForm.setSelectedTou(UserContextService.getSearchState().getSelectedTou());

        /*
         * added by jra to change the way we carry around the PublicatinPermission that was
         * chosen by the user
         */
        /*
         * get the index number of the category the user clicked on
         */
        SearchForm searchForm = (SearchForm) request.getSession().getAttribute( WebConstants.SessionKeys.SEARCH_FORM );
        
        //added for pub to pub (clear out values if we have done an article level search in the past it will still be in the session objects)
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
        
        //save Permission Type in cache for Pricing page display
        String permType = (String)request.getAttribute(WebConstants.RequestKeys.PERM);
        SearchState searchState = UserContextService.getSearchState();       
        
        //added for pub to pub (clear out values if we have done an article level search in the past it will still be in the session objects) 
        searchState.setSelectedRlPermissionType(searchForm.getSelectedRlPermissionType());
        //searchState.setSelectedOfferChannel(searchForm.getSelectedOfferChannel());
        searchState.setSelectedRlPubCode(searchForm.getSelectedRlPubCode());
                
    	if (searchState != null) {
            searchState.setSelectedPermissionType(searchForm.getSelectedPermCatDisplay().getCategoryDescription());
            csaForm.setSelectedCategory(searchForm.getSelectedPermCatDisplay().getCategoryDescription());
    	} else {
    		csaForm.setSelectedCategory("");
    		//searchState.setSelectedPermissionType(""); its null 
    	}
        
        request.getSession().setAttribute(WebConstants.SessionKeys.PERM, csaForm.getSelectedCategory());
        return mapping.findForward( CHAPTER_SEARCH_RESULTS);
    }
	
	public ActionForward clearParameters(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response ){
		ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
		csaForm.clearSearchParameters();
		return mapping.findForward( CHAPTER_SEARCH_RESULTS);
		
	}
	
    //******************************************************************
    //  The following two methods are for the "more" and "less" links
    //  on the search results page.
    
    public ActionForward next( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
    	ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
        csaForm.incrPageNo();
        
        if (Integer.parseInt(csaForm.getCount()) > 0)
        {
            getResults(csaForm);
        }
        
        return mapping.findForward( SUCCESS );
    }
    
    public ActionForward prev( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
    	ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
        csaForm.decrPageNo();
        
        if (Integer.parseInt(csaForm.getCount()) > 0)
        {
            getResults(csaForm);
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
        ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
        String destination;
        int  count = 0;
        
        if (csaForm != null)
        {
        	return chapterSearch(mapping,csaForm,request,response);
        	/*count = articleSearch(mapping,csaForm,request,response);

            if (count > 0)
            {
                getResults(csaForm);
                destination = csaForm.getLastPage();
                updateFormValues(request, csaForm, count);
                csaForm.setLastPage( destination );
            }
            else
            {
                destination = FWD_NO_RESULTS;
            }*/
        }
        else
        {
            //  Log this... and redirect to the simple search page, for now.
    
        	return mapping.findForward( FAILURE);
            //addError( "Sort failed.  Reason:  Search form was null." );
        }

    }
    
    //*******************************************************************
    //  How many items to show per page.
    
    public ActionForward perPage( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
    	ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
        
       /* int numResults = Integer.parseInt(csaForm.getCount());
        int pageSize = Integer.parseInt(csaForm.getPageSize());
        int originalPageNo = Integer.parseInt(csaForm.getPageNo());
        
        int newPageNo = Math.min( (int)Math.ceil(numResults * 1.0 / pageSize), originalPageNo );
        csaForm.setPageNo( String.valueOf(newPageNo) );*/
        
    	//csaForm.setPageNo(pageNo);
        WorkExternal work = ((WorkRightsAdapter)csaForm.getSelectedPublication()).getWork();
    	csaForm.setChapters(ArticleSearchResultsPaging.getResultsWindow(work,1, Integer.parseInt(csaForm.getPageSize())));

        //return articleSearch(mapping,csaForm,request,response);
    	csaForm.clearPageRange();
    	csaForm.calculatePageRange();
    	csaForm.setPageNo("1");
        return mapping.findForward( SUCCESS );
    }
    
    public ActionForward toPage( ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response )
    {
    	ChapterSearchActionForm csaForm = castForm( ChapterSearchActionForm.class, form );
    	//get next page from saved search results
    	String pageNo = request.getParameter(WebConstants.RequestKeys.PNO );
    	int pageNum = Integer.parseInt(pageNo);
    	csaForm.setPageNo(pageNo);
    	WorkExternal work = ((WorkRightsAdapter)csaForm.getSelectedPublication()).getWork();
    	csaForm.setChapters(ArticleSearchResultsPaging.getResultsWindow(work,pageNum, Integer.parseInt(csaForm.getPageSize())));
    	
    	return mapping.findForward(SUCCESS);
    }
    
    public ActionForward continuePurchase(ActionMapping       mapping
            , ActionForm          form
            , HttpServletRequest  request
            , HttpServletResponse response ) {
    	
    	ChapterSearchActionForm chapterSearchForm = castForm( ChapterSearchActionForm.class, form );
    	String indexNo = request.getParameter( CHAPTERINDEX );
    	int selectedIndex = Integer.parseInt(indexNo);
    	List<ChapterDisplay> subWorks = chapterSearchForm.getChapters();
    	ChapterDisplay chapterDisplay = subWorks.get(selectedIndex);
    	//get index to which indicates the selected subWork (article)
    	ArticleSearchState articleSearchState = ArticleSearchResultsPaging.getArticleSearchStateFromUserContext();
    	
    	articleSearchState.setSelectedSubWork(chapterDisplay.getWork());
    	return mapping.findForward( RLQUICKPRICE );
    }
    
    //  Helper method to grab our result window and stuff
    //  it into our search form.

    protected void getResults(ChapterSearchActionForm searchForm)
    {
        int pageNo = Integer.parseInt( searchForm.getPageNo() );
       int pageSize = Integer.parseInt( searchForm.getPageSize() );
        WorkExternal work = ((WorkRightsAdapter)searchForm.getSelectedPublication()).getWork();
        searchForm.setChapters(ArticleSearchResultsPaging.getResultsWindow(work, pageNo, pageSize));
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
