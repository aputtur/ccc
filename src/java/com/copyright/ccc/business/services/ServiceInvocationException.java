package com.copyright.ccc.business.services;

import com.copyright.ccc.CCException;

/**
 * Exception class indicating the fact that 
 * underlying shared services limits were exceeded
 * during the handling of an instance of <code>PurchasablePermission</code>.
 */
public class ServiceInvocationException extends CCException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	   * Creates a new <code>ServiceInvocationException</code> with <code>null</code> message.
	   */
	    public ServiceInvocationException()
	  {
	    super();  
	  }


	  /**
	   * Creates a new <code>ServiceInvocationException</code> with the given message.
	   *
	   * @param message  the error message.
	   */
	  public ServiceInvocationException(String message)
	  {
	    super(message); 
	  }


	  /**
	   * Creates a new <code>ServiceInvocationException</code> with the given message and
	   * given cause exception.
	   *
	   * @param message  the error message.
	   * @param cause  the cause of this exception.
	   */
	    public ServiceInvocationException(String message, Throwable cause) 
	    {
	        super(message,cause);
	    }
	    
	    
	  /**
	   * Creates a new <code>ServiceInvocationException</code> with the
	   * given cause exception and its message.
	   *
	   * @param cause  the cause of this exception.
	   */
	    public ServiceInvocationException(Throwable cause) 
	    {
	        super(cause);
	    }
}
