package com.copyright.ccc.web.transaction;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class PricingError implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NO_ACTION_MODE = 0;
    public static final int SPECIAL_ORDER_MODE = 1;
    public static final int REGULAR_ORDER_MODE = 2;
    
    private String _errorIcon;
    private String _errorMessageKey;
    private String _errorMessageArg;
    private int _errorAction;
    
    PricingError(String errorIcon, String errorMessageKey, int errorAction)
    {
        this(errorIcon, errorMessageKey, StringUtils.EMPTY, errorAction);
    }
   
    PricingError(String errorIcon, String errorMessageKey, String errorMessageArg, int errorAction)
    {
        _errorIcon = errorIcon;
        _errorMessageKey = errorMessageKey;
        _errorMessageArg = errorMessageArg;
        _errorAction = errorAction;
    }

    public void setErrorIcon(String errorIcon)
    {
        this._errorIcon = errorIcon;
    }

    public String getErrorIcon()
    {
        return _errorIcon;
    }

    public void setErrorMessageKey(String errorMessageKey)
    {
        this._errorMessageKey = errorMessageKey;
    }

    public String getErrorMessageKey()
    {
        return _errorMessageKey;
    }

    public void setErrorAction(int errorAction)
    {
        this._errorAction = errorAction;
    }

    public int getErrorAction()
    {
        return _errorAction;
    }

    public void setErrorMessageArgKey(String errorMessageArg)
    {
        this._errorMessageArg= errorMessageArg;
    }

    public String getErrorMessageArg()
    {
        return _errorMessageArg;
    }
}
