package com.copyright.ccc.business.services.payment;

public class PreAuthorize extends CreditCardTransaction
{
	@Override
	protected PaymentResponse finishAction(PaymentVendor paymentVendor)
	{
		return paymentVendor.preAuthorize();
    }
}
