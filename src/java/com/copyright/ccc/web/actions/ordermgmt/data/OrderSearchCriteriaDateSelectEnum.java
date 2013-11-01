package com.copyright.ccc.web.actions.ordermgmt.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum OrderSearchCriteriaDateSelectEnum {
	
	ORDER_DATE(true,"Order Date"),
	INVOICE_DATE(true,"Invoice Date"),
	START_OF_TERM(true,"Start of Term Date"),
	LAST_UPDATE(true,"Last Update Date");
	
	private String userText;
	private boolean enabled;
	
	private OrderSearchCriteriaDateSelectEnum(boolean enabled, String userText) {
		this.userText = userText;
		this.enabled = enabled;
	}
	
	private  static List<LabelValueBean> searchSelectDateFields = null;
	static{
			searchSelectDateFields = new ArrayList<LabelValueBean>();
			for ( OrderSearchCriteriaDateSelectEnum os : OrderSearchCriteriaDateSelectEnum.values() ) {
				if ( os.isEnabled() ) {
					searchSelectDateFields.add(new LabelValueBean(os.getUserText(), os.name()));
				}
			}			
		}
	
	public static List<LabelValueBean> getSearchSelectDateFields() {
	
		return searchSelectDateFields;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static OrderSearchCriteriaDateSelectEnum getSearchDateEnumFor( String choice ) {
		for ( OrderSearchCriteriaDateSelectEnum en : OrderSearchCriteriaDateSelectEnum.values() ) {
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
