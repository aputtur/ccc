package com.copyright.ccc.business.services.cart;


import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.payment.PaymentRequest;
import com.copyright.ccc.business.services.payment.PaymentResponse;
import com.copyright.ccc.business.services.user.User;

public class TestChargeCard {
	
	/*
	public void testChargeCard(){
		PaymentRequest paymentRequest = null;
		paymentRequest.setCccPaymentProfileID("123456789");
		try {
			PaymentResponse _paymentResponse = CheckoutServices.chargeCard( paymentRequest );
		} catch (CreditCardValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreditCardAuthorizationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CashierCheckoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	public void testIssueCredit(){
		User cccUser = UserContextService.getSharedUser();
		double exchangeRate = 1.0;
		Integer lastFour = new Integer("1111");
		PaymentResponse _paymentResponse = null;
				
		PaymentRequest _paymentRequest = new PaymentRequest();
		 _paymentRequest.setAmount("110");
		 _paymentRequest.setPaymentId("10002689");
		 _paymentRequest.setCurrencyCode("USD");
		 _paymentRequest.setCardHolderName("Raju Pandeti");
		 _paymentRequest.setCardType("VISA");
		 _paymentRequest.setCybersourceProfileID("2835462004500176056203");
		 _paymentRequest.setCccPaymentProfileID("10000152");
		 _paymentRequest.setCardExpirationDate("01/2013");
		 _paymentRequest.setLastFourDigits(lastFour);
		 _paymentRequest.setMerchantRefID("CCOM200808");
		 _paymentRequest.setExchangeRate(exchangeRate);
		 _paymentRequest.setRequestID("2850196627170008284268"); //very important for credit
		 _paymentRequest.setRequestToken("-1");                  
		 _paymentRequest.setCreditMemoNumber("CCOM200808"); // same as merchantRefId
		 _paymentRequest.setFollowOnCredit(true);    // less than 60 days set true otherwise false
		 
		 
				 
		CheckoutServices.setUserInfoOnPaymentRequest(cccUser,_paymentRequest);	
			_paymentResponse = CheckoutServices.issueCredit(_paymentRequest);
	}
}
