package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCAction;

public class TimeoutAction extends CCAction
{
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
//        ChangePasswordActionForm actionForm = castForm( ChangePasswordActionForm.class, form );
        if (UserContextService.isUserAuthenticated()) {
            return mapping.findForward( "success" );
        } else {
            return mapping.findForward( "showTimeoutView" );                   
        }
    }

}
