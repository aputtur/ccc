package com.copyright.ccc.web.forms.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.data.security.Role;

public class AutoDunningForm extends CCValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mDaysPastDue;
	private String mCustomerId;
	private String mSentEmailTo;
	private String mTotalNumEmailSent;

	private String _username;
	private User _user;

	private Role[] _userRoles;
	private Role[] _applicationRoles;
	private String[] _savedUserRoleCodes;

	private boolean _doFind = false;
	private boolean mUITestMode = false;

	/**
	 * Reset all properties to their default values.
	 */
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		// need to reset this to empty array in order to correctly
		// recognize cases where none of the associated checkboxes are selected
		_savedUserRoleCodes = new String[0];
		super.reset(mapping, request);
	}

	public void setUsername(String username)
	{
		this._username = username;
	}

	public String getUsername()
	{
		return _username;
	}

	public void setUser(User user)
	{
		this._user = user;
	}

	public User getUser()
	{
		return _user;
	}

	public void setUserRoles(Role[] userRoles)
	{
		this._userRoles = userRoles;
	}

	public Role[] getUserRoles()
	{
		return _userRoles;
	}

	public void setApplicationRoles(Role[] applicationRoles)
	{
		this._applicationRoles = applicationRoles;
	}

	public Role[] getApplicationRoles()
	{
		return _applicationRoles;
	}

	public void setSavedUserRoleCodes(String[] savedUserRoleCodes)
	{
		this._savedUserRoleCodes = savedUserRoleCodes;
	}

	public String[] getSavedUserRoleCodes()
	{
		return _savedUserRoleCodes;
	}

	public void setDoFind(boolean doFind)
	{
		this._doFind = doFind;
	}

	public boolean isDoFind()
	{
		return _doFind;
	}

	public String getDaysPastDue()
	{
		return mDaysPastDue;
	}

	public void setDaysPastDue(String daysPastDue)
	{
		mDaysPastDue = daysPastDue;
	}

	public String getSentEmailTo()
	{
		return mSentEmailTo;
	}

	public void setSentEmailTo(String sentEmailTo)
	{
		mSentEmailTo = sentEmailTo;
	}

	public String getTotalNumEmailSent()
	{
		return mTotalNumEmailSent;
	}

	public void setTotalNumEmailSent(String totalNumEmailSent)
	{
		mTotalNumEmailSent = totalNumEmailSent;
	}

	public String getCustomerId()
	{
		return mCustomerId;
	}

	public void setCustomerId(String customerId)
	{
		mCustomerId = customerId;
	}

	public boolean getUITestMode()
	{
		return this.isUITestMode();
	}

	public boolean isUITestMode()
	{
		return mUITestMode;
	}

	public void setUITestMode(boolean testMode)
	{
		mUITestMode = testMode;
	}

	
}
