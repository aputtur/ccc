package com.copyright.ccc.business.services;

public enum ItemParmEnum {
	
	NUM_COPIES("Copies", ItemConstants.INTEGER, 0, 0),
	NUM_PAGES("Pages", ItemConstants.INTEGER, 0, 0),
	NUM_STUDENTS("Students", ItemConstants.INTEGER, 0, 0),
	NUM_RECIPIENTS("Recipients", ItemConstants.INTEGER, 0, 0),
	PUBLICATION_YEAR_OF_USE("Year of Publication Licensed", ItemConstants.INTEGER, 0, 0),
	DATE_OF_PUBLICATION_USED("Date of Publication Licened", ItemConstants.DATE, 0, 0),
	CIRCULATION_DISTRIBUTION("Circulation/Distribution", ItemConstants.INTEGER, 0, 0),
	BUSINESS("Business Type", ItemConstants.STRING, 0, 0),
	FOR_PROFIT("Business Type for Profit", ItemConstants.BOOLEAN, 0, 0),
	TYPE_OF_CONTENT("Type of Content", ItemConstants.STRING, 0, 0),
	IS_AUTHOR("Is Author", ItemConstants.BOOLEAN, 0, 0),
	REPUBLICATION_DATE("Republication Date", ItemConstants.DATE, 0, 0),
//	CONTENTS_PUBLICATION_DATE("Contents Publication Date", ItemConstants.DATE, 0, 0),
	DATE_OF_USE("Date of Use", ItemConstants.DATE, 0, 0),
	DURATION("Duration", ItemConstants.INTEGER, 0, 0),
	DURATION_CD("Duration Code", ItemConstants.STRING, 0, 0),
	WEB_ADDRESS("Web Address", ItemConstants.STRING, 0, 0),
	CHAPTER_ARTICLE("Chapter/Article", ItemConstants.STRING, 0, 0),
	CUSTOM_AUTHOR("Author", ItemConstants.STRING, 0, 0),
	CUSTOM_VOLUME("Volume", ItemConstants.STRING, 0, 0),
	CUSTOM_EDITION("Edition", ItemConstants.STRING, 0, 0),
	PAGE_RANGE("Page range", ItemConstants.STRING, 0, 0),
	REPUBLISHING_ORGANIZATION("Repub Organization", ItemConstants.STRING, 0, 0),
	WORKID("WORKID", ItemConstants.STRING, 0, 0),
	WHICHWORK("WHICHWORK", ItemConstants.STRING, 0, 0),
	NEW_PUBLICATION_TITLE("Repub Publication", ItemConstants.STRING, 0, 0),
	TRANSLATION_LANGUAGE("Translation Language", ItemConstants.STRING, 0, 0),
	IS_TRANSLATED("Translating", ItemConstants.BOOLEAN, 0, 0),
	REPUBLICATION_DESTINATION("Repub Destination", ItemConstants.STRING, 0, 0),
	REPUBLISH_IN_VOL_ED("Republish in Vol/Ed", ItemConstants.STRING, 0, 0),
	DATE_OF_ISSUE("Date of issue", ItemConstants.STRING, 0, 0),
	FULL_ARTICLE("Full Article", ItemConstants.BOOLEAN, 0, 0),
	NUM_CHARTS("Charts", ItemConstants.INTEGER, 0, 0),
	NUM_EXCERPTS("Excerpts", ItemConstants.INTEGER, 0, 0),
	NUM_CARTOONS("Cartoons", ItemConstants.INTEGER, 0, 0),
	NUM_FIGURES("Figures", ItemConstants.INTEGER, 0, 0),
	NUM_GRAPHS("Graphs", ItemConstants.INTEGER, 0, 0),
	NUM_ILLUSTRATIONS("Illustrations", ItemConstants.INTEGER, 0, 0),
	NUM_LOGOS("Logos", ItemConstants.INTEGER, 0, 0),
	NUM_PHOTOS("Photos", ItemConstants.INTEGER, 0, 0),
	NUM_QUOTES("Quotes", ItemConstants.INTEGER, 0, 0),
	RLS_PAGES("RLS Pages",ItemConstants.INTEGER, 0, 0),
// Legacy fee information for display
	ENTIRE_BOOK_FEE("Entire Out of Print Book", ItemConstants.NUMBER, 0, 0),
	PER_PAGE_FEE("Per Page", ItemConstants.NUMBER, 0, 0),
	BASE_FEE("Per Copy", ItemConstants.NUMBER, 0, 0),
	FLAT_FEE("Flat", ItemConstants.NUMBER, 0, 0),
	PER_LOGO("Per Graph", ItemConstants.NUMBER, 0, 0),
	PER_GRAPH("Per Graph", ItemConstants.NUMBER, 0, 0),
	PER_CARTOON("Per Cartoon", ItemConstants.NUMBER, 0, 0),
	PER_EXCERPT("Per Excerpt", ItemConstants.NUMBER, 0, 0),
	PER_QUOTE("Per Quote", ItemConstants.NUMBER, 0, 0),
	PER_CHART("Per Chart", ItemConstants.NUMBER, 0, 0),
	PER_PHOTOGRAPH("Per Photograph", ItemConstants.NUMBER, 0, 0),
    PER_ILLUSTRATION("Per Illustration", ItemConstants.NUMBER, 0, 0),
    PER_FIGURE ("Per Figure", ItemConstants.NUMBER, 0, 0),
	MAXIMUM_ROYALTY_FEE("Max Royalty", ItemConstants.NUMBER, 0, 0),
	PER_ARTICLE_AUTHOR("Per Article Author", ItemConstants.NUMBER, 0, 0),
	PER_ARTICLE_NON_AUTHOR("Per Article Non-Author", ItemConstants.NUMBER, 0, 0),
	LICENSEE_FEE("Licensee fee", ItemConstants.NUMBER, 0, 0),
	TOTAL_AMOUNT("Total Amount", ItemConstants.NUMBER, 0, 0),
	DISCOUNT("Discount", ItemConstants.NUMBER, 0, 0),
	ROYALTY("Royalty", ItemConstants.NUMBER, 0, 0),
	TO_30_DAYS_FEE("1-30 days", ItemConstants.NUMBER, 0, 0),
	TO_180_DAYS_FEE("30-180 days", ItemConstants.NUMBER, 0, 0),
	TO_365_DAYS_FEE("180-365 days", ItemConstants.NUMBER, 0, 0),
	UNLIMITED_DAYS_FEE("365+ Days", ItemConstants.NUMBER, 0, 0),
	TO_49_RECIPIENTS_FEE("1 to 49", ItemConstants.NUMBER, 0, 0),
	TO_249_RECIPIENTS_FEE("50 to 249", ItemConstants.NUMBER, 0, 0),
	TO_499_RECIPIENTS_FEE("250 to 499", ItemConstants.NUMBER, 0, 0),
	TO_500P_RECIPIENTS_FEE("500+", ItemConstants.NUMBER, 0, 0),
	TOTAL_ROYALTY_FFM_INST("TOTAL_ROYALTY_FFM_INST", ItemConstants.INTEGER, 0, 0),
    LICENSEE_DISCOUNT_FFM_INST("LICENSEE_DISCOUNT_FFM_INST", ItemConstants.INTEGER, 0, 0),
    LICENSEE_FEE_FFM_INST("LICENSEE_FEE_FFM_INST", ItemConstants.INTEGER, 0, 0),
    RIGHTSHOLER_FEE_FFM_INST("RIGHTSHOLER_FEE_FFM_INST", ItemConstants.INTEGER, 0, 0),
    ROYALTY_PAYABLE_FFM_INST("ROYALTY_PAYABLE_FFM_INST", ItemConstants.INTEGER, 0, 0),
	LCN_REQUESTED_ENTIRE_WORK("Lcn Requested Entire Work", ItemConstants.BOOLEAN,0 ,0),
	LICENSEEFEEIFAPPLICABLE("LINENSEE_FEE_IF_APPLICABLE", ItemConstants.STRING,0 ,0),
	OFFERCHANNEL("OFFERCHANNEL", ItemConstants.STRING,0 ,0),
	NRLS_SO("NRLS_SO", ItemConstants.STRING,0 ,0);

	
	
	String label;
	String dataTypeCd;
	Integer fieldLength;
	Integer displayWidth;
	
	private ItemParmEnum(String label, String dataType, Integer fieldLength, Integer displayWidth) {
		this.label = label;
		this.dataTypeCd = dataType;
		this.fieldLength = fieldLength;
		this.displayWidth = displayWidth;
	}
	

	public static ItemParmEnum getEnumForName( String parmName ) {
		for ( ItemParmEnum en : ItemParmEnum.values() ) {
			if ( parmName.equalsIgnoreCase(en.name())) {
				return en;
			} 
		}
		return null;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDataTypeCd() {
		return dataTypeCd;
	}

	public void setDataTypeCd(String dataTypeCd) {
		this.dataTypeCd = dataTypeCd;
	}

	public Integer getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}

	public Integer getDisplayWidth() {
		return displayWidth;
	}

	public void setDisplayWidth(Integer displayWidth) {
		this.displayWidth = displayWidth;
	}
}
