package com.copyright.ccc.business.services;

import com.copyright.ccc.business.services.user.SearchPermissionTypeEnum;

/**
 * Represents an enumeration of the different permission categories that
 * get displayed for a work's Permission Summary. Each is given an id
 * that identifies it uniquely as a long value such that it will integrate
 * with some of the legacy code. For convenience, each PermissionCategoryEnum instance also carries with it
 * a reference to the corresponding SearchPermissionTypeEnum.
 * 
 * @author jarbo
 *
 */
public enum PermissionCategoryEnum {
    APS_CATAGORYID(2L,"aps", SearchPermissionTypeEnum.ACADEMIC_TRX_PHOTOCOPY),
    DPS_CATAGORYID(5L, "dps", SearchPermissionTypeEnum.BUSINESS_TRX_DIGITAL),
    RLS_CATAGORYID(6L, "rls", SearchPermissionTypeEnum.REPUBLICATION),
    ECC_CATAGORYID(1L, "ecc", SearchPermissionTypeEnum.ACADEMIC_TRX_DIGITAL),
    TRSILL_CATAGORYID(3L, "trsIll", SearchPermissionTypeEnum.BUSINESS_TRX_ILL),
    TRSPHOTO_CATAGORYID(4L, "trsPhoto", SearchPermissionTypeEnum.BUSINESS_TRX_PHOTOCOPY),
    AAS_CATAGORYID(8L, "aas", SearchPermissionTypeEnum.BUSINESS_LIC_PHOTOCOPY),
    DRA_CATAGORYID(9L, "dra", SearchPermissionTypeEnum.BUSINESS_LIC_DIGITAL),
    ARS_CATAGORYID(10L, "ars", SearchPermissionTypeEnum.ACADEMIC_LIC_DIGITAL),
    RLR_CATAGORYID(7L, "rlr", SearchPermissionTypeEnum.RIGHTSLINK_REPRINT);

    //pay-per-use permission categories
    private static final PermissionCategoryEnum[] ppuCategories = new PermissionCategoryEnum[]{
    		APS_CATAGORYID,
    		DPS_CATAGORYID,
    		ECC_CATAGORYID,
    		RLS_CATAGORYID,
    		TRSILL_CATAGORYID,
    		TRSPHOTO_CATAGORYID};

    //annual license categories
    private static final PermissionCategoryEnum[] licenseCategories = new PermissionCategoryEnum[]{
		AAS_CATAGORYID,
		DRA_CATAGORYID,
		ARS_CATAGORYID};
    
    //rightslink permission category
    private static final PermissionCategoryEnum[] rightslinkCategories = new PermissionCategoryEnum[]{
    	RLR_CATAGORYID
    };
    
    private long id;
    private String categoryCode; //one of aas, dps, trs, etc..
    private SearchPermissionTypeEnum searchPermissionTypeEnum;
    
    /**
     * constructs an instance of PermissionCategoryEnum. The id is a unique number representing the
     * category. This is needed to satisfy some legacy code that was using constant Long values. 
     * The categoryCode is a String that identifies the product. The categoryCode string value is used in 
     * query parameters to identify 1 or more categorys (aas,dra,ars,trsIll,trsPhoto,aps,dps,rls,rlr). The
     * SearchPermissionTypeEnum associates the PermissionCategoryEnum with the specified SearchPermissionTypeEnum.
     * 
     * @param id
     * @param categoryCode
     * @param searchPermissionTypeEnumVal
     */
    private PermissionCategoryEnum(long id, String categoryCode, SearchPermissionTypeEnum searchPermissionTypeEnumVal) {
    	this.id = id;
    	this.categoryCode = categoryCode;
    	this.searchPermissionTypeEnum = searchPermissionTypeEnumVal;
    }

    /**
     * Returns the PermissionCategoryEnum value that corresponds to the 
     * categoryCode provided, null otherwise.
     * 
     * @param categoryCode
     * @return
     */
    public static PermissionCategoryEnum valueOfCategoryCode(String categoryCode) {
    	for(PermissionCategoryEnum pEnum: PermissionCategoryEnum.values()) {
    		if (pEnum.categoryCode.equals(categoryCode)) {
    			return pEnum;
    		}
    	}
    	return null;
    }
    
    /**
     * Returns this categories numeric product category id
     * @return
     */
	public long getId() {
		return id;
	}

	/**
	 * Returns the String based category code that represents this PermissionCategoryEnum.
	 * @return
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	public static PermissionCategoryEnum[] getTraditionalPpuCategories() {
		return ppuCategories;
	}

	public static PermissionCategoryEnum[] getLicenseCategories() {
		return licenseCategories;
	}

	public static PermissionCategoryEnum[] getRightslinkPpuCategories() {
		return rightslinkCategories;
	}

	/**
	 * Returns the SearchPermissionTypeEnum that is tied to this PermissionCategoryEnum.
	 * @return
	 */
	public SearchPermissionTypeEnum getSearchPermissionTypeEnum() {
		return searchPermissionTypeEnum;
	}
    
}
