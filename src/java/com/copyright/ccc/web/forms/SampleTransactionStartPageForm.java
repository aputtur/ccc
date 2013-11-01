package com.copyright.ccc.web.forms;

import com.copyright.ccc.web.CCValidatorForm;

public class SampleTransactionStartPageForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _action;
    private String _typeOfUse;
    private String _sampleValue;
    
    public void setTypeOfUse(String typeOfUse)
    {
        this._typeOfUse = typeOfUse;
    }

    public String getTypeOfUse()
    {
        return _typeOfUse;
    }

    public void setAction(String action)
    {
        this._action = action;
    }

    public String getAction()
    {
        return _action;
    }


    public void setSampleValue(String sampleValue)
    {
        this._sampleValue = sampleValue;
    }

    public String getSampleValue()
    {
        return _sampleValue;
    }
}
