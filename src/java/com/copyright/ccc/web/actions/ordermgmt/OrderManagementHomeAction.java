package com.copyright.ccc.web.actions.ordermgmt;





public class OrderManagementHomeAction extends OMBaseAction {

	private static final long serialVersionUID = 1L;	
    private QuickTabSelect quickTabSelect = null;
    
    private String tbdDisplay = null;
    
    public String getTbdDisplay() {
		return tbdDisplay;
	}

	public void setTbdDisplay(String tbdDisplay) {
		this.tbdDisplay = tbdDisplay;
	}

	public static final String ORDER_SEARCH = "order_search";
    public static final String RESEARCH_SEARCH = "research_search";
    public static final String ADJUSTMENT_SEARCH = "adjustment_search";
    public static final String NEW_ORDER = "new_order";
	
	/**
	 * Default action - executed when page is first reached
	 */
    @Override
	public String execute()
	{	
		super.handleQuickTabSelected();
		
		if ( quickTabSelect != null ) {
			setTbdDisplay(quickTabSelect.displayText());
		}
		if ( quickTabSelect != null && quickTabSelect.isSearch() ) {
				
			if ( quickTabSelect.isOrderSearch() ) {
				if ( quickTabSelect.isSearchByAllCriteria() ) {
					setTbdDisplay(quickTabSelect.name());
					return ORDER_SEARCH;
				}
				if ( quickTabSelect.isSearchByUser() ) {
					// do search by user
					quickTabUserCounts.setUserMyOrders( getMyOrders() + 1 );
					return ORDER_SEARCH;
				}
				if ( quickTabSelect.isNew() ) {
					quickTabUserCounts.setUserMyOrders( getMyOrders() + 1 );
					return NEW_ORDER;
				}
			} else 
			if ( quickTabSelect.isAdjustmentSearch() ) {
				if ( quickTabSelect.isSearchByAllCriteria() ) {
					quickTabUserCounts.setUserMyAdjustments( getMyAdjustments() + 1 );
					return ADJUSTMENT_SEARCH;
				}
				if ( quickTabSelect.isSearchByUser() ) {
					// do search by user
					quickTabUserCounts.setUserMyAdjustments( getMyAdjustments() - 1 );
					quickTabUserCounts.setUserPendingAdjustments( getPendingAdjustments() + 1 );
					return ADJUSTMENT_SEARCH;
				}
				if ( quickTabSelect.isSearchByPending() ) {
					// do search for pending
					quickTabUserCounts.setUserPendingAdjustments( getPendingAdjustments() - 1 );
					return ADJUSTMENT_SEARCH;
				}
			} else
			if ( quickTabSelect.isResearchSearch() ) {
				if ( quickTabSelect.isSearchByAllCriteria() ) {
					quickTabUserCounts.setUserMyAdjustments( getMyAdjustments() + 1 );
					return RESEARCH_SEARCH;
				}
				if ( quickTabSelect.isSearchByUser() ) {
					// do search by user
					quickTabUserCounts.setUserUnassignedResearch( getUnassignedResearch() + 1 );
					return RESEARCH_SEARCH;
				}
				if ( quickTabSelect.isSearchByAssigned() ) {
					// do search for assigned
					quickTabUserCounts.setUserAssignedResearch( getAssignedResearch() - 1 );
					return RESEARCH_SEARCH;
				}
				if ( quickTabSelect.isSearchByUnassigned() ) {
					// do search for unassigned
					quickTabUserCounts.setUserAssignedResearch( getAssignedResearch() + 1 );
					quickTabUserCounts.setUserUnassignedResearch( getUnassignedResearch() - 1 );
					return RESEARCH_SEARCH;
				}
			} 
		} 
		return SUCCESS;
	}
    
    @Override
	public QuickTabSelect getDefaultQuickTabSelect() {
		return QuickTabSelect.ordermgmt_menu_home;
	}
		
}
