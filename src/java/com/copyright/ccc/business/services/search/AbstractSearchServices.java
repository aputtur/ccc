package com.copyright.ccc.business.services.search;

import java.util.Date;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.api.SearchServicesIntf;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;

public abstract class AbstractSearchServices implements SearchServicesIntf
{
    protected AbstractSearchServices() {
    }
    
    public Publication[] getResultsWindow(int pageNum, int pageSize) {

        // get searchResults from SearchState object
        // in user context, copy specified page to 
        // new array and return

        Publication[] searchResult = null;

        searchResult = 
                getSearchStateFromUserContext().getSearchResults().getResultSet();

        int numResults = searchResult.length;
        int numTotalResults = getSearchStateFromUserContext().getResultsTotalSize();
        numTotalResults = numTotalResults > 1000 ? 1000 : numTotalResults;

        //  2011-09-12  MSJ
        //  Page size should be compared to TOTAL number of results because we
        //  are in here to grab a NEW window, not the previous one.

        if (pageSize > numTotalResults)
        {
            pageSize = numTotalResults;
        }
        pageNum = Math.min( (int)Math.ceil( numTotalResults * 1.0 / pageSize ), pageNum );
        int startPos = (pageNum - 1) * pageSize;

        //  2011-09-12  MSJ
        //  We have to take into account both page offset AND page size because
        //  we might be coming in here to get a larger window, not just a window
        //  for a page greater than one...

        if (startPos > searchResult.length - 1 || (pageSize > searchResult.length && numTotalResults > pageSize))
        {
        	//we don't have enough results to show this page, we need to get the next 100 and add to results
        	CCSearchCriteria ccSearchCriteria = getSearchStateFromUserContext().getSearchCriteria();
        	PublicationSearch criteria = ccSearchCriteria.getPublicationSearch();
        	
        	String sortBy = PublicationSearch.SORT_RELEVANCE;
            
            if (ccSearchCriteria.getSortType() == 1) sortBy = PublicationSearch.SORT_TITLE;
            if (ccSearchCriteria.getSortType() == 2) sortBy = PublicationSearch.SORT_PUBLISHER;
            
            // get last accessed page number
            int previousPageNum=searchResult.length/pageSize;
            int skippedPages=pageNum-previousPageNum;

            if(skippedPages>1 ){// we are showing only 4 records at  a time
            	//user can jump to 3rd or  4th records only hence fill the holes
            	 for(int pageCount=previousPageNum+1;pageCount<=pageNum; pageCount++ ){
            		 performSearch( criteria, sortBy, pageCount, 100, pageSize);	 
            	 }
            }
            else{
            	performSearch( criteria, sortBy, pageNum, 100, pageSize);
            }
            searchResult = 
                getSearchStateFromUserContext().getSearchResults().getResultSet();
        }
        if ((startPos + pageSize) > searchResult.length){
        	//adjust page size to last remaining records e.g 382 : page size (1)100, (2)100,(3)100 and (4)82
        	pageSize = searchResult.length - startPos;
        }
  
        getSearchStateFromUserContext().setCurrentPage(pageNum);
        getSearchStateFromUserContext().setPageSize(pageSize);

        Publication[] resultWindow = new Publication[pageSize];
        System.arraycopy(searchResult, startPos, resultWindow, 
                         0, pageSize);

        return resultWindow;
    }

    public Publication[] getNextPage() {

        int pageSize = getSearchStateFromUserContext().getPageSize();
        int pageNum = getSearchStateFromUserContext().getCurrentPage();
        Publication[] resultWindow = new Publication[pageSize];

        Publication[] searchResult = 
            getSearchStateFromUserContext().getSearchResults().getResultSet();

        System.arraycopy(searchResult, pageNum * pageSize, resultWindow, 0, 
                         pageSize);

        getSearchStateFromUserContext().setCurrentPage(pageNum++);


        return resultWindow;

    }


    public Publication[] getPreviousPage() {

        int pageSize = getSearchStateFromUserContext().getPageSize();
        int pageNum = getSearchStateFromUserContext().getCurrentPage();
        Publication[] resultWindow = new Publication[pageSize];

        Publication[] searchResult = 
            getSearchStateFromUserContext().getSearchResults().getResultSet();

        System.arraycopy(searchResult, (pageNum - 2) * pageSize, resultWindow, 
                         0, pageSize);

        getSearchStateFromUserContext().setCurrentPage(pageNum--);


        return resultWindow;

    }

    
    /**
     * Gets and returns the current CCSearchCriteria from the Users
     * SearchState. This method should never return null.
     * <p>
     * Note that since this method depends on the UserContextService, it could throw a
     * SecurityRuntimeException if no UserContext is found.
     * @return the users current CCSearchCriteria
     */
    public CCSearchCriteria getSearchCriteria() {
        return getSearchStateFromUserContext().getSearchCriteria();
    }

    /**
     * Gets and returns the current SearchState from the UserContext.
     * If the SearchState on the UserContext is null, a new SearchState is created
     * before being assigned to the UserContext and then returned.
     * <p>
     * Note that since this method depends on the UserContextService, it could throw a
     * SecurityRuntimeException if no UserContext is found.
     * @return the users current SearchState
     */
    protected SearchState getSearchStateFromUserContext() {
    	SearchState searchState = null;
    	if (UserContextService.isAvailable()) {
    		searchState = UserContextService.getSearchState();
    	}
    	
        if (searchState == null) {
            searchState = new SearchState();
        }
        if (UserContextService.isAvailable()) {
            UserContextService.setSearchState(searchState);
        }

        return searchState;

    }
    
    /**
     * This method looks for a PublicationPermission in the
     * current Sessions search results that:<br>
     * 1. matches the work on the supplied PublicationPermission object.<br>
     * 2. matches the type of use on the supplied PublicationPermission.<br>
     * 3. matches the licenseeClass provided<br>
     * 4. covers the publication date provided.<br><br>
     * 
     * If no PublicationPermission is found, the method returns the 
     * original PublicationPermission.
     * 
     * @param pubPerm
     * @param licenseeClass
     * @param pubDate
     */
    public PublicationPermission getAlternativeRightFromSearchResults(PublicationPermission pubPerm, int licenseeClass, Date pubDate) {
        //objective: find the PublicationPermission in the search results that matches the arguments supplied.
        Publication publication = pubPerm.getPublication();
        UsageDescriptor usageDescriptor = pubPerm.getUsageDescriptor();
        
        Publication[] searchResult = UserContextService.getSearchState().getSearchResults().getResultSet();
        //iterate over the search results looking for a matching publication
        for (int i=0; i<searchResult.length; i++) {
            if (searchResult[i].getWrkInst() == publication.getWrkInst()) { 
                //found the pub, now retrieve the permissions that match the usageDescriptor
                PublicationPermission[] perms = searchResult[i].getPublicationPermissions(usageDescriptor.getTypeOfUse(), licenseeClass);
                //permissions retrieved, now find the one that matches the licenseeClass
                for (int j=0; j<perms.length; j++) {
                    if ((perms[j].getUsageDescriptor().getTypeOfUse()==usageDescriptor.getTypeOfUse()) &&
                       (perms[j].getLicenseeClass() == licenseeClass) &&
                       (perms[j].getPubBeginDate().before(pubDate)) &&
                       (perms[j].getPubEndDate().after(pubDate)))
                       {
                           return perms[j];
                       }
                }
            }
        }
        
        return pubPerm;
    }
}
