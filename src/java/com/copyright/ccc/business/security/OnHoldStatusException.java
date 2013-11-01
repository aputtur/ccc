package com.copyright.ccc.business.security;

import com.copyright.workbench.security.SecurityRuntimeException;

public class OnHoldStatusException extends SecurityRuntimeException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new <code>SecurityRuntimeException</code> with 
     * <code>null</code> message.
     */
    public OnHoldStatusException()
    {
        super();
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * message.
     */
    public OnHoldStatusException( String message )
    {
        super( message );
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * cause exception.
     */
    public OnHoldStatusException( Throwable cause )
    {
        super( cause );
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * message and cause exception.
     */
    public OnHoldStatusException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
