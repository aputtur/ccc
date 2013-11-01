package com.copyright.ccc.business.security;

import com.copyright.data.security.PrivilegeCode;

/*
    Changes     Enter change history here.
    
    when        who         what
    ----------  ----------  -----------------------------------------------------------------
    2007/10/15  MSJ         Added USERINFO to correspond with changes D. Stine made in SS.
    2009/4/15   WFM         Added PUBLISHERINFO for managing Rights Link Publisher Accounts
*/

/**
 * Represents the available privilege codes.
 */
public class CCPrivilegeCode extends PrivilegeCode
{
    private static final String ENTER_ORDER_ADJUSTMENT_CODE = "ENTER_ORDADJ";
    private static final String COMMIT_ORDER_ADJUSTMENT_CODE = "COMMIT_ORDADJ";
    private static final String VIEW_ENTERPRISE_REPORTS_CODE = "VIEW_ENTERPRISE_REPORTS";
    private static final String EMULATE_USER_CODE = "EMULATE_USER";
    private static final String MANAGE_ROLES_CODE = "MANAGE_ROLES";
    private static final String MANAGE_USERINFO_CODE = "MANAGE_USERINFO";
    private static final String VIEW_USERINFO_CODE = "VIEW_USERINFO";
    private static final String MANAGE_PUBLISHERINFO_CODE = "MANAGE_PUBLISHERINFO";
    private static final String AUTO_DUNNING_CODE = "AUTO_DUNNING";
    private static final String SEARCH_ORDERS_CODE = "SEARCH_ORDERS";
    private static final String MANAGE_ORDERS_CODE = "MANAGE_ORDERS";
    
    public static final CCPrivilegeCode ENTER_ORDER_ADJUSTMENT = 
        new CCPrivilegeCode( ENTER_ORDER_ADJUSTMENT_CODE );
    
    public static final CCPrivilegeCode COMMIT_ORDER_ADJUSTMENT = 
        new CCPrivilegeCode( COMMIT_ORDER_ADJUSTMENT_CODE );
    
    public static final CCPrivilegeCode VIEW_ENTERPRISE_REPORTS = 
        new CCPrivilegeCode( VIEW_ENTERPRISE_REPORTS_CODE );
    
    public static final CCPrivilegeCode EMULATE_USER = 
        new CCPrivilegeCode( EMULATE_USER_CODE );
    
    public static final CCPrivilegeCode MANAGE_ROLES = 
        new CCPrivilegeCode( MANAGE_ROLES_CODE );

    public static final CCPrivilegeCode MANAGE_USERINFO = 
        new CCPrivilegeCode( MANAGE_USERINFO_CODE );
        
    public static final CCPrivilegeCode VIEW_USERINFO = 
        new CCPrivilegeCode( VIEW_USERINFO_CODE );
        
    public static final CCPrivilegeCode MANAGE_PUBLISHERINFO = 
        new CCPrivilegeCode( MANAGE_PUBLISHERINFO_CODE );
    
    public static final CCPrivilegeCode AUTO_DUNNING = 
        new CCPrivilegeCode( AUTO_DUNNING_CODE );
        
    public static final CCPrivilegeCode SEARCH_ORDERS = 
        new CCPrivilegeCode( SEARCH_ORDERS_CODE );
        
    public static final CCPrivilegeCode MANAGE_ORDERS = 
        new CCPrivilegeCode( MANAGE_ORDERS_CODE );
    
    /**
     * Private constructor to ensure singleton instances.
     */
    private CCPrivilegeCode( String code )
    {
        super( code );
    }
}