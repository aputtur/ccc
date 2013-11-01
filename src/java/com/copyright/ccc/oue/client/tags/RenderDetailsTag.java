/**
 * 
 */
package com.copyright.ccc.oue.client.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.copyright.ccc.oue.client.OpenUrlExtensionUtils;



/**
 * @author fgracely
 *
 */
public class RenderDetailsTag extends TagSupport
{

	private static final long serialVersionUID = 1L;


	@Override
	public int doStartTag() throws JspException 
	{

		try 
		{
			String html = OpenUrlExtensionUtils.getDetailsHtml(pageContext.getSession());
			if (html != null)
			{
				pageContext.getOut().write(html);
			}
		   
		} catch(IOException e) {
			throw new JspTagException("An IOException occurred.");
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public void release()
	{
		super.release();
	}



 


}
