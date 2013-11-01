package com.copyright.ccc.web.actions.ordermgmt.data.base;


public enum SortDirectionEnum {

	ASC("ASC"), DESC("DESC");
	
	private String codeValue;
	
	private SortDirectionEnum(String codeValue) {
		this.codeValue = codeValue;
	}
	
	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	
	public static SortDirectionEnum getSortDirectionEnumFor( String direction ) {
		for ( SortDirectionEnum en : SortDirectionEnum.values() ) {
			if ( direction.equalsIgnoreCase(en.name())) {
				return en;
			} else if ( direction.equalsIgnoreCase("ascending") ) {
				return SortDirectionEnum.ASC;
			} else if ( direction.equalsIgnoreCase("descending") ) {
				return SortDirectionEnum.DESC;
			}
		}
		return null;
	}
}
