
package com.copyright.ccc.business.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public enum ProductEnum {
	
	DPS(36L, "Digital Permissions Service", 5L, false ),
	TRS(1L, "Transactional Reporting Service", 4L, false),
	APS(2L, "Academic Permissions Service", 2L, true),
	ECC(12L, "Electronic Course Content Service", 1L, true ),
	RLS(44L, "Republication License Service", 6L, false ),
	RL (100L, "Rightslink", null, false ),
	RLR(200L, "Reprint", 7L, false );
	
	
	Long productSourceKey;
	String productName;
	Long categoryId;
	boolean isAcademic;
	
	private ProductEnum(Long productSourceKey, String productName, Long categoryId, boolean isAcad) {
		this.productSourceKey = productSourceKey;
		this.productName = productName;
		this.categoryId = categoryId;
		this.isAcademic = isAcad;
	}
	

	public static ProductEnum getEnumForName( String name ) {
		for ( ProductEnum pe : ProductEnum.values() ) {
			if ( name.equalsIgnoreCase(pe.name())) {
				return pe;
			} 
		}
		return null;
	}
	
	public static ProductEnum getEnumForProductSourceKey( Long productSourceKey ) {
		for ( ProductEnum pe : ProductEnum.values() ) {
			if ( productSourceKey.equals(pe.productSourceKey)) {
				return pe;
			} 
		}
		return null;
	}
	
	public static List<LabelValueBean> getProductCodes() {
		
		List<LabelValueBean> productCodes = new ArrayList<LabelValueBean>();
		for ( ProductEnum pe : ProductEnum.values() ) {
			LabelValueBean labelValueBean = new LabelValueBean();
			labelValueBean.setLabel(pe.name());
			labelValueBean.setValue(pe.getProductSourceKey().toString());
			productCodes.add(labelValueBean);
		}
		return productCodes;
	}
	
	public static List<LabelValueBean> getAcademicProductCodes() {
		
		List<LabelValueBean> productCodes = new ArrayList<LabelValueBean>();
		for ( ProductEnum pe : ProductEnum.values() ) {
			if (pe.getIsAcademic())
			{
				LabelValueBean labelValueBean = new LabelValueBean();
				labelValueBean.setLabel(pe.name());
				labelValueBean.setValue(pe.getProductSourceKey().toString());
				productCodes.add(labelValueBean);
			}
		}
		return productCodes;
	}
	
	public static List<LabelValueBean> getNonAcademicProductCodes() {
		
		List<LabelValueBean> productCodes = new ArrayList<LabelValueBean>();
		for ( ProductEnum pe : ProductEnum.values() ) {
			if (!pe.getIsAcademic())
			{
				LabelValueBean labelValueBean = new LabelValueBean();
				labelValueBean.setLabel(pe.name());
				labelValueBean.setValue(pe.getProductSourceKey().toString());
				productCodes.add(labelValueBean);
			}
		}
		return productCodes;
	}

	
	public Long getProductSourceKey() {
		return productSourceKey;
	}

	public String getProductName() {
		return productName;
	}

	public Long getCategoryId() {
		return categoryId;
	}
	
	public boolean getIsAcademic() {
		return isAcademic;
	}
}
