package com.copyright.ccc.business.services.user;

/**
 * SearchPermissionTypeEnum identifies the permission types a user
 * can request be displayed in the permission summary page. This enum
 * was created to replace the <code>SearchPermissionType</code>.
 * <p>
 * It is not clear how this is different from PermissionCategoryEnum (which replaces
 * hard coded long values) except that they are used in different places.
 */
public enum SearchPermissionTypeEnum
{
	ACADEMIC_TRX_PHOTOCOPY( 0, true ),
	BUSINESS_TRX_PHOTOCOPY ( 1, true ),
    ACADEMIC_TRX_DIGITAL ( 2, true ),
    BUSINESS_TRX_DIGITAL ( 3, true ),
    REPUBLICATION ( 4, true ),
    BUSINESS_LIC_PHOTOCOPY ( 5, false ),
    BUSINESS_LIC_DIGITAL ( 6, false ),
    ACADEMIC_LIC_DIGITAL ( 7, false ),
    RIGHTSLINK_REPRINT ( 8, false ),
	RIGHTSLINK_DIGITAL ( 9, false ),
    BUSINESS_TRX_ILL ( 10, true );

	private SearchPermissionType legacyType;
	private boolean isPayPerUse;
	
	private SearchPermissionTypeEnum(int ordinal, boolean isPayPerUse) {
		legacyType = SearchPermissionType.getByValue(ordinal);
		this.isPayPerUse = isPayPerUse;
	}

	public SearchPermissionType getLegacyType() {
		return legacyType;
	}
	public boolean isPayPerUse() {
		return isPayPerUse;
	}
}