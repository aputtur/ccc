package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.actions.TilesAction;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.web.forms.OrderHistoryActionForm;
import com.copyright.ccc.web.util.WebUtils;

/**
* OrderHistory base action class for implementing the /orderHistoryEmpty action.  
* This loads the main order history page with no results displayed for cases 
* where struts validation failed.
*/
public class OrderHistoryEmptyAction extends TilesAction
{
    protected Logger _logger = Logger.getLogger( this.getClass() );

    public OrderHistoryEmptyAction() { }

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {      
       OrderHistoryActionForm ohForm = WebUtils.castForm( OrderHistoryActionForm.class, form );
       this.initUI(ohForm);

       boolean cartEmpty = CartServices.isCartEmpty();
       ohForm.setCartEmpty(cartEmpty);
       
       boolean cartAcademic = CartServices.isAcademicCart();
       ohForm.setCartAcademic(cartAcademic);
       
       return mapping.findForward("continue");
    }

    /*
    * Internal helper for initializing the struts tag structures
    */
    protected void initUI(OrderHistoryActionForm form)
    {
       form.setSearchOptions(WebUtils.getOHSearchOptions());
       form.setSortOptions(WebUtils.getOHSortOptions());
       form.updateSearchLabel();
       
       form.setIsAdjustmentUser(UserContextService.hasPrivilege(CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT));

    }
}
