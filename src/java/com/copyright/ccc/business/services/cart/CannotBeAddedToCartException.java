package com.copyright.ccc.business.services.cart;

/**
 * Exception class indicating that an instance of <code>PurchasablePermission</code>
 * cannot be added to the shopping cart during an e-commerce transaction.
 */
public class CannotBeAddedToCartException extends PurchasablePermissionException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

CannotBeAddedToCartException( PurchasablePermission purchasablePermission )
  {
    super( purchasablePermission );
  }
}
