package com.copyright.ccc.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.forms.admin.EmulationForm;
import com.copyright.workbench.security.SecurityRuntimeException;

public class EmulationAction extends AdminAction
{
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String STOP = "stop";
    
    public ActionForward start( ActionMapping mapping, 
                                                   ActionForm form, 
                                                   HttpServletRequest request, 
                                                   HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.EMULATE_USER );

        EmulationForm actionForm = castForm( EmulationForm.class, form );
        ActionMessages errors = new ActionMessages();
        String username = actionForm.getEmulationUsername();
        
        if ( StringUtils.isEmpty( username ) ) {
        	errors.add("general", new ActionMessage("admin.error.username.input.missing"));
        } else {
        	username = username.trim();
        	if ( StringUtils.isEmpty( username ) ) {
        		errors.add("general", new ActionMessage("admin.error.username.input.missing"));
        	}
        }
        
        if ( !errors.isEmpty() ) {
        	request.setAttribute( Globals.ERROR_KEY, errors );
        	return mapping.findForward( FAILURE ); 
        }
        
        try
        {
        	if (UserServices.isCCUser(username) || UserServices.isRLUser(username)) {
               UserContextService.beginEmulation( username );
        	}
            else {
                ActionMessages m = new ActionMessages();
                m.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "Cannot Find User", false ) );
                this.addErrors( request, m );    
                return mapping.findForward( FAILURE );
        	}
        }
        catch ( SecurityRuntimeException e )
        {
            ActionMessages m = new ActionMessages();
            m.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( e.getMessage(), false ) );
            this.addErrors( request, m );
            
            return mapping.findForward( FAILURE );
        }
        
        return mapping.findForward( SUCCESS );
    }
    
    public ActionForward stop( ActionMapping mapping, 
                                                   ActionForm form, 
                                                   HttpServletRequest request, 
                                                   HttpServletResponse response)
    {
        // every user needs to be able to run this method, as we check privileges
        // on the ACTIVE user.  If we are emulating a non-privileged user (which
        // is the most common case), we must be able to get out of emulation.
        
        // UserContextService.checkPrivilege( CCPrivilegeCode.EMULATE_USER );

//        EmulationForm actionForm = castForm( EmulationForm.class, form );

        if ( UserContextService.isEmulating() )
            UserContextService.stopEmulation();
    
        return mapping.findForward( STOP );
    }
}
