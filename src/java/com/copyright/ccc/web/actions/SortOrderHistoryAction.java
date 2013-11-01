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
* Struts action class called when the user changes the sort settings for the 
* main order history tab.  This action is similar to the ProcessOrderHistoryAction
* action class except that the newSearch boolean form value is set to false and the
* searchPage value remains unspecified.  Sorts may be done on search filtered items 
* or on the default result view.
*/
public class SortOrderHistoryAction extends OrderHistoryBaseAction 
{
    public SortOrderHistoryAction() { }

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {      
       OrderHistoryActionForm ohForm = WebUtils.castForm( OrderHistoryActionForm.class, form );

       ohForm.setCurrentPage(1); // Each new search should start on page 1
       super.initUI(ohForm);
       ohForm.setNewSearch(false); // this is a sort not a search

       super.getList(ohForm);

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
}
