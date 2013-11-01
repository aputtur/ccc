package com.copyright.ccc.business.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.web.WebConstants;
import com.copyright.workbench.logging.LoggingUtil;
import com.copyright.workbench.security.SecurityRuntimeException;
import com.copyright.workbench.util.StringUtils2;


/**
 * The first servlet <code>Filter</code> of the filter chain for all incoming
 * requests.  This class is the primary provider of request/response cycle
 * processing hooks.
 * <p>
 * Handles the following tasks:
 * <ul>
 * <li> Manages the log4j nested diagnostic context (MDC)</li>
 * <li> Configures the <code>HttpServletRequest</code> character encoding</li>
 * <li> Establishes and removes the <code>UserContext</code> on the thread of
 * execution</li>
 * </ul>
 */
public class UserSessionFilter implements javax.servlet.Filter
{
    static Logger _logger = Logger.getLogger( UserSessionFilter.class );

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain filterChain )
        throws IOException, ServletException
    {
        try
        {
            if ( request instanceof HttpServletRequest &&
                 response instanceof HttpServletResponse )
            {
                doFilter(
                    (HttpServletRequest) request,
                    (HttpServletResponse) response,
                    filterChain );
            }
            else
            {
                // Pass control to the next filter.
                filterChain.doFilter( request, response );
            }
        }
        catch ( Throwable e )
        {

            // Catch any Exceptions that have not otherwise been caught.  This
            // is one of the few places in the application we should actually
            // catch java.lang.Exception.

        	LoggingUtil.logUnwrappedException(_logger, Level.ERROR, "UserSessionFilter caught exception in filter chain.", e);

            if ( request instanceof HttpServletRequest &&
                 response instanceof HttpServletResponse )
            {
                HttpServletRequest httpRequest = (HttpServletRequest) request;

                SecurityUtils.setExceptionInformationOnSession( httpRequest, e );

                try
                {
                    /**
                     * Perform a forward instead of a redirect.  Redirecting
                     * in the context of some problems can put the session into
                     * an infinite loop.  For example, an exception in
                     * UserSessionFilter.handleNewAuthentication() will occur
                     * repeatedly as the client continues to follow each new
                     * redirect, and each new request from the not-yet-
                     * authenticated session is passed to
                     * handleNewAuthentication().  [dstine 11/9/06]
                     */

                    if ( response.isCommitted() )
                    {
                        /*
                         * What to do here?  We can't redirect to the error page, as
                         * it's too late (the response has been committed), and we can't
                         * forward to the error page for reasons mentioned in above
                         * comment.  One option is to check for exception information
                         * on the session in the application header JSP file and show some
                         * truncated version of the exception information there.  Another
                         * option is to do nothing extra and leave the exception logged,
                         * but not displayed on the browser.
                         */
                    }
                    else
                    {
                        RequestDispatcher rd =
                            request.getRequestDispatcher( WebConstants.APPLICATION_ERROR );
                        rd.forward( request, response );
                    }
                }
                catch ( IOException ioe )
                {
                    throw new CCRuntimeException( ioe );
                }
            }
            else
            {
                // At this point, just rethrow the exception.  There isn't too
                // much we can do; we aren't even servicing an HTTP servlet
                // request.  The servlet framework will fail over to the
                // appropriate error page (500).

                if ( e instanceof RuntimeException )
                {
                    throw (RuntimeException) e;
                }
                else
                {
                    throw new CCRuntimeException( e );
                }
            }
        }
    }

    private void doFilter(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain )
        throws IOException, ServletException
    {
        try
        {
            HttpServletResponseWrapper wrappedResponse = SecurityUtils.createUrlWithoutSessionId( response );

            SecurityLogUtils.establishLog4jDiagnosticContext( request );

            if ( SecurityUtils.isRequestForDynamicContent( request ) )
            {
                SecurityLogUtils.logRequest( request );

               //TODO: handle invalid sessions

//                if ( SecurityUtils.hasSessionBeenInvalidated( request ) )
//                {
                    // If the session was granted previously but is now invalid,
                    // we assume that the session has timed out.  Clear out the
                    // UserContext from the session so that establishUserContext()
                    // will create a new one from scratch.

                    // KMeyer - Can not get session from request, it is gone, fall through to
                    // struts requestProcessor and handle it there.

//                    UserContextService.setContextOnSession( request.getSession(), null );
//                }


                if ( ! establishUserContext( request, wrappedResponse ) )
                    // terminate request processing
                    return;


                // we keep a list of the last few URLs visited for the use
                // of actions that may need to jump to a recently visited
                // URL.
                addPathToNavigationHistory( request, wrappedResponse );

                // if password change is enforced redirect to password change
                if(!redirectToPwdChg(request,response))return;
                SecurityLogUtils.setAUIDOnLog4jDiagnosticContext();
            }

            // Pass control to the next filter.
            filterChain.doFilter( request, wrappedResponse );
        }
        finally
        {
            try
            {
                SecurityLogUtils.cleanupLog4jDiagnosticContext();
            }
            finally
            {
                cleanupUserContext();
            }
        }
    }

    @Override
    public void destroy()
    {
    }


    /**
     * Establishes the <code>UserContext</code> on the thread of execution for
     * this request/response.  Returns <code>true</code> if request processing
     * should continue, <code>false</code> if not.
     */
    private boolean establishUserContext( HttpServletRequest request, HttpServletResponse response )
    {
    	HttpSession session = request.getSession();

         // Give UserContext a handle on the session.
        UserContextService.setHttpSession( session );

        // Look for the UserContext in the session.
        CCUserContext context = UserContextService.getContextFromSession();

        try
        {
            if ( context == null )
            {
                // establishing UserContext for the first time - must be a new session.
                context = SystemServices.createUserContextAtSessionStart( request );

                // put UserContext on the session for retrieval by future requests
                UserContextService.setContextOnSession( context );
            }

            // establish UserContext on thread of execution.
            UserContextService.setCurrent( context );

            SecurityUtils.handleAuthentication( request );

            // set persistent cookie
            CookieUtils.addAUIDCookie( request, response );
        }
        catch (OnHoldStatusException ohse)
        {
        	forwardOnHoldStatusError( request, response, ohse  );
            return false;

        }
        catch ( AuthenticationFailureException e )
        {
            forwardOnAuthenticationError( request, response, e  );
            return false;
        }
        catch ( RLinkInCompleteException rlie)
        {
        	forwardOnRLinkInCompleteExceptionError( request, response, rlie  );
            return false;
        }

        return true;
    }

    private void forwardOnRLinkInCompleteExceptionError( HttpServletRequest request, HttpServletResponse response, Throwable e )
    {
        try
        {
        	SecurityUtils.invalidateSession( request );

        	request.setAttribute( WebConstants.RequestKeys.RIGHTSLINK_INCOMPLETE_EXCEPTION, e );

            RequestDispatcher rd =
                request.getRequestDispatcher( WebConstants.LOGIN_ERROR );

            rd.forward( request, response );
        }
        catch ( IOException ioe )
        {
            throw new SecurityRuntimeException( ioe );
        }
        catch ( ServletException se )
        {
            throw new SecurityRuntimeException( se );
        }
    }

    private void forwardOnHoldStatusError( HttpServletRequest request, HttpServletResponse response, Throwable e )
    {
        try
        {
            request.setAttribute( WebConstants.RequestKeys.LOGIN_EXCEPTION, e );

            RequestDispatcher rd =
                request.getRequestDispatcher( WebConstants.LOGIN_ERROR );

            rd.forward( request, response );
        }
        catch ( IOException ioe )
        {
            throw new SecurityRuntimeException( ioe );
        }
        catch ( ServletException se )
        {
            throw new SecurityRuntimeException( se );
        }
    }

    private void forwardOnAuthenticationError( HttpServletRequest request, HttpServletResponse response, Throwable e )
    {
        try
        {
            SecurityUtils.invalidateSession( request );

            request.setAttribute( WebConstants.RequestKeys.LOGIN_EXCEPTION, e );

            RequestDispatcher rd =
                request.getRequestDispatcher( WebConstants.LOGIN_ERROR );

            rd.forward( request, response );
        }
        catch ( IOException ioe )
        {
            throw new SecurityRuntimeException( ioe );
        }
        catch ( ServletException se )
        {
            throw new SecurityRuntimeException( se );
        }
    }

    /**
     * Remove the <code>UserContext</code> from thread local storage.  This
     * prevents inadvertent sharing of <code>UserContext</code>s in a pooled
     * thread situation.
     */
    private void cleanupUserContext()
    {
    	UserContextService.setSessionNewlyCreated(Boolean.FALSE);
        UserContextService.setCurrent( null );
        UserContextService.setHttpSession( null );
    }

    /**
     * Add the current request URL to the navigation history.  The navigation
     * history stores the last few URLs that were visited in the app.
     *
     * @param request
     * @param response
     */
    private static void addPathToNavigationHistory( HttpServletRequest request, HttpServletResponse response )
    {
        // if request was an AJAX request, don't add to nav history
        if ( SecurityUtils.isAJAXRequest( request ) )
            return;

        if( SecurityUtils.isPopupRequest( request ) )
            return;

        StringBuffer sb = new StringBuffer();

        String queryString = request.getQueryString();
        String path = request.getServletPath();

        sb.append( path );

        if ( ! StringUtils2.isNullOrEmpty( queryString ) )
        {
            sb.append( "?" );
            sb.append( queryString );
        }

        UserContextService.pushToNavHistory( sb.toString() );
    }

    // if user has been enforced to change password, redirect to password change page.
    private static boolean redirectToPwdChg(HttpServletRequest request, HttpServletResponse response)
    {
    	String path = request.getServletPath();
    	boolean isSecuredOps = request.isSecure();

    	// check for the secured operations while working on machine where SSL is not installed.
    	if(!isSecuredOps)
    	{
    		if(ListSecuredActions.patterns.contains(path))
    		{
    			isSecuredOps = Boolean.TRUE;
    		}
    		else
    		{
    			if(ListSecuredActions.patterns.contains(path.substring(0,path.lastIndexOf("/"))))
    				isSecuredOps = Boolean.TRUE;
    		}
    	}

    	if(isSecuredOps && null != UserContextService.getAuthenticatedAppUser() &&
    			UserContextService.getAuthenticatedAppUser().getEnforcePwdChg())
    	{
    		try
    		{
    			String changePasswordOps = "/changePassword.do";
    			if(!changePasswordOps.equalsIgnoreCase(path))
    			{
    				response.sendRedirect(changePasswordOps);
    				return Boolean.FALSE;
    			}
	    		return Boolean.TRUE;
    		}
    		catch(Exception ex)
    		{
    			_logger.error(ex);
    		}
    	}
    	return Boolean.TRUE;
    }
}
