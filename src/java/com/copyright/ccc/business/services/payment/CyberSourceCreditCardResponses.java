package com.copyright.ccc.business.services.payment;

import java.util.HashMap;
import java.util.Map;

public class CyberSourceCreditCardResponses 
{
    public final static String SUCCESS = "100";
    public final static String MISSING_FIELDS = "101";
    public final static String INVALID_DATA = "102";
    public final static String DUPLICATE_MERCHANT_REFERENCE = "104";
    public final static String GENERAL_SYSTEM_FAILURE = "150";
    public final static String SERVER_TIMEOUT = "151";
    public final static String DID_NOT_FINISH = "152";
    public final static String REQUEST_QUESTIONS = "201";
    public final static String EXPIRED_CARD = "202";
    public final static String GENERAL_DECLINE_CARD = "203";
    public final static String INSUFFICIENT_FUNDS = "204";
    public final static String LOST_CARD = "205";
    public final static String BANK_UNAVAIBLE = "207";
    public final static String INACTIVE_CARD = "208";
    public final static String CREDIT_LIMIT = "210";
    public final static String INVALID_CVN = "211";
    public final static String NEGATIVE_CUSTOMER = "221";
    public final static String INVALID_ACCOUNT = "231";
    public final static String INVALID_CARD_TYPE = "232";
    public final static String GENERAL_DECLINE_PROCESSOR = "233";
    public final static String CONFIGURATION_ERROR = "234";
    public final static String EXCEEDS_AMOUNT = "235";
    public final static String PROCESSOR_FAILURE = "236";
    public final static String DUPLICATE_AUTHORIZATION = "238";
    public final static String INVALID_TRANSACTION_AMOUNT = "239";
    public final static String CARD_TYPE_MISMATCH = "240";
    public final static String INVALID_REQUEST_ID = "241";
    public final static String INVALID_CAPTURE = "242";
    public final static String NOT_VOIDABLE = "246";
    public final static String ALREADY_VOIDED = "247";
    public final static String PROCESSOR_TIMEOUT = "250";
    public final static String CYBERSOURCE_DECLINED = "520";
    
    // CCC added value for exceptions thrown while processing transactions
    public final static String TRANSACTION_EXCEPTION = "999";
    
    private static Map<String, String> notifyOnError;
    private static Map<String, String> errorMessages;
    private static String contactMessage = "Please contact <a href=\"mailto:customercare@copyright.com\">customercare@copyright.com</a>.  ";
    private static String noRetryMessage = "Do not attempt to retry this request.  " + contactMessage;
    private static String unknownError = contactMessage + "Unknown Error.";
    private static String tryAnotherCard = "  Please try another card.";
    private static String updateOrTryAnotherCard = "  Please update your current card or try another one.";
    private static String fewMinutes = "  You may be able to try your request again in a few minutes.";
    
    static
    {
    	errorMessages = new HashMap<String, String>();
    	errorMessages.put(MISSING_FIELDS, contactMessage + "Your credit card request is missing one or more required fields.");
    	errorMessages.put(INVALID_DATA, contactMessage + "One or more fields in the credit card request contains invalid data");
    	errorMessages.put(DUPLICATE_MERCHANT_REFERENCE, contactMessage + "The merchantReferenceCode sent with this authorization request matches the merchantReferenceCode of another authorization request.");
    	errorMessages.put(GENERAL_SYSTEM_FAILURE, noRetryMessage + "General system failure.");
    	errorMessages.put(SERVER_TIMEOUT, noRetryMessage + "The request was received but there was a server timeout.");
    	errorMessages.put(DID_NOT_FINISH, noRetryMessage + "The request was received, but a service did not finish running in time.");
    	errorMessages.put(REQUEST_QUESTIONS, noRetryMessage + "The issuing bank has questions about the request.");
    	errorMessages.put(EXPIRED_CARD, "Expired card. You might also receive this if the expiration date you provided does not match the date the issuing bank has on file." + updateOrTryAnotherCard);
    	errorMessages.put(GENERAL_DECLINE_CARD, "General decline of the card. No other information provided by the issuing bank." + tryAnotherCard);
    	errorMessages.put(INSUFFICIENT_FUNDS, "Insufficient funds in the account." + tryAnotherCard);
    	errorMessages.put(LOST_CARD, "Your card has been reported as stolen or lost." + tryAnotherCard);
    	errorMessages.put(BANK_UNAVAIBLE, "Issuing bank unavailable." + fewMinutes);
    	errorMessages.put(INACTIVE_CARD, "Inactive card or card not authorized for card-not-present transactions." + tryAnotherCard);
    	errorMessages.put(CREDIT_LIMIT, "The card has reached the credit limit." + tryAnotherCard);
    	errorMessages.put(INVALID_CVN, "Invalid card verification number." + updateOrTryAnotherCard);
    	errorMessages.put(NEGATIVE_CUSTOMER, contactMessage + "Your customer profile has some missing information.");
    	errorMessages.put(INVALID_ACCOUNT, "Invalid credit card number." + updateOrTryAnotherCard);
    	errorMessages.put(INVALID_CARD_TYPE, "The card type is not accepted by the payment processor." + tryAnotherCard);
    	errorMessages.put(GENERAL_DECLINE_PROCESSOR, "General decline by the processor." + tryAnotherCard);
    	errorMessages.put(CONFIGURATION_ERROR, noRetryMessage + "There is a problem with your CyberSource merchant configuration.");
    	errorMessages.put(EXCEEDS_AMOUNT, contactMessage + "The requested amount exceeds the originally authorized amount.");
    	errorMessages.put(PROCESSOR_FAILURE, "The payment processing system is unavailable temporarily." + fewMinutes);
    	errorMessages.put(DUPLICATE_AUTHORIZATION, contactMessage + "The authorization has already been captured.");
    	errorMessages.put(INVALID_TRANSACTION_AMOUNT, contactMessage + "The requested transaction amount must match the previous transaction amount.");
    	errorMessages.put(CARD_TYPE_MISMATCH, "The card type sent is invalid or does not correlate with the credit card number." + updateOrTryAnotherCard);
    	errorMessages.put(INVALID_REQUEST_ID, contactMessage + "The request ID is invalid.");
    	errorMessages.put(INVALID_CAPTURE, contactMessage + "You requested a capture through the API, but there is no corresponding, unused authorization record.");
    	errorMessages.put(NOT_VOIDABLE, contactMessage + "The capture or credit is not voidable because the capture or credit information has already been submitted to your processor. Or, you requested a void for a type of transaction that cannot be voided.");
    	errorMessages.put(ALREADY_VOIDED, contactMessage + "You requested a credit for a capture that was previously voided.");
    	errorMessages.put(PROCESSOR_TIMEOUT, noRetryMessage + "The request was received, but there was a timeout at the payment processor.");
    	errorMessages.put(CYBERSOURCE_DECLINED, contactMessage + "The authorization request was approved by the issuing bank but declined by CyberSource based on your Smart Authorization settings.");

    	// CCC added value to reflect exceptions during transaction processing
    	errorMessages.put(TRANSACTION_EXCEPTION, "The authorization request generated exception while processing.");

    	notifyOnError = new HashMap<String, String>();
    	notifyOnError.put(MISSING_FIELDS, "");
    	notifyOnError.put(DUPLICATE_MERCHANT_REFERENCE, "");
    	notifyOnError.put(GENERAL_SYSTEM_FAILURE, "");
    	notifyOnError.put(SERVER_TIMEOUT, "");
    	notifyOnError.put(DID_NOT_FINISH, "");
    	notifyOnError.put(REQUEST_QUESTIONS, "");
    	notifyOnError.put(NEGATIVE_CUSTOMER, "");
    	notifyOnError.put(CONFIGURATION_ERROR, "");
    	notifyOnError.put(EXCEEDS_AMOUNT, "");
    	notifyOnError.put(DUPLICATE_AUTHORIZATION, "");
    	notifyOnError.put(INVALID_TRANSACTION_AMOUNT, "");
    	notifyOnError.put(INVALID_REQUEST_ID, "");
    	notifyOnError.put(INVALID_CAPTURE, "");
    	notifyOnError.put(NOT_VOIDABLE, "");
    	notifyOnError.put(ALREADY_VOIDED, "");
    	notifyOnError.put(PROCESSOR_TIMEOUT, "");
    	notifyOnError.put(CYBERSOURCE_DECLINED, "");
    }
    
    public static String getErrorMessage(String key)
    {
        String _message = errorMessages.get(key);	
        if (_message == null)
        {
        	return unknownError;
        }
        return _message;
    }
    
    public static boolean notifyOnError(String key)
    {
    	return notifyOnError.containsKey(key);
    }
}
