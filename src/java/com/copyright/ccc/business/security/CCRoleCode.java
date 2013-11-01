package com.copyright.ccc.business.security;

import com.copyright.data.security.RoleCode;

/*
    Changes     Enter change history here.
    
    when        who         what
    ----------  ----------  -----------------------------------------------------------------
    2007/10/15  MSJ         Added tech coord to correspond with changes D. Stine made in SS.
*/

/**
 * Represents the available role codes.
 */
public class CCRoleCode extends RoleCode 
{
    private static final String FINANCE_CODE = "FINANCE";
    private static final String CUSTOMER_RELATIONS_CODE = "CRR";
    private static final String SYSTEM_ADMIN_CODE = "SYSADMIN";
    private static final String TECH_COORD_CODE = "TECH_COORD";
    
    public static final CCRoleCode FINANCE = new CCRoleCode( FINANCE_CODE );
    public static final CCRoleCode CUSTOMER_RELATIONS = new CCRoleCode( CUSTOMER_RELATIONS_CODE );
    public static final CCRoleCode SYSTEM_ADMIN = new CCRoleCode( SYSTEM_ADMIN_CODE );
    public static final CCRoleCode TECH_COORD = new CCRoleCode( TECH_COORD_CODE );
    
    
    /**
     * Private constructor to ensure singleton instances.
     */
    private CCRoleCode( String code )
    {
        super( code );
    }
}