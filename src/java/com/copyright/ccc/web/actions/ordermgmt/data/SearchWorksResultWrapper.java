package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.copyright.svc.worksremote.api.search.SearchResult;
import com.copyright.svc.worksremote.api.search.SearchResultSet;

public class SearchWorksResultWrapper extends SearchResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	SearchResultSet svcResults = new SearchResultSet();
	
	List<SearchWorkWrapper> worksList = new ArrayList<SearchWorkWrapper>();
	
	public SearchWorksResultWrapper() {
		super();
	}
	
	public SearchWorksResultWrapper(SearchResultSet svcResults) {
		super();
		setSvcResults( svcResults );
		populateWorksList();
	}

	public SearchResultSet getSvcResults() {
		return svcResults;
	}

	public void setSvcResults(SearchResultSet svcResults) {
		this.svcResults = svcResults;
	}
	
	public boolean isHasResults() {
		if ( getSvcResults() != null ) {
			if ( getSvcResults().getResults() != null && !getSvcResults().getResults().isEmpty() ) {
				return true;
			}
		}
		return false;
	}

	public long getTotalRows() {
		
		if ( getSvcResults() != null ) {
			if ( getSvcResults().getResults() != null && !getSvcResults().getResults().isEmpty() ) {
				return getSvcResults().getNumMatches();
			}
		}
		return 0;
	}
	
	private void populateWorksList() {
		setWorksList(new ArrayList<SearchWorkWrapper>());
		if ( getSvcResults() != null ) {
			if ( getSvcResults().getResults() != null && !getSvcResults().getResults().isEmpty() ) {
				for (SearchResult sr : getSvcResults().getResults() ) {
					getWorksList().add( new SearchWorkWrapper(sr));
				}
			}
		}
	}

	public List<SearchWorkWrapper> getWorksList() {
		return worksList;
	}

	public void setWorksList(List<SearchWorkWrapper> worksList) {
		this.worksList = worksList;
	}
	
}
