package com.copyright.ccc.business.services.payment;

public class Credit extends CreditCardTransaction
{
	@Override
	protected PaymentResponse finishAction(PaymentVendor paymentVendor)
    {
    	return paymentVendor.credit();
    }

}
