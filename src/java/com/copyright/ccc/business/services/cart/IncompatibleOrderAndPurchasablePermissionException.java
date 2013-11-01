package com.copyright.ccc.business.services.cart;

/**
 * Exception class that indicates that the instances of <code>OrderPurchases</code> and 
 * <code>PurchasablePermission</code> provided for the transaction are incompatible.
 * (e.g. an academic <code>OrderPurchases</code> instance and a non-academic <code>PurchasablePermission</code>
 *  instance, or a non-academic <code>OrderPurchases</code> instance and an academic 
 *  instance <code>PurchasablePermission</code>), thus not being able for the transaction to be completed.
 */
public class IncompatibleOrderAndPurchasablePermissionException extends PurchasablePermissionException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private long _orderNumber;
  
  IncompatibleOrderAndPurchasablePermissionException( long orderNumber,
                                                      PurchasablePermission purchasablePermission )
  {
    super( purchasablePermission );
    setOrderNumber( orderNumber );
  }


  /**
   * Returns the confilicting order number.
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
