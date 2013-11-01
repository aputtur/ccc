package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.ContextualHelpForm;

public class ContextualHelpAction extends CCAction
{

    public ActionForward defaultOperation(ActionMapping mapping, ActionForm form, 
                                      HttpServletRequest request, 
                                      HttpServletResponse response) throws Exception
    {
        ContextualHelpForm contextualHelpForm = castForm( ContextualHelpForm.class, form );
        
        contextualHelpForm.setHelpTitleId( request.getParameter( WebConstants.RequestKeys.HELP_TITLE_ID ) );
        contextualHelpForm.setHelpBodyId( request.getParameter(WebConstants.RequestKeys.HELP_BODY_ID) );
        
        return mapping.findForward( SHOW_MAIN );
    }
}
