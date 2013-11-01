package com.copyright.ccc.business.security;

import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.business.services.user.UserServicesConstants;
import com.copyright.opi.InvocationExpectedEventRuntimeException;
import com.copyright.opi.data.InvocationResult;
import com.copyright.opi.data.ModState;
import com.copyright.workbench.security.SecurityRuntimeException;


/**
 * Service methods for internal use of user context/session management subsystem.
 * 
 * This class is not exposed outside the <code>security</code> package; it contains
 * methods for performing system-level, "privileged" functions and thus its visibility
 * outside its intended area of use should be as limited as possible.
 * 
 * "Privileged" functions come in 2 forms:
 * 
 *  1.  A User record is being created or modified.  Since the persistence
 *      mechanism has the concept of a "modUser" (entity who last modified
 *      a given record), we need some "super" user who represents the system
 *      creating and modifying User records themselves in the course of login,
 *      logout, authentication, etc.  We therefore have a special user ID
 *      that is set on the <code>UserContext</code> for these operations.  These
 *      operations should be carefully guarded so that other application code
 *      can't erroneously masquerade as the super user.
 *      
 *  2.  The <code>UserContext</code> is being modified in a sensitive way.
 *      Examples of this are when an emulation session begins or ends, and
 *      when the <code>UserContext</code> is marked as "authenticated".
 *      Since actual user credential checking and marking of the <code>
 *      UserContext</code> as "authorized" happen in two distinct steps,
 *      and are performed by different subsystems of the application, the
 *      code that authenticates the user context must be independent and
 *      should be carefully guarded.
 * 
 * Most of the code in here has to do with setting up new <code>UserContext</code>s.
 * 
 * There are five scenarios under which we'll be seting up a new user
 * context:
 *
 * 1.  User has no cookie and hits the site -- we need to create a cookie
 *     and a CC User table row for her.  The user record will be marked as
 *     anonymous and will be used to track search settings and shopping cart.
 *     No shared user record will be made at this time.
 *
 * 2.  User has a cookie that points to an anonymous user record -- need to
 *     establish user context for that anonymous user.  We can assume that
 *     there is no shared user record corresponding.
 *
 * 3.  User has a cookie that points to a recognized user.  This means the
 *     user has registered and we can assume that there is a corresponding
 *     Shared user.
 *
 * 4.  User is logging in and we have a record for her.  We can assume our
 *     record is not anonymous and that there is a shared user record.
 *
 * 5.  User is logging in and we do not have a record for her.  This can
 *     happen when the user has used CCC resources and is an established
 *     entity in OID, Telesales, etc., but has never hit the CC website.
 *     In this case, we lazily create a new record in our user table.
 *
 *
 * NOTE: Much of the routines in here changed dramatically in late March, 2007
 * when requirements for the app were changed such that user settings (such
 * as last search type, shopping cart, etc.) should follow the browser, not
 * the user login.  In other words, when a user logs in, s/he gets his/her credentials
 * loaded into context, but the settings data remains the same as what was
 * previously associated with the unique ID (AUID) in the browser's cookie.
 * 
 * This does allow the same user to be associated with several AUIDs, but any
 * other user can log in and trump the last user's association with the AUID
 * on that machine.
 * 
 * Stored procedures changed dramatically and became a little more proactive in
 * the process of executing these business rules.  Close attention must be paid
 * to the semantics of the stored procs when trying to understand or modify
 * this code.
 */
/* package */ final class SystemServices
{
    private static long SYSTEM_USER_ID = 100;

    private static ThreadLocal<CCUserContext> _swappedContext = new ThreadLocal<CCUserContext>();

    static Logger _logger = Logger.getLogger( SystemServices.class );
    
    /**
     * <code>UserPair</code> exists solely so that
     * <code>loadUsersByLoginUsername</code> can return both users at once.
     */
    private static class UserPair
    {
        private CCUser _ccUser;
        private User _sharedUser;
        
        public UserPair( CCUser ccUser, User sharedUser )
        {
            _ccUser = ccUser;
            _sharedUser = sharedUser;
        }
        
        public CCUser getCCUser()
        {
            return _ccUser;
        }
        
        public User getSharedUser()
        {
            return _sharedUser;
        }
    }


    /*x*********************************************
     *  DEFAULT ACCESS METHODS - THIS IS THE API OF THIS
     *  CLASS - THE CLASS HAS NO PUBLIC METHODS AND
     *  IS ITSELF DECLARED WITH DEFAULT ACCESS
     * *********************************************/

    
    /**
     * This method calls <code>loadUsersByLoginUsername( String )</code> and
     * sets up the <code>UserContext</code> as authenticated with the loaded
     * users.  This is a sensitive method in that it causes the
     * <code>UserContext</code> to be authenticated, without doing any checks
     * for valid credentials, etc.  It is meant to be called after it is known
     * that a user has successfully authenticated against the application's user
     * authentication mechanism.
     * 
     * This separation of check from setup exists because we want to keep the
     * application authentication mechanism as independent from the rest of
     * the app code as possible.
     */
    static void authenticateUserContext( String username )
    {
        UserPair userPair = loadUsersByLoginUsername( username );
        
        User sharedUser = userPair.getSharedUser();

        /*
        if ( sharedUser.getOnHold() )
        {
            throw new OnHoldStatusException(
                "Your account has been placed on hold.  Please contact customer support." );
        }
        */
        UserContextService.setActiveAppUser( userPair.getCCUser() );
        UserContextService.setActiveUser( sharedUser );

        UserContextService.setAuthenticated();
    }

    /**
     * This method calls <code>loadUsersByLoginUsername( String )</code> and
     * sets up the <code>UserContext</code> as active with the loaded
     * users.  It does not establish authentication.
     * 
     */
    static void establishUserContextWithoutAuthentication( String username )
    {
        UserPair userPair = loadUsersByLoginUsername( username );
        
        User sharedUser = userPair.getSharedUser();

        /*
        if ( sharedUser.getOnHold() )
        {
            throw new OnHoldStatusException(
                "Your account has been placed on hold.  Please contact customer support." );
        }
        */
        UserContextService.setActiveAppUser( userPair.getCCUser() );
        UserContextService.setActiveUser( sharedUser );

        UserContextService.unAuthenticateUserContext();
//        UserContextService.setAuthenticated();
    }


    /**
     * Create a <code>UserContext</code> given a servlet request.  The calling
     * of this method marks the beginning of a new user session..
     * 
     * This method is called in the <code>UserSessionFilter</code> to create
     * the new context.  It then sets the context on the HTTP session.
     * 
     */
    static CCUserContext createUserContextAtSessionStart( HttpServletRequest request )
    {
        String auid = CookieUtils.findAUIDInCookies( request );

        if ( StringUtils.isEmpty( auid ) )
        {
            auid = CookieUtils.generateAUID( 0 );
        }
        
        return createUserContextForAUID( auid );
    }
    
    /**
     * Create a new <code>UserContext</code> given an AUID.  This method is used
     * by <code>createUserContextAtSessionStart( HttpServletRequest )</code>
     * and by testing code for setting up a <code>UserContext</code> for JUnit
     * tests.
     * 
     * This is the only place that the constructor for
     * <code>CCUserContext</code> should ever be called.
     * 
     */
    static CCUserContext createUserContextForAUID( String auid )
    {
        User sharedUser = null;

        setSystemUserOnUserContext();
        
        // get a CCUser for our AUID, which was either in the cookies or was
        // newly generated.  The database code will create us a new CCUser
        // if we pass in a new AUID, which of course won't be found in the
        // database.
        CCUser ccUser = UserServices.getCCUserForNewSession( auid );

         // ccUser should never be null -- proc under getCCUserForNewSession
         // should guarantee us a valid object, and noDataFoundAcceptable should
         // be false as well.

        // if we have a non-anonymous CCUser, we'd better have a corresponding
        // Telesales user.
        if ( ! ccUser.isAnonymous() )
        {
            sharedUser = UserServices.getSharedUserForCCUser( ccUser );
        }

        restoreUserContext();

        CCUserContext context = new CCUserContext( ccUser, sharedUser );

        SecurityLogUtils.setAUIDOnLog4jDiagnosticContext( ccUser.getAuid() );

        return context;
    }

    /**
     * Loads the CC and Shared users for the given username and sets up the
     * <code>UserContext</code> with the active users as the loaded ones.
     * Leaves the authenticated users alone, thus causing the authenticated
     * and active users to be different (this is the definition of emulation)
     * 
     * Note that emulating oneself is not allowed.  I'm not sure what effect
     * this would have, if any.  It is at least a guard against users accidentally
     * entering their own user IDs when the attempt to emulate, and causing
     * confusion.
     */
    static void emulateUserContext( String username )
    {
        if ( ! UserContextService.isUserAuthenticated() )
            throw new SecurityRuntimeException( "emulation attempted when not authenticated" );
   
        UserPair up = loadUsersByLoginUsername( username );
        
        if ( up._ccUser.getID() == UserContextService.getAuthenticatedAppUser().getID() )
            throw new SecurityRuntimeException( "cannot emulate yourself" );
            
        UserContextService.setActiveAppUser( up._ccUser );
        UserContextService.setActiveUser( up._sharedUser );
    }

    /**
     * Copy the authenticated users into the active user slots.  This has the
     * effect of ending an emulation session.
     * 
     * @param context
     * @return
     */
    static CCUserContext unEmulateUserContext( CCUserContext context )
    {
        if ( ! context.isAuthenticated() )
            throw new SecurityRuntimeException( "de-emulation attempted when not authenticated" );

        UserPair up = loadUsersByLoginUsername( context.getAuthenticatedAppUser().getUsername().trim() );

        context.setActiveAppUser( up._ccUser );
        context.setActiveUser( up._sharedUser );
//        context.setActiveAppUser( context.getAuthenticatedAppUser() );
//        context.setActiveUser( context.getAuthenticatedUser() );

        return context;
    }

    
    
    /*x*********************************************
     *  PRIVATE METHODS
     * *********************************************/


    /**
     * Given a username, try to load that user from the CC database and then
     * load the corresponding Shared user from that user's party ID.
     * 
     * If the user is not found in the CC database, s/he may be a legacy user
     * who exists in LDAP and Telesales but not in CC.  In this case, we'll
     * call the Shared Serivces method to load the user by username, then
     * lazily create a new CC user record.
     * 
     * Not that in order to load the CC user in the case that s/he exists in the
     * CC database, we call <code>mergeUserWithContext( String, CCUser )</code>
     * which will load up only some of the fields in the CCUser object, and will
     * keep most of them the way they are.
     * 
     * Most of the work in <code>mergeUserWithContext( String, CCUser )</code>
     * happens at the stored procedure level, so this method must work in concert
     * with the stored proc.
     * 
     * This "merge" operation is performed because of a requirement that user
     * settings follow the user's browser, not his/her login credentials.  
     * Therefore, we need to load the user identifying data from the database
     * record, but keep the settings data the way it was loaded from the AUID
     * in the browser cookie.
     * 
     * @param username
     * @return
     */
    private static UserPair loadUsersByLoginUsername( String username )
    {
        // we'll return these two objects as the newly loaded pair of users
        User sharedUser = null;
        CCUser newCCUser = null;

        // get user currently in context so we can merge in request_client
        // with newly loaded user if different user than last time
        CCUser currentCCUser = UserContextService.getActiveAppUser();

        LinkedList<String> navHistoryList = UserContextService.getUserContext().getNavHistoryList();

        setSystemUserOnUserContext();

        try
        {            
            newCCUser = UserServices.mergeUserWithContext( username, currentCCUser );
            
            sharedUser = UserServices.getSharedUserForCCUser( newCCUser );
        }
        // if we don't find the CCUser by username, look in Telesales and lazily
        // create a new CCUser record for the user found there.
        catch ( InvocationExpectedEventRuntimeException e )
        {
            if ( InvocationResult.NO_DATA_FOUND.equals( e.getReturnCodes().getInvocationResult() ) )
            {
                //Check for First Time RightsLink User create needed records
                if ( !UserServices.isUserNameInCCUser(username) )
                {
                	if (UserServices.isCCUser(username))
                	{
                		//Create a record in CCC_USER table
                		newCCUser = UserServices.provisionCCUser(username);
                	}

                	else if (UserServices.isRLUser(username))
                	{
                		//Provision into CCC_USER table
                		newCCUser = UserServices.provisionCCUserForRLUser(username);

                		//RightsLink User - Provision RL user
                		UserServices.provisionRLUser(username);
                	}
                }
                sharedUser = UserServices.getSharedUserForUsername( username );

                // out of luck; can't find username in Telesales
                if ( sharedUser == null )
                {
                    throw new AuthenticationFailureException( "There was a data error retrieving user: " + username );
                }
                
                if (newCCUser == null)
                {
                	// lazy creation of CC User record
                	// copies fields from shared user into CC user
                    newCCUser = UserServices.createCCUser( sharedUser );
                    newCCUser = saveNewCCUserWithAUID( newCCUser );
                }
            }
            // any other exceptions and we've encountered something we don't
            // want to gracefully handle.
            else
            {
                throw e;
            }
        }
        finally
        {
            restoreUserContext();            
        }

        UserContextService.getUserContext().setNavigationHistory(navHistoryList);
        
        SecurityLogUtils.setAUIDOnLog4jDiagnosticContext( newCCUser.getAuid() );

        return new UserPair( newCCUser, sharedUser );
    }

    /**
     * Used by <code>loadUsersByLoginUsername( String )</code> when lazy creation
     * of a CC user record is needed.  Creates a new AUID value and saves a new
     * <code>CCUser</code> with that new AUID.
     * 
     * TODO: Check the Rightsphere code.  The explicit check for a duplicate
     * AUID I think is here because AUIDs are generated with some cryptographically
     * strong randomized key generation routine, which means there is a chance,
     * however small, of generating the same one twice (perhaps if two machines
     * generate AUIDs at exactly the same time and happen to somehow come up with
     * the same value -- I'm not sure).  There may be some code to sleep for a 
     * moment and try again upon dupe generation.
     * 
     * @param ccUser
     * @return CCUser newly made
     */
    private static CCUser saveNewCCUserWithAUID( CCUser ccUser )
    {
        // This service only allows creation of new users.
        if ( ! ModState.NEW.equals( ccUser.getModState() ) )
        {
            throw new CCRuntimeException(
                UserServicesConstants.Messages.NEW_USER_MUST_HAVE_MOD_STATE_NEW );
        }

        String auid = CookieUtils.generateAUID( 0 );

        ccUser.setAuid( auid );
        
        if ( ccUser.getLastSessionStart() == null )
            ccUser.setLastSessionStart( new Date() );

        setSystemUserOnUserContext();

        try
        {
            // Attempt to save with a new AUID.
            ccUser = UserServices.save( ccUser );
        }
        catch ( InvocationExpectedEventRuntimeException ieere )
        {
            if ( ieere.getReturnCodes() != null )
            {
                if ( InvocationResult.DUP_VAL_ON_INDEX.equals(
                    ieere.getReturnCodes().getInvocationResult() ) )
                {
                    // Violation of AU_ID unique constraint.  (Only other
                    // unique constraint on CCC_USER is on USR_ID, which is
                    // filled from a sequence.)
                    String msg = MessageFormat.format(
                        "Encountered duplicate AUID {0}", new Object[]{ ccUser.getAuid() } );

                    throw new CCRuntimeException( msg, ieere );
                }
                else
                {
                    // Some other InvocationResult; don't handle here.
                    throw ieere;
                }
            }
            else
            {
                // No ReturnCodes; don't handle here.
                throw ieere;
            }
        }
        finally
        {
            restoreUserContext();
        }

        return ccUser;
    }

    /**
     * Creates a <code>UserContext</code> that contains the System user.  Call
     * this method before calling a service that would otherwise fail due
     * to lack of <code>UserContext</code> and which should be executed by
     * a "privileged" user.
     * 
     * Swaps out the existing user context.  The swapped-out user context must
     * be restored using <code>restoreUserContext()</code> after system call
     * is made.
     * 
     * To make the user context swapping reentrant we store the swapped-out user
     * context in a <code>ThreadLocal</code> variable.
     */
    private static void setSystemUserOnUserContext()
    {
        CCUserContext oldContext = null;

        try
        {
            oldContext = UserContextService.getUserContext();
        }
        catch( SecurityRuntimeException sre )
        {
            // eat this one - we're OK with getting a null UserContext here.
            if ( _logger.isDebugEnabled() )
                _logger.debug( "found empty UserContext" );
        }

        setSwappedUserContext( oldContext );

        CCUser sysUser = UserServices.createCCUser();

        sysUser.setID( SYSTEM_USER_ID );

        CCUserContext ctxt = UserContextService.createUserContext( sysUser );

        UserContextService.setCurrent( ctxt );
    }

    /**
     * Restores the <code>UserContext</code> (if there is one) that was on the
     * thread of execution immediately before <code>setSystemUserOnUserContext()</code>
     * was called.
     */
    private static void restoreUserContext()
    {
        if ( UserContextService.getActiveAppUser().getID() != SYSTEM_USER_ID )
            throw new SecurityRuntimeException( "attempting to restore user context when system user is not active -- could be nuking current valid context" );
            
        CCUserContext swappedContext = getSwappedUserContext();

        UserContextService.setCurrent( swappedContext );
    }

    private static void setSwappedUserContext( CCUserContext ctxt )
    {
        _swappedContext.set( ctxt );
    }

    private static CCUserContext getSwappedUserContext()
    {
        return _swappedContext.get();
    }
}
