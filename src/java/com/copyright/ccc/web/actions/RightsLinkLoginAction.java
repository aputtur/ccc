package com.copyright.ccc.web.actions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.rlnk.RlnkRightsServices;
import com.copyright.ccc.business.services.rlnk.RlnkConstants.RlnkSessionConstants;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;


public class RightsLinkLoginAction extends CCAction{
	//private static final String CANCEL_PATH 	= "/articleSearch.do?operation=show&page=last";
	private static final String SHOW_MAIN 			= "showMain";
	private static final String SHOW_RL_REPRICE			= "showReprice";
	
	private static final Logger LOGGER = Logger.getLogger( RightsLinkLoginAction.class );
	
	
	
	/**
	 * If session RL_REQUEST_PARM_IN_SESSION is not null then returning for Login check.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward defaultOperation(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response){
		ActionForward 		forward 		=	 mapping.findForward( SHOW_MAIN );
		Map<String, String> requestParameters = new HashMap<String, String>();
		request.getSession().setAttribute(WebConstants.SessionKeys.RIGHTSLINK_LOGIN_MESSAGE,"");
		if(request.getSession().getAttribute(RlnkSessionConstants.RL_REQUEST_PARM_IN_SESSION)!=null ){
			@SuppressWarnings("unchecked")
			Map<String, String> params = (Map<String, String>)request.getSession().getAttribute(RlnkSessionConstants.RL_REQUEST_PARM_IN_SESSION);
			requestParameters=params;
		}

		//Add to that another name value pair for the password.  The name is password and the value should be c.comIsInDanvers,MA!2@1
		if(UserContextService.isUserAuthenticated()){
			RlnkRightsServices.addLoginInfo(requestParameters);
			request.getSession().setAttribute(RlnkSessionConstants.RL_REQUEST_PARM_IN_SESSION,requestParameters);
		}

		forward=  mapping.findForward(SHOW_RL_REPRICE );
		return forward;
	}
	
	

}
