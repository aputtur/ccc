/*
* RegistrationAction.java
* Copyright (c) 2005, Copyright Clearance Center, Inc. All rights reserved.
* ----------------------------------------------------------------------------
* Revision History
* 09-14-2005   PPAI  Created.
* 02-07-2006   TF    Added alreadyRegistered 'forward' for already registered exception
* 04-06-2007   PPAI    CC2 changes
* ----------------------------------------------------------------------------
*/
package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.security.CookieUtils;
import com.copyright.ccc.business.security.SecurityUtils;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.email.services.EmailVerificationService;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.LoginForm;
import com.copyright.ccc.web.forms.RegisterAddUserForm;
import com.copyright.ccc.web.forms.RegisterIndividualForm;
import com.copyright.ccc.web.forms.RegisterOrganizationForm;
import com.copyright.data.ValidationException;
import com.copyright.data.account.EmailAddressNotUniqueException;
import com.copyright.mail.MailSendFailedException;
import com.copyright.service.comm.CommunicationsServiceAPI;
import com.copyright.service.comm.CommunicationsServiceFactory;
import com.copyright.svc.centralQueue.api.CentralQueueService;
import com.copyright.svc.centralQueue.api.data.CQConsumerContext;
import com.copyright.svc.centralQueue.api.data.MarketSegment;
import com.copyright.svc.centralQueue.api.data.Source;
import com.copyright.svc.centralQueue.api.data.UserType;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.rlUser.api.RLUserServicesInterface;
import com.copyright.svc.rlUser.api.data.RlnkUser;
import com.copyright.svc.telesales.api.TelesalesCompositeService;
import com.copyright.svc.telesales.api.TelesalesService;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.ChannelSourceTypeEnum;
import com.copyright.svc.telesales.api.data.ClassCodeEnum;
import com.copyright.svc.telesales.api.data.Classification;
import com.copyright.svc.telesales.api.data.ContactPoint;
import com.copyright.svc.telesales.api.data.ContactPointPurposeEnum;
import com.copyright.svc.telesales.api.data.CustomerCategoryEnum;
import com.copyright.svc.telesales.api.data.MarketSegmentEnum;
import com.copyright.svc.telesales.api.data.Person;
import com.copyright.svc.telesales.api.data.PhoneLineTypeEnum;
import com.copyright.svc.telesales.api.data.TelesalesServiceConsumerContext;
import com.copyright.svc.telesales.api.data.UserTypeEnum;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.ArAccountInfo;
import com.copyright.svc.tf.api.data.Party;
import com.copyright.svc.tf.api.data.TFConsumerContext;
import com.copyright.workbench.net.SimpleIPAddress;

public class RegistrationAction extends CCAction
{
    String queryString = null;
    private static final String CCC_CCC = "ccc_ccc";

    private static final String _FAILURE    = "failure";
    private static final String _SUCCESS    = "success";
    private static final String _DENIED     = "accessDenied";
    private static final String _DUPLICATE  = "alreadyRegistered";
    private static final String _FAILADDUSR = "failureAddUser";

    public ActionForward defaultOperation( 
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response )
    throws IOException, ServletException 
    {
        String fwd = _SUCCESS;

        if ("GET".equalsIgnoreCase( request.getMethod() ))
        {
            return (mapping.findForward( _DENIED ));
        }

        LdapUserService ldapService = ServiceLocator.getLdapUserService();
        TFService tfService =ServiceLocator.getTFService();
        CentralQueueService cqService = ServiceLocator.getCentralQueueService();
        TelesalesService telesalesService = ServiceLocator.getTelesalesService();
        TelesalesCompositeService telesalesCompositeService = ServiceLocator.getTelesalesCompositeService();
        RLUserServicesInterface rlUserService= ServiceLocator.getRLUserService();

        SimpleIPAddress ip = new SimpleIPAddress( SecurityUtils.determineClientIP( request ) );
        boolean internalIP = SecurityUtils.isInternalIP( ip ) || SecurityUtils.isLoopbackIP( ip );
        boolean isIntranetEmailCheckEnable = CC2Configuration.getInstance().isEmailVerifcationIntranetCheckEnabled();

        boolean isEmailVerificationEnabled = CC2Configuration.getInstance().isEmailVerifcationEnabled();

        String currentFax = null;
        String currentBillFax = null;
        String rightLnkPartyId=null;
        LdapUser ldapUser=null;

        if ( mapping.getPath().equalsIgnoreCase( "/registerIndividual" ))
        {
            //  **********************************************************
            //  ****                   INDIVIDUAL                     ****
            //  **********************************************************

            RegisterIndividualForm frm = castForm( RegisterIndividualForm.class, form );

            try
            {
                if ("UPDATE".equalsIgnoreCase( frm.getStatus() )) 
                {        	          	         	  
                    if (!SystemStatus.isTelesalesUp())
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add( 
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage("errors.registrationError", 
                                "Update not possible at this time. Please try again later."
                            )
                        );
                        saveErrors(request, errors);
                        return (mapping.findForward(_FAILURE));
                    }

                    ldapUser = 
                        ldapService.getUserByPartyId(
                            new LdapUserConsumerContext(), 
                            frm.getMailingPersonPartyId()
                        );

                    rightLnkPartyId=ldapUser.getRightsLinkPartyID();

                    if (StringUtils.isNotEmpty( rightLnkPartyId ) && !SystemStatus.isRightslinkUp())
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage(
                                "errors.registrationError", 
                                "Update not possible at this time. Please try again later." )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILURE ));
                    }
                    //  Mail Person for Update Comparison

                    Person mailPerson = 
                        telesalesService.getPersonByPartyId(
                            new TelesalesServiceConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Bill To Info for Update Comparison

                    ARAccount arUpdateAccount = 
                        telesalesService.getARAccountByARAccountNumber(
                            new TelesalesServiceConsumerContext(), 
                            frm.getAccount(), 
                            UserTypeEnum.IND
                        );

                    //  Rebuild the mailperson for Updates comparison

                    mailPerson.getName().setPrefix( transTitle( frm.getTitle() ) );
                    mailPerson.getName().setFirstName( frm.getFirstName() );
                    mailPerson.getName().setMiddleName( frm.getMiddleName() );
                    mailPerson.getName().setLastName( frm.getLastName() );

                    //mailPerson.getOrganizationContact().setDepartment(frm.getDepartment());
                    //mailPerson.getOrganizationContact().setJobTitle(frm.getJobTitle());

                    String postalCode = "";

                    if ("US".equalsIgnoreCase( frm.getCountry() )) 
                    {
                        postalCode = frm.getZipcode() + frm.getZipPlus4();
                    }
                    else {
                        postalCode = frm.getZipcode();
                    }
                    mailPerson.getPartySites().get( 0 ).getLocation().setAddress1( frm.getAddress1() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setAddress2( frm.getAddress2() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setAddress3( frm.getAddress3() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setCity( frm.getCity() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setPostalCode( postalCode );
                    mailPerson.getPartySites().get( 0 ).getLocation().setState( frm.getState() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setCountry( frm.getCountry() );

                    for (int k = 0; k < mailPerson.getContactPoints().size(); k++)              	  		              
                    {
                        if (mailPerson.getContactPoints().get( k ).getPhoneLineType() != null)
                        {
                            if (mailPerson.getContactPoints().get( k ).getPhoneLineType().equals( PhoneLineTypeEnum.GENERAL ))  
                            {
                                mailPerson.getContactPoints().get( k ).setDisplayPhone( frm.getPhone() );
                                //mailPerson.getContactPoints().get(k).setPhoneNumber(frm.getPhone());
                                mailPerson.getContactPoints().get( k ).setPhoneExtension( frm.getPhoneExtension() );
                            }
                            else if (mailPerson.getContactPoints().get( k ).getPhoneLineType().equals( PhoneLineTypeEnum.FAX ))
                            {
                                currentFax = mailPerson.getContactPoints().get( k ).getPhoneNumber();
                                mailPerson.getContactPoints().get( k ).setPhoneNumber( frm.getFax() );
                                mailPerson.getContactPoints().get( k ).setDisplayPhone( frm.getFax() );
                            }
                        }
                    }
                    int j = 0;

                    for (Classification classification : mailPerson.getClassifications())
                    {
                        if (classification.getCategory().equals( CustomerCategoryEnum.MARKETSEGMENT ))
                        {
                            mailPerson.getClassifications().get( j )
                                .setCode( getClassCode( frm.getPersonType().toLowerCase() ) );
                        }
                        j = j + 1;
                    }

                    Map<String,String> updateMap = 
                        telesalesService.compareContactAndBillingInformation(
                            new TelesalesServiceConsumerContext(), 
                            mailPerson, 
                            arUpdateAccount
                        );

                    Party pty = 
                        tfService.getIndLicenseePartyByPartyId( 
                            new TFConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Update LDAP

                    updateLdapUser( 
                        frm.getUserName().trim(), 
                        frm.getTitle(), 
                        frm.getFirstName(), 
                        frm.getMiddleName(), 
                        frm.getLastName(), "", "", 
                        frm.getAddress1(), 
                        frm.getAddress2(), 
                        frm.getAddress3(), "", 
                        frm.getCity(), 
                        frm.getState(), 
                        postalCode, 
                        frm.getCountry(), 
                        frm.getPhone(), 
                        frm.getFax(), 
                        frm.getMailingPersonPartyId(), 
                        frm.getAccount(), 
                        com.copyright.svc.ldapuser.api.data.UserType.INDIVIDUAL.getValue()
                    );

                    //  Update Party table

                    updateIndParty(
                        pty.getPtyInst(), 
                        frm.getFirstName(), 
                        frm.getMiddleName(), 
                        frm.getLastName(), 
                        pty.getAccountNumber(), 
                        pty.getPartyId()
                    );

                    Person person = 
                        telesalesService.getPersonByPartyId(
                            new TelesalesServiceConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Update Market segment

                    for (Classification classification : person.getClassifications())
                    {
                        if (classification.getCategory().equals( CustomerCategoryEnum.MARKETSEGMENT ))
                        {
                            telesalesService.updateCodeAssignment( 
                                new TelesalesServiceConsumerContext(),
                                classification.getId(), 
                                getMktSegment( frm.getPersonType() ), 
                                classification.getVersion()
                            );
                        }
                    }

                    //  Update Person name

                    telesalesService.updatePersonName(
                        new TelesalesServiceConsumerContext(),
                        person.getPartyId(), 
                        person.getPartyVersion(), 
                        transTitle(frm.getTitle()), 
                        "",
                        frm.getFirstName(),
                        frm.getMiddleName(),
                        frm.getLastName()
                    );

                    //  Update Address

                    person.getPartySites().get( 0 ).getLocation().setAddress1( frm.getAddress1() );
                    person.getPartySites().get( 0 ).getLocation().setAddress2( frm.getAddress2() );
                    person.getPartySites().get( 0 ).getLocation().setAddress3( frm.getAddress3() );
                    person.getPartySites().get( 0 ).getLocation().setCity( frm.getCity() );
                    person.getPartySites().get( 0 ).getLocation().setState( frm.getState() );
                    person.getPartySites().get( 0 ).getLocation().setPostalCode( postalCode );
                    person.getPartySites().get( 0 ).getLocation().setCountry( frm.getCountry() );

                    telesalesService.updateLocation(
                        new TelesalesServiceConsumerContext(), 
                        person.getPartySites().get( 0 ).getLocation()
                    );

                    //  Update Phone / FAX

                    List<ContactPoint> inactiveFaxContacts = new ArrayList<ContactPoint>();
                    List<ContactPoint> allFaxContacts = new ArrayList<ContactPoint>();

                    for (ContactPoint contactPoint: person.getContactPoints())
                    {
                        if (contactPoint.getPhoneLineType() != null)
                        {
                            if (contactPoint.getPhoneLineType().equals( PhoneLineTypeEnum.GENERAL ))
                            {
                                contactPoint.setPhoneNumber( frm.getPhone() );
                                contactPoint.setPhoneExtension( frm.getPhoneExtension() );

                                telesalesService.updateContactPoint(
                                    new TelesalesServiceConsumerContext(), 
                                    person.getPartyId(), 
                                    person.getPartyId(), 
                                    frm.getCountry(), 
                                    contactPoint
                                );
                            }
                            else if (contactPoint.getPhoneLineType().equals( PhoneLineTypeEnum.FAX ))
                            {
                                //	2010-04-23 MSJ
                                //	Inactivate the FAX number instead of trying to null
                                //	it out.

                                allFaxContacts.add( contactPoint );

                                if (frm.getFax() == null || "".equals(frm.getFax()))
                                {
                                    telesalesService.inactivateContactPoint(
                                        new TelesalesServiceConsumerContext(), 
                                        person.getPartyId(), 
                                        contactPoint.getRelPartyId(),
                                        frm.getCountry(), 
                                        contactPoint
                                    );
                                }
                                else 
                                {
                                    if ("A".equalsIgnoreCase( contactPoint.getStatus() ))
                                    {
                                        contactPoint.setPhoneNumber( frm.getFax() );

                                        telesalesService.updateContactPoint(
                                            new TelesalesServiceConsumerContext(), 
                                            person.getPartyId(), 
                                            person.getPartyId(), 
                                            frm.getCountry(), 
                                            contactPoint
                                        );
                                    }
                                    else
                                    {
                                        inactiveFaxContacts.add( contactPoint );
                                    }
                                }
                            }
                        }
                    }
                    //  Check if the Fax Number is empty before and not empty now.
                    //  In this case, we need to create the new fax number.

                    if (!frm.getFax().isEmpty() && allFaxContacts.size() == inactiveFaxContacts.size())
                    {
                        //  Create NEW Fax

                        telesalesService.createPhoneFaxNumber(
                            new TelesalesServiceConsumerContext(),
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() ), 
                            ContactPointPurposeEnum.NONE, 
                            "", 
                            "", 
                            frm.getFax(), 
                            "", 
                            PhoneLineTypeEnum.FAX, 
                            false, 
                            ChannelSourceTypeEnum.COPYRIGHTCOM
                        );
                    }
                    //  Update only RLink customer
                    //  update rightslink user

                    if (StringUtils.isNotEmpty( rightLnkPartyId) ) 
                    {
                        RlnkUser rlnkIndVUser = null;

                        rlnkIndVUser = rlUserService.getUserByUserName( ldapUser.getUsername() );
                        rlnkIndVUser.setTaxId( frm.getTaxId() );

                        rlUserService.updateUser( rlnkIndVUser );
                    }

                    if (StringUtils.isNotEmpty( rightLnkPartyId ) && ldapUser.getPartyID() != null)
                    {
                        if (ldapUser.getPartyID().equalsIgnoreCase( ldapUser.getRightsLinkPartyID() ))
                        {
                            cqService.updateLicensee(
                                new CQConsumerContext(), 
                                Source.COPYRIGHT_COM, 
                                frm.getUserName().trim(),
                                UserType.INDIVIDUAL, 
                                true,
                                transTitle( frm.getTitle() ), 
                                frm.getFirstName(), 
                                frm.getMiddleName(),
                                frm.getLastName(), 
                                "", 
                                frm.getAddress1(), 
                                frm.getAddress2(),
                                frm.getAddress3(), 
                                "",
                                postalCode, 
                                frm.getState(),
                                frm.getCity(), 
                                "", 
                                frm.getCountry(), 
                                frm.getPhone(),
                                frm.getPhoneExtension(), 
                                frm.getFax(), 
                                "", "", "", "", "",
                                "", "", "", "", "", 
                                "", "", "", "", "", 
                                "", "", "", "", ""
                            );
                        }
                    }
                    //  Update Account e-mail

                    String name = frm.getFirstName() + " " + frm.getLastName();
                    sendSetPasswordEmail( name, updateMap, ldapUser.getUsername().trim(), request );
                }
                else // Create NEW User
                {
                    if (internalIP)
                    {
                        if (isIntranetEmailCheckEnable)
                        {
                            if(isEmailVerificationEnabled && !EmailVerificationService.verifyEmail( frm.getUserName().trim() ))
                            {
                                ActionMessages errors = new ActionMessages();
                                errors.add(
                                    ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage( "errors.email", "" )
                                );
                                saveErrors( request, errors );
                                return (mapping.findForward( _FAILURE ));
                            }
                        }
                    }
                    else
                    {
                        if (isEmailVerificationEnabled && !EmailVerificationService.verifyEmail( frm.getUserName().trim() ))
                        {
                            ActionMessages errors = new ActionMessages();
                            errors.add(
                                ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage( "errors.email", "" )
                            );
                            saveErrors( request, errors );
                            return (mapping.findForward( _FAILURE )); 
                        }
                    }

                    if (!ldapService.isValidUserName( new LdapUserConsumerContext(), frm.getUserName().trim() ))
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage( "errors.email", "" )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILURE ));            	  
                    }

                    if (!ldapService.isUsernameAvailable( new LdapUserConsumerContext(), frm.getUserName().trim() ))
                    {        		          		 
                        request.setAttribute( WebConstants.RequestKeys.EMAIL, frm.getUserName().trim() );
                        return (mapping.findForward( _DUPLICATE ));                         		        		 
                    }
                    //  Add to LDAP

                    String postalCode = "";

                    if ("US".equalsIgnoreCase( frm.getCountry() )) {
                        postalCode = frm.getZipcode() + frm.getZipPlus4();
                    }
                    else {
                        postalCode = frm.getZipcode();
                    }
                    LdapUser ldUser = 
                        createLdapUser( 
                            frm.getUserName().trim(), 
                            frm.getPassword(), 
                            com.copyright.svc.ldapuser.api.data.UserType.INDIVIDUAL.getValue(), 
                            frm.getTitle(), 
                            frm.getFirstName(), 
                            frm.getMiddleName(), 
                            frm.getLastName(), 
                            "", 
                            frm.getAddress1(), 
                            frm.getAddress2(), 
                            frm.getAddress3(), 
                            frm.getCity(), 
                            postalCode, 
                            frm.getCountry(), 
                            frm.getState(), 
                            frm.getPhone(), 
                            frm.getFax(), 
                            ""
                        );

                    //	Add to Party table

                    Long ptyInst = 
                        tfService.provisionIndividualAndIndividualLicenseeTransactionalAccount(
                            new TFConsumerContext(), 
                            "", 
                            frm.getFirstName(), 
                            "", 
                            frm.getLastName(), 
                            "", 
                            null, 
                            null, 
                            "WWW_USER"
                        );

                    Party licenseeParty = 
                        tfService.getLicenseePartyByPtyInst( new TFConsumerContext(), ptyInst );

                    // Update in LDAP

                    updateLdapUser( ldUser, licenseeParty.getPartyId() );

                    // Provision in CC

                    createCCUser( ldUser );

                    cqService.createLicensee(
                        new CQConsumerContext(), 
                        Source.COPYRIGHT_COM, 
                        frm.getUserName().trim(), 
                        UserType.INDIVIDUAL,
                        transTitle( frm.getTitle() ), 
                        frm.getFirstName(), 
                        frm.getMiddleName(),
                        frm.getLastName(), 
                        "", 
                        frm.getAddress1(), 
                        frm.getAddress2(),
                        frm.getAddress3(), 
                        "",
                        postalCode, 
                        frm.getState(),
                        frm.getCity(), 
                        "", 
                        frm.getCountry(), 
                        frm.getPhone(),
                        frm.getPhoneExtension(), 
                        frm.getFax(), 
                        false, 
                        "", "", "", "", "",
                        "", "", "", "", "", 
                        "", "", "", "", "", 
                        "", "", "", "", "", 
                        MarketSegment.valueOf( getSegment( frm.getPersonType() ) ), 
                        ""
                    );
                }
            }
            catch (EmailAddressNotUniqueException eanu)
            {
                _logger.error( ExceptionUtils.getFullStackTrace( eanu ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", eanu.getMessage() )
                );
                saveErrors( request, errors );
                request.setAttribute( WebConstants.RequestKeys.EMAIL, frm.getUserName().trim() );
                return (mapping.findForward( _DUPLICATE ));
            }
            catch (ValidationException ve)
            {
                _logger.error( ExceptionUtils.getFullStackTrace( ve ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", ve.getMessage() )
                );
                saveErrors( request, errors );
                return (mapping.findForward( _FAILURE ));
            }
            catch (Exception e) 
            { 
                _logger.error( ExceptionUtils.getFullStackTrace( e ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", e.getMessage() )
                );
                saveErrors( request, errors );
                return (mapping.findForward( _FAILURE ));
            } 

            if ("UPDATE".equalsIgnoreCase( frm.getStatus() ))
            {
                //  Update the user context

                long mainPartyId = Long.valueOf( frm.getMailingPersonPartyId() ).longValue();
                UserContextService.updateSharedUser( mainPartyId );

                //  Update the Always invoice me flag

                UserServices.updateCurrentUserAlwaysInvoice( "T".equalsIgnoreCase( frm.getAlwaysInvoiceFlag() ) );
                UserServices.updateCurrentUserAutoInvoiceSpecialOrder( "T".equalsIgnoreCase( frm.getAutoInvoiceSpecialOrderFlag() ) );

                //  MSJ 2008/01/24
                //  Add call to toggle the skip quickprice flag.

                request.setAttribute( WebConstants.RequestKeys.EMAIL, "" );                        
                return mapping.findForward( _SUCCESS );
            }
            else
            {
                LoginForm loginForm = new LoginForm();
                String _destination = "displayRegisterIndView";

                //  2007/07/30  MSJ
                //  Instead of passing u/p in URL create login form,
                //  prepopulate and stuff in session.

                loginForm.setAutoLoginUsername( frm.getUserName().trim() );
                loginForm.setAutoLoginPassword( frm.getPassword() );
                loginForm.setAutoLoginPhase( LoginForm.AL_PHASE_ONE );
                loginForm.setAutoLoginForward( _destination );

                request.getSession().setAttribute( "loginForm", loginForm );
                request.setAttribute( "loginForm", loginForm );

                return mapping.findForward( "successAutoLogin" );
            }
        }
        else if (mapping.getPath().equalsIgnoreCase( "/registerOrganization" ))
        {
            //  **********************************************************
            //  ****                  ORGANIZATION                    ****
            //  **********************************************************

            RegisterOrganizationForm frm = castForm( RegisterOrganizationForm.class, form );

            try
            {
                if ("UPDATE".equalsIgnoreCase( frm.getStatus() ))
                {
                    ldapUser = ldapService.getUserByPartyId(
                        new LdapUserConsumerContext(), 
                        frm.getMailingPersonPartyId()
                    );

                    rightLnkPartyId = ldapUser.getRightsLinkPartyID();

                    if (!SystemStatus.isTelesalesUp())
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage( 
                                "errors.registrationError", 
                                "Update not possible at this time. Please try again later." )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILURE ));
                    }

                    if (StringUtils.isNotEmpty( rightLnkPartyId ) && !SystemStatus.isRightslinkUp())
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage(
                                "errors.registrationError", 
                                "Update not possible at this time. Please try again later.")
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILURE ));
                    }

                    Person person = 
                        telesalesService.getPersonByPartyId(
                            new TelesalesServiceConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Mail Person for Update Comparison

                    Person mailPerson = 
                        telesalesService.getPersonByPartyId( 
                            new TelesalesServiceConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Bill To Info for Update Comparison

                    ARAccount arUpdateAccount = 
                        telesalesService.getARAccountByARAccountNumber(
                            new TelesalesServiceConsumerContext(), 
                            frm.getAccount(), 
                            UserTypeEnum.ORG
                        );

                    Party pty = 
                        tfService.getIndLicenseePartyByPartyId(
                            new TFConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Update rightslink user

                    if (StringUtils.isNotEmpty( rightLnkPartyId ))
                    {
                        RlnkUser rlnkOrgUser = null;

                        rlnkOrgUser = rlUserService.getUserByPartyID( Long.valueOf( rightLnkPartyId ) );

                        rlnkOrgUser.setIndustry( frm.getIndustryType() );
                        rlnkOrgUser.setTaxId( frm.getTaxId() );
                        rlnkOrgUser.setOrgStatus( frm.getOrgStatus() );

                        rlUserService.updateUser( rlnkOrgUser );
                    }

                    //  Rebuild the mailperson for Updates comparison

                    String postalCode = "";

                    if ("US".equalsIgnoreCase( frm.getCountry() )) 
                    {
                        postalCode = frm.getZipcode() + frm.getZipPlus4();
                    }
                    else {
                        postalCode = frm.getZipcode();
                    }

                    mailPerson.getName().setPrefix( transTitle( frm.getTitle() ) );
                    mailPerson.getName().setFirstName( frm.getFirstName() );
                    mailPerson.getName().setMiddleName( frm.getMiddleName() );
                    mailPerson.getName().setLastName( frm.getLastName() );

                    mailPerson.getOrganizationContact().setDepartment( frm.getDepartment() );
                    mailPerson.getOrganizationContact().setJobTitle( frm.getJobTitle() );

                    mailPerson.getPartySites().get( 0 ).getLocation().setAddress1( frm.getAddress1() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setAddress2( frm.getAddress2() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setAddress3( frm.getAddress3() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setCity( frm.getCity() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setPostalCode( postalCode );
                    mailPerson.getPartySites().get( 0 ).getLocation().setState( frm.getState() );
                    mailPerson.getPartySites().get( 0 ).getLocation().setCountry( frm.getCountry() );

                    for (int k = 0; k < mailPerson.getContactPoints().size(); k++)              	  		              
                    {
                        if (mailPerson.getContactPoints().get( k ).getPhoneLineType() != null)
                        {
                            if (mailPerson.getContactPoints().get( k ).getPhoneLineType().equals( PhoneLineTypeEnum.GENERAL ))  
                            {
                                mailPerson.getContactPoints().get( k ).setDisplayPhone( frm.getPhone() );
                                //mailPerson.getContactPoints().get(k).setPhoneNumber(frm.getPhone());
                                mailPerson.getContactPoints().get( k ).setPhoneExtension( frm.getPhoneExtension() );
                            }
                            else if (mailPerson.getContactPoints().get( k ).getPhoneLineType().equals( PhoneLineTypeEnum.FAX ))
                            {
                                currentFax = mailPerson.getContactPoints().get( k ).getPhoneNumber();
                                //mailPerson.getContactPoints().get(k).setPhoneNumber(frm.getFax());
                                mailPerson.getContactPoints().get( k ).setDisplayPhone( frm.getFax() );
                            }
                        }
                    }

                    int j = 0;

                    for (Classification classification : mailPerson.getClassifications())
                    {
                        if (classification.getCategory().equals( CustomerCategoryEnum.MARKETSEGMENT ))
                        {
                            mailPerson.getClassifications().get( j )
                                .setCode( getClassCode( frm.getPersonType().toLowerCase() ) );
                        }
                        j = j + 1;
                    }

                    //  Rebuild ARAccount object for Update Comparison

                    arUpdateAccount.getName().setPrefix( transTitle( frm.getBillingTitle() ) );
                    arUpdateAccount.getName().setFirstName( frm.getBillingFirstName() );
                    arUpdateAccount.getName().setMiddleName( frm.getBillingFirstName() );
                    arUpdateAccount.getName().setLastName( frm.getLastName() );

                    arUpdateAccount.getOrganizationContact().setDepartment( frm.getBillingDepartment() );
                    arUpdateAccount.getOrganizationContact().setJobTitle( frm.getBillingJobTitle() );

                    arUpdateAccount.getLocation().setAddress1( frm.getBillingAddress1() );
                    arUpdateAccount.getLocation().setAddress2( frm.getBillingAddress2() );
                    arUpdateAccount.getLocation().setAddress3( frm.getBillingAddress3() );
                    arUpdateAccount.getLocation().setCity( frm.getBillingCity() );
                    arUpdateAccount.getLocation().setCountry( frm.getBillingCountry() );

                    String billingPostalCode = "";

                    if ("US".equalsIgnoreCase( frm.getBillingCountry() )) 
                    {
                        billingPostalCode = frm.getBillingZipcode() + frm.getBillingZipPlus4();
                    }
                    else {
                        billingPostalCode = frm.getBillingZipcode();
                    }
                    arUpdateAccount.getLocation().setPostalCode( billingPostalCode );
                    arUpdateAccount.getLocation().setState( frm.getBillingState() );

                    for (int m = 0; m < arUpdateAccount.getContactPoints().size(); m++)              	  		              
                    {
                        if (arUpdateAccount.getContactPoints().get( m ).getPhoneLineType() != null)
                        {
                            if (arUpdateAccount.getContactPoints().get( m ).getPhoneLineType().equals( PhoneLineTypeEnum.GENERAL ))
                            {
                                arUpdateAccount.getContactPoints().get( m ).setDisplayPhone( frm.getBillingPhone() );
                                arUpdateAccount.getContactPoints().get( m ).setPhoneExtension( frm.getBillingPhoneExtension() );
                            }
                            else if (arUpdateAccount.getContactPoints().get( m ).getPhoneLineType().equals( PhoneLineTypeEnum.FAX ))
                            {
                                currentBillFax = arUpdateAccount.getContactPoints().get( m ).getPhoneNumber();
                                //arUpdateAccount.getContactPoints().get(m).setPhoneNumber(frm.getBillingFax());
                                arUpdateAccount.getContactPoints().get( m ).setDisplayPhone( frm.getBillingFax() );
                            }
                        }
                    }

                    Map<String,String> updateMap = 
                        telesalesService.compareContactAndBillingInformation(
                            new TelesalesServiceConsumerContext(), 
                            mailPerson, 
                            arUpdateAccount
                        );

                    //  Update LDAP

                    updateLdapUser(
                        frm.getUserName().trim(), 
                        frm.getTitle(), 
                        frm.getFirstName(), 
                        frm.getMiddleName(),
                        frm.getLastName(), 
                        frm.getCompany(), 
                        frm.getDepartment(), 
                        frm.getAddress1(), 
                        frm.getAddress2(), 
                        frm.getAddress3(), 
                        "", 
                        frm.getCity(), 
                        frm.getState(), 
                        postalCode, 
                        frm.getCountry(), 
                        frm.getPhone(), 
                        frm.getFax(), 
                        frm.getMailingPersonPartyId(), 
                        frm.getAccount(), 
                        com.copyright.svc.ldapuser.api.data.UserType.ORGANIZATION.getValue()
                    );

                    //  Update Party table

                    updateIndParty(
                        pty.getPtyInst(), 
                        frm.getFirstName(), 
                        frm.getMiddleName(), 
                        frm.getLastName(), 
                        pty.getAccountNumber(), 
                        pty.getPartyId()
                    );


                    //  Update Market segment

                    for (Classification classification : person.getClassifications())
                    {
                        if (classification.getCategory().equals( CustomerCategoryEnum.MARKETSEGMENT ))
                        {
                            telesalesService.updateCodeAssignment(
                                new TelesalesServiceConsumerContext(),
                                classification.getId(), 
                                getMktSegment( frm.getPersonType() ), 
                                classification.getVersion()
                            );
                        }
                    }

                    //	Update Person name

                    telesalesService.updatePersonName(
                        new TelesalesServiceConsumerContext(),
                        person.getPartyId(), 
                        person.getPartyVersion(), 
                        transTitle( frm.getTitle() ), 
                        "",
                        frm.getFirstName(),
                        frm.getMiddleName(),
                        frm.getLastName()
                    );

                    //  Update title/dept

                    telesalesService.updateOrganizationContact(
                        new TelesalesServiceConsumerContext(),
                        person.getPartyId(),
                        person.getPartyVersion(),
                        person.getRelPartyVersion(),
                        person.getOrganizationContact().getId(),
                        person.getOrganizationContact().getVersion(),
                        frm.getJobTitle(),
                        frm.getDepartment()
                    );

                    // 	Update Address

                    person.getPartySites().get( 0 ).getLocation().setAddress1( frm.getAddress1() );
                    person.getPartySites().get( 0 ).getLocation().setAddress2( frm.getAddress2() );
                    person.getPartySites().get( 0 ).getLocation().setAddress3( frm.getAddress3() );
                    person.getPartySites().get( 0 ).getLocation().setCity( frm.getCity() );
                    person.getPartySites().get( 0 ).getLocation().setState( frm.getState() );
                    person.getPartySites().get( 0 ).getLocation().setPostalCode( postalCode );
                    person.getPartySites().get( 0 ).getLocation().setCountry( frm.getCountry() );

                    telesalesService.updateLocation(
                        new TelesalesServiceConsumerContext(), 
                        person.getPartySites().get( 0 ).getLocation()
                    );

                    //  Update Phone / FAX

                    List<ContactPoint> inactiveFaxContacts = new ArrayList<ContactPoint>();
                    List<ContactPoint> allFaxContacts = new ArrayList<ContactPoint>();

                    for (ContactPoint contactPoint: person.getContactPoints())
                    {
                        if (contactPoint.getPhoneLineType() != null)
                        {
                            if (contactPoint.getPhoneLineType().equals( PhoneLineTypeEnum.GENERAL ))
                            {
                                contactPoint.setPhoneNumber( frm.getPhone() );
                                contactPoint.setPhoneExtension( frm.getPhoneExtension() );

                                telesalesService.updateContactPoint( 
                                    new TelesalesServiceConsumerContext(), 
                                    person.getPartyId(), 
                                    person.getRelPartyId(), 
                                    frm.getCountry(), 
                                    contactPoint
                                );
                            }
                            else if (contactPoint.getPhoneLineType().equals( PhoneLineTypeEnum.FAX ))
                            {
                                allFaxContacts.add( contactPoint );

                                if (frm.getFax() == null || "".equals( frm.getFax() ))
                                {
                                    telesalesService.inactivateContactPoint(
                                        new TelesalesServiceConsumerContext(), 
                                        person.getPartyId(), 
                                        contactPoint.getRelPartyId(), 
                                        frm.getCountry(), 
                                        contactPoint
                                    );
                                }
                                else 
                                {
                                    if(contactPoint.getStatus().equalsIgnoreCase("A"))
                                    {
                                        contactPoint.setPhoneNumber( frm.getFax() );

                                        telesalesService.updateContactPoint(
                                            new TelesalesServiceConsumerContext(), 
                                            person.getPartyId(),
                                            contactPoint.getRelPartyId(), 
                                            frm.getCountry(), 
                                            contactPoint
                                        );
                                    }
                                    else
                                    {
                                        inactiveFaxContacts.add( contactPoint );
                                    }
                                }              			  
                            }
                        }
                    }

                    //  Check if the Fax Number is empty before and not empty now.
                    //  In this case, we need to create the new fax number.

                    if (!frm.getFax().isEmpty() && allFaxContacts.size() == inactiveFaxContacts.size())
                    {
                        //Create NEW Fax

                        telesalesService.createPhoneFaxNumber(
                            new TelesalesServiceConsumerContext(),
                            person.getRelPartyId(), 
                            ContactPointPurposeEnum.NONE, 
                            "", 
                            "", 
                            frm.getFax(), 
                            "", 
                            PhoneLineTypeEnum.FAX, 
                            false, 
                            ChannelSourceTypeEnum.COPYRIGHTCOM
                        );
                        updateMap.put( "Fax Number", frm.getFax() );
                    }

                    //  Update Billing Contact info if different from Mailing

                    String mailPartyId = frm.getMailingPersonPartyId();
                    String billPartyId = frm.getBillingPersonPartyId();

                    if ((!mailPartyId.equalsIgnoreCase( billPartyId )) && 
                        ("F".equalsIgnoreCase( frm.getSameBillingFlag() )))
                    {
                        //  MAIL TO and BILL TO contact were different and they remain different
                        //  Need to update the Billing Contact Info separately

                        Party billPty = new Party();

                        billPty = tfService.getIndLicenseePartyByPartyId(
                            new TFConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( billPartyId )
                        );

                        Person billPerson = 
                            telesalesService.getPersonByPartyId(
                                new TelesalesServiceConsumerContext(), 
                                org.apache.commons.lang.math.NumberUtils.toLong( billPartyId )
                            ); 

                        ARAccount arAcct = null;
                        List<ARAccount> accounts = null;
                        ARAccount arAccount = null;

                        accounts = 
                            telesalesService.getARAccountByPartyId(
                                new TelesalesServiceConsumerContext(), 
                                org.apache.commons.lang.math.NumberUtils.toLong( mailPartyId ), 
                                UserTypeEnum.ORG
                            );

                        //  Accounts... yeah.  We just want one I think, yet we get a list
                        //  back of who knows how many accounts?

                        Iterator<ARAccount> i = accounts.iterator();

                        while (i.hasNext()) 
                        {
                            arAcct = i.next();

                            if (arAcct.getAccountNumber().equals( frm.getAccount() )) 
                            {
                                //  If the account numbers match, this should be a winner.

                                arAccount = arAcct;
                                break;
                            }
                        }
                                                                	  
                        //  Update Party table

                        updateIndParty(
                            billPty.getPtyInst(), 
                            frm.getBillingFirstName(), 
                            frm.getBillingMiddleName(), 
                            frm.getBillingLastName(), 
                            billPty.getAccountNumber(), 
                            billPty.getPartyId()
                        );

                        //	Update Person name

                        telesalesService.updatePersonName(
                            new TelesalesServiceConsumerContext(),
                            arAccount.getBillToPartyId(),
                            arAccount.getBillToPartyVersion(),
                            transTitle( frm.getBillingTitle() ), 
                            "",
                            frm.getBillingFirstName(),
                            frm.getBillingMiddleName(),
                            frm.getBillingLastName()
                        );

                        //  Update title/dept

                        telesalesService.updateOrganizationContact(
                            new TelesalesServiceConsumerContext(),
                            arAccount.getBillToPartyId(),
                            arAccount.getBillToPartyVersion(),
                            arAccount.getBillToRelPartyId(),
                            arAccount.getOrganizationContact().getId(),
                            arAccount.getOrganizationContact().getVersion(),
                            frm.getBillingJobTitle(),
                            frm.getBillingDepartment()
                        );

                        // 	Update Address

                        arAccount.getLocation().setAddress1( frm.getBillingAddress1() );
                        arAccount.getLocation().setAddress2( frm.getBillingAddress2() );
                        arAccount.getLocation().setAddress3( frm.getBillingAddress3() );
                        arAccount.getLocation().setCity( frm.getBillingCity() );
                        arAccount.getLocation().setState( frm.getBillingState() ); 
                        arAccount.getLocation().setPostalCode( billingPostalCode );
                        arAccount.getLocation().setCountry( frm.getBillingCountry() );

                        telesalesService.updateLocation(
                            new TelesalesServiceConsumerContext(), 
                            arAccount.getLocation()
                        );

                        //	Update Phone/Fax

                        for (ContactPoint contactPoint: arAccount.getContactPoints())
                        {
                            if (contactPoint.getPhoneLineType() != null)
                            {
                                if (contactPoint.getPhoneLineType().equals( PhoneLineTypeEnum.GENERAL ))
                                {
                                    contactPoint.setPhoneNumber( frm.getBillingPhone() );
                                    contactPoint.setPhoneExtension( frm.getBillingPhoneExtension() );

                                    telesalesService.updateContactPoint(
                                        new TelesalesServiceConsumerContext(), 
                                        billPerson.getPartyId(), 
                                        billPerson.getRelPartyId(), 
                                        frm.getBillingCountry(), 
                                        contactPoint
                                    );
                                }
                                else if (contactPoint.getPhoneLineType().equals( PhoneLineTypeEnum.FAX ))
                                {
                                    if (frm.getBillingFax() == null || "".equals(frm.getBillingFax())) 
                                    {
                                        telesalesService.inactivateContactPoint(
                                            new TelesalesServiceConsumerContext(), 
                                            billPerson.getPartyId(), 
                                            contactPoint.getRelPartyId(), 
                                            frm.getBillingCountry(), 
                                            contactPoint
                                        );
                                    }
                                    else 
                                    {
                                        contactPoint.setPhoneNumber( frm.getBillingFax() );

                                        telesalesService.updateContactPoint(
                                            new TelesalesServiceConsumerContext(), 
                                            billPerson.getPartyId(), 
                                            billPerson.getRelPartyId(), 
                                            frm.getBillingCountry(), 
                                            contactPoint
                                        );
                                    }
                                }
                            }
                        }

                        //  Check if the Fax Number is empty before and not empty now.
                        //  In this case, we need to create the new fax number.

                        if (currentBillFax == null && !frm.getBillingFax().isEmpty())
                        {
                            //Create NEW Fax

                            telesalesService.createPhoneFaxNumber(
                                new TelesalesServiceConsumerContext(),
                                billPerson.getRelPartyId(), 
                                ContactPointPurposeEnum.NONE, 
                                "", 
                                "", 
                                frm.getBillingFax(), 
                                "", 
                                PhoneLineTypeEnum.FAX, 
                                false, 
                                ChannelSourceTypeEnum.COPYRIGHTCOM
                            );
                            updateMap.put( "Billing Fax Number", frm.getBillingFax() );
                        }


                    }
                    else if (!(mailPartyId.equalsIgnoreCase( billPartyId )) && 
                              ("T".equalsIgnoreCase(frm.getSameBillingFlag())))
                    {
                        _logger.info( "\nMailTo Party ID != BillTo Party ID, but now making the SAME" );

                        //  Mail To and Bill To were different - But now the user has 
                        //  opted for the Mail To and Bill To to be SAME.

                        Person loginPerson = 
                            telesalesService.getPersonByPartyId(
                                new TelesalesServiceConsumerContext(),
                                org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                            );

                        telesalesService.updateBillToContact(
                            new TelesalesServiceConsumerContext(),	
                            ChannelSourceTypeEnum.COPYRIGHTCOM, 
                            loginPerson.getPartyId(), 
                            loginPerson.getRelPartyId(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( billPartyId )
                        );
                    }
                    else if ((mailPartyId.equalsIgnoreCase( billPartyId )) && 
                             ("F".equalsIgnoreCase(frm.getSameBillingFlag())))
                    {
                        _logger.info( "\nMailTo Party ID == BillTo Party ID, opting for new BillTo" );

                        //  Mail To and Bill TO were same before. But now the user has 
                        //  opted for a different Bill To Contact.
                        //  Create New BILL To Contact

                        Person loginPerson = 
                            telesalesService.getPersonByPartyId(
                                new TelesalesServiceConsumerContext(),
                                org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                            );

                        Long billPartyId2 = 
                            telesalesCompositeService.provisionBillToContact(
                            new TelesalesServiceConsumerContext(), 
                            ChannelSourceTypeEnum.COPYRIGHTCOM, 
                            loginPerson.getPartyId(), 
                            transTitle( frm.getBillingTitle() ), 
                            "", 
                            frm.getBillingFirstName(), 
                            frm.getBillingMiddleName(), 
                            frm.getBillingLastName(),
                            frm.getBillingJobTitle(), 
                            frm.getBillingDepartment(), 
                            frm.getBillingAddress1(),
                            frm.getBillingAddress2(), 
                            frm.getBillingAddress3(), 
                            "", 
                            frm.getBillingCity(), 
                            frm.getBillingState(), 
                            frm.getBillingZipcode(), 
                            frm.getBillingCountry(), 
                            frm.getBillingPhone(), 
                            frm.getBillingPhoneExtension(), 
                            frm.getBillingFax(), 
                            ""
                        );

                        //  Add to Party table

                        tfService.addIndividualToOrganizationLicenseeTransactionalAccount(
                            new TFConsumerContext(), 
                            frm.getBillingTitle(), 
                            frm.getBillingFirstName(), 
                            frm.getBillingMiddleName(), 
                            frm.getBillingLastName(), "", 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getAccount() ), 
                            billPartyId2, 
                            "WWW_USER"
                        );
                    }
                    else if ((mailPartyId.equalsIgnoreCase( billPartyId) ) &&
                             ("T".equalsIgnoreCase( frm.getSameBillingFlag() )))
                    {
                        //  MAIL To and BILL To were same before and same now
                        //  Don't do anything
                        //  arUpdateAccount = null;

                        _logger.info( "\nMailTo Party ID == BillTo Party ID and STAYS the SAME" );
                    }

                    //  Update only RLink customer

                    ldapUser = ldapService.getUserByPartyId(
                        new LdapUserConsumerContext(), 
                        frm.getMailingPersonPartyId()
                    );

                    if (ldapUser.getRightsLinkPartyID() != null && ldapUser.getPartyID() != null)
                    {
                        if (ldapUser.getPartyID().equalsIgnoreCase( ldapUser.getRightsLinkPartyID() ))
                        {
                            cqService.updateLicensee(
                                new CQConsumerContext(), 
                                Source.COPYRIGHT_COM, 
                                frm.getUserName().trim(),
                                UserType.ORGANIZATION, 
                                true,
                                transTitle( frm.getTitle() ), 
                                frm.getFirstName(), 
                                frm.getMiddleName(),
                                frm.getLastName(), 
                                "", 
                                frm.getAddress1(), 
                                frm.getAddress2(),
                                frm.getAddress3(), 
                                "",
                                postalCode, 
                                frm.getState(),
                                frm.getCity(), 
                                "", 
                                frm.getCountry(), 
                                frm.getPhone(),
                                frm.getPhoneExtension(), 
                                frm.getFax(), 
                                frm.getBillingTitle().toUpperCase(), 
                                frm.getBillingFirstName(),
                                frm.getBillingMiddleName(), 
                                frm.getBillingLastName(), 
                                "",
                                frm.getBillingAddress1(), 
                                frm.getBillingAddress2(), 
                                frm.getBillingAddress3(), 
                                "", 
                                frm.getBillingZipcode(), 
                                frm.getBillingState(), 
                                frm.getBillingCity(), 
                                "",
                                frm.getBillingCountry(), 
                                frm.getBillingPhone(), 
                                frm.getBillingPhoneExtension(), 
                                frm.getBillingFax(), 
                                frm.getCompany(), 
                                frm.getDepartment(), 
                                frm.getJobTitle()
                            );
                        }
                    }

                    //  Update Account e-mail

                    String name = frm.getFirstName() + " " + frm.getLastName();
                    sendSetPasswordEmail( name, updateMap, ldapUser.getUsername().trim(), request );   
                }
                else    //  Create NEW User
                {
                    if (internalIP)
                    {
                        if (isIntranetEmailCheckEnable)
                        {
                            if (isEmailVerificationEnabled && !EmailVerificationService.verifyEmail( frm.getUserName().trim() ))
                            {
                                ActionMessages errors = new ActionMessages();
                                errors.add(
                                    ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage( "errors.email", "" )
                                );
                                saveErrors( request, errors );
                                return (mapping.findForward( _FAILURE ));
                            }
                        }
                    }
                    else
                    {
                        if (isEmailVerificationEnabled && !EmailVerificationService.verifyEmail( frm.getUserName().trim() ))
                        {
                            ActionMessages errors = new ActionMessages();
                            errors.add(
                                ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage( "errors.email", "" )
                            );
                            saveErrors( request, errors );
                            return (mapping.findForward( _FAILURE )); 
                        }
                    }

                    if (!ldapService.isValidUserName( new LdapUserConsumerContext(), frm.getUserName().trim() ))
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage( "errors.email", "" )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILURE ));            	  
                    }

                    if (!ldapService.isUsernameAvailable( new LdapUserConsumerContext(), frm.getUserName().trim() ))
                    {        		          		 
                        request.setAttribute( WebConstants.RequestKeys.EMAIL, frm.getUserName().trim() );
                        return (mapping.findForward( _DUPLICATE ));                        		        		 
                    }

                    //  Add to LDAP

                    String postalCode = "";

                    if ("US".equalsIgnoreCase( frm.getCountry() )) 
                    {
                        postalCode = frm.getZipcode() + frm.getZipPlus4();
                    }
                    else {
                        postalCode = frm.getZipcode();
                    }

                    LdapUser ldUser = 
                        createLdapUser( 
                            frm.getUserName().trim(), 
                            frm.getPassword(), 
                            com.copyright.svc.ldapuser.api.data.UserType.ORGANIZATION.getValue(), 
                            frm.getTitle(), 
                            frm.getFirstName(), 
                            frm.getMiddleName(), 
                            frm.getLastName(), 
                            frm.getDepartment(), 
                            frm.getAddress1(), 
                            frm.getAddress2(), 
                            frm.getAddress3(), 
                            frm.getCity(), 
                            postalCode, 
                            frm.getCountry(), 
                            frm.getState(), 
                            frm.getPhone(), 
                            frm.getFax(), 
                            frm.getCompany()  
                        );

                    //  Add to Party table

                    Long ptyInst = 
                        tfService.provisionIndividualAndOrganizationLicenseeTransactionalAccount(
                            new TFConsumerContext(), 
                            frm.getCompany(), 
                            "", 
                            "", 
                            frm.getFirstName(), 
                            "", 
                            frm.getLastName(), 
                            "", 
                            null, 
                            null, 
                            "WWW_USER" 
                        );

                    Party licenseeParty = 
                        tfService.getLicenseePartyByPtyInst( new TFConsumerContext(), ptyInst );

                    //  Update in LDAP

                    updateLdapUser( ldUser, licenseeParty.getPartyId() );

                    //  Provision in CC

                    createCCUser( ldUser );

                    Boolean billSame = false;

                    if ("T".equalsIgnoreCase( frm.getSameBillingFlag() ))
                    {
                        billSame = true;
                    }
                    else
                    {
                        billSame = false;
                    }

                    if (!billSame)  //  Provision Bill To in Party table
                    {
                        Long billPtyInst = 
                            tfService.addIndividualToOrganizationLicenseeTransactionalAccount(
                                new TFConsumerContext(), 
                                "", 
                                frm.getBillingFirstName(), 
                                "", 
                                frm.getBillingLastName(),
                                "", 
                                licenseeParty.getAccountNumber(), 
                                null, 
                                "WWW_USER"
                            );
                    }

                    String billingPostalCode = "";

                    if ("US".equalsIgnoreCase( frm.getBillingCountry() ))
                    {
                        billingPostalCode = frm.getBillingZipcode() + frm.getBillingZipPlus4();
                    }
                    else {
                        billingPostalCode = frm.getBillingZipcode();
                    }

                    cqService.createLicensee(
                        new CQConsumerContext(), 
                        Source.COPYRIGHT_COM, 
                        frm.getUserName().trim(), 
                        UserType.ORGANIZATION,
                        transTitle( frm.getTitle() ), 
                        frm.getFirstName(), 
                        frm.getMiddleName(),
                        frm.getLastName(), 
                        "", 
                        frm.getAddress1(), 
                        frm.getAddress2(),
                        frm.getAddress3(), 
                        "",
                        postalCode, 
                        frm.getState(),
                        frm.getCity(), 
                        "", 
                        frm.getCountry(), 
                        frm.getPhone(),
                        frm.getPhoneExtension(), 
                        frm.getFax(), 
                        billSame, 
                        transTitle( frm.getBillingTitle() ), 
                        frm.getBillingFirstName(),
                        frm.getBillingMiddleName(), 
                        frm.getBillingLastName(), 
                        "",
                        frm.getBillingAddress1(), 
                        frm.getBillingAddress2(), 
                        frm.getBillingAddress3(), 
                        "", 
                        billingPostalCode, 
                        frm.getBillingState(), 
                        frm.getBillingCity(), 
                        "",
                        frm.getBillingCountry(), 
                        frm.getBillingPhone(), 
                        frm.getBillingPhoneExtension(), 
                        frm.getBillingFax(), 
                        frm.getCompany(), 
                        frm.getDepartment(), 
                        frm.getJobTitle(), 
                        MarketSegment.valueOf( getSegment( frm.getPersonType() ) ), 
                        ""
                    );
                }
            }
            catch (EmailAddressNotUniqueException eanu)
            {
            _logger.error( ExceptionUtils.getFullStackTrace( eanu ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", eanu.getMessage() )
                );
                saveErrors( request, errors );
                request.setAttribute( WebConstants.RequestKeys.EMAIL, frm.getUserName().trim() );
                return (mapping.findForward( _DUPLICATE ));
            }
            catch ( ValidationException ve)
            {
            _logger.error( ExceptionUtils.getFullStackTrace( ve ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", ve.getMessage() )
                );
                saveErrors( request, errors );
                return (mapping.findForward( _FAILURE ));
            }
            catch (Exception e) 
            { 
                _logger.error( ExceptionUtils.getFullStackTrace( e ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", e.getMessage() )
                );
                saveErrors( request, errors );
                return (mapping.findForward( _FAILURE ));
            }
                        
            if ("UPDATE".equalsIgnoreCase(frm.getStatus()))
            {
                //  Update the user context

                long mainPartyId = Long.valueOf( frm.getMailingPersonPartyId() ).longValue();
                UserContextService.updateSharedUser( mainPartyId );

                //  Update the Always invoice me flag

                UserServices.updateCurrentUserAlwaysInvoice( "T".equalsIgnoreCase( frm.getAlwaysInvoiceFlag() ) );
                UserServices.updateCurrentUserAutoInvoiceSpecialOrder( "T".equalsIgnoreCase( frm.getAutoInvoiceSpecialOrderFlag() ) );

                //  MSJ 2008/01/24
                //  Add call to toggle the skip quickprice flag.

                request.setAttribute( WebConstants.RequestKeys.EMAIL, "" );
                return mapping.findForward( _SUCCESS );
            }
            else
            {
                LoginForm loginForm = new LoginForm();
                String _destination = "displayRegisterOrgView";

                //  2007/07/30  MSJ
                //  Instead of passing u/p in URL create login form,
                //  prepopulate and stuff in session.

                loginForm.setAutoLoginUsername( frm.getUserName().trim() );
                loginForm.setAutoLoginPassword( frm.getPassword() );
                loginForm.setAutoLoginPhase( LoginForm.AL_PHASE_ONE );
                loginForm.setAutoLoginForward( _destination );

                request.getSession().setAttribute( "loginForm", loginForm );
                request.setAttribute( "loginForm", loginForm );

                return mapping.findForward( "successAutoLogin" );
            }

        }
        else if (mapping.getPath().equalsIgnoreCase( "/registerAddUser" ))
        {
            //  **********************************************************
            //  ****              ADD ADDITIONAL USER                 ****
            //  **********************************************************

            RegisterAddUserForm frm = castForm( RegisterAddUserForm.class, form );

            try
            {
                if ("UPDATE".equalsIgnoreCase( frm.getStatus() ))
                {
                    ldapUser = 
                        ldapService.getUserByPartyId(
                            new LdapUserConsumerContext(), 
                            frm.getMailingPersonPartyId()
                        );

                    rightLnkPartyId = ldapUser.getRightsLinkPartyID();

                    if (!SystemStatus.isTelesalesUp())
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage( "errors.registrationError", "Update not possible at this time. Please try again later." )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILADDUSR ));
                    }

                    if (StringUtils.isNotEmpty( rightLnkPartyId ) && !SystemStatus.isRightslinkUp())
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage( "errors.registrationError", "Update not possible at this time. Please try again later." )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILURE ));
                    }

                    //  Mail Person for Update Comparison

                    Person mailPerson = 
                        telesalesService.getPersonByPartyId(
                            new TelesalesServiceConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Bill To Info for Update Comparison

                    ARAccount arUpdateAccount = 
                        telesalesService.getARAccountByARAccountNumber(
                            new TelesalesServiceConsumerContext(), 
                            frm.getAccount(), 
                            UserTypeEnum.ORGADD
                        );

                    //  update rights link

                    if (StringUtils.isNotEmpty( rightLnkPartyId ))
                    {
                        RlnkUser rlnkaddUser = null;

                        rlnkaddUser = rlUserService.getUserByPartyID( Long.valueOf( rightLnkPartyId ) );
                        rlnkaddUser.setTaxId( frm.getTaxId() );

                        rlUserService.updateUser( rlnkaddUser );
                    }

                    //  Rebuild the mailperson for Updates comparison

                    mailPerson.getName().setPrefix( transTitle( frm.getTitle() ) );
                    mailPerson.getName().setFirstName( frm.getFirstName() );
                    mailPerson.getName().setMiddleName( frm.getMiddleName() );
                    mailPerson.getName().setLastName( frm.getLastName() );

                    for (int k = 0; k < mailPerson.getContactPoints().size(); k++)              	  		              
                    {
                        if (mailPerson.getContactPoints().get( k ).getPhoneLineType() != null)
                        {
                            if (mailPerson.getContactPoints().get( k ).getPhoneLineType().equals( PhoneLineTypeEnum.GENERAL ))  
                            {
                                mailPerson.getContactPoints().get( k ).setDisplayPhone( frm.getPhone() );
                                //mailPerson.getContactPoints().get(k).setPhoneNumber(frm.getPhone());
                                mailPerson.getContactPoints().get( k ).setPhoneExtension( frm.getPhoneExtension() );
                            }
                            else if (mailPerson.getContactPoints().get( k ).getPhoneLineType().equals( PhoneLineTypeEnum.FAX ))
                            {
                                //mailPerson.getContactPoints().get(k).setPhoneNumber(frm.getFax());
                                currentFax = mailPerson.getContactPoints().get( k ).getPhoneNumber();
                                mailPerson.getContactPoints().get( k ).setDisplayPhone( frm.getFax() );
                            }
                        }
                    }

                    int j = 0;

                    for (Classification classification : mailPerson.getClassifications())
                    {
                        if (classification.getCategory().equals( CustomerCategoryEnum.MARKETSEGMENT ))
                        {
                            mailPerson.getClassifications().get(j)
                                .setCode(getClassCode( frm.getPersonType().toLowerCase() ));
                        }
                        j = j + 1;
                    }

                    Map<String, String> updateMap = 
                        telesalesService.compareContactAndBillingInformation(
                            new TelesalesServiceConsumerContext(), 
                            mailPerson, 
                            arUpdateAccount
                        );

                    Party pty = 
                        tfService.getIndLicenseePartyByPartyId(
                            new TFConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Update LDAP

                    updateLdapUser(
                        frm.getUserName().trim(), 
                        frm.getTitle(), 
                        frm.getFirstName(), 
                        frm.getMiddleName(),
                        frm.getLastName(), 
                        "", "", "", "", "", 
                        "", "", "", "", "", 
                        frm.getPhone(), 
                        frm.getFax(), 
                        frm.getMailingPersonPartyId(), 
                        frm.getAccount(), 
                        com.copyright.svc.ldapuser.api.data.UserType.ORG_ADD.getValue()
                    );

                    //  Update Party table

                    updateIndParty(
                        pty.getPtyInst(), 
                        frm.getFirstName(), 
                        frm.getMiddleName(), 
                        frm.getLastName(), 
                        pty.getAccountNumber(), 
                        pty.getPartyId()
                    );

                    Person person = 
                        telesalesService.getPersonByPartyId(
                            new TelesalesServiceConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getMailingPersonPartyId() )
                        );

                    //  Update Market segment

                    for (Classification classification : person.getClassifications())
                    {
                        if (classification.getCategory().equals( CustomerCategoryEnum.MARKETSEGMENT ))
                        {
                            telesalesService.updateCodeAssignment(
                                new TelesalesServiceConsumerContext(),
                                classification.getId(), 
                                getMktSegment( frm.getPersonType() ), 
                                classification.getVersion()
                            );
                        }
                    }

                    //	Update Person name

                    telesalesService.updatePersonName(
                        new TelesalesServiceConsumerContext(),
                        person.getPartyId(), 
                        person.getPartyVersion(), 
                        transTitle( frm.getTitle() ),
                        "",
                        frm.getFirstName(),
                        frm.getMiddleName(),
                        frm.getLastName()
                    );        	  	

                    //	Update Phone/Fax

                    for (ContactPoint contactPoint: person.getContactPoints())
                    {
                        if (contactPoint.getPhoneLineType() != null)
                        {
                            if (contactPoint.getPhoneLineType().equals( PhoneLineTypeEnum.GENERAL ))
                            {
                                contactPoint.setPhoneNumber( frm.getPhone() );
                                contactPoint.setPhoneExtension( frm.getPhoneExtension() );

                                telesalesService.updateContactPoint(
                                    new TelesalesServiceConsumerContext(), 
                                    person.getPartyId(), 
                                    person.getRelPartyId(), 
                                    "ZZ", 
                                    contactPoint
                                );
                            }
                            else if (contactPoint.getPhoneLineType().equals( PhoneLineTypeEnum.FAX ))
                            {
                                if (frm.getFax() == null || "".equals( frm.getFax() ))
                                {
                                    telesalesService.inactivateContactPoint(
                                        new TelesalesServiceConsumerContext(), 
                                        person.getPartyId(), 
                                        contactPoint.getRelPartyId(), 
                                        "ZZ", 
                                        contactPoint
                                    );
                                }
                                else 
                                {
                                    contactPoint.setPhoneNumber( frm.getFax() );

                                    telesalesService.updateContactPoint(
                                        new TelesalesServiceConsumerContext(), 
                                        person.getPartyId(), 
                                        person.getRelPartyId(), 
                                        "ZZ", 
                                        contactPoint
                                    );
                                }
                            }
                        }
                    }

                    //  Check if the Fax Number is empty before and not empty now.
                    //  In this case, we need to create the new fax number.

                    if (currentFax == null && !frm.getFax().isEmpty())
                    {
                        //Create NEW Fax

                        telesalesService.createPhoneFaxNumber(
                            new TelesalesServiceConsumerContext(),
                            person.getRelPartyId(), 
                            ContactPointPurposeEnum.NONE, 
                            "", 
                            "", 
                            frm.getFax(), 
                            "", 
                            PhoneLineTypeEnum.FAX, 
                            false, 
                            ChannelSourceTypeEnum.COPYRIGHTCOM
                        );
                        updateMap.put( "Fax Number", frm.getFax() );
                    }

                    //  Update only RLink customer

                    ldapUser = 
                        ldapService.getUserByPartyId( 
                            new LdapUserConsumerContext(), 
                            frm.getMailingPersonPartyId()
                        );

                    if (ldapUser.getRightsLinkPartyID() != null && ldapUser.getPartyID() != null)
                    {
                        if (ldapUser.getPartyID().equalsIgnoreCase(ldapUser.getRightsLinkPartyID()))
                        {
                            cqService.updateLicensee(
                                new CQConsumerContext(), 
                                Source.COPYRIGHT_COM, 
                                frm.getUserName().trim(), 
                                UserType.ORG_ADD, 
                                true,
                                transTitle( frm.getTitle() ), 
                                frm.getFirstName(), 
                                frm.getMiddleName(),
                                frm.getLastName(), 
                                "", "", "", "", "",
                                "", "", "", "", "", 
                                frm.getPhone(),
                                frm.getPhoneExtension(), 
                                frm.getFax(), 
                                "", "", "", "", "", 
                                "", "", "", "", "", 
                                "", "", "", "", "", 
                                "", "", "", "", 
                                frm.getTitle()
                            );
                        }
                    }

                    //  Update Account e-mail

                    String name = frm.getFirstName() + " " + frm.getLastName();
                    sendSetPasswordEmail( name, updateMap, ldapUser.getUsername().trim(), request );
                }
                else    //  Create NEW User
                {
                    if (internalIP)
                    {
                        if (isIntranetEmailCheckEnable)
                        {
                            if (isEmailVerificationEnabled && !EmailVerificationService.verifyEmail( frm.getUserName().trim() ))
                            {
                                ActionMessages errors = new ActionMessages();
                                errors.add(
                                    ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage( "errors.email", "" )
                                );
                                saveErrors( request, errors );
                                return (mapping.findForward( _FAILADDUSR ));
                            }
                        }
                    }
                    else
                    {
                        if(isEmailVerificationEnabled && !EmailVerificationService.verifyEmail( frm.getUserName().trim() ))
                        {
                            ActionMessages errors = new ActionMessages();
                            errors.add(
                                ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage( "errors.email", "" )
                            );
                            saveErrors( request, errors );
                            return (mapping.findForward( _FAILADDUSR ));
                        }
                    }

                    if (!ldapService.isValidUserName( new LdapUserConsumerContext(), frm.getUserName().trim() ))
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage( "errors.email", "" )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILURE ));            	  
                    }

                    if (!ldapService.isUsernameAvailable( new LdapUserConsumerContext(), frm.getUserName().trim() ))
                    {        		          		 
                        request.setAttribute( WebConstants.RequestKeys.EMAIL, frm.getUserName().trim() );
                        return (mapping.findForward( _DUPLICATE ));                          		        		 
                    }

                    /* we have to do this to provision the Add User functionality even when 
                    * Telesales is DOWN.
                    * 
                    * 
                    * 
                    */
                    ArAccountInfo arAccount = 
                        tfService.getArAccountInfoByAccount(
                            new TFConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getAccount() )
                        );

                    Party orgParty = 
                        tfService.getOrgLicenseePartyByAccountNumber(
                            new TFConsumerContext(), 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getAccount() )
                        );

                    if (arAccount == null || orgParty == null)
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage(
                                "errors.registrationError", 
                                "You have entered either an invalid Account Number or Primary Contact Last Name." )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILADDUSR ));
                    }

                    Long adminPartyId = arAccount.getAdminPartyId();
                    Long orgPartyId = orgParty.getPartyId();

                    Boolean isInd = true;

                    if (adminPartyId != null)   //  Assumption --> AdminPartyId NULL --> IND
                    {
                        if (adminPartyId.equals( orgPartyId ))
                        {
                            isInd = true; 
                        }
                        else {
                            isInd = false;
                        }
                    }

                    if (isInd)
                    {
                        ActionMessages errors = new ActionMessages();
                        errors.add(
                            ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage(
                                "errors.registrationError", 
                                "This account was setup as an individual account when you registered. " +
                                "You may only add additional users to an Organizational account." )
                        );
                        saveErrors( request, errors );
                        return (mapping.findForward( _FAILADDUSR ));
                    }

                    Party adminParty = 
                        tfService.getIndPartyAndTypeByPartyIdAndAccount(
                            new TFConsumerContext(), 
                            adminPartyId, 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getAccount() )
                        );

                    String adminLastName = adminParty.getLastName();

                    if (!adminLastName.equalsIgnoreCase( frm.getMainContactLastName().trim() ))
                    {
                        if (!isInd)     //  ORG Account
                        {
                            ActionMessages errors = new ActionMessages();
                            errors.add(
                                ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage(
                                    "errors.registrationError", 
                                    "You have entered either an invalid Account Number or Primary Contact Last Name." )
                            );
                            saveErrors( request, errors );
                            return (mapping.findForward( _FAILADDUSR ));
                        }
                        else            //  INDIVIDUAL Account
                        {
                            ActionMessages errors = new ActionMessages();
                            errors.add(
                                ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage(
                                    "errors.registrationError", 
                                    "This account was setup as an individual account when you registered. " +
                                    "You may only add additional users to an Organizational account." )
                            );
                            saveErrors( request, errors );
                            return (mapping.findForward( _FAILADDUSR ));
                        }
                    }

                    //  Add to LDAP

                    LdapUser ldUser = 
                        createLdapUser(
                            frm.getUserName().trim(), 
                            frm.getPassword(), 
                            com.copyright.svc.ldapuser.api.data.UserType.ORG_ADD.getValue(), 
                            frm.getTitle(), 
                            frm.getFirstName(), 
                            frm.getMiddleName(), 
                            frm.getLastName(), 
                            "", "", "", "", "", "", "", "", 
                            frm.getPhone(), 
                            frm.getFax(), 
                            ""  
                        );

                    //  Add to Party table

                    Long ptyInst = 
                        tfService.addIndividualToOrganizationLicenseeTransactionalAccount(
                            new TFConsumerContext(), 
                            "", 
                            frm.getFirstName(), 
                            "", 
                            frm.getLastName(),
                            "", 
                            org.apache.commons.lang.math.NumberUtils.toLong( frm.getAccount() ), 
                            null, 
                            "WWW_USER"
                        );

                    Party licenseeParty = 
                        tfService.getLicenseePartyByPtyInst( new TFConsumerContext(), ptyInst );

                    //  Update in LDAP

                    updateLdapUser( ldUser, licenseeParty.getPartyId() );

                    //  Provision in CC

                    createCCUser( ldUser );

                    cqService.createLicensee(
                        new CQConsumerContext(), 
                        Source.COPYRIGHT_COM, 
                        frm.getUserName().trim(), 
                        UserType.ORG_ADD,
                        transTitle( frm.getTitle() ), 
                        frm.getFirstName(), 
                        frm.getMiddleName(),
                        frm.getLastName(), 
                        "", "", "", "", "",
                        "", "", "", "", "", 
                        frm.getPhone(),
                        frm.getPhoneExtension(), 
                        frm.getFax(), 
                        false, 
                        "", "", "", "", "",
                        "", "", "", "", "", 
                        "", "", "", "", "", 
                        "", "", "", "", "",
                        MarketSegment.valueOf( getSegment( frm.getPersonType() ) ), 
                        frm.getAccount()
                    );
                }


            }
            catch (EmailAddressNotUniqueException eanu)
            {
                _logger.error( ExceptionUtils.getFullStackTrace( eanu ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", eanu.getMessage() )
                );
                saveErrors( request, errors );
                request.setAttribute( WebConstants.RequestKeys.EMAIL, frm.getUserName().trim() );
                return (mapping.findForward( _DUPLICATE ));
            }
            catch (ValidationException ve)
            {
                _logger.error( ExceptionUtils.getFullStackTrace( ve ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", ve.getMessage() )
                );
                saveErrors( request, errors );
                return (mapping.findForward( _FAILADDUSR ));
            }
            catch (Exception e) 
            { 
                _logger.error( ExceptionUtils.getFullStackTrace( e ) );

                ActionMessages errors = new ActionMessages();
                errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "errors.registrationError", e.getMessage() )
                );
                saveErrors( request, errors );
                return (mapping.findForward( _FAILADDUSR ));
            }

            if ("UPDATE".equalsIgnoreCase( frm.getStatus() ))
            {
                //  Update the user context

                long mainPartyId = Long.valueOf( frm.getMailingPersonPartyId() ).longValue();
                UserContextService.updateSharedUser( mainPartyId );

                //  Update the Always invoice me flag

                UserServices.updateCurrentUserAlwaysInvoice( "T".equalsIgnoreCase( frm.getAlwaysInvoiceFlag() ) );
                UserServices.updateCurrentUserAutoInvoiceSpecialOrder( "T".equalsIgnoreCase( frm.getAutoInvoiceSpecialOrderFlag() ) );

                //  MSJ 2008/01/24
                //  Add call to toggle the skip quickprice flag.

                request.setAttribute( WebConstants.RequestKeys.EMAIL, "" );
                return mapping.findForward( _SUCCESS );
            }
            else
            {
                if (!UserContextService.isUserAuthenticated())  //User not logged in
                {
                    LoginForm loginForm = new LoginForm();
                    String _destination = "displayRegisterView";

                    //  2007/07/30  MSJ
                    //  Instead of passing u/p in URL create login form,
                    //  prepopulate and stuff in session.

                    loginForm.setAutoLoginUsername( frm.getUserName().trim() );
                    loginForm.setAutoLoginPassword( frm.getPassword() );
                    loginForm.setAutoLoginForward( _destination );

                    request.getSession().setAttribute( "loginForm", loginForm );
                    request.setAttribute( "loginForm", loginForm );

                    return mapping.findForward( "successAutoLogin" );
                }
                else
                {
                    request.setAttribute( WebConstants.RequestKeys.EMAIL, frm.getUserName().trim() );
                    return mapping.findForward( _SUCCESS ) ;
                }
            }
        }
        return mapping.findForward( _SUCCESS );
    }

    private MarketSegmentEnum getMktSegment( String type )
    {
        if (type.equalsIgnoreCase( "academic user" ) || type.equalsIgnoreCase( "academic" )) 
        {
            return MarketSegmentEnum.ACADEMICUSER;
        }
        else if (type.equalsIgnoreCase( "business user" ) || type.equalsIgnoreCase( "business" ))
        {
            return MarketSegmentEnum.BUSINESSUSER;
        }
        else if (type.equalsIgnoreCase( "author or creator" )) 
        {
            return MarketSegmentEnum.AUTHORORCREATOR;
        }
        else if (type.equalsIgnoreCase( "publisher" )) 
        {
            return MarketSegmentEnum.PUBLISHER;
        }
        else if (type.equalsIgnoreCase( "other" ))
        {
            return MarketSegmentEnum.OTHER;
        }
        else {
            return MarketSegmentEnum.OTHER;
        }
    }

    private LdapUser createLdapUser( 
        String userName, String pw, String userType , String prefix, 
        String firstName, String middleName, String lastName, String dept, String mailLine1,
        String mailLine2, String mailLine3, String mailCity, String mailPostalCode, 
        String mailCountryCode, String mailState, String mailPhone, String mailFax, String orgName )
    {
        LdapUser ldapUser = new LdapUser();

        ldapUser.setUsername( userName );
        ldapUser.setEmail( userName );
        ldapUser.setPassword( pw );
        ldapUser.setUserType( userType );
        ldapUser.setPrefix( prefix );
        ldapUser.setFirstName( firstName );
        ldapUser.setMiddleName( middleName );
        ldapUser.setLastName( lastName );
        ldapUser.setDepartmentName( dept );
        ldapUser.setMailToLine1( mailLine1 );
        ldapUser.setMailToLine2( mailLine2 );
        ldapUser.setMailToLine3( mailLine3 );
        ldapUser.setMailToCity( mailCity );
        ldapUser.setMailToPostalCode( mailPostalCode );
        ldapUser.setMailToCountryCode( mailCountryCode );
        ldapUser.setMailToStateProvince( mailState );
        ldapUser.setMailToPhoneNumber( mailPhone );
        ldapUser.setMailToFaxNumber( mailFax );
        ldapUser.setOrganizationName( orgName );

        LdapUserService ldapService = ServiceLocator.getLdapUserService();

        ldapService.createUser( new LdapUserConsumerContext(), ldapUser );
        ldapService.addUserToGroup( new LdapUserConsumerContext(), userName, CCC_CCC );

        return ldapService.getUser( new LdapUserConsumerContext(), userName );
    }

    private void updateLdapUser( LdapUser ldapUser, Long interimPartyId )
    {
        //  Update in LDAP

        ldapUser.setPartyID( String.valueOf( interimPartyId ) );
        LdapUserService ldapService =  ServiceLocator.getLdapUserService();
        ldapService.updateUser( new LdapUserConsumerContext(), ldapUser );
    }

    private void updateLdapUser(
        String userName, String prefix, String firstName, String middleName,
        String lastName, String orgName, String departmentName, String mailToLine1, String mailToLine2,
        String mailToLine3, String mailToLine4, String mailToCity, String mailToStateProvince, 
        String mailToPostalCode, String mailToCountryCode, String mailToPhoneNumber, 
        String mailToFaxNumber, String partyID, String arAccountNumber, 
        String userType )
    {
        LdapUserService ldapService =  ServiceLocator.getLdapUserService();

        //  ldapService.updateUser(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21)
        
        ldapService.updateUser( 
            new LdapUserConsumerContext(), 
            userName, 
            prefix, 
            firstName, 
            middleName, 
            lastName, 
            orgName, 
            departmentName, 
            mailToLine1, 
            mailToLine2, 
            mailToLine3, 
            mailToLine4, 
            mailToCity, 
            mailToStateProvince, 
            mailToPostalCode, 
            mailToCountryCode, 
            mailToPhoneNumber, 
            mailToFaxNumber, 
            userName, 
            partyID, 
            null, 
            userType 
        );
    }

    //  Updates a record in ccc_party table with org_type = I

    private void updateIndParty( 
        Long ptyInst, String firstName, String middleName, 
        String lastName, Long acct, Long ptyId )
    {
        TFService tfService = ServiceLocator.getTFService();

        tfService.updateIndividualParty( 
            new TFConsumerContext(), 
            ptyInst, 
            "", 
            firstName, 
            middleName, 
            lastName, 
            "", 
            acct, 
            ptyId, 
            "WWW_USER"
        );
    }

    private void createCCUser( LdapUser ldUser )
    {
        CCUser newCCUser = null;

        String auid = CookieUtils.generateAUID( 0 );

        newCCUser = UserServices.createCCUser();

        newCCUser.setUsername( ldUser.getUsername().trim() );
        newCCUser.setAuid( auid );
        newCCUser.setPartyID( org.apache.commons.lang.math.NumberUtils.toLong( ldUser.getPartyID() ) );

        UserServices.save( newCCUser );
    }

    private ClassCodeEnum getClassCode( String type )
    {
        if (type.equalsIgnoreCase( "academic user" )) 
        {
            return ClassCodeEnum.ACADEMIC;
        }
        else if (type.equalsIgnoreCase( "business user" )) 
        {
            return ClassCodeEnum.BUSINESS;
        }
        else if (type.equalsIgnoreCase( "author or creator" )) 
        {
            return ClassCodeEnum.AUTHORORCREATOR;
        }
        else if (type.equalsIgnoreCase( "publisher" )) 
        {
            return ClassCodeEnum.PUBLISHER;
        }
        else if (type.equalsIgnoreCase( "other" )) 
        {
            return ClassCodeEnum.OTHER;
        }
        else {
            return ClassCodeEnum.OTHER;
        }	  	  
    }

    private String getSegment( String type )
    {
        if (type.equalsIgnoreCase( "academic user" )) 
        {
            return "ACADEMIC_USER";
        }
        else if (type.equalsIgnoreCase( "business user" )) 
        {
            return "BUSINESS_USER";
        }
        else if (type.equalsIgnoreCase( "author or creator" )) 
        {
            return "AUTHOR_OR_CREATOR";
        }
        else if (type.equalsIgnoreCase( "publisher" )) 
        {
            return "PUBLISHER";
        }
        else if (type.equalsIgnoreCase( "other" )) 
        {
            return "OTHER";
        }
        else {
            return "OTHER";
        }
    }

    private String transTitle( String ttl )
    {
        if (ttl.equalsIgnoreCase( "Mr." ))
        { 
            return "MR.";
        }
        else if (ttl.equalsIgnoreCase( "Ms." ))
        {
            return "MS.";
        }
        else if (ttl.equalsIgnoreCase( "Miss" ))
        {
            return "MISS";
        }
        else if (ttl.equalsIgnoreCase( "Mrs." ))
        {
            return "MRS.";
        }
        else if (ttl.equalsIgnoreCase( "Dr." ))
        {
            return "DR.";
        }
        else if (ttl.equalsIgnoreCase( "Prof." ))
        {
            return "PROF";
        }
        else if (ttl.equalsIgnoreCase( "Sir" ))
        {
            return "SIR";
        }
        else
        {
            return ttl;
        }
    }

    private void sendSetPasswordEmail( 
        String personName, 
        Map<String,String> updMap, 
        String userName, 
        HttpServletRequest request ) 
    {
        String userDisplayName = personName;

        CommunicationsServiceAPI commService = CommunicationsServiceFactory.getInstance().getService();
        ActionMessages ae = new ActionMessages();

        try
        {
            commService.sendUpdateAccountEmail( "copyright.com", userDisplayName, userName, updMap );
        }
        catch (MailSendFailedException esfe)
        {
            ae.add(
                ActionMessages.GLOBAL_MESSAGE, 
                new ActionMessage( "errors.personNotFound", personName )
            );
            _logger.error( ExceptionUtils.getFullStackTrace( esfe ) );
            addErrors( request,  ae );
        }
    }
}