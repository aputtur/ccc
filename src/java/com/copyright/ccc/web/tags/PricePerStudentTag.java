package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 * Tag handler that checks a page scoped OrderLicense bean's Price attribute and displays 
 * the price per student if there are any students.  Note that this class does not know
 * if the License is an academic license since that information is in the Purchase.  Thus
 * a logic tag must be used to conditionally execute this tag only if the license is an
 * academic license.
 * 
 * This tag implementation is defined in cc2-util.tld.
 *
 */
public class PricePerStudentTag extends TagSupport 
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected String bean;  // The page scoped bean to interrogate for a price per student

   public PricePerStudentTag() { }
   
   @Override
   public int doStartTag() throws JspException 
   {
        TagUtils utils = TagUtils.getInstance();

        Double price = (Double) utils.lookup(pageContext, bean, "priceValue", "page");

        if (price != null) 
        {
            try {
                double priceNum = price.doubleValue();
                Integer students = (Integer) utils.lookup(pageContext, bean, "numberOfStudents", "page");
                if (students != null) {
                   int numOfStudents = students.intValue();
                   if (numOfStudents > 0) 
                   {
                       double pricePerStudent = priceNum / numOfStudents;
                       String priceString = NumberFormat.getCurrencyInstance().format(pricePerStudent);
                       StringBuffer buffer = new StringBuffer("(" + priceString + " per student)");
                       
                       try { pageContext.getOut().print(buffer.toString()); }
                       catch (IOException ioe) { 
                          throw new JspException("Tag.PricePerStudentTag: IOException while writing to client " + ioe.getMessage());
                       }
                   }
                }
            } catch (Exception e) {
               throw new JspException("Tag.PricePerStudentTag: ParseException while writing to client " + e.getMessage());
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
}
