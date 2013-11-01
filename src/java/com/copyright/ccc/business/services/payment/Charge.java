package com.copyright.ccc.business.services.payment;

public class Charge extends CreditCardTransaction
{
	@Override
    protected PaymentResponse finishAction(PaymentVendor paymentVendor)
    {
    	return paymentVendor.charge();
    }
}
