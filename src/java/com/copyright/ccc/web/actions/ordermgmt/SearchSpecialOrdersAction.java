package com.copyright.ccc.web.actions.ordermgmt;

import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;

public class SearchSpecialOrdersAction extends OMBaseAction {

	private static final long serialVersionUID = 1L;	
    private QuickTabSelect quickTabSelect = null;
    private SpecialOrdersSearchCriteria searchCriteria = new SpecialOrdersSearchCriteria();
    
    private String tbdDisplay = "Research";
    
	private boolean showResults = false;
	private boolean showCriteria = true;

	private boolean searching;

	OrderSearchResultWrapper searchResults;
	

	
	public OrderSearchResultWrapper getSearchResults() {
		return searchResults;
	}

	public SpecialOrdersSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(SpecialOrdersSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public void setSearchResults(OrderSearchResultWrapper searchResults) {
		this.searchResults = searchResults;
	}
    
	@Override
    public boolean isSearching() {
		return searching;
	}

	@Override
	public void setSearching(boolean searching) {
		this.searching = searching;
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
    
    public String getTbdDisplay() {
		return tbdDisplay;
	}

	public void setTbdDisplay(String tbdDisplay) {
		this.tbdDisplay = tbdDisplay;
	}

	
	/**
	 * Default action - executed when page is first reached
	 */
	@Override
	public String execute()
	{	
		setTbdDisplay(null);
		quickTabUserCounts = new QuickTabUserCounts(
				getMyOrders()	/* userMyOrders */ 
			   ,getMyAdjustments()	/* userMyAdjustments */ 
			   ,getPendingAdjustments()	/* userPendingAdjustments */ 
			   ,getMyResearch()	/* userMyResearch */ 
			   ,getAssignedResearch()	/* userAssignedResearch */ 
			   ,getUnassignedResearch()	/* userUnassignedResearch */ 
	        );
		//quickTabSelect = QuickTabSelect.matching(getQuickTabSelected());
		if ( quickTabSelect != null ) {
			setTbdDisplay(quickTabSelect.displayText());
			
			if ( quickTabSelect.isResearchSearch() ) {
				if ( quickTabSelect.isSearchByAllCriteria() ) {
					if (!isSearching())
					{
						//clear results, go to order search (display criteria)
						showResults = false;
						showCriteria = true;
					}
					else {
						searchResults = new OrderSearchResultWrapper();
						if (searchResults.getDetailsList().size() > 0)
						{
							showResults = true;
							showCriteria = false;
						}
					}
					return SUCCESS;
					//quickTabUserCounts.setUserMyAdjustments( getMyAdjustments() + 1 );
					//return RESEARCH_SEARCH;
				}
				if ( quickTabSelect.isSearchByUser() ) {
					searchCriteria.setAssignedTo("Essie");
					searchCriteria.setDetailStatus("Not Completely Entered");
					searchResults = new OrderSearchResultWrapper();
					if (searchResults.getDetailsList().size() > 0)
					{
						showResults = true;
						showCriteria = false;
					}
					return SUCCESS;
					//quickTabUserCounts.setUserUnassignedResearch( getUnassignedResearch() + 1 );
					//return RESEARCH_SEARCH;
				}
				if ( quickTabSelect.isSearchByAssigned() ) {
					// do search for assigned
					//figure out how to pass to search the fact that assigned_to is NOT NULL
					searchResults = new OrderSearchResultWrapper();
					if (searchResults.getDetailsList().size() > 0)
					{
						showResults = true;
						showCriteria = false;
					}
					return SUCCESS;					
					//quickTabUserCounts.setUserAssignedResearch( getAssignedResearch() - 1 );
					//return RESEARCH_SEARCH;
				}
				if ( quickTabSelect.isSearchByUnassigned() ) {
					// do search for unassigned
					searchCriteria.setAssignedTo("");
					searchResults = new OrderSearchResultWrapper();
					if (searchResults.getDetailsList().size() > 0)
					{
						showResults = true;
						showCriteria = false;
					}
					return SUCCESS;
					//quickTabUserCounts.setUserUnassignedResearch( getUnassignedResearch() - 1 );
					//return RESEARCH_SEARCH;
				}
			}
		}
		return SUCCESS;
	}
	
	@Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_order;
	}	
		
}
