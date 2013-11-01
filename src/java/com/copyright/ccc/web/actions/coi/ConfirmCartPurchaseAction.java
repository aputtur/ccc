package com.copyright.ccc.web.actions.coi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.CheckoutResults;
import com.copyright.ccc.business.data.OrderPartnerXref;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum;
import com.copyright.ccc.business.data.OrderSearchCriteriaSortDirectionEnum;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CheckoutException;
import com.copyright.ccc.business.services.cart.CheckoutResultsImpl;
import com.copyright.ccc.business.services.cart.CheckoutServices;
import com.copyright.ccc.business.services.cart.CreditCardAuthorizationException;
import com.copyright.ccc.business.services.cart.CreditCardDetails;
import com.copyright.ccc.business.services.cart.CreditCardValidationException;
import com.copyright.ccc.business.services.cart.InvoiceDetails;
import com.copyright.ccc.business.services.cart.InvoiceValidationException;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.order.OrderPartnerXrefServices;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.rlnk.RlnkRightsServices;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.config.CC2Constants;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.BasePaymentForm;
import com.copyright.ccc.web.forms.coi.ReviewSubmitCartActionForm;
import com.copyright.data.payment.AuthorizationException;
import com.copyright.data.payment.InvoiceRequest;
import com.copyright.rightslink.base.data.OrderResponse;
import com.copyright.rightslink.base.data.PaymentData;
import com.copyright.rightslink.base.data.PriceData;
import com.copyright.svc.centralQueue.api.data.CQConsumerContext;
import com.copyright.svc.centralQueue.api.data.EmailType;
import com.copyright.svc.centralQueue.api.data.Source;
import com.copyright.svc.order.api.OrderService;
import com.copyright.svc.order.api.data.Cart;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemParm;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.ItemStatusQualifierEnum;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.rightsResolver.api.data.ItemRequest;
import com.copyright.svc.rightsResolver.api.data.OrderRequest;
import com.copyright.svc.rightsResolver.api.data.RightsResolverConsumerContext;
import com.copyright.svc.rlOrder.api.RLOrderServicesInterface;
import com.copyright.svc.rlOrder.api.data.JobTicket;
import com.copyright.svc.rlOrder.api.data.License;
import com.copyright.svc.rlOrder.api.data.OrderAttribute;
import com.copyright.svc.telesales.api.data.ContactTypeEnum;
import com.copyright.svc.telesales.api.data.TelesalesServiceConsumerContext;
import com.copyright.svc.telesales.api.data.UserTypeEnum;



public class ConfirmCartPurchaseAction extends CCAction
{
	/* public ActionForward confirmCartPurchase( ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response )
    {
        return mapping.findForward( "purchaseOK" );
    } */

	private final static String PURCHASE_CART = "purchaseOK";
	private final static String EDIT_PAYMENT_INFO = "editPayment";
	private final static String CREDIT_AUTH_ERROR = "error";
	private final static String CONFIRM_CART_SESSION_EXPIRED_MSG_KEY = "revieworder.session.expired.msg";
	private final static String NEWLINE = CC2Constants.NEWLINE;
	protected static final Logger _logger = Logger.getLogger( ConfirmCartPurchaseAction.class );
	private static final String ORDER_BEAN = "Order Bean";
	private static final String STRING = "String";
	private static final String G = "G";
	private static final String RLNK = "RLNK";



	public ConfirmCartPurchaseAction()
	{
	}

	/**
	 * If no operation is specified, go to <code>confirmPurchase()</code>.
	 */
	public ActionForward defaultOperation( ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response )
	{
		return confirmPurchase( mapping, form, request, response );
	}

	/**
	 * Struts action that allows for editing of the data on the review order
	 * JSP. There are two forward actions coded depending on if the user has
	 * elected to edit the payment information or the address information.
	 * @param mapping is the struts ActionMapping passed in by the struts controller
	 * @param form is the struts ActionForm passed in by the struts controller
	 * @param request is the HttpServletRequest object
	 * @param response is the HttpServletResponse object
	 * @return the ActionForward object to the "editAddress" mapping or the
	 * "editPayment" action mapping.
	 */
	public ActionForward confirmPurchase( ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response )
	{
		ReviewSubmitCartActionForm cartForm = 
			castForm( ReviewSubmitCartActionForm.class, form );
		/*
		 * if the session was newly created, it must have just expired.
		 * Send back to cart page.
		 * the "nested if" handles the case where the user visits the page without a properly
		 * populated session AND the session is known to be new.
		 */
		if (cartForm.getCartItems()==null || cartForm.getCartItems().isEmpty()) {
			ActionForward af = mapping.findForward("viewCart");			
			if (UserContextService.isSessionNewlyCreated()) {
				request.setAttribute(WebConstants.RequestKeys.SESSION_EXPIRED_MSG,
						getResources(request).getMessage(CONFIRM_CART_SESSION_EXPIRED_MSG_KEY));
			}
			return af;
		}

		String editPaymentButton = request.getParameter( "editPayment" );
		mapping.findForward( CREDIT_AUTH_ERROR );

		ActionForward forward;
		/* ************* Comment 11/15/2006 ***************** */
		if ( editPaymentButton != null && !editPaymentButton.equals( "" ) )
		{
			// The EDIT Payment info button was pressed
			forward = mapping.findForward( EDIT_PAYMENT_INFO );
			//forward = mapping.findForward("reviewPayment");
			return forward;
		}
		else
		{
			// Call the Cashier service here and return accordingly
			// ************ Comment out 11/15/2006
			forward = this.purchaseOrder(cartForm, mapping, request);
			//return mapping.findForward( "purchaseOK" );
			return forward;

		}
	}

	/**
	 * Attempt to submit the shopping cart for purchase.  Return the
	 * action forward depending on the success or failure of the purchase \
	 * submission is successful
	 * @param form is the ReviewSubmitCartActionForm backing the confirmPurchase.jsp
	 * @param mapping is the Struts ActionMapping object.
	 * @param req is the HttpServletRequest.
	 */
	private ActionForward purchaseOrder( ReviewSubmitCartActionForm form, 
			ActionMapping mapping, 
			HttpServletRequest req )
	{		
		ReviewSubmitCartActionForm reviewForm = form;

		ActionForward forward;  
		//KeyValuePair kvPair;
		String paymentType = reviewForm.getPaymentType();
		String ccCardType = reviewForm.getCreditCardType();
		String paymentProfileId = reviewForm.getPaymentProfileId();
		String cccPaymentProfileId = reviewForm.getCccPaymentProfileId();
		String currencyType = reviewForm.getFundingCurrencyType();
		String fundingCurrencyAmount = reviewForm.getFundingCurrencyAmount();
		if(fundingCurrencyAmount.contains(",")){
			fundingCurrencyAmount = fundingCurrencyAmount.replace(",", "");
		}
		String exchangeRate = reviewForm.getExchangeRate();
		int currencyRateId = reviewForm.getFundingCurrencyRateId();
		String ccExpirationDate = reviewForm.getExpirationDate();
		String cardHolderName = reviewForm.getCreditCardNameOn();
		CheckoutResults chkResults;

		if (CartServices.isCartEmpty())
		{
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.checkout", "Shopping cart must not be empty when trying to checkout shopping cart"));
			saveErrors(req, errors);
			return (mapping.findForward("cartEmpty"));
		}


		/*
		 * ensure this isn't the 2nd submit. You must pass a true 
		 * to remove the token from the session. If you don't, subsequent
		 * submits will succeed because the token will remain in the session.
		 */
		if ( ! this.isTokenValid( req, true ) ) {
		//if ( ! this.isTokenValid(req) ) {
			// Set user message and forward to the result page
			ActionMessages errors = new ActionMessages();
			ActionMessage msg = new ActionMessage( "advisor.double.submit" );
			errors.add( "dblSbmt", msg );
			this.saveErrors( req, errors );
			return mapping.findForward( PURCHASE_CART );
		}

		Cart cart = CartServices.getCart();

		// TODO: Wrap in a try/catch(ValidationException) and look for 
		// credit authorization error messages.
		if ( BasePaymentForm.INVOICE_TYPE.equalsIgnoreCase( paymentType ) || BasePaymentForm.NA.equals( paymentType ) )
		{
			// Invoice order
			InvoiceRequest invRequest = new InvoiceRequest();
			invRequest.setPoNum( form.getPurchaseOrderNumber() );         
			InvoiceDetails invDetails = CheckoutServices.createInvoiceDetails();
			invDetails.setPONumber( form.getPurchaseOrderNumber() );
			invDetails.setPromotionCode(form.getPromoCode());

			try {
				chkResults = CheckoutServices.checkoutCart(invDetails);
			} catch ( InvoiceValidationException e )
			{
				_logger.error("CKOUTERR_BEGIN " + NEWLINE + ExceptionUtils.getFullStackTrace(e) + NEWLINE + "CKOUTERR_END" );
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.checkout", e.getMessage()));
				saveErrors(req, errors);
				return (mapping.findForward("error"));
			}
			catch ( CheckoutException e )
			{
				if (e.getLogMessage()!=null && e.getLogMessage().length()>0) {
					_logger.error("CKOUTERR_BEGIN" + NEWLINE + e.getLogMessage() + NEWLINE 
							+ ExceptionUtils.getFullStackTrace(e) + NEWLINE + "CKOUTERR_END");
				} else {
					_logger.error("CKOUTERR_BEGIN " + NEWLINE + ExceptionUtils.getFullStackTrace(e) + NEWLINE + "CKOUTERR_END" );
				}
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.checkout", e.getMessage()));
				saveErrors(req, errors);
				return (mapping.findForward("error"));
			} catch (Exception e) {
				_logger.error("CKOUTERR_BEGIN " + NEWLINE + ExceptionUtils.getFullStackTrace(e) + NEWLINE + "CKOUTERR_END" );
				throw new CCRuntimeException(e);
			}
		}
		else
		{
			// Credit Card order

			CreditCardDetails creditCardDetails = 
				CheckoutServices.createCreditCardDetails();

			creditCardDetails.setCurrencyType(currencyType);
			creditCardDetails.setExchangeRate(new BigDecimal(exchangeRate));
			creditCardDetails.setCurrencyPaidTotal(new BigDecimal(fundingCurrencyAmount));
			creditCardDetails.setUsdPaidTotal(CartServices.getTotalChargePrice(cart));
			creditCardDetails.setCurrencyRateId(Long.valueOf(currencyRateId));
			Date exchangeRateDate = reviewForm.getCurrencyRateDate();
			creditCardDetails.setExchangeDate(exchangeRateDate);
			creditCardDetails.setCardHolderName(cardHolderName);
			creditCardDetails.setCardExpirationDate(ccExpirationDate);
			creditCardDetails.setCardType(ccCardType);
			String ccNumber = reviewForm.getCreditCardNumber();
			try
			{
				creditCardDetails.setLastFourCc(new Integer(ccNumber));
			}catch (NumberFormatException nfe){
				_logger.error("CKOUTERR_BEGIN " + NEWLINE + ExceptionUtils.getFullStackTrace(nfe) + NEWLINE + "CKOUTERR_END" );
				throw new CCRuntimeException(nfe);
			}
			creditCardDetails.setPaymentProfileIdentifier(paymentProfileId);
			try {
				creditCardDetails.setPaymentProfileCccId(new Long(cccPaymentProfileId));
			}catch (NumberFormatException nfe) {
				_logger.error("CKOUTERR_BEGIN " + NEWLINE + ExceptionUtils.getFullStackTrace(nfe) + NEWLINE + "CKOUTERR_END" );
				throw new CCRuntimeException(nfe);
			}

			try
			{
				chkResults = CheckoutServices.checkoutCart( creditCardDetails );
			} catch ( CreditCardValidationException e )
			{
				_logger.error( ExceptionUtils.getFullStackTrace(e) );
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.credit-card-invalid", (e.getMessage() == null) ? "" : e.getMessage()));
				saveErrors(req, errors);
				return (mapping.findForward("creditCardFailure"));
			} catch ( CreditCardAuthorizationException e )
			{
				ActionMessages errors = new ActionMessages();
				String cybersourceErrorMessage = null; 
				if (e.getCreditCardDetails() != null) {
					cybersourceErrorMessage = e.getCreditCardDetails().getCybersourceErrorMessage();
				}
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.credit-card", StringUtils.defaultString(cybersourceErrorMessage)));
				saveErrors(req, errors);
				return (mapping.findForward("creditCardFailure"));
			}catch ( AuthorizationException e ) 
			{
				_logger.error( ExceptionUtils.getFullStackTrace(e) );
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.credit-card", (e.getMessage() == null) ? "" : e.getMessage()));
				saveErrors(req, errors);
				return (mapping.findForward("creditCardFailure"));
			} catch ( CheckoutException e )
			{
				if (e.getLogMessage()!=null && e.getLogMessage().length()>0) {
					_logger.error("CKOUTERR_BEGIN" + NEWLINE + e.getLogMessage() + NEWLINE 
							+ ExceptionUtils.getFullStackTrace(e) + NEWLINE+ "CKOUTERR_END");
				} else {
					_logger.error("CKOUTERR_BEGIN " + NEWLINE + ExceptionUtils.getFullStackTrace(e) + NEWLINE + "CKOUTERR_END" );
				}
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.checkout", e.getMessage()));
				saveErrors(req, errors);
				return (mapping.findForward("error"));
			} catch (Exception e) {
				_logger.error("CKOUTERR_BEGIN " + NEWLINE + ExceptionUtils.getFullStackTrace(e) + NEWLINE + "CKOUTERR_END" );
				throw new CCRuntimeException(e);
			}
		}


		//Reset the Items List in the Form

		if (chkResults != null)
		{
			List<OrderLicense> ordLicenseList = chkResults.getOrderItems();
			for (OrderLicense ordLic : ordLicenseList)
			{
				if (ordLic.getProductCd() != null)
				{
					if (ordLic.isRightsLink())
					{
						_logger.debug("RL Item id: " + ordLic.getID());
						OrderRequest orderRequest = new OrderRequest();

						CheckoutResultsImpl chk = (CheckoutResultsImpl) chkResults;
						ItemRequest itemRequest = PricingServices.getRLItemRequestFromOrderLicense(chk.getOrderPurchase(), ordLic);
						orderRequest.setItemRequest(itemRequest);

						PaymentData paymentData = new PaymentData();

						if (ordLic.isSpecialOrder() && BasePaymentForm.CREDIT_TYPE.equalsIgnoreCase(paymentType)) {
							CreditCardDetails creditCardDetails = chk.getCreditCardDetails();
							if (creditCardDetails != null)
							{
								paymentData.setAuthNum(null);
								paymentData.setCardType(creditCardDetails.getCardType());
								paymentData.setCardHolderName(cardHolderName);
								paymentData.setPlainTextExpirationDate(ccExpirationDate);
								paymentData.setPlainTextLast4Digits(creditCardDetails.getLastFourCc().toString());
								paymentData.setCurrencyRateId(creditCardDetails.getCurrencyRateId().intValue());
								paymentData.setReconciliationID(null);
								paymentData.setRequestID(null);
								paymentData.setRequestToken(null);
								paymentData.setRlProfileId(creditCardDetails.getPaymentProfileCccId().toString());
								paymentData.setStatus(null);
								paymentData.setFundingCurrency(creditCardDetails.getCurrencyType());
								//paymentData.setFundingValue(ordLic.getTotalPriceValue().doubleValue());
								paymentData.setFundingValue(ordLic.getPriceValueRaw());
								paymentData.setSuccess(false);            					
							}else{
								_logger.error("NULL CC Profile returned");
							}
						}
						else if ( BasePaymentForm.INVOICE_TYPE.equals( paymentType ) || BasePaymentForm.NA.equals( paymentType ))
						{ 
							paymentData.setAuthNum("0");
							paymentData.setRlProfileId("0");
							paymentData.setCurrencyRateId(currencyRateId);//temporary usd 
							paymentData.setFundingCurrency(currencyType);
							paymentData.setFundingValue(ordLic.getPriceValueRaw());
							paymentData.setSuccess(true);
						} 
						else
						{
							CreditCardDetails creditCardDetails = chk.getCreditCardDetails();

							if (creditCardDetails != null)
							{
								paymentData.setAuthNum(creditCardDetails.getCcAuthNo());
								paymentData.setCardType(creditCardDetails.getCardType());
								paymentData.setCardHolderName(cardHolderName);
								paymentData.setPlainTextExpirationDate(ccExpirationDate);
								paymentData.setPlainTextLast4Digits(creditCardDetails.getLastFourCc().toString());
								paymentData.setCurrencyRateId(creditCardDetails.getCurrencyRateId().intValue());
								//paymentData.setCurrencyRateId(creditCardDetails.getCurrencyRateId().intValue());
								paymentData.setReconciliationID(creditCardDetails.getReconciliationID());
								paymentData.setRequestID(creditCardDetails.getRequestId());
								paymentData.setRequestToken(creditCardDetails.getRequestToken());
								paymentData.setRlProfileId(creditCardDetails.getPaymentProfileCccId().toString());
								paymentData.setStatus(creditCardDetails.getStatus());
								paymentData.setFundingCurrency(creditCardDetails.getCurrencyType());
								//paymentData.setFundingValue(ordLic.getTotalPriceValue().doubleValue());
								paymentData.setFundingValue(ordLic.getPriceValueRaw());
								paymentData.setSuccess(creditCardDetails.isSuccess());
							}

						}

						orderRequest.setPaymentData(paymentData);
						orderRequest.setSessionID(RlnkRightsServices.getRlnkSessionID());

						OrderResponse orderResp = ServiceLocator.getRightsProcessorService().createOrder(new RightsResolverConsumerContext(), orderRequest);

						if (orderResp.isExceptionOccurred())
						{
							_logger.error("Exception in RL Full Stack Trace: " + ExceptionUtils.getFullStackTrace(orderResp.getThrowable()));
							_logger.error("Exception in RL: " + orderResp.getThrowable().getMessage());
						}

						//String rlLicenseNo = orderResp.get("LICENSENO");
						//String rlJobTicketNo = orderResp.get("JOBTICKETID");

						Long rlLicenseNo = null;
						Long rlJobTicketNo = null;

						String invoiceNo = null;

						if(orderResp.getLicenseNo()!= null && !orderResp.getLicenseNo().isEmpty())
						{
							try
							{
								rlLicenseNo = Long.valueOf(orderResp.getLicenseNo());
								invoiceNo = orderResp.getInvoiceNo();
								ordLic.setExternalDetailId(Long.valueOf(orderResp.getLicenseNo()));

								if (invoiceNo != null)
								{
									invoiceNo = "RLNK" + invoiceNo;
								}
								_logger.info( "Create Order returned  License#: " + rlLicenseNo + " for Invoice#: " + invoiceNo + " and Item Id: " + ordLic.getID() );
							}
							catch (NumberFormatException e)
							{
								rlLicenseNo = null;
								_logger.error("could not create Long from license number for Item Id: " + ordLic.getID() + LogUtil.appendableStack(e));
							}
						}

						if(orderResp.getJobTicketID() != null && !orderResp.getJobTicketID().isEmpty())
						{
							try
							{
								rlJobTicketNo = Long.valueOf(orderResp.getJobTicketID());
								ordLic.setExternalDetailId(Long.valueOf(orderResp.getJobTicketID()));
								_logger.info( "Create Order returned Job Ticket#: " + rlJobTicketNo + " for Item Id: " + ordLic.getID());
							}
							catch (NumberFormatException e)
							{
								rlJobTicketNo = null;
								_logger.error( "could not create Long from job ticket number for Item Id: " + ordLic.getID()+ LogUtil.appendableStack(e));
							}
						}

						//Call COI replication           				            				            				            				            				            					
						if (!orderResp.isExceptionOccurred())
						{
							replicateInCOI(orderResp, ordLic);
						}

					}
				}

			}

			//Get the Updated Order Licenses to display on the Order Confirmation page 
			OrderSearchResult orderSearchResult = null;

			try
			{
				OrderSearchCriteria orderSearchCriteria=new OrderSearchCriteria();
				orderSearchCriteria.setSortByColumn(OrderSearchCriteriaSortColumnEnum.SORT_BY_ORDER_DETAIL_ID);
				orderSearchCriteria.setSortDirection(OrderSearchCriteriaSortDirectionEnum.DESC);
				orderSearchCriteria.setConfirmNumber(chkResults.getOrderConfirmationNumber());
				orderSearchCriteria.setSearchToRow(ItemConstants.MAX_CONFIRM_ROWS);
				orderSearchResult  = OrderLicenseServices.searchCOIOrderLicenses(orderSearchCriteria); //searchOrderLicensesForConfirmationNumber(chkResults.getOrderConfirmationNumber());
			}
			catch ( OrderLicensesException ole )
			{
				_logger.error(ExceptionUtils.getFullStackTrace(ole));
			}

			reviewForm.setCartItems(new ArrayList<TransactionItem>());
			if(orderSearchResult!=null){
				reviewForm.setCartItems( orderSearchResult.getOrderLicenses());	
			}

			reviewForm.setConfirmNumber( chkResults.getOrderConfirmationNumber() );
			reviewForm.setOrderDate(chkResults.getOrderDate());

			User sessionUser = UserContextService.getSharedUser();
			String userName = "";
			String userNameForGreeting = StringUtils.EMPTY;
			if (sessionUser != null)
			{
				userName = sessionUser.getUsername();
				userNameForGreeting = StringUtils.join(
						new Object[] {
								sessionUser.getFirstName(),
								sessionUser.getLastName()}," ");
			}

			ServiceLocator.getCentralQueueService().orderPlaced(new CQConsumerContext(), Source.COPYRIGHT_COM, userName);

			//	2012-01-04	MSJ
			//	Adding code to stuff the relationship between the PermissionsDirect
			//	user and the order confirmation.  This will be used by finance
			//	to tie purchases back to specific users/sids/wc ids.  This is
			//	for reporting purposes.
			//	2013-08-28	MSJ
			//	We switched from webtrends to google analytics.

			String utm_source = (String) req.getSession()
				.getAttribute( WebConstants.SessionKeys.UTM_SOURCE );

			if (utm_source != null)
			{
				OrderPartnerXrefServices svc = new OrderPartnerXrefServices();
				OrderPartnerXref opx = svc.createOrderPartnerXref();

				opx.setOrderConfirm( chkResults.getOrderConfirmationNumber() );
				opx.setPartnerId( utm_source );

				svc.setOrderPartnerXref( opx );

				if (_logger.isDebugEnabled()) {
					_logger.info("\nSetting Order Partner XRef value : " + utm_source);
				}
			}

			// Attempt to send confirmation email - queue it up to the central queue for delivery

			boolean contactRestricted = false;

			if (sessionUser.getTelesalesIsUp()) {
				if(null != sessionUser && null != sessionUser.getPartyId() && null != sessionUser.getArAccountNumber()){
					UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(UserTypeEnum.class, sessionUser.getLdapUser().getUserType());
					contactRestricted = ServiceLocator.getTelesalesService().isContactRestricted(new TelesalesServiceConsumerContext(), 
							sessionUser.getPartyId(), sessionUser.getArAccountNumber(), userTypeEnum, ContactTypeEnum.EMAIL_ORDER_CONFIRMATION);

				}
			}

			if(!contactRestricted)
			{
				ServiceLocator.getCentralQueueService().createEmail(new CQConsumerContext(), 
						Source.COPYRIGHT_COM, 
						EmailType.ORDER_CONFIRMATION, 
						userNameForGreeting,
						userName,  Long.valueOf(chkResults.getOrderConfirmationNumber()).toString());
			}

			forward = mapping.findForward( PURCHASE_CART );
		}
		else
		{
			ActionMessages errors = new ActionMessages();
			ActionMessage msg = new ActionMessage( "Problem with Checkout...." );
			errors.add( "error", msg );
			this.saveErrors( req, errors );
			forward = mapping.findForward( "error" );  
		}

		//forward = mapping.findForward( PURCHASE_CART );
		//forward = (ActionForward) confNumber;

		return forward;
	}


	public boolean replicateInCOI(OrderResponse ordResp, OrderLicense ordLic)
	{
		//	Order order = getOrder();
		RLOrderServicesInterface svcRlOrder = ServiceLocator.getRLOrderService();
		OrderService orderService = ServiceLocator.getOrderService();
		OrderConsumerContext orderConsumerContext = new OrderConsumerContext();
		Item coiItem = orderService.getItemById(new OrderConsumerContext(), Long.valueOf(ordLic.getID()));

		Long rlJobTicketNo = null;

		String invoiceNo = null;

		if(ordResp.getLicenseNo()!= null && !ordResp.getLicenseNo().isEmpty())
		{
			try
			{
				invoiceNo = ordResp.getInvoiceNo();
	
				if (invoiceNo != null)
				{
					invoiceNo = "RLNK" + invoiceNo;
				}
	
			}
			catch (NumberFormatException e)
			{
				_logger.error("could not create Long from licenseNo " + ordResp.getLicenseNo() + LogUtil.appendableStack(e));
			}
		}
		
		if(ordResp.getJobTicketID() != null && !ordResp.getJobTicketID().isEmpty())
		{
			try
			{
				rlJobTicketNo = Long.valueOf(ordResp.getJobTicketID());
			}
			catch (NumberFormatException e)
			{
				_logger.error("could not create Long from jobTicketId " + ordResp.getJobTicketID() + LogUtil.appendableStack(e));
				rlJobTicketNo = null;
			}
		}

		if (rlJobTicketNo != null)
		{
			JobTicket jobTicket = svcRlOrder.getJobTicket(ordLic.getExternalDetailId());			
			updateItem(jobTicket, coiItem);
			orderService.updateItem(orderConsumerContext, coiItem);
		}
		else
		{
			License license = svcRlOrder.getLicense(ordLic.getExternalDetailId());			
			updateItem(license, coiItem);
			orderService.updateItem(orderConsumerContext, coiItem);
		}
		//return getSuccessResult();
		return true;
	}

	protected void updateItem(JobTicket jobTicket, Item coiItem)
	{
		updateFees(jobTicket.getPriceData(), coiItem);
		updateItemParms(jobTicket.getOrderAttributes(), coiItem);

		coiItem.setRlDetailHtml(jobTicket.getRlDetailHTML());
		coiItem.setJobTicketNumber(jobTicket.getJobTicketID());
	}

	private void updateItem(License license, Item coiItem)
	{		
		updateFees(license.getPriceData(), coiItem);
		updateItemParms(license.getOrderAttributes(), coiItem);		    	

		coiItem.setInvoiceNumber(RLNK + String.valueOf(license.getInvoiceNumber()));
		Date date = new Date();
		coiItem.setInvoiceDate(date);
		coiItem.setBaseInvoiceDate(date);
		coiItem.setExternalDetailId(license.getLicenseNumber());
		coiItem.setItemAvailabilityCd(G);    		
		coiItem.setItemStatusCd(ItemStatusEnum.INVOICED); 
		if (coiItem.getPaymentId() != null) {
			coiItem.setItemStatusQualifier(ItemStatusQualifierEnum.PAID_CREDIT_CARD);
		} else {
			coiItem.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICED);			
		}
		coiItem.setOverrideAvailabilityCd(G);    		
		coiItem.setOverrideDtm(date);

		coiItem.setRlDetailHtml(license.getRlDetailHTML());

	}

	private void updateItemParms(Map<String, OrderAttribute> orderAttributes, Item coiItem)
	{
		String key = null;
		Map<String, ItemParm> itemParms = coiItem.getItemParms();
		ItemParm itemParm = null;
		for (Entry<String, OrderAttribute> attribute : orderAttributes.entrySet())
		{
			key = attribute.getKey();
			itemParm = itemParms.get(key);
			if (itemParm == null)
			{
				itemParm = new ItemParm();
				itemParm.setParmName(key);
				itemParm.setRlSourceBeanCd(ORDER_BEAN);
				itemParm.setDataTypeCd(STRING);
				itemParms.put(key, itemParm);
			}
			itemParm.setParmValue(attribute.getValue().getValue());
		}

	}

	private void updateFees(PriceData priceData, com.copyright.svc.order.api.data.Item coiItem)
	{
		coiItem.setTotalPrice(priceData.getTotalPrice());
		//coiItem.setDistributionPayable(priceData.getDistributionAmount());

		// get first itemFees element 
		ItemFees itemFees = coiItem.getAllFees().iterator().next();
		itemFees.setLicenseeFee(priceData.getLicenseeFee());
		itemFees.setRightsholderFee(priceData.getRightsHolderFee());
		itemFees.setDiscount(priceData.getDiscount());
		itemFees.setDistributionPayable(priceData.getDistributionAmount());
		itemFees.setHardCopyCost(priceData.getHardCopyCost());
		itemFees.setPriceAdjustment(priceData.getPriceAdjustment());
		itemFees.setShippingFee1(priceData.getShippingCost1());
		itemFees.setShippingFee2(priceData.getShippingCost2());
		itemFees.setShippingFee3(priceData.getShippingCost3());
		itemFees.setShippingFee4(priceData.getShippingCost4());
		itemFees.setShippingFee5(priceData.getShippingCost5());
		itemFees.setShippingFee6(priceData.getShippingCost6());
		itemFees.setTotalTax(priceData.getTaxAmount());

	}

}
