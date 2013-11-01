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
import com.copyright.ccc.web.forms.LoginForm;


public class LoginAction extends CCAction
{
    private static final Logger _logger = Logger.getLogger(LoginAction.class);
    
    private static final String RETRY_PATH = "/loginRetry";
    private static final String FIRST_TIME_PATH = "/login";

    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {

        LoginForm loginForm = castForm( LoginForm.class, form );

        ActionMessages errors = new ActionMessages();
        
        String actionPath = mapping.getPath();

        if ( actionPath.equals( RETRY_PATH ) )
        {
            ActionMessage loginError = new ActionMessage( "security.login.loginFailed" );
        	errors.add( "general", loginError );
            
            // prevent infinite loop when auto-login is enabled
            loginForm.setAutoLoginForward( null );
        }
        
        //TODO needed call to RL service to determine if RL registration is incomplete
        /*
         * 
         *  //check RL login status service to see if this is a RL user and if the registration is not completed yet
         if (RLService.registrationIsCompleted) {
            ActionMessage loginError = new ActionMessage( "security.login.RLRegistrationInComplete" );
            errors.add( "general", loginError );
            
            // prevent infinite loop when auto-login is enabled
            loginForm.setAutoLoginForward( null ):
         }
         */
        
        Throwable loginException = null;
        
        if ( ( loginException = ( Throwable ) request.getAttribute( WebConstants.RequestKeys.LOGIN_EXCEPTION ) ) != null )
        {
            errors.add( "general", new ActionMessage( loginException.getMessage(), false ) );
        }

        saveErrors( request, errors );

        return mapping.findForward( SHOW_MAIN );
    }
}
