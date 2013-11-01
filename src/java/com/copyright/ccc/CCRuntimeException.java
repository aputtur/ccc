package com.copyright.ccc;

import com.copyright.base.CCCRuntimeException;

/**
 * Base Copyright.com runtime exception class to be extended by any unchecked exception thrown by the application. 
 */
public class CCRuntimeException extends CCCRuntimeException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


/**
   * Creates a new <code>CCRuntimeException</code> with <code>null</code>
   * message.
   */
    public CCRuntimeException()
  {
    super();  
  }


  /**
   * Creates a new <code>CCRuntimeException</code> with the given message.
   *
   * @param message  the error message.
   */
  public CCRuntimeException(String message)
  {
    super(message); 
  }


  /**
   * Creates a new <code>CCRuntimeException</code> with the given message and
   * given cause exception.
   *
   * @param message  the error message.
   * @param cause  the cause of this exception.
   */
    public CCRuntimeException(String message, Throwable cause) 
    {
        super(message,cause);
    }
    
    
  /**
   * Creates a new <code>CCRuntimeException</code> with the
   * given cause exception and its message.
   *
   * @param cause  the cause of this exception.
   */
    public CCRuntimeException(Throwable cause) 
    {
        super(cause);
    }
}
