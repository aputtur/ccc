package com.copyright.ccc;

import com.copyright.base.CCCException;

/**
 * Base Copyright.com exception class to be extended by any custom checked exception thrown by the application. 
 */
public class CCException extends CCCException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


/**
   * Creates a new <code>CCException</code> with <code>null</code> message.
   */
    public CCException()
  {
    super();  
  }


  /**
   * Creates a new <code>CCException</code> with the given message.
   *
   * @param message  the error message.
   */
  public CCException(String message)
  {
    super(message); 
  }


  /**
   * Creates a new <code>CCException</code> with the given message and
   * given cause exception.
   *
   * @param message  the error message.
   * @param cause  the cause of this exception.
   */
    public CCException(String message, Throwable cause) 
    {
        super(message,cause);
    }
    
    
  /**
   * Creates a new <code>CCException</code> with the
   * given cause exception and its message.
   *
   * @param cause  the cause of this exception.
   */
    public CCException(Throwable cause) 
    {
        super(cause);
    }
}
