package com.copyright.ccc.business.security;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.ConsumingAppCodes;
import com.copyright.workbench.security.SecurityRuntimeException;

public class SecurityLogUtils
{
    /**
     * Key with which to pair the client IP address in the log4j diagnostic
     * context.
     */
    private static final String CLIENT_IP_MDC_KEY = "ClientIP";

    /**
     * Key with which to pair the AUID in the log4j diagnostic context.
     */
    private static final String AUID_MDC_KEY = "AUID";

    /**
     * Key with which to pair the Application Code in the log4j diagnostic context.
     */
    private static final String APP_CODE_MDC_KEY = "AppCode";


    static Logger _logger = Logger.getLogger( SecurityLogUtils.class );

    /**
     * Pushes values into the log4j mapped diagnostic context (MDC) for this
     * thread.  The values may be accessed via <code>%X{KEY}</code> in the
     * appender conversion pattern.
     */
    static void establishLog4jDiagnosticContext(
        HttpServletRequest request )
    {
        // Put the application code into the MDC for this thread.
        MDC.put( APP_CODE_MDC_KEY, ConsumingAppCodes.CCC );

        // Put the client IP into the MDC for this thread.
        MDC.put( CLIENT_IP_MDC_KEY, SecurityUtils.determineClientIP( request ) );
    }

    /**
     * Pushes values from the <code>UserContext</code> into the log4j mapped
     * diagnostic context (MDC) for this thread.  The values may be accessed
     * via <code>%X{KEY}</code> in the appender conversion pattern.
     */
    static void setAUIDOnLog4jDiagnosticContext()
    {
        // Attempt to put the AUID into the MDC for this thread.
        try
        {
            CCUser activeUser = UserContextService.getActiveAppUser();

            if ( activeUser != null )
            {
                setAUIDOnLog4jDiagnosticContext( activeUser.getAuid() );
            }
        }
        catch ( SecurityRuntimeException sre )
        {
            // Nothing to do; there will be no AUID on the MDC, and processing
            // should continue.
            _logger.warn("exception encountered when updating log4j diagnostic context: " + sre.toString() );
        }
    }
    
    /**
     * Pushes values from the <code>UserContext</code> into the log4j mapped
     * diagnostic context (MDC) for this thread.  The values may be accessed
     * via <code>%X{KEY}</code> in the appender conversion pattern.
     */
    static void setAUIDOnLog4jDiagnosticContext( String auid )
    {
        // Put the AUID into the MDC for this thread.
        MDC.put( AUID_MDC_KEY, auid );
    }

    /**
     * Removes the values we placed into the mapped diagnostic context (MDC)
     * for this thread.
     */
    static void cleanupLog4jDiagnosticContext()
    {
        // If we were using the nested diagnostic context (NDC), we would be
        // obligated to call remove()in order to free memory allocated to the
        // thread (see javadoc for org.apache.log4j.NDC.remove().)  It does
        // not appear that this step is compulsory for the MDC.

        MDC.remove( CLIENT_IP_MDC_KEY );
        MDC.remove( APP_CODE_MDC_KEY );
        MDC.remove( AUID_MDC_KEY );
    }

    /**
     * Logs the specified <code>HttpServletRequest</code> if the app is
     * configured to do so.
     */
    static void logRequest( HttpServletRequest request )
    {
        String url = WebUtils.getRequestedURL( request );
        String threadName = Thread.currentThread().getName();
        String msg = MessageFormat.format(
            "Thread = {0}, URL = {1}", new Object[]{ threadName, url } );

        _logger.debug(msg);
    }
    
    public static void logRequestInformation( HttpServletRequest request )
    {
    	@SuppressWarnings("unchecked")
        Map<String,String[]> parameterMap = request.getParameterMap();
        @SuppressWarnings("unchecked")
        Enumeration<String> attributeNames = request.getAttributeNames();

        Set<Map.Entry<String,String[]>> parameterEntrySet = parameterMap.entrySet();

        _logger.debug( "===> HTTP request information: " + System.identityHashCode( request ) + " <===" );

        _logger.debug( "request parameters:" );

        for ( Iterator<Map.Entry<String,String[]>> paramItr = parameterEntrySet.iterator(); paramItr.hasNext(); )
        {
            Map.Entry<String,String[]> entry = paramItr.next();
            _logger.debug( "\t" + entry.getKey() + "=" + entry.getValue() );
        }

        _logger.debug( "request attributes:" );

        while ( attributeNames.hasMoreElements() )
        {
            String attrName = attributeNames.nextElement();
            Object attrValue = request.getAttribute( attrName );
            _logger.debug( "\t" + attrName + "=" + attrValue.toString() );
        }

        _logger.debug( "request is secure: " + request.isSecure() );

        _logger.debug( "auth type: " + request.getAuthType() );

        if ( request.getUserPrincipal() != null )
            _logger.debug( "security principal name: " + request.getUserPrincipal().getName() );

        _logger.debug ( "remote user: " + request.getRemoteUser() );

        _logger.debug( "===> end request information: " + System.identityHashCode( request ) + " <===" );
    }
}
