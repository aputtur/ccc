package com.copyright.ccc.web.tags;

import java.util.Stack;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Provides access to a page-scope stack (LIFO) of boolean values.
 * <p>
 * The stack is meant to be used to store the results of
 * boolean logic comparison custom tags, mainly for the
 * implementation of the <tt>ElseTag</tt> tag.
 * <p>
 * Note that the pop operation removes the last-inserted value
 * from the stack and stores it in a page-scope variable.  It
 * can then be retrieved with <tt>lastValue()</tt>.
 */
public abstract class BooleanLogicTag extends TagSupport
{
	private static final long serialVersionUID = 1L;
	
    /**
     * Page-context key with which to store evaluation value stack
     */
    static String STACK_PC_ID = "__CC2_CONDITIONAL_TAG_EVAL_STACK__";
    static String LAST_VAL_ID = "__CC2_CONDITIONAL_TAG_LAST_EVAL__";
 
    private Stack<Boolean> getPageEvalStack()
    {
    	@SuppressWarnings("unchecked")
        Stack<Boolean> evalStack = (Stack<Boolean>) pageContext.getAttribute( STACK_PC_ID );
        
        if ( evalStack == null )
        {
            evalStack = new Stack<Boolean>();
            pageContext.setAttribute( STACK_PC_ID, evalStack );
        }
        
        return evalStack;
    }
    
    /**
     * Get the last-inserted value off the stack
     * 
     * @return the last-inserted value off the stack
     */
    public final boolean popValue()
    {
        Boolean lastVal = getPageEvalStack().pop();
        pageContext.setAttribute( LAST_VAL_ID, lastVal );
        return lastVal.booleanValue();
    }
    
    public final boolean lastValue()
    {
        return ( ( Boolean ) pageContext.getAttribute( LAST_VAL_ID ) ).booleanValue();
    }
    
    /**
     * Push a value onto the page-scope stack
     * @param val the value
     */
    public final void pushValue( boolean val )
    {
        getPageEvalStack().push( Boolean.valueOf( val ) );
    }
}
