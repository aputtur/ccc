package com.copyright.ccc.business.services.cart;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCException;
import com.copyright.data.ValidationException;
import com.copyright.data.ValidationFailure;


/**
 * Exception abstract class that represent cases of validation issues in e-commerce related activities.
 * Encapsulates an instance of <code>com.copyright.data.ValidationException</code>.
 */
public abstract class ECommerceValidationException extends CCException
{
	private static final long serialVersionUID = 1L;
	
  private ValidationException _validationException;
  
  private static final Logger _logger = Logger.getLogger( ECommerceValidationException.class );
    
  ECommerceValidationException(){}
  
  ECommerceValidationException( ValidationException validationException )
  {
    setValidationException( validationException );
  }

  /**
   * Return a list of offending field names that compose this <code>ECommerceValidationException</code> instance.
   */
  public List<String> getValidationErrors()
  {
    List<String> validationErrors = new ArrayList<String>(0);
    
    @SuppressWarnings("unchecked")
    Iterator<ValidationFailure> validationFailuresIterator = getValidationException().getValidationFailures().iterator();
    
    while( validationFailuresIterator.hasNext() )
    {
      ValidationFailure currentValidationFailure = validationFailuresIterator.next();
      
      String offendingAttributePath = currentValidationFailure.getOffendingAttributePath();
      
      validationErrors.add( offendingAttributePath );
      
    }
    
    return validationErrors;
  }
  
  /**
   * Return a list of offending message codes that compose this <code>ECommerceValidationException</code> instance.
   */
  public List<String> getValidationMessageCodes()
  {
    List<String> validationMessageCodes = new ArrayList<String>(0);
    
    @SuppressWarnings("unchecked")
    Iterator<ValidationFailure> validationFailuresIterator = getValidationException().getValidationFailures().iterator();
    
    while( validationFailuresIterator.hasNext() )
    {
      ValidationFailure currentValidationFailure = validationFailuresIterator.next();
      
      String offendingMessageCode = currentValidationFailure.getMessageCode();
      
      validationMessageCodes.add( offendingMessageCode );
      
    }
    
    return validationMessageCodes;
  }

  protected void setValidationException(ValidationException validationException)
  {
    this._validationException = validationException;
  }

  protected ValidationException getValidationException()
  {
    return _validationException;
  }

  @Override
  public void printStackTrace()
  {
	_logger.error( ExceptionUtils.getFullStackTrace(getValidationException()) );
  }

  @Override
  public void printStackTrace(PrintStream PrintStream)
  {
	_logger.error( ExceptionUtils.getFullStackTrace(getValidationException()) );
  }

  @Override
  public void printStackTrace(PrintWriter PrintWriter)
  {
	_logger.error( ExceptionUtils.getFullStackTrace(getValidationException()) );
  }

  @Override
  public Throwable getCause()
  {
    return getValidationException().getCause();
  }
}
