package com.copyright.ccc.web.actions;

import com.copyright.ccc.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.forms.ViewReportsActionForm;
import com.copyright.ccc.web.util.ReportUtil;

public class ShowReportAction extends Action 
{
    public ShowReportAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        response.setHeader("Cache-Control","store, no-cache"); 
        response.setHeader("Pragma","no-cache"); 
        
        ActionForward forward = mapping.findForward("continue");
        ViewReportsActionForm viewForm = WebUtils.castForm( ViewReportsActionForm.class, form );
        
        /*ReportUtil reportUtil = new ReportUtil();
        reportUtil.getHTMLReport(viewForm);*/

        getReportUtil().getHTMLReport(viewForm);
        
        if(null == request.getAttribute("viewReportsForm"))
        {
        	ViewReportsActionForm viewReportsForm = new ViewReportsActionForm();
        	viewReportsForm.setReportData(viewForm.getReportData());
        	request.setAttribute("viewReportsForm", viewReportsForm);
        }
        
        
        return forward;
    }
    
    protected ReportUtil getReportUtil()
    {
    	return new ReportUtil();
    }
}
