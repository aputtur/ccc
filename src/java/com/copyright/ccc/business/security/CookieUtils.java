package com.copyright.ccc.business.security;

import java.text.MessageFormat;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.workbench.security.HashFunctions;
import com.copyright.workbench.security.SecurityRuntimeException;

public class CookieUtils
{
    private static String AUID_COOKIE_NAME = "CC.AUID";
    

    static Logger _logger = Logger.getLogger(CookieUtils.class);

    /**
     * Add any persistent cookies to the client machine.  At present, we only
     * add one.
     */
    public static void addAUIDCookie( HttpServletRequest request,
                                      HttpServletResponse response )
    {
        // We do not check for the prior existence of the cookie, since we
        // want to update the expiration timestamp.

        CCUser identityUser =
            UserContextService.getUserContext().getActiveAppUser();

        if ( identityUser == null )
        {
            throw new SecurityRuntimeException();
        }

        String auid = identityUser.getAuid();

        if ( !StringUtils.isEmpty( auid ) )
        {
            int expirationDays = CC2Configuration.getInstance().getCookieAUIDExpirationDays();
            int cookieMaxAgeInSeconds =
                (int) ( expirationDays * DateUtils.MILLIS_PER_DAY / DateUtils.MILLIS_PER_SECOND );

            Cookie cookie = new Cookie( AUID_COOKIE_NAME, auid );

            cookie.setMaxAge( cookieMaxAgeInSeconds );

            // Cookie is valid for all requests in the domain.
            cookie.setPath( "/" );

            response.addCookie( cookie );
        }

        if ( _logger.isDebugEnabled() )
        {
            String auidAlreadyInCookies = findAUIDInCookies( request );
            if ( StringUtils.isEmpty( auidAlreadyInCookies ) )
            {
                logNewCookie( auid, request );
            }
            else if ( !auid.equals( auidAlreadyInCookies ) )
            {
                logNewCookie( auid, request );
            }
        }
    }


    public static String generateAUID( long pauseInMillis )
    {
        // Pause for a configurable amount of time prior to generating an AUID.
        if ( pauseInMillis > 0 )
        {
            try
            {
                Thread.sleep( pauseInMillis );
            }
            catch ( InterruptedException ie )
            {
                throw new CCRuntimeException( ie );
            }
        }

        return HashFunctions.SHA1();
    }


    /**
     * Returns the <code>Cookie</code> containing the CC AUID, if the
     * request has one.
     */
    public static String findAUIDInCookies( HttpServletRequest request )
    {
        Cookie[] cookies = request.getCookies();

        if ( cookies != null )
        {
            for ( int i = 0; i < cookies.length; i++ )
            {
                Cookie cookie = cookies[i];
                String cookieName = cookie.getName();
                if ( AUID_COOKIE_NAME.equals( cookieName ) )
                {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public static boolean removeCC2Cookie( HttpServletRequest request, HttpServletResponse response )
    {
        Cookie[] cookies = request.getCookies();
        boolean foundCookie = false;

        if ( cookies != null )
        {
            for ( int i = 0; i < cookies.length; i++ )
            {
                Cookie cookie = cookies[i];

                String cookieName = cookie.getName();

                if ( AUID_COOKIE_NAME.equals( cookieName ))
                {
                    cookie.setMaxAge( 0 );

                    cookie.setPath( "/" );

                    response.addCookie( cookie );

                    foundCookie = true;
                }
            }
        }

        return foundCookie;
    }

    /**
     * Log that we set a new cookie.
     */
    private static void logNewCookie( String auid, HttpServletRequest request )
    {
        String ip = SecurityUtils.determineClientIP( request );
        String msg =
            MessageFormat.format( "Setting new cookie value for {0} with AUID = {1}",
                                  new Object[] { ip, auid } );
        _logger.debug( msg );
    }
}
