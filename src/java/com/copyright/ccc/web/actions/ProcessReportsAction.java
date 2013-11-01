package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ProcessReportsAction extends Action 
{
    public ProcessReportsAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        ActionForward forward = mapping.findForward("number");
        String radio = request.getParameter("radio"); // default radio = "invNum"
        
        if ("orderDate".equals(radio)) {
           forward = mapping.findForward("orderDate"); 
        } else if ("invDate".equals(radio)) {
           forward = mapping.findForward("invoiceDate");
        } else if ("sotDate".equals(radio)) {
           forward = mapping.findForward("sotDate");
        }

        return forward;
    }
}
