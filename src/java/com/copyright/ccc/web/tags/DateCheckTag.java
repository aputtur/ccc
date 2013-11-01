package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 * Tag handler that checks a page scoped bean's Date object and displays a Billing Date
 * warning in the JSP if the current date is within a specified number of days from the
 * bean's Start of Term date. This tag implementation is defined in cc2-util.tld.
 * 
 * The tag defaults to a threshold of 55 days after the Start of Term.
 *
 * This tag uses the following form values: 
 * "bean" is a reference to the page scoped bean that contains the string property 
 * to subject to truncation.
 * 
 * "property" is the bean property that contains the string the tag processes.
 * 
 * "threshold" is the number of days till the bean Date after which a bill warning 
 * message will be displayed.
 *
 */
public class DateCheckTag extends TagSupport 
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected String bean;     // The page scoped bean to interrogate for a Date object
   protected String property; // The bean property name for the Date
   protected String threshold; // The warning threshold after the bean Date in days

   private final static long fiftyFiveDays = 4752000000L; // 55*24*60*60000 => 55 days is default threshold
   private long cutoff = fiftyFiveDays;
   private final static long fiveDays = 432000000L; //5*24*60*60000 => 5 days is default threshold
   public DateCheckTag() { }

   @Override
   public int doStartTag() throws JspException 
   {
       Date now = new Date();
       TagUtils utils = TagUtils.getInstance();

       Date startOfTerm = (Date) utils.lookup(pageContext, bean, property, "page");

       if (startOfTerm != null) 
       {
           if (this.threshold != null && !"".equals(this.threshold)) 
           {
              try { 
                 long days = Long.parseLong(this.threshold);
                 cutoff = days * 8640000L;
              } catch (NumberFormatException nfe) {
                 cutoff = fiftyFiveDays; // default to 55 days
              }
           }
           // Determine if we are within the threshold period after the start of term
           long time = now.getTime();
           long termTime = startOfTerm.getTime() + cutoff;
           //show the message for 5 days 
           if (time >= termTime && time < (termTime + fiveDays)) 
           {
               String html = "<b style=\"color:#FF0000;\">APPROACHING BILLING DATE</b><br /><div style=\"float: right;\"><a href=\"http://support.copyright.com/index.php?action=article&id=116&relid=22\">More Info</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>";
               // Output billing date warning
               try { pageContext.getOut().print(html); }
               catch (IOException ioe) { 
                  throw new JspException("Tag.DateCheckTag: IOException while writing to client" + ioe.getMessage());
               }
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

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getThreshold() {
        return threshold;
    }
}
