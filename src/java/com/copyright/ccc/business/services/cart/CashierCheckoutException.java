package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.CCException;

/**
 * Wrapper checked exception class for <code>com.copyright.data.order.cashier.CashierCheckoutException</code>
 */
public class CashierCheckoutException extends CCException
{

      /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private com.copyright.data.order.cashier.CashierCheckoutException _sharedServiceCashierCheckoutException;

        
      CashierCheckoutException( com.copyright.data.order.cashier.CashierCheckoutException cce )
      {
        if( cce == null )
        {
          throw new IllegalArgumentException( "An instance of com.copyright.data.order.cashier.CashierCheckoutException must be provided ");
        }
        
        _sharedServiceCashierCheckoutException = cce;
      }

  /**
   * Returns the checkout error code provided by the wrapped <code>com.copyright.data.order.cashier.CashierCheckoutException</code>
   */
  public String getCheckoutErrorCode()
      {
        return _sharedServiceCashierCheckoutException.getCheckoutErrorCode();
      }

}
