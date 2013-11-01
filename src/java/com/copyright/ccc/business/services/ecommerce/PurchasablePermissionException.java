package com.copyright.ccc.business.services.ecommerce;

import com.copyright.ccc.CCException;

/**
 * Abstract exception class indicated <code>PurchasablePermission</code>-related issues.
 */
public abstract class PurchasablePermissionException extends CCException
{
	private static final long serialVersionUID = 1L;
	
  private PurchasablePermission _purchasablePermission;
  
  PurchasablePermissionException(){}
  
  PurchasablePermissionException( PurchasablePermission purchasablePermission )
  {
    setPurchasablePermission( purchasablePermission ) ;
  }

  /**
   * Returns the conflicting <code>PurchasablePermission</code> instance.
   */
  public final PurchasablePermission getPurchasablePermission()
  {
    return _purchasablePermission;
  }

  protected void setPurchasablePermission(PurchasablePermission purchasablePermission)
  {
    this._purchasablePermission = purchasablePermission;
  }
}
