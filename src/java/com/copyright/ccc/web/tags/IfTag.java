package com.copyright.ccc.web.tags;

/**
 * Simple <tt>ConditionTestTag</tt> that expects a Java expression in
 * its <tt>exprVal</tt> attribute (<tt>&lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;</tt>
 * for the <tt>exprVal</tt> attribute in the tag definition in the .tld file).  Tests
 * result of the expression's evaluation.
 */
public class IfTag extends ConditionTestTag
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean _exprVal;
    
	@Override
    public boolean testCondition()
    {
        return _exprVal;
    }

    public void setExprVal( boolean exprVal )
    {
        _exprVal = exprVal;
    }

    public boolean isExprVal()
    {
        return _exprVal;
    }
}
