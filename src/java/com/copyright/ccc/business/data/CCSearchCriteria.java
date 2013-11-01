package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;

/**
 * @author  Mike Tremblay
 * @version 1.0
 * Created 26-Oct-2006
 */
public interface CCSearchCriteria  extends  Serializable{

    public static final int BASIC_SEARCH_TYPE = 0;

    public static final int ADVANCED_SEARCH_TYPE = 1;

    public static final int RELEVANCE_SORT = 0;

    public static final int TITLE_SORT = 1;

    public static final int PUBLISHER_SORT = 2;

    public void setTitleOrStdNo(String searchCriteria);

    public void setSearchType(int searchType);

    public void setPublisher(String publisher);

    public void setAuthorEditor(String authorEditor);

    public void setVolume(String volume);

    public void setEdition(String edition);

    public void setSortType(int sortType);

    public String getTitleOrStdNo();

    public int getSearchType();

    public String getPublisher();

    public String getAuthorEditor();

    public String getVolume();

    public String getEdition();

    public int getSortType();
    
    public Map<Long,String> getDisplayablePermissions();
    
    public void setDisplayablePermissions(Map<Long,String> displayablePermissions);

    public void setWrWrkInst( String wrWrkInst );
    public String getWrWrkInst();
    
    //  2009-12-07  MSJ
    //  Adding methods for summit project metadata limiter
    //  fields.  Please not the discrepancy between the data
    //  types of the setters and getters.  For more information
    //  see SearchCriteriaImpl.java under services/search.
    
    public void setCountries(String[] countries);
    public void setLanguages(String[] languages);
    public void setPubTypes(String[] pubTypes);
    public void setSeries(String series);
    
    public List<String> getCountries();
    public List<String> getLanguages();
    public List<String> getPubTypes();
    public String getSeries();
    
    public String toString();
    
    public PublicationSearch getPublicationSearch();
    
    public void clear();
}
