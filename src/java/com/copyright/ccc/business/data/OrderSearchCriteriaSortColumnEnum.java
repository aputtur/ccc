package com.copyright.ccc.business.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.copyright.data.order.OrderMgmtSearchSortConstants;

public enum OrderSearchCriteriaSortColumnEnum {
	
	DEFAULT_RESULTS_PER_PAGE( 25, true, "MAX_RESULTS", "Page Size" ),

	ASCENDING_SORT( 0,true, "ASC", "Ascending" ),
	DESCENDING_SORTORDER( 1, true,"DESC","Descending" ),

	SORT_BY_DEFAULT(0,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_PURCHASE_DATE,"Default"), 
	SORT_BY_VIEW_DEFAULT(9,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_LICENSE_ID,"Default"), 
	// Both Purchase and License Sorts
	
	SORT_BY_ORDER_DATE(0, true, OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_PURCHASE_DATE,"Order Date"),
	
	// Order Purchase Sorts
	SORT_BY_CONFIRM_NUMBER(1,true,OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_CONFIRM_NUM,"Confirm #"),
	SORT_BY_INSTITUTION(2,true,OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_UNIVERSITY,"Institution Name"),
	SORT_BY_START_OF_TERM(3,true,OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_START_OF_TERM,"Start of Term"),
	SORT_BY_COURSE_NAME(4,true,OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_COURSE_NAME,"Bundle/Course Name"),
	SORT_BY_COURSE_NUMBER(5,true,OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_COURSE_NUMBER,"Bundle/Course Number"),
	SORT_BY_INSTRUCTOR(6,true,OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_INSTRUCTOR,"Instructor"),
	SORT_BY_YOUR_REFERENCE(7,true,OrderMgmtSearchSortConstants.PURCHASE_SORT_TYPE_LCN_HEADER_REF_NUM, "Licensee Ref Number"),
	
	// Order License Sorts
	SORT_BY_PUBLICATION_TITLE(8,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_TITLE,"Publication Title"),
	SORT_BY_ORDER_DETAIL_ID(9,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_LICENSE_ID,"Detail #"), 
	SORT_BY_PERMISSION_TYPE(10,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_TYPE_OF_USE_DISPLAY,"Permission Type"),
	SORT_BY_PERMISSION_STATUS(11,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_DISPLAY_PERMSSION_STATUS,"Permission Status"), 
	SORT_BY_BILLING_STATUS(12,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_BILLING_STATUS,"Billing Status"),
	SORT_BY_INVOICE_NUMBER(13,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_INVOICE_NUMBER,"Invoice Number"),
	SORT_BY_REPUBLICATION_TITLE(14,false,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_REPUB_TITLE,"Republication Title"),
	SORT_BY_REPUBLICATION_DATE(15,false,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_REPUB_DATE,"Republication Date"),
	SORT_BY_REPUBLICATION_PUBLISHER(16,false,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_REPUB_ORG,"Republication Publisher"),
	SORT_BY_YOUR_ACC_REFERENCE(17,true,OrderMgmtSearchSortConstants.LICENSE_SORT_TYPE_LCN_DETAIL_REF_NUM,"Licensee Acct Ref Number"),
	SORT_BY_INTERNET_LOGIN(18,true,"UNKNOWN","Internet Login"),
	SORT_BY_LICENSEE_ACCOUNT(19,true,"UNKNOWN","Licensee Account"),
	SORT_BY_LAST_UPDATE_DATE(20,true,"UNKNOWN","Last Update Date");


	private int displayOrder;
	private String dbColumn;
	private String userText;
	private boolean enabled;
	
	OrderSearchCriteriaSortColumnEnum(int displayOrder, boolean enabled, String dbColumn, String userText ) {
		this.displayOrder = displayOrder;
		this.dbColumn = dbColumn;
		this.userText = userText;
		this.enabled = enabled;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDbColumn() {
		return dbColumn;
	}

	public void setDbColumn(String dbColumn) {
		this.dbColumn = dbColumn;
	}

	public String getUserText() {
		return userText;
	}

	public void setUserText(String userText) {
		this.userText = userText;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	private static List<String> sprint2SortFields = new ArrayList<String>();
	
	static {

		// NOTE - THIS LIMITS SORT FIELDS FOR SPRINT 2 - Bob D 7/13/2010
		sprint2SortFields.add(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DATE.name());
		sprint2SortFields.add(OrderSearchCriteriaSortColumnEnum.SORT_BY_CONFIRM_NUMBER.name());
		sprint2SortFields.add(OrderSearchCriteriaSortColumnEnum.SORT_BY_START_OF_TERM.name());
		sprint2SortFields.add(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID.name());
	}
	
private static List<String> sprint3SortFields = new ArrayList<String>();
	
	static {

		// NOTE - THESE ARE FIELDS ADDED FOR SPRINT 3 AFTIN ADDITION TO SPRINT 2 -- Essie 9/2/10
		sprint3SortFields.add(OrderSearchCriteriaSortColumnEnum.SORT_BY_INVOICE_NUMBER.name());
		sprint3SortFields.add(OrderSearchCriteriaSortColumnEnum.SORT_BY_INTERNET_LOGIN.name());
		sprint3SortFields.add(OrderSearchCriteriaSortColumnEnum.SORT_BY_LICENSEE_ACCOUNT.name());
		sprint3SortFields.add(OrderSearchCriteriaSortColumnEnum.SORT_BY_LAST_UPDATE_DATE.name());
	}

	private static List<LabelValueBean> orderSortFields = null;
	static{
		int enabled = 0;
		for ( OrderSearchCriteriaSortColumnEnum os : OrderSearchCriteriaSortColumnEnum.values() ) {
			if ( os.isEnabled() ) { enabled++; }
		}
		orderSortFields = new ArrayList<LabelValueBean>(enabled + 1);
		for ( OrderSearchCriteriaSortColumnEnum os : OrderSearchCriteriaSortColumnEnum.values() ) {
			if ( os.isEnabled() && os.name().startsWith("SORT_BY") ) {
				if ( os.equals(SORT_BY_DEFAULT) ) {
					orderSortFields.add(new LabelValueBean(os.getUserText(), os.name()));
				} else {
					// NOTE - THIS LIMITS SORT FIELDS FOR SPRINT 2 & 3 - Essie 9/2/2010
					if ( sprint2SortFields.contains(os.name()) || sprint3SortFields.contains(os.name())) {
						orderSortFields.add(new LabelValueBean(os.getUserText(), os.name()));
					}
				}
			}
		}	
	}
	
	private static List<LabelValueBean> adjustmentSortFields = null;
	static{
		int enabled = 0;
		for ( OrderSearchCriteriaSortColumnEnum os : OrderSearchCriteriaSortColumnEnum.values() ) {
			if ( os.isEnabled() ) { enabled++; }
		}
		adjustmentSortFields = new ArrayList<LabelValueBean>(enabled + 1);
		for ( OrderSearchCriteriaSortColumnEnum os : OrderSearchCriteriaSortColumnEnum.values() ) {
			if ( os.isEnabled() && os.name().startsWith("SORT_BY") ) {
				if ( os.equals(SORT_BY_DEFAULT) ) {
					adjustmentSortFields.add(new LabelValueBean(os.getUserText(), os.name()));
				} else {
					// NOTE - THIS LIMITS SORT FIELDS TO THE LIST FROM SPRINT 2
					if ( sprint2SortFields.contains(os.name())) {
						adjustmentSortFields.add(new LabelValueBean(os.getUserText(), os.name()));
					}
				}
			}
		}	
	}
	
	public static List<LabelValueBean> getOrderSortFieldSelects() {
		return orderSortFields;
	}
	
	public static List<LabelValueBean> getAdjustmentSortFieldSelects() {
		return adjustmentSortFields;
	}
	
	private static List<LabelValueBean> viewOrderSortFields = null;
	static{
		int enabled = 0;
		for ( OrderSearchCriteriaSortColumnEnum os : OrderSearchCriteriaSortColumnEnum.values() ) {
			if ( os.isEnabled() ) { enabled++; }
		}
			viewOrderSortFields = new ArrayList<LabelValueBean>(enabled + 1);
			for ( OrderSearchCriteriaSortColumnEnum os : OrderSearchCriteriaSortColumnEnum.values() ) {
				//make sure only one appropriate default sort is added
				if ( os.isEnabled() && os.name().startsWith("SORT_BY")) {
					if ( os.equals(SORT_BY_VIEW_DEFAULT) ) {
						viewOrderSortFields.add(new LabelValueBean(os.getUserText(), os.name()));
					} else {
					// NOTE - THIS LIMITS SORT FIELDS FOR SPRINT 2 & 3 - Essie 9/2/2010
						if ( sprint2SortFields.contains(os.name()) || sprint3SortFields.contains(os.name())) {
							viewOrderSortFields.add(new LabelValueBean(os.getUserText(), os.name()));
						}
					}
				}
			}			
		}
	
	public static List<LabelValueBean> getViewOrderSortFieldSelects() {
		
		return viewOrderSortFields;
	}

	public static OrderSearchCriteriaSortColumnEnum getEnumFor( String nameOrUserText ) {
		OrderSearchCriteriaSortColumnEnum en = getEnumForName(nameOrUserText);
		if ( en == null ) {
			en = getEnumForUserText(nameOrUserText);
		}
		return en;
	}

	public static OrderSearchCriteriaSortColumnEnum getEnumForUserText( String orderSearchCriteriaSortNameOrUserText ) {
		for ( OrderSearchCriteriaSortColumnEnum en : OrderSearchCriteriaSortColumnEnum.values() ) {
			if ( orderSearchCriteriaSortNameOrUserText.equalsIgnoreCase(en.getUserText()) ) {
				return en;
			} 
		}
		return null;
	}
	
	public static OrderSearchCriteriaSortColumnEnum getEnumForName( String orderSearchCriteriaSortNameOrUserText ) {
		for ( OrderSearchCriteriaSortColumnEnum en : OrderSearchCriteriaSortColumnEnum.values() ) {
			if ( orderSearchCriteriaSortNameOrUserText.equalsIgnoreCase(en.name())) {
				return en;
			} 
		}
		return null;
	}

}
