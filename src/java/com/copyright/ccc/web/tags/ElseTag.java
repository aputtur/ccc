package com.copyright.ccc.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ElseTag extends BooleanLogicTag
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public final int doStartTag() throws JspException
    {   
        if ( ! lastValue() )
            return TagSupport.EVAL_BODY_INCLUDE;
        else
            return TagSupport.SKIP_BODY;
    }
}
