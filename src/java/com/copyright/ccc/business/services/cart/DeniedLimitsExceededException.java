package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.data.pricing.LimitsExceededException;

/**
 * Exception class indicating the fact that quantities limits were exceeded
 * during the handling of an instance of <code>PurchasablePermission</code>, thus becoming denied.
 */
public class DeniedLimitsExceededException extends CCLimitsExceededException
{
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public DeniedLimitsExceededException( TransactionItem transactionItem,
                                 LimitsExceededException limitsExceededException )
  {
    super( transactionItem, limitsExceededException );
  }
}