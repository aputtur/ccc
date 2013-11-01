package com.copyright.ccc.web.actions;

import com.copyright.ccc.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.forms.OrderViewActionForm;

public class ProcessOrderViewAction extends OrderViewAction
{
    public ProcessOrderViewAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
       OrderViewActionForm ovForm = WebUtils.castForm( OrderViewActionForm.class, form );
       ovForm.setSearch(1); // A user search was executed.

       ActionForward forward = super.execute(mapping, form, request, response);
//       ovForm.setSearchOption(null);
       return forward;
    }
}
