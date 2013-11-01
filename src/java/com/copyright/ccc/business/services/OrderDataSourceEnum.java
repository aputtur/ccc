
package com.copyright.ccc.business.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum OrderDataSourceEnum {
	
	TF(Long.valueOf(1), "TF"),
	OMS(Long.valueOf(2), "OMS"),
	ALL(null, "ALL");

	private Long orderDataSourceId;
	private String orderDataSourceLabel;

	
	private OrderDataSourceEnum(Long orderDataSourceId, String orderDataSourceLabel)
	{
		this.orderDataSourceId = orderDataSourceId;
		this.orderDataSourceLabel = orderDataSourceLabel;		
	}
	
	public static OrderDataSourceEnum getEnumForName( String name ) {
		for ( OrderDataSourceEnum en : OrderDataSourceEnum.values() ) {
			if ( name.equalsIgnoreCase(en.name())) {
				return en;
			} 
		}
		return null;
	}
	
//	private static List<LabelValueBean> orderItemAvailabilityCodes = null;
	
	public static List<LabelValueBean> getOrderDataSourceCodes() {
		
		List<LabelValueBean> orderDataSourceCodes = new ArrayList<LabelValueBean>();
		for ( OrderDataSourceEnum ods : OrderDataSourceEnum.values() ) {
			LabelValueBean labelValueBean = new LabelValueBean();
			labelValueBean.setLabel(ods.getOrderSourceLabel());
			labelValueBean.setValue(ods.name());
			orderDataSourceCodes.add(labelValueBean);
		}
		return orderDataSourceCodes;
	}


	
	public String getOrderSourceLabel() {
		return orderDataSourceLabel;
	}

	public Long getOrderDataSourceId() {
		return orderDataSourceId;
	}

	public void setOrderDataSourceId(Long orderDataSourceId) {
		this.orderDataSourceId = orderDataSourceId;
	}

	public String getOrderDataSourceLabel() {
		return orderDataSourceLabel;
	}

	public void setOrderDataSourceLabel(String orderDataSourceLabel) {
		this.orderDataSourceLabel = orderDataSourceLabel;
	}

}
