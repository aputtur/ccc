package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;

/**
 * Submit a user email address to perform user information lookup.
 * This is a user administrator form.
 * @author Jessop
 */
public class UserAlertForm extends CCValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _flag;
	private String _message;

    public UserAlertForm() {
		_flag = null;
		_message = null;
	}

	public UserAlertForm(String flag, String message) {
		_flag = flag;
		_message = message;
	}
	
	public void setFlag(String flag) {
	    if (flag == null) {
	        _flag = "OFF";
	    }
	    else {
	        _flag = flag.toUpperCase();
	        if (!(_flag.equals("ON") || _flag.equals("OFF"))) _flag = "OFF";
	    }
	}
	
	public String getFlag() {
	    return _flag;
	}

	public void setMessage(String message) {
	    if (message == null) {
	        _message = "There is no user alert at this time.";
	    }
	    else {
	        _message = message;
	    }
	}
	
	public String getMessage() {
	    return _message;
	}
}
