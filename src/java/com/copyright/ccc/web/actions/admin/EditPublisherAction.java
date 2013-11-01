package com.copyright.ccc.web.actions.admin;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.CCUserContext;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.RLinkPublisherServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.admin.EditPublisherActionForm;
import com.copyright.ccc.web.util.WebUtils;

public class EditPublisherAction extends AdminAction {

    public static final String SUCCESS = "success";

    public EditPublisherAction() {
    }
    
    public ActionForward defaultOperation( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        
        if (editForm.getEditMode()) {
            return editPublisher(mapping, form, request, response);
        }
        else {
            return newPublisher(mapping, form, request, response);
        }
    }
    
    public ActionForward newPublisher(ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        editForm.reset();
        editForm.setEditMode(false);
        editForm.setSaveAllowed(false);
        return mapping.findForward(SHOW_MAIN);
    }
    
    public ActionForward editPublisher( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        editForm.reset();
        HttpSession httpSession = request.getSession();
        RLinkPublisher selectedPublisher = (RLinkPublisher)httpSession.getAttribute(WebConstants.SessionKeys.SELECTED_PUBLISHER);
        editForm.setPublisher(selectedPublisher);
        editForm.setEditMode(true);
        editForm.setPermOptionDesc(selectedPublisher.getPermOptionDesc());
        editForm.setLearnMoreDesc(selectedPublisher.getLearnMoreDesc());
        editForm.setAccountNum(Long.valueOf(selectedPublisher.getAccountNum()).toString());
        editForm.setPubName(selectedPublisher.getPubName());
        editForm.setPubUrl(selectedPublisher.getPubUrl());
        //now set the sub accounts if any
         setSubAccounts(editForm,selectedPublisher);

        return mapping.findForward(SHOW_MAIN);
    }
    
    public ActionForward lookup( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        editForm.resetSubAccountInfo();
        editForm.resetSubAccounts();
        editForm.setEditMode(false);
        ActionMessages actionErrors = new ActionMessages();
        try {
            if (editForm.getAccountNum().trim().length()!=10 || !WebUtils.isAllDigit(editForm.getAccountNum().trim())) {
                logActionError(actionErrors,"admin.publisher.error.invalidAcccountNumber");
                return mapping.findForward(SHOW_MAIN);               
            }
            //call service to get publisher information based on the account number
            RLinkPublisher rlinkPublisher = RLinkPublisherServices.getRLinkPublisherByAccount(new Long(editForm.getAccountNum().trim()));
            //check if publisher is null
            if (rlinkPublisher == null) {
                logActionError(actionErrors,"admin.publisher.error.lookup.acccountNumberNotFound");
                return mapping.findForward(SHOW_MAIN);
            }
            //if not null then set publisher object on the form
            editForm.setSaveAllowed(true);
            return savePublisherToForm(mapping, editForm, rlinkPublisher);
        }
        
        catch (Exception exc){
            //record error and show page with error
        	_logger.error(LogUtil.getStack(exc));
             return processLookupException(mapping, actionErrors, exc);

        } 
        finally {
            if ( !actionErrors.isEmpty() )  {
               this.saveErrors( request, actionErrors );
               editForm.resetExceptAcccountNumber();
            }
        }
    }
    
    public ActionForward save ( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response) 
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        ActionMessages actionErrors = new ActionMessages();
        try {
            if (!(Pattern.matches("[^http[s]?://]?[-a-zA-Z0-9_.:]+[-a-zA-Z0-9_:@&?=+,.!/~*'%$]*$",editForm.getPubUrl()))){
                logActionError(actionErrors,"admin.publisher.error.invalidURL");
                return mapping.findForward(SHOW_MAIN);               
            }
            //get user information
            CCUserContext ccUserContext = (CCUserContext)request.getSession().getAttribute(WebConstants.SessionKeys.CC2_USER_CONTEXT);
            String username = ccUserContext.getActiveUser().getUsername();
            //determine if this is a save or an update
            if (editForm.getEditMode()) {
                //update the RLinkPublisher Object
                 RLinkPublisher rlinkPublisher = editForm.getPublisher();
                 saveFormToPublisher(editForm,rlinkPublisher);
                 rlinkPublisher.setUpdUser(username);
                 RLinkPublisherServices.updateRLinkPublisher( rlinkPublisher );
                 return mapping.findForward(SHOW_MAIN); 
            }
            else {
                //call service to create new publisher information
                 RLinkPublisher rlinkPublisher = RLinkPublisherServices.getRLinkPublisherByAccount(new Long(editForm.getAccountNum().trim()));
                 saveFormToPublisher(editForm,rlinkPublisher);
                 rlinkPublisher.setCreUser(username);
                 RLinkPublisher updatedRLinkPublisher = RLinkPublisherServices.createRLinkPublisher( rlinkPublisher );
                 editForm.setPublisher(updatedRLinkPublisher);
                 request.getSession().setAttribute(WebConstants.SessionKeys.SELECTED_PUBLISHER,updatedRLinkPublisher);
                 editForm.setSaveAllowed(false);
                 return mapping.findForward(SHOW_MAIN);
            }
        }
        catch (Exception exc){
            //record error and show page with error
        	_logger.error(LogUtil.getStack(exc));
            return processSaveException(mapping,actionErrors,exc);
        }
        finally {
            if ( !actionErrors.isEmpty() )  this.saveErrors( request, actionErrors );
        }
                                              
    }
    
    public ActionForward cancel( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
        editForm.reset();
        return mapping.findForward(SUCCESS);                                  
    }
    

    public ActionForward deleteSubAccount( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        ActionMessages actionErrors = new ActionMessages();
        //go through collection of publisher recorsds mark for delete then send the list to the service
       // ArrayList<RLinkPublisher> lstSubAccountsToDelete = new ArrayList<RLinkPublisher>();
        EditPublisherActionForm editPublisherActionForm = castForm( EditPublisherActionForm.class, form );
        int selectedRow = editPublisherActionForm.getSelectedSubAccountRow();
        try {
            RLinkPublisherServices.deleteRLinkPublisherDetail(editPublisherActionForm.getSubAccounts().get(selectedRow).getPublisher());
            //now refresh sub accounts array
            EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
            HttpSession httpSession = request.getSession();
            RLinkPublisher selectedPublisher = (RLinkPublisher)httpSession.getAttribute(WebConstants.SessionKeys.SELECTED_PUBLISHER);
            RLinkPublisher[] rlSubAccountPublishers = RLinkPublisherServices.getPublisherDetailsById(selectedPublisher.getPubID());
            if (rlSubAccountPublishers != null) {
               editForm.setSubAccounts(rlSubAccountPublishers);
            }  
            else {
            	editForm.resetSubAccounts();
            }
            return mapping.findForward(SHOW_MAIN);
        }
        catch(Exception exc){
            //record error and show page with error
        	_logger.error(LogUtil.getStack(exc));
            logActionError(actionErrors,"admin.publisher.error.deleteFailed");
            return mapping.findForward(SHOW_MAIN);
        }
        finally {
            if ( !actionErrors.isEmpty() )  this.saveErrors( request, actionErrors );
        }                                         
    }
    
    public ActionForward refreshSubAccounts(ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){
            UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
            EditPublisherActionForm editForm = castForm( EditPublisherActionForm.class, form );
            setSubAccounts(editForm, editForm.getPublisher());
            return mapping.findForward(SHOW_MAIN);
    }
    
    private void setSubAccounts(EditPublisherActionForm editForm, RLinkPublisher rlinkPublisher) {
        RLinkPublisher[] rlSubAccountPublishers = RLinkPublisherServices.getPublisherDetailsById(rlinkPublisher.getPubID());
        if (rlSubAccountPublishers != null) {
           editForm.setSubAccounts(rlSubAccountPublishers);
        }
    }
    
    protected ActionForward savePublisherToForm(ActionMapping mapping, EditPublisherActionForm editForm, RLinkPublisher rlinkPublisher){
        //editForm.setPublisher(rlinkPublisher);
        editForm.setPubUrl(rlinkPublisher.getPubUrl());
        editForm.setPubName(rlinkPublisher.getPubName());
        editForm.setPermOptionDesc(rlinkPublisher.getPermOptionDesc());
        editForm.setLearnMoreDesc(rlinkPublisher.getLearnMoreDesc());
        return mapping.findForward(SHOW_MAIN);
    }
    
    protected void saveFormToPublisher(EditPublisherActionForm editForm, RLinkPublisher rlinkPublisher) {
        rlinkPublisher.setPubUrl(editForm.getPubUrl());
        rlinkPublisher.setPermOptionDesc(editForm.getPermOptionDesc());
        rlinkPublisher.setLearnMoreDesc(editForm.getLearnMoreDesc());
    }
    
    protected ActionForward processLookupException(ActionMapping mapping, ActionMessages actionErrors, Exception exc) {
        if (exc instanceof CCCRuntimeException){
            logActionError(actionErrors,"admin.publisher.error.general",((CCCRuntimeException)exc).getMessageCode());
        }
        else {
            //record error and show page with error
            logActionError(actionErrors,"admin.publisher.error.lookup.Failed");
        } 
        return mapping.findForward(SHOW_MAIN);
    }
    
    protected ActionForward processSaveException(ActionMapping mapping, ActionMessages actionErrors, Exception exc) {
        if (exc instanceof CCCRuntimeException){
            logActionError(actionErrors,"admin.publisher.error.general",((CCCRuntimeException)exc).getMessageCode());
        }
        else {
            //record error and show page with error
            logActionError(actionErrors,"admin.publisher.error.lookup.Failed");
        } 
        return mapping.findForward(SHOW_MAIN);
    }
    
    protected void logActionError(ActionMessages actionErrors, String errorType, String messageCode ){
        ActionMessage actionError = new ActionMessage(errorType,messageCode);
        actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
    }
    
    protected void logActionError(ActionMessages actionErrors, String errorType ){
        ActionMessage actionError = new ActionMessage(errorType);
        actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);     
    }
}
