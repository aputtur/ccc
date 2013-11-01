<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<%@ page contentType="text/html" %>
<%@ page isErrorPage="true" %>

<%@ page import="com.copyright.base.Constants" %>
<%@ page import="com.copyright.base.config.AppServerConfiguration" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.lang.SystemUtils" %>
<%@ page import="org.apache.commons.lang.exception.ExceptionUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.apache.struts.util.ResponseUtils" %>

<div id="mainContainer">
    <h2>The application encountered a problem</h2>

    <%
        // Get information about the actual exception.  Note that there are 
        // two possible sources.  In some cases, both sources will have 
        // relevant information.

        // Source 1: Unhandled exception thrown inside the UserSessionFilter 
        // request processing cycle.  Two session attributes are populated by 
        // SecurityUtils.setExceptionInformationOnSession().
        
        // Source 2: Unhandled exception thrown inside of an actual JSP page.  
        // This should be infrequent, since there should be very little logic 
        // inside our JSP files, but it can indeed happen.
        // Variable "exception" is available by way of page directive 
        // isErrorPage=true.
        
        String stackTrace = getStackTrace( session, exception  );
        String message = getMessage( session, exception );
        String visibleMessage = getVisibleMessage( stackTrace, message );
        String commentMessage = getCommentMessage( stackTrace );
    %>

    <%= visibleMessage %>
    <br>
    <br>
    
    <!--
    <% 
        // Dump full stack trace to a comment in the response for debugging purposes. 
    %>
    <%= commentMessage %>
    -->
    
    <% 
    /*
        The fully-assembled page must be at least 512 bytes, due to IE Friendly Error Messages.
        Reference: http://forum.java.sun.com/thread.jspa?threadID=642939&messageID=3793598
        
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
        ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
    */
    %>
    
    <%!
    
    /**
     * Returns the consolidated stack trace for both the application exception 
     * (if any) and the JSP exception (if any).
     */
    String getStackTrace( HttpSession session, Throwable jspException )
    {
        String stackTrace = Constants.EMPTY_STRING;

        // If UserSessionFilter caught an exception, get the stack trace.
        Exception appException = (Exception) session.getAttribute( WebConstants.SessionKeys.APPLICATION_EXCEPTION );
        if ( appException != null )
        {
            stackTrace = stripDangerousCharacters(ExceptionUtils.getFullStackTrace( appException ) );
        }

        // If there was a JSP exception, get the stack trace.
        if ( jspException != null )
        {
            String jspTrace = stripDangerousCharacters(ExceptionUtils.getFullStackTrace( jspException ));
            
            // JSP exception hasn't been logged yet.
            Logger logger = Logger.getLogger( "error.jsp" );
            logger.error( jspTrace );
            
            if ( Constants.EMPTY_STRING.equals( stackTrace ) )
            {
                stackTrace = jspTrace;
            } 
            else 
            {
                stackTrace += "<br>" + jspTrace;
            }
        }
        
        return stackTrace;
    }
    
    /**
     * Returns the consolidated message for both the application exception (if 
     * any) and the JSP exception (if any).
     */
    String getMessage( HttpSession session, Throwable jspException )
    {
        String initialMessage = "No additional information";
        String message = initialMessage;
        
        Exception appException = (Exception) session.getAttribute( WebConstants.SessionKeys.APPLICATION_EXCEPTION );
        String appMessage = (String) session.getAttribute( WebConstants.SessionKeys.APPLICATION_EXCEPTION_FRIENDLY_MSG );
        
        // If UserSessionFilter caught an exception, get the message.
        if ( !StringUtils.isEmpty( appMessage ) )
        {
            message = stripDangerousCharacters(appMessage);
        }
        else if ( appException != null )
        {
            message = stripDangerousCharacters(appException.toString());
        }
        
        // If there was a JSP exception, get the message.
        if ( jspException != null )
        {
            if ( initialMessage.equals( message ) )
            {
                message = stripDangerousCharacters(jspException.toString());
            }
            else
            {
                message += " [" + stripDangerousCharacters(jspException.toString()) + "]";
            }
        }
        
        return message;
    }

    /**
     * Returns the consolidated message for display in the browser.
     */
    String getVisibleMessage( String stackTrace, String message )
    {
        String visibleMessage = Constants.EMPTY_STRING;
        
        if ( AppServerConfiguration.isDEV() || AppServerConfiguration.isLOCAL() )
        {
            // Show the message and stack trace in developer environments.
            
            String visibleTrace = StringUtils.replace( 
            		stripDangerousCharacters(stackTrace), SystemUtils.LINE_SEPARATOR, "<br>" );
            
            visibleTrace = StringUtils.replace( visibleTrace, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;" );
            
            visibleMessage = 
                MessageFormat.format( 
                    "{0} - {1}{2}{3}{4}", 
                    new Object[]{ 
                        getTimestamp(), 
                        message, 
                        "<br>", 
                        "<br>", 
                        visibleTrace } );
        }
        else
        {
            // Show only the message in other environments.
            
            visibleMessage = MessageFormat.format( "{0} - {1}", new Object[]{ getTimestamp(), message } );
        }
        
        return visibleMessage;
    }
    
    /**
     * Returns the consolidated message for display in an HTML comment.
     */
    String getCommentMessage( String stackTrace )
    {
        String commentMessage  = Constants.EMPTY_STRING;
        
        if ( AppServerConfiguration.isDEV() || AppServerConfiguration.isLOCAL() )
        {
            // Stack trace already printed visibly.
        }
        else if ( AppServerConfiguration.isPRD() )
        {
            // Don't expose stack traces to user in production.
        }
        else
        {
            commentMessage = 
                MessageFormat.format( 
                    "{0}{1}{2}Full stack trace of exception:{3}{4}{5}",
                    new Object[]{ 
                        getTimestamp(), 
                        SystemUtils.LINE_SEPARATOR, 
                        SystemUtils.LINE_SEPARATOR, 
                        SystemUtils.LINE_SEPARATOR, 
                        SystemUtils.LINE_SEPARATOR, 
                        stackTrace } );
        }
        
        return commentMessage;
    }
    
    /**
     * Returns a timestamp for cross-referencing with the server log.
     */
    String getTimestamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss.SSS" );
        return sdf.format( new Date() );
    }

    String stripDangerousCharacters(String input) {
    	if (input==null) return input;
    	String result = input.replace("<","&lt;");
    	result = result.replace(">","&gt;");
    	return result;
    }
    %>
</div>
