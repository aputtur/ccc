package com.copyright.ccc.web.forms.coi;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.web.forms.BasePaymentForm;
import com.copyright.data.order.UsageDataNet;


public class ReviewSubmitCartActionForm extends CartBaseForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public static final String UNKOWN_NAME = "unknown";
    public static final String SEARCH = "Go To Search";
    public static final String CLOSE = "Close Window";

    private String userName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String email;
    private String company;
    private String phone;
    private String promoCode;
    private String alwaysInvoice = "F";
    private String alwaysInvoiceFlag = "F";
    
    private boolean hasSpecialPermissions = false;
    private boolean hasRePrintsItems = false;
    private boolean hasPreviewPdfURL = false;

    private String billingCode; // was cost center
    private boolean captureBilling; // UI input form control
    private String billingCodeName; // UI Label name
    private String purchaseOrderNumber;
    private String creditCardType;
    private String creditCardNumber;
    private String expMonth;
    private String expYear;
    private String creditCardNameOn;
    private String paymentProfileId;
    private String cccPaymentProfileId;
	private String expirationDate;
    private String priceTotal;
    private String acceptTerms;
    private String acceptRLRTerms;
    //private String acceptSPTerms;
    private long confirmNumber;
    private String paymentType;
    private String currencyType = "USD";
    private String fundingCurrencyType ="";
    private String fundingCurrencyAmount ="";
    private Date currencyRateDate ;
    private int fundingCurrencyRateId;
    private String exchangeRate ="";
    private String orderDate;
    private List<? extends TransactionItem> _cartItems;
    
    private boolean regularOrders;
    private boolean specialOrders;

   // private String paymentMethodRadioButton;
    private boolean regularInvoice;
    private String editAddress;
    private String editPayment;
    private boolean orderFailed; // For error message control on confirmation page
    
    private String billToContactName;
    private String billToEmail;
    private String billToPhone;
    private String userChannel;
    private String citationText = "We suggest that you print and use the provided citation information.  Sometimes referred to as the \"attribution\" or \"credit line\", these guidelines can be used to ensure that you properly give credit to the rightsholder of the content you are using.";
    
    private boolean displayEditPaymentLink = true;
   
    private boolean teleSalesUp = true;
    
    private ReviewTermsActionForm termsForm;
    
    public int getNumberOfCartItems()
    {
        if(_cartItems == null) return 0;
        else return _cartItems.size();
    }
    
    public void setCartItems( List<? extends TransactionItem> cartItems )
    {
        this._cartItems = cartItems;
    }

    public List<? extends TransactionItem> getCartItems()
    {
        return _cartItems;
    }

    public ReviewSubmitCartActionForm()
    {
    	termsForm = new ReviewTermsActionForm();
    }
    
    public ReviewTermsActionForm getTermsForm() {
		return termsForm;
	}

	public void setTermsForm(ReviewTermsActionForm termsForm) {
		this.termsForm = termsForm;
	}

	public boolean isTeleSalesUp() {
    	return teleSalesUp;
	}
	
	public void setTeleSalesUp(boolean teleSalesUp) {
		this.teleSalesUp = teleSalesUp;
	}
	
	public boolean getTeleSalesUp() {
		return teleSalesUp;
	}
	
    
    public void setHasSpecialPermissions( boolean hasSpecialPermissions )
    {
        this.hasSpecialPermissions = hasSpecialPermissions;
    }

    public boolean getHasSpecialPermissions()
    {
        return hasSpecialPermissions;
    }
    public void setHasRePrintsItems( boolean hasRePrintsItems )
    {
        this.hasRePrintsItems = hasRePrintsItems;
    }

    public boolean getHasRePrintsItems()
    {
        return hasRePrintsItems;
    }
    public void setHasPreviewPdfURL( boolean hasPreviewPdfURL )
    {
        this.hasPreviewPdfURL = hasPreviewPdfURL;
    }

    public boolean getHasPreviewPdfURL()
    {
        return hasPreviewPdfURL;
    }
    
    
    public void setOrderDate( String ordDate )
    {
        this.orderDate = ordDate;
    }

    public String getOrderDate()
    {
        return orderDate;
    }


    public void setCreditCardDisplay( String creditDisplay )
    {
    }
    public void setPaymentProfileId(String paymentProfileId) {
		this.paymentProfileId = paymentProfileId;
	}

	public String getPaymentProfileId() {
		return paymentProfileId;
	}
	
	public void setCccPaymentProfileId(String cccPaymentProfileId) {
		this.cccPaymentProfileId = cccPaymentProfileId;
	}

	public String getCccPaymentProfileId() {
		return cccPaymentProfileId;
	}

	public void setExpirationDate(String  expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

    public String getCreditCardDisplay()
    {
    	String creditDisplay = "CC ending in " + this.creditCardNumber;
    	/* String creditDisplay = "";
        StringBuffer maskNumber = 
            new StringBuffer( "CC ending in " ); // 8 masked positions
        if ( this.creditCardNumber != null && 
             this.creditCardNumber.length() > 12 )
        {
            int length = this.creditCardNumber.length();
            int idx = length - 4;
            String last4Digits = this.creditCardNumber.substring( idx );
            maskNumber.append( last4Digits );
            creditDisplay = maskNumber.toString();
        } */

        return creditDisplay;
    }
 
    public String getCartTotalWT()
    {
        if (_cartTotal != null)
        	
        	if ( getCartTotal() != null)
    		{
    			return getCartTotal().substring(1).trim();
    		}
    	return "";
    }

    
    public String getCartChargeTotalWT()
    {
        if (_cartChargeTotal != null)
        	
        	if ( getCartChargeTotal() != null)
    		{
    			return getCartChargeTotal().substring(1).trim();
    		}
    	return "";
    }
    


   
    public void setAlwaysInvoice( String inv )
    {
        this.alwaysInvoice = inv;
    }

    public String getAlwaysInvoice()
    {
        return alwaysInvoice;
    }

    public void setAlwaysInvoiceFlag( String inv )
    {
        this.alwaysInvoiceFlag = inv;
    }

    public String getAlwaysInvoiceFlag()
    {
        return alwaysInvoiceFlag;
    }

    public void setPromoCode( String promo )
    {
        this.promoCode = promo;
    }

    public String getPromoCode()
    {
        return promoCode;
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

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
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

   /* public void setPaymentMethodRadioButton( String paymentMethodRadioButton )
    {
        this.paymentMethodRadioButton = paymentMethodRadioButton;
        if ( CREDIT_TYPE.equals( paymentMethodRadioButton ) )
        {
        	this.setCreditCardDisplay(this.getCardName());
            //this.setPaymentType( this.getCardName() );
        	this.setPaymentType(CREDIT_TYPE);
        }
        else if ( INVOICE_TYPE.equals( paymentMethodRadioButton ) )
        {
            this.setPaymentType( INVOICE );
        }
        else{
        	this.setPaymentType("n/a");
        	this.setFundingCurrencyRateId(1);
        }
    }

    public String getPaymentMethodRadioButton()
    {
        return paymentMethodRadioButton;
    }
    */

    /*
    * UI attribute designating payment type based on radio button selection
    */

    public void setPaymentType( String paymentType )
    {
        this.paymentType = paymentType;
    }

    public String getPaymentType()
    {
        return this.paymentType;
    }

    public void setCreditCardType( String creditCardType )
    {
        this.creditCardType = creditCardType;
    }

    public String getCreditCardType()
    {
        return creditCardType;
    }
    
    public void setCurrencyType( String currencyType )
    {
    	this.currencyType = currencyType;
    }

    public String getCurrencyType()
    {
        return currencyType;
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
    
    public void setCreditCardNumber( String creditCardNumber )
    {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumberFull()
    {
        return creditCardNumber;
    }

    /*
    * Getter method for displaying a masked out credit card
    * number where only the last 4 digits are shown.
    */

    public String getCreditCardNumber()
    {
        return creditCardNumber;
    }

    public void setExpMonth( String month )
    {
        this.expMonth = month;
    }

    public String getExpMonth()
    {
        return this.expMonth;
    }

    public void setExpYear( String year )
    {
        this.expYear = year;
    }

    public String getExpYear()
    {
        return expYear;
    }

    public void setCreditCardNameOn( String creditCardNameOn )
    {
        if(creditCardNameOn.contains("<")){
        	String[] cardHolderName = creditCardNameOn.split("<");       
        	this.creditCardNameOn = cardHolderName[0];
        }else{
        	this.creditCardNameOn = creditCardNameOn;
        }
    	
    }

    public String getCreditCardNameOn()
    {
        return creditCardNameOn;
    }

    public void setPriceTotal( String priceTotal )
    {
        this.priceTotal = priceTotal;
    }

    public String getPriceTotal()
    {
        return priceTotal;
    }

    public void setAcceptTerms( String acceptTerms )
    {
        this.acceptTerms = acceptTerms;
    }

    public String getAcceptTerms()
    {
        return acceptTerms;
    }
  /*  public void setAcceptSPTerms( String acceptSPTerms )
    {
        this.acceptSPTerms = acceptSPTerms;
    }

    public String getAcceptSPTerms()
    {
        return acceptTerms;
    }*/
    
    public void setAcceptRLRTerms( String acceptRLRTerms )
    {
        this.acceptRLRTerms = acceptRLRTerms;
    }

    public String getAcceptRLRTerms()
    {
        return acceptRLRTerms;
    }

    public void setConfirmNumber( long confNumber )
    {
        this.confirmNumber = confNumber;
    }

    public long getConfirmNumber()
    {
        return confirmNumber;
    }

    @Override
    public void setPage( int page )
    {
        super.page = page;
    }
    /*
    * For struts multipage form validation control.  The page configures the
    * validation framework to only validate against that portion of a multipage
    * form that the user has seen.  Consequently the struts validation framework
    * will validate all forms less than or equal to the current form page value.
    */

    @Override
    public int getPage()
    {
        return super.page;
    }

    public void setCountry( String country )
    {
        this.country = country;
    }

    public String getCountry()
    {
        return country;
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

    public void setState( String state )
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }

    public void setZip( String zip )
    {
        this.zip = zip;
    }

    public String getZip()
    {
        return zip;
    }

    public void setHasRegularOrders( boolean regularOrders )
    {
        this.regularOrders = regularOrders;
    }
    
    public boolean getHasRegularOrders()
    {
        return regularOrders;
    }

    /**
    * Returns true if the cart contains one or more regular orders
    * Struts needs "isXXXXX()" to imply a boolean attribute for tags
    */
    public boolean isHasRegularOrders()
    {
        return regularOrders;
    }

    /**
    * Returns true if the cart contains one or more regular orders
    * Duplicate of isHasRegularOrders that just doesn't look so awkward in code
    */
    public boolean hasRegularOrders()
    {
        return regularOrders;
    }


    public void setHasSpecialOrders( boolean anySpecialOrders )
    {
        this.specialOrders = anySpecialOrders;
    }

    /**
    * Returns true if the cart contains one or more special orders
    * Struts needs "isXXXXX()" to imply a boolean attribute for tags
    */
    public boolean isHasSpecialOrders()
    {
        return this.specialOrders;
    }

    /**
    * Returns true if the cart contains one or more special orders
    * Duplicate of isHasSpecialOrders that just doesn't look so awkward in code
    */
    public boolean hasSpecialOrders()
    {
        return this.specialOrders;
    }

    /*
    * Submit button name to redirect reviewOrder.jsp form to the
    * editBillingAddress.do action.
    */

    public void setEditAddress( String editAddress )
    {
        this.editAddress = editAddress;
    }

    public String getEditAddress()
    {
        return editAddress;
    }

    /*
    * Submit button name to redirect reviewOrder.jsp form to the
    * selectPaymentMethod.do action.
    */

    public void setEditPayment( String editPayment )
    {
        this.editPayment = editPayment;
    }

    public String getEditPayment()
    {
        return editPayment;
    }

    /*
    * Helper method that returns true if the editAddress
    * attribute is not an empty String.
    */

    public boolean isEditAddress()
    {
        return true;
    }

    public boolean isEditPayment()
    {
        return true;
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
    
    public void setUserChannel( String channel )
    {
        this.userChannel = channel;
    }

    public String getUserChannel()
    {
        return userChannel;
    }
    
    public void setCitationText( String citation )
    {
        this.citationText = citation;
    }

    public String getCitationText()
    {
        return citationText;
    }
    
    


    /*
    * Custom validation for the Review/Submit checkout JSP.  If the user selects
    * a payment type of Credit Card but the cart contains one or more special
    * orders we need a valid billing address for the user so we must check for
    * a valid address before forwarding to the Review/Submit checkout JSP.
    * @param mapping is the Struts ActionMapping value
    * @param request is the HttpServletRequest object.
    * @return ActionErrors object populated with ActionError objects for each
    * validation failure.
    */
    @Override
    public ActionErrors validate( ActionMapping mapping, 
                                  HttpServletRequest request )
    {
        // Get any errors from the declaritive validation framework
        ActionErrors errors = super.validate( mapping, request );

    if ( this.paymentType != null )
        {
            if ( this.paymentType.equalsIgnoreCase(BasePaymentForm.PAYMENT_TYPE_CREDIT_CARD ) )
            {
            	if(StringUtils.isEmpty(this.getPaymentProfileId())){
                    ActionMessage error = 
                        new ActionMessage( "errors.required", "Credit Card Number" );
                    errors.add( "", error );
                }
            }
        }

        if ( this.page >= 2 )
        {
            // Make sure the user checked the "Accept Terms" checkbox
            this.validateTerms( errors );
        }

        this.validatePaymentOption( errors );
        // Comment out address validation because Jon Palace says the user
        // can't fix this information anyway and we should be able to get
        // a correct invoice address from the customer if needed.
        //if (!INVOICE.equals(this.paymentType)) 
        //{
        //   if (this.isHasSpecialOrders()) { this.validateAddress(errors); }
        //}

        return errors;
    }

    /*
    * Validation method for checking if the user has selected a payment option
    * only if there are no Special Orders in the shopping cart.  If there are
    * Special Orders the invoice selection is mandatory so no validation is necessary.
    * @param ActionErrors object to which any new validation errors will be added.
    * @return ActionErrors object populated with an ActionError object for each
    * validation error.
    */

    private ActionErrors validatePaymentOption( ActionErrors errors )
    {
        return errors;
    }

    /*
    * Validation method for checking all invoice address fields as required.
    * @param ActionErrors object to which any new validation errors will be added.
    * @return ActionErrors object populated with an ActionError object for each
    * validation error.
    */

    public ActionErrors validateAddress( ActionErrors errors )
    {
        String blank = "";
        if ( this.address1 == null || this.address1.trim().equals( blank ) )
        {
            ActionMessage error = 
                new ActionMessage( "errors.billing.required", "company name" );
            errors.add( "addr1", error );
        }

        if ( this.address2 == null || this.address2.trim().equals( blank ) )
        {
            ActionMessage error = 
                new ActionMessage( "errors.billing.required", "address" );
            errors.add( "addr2", error );
        }

        if ( this.city == null || this.city.trim().equals( blank ) )
        {
            ActionMessage error = 
                new ActionMessage( "errors.billing.required", "city" );
            errors.add( "city", error );
        }

        if ( this.state == null || this.state.trim().equals( blank ) )
        {
            ActionMessage error = 
                new ActionMessage( "errors.billing.required", "state" );
            errors.add( "state", error );
        }

        if ( this.zip == null || this.zip.trim().equals( blank ) )
        {
            ActionMessage error = 
                new ActionMessage( "errors.billing.required", "zip code" );
            errors.add( "zip", error );
        }

        if ( this.country == null || this.country.trim().equals( blank ) )
        {
            ActionMessage error = 
                new ActionMessage( "errors.billing.required", "country code" );
            errors.add( "country", error );
        }

        return errors;
    }

    /*
    * Validation method for checking if the user has checked the Terms checkbox.
    * @param ActionErrors object to which any new validation errors will be added.
    * @return ActionErrors object populated with an ActionError object for each
    * validation error.
    */

    private ActionErrors validateTerms( ActionErrors errors )
    {

        if ( this.acceptTerms == null || this.acceptTerms.trim().equals( "" ) )
        {
            ActionMessage error = 
                new ActionMessage( "advisor.terms.required" );
            errors.add( "terms", error );
        }
        
        /*if(getHasSpecialPermissions() && (this.acceptSPTerms == null || this.acceptSPTerms.trim().equals( "" ))){
        	 ActionMessage error = 
                 new ActionMessage( "specialPerm.terms.required" );
             errors.add( "spTerms", error );
        }*/
         if(getHasRePrintsItems() && (this.acceptRLRTerms == null || this.acceptRLRTerms.trim().equals( "" ))){
        	 ActionMessage error = 
                 new ActionMessage( "rprints.terms.required" );
             errors.add( "rlrTerms", error );
        }
   
        return errors;
    }

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

    public String getSearchLabel()
    {
        return SEARCH;
    }

    public String getCloseLabel()
    {
        return CLOSE;
    }


    /**
    * Return an indexed order Special Terms Text for one of the Collection of
    * Regular Order Items.  The index values start at 0.
    * If the index is out of bounds the text string is set to: "No Terms Available".
    *
    * @param index designates what order item to return the special terms text for.
    * @return the special terms text for a given regular order item.
    */
    public String getRegularTerm( int index )
    {
        String text = "No Terms Available";
        return text;
    }

    /**
    * Return an indexed order Special Terms Text for one of the Collection of
    * Special Order Items.  The index values start at 0.
    * If the index is out of bounds the text string is set to: "No Terms Available".
    *
    * @param index designates what order item to return the special terms text for.
    * @return the special terms text for a given special order item.
    */
    public String getSpecialTerm( int index )
    {
        String text = "No Terms Available";
        return text;
    }

    /**
    * Helper method for finding the String Label for duration
    * based on the index values defined in UsageDataNet.
    *
    * @param idx is the index value of duration as defined by the select
    * tag in the shopping cart.  It is a long in the PermissionRequestTransfer
    * object but is cast to an int internally.  If there is any truncation in
    * the cast you have a logic error in calculating the index argument.
    * @return the duration as a displayable string.
    */
    public String getDurationLabel( long idx )
    {
        int index = ( int )idx;
        String duration = "";
        switch ( index )
        {
            case UsageDataNet.TO_30_DAYS_FEE:
                duration = 
                        UsageDataNet.feeStringValues[UsageDataNet.TO_30_DAYS_FEE];
                break; // "30 Days"; 
            case UsageDataNet.TO_180_DAYS_FEE:
                duration = 
                        UsageDataNet.feeStringValues[UsageDataNet.TO_180_DAYS_FEE];
                break; // "180 Days";
            case UsageDataNet.TO_365_DAYS_FEE:
                duration = 
                        UsageDataNet.feeStringValues[UsageDataNet.TO_365_DAYS_FEE];
                break; // "365 Days";
            case UsageDataNet.UNLIMITED_DAYS_FEE:
                duration = 
                        UsageDataNet.feeStringValues[UsageDataNet.UNLIMITED_DAYS_FEE];
                break; // "Unlimited";
        }

        return duration;
    }

    public void setRegularInvoice( boolean regularInvoice )
    {
        this.regularInvoice = regularInvoice;
    }

    public boolean isRegularInvoice()
    {
        return regularInvoice;
    }

    /**
    * Return true if the regular items are invoiced or there
    * are special ordes (which must be invoiced).
    * @return this.regularInvoice && this.hasSpecialOrders
    */
    public boolean isHasAnInvoice()
    {
        return this.regularInvoice || this.hasSpecialOrders();
    }

    public void setOrderFailed( boolean orderFailed )
    {
        this.orderFailed = orderFailed;
    }

    public boolean isOrderFailed()
    {
        return orderFailed;
    }

    /*
    * Helper method so the JSP can keep table <td> tags consisten across rows
    */

    public String getColspan()
    {
        int colspan = 3;
        if ( this.hasRegularOrders() )
        {
            colspan += 1;
        }
        if ( this.hasSpecialOrders() )
        {
            colspan += 1;
        }
        return String.valueOf( colspan );
    }
    
    public String getIdnoWT()
	{
		String idno = "";
		if (getCartItems() != null)
		{
			for (TransactionItem perm:getCartItems())
			{
				if (idno != null && !idno.equals(""))
				{
					idno = idno + ";" + perm.getStandardNumber();
				}
				else
				{
					idno = perm.getStandardNumber();
				}
			}
			
		}
		
		return idno;
	}

    @Override
    public ActionErrors validate( ActionMapping mapping, 
                                  ServletRequest request )
    {
        return super.validate( mapping, request );
    }
    
    
    public String  getDisplayEstimatedPriceMessage(){
    	// if u have priced sp
    	//or if u have any TBDs show this message if only reprint priced  
    	if(getCartChargeTotal()!=null && StringUtils.isNotEmpty(getCartChargeTotal())  && 
    			getCartChargeTotal().compareToIgnoreCase(getCartTotal())!=0){
    		return "<strong>Note:</strong> " + getCartChargeTotal() + " of your total will be charged to your credit card immediately. Your card will be charged for Special Order and Reprint items later.";
    		
    	}
    	return StringUtils.EMPTY;
    	
    }

	public void setDisplayEditPaymentLink(boolean displayEditPaymentLink) {
		this.displayEditPaymentLink = displayEditPaymentLink;
	}

	public boolean isDisplayEditPaymentLink() {
		return displayEditPaymentLink;
	}
   
}
