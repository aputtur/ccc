package com.copyright.ccc.web.contentmgmt;

public class ContentNotFoundException extends ContentManagementException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Creates a new <code>CannotFindContentException</code> with <code>null</code> message.
     */
      public ContentNotFoundException()
    {
      super();  
    }


    /**
     * Creates a new <code>ContentManagementException</code> with the given message.
     *
     * @param message  the error message.
     */
    public ContentNotFoundException(String message)
    {
      super(message); 
    }


    /**
     * Creates a new <code>CannotFindContentException</code> with the given message and
     * given cause exception.
     *
     * @param message  the error message.
     * @param cause  the cause of this exception.
     */
      public ContentNotFoundException(String message, Throwable cause) 
      {
          super(message,cause);
      }
      
      
    /**
     * Creates a new <code>CannotFindContentException</code> with the
     * given cause exception and its message.
     *
     * @param cause  the cause of this exception.
     */
      public ContentNotFoundException(Throwable cause) 
      {
          super(cause);
      }
}
