package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.OrderDetailActionForm;
import com.copyright.ccc.web.forms.OrderHistoryActionForm;
import com.copyright.ccc.web.tags.PageScroller;
import com.copyright.ccc.web.util.OHSearchSpec;
import com.copyright.ccc.web.util.WebUtils;

/**
 * OrderHistory struts action class for redisplaying the order history detail
 * tab when the user returns from the view order detail page. This class uses a
 * persistent object (OHSearchSpec) saved in the session to redisplay the order
 * history detail tab search result page.
 */
public class RefreshOrderDetailAction extends OrderDetailAction {
	public RefreshOrderDetailAction() {
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		OHSearchSpec savedSpec = (OHSearchSpec) session
				.getAttribute(WebConstants.SessionKeys.DETAIL_SEARCH_SPEC);
		String searchOpt = savedSpec.getSearchOption();

		OrderDetailActionForm odForm = WebUtils.castForm( OrderDetailActionForm.class, form );
		odForm.setDirection(savedSpec.getDirection());
		odForm.setSortOption(savedSpec.getSortOption());
		// Now get the saved displayFromRow and displayToRows so we can
		// save the user's paging value.
		int fromRow = savedSpec.getFromRow();
		int toRow = savedSpec.getToRow();

		OrderLicenses licenses = UserContextService.getOrderLicenses();
		// Set the start and end rows to the previous value from order history
		licenses.setReadStartRow(fromRow + 1); // add 1 for Keith's oracle index
												// translation
		licenses.setReadEndRow(toRow + 1); // add 1 for Keith's oracle index
											// translation

		try {
			int opt = Integer.parseInt(searchOpt);
			switch (opt) {
			case DisplaySpecServices.ORDERDATEFILTER:

			case DisplaySpecServices.REPUBLICATIONDATEFILTER:
				odForm.setSearchText(""); // Clear the free input field for date
											// searches
				odForm.setBeginDate(savedSpec.getBeginDate());
				odForm.setEndDate(savedSpec.getEndDate());
				break;
			case DisplaySpecServices.CONFNUMFILTER:
			case DisplaySpecServices.PUBLICATIONTITLEFILTER:

			case DisplaySpecServices.REPUBLICATIONTITLEFILTER:

			case DisplaySpecServices.INVOICENUMBERFILTER:

			case DisplaySpecServices.YOURREFERENCEFILTER:

			case DisplaySpecServices.ORDERDETAILIDFILTER:

			case DisplaySpecServices.REPUBLICATIONPUBLISHERFILTER:
				// Only set the input field for non date searches
				odForm.setSearchText(savedSpec.getSearchText());
				break;
			case DisplaySpecServices.PERMISSIONTYPEFILTER:
				odForm.setSearchText(""); // Clear the free input field for
											// permission type
				odForm.setSearchOption(savedSpec.getSearchOption());
				odForm.setPermissionType(savedSpec.getSelectOption());
				break;
			case DisplaySpecServices.PERMISSIONSTATUSFILTER:
				odForm.setSearchText(""); // Clear the free input field for
											// permission status
				odForm.setSearchOption(savedSpec.getSearchOption());
				odForm.setPermissionStatus(savedSpec.getSelectOption());
				break;
			case DisplaySpecServices.BILLINGSTATUSFILTER:
				odForm.setSearchText(""); // Clear the free input field for
											// billing status
				odForm.setSearchOption(savedSpec.getSearchOption());
				odForm.setBillingStatus(savedSpec.getSelectOption());
				break;
			case DisplaySpecServices.SPECIALORDERFILTER:
				odForm.setSearchText("");
				odForm.setSearchOption(savedSpec.getSearchOption());
				odForm.setSpecialOrderUpdate(savedSpec.getSelectOption());
			}
		} catch (Exception exc) {
			// if the search option is corrupt just set everything
			odForm.setSearchText(savedSpec.getSearchText());
			odForm.setBeginDate(savedSpec.getBeginDate());
			odForm.setEndDate(savedSpec.getEndDate());
		}

		odForm.setSearchPage(true);
		odForm.setSearchOption(searchOpt);
		odForm.setSortOption(savedSpec.getSortOption());

		this.getOrderDetails(odForm);
		if (odForm.hasErrors()) {
			this.saveErrors(request, odForm.getErrors());
		}

		super.initUI(odForm);

		return mapping.findForward("continue");

	}

	private void getOrderDetails(OrderDetailActionForm form) {
		DisplaySpec spec = form.getDisplaySpec();
		spec.resetLastDisplaySpecAttributes();

		try {
			OrderLicenses licenses;
			int totalItems = 0;

			if (super.sortValid(form)) {
				licenses = OrderLicenseServices
						.searchOrderDetailsForDisplaySpec()
						.getOrderLicensesObject();
				form.setLicenseList(licenses);
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
			_logger.error("RefreshOrderDetailAction.getOrderDetails", olex);
		} catch (Exception exc) {
			form.setTotalPages(0);
			_logger.error("RefreshOrderDetailAction.getOrderDetails", exc);
		}
	}
}
