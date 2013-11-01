package com.copyright.ccc.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.CCValidatorForm;

public class LoginForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final byte AL_OFF = 0;
    public static final byte AL_PHASE_ONE = 1;
    public static final byte AL_PHASE_TWO = 2;

    private int _numRetries = 0;

    private String _autoLoginUsername;
    private String _autoLoginPassword;
    private String _autoLoginForward;
    private String _formSubmit = "false";

    private byte _autoLoginPhase;
    
    private String _sessionInitiator;
    
    private String _forgotPasswordUsername;
    
    private long _cartID = 0;

    public void incrementNumRetries()
    {
        _numRetries++;
    }
    
    public void clearNumRetries()
    {
        _numRetries = 0;
    }

    public void setNumRetries( int numRetries )
    {
        _numRetries = numRetries;
    }

    public int getNumRetries()
    {
        return _numRetries;
    }

    public void setForgotPasswordUsername( String forgotPasswordUsername )
    {
        _forgotPasswordUsername = forgotPasswordUsername;
    }

    public String getForgotPasswordUsername()
    {
        return _forgotPasswordUsername;
    }

    public void setAutoLoginUsername( String autoLoginUsername )
    {
        _autoLoginUsername = autoLoginUsername;
    }

    public String getAutoLoginUsername()
    {
        return _autoLoginUsername;
    }

    public void setAutoLoginPassword( String autoLoginPassword )
    {
        _autoLoginPassword = autoLoginPassword;
    }

    public String getAutoLoginPassword()
    {
        return _autoLoginPassword;
    }

    public void setAutoLoginForward( String autoLoginForward )
    {
        _autoLoginForward = autoLoginForward;
    }

    public String getAutoLoginForward()
    {
        return _autoLoginForward;
    }

    public void setAutoLoginPhase( byte autoLoginPhase )
    {
        _autoLoginPhase = autoLoginPhase;
    }

    public byte getAutoLoginPhase()
    {
        return _autoLoginPhase;
    }

    public void setSessionInitiator( String sessionInitiator )
    {
        _sessionInitiator = sessionInitiator;
    }

    public String getSessionInitiator()
    {
        return _sessionInitiator;
    }

    public void setFormSubmit( String formSubmit )
    {
        this._formSubmit = formSubmit;
    }

    public String getFormSubmit()
    {
        return _formSubmit;
    }

  public void setCartID(long cartID)
  {
    this._cartID = cartID;
  }

  public long getCartID()
  {
    return _cartID;
  }

  @Override
  public void reset(ActionMapping ActionMapping, 
                    HttpServletRequest HttpServletRequest)
  {
    super.reset(ActionMapping, HttpServletRequest);
    
    //setCartID( 0 );
  }
}
