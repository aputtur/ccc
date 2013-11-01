package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;

public class LoginFailureAction extends CCAction {
    private static final Logger _logger = Logger.getLogger(LoginAction.class);
    
    private static final String RETRY_PATH = "/loginFailure";
    private static final String FIRST_TIME_PATH = "/login";

    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {

        ActionMessages errors = new ActionMessages();
        if ( ( ( Throwable ) request.getAttribute( WebConstants.RequestKeys.LOGIN_EXCEPTION ) ) != null )
        {
            errors.add( "general", new ActionMessage( "errors.accountOnHold") );
        }
        
        if ( ( ( Throwable ) request.getAttribute( WebConstants.RequestKeys.RIGHTSLINK_INCOMPLETE_EXCEPTION ) ) != null )
        {
            errors.add( "general", new ActionMessage( "security.login.RLRegistrationInComplete") );
        }


        saveErrors( request, errors );

        return mapping.findForward( SHOW_MAIN );
    }
}
