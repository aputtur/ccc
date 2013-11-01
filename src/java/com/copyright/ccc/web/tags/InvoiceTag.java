package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.copyright.ccc.business.services.ItemConstants;

public class InvoiceTag  extends TagSupport
{
	private static final long serialVersionUID = 1L;
	
	protected String license;
	
	
	@Override
	 public int doStartTag() throws JspException 
	    {
		 TagUtils utils = TagUtils.getInstance(); 
		// Date invoiceDate = (Date)utils.lookup(pageContext, license, "invoiceDate", "page");
		 String paymentMethodCode = (String)utils.lookup(pageContext, license, "paymentMethodCd", "page");
		// long timeDiff = 0;
		// if(invoiceDate != null){
		//  timeDiff = ( new Date().getTime() - invoiceDate.getTime());
		// }
		
		// timeDiff = timeDiff/(1000*60*60*24);
		 
	//	 if( timeDiff >= 0 && timeDiff < 1){
			 if(ItemConstants.PAYMENT_METHOD_CREDIT_CARD.equals(paymentMethodCode)){
				 Date ccTrxDate = (Date)utils.lookup(pageContext, license, "ccTrxDate", "page");
				 long timeDiff = 0;
				 if(ccTrxDate != null){
				  timeDiff = ( new Date().getTime() - ccTrxDate.getTime());
				 }
				 
				 timeDiff = timeDiff/(1000*60*60*24);
				 
						 if( timeDiff >= 0 && timeDiff < 1){
				 try { pageContext.getOut().print("<b>Note:</b> "+ItemConstants.PAID_BYCC_24HRS);
				return EVAL_BODY_INCLUDE;}
                 catch (IOException ioe) { 
                    throw new JspException("Tag.OrderTotalTag: IOException while writing to client " + ioe.getMessage());
                 }
						 }
				 
			 }
			 else if(ItemConstants.PAYMENT_METHOD_INVOICE.equals(paymentMethodCode)){
				 Date invoiceDate = (Date)utils.lookup(pageContext, license, "invoiceDate", "page");
				 long timeDiff = 0;
					 if(invoiceDate != null){
					  timeDiff = ( new Date().getTime() - invoiceDate.getTime());
					 }
					 
					 timeDiff = timeDiff/(1000*60*60*24);
					 
							 if( timeDiff >= 0 && timeDiff < 1){
				 
				 try { pageContext.getOut().print("<b>Note:</b> "+ItemConstants.INVOICED_24HRS);
				 return EVAL_BODY_INCLUDE;}
                 catch (IOException ioe) { 
                    throw new JspException("Tag.OrderTotalTag: IOException while writing to client " + ioe.getMessage());
                 } 
							 }
			 }
			 
		// }
		 
		 
		 
		  return SKIP_BODY;
	    }
	
	
	 public String getLicense() {
	 		return license;
	 	}

	 	public void setLicense(String license) {
	 		this.license = license;
	 	}
		
	
	

}
