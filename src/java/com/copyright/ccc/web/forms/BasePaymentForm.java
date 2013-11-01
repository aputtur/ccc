package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.copyright.base.enums.PaymentGateway;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.CheckoutServices;
import com.copyright.ccc.business.services.cart.CreditCardDetails;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.data.ValidationException;
import com.copyright.data.payment.CreditCard;
import com.copyright.data.payment.PaymentMethod;
import com.copyright.mail.MailDispatcher;
import com.copyright.mail.MailDispatcherImpl;
import com.copyright.mail.MailMessage;
import com.copyright.mail.MailSendFailedException;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.Name;
import com.copyright.svc.telesales.api.data.Organization;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo;

/**
 * 
 * @author APuttur
 */
public class BasePaymentForm extends CCValidatorForm
{
    /**
     * Form to handle the display/selection and payment
     * of unpaid invoices.
     */
    private static final long serialVersionUID = 1L;
    
    public static final String CREDIT_TYPE = "credit-card";
    public static final String INVOICE_TYPE = "invoice";
    public static final String NA = "n/a";
    public static final String VISA_NAME = "Visa";
    public static final String AMEX_NAME = "American Express";
    public static final String MC_NAME = "MasterCard";
    public static final String USD_NAME = "USD - $";
    public static final String EUR_NAME = "EUR - €";
    public static final String GBP_NAME = "GBP - £";
    public static final String JPY_NAME = "JPY - ¥";
    
    //private static final Logger _logger = Logger.getLogger(UnpaidInvoiceForm.class);
    private boolean cybersourceSiteUp = SystemStatus.isCybersourceSiteUp();
    
    public static final String PAYMENT_TYPE_INVOICE = "invoice";
    public static final String PAYMENT_TYPE_CREDIT_CARD = "credit-card";
    
    private CreditCardDetails creditCardDetails;
    private  PaymentMethod _creditCardInfo;
    
    private String paymentType; //"credit-card or invoice
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userCompany;
    
    
    
    private String currencyType;
    private int currencyRateId;
    private Date currencyRateDate;
    
    // added new property this was saved in session
    public String  fundingCurrencyType; 
    public Integer fundingCurrencyRateId;
    public String  fundingCurrencyAmount;  
    public String  exchangeRate;
    private int    exchangeRateId;
    public String  exchangeRateDate;
    
    //Credit card details
    private String creditCardType;
    private String paymentProfileId;
    private String cccPaymentProfileId;
    
    private String creditCardNumber;
    private String creditCardNameOn;
    private String expirationDate;
    private Collection<LabelValueBean> ccOptions;
    private Collection<String> currencyOptions;
    private String expMonth;
    private Collection<LabelValueBean> expMonthOptions;
    private String expYear;
    private Collection<LabelValueBean> expYearOptions;
    
    

	private String firstName;
	private String lastName;
	private String hopURL;
	private String cccURL;
	private String responseEmail;
	private String signature;

	private List<PaymentProfileInfo> creditCards;
	private String subscriptionSignature;

// Address
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;
    private String billToContactName;
    private String billToEmail;
    private String billToPhone;
	
	// Purchase Order Number  & total
    private String purchaseOrderNumber = "";
    private String chargeTotal;
    
    
    public void setCurrencyRateId( int currencyRateId )
    {
    	this.currencyRateId = currencyRateId;
    }

    public int getCurrencyRateId()
    {
        return currencyRateId;
    }
    public void setCurrencyType( String currencyType )
    {
    	this.currencyType = currencyType;
    	
    }

    public String getCurrencyType()
    {
        return currencyType;
    }
    public void setCartChargeTotal(String chargeTotal)
    {
        this.chargeTotal = chargeTotal;
        
    }

    public String getCartChargeTotal()
    {
        return chargeTotal;
    }
   
    public BasePaymentForm() {
       reSet();
    }

    public void reSet()
    {
    	   _creditCardInfo = new CreditCard();
           purchaseOrderNumber = "";
           this.setPaymentType(PAYMENT_TYPE_INVOICE);
           this.currencyType ="USD";
           
           setExpMonthOptions();
           setExpYearOptions();
    }
    public boolean getCybersourceSiteUp()
    {
		return cybersourceSiteUp;
	}

	public void setCybersourceSiteUp(boolean cybersourceSiteUp)
	{
		this.cybersourceSiteUp = cybersourceSiteUp;
	}

   
    public void setPaymentType( String payType )
    {
        this.paymentType = payType;
    }

    public String getPaymentType()
    {
        return paymentType;
    }

    public void setPurchaseOrderNumber( String purchaseOrderNumber )
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getPurchaseOrderNumber()
    {
        return purchaseOrderNumber;
    }

    //  *************************************************************
    //  Credit Card section...
    
    //********************* Add *******************************************
    
    public void setPaymentProfileId( String paymentProfileId )
    {
        this.paymentProfileId = paymentProfileId;
    }

    public String getPaymentProfileId()
    {
        return paymentProfileId;
    }
    
    public void setCccPaymentProfileId( String cccPaymentProfileId )
    {
        this.cccPaymentProfileId = cccPaymentProfileId;
    }

    public String getCccPaymentProfileId()
    {
        return cccPaymentProfileId;
    }
    
    public void setCreditCardType( String creditCardType )
    {
        this.creditCardType = creditCardType;
    }

    public String getCreditCardType()
    {
        return creditCardType;
    }
    
    public void setCreditCardNumber( String creditCardNumber )
    {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber()
    {
        return this.creditCardNumber;
    }
    
    public void setCreditCardNameOn( String creditCardNameOn )
    {
        this.creditCardNameOn = creditCardNameOn;
        
    }

    public String getCreditCardNameOn()
    {
    	return creditCardNameOn;
       
    }
    
    public void setExpirationDate( String expirationDate )
    {
        this.expirationDate = expirationDate;
    }

    public String getExpirationDate()
    {
        return expirationDate;
    }
    
    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getFirstName()
    {
        return firstName;
    }
    
    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    public String getLastName()
    {
        return lastName;
    }
    
    public void setHopURL( String hopURL )
    {
        this.hopURL = hopURL;
    }

    public String getHopURL()
    {
    	return hopURL;
    }
    
    public void setCccURL( String url )
    {
    	this.cccURL = url;
    }

    public String getCccURL()
    {
        return cccURL;
    }
    
    public void setResponseEmail( String responseEmail )
    {
        this.responseEmail = responseEmail;
    }

    public String getResponseEmail()
    {
        return responseEmail;
    }
    
    public void setSignature( String signature )
    {
        this.signature = signature;
    }

    public String getSignature()
    {
        return signature;
    }
    
    public void setCreditCards(List<PaymentProfileInfo> creditCards )
    {
        this.creditCards = creditCards;
        /*
        for( PaymentProfileInfo profileInfo:creditCards){
        	String cardType = profileInfo.getCardType();
        	System.out.println(cardType);
			
    	} */
    }

    public List<PaymentProfileInfo> getCreditCards()
    {
        return creditCards;
    }
    
    public void setSubscriptionSignature( String subscriptionSignature )
    {
        this.subscriptionSignature = subscriptionSignature;
    }

    public String getSubscriptionSignature()
    {
        return subscriptionSignature;
    }

    public void setAddress1( String address1 )
    {
        this.address1 = address1;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress2( String address2 )
    {
        this.address2 = address2;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }

    public void setState( String state )
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }

    public void setZip( String zip )
    {
        this.zip = zip;
    }

    public String getZip()
    {
        return zip;
    }
    
    public void setCountry( String country )
    {
        this.country = country;
    }

    public String getCountry()
    {
        return country;
    }

    public void setBillToContactName( String billName )
    {
        this.billToContactName = billName;
    }

    public String getBillToContactName()
    {
        return billToContactName;
    }
    
    public void setBillToEmail( String email )
    {
        this.billToEmail = email;
    }

    public String getBillToEmail()
    {
        return billToEmail;
    }
    

    
    public void setBillToPhone( String phone )
    {
        this.billToPhone = phone;
    }

    public String getBillToPhone()
    {
        return billToPhone;
    }
    public void setFundingCurrencyType( String fundingCurrencyType )
    {
        this.fundingCurrencyType = fundingCurrencyType;
        
    }

    public String getFundingCurrencyType()
    {
        return fundingCurrencyType;
    }
    
    public void setFundingCurrencyRateId( Integer fundingCurrencyRateId )
    {
        this.fundingCurrencyRateId = fundingCurrencyRateId.intValue();
    }

    public int getFundingCurrencyRateId()
    {
        return fundingCurrencyRateId;
    }
    
    public void setCurrencyRateDate(Date currencyRateDate )
    {
        this.currencyRateDate = currencyRateDate;
    }

    public Date getCurrencyRateDate()
    {
        return currencyRateDate;
    }
    
    public void setExchangeRateDate(String exchangeRateDate )
    {
        this.exchangeRateDate = exchangeRateDate;
    }

    public String getExchangeRateDate()
    {
        return exchangeRateDate;
    }
    
    public void setFundingCurrencyAmount( String fundingCurrencyAmount )
    {
    	this.fundingCurrencyAmount = fundingCurrencyAmount;
    	
    }

    public String getFundingCurrencyAmount()
    {
        return fundingCurrencyAmount;
    }
    
    public void setExchangeRate( String exchangeRate )
    {
        this.exchangeRate = exchangeRate;
    }

    public String getExchangeRate()
    {
        return exchangeRate;
    }
    public void setExchangeRateId( int exchangeRateId )
    {
    	this.exchangeRateId = exchangeRateId;
    }

    public int getExchangeRateId()
    {
        return exchangeRateId;
    }
    
    //********************************************************************

    public PaymentMethod getPaymentMethod() { return _creditCardInfo; }
    public String getCardholderName() { return ((CreditCard) _creditCardInfo).getCardholderName(); }
    public String getCCExpiration() {
        String expirationDate = null;

        if (((CreditCard) _creditCardInfo).getCcExpirationMon() != null &&
            ((CreditCard) _creditCardInfo).getCcExpirationYear() != null)
        {
            expirationDate = ((CreditCard) _creditCardInfo).getCcExpirationMon()
                + "/" + ((CreditCard) _creditCardInfo).getCcExpirationYear();
        }

        return expirationDate;
    }
    public String getLastFourDigits() { return ((CreditCard) _creditCardInfo).getLast4Digits(); }
    public String getCreditCardName() { return ((CreditCard) _creditCardInfo).getType().getName(); }
    public String getCcType() { return ((CreditCard) _creditCardInfo).getCcType(); }
    public String getCcExpirationMonth() { return ((CreditCard) _creditCardInfo).getCcExpirationMon(); }
    public String getCcExpirationYear() { return ((CreditCard) _creditCardInfo).getCcExpirationYear(); }
    public String getCcNumber() { return ((CreditCard) _creditCardInfo).getCcNumber(); }

    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public String getUserPhone() { return userPhone; }
    public String getUserCompany() { return userCompany; }

   // public Collection<LabelValueBean> getExpMonths() { return _ccExpMonths; }
   // public Collection<LabelValueBean> getExpYears() { return _ccExpYears; }

   
    public void setCardholderName(String ccName) { ((CreditCard) _creditCardInfo).setCardholderName(ccName); }
    public void setCardSecurityCode(String ccCode) { ((CreditCard) _creditCardInfo).setCardSecurityCode(ccCode); }
    public void setCcExpirationMonth(String ccExpMon) { ((CreditCard) _creditCardInfo).setCcExpirationMon(ccExpMon); }
    public void setCcExpirationYear(String ccExpYear) { ((CreditCard) _creditCardInfo).setCcExpirationYear(ccExpYear); }
    public void setCcNumber(String ccNumber) { ((CreditCard) _creditCardInfo).setCcNumber(ccNumber); }
    public void setCcType(String ccType) { ((CreditCard) _creditCardInfo).setCcType(ccType); }
    public void clearSecureData() { ((CreditCard) _creditCardInfo).clearSecureData(); }
   

    public void setUserName(String uname) { userName = uname; }
    public void setUserEmail(String email) { userEmail = email; }
    public void setUserPhone(String phone) { userPhone = phone; }
    public void setUserCompany(String company) { userCompany = company; }
   
    public void setCurrencyOptions(ArrayList<String> currencyOptions )
    {
        this.currencyOptions = currencyOptions;
    }

    public ArrayList<String> getCurrencyOptions()
    {
        return (ArrayList<String>) currencyOptions;
    }


    public void setCcOptions( Collection<LabelValueBean> ccOptions )
    {
        this.ccOptions = ccOptions;
    }

    public Collection<LabelValueBean> getCcOptions()
    {
        return ccOptions;
    }
    public void setExpMonthOptions( Collection<LabelValueBean> expMonthOptions )
    {
        this.expMonthOptions = expMonthOptions;
    }

    public Collection<LabelValueBean> getExpMonthOptions()
    {
        return expMonthOptions;
    }

    public void setExpYearOptions( Collection<LabelValueBean> expYearOptions )
    {
        this.expYearOptions = expYearOptions;
    }

    public Collection<LabelValueBean> getExpYearOptions()
    {
        return expYearOptions;
    }

    public void setExpMonth( String expMonth )
    {
        this.expMonth = expMonth;
    }

    public String getExpMonth()
    {
        return expMonth;
    }

    public void setExpYear( String expYear )
    {
        this.expYear = expYear;
    }

    public String getExpYear()
    {
        return expYear;
    }



    //  Build our dropdown data.

    /*
     * Set up a Collection for the Credit Card HTML Select tag Options for
     * the credit card type.  The first option is a label with an invalid
     * option value so the user is required to select a credit card type.
     * @param form is the SelectPaymentActionForm holding the credit card options
     * generated by this method.
     */

    public void setCCOptions( )
     {
         ArrayList<LabelValueBean> ccOptions = new ArrayList<LabelValueBean>();
         LabelValueBean bean = new LabelValueBean( "--choose--", "" );
         ccOptions.add( bean );
         bean = new LabelValueBean( BasePaymentForm.AMEX_NAME, CreditCard.CC_TYPE_AMEX );
         ccOptions.add( bean );
         bean = new LabelValueBean( BasePaymentForm.MC_NAME, CreditCard.CC_TYPE_MASTERCARD );
         ccOptions.add( bean );
         // Visa value is same as Visa name
         bean = new LabelValueBean( BasePaymentForm.VISA_NAME, CreditCard.CC_TYPE_VISA );
         ccOptions.add( bean );
         setCcOptions( ccOptions );
     }
     
     /*
      * Set up a Collection for the Credit Card HTML Select tag Options for
      * the multi currency type.  The first option is a label with a default USD.     * generated by this method.
      */
    public void setCurrencyOptions()
     {
         ArrayList<String> currencyOptions = new ArrayList<String>();

         currencyOptions.add(BasePaymentForm.USD_NAME);
       setCurrencyOptions(currencyOptions);
     }

    /*
     * Set up a Collection for the Credit Card HTML month Select tag Options
     * for the credit card expiration month.  The first option is a label with an
     * invalid option value so the user is required to select a valid month.
     * @param form is the SelectPaymentActionForm holding the credit card expiration
     * month options generated by this method.
     */

     public void setExpMonthOptions(  )
     {
         ArrayList<LabelValueBean> monthOptions = new ArrayList<LabelValueBean>();
         LabelValueBean bean = new LabelValueBean( "MM", "" );
         monthOptions.add( bean );
         String zero = "0";
         for ( int i = 1; i < 10; i++ )
         {
             String label = zero + i;
             bean = new LabelValueBean( label, label );
             monthOptions.add( bean );
         }

         for ( int i = 10; i <= 12; i++ )
         {
             String s = String.valueOf( i );
             bean = new LabelValueBean( s, s ); // label and value are the same
             monthOptions.add( bean );
         }

         setExpMonthOptions( monthOptions );
     }

     /*
      * Set up a Collection for the Credit Card HTML Select tag Options for
      * the credit card expiration year.  Start with the current year and
      * provide a list of including 18 years in to the future. The first
      * option is a label with an invalid option value so the user is
      * required to select a valid month.
      * @param form is the SelectPaymentActionForm holding the credit card
      * expiration year options generated by this method.
      */

     public void setExpYearOptions( )
      {
          ArrayList<LabelValueBean> yearOptions = new ArrayList<LabelValueBean>();
          LabelValueBean bean = new LabelValueBean( "YYYY", "" );
          yearOptions.add( bean );
          Calendar calendar = new GregorianCalendar();
          int year = calendar.get( Calendar.YEAR );

          for ( int i = year; i < year + 18; i++ )
          {
              String value = String.valueOf( i );
              bean = new LabelValueBean( value, value );
              yearOptions.add( bean );
          }

          setExpYearOptions( yearOptions );
      }

    protected void sendMail(){
    	Long acctNo = UserContextService.getAuthenticatedSharedUser().getAccountNumber();
		
		//  Go ahead and send out our email message.  But since
		//  we got here, our payment was good, so let us set our
		//  flag to success.
		
		
		String email_to_user = getUserEmail();
		String email_to_finance = CC2Configuration.getInstance().getInvoicePaymentEmailToFinance();
		String email_subj = CC2Configuration.getInstance().getInvoicePaymentEmailSubj();
		// String email_body = buildMessage(frm);
		
		email_subj = email_subj + acctNo.toString();
		
		MailMessage msg = new MailMessage();
		msg.setFromEmail("donotreply@copyright.com");
		msg.setRecipient(email_to_user);
		msg.setBccRecipient(email_to_finance);
		msg.setSubject(email_subj);
		// msg.setBody(email_body);
		
		//Disable the email send for now
		MailDispatcher email = new MailDispatcherImpl();
		try{
		email.send(msg); 
		}
		catch (ValidationException e)
		{
		String err = getCreditCardName() + " ending in " + getLastFourDigits();
		ActionErrors errors = new ActionErrors();
		
		errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.credit-card", err));
		
		}
		catch (MailSendFailedException e){
			
			_logger.error("\nInvoice Payment Transaction Completed but email failed.\n", e);
		
			String err = "Your transaction completed, but your email confirmation failed.";
			ActionErrors errors = new ActionErrors();
			
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(err));
		}
		catch (Exception e){
			
			_logger.error("An unexpected error occurred.", e);
			String err = "An unexpected error occurred.";
			ActionErrors errors = new ActionErrors();
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(err));
		
		} 
		
		
    }


	public void setCreditCardDetails(CreditCardDetails creditCardDetails) {
		this.creditCardDetails = creditCardDetails;
	}

	
	public void setINDUserInfo(User cccUser) {
		//Individual User Mail To and Bill To are the same
        String firstName = cccUser.getFirstName();
        String lastName = cccUser.getLastName();
        String custName = firstName + " " + lastName;
        setUserName( custName );
        setBillToContactName(custName);
        
        
         Location billToAddress = cccUser.getMailingAddress();
         
         if (billToAddress != null)
         {
       	  String address2 =  billToAddress.getAddress2();
       	  if(address2 == null){
       		  address2 = "";
       	  }
       	  String state =  billToAddress.getState();
       	  if(state == null){
       		  state = "";
       	  }
       	  setAddress1( billToAddress.getAddress1() );
             setAddress2(address2 );
             setCity( billToAddress.getCity() );
             setState( state );
             String zipCode = billToAddress.getPostalCode();
             if ( zipCode == null ){
           	  zipCode = "";
             }
             setZip( zipCode );
             String country = billToAddress.getCountry();
             if ( country != null )
             {
                   setCountry( country );
             }
                                
         }
         
           InternetAddress email = cccUser.getEmailAddress();
           
           if ( email != null)
           {
               setUserEmail( email.getAddress() );
           }
           else
           {
               setUserEmail("");
           }
           setUserPhone( cccUser.getPhoneNumber() );
	}
	
	public void setOrgUserInfo(User cccUser) {
	    String firstName = cccUser.getFirstName();
        String lastName = cccUser.getLastName();
        String custName = firstName + " " + lastName;
        
        setUserName( custName );
        setUserEmail(cccUser.getUsername());
        setUserPhone(cccUser.getPhoneNumber());
        
        ARAccount account = cccUser.getAccount();
        Organization org = cccUser.getOrganization();
      
      if ( org != null)
      {
          //String comp = org.getName();
          String comp = org.getOrganizationName();
          setUserCompany( comp );  
          
      }
               
      if (account != null)
      {

          Name billToPerson = account.getName();
      
          if (billToPerson != null)
          {
              /* ************ Modified this SCR# 8750 *************** */
              String billFirstName = billToPerson.getFirstName();
              String billLastName = billToPerson.getLastName();
              String billCustName = billFirstName + " " + billLastName;
              setBillToContactName(billCustName);

              InternetAddress email = cccUser.getBillingEmailAddress();
      
              if ( email != null)
              {
                  setBillToEmail( email.getAddress() );
              }
              else
              {
                  setUserEmail("");
              }
              setBillToPhone( cccUser.getBillingPhoneNumber() );

              Location billToAddress = cccUser.getBillingAddress();
              
              if (billToAddress != null)
              {
              	String address2 =  billToAddress.getAddress2();
            	  	if(address2 == null){
            	  		address2 = "";
            	  	}
            	  	String state =  billToAddress.getState();
            	  	if(state == null){
            	  		state = "";
            	  	}
              	setAddress1( billToAddress.getAddress1() );
                  setAddress2( address2 );
                  setCity( billToAddress.getCity() );
                  setState(state );
                  String zipCode = billToAddress.getPostalCode();
                  if ( zipCode == null ){
                	  zipCode = "";
                  }
                  setZip( zipCode );
                  String country = billToAddress.getCountry();
                  if ( country != null )
                  {
                      setCountry( country );
                  }                         
               
              }
      
          }
      }
	}
	
	public void clearUserInfo() {
	 	setFirstName("");
	    setLastName("");
	    setAddress1("");
	    setAddress2("");
	    setCity("");
	    setState("");
	    setZip("");
	    setCountry("");
	    setBillToEmail("");
	    setBillToPhone("");
	}

	public CreditCardDetails getCreditCardDetails() {
		if(creditCardDetails==null) {
			creditCardDetails = 
				CheckoutServices.createCreditCardDetails();
			creditCardDetails.setPaymentGateway(PaymentGateway.CYBERSOURCE);
		}
		return creditCardDetails;
	}


   


}