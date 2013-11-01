package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.CCException;
import com.copyright.ccc.business.data.TransactionItem;

/**
 * Abstract exception class indicated <code>PurchasablePermission</code>-related issues.
 */
public abstract class TransactionItemException extends CCException
{
	private static final long serialVersionUID = 1L;
	
  private TransactionItem _transactionItem;
  
  TransactionItemException(){}
  
  TransactionItemException( TransactionItem transactionItem )
  {
    setTransactionItem( transactionItem ) ;
  }

  /**
   * Returns the conflicting <code>PurchasablePermission</code> instance.
   */
  public final TransactionItem getTransactionItem()
  {
    return _transactionItem;
  }

  protected void setTransactionItem(TransactionItem transactionItem)
  {
    this._transactionItem = transactionItem;
  }
}
