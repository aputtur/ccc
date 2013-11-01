
package com.copyright.ccc.business.services;

public enum ItemDescriptionParmEnum {
	
	STANDARD_NUMBER("Standard Number", ItemConstants.STRING, 0, 0),
	PUBLISHER("Publisher", ItemConstants.STRING, 0, 0),
	PUBLICATION_YEAR("Publication Year", ItemConstants.STRING, 0, 0),
	AUTHOR("Author", ItemConstants.STRING, 0, 0),
	EDITOR("Editor", ItemConstants.STRING, 0, 0),
	VOLUME("Volume", ItemConstants.STRING, 0, 0),
	EDITION("Edition", ItemConstants.STRING, 0, 0),
	SERIES("Series", ItemConstants.STRING, 0, 0),
	SERIES_NUMBER("Series Number", ItemConstants.STRING, 0, 0),
	PUBLICATION_TYPE("Publication Type", ItemConstants.STRING, 0, 0),
	COUNTRY("Country", ItemConstants.STRING, 0, 0),
	LANGUAGE("Language", ItemConstants.STRING, 0, 0),
	IDNO_LABEL("Idno Label", ItemConstants.STRING, 0, 0),
	IDNO_TYPE_CD("Idno Type Cd", ItemConstants.STRING, 0, 0),
	PAGES("Pages", ItemConstants.STRING, 0, 0),
	PUB_START_DATE("Pub Start Date", ItemConstants.STRING, 0, 0),
	PUB_END_DATE("Pub end Date", ItemConstants.STRING, 0, 0),
	OCLC_NUMBER("OCLC Number", ItemConstants.STRING, 0, 0),
	IDNO_WOP("IDNO WOP", ItemConstants.STRING, 0, 0),
	// Temporary work around
//	ITEM_SUB_SOURCE_CD("Sub Source Cd", ItemConstants.STRING, 0, 0),
//	ITEM_SUB_SOURCE_KEY("Sub Source Key", ItemConstants.INTEGER, 0, 0),
	
	// article related metadata fields
	GRANULAR_WORK_START_PAGE("Start Page", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_END_PAGE("End Page", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_PUBLICATION_DATE("Publication Date", ItemConstants.DATE, 0, 0),
	GRANULAR_WORK_AUTHOR("Author", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_DOI("DOI", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_VOLUME("Volume", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_ISSUE("Issue", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_NUMBER("Number", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_PAGE_RANGE("Page Range", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_SEASON("Season", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_QUARTER("Quarter", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_WEEK("Week", ItemConstants.STRING, 0, 0),
	GRANULAR_WORK_SECTION("Section", ItemConstants.STRING, 0, 0);

	
	String label;
	String dataTypeCd;
	Integer fieldLength;
	Integer displayWidth;
	
	private ItemDescriptionParmEnum(String label, String dataType, Integer fieldLength, Integer displayWidth) {
		this.label = label;
		this.dataTypeCd = dataType;
		this.fieldLength = fieldLength;
		this.displayWidth = displayWidth;
	}
	
	public static ItemDescriptionParmEnum getEnumForName( String parmName ) {
		for ( ItemDescriptionParmEnum en : ItemDescriptionParmEnum.values() ) {
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
