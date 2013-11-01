package com.copyright.ccc.web.util;

import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.search.SearchState;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalConsumerContext;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalResult;

public class RightsLnkQPriceUtil {
	
	private RightsLnkQPriceUtil() {}
	
	 //Helper method to retrieve the selected publication from the works search results
    public static Publication getSelectedPublication(long wrkInst ) {
        // get from UserContext if exists, otherwise create 
        // and add to user context;
    	
        SearchState searchState = UserContextService.getSearchState();

        if (searchState == null) {
            return null;
        }
        Publication publication=getPublication(wrkInst);
        if(publication!=null){
        	searchState.setSelectedWork(((WorkRightsAdapter)publication).getWork());
    		return publication;	
        }

        return null;

    }
    /**
     * 
     * @param pubPrimaryKey
     */
    	public static Publication getPublication(long pubPrimaryKey){
    		boolean prepopulateTFRights=true;
			List<String> sortList = new ArrayList<String>();
			int page = 1;
			int pageSize = 100;
			
			PublicationSearch publicationSearch = new PublicationSearch();
			publicationSearch.setWrWrkInst(String.valueOf(pubPrimaryKey));
			SearchRetrievalResult result = ServiceLocator.getSearchRetrievalService().searchPublication(new SearchRetrievalConsumerContext(), publicationSearch, sortList, page, pageSize);
			List<WorkExternal> returnedWorks = result.getWorks();

			if( returnedWorks == null || returnedWorks.size() <= 0 ) return null;
            Publication publication = new WorkRightsAdapter(returnedWorks.get(0), prepopulateTFRights);
            return publication;
    	}
}
