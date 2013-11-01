package com.copyright.ccc.business.services.payment;
/* This data object extends the PaymentRequest object to allow the ability to pay multiple invoices.  This class simply allows for the existence of multiple invoices that will be
 * concatenated in a single string and sent in one of the cyberouce ref fields.  This should only be for reference only as the requestID should still be used to reconcile rightslink 
 * and the financial systems.
 * 
 */

import java.io.Serializable;


public class InvoicePaymentRequest extends PaymentRequest implements Serializable
{
	
	private static final long serialVersionUID = 1L;
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
    private String invoiceNumbers = null;

	public InvoicePaymentRequest(String amount, String paymentId,String invoiceList, String arReceiptNumber, String cardHolderName, String cardType, String paymentProfileId, String cccPaymentProfileId, String ccExpirationDate,Integer ccNumber, String merchantRefId, double exchangeRate)
    {
    	
    	super(amount,paymentId, arReceiptNumber,cardHolderName,cardType,paymentProfileId,cccPaymentProfileId,ccExpirationDate,ccNumber,merchantRefId,exchangeRate);
    	this.invoiceNumbers = invoiceList;
    }
    
    public String getInvoiceNumbers()
    {
		return invoiceNumbers;
	}


	public void setInvoiceNumbers(String invoices)
	{
		invoiceNumbers = invoices;
	}
	
	@Override
	public String toString()
	{
		StringBuffer _sb = new StringBuffer("amount: ").append(getAmount()).append(LINE_SEPARATOR);
		_sb.append("fundingAmount: ").append(getFundingAmount()).append(LINE_SEPARATOR);
		_sb.append("userID: ").append(getUserID()).append(LINE_SEPARATOR);
		_sb.append("paymentId: ").append(getPaymentId()).append(LINE_SEPARATOR);
		_sb.append("acctNum: ").append(getAcctNum()).append(LINE_SEPARATOR);
		_sb.append("CCCacctNum: ").append(getCCCacctNum()).append(LINE_SEPARATOR);
		_sb.append("company: ").append(getCompany()).append(LINE_SEPARATOR);
		_sb.append("authCode: ").append(getAuthCode()).append(LINE_SEPARATOR);
		_sb.append("currecyCode: ").append(getCurrencyCode()).append(LINE_SEPARATOR);
		_sb.append("firstName: ").append(getFirstName()).append(LINE_SEPARATOR);
		_sb.append("lastName: ").append(getLastName()).append(LINE_SEPARATOR);
		_sb.append("emailAddress: ").append(getEmailAddress()).append(LINE_SEPARATOR);
		_sb.append(LINE_SEPARATOR);
		_sb.append("CybersoueceSubscription: ").append(getCybersourceProfileID()).append(LINE_SEPARATOR);
		_sb.append("currencyCode: ").append(getCurrencyCode()).append(LINE_SEPARATOR);
		_sb.append("exchangeRate: ").append(getExchangeRate()).append(LINE_SEPARATOR);
		_sb.append("originalAmount: ").append(getOriginalAmount()).append(LINE_SEPARATOR);
		_sb.append("creditMemoNumber: ").append(getCreditMemoNumber()).append(LINE_SEPARATOR);
		_sb.append("requestID: ").append(getRequestID()).append(LINE_SEPARATOR);
		_sb.append("requestToken: ").append(getRequestToken()).append(LINE_SEPARATOR);
		_sb.append("followOnCredit: ").append(isFollowOnCredit()).append(LINE_SEPARATOR);
		_sb.append("InvoiceList: ").append(getInvoiceNumbers()).append(LINE_SEPARATOR);
		return _sb.toString();
	}

}