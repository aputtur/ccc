package com.copyright.ccc.web;

import java.util.Date;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionServlet;

import com.copyright.ccc.business.security.ListSecuredActions;

public class CCActionServlet extends ActionServlet
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void initServlet() throws ServletException
    {
        ConvertUtils.register( new DateConverter(), Date.class );
        
        // used to collect secured actions from web.xml
        // we can check secured actions when request is secured
        // but on developer machines we do not have keys installed.
         
        try
        {
        	ListSecuredActions.collectSecuredActions(getServletContext().getRealPath("/WEB-INF/web.xml"));
        }
        catch(Exception pe) // could be parse exceptions but should not be if the servlet is loaded
        {
        	throw new ServletException(pe);
        }
        
        super.initServlet();
    }
}
