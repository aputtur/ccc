package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.LoginForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.ConsumingApp;
import com.copyright.data.comm.ForgotPasswordFormEmailBody;
import com.copyright.data.comm.FormEmail;
import com.copyright.mail.MailSendFailedException;
import com.copyright.service.comm.CommunicationsServiceAPI;
import com.copyright.service.comm.CommunicationsServiceFactory;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.ldapuser.api.data.UserNameNotFoundException;

public class ForgotPasswordAction extends CCAction
{
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        LoginForm actionForm = castForm( LoginForm.class, form );
        String providedUsername = WebUtils.escapeBrackets( WebUtils.stripCRLF( actionForm.getForgotPasswordUsername() ) );
        String forwardAction = "success";
        
        ForgotPasswordFormEmailBody body = new ForgotPasswordFormEmailBody();
                       
        ActionMessages ae = new ActionMessages();
        
        LdapUserService ldapService = null;
        ldapService =ServiceLocator.getLdapUserService(); 
                       
        String rspCode = "";
        try
        {
         rspCode = ldapService.generateForgottenPasswordCodeForUser(new LdapUserConsumerContext(), providedUsername) ;
        }
        catch(UserNameNotFoundException unnfe)
        {
        	ae.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.personNotFound", 
        																		providedUsername ) );
                forwardAction = "failure";            
                addErrors( request, ae );
                return mapping.findForward( forwardAction );
        	
        }
        String baseURL = this.getBaseURLFromRequest(request);
        String link = 
            baseURL + 
            WebConstants.RESET_PASSWORD_URL + 
            "?responseCode=" + rspCode;
         LdapUser ldUser = null;               
        try
        {
        	ldUser = ldapService.getUser(new LdapUserConsumerContext(), providedUsername);

            if ( ldUser != null ) {
                request.setAttribute(WebConstants.RequestKeys.EMAIL, ldUser.getUsername());
                //ldapService.            
            }
        }
        catch ( UserNameNotFoundException e )
        {
            ae.add(
                ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.personNotFound", providedUsername ) );
            
            forwardAction = "failure";            
            addErrors( request, ae );
            return mapping.findForward( forwardAction );
        } 
        
        String userDisplayName = "";
        String userEmail="";
        if (ldUser != null){
           userDisplayName = ldUser.getFirstName() + " " + ldUser.getLastName();
           userEmail=ldUser.getUsername();
        }
        
        body.setGreetingName( userDisplayName );

        body.setApplicationName( ConsumingApp.COPYRIGHT_DOT_COM.getDisplayName() );

        MessageResources messages = getResources( request );

        String forgotPasswordInstructions = messages.getMessage( "auth.pwd.forgot.instructions" );

        body.setInstructions( forgotPasswordInstructions );

        body.setDynamicLink( link );

        ///// Create and send the email. /////
        FormEmail formEmail = new FormEmail( body );

        //formEmail.setRecipient( user.getEmailAddress().getAddress() );
        formEmail.setRecipient( userEmail);

        CommunicationsServiceAPI commService = CommunicationsServiceFactory.getInstance().getService();

        try
        {
        commService.sendFormEmail( formEmail );
        }
        catch ( MailSendFailedException esfe )
        {

         ae.add(
            ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.personNotFound", providedUsername ) );
         
        _logger.error( ExceptionUtils.getFullStackTrace( esfe ) );
        forwardAction = "failure";
        addErrors( request,  ae );
        return mapping.findForward( forwardAction );
        }
        
        return mapping.findForward( forwardAction );
    }

    private String getBaseURLFromRequest(HttpServletRequest request) {
    	StringBuffer requestURL = request.getRequestURL();
        StringBuffer requestURLLower = new StringBuffer(requestURL.toString().toLowerCase());
        int lastPart;
        
        int firstPart = requestURLLower.indexOf("http://");
        if (firstPart > -1) {
            requestURLLower = requestURLLower.replace(0, firstPart + 7, "" );
        }
        lastPart = requestURLLower.indexOf("/cc2");
        if (lastPart > -1) {
            lastPart = lastPart + 4;
        } else {
            lastPart = requestURLLower.indexOf("/ccc");
            if (lastPart > -1) {
                lastPart = lastPart + 4;
            } else {
                lastPart = requestURLLower.indexOf("/");                            
            }
        }
        if (lastPart > -1) {
            requestURLLower = requestURLLower.replace(lastPart,requestURLLower.length(),"");
        }
              
        return "https://" + requestURLLower.toString();
    /*    StringBuffer requestURL = request.getRequestURL();
        StringBuffer requestURLLower = new StringBuffer(requestURL.toString().toLowerCase());
        int lastPart;
        String baseURL = "";
                                          
        int firstPart = requestURLLower.indexOf("http:");
        if (firstPart > -1) {
        	requestURLLower = requestURLLower.replace(0, firstPart + 7, "");
        	baseURL = "https://" ;
        	//return requestURLLower.toString();
        }
      /*  else
        {
        	return "https://" + requestURLLower.toString();
        } */
        
    /*    lastPart = requestURLLower.indexOf("/cc2");
        
        if (lastPart > -1) {
            lastPart = lastPart + 4;
        } else {
            lastPart = requestURLLower.indexOf("/ccc");
            if (lastPart > -1) {
                lastPart = lastPart + 4;
            } else {
                lastPart = requestURLLower.indexOf("/");                            
            }
        }
        if (lastPart > -1) {
            requestURLLower = requestURLLower.replace(lastPart,requestURLLower.length(),"");
        }
        
        return baseURL + requestURLLower.toString(); */
               
     }

}
