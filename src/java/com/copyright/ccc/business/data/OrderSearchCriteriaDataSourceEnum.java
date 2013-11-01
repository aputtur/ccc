package com.copyright.ccc.business.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum OrderSearchCriteriaDataSourceEnum {
	
	TF(1, "TF"), OMS(2, "OMS"), ALL(99, null);
	
	private int intVaue;
	private String sourceCd;
	
	private OrderSearchCriteriaDataSourceEnum(int intValue, String sourceCd) {
		this.intVaue = intValue;
		this.sourceCd = sourceCd;
	}
	
	private static List<LabelValueBean> dataSourceFields = null;
	static{
		dataSourceFields = new ArrayList<LabelValueBean>();
		for ( OrderSearchCriteriaDataSourceEnum ds : OrderSearchCriteriaDataSourceEnum.values() ) {
			dataSourceFields.add(new LabelValueBean(ds.getSourceCd(), ds.name()));
		}	
	}
	
	public static List<LabelValueBean> getDataSourceFieldSelects() {
	
		return dataSourceFields;
	}
	
	public static OrderSearchCriteriaDataSourceEnum getDataSourceEnumFor( String dataSource ) {
		for ( OrderSearchCriteriaDataSourceEnum ds : OrderSearchCriteriaDataSourceEnum.values() ) {
			if ( dataSource.equalsIgnoreCase(ds.name())) {
				return ds;
			} else if ( dataSource.equalsIgnoreCase(ds.getSourceCd()) ) {
				return ds;
			} 
		}
		return null;
	}
	
	public static OrderSearchCriteriaDataSourceEnum getDataSourceEnumFor( int intValue ) {
		for ( OrderSearchCriteriaDataSourceEnum ds : OrderSearchCriteriaDataSourceEnum.values() ) {
			if ( intValue == ds.getIntVaue() ) {
				return ds;
			} 
		}
		return null;
	}


	public int getIntVaue() {
		return intVaue;
	}

	public void setIntVaue(int intVaue) {
		this.intVaue = intVaue;
	}

	public String getSourceCd() {
		return sourceCd;
	}

	public void setSourceCd(String sourceCd) {
		this.sourceCd = sourceCd;
	}
}
