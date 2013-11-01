package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.WebConstants;
import com.copyright.data.order.CartStatusException;
import com.copyright.svc.order.api.data.OrderConsumerContext;

/**
 * Action class to update cart
 * @author asaxena
 *
 */
public class UpdateCartAction extends Action
{
	 private static final Logger LOGGER = Logger.getLogger( UpdateCartAction.class );

	 private static final String SHOW_CART = "showCart";
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String cartId = request.getParameter(WebConstants.EMULATED_USER_CART_ID);

		LOGGER.info("Updating Cart Id to: " + cartId);
		
		try
		{
			com.copyright.svc.order.api.data.Cart cart = ServiceLocator.getCartService().getCartById(new OrderConsumerContext(), new Long(cartId));
			
			UserContextService.setCOICart(cart);
			
			UserServices.updateCurrentUserLastCartID(new Long(cartId));
		}
		catch(CartStatusException cse)
		{
			if(!cse.isCheckedOut())
				LOGGER.info( ExceptionUtils.getFullStackTrace(cse) );
		}
	
		return mapping.findForward(SHOW_CART);
		
	}

}
