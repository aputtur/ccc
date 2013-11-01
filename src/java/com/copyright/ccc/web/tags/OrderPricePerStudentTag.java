package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.copyright.ccc.business.data.OrderLicense;

/**
 * Tag handler that checks a request scoped form's OrderLicense List to calculate the
 * OrderPricePerStudent for the total Purchase.  The calculation requires that all
 * Licenses in the List have the same number of students.  If the number of students 
 * is 0 or two or more licenses don't have the same number of students nothing is rendered
 * 
 * This tag implementation is defined in cc2-util.tld.
 *
 */
public class OrderPricePerStudentTag extends TagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderPricePerStudentTag() { }
    
    protected String bean; // The page scoped bean to interrogate for a price
    
    @Override
    public int doStartTag() throws JspException 
    {
        TagUtils utils = TagUtils.getInstance();

        @SuppressWarnings("unchecked")
        List<OrderLicense> licenses = (List<OrderLicense>) utils.lookup(pageContext, bean, "orderDetails", "session");

        double total = 0D;

        if (licenses != null) 
        {
            try {
                 int lastNumOfStudents = 0;
                 int numOfStudents = 0;
                 boolean check = false;

                 Iterator<OrderLicense> itr = licenses.iterator();
                 while (itr.hasNext()) 
                 {
                    OrderLicense aLicense = itr.next();  
                    numOfStudents = aLicense.getNumberOfStudents();

                    // If one license has 0 students we can't calc price per student
                    if (numOfStudents == 0) { return SKIP_BODY; }
                    else if (check) {
                       if (numOfStudents != lastNumOfStudents) {
                          return SKIP_BODY;
                       }
                    } else { check = true; }

                    lastNumOfStudents = numOfStudents;
                    double price = aLicense.getPriceValue();
                    total += price;
                 }

                 total = total / lastNumOfStudents;

                 String priceString = NumberFormat.getCurrencyInstance().format(total);
                 String output = "(" + priceString + " per student)";
                 try { pageContext.getOut().print(output); }
                 catch (IOException ioe) { 
                    throw new JspException("Tag.OrderPricePerStudentTag: IOException while writing to client " + ioe.getMessage());
                 }
            } catch (Exception e) {
                throw new JspException("Tag.OrderPricePerStudentTag: ParseException while writing to client " + e.getMessage());
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
