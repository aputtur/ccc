package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.CCRuntimeException;

/**
 * Exception class indicating the presence of issues when trying
 * to build an instance of <code>PurchasablePermission</code>
 */
public class UnableToBuildPurchasablePermissionException extends CCRuntimeException
{
    
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


/**
   * Creates a new <code>UnableToBuildPurchasablePermissionException</code> with <code>null</code> message.
   */
    UnableToBuildPurchasablePermissionException()
  {
    super();  
  }


  /**
   * Creates a new <code>UnableToBuildPurchasablePermissionException</code> with the given message.
   *
   * @param message  the error message.
   */
  UnableToBuildPurchasablePermissionException(String message)
  {
    super(message); 
  }


  /**
   * Creates a new <code>UnableToBuildPurchasablePermissionException</code> with the given message and
   * given cause exception.
   *
   * @param message  the error message.
   * @param cause  the cause of this exception.
   */
    UnableToBuildPurchasablePermissionException(String message, Throwable cause) 
    {
        super(message,cause);
    }
    
    
  /**
   * Creates a new <code>UnableToBuildPurchasablePermissionException</code> with the
   * given cause exception and its message.
   *
   * @param cause  the cause of this exception.
   */
   UnableToBuildPurchasablePermissionException(Throwable cause) 
    {
        super(cause);
    }
}
