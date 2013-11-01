package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.data.pricing.LimitsExceededException;

/**
 * Exception class indicating the fact that quantities limits were exceeded
 * during the handling of an instance of <code>PurchasablePermission</code>, thus becoming a special order.
 */
public class SpecialOrderLimitsExceededException extends CCLimitsExceededException
{
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public SpecialOrderLimitsExceededException( TransactionItem transactionItem,
                                       LimitsExceededException limitsExceededException )
  {
    super( transactionItem, limitsExceededException );
  }
}