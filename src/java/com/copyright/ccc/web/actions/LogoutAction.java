package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.CookieUtils;
import com.copyright.ccc.business.security.SecurityUtils;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.rlnk.RlnkRightsServices;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.workbench.util.StringUtils2;

public class LogoutAction extends CCAction
{
    static Logger _logger = Logger.getLogger(LoginAction.class);
    
    private static final String LOGOUT_FAILURE = "failure";
    private static final String LOGOUT_SUCCESS = "success";
    private static final String LOGOUT_SUCCESS_CEMPRO = "successCempro";
    private static final String LOGOUT_RELOGIN = "relogin";
    
    public ActionForward logoutAuthorizedUser(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
    	RlnkRightsServices.logout();
        UserContextService.setCurrent( null );     

        String isCempro = (String) request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO);
        SecurityUtils.invalidateSession( request );

        if (StringUtils2.isNullOrEmpty(isCempro)){
        	return mapping.findForward( LOGOUT_SUCCESS );
        } else if (isCempro.equals("1")){
        	return mapping.findForward( LOGOUT_SUCCESS_CEMPRO );
        } else {
        	return mapping.findForward( LOGOUT_SUCCESS );
        }
    }

    public ActionForward removeCookies(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        UserContextService.setCurrent( null );

        SecurityUtils.invalidateSession( request );

        CookieUtils.removeCC2Cookie( request, response );
        
        return mapping.findForward( LOGOUT_RELOGIN );
    }
}
