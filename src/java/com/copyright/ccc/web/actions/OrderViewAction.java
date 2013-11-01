package com.copyright.ccc.web.actions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.actions.TilesAction;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.OrderViewActionForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.Organization;

public class OrderViewAction extends TilesAction {
	protected Logger _logger = Logger.getLogger(this.getClass());
	private long last4CC;
	private String CREDIT = "Credit Card";
	

	public OrderViewAction() {
	
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		OrderViewActionForm ovForm = WebUtils.castForm( OrderViewActionForm.class, form );
		ActionForward forward = mapping.findForward("continue");
		OrderLicenses licenses =null;
		OrderPurchase purchase=null;
		
		
		
				// This comes from Gen if there were edit exceptions
		@SuppressWarnings("unchecked")
		List<String> cascadeErrors = (List<String>) request.getAttribute(WebConstants.RequestKeys.ORDER_CASCADE_UPDATE_ERROR_STRINGS);
		if (cascadeErrors == null) {
			cascadeErrors = new ArrayList<String>();
		}

		ovForm.setCascadeErrors(cascadeErrors);

		// This comes from Gen if closed orders were trying to be edited in
		// cascade update
		@SuppressWarnings("unchecked")
		List<String> cascadeClosedErrors = (List<String>) request.getAttribute(WebConstants.RequestKeys.ORDER_CASCADE_UPDATE_CLOSED_STRINGS);
		if (cascadeClosedErrors == null) {
			cascadeClosedErrors = new ArrayList<String>();
		}
		ovForm.setCascadeClosedErrors(cascadeClosedErrors);

		ovForm.setCascadeCancelErrors(null);
		List<String> cascadeCancelErrors = (List<String>) UserContextService.getHttpSession().getAttribute(WebConstants.RequestKeys.ORDER_CASCADE_CANCEL_INVOICED_STRINGS);
		if (cascadeCancelErrors == null) {
			cascadeCancelErrors = new ArrayList<String>();
		}
		ovForm.setCascadeCancelErrors(cascadeCancelErrors);
		UserContextService.getHttpSession().removeAttribute(WebConstants.RequestKeys.ORDER_CASCADE_CANCEL_INVOICED_STRINGS);
		
		ovForm.setCascadeEditErrors(null);
		List<String> cascadeEditErrors = (List<String>) UserContextService.getHttpSession().getAttribute(WebConstants.RequestKeys.ORDER_CASCADE_EDIT_INVOICED_STRINGS);
		if (cascadeEditErrors == null) {
			cascadeEditErrors = new ArrayList<String>();
		}
		ovForm.setCascadeEditErrors(cascadeEditErrors);
		UserContextService.getHttpSession().removeAttribute(WebConstants.RequestKeys.ORDER_CASCADE_EDIT_INVOICED_STRINGS);
		
		
		if (ovForm.getLastId() != null && !ovForm.getLastId().equalsIgnoreCase(ovForm.getId())) {
		    ovForm.setSearchOption(null);
		    ovForm.setSearchText(null);
		}
		ovForm.setLastId(ovForm.getId());

		String sortOption = ovForm.getSortOption();
		if (sortOption == null || "".equals(sortOption)) {
			ovForm.setSortOption(String.valueOf(WebUtils.SELECT_ONE));
		}

		int searchOpt = DisplaySpecServices.NOFILTER;
		String searchId = ovForm.getSearchOption();
		String viewOrder = request.getParameter("viewOrder");
		/*
		 * this 'if' check (prevents entry to the else) was added to prevent us
		 * from forwarding to a "viewAll" action under the wrong conditions.
		 * This "viewOrder" parameter is only provided on the "View Order" links
		 * that are displayed under each order row on the View Orders tab on the
		 * Order History page. See jira item: CC-1912
		 */
		if (StringUtils.isNotEmpty(viewOrder)) {
			// avoid the next block (the else) because that is for detail
			// searches only
		} else {
			if (searchId != null && !"".equals(searchId)) {
				try {
					searchOpt = Integer.parseInt(searchId);
					/*if (DisplaySpecServices.NOFILTER == searchOpt) {
						forward = mapping.findForward("viewAll");
					}*/
					// else if (searchOpt == -1) {
					// Choose One was the search option so get the last sort
					// data
					// ovForm.useLastSearch();
					// }
				} catch (NumberFormatException nfe) {
					_logger.error("OrderViewAction.execute()", nfe);
					searchOpt = DisplaySpecServices.NOFILTER;
				}
			}
		}

		this.initUI(ovForm);
		ovForm.clearForm();
		// ovForm.getDisplaySpec(); // Set Purchase Display Spec in UserContext
		DisplaySpec spec = ovForm.getLicenseSpec(ovForm.getDisplaySpec());
		
		Long confirmationNumber = new Long(ovForm.getId());
		ovForm.setAcademic(false);
		ovForm.setOrderBundle(null);
		// OrderPurchaseServices services = new OrderPurchaseServices();
		if(ovForm.haveCriteria()){
		try {
			// OrderPurchases purchases =
			// OrderPurchaseServices.getOrderPurchases();
			// OrderPurchase purchase =
			// OrderPurchaseServices.getOrderPurchaseForConfirmationNumber(confirmationNumber);

			
			OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
//			OrderSearchCriteria orderSearchCriteria = OrderPurchaseServices.getOrderSearchCriteriaFromDisplaySpec(spec);
			orderSearchCriteria.setConfirmNumber(confirmationNumber);
			OrderSearchResult orderSearchResult = OrderPurchaseServices.searchOrderPurchases(orderSearchCriteria);
			
			
			OrderPurchases purchases = orderSearchResult.getOrderPurchasesObject();
			
			if(purchases==null || purchases.getOrderPurchaseList().isEmpty()){
				ovForm.setOrderDetails(new ArrayList<OrderLicense>());
				
			} else {
			  purchase = purchases.getOrderPurchaseList().get(0);
			  orderSearchCriteria = OrderLicenseServices.getOrderSearchCriteriaFromDisplaySpec(spec);
			  orderSearchCriteria.setConfirmNumber(confirmationNumber);
			  orderSearchCriteria.setSearchToRow(ItemConstants.MAX_CONFIRM_ROWS);
			  OrderSearchResult licenseSearchResult = OrderLicenseServices.searchOrderLicenses(orderSearchCriteria);
			  licenseSearchResult = OrderLicenseServices.getOriginalOrderLicensesForOrderSearchResult(licenseSearchResult);
			  
			  licenses = licenseSearchResult.getOrderLicensesObject();
//			 licenses =OrderLicenseServices.searchOrderLicenses(orderSearchCriteria).getOrderLicensesObject();
			  UserContextService.setOrderLicenses(licenses);
			  List<OrderLicense> licenseDetails = licenses.getDisplayLicenseList();
			  ovForm.setPayMethod(this.getPayMethod(licenseDetails));
			  ovForm.setOrderDetails(licenseDetails);
			
				if (purchase.getOrderBundles() != null && !purchase.getOrderBundles().isEmpty()) {
					ovForm.setAcademic(true);
					long purchaseId = licenses.getOrderLicenseList().get(0).getPurchaseId();
					OrderBundle orderBundle = licenseSearchResult.getOrderBundles().get(purchaseId);
					if (orderBundle != null) {
						ovForm.setOrderBundle(orderBundle);
					}
					else {
						ovForm.setOrderBundle(purchase.getOrderBundles().get(0));
					}

				}
				Date orderDate = purchase.getOrderDate();
				ovForm.setOrderDate(orderDate);

				
				User user =OrderPurchaseServices.getUserForPurchase(purchase);
				ovForm.setCustName(null);
				if (user != null) {
					String userName = user.getName();
					ovForm.setCustName(userName);

					InternetAddress emailAddress = user.getEmailAddress();
					String email = "";
					if (emailAddress != null) {
						email = emailAddress.getAddress();
					}
					ovForm.setCustEmail(email);

					String phone = user.getPhoneNumber();
					ovForm.setCustPhone(phone);

					ARAccount account = user.getAccount();
					Long acctNo = null;
					if (account != null) {
						acctNo = Long.valueOf(account.getAccountNumber());
					}
					if (acctNo != null) {
						ovForm.setAcctNumber(acctNo.longValue());
					}

					Organization org = user.getOrganization();
					String company = "";
					if (org != null) {
						company = org.getOrganizationName();
					}
					ovForm.setCustCompany(company);

					Location address = user.getBillingAddress();

					ovForm.setBillingAddress(account, org, address);

				}

				String poNum = purchase.getPoNumber();
				ovForm.setPoNumber(poNum);

				String promoCode = purchase.getPromoCode();
				ovForm.setPromoCode(promoCode);

				ovForm.setAcademic(purchase.isAcademic());

				boolean closed = purchase.isClosed();
				ovForm.setClosed(closed);

				boolean cancelable = purchase.isOpen();
				ovForm.setCancelable(cancelable);

				
				int academicOrderCount = 0;
				int nonAcademicOrderCount = 0;
				boolean hasPricedAcademicOrder = false;
				boolean hasPricedNonAcademicOrder = false;
				boolean hasTBDAcademicOrder = false;
				boolean hasTBDNonAcademicOrder = false;

		
				ovForm.setHasAcademicItems(Boolean.FALSE);
				ovForm.setHasAcademicSpecialOrders(Boolean.FALSE);
				ovForm.setHasNonAcademicItems(Boolean.FALSE);
				ovForm.setHasReprintOrders(Boolean.FALSE);
				ovForm.setHasNonReprintSpecialOrders(Boolean.FALSE);
				ovForm.setHasNonAcademicSpecialOrders(Boolean.FALSE);
				ovForm.setContainsTBDAcademicOrder(Boolean.FALSE);
				ovForm.setContainsTBDNonAcademicOrder(Boolean.FALSE);
				ovForm.setContainsTBDAcademicOrder(Boolean.FALSE);
				ovForm.setContainsTBDOrder(Boolean.FALSE);
				
				for (OrderLicense license : licenseDetails) {
					if (license.isAcademic()) {
						if (license.getPrice() == ItemConstants.COST_TBD) {
							hasTBDAcademicOrder = true;
						} else {
							hasPricedAcademicOrder = true;
						}
						academicOrderCount++;
						ovForm.setHasAcademicItems(Boolean.TRUE);
						if (license.isUnresolvedSpecialOrder()) {
							ovForm.setHasAcademicSpecialOrders(Boolean.TRUE);
						}
					}
					if (!license.isAcademic()) {
						if (license.getPrice() == ItemConstants.COST_TBD) {
							hasTBDNonAcademicOrder = true;
						} else {
							hasPricedNonAcademicOrder = true;
						}
						nonAcademicOrderCount++;
						ovForm.setHasNonAcademicItems(Boolean.TRUE);
						if (license.isUnresolvedSpecialOrder()) {
							if (!license.isReprint()) {
								ovForm.setHasNonReprintSpecialOrders(Boolean.TRUE);
							}
						}
						if (license.isReprint()) {
							ovForm.setHasReprintOrders(Boolean.TRUE);
						}
						ovForm.setHasNonAcademicSpecialOrders(Boolean.TRUE);
					}
					ovForm.setNumberOfAcademicOrders(academicOrderCount);
					ovForm.setNumberOfNonAcademicOrders(nonAcademicOrderCount);
					ovForm.setNumberOfOrderItems(licenseDetails.size());

					ovForm.setContainsTBDAcademicOrder(hasPricedAcademicOrder && hasTBDAcademicOrder);
					ovForm.setContainsTBDNonAcademicOrder(hasPricedNonAcademicOrder && hasTBDNonAcademicOrder);
					if (((!hasPricedAcademicOrder && hasTBDAcademicOrder) || 
							(hasPricedAcademicOrder && hasTBDAcademicOrder)) || 
							((!hasPricedNonAcademicOrder && hasTBDNonAcademicOrder) || 
									(hasPricedNonAcademicOrder && hasTBDNonAcademicOrder))) {
						ovForm.setContainsTBDOrder(true);
					}

				}

				ovForm.setCourseTotal(OrderLicenseServices.getOrderTotalStringForLicenses(licenseDetails, ItemConstants.TOTAL_COURSE));
				ovForm.setSingleItemTotal(OrderLicenseServices.getOrderTotalStringForLicenses(licenseDetails, ItemConstants.TOTAL_SINGLE));
				ovForm.setOrderTotal(OrderLicenseServices.getOrderTotalStringForLicenses(licenseDetails, ItemConstants.TOTAL_ORDER));

				ovForm.setShowHeaderSection(Boolean.FALSE);
				if (ovForm.getHasAcademicItems() && ovForm.getHasNonAcademicItems()) {
					ovForm.setShowHeaderSection(Boolean.TRUE);
					_logger.info("Show Header");
				}

			}

		} catch (OrderPurchasesException ope) {
			_logger.error("OrderHistoryAction.getPurchases(): ", ope);
		} catch (Exception exc) {
			_logger.error("OrderHistoryAction.getPurchases(): ", exc);
		}
		}
		return forward;
	}

	/*
	 * The Purchase object doesn't hold the payment type so I have to find a
	 * detail to get that information. Note: This is cruel and unnatural
	 * punishment. There really should be a getter for this on the Purchase.
	 */
	private String getPayMethod(List<OrderLicense> details) {
		String payMethod = OrderViewActionForm.INVOICE; // "Invoice"
		if (details != null) {
			Iterator<OrderLicense> itr = details.iterator();
			String trxId = null;
			boolean foundSO = false;
			boolean foundCC = false;
			boolean loop = itr.hasNext();
			while (loop) {
				OrderLicense license = itr.next();
				String paymentMethod = license.getPaymentMethodCd();

				boolean specialOrder = license.isSpecialOrder();
				if (specialOrder) {
					foundSO = true;
				}
				trxId = license.getTransactionId();
				// if (trxId != null && !"".equals(trxId)) {
				// foundCC = true;
				// last4CC = license.getLastFourCreditCard();
				// payMethod = OrderViewActionForm.CREDIT_CARD + last4CC;
				// //"Credit Card ending in "
				// }
				// Updated for bug 1446
				if (ItemConstants.PAYMENT_METHOD_CREDIT_CARD.equals(paymentMethod)) {
					foundCC = true;
					last4CC = license.getLastFourCreditCard();
					
					if (last4CC > 0) {
						DecimalFormat myFormat = new DecimalFormat("0000");// format to retain leading zero's
						payMethod = OrderViewActionForm.CREDIT_CARD + myFormat.format(last4CC); // "Credit Card ending in "
					}
					else
					{
						payMethod = CREDIT;
					}
				}

				if (foundSO && foundCC) {
					loop = false;
				} else {
					if (!itr.hasNext()) {
						loop = false;
					}
				}
			}

		
		}

		return payMethod;
	}

	/*
	 * Internal helper for initializing the struts tag structures
	 */
	private void initUI(OrderViewActionForm form) {
		form.setSearchOptionList(WebUtils.getOVSearchOptions());
		form.setSortOptionList(WebUtils.getOVSortOptions());
		form.setPermissionTypeList(WebUtils.getODPermTypeOptions());
		form.setPermissionStatusList(WebUtils.getODPermStatusOptions());
		form.setBillingStatusList(WebUtils.getODBillStatusOptions());
		form.setIsAlwaysInvoice(UserContextService.getActiveAppUser().isAlwaysInvoice());
		
		try {
			boolean empty = CartServices.isCartEmpty();
			form.setCartEmpty(empty);
			boolean cartAcademic = CartServices.isAcademicCart();
			form.setCartAcademic(cartAcademic);
		} catch (Exception e) {
			// TODO: Ask Lucas why this happens with a CCC Version Mismatch
			_logger.error("OrderViewAction Exception", e);
		}
	}

	/*
	 * Determine if the selected search option value is valid. All selections
	 * are valid except "Choose One".
	 * 
	 * @return false if the user has selected a search option value of Choose
	 * One. Otherwise return true. All free form search options return true.
	 */
	private boolean searchValid(HttpServletRequest req, OrderViewActionForm form) {
		boolean searchValid = false;

		int filterBy = form.getSearchSwitch();

		switch (filterBy) {
		case DisplaySpecServices.PERMISSIONTYPEFILTER: // 10 = Permission Type
			String status = form.getPermissionType();
			if (!"-1".equals(status)) {
				searchValid = true;
			}
			break;
		case DisplaySpecServices.PERMISSIONSTATUSFILTER: // 11 = Permission
			// Status
			status = form.getPermissionStatus();
			if (!"-1".equals(status)) {
				searchValid = true;
			}
			break;
		case DisplaySpecServices.BILLINGSTATUSFILTER: // 12 = Billing Status
			status = form.getBillingStatus();
			if (!"".equals(status)) {
				searchValid = true;
			}
			break;
		case -1:
			if (form.getSearch() != 1) {
				// orderView action returns detail list
				searchValid = true;
			} else {
				// Manually surface an error message to the user
				ActionMessages errors = new ActionMessages();
				ActionMessage error = new ActionMessage("ov.error.select.search");
				errors.add("search", error);
				this.saveErrors(req, errors);
			}
			break;
		default:
			searchValid = true;
			break;
		}

		return searchValid;
	}
}
