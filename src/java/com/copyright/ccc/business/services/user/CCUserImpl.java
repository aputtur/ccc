package com.copyright.ccc.business.services.user;

import java.util.Date;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.opi.data.StandardDO;

public class CCUserImpl extends StandardDO implements CCUser
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _auid;
	private String _rlnkSessionId;
    private String _username;
    private String _userType;
    private String _pk;
    
    private long _lastCartID;
    private long _partyID;
    private long _requestClientVersion;
    
    private boolean _alwaysInvoice = false;
    private boolean _skipQuickprice = false;
    private boolean _autoInvoiceSpecialOrder = false;
    
    
    private boolean _enforcePwdChg = false;
    private Date _lastSessionStart;

    /*
     * An array of booleans, each element of which identifies an individual type of use and whether
     * it is turned "on" (true) for display or "off" (false).
     */
    private boolean[] _searchPreferences = new boolean[SearchPermissionTypeEnum.values().length];
  
    public static final String DEFAULT_USER_TYPE = "CUST";

    /**
     * Turns "on" all types of use for display in the permission summary
     */
    public void setAllTypesOfUseSelected() {
    	for(SearchPermissionTypeEnum value: SearchPermissionTypeEnum.values()) {
    		_searchPreferences[value.ordinal()]=true;
    	}
    }
    /**
     * @deprecated
     * @see setSearchPreference(SearchPermissionTypeEnum, boolean)
     */
    @Deprecated
    public void setSearchPreference( SearchPermissionType permType, boolean value )
    {
        _searchPreferences[permType.getValue()] = value;
    }
    /**
     * @deprecated
     * @see getSearchPreference(SearchPermissionTypeEnum)
     */
    @Deprecated
    public boolean getSearchPreference( SearchPermissionType permType )
    {
        return _searchPreferences[permType.getValue()];
    }

    /*
     * (non-Javadoc)
     * @see com.copyright.ccc.business.data.CCUser#setSearchPreference(com.copyright.ccc.business.services.user.SearchPermissionTypeEnum, boolean)
     */
    public void setSearchPreference( SearchPermissionTypeEnum permType, boolean value )
    {
    	setSearchPreference(permType.getLegacyType(), value);
    }
    /*
     * (non-Javadoc)
     * @see com.copyright.ccc.business.data.CCUser#getSearchPreference(com.copyright.ccc.business.services.user.SearchPermissionTypeEnum)
     */
    public boolean getSearchPreference( SearchPermissionTypeEnum permType )
    {
    	return getSearchPreference(permType.getLegacyType());
    }
    
    
    // protected default constructor for safety
    protected CCUserImpl()
    {
        setUserType( DEFAULT_USER_TYPE );
    }
    
    public void setAuid( String auid )
    {
        _auid = auid;
    }

    public String getAuid()
    {
        return _auid;
    }

    public void setUsername( String username )
    {
        _username = username;
    }

    public String getUsername()
    {
        return _username;
    }

    public void setLastCartID( long lastCartID )
    {
        _lastCartID = lastCartID;
    }

    public long getLastCartID()
    {
        return _lastCartID;
    }

    public boolean isAnonymous()
    {
        return _username == null;
    }

    public void setAlwaysInvoice( boolean alwaysInvoice )
    {
        _alwaysInvoice = alwaysInvoice;
    }

    public boolean isAlwaysInvoice()
    {
        return _alwaysInvoice;
    }

    public void setUserType( String userType )
    {
        _userType = userType;
    }

    public String getUserType()
    {
        return _userType;
    }

    public void setPartyID( long partyID )
    {
        _partyID = partyID;
    }

    public long getPartyID()
    {
        return _partyID;
    }

    public void setLastSessionStart( Date lastSessionStart )
    {
        _lastSessionStart = lastSessionStart;
    }

    public Date getLastSessionStart()
    {
        return _lastSessionStart;
    }

    public void setPK( String pk )
    {
        _pk = pk;
    }

    public String getPK()
    {
        return _pk;
    }

    public void setRequestClientVersion( long requestClientVersion )
    {
        _requestClientVersion = requestClientVersion;
    }

    public long getRequestClientVersion()
    {
        return _requestClientVersion;
    }
    
    public void setSkipQuickprice( boolean skip )
    {
        _skipQuickprice = skip;
    }

    public boolean skipQuickprice()
    {
        return _skipQuickprice;
    }
    public void setAutoInvoiceSpecialOrder( boolean autInvoice )
    {
    	_autoInvoiceSpecialOrder = autInvoice;
    }

    public boolean isAutoInvoiceSpecialOrder()
    {
        return _autoInvoiceSpecialOrder;
    }

	public boolean getEnforcePwdChg()
	{
		return _enforcePwdChg;
	}

	public void setEnforcePwdChg(boolean enforcePwdChg)
	{
		_enforcePwdChg = enforcePwdChg;
	}
	
	public String getRlnkSessionId() {
		// TODO Auto-generated method stub
		return _rlnkSessionId;
	}
	
	public void setRlnkSessionId(String rlnkSessionId) {
		_rlnkSessionId=rlnkSessionId;
	}
}
