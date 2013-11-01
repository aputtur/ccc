package com.copyright.ccc.business.services.cart;

/**
 * Represent cases where an instance of <code>PurchsabablePermission</code>
 * could not be removed from the shopping cart.
 */
public class CannotBeRemovedFromCartException extends PurchasablePermissionException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private CannotBeRemovedFromCartException()
  {
  }
  
  CannotBeRemovedFromCartException( PurchasablePermission purchasablePermission )
  {
    super( purchasablePermission );
  }
}
