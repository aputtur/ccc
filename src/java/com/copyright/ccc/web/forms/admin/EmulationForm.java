package com.copyright.ccc.web.forms.admin;

import com.copyright.ccc.web.CCValidatorForm;

public class EmulationForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _emulationUsername;
    private String _operation;

    public void setEmulationUsername(String emulationUsername)
    {
        _emulationUsername = emulationUsername;
    }

    public String getEmulationUsername()
    {
        return _emulationUsername;
    }
    
    public void setOperation( String operation )
    {
        _operation = operation;
    }
    
    @Override
    public String getOperation()
    {
        return _operation;
    }
}
