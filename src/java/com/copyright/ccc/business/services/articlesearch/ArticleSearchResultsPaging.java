package com.copyright.ccc.business.services.articlesearch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.searchRetrieval.api.SearchRetrievalService;
import com.copyright.svc.searchRetrieval.api.data.ArticleSearch;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalConsumerContext;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalResult;

public class ArticleSearchResultsPaging {

    
    protected static final Logger _logger = Logger.getLogger( ArticleSearchResultsPaging.class );

 
    protected static final int MAXRESULTSREQUEST=100;

	public static SearchRetrievalResult search( ArticleSearch articleSearch, WorkExternal work, List <String> sortList, int startPage) {
		return search( articleSearch, work, sortList, startPage, MAXRESULTSREQUEST);
	}
	private static SearchRetrievalResult search( ArticleSearch articleSearch, WorkExternal selectedWork, List <String> sortList, int startPage, int pageSize) {	
		SearchRetrievalService searchService = ServiceLocator.getSearchRetrievalService();
        SearchRetrievalResult result =
        	searchService.searchArticle(new SearchRetrievalConsumerContext(), articleSearch, selectedWork, sortList, startPage,pageSize);
        //Save the first page of the results in the form class and save the entire result set to USerContext
        ArticleSearchResultsPaging.getArticleSearchStateFromUserContext().setSearchResultWorks(result.getWorks().get(0).getSubWorks());
        getArticleSearchStateFromUserContext().setSearchCriteria(articleSearch);
        getArticleSearchStateFromUserContext().setSortList(sortList);
        getArticleSearchStateFromUserContext().setCurrentPage(startPage);
        return result;

	}
	
	/**
     * gets the currently cached batch of searchResults from SearchState object
     * in user context. If the pageNum being requested can be satisfied from
     * the already cached searchResults, copy the page of works to a new List and return.
     * If the pageNum being requested cannot be satisfied by the already
     * cached searchResults, fetch the next batch from SRS and then continue as 
     * above.
	 * 
	 * @param work
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
    public static List<WorkExternal> getResultsWindow(WorkExternal work, int pageNum, int pageSize) {
    	List<WorkExternal> searchResultWorks = null;

        searchResultWorks = 
        	getArticleSearchStateFromUserContext().getSearchResultWorks();

        int numResults = searchResultWorks.size();
        if (pageSize > numResults) {
            pageSize = numResults;
        }
          
        int startPos = (pageNum - 1) * pageSize;
     
        //need to fetch next 100 since we don't have it in cache
        if (pageNum*pageSize>searchResultWorks.size()){
        	//try to determine the page number that SRS needs (100 results per fetch)
        	int srsPage=(pageNum*pageSize/100)+1;
        	int currentPage = getArticleSearchStateFromUserContext().getCurrentPage();
        	int numPagesToGrab = srsPage - currentPage;
        	
        	for (int index=0; index<numPagesToGrab; index++){
        		SearchRetrievalResult searchRetrievalResult =
        		search(getArticleSearchStateFromUserContext().getSearchCriteria(), work,getArticleSearchStateFromUserContext().getSortList(),getArticleSearchStateFromUserContext().getCurrentPage()+1);
        		getArticleSearchStateFromUserContext().addSearchResultsWorks(searchRetrievalResult.getWorks());
        	}
        	/*
        	 * just fetched new batch of works from SRS, ensure we start at the first one
        	 */
        	startPos=0;
        }
        getArticleSearchStateFromUserContext().setPageSize(pageSize);

        return copyPage(startPos, pageSize);

    }

    public static List<WorkExternal> copyPage(int startPos, int pageSize) {
    	
    	List<WorkExternal> searchResultWorks = 
        	getArticleSearchStateFromUserContext().getSearchResultWorks();

    	
        List<WorkExternal> resultWindow = new ArrayList<WorkExternal>();
        
        WorkExternal[]workArray = new WorkExternal[searchResultWorks.size()];
        searchResultWorks.toArray(workArray);
        
        int endPos = searchResultWorks.size()>pageSize+startPos ? pageSize+startPos : searchResultWorks.size();
        for (int index=startPos; index<endPos; index++){
        	resultWindow.add(workArray[index]);
        	
        }

        return resultWindow;
    }


    public ArticleSearch getSearchCriteria() {

        return getArticleSearchStateFromUserContext().getSearchCriteria();
    }

    public static ArticleSearchState getArticleSearchStateFromUserContext() {
        // get from UserContext if exists, otherwise create 
        // and add to user context;

        ArticleSearchState articleSearchState = UserContextService.getArticleSearchState();

        if (articleSearchState == null) {
        	articleSearchState = new ArticleSearchState();
            UserContextService.setArticleSearchState(articleSearchState);
        }

        return articleSearchState;

    }
}
