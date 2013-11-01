package com.copyright.ccc.business.security;

import java.security.Principal;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.base.config.AppServerConfiguration;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.config.ApplicationResources;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.WebConstants;
import com.copyright.data.account.AccountNotFoundException;
import com.copyright.data.account.PersonNotFoundException;
import com.copyright.data.account.UserNotFoundException;
import com.copyright.opi.InvocationExpectedEventRuntimeException;
import com.copyright.opi.data.InvocationResult;
import com.copyright.opi.data.ReturnCodes;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.workbench.net.SimpleIPAddress;
import com.copyright.workbench.security.SecurityRuntimeException;

/**
 * This class is used to support the <code>UserSessionFilter</code>.  It contains
 * several static methods that provide functionality that the filter needs as it
 * sets up <code>UserContext</code>.
 *
 * This is really just a repository for session filter code, used to keep the
 * <code>UserSessionFilter</code> code relatively uncluttered.
 */
public class SecurityUtils
{

    static Logger _logger = Logger.getLogger( SecurityUtils.class );

    /**
     * This method is used as a gate in the <code>UserSessionFilter</code>.  Any
     * URL that does not pass this test will NOT be processed by
     * <code>establishUserContext()</code>.  This is to keep requests for images,
     * css, javascript, etc. from being processed by the whole user context
     * mechanism.
     *
     * @param request
     */
    public static boolean isRequestForDynamicContent( HttpServletRequest request )
    {
        String uri = request.getRequestURI();
        uri = uri.toLowerCase();

        // The idea of checking for dynamic content based solely on url
        // matching doesn't seem ideal.  But it may be the best we can do in
        // J2EE.

        // One alternate strategy is to resolve the URI to a physical location
        // ("real path"?) and test to see if a file exists at that location.

        // This statement must account for all URL patterns specified as
        // generating dynamic content in web.xml <servlet-mapping> elements.
        // [dstine 7/11/06]
        boolean isRequestForDynamicContent =
            uri.endsWith( ".jsp" ) ||   // Not in web.xml, but dynamic nonetheless.
            uri.endsWith( ".do" ) ||
            uri.endsWith( ".action" ) ||  // Added to support struts2 actions
            uri.endsWith( ".pdf" ) ||
            uri.endsWith( "j_security_check");

        return isRequestForDynamicContent;
    }


    public static boolean isRequestForAuthentication( HttpServletRequest request )
    {
        if ( request.getRequestURI().endsWith( "j_security_check" ) )
            return true;

        return false;

        //if ( request.getParameter( WebConstants.RequestKeys.REQUEST_IS_AUTH ) != null )
        //    return true;

        //return false;
    }

    /**
     * This test will only work if the proper request parameter has been set.
     * It is meant to work along with our custom client-side ContentLoader,
     * which will set the appropriate request parameter when it makes AJAX
     * requests.
     *
     * @param request
     */
    public static boolean isAJAXRequest( HttpServletRequest request )
    {
        String isAjaxParam = request.getParameter( WebConstants.IS_AJAX_RESPONSE );

        if ( isAjaxParam != null
            && isAjaxParam.equals( Boolean.TRUE.toString() ) )
                return true;

        return false;
    }

    /**
     * This test will only work if the proper request parameter has been set.
     * It is meant to work along with our custom openPopup function,
     * which will set the appropriate request parameter when opens a
     * popup window
     *
     * @param request
     */
     public static boolean isPopupRequest( HttpServletRequest request )
     {
         String isPopupParam = request.getParameter( WebConstants.IS_POPUP );

         if( isPopupParam != null && isPopupParam.equals( Boolean.TRUE.toString() ) )
            return true;

        return false;
     }

    /**
     * Encapsulates logic to determine the client IP since HttpServletRequest.getRemoteAddr may not work.
     * CC is deployed into an Oracle App Server with a Web Cache and a load balancer in front,
     * therefore the RemoteAddr available on the request object may either be the Web Cache server or the
     * load balancer, and not the actual originating client IP.  Each of these systems can add Http request
     * headers to propagate the client IP:
     *
     * <ul>
     * <li>"CLIENT" is put on by the load balancer (present in DEV, per lxu 8/10/06)</li>
     * <li>"CLIENTIP" is put on by the Web Cache</li>
     * <li>request.getRemoteAddr() is correct for a developer machine (standalone OC4J container or embedded container)</li>
     * </ul>
     */
    public static String determineClientIP( HttpServletRequest request )
    {
        String ipFromCLIENTHeader = request.getHeader( "CLIENT" );
        String sourceFromCLIENTHeader = "CLIENT Header";
        String ipFromCLIENTIPHeader = request.getHeader( "CLIENTIP" );
        String sourceFromCLIENTIPHeader = "CLIENTIP Header";
        String ipFromRequest = request.getRemoteAddr();
        String sourceFromRequest = "Request RemoteAddr";

        String ip = ipFromCLIENTHeader;
        String source = sourceFromCLIENTHeader;

        if ( StringUtils.isBlank( ip ) )
        {
            ip = ipFromCLIENTIPHeader;
            source = sourceFromCLIENTIPHeader;
        }

        if ( StringUtils.isBlank( ip ) )
        {
            ip = ipFromRequest;
            source = sourceFromRequest;
        }

        String msg = MessageFormat.format(
            "Using client IP {0} from {1}.  {2} = {3}, {4} = {5}, {6} = {7}.",
            new Object[]{ ip, source, sourceFromCLIENTHeader, ipFromCLIENTHeader,
                sourceFromCLIENTIPHeader, ipFromCLIENTIPHeader, sourceFromRequest, ipFromRequest } );

        _logger.debug(msg);

        return ip;
    }

    /**
     * TODO: not sure if this processing is still needed on the JAAS username.
     *
     * @param remoteUsername
     * @return
     */
    private static String formatRemoteUsername( String remoteUsername )
    {
        // In development at least, sometimes the realm is prepended to the RemoteUser.
        String username =
            StringUtils.contains( remoteUsername, "/" ) ?
                StringUtils.substringAfter( remoteUsername, "/" ):
                remoteUsername;

        // Rightsphere lowercases the username here, but this was causing problems
        // in CC2, as we've got existing users with uppercase characters in
        // them already.
        // username = username.toLowerCase();

        return username;
    }


    static String getJaasRemoteUser( HttpServletRequest request )
    {
        String remoteUser = request.getRemoteUser();

        if ( remoteUser == null )
        {
            return null;
        }

        return formatRemoteUsername( remoteUser );
    }

    /**
     * This method is used as the hook in the request/response cycle for determining
     * if the request is one for authentication, and calling the proper methods
     * to set up an authenticated <code>UserContext</code> if so.
     *
     * @param request
     */
    static void handleAuthentication( HttpServletRequest request )
    {
        Principal userPrincipal = request.getUserPrincipal();

        _logger.debug( "==========>    in handleAuthentication: " + request.getRequestURI() );

        //if ( isRequestForAuthentication( request ) )
        if ( userPrincipal != null )
        {
            // we have an authenticated user according to JAAS
            String jaasUsername = formatRemoteUsername( userPrincipal.getName() ).trim();

            LdapUserService ldapService =ServiceLocator.getLdapUserService();
        	LdapUser ldapUser = ldapService.getUser(new LdapUserConsumerContext(), jaasUsername);

        	if ((ldapUser.getRightsLinkPartyID() == null) && (ldapUser.getPartyID() == null) )
        	{
        			throw new RLinkInCompleteException(
        			"Your registration is still being processed. Please try again later." );
        	}

        	if ((ldapUser.getRightsLinkPartyID() != null) && (ldapUser.getPartyID() == null) )
        	{
        		if (org.apache.commons.lang.math.NumberUtils.toLong(ldapUser.getRightsLinkPartyID()) < 0 )
        		{
        			throw new RLinkInCompleteException(
        			"Your registration is still being processed. Please try again later." );
        		}
        	}

            //Check for First Time RightsLink User
            if ( !UserServices.isUserNameInCCUser(jaasUsername) )
            {
            	if (UserServices.isCCUser(jaasUsername))
            	{
            		//Create a record in CCC_USER table
            		UserServices.provisionCCUser(jaasUsername);
            	}

            	else if (UserServices.isRLUser(jaasUsername))
            	{
            		//Provision into CCC_USER table
            		UserServices.provisionCCUserForRLUser(jaasUsername);

            		//RightsLink User - Provision RL user
            		UserServices.provisionRLUser(jaasUsername);
            	}
            	/*else
            	{
            		//must be First Time Copyright.com user
            		//UserServices.provisionCCUser(username);
            	} */
            }

            if ( UserContextService.getAuthenticatedAppUser() == null )
            {
                // we have a new authentication request.  Must set up UserContext appropriately.
                setupUserContextAsAuthenticated( jaasUsername );
            }
            else
            {
                String appUsername = UserContextService.getAuthenticatedAppUser().getUsername();

                if ( ! jaasUsername.equals( appUsername ) && ! UserContextService.isEmulating() )
                {
                    // login credentials are different from already-logged in user.  Swap
                    // in new UserContext setup.
                    setupUserContextAsAuthenticated( jaasUsername );
                }

                // otherwise do nothing - creds supplied are already authenticated
            }

            if (UserContextService.getAuthenticatedSharedUser() != null)
            {
            	_logger.info("Login User: " + UserContextService.getAuthenticatedAppUser().getUsername()
            				+ " Party id: "  + UserContextService.getAuthenticatedAppUser().getPartyID()
            				+ " User Channel: " + UserContextService.getAuthenticatedSharedUser().getUserChannel());
            }
        }
        // otherwise do nothing - no JAAS principal - this not an authentication request
    }

    /**
     * Called by <code>handleAuthentication( HttpServletRequest )</code>
     * if necessary.  Sets the <code>UserContext</code> up as authenticated,
     * catches some commonly encountered exceptions, and throws
     * a <code>SecurityRuntimeException</code> appropriately.
     *
     * Does not actually do any authentication; it is assumed that the
     * actual authentication check against the credentials store as already
     * completed.
     *
     * @param username
     */
    private static void setupUserContextAsAuthenticated( String username )
    {
        try
        {
            SystemServices.authenticateUserContext( username );
        }
        catch ( UserNotFoundException unfe )
        {
            // This username does not exist in OID.  It may or may not exist in Telesales.
            String msg = MessageFormat.format( "The username {0} is not in OID",
                new Object[]{ username } );

            _logger.warn( "Login Failure: " + msg );

            throw new SecurityRuntimeException( msg, unfe );
        }
        catch ( PersonNotFoundException pnfe )
        {
            // This username exists in OID, but it does not exist in Telesales.
            String msg = MessageFormat.format( "The username {0} is in OID, but is not in Telesales",
                new Object[]{ username } );

            _logger.warn( "Login Failure: " + msg );

            throw new SecurityRuntimeException( msg, pnfe );
        }
        catch ( AccountNotFoundException anfe )
        {
            String msg = MessageFormat.format( "The username {0} generated an AccountNotFoundException",
                new Object[]{ username } );

            _logger.warn( "Login Failure: " + msg );

            throw new SecurityRuntimeException( msg, anfe );
        }
    }


    /**
     * Returns <code>true</code> if and only if the request is from a user
     * agent whose previously granted session is no longer valid.  This can
     * happen in two cases:
     * <ol>
     * <li>The session has timed out (expired)</li>
     * <li>We have called <code>HttpSession.invalidate()</code> -- for example,
     * when the user logs out</li>
     * </ol>
     */
    public static final boolean hasSessionBeenInvalidated( HttpServletRequest request )
    {
        // Have we granted this user agent a session before now?  If so, the
        // session ID must come from either a session cookie or the
        // (rewritten) URL.
        boolean hasBeenGrantedASession =
            request.isRequestedSessionIdFromCookie() || request.isRequestedSessionIdFromURL();

        // Is this user agent's session ID invalid?
        // NOTE: Should be equivalent to "request.getRequestedSessionId() == null".
        boolean sessionIsInvalid = ! request.isRequestedSessionIdValid();

        // If both are true, then either
        // 1. The session has timed out, or
        // 2. We have called HttpSession.invalidate() -- for example, when the
        //    user logs out
        return hasBeenGrantedASession && sessionIsInvalid;
    }

    /**
     * The action that performs a logout should call
     * <code>invalidateSession( HttpServletRequest )</code>, which sets a session
     * variable indicating that that code has run to explicitly invalidate the
     * session.  This is used to show a "session timed out" message when an
     * explicit logout has not occurred, and to act normally un-authenticated
     * when an explicit logout HAS occurred.
     *
     * @param request
     */
    public static final boolean hasSessionBeenInvalidatedByApplication( HttpServletRequest request )
    {
        return
            BooleanUtils.toBoolean( (Boolean) request.getSession().getAttribute(
                WebConstants.SessionKeys.SESSION_INVALIDATED_BY_APPLICATION ) );
    }

    /**
     * Invalidate the session, and place a token into the session indicating
     * that it was intentionally invalidated by the application.
     */
    public static void invalidateSession( HttpServletRequest request )
    {
        request.getSession().invalidate();

        // Set a flag on to signal CC2RequestProcessor that the
        // session did not time out on its own.
        request.getSession().setAttribute(
            WebConstants.SessionKeys.SESSION_INVALIDATED_BY_APPLICATION, Boolean.TRUE );
    }

    /**
     * Set exception information on the session for retrieval by the
     * application error page.
     */
    public static void setExceptionInformationOnSession( HttpServletRequest request, Throwable e )
    {
        HttpSession session = request.getSession();

        if ( ! AppServerConfiguration.isPRD() )
        {
            session.setAttribute( WebConstants.SessionKeys.APPLICATION_EXCEPTION, e );
        }

        // TODO: Expand to include additional translations; rework if OPI is
        // changed to directly support a user-friendly message.

        if ( e instanceof InvocationExpectedEventRuntimeException )
        {
            InvocationExpectedEventRuntimeException ieere = (InvocationExpectedEventRuntimeException) e;
            ReturnCodes returnCodes = ieere.getReturnCodes();

            if ( InvocationResult.CCC_VERSION_MISMATCH.equals( returnCodes.getInvocationResult() ) )
            {
                String msg = MessageFormat.format(
                    "{0} - {1} - {2}",
                    new Object[]{
                        ApplicationResources.getInstance().getString( "exception.cccVersionMismatch" ),
                        returnCodes.getProcedureName(),
                        returnCodes.getProcedureOperation() } );

                session.setAttribute( WebConstants.SessionKeys.APPLICATION_EXCEPTION_FRIENDLY_MSG, msg );
            }
        }
    }

    public static void invalidateSessionIfSessionIdFromURL( HttpServletRequest request )
    {
        if (request.isRequestedSessionIdFromURL())
        {
          HttpSession session = request.getSession();
          if (session != null) session.invalidate();
        }
    }

    /**
     * Determines whether the URL for Timeout is Allowed to continue
     */
    public static boolean isTimeOutAllowed (String timeOutURL) {
        String TIMEOUT_ALLOWED_ACTIONS[] = new String[9];
        TIMEOUT_ALLOWED_ACTIONS[0] = "/home.do";
        TIMEOUT_ALLOWED_ACTIONS[1] = "/viewPage.do";
        TIMEOUT_ALLOWED_ACTIONS[2] = "/ppuSearch.do";
        TIMEOUT_ALLOWED_ACTIONS[3] = "/basicSearch.do";
        TIMEOUT_ALLOWED_ACTIONS[4] = "/businessSearch.do";
        TIMEOUT_ALLOWED_ACTIONS[5] = "/academicSearch.do";
        TIMEOUT_ALLOWED_ACTIONS[6] = "/manageAccount.do";
        TIMEOUT_ALLOWED_ACTIONS[7] = "/search.do";
        TIMEOUT_ALLOWED_ACTIONS[8] = "/cart.do";


        int tCtr = 0;
        for (tCtr=0; tCtr < TIMEOUT_ALLOWED_ACTIONS.length; tCtr++) {
            if (timeOutURL.indexOf(TIMEOUT_ALLOWED_ACTIONS[tCtr]) > 0) {
                return true;
            }
        }
        return false;
    }


    public static HttpServletResponseWrapper createUrlWithoutSessionId( HttpServletResponse response )
    {
        // wrap response to remove URL encoding
        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(response)
        {
        	@Override
            public String encodeRedirectUrl(String url) { return url; }

        	@Override
            public String encodeRedirectURL(String url) { return url; }

        	@Override
            public String encodeUrl(String url) { return url; }

        	@Override
        	public String encodeURL(String url) { return url; }
        };

        return wrappedResponse;
    }

    /**
     * Returns <code>true</code> iff the specified IP is a CCC internal IP
     * address.
     */
    public static boolean isInternalIP( SimpleIPAddress ip )
    {
        return CC2Configuration.getInstance().getIPInternalRange().inRange( ip );
    }

    /**
     * Returns <code>true</code> iff the specified IP is the loopback IP.
     */
    public static boolean isLoopbackIP( SimpleIPAddress ip )
    {
        SimpleIPAddress loopback = new SimpleIPAddress( "127.0.0.1" );
        return loopback.getArray().equals( ip.getArray() );
    }
}
