package com.copyright.ccc.web.actions.coi;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.CCAction;

import com.copyright.ccc.web.actions.coi.ReviewTermsConfirmAction;
import com.copyright.ccc.web.forms.coi.ReviewSubmitCartActionForm;
import com.copyright.ccc.web.forms.coi.ReviewTermsActionForm;

public class PrintConfirmPurchaseAction extends CCAction
{
    public ActionForward defaultOperation(ActionMapping mapping, ActionForm form, 
                                      HttpServletRequest request, 
                                      HttpServletResponse response)
    {
    	ReviewTermsConfirmAction  reviewTermsAction = new ReviewTermsConfirmAction();
    	ReviewTermsActionForm reviewTermsActionForm = ((ReviewSubmitCartActionForm)form).getTermsForm();
    	reviewTermsAction.reviewTerms(mapping, reviewTermsActionForm, request, response);
        return mapping.findForward( SHOW_MAIN );
    }
}
