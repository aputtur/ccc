package com.copyright.ccc.business.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum OrderSearchCriteriaSortDirectionEnum {
	
	ASC("ASC", 0, "Ascending"), DESC("DESC", 1, "Descending");
	
	private String sqlValue;
	private int intVaue;
	private String userText;
	
	private OrderSearchCriteriaSortDirectionEnum(String sqlValue, int intValue, String userText) {
		this.sqlValue = sqlValue;
		this.intVaue = intValue;
		this.userText = userText;
	}
	
	private static List<LabelValueBean> sortDirectionFields = null;
	static{
		sortDirectionFields = new ArrayList<LabelValueBean>();
		for ( OrderSearchCriteriaSortDirectionEnum os : OrderSearchCriteriaSortDirectionEnum.values() ) {
			sortDirectionFields.add(new LabelValueBean(os.getUserText(), os.name()));
		}
	}
	
	public static List<LabelValueBean> getSortDirectionFieldSelects() {
		return sortDirectionFields;
	}
	
	public static OrderSearchCriteriaSortDirectionEnum getSortDirectionEnumFor( String direction ) {
		for ( OrderSearchCriteriaSortDirectionEnum en : OrderSearchCriteriaSortDirectionEnum.values() ) {
			if ( direction.equalsIgnoreCase(en.name())) {
				return en;
			} else if ( direction.equalsIgnoreCase(en.getUserText()) ) {
				return en;
			} 
		}
		return null;
	}
	
	public static OrderSearchCriteriaSortDirectionEnum getSortDirectionEnumFor( int intValue ) {
		for ( OrderSearchCriteriaSortDirectionEnum en : OrderSearchCriteriaSortDirectionEnum.values() ) {
			if ( intValue == en.getIntVaue() ) {
				return en;
			} 
		}
		return null;
	}

	public String getSqlValue() {
		return sqlValue;
	}

	public void setSqlValue(String sqlValue) {
		this.sqlValue = sqlValue;
	}

	public int getIntVaue() {
		return intVaue;
	}

	public void setIntVaue(int intVaue) {
		this.intVaue = intVaue;
	}

	public String getUserText() {
		return userText;
	}

	public void setUserText(String userText) {
		this.userText = userText;
	}
}
