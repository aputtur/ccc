package com.copyright.ccc.web;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.InvalidCancelException;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.apache.struts.util.RequestUtils;

import com.copyright.ccc.business.security.SecurityUtils;
import com.copyright.ccc.web.util.WebUtils;

public class CC2RequestProcessor extends TilesRequestProcessor
{
    private static Logger _logger = Logger.getLogger( CC2RequestProcessor.class );

//    private static String TIMEOUT_REDIRECT_URL = "/home.do";
    private static String TIMEOUT_REDIRECT_URL = "/timedOut.do";


    /*
     * no use for processPreprocess right now.  Nav history moved
     * to UserSessionFilter
     */

    @Override
    protected boolean processPreprocess(HttpServletRequest request,
                                        HttpServletResponse response)
    {
      	super.processPreprocess(request, response);

        //Build request URI sequence
        buildURIPathSequence( request );

        String requestURL;

        if (SecurityUtils.hasSessionBeenInvalidated( request ) == true
            && SecurityUtils.hasSessionBeenInvalidatedByApplication( request ) == false ) {
            if (SecurityUtils.isTimeOutAllowed(request.getRequestURL().toString())) {
                if (request.getQueryString() != null) {
                    if (request.getQueryString().length() > 0) {
                        requestURL = request.getRequestURL().toString() + "?" + request.getQueryString();
                    } else {
                        requestURL = request.getRequestURL().toString();
                    }
                } else {
                    requestURL = request.getRequestURL().toString();
                }
                _logger.debug("Timeout Redirect to Original URL: " + requestURL);
            } else {
               requestURL = "http://" + WebUtils.getBaseURLFromRequest( request ) + TIMEOUT_REDIRECT_URL;
                _logger.debug("Timeout Redirect to Home: " + requestURL);
            }
            try {

                response.sendRedirect( requestURL );
            } catch ( IOException ioe )
            {
                return false;
            }

            return false;
        }
        return true;

    }




    /**
     * <p>If this request was not cancelled, and the request's
     * {@link ActionMapping} has not disabled validation, call the
     * <code>validate</code> method of the specified {@link ActionForm},
     * and forward to the input path if there were any errors.
     * Return <code>true</code> if we should continue processing,
     * or <code>false</code> if we have already forwarded control back
     * to the input form.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param form The ActionForm instance we are populating
     * @param mapping The ActionMapping we are using
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     * @exception InvalidCancelException if a cancellation is attempted
     *              without the proper action configuration
     */
    @Override
    protected boolean processValidate(HttpServletRequest request,
                                       HttpServletResponse response,
                                       ActionForm form,
                                       ActionMapping mapping)
        throws IOException, ServletException, InvalidCancelException
    {
        ActionMessages containsScriptError = new ActionMessages();

        //validate no scripts
        boolean hasNoScripts = validateNoScriptsInRequest( request );
        if( !hasNoScripts )
        {
            containsScriptError.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.containsScript") );

            // Special handling for multipart request
            if (form!=null && form.getMultipartRequestHandler() != null) {
                form.getMultipartRequestHandler().rollback();
            }

            // Was an input path (or forward) specified for this mapping?
            String input = mapping.getInput();
            if (input == null) {
                String noScriptMessage = "Scripting is not allowed.  Please make sure that the data you entered does not contain a script.";
                request.getSession().setAttribute(WebConstants.SessionKeys.CCC_APPLICATION_EXCEPTION_TRACE, noScriptMessage);

                URL errorURL = RequestUtils.absoluteURL( request, WebConstants.APPLICATION_ERROR );
                response.sendRedirect( errorURL.toExternalForm() );

                return (false);
            }

            // Save our error messages and return to the input form if possible
            request.setAttribute(Globals.ERROR_KEY, containsScriptError);

            if (moduleConfig.getControllerConfig().getInputForward()) {
                ForwardConfig forward = mapping.findForward(input);
                processForwardConfig( request, response, forward);
            } else {
                internalModuleRelativeForward(input, request, response);
            }

            return (false);
        }
        else
            return super.processValidate( request, response, form, mapping );
    }

    /**
     * A CC2-specific extension to the standard Struts support for
     * no-cache headers for all responses, if requested.
     *
     * CC will set no-cache by default to ensure accuracy of information.
     * However, this results in "Page Expired" when the user uses the back button
     * to navigate to a page that was generated from an HTTP POST.  In this case,
     * we will by-pass the Struts no-cache directive so the user can see their
     * page.  If the client browser is set to "don't cache", then there is
     * nothing we can do anyway.
     * <strong>NOTE</strong> - This header will be overridden
     * automatically if a <code>RequestDispatcher.forward()</code> call is
     * ultimately invoked.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     */
    @Override
    protected void processNoCache( HttpServletRequest request, HttpServletResponse response )
    {
        if ( !"POST".equals(request.getMethod()) )
        {
            super.processNoCache(request, response);
        }
    }

    protected boolean validateNoScriptsInRequest( HttpServletRequest request )
    {
    	@SuppressWarnings("unchecked")
        Map<String,String[]> parameterMap = request.getParameterMap();
        Iterator<Map.Entry<String,String[]>> parameterMapKeyIterator = parameterMap.entrySet().iterator();

        while( parameterMapKeyIterator.hasNext() )
        {
        	Map.Entry<String, String[]> mapEntry = parameterMapKeyIterator.next();

            String[] parameterValueArray = parameterMap.get( mapEntry.getKey() );

            for( int i = 0; i < parameterValueArray.length; i++ )
            {
                String parameterValue = parameterValueArray[i];
                boolean hasNoScripts = CCFieldChecks.validateNoScripts( parameterValue );
                if( !hasNoScripts ) return false;
            }
        }

        return true;
    }



  ///////////////////////////////
  //Private Methods
  ///////////////////////////////

  private void buildURIPathSequence( HttpServletRequest request )
  {
      boolean requestURISequenceNotPresent = request.getAttribute(WebConstants.RequestKeys.REQUEST_URI_SEQUENCE) == null;

      if ( requestURISequenceNotPresent )
      {
        request.setAttribute( WebConstants.RequestKeys.REQUEST_URI_SEQUENCE, new ArrayList<String>()  );

        boolean actionConfigDataPresent = request.getAttribute("org.apache.struts.action.mapping.instance") != null;

        if( actionConfigDataPresent )
        {
        	 @SuppressWarnings("unchecked")
              List<String> uriPathSequence = (List<String>)request.getAttribute(WebConstants.RequestKeys.REQUEST_URI_SEQUENCE);

              String actionConfigURI = request.getContextPath() +
                                       ( (ActionConfig) request.getAttribute("org.apache.struts.action.mapping.instance") ).getPath();

              uriPathSequence.add( actionConfigURI );
        }

      }

      String currentRequestURI = request.getRequestURI();
      @SuppressWarnings("unchecked")
      List<String> uriPathSequence = (List<String>) request.getAttribute( WebConstants.RequestKeys.REQUEST_URI_SEQUENCE );
      uriPathSequence.add( currentRequestURI );
      request.setAttribute( WebConstants.RequestKeys.REQUEST_URI_SEQUENCE, uriPathSequence );
  }

}
