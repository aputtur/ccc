package com.copyright.ccc.web.actions;

import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.OrderHistoryActionForm;
import com.copyright.ccc.web.util.OHSearchSpec;
import com.copyright.ccc.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
* Struts action class called when the user changes the search settings for the 
* main order history tab.  This action is similar to the SortOrderHistoryAction
* action class except that the newSearch boolean value is always set to true. 
*/
public class ProcessOrderHistoryAction extends OrderHistoryBaseAction 
{
    

    public ProcessOrderHistoryAction() { }

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {      
       OrderHistoryActionForm ohForm = WebUtils.castForm( OrderHistoryActionForm.class, form );

       ohForm.setSearchPage(true);   
       ohForm.setCurrentPage(1); // Each new search should start on page 1
       super.initUI(ohForm);

       ohForm.setNewSearch(true); // this is a search not a sort
       this.getPurchases(ohForm);

       // If the date field was invalid surface a user message
       if (ohForm.hasErrors()) {
          this.saveErrors(request, ohForm.getErrors()); 
       } else {
          HttpSession session = request.getSession();
          OHSearchSpec searchSpec = new OHSearchSpec(ohForm.getSpec());
          session.setAttribute(WebConstants.SessionKeys.MAIN_SEARCH_SPEC, searchSpec);
       }

       return mapping.findForward("continue");
    }
    
    private void getPurchases(OrderHistoryActionForm form)
    {
        super.getList(form);
    }
}
