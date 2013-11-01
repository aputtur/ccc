package com.copyright.ccc.web.forms;

import org.apache.log4j.Logger;

import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.workbench.logging.LoggerHelper;

/**
 * Submit a user email address to perform user information lookup.
 * This is a user administrator form.
 */
public class UserLookupForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _emailAddress;
    private Long _accountNumber;
    private static final Logger sLogger = LoggerHelper.getLogger();
    
    public UserLookupForm() { _emailAddress = null; _accountNumber = null; }
    public UserLookupForm(String emailAddress) {
        _emailAddress = emailAddress;
        _accountNumber = null;
    }
    public UserLookupForm(Long accountNumber) {
        _accountNumber = accountNumber;
        _emailAddress = null;
    }
    public UserLookupForm(String emailAddress, Long accountNumber) {
        _emailAddress = emailAddress;
        _accountNumber = accountNumber;
    }
    
    public void setEmailAddress(String emailAddress) {
        _emailAddress = emailAddress;
    }
    public void setAccountNumber(String accountNumber) {
        try {
            _accountNumber = Long.valueOf(accountNumber);
        }
        catch(Exception e) {
        	sLogger.warn("unable to create a Long from acct # " + accountNumber + LogUtil.appendableStack(e));
            _accountNumber = null;
        }
    }
    
    public String getEmailAddress() {
        return _emailAddress;
    }
    public String getAccountNumber() {
        String retval = null;
        
        try {
            retval = _accountNumber.toString();
        }
        catch(Exception e) {
        	sLogger.warn("unable to create a String from acct # " + _accountNumber + LogUtil.appendableStack(e));
        }
        return retval;
    }
    public long getAccountNumberAsLong() {
        long retval = 0;
        
        try {
            retval = _accountNumber.longValue();
        }
        catch(Exception e) {
        	sLogger.warn("unable to create a Long from acct # " + _accountNumber + LogUtil.appendableStack(e));
        }
        return retval;
    }
}