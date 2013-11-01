package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 * Tag class for enforcing a line break in text that has no wrappable blanks
 * beyond a certain threshold.  The default width threshhold is 30 characters
 * but a width cutoff can be specified in the tag as width.  This tag inserts a
 * <br> tag for every string of continuous non-blank characters so a display field
 * won't exceed a certain width for layout.
 */
public class FieldWrapTag extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String bean; // The page scoped bean to interrogate for a form value
    protected String property; // The bean property indicating the field width to use for wrap boundary
    protected String width; // The field width to use for wrap boundary
    protected boolean filter; //The field indicating whether or not to replace html-sensitive characters with their entity equivalents
    protected boolean useWordBreak; //The field indicating whether or not to use <wbr> for the break so that when the text is copied, the line breaks are not copied
    private int threshhold = 30;
    private final static String blank = " ";
    private final static String brk = "<br/>";
    private final static String wrdBrk = "<wbr>";
    private StringBuffer buffer;

    public FieldWrapTag() { }
    
    /**
    * Access the Struts form bean in the page scope.  For a specified property of the 
    * bean truncate any text sequences between blanks that are longer than a threshold amount.
    * HTML tables will only wrap on blank boundaries so we must truncate long non blank char
    * sequences to avoid table cell expansion or overlap of text into other cells.
    * @return SKIP_BODY
    * @throws JspException
    */
    @Override
    public int doStartTag() throws JspException 
    {
        this.buffer = new StringBuffer();
        TagUtils utils = TagUtils.getInstance();
        if (this.width != null) {
            try {
                this.threshhold = Integer.parseInt(this.width);
            } catch (NumberFormatException nfe) {
                // set default text width limit to 30
                this.threshhold = 30;
            }
        }

        String text = (String) utils.lookup(pageContext, bean, property, "page");
        
        if (text != null) 
        {
             text = text.trim();
             
             //filter out sensitive html if filter is true
              if( filter )
                  text = TagUtils.getInstance().filter(text);
                    
             // tokenize the text by blanks since HTML tables will break text on blanks
             StringTokenizer tokenizer = new StringTokenizer(text, blank);
             
             while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();   
                this.buffer.append(this.breakText(token));
             }
             
             String html = "";
             int idx = buffer.lastIndexOf(blank);
             if (idx > 0) {
                html =  buffer.substring(0, idx);               
             } else {
                html = buffer.toString();
             }
             
             try { pageContext.getOut().print(html.trim()); }
             catch (IOException ioe) { 
                throw new JspException("Tag.FieldWrapTag: IOException while writing modified text to client" + ioe.getMessage());
             }
        }

        return SKIP_BODY;
    }

    private String breakText(String text) 
    {        
       String  brkString = (useWordBreak) ? wrdBrk : brk;
       
       String rtnValue = "";
       if (text.length() > this.threshhold) {
          String frag1 = text.substring(0, this.threshhold);
          String frag2 = text.substring(this.threshhold);
          frag2 = this.breakText(frag2);
          rtnValue = frag1 + brkString + frag2 + blank;
       } else {
          rtnValue = text + blank;
       }
       
       return rtnValue;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getBean() {
        return bean;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidth() {
        return width;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setFilter(boolean filter)
    {
        this.filter = filter;
    }

    public boolean isFilter()
    {
        return filter;
    }

    public void setUseWordBreak(boolean useWordBreak)
    {
        this.useWordBreak = useWordBreak;
    }

    public boolean isUseWordBreak()
    {
        return useWordBreak;
    }
}
