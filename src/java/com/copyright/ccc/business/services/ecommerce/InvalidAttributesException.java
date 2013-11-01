package com.copyright.ccc.business.services.ecommerce;

import com.copyright.data.ValidationException;

/**
 * Exception that represent cases where invalid attributes were found
 * in an instance of <code>PurchasablePermission</code> in an e-commerce related activity.
 */
public class InvalidAttributesException extends ECommerceValidationException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PurchasablePermission _purchasablePermission;
  
  private InvalidAttributesException(){}
  
  InvalidAttributesException(ValidationException validationException, PurchasablePermission purchasablePermission)
  {
    super( validationException );
    setPurchasablePermission( purchasablePermission );
  }

  /**
   * Returns the instance of <code>PurchasablePermission</code> that caused this exception to be thrown.
   */
  public PurchasablePermission getPurchasablePermission()
  {
    return _purchasablePermission;
  }
  
  private void setPurchasablePermission(PurchasablePermission purchasablePermission)
  {
    this._purchasablePermission = purchasablePermission;
  }
}
