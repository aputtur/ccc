package com.copyright.ccc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CCForwardAction extends CCAction
{
    public static final String FORWARD_PATH = "forwardPath";
    
    public ActionForward defaultOperation(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                          throws Exception
    {
        return new ActionForward(FORWARD_PATH, mapping.getParameter(), true);
    }
}