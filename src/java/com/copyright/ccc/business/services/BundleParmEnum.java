package com.copyright.ccc.business.services;

public enum BundleParmEnum {
	
	ESTIMATED_COPIES("Estimated Copies", ItemConstants.INTEGER, 0, 0),
	NUM_STUDENTS("Number of Students", ItemConstants.INTEGER, 0, 0),
	LCN_BUNDLE_REFERENCE("Your Reference", ItemConstants.STRING, 0, 0),
	INSTRUCTOR("Instructor", ItemConstants.STRING, 0, 0),
	LCN_BUNDLE_ACCOUNTING_REFERENCE("Your Accounting Reference", ItemConstants.STRING, 0, 0);
	
	String label;
	String dataTypeCd;
	Integer fieldLength;
	Integer displayWidth;
	
	private BundleParmEnum(String label, String dataType, Integer fieldLength, Integer displayWidth) {
		this.label = label;
		this.dataTypeCd = dataType;
		this.fieldLength = fieldLength;
		this.displayWidth = displayWidth;
	}
	

	public static BundleParmEnum getEnumForName( String parmName ) {
		for ( BundleParmEnum en : BundleParmEnum.values() ) {
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
