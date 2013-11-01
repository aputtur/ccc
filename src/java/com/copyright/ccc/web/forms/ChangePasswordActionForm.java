package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;

public class ChangePasswordActionForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _password;
    private String _passwordConfirmation;


    public void setPassword( String password )
    {
        this._password = password;
    }

    public String getPassword()
    {
        return _password;
    }

    public void setPasswordConfirmation( String passwordConfirmation )
    {
        this._passwordConfirmation = passwordConfirmation;
    }

    public String getPasswordConfirmation()
    {
        return _passwordConfirmation;
    }
}
