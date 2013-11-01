package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCAction;
import com.copyright.workbench.logging.LoggerHelper;

public class AjaxMethodsAction extends CCAction
{
	private static final Logger _logger = LoggerHelper.getLogger();
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )throws IOException
    {
    	  
  	 		
  	 		
  	 		response.setContentType("text/html");
  	 		PrintWriter out = response.getWriter();
  	 		StringBuilder sb = new StringBuilder();
  	 		try{
  	      boolean authenticated = UserContextService.isUserAuthenticated();
  	    if ( _logger.isDebugEnabled() )
		{
			_logger.debug(" Ajax call to check user login status : " + authenticated);
		}
  	      					sb.append(authenticated);
  			    			out.println(sb);
  			      	 		out.flush();
  			
      	 		
  	 		}catch (CCRuntimeException e){
  	 			out.println("false");
	      	 		out.flush();
  	 			
  	 		}
  	 		return null;
    }
}
