package com.copyright.ccc.business.security;

import com.copyright.workbench.security.SecurityRuntimeException;

public class RLinkInCompleteException extends SecurityRuntimeException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new <code>SecurityRuntimeException</code> with 
     * <code>null</code> message.
     */
    public RLinkInCompleteException()
    {
        super();
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * message.
     */
    public RLinkInCompleteException( String message )
    {
        super( message );
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * cause exception.
     */
    public RLinkInCompleteException( Throwable cause )
    {
        super( cause );
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * message and cause exception.
     */
    public RLinkInCompleteException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
