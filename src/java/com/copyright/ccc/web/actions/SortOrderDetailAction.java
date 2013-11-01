package com.copyright.ccc.web.actions;

import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.OrderDetailActionForm;
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
* detail order history tab.  This action is similar to the ProcessOrderDetailAction
* action class except that the searchPage value is not set to true.  
*/
public class SortOrderDetailAction  extends OrderDetailAction
{
    public SortOrderDetailAction() { }
    
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response ) 
    {
        OrderDetailActionForm odForm = WebUtils.castForm( OrderDetailActionForm.class, form );
        odForm.setSearchNotSort(false); // A user sort was executed.

        ActionForward forward = super.execute(mapping, form, request, response);

        HttpSession session = request.getSession();
        OHSearchSpec searchSpec = new OHSearchSpec(odForm.getDisplaySpec());
        session.setAttribute(WebConstants.SessionKeys.DETAIL_SEARCH_SPEC, searchSpec);
        return forward;
    }
}
