package com.copyright.ccc.web.actions.ordermgmt;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public enum QuickTabSelect {

	ordermgmt_menu_home(false,"NONE","NONE"),
	ordermgmt_menu_order(false,"ORDER","ALL"),
	ordermgmt_menu_order_my(true,"ORDER","MY"),
	ordermgmt_menu_order_new(false,"ORDER","NEW"),
	ordermgmt_menu_adjustment(false,"ADJUSTMENT","ALL"),
	ordermgmt_menu_adjustment_my(true,"ADJUSTMENT","MY"),
	ordermgmt_menu_adjustment_pending(true,"ADJUSTMENT","PENDING"),
	ordermgmt_menu_research(false,"RESEARCH","ALL"),
	ordermgmt_menu_research_my(true,"RESEARCH","MY"),
	ordermgmt_menu_research_assigned(true,"RESEARCH","ASSIGNED"),
	ordermgmt_menu_research_unassigned(true,"RESEARCH","UNASSIGNED"),
	ordermgmt_menu_emulate(false,"NONE","NONE"),
	ordermgmt_menu_reports(false,"NONE","NONE");
    
	private boolean search = false;
	private String  searchFor = "NONE";
	private String  searchBy  = "NONE";
	
	public static final String ORDER_SEARCH = "ORDER";
	public static final String RESEARCH_SEARCH = "RESEARCH";
	public static final String ADJUSTMENT_SEARCH = "ADJUSTMENT";
	
	public static final String SEARCH_BY_ALL_CRITERIA = "ALL";
	public static final String SEARCH_BY_USER = "MY";
	public static final String SEARCH_BY_PENDING = "PENDING";
	public static final String SEARCH_BY_ASSIGNED = "ASSSIGNED";
	public static final String SEARCH_BY_UNASSIGNED = "UNASSIGNED";
	public static final String NEW_ORDER = "NEW";
	
	private QuickTabSelect(boolean isSearch, String searchFor, String searchBy ) {
		this.search = isSearch;
		this.searchFor = searchFor;
		this.searchBy = searchBy;
	}
	public String displayText() {
		return "Name=" + this.name() + " SearchFor=" + this.getSearchFor() + " SearchBy=" + this.searchBy;
	}
	public static QuickTabSelect matching(String dotFormatted) {
		
		if ( StringUtils.isEmpty(dotFormatted)) {
			return null;
		}
		
 		for(QuickTabSelect quickTabSelect : QuickTabSelect.values())
		{
			if ( quickTabSelect.name().replaceAll("_",".").equalsIgnoreCase(dotFormatted) ) {
				return quickTabSelect;
			}
		}
		return null;				
	}
    public static QuickTabSelect matching(List<String> dotFormatted, QuickTabSelect defaultQuickTab, QuickTabUserCounts quickTabUserCounts) {
		
		if ( dotFormatted == null || dotFormatted.isEmpty() ) {
			return defaultQuickTab;
		}
		String lastDotFormatted = dotFormatted.get(0);
 		for(QuickTabSelect quickTabSelect : QuickTabSelect.values())
		{
			if ( quickTabSelect.getDotFormattedSelectedValue().equalsIgnoreCase(lastDotFormatted) ) {
				if ( quickTabSelect.isSearch() ) {
					switch ( quickTabSelect ) {
					case ordermgmt_menu_order_my: 
						 if ( quickTabUserCounts.getUserMyOrders() <= 0 ) {
							 return QuickTabSelect.ordermgmt_menu_order;
						 }
						 break;
					case ordermgmt_menu_adjustment: 
						 return QuickTabSelect.ordermgmt_menu_adjustment;
						 	 
					case ordermgmt_menu_adjustment_my: 
						 if ( quickTabUserCounts.getUserMyAdjustments() <= 0 ) {
							 return QuickTabSelect.ordermgmt_menu_adjustment_my;
						 }
						 break;
					case ordermgmt_menu_adjustment_pending: 
						 if ( quickTabUserCounts.getUserPendingAdjustments() <= 0 ) {
							 return QuickTabSelect.ordermgmt_menu_adjustment_pending;
						 }
						 break;
					case ordermgmt_menu_research_my: 
						 if ( quickTabUserCounts.getUserMyResearch() <= 0 ) {
							 return QuickTabSelect.ordermgmt_menu_research;
						 }
						 break;
					case ordermgmt_menu_research_assigned: 
						 if ( quickTabUserCounts.getUserAssignedResearch() <= 0 ) {
							 return QuickTabSelect.ordermgmt_menu_research;
						 }
						 break;
					case ordermgmt_menu_research_unassigned: 
						 if ( quickTabUserCounts.getUserUnassignedResearch() <= 0 ) {
							 return QuickTabSelect.ordermgmt_menu_research;
						 }
						 break;
					}
				}
				return quickTabSelect;
			}
		}
		return defaultQuickTab;				
	}
        
    public String getDotFormattedSelectedValue() {
    	return this.name().replaceAll("_",".");
    }
    
	public boolean isSearch() {
		return search;
	}
	public String getSearchBy() {
		return searchBy;
	}
	public String getSearchFor() {
		return searchFor;
	}
	public boolean isNotPredefinedQueryType() {
		return 
		   !this.getSearchBy().equalsIgnoreCase(SEARCH_BY_USER)
		&& !this.getSearchBy().equalsIgnoreCase(SEARCH_BY_PENDING)
		&& !this.getSearchBy().equalsIgnoreCase(SEARCH_BY_ASSIGNED)
		&& !this.getSearchBy().equalsIgnoreCase(SEARCH_BY_UNASSIGNED)
		&& !this.getSearchFor().equalsIgnoreCase(RESEARCH_SEARCH)
		;
	}
	public boolean isOrderSearch() {
		return this.searchFor.equalsIgnoreCase(ORDER_SEARCH);
	}
	public boolean isResearchSearch() {
		return this.searchFor.equalsIgnoreCase(RESEARCH_SEARCH);
	}
	public boolean isAdjustmentSearch() {
		return this.searchFor.equalsIgnoreCase(ADJUSTMENT_SEARCH);
	}
	public boolean isSearchByAllCriteria() {
		return this.searchBy.equalsIgnoreCase(SEARCH_BY_ALL_CRITERIA);
	}
	public boolean isSearchByUser() {
		return this.searchBy.equalsIgnoreCase(SEARCH_BY_USER);
	}
	public boolean isSearchByPending() {
		return this.searchBy.equalsIgnoreCase(SEARCH_BY_PENDING);
	}
	public boolean isSearchByAssigned() {
		return this.searchBy.equalsIgnoreCase(SEARCH_BY_ASSIGNED);
	}
	public boolean isSearchByUnassigned() {
		return this.searchBy.equalsIgnoreCase(SEARCH_BY_UNASSIGNED);
	}
	public boolean isNew() {
		return this.searchBy.equalsIgnoreCase(NEW_ORDER);
	}
}

