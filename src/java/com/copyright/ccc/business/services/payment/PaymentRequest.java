package com.copyright.ccc.business.services.payment;


import java.io.Serializable;

public class PaymentRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private String amount = null;
	private String fundingAmount = null;
	private String userID = null;
    int currencyRateId = 1;
	private String paymentId;
    private long acctNum;
    private long CCCacctNum;
    private String company;
    private String authCode;
    private String firstName = null;
    private String lastName = null;
    private String cardHolderName = null;
    private String address1 = null;
    private String address2 = null;
    private String address3 = null;
    private String city = null;
    private String state = null;
    private String zip = null;
    private String country = null;
    private String emailAddress = null;
    
    private String ccExpirationDate = null;
    private int ccNumber;
    private String merchantRefId = null;
    private String paymentProfileId = null;
    private String cccProfileId = null;
    private String currencyCode = null;
    private double exchangeRate = 1.0;
    private double originalAmount = 0.0;
    private String creditMemoNumber = "0";
    private String requestID = "-1";
    private String requestToken = "-1";
    private String cardType = null;
    private boolean followOnCredit = false;
    	
      
    public PaymentRequest(String amount, String paymentId, String currencyType, String cardHolderName, String cardType, String paymentProfileId, String cccPaymentProfileId, String ccExpirationDate,Integer ccNumber,String merchantRefId, double exchangeRate){
    	    this(amount,currencyType,cardType,paymentProfileId,cccPaymentProfileId,merchantRefId,paymentId,exchangeRate);
    		this.paymentId = paymentId;
    		this.cardHolderName = cardHolderName;
    		this.cccProfileId = cccPaymentProfileId;
    		this.ccExpirationDate = ccExpirationDate;
    		this.ccNumber = ccNumber.intValue();
    		this.amount =amount.trim();
        	this.currencyCode = currencyType;
        	this.cardType = cardType;
        	this.paymentProfileId = paymentProfileId;
        	this.merchantRefId = merchantRefId;
        	this.exchangeRate = exchangeRate;
    }
    public PaymentRequest(String amount, String currencyType, String cardType, String paymentProfileId, String cccPaymentProfileId, String merchantRefId, String paymentId, double exchangeRate)
    {
    	this.setAmount(amount.trim());
    	this.setCurrencyCode(currencyType);
    	this.setCardType(cardType);
    	this.setCybersourceProfileID(paymentProfileId);
    	this.setCccPaymentProfileID(cccPaymentProfileId);
    	this.setMerchantRefID(merchantRefId);
    	this.setPaymentId(paymentId);
    	this.setExchangeRate(exchangeRate);
    	this.setCreditMemoNumber(merchantRefId);
    	//this.setFundingAmount(amount.trim());
    	//this.setOriginalAmount(originalAmount);
     }
   
	public PaymentRequest() {
		// TODO Auto-generated constructor stub
	}
	/**
     * gets streetAddress1
     * @return streetAddress1 
     */
    public String getStreetAddress1()
    {
        return address1;
    }
    public void setStreetAddress1(String address1) {
		this.address1 = address1;
	}

    /**
     * gets streetAddress2
     * @return streetAddress2 
     */
    public String getStreetAddress2()
    {
        return address2;
    }
    public void setStreetAddress2(String address2) {
		this.address2 = address2;
	}

    /**
     * gets streetAddress3
     * @return streetAddress3 
     */
    public String getStreetAddress3()
    {
        return address3;
    }
    public void setStreetAddress3(String address3) {
		this.address3 = address3;
	}

    /**
     * gets city
     * @return city 
     */
    public String getCity()
    {
        return city;
    }
    public void setCity(String city) {
		this.city = city;
	}

    /**
     * gets state
     * @return state 
     */
    public String getState()
    {
        return state;
    }
    public void setState(String state) {
		this.state = state;
	}

    /**
     * gets postalCode
     * @return postalCode 
     */
    public String getPostalCode()
    {
        return zip;
    }
    public void setPostalCode(String zip) {
		this.zip = zip;
	}
    /**
     * gets country
     * @return country 
     */
    public String getCountry()
    {
        return country;
    }
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * gets addresseName
     * @return addresseName 
     */
    
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return Returns the amount.
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param paymentId The paymentId to set.
	 */
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	/**
	 * @return Returns the licenseNumber.
	 */
	public String getPaymentId() {
		return paymentId;
	}

	/**
	 * @param acctNum The acctNum to set.
	 */
	public void setAcctNum(long acctNum) {
		this.acctNum = acctNum;
	}

	/**
	 * @return Returns the acctNum.
	 */
	public long getAcctNum() {
		return acctNum;
	}

	/**
	 * @param company The company to set.
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return Returns the company.
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param authCode The authCode to set.
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/**
	 * @return Returns the authCode.
	 */
	public String getAuthCode() {
		return authCode;
	}
	
	/**
	 * @param cardHolderName The cardHolderName to set.
	 */
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	/**
	 * @return Returns the cardHolderName.
	 */
	public String getCardHolderName() {
		return cardHolderName;
	}
	
	/**
	 * @param cardExpirationDate The cardExpirationDate to set.
	 */
	public void setCardExpirationDate(String ccExpirationDate) {
		this.ccExpirationDate = ccExpirationDate;
	}

	/**
	 * @return Returns the cardExpirationDate.
	 */
	public String getCardExpirationDate() {
		return ccExpirationDate;
	}
	
	/**
	 * @param cardLastFourDigits The cardNumber to set.
	 */
	public void setLastFourDigits(Integer ccNumber) {
		if (ccNumber != null){
			this.ccNumber = ccNumber.intValue();
		}
	}

	/**
	 * @return Returns the cardLastFourDigits.
	 */
	public int getLastFourDigits() {
		return ccNumber;
	}

	/**
	 * @param currencyRateId The currencyRateId to set.
	 */
	public void setCurrencyRateId(int currencyRateId) {
		this.currencyRateId = currencyRateId;
	}

	/**
	 * @return Returns the invoiceNumber.
	 */
	public int getCurrencyRateId() {
		return currencyRateId;
	}

	/**
	 * @param userID The userID to set.
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return Returns the userID.
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param cardType The userID to set.
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return Returns the cardType.
	 */
	public String getCardType() {
		return this.cardType;
	}
	
	
	/**
	 * @param currencyCode The currencyCode to set.
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return Returns the currencyCode.
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	
    public void cleanup()
    {
    	
    }

	/**
	 * @param emailAddress The emailAddress to set.
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return Returns the emailAddress.
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param cCCacctNum The cCCacctNum to set.
	 */
	public void setCCCacctNum(long cCCacctNum) {
		CCCacctNum = cCCacctNum;
	}

	/**
	 * @return Returns the cCCacctNum.
	 */
	public long getCCCacctNum() {
		return CCCacctNum;
	}

	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param exchangeRate The exchangeRate to set.
	 */
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	/**
	 * @return Returns the exchangeRate.
	 */
	public double getExchangeRate() {
		return exchangeRate;
	}

	/**
	 * @param originalAmount The originalAmount to set.
	 */
	public void setOriginalAmount(double originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @return Returns the originalAmount.
	 */
	public double getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @param creditMemoNumber The creditMemoNumber to set.
	 */
	public void setCreditMemoNumber(String creditMemoNumber) {
		this.creditMemoNumber = creditMemoNumber;
	}

	/**
	 * @return Returns the creditMemoNumber.
	 */
	public String getCreditMemoNumber() {
		return creditMemoNumber;
	}
	
	/**
	 * @param fundingAmount The fundingAmount to set.
	 */
	public void setFundingAmount(String fundingAmount) {
		this.fundingAmount = fundingAmount;
	}

	/**
	 * @return Returns the fundingAmount.
	 */
	public String getFundingAmount() {
		return fundingAmount;
	}

	/**
	 * @param requestID The requestID to set.
	 */
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	/**
	 * @return Returns the requestID.
	 */
	public String getRequestID() {
		return requestID;
	}

	/**
	 * @param paymentProfileId The paymentProfileId to set.
	 */
	public void setCybersourceProfileID(String paymentProfileId) {
		this.paymentProfileId = paymentProfileId;
	}

	/**
	 * @return Returns the requestID.
	 */
	public String getCybersourceProfileID() {
		return paymentProfileId;
	}
	
	/**
	 * @param merchantRefId The merchantRefId to set.
	 */
	public void setMerchantRefID(String merchantRefId) {
		this.merchantRefId = merchantRefId;
	}

	/**
	 * @return Returns the requestID.
	 */
	public String getMerchantRefID() {
		return merchantRefId;
	}
	
	/**
	 * @param cccProfileId The cccProfileId to set.
	 */
	public void setCccPaymentProfileID(String cccProfileId) {
		this.cccProfileId = cccProfileId;
	}

	/**
	 * @return Returns the requestID.
	 */
	public String getCccPaymentProfileID() {
		return cccProfileId;
	}
	
	/**
	 * @param followOnCredit The followOnCredit to set.
	 */
	public void setFollowOnCredit(boolean followOnCredit) {
		this.followOnCredit = followOnCredit;
	}

	/**
	 * @return Returns the followOnCredit.
	 */
	public boolean isFollowOnCredit() {
		return followOnCredit;
	}

	/**
	 * @param requestToken The requestToken to set.
	 */
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	/**
	 * @return Returns the requestToken.
	 */
	public String getRequestToken() {
		return requestToken;
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
		_sb.append("CybersourceSubscription: ").append(getCybersourceProfileID()).append(LINE_SEPARATOR);
		_sb.append("currencyCode: ").append(getCurrencyCode()).append(LINE_SEPARATOR);
		_sb.append("exchangeRate: ").append(getExchangeRate()).append(LINE_SEPARATOR);
		_sb.append("originalAmount: ").append(getOriginalAmount()).append(LINE_SEPARATOR);
		_sb.append("creditMemoNumber: ").append(getCreditMemoNumber()).append(LINE_SEPARATOR);
		_sb.append("requestID: ").append(getRequestID()).append(LINE_SEPARATOR);
		_sb.append("requestToken: ").append(getRequestToken()).append(LINE_SEPARATOR);
		_sb.append("followOnCredit: ").append(isFollowOnCredit()).append(LINE_SEPARATOR);
		return _sb.toString();
	}
	
}

