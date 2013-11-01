package com.copyright.ccc.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.security.CCUserContext;
import com.copyright.ccc.business.security.SecurityUtils;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.email.services.EmailVerificationService;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.UpdateEmailAddressActionForm;
import com.copyright.mail.MailSendFailedException;
import com.copyright.persist.account.LDAPUser;
import com.copyright.service.comm.CommunicationsServiceAPI;
import com.copyright.service.comm.CommunicationsServiceFactory;
import com.copyright.svc.centralQueue.api.CentralQueueService;
import com.copyright.svc.centralQueue.api.data.CQConsumerContext;
import com.copyright.svc.centralQueue.api.data.Source;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.telesales.api.TelesalesService;
import com.copyright.svc.telesales.api.data.ContactPoint;
import com.copyright.svc.telesales.api.data.ContactPointPurposeEnum;
import com.copyright.svc.telesales.api.data.ContactPointTypeEnum;
import com.copyright.svc.telesales.api.data.Person;
import com.copyright.svc.telesales.api.data.TelesalesServiceConsumerContext;
import com.copyright.svc.userInfo.api.cyberSource.CyberSourceService;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo;
import com.copyright.svc.userInfo.api.data.UserInfoConsumerContext;
import com.copyright.workbench.net.SimpleIPAddress;
import com.copyright.workbench.util.StringUtils2;

/*  CHANGELOG
 *  When        Who     What
 *  ----------  ---     -------------------------------------
 *  2013-02-04  MSJ     Cleaned up error handling a little.
 */
public class UpdateEmailAddressAction extends CCAction
{
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    
    public ActionForward updateEmail( ActionMapping mapping
                                    , ActionForm form
                                    , HttpServletRequest request
                                    , HttpServletResponse response )
    {
        UpdateEmailAddressActionForm actionForm = 
            castForm( UpdateEmailAddressActionForm.class, form );

        User currentUser = UserContextService.getActiveSharedUser();
        String emailAddrStr = actionForm.getEmailAddress();
        String oldEmailAddrStr=currentUser.getUsername();
        TelesalesService telesalesService =ServiceLocator.getTelesalesService();
        LdapUserService ldapUserService = ServiceLocator.getLdapUserService();

        if ((emailAddrStr == null) || (emailAddrStr.isEmpty()))
        {
            return updateEmailPasswordError( 
                mapping, request, "Please provide New E-mail." );
        }

        if ((emailAddrStr != null) && (emailAddrStr != ""))
        {
            emailAddrStr = actionForm.getEmailAddress().trim();
            //LdapUser ldapUser = ldapUserService.getUser(new LdapUserConsumerContext(), emailAddrStr);
            if (currentUser.getLdapUser() != null)
            {
                String currentUserName = currentUser.getLdapUser().getUsername();  
                
                //get the latest from ldap to protect against a name change before LDAP update with first Time RL User login update (CC-3598)
                
                LdapUser ldapUser = 
                    ldapUserService.getUser( new LdapUserConsumerContext(), currentUserName);
                
                if (StringUtils.isBlank(currentUser.getLdapUser().getPartyID()) || StringUtils.isBlank(ldapUser.getPartyID()))
                {
                    return updateEmailPasswordError( 
                        mapping, request, "Unable to modify email address. Please try again later." );
                }
                if (currentUser.getLdapUser().getPartyID() != null && 
                    Long.valueOf( currentUser.getLdapUser().getPartyID() ) <= 0 ||
                    ldapUser.getPartyID() != null && Long.valueOf( ldapUser.getPartyID() ) <= 0)
                {
                    return updateEmailPasswordError( mapping, request, 
                        "Unable to modify email address. Please try again later." );
                }
            }
            emailAddrStr = actionForm.getEmailAddress().trim();

            String currentUserStr = currentUser.getUsername();
            String newUserStr = emailAddrStr;

            //check if the user name is valid and available

            int atIndex = emailAddrStr.indexOf( "@" );
            int dotIndex = emailAddrStr.indexOf( "." );

            if ((atIndex < 1) || (dotIndex < 1))
            {
                return updateEmailPasswordError( 
                    mapping, request, "The email address is not valid. Unable to modify email address." );
            }               
            if (!ldapUserService.isValidUserName( new LdapUserConsumerContext(), emailAddrStr )) 
            {
                return updateEmailPasswordError(
                    mapping, request, "The email address is not valid. Unable to modify email address." );            	
            }
            if (!ldapUserService.isUsernameAvailable( new LdapUserConsumerContext(), emailAddrStr ))
            {
                return updateEmailPasswordError(
                    mapping, request, "The email address <b>" + emailAddrStr + "</b> is not available." );            	
            }
            SimpleIPAddress ip = new SimpleIPAddress( SecurityUtils.determineClientIP( request ) );
            boolean internalIP = SecurityUtils.isInternalIP( ip ) || SecurityUtils.isLoopbackIP( ip );
            boolean isIntranetEmailCheckEnable = CC2Configuration.getInstance().isEmailVerifcationIntranetCheckEnabled();
            boolean isEmailVerificationEnabled = CC2Configuration.getInstance().isEmailVerifcationEnabled();

            if (internalIP)
            {
                if (isIntranetEmailCheckEnable)
                {
                    if (isEmailVerificationEnabled && 
                        !EmailVerificationService.verifyEmail( emailAddrStr ))
                    {
                        return emailError( mapping, request, "" );
                    }
                }
            }
            else
            {
                if (isEmailVerificationEnabled && 
                    !EmailVerificationService.verifyEmail( emailAddrStr ))
                {
                    return emailError( mapping, request, "" );
                }
            }
            try
            {
                ldapUserService.updateUserName( 
                    new LdapUserConsumerContext(),
                    currentUser.getUsername(), 
                    emailAddrStr
                );
            }
            catch (Exception exc) {
                _logger.error( ExceptionUtils.getFullStackTrace( exc ) );
            }

            //update the local copy of current user
            currentUser = UserContextService.getActiveSharedUser();
            Person person = telesalesService.getPersonByPartyId( new TelesalesServiceConsumerContext(), currentUser.getPartyId() );
            List<ContactPoint> lstContactPoint = person.getContactPoints();
            ContactPoint foundContactPoint = null;

            for (ContactPoint cp: lstContactPoint) {
                if (cp.getContactPointType() == ContactPointTypeEnum.EMAIL) {
                    foundContactPoint = cp;
                }
            }
            if (foundContactPoint == null) {
                return updateEmailPasswordError(
                    mapping, request, "Email address update failed, Unable to modify email address" );
            }
            else {
                telesalesService.updateContactPointEmail ( 
                    new TelesalesServiceConsumerContext(),
                    foundContactPoint.getRelPartyId(), 
                    emailAddrStr, 
                    ContactPointPurposeEnum.INTERNET,
                    foundContactPoint.getPrimaryFlag(), 
                    "A", foundContactPoint.getContactPointId(), 
                    foundContactPoint.getContactPointVersion()
                );
            }
            CCUser updatedCCUser = UserServices.getCCUserForAUID( UserContextService.getActiveAppUser().getAuid() );

            if(updatedCCUser == null)
            {
                return updateEmailPasswordError(
                    mapping, request, "Email address update failed, Unable to modify email address" ); 
            }
            UserContextService.updateAppUser( updatedCCUser );
            UserContextService.updateSharedUser( currentUser.getPartyId().longValue() );

            // NOTE IT WOULD BE BETTER TO DO AN AUTOLOGIN HERE / Leave the user logged in.
            // The issue is that the login mechanism still thinks you have the old ID.

            UserServices.updateCurrentUsername( emailAddrStr );

            CentralQueueService cqService = ServiceLocator.getCentralQueueService();

            //Add to queue for update back to RightsLink if this is also a RLink User
            if (actionForm.getRlUser())
            {
                cqService.changeLicenseeUsername(
                    new CQConsumerContext(), 
                    Source.COPYRIGHT_COM, 
                    currentUserStr, 
                    newUserStr
                );
            }          
            sendSetNewUserNameEmail( 
                ldapUserService.getUser( new LdapUserConsumerContext(), emailAddrStr ), 
                request 
            );

            // if update user name is success update cc profile
            try {
                updateCCProfileUserId( oldEmailAddrStr, emailAddrStr );	
            }
            catch(Exception e) {
                // if exception user will loose credit card
                _logger.info( "Update vredit card profile usedid failed" );
                _logger.error( ExceptionUtils.getFullStackTrace( e ) );	
            }
            UserContextService.unAuthenticateUserContext();
            request.getSession().invalidate();

            CCUserContext ctxt = UserContextService.createUserContext( updatedCCUser );

            UserContextService.setCurrent( ctxt ); 
        }
        return mapping.findForward( SUCCESS );
    }

    private ActionForward updateEmailPasswordError( ActionMapping m, 
                                                    HttpServletRequest r, 
                                                    String msg )
    {
        ActionMessages errors = new ActionMessages();

        errors.add( ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage( "errors.updateEmailPasswordError", msg )
        );
        saveErrors( r, errors );
        return m.findForward( FAILURE );
    }

    private ActionForward emailError( ActionMapping m, 
                                      HttpServletRequest r, 
                                      String msg )
    {
        ActionMessages errors = new ActionMessages();

        errors.add( ActionMessages.GLOBAL_MESSAGE,
            new ActionMessage( "errors.email", msg )
        );
        saveErrors( r, errors );
        return m.findForward( FAILURE );
    }

    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        UpdateEmailAddressActionForm actionForm = castForm( UpdateEmailAddressActionForm.class, form );
        
        //User currentUser = UserContextService.getActiveSharedUser();
        String emailAddrStr;
        if (actionForm.getEmailAddress() != null && actionForm.getEmailAddress() != "") {
        	emailAddrStr =	actionForm.getEmailAddress().trim();        	
        }
        else {
        	emailAddrStr = actionForm.getEmailAddress();
        }
        //String oldEmailAddrStr=currentUser.getUsername();
        TelesalesService telesalesService =ServiceLocator.getTelesalesService();
    	LdapUserService  ldapUserService = ServiceLocator.getLdapUserService();
    	
   if ( StringUtils2.isNullOrEmpty(emailAddrStr)) { 	    	      
   //if StringUtils2.isNullOrEmpty(emailAddrStr) {
        //if (emailAddrStr == null || emailAddrStr == "") {
        	//this could be the first pass through check if e-business(tele-sales)is up and running.

             boolean teleSalesUp = false;
             try {
             	teleSalesUp = SystemStatus.isTelesalesUp();
             }
             catch (Exception exc){
         		_logger.error( ExceptionUtils.getFullStackTrace(exc) );
             }

             actionForm.setUpdateable(telesalesService!=null&&teleSalesUp);
             
             //ldapUserService.
             User sessionUser = UserContextService.getSharedUser(); 
             long userPartyId = sessionUser.getPartyId();
             String userName = sessionUser.getUsername();
             try {
	             LdapUser ldapUser = ldapUserService.getUser(new LdapUserConsumerContext(),userName);
	             if (ldapUser.getRightsLinkPartyID()==null || Integer.parseInt(ldapUser.getRightsLinkPartyID()) == 0){
	            	 actionForm.setSamePartyID(false);
	            	 actionForm.setRlUser(false);
	             }
	             else {
	            	 long rightsLinkPartyId = Long.parseLong(ldapUser.getRightsLinkPartyID());
	            	 actionForm.setSamePartyID(userPartyId == rightsLinkPartyId);
	            	 actionForm.setRlUser(true);
	             }
             }
             catch (Exception exc){
         		_logger.error( ExceptionUtils.getFullStackTrace(exc) );
             }

             return mapping.findForward(FAILURE);
        }
        
        return mapping.findForward( SUCCESS );
    }
    
    private void sendSetNewUserNameEmail(LdapUser user, HttpServletRequest request ) 
    {
        
        String userDisplayName = user.getFirstName() + " " + user.getLastName();
                                    
        CommunicationsServiceAPI commService = CommunicationsServiceFactory.getInstance().getService();
        ActionMessages ae = new ActionMessages();
        java.util.Map<String, String> updateMap = new java.util.HashMap<String, String>();
        updateMap.put("User Name", user.getUsername());

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
    
    private void  updateCCProfileUserId(String oldUserId,String newUserId){
    	CyberSourceService cyberSourceService=ServiceLocator.getCyberSourceService();
    	PaymentProfileInfo criteria=new PaymentProfileInfo();
    	criteria.setUserId(oldUserId);
    	criteria.setStatus("enabled");
    //	PaymentProfileInfo updateProfileInfo=null;
    	List<PaymentProfileInfo> profileInfos =cyberSourceService.findPaymentProfiles(new UserInfoConsumerContext(), criteria, 0,25, null);
    	if(profileInfos!=null && profileInfos.size()>0){
    		for( PaymentProfileInfo profileInfo:profileInfos){
    			criteria=new PaymentProfileInfo();
        		profileInfo.setUserId(newUserId);
        		criteria.setCccProfileId( profileInfo.getCccProfileId());
        		cyberSourceService.updatePaymentProfiles(new UserInfoConsumerContext(), criteria, profileInfo, true);
        	}
    	}
    	
    }
}
