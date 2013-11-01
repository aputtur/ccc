package com.copyright.ccc.business.security;

import com.copyright.workbench.security.SecurityRuntimeException;

public class AuthenticationFailureException extends SecurityRuntimeException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new <code>SecurityRuntimeException</code> with 
     * <code>null</code> message.
     */
    public AuthenticationFailureException()
    {
        super();
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * message.
     */
    public AuthenticationFailureException( String message )
    {
        super( message );
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * cause exception.
     */
    public AuthenticationFailureException( Throwable cause )
    {
        super( cause );
    }
    
    /**
     * Creates a new <code>SecurityRuntimeException</code> with the specified 
     * message and cause exception.
     */
    public AuthenticationFailureException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
