package com.copyright.ccc.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;

import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.workbench.logging.LoggerHelper;

public class SearchResultTag extends TagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String bean; // The page scoped bean to interrogate for a message
	  private static final Logger _logger = LoggerHelper.getLogger();
    public SearchResultTag() { }

    @Override
    public int doStartTag() throws JspException 
    {
       TagUtils utils = TagUtils.getInstance();
       
       boolean suppress = false;
       String searchOpt = (String) utils.lookup(pageContext, bean, "searchOption", "request");
       Integer numOfItems = (Integer) utils.lookup(pageContext, bean, "totalItems", "request");
       int itemCount = 0;
       if (numOfItems != null) {
          itemCount = numOfItems.intValue();    
       }

       StringBuffer buffer = new StringBuffer("<b>Results:</b>&nbsp;");

       if (itemCount == 1) { buffer.append("Items 1 of 1 matching item for <b>"); }
       else if (itemCount > 1) {
          String itemRange = (String) utils.lookup(pageContext, bean, "itemRange", "request");
          buffer.append("Items " + itemRange + " of " + itemCount + " matching items for <b>");
       } else if (itemCount == 0) {
          buffer.append("No matching items found for <b>");
       }

       if (searchOpt != null) 
       {
          try {
              int searchVal = Integer.parseInt(searchOpt);
              switch (searchVal) {
                  case DisplaySpecServices.ORDERDATEFILTER:
                     String beginDate = (String) utils.lookup(pageContext, bean, "beginDate", "request");
                     String endDate = (String) utils.lookup(pageContext, bean, "endDate", "request");
                     if ("".equals(beginDate) || "".equals(endDate)) {
                          suppress = true;
                     } else {
                           buffer.append("Order Date: ");
                           buffer.append(beginDate);
                      
                           buffer.append(":");
                           buffer.append(endDate + "</b>");                            
                     }
                  break;
                  case DisplaySpecServices.STARTOFTERMFILTER:
                     beginDate = (String) utils.lookup(pageContext, bean, "beginDate", "request");
                     endDate = (String) utils.lookup(pageContext, bean, "endDate", "request");
                     buffer.append("Start of Term: ");

                     if ("".equals(beginDate) || "".equals(endDate)) {
                         suppress = true;
                     } else {
                          buffer.append("Start of Term: ");
                          buffer.append(beginDate);
    
                          buffer.append(":");
                          buffer.append(endDate + "</b>");
                     }
                  break;
                  case DisplaySpecServices.REPUBLICATIONDATEFILTER:
                     beginDate = (String) utils.lookup(pageContext, bean, "beginDate", "request");
                     endDate = (String) utils.lookup(pageContext, bean, "endDate", "request");

                     if ("".equals(beginDate) || "".equals(endDate)) {
                        suppress = true;
                     } else {
                         buffer.append("Republication Date: ");
                         buffer.append(beginDate);

                         buffer.append(":");
                         buffer.append(endDate + "</b>");     
                     }
                  break;
                  case DisplaySpecServices.PERMISSIONSTATUSFILTER:
                     String option = (String) utils.lookup(pageContext, bean, "permissionStatus", "request");
                     if ("-1".equals(option)) { suppress = true; }
                     else {
                        String searchLabel = (String) utils.lookup(pageContext, bean, "searchLabel", "request");
                        buffer.append(searchLabel + "</b>");
                     }
                  break;
                  case DisplaySpecServices.PERMISSIONTYPEFILTER:
                     option = (String) utils.lookup(pageContext, bean, "permissionType", "request");
                     if ("-1".equals(option)) { suppress = true; }
                     else {
                        String searchLabel = (String) utils.lookup(pageContext, bean, "searchLabel", "request");
                        buffer.append(searchLabel + "</b>");
                     }                  
                  break;
                  case DisplaySpecServices.BILLINGSTATUSFILTER:
                     option = (String) utils.lookup(pageContext, bean, "billingStatus", "request");
                     if ("".equals(option)) { suppress = true; }
                     else {
                        String searchLabel = (String) utils.lookup(pageContext, bean, "searchLabel", "request");
                        buffer.append(searchLabel + "</b>");
                     } 
                  break;
                  case DisplaySpecServices.SPECIALORDERFILTER:
                      option = (String) utils.lookup(pageContext, bean, "specialOrderUpdate", "request");
                      if ("".equals(option)) { suppress = true; }
                      else {
                         String searchLabel = (String) utils.lookup(pageContext, bean, "searchLabel", "request");
                         buffer.append(searchLabel + "</b>");
                      } 
                   break;
                  default:
                     // Check for blank user input text and suppress output if text is blank
                     String text = (String) utils.lookup(pageContext, bean, "searchText", "request");
                     if (text != null && !"".equals(text)) {
                        text = text.trim();
                        if ("".equals(text)) {
                           suppress = true;
                        } else {
                            String searchLabel = (String) utils.lookup(pageContext, bean, "searchLabel", "request");
                            buffer.append(searchLabel + "</b>");                            
                        }
                     } else { suppress = true; }
                  break;
              }
              
              if (!suppress) {
                 try { pageContext.getOut().print(buffer.toString()); }
                 catch (IOException ioe) { 
                    throw new JspException("Tag.SearchEmptyTag: IOException while writing to client" + ioe.getMessage());
                 }
              }
          } catch (NumberFormatException nfe) {
        	  _logger.error( ExceptionUtils.getFullStackTrace(nfe) ); 
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
