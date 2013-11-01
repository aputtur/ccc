package com.copyright.ccc.business.services.cart;


/**
 * Exception class that represent cases when a special order <code>PurchasablePermission</code>
 * instance becomes a regular order one, as consequence of an e-commerce related transaction.
 */
public class ChangedToRegularOrderException extends PurchasablePermissionException
{
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private ChangedToRegularOrderException(){}
  
  ChangedToRegularOrderException( PurchasablePermission purchasablePermission )
  {
    super( purchasablePermission );
  }
}