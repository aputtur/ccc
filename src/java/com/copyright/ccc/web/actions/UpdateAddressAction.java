package com.copyright.ccc.web.actions;

import com.copyright.ccc.web.forms.UpdateAddressActionForm;
import com.copyright.ccc.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


/**
 * This is a preprocess action class only.  No user input is received or
 * processed by this action.  Validation is turned off.
 */
public class UpdateAddressAction extends Action
{
    private ActionMessages messages;
    private ActionMessages errors=null;
    
    public UpdateAddressAction() { }

    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response )
    {
        ActionForward forward = mapping.findForward("continue");
        UpdateAddressActionForm addrForm = WebUtils.castForm( UpdateAddressActionForm.class, form );
                
        // After updating Address & Billing info notify the user of success
        if ("true".equals(addrForm.getSbmt())) {
           // Update the address and invoicing option according to the form data
           this.update(addrForm);
           
          // if (errors == null) {
               messages = new ActionMessages();
               ActionMessage message = new ActionMessage("admin.update.ok");
               messages.add("updtOk", message);
               this.saveMessages(request, messages);
          // }
        }
        
        return forward;
    }

    private void update(UpdateAddressActionForm form) 
    {
       // TODO: Add calls to service layer when implemented 
       // TODO: Add action errors if any to this.errors and save with this.saveErrors(errors)
    }
}
