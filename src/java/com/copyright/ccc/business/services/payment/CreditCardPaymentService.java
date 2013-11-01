package com.copyright.ccc.business.services.payment;

import org.apache.log4j.Logger;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.services.cart.CheckoutServices;
import com.copyright.ccc.business.services.cart.CreditCardAuthorizationException;
import com.copyright.ccc.business.services.cart.CreditCardDetails;
import com.copyright.ccc.business.services.cart.CreditCardValidationException;

public class CreditCardPaymentService
{
	
	private static final Logger LOGGER = Logger.getLogger( CreditCardPaymentService.class );

	
	public static CreditCardDetails getNewCreditCardDetails() {
				return  CheckoutServices.createCreditCardDetails();
	}
	/**
	 * Handles shopping cart charge using credit card as payment method.
	 * Returns an instance of <code></code> for this transaction, if successful.
	 */
	public static PaymentResponse authorizeCreditCard(PaymentRequest paymentRequest ) 
	throws CreditCardValidationException, 
	CreditCardAuthorizationException 
	{
		
		
		
		if( paymentRequest == null )
		{
			throw new IllegalArgumentException( "chargeCard method was provided with a null paymentRequest" );
		}
		PaymentResponse paymentResponse = null;

		PreAuthorize preAuth = new PreAuthorize();
		paymentResponse = preAuth.process(paymentRequest);
		LOGGER.info("preauth: " + paymentResponse.toString());


		return paymentResponse;
	}
	
	public static PaymentResponse chargeCreditCard(PaymentResponse paymentResponse, PaymentRequest paymentRequest)
	throws CCRuntimeException 
	{
		if( paymentRequest == null )
		{
			throw new IllegalArgumentException( "chargeCard method was provided with a null paymentRequest" );
		}
		paymentRequest.setRequestID(paymentResponse.getAuthRequestID());
		paymentRequest.setRequestToken(paymentResponse.getAuthRequestToken());
		paymentResponse = new Charge().process(paymentRequest);
		LOGGER.info("charge: " + paymentResponse.toString());
		
		// fake failure response for testing
//		paymentResponse.setStatus(CyberSourceCreditCardResponses.CREDIT_LIMIT);
//		paymentResponse.setSuccess(false);

		return paymentResponse;
	}

	/**
	 * Handles credit back to the charged card using credit card as payment method.
	 * 
	 */
	public static PaymentResponse issueCredit(PaymentRequest paymentRequest) throws CCRuntimeException {
		PaymentResponse paymentResponse = null;
		if( paymentRequest == null ){
			throw new IllegalArgumentException( "PaymentRequest cannot be null" );
		}

		Credit _credit = new Credit();
		paymentResponse = _credit.process(paymentRequest);

		return paymentResponse;
	}

	

	
	
}
