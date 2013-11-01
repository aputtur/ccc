package com.copyright.ccc.business.services;

//import java.util.ArrayList;
//import java.util.List;

//import org.apache.struts.util.LabelValueBean;

public enum PubTypeEnum {
	
	ALL("0", "all"),
	Blog("1", "Blog"),
    BOOK("2", "Book"),
    EBOOK("3", "e-Book"),
    EJOURNAL("4", "e-Journal"),
    JOURNAL("5", "Journal"),
    MONO("6", "Monographic Series"),
    NEWSLETTER("7", "Newsletter"),
    NEWSPAPER("8", "Newspaper"),
    OTHER("9", "Other"),
    WEBSITE("10", "Website");
	
	
	String pubTypeCode;
	String pubTypeName;
	
	
	private PubTypeEnum(String pubTypeKey, String pubTypeName) {
		this.pubTypeCode = pubTypeKey;
		this.pubTypeName = pubTypeName;
	}
	
	public static String getPubTypeCodeForName( String name ) {
		for ( PubTypeEnum pte : PubTypeEnum.values() ) {
			if ( name.equalsIgnoreCase(pte.getPubTypeName())) {
				return pte.pubTypeCode;
			} 
		}
		return null;
	}
	
	public String getPubTypeName() {
		return pubTypeName;
	}
	

	/* public static ProductEnum getEnumForName( String name ) {
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
	} */
	
	
}
