package com.copyright.ccc.business.services.ecommerce;

import com.copyright.data.pricing.LimitsExceededException;

/**
 * Exception class indicating the fact that quantities limits were exceeded
 * during the handling of an instance of <code>PurchasablePermission</code>
 * in an e-commerce transaction, thus its right status becoming "Contact Rightsholder Directly".
 */
public class ContactRHDirectlyLimitsExceededException extends CCLimitsExceededException
{
      
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

ContactRHDirectlyLimitsExceededException( PurchasablePermission purchasablePermission, 
                                            LimitsExceededException limitsExceededException )
  {
    super( purchasablePermission, limitsExceededException );
  }
}
