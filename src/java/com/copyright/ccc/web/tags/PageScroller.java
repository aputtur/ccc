package com.copyright.ccc.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 * Tag handler for providing a scrolling mechanism for displaying page results
 * in a paged fashion.  This tag implementation is defined in cc2-util.tld.
 * The scrolling function displays up to a block of 5 pages.  If there are more
 * than 5 pages "More" is displayed after the last of the 5 visible pages.  Clicking
 * "More" moves forward to the next block of 5 visible pages or the last 5 visible
 * pages if less than 5 new pages exist.  Likewise if there are pages existing before 
 * the first of the 5 displayable pages "Back" will be displayed with a link to display the
 * previous block of 5 visible pages.
 *
 * This tag uses the following form values: 
 * "action" describes the struts action to use for each page link.  This action will
 * have a 'pg' query parameter added set to the value of the current page for each
 * page link.  For instance if your page load action was 'processA.do' the rendered
 * tag for the page 3 link would be: '<a href=processA.do?pg=3>3</a>'
 * 
 * "currentPage" describes which of the 5 visible links is currently displayed.  The
 * current page JSP form value must be accessible with an ID through javascript.  This
 * is so it can be changed to a new currentPage value if the user clicks a page link.
 * 
 * "resultsPerPage" defines how many visible items are displayed on each page.  This
 * value defaults to 25 and is not a required tag attribute.
 * 
 * "totalPages" indicates how many total pages could be displayed as calculated by
 * the resultsPerPage attribute.
 * 
 * Required javascript: The scroller tag expects a javascript:scroll(currPage) 
 * function to exist that changes the JPS's currentPage attribute value to the 
 * new page defined by the user click so the new item list and scroll page 
 * configuration can be updated for the new viewing page.
 */
public class PageScroller extends TagSupport 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int VISIBLE_PAGE_LINKS = 5;
    public static final int RESULTS_PER_PAGE = 25;  // The number of items to display per page

    protected String name = null; // The name of a form bean referenced for page variables
    protected String action;      // The struts action called by clicking the page links
    protected String totalPages = null;  // The total pages available for viewing.
    protected String currentPage = null; // The current page displayed.  First page is 1.
    protected String startPage = null;   // The start page displayed.
    protected String resultsPerPage;  // The number of items to display per page
    protected String scope = null; // The scope of the bean access.
    
    public PageScroller() { 
       super();
       this.resultsPerPage = String.valueOf(RESULTS_PER_PAGE);
    }

    /**
    * Access the Struts form bean in the given scope.  Determine the current display
    * page and total displayable pages by accessing the getter for the attributes
    * specified in the tag by "currentPage" and "totalPages".
    * @throws JspException
    */
    @Override
    public int doStartTag() throws JspException { 

       int pageCount = 0;

       try 
       {          
           String stringVal = (String) TagUtils.getInstance().lookup(pageContext, name, action, scope);
           if (stringVal != null) {
               this.action = stringVal; // the page link's struts action
           }

           // totalPages is a required property
           Integer value = (Integer) TagUtils.getInstance().lookup(pageContext, name, totalPages, scope);
           if (value != null) {
               // Convert value to the String with some formatting
               totalPages = value.toString();
               pageCount = value.intValue();
           }
           
           int viewPage = 1;
           // currentPage is a required property
           value = (Integer) TagUtils.getInstance().lookup(pageContext, name, currentPage, scope);
           if (value != null) {
               // Convert value to the String with some formatting
               currentPage = value.toString();
               viewPage = value.intValue();
           }
           
           int firstPage = 1;
           // startPage is a required property
           value = (Integer) TagUtils.getInstance().lookup(pageContext, name, startPage, scope);
           if (value != null) {
               // Convert value to the String with some formatting
               startPage = value.toString();
               firstPage = value.intValue();
           }
           
           // resultsPerPage is not required - default is 25
           value = (Integer) TagUtils.getInstance().lookup(pageContext, name, resultsPerPage, scope);
           if (value != null) {
               // Over ride the default with user setting
               resultsPerPage = value.toString();
           }
           
           // Only output page scroller if necessary
           if (pageCount > 1) {
               this.writeScroller(firstPage, viewPage, pageCount);
           }
       } catch (IOException ioe) {
          throw new JspException("Tag.PageScroller: IOException while writing to client" + ioe.getMessage());
       }

       return SKIP_BODY;
    }
    
    /*
    * @param start is the first visible scroll page link number
    * @param current is the current displayed scroll page
    * @param total is the total number of scrollable pages
    */
    private void writeScroller(int start, int current, int total) throws IOException
    {
        String closeComma = "</a>,\n";
        String closeEnd = "</a>\n";
        String closeString;
        StringBuffer buffer = new StringBuffer();

        int end = start + VISIBLE_PAGE_LINKS - 1;

        boolean back = false;
        if (start > 1) { back = true; }
        
        // Check to see if we need to update the scroll page links
        if (current < start) {
           start = start - VISIBLE_PAGE_LINKS;
           if (start < 1) { start = 1; }
        } else if (current > end) {
           start += VISIBLE_PAGE_LINKS; 
           end = start + VISIBLE_PAGE_LINKS - 1;
        }

        boolean more = false;
        if (end < total) { 
           more = true; 
        } else if (end > total) {
           end = total; 
        }
 
        if (back) {
           buffer.append("<a href=\"javascript:scroll("+(start - 1)+");\">back</a>, "); 
        }

        //buffer.append("<!-- pageScroller BEGIN -->\n");
        for (int i=start;i<=end;i++) 
        {
           if (i < end) { closeString = closeComma; }
           else { 
              if (more) { closeString = closeComma; }
              else { closeString = closeEnd; }
           }
           
           if (i == current) {
              buffer.append("<a href=\"javascript:scroll("+i+");\"><b style='color: black;'>"+i+"</b>"); 
           } else {
              buffer.append("<a href=\"javascript:scroll("+i+");\">"+i);
           }
           buffer.append(closeString);
        }
        
        if (more) {
           buffer.append("<a href=\"javascript:scroll("+(end + 1)+");\">more</a>"); 
        }

        //buffer.append("<!-- pageScroller END -->\n");   
        pageContext.getOut().print(buffer.toString());
    }

    /*
    * Output simple page scroller of form: << < Page 1 or x > >>
    private void writeSimpleScroller(int next, int prev) throws IOException
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<!-- pageScroller BEGIN -->\n");
        buffer.append("<a href=\"javascript:gotoPage(1)\">&lt;&lt;</a>\n");
        buffer.append("<a href=\"javascript:gotoPage("+prev+")\">&lt;</a> Page " + currentPage + " of " + totalPages + "\n");
        buffer.append("<a href=\"javascript:gotoPage("+next+")\">&gt;</a> <a href=\"javascript:gotoPage("+totalPages+")\">\n");
        buffer.append("&gt;&gt;</a>\n");
        buffer.append("<!-- pageScroller END -->\n");   
        pageContext.getOut().print(buffer.toString());
    }
*/
    @Override
    public int doEndTag() throws JspException {
       return EVAL_PAGE;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setResultsPerPage(String resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public String getResultsPerPage() {
        return resultsPerPage;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public String getStartPage() {
        return startPage;
    }
}
