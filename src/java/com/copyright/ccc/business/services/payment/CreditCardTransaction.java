package com.copyright.ccc.business.services.payment;

public abstract class CreditCardTransaction 
{
    public PaymentResponse process(PaymentRequest paymentRequest)
    {
    	PaymentResponse _paymentResponse = null;
		PaymentVendor _paymentVendor = PaymentServiceMgr.getPaymentVendor(paymentRequest);

		try
		{
			_paymentResponse = finishAction(_paymentVendor);
		}
		finally
		{
			if (paymentRequest != null)
			{
				paymentRequest.cleanup();
			}
		}
		return _paymentResponse;
    }
    
    protected abstract PaymentResponse finishAction(PaymentVendor paymentVendor);
}
