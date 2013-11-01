package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.OrderHistoryActionForm;
import com.copyright.ccc.web.tags.PageScroller;
import com.copyright.ccc.web.util.OHSearchSpec;
import com.copyright.ccc.web.util.WebUtils;

/**
* OrderHistory struts action class for redisplaying the main order history tab
* when the user returns from the view order detail page.  This class uses a 
* persistent object (OHSearchSpec) saved in the session to redisplay the main
* tab search result page.
*/
public class RefreshOrderHistoryAction extends OrderHistoryBaseAction 
{
    protected Logger _rfLogger = Logger.getLogger( this.getClass() );

    public RefreshOrderHistoryAction() { }

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {      
       HttpSession session = request.getSession();
       OHSearchSpec savedSpec = (OHSearchSpec) session.getAttribute(WebConstants.SessionKeys.MAIN_SEARCH_SPEC);
       
       if (savedSpec == null)
       {
    	   return mapping.findForward("orderHistory");
       }
       OrderHistoryActionForm ohForm = WebUtils.castForm( OrderHistoryActionForm.class, form );  
       ohForm.setSortOption(savedSpec.getSortOption());

       String searchOpt = savedSpec.getSearchOption();
       ohForm.setSearchOption(searchOpt);
       ohForm.setDirection(savedSpec.getDirection());

       try {
          int opt = Integer.parseInt(searchOpt);
          switch (opt) {
             case DisplaySpecServices.ORDERDATEFILTER:
             case DisplaySpecServices.STARTOFTERMFILTER:
               ohForm.setSearchText(""); // Clear the free input field for date searches
               ohForm.setBeginDate(savedSpec.getBeginDate());
               ohForm.setEndDate(savedSpec.getEndDate());
             break;  
             case DisplaySpecServices.CONFNUMFILTER:
             case DisplaySpecServices.SCHOOLFILTER:
             case DisplaySpecServices.COURSENAMEFILTER:
             case DisplaySpecServices.COURSENUMBERFILTER:
             case DisplaySpecServices.INSTRUCTORFILTER:
             case DisplaySpecServices.YOURREFERENCEFILTER:
             case DisplaySpecServices.YOURACCTREFERENCEFILTER:
               // Only set the input field for non date searches
               ohForm.setSearchText(savedSpec.getSearchText());
             break;
          }
       } catch (Exception exc) {
          // if the search option is corrupt just set everything
          ohForm.setSearchText(savedSpec.getSearchText());
          ohForm.setBeginDate(savedSpec.getBeginDate());
          ohForm.setEndDate(savedSpec.getEndDate());
       }

       // Get the filter setting from the session since the detail page sets
       // this value to "ALL"
       Integer filter = (Integer) session.getAttribute("statusFilter");
       if (filter != null) {
          ohForm.setStatus(filter.intValue());
       }

       int fromRow = savedSpec.getFromRow();
       super.initUI(ohForm);
       this.getList(ohForm, fromRow);

       int status = ohForm.getStatus();
       if (status == OrderHistoryActionForm.DEFAULT) 
       {
          // Default to Open filter
          status = OrderHistoryActionForm.OPEN;

          // Now check for previous status filter setting
          Object obj = session.getAttribute("statusFilter");
          if (obj != null) {
             Integer sessFilter = (Integer) obj;
             // There's a valid session status filter and the user didn't click a filter link
             status = sessFilter.intValue();
          }

          ohForm.setStatus(status);
       }

       return mapping.findForward("continue");
    }

    /*
    * Get the Purchase list based on the search and sort selections in 
    * the form.  
    */
    public void getList(OrderHistoryActionForm form, int fromRow)
    {
        DisplaySpec spec = form.getDisplaySpec(); 
        int readNumberOfPurchaseRows = spec.getResultsPerPage();
        int toRow = fromRow + readNumberOfPurchaseRows - 1;
        spec.setDisplayFromRow(fromRow);
        spec.setDisplayToRow(toRow);

        // If validation failed suppress search
        if (form.isValid()) {
            try 
            {
               // Set all internal display spec last attributes to their current
               // setting so the DisplaySpec won't default back to page 1.  This is
               // necessary to preserve the scroll page setting when returning from
               // the order detail view page.
               if (fromRow > 0) { 
                  // if the starting row is 0 suppress the reset so it will
                  // be initted to 1 in the order purchase service class.  Otherwise
                  // the service class returns nothing.
                  spec.resetLastDisplaySpecAttributes();
               }

               // Get the order purchases set for the view detail page
//               OrderPurchases purchases = UserContextService.getOrderPurchases();
               // Set the start and end rows to the previous value from order history
//               purchases.setReadStartRow(fromRow + 1); // for oracle's 1 based indices
//               purchases.setReadEndRow(toRow + 1); // for oracle's 1 based indices

               // Now call the order management class with the order purchases reset
               // back to the same row range that existed on the order history tab
//               purchases = OrderPurchaseServices.getOrderPurchases();
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
            	_rfLogger.error("OrderHistoryAction.getPurchases(): ", ope);
            } catch (Exception exc) { 
            	_rfLogger.error("OrderHistoryAction.getPurchases(): ", exc);
            }
        }
    }
}
