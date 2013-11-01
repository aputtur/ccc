package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.data.pricing.LimitsExceededException;

/**
 * Exception class indicating the fact that 
 * underlying shared services limits were exceeded
 * during the handling of an instance of <code>PurchasablePermission</code>.
 */
public class SystemLimitsExceededException extends CCLimitsExceededException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public SystemLimitsExceededException( TransactionItem transactionItem,
                                 LimitsExceededException limitsExceededException )
  {
    super( transactionItem, limitsExceededException );
  }
}
