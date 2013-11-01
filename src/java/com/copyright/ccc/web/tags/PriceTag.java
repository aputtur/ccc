package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ProductEnum;

/**
 * Tag handler that checks a page scoped OrderLicense bean's Price attribute and displays 
 * the price formatted for local currency. 
 * 
 * This tag implementation is defined in cc2-util.tld.
 * 
 */
public class PriceTag extends TagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String bean;  // The page scoped bean to interrogate for a price
    protected String label; // Optional label before price value

    private final static char granted = 'G';
    private final static char denied = 'D';
    private final static char contactRightsholder = 'C';
    private final static char contactRightsholderDirectly = 'I';
    private final static char researchFurther = 'R';
    private final static char canceled = 'X';
    private final static int AWAITING_CUST_REPLY = 320;
     
    public PriceTag() { }

    @Override
    public int doStartTag() throws JspException 
    {
        TagUtils utils = TagUtils.getInstance();

        Boolean isSpecialOrder = (Boolean) utils.lookup(pageContext, bean, "specialOrder", "page");
        boolean isSpecOrder = false;
        if (isSpecialOrder != null) {
            isSpecOrder = isSpecialOrder.booleanValue();
        }      
        
        char statusCode = 'z'; // default represents no valid code
        String code = (String) utils.lookup(pageContext, bean, "itemAvailabilityCd", "page");
        
        if (code != null) {
           char[] permStatus = code.toCharArray();
           if (permStatus.length > 0) {
             statusCode = permStatus[0];
           }
        } else {
        	statusCode = 'R';
        }

        if (isSpecOrder) 
        {
            // Check the status codes from SS
            switch (statusCode) 
            {
               case granted: 
                  this.getPrice(utils);
               break;
               case denied:
                  String odtStatusCd = (String) utils.lookup(pageContext, bean, "licenseStatusCd", "page");
                  int intOdtStatusCd = -1;
                  try {
                      Integer iCode = Integer.valueOf(odtStatusCd); 
                      if (code != null) {
                         intOdtStatusCd = iCode.intValue();
                      }
                  } catch (NumberFormatException nfe) {
                     intOdtStatusCd = -1; 
                  }

                  if (intOdtStatusCd == AWAITING_CUST_REPLY) {
                     this.getTBDPrice();
                  } else {
                     this.getZeroPrice();
                  }
               break;
               case contactRightsholder:
                   String productCd = (String) utils.lookup(pageContext, bean, "productCd", "page");
                   Double price = (Double) utils.lookup(pageContext, bean, "priceValueRaw", "page");
                   if (productCd.equalsIgnoreCase(ProductEnum.RLR.name())) {
                	   if (price == ItemConstants.RL_NOT_PRICED) {
                		   this.getTBDReprintPrice();
                	   } else {
                    	   this.getPrice(utils);
                	   }
                   } else {
                	   this.getTBDPrice();
           		   }    
            	break;
               case researchFurther:
                  this.getTBDPrice();
               break;
               case contactRightsholderDirectly:
                  this.getZeroPrice();
               break;
               case canceled:
                  this.getZeroPrice();
               break;
            }
        } else {
            // It's not a special order so just show the price
            this.getPrice(utils);    
        }

        return SKIP_BODY;
    }

    private void getPrice(TagUtils utils) throws JspException
    {
        Double price = (Double) utils.lookup(pageContext, bean, "priceValue", "page");

        if (price != null) 
        {
            try {
                double priceNum = price.doubleValue();
                String priceString = NumberFormat.getCurrencyInstance().format(priceNum);
                if (this.label != null && !"".equals(label)) {
                   priceString = label + priceString; 
                }
                try { pageContext.getOut().print(priceString); }
                catch (IOException ioe) { 
                   throw new JspException("Tag.PriceTag: IOException while writing to client" + ioe.getMessage());
                }
            } catch (Exception e) {
               throw new JspException("Tag.PriceTag: ParseException while writing to client" + e.getMessage());
            }
        }        
    }

    private void getZeroPrice() throws JspException 
    {
        String price = "$0.00";
        if (this.label != null && !"".equals(label)) {
           price = label + price; 
        } 
        try { pageContext.getOut().print(price); }
        catch (IOException ioe) { 
           throw new JspException("Tag.PriceTag: IOException while writing to client" + ioe.getMessage());
        }
    }

    private void getTBDPrice() throws JspException 
    {
        String price = "$TBD<br />(Special Order)";
        if (this.label != null && !"".equals(label)) {
           price = label + price; 
        } 
        try { pageContext.getOut().print(price); }
        catch (IOException ioe) { 
           throw new JspException("Tag.PriceTag: IOException while writing to client" + ioe.getMessage());
        }
    }

    private void getTBDReprintPrice() throws JspException 
    {
        String price = "$TBD<br />(Reprint)";
        if (this.label != null && !"".equals(label)) {
           price = label + price; 
        } 
        try { pageContext.getOut().print(price); }
        catch (IOException ioe) { 
           throw new JspException("Tag.PriceTag: IOException while writing to client" + ioe.getMessage());
        }
    }
    
    public void setBean(String bean) {
       this.bean = bean;
    }

    public String getBean() {
       return bean;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
