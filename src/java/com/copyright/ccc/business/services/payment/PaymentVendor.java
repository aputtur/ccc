package com.copyright.ccc.business.services.payment;

/**  This class represents a Payment Vendor. All payment vendors must inherit from this
 *   class and must implement these methods
 */
public abstract class PaymentVendor
{
    protected PaymentRequest paymentRequest = null;
   
    abstract public PaymentResponse preAuthorize();
    abstract public PaymentResponse charge();
    abstract public PaymentResponse credit();
    
	
	
}