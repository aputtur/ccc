package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.RLOrders;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.order.RLOrderServices;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.RLOrderHistoryForm;

public class RLOrderHistoryAction extends CCAction
{
	private static Logger _logger = Logger.getLogger( RLOrderServices.class );
	private static final String SUCCESS = "success";

	public RLOrderHistoryAction() {
		super();
	}

	public ActionForward defaultOperation( ActionMapping       mapping
	                                     , ActionForm          form
										 , HttpServletRequest  request
										 , HttpServletResponse response )
	{
		//	Our default action is to "show" our rightslink
		//	order history page.

		return show(mapping, form, request, response);
	}

	public ActionForward show( ActionMapping       mapping
	                         , ActionForm          form
							 , HttpServletRequest  request
							 , HttpServletResponse response )
	{
		RLOrders newOrders;
		ActionMessages errors = new ActionMessages();
		ActionMessage serviceError = new ActionMessage("service.rightslink.error");
		//boolean formIsEmpty = true;

		//	First see what we have for a form.  Try from our
		//	session first, if not, then our passed-in action
		//	form.

		RLOrderHistoryForm rlohf = castForm( RLOrderHistoryForm.class, form );

		// Based on our form variables, we want to call our
		// service, load our data, then set paging and what-not.

//		formIsEmpty = rlohf.isEmpty();

		if (rlohf.hasChanged()) {
			// Instead of leaving these as directly
			// set values, we keep them separate from
			// the actual RLOrders object because we need
			// them to actually calculate what orders we
			// want to return.

			int newState = rlohf.getReqState();
			int newDirection = rlohf.getReqDirection();
			int newSortBy = rlohf.getReqSortBy();

			// We default to a page size of 25.  Also, the
			// "currentPage" is set already in the form and
			// that is fine because it is sort of a soft
			// number that is used to calculate what items
			// are returned on the fly.

			int newPageSize = 25;

			try {
				newOrders =
					RLOrderServices.getOrdersWith(newState, newSortBy, newDirection);

				newOrders.setPageSize(newPageSize);
				rlohf.setOrders(newOrders);
				rlohf.setRlServiceUp(true);
			}
			catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, serviceError);
				saveErrors(request, errors);
				_logger.error(e.getMessage());
				rlohf.setRlServiceUp(false);
			}
		}
		else {
			try {
				newOrders = RLOrderServices.getOrdersWith(DisplaySpecServices.RLCOMPLETEDSTATE, DisplaySpecServices.RLORDERDATESORT, DisplaySpecServices.SORTDESCENDING);
				newOrders.setPageSize(25);
				rlohf.setOrders(newOrders);
				rlohf.setRlServiceUp(true);
			}
			catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, serviceError);
				saveErrors(request, errors);
				_logger.error(e.getMessage());
				rlohf.setRlServiceUp(false);
			}				
		}
		putForm(request, rlohf);
		return mapping.findForward(SUCCESS);
	}
	private void putForm(HttpServletRequest request, ActionForm form) {
		request.getSession().setAttribute(WebConstants.SessionKeys.RIGHTSLINK_OH_FORM, form);
	}
}