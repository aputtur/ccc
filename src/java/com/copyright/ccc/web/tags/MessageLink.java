package com.copyright.ccc.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 * Tag handler that truncates long strings and turns the string into a javascript
 * link to the Purchase detail page in order detail tab of order history.
 * 
 * The tag defaults to a string length or 45 characters and will truncate to 42 and 
 * add "... See more" to any string greater than 45 characters.  The cutoff limit 
 * for string size can be defined in the tag.  The tag assumes the struts action
 * 'orderDetail.do?id=xxxx' exists where the id is an order detial id.  If a user
 * needs a different action it is suggested that this class have an elective link 
 * attribute added called 'link' that can define the action in an <a> tag similar
 * to the current implementation for orderDetail.do.  If the link attribute is 
 * missing then the tag can default to it's current href.
 *
 * This tag uses the following form values: 
 * "bean" is a reference to the page scoped bean that contains the string property 
 * to subject to truncation.
 * 
 * "property" is the bean property that contains the string the tag processes.
 * 
 * "limit" is the number of characters to allow in a string before subjecting it
 * to truncation.  The string will be truncated to limit - 3 and have "..." added
 * to the end of it.
 * 
 */
public class MessageLink extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String bean; // The page scoped bean to interrogate for a message
    protected String property; // The String message to be inspected for truncation
    protected String limit;  // user definable truncation cutoff - not required
    private int cutoff = 45; // default text cutoff can be over ridden with limit
    private String object;
    private String confirmNumber;

    public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	private String msg;
    
    public MessageLink() { }
    
    /**
    * Access the Struts form bean in the given scope.  Determine the current display
    * page and total displayable pages by accessing the getter for the attributes
    * specified in the tag by "currentPage" and "totalPages".
    * @throws JspException
    */
    @Override
    public int doStartTag() throws JspException 
    {
    	if(object == null){
    		object = bean;
    	}
       if (limit != null && !"".equals(limit)) 
       {
          try { this.cutoff = Integer.parseInt(limit); }
          catch (Exception exc) { cutoff = 45; }
       }

       TagUtils utils = TagUtils.getInstance();

       String stringVal = (String) utils.lookup(pageContext, bean, property, "page");
       if (stringVal != null) 
       {
          this.msg = stringVal;

          int length = this.msg.length();
          if (length > cutoff) 
          {
             //Long purInst = (Long) utils.lookup(pageContext, object, "purInst", "page");
        	  
             if (getConfirmNumber() != null) {
            	 String[] cs = getConfirmNumber().split("\\.");
            	 if ( cs != null && cs.length == 2 ) {
                   Long purInst = (Long) utils.lookup(pageContext, cs[0], cs[1], "page"); 
                   long detailId = purInst.longValue();
                   // truncate 13 more than the cutoff index so we can add three ellipsis marks & 'See more'
                   this.msg = this.msg.substring(0, cutoff - 13) + " ... <a href=\"orderView.do?id=" + detailId + "\">See more</a>";
            	 }
             }
          }
    
          try { pageContext.getOut().print(this.msg); }
          catch (IOException ioe) { 
             throw new JspException("Tag.MessageLink: IOException while writing to client" + ioe.getMessage());
          }
       }
       
       return SKIP_BODY;
    }

    public void setBean(String bean) {
       this.bean = bean;
    }

    public String getBean() {
       return bean;
    }

    public void setLimit(String limit) {
       this.limit = limit;
    }

    public String getLimit() {
       return limit;
    }

    public void setProperty(String property) {
       this.property = property;
    }

    public String getProperty() {
       return property;
    }

	public String getConfirmNumber() {
		return confirmNumber;
	}

	public void setConfirmNumber(String confirmNumber) {
		this.confirmNumber = confirmNumber;
	}
}
