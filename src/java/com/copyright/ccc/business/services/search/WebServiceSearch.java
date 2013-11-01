package com.copyright.ccc.business.services.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.CCSearchCriteria;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermissionSearchResult;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.data.inventory.BasicSearchCriteria;
import com.copyright.data.inventory.BibliographicCondition;
import com.copyright.data.inventory.ParseRulesImpl;
import com.copyright.data.inventory.SearchCriteria;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.searchRetrieval.api.SearchRetrievalService;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalConsumerContext;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalResult;

/**
 * Wraps the search and work services with an easy-to-use interface.
 *
 *	changes:	List modifications, most recent at TOP.
 *
 *	when		who		what
 *	----------	-------	-----------------------------------------
 *	2010-02-08	Jessop	Removed all the query string logic.  Tied
 *						into new searchRetrieval wrapper service.
 */
public class WebServiceSearch extends AbstractSearchServices 
{
    //  In most cases we will be using the new SearchRetrieval
    //  wrapper service.  But... we do still need direct access
    //  to the SearchRetrieverService for special cases.
    
    private static Set<Character> alphaNumeric = null;

    protected static final Logger _logger = Logger.getLogger( WebServiceSearch.class );

    static
    {
        alphaNumeric = new HashSet<Character>();
        for (char x = '0'; x <= '9'; x++) alphaNumeric.add(x);
        for (char x = 'A'; x <= 'Z'; x++) alphaNumeric.add(x);
    }

    public WebServiceSearch() {
    }
    
    public int performSearch( PublicationSearch criteria
                            , String sortBy
                            , int page
                            , int pageSize
                            , int displayPageSize)
    {
        PublicationRightsSearchResult publicationRightsSearchResult = null;
        SearchRetrievalService searchRetrievalService = ServiceLocator.getSearchRetrievalService();
        List<String> sortList = new ArrayList<String>();

        if (_logger.isDebugEnabled())
        {
            StringBuffer obuf = new StringBuffer();
            obuf.append("\n\n...in performSearch...")
                .append("\nstdno/ttl: ").append(criteria.getPubTitleOrStdNumber())
                .append("\nauth/ed:   ").append(criteria.getAuthorOrEditor())
                .append("\npub/rght:  ").append(criteria.getPublisherOrRightsholder())
                .append("\nsort:      ").append(sortBy)
                .append("\nseries:    ").append(criteria.getSeriesName())
                .append("\nlanguage:  ").append(criteria.getLanguage())
                .append("\ncountry:   ").append(criteria.getCountry())
                .append("\npub. type: ").append(criteria.getPubType())
                .append("\n\n");
            _logger.debug(obuf.toString());
        }
        
        sortList.add(sortBy);
        
        SearchRetrievalResult result = 
        	searchRetrievalService.searchPublication( new SearchRetrievalConsumerContext()
                                     , criteria
                                     , sortList
                                     , page
                                     , displayPageSize );
                                     
        //  Package works without searching for the rights to save time.
        //  We will get the rights when the user selects an individual work
        
        if (_logger.isDebugEnabled())
        {
            List<WorkExternal> works = result.getWorks();
            StringBuffer obuf = new StringBuffer();
            
            obuf.append("\n\nsearchPublication returned ");
            
            if (works.size() > 0) obuf.append(Long.toString(works.size()));
            else obuf.append("0");
            
            obuf.append(" works.\n\n");
            _logger.debug(obuf.toString());
            
            obuf = new StringBuffer();
            obuf.append("\n\n...Works...");
            
            for (WorkExternal work : works)
            {
                obuf.append("\ntitle:    ").append(work.getFullTitle())
                    .append("\nidno:     ").append(work.getIdno())
                    .append("\ntype cd:  ").append(work.getIdnoTypeCode())
                    .append("\nwrk inst: ").append(work.getPrimaryKey())
                    .append("\ntf inst:  ").append(work.getTfWksInst())
                    .append("\nwks inst: ").append(work.getTfWrkInst())
                    .append("\n");
            }
            _logger.debug(obuf.toString());
        }

        PublicationPermissionSearchResult previousResults = 
            getSearchStateFromUserContext().getSearchResults();
            
        if (previousResults != null && previousResults.getNumResultsReturned() > 0 && page > 1) {
        	//append new results to old
        	_logger.info("\nAppending previous results to new results.\n");
        	previousResults.appendNewResults(result.getWorks());
        	getSearchStateFromUserContext().setSearchResults(previousResults);
        }
        else {
            publicationRightsSearchResult = 
                new PublicationRightsSearchResult(result.getWorks());

            getSearchStateFromUserContext().setSearchResults(publicationRightsSearchResult);
        	
        }
        getSearchStateFromUserContext().setCurrentPage(page);
        getSearchStateFromUserContext().setPageSize(pageSize);
        getSearchStateFromUserContext().setResultsTotalSize(result.getResultCount());

        return result.getResultCount();
    }

    //  The only difference between the basic and advanced search
    //  methods is whether or not the generated publication objects
    //  should look up rights as they are being created.  The second
    //  parameter of the call to "performSearch" indicates 0 for false
    //  or 1 for true (whether or not to load rights).

    public int performBasicSearch(CCSearchCriteria ccSearchCriteria, int displayPageSize)
    {
        String sortBy = PublicationSearch.SORT_RELEVANCE;
        PublicationSearch criteria = ccSearchCriteria.getPublicationSearch();
        
        //	No limiters allowed in basic search.  In fact only the
        //	title field should be included.  Nulling these values here
        //	allows us to maintain any values that had been pre-set in
        //	the search form if the user had already performed an advanced
        //	search.
        
        criteria.setCountry(null);
        criteria.setLanguage(null);
        criteria.setPubType(null);
        criteria.setSeriesName(null);
        criteria.setPublisherOrRightsholder(null);
        criteria.setAuthorOrEditor(null);
        
        if (ccSearchCriteria.getSortType() == 1) sortBy = PublicationSearch.SORT_TITLE;
        if (ccSearchCriteria.getSortType() == 2) sortBy = PublicationSearch.SORT_PUBLISHER;
        
        getSearchStateFromUserContext().setSearchCriteria(ccSearchCriteria);
        
        return performSearch( criteria, sortBy, 1, 100, displayPageSize);
    }
    
    //  2011-07-14  MSJ
    //  This method performs secondary and tertiary searches before we
    //  offer the user "search instead" options.  See our search actions
    //  under web\actions.
    
    public int performSecondarySearch( CCSearchCriteria ccSearchCriteria
                                     , int displayPageSize
                                     , String alternativeTitle
                                     , Boolean basicSearch )
    {
        //  We want to search, but we don't want to save off our criteria.
        //  Also, if an alternative title was passed in, we want to substitute
        //  the title in the criteria with this one.  IF the basicSearch flag
        //  is not null and set to true, we will wipe all criteria as well.
        
        String originalTitle = null;
        String sortBy = PublicationSearch.SORT_RELEVANCE;
        
        PublicationSearch criteria = ccSearchCriteria.getPublicationSearch();
        PublicationSearch newCriteria = new PublicationSearch();
        
        newCriteria.setHasRights("Y");
        
        if (alternativeTitle != null) {
            newCriteria.setPubTitleOrStdNumber(alternativeTitle);
        }
        else {
            newCriteria.setPubTitleOrStdNumber(criteria.getPubTitleOrStdNumber());
        }
        if (basicSearch != null && basicSearch.equals(Boolean.TRUE)) {
            newCriteria.setCountry(null);
            newCriteria.setLanguage(null);
            newCriteria.setPubType(null);
            newCriteria.setSeriesName(null);
            newCriteria.setPublisherOrRightsholder(null);
            newCriteria.setAuthorOrEditor(null);
        }
        else {
            newCriteria.setCountry(criteria.getCountry());
            newCriteria.setLanguage(criteria.getLanguage());
            newCriteria.setPubType(criteria.getPubType());
            newCriteria.setSeriesName(criteria.getSeriesName());
            newCriteria.setPublisherOrRightsholder(criteria.getPublisherOrRightsholder());
            newCriteria.setAuthorOrEditor(criteria.getAuthorOrEditor());
        }
        return performSearch( newCriteria, sortBy, 1, 100, displayPageSize);
    }
    
    public List<String> getSuggestions(CCSearchCriteria ccSearchCriteria, int numberOfItems, int count)
    {
        //  2011-07-05  MSJ
        //  Really all we need to do is pass in the title string into the fuzzy search
        //  provided by Lech.  This will do its best to figure out possible titles
        //  that might come close to what the user was originally searching for.
        
        List<String> results = null;
        
        PublicationSearch criteria = ccSearchCriteria.getPublicationSearch();
        String title = criteria.getPubTitleOrStdNumber(); // Need to add validation.
        
        SearchRetrievalService searchRetrievalService = ServiceLocator.getSearchRetrievalService();
        
        if (title != null)
        {
            results = searchRetrievalService.fuzzyTitlesForQuery(
                new SearchRetrievalConsumerContext(),
                title, numberOfItems, count
            );
        }
        return results;
    }
    
    public int performAdvancedSearch(CCSearchCriteria ccSearchCriteria, int displayPageSize)
    {
        String sortBy = PublicationSearch.SORT_RELEVANCE;
  //      String originalTitle = new String(ccSearchCriteria.getTitleOrStdNo());
        int count = 0;
        
        if (ccSearchCriteria.getSortType() == 1) sortBy = PublicationSearch.SORT_TITLE;
        if (ccSearchCriteria.getSortType() == 2) sortBy = PublicationSearch.SORT_PUBLISHER;
        
        PublicationSearch criteria = ccSearchCriteria.getPublicationSearch();
        getSearchStateFromUserContext().setSearchCriteria(ccSearchCriteria);

        return performSearch( criteria
                            , sortBy
                            , 1
                            , 100 
                            , displayPageSize);
    }
    
    /*
     * This method searches for the next results (100) from the search service
     */
 /*   public int nextPage(int pageNumber){
    	//get the current search criteria and just get the next set of results
    	CCSearchCriteria ccSearchCriteria = getSearchStateFromUserContext().getSearchCriteria();
        String sortBy = PublicationSearch.SORT_RELEVANCE;
        
        if (ccSearchCriteria.getSortType() == 1) sortBy = PublicationSearch.SORT_TITLE;
        if (ccSearchCriteria.getSortType() == 2) sortBy = PublicationSearch.SORT_PUBLISHER;
        
        PublicationSearch criteria = ccSearchCriteria.getPublicationSearch();
        
        return performSearch( criteria
                , sortBy
                , pageNumber
                , 100 );
    }*/

    private String clean(String idno)
    {
        //  Just numbers.  Numbers and letters maybe.  No
        //  punctuation!
        
        StringBuffer x = new StringBuffer(idno.toUpperCase());
        
        for (int i = 0; i < x.length(); i++)
        {
            if (!alphaNumeric.contains(x.charAt(i))) x.deleteCharAt(i);
        }
        return x.toString();
    }

    public Publication getSingleItem(String stdNumber)
    {
    	SearchRetrievalService searchRetrievalService = ServiceLocator.getSearchRetrievalService();
        if (stdNumber == null || stdNumber.length() == 0) {
             return null;
        }

        // Get instance of WorksQueryBuilder and construct your query
        
        String stdno = clean(stdNumber);
        List<WorkExternal> works = null;
        SearchRetrievalResult result = null;
        SearchRetrievalConsumerContext context = new SearchRetrievalConsumerContext();
        PublicationSearch query = new PublicationSearch();
        List<String> sort = new ArrayList<String>();
        Publication item = null;
        
        //  Perform a simple search, if we get more than one item back,
        //  we need to scan for a matching item based on the stdnumber.
        
        if (_logger.isDebugEnabled())
        {
            _logger.debug("\nRelevancy sorted search on STDNO: " + stdNumber + "\n");
        }
        
        query.setPubTitleOrStdNumber(stdNumber);
        sort.add(PublicationSearch.SORT_RELEVANCE);
        result = searchRetrievalService.searchPublication(context, query, sort, 1, 100);
        
        works = result.getWorks();
        
        if (works != null && works.size() > 0)
        {
            for (WorkExternal work : works)
            {
                String idno = work.getIdnoWop();
                
                if ((idno != null && idno.length() > 1) && stdno.equalsIgnoreCase(idno))
                {
                    item = new WorkRightsAdapter(work, true);
                    break;
                }
            }
        }
        return item;
    }

    public Publication getSingleItemByTF(long tfWrkInst)
    {
		SearchRetrievalService searchRetrievalService = ServiceLocator.getSearchRetrievalService();
		
		List<String> sortList = new ArrayList<String>();
		int page = 1;
		int pageSize = 100;
		
		PublicationSearch publicationSearch = new PublicationSearch();
		publicationSearch.setTfWrkInst(String.valueOf(tfWrkInst));
		SearchRetrievalResult result = searchRetrievalService.searchPublication(new SearchRetrievalConsumerContext(), publicationSearch, sortList, page, pageSize);
		List<WorkExternal> myList = result.getWorks();
		  
		if( myList != null && myList.size() > 0)
		{	  
			return new WorkRightsAdapter(myList.get(0),true);
		}
		else
		{
			return null;
		}
    }

    //get Single Item using Wr Work Inst
    /**
     * Returns an instance of Publication prepopulated with metadata for the supplied
     * publication pubPrimaryKey (wr_wrk_inst), and TF Rights
     */
    public Publication getSingleItemByKey(long pubPrimaryKey) 
    {
       return getSingleItemByKey(pubPrimaryKey, true);
    }
    /**
     * Returns an instance of Publication prepopulated with metadata for the supplied
     * publication pubPrimaryKey (wr_wrk_inst), and TF Rights if prepopulateTFRights is
     * true.
     */    
    public Publication getSingleItemByKey(long pubPrimaryKey, boolean prepopulateTFRights) 
    {
    	SearchRetrievalService searchRetrievalService = ServiceLocator.getSearchRetrievalService();
        SearchCriteria bsc = new BasicSearchCriteria();

        /* create a search condition */

        BibliographicCondition con = new BibliographicCondition();

        con.setFieldName(BibliographicCondition.BASIC_FIELD);
        con.setFieldValue("pub_pk:" + pubPrimaryKey);

        ParseRulesImpl pubOwnerParseRules = new ParseRulesImpl(false, false, "");

        BibliographicCondition pubOwnerCondition =
            new BibliographicCondition(BibliographicCondition.PUBLICATION_OWNER_FIELD,
                                       Long.toString(10L), pubOwnerParseRules);

        ParseRulesImpl config = new ParseRulesImpl(false, false, "");
        con.setParseRules(config);

        /* add the condition to the criteria */

        bsc.addCondition(con);
        bsc.addCondition(pubOwnerCondition);
        //bsc.setMaxResults(100);
        bsc.setPageSize(100); //formerly maxResults
        bsc.setPageNumber(1);

		List<String> sortList = new ArrayList<String>();
		int page = 1;
		int pageSize = 100;
		
		PublicationSearch publicationSearch = new PublicationSearch();
		publicationSearch.setWrWrkInst(String.valueOf(pubPrimaryKey));
		SearchRetrievalResult result = searchRetrievalService.searchPublication(new SearchRetrievalConsumerContext(), publicationSearch, sortList, page, pageSize);
		List<WorkExternal> returnedWorks = result.getWorks();
      
        if( returnedWorks == null || returnedWorks.size() <= 0 ) return null;

        Publication publication = new WorkRightsAdapter(returnedWorks.get(0), prepopulateTFRights);

        return publication;
    }
 
   
    public Publication[] getSearchResults() {
    	return getSearchStateFromUserContext().getSearchResults().getResultSet();
    }
}
