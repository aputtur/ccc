package com.copyright.ccc.web.tags.security;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.tags.ConditionTestTag;
import com.copyright.workbench.security.SecurityRuntimeException;

public class IfUserHasPrivilegeTag extends ConditionTestTag
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String _code;
    
	@Override
    public boolean testCondition()
    {
        try
        {
            UserContextService.getUserContext();
        }
        catch ( SecurityRuntimeException e )
        {
            return false;
        }

        CCPrivilegeCode codeObj = null;
        
        if ( _code == null )
            throw new CCRuntimeException( "unset privilege code" );
        else if ( _code.equals( "commitOrderAdjustment" ) )
            codeObj = CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT;
        else if ( _code.equals( "emulate" ) )
            codeObj = CCPrivilegeCode.EMULATE_USER;
        else if ( _code.equals( "enterOrderAdjustment" ) )
            codeObj = CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT;
        else if ( _code.equals( "manageRoles" ) )
            codeObj = CCPrivilegeCode.MANAGE_ROLES;
        else if ( _code.equals( "viewReports" ) )
            codeObj = CCPrivilegeCode.VIEW_ENTERPRISE_REPORTS;
        else if ( _code.equals( "any" ) )
        {
            if ( UserContextService.hasPrivilege( CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT ) ||
                 UserContextService.hasPrivilege( CCPrivilegeCode.EMULATE_USER ) ||
                 UserContextService.hasPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT ) ||
                 UserContextService.hasPrivilege( CCPrivilegeCode.MANAGE_ROLES ) ||
                 UserContextService.hasPrivilege( CCPrivilegeCode.VIEW_ENTERPRISE_REPORTS ) ||
                 UserContextService.hasPrivilege(CCPrivilegeCode.MANAGE_PUBLISHERINFO))
                    return true;
            else
                return false;
        }
        else
            throw new CCRuntimeException( "unknown privilege code: " + _code );
        
        return UserContextService.hasPrivilege( codeObj );
    }

    public void setCode( String privilegeCode )
    {
        _code = privilegeCode;
    }

    public String getCode()
    {
        return _code;
    }
}
