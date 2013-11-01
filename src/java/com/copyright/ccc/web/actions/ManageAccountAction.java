package com.copyright.ccc.web.actions;

import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.ManageAccountActionForm;

import com.copyright.svc.telesales.api.data.UserChannelEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageAccountAction extends CCAction 
{
    public ManageAccountAction() { }
    
    public ActionForward defaultOperation( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        ActionForward forward = mapping.findForward("continue");
        ManageAccountActionForm mgrForm = castForm( ManageAccountActionForm.class, form );
        //UserContext ctx = UserContextService.getContextFromSession();
        
        User sessionUser = UserContextService.getSharedUser();
                                                       
        if (sessionUser != null)
        {
            String sessionChannel;
            sessionChannel = sessionUser.getUserChannel();
            String gpoConstant = UserChannelEnum.GPO.getUserType();
            
            mgrForm = setHasCustResponseOrders(mgrForm, sessionUser) ;
            if (sessionChannel != null)
            {
            	if (sessionChannel.equalsIgnoreCase(gpoConstant)) 
            	{
            		// Set default GPO radio selection
            		mgrForm.setGPO();
            } 	
            else 
            {
               mgrForm.setPayPerUse();
            }
         }
           else
            	{
            		mgrForm.setPayPerUse();
            	}
        } else {
            mgrForm.setPayPerUse(); // default
            mgrForm.setHasCustResponseOrders(false);
        }

        // Check if user is logged in.  If so disable radio buttons per M. Tremblay
        boolean authenticated = UserContextService.isUserAuthenticated();
        mgrForm.setIsLoggedIn(authenticated);

        // UserContextService.setCurrent(ctx);
        // Note the user can be known but not authenticated.  We require
        // authentication to accept the user as who they appear to be

/* -- KM Don't think wee need to do any of this now.
        boolean authenticated = UserContextService.isUserAuthenticated();
        mgrForm.setIsLoggedIn(authenticated);
        //boolean anonymous = UserContextService.isUserAnonymous();
        if (authenticated) {
        
            UserContext usrCtxt = UserContextService.getCurrentAsInterface();
            AccountServiceFactory factory = AccountServiceFactory.getInstance();
            AccountServiceAPI api = factory.getService();
            User sharedUser = UserContextService.getAuthenticatedSharedUser();
            Account account = sharedUser.getAccount();

            CCUser user = UserContextService.getAuthenticatedAppUser();       
            CCUserContext userContext = UserContextService.getUserContext();

            String type = user.getUserType(); 
        } */

        return forward;
      
    }
    
    private ManageAccountActionForm setHasCustResponseOrders(ManageAccountActionForm mgrFormParm, User sessionUserParm)
    {
    	OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
    	OrderSearchResult orderSearchResult = new OrderSearchResult();
    	
    	orderSearchCriteria.setBuyerPartyId(sessionUserParm.getPartyId());
    	orderSearchCriteria.setSpecialOrderUpdate(ItemConstants.SPECIAL_ORDER_UPDATE_AWAITING_LCN_CONFIRM);
    	    	    	
    	try
    	{
    		orderSearchResult = OrderLicenseServices.searchOrderLicenses(orderSearchCriteria) ;
    		
    		if (orderSearchResult.getTotalRows() > 0 )
    		{
    			mgrFormParm.setHasCustResponseOrders(true);
    		}
    		else
    		{
    			mgrFormParm.setHasCustResponseOrders(false);
    		}
    	}
    	catch (OrderLicensesException ole)
    	{
    		mgrFormParm.setHasCustResponseOrders(false);
    	}
    	return mgrFormParm ;
    }
}
