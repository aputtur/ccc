package com.copyright.ccc.business.data;

import java.util.Date;

import com.copyright.ccc.business.services.user.SearchPermissionType;
import com.copyright.ccc.business.services.user.SearchPermissionTypeEnum;
import com.copyright.opi.data.ModState;

public interface  CCUser
{
    void setID( long id );
    long getID();
    
    void setAuid( String auid );
    String getAuid();
    // rights link session id
    void setRlnkSessionId( String rlnkSessionId );
    String getRlnkSessionId();


    void setUsername( String username );
    String getUsername();

    void setUserType( String userType );
    String getUserType();

    void setLastCartID( long lastCartID );
    long getLastCartID();

    void setPartyID( long partyID );
    long getPartyID();

    boolean isAnonymous();

    void setAlwaysInvoice( boolean alwaysInvoice );
    boolean isAlwaysInvoice();

    void setLastSessionStart( Date lastSessionStart );
    Date getLastSessionStart();
    
    void setPK( String pk );
    String getPK();
    
    void setModState( ModState modState );
    ModState getModState();
    
    public void setAllTypesOfUseSelected();
    
    /**
     * @deprecated
     * @see setSearchPreference(SearchPermissionTypeEnum, boolean)
     */
    @Deprecated
    void setSearchPreference( SearchPermissionType permType, boolean value );
    
    /**
     * @deprecated
     * @see getSearchPreference(SearchPermissionTypeEnum)
     */
    @Deprecated
    boolean getSearchPreference( SearchPermissionType permType );

    void setSearchPreference( SearchPermissionTypeEnum permType, boolean value );
    boolean getSearchPreference( SearchPermissionTypeEnum permType );
    
    void setSkipQuickprice( boolean skip );
    boolean skipQuickprice();
    void setAutoInvoiceSpecialOrder( boolean autoInvoice );
    boolean isAutoInvoiceSpecialOrder();
    
    void setEnforcePwdChg(boolean enforcePwdChg);
    boolean getEnforcePwdChg();
}
