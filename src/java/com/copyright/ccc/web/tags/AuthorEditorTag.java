package com.copyright.ccc.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 * Tag handler that checks a page scoped OrderLicense bean's Author and Editor Strings and
 * displays them with the label "<b>Author/Editor:</b>&nbsp;" and separated by a "/" if both the author
 * and editor fields are nonblank.  If both fields are blank no output is written to the 
 * page context output stream.  If both the author and editor are present the author is
 * written first.
 * 
 * This tag implementation is defined in cc2-util.tld.
 * 
 */
public class AuthorEditorTag extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthorEditorTag() { }
    
    protected String bean; // The page scoped bean to interrogate for the author & editor Strings
    private final static String label = "<b>Author/Editor:</b>&nbsp;";
    
    @Override
    public int doStartTag() throws JspException 
    {
       TagUtils utils = TagUtils.getInstance();

       String author = (String) utils.lookup(pageContext, bean, "author", "page");
       String editor = (String) utils.lookup(pageContext, bean, "editor", "page");
       StringBuffer buffer = new StringBuffer();
           
       try {           
           if (author != null && !"".equals(author)) 
           {
               buffer.append(label);
               buffer.append(author);
               if (editor != null && !"".equals(editor)) {
                   buffer.append("/");
                   buffer.append(editor);
               }
               
               pageContext.getOut().print(buffer.toString());

           } else if (editor != null && !"".equals(editor)) {
               buffer.append(label);
               buffer.append(editor);
               
               pageContext.getOut().print(buffer.toString());
           }
       } catch (IOException ioe) { 
          throw new JspException("Tag.AuthorEditorTag: IOException while writing to client " + ioe.getMessage());
       } catch (Exception e) {
          throw new JspException("Tag.AuthorEditorTag: ParseException while writing to client " + e.getMessage());
       }

       return SKIP_BODY;
    }

     public void setBean(String bean) {
       this.bean = bean;
     }

     public String getBean() {
       return bean;
     }
}
