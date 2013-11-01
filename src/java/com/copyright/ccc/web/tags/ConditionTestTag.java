package com.copyright.ccc.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Provides methods for implementing JSP tags that test boolean conditions.
 * 
 * Implement <tt>testCondition()</tt> to provide a new custom tag that
 * tests some condition.
 * <p>
 * Optionally override <tt>uponCondition()</tt> to change the behavior of the
 * tag based on the result of <tt>testCondition()</tt>.  The default behavior
 * is to skip the tag body if <tt>testCondition()</tt> returns false, and
 * render the tag body otherwise.
 * <p>
 * Note that this class has a property called <tt>not</tt>.  If you specify
 * a tag attribute named "not" in your DTD, setting <tt>not="true"</tt> in
 * the JSP tag will flip the logic of the tag.
 */
public abstract class ConditionTestTag extends BooleanLogicTag
{
	private static final long serialVersionUID = 1L;
	
    /**
     * tag property that indicates boolean test should be flipped
     */
    private boolean _not = false;
    
    /**
     * Implement this method to provide condition-testing logic tag.
     */
    public abstract boolean testCondition();

    /**
     * Test the tag's condition, push the result onto the page-scope stack,
     * and return the result of <tt>uponCondition()</tt>.
     * 
     * @throws JspException
     */
    @Override
    public final int doStartTag() throws JspException
    {
        boolean cond = _not ^ testCondition();
                
        pushValue( cond );
        
        return uponCondition( cond );
    }
    
    /**
     * Pop the top value off the stack and squirrel it away for the else
     * tag's use.
     */
    @Override
    public final int doEndTag()
    {
        popValue();
        
        return TagSupport.EVAL_PAGE;
    }
    
    /**
     * Override this method to do something beside skip the tag body when
     * <tt>testCondition()</tt> returns false, and evaluate the body when
     * it returns true.
     * 
     * @param cond
     */
    public int uponCondition( boolean cond )
    {
        if ( cond )
            return TagSupport.EVAL_BODY_INCLUDE;
        else
            return TagSupport.SKIP_BODY;
    }
    
    public boolean getNot()
    {
        return _not;
    }
    
    public void setNot( boolean not )
    {
        _not = not;
    }
}
