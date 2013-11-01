package com.copyright.ccc.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.quartz.services.AutoDunningScheduler;
import com.copyright.ccc.web.forms.admin.AutoDunningForm;

public class AutoDunningAction extends AdminAction
{
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";

	private static final String SHOW_PAGE = "showAutoDunning";

	private static final String MSG_DAYSPASTDUE_NOT_INTEGER = "admin.error.days.past.due.input.not.integer";
	private static final String MSG_DAYSPASTDUE_MISSING = "admin.error.days.past.due.input.missing";
	private static final String MSG_DAYSPASTDUE_TOO_LARGE = "admin.error.days.past.due.input.larger.than.999";
	private static final String MSG_CUSTOMERID_EMPTY = "admin.error.customer.id.missing";
	private static final String MSG_CUSTOMERID_TOO_LONG = "admin.error.customer.id.larger.than.20characters";
	
	public ActionForward defaultOperation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)
	{
		UserContextService.checkPrivilege(CCPrivilegeCode.AUTO_DUNNING);

		ActionMessages errors = new ActionMessages();
		AutoDunningForm autoDunningForm = castForm( AutoDunningForm.class, form);
		boolean testMode = CC2Configuration.getInstance().getAutoDunningTestModeEnable();
		autoDunningForm.setUITestMode(testMode);

		if (!errors.isEmpty())
			this.saveErrors(request, errors);
		
	
		return mapping.findForward(SHOW_PAGE);
	}

	public ActionForward autoDunning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)
	{
		UserContextService.checkPrivilege(CCPrivilegeCode.AUTO_DUNNING);

		ActionMessages errors = new ActionMessages();
		AutoDunningForm autoDunningForm = castForm( AutoDunningForm.class, form);

		boolean testMode = CC2Configuration.getInstance().getAutoDunningTestModeEnable();
		autoDunningForm.setUITestMode(testMode);

		if (StringUtils.isEmpty(autoDunningForm.getDaysPastDue()))
		{
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(MSG_DAYSPASTDUE_MISSING));
		}
		else if (!StringUtils.isNumeric(autoDunningForm.getDaysPastDue()))
		{
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(MSG_DAYSPASTDUE_NOT_INTEGER));
		}
		else if (StringUtils.isNumeric(autoDunningForm.getDaysPastDue())&&Integer.parseInt(autoDunningForm.getDaysPastDue())>999)
		{
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(MSG_DAYSPASTDUE_TOO_LARGE));
		}
		else if (StringUtils.isEmpty(autoDunningForm.getCustomerId()))
		{
			autoDunningForm.setCustomerId("");
			if(!testMode)
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(MSG_CUSTOMERID_EMPTY));
			}
		}
		else if (!StringUtils.isEmpty(autoDunningForm.getCustomerId())&&autoDunningForm.getCustomerId().length()>20)
		{
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(MSG_CUSTOMERID_TOO_LONG));
		}
		
			
		if (!errors.isEmpty())
		{
			this.saveErrors(request, errors);
		}
		else
		{
			autoDunningForm.setDoFind(true);
			runJob(autoDunningForm, errors);
			request.setAttribute("DAYSPASTDUE", autoDunningForm.getDaysPastDue().trim());
			request.setAttribute("CUSTOMERID", autoDunningForm.getCustomerId().trim());
		}

		return mapping.findForward(SHOW_PAGE);
	}

	private void runJob(AutoDunningForm editForm, ActionMessages errors)
	{

		if (editForm.getApplicationRoles() == null)
		{
			editForm.setApplicationRoles(UserServices.getDefaultCC2ApplicationRoles());
		}

		if (StringUtils.isNotBlank(editForm.getDaysPastDue()) && editForm.isDoFind())
		{
			AutoDunningScheduler.getInstance().runSchedule(servlet, editForm);
		}
	}
}
