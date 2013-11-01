package com.copyright.ccc.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdminHomeAction extends AdminAction
{
    public ActionForward defaultOperation( ActionMapping mapping, 
                                                   ActionForm form, 
                                                   HttpServletRequest request, 
                                                   HttpServletResponse response)
    {
        return mapping.findForward("showMain");
    }

    public ActionForward stop( ActionMapping mapping, 
                                                   ActionForm form, 
                                                   HttpServletRequest request, 
                                                   HttpServletResponse response)
    {
        return mapping.findForward("showMain");
    }

}
