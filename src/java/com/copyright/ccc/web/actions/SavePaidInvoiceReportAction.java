package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.util.PaidInvoiceReportUtil;
import com.copyright.ccc.web.util.ReportUtil;

public class SavePaidInvoiceReportAction extends SaveDateReportAction
{

	/* (non-Javadoc)
	 * @see com.copyright.ccc.web.actions.SaveDateReportAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		form = (ActionForm)request.getAttribute("viewPaidInvoiceReportForm");
		return super.execute(mapping, form, request, response);
	}
	
	@Override
	protected ReportUtil getReportUtil()
    {
    	return new PaidInvoiceReportUtil();
    }

}
