package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.forms.ViewReportsActionForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.workbench.logging.LoggerHelper;


public class ViewPaidInvoiceReportAction extends Action
{
	private static final Logger _logger = LoggerHelper.getLogger(); 
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		ActionForward forward = mapping.findForward("continue");
        ViewReportsActionForm viewForm = WebUtils.castForm( ViewReportsActionForm.class, form );
 
        LdapUserService ldapUserService = ServiceLocator.getLdapUserService();
        	     
	     ActionMessages errors = new ActionMessages();
	     LdapUser ldapUser = null;
	     try
	     {
	         User currentUser = UserContextService.getActiveSharedUser();
	         ldapUser = ldapUserService.getUser(new LdapUserConsumerContext(),currentUser.getUsername());
	     }
	     catch (Exception exc)
	     {
	     	_logger.info( ExceptionUtils.getFullStackTrace( exc ) );
	     }
	     if(ldapUserService == null || ldapUser == null) {
		        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("<b>We're sorry: It's not possible to view paid invoice report right now. Please try again later.</b>", false));
		        saveErrors(request, errors);
		        return mapping.findForward("down");
	     }
	     
	     if (UserContextService.hasPrivilege(CCPrivilegeCode.VIEW_ENTERPRISE_REPORTS)) {
	         viewForm.setEnterpriseViewInd("T");
	     }
	     
	     return forward;
	}

}
