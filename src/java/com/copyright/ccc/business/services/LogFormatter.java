package com.copyright.ccc.business.services;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.cart.CreditCardDetails;
import com.copyright.svc.order.api.data.Cart;
import com.copyright.svc.order.api.data.Payment;

/**
 * Helper class that centralizes the logic for creating
 * a log string for various data objects
 * 
 * @author jarbo
 *
 */
public class LogFormatter {
	/**
	 * build a String that can be logged for diagnosing
	 * cart issues
	 */
	public static String format(Cart cart) {
		StringBuilder sb = new StringBuilder();
		sb.append("Cart Data: ");
		if (cart!=null) {
			sb.append("cartId=").append(cart.getCartId()).append(";");
			sb.append("buyerPtyInst=").append(cart.getBuyerPtyInst()).append(";");
			sb.append("buyerName=").append(cart.getBuyerName()).append(";");
			sb.append("buyerPartyId=").append(cart.getBuyerPartyId()).append(";");
			sb.append("licenseeName=").append(cart.getLicenseeName()).append(";");
			sb.append("licenseeAccount=").append(cart.getLicenseeAccount()).append(";");
			sb.append("licenseePtyInst=").append(cart.getLicenseePtyInst()).append(";");
			sb.append("licenseePartyId=").append(cart.getLicenseePartyId()).append(";");
		} else {
			sb.append(" cart is null;");
		}
		appendActiveUser(sb);
		appendAuthUser(sb);
		return sb.toString();		
	}
	
	/**
	 * build a String that can be logged for diagnosing
	 * credit card issues
	 */
	public static String format(CreditCardDetails creditCardDetails) {
		StringBuilder sb = new StringBuilder();
		sb.append("CreditCardDetails Data: ");
		if (creditCardDetails!=null) {
			sb.append("paymentProfileCccId=").append(creditCardDetails.getPaymentProfileCccId()).append(";");
			sb.append("paymentId=").append(creditCardDetails.getPaymentId()).append(";");				
		} else {
			sb.append(" creditCardDetails is null;");
		}
		appendActiveUser(sb);
		appendAuthUser(sb);
		return sb.toString();		
	}

	/**
	 * build a String that can be logged for diagnosing
	 * payment issues
	 */
	public static String format(Payment payment) {
		StringBuilder sb = new StringBuilder();
		sb.append("Payment Data: ");
		if (payment!=null) {
			sb.append("paymentMethodCd=").append(payment.getPaymentMethodCd()).append(";");
			sb.append("paymentId=").append(payment.getPaymentId()).append(";");
		} else {
			sb.append("payment is null;");
		}
		appendActiveUser(sb);
		appendAuthUser(sb);
		return sb.toString();		
	}
	
	private static void appendActiveUser(StringBuilder sb) {
		if (UserContextService.getActiveAppUser()!=null) {
			sb.append("activeAppUser=").append(UserContextService.getActiveAppUser().getUsername()).append(";");		
		}
	}
	private static void appendAuthUser(StringBuilder sb) {
		if (UserContextService.getAuthenticatedAppUser()!=null) {
			sb.append("authAppUser=").append(UserContextService.getAuthenticatedAppUser().getUsername()).append(";");				
		}
	}
	
}
