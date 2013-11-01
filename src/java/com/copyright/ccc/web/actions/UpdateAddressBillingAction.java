package com.copyright.ccc.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.telesales.api.TelesalesService;
import com.copyright.svc.telesales.api.data.Organization;
import com.copyright.svc.telesales.api.data.TelesalesServiceConsumerContext;

public class UpdateAddressBillingAction extends CCAction 
{
    public UpdateAddressBillingAction() { }
    
    public ActionForward defaultOperation( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
    	TelesalesService telesalesService =ServiceLocator.getTelesalesService();
        LdapUserService ldapUserService = ServiceLocator.getLdapUserService();
        ActionMessages errors = new ActionMessages();
        boolean teleSalesUp = false;
        LdapUser ldapUser = null;
        try {
        	teleSalesUp = SystemStatus.isTelesalesUp();
            User currentUser = UserContextService.getActiveSharedUser();
            ldapUser = ldapUserService.getUser(new LdapUserConsumerContext(),currentUser.getUsername());
        }
        catch (Exception exc){
    		_logger.error( ExceptionUtils.getFullStackTrace(exc) );
        }
        if(!teleSalesUp || ldapUser == null) {
	        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("<b>We're sorry: It's not possible to update your address and billing information right now. Please try again later.</b>", false));
	        saveErrors(request, errors);
	        return mapping.findForward("down");
        }

        String partyId = ldapUser.getPartyID();
        if (!(partyId != null && WebUtils.isAllDigit(partyId) && Long.parseLong(partyId) > 0))
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("We're sorry: your registration has not completed. It's not possible to update your address and billing information right now. Please try again later.", false));
            saveErrors(request, errors);
            return mapping.findForward("down");
        }
        
        List<Organization> orgList  =	telesalesService.getOrgByPartyId(
				new TelesalesServiceConsumerContext(), 
				org.apache.commons.lang.math.NumberUtils.toLong(partyId));

        if (orgList.size() >1)
        {
        	errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("We are currently unable to update your address and billing information online. Please contact Customer Service at 978-646-2600.", false));
        	saveErrors(request, errors);
        	return mapping.findForward("down");
        }
        String rlPartyId = ldapUser.getRightsLinkPartyID();
        if ((rlPartyId != null && WebUtils.isAllDigit(rlPartyId) && Long.parseLong(rlPartyId) > 0) && !SystemStatus.isRightslinkUp() )
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("We're sorry: your registration has not completed. It's not possible to update your address and billing information right now. Please try again later.", false));
            saveErrors(request, errors);
            return mapping.findForward("down");
        }
        if (errors.isEmpty()) {
        	return mapping.findForward("showRegistration");
        }
        return mapping.findForward("continue");
    }
}
