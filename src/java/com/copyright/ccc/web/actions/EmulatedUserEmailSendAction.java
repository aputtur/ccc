/*
 * EmulatedUserEmailSendAction.java
 * Copyright (c) 2010, Copyright Clearance Center, Inc. All rights reserved.
 * ----------------------------------------------------------------------------
 * Revision History
 * 04-15-2005   ASAXENA  Created.
 * ----------------------------------------------------------------------------
 */

package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.CCUserImpl;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.svc.extEmail.api.EmailSendService;
import com.copyright.svc.extEmail.api.data.EmailSendConsumerContext;
import com.copyright.svc.order.api.data.OrderConsumerContext;
import com.copyright.svc.order.api.data.OrderSource;
import com.copyright.workbench.util.StringUtils2;

/**
 * Action class used to send email to emulated user.
 * 
 * @author asaxena
 * 
 */
public class EmulatedUserEmailSendAction extends CCAction
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.copyright.ccc.web.CCAction#dispatchMethod(org.apache.struts.action
	 * .ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	@Override
	protected ActionForward dispatchMethod(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String name) throws Exception
	{

		User emulatedUser = UserContextService.getActiveSharedUser();

		String enforcePwdChg = request
				.getParameter(WebConstants.ENFORCE_PWD_CHG);

		if (!StringUtils2.isNullOrEmpty(enforcePwdChg))
		{
			UserContextService.getAppUser().setEnforcePwdChg(
					Boolean.valueOf(enforcePwdChg));
			UserServices.updatePasswordEnforcement(UserContextService
					.getAppUser());
		}

		EmailSendService emailService =ServiceLocator.getEmailSendService();
		emailService.sendEmulatedUserOrderEmail(new EmailSendConsumerContext(),
				emulatedUser.getFirstName(), emulatedUser.getLastName(), emulatedUser.getUsername(),
					Long.valueOf(
						UserContextService.getAuthenticatedAppUser()
								.getLastCartID()).toString());

		// CookieUtils.removeCC2Cookie(request, response);

		/*Cart cart = CartServiceFactory.getInstance().getService().getNewCart(
				UserContextService.getSessionInitiator());*/
		
		UserContextService.setCOICart(null);
		
		com.copyright.svc.order.api.data.Cart newCart = new com.copyright.svc.order.api.data.Cart();
	    newCart.setCartSource(OrderSource.COPYRIGHT_DOT_COM);
	    com.copyright.svc.order.api.data.Cart cart = ServiceLocator.getCartService().saveNewCart(new OrderConsumerContext(), newCart);

		long appUserRequestClientVersion = ((CCUserImpl) UserContextService
				.getAppUser()).getRequestClientVersion();
		long authUserRequestClientVersion = ((CCUserImpl) UserContextService
				.getAuthenticatedAppUser()).getRequestClientVersion();
		if (appUserRequestClientVersion > authUserRequestClientVersion)
			((CCUserImpl) UserContextService.getAuthenticatedAppUser())
					.setRequestClientVersion(appUserRequestClientVersion);

		UserContextService.setCOICart(cart);
		/*
		 * UserContextService.getAuthenticatedAppUser()
		 * .setLastCartID(cart.getID());
		 */
		UserServices.updateAuthenticatedUserLastCartID(cart.getCartId());

		/*
		 * UserContextService.getActiveAppUser();
		 * UserContextService.getAppUser();
		 * UserContextService.getAuthenticatedAppUser();
		 */

		// CartServices.emptyCart();
		// this attribute does not have any specific purpose but to invoke the
		// forward the mapping successfully.
		request.setAttribute(WebConstants.RequestKeys.EMAIL, "");
		return mapping.findForward("success");
	}

}
