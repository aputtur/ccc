package com.copyright.ccc.web.forms.coi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.LabelValueBean;

import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo;

public class SelectPaymentActionForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hopURL = "";
	private String cccURL = "";
	private String responseEmail="";
	private String signature="";
	private String subscriptionSignature="";
	private String userName="";
	private String firstName="";
	private String lastName="";
    private String address1="";
    private String address2="";
    private String city="";
    private String state="";
    private String zip="";
    private String country="";
    private String email="";
    private String company="";
    private String phone="";
    private String status = "";
    private String paymentType = "";
    private Boolean alwaysInvoice =false;
    private Boolean alwaysInvoiceFlag = false;
    private Boolean hasOnlySpecialOrders = false;
    private Boolean canOnlyBeInvoiced = false;
    private List<PaymentProfileInfo> creditCards;

    private String billingCode; // was cost center
    private boolean captureBilling; // UI input form control
    private String billingCodeName; // UI Label name
    private String purchaseOrderNumber = "";
    private String creditCardType;
    private String paymentProfileId;
    private String cccPaymentProfileId;
    private String currencyType;
    private int currencyRateId;
    private String creditCardNumber;
    private String creditCardNameOn;
    private String expirationDate;
    private Collection<LabelValueBean> ccOptions;
    private Collection<String> currencyOptions;
    private String expMonth;
    private Collection<LabelValueBean> expMonthOptions;
    private String expYear;
    private Collection<LabelValueBean> expYearOptions;
    private Collection<PurchasablePermission> cartItems;
    private String cartTotal;
    private String cartTotalWithNoDollarSign;
    private String cartChargeTotalWithNoDollarSign;
    private String cartChargeTotal;
    
    //private String[] checkedRow;

    // For validation page refresh persistence
    private String paymentMethodRadioButton = "";
  //  private boolean invoiceEditable; // UI control
  //  private boolean paymentChoice; // UI control - turns on payment radio button
  //  private boolean paymentInvoice; // UI control - shows Invoice input area
  //  private boolean paymentCredit; // UI control - shows Credit card input area
  //  private boolean noPaymentAuth; // UI control - user has no valid payment options
  //  private boolean forceInvoice; // UI control to remove invoice select radio input
    private boolean hasSpecialOrders; // UI control for Payment warning message
 //   private String editPage; // UI flow control variable
  //  private String instructions; // Purchase Instructions (Agent or Knowledge Worker)
    
    private boolean cybersourceSiteUp = SystemStatus.isCybersourceSiteUp();
  
   private String billToContactName;
   private String billToEmail;
   private String billToPhone;
   private String userChannel;
   
   
   protected boolean _hasOnlyNonPricedOrders = false;
   protected boolean _showExcludeTBDItemText = false;
   
   private boolean teleSalesUp = true;

    public boolean isTeleSalesUp() {
    	return teleSalesUp;
	}
	
	public void setTeleSalesUp(boolean teleSalesUp) {
		this.teleSalesUp = teleSalesUp;
	}
	
	public boolean getTeleSalesUp() {
		return teleSalesUp;
	}
		
	public SelectPaymentActionForm()
    {
    }
    
    public void reSet()
    {
        this.purchaseOrderNumber = "";
        this.creditCardType = "";
        this.currencyType ="USD";
        this.creditCardNumber = "";
        this.creditCardNameOn = "";
        this.expMonth = "";
        this.expYear = "";
        this.status = "";
        this.paymentMethodRadioButton = "invoice";
        this.paymentType = "invoice";
        this.paymentProfileId = "";
        this.cccPaymentProfileId = "";
        this.expirationDate = "";
    }

    /**
    * Reset all properties to their default values.

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
       super.reset(mapping, request);
    }    */
     public void setHasOnlySpecialOrders( Boolean hasOnlySpecialOrders )
     {
         this.hasOnlySpecialOrders = hasOnlySpecialOrders;
     }

     public Boolean getHasOnlySpecialOrders()
     {
         return hasOnlySpecialOrders;
     }
     
    public void setAlwaysInvoice( Boolean alwaysInvoice )
    {
        this.alwaysInvoice = alwaysInvoice;
    }

    public Boolean getAlwaysInvoice()
    {
        return alwaysInvoice;
    }

    public void setAlwaysInvoiceFlag( Boolean alwaysInvoiceFlag )
    {
        this.alwaysInvoiceFlag = alwaysInvoiceFlag;
    }

    public Boolean getAlwaysInvoiceFlag()
    {
        return alwaysInvoiceFlag;
    }
        
    public void setPaymentType( String payType )
    {
        this.paymentType = payType;
    }

    public String getPaymentType()
    {
        return paymentType;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setCompany( String company )
    {
        this.company = company;
    }

    public String getCompany()
    {
        return company;
    }

    public void setPhone( String phone )
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setCartTotal( String cartTotal )
    {
        this.cartTotal = cartTotal;
        setCartTotalWithNoDollarSign(cartTotal);
    }

    public String getCartTotal()
    {
    	return cartTotal;
    }
    
    public void setCartTotalWithNoDollarSign( String cartTotal )
    {
    	String formattedCartTotal = cartTotal.substring(1).trim();
        this.cartTotalWithNoDollarSign = formattedCartTotal;
                
    }

    public String getCartTotalWithNoDollarSign()
    {
    	return cartTotalWithNoDollarSign;
    }

    public void setCartChargeTotal( String cartChargeTotal )
    {
        this.cartChargeTotal = cartChargeTotal;
        setCartChargeTotalWithNoDollarSign( cartChargeTotal );
                
    }

    public String getCartChargeTotal()
    {
    	return cartChargeTotal;
    }
    
    public void setCartChargeTotalWithNoDollarSign( String cartTotal )
    {
    	String formattedCartChargeTotal = cartTotal.substring(1).trim();
        this.cartChargeTotalWithNoDollarSign = formattedCartChargeTotal;
                
    }

    public String getCartChargeTotalWithNoDollarSign()
    {
    	return cartChargeTotalWithNoDollarSign;
    }
    public void setCartItems( Collection<PurchasablePermission> cartItems )
    {
        this.cartItems = cartItems;
    }

    public Collection<PurchasablePermission> getCartItems()
    {
        return cartItems;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
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

    public void setPaymentMethodRadioButton( String paymentMethodRadioButton )
    {
        this.paymentMethodRadioButton = paymentMethodRadioButton;
    }

    public String getPaymentMethodRadioButton()
    {
        return paymentMethodRadioButton;
    }

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

    public void setBillingCode( String name )
    {
        this.billingCode = name;
    }

    public String getBillingCode()
    {
        return this.billingCode;
    }

    public void setPurchaseOrderNumber( String purchaseOrderNumber )
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getPurchaseOrderNumber()
    {
        return purchaseOrderNumber;
    }

    public void setCreditCardNumber( String creditCardNumber )
    {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber()
    {
        return this.creditCardNumber;
    }

    /*
    * Not currently used because it breaks declaritive validation
    */

    public String getDisplayCardNumber()
    {
        String rtnValue = "";
        if ( this.creditCardNumber != null )
        {
            int length = this.creditCardNumber.length();
            if ( length > 4 )
            {
                int finalDigitsIdx = length - 4;
                String digits = 
                    this.creditCardNumber.substring( finalDigitsIdx );
                StringBuffer mask = new StringBuffer( "********" );
                int maskChars = length - 12;
                if ( maskChars > 0 )
                {
                    for ( int i = 0; i < maskChars; i++ )
                    {
                        mask.append( "*" );
                    }
                }

                rtnValue = mask.toString() + digits;
            }
        }

        return rtnValue;
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

    public void setCreditCardNameOn( String creditCardNameOn )
    {
        this.creditCardNameOn = creditCardNameOn;
        
    }

    public String getCreditCardNameOn()
    {
    	return creditCardNameOn;
       
    }

/*
    public void setInvoiceEditable( boolean invoiceEditable )
    {
        this.invoiceEditable = invoiceEditable;
    }

    public boolean isInvoiceEditable()
    {
        return invoiceEditable;
    }

    public void setPaymentChoice( boolean paymentChoice )
    {
        this.paymentChoice = paymentChoice;
    }

    public boolean isPaymentChoice()
    {
        return paymentChoice;
    }

    public void setPaymentInvoice( boolean paymentInvoice )
    {
        this.paymentInvoice = paymentInvoice;
    }

    public boolean isPaymentInvoice()
    {
        return paymentInvoice;
    }

    public void setPaymentCredit( boolean paymentCredit )
    {
        this.paymentCredit = paymentCredit;
    }

    public boolean isPaymentCredit()
    {
        return paymentCredit;
    }

    public void setEditPage( String returnPage )
    {
        this.editPage = returnPage;
    }

    public String getEditPage()
    {
        return editPage;
    }
*/
    public void setCaptureBillingCode( boolean captureBilling )
    {
        this.captureBilling = captureBilling;
    }

    public boolean isCaptureBillingCode()
    {
        return captureBilling;
    }

    public void setBillingCodeName( String billingCodeName )
    {
        this.billingCodeName = billingCodeName;
    }

    public String getBillingCodeName()
    {
        return billingCodeName;
    }

/*
    public void setInstructions( String instructions )
    {
        this.instructions = instructions;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public void setNoPaymentAuth( boolean noPaymentAuth )
    {
        this.noPaymentAuth = noPaymentAuth;
    }

    public boolean isNoPaymentAuth()
    {
        return noPaymentAuth;
    }

    public void setForceInvoice( boolean forceInvoice )
    {
        this.forceInvoice = forceInvoice;
    }

    public boolean isForceInvoice()
    {
        return forceInvoice;
    }
    */
    private void hasSpecialOrder()
    {

    }

    public void setHasSpecialOrders( boolean hasSpecialOrders )
    {
        this.hasSpecialOrders = hasSpecialOrders;
    }

    public boolean isHasSpecialOrders()
    {
        return hasSpecialOrders;
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
    
    public void setExpirationDate( String expirationDate )
    {
        this.expirationDate = expirationDate;
    }

    public String getExpirationDate()
    {
        return expirationDate;
    }
    
    public void setBillToPhone( String phone )
    {
        this.billToPhone = phone;
    }

    public String getBillToPhone()
    {
        return billToPhone;
    }
    
    public void setUserChannel( String channel )
    {
        this.userChannel = channel;
    }

    public String getUserChannel()
    {
        return userChannel;
    }
    
    public void setHasOnlyNonPricedOrders( boolean onlySpec )
    {
        this._hasOnlyNonPricedOrders = onlySpec;
    }

    public boolean getHasOnlyNonPricedOrders()
    {
        return _hasOnlyNonPricedOrders;
    }
    public void setShowExcludeTBDItemText( boolean showExcludeTBDItemAText )
    {
        this._showExcludeTBDItemText = showExcludeTBDItemAText;
    }

    public boolean getShowExcludeTBDItemText()
    {
        return _showExcludeTBDItemText;
    }  

    public boolean getCybersourceSiteUp()
    {
		return cybersourceSiteUp;
	}

	public void setCybersourceSiteUp(boolean cybersourceSiteUp)
	{
		this.cybersourceSiteUp = cybersourceSiteUp;
	}

	@Override
    public ActionErrors validate( ActionMapping mapping, 
                                  HttpServletRequest request )
    {
        // Get any errors from the declaritive validation framework
        ActionErrors errors = super.validate( mapping, request );

        if (this.getOperation() == null)
        {
            this.reSet();
            errors = null;
        }
        
       else if (this.getOperation().equals("reviewCart"))
        {
            if ( this.paymentType.equalsIgnoreCase( "credit-card" ) )
            {
            /* if ( this.creditCardNumber == null || 
                 "".equals( this.creditCardNumber ) )
            {
                ActionMessage error = 
                    new ActionMessage( "errors.required", "Credit Card Number" );
                errors.add( "", error );
            } */
            
                Calendar now = Calendar.getInstance();
           
                if (!this.getExpYear().equalsIgnoreCase("") && !this.getExpMonth().equalsIgnoreCase(""))
                {
                    int expYear = Integer.parseInt(this.getExpYear());
                    int expMonth = Integer.parseInt(this.getExpMonth());
           
                    //int yr = this.getExpYear().i
            
                    if (expYear <= now.get(Calendar.YEAR))
                    {
                
                        if (expMonth < now.get(Calendar.MONTH) + 1)
                        {
                            ActionMessage error = 
                                new ActionMessage( "errors.expired", "Credit Card ");
                            errors.add( "", error );
                        }
                
                
                    }
                }
                       
            
            }
        
        }
        return errors;
    }

	public void setCanOnlyBeInvoiced(Boolean canOnlyBeInvoiced) {
		this.canOnlyBeInvoiced = canOnlyBeInvoiced;
	}

	public Boolean getCanOnlyBeInvoiced() {
		return canOnlyBeInvoiced;
	}


	 public  boolean getDisplayChargeTotal(){
	    	if(getCartChargeTotal()!=null && StringUtils.isNotEmpty(getCartChargeTotal())  && 
	    			getCartChargeTotal().compareToIgnoreCase(getCartTotal())!=0){
	    		return true;
	    	}
	    	return false;
	    }
}
