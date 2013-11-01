package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.ChangePasswordActionForm;
import com.copyright.mail.MailSendFailedException;
import com.copyright.service.comm.CommunicationsServiceAPI;
import com.copyright.service.comm.CommunicationsServiceFactory;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.InvalidPasswordCodeException;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.ldapuser.api.data.PasswordPolicyException;
import com.copyright.svc.ldapuser.api.data.UserNameNotFoundException;

public class ResetPasswordAction extends CCAction
{
    private static final String INVALID_PASSWORD = 
        "Your password must be at least 6 characters and should not include your email address.";

    private static final String PASSWORDS_DONT_MATCH = "Passwords do not match.";

	String responseCode = null;
	
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {

        responseCode = request.getParameter("responseCode");
        
        ChangePasswordActionForm actionForm = castForm( ChangePasswordActionForm.class, form );
  
        actionForm.setPassword("");
        actionForm.setPasswordConfirmation("");
                          
        return mapping.findForward("prompt");
              
 }
 
    public ActionForward handleReset(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
       
        ChangePasswordActionForm actionForm = castForm( ChangePasswordActionForm.class, form );
        String password = actionForm.getPassword();
        String passwordConfirmation = actionForm.getPasswordConfirmation();
        
        //String responseCode = request.getParameter("responseCode");
        
        ActionMessages ae = new ActionMessages();

        if (password == null || password.length() == 0) {
            ae.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( INVALID_PASSWORD, false ) );
            addErrors( request, ae );
            return mapping.findForward( "prompt" );
        }
               
        if ( ! password.equals( passwordConfirmation ) )
        { 
            ae.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( PASSWORDS_DONT_MATCH, false ) );
            addErrors( request, ae );
            return mapping.findForward( "retry" );
        }

        if (password.length() < 6 || passwordConfirmation.length() < 6)
        { 
            ae.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( INVALID_PASSWORD, false ) );
            addErrors( request, ae );
            return mapping.findForward( "prompt" );
        }
                        
        ActionMessages aeMsg = new ActionMessages();
        
        LdapUserService ldapService =ServiceLocator.getLdapUserService();
        String respCode = responseCode;
        String userName = null;
        try
        {
        	userName = ldapService.getUsernameByForgottenPasswordCode(new LdapUserConsumerContext(), respCode);
        }
        catch(InvalidPasswordCodeException ipce)
        {
        	aeMsg.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ipce.getMessage(), false ) );
            addErrors( request, aeMsg );
            return mapping.findForward( "retry" );
        }
        
        LdapUser ldUser = null;               
        try
        {
        	ldUser = ldapService.getUser(new LdapUserConsumerContext(), userName);

            if ( ldUser != null ) {
                request.setAttribute(WebConstants.RequestKeys.EMAIL, ldUser.getUsername());
                //ldapService.            
            }
        }
        catch ( UserNameNotFoundException e )
        {
            ae.add(
                ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.personNotFound", userName ) );
            
            String forwardAction = "failure";            
            addErrors( request, ae );
            return mapping.findForward( forwardAction );
        }
        
        try
        {
        	ldapService.resetPasswordByForgottenPasswordCode(
                new LdapUserConsumerContext(), respCode, password );
        }
        catch(PasswordPolicyException ppe)
        {
            //  2013-01-18  MSJ
            //  Use the invalid password message instead of dumping the error passed
            //  back from the service.
            
        	aeMsg.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( INVALID_PASSWORD, false ) );
            addErrors( request, aeMsg );
            return mapping.findForward( "retry" );
        }
        catch(InvalidPasswordCodeException ipce)
        {
        	aeMsg.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ipce.getMessage(), false ) );
            addErrors( request, aeMsg );
            return mapping.findForward( "retry" );
        }

        if (ldUser != null) {
        	sendSetPasswordEmail(ldUser, request );
        }
        return mapping.findForward( "success" );
    }
    
    private void sendSetPasswordEmail(LdapUser user, HttpServletRequest request ) 
    {
        
        String userDisplayName = user.getFirstName() + " " + user.getLastName();
                                    
        CommunicationsServiceAPI commService = CommunicationsServiceFactory.getInstance().getService();
        ActionMessages ae = new ActionMessages();
        java.util.Map<String, String> updateMap = new java.util.HashMap<String, String>();
        updateMap.put("Password", "");

        try
        {
           commService.sendUpdateAccountEmail("copyright.com", userDisplayName, user.getUsername(), updateMap);
        }
        catch ( MailSendFailedException esfe )
        {

         ae.add(
            ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.personNotFound", user.getUsername() ) );
         
        _logger.error( ExceptionUtils.getFullStackTrace( esfe ) );
        addErrors( request,  ae );
       } 

  }
    
}
