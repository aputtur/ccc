package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.actions.TilesAction;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.OrderHistoryActionForm;
import com.copyright.ccc.web.tags.PageScroller;
import com.copyright.ccc.web.util.OHSearchSpec;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.ldapuser.api.LdapUserService;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.ldapuser.api.data.LdapUserConsumerContext;
import com.copyright.svc.ldapuser.api.data.UserNameNotFoundException;

/**
 * OrderHistory base action class for implementing the /orderHistory and
 * /processOrderHistory actions.  This class allows for the setting of the
 * searchPage control variable to control whether the user is on a search
 * page or the main Order History view page.  If the user performs a search
 * (/processOrderHistory) and the input validation passes the searchPage
 * attribute is set to true.  Otherwise for non search page loads (/orderHistory)
 * the searchPage attribute is set to false.
 */
public class OrderHistoryBaseAction extends TilesAction
{
    protected Logger _logger = Logger.getLogger( this.getClass() );

    public OrderHistoryBaseAction() { }

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {      
       OrderHistoryActionForm ohForm = WebUtils.castForm( OrderHistoryActionForm.class, form );

       boolean cartEmpty = CartServices.isCartEmpty();
       ohForm.setCartEmpty(cartEmpty);

       boolean cartAcademic = CartServices.isAcademicCart();
       ohForm.setCartAcademic(cartAcademic);

       String searchOpt = ohForm.getSearchOption();
       if (searchOpt == null || "".equals(searchOpt) || "0".equals(searchOpt)) {
          ohForm.setSearchOption("1"); // Default to confirmation number
       }

       HttpSession session = request.getSession();
       int status = ohForm.getStatus();
       if (status == OrderHistoryActionForm.DEFAULT) 
       {
          // Default to OPEN filter cc-2693 to improve order history load times
          status = OrderHistoryActionForm.OPEN;;

          // Now check for previous status filter setting
          Object obj = session.getAttribute("statusFilter");
          if (obj != null) {
             Integer sessFilter = (Integer) obj;
             // There's a valid session status filter and the user didn't click a filter link
             status = sessFilter.intValue();
          }
       }

       ohForm.setStatus(status);
       session.setAttribute("statusFilter", Integer.valueOf(status));

       this.initUI(ohForm);
       this.getList(ohForm); 

       OHSearchSpec savedSpec = new OHSearchSpec(ohForm.getSpec());
       session.setAttribute(WebConstants.SessionKeys.MAIN_SEARCH_SPEC, savedSpec);

       return mapping.findForward("continue");
    }

    /*
    * Get the Purchase list based on the search and sort selections in 
    * the form.  Set the form searchPage attribute to false so the default
    * View: Open | Closed | All page loads.
    */
    public void getList(OrderHistoryActionForm form)
    {
        DisplaySpec spec = form.getDisplaySpec(); 
        // If validation failed suppress search
        if (form.isValid()) {
            try 
            {
//               OrderPurchases purchases = OrderPurchaseServices.getOrderPurchases();
               OrderSearchCriteria orderSearchCriteria = OrderPurchaseServices.getOrderSearchCriteriaFromDisplaySpec(UserContextService.getPurchaseDisplaySpec());
               OrderSearchResult orderSearchResult = OrderPurchaseServices.searchOrderPurchases(orderSearchCriteria);
               OrderPurchases purchases = orderSearchResult.getOrderPurchasesObject();
               
               form.setPurchases(purchases);
               
               int rowCount = purchases.getTotalRowCount();
               int resultsPerPage = spec.getResultsPerPage();
               if (rowCount > 0) 
               {
                  int totalPages = rowCount / resultsPerPage;
                  int mod = rowCount % resultsPerPage;
                  if (mod > 0) { totalPages += 1; }
                  form.setTotalPages(totalPages);
                  
                  int currPage = form.getCurrentPage();
                  int startPage = form.getStartPage();
    
                  int nextBlockIndex = startPage +  PageScroller.VISIBLE_PAGE_LINKS;
    
                  if (currPage >= nextBlockIndex) {
                     startPage = nextBlockIndex;
                     // StartPage could be reduced to give 5 total pages at end of list
                     form.setStartPage(startPage);
                  } else if (currPage < startPage) {
                     startPage -= PageScroller.VISIBLE_PAGE_LINKS;
                     form.setStartPage(startPage);
                  }
               } else {
                  form.setTotalPages(0); 
               }
    
               int itemCount = purchases.getTotalRowCount();
               form.setTotalItems(itemCount);
            } catch (OrderPurchasesException ope) { 
               _logger.error("OrderHistoryAction.getPurchases(): ", ope);
            } catch (Exception exc) { 
               _logger.error("OrderHistoryAction.getPurchases(): ", exc);
            }
        }
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
       
       //if form shows other then the view options of "All", "Open", "Closed" (i.e. the drop down of "search all orders by:" is being used)
       //be sure the display spec specifies a view of "ALL"
       if (!form.getSearchLabel().equalsIgnoreCase(OrderHistoryActionForm.ALL_ORDERS) && 
    	   !form.getSearchLabel().equalsIgnoreCase(OrderHistoryActionForm.OPEN_ORDERS) &&
    	   !form.getSearchLabel().equalsIgnoreCase(OrderHistoryActionForm.CLOSED_ORDERS))
       {
    	   form.setStatus(OrderHistoryActionForm.ALL);
    	   form.initDisplaySpec();
       }
       //Check if we should display RL Orders tab
       User currentUser = UserContextService.getActiveSharedUser();
   	   LdapUserService  ldapService = ServiceLocator.getLdapUserService();
   	   
       LdapUser ldUser = null; 
       String userName = currentUser.getUsername() ;
       
       try
       {
       	  ldUser = ldapService.getUser(new LdapUserConsumerContext(), userName);
          if(ldUser.getRightsLinkPartyID()==null ||ldUser.getRightsLinkPartyID()==""){
        	  //looks like this is not a RL User show indicate to hide RL Orders tab
        	  form.setRlUser(false);
          }
          else{
        	  form.setRlUser(true);
          }
    
       }
       catch ( UserNameNotFoundException e )
       {
           _logger.error( ExceptionUtils.getFullStackTrace(e) );
       }

    }
}
