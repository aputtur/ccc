package com.copyright.ccc.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.copyright.ccc.business.services.DisplaySpecServices;

public class SearchEmptyTag extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String bean; // The page scoped bean to interrogate for a message

    public SearchEmptyTag() { }

    @Override
    public int doStartTag() throws JspException 
    {
       TagUtils utils = TagUtils.getInstance();

       String searchOpt = (String) utils.lookup(pageContext, bean, "searchOption", "request");
       Integer numOfItems = (Integer) utils.lookup(pageContext, bean, "totalItems", "request");
       
       int itemCount = 0;
       if (numOfItems != null) {
          itemCount = numOfItems.intValue();    
       }

       if (itemCount == 0) 
       {
           StringBuffer buffer = new StringBuffer("Your search ");
    
           if (searchOpt != null) 
           {
              buffer.append("for <b>");

              try 
              {
                  int searchVal = Integer.parseInt(searchOpt);
                  switch (searchVal) {
                      case DisplaySpecServices.ORDERDATEFILTER:
                         buffer.append("Order Date ");
                         String beginDate = (String) utils.lookup(pageContext, bean, "beginDate", "request");
                         String endDate = (String) utils.lookup(pageContext, bean, "endDate", "request");
                         if (beginDate != null && !"".equals(beginDate)) {
                            buffer.append(beginDate);
                            if (endDate != null && !"".equals(endDate)) {
                                buffer.append(":");
                                buffer.append(endDate);                            
                            }
                         } else {
                            if (endDate != null && !"".equals(endDate)) {
                               buffer.append(endDate);                            
                            } 
                         }
                      break;
                      case DisplaySpecServices.STARTOFTERMFILTER:
                         buffer.append("Start of Term ");
                         beginDate = (String) utils.lookup(pageContext, bean, "beginDate", "request");
                         buffer.append(beginDate);
                         endDate = (String) utils.lookup(pageContext, bean, "endDate", "request");
                         buffer.append(":");
                         buffer.append(endDate);
                      break;
                      case DisplaySpecServices.NOFILTER:
                      default:
                         String searchLabel = (String) utils.lookup(pageContext, bean, "searchLabel", "request");
                         buffer.append(searchLabel + "</b>");
                      break;
                  }
              } catch (NumberFormatException nfe) {
                  buffer.append("unknown</b>");
              }
              
              buffer.append("</b> found no matches");
          } else {
              buffer.append(" found no matches");
          }

           try { pageContext.getOut().print(buffer.toString()); }
           catch (IOException ioe) { 
              throw new JspException("Tag.SearchResultTag: IOException while writing to client" + ioe.getMessage());
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
