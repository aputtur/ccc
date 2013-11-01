package com.copyright.ccc.business.services.cart;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.base.enums.PaymentGateway;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.CheckoutResults;
import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.BundleParmEnum;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ItemHelperServices;
import com.copyright.ccc.business.services.LogFormatter;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.business.services.payment.Charge;
import com.copyright.ccc.business.services.payment.Credit;
import com.copyright.ccc.business.services.payment.CyberSourceCreditCardResponses;
import com.copyright.ccc.business.services.payment.PaymentRequest;
import com.copyright.ccc.business.services.payment.PaymentResponse;
import com.copyright.ccc.business.services.payment.PreAuthorize;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.config.CC2Constants;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.data.order.CoursePack;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.payment.AuthorizationException;
import com.copyright.data.payment.CheckoutData;
import com.copyright.data.payment.InvoiceRequest;
import com.copyright.data.payment.PaymentData;
import com.copyright.service.order.CartServiceAPI;
import com.copyright.service.order.CartServiceFactory;
import com.copyright.service.order.CashierServiceAPI;
import com.copyright.service.order.CashierServiceFactory;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.UserType;
import com.copyright.svc.order.api.data.Bundle;
import com.copyright.svc.order.api.data.BundleParm;
import com.copyright.svc.order.api.data.Cart;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.ItemStatusQualifierEnum;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.Payment;
import com.copyright.svc.order.api.data.SpecialOrderTypeEnum;
import com.copyright.svc.telesales.api.data.RightsholderOrganization;
import com.copyright.workbench.time.DateUtils2;




/**
 * @author Lucas Alberione
 *
 * Class containing a set of services responsible for
 * shopping cart checkout management.
 */
public final class CheckoutServices {

	private static final Logger LOGGER = Logger.getLogger( CheckoutServices.class );

	private static final String DATE_MASK_MM_DD_YYYY = "MM/dd/yyyy";
	private static final String NEWLINE = CC2Constants.NEWLINE;
	private static final String LOGHDR = "CCC Diagnostic data: ";


	private static final Logger _logger = Logger.getLogger( CartServices.class );


	private CheckoutServices(){}

	private static void initOrderHeaderContext (Cart cart) {

		cart.setOrderEnteredBy(UserContextService.getAuthenticatedAppUser().getUsername());
		cart.setBuyerUserIdentifier(UserContextService.getActiveAppUser().getUsername());
		cart.setLastUpdateDtm(new Date());

		LdapUser ldapUser = UserContextService.getActiveSharedUser().getLdapUser();
		User user = UserContextService.getActiveSharedUser();

		/*
		 * To address CC-2332, null LICENSEE_ACCOUNT on ORDER_HEADER when a Rightslink user first logs in to copyright.com
		 * If you fill your cart anonymously and only login when you click ‘Checkout’, you end up in this method with both ‘account’ 
		 * and ‘accountInfo’ set to NULL and since this method only retrieves data from UserContextServices, it can only fill 
		 * LICENSEE_ACCOUNT with NULL.
		 */
		if (user.getAccount() == null && user.getAccountInfo() == null) {
			UserContextService.updateSharedUser(user.getParty().getPartyId());
			user = UserContextService.getActiveSharedUser();
		}
		
		Long partyIdLong = null;
		try { 
			partyIdLong = new Long(ldapUser.getPartyID()); 
		}
		catch (Exception e) {
			LOGGER.warn("exception creating a Long partyId for " + ldapUser.getUsername() + NEWLINE + ExceptionUtils.getFullStackTrace(e));
			partyIdLong = 0L;
		}

		if (partyIdLong.compareTo(0L) < 0) {
			//		  cart.setBuyerUserIdentifier(ldapUser.getUsername());	  
			StringBuffer concatName = new StringBuffer();
			boolean firstConcat = true;
			if (ldapUser.getPrefix() != null) {
				firstConcat = false;
				concatName.append(user.getDisplayTitle(ldapUser.getPrefix()));
			}
			if (ldapUser.getFirstName() != null) {
				if (!firstConcat) {
					concatName.append(" ");
				}
				firstConcat = false;
				concatName.append(ldapUser.getFirstName());
			}
			if (ldapUser.getMiddleName() != null) {
				if (!firstConcat) {
					concatName.append(" ");
				}
				firstConcat = false;
				concatName.append(ldapUser.getMiddleName());
			}
			if (ldapUser.getLastName() != null) {
				if (!firstConcat) {
					concatName.append(" ");
				}
				firstConcat = false;
				concatName.append(ldapUser.getLastName());
			}
			cart.setBuyerName(concatName.toString());
			cart.setBuyerPartyId(partyIdLong);
			cart.setBuyerPtyInst(new Long(partyIdLong.longValue() * -1));
			cart.setLicenseeAccount(partyIdLong.toString());
			if (ldapUser.getUserType().equals(UserType.ORG_ADD.getValue()) ||
					ldapUser.getUserType().equals(UserType.ORGANIZATION.getValue())) {
				cart.setLicenseeName(ldapUser.getOrganizationName());
				cart.setLicenseePartyId(partyIdLong);
				cart.setLicenseePtyInst(new Long(partyIdLong.longValue() * -1));
			} else {
				cart.setLicenseeName(cart.getBuyerName());
				cart.setLicenseePartyId(partyIdLong);
				cart.setLicenseePtyInst(new Long(partyIdLong.longValue() * -1));
			}
		} else {
			//		  cart.setBuyerUserIdentifier(ldapUser.getUsername());	  
			StringBuffer concatName = new StringBuffer();
			boolean firstConcat = true;
			if (user.getTitle() != null) {
				firstConcat = false;
				concatName.append(user.getTitle());
			}
			if (user.getFirstName() != null) {
				if (!firstConcat) {
					concatName.append(" ");
				}
				firstConcat = false;
				concatName.append(user.getFirstName());
			}
			if (user.getMiddleName() != null) {
				if (!firstConcat) {
					concatName.append(" ");
				}
				firstConcat = false;
				concatName.append(user.getMiddleName());
			}
			if (user.getLastName() != null) {
				if (!firstConcat) {
					concatName.append(" ");
				}
				firstConcat = false;
				concatName.append(user.getLastName());
			}
			cart.setBuyerName(concatName.toString());
			cart.setBuyerPtyInst(user.getPtyInst());
			cart.setBuyerPartyId(user.getPartyId());
			cart.setLicenseeAccount(user.getArAccountNumber());
			if (user.getOrgParty() != null)
			{
				cart.setLicenseePartyId(user.getOrgParty().getPartyId());
				cart.setLicenseeName(user.getOrgParty().getOrgName());
			}
			else
			{
				cart.setLicenseePartyId(user.getPartyId());
				cart.setLicenseeName(concatName.toString());
			}
			cart.setLicenseePtyInst(user.getOrgPtyInst());

		}


	}

	private static void initItemStatus (Cart cart, Payment payment) {
		
		 boolean autoAcceptInvoice=UserContextService.getActiveAppUser().isAutoInvoiceSpecialOrder();
		 
		for (Item item : cart.getItems()) {
			item.setItemOrigAvailabilityCd(item.getItemAvailabilityCd());
			item.setAutoInvoice(Boolean.TRUE);
			if (item.getProductCd().equals(ProductEnum.RLR.name())) {
				item.setItemStatusCd(ItemStatusEnum.AWAITING_FULFILLMENT);
				item.setItemStatusDtm(new Date());
				item.setPayLaterCcProfileId(payment.getCcProfileId());
				item.setPayLaterProfileIdentifier(payment.getPaymentProfileIdentifier());
			} else {
					// if TF backed special order then read autoAccept flag from profile
			  if(item.getIsSpecialOrder()) {
						item.setAutoInvoice(autoAcceptInvoice);
					}
				if (item.getItemAvailabilityCd() == null) {
					item.setItemStatusCd(ItemStatusEnum.AWAITING_RESEARCH);
					item.setItemStatusQualifier(ItemStatusQualifierEnum.ERROR);
					item.setItemStatusDtm(new Date());
				} else  if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
					item.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
					item.setItemStatusDtm(new Date());
					item.setBaseInvoiceDate(new Date());
					if (payment.getPaymentMethodCd().equals(ItemConstants.PAYMENT_METHOD_CREDIT_CARD)) {
						if (CartServices.getItemPrice(item).compareTo(new BigDecimal(0)) > 0) {
							item.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_CREDIT_CARD);
							item.setPaymentId(payment.getPaymentId());					  
						} else {
							item.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  						  
						}
					} else {
						item.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
					}
				} else if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getStandardPermissionCd())) {
					item.setItemStatusCd(ItemStatusEnum.AWAITING_RESEARCH);
					item.setItemStatusQualifier(ItemStatusQualifierEnum.ERROR);
					item.setItemStatusDtm(new Date());
				} else if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd())) {
					if (item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER) ||
							item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER)) {
						item.setItemStatusCd(ItemStatusEnum.AWAITING_RH);
						item.setItemStatusDtm(new Date());
						item.setItemStatusQualifier(ItemStatusQualifierEnum.FIRST_REQUEST);
					}
					if (item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER)) {
						item.setItemStatusCd(ItemStatusEnum.AWAITING_RESEARCH);
						item.setItemStatusQualifier(ItemStatusQualifierEnum.ERROR);
						item.setItemStatusDtm(new Date());
					}
				} else if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.NO_CHARGE.getStandardPermissionCd())) {
					item.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
					item.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
					item.setItemStatusDtm(new Date());
				} else if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd())) {
					item.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
					item.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
					item.setItemStatusDtm(new Date());
				}

			}
			LOGGER.debug("Item: " + item.getItemId() + " Status Set: " + item.getItemStatusCd());
		}
	}

	private static void initEstimatedQty(Cart cart) {
		for (Bundle bundle : cart.getBundles()) {
			BundleParm numStudentsParm = bundle.getBundleParms().get(BundleParmEnum.NUM_STUDENTS.name());
			if (numStudentsParm == null) {
				continue;
			}
			BundleParm estimatedCopiesParm = bundle.getBundleParms().get(BundleParmEnum.ESTIMATED_COPIES.name());
			if (estimatedCopiesParm == null) {
				return;
//				estimatedCopiesParm = new BundleParm();
//				estimatedCopiesParm.setParmName(BundleParmEnum.ESTIMATED_COPIES.name());
			}
			ItemHelperServices.initBundleParmAttributesFromEnum(estimatedCopiesParm);
			estimatedCopiesParm.setBundleId(numStudentsParm.getBundleId());	
			estimatedCopiesParm.setParmValue(numStudentsParm.getParmValue());	
			estimatedCopiesParm.setParmValueNumeric(numStudentsParm.getParmValueNumeric());	
			bundle.getBundleParms().put(estimatedCopiesParm.getParmName(), estimatedCopiesParm);
		}
	}
	
	private static void initItemStatusForAddItemToOrder(Item item) {
		
		boolean autoAcceptInvoice=UserContextService.getActiveAppUser().isAutoInvoiceSpecialOrder();

		item.setItemOrigAvailabilityCd(item.getItemAvailabilityCd());
		item.setAutoInvoice(Boolean.TRUE);
		if (item.getProductCd().equals(ProductEnum.RLR.name())) {
			item.setItemStatusCd(ItemStatusEnum.AWAITING_FULFILLMENT);
			item.setItemStatusDtm(new Date());
		} else {
			// if TF backed special order then read autoAccept flag from profile
			  if(item.getIsSpecialOrder()) {
						item.setAutoInvoice(autoAcceptInvoice);
					}
			if (item.getItemAvailabilityCd() == null) {
				item.setItemStatusCd(ItemStatusEnum.AWAITING_RESEARCH);
				item.setItemStatusDtm(new Date());
			} else  if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
				item.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
				item.setItemStatusDtm(new Date());
				item.setBaseInvoiceDate(new Date());
				item.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
			} else if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getStandardPermissionCd())) {
				item.setItemStatusCd(ItemStatusEnum.AWAITING_RESEARCH);
				item.setItemStatusDtm(new Date());
			} else if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd())) {
				if (item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER) ||
						item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER)) {
					item.setItemStatusCd(ItemStatusEnum.AWAITING_RH);
					item.setItemStatusDtm(new Date());
					item.setItemStatusQualifier(ItemStatusQualifierEnum.FIRST_REQUEST);
				}
				if (item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER)) {
					item.setItemStatusCd(ItemStatusEnum.AWAITING_RESEARCH);
					item.setItemStatusDtm(new Date());
				}
			} else if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.NO_CHARGE.getStandardPermissionCd())) {
				item.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
				item.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
				item.setItemStatusDtm(new Date());
			} else if (item.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd())) {
				item.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
				item.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
				item.setItemStatusDtm(new Date());
			}

		}
		LOGGER.debug("Item: " + item.getItemId() + " Status Set: " + item.getItemStatusCd());

	}


	/**
	 * Handles shopping cart checkout using invoice as payment method.
	 * Returns an instance of <code>CheckoutResults</code> for this transaction, if successful.
	 */
	public static CheckoutResults checkoutCart( InvoiceDetails invoiceDetails )
	throws CheckoutException, InvoiceValidationException
	{
		Cart cart = CartServices.getCart();
		if (cart==null) {
			throw new CCRuntimeException("Unable to checkout the cart. CartServices returned a null cart reference.");
		}
		Payment payment = new Payment();
		payment.setPaymentMethodCd(ItemConstants.PAYMENT_METHOD_INVOICE);  //TODO This is meant to be an enum in svc order	  

		initOrderHeaderContext (cart);
		initItemStatus (cart, payment );
		initRightsholdersForCartItems(cart);
		initEstimatedQty(cart);

		LOGGER.debug( "Initializing Cart Header for Invoice Checkout ");	

		cart.setPoNum(invoiceDetails.getPONumber());
		cart.setPromoCode(invoiceDetails.getPromotionCode());

		LOGGER.debug( "Buyer Name: " + cart.getBuyerName() + " User Identifier: " + cart.getBuyerUserIdentifier() + " PtyInst: " + cart.getBuyerPtyInst() + " Party Id: " + cart.getBuyerPartyId() );	
		LOGGER.debug( "Licensee Name: " + cart.getLicenseeName() + " Account: " + cart.getLicenseeAccount() + " PtyInst: " + cart.getLicenseePtyInst() + " Party Id: " + cart.getLicenseePartyId() );	
		LOGGER.debug( "Po Num: " + cart.getPoNum() + " Promotion Code: " + cart.getPromoCode());	

		CheckoutResults checkoutResults;

		try
		{
			cart = CartServices.updateCart(cart);
			checkoutResults = checkoutCart(cart, payment);
			return checkoutResults;
		}catch ( Exception e ) 
		{
			String logMsg=ExceptionUtils.getFullStackTrace(e) + NEWLINE + LOGHDR + LogFormatter.format(cart) + LogFormatter.format(payment);
			throw new CheckoutException( "CheckoutServices.checkoutCart: error during checkout using invoice as payment method.", e, logMsg );
		}

	}


	private static CheckoutResults checkoutCart( Cart cart, Payment payment ) {
		Long confirmationNumber = null;
		OrderSearchResult orderSearchResult;

		LOGGER.debug( "Initializing Cart Header for Payment Data Checkout ");	
		LOGGER.debug( "Buyer Name: " + cart.getBuyerName() + " User Identifier: " + cart.getBuyerUserIdentifier() + " PtyInst: " + cart.getBuyerPtyInst() + " Party Id: " + cart.getBuyerPartyId() );	
		LOGGER.debug( "Licensee Name: " + cart.getLicenseeName() + " Account: " + cart.getLicenseeAccount() + " PtyInst: " + cart.getLicenseePtyInst() + " Party Id: " + cart.getLicenseePartyId() );	

		// Persist the changes to the cart.  

		try
		{
			if (payment != null && payment.getPaymentMethodCd().equals(ItemConstants.PAYMENT_METHOD_CREDIT_CARD)) {
				confirmationNumber = 
					ServiceLocator.getCartService().checkout(
							new OrderConsumerContext(), 
							cart.getCartId(), 
							payment
					);
			} else {
				confirmationNumber = 
					ServiceLocator.getCartService().checkout(
							new OrderConsumerContext(), 
							cart.getCartId(), 
							null
					);

			}

			if( confirmationNumber != null )
			{
				LOGGER.debug( "Checkout confirmation number: " + confirmationNumber.longValue() );	
				String orderTotal = CartServices.getCartChargeTotal();
				LOGGER.debug( "Order Total: " + orderTotal );
				CartServices.resetCartPersistence();
				try
				{
					orderSearchResult = OrderLicenseServices.searchAllOrderLicensesForConfirmationNumber(confirmationNumber);
				}
				catch ( OrderLicensesException ole )
				{
					String logMsg= "Unable to retrieve order details for confirmation number: " + confirmationNumber + NEWLINE + ExceptionUtils.getFullStackTrace(ole);				
					throw new CheckoutException( "Unable to retrieve order details for confirmation number " + confirmationNumber, ole, logMsg );
				}

				List<OrderLicense> orderLicensesList = orderSearchResult.getOrderLicenses();
				OrderPurchase orderPurchase = orderSearchResult.getOrderPurchaseByDisplaySequence(0);
				String orderDate = DateUtils2.formatDate( new Date(), DATE_MASK_MM_DD_YYYY ) ;

				LOGGER.debug( "Shopping cart checked out successfully, Confirmation number: " + confirmationNumber.longValue() );
				LOGGER.debug( "Number of new licenses: " + orderLicensesList.size() );

				return new CheckoutResultsImpl( confirmationNumber,
						orderDate,
						orderPurchase,
						orderLicensesList,
						orderTotal );

			}else {
				LOGGER.debug( "Checkout confirmation number:null" );
				String logMsg= "Could not get a valid confirmation number while checking out cart" + NEWLINE + LOGHDR + LogFormatter.format(cart);
				throw new CheckoutException( "Could not get a valid confirmation number while checking out cart", logMsg );
			}
		} catch ( Exception e ) {
			String logMsg=ExceptionUtils.getFullStackTrace(e) + NEWLINE + LOGHDR + LogFormatter.format(cart);
			throw new CheckoutException( "CheckoutServices.checkoutCart: error during checkout using invoice as payment method.", e, logMsg );
		}

	}



	public static void setUserInfoOnPaymentRequest(User cccUser, PaymentRequest paymentRequest )
	{
		String firstName = null;
		String lastName = null;
		String userid = null;
		long acctNum =-1 ;
		long CCCacctNum =-1;

		InternetAddress email = cccUser.getEmailAddress();

		if ( email != null)
		{
			userid = email.getAddress();
		}
		else
		{
			userid = "";
		}

		firstName = cccUser.getFirstName();
		lastName = cccUser.getLastName();
		acctNum = cccUser.getAccountNumber();
		CCCacctNum = cccUser.getAccountNumber();
		String fundingCurrencyAmount = paymentRequest.getAmount();
		paymentRequest.setFundingAmount(fundingCurrencyAmount);
		paymentRequest.setUserID(userid);
		paymentRequest.setAcctNum(acctNum);
		paymentRequest.setCCCacctNum(CCCacctNum);
		paymentRequest.setFirstName(firstName);
		paymentRequest.setLastName(lastName);
		paymentRequest.setEmailAddress(userid);
	}


	//	2012-11-29	MSJ
	//	Added another payment info. set method to cover an anonymous
	//	user payment.  Acct. no. will belong to a real, CCC user.
	//	The other parameters will belong to the payer.

	public static void setUserInfoOnPaymentRequest(
		String accountNumber,
		String firstName,
		String lastName,
		String emailAddress,
		PaymentRequest paymentRequest )
	{
		long acctNum = -1;

		try
		{
			acctNum = Long.parseLong( accountNumber );
		}
		catch (NumberFormatException e)
		{
			LOGGER.info( "Invalid account number provided.  Could not parse value." );
		}
		long cccAcctNum = acctNum;
		String userID = emailAddress;
		String fundingCurrencyAmount = paymentRequest.getAmount();

		paymentRequest.setFundingAmount( fundingCurrencyAmount );
		paymentRequest.setUserID( userID );
		paymentRequest.setAcctNum( acctNum );
		paymentRequest.setCCCacctNum( cccAcctNum );
		paymentRequest.setFirstName( firstName );
		paymentRequest.setLastName( lastName );
		paymentRequest.setEmailAddress( userID );
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
		
		// fake failure response for testing
//		paymentResponse.setAuthStatus(CyberSourceCreditCardResponses.GENERAL_DECLINE_PROCESSOR);
//		paymentResponse.setAuthSuccess(false);

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


	/**
	 * Handles shopping cart checkout using credit card as payment method.
	 * Returns an instance of <code>CheckoutResults</code> for this transaction, if successful.
	 */
	public static CheckoutResults checkoutCart( CreditCardDetails creditCardDetails ) 
	throws CreditCardValidationException, 
	CreditCardAuthorizationException, 
	AuthorizationException,
	CheckoutException
	{
		boolean processPayment = true;

		if( creditCardDetails == null )
		{
			LOGGER.error("checkoutCart method was passed a null creditCardDetails object");
			throw new IllegalArgumentException( "CreditCardDetails cannot be null" );
		}

		if (creditCardDetails.getCcTrxId() != null) {
			processPayment = false;
		}

		CheckoutResults checkoutResults = null;

		String orderAmountString = "";

		if (creditCardDetails.getCurrencyPaidTotal() != null) {
			orderAmountString = creditCardDetails.getCurrencyPaidTotal().toString().trim();
		}

		String paymentProfileCccIdString = null;
		if (creditCardDetails.getPaymentProfileCccId() != null) {
			long cccProfileId = creditCardDetails.getPaymentProfileCccId().longValue();
			paymentProfileCccIdString = String.valueOf(cccProfileId);
		}

		String paymentId= "";
		String merchantRefIdCyb = null;
		String merchantRefId = null;
		String currencyType = creditCardDetails.getCurrencyType();
		double exchangeRate = creditCardDetails.getExchangeRate().doubleValue();

		creditCardDetails.setPaymentGateway(PaymentGateway.CYBERSOURCE);

		Cart cart = CartServices.getCart();

		if (CartServices.getCartChargeTotal().equalsIgnoreCase(ItemConstants.COST_ZERO_DOLLAR)) {
			processPayment=false;
		}

		boolean hasTfDetails = false;
		boolean hasRlDetails = false;

		for (Item item : cart.getItems()) {
			if (item.getRightSourceCd().equals(RightSourceEnum.RL.name()) && 
					item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER)) {
				hasRlDetails = true;
			}
			if (item.getRightSourceCd().equals(RightSourceEnum.TF.name()) && 
					item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER)) {
				hasTfDetails = true;
			}
		}

		if (hasRlDetails && !hasTfDetails) {
			creditCardDetails.setMerchantRefId(ItemConstants.MERCHANT_REFERENCE_MULTIPLE);
		} else {
			try {
				merchantRefId = ServiceLocator.getOrderService().getNewMerchantReferenceIdentifier(new OrderConsumerContext());
				creditCardDetails.setMerchantRefId(merchantRefId);
				merchantRefIdCyb = merchantRefId;
			} catch (Exception e) {
				String logMsg= "error getting merchant reference id sequence" + NEWLINE + ExceptionUtils.getFullStackTrace(e) 
					+ NEWLINE + LOGHDR + LogFormatter.format(cart);
				throw new CheckoutException( "CheckoutServices.checkoutCart: error getting merchant reference id sequence", e, logMsg );           
			}
		}
		boolean hasRlDetailsCyb = false;
		for (Item item : cart.getItems()) {
			if (item.getProductCd().equalsIgnoreCase(ProductEnum.RL.name()) && item.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER)) {
				hasRlDetailsCyb = true;
			}
		}
		if(hasRlDetailsCyb){
			merchantRefIdCyb=ItemConstants.MERCHANT_REFERENCE_MULTIPLE;
		}
		creditCardDetails.getPayment().setPayerPartyId(cart.getBuyerPartyId());
		creditCardDetails.getPayment().setPayerPtyInst(cart.getBuyerPtyInst());  		  

		PaymentResponse paymentResponse = null;
		Payment payment = null;
		PaymentRequest paymentRequest = null;

		if (processPayment)
		{
			try {
				payment = ServiceLocator.getCartService().saveNewPayment(new OrderConsumerContext(), creditCardDetails.getPayment());
				creditCardDetails.setPayment(payment);
				paymentId = creditCardDetails.getPaymentId().toString();
			} catch (Exception e) {
				String logMsg="error writing payment record " + NEWLINE + ExceptionUtils.getFullStackTrace(e) + NEWLINE + LOGHDR + LogFormatter.format(cart) 
						+ LogFormatter.format(creditCardDetails);
				throw new CheckoutException( "CheckoutServices.checkoutCart: error writing new payment record", e, logMsg);
			}

			paymentRequest = new PaymentRequest(orderAmountString,paymentId,currencyType,creditCardDetails.getCardHolderName(),
					creditCardDetails.getCardType(),creditCardDetails.getPaymentProfileIdentifier(),
					paymentProfileCccIdString,creditCardDetails.getCardExpirationDate(),creditCardDetails.getLastFourCc(),merchantRefIdCyb,exchangeRate);

			User cccUser = UserContextService.getSharedUser();
			setUserInfoOnPaymentRequest(cccUser, paymentRequest);
			
			/*
			 *  This is just pre-authorization...
			 */
			paymentResponse = authorizeCreditCard(paymentRequest);
			
			creditCardDetails.setCcAuthNo(paymentResponse.getAuthNum());
			creditCardDetails.setCcAuthDate(new Date());
//			creditCardDetails.setPurchaseDate(new Date());
			creditCardDetails.setReconciliationID(paymentResponse.getReconciliationID());
//			creditCardDetails.setMerchantRefId(paymentResponse.getMerchantRefId());
			creditCardDetails.setCurrencyType(paymentRequest.getCurrencyCode());
			creditCardDetails.setAuthRequestId(paymentResponse.getAuthRequestID());

			creditCardDetails.setAuthRequestToken(paymentResponse.getAuthRequestToken());
			creditCardDetails.setAuthStatus(paymentResponse.getAuthStatus());
			if (creditCardDetails.getAuthStatus() != null && !creditCardDetails.getAuthStatus().equals(CyberSourceCreditCardResponses.SUCCESS) ) {
				String cybersourceMessage = CyberSourceCreditCardResponses.getErrorMessage(creditCardDetails.getAuthStatus());
				creditCardDetails.setCybersourceErrorMessage(cybersourceMessage);				
			} 
			creditCardDetails.setAuthSuccess(paymentResponse.getAuthSuccess());

			try 
			{
				if(paymentResponse != null)
				{
					creditCardDetails.setPayment(ServiceLocator.getCartService().updatePayment(new OrderConsumerContext(), creditCardDetails.getPayment()));
					payment = creditCardDetails.getPayment();
				}
			}
			catch (Exception e) 
			{
				String logMsg="error updating payment info on creditCardDetails " + NEWLINE + ExceptionUtils.getFullStackTrace(e) 
						+ NEWLINE + LOGHDR + LogFormatter.format(cart) 
						+ LogFormatter.format(creditCardDetails);
				throw new CheckoutException("Error updating payment information to checkout", e, logMsg);    	
			}
		}
		else
		{
			payment = new Payment();
			payment.setPaymentMethodCd(ItemConstants.PAYMENT_METHOD_INVOICE);  //TODO This is meant to be an enum in svc order	  
		}
		
		/*
		 * Cart processing takes place only when processPayment is false OR paymentResponse.authSuccess is true
		 * In other words, for processPayment == true, don't process shopping cart unless preauth succeeded.
		 * This prevents clearing shopping cart.
		 */
		if ( (processPayment == false) ||
			 (processPayment == true && paymentResponse != null && paymentResponse.getAuthSuccess()))
		{
			/*
			 * Cart processing goes here...
			 */
			initOrderHeaderContext (cart);
			initItemStatus (cart, creditCardDetails.getPayment());
			initRightsholdersForCartItems(cart);
			initEstimatedQty(cart);
	
			try
			{
				cart = CartServices.updateCart(cart);
			} 
			catch (Exception e)
			{
				String logMsg = "error updating cart just prior to checkout." + NEWLINE + ExceptionUtils.getFullStackTrace(e) 
						+ NEWLINE + LOGHDR + LogFormatter.format(cart);
				throw new CheckoutException("error updating cart just prior to checkout.", e, logMsg);
			}
	
			try
			{
				checkoutResults = checkoutCart(cart, payment);
	
				if (payment != null && payment.getPaymentId() != null)
				{
					payment = ServiceLocator.getOrderService().getPaymentById(new OrderConsumerContext(), payment.getPaymentId());
					creditCardDetails.setPayment(payment);
				}
			}
			catch ( Exception e ) 
			{
				String logMsg="error during checkout using credit card as payment method" + NEWLINE + ExceptionUtils.getFullStackTrace(e) 
						+ NEWLINE + LOGHDR + LogFormatter.format(cart) + LogFormatter.format(payment);
	
				throw new CheckoutException( "CheckoutServices.checkoutCart: error during checkout using credit card as payment method.", e, logMsg );
			}
		}
		
		/*
		 *  This is where the card is charged...
		 */

		if (processPayment && paymentResponse != null && paymentResponse.getAuthSuccess())
		{
			paymentResponse = chargeCreditCard(paymentResponse, paymentRequest);
			
			String authDateTimeString = paymentResponse.getAuthDate();
			creditCardDetails.setCcAuthDateString(authDateTimeString);
			if(authDateTimeString == null ||authDateTimeString == "" )
			{
				_logger.debug("empty time stamp due to amount greater than $1000");
			}
			else 
			{
				try {
					String[] authDateFormat = authDateTimeString.split("T");
					String authDateFormat1 = authDateFormat[0]+":"; 
					String authDateFormat2 = authDateFormat[1];
					if(authDateFormat2.contains("Z")){
						authDateFormat2 = authDateFormat2.replace("Z", "");
					}
					String authDateTime = authDateFormat1+ authDateFormat2;
					DateFormat cybDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
					Date cybAuthDate =cybDateFormat.parse(authDateTime);
					creditCardDetails.setCcTrxDate(cybAuthDate);
				} catch (ParseException e1) {
					LOGGER.error("error parsing authDateTimeString " + authDateTimeString + NEWLINE + ExceptionUtils.getFullStackTrace(e1) 
							+ NEWLINE + LOGHDR + LogFormatter.format(cart) );
				}
			}
//			creditCardDetails.setCcAuthNo(paymentResponse.getAuthNum());
//			creditCardDetails.setCcTrxDate(new Date());
			creditCardDetails.setPurchaseDate(new Date());
			creditCardDetails.setReconciliationID(paymentResponse.getReconciliationID());
//			creditCardDetails.setMerchantRefId(paymentResponse.getMerchantRefId());
			creditCardDetails.setCurrencyType(paymentRequest.getCurrencyCode());
			creditCardDetails.setRequestId(paymentResponse.getRequestID());
			try {
				BigDecimal requestIdBigDecimal = new BigDecimal(paymentResponse.getRequestID().trim());
				creditCardDetails.setCcTrxId(requestIdBigDecimal);
			}catch (NumberFormatException nfe) {
				String logMsg="error assigning requestId " + paymentResponse.getRequestID() + " to CcTrxId on creditCardDetails " 
						+ NEWLINE + ExceptionUtils.getFullStackTrace(nfe) 
							+ NEWLINE + LOGHDR + LogFormatter.format(cart) 
								+ LogFormatter.format(creditCardDetails);
				throw new CheckoutException(nfe,logMsg);
			} 
			creditCardDetails.setRequestToken(paymentResponse.getRequestToken());
			creditCardDetails.setStatus(paymentResponse.getStatus());
			if (creditCardDetails.getStatus() != null && !creditCardDetails.getStatus().equals(CyberSourceCreditCardResponses.SUCCESS) ) {
				String cybersourceMessage = CyberSourceCreditCardResponses.getErrorMessage(creditCardDetails.getStatus());
				creditCardDetails.setCybersourceErrorMessage(cybersourceMessage);
			}
			creditCardDetails.setSuccess(paymentResponse.getSuccess());

			try 
			{
				if(paymentResponse != null)
				{
					creditCardDetails.setPayment(ServiceLocator.getOrderService().updatePayment(new OrderConsumerContext(), creditCardDetails.getPayment()));
//					payment = creditCardDetails.getPayment();
				}
			}
			catch (Exception e) 
			{
				String logMsg="error updating payment info on creditCardDetails " + NEWLINE + ExceptionUtils.getFullStackTrace(e) 
						+ NEWLINE + LOGHDR + LogFormatter.format(cart) 
						+ LogFormatter.format(creditCardDetails);
				throw new CheckoutException("Error updating payment information to checkout", e, logMsg);    	
			}
		}
		
		// Connect credit card details to checkoutResults before it is returned
		if(checkoutResults != null)
		{
			CheckoutResultsImpl check = (CheckoutResultsImpl) checkoutResults;
			check.setCreditCardDetails(creditCardDetails);
			checkoutResults = check;
		}
		
		if (processPayment && paymentResponse != null && !paymentResponse.getSuccess())
		{
			AuthorizationException a = new AuthorizationException("CheckoutServices.checkoutCart: error during checkout using credit card as payment method.");
			String cybersourceErrorMessage = null;
			if(creditCardDetails.getAuthStatus() != null && !creditCardDetails.getAuthStatus().equals("100"))
			{
				cybersourceErrorMessage = CyberSourceCreditCardResponses.getErrorMessage(creditCardDetails.getAuthStatus());
				creditCardDetails.setCybersourceErrorMessage(cybersourceErrorMessage);
				a.setMessageCode(cybersourceErrorMessage);
				_logger.info("Credit Card Auth Failed: " + UserContextService.getActiveAppUser().getUsername() + " PMT rec: " + paymentId + " "
						+ "(" + creditCardDetails.getStatus() + ")" + cybersourceErrorMessage);
				throw new CreditCardAuthorizationException(a, creditCardDetails);
			}
			else
			{
				cybersourceErrorMessage = CyberSourceCreditCardResponses.getErrorMessage(creditCardDetails.getStatus());
				creditCardDetails.setCybersourceErrorMessage(cybersourceErrorMessage);
				a.setMessageCode(cybersourceErrorMessage);
				_logger.info("Credit Card Charge Failed: " + UserContextService.getActiveAppUser().getUsername() + " PMT rec: " + paymentId + " "
						+ "(" + creditCardDetails.getStatus() + ")" + cybersourceErrorMessage);
//				throw new CreditCardAuthorizationException(a, creditCardDetails);
			}
		}

		//check
		return checkoutResults;
	}


	/**
	 * Returns a new instance of <code>CreditCardDetails</code>.
	 */
	public static CreditCardDetails createCreditCardDetails(){

		return new CreditCardDetailsImpl( new Payment() );

	}

	/**
	 * Returns a new instance of <code>InvoiceDetails</code>.
	 */
	public static InvoiceDetails createInvoiceDetails(){

		return new InvoiceDetailsImpl( );

	}


	/**
	 * Adds an instance of <code>PurchasablePermission</code> to an existing order identified by <code>orderNumber</code>
	 * argument. Returns the confirmation number for the transaction.
	 */
	@SuppressWarnings("deprecation")
	public static long addItemToExistingOrder( PurchasablePermission purchasablePermission, long orderNumber, OrderBundle orderBundle ) 
	throws OrderClosedException, CourseNotDefinedException,
	IncompatibleOrderAndPurchasablePermissionException
	{
		if( purchasablePermission == null)
		{
			LOGGER.error("PurchasablePermission not found");

			throw new IllegalArgumentException( "PurchasablePermission cannot be null" );
		}

		OrderConsumerContext orderConsumerContext = new OrderConsumerContext();

		OrderPurchase orderPurchase = null;

		PurchasablePermissionImpl ppImpl = (PurchasablePermissionImpl) purchasablePermission;
		Item item = ppImpl.getItem();

		try {
			orderPurchase = OrderPurchaseServices.getOrderPurchaseForConfirmationNumber(orderNumber);
		} catch (OrderPurchasesException opex) {
			LOGGER.error(LogUtil.getStack(opex));
			throw new CheckoutException( "Unable to add item to order, couldn't find order: " + orderNumber );
		}

		if (orderPurchase==null) {
			throw new CheckoutException( "null OrderPurchase returned for order " + orderNumber);
		}

		if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {

			try {

				if (ppImpl.isAcademic()) {

					OrderSearchResult orderSearchResult = OrderLicenseServices.searchAllOrderLicensesForConfirmationNumber(orderNumber);

					for (OrderLicense orderLicense : orderSearchResult.getOrderLicenses()) {
						if (orderLicense.isAcademic()) {
							if ( orderLicense.getBundleId() != null ) {
								item.setBundleId(orderLicense.getBundleId());
								break;
							}
						}
					}

					/*
					 * part of fix for CC-1795 unable to add item to existing TF order
					 * 
					 * cause: item.getBundleId() != null because we had computed it above.
					 * Given that, we fell to the else clause that was below (has been removed) 
					 * and it would throw an exception.
					 * It think the intent was to make one more attempt to figure out the 
					 * bundleId. If that doesn't work, then throw an exception.
					 */
					/*
					 * if we didn't determine the bundleId above, see if we can grab it from
					 * the orderBundle parameter. If not, we can't continue.
					 */
					if (item.getBundleId() == null && orderBundle != null) {
						OrderBundle newOrderBundle = OrderPurchaseServices.updateOrderBundle(orderPurchase, orderBundle);
						item.setBundleId(newOrderBundle.getBundleId());
					}
					// if we STILL don't have a bundle we can't recover
					if (item.getBundleId() == null) {
						throw new CourseNotDefinedException(ppImpl);
						//throw new CourseNotDefinedException( "No bundle created for academic item for order: " + orderNumber );            		
					}

				}

				//	        	item.setItemStatusCd(ItemStatusEnum.INVOICE_READY);     	
				CheckoutServices.initItemStatusForAddItemToOrder(item);
				CheckoutServices.initRightsholdersForItem(item);

				ServiceLocator.getOrderService().addItemToOrder(orderConsumerContext, orderPurchase.getOrderHeaderId(), item);
				//ServiceLocator.getOrderService().addItemToBundle(new OrderConsumerContext(), item.getBundleId(), item);
				//ServiceLocator.getOrderService().a
			} catch(CourseNotDefinedException cdne) {
	  			LOGGER.error(LogUtil.getStack(cdne));				
				throw new CourseNotDefinedException(ppImpl);
			} catch (Exception e) {
				LOGGER.error("Unable to add item " + item.getItemId() + " to order " + orderPurchase.getOrderHeaderId() 
						+ NEWLINE + ExceptionUtils.getFullStackTrace(e));
				throw new CheckoutException( "Unable to add item to order: " + orderNumber , e);
			}
			return orderPurchase.getConfirmationNumber();
		}

		if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.TF.getOrderDataSourceId().intValue()) {

			Long confirmationNumber = null;

			com.copyright.data.order.Cart temporaryCart;

			try
			{
				temporaryCart = getNewTfCartInstance();
			} catch (Exception e)
			{
	  			LOGGER.error(LogUtil.getStack(e));				
				throw new CheckoutException("Unable to get new cart instance", e );
			}


			CoursePack coursePack = null;

			//CoursePack retrieval process
			if( purchasablePermission.isAcademic() )
			{

				try
				{

					boolean isOrderPurchaseConsistent = false;

					if (orderPurchase != null) {
						if (orderPurchase.getCoursePack() != null) {
							isOrderPurchaseConsistent = true;
						}
					}

					//	        	  boolean isOrderPurchaseConsistent = orderPurchase != null && 
					//	                                                  orderPurchase.getCoursePack() != null;

					if ( isOrderPurchaseConsistent )
					{
						coursePack = orderPurchase.getCoursePack();
					}else
					{
						throw new CheckoutException( "Inconsistent OrderPurchase for confirmation number " + orderNumber + " found." );
					}

					temporaryCart = getCartServiceAPI().updateCoursePack( coursePack, temporaryCart );

				} catch (Exception e)
				{
		  			LOGGER.error(LogUtil.getStack(e));
					throw new CheckoutException("Unable to retrieve OrderPurchases for confirmation number " + orderNumber, e );
				}

			}

			try
			{

				PermissionRequest permissionRequest = PurchasablePermissionFactory.buildPermissionRequest(ppImpl);

				temporaryCart = getCartServiceAPI().addToCart( permissionRequest, temporaryCart );   

				if (orderPurchase.isAcademic()) {
					temporaryCart = getCartServiceAPI().updateCoursePack( coursePack, temporaryCart );
				}

				CheckoutData checkoutData = new CheckoutData();

				checkoutData.setPurInst( orderNumber );

				PaymentData paymentData = buildTfPaymentData();
				// TODO Set-up the payment data    

				paymentData.setPaymentMethod( new InvoiceRequest() );

				checkoutData.setPaymentData( paymentData );

				checkoutData.setCart( temporaryCart );

				confirmationNumber = getCashierServiceAPI().checkoutCart( checkoutData );
			}
			catch ( Exception e )
			{
				LOGGER.error("Could not checkout temporary cart for order number " + orderNumber + LogUtil.appendableStack(e) , e );

				throw new CheckoutException( "Could not checkout temporary cart for order number " + orderNumber , e );
			}

			if( confirmationNumber != null )
			{
				LOGGER.debug("Item added to order " + orderNumber + " successfully" );

				return confirmationNumber.longValue();

			}else
			{
				LOGGER.error("Could not get a valid confirmation number for transaction" );

				throw new CheckoutException( "Could not get a valid confirmation number for transaction" );
			}    
		}

		return 0;

	}

	private static CashierServiceAPI getCashierServiceAPI()
	{
		return CashierServiceFactory.getInstance().getService();
	}

	private static com.copyright.data.order.Cart getNewTfCartInstance()
	{
		return getCartServiceAPI().getNewCart(TransactionConstants.CC_ORDER_SOURCE_CODE);
	}

	private static CartServiceAPI getCartServiceAPI()
	{
		return CartServiceFactory.getInstance().getService();
	}

	private static PaymentData buildTfPaymentData()
	{
		PaymentData  paymentData = new PaymentData();

		User user = UserContextService.getSharedUser();

		//  2009-07-23  MSJ
		//  Gotta xfer our new user to our old user object type.
		//  I followed the code down pretty low and it LOOKS like all
		//  it really needs is the party ID.

		//Make sure ldapUser attribute value is up to date
		user.refreshBasicData();

		//Get the latest User data
		user.loadRegistrationData();

		com.copyright.data.account.User tmpUser = 
			new com.copyright.data.account.User(
					user.getUsername(), user.getEmailAddress(), user.getPassword(),
					user.getFirstName(), user.getLastName());

		tmpUser.setPartyId(user.getPartyId());
		paymentData.setBuyer( tmpUser );

		//TODO 11/08/2006 lalberione: Do we have to provide a billing ref? If so, where to get it from?
		//paymentData.setBillingRef(billingCode);

		return paymentData;
	}

	private static void initRightsholdersForCartItems(Cart cart) {

		TreeMap<Long, RightsholderOrganization> userMap = new TreeMap<Long, RightsholderOrganization>();
		Long ptyInst = null;

		for (Item item : cart.getItems()) {
			for (ItemFees itemFees : item.getAllFees()) {
				try {      	
					//TODO getByrInst() sometimes throws an exception on null to long conversion
					ptyInst = itemFees.getOrigDistPtyInst();
					if (ptyInst != null) {
						if (userMap.containsKey(ptyInst)) {
							RightsholderOrganization rhOrg;
							rhOrg = userMap.get(ptyInst);
							String rhAcct = null;
							if (rhOrg.getRhAccountNumber() != null) {
								rhAcct = rhOrg.getRhAccountNumber().toString();
							}
							itemFees.setOrigDistAccount(rhAcct);
							itemFees.setOrigDistPartyId(rhOrg.getOrgPartyId());
							itemFees.setActualDistAccount(rhAcct);
							itemFees.setActualDistPartyId(rhOrg.getOrgPartyId());
						} else {
							//                    		byrInstLong = new Long (98); // For Dev Testing
							RightsholderOrganization rhOrg;
							rhOrg = UserServices.getOrganizationByPtyInst(ptyInst);
							userMap.put(ptyInst, rhOrg);
							String rhAcct = null;
							if (rhOrg.getRhAccountNumber() != null) {
								rhAcct = rhOrg.getRhAccountNumber().toString();
							}
							itemFees.setOrigDistAccount(rhAcct);
							itemFees.setOrigDistPartyId(rhOrg.getOrgPartyId());
							itemFees.setActualDistAccount(rhAcct);
							itemFees.setActualDistPartyId(rhOrg.getOrgPartyId());
						}
					}
				}
				catch (Exception e) {
					LOGGER.warn("Rightsholder ptyInst: " + ptyInst + " Not Found " + LogUtil.appendableStack(e));
				}

			}
		}
	}

	private static void initRightsholdersForItem(Item item) {

		TreeMap<Long, RightsholderOrganization> userMap = new TreeMap<Long, RightsholderOrganization>();
		Long ptyInst = null;

		for (ItemFees itemFees : item.getAllFees()) {
			try {      	
				//TODO getByrInst() sometimes throws an exception on null to long conversion
				ptyInst = itemFees.getOrigDistPtyInst();
				if (ptyInst != null) {
					if (userMap.containsKey(ptyInst)) {
						RightsholderOrganization rhOrg;
						rhOrg = userMap.get(ptyInst);
						String rhAcct = null;
						if (rhOrg.getRhAccountNumber() != null) {
							rhAcct = rhOrg.getRhAccountNumber().toString();
						}
						itemFees.setOrigDistAccount(rhAcct);
						itemFees.setOrigDistPartyId(rhOrg.getOrgPartyId());
						itemFees.setActualDistAccount(rhAcct);
						itemFees.setActualDistPartyId(rhOrg.getOrgPartyId());
					} else {
						//                    		byrInstLong = new Long (98); // For Dev Testing
						RightsholderOrganization rhOrg;
						rhOrg = UserServices.getOrganizationByPtyInst(ptyInst);
						userMap.put(ptyInst, rhOrg);
						String rhAcct = null;
						if (rhOrg.getRhAccountNumber() != null) {
							rhAcct = rhOrg.getRhAccountNumber().toString();
						}
						itemFees.setOrigDistAccount(rhAcct);
						itemFees.setOrigDistPartyId(rhOrg.getOrgPartyId());
						itemFees.setActualDistAccount(rhAcct);
						itemFees.setActualDistPartyId(rhOrg.getOrgPartyId());
					}
				}
			}
			catch (Exception e) {
				LOGGER.warn("Rightsholder ptyInst: " + ptyInst + " Not Found " + LogUtil.appendableStack(e));
			}

		}
	}

}
