package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.UserAlertForm;
import com.copyright.ccc.web.forms.UserLookupForm;
import com.copyright.ccc.web.util.WebAdmin;

/**
 * Administrative action that handles user updates as well as alert messages.
 *
 * @author Michael Jessop &lt;&gt;
 * @version 1.0
 *
 * This class/action contains the methods used in performing user and site
 * management tasks such as setting the alert message, and modifying user
 * account information.
 *
 * changes          Please enter any changes you make here.  Put the most
 *                  recent changes at the TOP of the list, please.
 *
 * when         who         what
 * ----------   ----------  ------------------------------------------------
 * 2009/07/23   Jessop      Commenting out all UserServices references.
 * 2007/12/04   Jessop      Added action to view users by account.
 * 2007/10/15   Jessop      Created this module.
 *
 */
public final class UserAdminAction extends CCAction
{
    //  Couple of useful constants...
    
    private static final String SUCCESS = "success";
    
    //  Constants that map to forward actions specified in the
    //  userAdmin action in struts-config.xml.
    
    private static final String USER_LOOKUP_PAGE = "showUserLookup";
    private static final String USER_ALERT_PAGE = "showUserAlert";
    
    //  Constants that represent the global form names for the
    //  several forms we use in administering our users.
    
    private static final String USER_LOOKUP_FORM = "userLookupForm";
    private static final String USER_ALERT_FORM = "userAlertForm";
    
    //  Constructor.
    
    public UserAdminAction() {
        super();
    }
    
    //  The default operation is called when no operation parameter
    //  was specified in the URL.
    
    public ActionForward defaultOperation( ActionMapping       mapping
                                         , ActionForm          form
                                         , HttpServletRequest  request
                                         , HttpServletResponse response )
    {
        return show(mapping, form, request, response);
    }
    
    //  This is the default operation.  This function checks for user
    //  roles/privileges and redirects to display the user lookup page.
    
    public ActionForward show( ActionMapping       mapping
                             , ActionForm          form
                             , HttpServletRequest  request
                             , HttpServletResponse response )
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.VIEW_USERINFO );
        
        UserLookupForm lookupForm = new UserLookupForm();
        
        if (_logger.isDebugEnabled())
        {
            _logger.debug("UserAdminAction :: show (default operation)");
        }
        request.setAttribute(USER_LOOKUP_FORM, lookupForm);
        
        return mapping.findForward(USER_LOOKUP_PAGE);
    }
    
    //  Routes the administrator to the alert status and message editing
    //  page.  This is the message that appears on the login screen
    //  informing the user things like the site will be going down at a
    //  certain date and time.
    
    public ActionForward status( ActionMapping       mapping
                               , ActionForm          form
                               , HttpServletRequest  request
                               , HttpServletResponse response )
    {
        UserContextService.checkPrivilege(CCPrivilegeCode.VIEW_USERINFO);
        
        WebAdmin webAdmin = new WebAdmin();
        
        UserAlertForm alertForm = 
            new UserAlertForm( webAdmin.get(WebAdmin.USERALERT_FLAG)
                             , webAdmin.get(WebAdmin.USERALERT_MESSAGE_TEXT) );
        
        request.setAttribute(USER_ALERT_FORM, alertForm);
        
        return mapping.findForward(USER_ALERT_PAGE);
    }
    
    //  This action sets the alert status and message and returns
    //  to the page that allows the adminstrator to change the alert
    //  status.
    
    public ActionForward alert( ActionMapping       mapping
                              , ActionForm          form
                              , HttpServletRequest  request
                              , HttpServletResponse response )
    {
        UserContextService.checkPrivilege(CCPrivilegeCode.MANAGE_USERINFO);
        
        UserAlertForm alertForm = castForm( UserAlertForm.class, form );
        WebAdmin webAdmin = new WebAdmin();
        
        webAdmin.update(WebAdmin.USERALERT_FLAG, alertForm.getFlag());
        webAdmin.update(WebAdmin.USERALERT_MESSAGE_TEXT, alertForm.getMessage());
        
        request.setAttribute(USER_ALERT_FORM, alertForm);
        
        return mapping.findForward(SUCCESS);
    }
}