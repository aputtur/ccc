package com.copyright.ccc.business.services.ecommerce;

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

SpecialOrderLimitsExceededException( PurchasablePermission purchasablePermission,
                                       LimitsExceededException limitsExceededException )
  {
    super( purchasablePermission, limitsExceededException );
  }
}