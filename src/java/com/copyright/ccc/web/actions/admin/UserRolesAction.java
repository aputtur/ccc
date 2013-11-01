package com.copyright.ccc.web.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.web.forms.admin.UserRolesForm;
import com.copyright.data.security.Role;
import com.copyright.opi.data.ModState;

public class UserRolesAction extends AdminAction
{
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    
    private static final String MSG_USERNAME_MISSING  = "admin.error.username.input.missing";
    private static final String MSG_USERNAME_NOTFOUND = "admin.error.username.notfound";
    private static final String MSG_USERNAME_INVALID  = "admin.error.username.format.invalid";
    private static final String MSG_APPLICATION_ERROR  = "admin.error";
    
    public ActionForward defaultOperation( ActionMapping mapping 
                                          ,ActionForm form 
                                          ,HttpServletRequest request 
                                          ,HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_ROLES );
        
        ActionMessages errors = new ActionMessages();
        UserRolesForm editForm = castForm( UserRolesForm.class, form );
        
        populateForm(editForm, errors);
        
        if ( !errors.isEmpty() )  this.saveErrors( request, errors );
        
        return mapping.findForward(SHOW_MAIN);
    }

    public ActionForward resetUser( ActionMapping mapping 
                                  ,ActionForm form 
                                  ,HttpServletRequest request 
                                  ,HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_ROLES );
        
        ActionMessages errors = new ActionMessages();
        UserRolesForm editForm = castForm( UserRolesForm.class, form );
        
        editForm.setUser( null );
        editForm.setUserRoles( null );
        editForm.setSavedUserRoleCodes( null );
        editForm.setDoFind( false );

        populateForm(editForm, errors);

        if ( !errors.isEmpty() ) this.saveErrors( request, errors );

        return mapping.findForward(SHOW_MAIN);
    }
    
    public ActionForward findUser( ActionMapping mapping 
                                  ,ActionForm form 
                                  ,HttpServletRequest request 
                                  ,HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_ROLES );
        
        ActionMessages errors = new ActionMessages();
        UserRolesForm editForm = castForm( UserRolesForm.class, form );
        
        if ( StringUtils.isEmpty( editForm.getUsername() ) ) {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( MSG_USERNAME_MISSING ) );            
        } else {
            editForm.setDoFind( true );
            populateForm(editForm, errors);
        }
        
        if ( !errors.isEmpty() ) this.saveErrors( request, errors );
        
        return mapping.findForward(SHOW_MAIN);
    }
    
    public ActionForward saveUserRoles ( ActionMapping mapping 
                                  ,ActionForm form 
                                  ,HttpServletRequest request 
                                  ,HttpServletResponse response)
    {
        UserContextService.checkPrivilege( CCPrivilegeCode.MANAGE_ROLES );

        ActionMessages errors = new ActionMessages();
        
        UserRolesForm editForm           = castForm( UserRolesForm.class, form );
        String[]      savedUserRoleCodes = editForm.getSavedUserRoleCodes();
        Role[]        savedUserRoles     = new Role[savedUserRoleCodes.length];
        User          user               = getUser( editForm.getUsername(), errors );
        Role[]        origUserRoles      = getUserRoles( user, errors );
        Role[] allRoles = getApplicationRoles( errors );
        
        if ( errors.isEmpty() ) {
        
            for(int i = 0; i < savedUserRoleCodes.length; i++)
            {
                savedUserRoles[i] = getRoleForCode( savedUserRoleCodes[i], allRoles );
            }        
            Role[] addedRoles = new Role[0];
            Role[] removedRoles = new Role[0];
            
            if(ArrayUtils.isEmpty(origUserRoles))
                addedRoles = savedUserRoles;
            else if(ArrayUtils.isEmpty(savedUserRoles))
                removedRoles = origUserRoles;
            else
            {
                addedRoles = getDifference(savedUserRoles, origUserRoles);
                removedRoles = getDifference(origUserRoles, savedUserRoles);
            }

            if ( (addedRoles.length + removedRoles.length) > 0 ) {  
            
                Role[] rolesToSave = 
                    new Role[addedRoles.length + removedRoles.length];
                
                for(int i = 0; i < addedRoles.length; i++)
                {
                    Role roleToAdd = addedRoles[i];
                    roleToAdd.setModState(ModState.NEW);
                    rolesToSave[i] = roleToAdd;
                }
                int startingIndex = addedRoles.length;
                for(int i = startingIndex; i < rolesToSave.length; i++)
                {
                    int removedRoleIndex = i - startingIndex;
                    Role roleToRemove = removedRoles[removedRoleIndex];
                    roleToRemove.setModState(ModState.DELETED);
                    rolesToSave[i] = roleToRemove;
                }
                
                _logger.debug("Saving roles for user" + editForm.getUsername());
                
                UserServices.save( user, rolesToSave);  
                return resetUser( mapping, form, request, response);
            }
        }
    
        populateForm(editForm, errors);
        
        if ( !errors.isEmpty() )  this.saveErrors( request, errors );
    
        return mapping.findForward(SHOW_MAIN);
        
    }
    
    private Role getRoleForCode( String matchCode, Role[] roles) {
        Role matchRole = null;
        for(int i = 0; i < roles.length; i++) {
            if ( matchCode.equalsIgnoreCase( roles[i].getRoleCode().getCode())) {
                matchRole = roles[i];
                break;
            }
        }
        return matchRole;
    }
    
    private Role[] getDifference ( Role[] fromRoles, Role[] toRoles ) {
        int diffCount = 0;
        String[] diffCodes = new String[fromRoles.length];
//        int ckF = fromRoles.length;
       // int ckT = toRoles.length;
        for( int i=0; i<fromRoles.length; i++) {
             Role checkRole = getRoleForCode ( fromRoles[i].getRoleCode().getCode(), toRoles );
             if ( checkRole == null ) {
                  diffCodes[diffCount++] = fromRoles[i].getRoleCode().getCode();
             }
        }
        Role[] diffRoles = new Role[diffCount];
        for(int i = 0; i < diffCount; i++) {
           diffRoles[i] = getRoleForCode ( diffCodes[i], fromRoles );
        }
        return diffRoles;
    }
    
    private void populateForm (UserRolesForm editForm, ActionMessages errors ) {
    
        if ( editForm.getApplicationRoles() == null ) {
          editForm.setApplicationRoles( UserServices.getDefaultCC2ApplicationRoles() );
        }
        
        if ( StringUtils.isNotBlank( editForm.getUsername() ) && editForm.isDoFind() ) {
        
            User user = getUser( editForm.getUsername(), errors );
            Role[] userRoles = getUserRoles( user, errors );
            
            if ( errors.isEmpty() ) {
               editForm.setUser( user );
               editForm.setUserRoles( userRoles );
               editForm.setSavedUserRoleCodes(createRoleCodesFromRoles(userRoles));
            }
        }
    }

    private User getUser( String userName, ActionMessages errors ) {
        
        User gUser = UserServices.getSharedUserForUsername( userName );
        if ( gUser == null ) {
           errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( MSG_USERNAME_NOTFOUND, userName ) );            
        } 
        return gUser;
    }

    private Role[] getUserRoles( User user, ActionMessages errors ) {
        Role[] userRoles = null;
        if ( user != null ) {
            userRoles = UserServices.getSharedUserRolesForUser( user ) ;
        }
        return userRoles;
    }

    private Role[] getApplicationRoles( ActionMessages errors ) {
        Role[] appRoles = UserServices.getDefaultCC2ApplicationRoles(); 
        if ( appRoles == null ) {
           errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( MSG_APPLICATION_ERROR, "Default User Roles" ) );                                
        }
        return appRoles;
    }
    
    private String[] createRoleCodesFromRoles(Role[] roles)
    {
        String[] roleCodes = new String[roles.length];
        for(int i = 0; i < roleCodes.length; i++)
        {
            roleCodes[i] = roles[i].getRoleCode().getCode();
        }
        
        return roleCodes;
    }
}
