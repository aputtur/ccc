package com.copyright.ccc.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.web.dispatcher.DispatcherConstants;
import com.copyright.ccc.web.dispatcher.DispatcherRequest;
import com.copyright.ccc.web.dispatcher.DispatcherRequestFactory;

public class CCDispatcher extends HttpServlet
{
     
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * The <code>service()</code> method is Called by the servlet container to allow the servlet to respond
     * to any HTTP GET or POST request.
     */
	@Override
    public void service( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
    {
       String type = request.getParameter( DispatcherConstants.ND_ARG_TYPE );
       String target = request.getParameter( DispatcherConstants.ND_ARG_TARGET );
                   
       DispatcherRequest dispatcherRequest = DispatcherRequestFactory.getDispatcherRequestInstance( type, target );

       boolean dispatcherRequestNotFound = dispatcherRequest == null;
      
       if ( dispatcherRequestNotFound ) {
        processRequestDispatcherNotFound( response );
        return;
       }
      
       boolean forcedUseRedirection = false;       
       boolean useRedirection = dispatcherRequest.isUseRedirect();
       boolean useForwarding = !useRedirection;
       
       if ( useForwarding ) { 
          forcedUseRedirection = !StringUtils.isEmpty( request.getParameter( DispatcherConstants.ND_ARG_USEREDIRECT )); 
       }
  
       if ( useRedirection || forcedUseRedirection ) {
          processRedirection( request, response, dispatcherRequest );
       } else {
          processForwarding( request, response, dispatcherRequest );
       }
       
    }


    ///////////////////
    //Private methods//
    ///////////////////
    
    private void processForwarding(HttpServletRequest request,
                              HttpServletResponse response, 
                              DispatcherRequest selectedRequest) throws IOException, 
                                                                        ServletException
    {
      String targetURL = selectedRequest.generateTargetURL( request );
      
      if ( !targetURL.equalsIgnoreCase( DispatcherConstants.ND_INTERNAL_REQUEST_URL) ) {
         // Forward the request to the target named
         ServletContext context = getServletContext();
                 
         RequestDispatcher dispatcher = context.getRequestDispatcher( targetURL );
      
         dispatcher.forward( request, response );
      }
    }

    
    private void processRedirection( HttpServletRequest request,
                                  HttpServletResponse response, 
                                  DispatcherRequest selectedRequest ) 
    throws IOException
    {
      String targetURL = selectedRequest.generateTargetURL( request );
      
      if( selectedRequest.isExternalResource() )
      {
        
        boolean protocolNotSpecified = !( targetURL.startsWith("http://") || targetURL.startsWith("https://") );
        
        if ( protocolNotSpecified )
        {
        
          String protocol =  request.isSecure() ? "https" : "http";
          String protocolSeparator = "://";
          targetURL = protocol + protocolSeparator + targetURL;
          
        }
        
      }else{
      
        targetURL = getWebAppBaseURL( request ) + targetURL;
      
      }
      
      if ( !targetURL.equalsIgnoreCase( DispatcherConstants.ND_INTERNAL_REQUEST_URL) ) {

         response.sendRedirect( targetURL ) ;

      }
    }
    
    
    private void processRequestDispatcherNotFound(HttpServletResponse response) throws IOException
    {
      response.sendError( HttpServletResponse.SC_NOT_FOUND,
                          "Dispatcher was unable to process request: no dispatcher request instance found." );
    }


  private String getWebAppBaseURL( HttpServletRequest request )
  {
    return request.getContextPath();
  }
}
