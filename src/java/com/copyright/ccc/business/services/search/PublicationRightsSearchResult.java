package com.copyright.ccc.business.services.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermissionSearchResult;
import com.copyright.domain.data.WorkExternal;


public class PublicationRightsSearchResult implements PublicationPermissionSearchResult{
    private Publication[] _publications;
    private List<WorkRightsAdapter> _lstPublications;
    private int _totalResultsFound;
    private int _numResultsReturned;
	//private static RightsResolverService rrService;
     protected  static final Logger _logger = Logger.getLogger( PublicationRightsSearchResult.class );
    
    protected PublicationRightsSearchResult() {
        _publications = null;
        _totalResultsFound = 0;
        _numResultsReturned = 0;
    }


    protected PublicationRightsSearchResult(List<WorkExternal> returnedWorks) 
    {
        _totalResultsFound = returnedWorks.size();
        _numResultsReturned =  (_totalResultsFound >1000 ? 1000 : _totalResultsFound);
        _publications = new WorkRightsAdapter[_totalResultsFound];

        _lstPublications = buildWorkRights(returnedWorks);
        _publications = new WorkRightsAdapter[_lstPublications.size()];
        _lstPublications.toArray(_publications);
    }
    
    public void appendNewResults(List<WorkExternal> returnedWorks) {
    	List<WorkRightsAdapter> lstAppendPublications = buildWorkRights(returnedWorks);
    	_lstPublications.addAll(lstAppendPublications);
    	//get new size for publications array and convert whole result list to array.
        _publications = new WorkRightsAdapter[_lstPublications.size()];
        _lstPublications.toArray(_publications);
    }
    
    public int getTotalResultsFound() {
        return _totalResultsFound;
    }

    public int getNumResultsReturned() {
        return _numResultsReturned;
    }

    public Publication[] getResultSet() {
        return _publications;
    }

    public void setResultSet(Publication[] publications) {
        _publications = publications;
    }

    public void setTotalResultsFound(int totalResultsFound) {
        _totalResultsFound = totalResultsFound;
    }

    public void setNumResultsReturned(int numResultsReturned) {
        _numResultsReturned = numResultsReturned;
    }
    
    private List<WorkRightsAdapter> buildWorkRights(List<WorkExternal> returnedWorks){
        List <WorkRightsAdapter> lstWorkRightsAdapter = new ArrayList<WorkRightsAdapter>();
        
        for (WorkExternal work : returnedWorks) {
            WorkRightsAdapter workRightAdapter = new WorkRightsAdapter(work);            
             lstWorkRightsAdapter.add(workRightAdapter);
        }
        
        return lstWorkRightsAdapter;
    }
}
