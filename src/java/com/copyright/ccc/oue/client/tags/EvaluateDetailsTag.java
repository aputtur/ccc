package com.copyright.ccc.oue.client.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.oue.client.OpenUrlExtensionUtils;

public class EvaluateDetailsTag extends SimpleTagSupport
{
	@Override
	public void doTag() throws JspException
	{
		boolean test = false;
		try
		{
			PageContext pageContext = (PageContext) getJspContext();

			String html = OpenUrlExtensionUtils.getDetailsHtml(pageContext.getSession());
			if (html != null)
			{
				test = true;
			}

			JspWriter jspWriter = pageContext.getOut();

			StringWriter sw = new StringWriter();
			if (test)
			{
				JspFragment body = getJspBody();
				body.invoke(sw);
			}
			jspWriter.write(sw.toString());
		}
		catch (IOException e)
		{
			throw new CCRuntimeException( e );
		}

	}

}
