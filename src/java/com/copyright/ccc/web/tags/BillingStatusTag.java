package com.copyright.ccc.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 * Tag handler that checks a page scoped bean's OrderLicense object and displays the 
 * Billing Status as an HTML snippet.
 * This tag implementation is defined in cc2-util.tld.
 * 
 * For a billing status of "Invoiced" the tag will also append ":" and the invoice number
 */
public class BillingStatusTag extends TagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String bean; // The page scoped bean for getting billing status

    public BillingStatusTag() { }
    
    @Override
    public int doStartTag() throws JspException 
    {
        String boldStart = "<b>";
        String boldend = "</b>";
        StringBuffer html = new StringBuffer("Billing Status:<br />");

        TagUtils utils = TagUtils.getInstance();

        String status = (String) utils.lookup(pageContext, bean, "billingStatus", "page");

        if ("invoiced".equalsIgnoreCase(status)) 
        {
            String invoiceNum = (String) utils.lookup(pageContext, bean, "invoiceId", "page");
            if (invoiceNum != null && !"".equals(invoiceNum)) 
            {
               html.append(boldStart + status + ": " + invoiceNum + boldend);    
            } else {
               html.append(boldStart + status + boldend);   
            }
        } else {
            html.append(boldStart + status + boldend);
        }

        try { pageContext.getOut().print(html.toString()); }
        catch (IOException ioe) { 
           throw new JspException("Tag.BillingStatusTag: IOException while writing to client" + ioe.getMessage());
        }

      //  return SKIP_BODY;
        return EVAL_BODY_INCLUDE;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getBean() {
        return bean;
    }
}
