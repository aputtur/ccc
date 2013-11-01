package com.copyright.ccc.web.actions.ordermgmt.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.services.ProductEnum;

public enum OrderRequiredFieldsEnum {
		
	APS_DETAIL_PAGE_RANGES("PAGE_RANGES",ProductEnum.APS,true,-1,-1,-1),
	APS_DETAIL_LCN_DTL_REF_NUM("LCN_DTL_REF_NUM (your reference)",ProductEnum.APS,false,38,-1,-1),
	APS_DETAIL_CHAPTER_ARTICLE("CHAPTER_ARTICLE",ProductEnum.APS,false,26,-1,-1),
	APS_DETAIL_AUTHOR("AUTHOR",ProductEnum.APS,false,17,-1,-1),
	APS_DETAIL_EDITOR("EDITOR",ProductEnum.APS,false,18,-1,-1),
	APS_DETAIL_EDITION("EDITION",ProductEnum.APS,false,22,-1,-1),
	APS_DETAIL_TRANSLATOR("TRANSLATOR",ProductEnum.APS,false,19,-1,-1),
	APS_DETAIL_DATE_OF_ISSUE("DATE_OF_ISSUE",ProductEnum.APS,false,195,-1,-1),
	APS_DETAIL_PUBLICATION_DATE("PUBLICATION_DATE",ProductEnum.APS,true,20,-1,-1),
	APS_DETAIL_VOLUME("VOLUME",ProductEnum.APS,false,21,-1,-1),
	APS_DETAIL_NUM_COPIES("NUM_COPIES",ProductEnum.APS,true,-1,114,-1),
	APS_DETAIL_NUM_PAGES("NUM_PAGES",ProductEnum.APS,true,-1,115,-1),
	APS_HEADER_START_OF_USE_DTM("START_OF_USE_DTM",ProductEnum.APS,false,-1,-1,1),
	APS_HEADER_ORGANIZATION("ORGANIZATION (univeristy)",ProductEnum.APS,false,-1,-1,2),
	APS_HEADER_BUNDLE_NAME("BUNDLE_NAME (course name)",ProductEnum.APS,false,-1,-1,3),
	APS_HEADER_BUNDLE_IDENTIFIER("BUNDLE_IDENTIFIER (course num)",ProductEnum.APS,false,-1,-1,4),
	APS_HEADER_INSTRUCTOR("INSTRUCTOR",ProductEnum.APS,false,-1,-1,5),
	APS_HEADER_LCN_HDR_REF_NUM("LCN_HDR_REF_NUM (your doc ref)",ProductEnum.APS,false,-1,-1,-1),
	APS_HEADER_LCN_ACCT_REF_NUM("LCN_ACCT_REF_NUM",ProductEnum.APS,false,-1,-1,-1),
	APS_HEADER_ORDER_ENTERED_BY("ORDER_ENTERED_BY",ProductEnum.APS,false,-1,-1,-1),
	APS_HEADER_COMMENTS("COMMENTS",ProductEnum.APS,false,-1,-1,19),
	APS_HEADER_EST_NUM_STUDENTS("EST_NUM_STUDENTS",ProductEnum.APS,false,-1,-1,6),
	DPS_HEADER_CHAPTER_ARTICLE("CHAPTER_ARTICLE",ProductEnum.DPS,false,408,-1,-1),
	DPS_HEADER_AUTHOR("AUTHOR",ProductEnum.DPS,false,405,-1,-1),
	DPS_HEADER_PUBLICATION_DATE("PUBLICATION_DATE",ProductEnum.DPS,true,404,-1,-1),
	DPS_DETAIL_DATE_TO_BE_USED("DATE_TO_BE_USED",ProductEnum.DPS,true,407,-1,-1),
	DPS_DETAIL_WEB_ADDRESS("WEB_ADDRESS",ProductEnum.DPS,false,409,-1,-1),
	DPS_DETAIL_DURATION("DURATION",ProductEnum.DPS,false,-1,1286,-1),
	DPS_DETAIL_NUM_RECIPIENTS("NUM_RECIPIENTS",ProductEnum.DPS,false,-1,1291,-1),
	DPS_DETAIL_LCN_DTL_REF_NUM("LCN_DTL_REF_NUM (your reference)",ProductEnum.DPS,false,424,-1,-1),
	DPS_HEADER_LCN_HDR_REF_NUM("LCN_HDR_REF_NUM (your doc ref)",ProductEnum.DPS,false,-1,-1,-1),
	DPS_HEADER_ORDER_ENTERED_BY("ORDER_ENTERED_BY",ProductEnum.DPS,false,-1,-1,-1),
	ECCS_DETAIL_CHAPTER_ARTICLE("CHAPTER_ARTICLE",ProductEnum.ECC,false,201,-1,-1),
	ECCS_DETAIL_AUTHOR("AUTHOR",ProductEnum.ECC,true,202,-1,-1),
	ECCS_DETAIL_EDITOR("EDITOR",ProductEnum.ECC,false,203,-1,-1),
	ECCS_DETAIL_EDITION("EDITION",ProductEnum.ECC,false,207,-1,-1),
	ECCS_DETAIL_TRANSLATOR("TRANSLATOR",ProductEnum.ECC,false,204,-1,-1),
	ECCS_DETAIL_DATE_OF_ISSUE("DATE_OF_ISSUE",ProductEnum.ECC,false,253,-1,-1),
	ECCS_DETAIL_PUBLICATION_DATE("PUBLICATION_DATE",ProductEnum.ECC,true,205,-1,-1),
	ECCS_DETAIL_VOLUME("VOLUME",ProductEnum.ECC,false,206,-1,-1),
	ECCS_DETAIL_LCN_DTL_REF_NUM("LCN_DTL_REF_NUM",ProductEnum.ECC,false,219,-1,-1),
	ECCS_DETAIL_NUM_STUDENTS("NUM_STUDENTS",ProductEnum.ECC,true,-1,918,-1),
	ECCS_DETAIL_NUM_PAGES("NUM_PAGES",ProductEnum.ECC,true,-1,115,-1),
	ECCS_DETAIL_PAGE_RANGES("PAGE_RANGES",ProductEnum.ECC,false,-1,-1,-1),
	ECCS_HEADER_START_OF_USE_DTM("START_OF_USE_DTM",ProductEnum.ECC,true,-1,-1,170),
	ECCS_HEADER_ORGANIZATION("ORGANIZATION (univeristy)",ProductEnum.ECC,true,-1,-1,171),
	ECCS_HEADER_BUNDLE_NAME("BUNDLE_NAME (course name)",ProductEnum.ECC,true,-1,-1,172),
	ECCS_HEADER_BUNDLE_IDENTIFIER("BUNDLE_IDENTIFIER (course num)",ProductEnum.ECC,false,-1,-1,173),
	ECCS_HEADER_INSTRUCTOR("INSTRUCTOR",ProductEnum.ECC,false,-1,-1,174),
	ECCS_HEADER_COMMENTS("COMMENTS",ProductEnum.ECC,false,-1,-1,176),
	ECCS_HEADER_LCN_HDR_REF_NUM("LCN_HDR_REF_NUM (your doc ref)",ProductEnum.ECC,false,-1,-1,-1),
	ECCS_HEADER_LCN_ACCT_REF_NUM("LCN_ACCT_REF_NUM",ProductEnum.ECC,false,-1,-1,-1),
	ECCS_HEADER_ORDER_ENTERED_BY("ORDER_ENTERED_BY",ProductEnum.ECC,false,-1,-1,-1),
	RLS_DETAIL_CHAPTER_ARTICLE("CHAPTER_ARTICLE",ProductEnum.RLS,false,314,-1,-1),
	RLS_DETAIL_AUTHOR("AUTHOR",ProductEnum.RLS,true,315,-1,-1),
	RLS_DETAIL_TRANSLATOR("TRANSLATOR",ProductEnum.RLS,false,334,-1,-1),
	RLS_DETAIL_PUBLICATION_DATE("PUBLICATION_DATE",ProductEnum.RLS,true,313,-1,-1),
	RLS_DETAIL_VOLUME("VOLUME",ProductEnum.RLS,false,325,-1,-1),
	RLS_DETAIL_FROM_PAGES("FROM_PAGES",ProductEnum.RLS,false,306,-1,-1),
	RLS_DETAIL_TO_PAGES("TO_PAGES",ProductEnum.RLS,false,307,-1,-1),
	RLS_DETAIL_NUM_PAGES("NUM_PAGES",ProductEnum.RLS,true,-1,-1,115),
	RLS_DETAIL_SECTION("SECTION",ProductEnum.RLS,false,322,-1,-1),
	RLS_DETAIL_TEXT_PORTION("TEXT_PORTION",ProductEnum.RLS,false,314,-1,-1),
	RLS_DETAIL_NON_TEXT_PORTION("NON_TEXT_PORTION",ProductEnum.RLS,false,344,-1,-1),
	RLS_DETAIL_TRANSLATED("TRANSLATED",ProductEnum.RLS,true,334,-1,-1),
	RLS_DETAIL_LANGUAGE("LANGUAGE",ProductEnum.RLS,false,337,-1,-1),
	RLS_DETAIL_CIRCULATION("CIRCULATION",ProductEnum.RLS,true,-1,996,-1),
	RLS_DETAIL_FULL_ARTICLE("FULL_ARTICLE",ProductEnum.RLS,false,-1,998,-1),
	RLS_DETAIL_NUM_CARTOONS("NUM_CARTOONS",ProductEnum.RLS,false,-1,1230,-1),
	RLS_DETAIL_NUM_CHARTS("NUM_CHARTS",ProductEnum.RLS,false,-1,988,-1),
	RLS_DETAIL_NUM_EXCERPTS("NUM_EXCERPTS",ProductEnum.RLS,false,-1,992,-1),
	RLS_DETAIL_NUM_FIGURES("NUM_FIGURES",ProductEnum.RLS,false,-1,989,-1),
	RLS_DETAIL_NUM_GRAPHS("NUM_GRAPHS",ProductEnum.RLS,false,-1,1000,-1),
	RLS_DETAIL_NUM_ILLUSTRATIONS("NUM_ILLUSTRATIONS",ProductEnum.RLS,false,-1,990,-1),
	RLS_DETAIL_NUM_LOGOS("NUM_LOGOS",ProductEnum.RLS,false,-1,994,-1),
	RLS_DETAIL_NUM_PHOTOS("NUM_PHOTOS",ProductEnum.RLS,false,-1,993,-1),
	RLS_DETAIL_NUM_QUOTES("NUM_QUOTES",ProductEnum.RLS,false,-1,991,-1),
	RLS_DETAIL_ORIG_AUTHOR("ORIG_AUTHOR",ProductEnum.RLS,true,-1,995,-1),
	RLS_DETAIL_RLS_PAGES("RLS_PAGES",ProductEnum.RLS,false,-1,999,-1),
	RLS_DETAIL_PAGE_RANGES("PAGE_RANGES",ProductEnum.RLS,true,-1,-1,-1),
	RLS_DETAIL_REPUB_PUB("REPUB_PUB",ProductEnum.RLS,true,-1,-1,245),
	RLS_DETAIL_REPUB_FORMAT("REPUB_FORMAT",ProductEnum.RLS,true,-1,-1,-1),
	RLS_DETAIL_REPUB_TITLE("REPUB_TITLE",ProductEnum.RLS,true,-1,-1,242),
	RLS_DETAIL_REPUB_IN_VOL_ED("REPUB_IN_VOL_ED",ProductEnum.RLS,false,-1,-1,243),
	RLS_DETAIL_START_OF_USE_DTM("START_OF_USE_DTM",ProductEnum.RLS,true,-1,-1,247),
	RLS_DETAIL_FOR_PROFIT("FOR_PROFIT (org status)",ProductEnum.RLS,true,-1,-1,228),
	TRS_DETAIL_CHAPTER_ARTICLE("CHAPTER_ARTICLE",ProductEnum.TRS,false,255,-1,-1),
	TRS_DETAIL_AUTHOR("AUTHOR",ProductEnum.TRS,false,180,-1,-1),
	TRS_DETAIL_EDITOR("EDITOR",ProductEnum.TRS,false,183,-1,-1),
	TRS_DETAIL_EDITION("EDITION",ProductEnum.TRS,false,185,-1,-1),
	TRS_DETAIL_TRANSLATOR("TRANSLATOR",ProductEnum.TRS,false,184,-1,-1),
	TRS_DETAIL_DATE_OF_ISSUE("DATE_OF_ISSUE",ProductEnum.TRS,false,186,-1,-1),
	TRS_DETAIL_PUBLICATION_DATE("PUBLICATION_DATE",ProductEnum.TRS,true,31,-1,-1),
	TRS_DETAIL_VOLUME("VOLUME",ProductEnum.TRS,false,181,-1,-1),
	TRS_DETAIL_NUM_COPIES("NUM_COPIES",ProductEnum.TRS,true,-1,114,-1),
	TRS_DETAIL_NUM_PAGES("NUM_PAGES",ProductEnum.TRS,true,-1,115,-1),
	TRS_DETAIL_LCN_DTL_REF_NUM("LCN_DTL_REF_NUM (your reference)",ProductEnum.TRS,false,37,-1,-1);

	private String fieldDescription;
	private ProductEnum productType;
	private boolean required;
	private int pdpInst;
	private int ffpInst;
	private int popInst;
	
	private OrderRequiredFieldsEnum(String fieldDescription, ProductEnum productType, boolean required, int pdpInst, int ffpInst, int popInst) {
		setFieldDescription(fieldDescription);
		setProductType(productType);
		setRequired(required);
		setPdpInst(pdpInst);
		setFfpInst(ffpInst);
		setPopInst(popInst);
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	private void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public ProductEnum getProductType() {
		return productType;
	}

	private void setProductType(ProductEnum productType) {
		this.productType = productType;
	}

	public boolean isRequired() {
		return required;
	}

	private void setRequired(boolean required) {
		this.required = required;
	}

	public int getPdpInst() {
		return pdpInst;
	}

	private void setPdpInst(int pdpInst) {
		this.pdpInst = pdpInst;
	}

	public int getFfpInst() {
		return ffpInst;
	}

	private void setFfpInst(int ffpInst) {
		this.ffpInst = ffpInst;
	}

	public int getPopInst() {
		return popInst;
	}

	private void setPopInst(int popInst) {
		this.popInst = popInst;
	}
	
	public boolean hasPdpInst() {
		return getPdpInst() > 0;
	}
	
	public boolean hasPopInst() {
		return getPopInst() > 0;
	}
	
	public boolean hasFfpInst() {
		return getFfpInst() > 0;
	}
	
	public boolean isRLS() {
		return getProductType().equals(ProductEnum.RLS);
	}
	public boolean isTRS() {
		return getProductType().equals(ProductEnum.TRS);
	}
	public boolean isECCS() {
		return getProductType().equals(ProductEnum.ECC);
	}
	public boolean isAPS() {
		return getProductType().equals(ProductEnum.DPS);
	}
	public boolean isDPS() {
		return getProductType().equals(ProductEnum.APS);
	}

	private boolean isProductType(ProductEnum productType ) {
		return getProductType().equals(productType);
	}
	
	public boolean isHeader() {
		return name().contains(HEADER_TAG);
	}
	
	public boolean isDetail() {
		return name().contains(DETAIL_TAG);
	}
	
	public static List<OrderRequiredFieldsEnum> requiredDetailFieldsFor( ProductEnum forProductType ) {
		return requiredFieldsFor( forProductType, FOR_DETAIL );
	}
	
	public static List<OrderRequiredFieldsEnum> requiredHeaderFieldsFor( ProductEnum forProductType ) {
		return requiredFieldsFor( forProductType, FOR_HEADER );
	}
	
	public static List<OrderRequiredFieldsEnum> requiredDetailFieldsFor( String forProductType ) {
		return requiredFieldsFor( forProductType, FOR_DETAIL );
	}
	
	public static List<OrderRequiredFieldsEnum> requiredHeaderFieldsFor( String forProductType ) {
		return requiredFieldsFor( forProductType, FOR_HEADER );
	}
	
	public static List<OrderRequiredFieldsEnum> requiredFieldsFor( ProductEnum forProductType, boolean isForHeader ) {
		List<OrderRequiredFieldsEnum> lst = new ArrayList<OrderRequiredFieldsEnum>();
		for ( OrderRequiredFieldsEnum odr : OrderRequiredFieldsEnum.values() ) {
			if ( isForHeader && odr.isHeader() ) {
				if ( odr.isProductType(forProductType) && odr.isRequired() ) {
					lst.add(odr);
				}
			} else if ( !isForHeader && odr.isDetail() ) {
				if ( odr.isProductType(forProductType) && odr.isRequired() ) {
					lst.add(odr);
				}				
			}
		}
		return lst;
	}
	
	public static List<OrderRequiredFieldsEnum> requiredFieldsFor( String forProductType, boolean isForHeader ) {
		if ( StringUtils.isEmpty(forProductType) ) {
			throw new IllegalArgumentException("forProductType is missing");
		}
		ProductEnum pe = ProductEnum.valueOf(forProductType);
		if ( pe == null ) {
			throw new IllegalArgumentException("productType = " + forProductType + " is not a ProductEnum type");
		}
		return requiredFieldsFor( pe, isForHeader );
	}
	
	public static final boolean FOR_HEADER = true;
	public static final boolean FOR_DETAIL = false;
	private static String  HEADER_TAG = "_HEADER_";
	private static String  DETAIL_TAG = "_DETAIL_";
	
	public static List<OrderRequiredFieldsEnum> requiredFieldsForDetailRLS() {
		return requiredDetailFieldsFor( ProductEnum.RLS );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForHeaderRLS() {
		return requiredHeaderFieldsFor( ProductEnum.RLS );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForDetailTRS() {
		return requiredDetailFieldsFor( ProductEnum.TRS );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForHeaderTRS() {
		return requiredHeaderFieldsFor( ProductEnum.TRS );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForDetailECCS() {
		return requiredDetailFieldsFor( ProductEnum.ECC );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForHeaderECCS() {
		return requiredHeaderFieldsFor( ProductEnum.ECC );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForDetailDPS() {
		return requiredDetailFieldsFor( ProductEnum.DPS );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForHeaderDPS() {
		return requiredHeaderFieldsFor( ProductEnum.DPS );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForDetailAPS() {
		return requiredDetailFieldsFor( ProductEnum.APS );
	}
	public static List<OrderRequiredFieldsEnum> requiredFieldsForHeaderAPS() {
		return requiredHeaderFieldsFor( ProductEnum.APS );
	}
	
	public static OrderRequiredFieldsEnum detailEnumFor( ProductEnum productType, String fieldName) {
		return enumFor( productType, fieldName, FOR_DETAIL);
	}
	
	public static OrderRequiredFieldsEnum headerEnumFor( ProductEnum productType, String fieldName) {
		return enumFor( productType, fieldName, FOR_HEADER);
	}
	
	public static OrderRequiredFieldsEnum detailEnumFor( String productType, String fieldName) {
		ProductEnum pe = ProductEnum.valueOf(productType);
		if ( pe == null ) {
			throw new IllegalArgumentException("ProductEnum not defined for "+ productType);
		}
		return enumFor( pe, fieldName, FOR_DETAIL);
	}
	
	public static OrderRequiredFieldsEnum headerEnumFor( String productType, String fieldName) {
		ProductEnum pe = ProductEnum.valueOf(productType);
		if ( pe == null ) {
			throw new IllegalArgumentException("ProductEnum not defined for "+ productType);
		}
		return enumFor( pe, fieldName, FOR_HEADER);
	}
	
	public static OrderRequiredFieldsEnum enumFor( ProductEnum productType, String fieldName, boolean isForHeader) {
		if ( productType == null ) {
			throw new IllegalArgumentException("Missing procuctType");
		}
		if ( StringUtils.isEmpty(fieldName) ) {
			throw new IllegalArgumentException("Missing fieldName");
		}
		StringBuffer name = new StringBuffer();
		
		name.append(productType.name());
		
		if ( isForHeader ) {
			name.append(HEADER_TAG);
		} else {
			name.append(DETAIL_TAG);
		}
		
		name.append(fieldName.toUpperCase());
		
		OrderRequiredFieldsEnum odr = OrderRequiredFieldsEnum.valueOf(name.toString());
		
		if ( odr != null ) {
			return odr;
		}
		
		throw new IllegalArgumentException("Enum entry for "+ name +" is undefined");
	}

	
}
