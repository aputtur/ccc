package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.ProcessMessage;
import com.copyright.ccc.web.actions.ordermgmt.data.SearchWorkWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.SearchWorksCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.SearchWorksResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.util.SearchWorkNoCriteriaException;
import com.copyright.svc.worksremote.api.WorksRemoteConsumerContext;
import com.copyright.svc.worksremote.api.search.SearchParms;
import com.copyright.svc.worksremote.api.search.SearchResultSet;

public class SearchWorksAction extends OMBaseAction implements CustomPageControlAware {

	private static final long serialVersionUID = 1L;	
	
	private static Logger _logger = Logger.getLogger(SearchWorksAction.class);
    
	private SearchWorksCriteriaWrapper searchCriteria = new SearchWorksCriteriaWrapper();
    private SearchWorksResultWrapper searchResults;
 
    private String newPage;
	private String newPageSize;

	private boolean showResults = false;
	private boolean showCriteria = true;
	private boolean searchExecuted = false;
	
	private boolean limitCriteriaToOnlyTfWorkId = true;
		
	public boolean isLimitCriteriaToOnlyTfWorkId() {
		return limitCriteriaToOnlyTfWorkId;
	}

	public void setLimitCriteriaToOnlyTfWorkId(boolean limitCriteriaToOnlyTfWorkId) {
		this.limitCriteriaToOnlyTfWorkId = limitCriteriaToOnlyTfWorkId;
	}

	private List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
	
 	
	public List<ProcessMessage> getProcessMessages() {
		return processMessages;
	}

	public void setProcessMessages(List<ProcessMessage> processMessages) {
		this.processMessages = processMessages;
	}

	public String searchAgain() {
		String sIdno = searchCriteria.getIdnoNumber();
		String sTitle = searchCriteria.getTitle();
		String sWrId = searchCriteria.getWrWorkId();
		String sTfId = searchCriteria.getTfWorkId();
		searchCriteria = getWorkSearchCriteriaInSession();
		searchCriteria.setIdnoNumber(sIdno);
		searchCriteria.setTitle(sTitle);
		searchCriteria.setTfWorkId(sTfId);
		searchCriteria.setWrWorkId(sWrId);
		//_logger.info(sTitle()+"="+searchCriteria.getTitle());
		searchCriteria.getPageControl().setPage(1);
		setSearching(true);
		setResorting(false);
		return execute();
		
	}
	/**
	 * Default action - executed when page is first reached
	 * and whenever new search or sort order is requested
	 */
	@Override
	public String execute()
	{	
		
		searchResults = new SearchWorksResultWrapper();
		showCriteria = true;
		showResults = false;
		
		if ( !isSearching() ) {
			searchCriteria = new SearchWorksCriteriaWrapper();
			return SUCCESS;
		}
										
		try {
			searchResults = performSearch(searchCriteria);
			if ( isLimitCriteriaToOnlyTfWorkId() ) {
				if ( searchResults.getWorksList() != null && !searchResults.getWorksList().isEmpty() ) {
					for ( SearchWorkWrapper sw : searchResults.getWorksList() ) {
						if ( sw.getWork().getDoNotUseInd() ) {
							processMessages.add(new ProcessMessage("Work is DNAR, please choose another"));
							break;
						}
					}
				}
			}
		} catch (SearchWorkNoCriteriaException e) {
			if ( processMessages.isEmpty() ) {
				if ( isLimitCriteriaToOnlyTfWorkId() ) {
					processMessages.add(new ProcessMessage("Please enter a TF Work ID"));
				} else {
					processMessages.add(new ProcessMessage("At least one search field must be specified"));
					
				}
			}
			showResults = false;
			return INPUT;
		}

		searchExecuted = true;

		if (searchResults.isHasResults()) {
			showResults = true;
		} else {
			searchCriteria.getPageControl().setListSize(0);
			searchCriteria.getPageControl().setPage(1);
		}
		
		storeWorkSearchCriteriaInSession(searchCriteria);		
		return SUCCESS;
	
	}
		
	public CustomPageControl getCustomPageControl() {
		if ( getSearchCriteria()!= null ) {
			if ( getSearchCriteria().getPageControl() != null ) {
				return getSearchCriteria().getPageControl();
			}
		}
		return new CustomPageControl();
	}
	
	@Override
	public String reSizePageResults() {
		int nPageSize = -1;
		if ( StringUtils.isNotEmpty(getNewPageSize()) ) {
			if ( StringUtils.isNumeric(getNewPageSize().trim()) ) {
				nPageSize = Integer.valueOf(getNewPageSize().trim()).intValue();
			}
		}
		searchCriteria = getWorkSearchCriteriaInSession();
		if ( nPageSize > 0 ) {
			searchCriteria.getPageControl().setPageSize(nPageSize);
			searchCriteria.getPageControl().setPage(searchCriteria.getPageControl().getPageAfterHandlingPageSizeChange());
			searchCriteria.getPageControl().setLastPageSize(nPageSize);
			searchCriteria.setStartEndDisplayRows();
		}
		setSearching(true);
		setResorting(false);
		
		execute();
		
		return SUCCESS;
	
	}	
	
	@Override
	public String rePageResults() {
		int nPage = -1;
		if ( StringUtils.isNotEmpty(getNewPage()) ) {
			if ( StringUtils.isNumeric(getNewPage().trim()) ) {
				nPage = Integer.valueOf(getNewPage().trim()).intValue();
			}
		}
		searchCriteria = getWorkSearchCriteriaInSession();
		if ( nPage > 0 ) {
			searchCriteria.getPageControl().setPage(nPage);
			searchCriteria.setStartEndDisplayRows();
		}
		setSearching(true);
		setResorting(false);
		
		execute();
		
		return SUCCESS;
	
	}			

	/**
	 * Does actual search process
	 * @param osc
	 * @return
	 */
	private SearchWorksResultWrapper performSearch(SearchWorksCriteriaWrapper searchWorkCriteria) {
		
		WorksRemoteConsumerContext context = new WorksRemoteConsumerContext();
		SearchParms searchParms = searchWorkCriteria.getWorkSearchParms(getProcessMessages());
		SearchResultSet searchResultSet = getWrSearchSvc().searchWorks(context, searchParms );
		
		if ( searchResults != null ) {
			if ( searchResultSet.getResults() != null && !searchResultSet.getResults().isEmpty() ) {
				searchResults = new SearchWorksResultWrapper( searchResultSet  );
				getCustomPageControl().setListSize(Long.valueOf(searchResults.getTotalRows()).intValue());
				getCustomPageControl().setTotalRecords(Long.valueOf(searchResults.getTotalRows()).intValue());
			} else {
				getCustomPageControl().setListSize(0);
			}
		} 
					
		return searchResults;

	}

	public SearchWorksCriteriaWrapper getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(SearchWorksCriteriaWrapper searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public SearchWorksResultWrapper getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(SearchWorksResultWrapper searchResults) {
		this.searchResults = searchResults;
	}

	public String getNewPage() {
		return newPage;
	}

	public void setNewPage(String newPage) {
		this.newPage = newPage;
	}

	public String getNewPageSize() {
		return newPageSize;
	}

	public void setNewPageSize(String newPageSize) {
		this.newPageSize = newPageSize;
	}

	public boolean isShowResults() {
		return showResults;
	}

	public void setShowResults(boolean showResults) {
		this.showResults = showResults;
	}

	public boolean isShowCriteria() {
		return showCriteria;
	}

	public void setShowCriteria(boolean showCriteria) {
		this.showCriteria = showCriteria;
	}

	public boolean isSearchExecuted() {
		return searchExecuted;
	}

	public void setSearchExecuted(boolean searchExecuted) {
		this.searchExecuted = searchExecuted;
	}
	
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}	
	
}

