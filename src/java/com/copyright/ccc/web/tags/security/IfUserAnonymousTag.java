package com.copyright.ccc.web.tags.security;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.web.tags.ConditionTestTag;
import com.copyright.workbench.security.SecurityRuntimeException;

public class IfUserAnonymousTag extends ConditionTestTag
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean testCondition()
    {
        try
        {
           return UserContextService.isUserAnonymous();
        }
        catch ( SecurityRuntimeException e )
        {
            return false;
        }
    }
}
