package com.copyright.ccc.web.actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.OrderDetailActionForm;
import com.copyright.ccc.web.forms.OrderHistoryActionForm;
import com.copyright.ccc.web.tags.PageScroller;
import com.copyright.ccc.web.util.OHSearchSpec;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.ldapuser.api.data.UserNameNotFoundException;
import com.copyright.svc.order.api.data.ItemStatusEnum;

import java.util.regex.*;

public class OrderDetailAction extends DispatchAction {
	protected Logger _logger = Logger.getLogger(this.getClass());

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		OrderDetailActionForm odForm = WebUtils.castForm( OrderDetailActionForm.class, form );
		if (odForm.isSearchNotSort()) {
			odForm.setCurrentPage(1);
		}
		
		if (request.getParameter("filter") != null)
        {
        	if (request.getParameter("filter").compareTo("custResponseOrders")==0)
        	{
        		//odForm.setBillingStatus(BillingStatusEnum.AWAITING_LCN_CONFIRM.name()) ;
        		odForm.setSpecialOrderUpdate(ItemStatusEnum.AWAITING_LCN_CONFIRM.name());
        		odForm.setPermissionType("1");
        		odForm.setCurrentPage(1);
        		odForm.setSearchNotSort(true);
        		odForm.setSearchPage(true);
        		odForm.setSearchOption(String.valueOf(DisplaySpecServices.SPECIALORDERFILTER));
        		        		
        		HttpSession session = request.getSession();
                OHSearchSpec searchSpec = new OHSearchSpec(odForm.getDisplaySpec());
                searchSpec.setSearchOption(String.valueOf(DisplaySpecServices.SPECIALORDERFILTER));
                searchSpec.setSelectOption(ItemStatusEnum.AWAITING_LCN_CONFIRM.name());
                session.setAttribute(WebConstants.SessionKeys.DETAIL_SEARCH_SPEC, searchSpec);
        	}
        }
		
		if (request.getParameter("licenseId") != null)
        {
        	String licenseId = request.getParameter("licenseId");
        	
        		odForm.setSearchText(licenseId);
        		odForm.setPermissionType("1");
        		odForm.setCurrentPage(1);
        		odForm.setSearchNotSort(true);
        		odForm.setSearchPage(true);
        		odForm.setSearchOption(String.valueOf(DisplaySpecServices.ORDERDETAILIDFILTER));
        		        		
        		HttpSession session = request.getSession();
                OHSearchSpec searchSpec = new OHSearchSpec(odForm.getDisplaySpec());
                session.setAttribute(WebConstants.SessionKeys.DETAIL_SEARCH_SPEC, searchSpec);
        	
        }

		this.getOrderDetails(odForm);
		if (odForm.hasErrors()) {
			this.saveErrors(request, odForm.getErrors());
		}

		this.initUI(odForm);

		return mapping.findForward("continue");
	}
		
	/*
	 * Internal helper for initializing the struts tag structures
	 */
	protected void initUI(OrderDetailActionForm form) {
		form.setSearchOptionList(WebUtils.getODSearchOptions());
		String searchOpt = form.getSearchOption();
		String noFilter = String.valueOf(DisplaySpecServices.NOFILTER);
		if (noFilter.equals(searchOpt)) {
			String pubTitleFilter = String
					.valueOf(DisplaySpecServices.PUBLICATIONTITLEFILTER);
			form.setSearchOption(pubTitleFilter);
		}
		form.setSortOptionList(WebUtils.getODSortOptions());
		form.setPermissionTypeList(WebUtils.getODPermTypeOptions());
		form.setPermissionStatusList(WebUtils.getODPermStatusOptions());
		form.setBillingStatusList(WebUtils.getODBillStatusOptions());
		form.setSpecialOrderUpdateList(WebUtils.getODSpecialOrderUpdateOptions());

		form.setIsAdjustmentUser(UserContextService
				.hasPrivilege(CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT));
		
		form.setIsAlwaysInvoice(UserContextService.getActiveAppUser().isAlwaysInvoice());

		boolean empty = CartServices.isCartEmpty();
		form.setCartEmpty(empty);
		boolean cartAcademic = CartServices.isAcademicCart();
		form.setCartAcademic(cartAcademic);

		// Check if we should display RL Orders tab
		User currentUser = UserContextService.getActiveSharedUser();

		LdapUserService ldapService = ServiceLocator.getLdapUserService();

		LdapUser ldUser = null;
		String userName = currentUser.getUsername();

		try {
			ldUser = ldapService.getUser(new LdapUserConsumerContext(),
					userName);
			if (ldUser.getRightsLinkPartyID() == null
					|| ldUser.getRightsLinkPartyID() == "") {
				// looks like this is not a RL User show indicate to hide RL
				// Orders tab
				form.setRlUser(false);
			} else {
				form.setRlUser(true);
			}

		} catch (UserNameNotFoundException e) {
			_logger.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	private void getOrderDetails(OrderDetailActionForm form) {
		// For initial page load return nothing. For all subsequent page loads
		// search = "1" from hidden form value so return according to
		// DisplaySpec

		if (!form.isSearchPage()) {
			return;
		}

		DisplaySpec spec = form.getDisplaySpec();

		try {
			OrderLicenses licenses;
			OrderSearchResult orderSearchResult;
			LdapUser user;
			int totalItems = 0;

			if (this.sortValid(form)) {
				orderSearchResult = OrderLicenseServices
						.searchOrderDetailsForDisplaySpec();
				licenses = orderSearchResult.getOrderLicensesObject();
				// UserContextService.setOrderLicenses(licenses);

				user = OrderLicenseServices.getLdapUserData();
				// OrderSearchResult orderSearchResult =
				// OrderLicenseServices.getOrderDetails();
				// OrderBundle orderBundle =
				// OrderLicenseServices.getCourseDetails(orderSearchResult);

				// licenses = OrderLicenseServices.getOrderDetails();
				// form.setOrderBundle(orderBundle);
				// form.setCourseName(orderBundle.getCourseName());
				// form.setCourseNum(orderBundle.getCourseNumber());
				form.setLicenseList(licenses);
				form.setUser(user);
				form.setFirstName(user.getFirstName());
				form.setLastName(user.getLastName());
				form.setEmail(user.getUsername());
				form.setPhone(user.getMailToPhoneNumber());
				// form.setPayMethod(user.)
				totalItems = licenses.getTotalRowCount();
			} else {
				form.setLicenseList(new OrderLicenses());
			}

			form.setTotalItems(totalItems);

			if (totalItems > 0) {
				int totalPages = totalItems
						/ OrderHistoryActionForm.RESULTS_PER_PAGE;
				if (totalItems % OrderHistoryActionForm.RESULTS_PER_PAGE > 0) {
					totalPages += 1;
				}
				form.setTotalPages(totalPages);

				int dispRow = spec.getDisplayFromRow(); // 
				int dispPage = 1 + dispRow / PageScroller.RESULTS_PER_PAGE;

				int currPage = form.getCurrentPage();
				int startPage = form.getStartPage();

				if (dispPage != currPage && dispPage == 1) {
					// The order license service has reset the paging to page 1
					// for a resort
					currPage = 1;
					form.setCurrentPage(currPage); // reset the current page to
					// page 1
					startPage = 1; // reset the paging window to start at page 1
				}

				int nextBlockIndex = startPage
						+ PageScroller.VISIBLE_PAGE_LINKS;

				if (currPage >= nextBlockIndex) {
					startPage = nextBlockIndex;
					form.setStartPage(startPage);
				} else if (currPage < startPage) {
					startPage -= PageScroller.VISIBLE_PAGE_LINKS;
					form.setStartPage(startPage);
				}
			} else {
				form.setTotalPages(0);
			}
		} catch (OrderLicensesException olex) {
			form.setTotalPages(0);
			_logger.error("OrderDetailAction.getOrderDetails", olex);
		} catch (Exception exc) {
			form.setTotalPages(0);
			_logger.error(ExceptionUtils.getFullStackTrace(exc));
			_logger.error("OrderDetailAction.getOrderDetails", exc);
		}
	}

	private boolean isSpecialCharacter(String text) {
		for (int i = 0; i < text.length(); i++) {
			if (!Character.isLetterOrDigit(text.charAt(i)))
				return true;
		}
		return false;

	}

	protected boolean sortValid(OrderDetailActionForm form) {
		boolean sortValid = false;

		String text = null;
		int filterBy = form.getSearchSwitch();
		Pattern p = Pattern.compile("[,\\s]+");

		switch (filterBy) {
		// Dates
		case DisplaySpecServices.ORDERDATEFILTER:
		case DisplaySpecServices.REPUBLICATIONDATEFILTER:
			String begin = form.getBeginDate();
			String end = form.getEndDate();
			if (begin != null && !"".equals(begin) && end != null
					&& !"".equals(end)) {
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				try {
					format.parse(begin);
					format.parse(end);
					sortValid = true;
				} catch (ParseException pex) {
					sortValid = false; // Suppress the sort
				}
			}
			break;

		// Text input
		case DisplaySpecServices.PUBLICATIONTITLEFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text)) {
				sortValid = true;
			}
			break;
		case DisplaySpecServices.YOURREFERENCEFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text)) {
				sortValid = true;
			}
			break;
		case DisplaySpecServices.CONFNUMFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text) && !isSpecialCharacter(text)) {
				try {
					Long.parseLong(text);
					sortValid = true;
				} catch (Exception exc) {
					sortValid = false;
				}
			}
			break;
		case DisplaySpecServices.REPUBLICATIONTITLEFILTER:
		case DisplaySpecServices.REPUBLICATIONPUBLISHERFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text)) {
				sortValid = true;
			}
			break;

		case DisplaySpecServices.CHAPTERTITLEFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text)) {
				sortValid = true;
			}
			break;

		case DisplaySpecServices.ORDERDETAILIDFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text) && !isSpecialCharacter(text)) {
				try {
					Long.parseLong(text);
					sortValid = true;
				} catch (Exception exc) {
					sortValid = false;
				}
			}
			break;
		case DisplaySpecServices.INVOICENUMBERFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text)) {

				sortValid = true;

			}
			break;
		// text = form.getSearchText();
		// if (text != null && !"".equals(text)) {
		// try {
		// Integer.parseInt(text);
		// sortValid = true;
		// } catch (Exception exc) {
		// sortValid = false;
		// }
		// }
		// break;

		// Select box inputs
		case DisplaySpecServices.PERMISSIONTYPEFILTER:
			String type = form.getPermissionType();
			try {
				int typeInt = Integer.parseInt(type);
				if (typeInt > WebUtils.SELECT_ONE) {
					sortValid = true;
				}
			} catch (Exception e) {
				sortValid = false;
			}
			break;
		case DisplaySpecServices.PERMISSIONSTATUSFILTER:
			type = form.getPermissionStatus();
			try {
				int typeInt = Integer.parseInt(type);
				if (typeInt > WebUtils.SELECT_ONE) {
					sortValid = true;
				}
			} catch (Exception e) {
				sortValid = false;
			}
			break;
		case DisplaySpecServices.BILLINGSTATUSFILTER:
			type = form.getBillingStatus();
			if (type != null && !"".equals(type)) {
				sortValid = true;
			}
			break;
		case DisplaySpecServices.SPECIALORDERFILTER:
			type = form.getSpecialOrderUpdate();
			if (type != null && !"".equals(type)) {
				sortValid = true;
			}
			break;
		case DisplaySpecServices.ARTICLETITLEFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text)) {
				try {
					sortValid = true;
				} catch (Exception exc) {
					sortValid = false;
				}
			}
			break;
		case DisplaySpecServices.TYPEOFUSEFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text)) {
				sortValid = true;
			}
			break;
		case DisplaySpecServices.JOBTICKETNUMBERFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text) && !isSpecialCharacter(text)) {
				try {
					Long.parseLong(text);
					sortValid = true;
				} catch (Exception exc) {
					sortValid = false;
				}
			}
			break;
		case DisplaySpecServices.LICENSENUMBERFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text) && !isSpecialCharacter(text)) {
				try {
					Long.parseLong(text);
					sortValid = true;
				} catch (Exception exc) {
					sortValid = false;
				}
			}
			break;
		case DisplaySpecServices.YOURLINEITEMREFERENCEFILTER:
			text = form.getSearchText();
			if (text != null && !"".equals(text)) {
				sortValid = true;
			}
			break;
//			text = form.getSearchText();
//			if (text != null && !"".equals(text) && !isSpecialCharacter(text)) {
//				try {
//					Long.parseLong(text);
//					sortValid = true;
//				} catch (Exception exc) {
//					sortValid = false;
//				}
//			}
//			break;
		}

		return sortValid;
	}
}
