package com.copyright.ccc.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.CCUserContext;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.RLinkPublisherServices;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.admin.EditPublisherActionForm;
import com.copyright.ccc.web.util.WebUtils;

public class AddPublisherSubaccountAction extends EditPublisherAction {

    public AddPublisherSubaccountAction() {
    }
    
    @Override
    public ActionForward defaultOperation( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response) {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        editForm.setLastSaved("");
        editForm.resetSubAccountInfo();
        return mapping.findForward(SHOW_MAIN);                            
    }
    
    @Override
    public ActionForward lookup( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        //after finding the desired account then show the page again
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        ActionMessages actionErrors = new ActionMessages();
        try {
            if (editForm.getAddedSubAccountNum().trim().length()!=10 || !WebUtils.isAllDigit(editForm.getAddedSubAccountNum().trim())) {
                logActionError(actionErrors,"admin.publisher.error.invalidAcccountNumber");
                return mapping.findForward(SHOW_MAIN);               
            }
            //call service to get publisher information based on the account number
            RLinkPublisher rlinkPublisher = RLinkPublisherServices.getRLinkPublisherByAccount(new Long(editForm.getAddedSubAccountNum().trim()));
            //check if publisher is null
            if (rlinkPublisher == null) {
                logActionError(actionErrors,"admin.publisher.error.lookup.acccountNumberNotFound");
                return mapping.findForward(SHOW_MAIN);
            }
            saveSubAccountToForm(mapping, editForm, rlinkPublisher);
            editForm.setSubAccountSaveAllowed(true);
            return mapping.findForward(SHOW_MAIN);
        }
        catch (Exception exc){
            //record error and show page with error
             return processLookupException(mapping, actionErrors, exc);
        } 
        finally {
            if ( !actionErrors.isEmpty() )  {
               this.saveErrors( request, actionErrors );
               editForm.reset(mapping, request);
            }
        }
    }
    
    @Override
    public ActionForward save( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){   
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        ActionMessages actionErrors = new ActionMessages();
        RLinkPublisher subAccount = editForm.getAddedSubAccount();
        HttpSession httpSession = request.getSession();
        RLinkPublisher selectedPublisher = (RLinkPublisher)httpSession.getAttribute(WebConstants.SessionKeys.SELECTED_PUBLISHER);
        if (subAccount == null || selectedPublisher == null) {
            return mapping.findForward(SHOW_MAIN);
        }
        subAccount.setPubID(selectedPublisher.getPubID());
        CCUserContext ccUserContext = (CCUserContext)request.getSession().getAttribute(WebConstants.SessionKeys.CC2_USER_CONTEXT);
        String username = ccUserContext.getActiveUser().getUsername();
        subAccount.setCreUser(username);
        try {
            RLinkPublisherServices.createRLinkPublisherDetail(subAccount);
            editForm.setSubAccountSaveAllowed(false);
            return mapping.findForward(SHOW_MAIN);
        }
        catch (Exception exc){
            //record error and show page with error
             return processLookupException(mapping, actionErrors, exc);
        } 
        finally {
            if ( !actionErrors.isEmpty() )  {
               this.saveErrors( request, actionErrors );
               editForm.setLastSaved("");
            }
            else {
               editForm.setLastSaved(Long.valueOf(subAccount.getAccountNum()).toString());
            }
        }
    }
    
    private void saveSubAccountToForm(ActionMapping mapping, EditPublisherActionForm editForm, RLinkPublisher rlinkPublisher) {
         editForm.setAddedSubAccount(rlinkPublisher);
    }
    
}
