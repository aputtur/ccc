package com.copyright.ccc.business.services.api;

import java.util.Date;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;

public interface SearchServicesIntf {

    public int performBasicSearch(CCSearchCriteria ccSearchCriteria, int displayPageSize);

    public int performAdvancedSearch(CCSearchCriteria ccSearchCriteria, int displayPageSize); 
    
    public int performSearch( PublicationSearch criteria
            , String sortBy
            , int page
            , int pageSize
            , int displayPageSize);

    public Publication getSingleItem(String idNo);

    public Publication getSingleItemByKey(long pubPrimaryKey);
    
    public Publication getSingleItemByTF(long tfKey);

    public Publication[] getResultsWindow(int pageNum, int pageSize);

    public Publication[] getNextPage();

    public Publication[] getPreviousPage();

    public CCSearchCriteria getSearchCriteria();

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
    public PublicationPermission getAlternativeRightFromSearchResults(PublicationPermission pubPerm, int licenseeClass, Date pubDate);

}
