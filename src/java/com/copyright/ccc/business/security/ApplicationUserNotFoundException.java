package com.copyright.ccc.business.security;

import com.copyright.ccc.CCRuntimeException;

public class ApplicationUserNotFoundException extends CCRuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Creates a new <code>CCRuntimeException</code> with <code>null</code>
     * message.
     */
      public ApplicationUserNotFoundException()
    {
      super();  
    }


    /**
     * Creates a new <code>CCRuntimeException</code> with the given message.
     *
     * @param message  the error message.
     */
    public ApplicationUserNotFoundException(String message)
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
      public ApplicationUserNotFoundException(String message, Throwable cause) 
      {
          super(message,cause);
      }
      
      
    /**
     * Creates a new <code>CCRuntimeException</code> with the
     * given cause exception and its message.
     *
     * @param cause  the cause of this exception.
     */
      public ApplicationUserNotFoundException(Throwable cause) 
      {
          super(cause);
      }
}
