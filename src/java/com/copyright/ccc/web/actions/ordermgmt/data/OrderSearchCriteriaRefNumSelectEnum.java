package com.copyright.ccc.web.actions.ordermgmt.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum OrderSearchCriteriaRefNumSelectEnum {
	
	DOC_REF("Doc Ref"),
	//ORDER_REF("Order Ref"),
	ORDER_DET_REF("Line Item Ref"),
	ACCT_REF("Accounting Ref"),
	PO_NUM("PO Number");
	
	private String userText;
	//private boolean enabled;
	
	private OrderSearchCriteriaRefNumSelectEnum(String userText) {
		this.userText = userText;
		//this.enabled = enabled;
	}
	
	private static List<LabelValueBean> searchSelectRefNumFields = null;
	static{
		searchSelectRefNumFields = new ArrayList<LabelValueBean>();
		for ( OrderSearchCriteriaRefNumSelectEnum os : OrderSearchCriteriaRefNumSelectEnum.values() ) {
			//if ( os.isEnabled() ) {
				searchSelectRefNumFields.add(new LabelValueBean(os.getUserText(), os.name()));
			//}
		}	
	}
	
	public static List<LabelValueBean> getSearchSelectRefNumFields() {
		return searchSelectRefNumFields;
	}
	
	/*public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}*/

	public static OrderSearchCriteriaRefNumSelectEnum getSearchRefNumEnumFor( String choice ) {
		for ( OrderSearchCriteriaRefNumSelectEnum en : OrderSearchCriteriaRefNumSelectEnum.values() ) {
			if ( choice.equalsIgnoreCase(en.name())) {
				return en;
			} else if ( choice.equalsIgnoreCase(en.getUserText()) ) {
				return en;
			} 
		}
		return null;
	}
	
	public String getUserText() {
		return userText;
	}

	public void setUserText(String userText) {
		this.userText = userText;
	}


}
