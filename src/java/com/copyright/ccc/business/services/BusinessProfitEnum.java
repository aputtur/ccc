package com.copyright.ccc.business.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.web.transaction.RepublicationConstants;

public enum BusinessProfitEnum {
	
	BUSINESS_FOR_PROFIT(RepublicationConstants.BUSINESS_FOR_PROFIT,"Yes"),
	BUSINESS_NON_FOR_PROFIT(RepublicationConstants.BUSINESS_NON_FOR_PROFIT,"No");

	private String dbCode;
	private String userText;
	
	private BusinessProfitEnum(String dbCode, String userText)
	{
		this.dbCode = dbCode;
		this.userText = userText;		
	}
	
	public static BusinessProfitEnum getEnumForName( String name ) {
		for ( BusinessProfitEnum en : BusinessProfitEnum.values() ) {
			if ( name.equalsIgnoreCase(en.name())) {
				return en;
			} 
		}
		return null;
	}
		
	public static List<LabelValueBean> getBusinessProfits() {
		
		List<LabelValueBean> businessProfitCodes = new ArrayList<LabelValueBean>();
		for ( BusinessProfitEnum rs : BusinessProfitEnum.values() ) {
			businessProfitCodes.add(new LabelValueBean(rs.getUserText(),rs.getDbCode()));
		}
		return businessProfitCodes;
	}

	public String getDbCode() {
		return dbCode;
	}

	public String getUserText() {
		return userText;
	}
}
