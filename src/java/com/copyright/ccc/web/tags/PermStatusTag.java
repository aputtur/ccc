package com.copyright.ccc.web.tags;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;

import com.copyright.base.Constants;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.util.LogUtil;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.workbench.logging.LoggerHelper;

public class PermStatusTag extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String bean; // The page scoped bean to interrogate for a message
    
    // The following constants are defined in com.copyright.data.order.OrderDetailStatusConstants
    private final static int DETAIL_NOT_SAVED = 0;
    private final static int REQ_ADDITIONAL_CUST_INFO = 310;
    private final static int AWAITING_CUST_REPLY = 320;
    private final static int FORWARD_TO_RH = 410;
    private final static int AWAITING_REPLY_2 = 420;
    private final static int AWAITING_REPLY_3 = 430;
    private final static int AWAITING_REPLY_FINAL = 440;
    //private final int WORK_NOT_FOUND = 610;
    //private final int RIGHT_NOT_PINNED = 700;
    private final static int CHECKING_AVAILABILITY = 730;
    private final static int AWAITING_INVOICING = 1010;
    private final static int INVOICED = 2000;
    private final static char granted = 'G';
    private final static char denied = 'D';
    private final static char contactRightsholder = 'C';
    private final static char contactRightsholderDirectly = 'I';
    private final static char researchFurther = 'R';
    private final static char canceled = 'X';
    private final static char publicDomain = 'P';
    private final static char lcnConfirm = 'L';
    private final static String MSG_PREFIX = "ov.status.code.msg.";
    private static final Logger _logger = LoggerHelper.getLogger();
    /**
    * Access the Struts form License bean in the page scope.  Try to find a mapping
    * for the text based on the displayPermissionStatusCd.  If the code is not found
    * use the displayPermissionStatus String text as a default.
    * @throws JspException
    */
    @Override
    public int doStartTag() throws JspException 
    {
    	try{
        StringBuffer bufr = new StringBuffer();
        TagUtils utils = TagUtils.getInstance();
        
        Boolean isSpecialOrder = (Boolean) utils.lookup(pageContext, bean, "specialOrder", "page");
        boolean isSpecOrder = false;	
        if (isSpecialOrder != null) {
            isSpecOrder = isSpecialOrder.booleanValue();
        }

        String itemStatusCd = (String) utils.lookup(pageContext, bean, "itemStatusCd", "page");

        String productCd = (String) utils.lookup(pageContext, bean, "productCd", "page");
        
        String odtStatusCd = (String) utils.lookup(pageContext, bean, "licenseStatusCd", "page");
        int intOdtStatusCd = -1;
        if(odtStatusCd != null && !odtStatusCd.isEmpty())
        {
	        try {
	           Integer code = Integer.valueOf(odtStatusCd); 
	           if (code != null) {
	              intOdtStatusCd = code.intValue();
	           }
	        } catch (NumberFormatException nfe) {
	        	_logger.warn(LogUtil.getStack(nfe));
	        	intOdtStatusCd = -1; 
	        }
        }
        
        boolean awaitingCustReply = false;
        if (intOdtStatusCd == AWAITING_CUST_REPLY) {
           awaitingCustReply = true;
        } 

        String status = "";
        char statusCode = 'z'; // default represents no valid code

        String code = (String) utils.lookup(pageContext, bean, "displayPermissionStatusCd", "page");
        if (code != null) {
           char[] permStatus = code.toCharArray();
           if (permStatus.length > 0) {
        	   if(code.toUpperCase().equals("CANCELED")){
        		   statusCode = 'X'; 
        	   }
        	   else{
        		   statusCode = code.charAt(0);
        	   }
           }
        } else {
        	statusCode = 'R';
        }

        if (itemStatusCd.equals(ItemStatusEnum.CANCELLED.name())) {
  		   statusCode = 'X'; 
         }
        
        if (itemStatusCd.equals(ItemStatusEnum.AWAITING_LCN_CONFIRM.name())) {
        	statusCode = 'L';
        }
        
        boolean updated = false;

        // Check the status codes from SS
        switch (statusCode) 
        {
            case granted: 
              status = this.getGrantedOutput(utils); //"Granted";
              updated = true;
            break;
            case denied:
              if (isSpecOrder) {
                status = this.getDeniedSpecOrderOutput(utils, intOdtStatusCd); // "Denied"; 
              } else {
                status = this.getDeniedOutput(utils, intOdtStatusCd); // "Denied"; 
              }
              updated = true;
            break;
            case contactRightsholder:
                if (itemStatusCd.equals(ItemStatusEnum.AWAITING_FULFILLMENT.name())) {
           		   	status = this.getAwaitingFulfillment(utils);
                   	updated = true;
                } else {
                	status = this.getSpecialOrder(utils, intOdtStatusCd);
                	updated = true;
                }              
            break;
            case lcnConfirm:
                   	status = this.getAwaitingLcnConfirm(utils);
                	updated = true;
                              
            break;
            case researchFurther:
              status = this.getSpecialOrder(utils, intOdtStatusCd); //"Special Order";
              updated = true;
            break;
            case contactRightsholderDirectly:
               status = this.getContactOutput(utils);
               updated = true;
            break;
            case publicDomain:
               status = this.getPublicDomainOutput(utils);
               updated = true;
            break;
            case canceled:
               status = "<b>Request has been canceled</b><br />";
               updated = true;
            break;
        }

        if (updated) 
        {
        	if (productCd.equals(ProductEnum.RLR.name())) {
            	bufr.append("<b>Reprint Status:</b> " + status);        		
        	} else {
            	bufr.append("<b>Permission Status:</b> " + status);        		
        	}
           try { pageContext.getOut().print(bufr.toString()); }
           catch (IOException ioe) { 
        	 _logger.error(LogUtil.getStack(ioe));
             throw new JspException("Tag.PermStatusTag: IOException while writing to client" + ioe.getMessage());
           }
        }
    	}
    	catch(Exception e){
    		_logger.error( ExceptionUtils.getFullStackTrace(e) );
    	}

        return SKIP_BODY;
    } 
     
    public PermStatusTag() { }

    private String getGrantedOutput(TagUtils utils) {
      StringBuffer html = new StringBuffer("<img src=\"/media/images/icon_check.gif\" width=\"17\" height=\"17\" alt=\"Granted\"/>&nbsp;<b>Granted</b><br />");
      //StringBuffer html = new StringBuffer("<img src=\"<html:rewrite href='/media/images/icon_check.gif'/>\" width=\"17\" height=\"17\" alt=\"Granted\"/>&nbsp;<b>Granted</b><br />");
        
      // Append External comments, if they exist, to the granted HTML.
      this.getExtComment(utils, html);
      return html.toString();   
    }
    
    private String getPublicDomainOutput(TagUtils utils) {
        StringBuffer html = new StringBuffer("<img src=\"/media/images/icon_check.gif\" width=\"17\" height=\"17\" alt=\"Public Domain\"/>&nbsp;<b>Public Domain</b><br />");
        return html.toString();
    }

    private String getDeniedOutput(TagUtils utils, int odtStatusCode) {
      StringBuffer html = new StringBuffer("<img src=\"/media/images/icon-unavailable.gif\" width=\"16\" height=\"16\" alt=\"Special Order\"/>&nbsp;<b>Denied</b><br />");
      String updateAndComments = this.getMessage(utils, odtStatusCode, true);
      html.append(updateAndComments);
      this.getExtComment(utils, html); // Per K. Graves/L. Brandon - 4/27/07
      return html.toString();   
    }   

    /*
    * Special orders awaiting customer reply get the yellow clock icon.  Otherwise
    * they get the red ghostbuster's icon
    */
    private String getDeniedSpecOrderOutput(TagUtils utils, int odtStatusCode) {
      StringBuffer html;
      if (odtStatusCode == AWAITING_CUST_REPLY || odtStatusCode == REQ_ADDITIONAL_CUST_INFO) {
         html = new StringBuffer("<img src=\"/media/images/clock_yellow.gif\" width=\"17\" height=\"17\" alt=\"Special Order\"/>&nbsp;<b>Denied</b><br />");
      } else {
         html = new StringBuffer("<img src=\"/media/images/icon-unavailable.gif\" width=\"16\" height=\"16\" alt=\"Special Order\"/>&nbsp;<b>Denied</b><br />");
      }
      String updateAndComments = this.getMessage(utils, odtStatusCode, true);
      html.append(updateAndComments);
      this.getExtComment(utils, html); // Per K. Graves/L. Brandon - 4/27/07
      return html.toString();   
    } 

    private String getContactOutput(TagUtils utils) 
    {
      StringBuffer html = new StringBuffer("<img src=\"/media/images/icon_contact.gif\" width=\"17\" height=\"17\" alt=\"Contact Rightsholder\"/>&nbsp;<b>Contact rightsholder directly</b><br />");
      // Append External comments, if they exist, to the granted HTML.
      this.getExtComment(utils, html);
      return html.toString();
    }  

    private String getSpecialOrder(TagUtils utils, int odtStatusCode) 
    {
       StringBuffer html = new StringBuffer("<img src=\"/media/images/clock.gif\" width=\"17\" height=\"17\" alt=\"Special Order\"/>&nbsp;<b>Special Order</b><br />");
       String updateAndComments = this.getMessage(utils, odtStatusCode, false);
       html.append(updateAndComments);
       this.getExtComment(utils, html);
       return html.toString();
    }

    private String getAwaitingFulfillment(TagUtils utils) 
    {
       StringBuffer html = new StringBuffer("<img src=\"/media/images/clock.gif\" width=\"17\" height=\"17\" alt=\"Special Order\"/>&nbsp;<b>Awaiting Fulfillment</b><br />");
//       String updateAndComments = this.getMessage(utils, odtStatusCode, false);
//       html.append(updateAndComments);
//       this.getExtComment(utils, html);
       return html.toString();
    }
    
    private String getAwaitingLcnConfirm(TagUtils utils) 
    {
       StringBuffer html = new StringBuffer("<img src=\"/media/images/clock.gif\" width=\"17\" height=\"17\" alt=\"Special Order\"/>&nbsp;<b>Special Order</b><br />");
       html.append("<b>Special order update: Awaiting customer response</b><br>");
       return html.toString();
    }

    
    private String getSpecialOrderWait(TagUtils utils, int odtStatusCode) {
       StringBuffer html = new StringBuffer("<img src=\"/media/images/clock_yellow.gif\" width=\"17\" height=\"17\" alt=\"Special Order\"/>&nbsp;<b>Special Order</b><br />");
       String updateAndComments = this.getMessage(utils, odtStatusCode, false);
       html.append(updateAndComments);
       this.getExtComment(utils, html); // Per K. Graves/L. Brandon - 4/27/07
       return html.toString();
    }

    private String getCancelOrder(TagUtils utils) {
       StringBuffer html = new StringBuffer("<b>Request has been canceled</b><br />");
       this.getExtComment(utils, html);
       return html.toString();
    }

    public void setBean(String bean) {
       this.bean = bean;
    }

    public String getBean() {
       return bean;
    }

    private String getMessage(TagUtils utils, int statusCode, boolean isDenied) //throws JspException
    {
       StringBuffer updateMsg = new StringBuffer();
       switch (statusCode) 
       {
            case DETAIL_NOT_SAVED:
            case REQ_ADDITIONAL_CUST_INFO:
            case AWAITING_CUST_REPLY:
            case FORWARD_TO_RH:
            case AWAITING_REPLY_2:
            case AWAITING_REPLY_3:
            case AWAITING_REPLY_FINAL:
            //case WORK_NOT_FOUND:
            //case RIGHT_NOT_PINNED:
            case CHECKING_AVAILABILITY:
               this.getResourceMessage(statusCode, updateMsg, utils);
            break;
            case AWAITING_INVOICING: // 1010
            case INVOICED: // 2000
               if (!isDenied) {
                  this.getResourceMessage(statusCode, updateMsg, utils);
               }
            break;
            default: 
               // Unknown codes should map to "Checking availability"
               ResourceBundle messages = ResourceBundle.getBundle("ApplicationResources");
               String key = MSG_PREFIX + CHECKING_AVAILABILITY;
               String cmnts = messages.getString(key);
               
               if (cmnts != null && !"".equals(cmnts)) {
                   updateMsg.append("<b>Special Order Update:</b> ");
                   updateMsg.append(cmnts); 
                   updateMsg.append("<br />");
               }
            break;
       }
       
       return updateMsg.toString();
    }
    
    /*
     * Update a StringBuffer by reference with special order update text based on a 
     * lookup keyed by the order detail status code.  If the message resource cannot be
     * found by the key use the Shared Services version of the message.
     */
    private void getResourceMessage(int statusCode, StringBuffer msg, TagUtils utils) 
    {
       try {
           ResourceBundle messages = ResourceBundle.getBundle("ApplicationResources");
           String key = MSG_PREFIX + statusCode;
           String update = messages.getString(key);
        
           if (update != null && !"".equals(update)) {
              msg.append("<b>Special Order Update:</b> ");
              msg.append(update); 
              msg.append("<br />");
           }
       } catch (Exception exc) {
    	   _logger.error(LogUtil.getStack(exc));
    	   // Since there is no CC2 mapping for the comment use shared services message
           try {
               String rtnValue = (String) utils.lookup(pageContext, bean, "licenseStatusCdMsg", "page");
               if (rtnValue != null && !"".equals(rtnValue)) {
                  msg.append("<b>Special Order Update:</b> ");
                  msg.append(rtnValue); 
                  msg.append("<br />");
               }
           } catch (Exception exc2) {
        	   _logger.error(LogUtil.getStack(exc2));
           }
       }
    }
    
    /*
    * Update a StringBuffer with external comment HTML if there is any
    */
    private void getExtComment(TagUtils utils, StringBuffer comments)
    {
        try {
            String cmnts = (String) utils.lookup(pageContext, bean, "externalCommentOverride", "page");
    
            if (cmnts != null && !"".equals(cmnts)) {
               comments.append("<b>Comment:</b> ");
               comments.append(cmnts); 
               comments.append("<br />");
            } 
        } catch (JspException exc) {
     	   _logger.error(LogUtil.getStack(exc)); 
        }
    }
    
    private String getContextPath()
    {
      String contextPath = Constants.EMPTY_STRING;
      
      ServletRequest request = pageContext.getRequest();
      
      if( request instanceof HttpServletRequest ) 
      {
        contextPath = ( (HttpServletRequest) request ).getContextPath() + "/";
      }
      
      return contextPath;
    }
}
