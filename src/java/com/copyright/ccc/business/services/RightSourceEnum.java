package com.copyright.ccc.business.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum RightSourceEnum {
	
	TF(Long.valueOf(1), "TF"),
	RL(Long.valueOf(2), "RL");

	private Long rightSourceId;
	private String rightSourceCd;
	
	private RightSourceEnum(Long rightSourceId, String rightSourceCd)
	{
		this.rightSourceId = rightSourceId;
		this.rightSourceCd = rightSourceCd;		
	}
	
	public static RightSourceEnum getEnumForName( String name ) {
		for ( RightSourceEnum en : RightSourceEnum.values() ) {
			if ( name.equalsIgnoreCase(en.name())) {
				return en;
			} 
		}
		return null;
	}
	
//	private static List<LabelValueBean> orderItemAvailabilityCodes = null;
	
	public static List<LabelValueBean> getRightSourceCodes() {
		
		List<LabelValueBean> orderDataSourceCodes = new ArrayList<LabelValueBean>();
		for ( RightSourceEnum rs : RightSourceEnum.values() ) {
			LabelValueBean labelValueBean = new LabelValueBean();
			labelValueBean.setLabel(rs.getRightSourceCd());
			labelValueBean.setValue(rs.name());
			orderDataSourceCodes.add(labelValueBean);
		}
		return orderDataSourceCodes;
	}


	
	public String getRightSourceCd() {
		return rightSourceCd;
	}

	public Long getRightSourceId() {
		return rightSourceId;
	}

	public void setRightSourceId(Long rightSourceId) {
		this.rightSourceId = rightSourceId;
	}


	public void setOrderDataSourceLabel(String rightSourceCd) {
		this.rightSourceCd = rightSourceCd;
	}

}
