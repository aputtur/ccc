package com.copyright.ccc.business.services.cart;

/**
 * Exception class indicatin that an order is closed, thus
 * unable to accept items to be appended to it.
 */
public class OrderClosedException extends PurchasablePermissionException
{
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private long _orderNumber;
  
  OrderClosedException( long orderNumber,
                        PurchasablePermission purchasablePermission )
  {
    super( purchasablePermission );
    setOrderNumber( orderNumber );
  }

  /**
   * Returns the conflicting order number.
   */
  public long getOrderNumber()
  {
    return _orderNumber;
  }

  private void setOrderNumber( long orderNumber )
  {
    this._orderNumber = orderNumber;
  }
  
}
