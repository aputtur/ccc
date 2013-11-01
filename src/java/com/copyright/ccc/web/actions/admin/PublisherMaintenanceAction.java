package com.copyright.ccc.web.actions.admin;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.RLinkPublisherServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.admin.PublisherMaintenanceForm;
import com.copyright.ccc.web.util.PublisherDisplay;


public class PublisherMaintenanceAction extends AdminAction {
    public static final String SUCCESS = "success";
    public static final String NEW_PUBLISHER = "newPublisher";
    public static final String EDIT_PUBLISHER = "editPublisher";
    
    public PublisherMaintenanceAction() {
    }
    
    public ActionForward defaultOperation( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response) {
                                          
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        return findPublishers(mapping,form,request,response);
                                              
    }
    
    public ActionForward findPublishers( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){
       UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
       PublisherMaintenanceForm publisherMaintenanceForm = castForm( PublisherMaintenanceForm.class, form );
       ActionMessages actionErrors = new ActionMessages();
       try {
           RLinkPublisher[] rlinkPublishers = RLinkPublisherServices.getRLinkPublishers();
           if (rlinkPublishers != null && rlinkPublishers.length != 0) {
                publisherMaintenanceForm.setPublishersDisplay(RLinkPublisherServices.getRLinkPublishers());
           }
       }
       catch (Exception exc) {
    	   _logger.error(LogUtil.getStack(exc));    	   
           ActionMessage actionError = new ActionMessage("admin.publisher.error.findPublishers");
           actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
       }
       finally {
           if ( !actionErrors.isEmpty() )  this.saveErrors( request, actionErrors );
       } 
       return mapping.findForward(SHOW_MAIN);                                      
   }
   
    public ActionForward newPublisher( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){
       UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
       return mapping.findForward(NEW_PUBLISHER);
                                              
   }
   
    public ActionForward editPublisher( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        PublisherMaintenanceForm publisherMaintenanceForm = castForm( PublisherMaintenanceForm.class, form );
        PublisherDisplay [] publishersDisplay = new PublisherDisplay[publisherMaintenanceForm.getPublishersDisplay().size()];
        publisherMaintenanceForm.getPublishersDisplay().toArray(publishersDisplay);
        PublisherDisplay publisherDisplay = publishersDisplay[publisherMaintenanceForm.getSelectedRow()];
        request.getSession().setAttribute(WebConstants.SessionKeys.SELECTED_PUBLISHER,publisherDisplay.getPublisher());
        return mapping.findForward(EDIT_PUBLISHER);                                         
    }
    
    public ActionForward deletePublisher( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response){
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_PUBLISHERINFO );
        ActionMessages actionErrors = new ActionMessages();
        //go through collection of publisher recorsds mark for delete then send the list to the service
        ArrayList<RLinkPublisher> lstPublishersToDelete = new ArrayList<RLinkPublisher>();
        PublisherMaintenanceForm publisherMaintenanceForm = castForm( PublisherMaintenanceForm.class, form );
        PublisherDisplay publisherDisplay = publisherMaintenanceForm.getPublishersDisplay().get( publisherMaintenanceForm.getSelectedRow());
        lstPublishersToDelete.add(publisherDisplay.getPublisher());
        try {
            //call delete service with lstPublishersToDelete
            //if delete is successful, just refresh the page
            for (RLinkPublisher rLinkPublisher: lstPublishersToDelete) {
                RLinkPublisherServices.deleteRLinkPublisher(rLinkPublisher);
            }
            return mapping.findForward(SUCCESS);
        }
        catch(Exception exc){
            //record error and show page with error
        	_logger.error(LogUtil.getStack(exc));
            ActionMessage actionError = new ActionMessage("admin.publisher.error.deleteFailed");
            actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
            return mapping.findForward(SHOW_MAIN);
        }
        finally {
            if ( !actionErrors.isEmpty() )  this.saveErrors( request, actionErrors );
        }                                         
    }
}
