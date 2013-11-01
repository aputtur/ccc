package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;

public class UpdateEmailAddressActionForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _emailAddress;
	private Boolean updateable = true;
	private Boolean samePartyID;
	private Boolean rlUser = false;

    public Boolean getRlUser() {
		return rlUser;
	}

	public void setRlUser(Boolean rlUser) {
		this.rlUser = rlUser;
	}

	public Boolean getSamePartyID() {
		return samePartyID;
	}

	public void setSamePartyID(Boolean samePartyID) {
		this.samePartyID = samePartyID;
	}

	public void setEmailAddress( String emailAddress )
    {
        _emailAddress = emailAddress;
    }

    public String getEmailAddress()
    {
        return _emailAddress;
    }
    
    public Boolean getUpdateable() {
    	return updateable;
    }
    
    public void setUpdateable(Boolean isUpdateable){
    	updateable = isUpdateable;
    }
}
