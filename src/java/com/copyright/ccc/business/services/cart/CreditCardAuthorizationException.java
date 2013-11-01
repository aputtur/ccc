package com.copyright.ccc.business.services.cart;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCException;
import com.copyright.data.SharedAppResources;
import com.copyright.data.payment.AuthorizationException;

/**
 * Exception class that indicates credit card authorization issues
 * when performing a credit-card-related transaction (e.g. checkout a cart).
 */
public class CreditCardAuthorizationException extends CCException
{
  
  /**
	 * 
	 */
	private static final Logger _logger = Logger.getLogger( CreditCardAuthorizationException.class );
	private static final long serialVersionUID = 1L;
private AuthorizationException _authorizationException;
  private CreditCardDetails _creditCardDetails;
     
  private CreditCardAuthorizationException(){}

  CreditCardAuthorizationException( AuthorizationException authorizationException,
                                    CreditCardDetails creditCardDetails )
  {
    setAuthorizationException( authorizationException );
    setCreditCardDetails( creditCardDetails );
  }

  private void setAuthorizationException(AuthorizationException authorizationException)
  {
    this._authorizationException = authorizationException;
  }

  private AuthorizationException getAuthorizationException()
  {
    return _authorizationException;
  }

  private void setCreditCardDetails(CreditCardDetails creditCardDetails)
  {
    this._creditCardDetails = creditCardDetails;
  }

  public CreditCardDetails getCreditCardDetails()
  {
    return _creditCardDetails;
  }

  @Override
  public Throwable getCause()
  {
    return getAuthorizationException().getCause();
  }

  @Override
  public void printStackTrace()
  {
	_logger.error( ExceptionUtils.getFullStackTrace(getAuthorizationException()) );
  }

  @Override
  public void printStackTrace(PrintStream PrintStream)
  {
	_logger.error( ExceptionUtils.getFullStackTrace(getAuthorizationException()) );
  }

  @Override
  public void printStackTrace(PrintWriter PrintWriter)
  {
	_logger.error( ExceptionUtils.getFullStackTrace(getAuthorizationException()) );
  }
  
  public String getDetailedMessage()
  {
      String messageKey = getAuthorizationException().getMessageCode();
      
      if(StringUtils.isEmpty( messageKey ))
      {
          return this.getMessage(); 
      }
      else
      {
          SharedAppResources sharedAppResources = SharedAppResources.getInstance();
          return sharedAppResources.getValue( messageKey );
      }
  }
}
