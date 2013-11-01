package com.copyright.ccc.web.forms;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ContextualHelpForm extends ActionForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _helpTitleId;
    private String _helpBodyId;


    public void setHelpTitleId(String helpTitleId)
    {
        this._helpTitleId = helpTitleId;
    }

    public String getHelpTitleId()
    {
        return _helpTitleId;
    }

    public void setHelpBodyId(String helpBodyId)
    {
        this._helpBodyId = helpBodyId;
    }

    public String getHelpBodyId()
    {
        return _helpBodyId;
    }
    
    private boolean isNumeric(String value)
    {
        try
        {
            int i = Integer.parseInt(value, 10);
            return true;
        }
        catch (NumberFormatException e)
        {
            //  no need to do anything.
        }
        return false;
    }
    
    @Override
    public ActionErrors validate( ActionMapping      mapping
                                , HttpServletRequest request )
    {
        ActionErrors messages = super.validate( mapping, request );
        
        if (!isNumeric(_helpTitleId) || !isNumeric(_helpBodyId))
        {
            messages.add( ActionMessages.GLOBAL_MESSAGE
                        , new ActionMessage("errors.help.invalidID") );
        }
        return messages;
    }
}
