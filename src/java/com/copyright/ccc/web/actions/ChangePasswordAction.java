package com.copyright.ccc.web.actions;

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.ChangePasswordActionForm;
import com.copyright.mail.MailSendFailedException;
import com.copyright.opi.data.ModState;
import com.copyright.service.comm.CommunicationsServiceAPI;
import com.copyright.service.comm.CommunicationsServiceFactory;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.ldapuser.api.data.UserNameNotFoundException;
import com.copyright.svc.ldapuser.api.data.PasswordPolicyException;
import com.copyright.workbench.util.StringUtils2;

/*  CHANGELOG
 *  When        Who     What
 *  ----------  ---     -------------------------------------
 *  2013-02-04  MSJ     Cleaned up error handling a little.
 *                      Changed error message.
 */
public class ChangePasswordAction extends CCAction
{
    private static final String FAILURE = "failure";
    private static final String SUCCESS = "success";
    private static final String INVALID_PASSWORD = 
    "Your password must be at least 6 characters and should not include your email address.";
    private static final String PASSWORDS_DONT_MATCH = "Passwords do not match.";

    private static Logger _logger = Logger.getLogger(ChangePasswordAction.class);

    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
    	//check if this is a temporary password set by an emulating user

        String enforcePwdChg = request.getParameter(WebConstants.ENFORCE_PWD_CHG);

    		if(!StringUtils2.isNullOrEmpty(enforcePwdChg))
    		{
    			UserContextService.getAppUser().setEnforcePwdChg(Boolean.valueOf(enforcePwdChg));
    			UserServices.updatePasswordEnforcement(UserContextService
    				.getAppUser());
    		}
    		
        ChangePasswordActionForm actionForm = castForm( ChangePasswordActionForm.class, form );
        boolean emulatedUserLoggedIn = false;

        User currentUser = UserContextService.getActiveSharedUser();
        
        String password = actionForm.getPassword();
        String passwordConfirmation = actionForm.getPasswordConfirmation();
        
        ActionMessages ae = new ActionMessages();
        
        LdapUserService ldapService =ServiceLocator.getLdapUserService();
        
        if(StringUtils2.isNullOrEmpty(password))
        	return mapping.findForward( FAILURE );       
                                
        if ( StringUtils.isNotEmpty(password ) && StringUtils.isNotEmpty(passwordConfirmation) 
             && !password.equals( passwordConfirmation ) )
        { 
            ae.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( PASSWORDS_DONT_MATCH, false ) );
            addErrors( request, ae );
            return mapping.findForward( FAILURE );
        }
        
        LdapUser ldUser = null; 
        String userName = currentUser.getUsername() ;
        
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
            ae.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.personNotFound", userName ) );
            addErrors( request, ae );
            return mapping.findForward( FAILURE );
        }
        
        try
        {
            ldapService.updatePassword( new LdapUserConsumerContext(), currentUser.getUsername(), password );
            CCUser ccUser = UserContextService.getAuthenticatedAppUser();

            // check to enforce user to change password

            if (ccUser.getEnforcePwdChg())
            {
                emulatedUserLoggedIn = ccUser.getEnforcePwdChg();
                ccUser.setEnforcePwdChg( Boolean.FALSE );
                ccUser.setModState( ModState.DIRTY );
                UserServices.updatePasswordEnforcement( ccUser );
            }
        }
        catch(PasswordPolicyException ppe)
        {
            //  2013-01-18  MSJ
            //  Use the invalid password message instead of dumping the error passed
            //  back from the service.  Added trap for PPE, specifically.
            
            ae.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( INVALID_PASSWORD, false ) );
            addErrors( request, ae );
            return mapping.findForward( FAILURE );
        }
        catch ( Exception e )
        {
            ae.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( e.getMessage(), false ) );
            addErrors( request, ae );
            return mapping.findForward( FAILURE );
        }                          
      
        actionForm.setPassword( "" );
        actionForm.setPasswordConfirmation( "" );

        if (ldUser != null) {
        	sendSetPasswordEmail( ldUser, request );
        }
        if(emulatedUserLoggedIn)
        {
        	LinkedList<String> navHistoryList = new LinkedList<String>(UserContextService.getNavigationHistory());

        	if(!navHistoryList.isEmpty())
            {
            	while(navHistoryList.getFirst().contains( mapping.getPath()) || navHistoryList.getFirst().contains( "/error404.do" ) )
                {
                	navHistoryList.removeFirst();
                }
            }
            UserContextService.getUserContext().setNavigationHistory( navHistoryList );
            ActionForward af = new ActionForward( navHistoryList.getFirst(), Boolean.TRUE );

            return af;
        }
        return mapping.findForward( SUCCESS );
    }

    public ActionForward prepUpdatePassword(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response ) {
    	
    	return mapping.findForward(FAILURE);

    }

    private void sendSetPasswordEmail(LdapUser user, HttpServletRequest request ) 
    {
        
        String userDisplayName = user.getFirstName() + " " + user.getLastName();
                                    
        CommunicationsServiceAPI commService = CommunicationsServiceFactory.getInstance().getService();
        ActionMessages ae = new ActionMessages();
        java.util.Map<String, String> updateMap = new java.util.HashMap<String, String>();
        updateMap.put( "Password", "" );

        try
        {
           commService.sendUpdateAccountEmail( "copyright.com", userDisplayName, user.getUsername(), updateMap );
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
