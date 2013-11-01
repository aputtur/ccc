package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.copyright.ccc.business.data.OrderLicense;

public class OrderTotalTag extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderTotalTag() { }
    
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
                 Iterator<OrderLicense> itr = licenses.iterator();
                 while (itr.hasNext()) 
                 {
                    OrderLicense aLicense = itr.next();  
                    double price = aLicense.getPriceValue();
                    total += price;
                 }

                 String priceString = NumberFormat.getCurrencyInstance().format(total);
                 try { pageContext.getOut().print(priceString); }
                 catch (IOException ioe) { 
                    throw new JspException("Tag.OrderTotalTag: IOException while writing to client " + ioe.getMessage());
                 }
             } catch (Exception e) {
                throw new JspException("Tag.OrderTotalTag: ParseException while writing to client " + e.getMessage());
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
