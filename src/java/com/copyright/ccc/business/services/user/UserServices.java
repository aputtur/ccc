package com.copyright.ccc.business.services.user;

//  New services references.

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.data.access.CC2DataAccessConstants;
import com.copyright.ccc.business.data.access.CC2OracleProcedureInvokerFactory;
import com.copyright.ccc.business.data.access.OracleServicesBase;
import com.copyright.ccc.business.security.CookieUtils;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.util.LogUtil;
import com.copyright.data.security.Privilege;
import com.copyright.data.security.Role;
import com.copyright.internal.security.AccessControlService;
import com.copyright.opi.InvocationExpectedEventRuntimeException;
import com.copyright.opi.SQLUtils;
import com.copyright.opi.SingleDTOProcedureInvoker;
import com.copyright.opi.TypedParameter;
import com.copyright.opi.data.ModState;
import com.copyright.opi.data.ReturnCodes;
import com.copyright.persist.security.AccessControlPersistence;
import com.copyright.svc.centralQueue.api.CentralQueueService;
import com.copyright.svc.centralQueue.api.data.CQConsumerContext;
import com.copyright.svc.centralQueue.api.data.Source;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.ldapuser.api.data.UserNameNotFoundException;
import com.copyright.svc.telesales.api.data.ContactRole;
import com.copyright.svc.telesales.api.data.RightsholderOrganization;
import com.copyright.svc.telesales.api.data.RoleTypeEnum;
import com.copyright.svc.telesales.api.data.TelesalesServiceConsumerContext;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.ArAccountInfo;
import com.copyright.svc.tf.api.data.Party;
import com.copyright.svc.tf.api.data.TFConsumerContext;

/**
 * This is a CCC "Service" class in the true sense of the word.  It exposes
 * a collection of static methods for performing CRUD operations on objects
 * related to a particular functional slice of the app.
 *
 * The definition is perhaps a bit stretched in that this class has methods for
 * accessing and persisting Shared Services "User" objects, via the Shared
 * Service <code>UserServiceAPI</code>, so some methods here do not use the
 * OPI framework directly.  Also, some methods interact with the <code>
 * UserContext</code> to provide conveniences for developers working on other
 * services that need to update the current User object both in the user context
 * and the persistence mechanism simultaneously.
 *
 * There are four categories of methods here:
 *
 * 1.  CCUser related methods - methods for fetching, updating, creating
 *     instances of the application-specific user object, CCUser
 *
 * 2.  Shared User Methods - methods for fetching and updating instances
 *     of the Shared Services User object
 *
 * 3.  Convenience methods for other services, for updating various fields
 *     of the CCUser object and ensuring the changes are persisted both
 *     in the database and in the current <code>UserContext</code>
 *
 * 4.  Role related methods - methods for fetching and updating user roles
 */
public final class UserServices extends OracleServicesBase
{
    static Logger _logger = Logger.getLogger( UserServices.class );


    /*x*********************************************
     *  CC USER RELATED METHODS
     * *********************************************/


    /**
     * Returns a newly-created <code>CCUser</code> and seeds fields with
     * defaults and values that need to be created for new user objects
     */
    public static CCUser createCCUser()
    {
        return new CCUserImpl();
    }

    public static CCUser createCCUser( User sharedUser )
    {
        if ( sharedUser == null )
            throw new NullArgumentException( "sharedUser" );

        CCUser ccUser = createCCUser();

        ccUser.setUsername( sharedUser.getUsername() );
        ccUser.setPartyID( sharedUser.getPartyId().longValue() );
        ccUser.setPK( sharedUser.getPK() );

        return ccUser;
    }

    /**
     * Returns the <code>CC2User</code> for the specified <code>AUID</code>.
     */
    public static CCUser getCCUserForAUID( String auid )
    {
        validateArgument( auid, "auid" );

        TypedParameter[] inputParameters = {
            new TypedParameter( auid )
        };

        SingleDTOProcedureInvoker invoker =
            CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
        invoker.configure(
            CC2DataAccessConstants.CCUser.READ_FOR_AUID_QUERY, CCUserDTO.getRefInstance(),
            inputParameters
        );

        invoker.setNoDataFoundAcceptable( true );
        invoker.invoke();

        CCUserImpl user = (CCUserImpl) invoker.getDTO();

        return user;
    }

    /**
     * Returns the <code>CC2User</code> for the specified <code>AUID</code>.
     */
    public static CCUser getCCUserForNewSession( String auid )
    {
        validateArgument( auid, "auid" );

        TypedParameter[] inputParameters = {
            new TypedParameter( auid )
        };

        SingleDTOProcedureInvoker invoker =
            CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
        invoker.configure(
            CC2DataAccessConstants.CCUser.READ_FOR_NEW_SESSION_QUERY, CCUserDTO.getRefInstance(),
            inputParameters
        );

        invoker.setNoDataFoundAcceptable( false );

        invoker.invoke();

        CCUserImpl user = ( CCUserImpl ) invoker.getDTO();

        return user;
    }

    /**
     * Returns the <code>CC2User</code> for the specified <code>username</code>
     *
     */
    public static Boolean isUserNameInCCUser( String username )
    {
    	validateArgument( username, "user_identifier" );

        TypedParameter[] inputParameters = {
            new TypedParameter( username )
        };

        SingleDTOProcedureInvoker invoker =
            CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
        invoker.configure(
            CC2DataAccessConstants.CCUser.READ_FOR_IDENTIFIER_QUERY, CCUserDTO.getRefInstance(),
            inputParameters
        );

        invoker.setNoDataFoundAcceptable( true );

        invoker.invoke();

        //CCUserImpl user = ( CCUserImpl ) invoker.;

        //return user;

        ReturnCodes ret = invoker.getReturnCodes();


        if (ret.getAffectedRows() == 0)
        {
        	return false;
        }
        else
        {
        	return true;
        }

    }

    /**
     * Checks if the User is a Copyright.com User
     *
     */
    public static Boolean isCCUser( String userName )
    {
    	try {
    		LdapUser ldapUser = ServiceLocator.getLdapUserService().getUser(new LdapUserConsumerContext(), userName);

    		if ( (ldapUser.getPartyID() != null) && (org.apache.commons.lang.math.NumberUtils.toLong(ldapUser.getPartyID()) > 0 ))
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	catch (UserNameNotFoundException ex) {
    		return false;
    	}

    }

    /**
     * Checks if the User is a RightsLink User
     *
     */
    public static Boolean isRLUser( String userName )
    {
    	try {

    		LdapUser ldapUser = ServiceLocator.getLdapUserService().getUser(new LdapUserConsumerContext(), userName);

    		if ( (ldapUser.getRightsLinkPartyID() != null) && (org.apache.commons.lang.math.NumberUtils.toLong(ldapUser.getRightsLinkPartyID()) > 0 ))
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	catch (UserNameNotFoundException ex) {
    		return false;
    	}

    }

    /**
     * Creates RL User entry in ccc_user table
     *
     */
    public static CCUser provisionCCUserForRLUser(String userName)
    {
    	LdapUser ldUser = ServiceLocator.getLdapUserService().getUser(new LdapUserConsumerContext(), userName);

    	CCUser newCCUser = null;

  	  	String auid = CookieUtils.generateAUID( 0 );

  	  	newCCUser = UserServices.createCCUser();

  	  	newCCUser.setUsername(userName);
  	  	newCCUser.setAuid( auid );
  	  	newCCUser.setPartyID(org.apache.commons.lang.math.NumberUtils.toLong(ldUser.getRightsLinkPartyID()));

  	  	newCCUser =
  	  		UserServices.save( newCCUser );
  	  	return newCCUser;

    }

    /**
     * Creates CCC User entry in ccc_user table
     *
     */
    public static CCUser provisionCCUser(String userName)
    {

    	LdapUser ldUser = ServiceLocator.getLdapUserService().getUser(new LdapUserConsumerContext(), userName);

    	CCUser newCCUser = null;

  	  	String auid = CookieUtils.generateAUID( 0 );

  	  	newCCUser = UserServices.createCCUser();

  	  	newCCUser.setUsername(userName);
  	  	newCCUser.setAuid( auid );
  	  	newCCUser.setPartyID(org.apache.commons.lang.math.NumberUtils.toLong(ldUser.getPartyID()));

  		UserServices.save( newCCUser );
  		return newCCUser;

    }

    /**
     * Creates RightsLink User provision
     *
     */
    public static void provisionRLUser( String userName )
    {
    	LdapUser ldUser = ServiceLocator.getLdapUserService().getUser(new LdapUserConsumerContext(), userName);

    	CentralQueueService cqService = ServiceLocator.getCentralQueueService();

    	TFService tfService = ServiceLocator.getTFService();

  	  	//Provision ccc_party table
  	  	if (ldUser.getUserType().equalsIgnoreCase(com.copyright.svc.ldapuser.api.data.UserType.INDIVIDUAL.getValue()))
  	  	{
 	  		tfService.provisionIndividualAndIndividualLicenseeTransactionalAccount(
    			  new TFConsumerContext(), "", ldUser.getFirstName(), "", ldUser.getLastName(), "", null,
    			  org.apache.commons.lang.math.NumberUtils.toLong(ldUser.getRightsLinkPartyID()), "WWW_USER");

  	  	cqService.addLicenseeService(new CQConsumerContext(), Source.COPYRIGHT_COM, userName,
  				com.copyright.svc.centralQueue.api.data.UserType.INDIVIDUAL, "", "", "", "",
  									"", "", "", "", "", "", "", "", "", "", "", "", "", "");
  	  	}
  	  	else
  	  	{
  	  		tfService.provisionIndividualAndOrganizationLicenseeTransactionalAccount(
    			new TFConsumerContext(), ldUser.getOrganizationName(), "", "", ldUser.getFirstName(), "",
    			ldUser.getLastName(), "", null, org.apache.commons.lang.math.NumberUtils.toLong(ldUser.getRightsLinkPartyID()), "WWW_USER" );

  	  	cqService.addLicenseeService(new CQConsumerContext(), Source.COPYRIGHT_COM, userName,
  				com.copyright.svc.centralQueue.api.data.UserType.ORGANIZATION, "", "", "", "",
  									"", "", "", "", "", "", "", "", "", "", "", "", "", "");
  	  	}



    }

    /**
     * Returns the <code>CC2User</code> for the specified <code>username</code>, merged
     * with the field values in User object currently in context.
     */
    public static CCUser mergeUserWithContext( String username, CCUser ccUser )
    {
        //CCUser exampleUser = UserServices.createCCUser();

        //CCUser exampleUser = ccUser;
        String ccUserUsername = ccUser.getUsername();
        ccUser.setUsername( username );

        //exampleUser.setUsername( username );
        //exampleUser.setAuid( ccUser.getAuid() );

        validateArgument( ccUser, "contextUser" );

        CCUserImpl userImpl = ( CCUserImpl ) ccUser;

        userImpl = ( CCUserImpl ) SQLUtils.ensureUnderlyingDTO( userImpl, CCUserDTO.class );

        TypedParameter[] inputParameters = {
            new TypedParameter( userImpl )
        };

        SingleDTOProcedureInvoker invoker =
            CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
        invoker.configure(
            CC2DataAccessConstants.CCUser.READ_FOR_LOGIN_USERNAME_QUERY, CCUserDTO.getRefInstance(),
            inputParameters
        );

        invoker.setNoDataFoundAcceptable( false );

        invoker.invoke();

        CCUserImpl retrievedUser = ( CCUserImpl ) invoker.getDTO();

        if ( retrievedUser.getLastSessionStart() == null )
            retrievedUser.setLastSessionStart( new Date() );

        ccUser.setUsername( ccUserUsername );

        return retrievedUser;
    }


    /**
     * Saves the specified <code>CCUser</code>.
     */
    public static CCUser save( CCUser user )
    {
        validateArgument( user, "user" );

        CCUserImpl userImpl = (CCUserImpl)user;

        userImpl = (CCUserImpl) SQLUtils.ensureUnderlyingDTO( userImpl, CCUserDTO.class );

        userImpl.setModUserID( 100 );

        try
        {
            /*
             * TODO:
             * CC attempts to generate a new AUID for the user here if
             * ModState is NEW.  We use different logic for determining whether
             * or not to generate an AUID.  Should we look at this way of doing
             * it as a cleaner solution.
             */
            CCUser savedUser = (CCUserImpl) saveDataObject(
                userImpl,
                CC2DataAccessConstants.CCUser.INSERT_QUERY,
                CC2DataAccessConstants.CCUser.UPDATE_QUERY,
                CC2DataAccessConstants.CCUser.DELETE_QUERY,CCUserDTO.getRefInstance()
            );

            _logger.debug( "passed-in user: " + System.identityHashCode( user ) + ", returned from saveDataObject: " +
                System.identityHashCode( savedUser ) );

            _logger.debug ( "users are same: " + ( user == savedUser ) );

            return savedUser;

        }
        catch ( InvocationExpectedEventRuntimeException ieere )
        {
            throw ieere;
        }
    }


    /*x*********************************************
     *  SHARED USER RELATED METHODS
     * *********************************************/


    public static User getSharedUserForCCUser( CCUser appUser )
    {
        if ( appUser == null )
            throw new NullArgumentException( "appUser" );

        if ( appUser.isAnonymous() )
            throw new CCRuntimeException( "attempted to retrieve a Shared Services User from an anonymous CCUser" );

        // Lookup by username instead of partyId.  The CCC_USER.PARTY_ID Id is not always
        // properly populated by CQ in LOCAL environments (svc-tf is always pointed to a single
        // schema and can't update in CCINTEG, for example).
        // [dstine 7/12/10]
        String username = appUser.getUsername();
        return getSharedUserForUsername( username );
    }

    private static Privilege[] getPrivilegesByPartyId(Long partyId) {
        //  This is the only way I can see to extract the privileges for a user
        //  on the fly.  The services involved still expect an OLD USER OBJECT
        //  and I cannot change that.  The INTERNAL service allows access to
        //  the privileges directly by party id...

        long pid = partyId.longValue();
        return AccessControlService.getPrivileges(pid);
    }

    /**
     * Returns the <code>User</code> for the specified <code>shared user.</code>.
     * Will return <code>null</code> (and log a warning) if the user is not registered.
     */
    public static User getSharedUserForUsername( String username )
    {
        if ( StringUtils.isEmpty( username ) )
            throw new NullArgumentException( "username" );

        User user = null;

        //  We'll START with just the LDAP and accountInfo structures for
        //  our shared user.  We shouldn't need our telesales or TF party
        //  information immediately.

        LdapUser ldapUser = null;
        ArAccountInfo accountInfo = null;

        //  Load our LDAP service.
        LdapUserService ldapService = ServiceLocator.getLdapUserService();
      //  Find our user.

        try {
        	ldapUser = ldapService.getUser(new LdapUserConsumerContext(), username);
            user = new User();
            user.setLdapUser(ldapUser);
        }
        catch (UserNameNotFoundException e) {
            _logger.warn("user " + username + " cannot be located." );
        }

        //  So far so good.  But we will be asked whether or not this
        //  account is on hold, so we need to snag the account info as well.

        if (user != null) {
            TFService tfService = ServiceLocator.getTFService();

            try {
            	if (user.getLdapUser().getPartyID() == null){
            		user.getLdapUser().setPartyID(user.getLdapUser().getRightsLinkPartyID());
            	}
                Long partyId = Long.valueOf(user.getLdapUser().getPartyID());

                if (partyId.longValue() > 0) {
                    Party party = tfService.getIndLicenseePartyByPartyId(
                        new TFConsumerContext(), partyId);
                    user.setParty(party);

                    accountInfo = tfService.getArAccountInfoByAccount(
                        new TFConsumerContext(), user.getAccountNumber());
                    user.setAccountInfo(accountInfo);
                }
            }
            catch(Exception e) {
                //  This was not meant to be.  Either our LDAP account number
                //  was incorrect or empty.

                _logger.error("UserServices :: error fetching account info for user " + username);
            }
            if (user.getPartyId() != null && getPrivilegesByPartyId(user.getPartyId())!=null) {
                 user.setPrivileges(getPrivilegesByPartyId(user.getPartyId()));
            }
        }

        return user;
    }

    /**
     * Returns the <code>User</code> for the specified <code>CCUser</code>.
     * Will return <code>null</code> if the user is not registered.
     */
    public static User getSharedUserForPartyID( long partyID )
    {
        User user = null;

        //  We'll START with just the LDAP and accountInfo structures for
        //  our shared user.  We shouldn't need our telesales or TF party
        //  information immediately.

        LdapUser ldapUser = null;
        ArAccountInfo accountInfo = null;


        try {
            ldapUser = ServiceLocator.getLdapUserService().getUserByPartyId(new LdapUserConsumerContext(), Long.toString(partyID));
            user = new User();
            user.setLdapUser(ldapUser);
        }
        catch (UserNameNotFoundException e) {
            _logger.error("could not find user by party id "  + partyID);
        }

        //  So far so good.  But we will be asked whether or not this
        //  account is on hold, so we need to snag the account info as well.

        if (user != null) {
            TFService tfService = ServiceLocator.getTFService();
          try {
                Party party = tfService.getIndLicenseePartyByPartyId(
                    new TFConsumerContext(), Long.valueOf(partyID));
                user.setParty(party);

                accountInfo = tfService.getArAccountInfoByAccount(
                    new TFConsumerContext(), user.getAccountNumber());
                user.setAccountInfo(accountInfo);
            }
            catch(Exception e) {
                _logger.warn(LogUtil.getStack(e));
            }
            user.setPrivileges(getPrivilegesByPartyId(user.getPartyId()));
        }

        return user;
    }

    public static User getUserByPartyId(Long partyId)
    {
        User user = null;

        //  We want to load this user up.  It should be a "complete" user
        //  object.  Start with the basics...

        LdapUser ldapUser = null;
        ArAccountInfo accountInfo = null;

       try {
            ldapUser =ServiceLocator.getLdapUserService().getUserByPartyId(new LdapUserConsumerContext(), Long.toString(partyId));
            user = new User();
            user.setLdapUser(ldapUser);
        }
        catch (UserNameNotFoundException e) {
            //  If we cannot locate the party, it isn't cool.  Swallow
            //  the error and simply return a null user object.
        	_logger.error("user not found for partyId " + partyId + LogUtil.appendableStack(e));
        }
        if (user != null) {
            //  So far so good.  But we will be asked whether or not this
            //  account is on hold, so we need to snag the account info as well.

            TFService tfService = ServiceLocator.getTFService();
            Party party = tfService.getIndLicenseePartyByPartyId(
                new TFConsumerContext(), partyId);

            accountInfo = tfService.getArAccountInfoByAccount(
                new TFConsumerContext(), party.getAccountNumber());

            user.setAccountInfo(accountInfo);

            //  Basics are complete.  Now load everything else.  The User
            //  object is self constructing once it has been primed.

            user.loadRegistrationData();

            user.setPrivileges(getPrivilegesByPartyId(user.getPartyId()));
        }
        return user;
    }

    public static User getUserByUsername(String username)
    {
        User user = null;

        //  We want to load this user up.  It should be a "complete" user
        //  object.  Start with the basics...

        LdapUser ldapUser = null;
        ArAccountInfo accountInfo = null;
        try {
            ldapUser = ServiceLocator.getLdapUserService().getUser(new LdapUserConsumerContext(), username);
            user = new User();
            user.setLdapUser(ldapUser);
        }
        catch (UserNameNotFoundException e) {
            //  If we cannot locate the party, it isn't cool.  Swallow
            //  the error and simply return a null user object.
        	_logger.error("user not found for username " + username + LogUtil.appendableStack(e));
        }
        if (user != null) {
            //  So far so good.  But we will be asked whether or not this
            //  account is on hold, so we need to snag the account info as well.

            TFService tfService = ServiceLocator.getTFService();
           try {
                Long partyId = Long.valueOf(user.getLdapUser().getPartyID());

                if (partyId > 0) {
                    Party party = tfService.getIndLicenseePartyByPartyId(
                        new TFConsumerContext(), partyId);

                    accountInfo = tfService.getArAccountInfoByAccount(
                        new TFConsumerContext(), party.getAccountNumber());

                    user.setAccountInfo(accountInfo);
                }

                //  Basics are complete.  Now load everything else.  The User
                //  object is self constructing once it has been primed.

                user.loadRegistrationData();
            }
            catch(Exception e) {
                //  At this point we can just swallow any errors.
            	_logger.error(LogUtil.getStack(e));
            }
            user.setPrivileges(getPrivilegesByPartyId(user.getPartyId()));
        }
        return user;
    }

    public static User getUserByPtyInst(Long ptyInst)
    {
        User user = null;
        ArAccountInfo accountInfo = null;

        //  We don't have our username so we need to look up the party, use the
        //  partyId to find our ldap user, etc.

        TFService tfService = ServiceLocator.getTFService();

        Party party = tfService.getLicenseePartyByPtyInst(new TFConsumerContext(), ptyInst);

        if (party == null) {
            //  Log an error here.  If we are being handed a party inst, we
            //  are getting it from our own system and it SHOULD exist.

            String msg = "UserServices.getUserByPtyInst :: " +
                "Call to TFService.getLicenseePartyByPartyInst FAILED for " +
                "party " + ptyInst.toString();

            _logger.error(msg);
            return user;
        }

        LdapUser ldapUser = ServiceLocator.getLdapUserService().getUserByPartyId(new LdapUserConsumerContext(),
            Long.toString(party.getPartyId()));

        if (ldapUser == null) {
            _logger.info("There is no LDAP user for PartyID " +
                party.getPartyId().toString());
        }
        user = new User();
        user.setLdapUser(ldapUser);

        if (user != null) {
            //  So far so good.  But we will be asked whether or not this
            //  account is on hold, so we need to snag the account info as well.

            try {
                if (party.getPartyId().longValue() > 0) {
                    accountInfo = tfService.getArAccountInfoByAccount(
                        new TFConsumerContext(), party.getAccountNumber());
                    user.setAccountInfo(accountInfo);
                }

                //  Basics are complete.  Now load everything else.  The User
                //  object is self constructing once it has been primed.

                user.loadRegistrationData();
            }
            catch(Exception e) {
                //  At this point we can just swallow any errors.
            	_logger.error(LogUtil.getStack(e));
            }
            user.setPrivileges(getPrivilegesByPartyId(user.getPartyId()));
        }
        return user;
    }

    public static List<ContactRole> getContactRolesByPartyIdAndType(Long partyId, String roleTypeString) {
        List<ContactRole> roles = null;
        try {
            RoleTypeEnum rt = RoleTypeEnum.valueOf(roleTypeString);
            roles = ServiceLocator.getTelesalesRightsholderService().getContactRolesByPartyIdAndType(
                new TelesalesServiceConsumerContext(), partyId, rt);
        }
        catch(Exception e) {
        	_logger.error(LogUtil.getStack(e));
        }

        return roles;
    }

    public static RightsholderOrganization getOrganizationByPartyId(Long partyId) {
        //  A little different.  Grab our rightsholder info and
        //  return it in a List of objects.  These will have to
        //  be type cast by the receiver.  This will always return
        //  the objects in order: Rightsholder, RightsholderOrganization.

        RightsholderOrganization rh = null;
        rh = ServiceLocator.getTelesalesRightsholderService().getRightsholderOrganizationByOrgPartyId(
            new TelesalesServiceConsumerContext(), partyId);

        if (rh == null) {
            _logger.info("Party ID of " + partyId.toString() + " invalid rightsholder ID.");
        }

        return rh;
    }

    public static RightsholderOrganization getOrganizationByPtyInst(Long ptyInst) {
        RightsholderOrganization rh = null;
        Party party = null;

        //  Get our account person.

        party = ServiceLocator.getTFService().getOrgRightsholderPartyByPtyInst(new TFConsumerContext(), ptyInst);

        if (party == null) {
            _logger.error("UserServices.getOrganizationByPtyInst :: " +
                "Specified rightsholder ptyInst was not found.  " +
                "PTYINST == " + ptyInst.toString());
            return rh;
        }
        rh = ServiceLocator.getTelesalesRightsholderService().getRightsholderOrganizationByOrgPartyId(
            new TelesalesServiceConsumerContext(), party.getPartyId());

        if (rh == null) {
            _logger.info("UserServices.getOrganizationByPtyInst :: " +
                "Could not find rightsholder for PartyId: " +
                party.getPartyId().toString());
        }
        return rh;
    }

    /*x*********************************************
     *  CONVENIENCE METHODS FOR OTHER SERVICES
     * *********************************************/

    public static void updateCurrentUserRLnkSessionID( String sessionId )
    {
        CCUser ccUser = UserContextService.getActiveAppUser();

        ccUser.setRlnkSessionId(sessionId);
        ccUser.setModState( ModState.DIRTY );
        ccUser = save( ccUser );

        UserContextService.updateAppUser( ccUser );
    }
    public static void updateCurrentUserLastCartID( long cartID )
    {
        CCUser ccUser = UserContextService.getActiveAppUser();

        ccUser.setLastCartID( cartID );
        ccUser.setModState( ModState.DIRTY );
        ccUser = save( ccUser );

        UserContextService.updateAppUser( ccUser );
    }

    public static void updateCurrentUserAlwaysInvoice( boolean alwaysInvoice )
    {
        CCUser ccUser = UserContextService.getActiveAppUser();

        ccUser.setAlwaysInvoice( alwaysInvoice );
        ccUser.setModState( ModState.DIRTY );
        ccUser = save( ccUser );

        UserContextService.updateAppUser( ccUser );
    }

    public static void updateCurrentUserSkipQuickprice( boolean skip )
    {
        CCUser ccUser = UserContextService.getActiveAppUser();

        ccUser.setSkipQuickprice( skip );
        ccUser.setModState( ModState.DIRTY );
        ccUser = save( ccUser );

        UserContextService.updateAppUser( ccUser );
    }
    
    public static void updateCurrentUserAutoInvoiceSpecialOrder( boolean autoInvoice )
    {
        CCUser ccUser = UserContextService.getActiveAppUser();

        ccUser.setAutoInvoiceSpecialOrder(autoInvoice);
        ccUser.setModState( ModState.DIRTY );
        ccUser = save( ccUser );

        UserContextService.updateAppUser( ccUser );
    }

    public static void updateCurrentUsername( String username )
    {
        CCUser ccUser = UserContextService.getActiveAppUser();

        ccUser.setUsername( username );
        ccUser.setModState( ModState.DIRTY );
        ccUser = save( ccUser );

        UserContextService.updateAppUser( ccUser );
    }

    /**
     * Sets the specified SearchPermissionTypeEnum for the ActiveUser to either
     * true (to be displayed) or false (do not display) and persists the change.
     * @param spType
     * @param value
     */
    public static void updateCurrentUserSearchPreference( SearchPermissionTypeEnum spType, boolean value) {
        CCUser ccUser = UserContextService.getActiveAppUser();

        ccUser.setSearchPreference( spType, value );
        /*
         * removed the following, it was preventing persistence of
         * search options for anonymous users.
         * if (ccUser.getPartyID() > 0)
         */
        if (ccUser.getAuid()!= null && ccUser.getAuid().length() > 0)
        {
        	ccUser.setModState( ModState.DIRTY );
        	ccUser = save( ccUser );
        }
        UserContextService.updateAppUser( ccUser );        
    }
    
    /**
     * @deprecated
     * @see updateCurrentUserSearchPreference( SearchPermissionTypeEnum spType, boolean value)
     */
    @Deprecated
    public static void updateCurrentUserSearchPreference( SearchPermissionType spType, boolean value )
    {
        CCUser ccUser = UserContextService.getActiveAppUser();

        ccUser.setSearchPreference( spType, value );

        /*
         * removed the following, it was preventing persistence of
         * search options for anonymous users.
         * if (ccUser.getPartyID() > 0)
         */
        if (ccUser.getAuid()!= null && ccUser.getAuid().length() > 0)
        {
        	ccUser.setModState( ModState.DIRTY );
        	ccUser = save( ccUser );
        }
        UserContextService.updateAppUser( ccUser );
    }


    /*x*********************************************
     *  ROLE RELATED METHODS
     * *********************************************/


    /**
     * Returns an array of <code>Role</code> objects for the specified
     * <code>CCUser</code> object.
     * Will return <code>null</code> if no roles are found.
     */
    public static Role[] getSharedUserRolesForUser( User user )
    {
        if ( user == null )
            throw new NullArgumentException( "user" );

        return AccessControlPersistence.getRoles( user.getPartyId().longValue() );
    }

    /**
     * Saves an array of <code>Role</code> objects for the specified
     * <code>User</code>, the Role objects will have their
     * partyIds set to the partyId of the input <code>User</code> object.
     * Will return <code>null</code> if no roles are found.
     */
    public static void save ( User user, Role[] userRoles )
    {
        if ( userRoles == null )
            throw new NullArgumentException( "userRoles" );

        if ( user == null )
            throw new NullArgumentException( "user" );

        Long userPartyID = user.getPartyId();

        for ( int idx=0; idx<userRoles.length; idx++) {
            userRoles[idx].setPartyID( userPartyID.longValue() );
        }

        AccessControlPersistence.save( userRoles );
    }


    /**
     * Returns an array of <code>Role</code> objects for the specified
     * <code>CCUser</code> object.
     * Will return <code>null</code> if no roles are found.
     */
    public static Role[] getDefaultCC2ApplicationRoles()
    {
        return AccessControlPersistence.getRolesForConsumingApp();
    }


    /**
     * Update password Enforcement <code>CCUser</code>.
     */
    public static CCUser updatePasswordEnforcement( CCUser ccUser )
    {
        validateArgument( ccUser, "user" );

        CCUserImpl userImpl = ( CCUserImpl ) ccUser;

        userImpl = ( CCUserImpl ) SQLUtils.ensureUnderlyingDTO( userImpl, CCUserDTO.class );

        TypedParameter[] inputParameters = {
            new TypedParameter( userImpl )
        };

        SingleDTOProcedureInvoker invoker =
            CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();
        invoker.configure(
            CC2DataAccessConstants.CCUser.UPDATE_PASSWORD_ENFORCEMENT, CCUserDTO.getRefInstance(),
            inputParameters
        );

        invoker.setNoDataFoundAcceptable( false );

        invoker.invoke();

        CCUserImpl updatedUser = ( CCUserImpl ) invoker.getDTO();

        UserContextService.updateAppUser(updatedUser);

        return updatedUser;
    }

    /**
     * During emulation new cart is assigned to Emulation User.
     * @param cartID
     */
    public static void updateAuthenticatedUserLastCartID( long cartID )
    {
        CCUser ccUser = UserContextService.getAuthenticatedAppUser();

        ccUser.setLastCartID( cartID );
        ccUser.setModState( ModState.DIRTY );
        ccUser = save( ccUser );
        ((CCUserImpl)UserContextService.getAppUser()).setRequestClientVersion(((CCUserImpl)ccUser).getRequestClientVersion());
        UserContextService.updateAuthenticatedUser(ccUser);
    }

}
