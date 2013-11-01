package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.business.data.TransactionItem;
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
private TransactionItem _transactionItem;
  
  private InvalidAttributesException(){}
  
  public InvalidAttributesException(ValidationException validationException, TransactionItem transactionItem)
  {
    super( validationException );
    setTransactionItem( transactionItem );
  }

  /**
   * Returns the instance of <code>PurchasablePermission</code> that caused this exception to be thrown.
   */
  public TransactionItem getTransactionItem()
  {
    return _transactionItem;
  }
  
  private void setTransactionItem(TransactionItem transactionItem)
  {
    this._transactionItem = transactionItem;
  }
}
