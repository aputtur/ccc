package com.copyright.ccc.web.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.RedirectDescriptor;
import com.copyright.ccc.web.util.WebUtils;

public class RedirectAction extends CCAction
{
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response ) throws ServletException
    {
        String target = WebUtils.stripCRLF(request.getParameter("target"));
        
        //  2009-04-14  MSJ
        //  Added this code to help protect the redirect process.
        //  I wrote and tested this during the company-wide meeting
        //  so it isn't exactly well thought out.  Forgive.
        
        RedirectDescriptor redirector = 
            new RedirectDescriptor(target);
            
        try {
            response.sendRedirect(redirector.getURL());
        } catch (IOException e) {
            throw new ServletException(e);
        }
        return null;
    }
}
