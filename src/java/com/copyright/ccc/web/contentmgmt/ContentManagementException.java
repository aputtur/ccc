package com.copyright.ccc.web.contentmgmt;

import com.copyright.ccc.CCException;

public class ContentManagementException extends CCException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Creates a new <code>ContentManagementException</code> with <code>null</code> message.
     */
      public ContentManagementException()
    {
      super();  
    }


    /**
     * Creates a new <code>ContentManagementException</code> with the given message.
     *
     * @param message  the error message.
     */
    public ContentManagementException(String message)
    {
      super(message); 
    }


    /**
     * Creates a new <code>ContentManagementException</code> with the given message and
     * given cause exception.
     *
     * @param message  the error message.
     * @param cause  the cause of this exception.
     */
      public ContentManagementException(String message, Throwable cause) 
      {
          super(message,cause);
      }
      
      
    /**
     * Creates a new <code>ContentManagementException</code> with the
     * given cause exception and its message.
     *
     * @param cause  the cause of this exception.
     */
      public ContentManagementException(Throwable cause) 
      {
          super(cause);
      }
}
