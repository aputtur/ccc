package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.web.util.DropDownElement;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.workbench.logging.LoggerHelper;

public class OrderHistoryActionForm extends OrderHistoryValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger sLogger = LoggerHelper.getLogger();
    public static final int RESULTS_PER_PAGE = 25; // default results per page
    
    public static final int OPEN = DisplaySpecServices.OPENSTATE;
    public static final int CLOSED = DisplaySpecServices.CLOSEDSTATE;
    public static final int ALL = DisplaySpecServices.ALLSTATES;
    public static final int DEFAULT = -1;
    public static final int SEARCH = 3;
    public static final String ASCENDING = "ascending";   // sort direction
    public static final String DESCENDING = "descending"; // sort direction
    public static final String ALL_ORDERS = "All Orders";
    public static final String OPEN_ORDERS = "Open Orders";
    public static final String CLOSED_ORDERS = "Closed Orders";
    
    private Collection<DropDownElement> searchOptions;
    private Collection<DropDownElement> sortOptions;
    private String direction; // User sort direction

    private String searchOption;
    private String searchText;
    private String searchLabel; // The combo box search label selected
    private String sortOption;
    private OrderPurchases purchases;
    
    private int fromRow;
    private int toRow;
    
    private int status; // main search page attribute - open/closed/all
    
    private DisplaySpec spec;
    private String pageAction;
    private int totalPages;  // total pages of items
    private int totalItems;  // all visible items
    private int currentPage; // current displayed page of items
    private int startPage;   // current first scrollable page link
    private int startItem; // nth item for display
    private int endItem;   // n + resultsPerPage item for display
    private boolean searchPage; // form control for search display
    private boolean cartEmpty;  // Test variable to determine if user can copy an order
    private boolean cartAcademic; // Test variable to determine if user can copy an order
    private boolean _isAdjustmentUser; // If true display the adjustment navigation options
    private boolean newSearch; // true when the user clicks "Go" for a new search
    private boolean rlUser = false;
     
    public boolean isRlUser() {
		return rlUser;
	}
	public void setRlUser(boolean rlUser) {
		this.rlUser = rlUser;
	}
	public String getSearchText() { return this.searchText; }
    public void setSearchText(String txt) { 
       if (txt != null) {
          this.searchText = txt.trim(); // trim for detail ID integers
       }
    }

    public OrderHistoryActionForm() 
    {
       this.searchOption = String.valueOf(DisplaySpecServices.NOFILTER);
       this.sortOption = String.valueOf(DisplaySpecServices.ORDERDATESORT); 
       this.direction = DESCENDING;
       this.pageAction = "processOrderHistory.do"; // paging link action
       this.currentPage = 1;
       this.startPage = 1;
       this.status = DEFAULT;
       super.setBeginDateFailed(false);
       super.setEndDateFailed(false);
    }
	
    public Collection<OrderPurchase> getDisplayPurchases() {
       if (this.purchases != null) {
    	   for(OrderPurchase ordPrch : this.purchases.getDisplayPurchaseList()){
    		 long confNum =  ordPrch.getConfirmationNumber();
    		  for(OrderBundle ordBundl : ordPrch.getOrderBundles()){
    			  String title = ordBundl.getBundleTitles();
    			  if ( sLogger.isDebugEnabled() )
    			  {
    				  sLogger.debug(title);
    			  }
    		  }
    	   }
          return this.purchases.getDisplayPurchaseList();    
       } else {
          return new ArrayList<OrderPurchase>();
       }
    }
    
    public OrderPurchases getPurchases() { return this.purchases; }

    public void setPurchases(OrderPurchases p) 
    { 
       this.purchases = p; 
    }
    
    public Collection<DropDownElement> getSearchOptions()
    {
      return this.searchOptions;
    }
	
    public void setSearchOptions(Collection<DropDownElement> options)
    {
       this.searchOptions = options;
    }
    
    public Collection<DropDownElement> getSortOptions()
    {
      return this.sortOptions;
    }
    
    public void setSortOptions(Collection<DropDownElement> options)
    {
      this.sortOptions = options;
    }
    
    public String getSearchOption()
    {
      return this.searchOption;
    }
    
    public void setSearchOption(String option)
    {
       this.searchOption = option;
    }
    
    public String getSortOption()
    {
      return this.sortOption;
    }

    public void setSortOption(String option)
    {
      this.sortOption = option;
    }

    @Override
    public String getBeginDate() { return super.getBeginDate(); }
    @Override
    public void setBeginDate(String date) { super.setBeginDate(date); }

    @Override
    public String getEndDate() { return super.getEndDate(); }
    @Override
    public void setEndDate(String date) { super.setEndDate(date); }
	
    /*
    * Method called to populate this form's internal DisplaySpec object
    * from all of the form's internal attributes.  Called by struts 
    * action method after the struts controller has populated the form.
    */
    public void initDisplaySpec() 
    {
        this.spec = UserContextService.getPurchaseDisplaySpec();
        this.spec.resetSearchDefault(); // clear out search field
        this.spec.setForceReRead(true); // for DB read in case user added a new order

        if (DisplaySpecServices.OPENSTATE == this.status) {
           this.spec.setOrderStateFilter(DisplaySpecServices.OPENSTATE); 
        } else if (DisplaySpecServices.CLOSEDSTATE == this.status) {
           this.spec.setOrderStateFilter(DisplaySpecServices.CLOSEDSTATE); 
        } else {
           this.spec.setOrderStateFilter(DisplaySpecServices.ALLSTATES);  
        }

        // Add a helper to set the sort direction and sort criteria.  If this is a 
        // search page (the user clicked Go) use the default sort option and direction
        // for the search option.  If this is not a search page (the user changed the sort
        // option or direction) keep the sort settings.
        this.setSortValues();

        int filterBy = DisplaySpecServices.NOFILTER; // default search option
        try {
           filterBy = Integer.parseInt(this.searchOption);
        } catch (NumberFormatException nfe) {
           filterBy = DisplaySpecServices.NOFILTER; // default search option 
        }
        this.spec.setSearchBy(filterBy);

        if (filterBy == DisplaySpecServices.ORDERDATEFILTER || 
            filterBy == DisplaySpecServices.STARTOFTERMFILTER) 
        {
            String beginDate = super.getBeginDate();
            String endDate = super.getEndDate();
            if (beginDate == null || endDate == null) {
                // One or more date fields was empty so default to NOFILTER
                filterBy = DisplaySpecServices.NOFILTER;
                this.spec.setSearchBy(filterBy);
            } else {
                // Validation sets search string for SS call if successful
                boolean goodDates = super.validateDates(this.spec);
                if (!goodDates) {
                    this.spec.setSearch(""); // clear out the search string
                    String startOrEnd;
                    String dateType;
                    ActionMessages errors = new ActionMessages();       
                    if (this.isBeginDateFailed()) {
                        ActionMessage error;
                        startOrEnd = "start";
                        if (filterBy == DisplaySpecServices.ORDERDATEFILTER) {
                           dateType = "Order Date";
                        } else {
                           dateType = "Start of Term";
                        }
                        error = new ActionMessage("oh.error.invalid.date", startOrEnd, dateType); 
                        errors.add("beginDateParse", error);
                    }

                    if (this.isEndDateFailed()) {
                        ActionMessage error;
                        startOrEnd = "end";
                        if (filterBy == DisplaySpecServices.ORDERDATEFILTER) {
                           dateType = "Order Date";
                        } else {
                           dateType = "Start of Term";
                        }
                        error = new ActionMessage("oh.error.invalid.date", startOrEnd, dateType); 
                        errors.add("endDateParse", error);
                    }
                    super.setErrors(errors);
                }
            }
        } else if (filterBy != DisplaySpecServices.NOFILTER) {
            String filter = this.getSearchText();
            if (filter != null) { 
               if (!"".equals(filter)) {
                  // Check if filter is a valid number if it is a conf number
                  if (filterBy == DisplaySpecServices.CONFNUMFILTER) {
                     try { 
                        Integer.parseInt(filter); // just to check for a valid String/number
                        this.spec.setSearch(filter); // it's valid so set it in the spec
                     } catch (NumberFormatException nfe) {
                        this.spec.setSearch("-1"); // Invalid conf num so use -1 to avoid Ordermgmt error
                     }
                  } else {
                      this.spec.setSearch(filter);  
                  }
               } else {
                  filterBy = DisplaySpecServices.NOFILTER;
                  this.spec.setSearchBy(filterBy);
               }
            } else {
                // The search text field is empty so default back to NOFILTER
                filterBy = DisplaySpecServices.NOFILTER;
                this.spec.setSearchBy(filterBy);
            }
        }

        if (this.direction != null && ASCENDING.equalsIgnoreCase(this.direction)) {
           this.spec.setSortOrder(DisplaySpec.ASCENDING); 
        } else {
           this.spec.setSortOrder(DisplaySpec.DESCENDING); 
        }

        this.spec.setResultsPerPage(RESULTS_PER_PAGE);
        // turn off display spec changed so we don't default to page 1
        this.spec.resetLastDisplaySpecAttributes(); 
        DisplaySpecServices.setDisplayPage(this.spec, this.currentPage);

        // Save the spec away in the UserContext
        UserContextService.setPurchaseDisplaySpec(this.spec);
    }

    private void setSortValues() 
    {
        int searchBy, sortBy, sortDir;

        if (this.isNewSearch()) {
            String searchOpt = this.searchOption;
            try {
               searchBy = Integer.parseInt(searchOpt); 
            } catch (NumberFormatException nfe) {
               searchBy = DisplaySpecServices.CONFNUMFILTER;
            }
            
            // Set the default sort option and direction based on the search option
            switch (searchBy) 
            {
               // case DisplaySpecServices.ORDERDATEFILTER:
               // break;
               // case DisplaySpecServices.CONFNUMFILTER:
               // break;
               case DisplaySpecServices.STARTOFTERMFILTER:
                  sortBy = DisplaySpecServices.STARTOFTERMSORT;
                  sortDir = DisplaySpec.DESCENDING; // recent to oldest
               break;
               case DisplaySpecServices.SCHOOLFILTER:
                  sortBy = DisplaySpecServices.SCHOOLSORT;
                  sortDir = DisplaySpec.ASCENDING; // alpha
               break;
               case DisplaySpecServices.COURSENAMEFILTER:
                  sortBy = DisplaySpecServices.COURSENAMESORT;
                  sortDir = DisplaySpec.ASCENDING; // alpha
               break;
               case DisplaySpecServices.COURSENUMBERFILTER:
                  sortBy = DisplaySpecServices.COURSENUMBERSORT;
                  sortDir = DisplaySpec.ASCENDING; // alpha
               break;
               case DisplaySpecServices.INSTRUCTORFILTER:
                  sortBy = DisplaySpecServices.INSTRUCTORSORT;
                  sortDir = DisplaySpec.ASCENDING; // alpha
               break;
               case DisplaySpecServices.YOURREFERENCEFILTER:
                  sortBy = DisplaySpecServices.YOURREFERENCESORT;
                  sortDir = DisplaySpec.ASCENDING; // alpha
               break;
               case DisplaySpecServices.YOURACCTREFERENCEFILTER:
                  sortBy = DisplaySpecServices.YOURACCTREFERENCESORT;
                  sortDir = DisplaySpec.ASCENDING; // alpha
               break;
               default:
                  sortBy = DisplaySpecServices.ORDERDATESORT;
                  sortDir = DisplaySpec.DESCENDING; // recent to oldest
               break;
            }

            if (sortDir == DisplaySpec.DESCENDING) {
               this.setDirection(OrderHistoryActionForm.DESCENDING);
            } else {
               this.setDirection(OrderHistoryActionForm.ASCENDING); 
            }
            this.setSortOption(String.valueOf(sortBy));
            
            this.spec.setSortOrder(sortDir);
            this.spec.setSortBy(sortBy);

        } else {
            sortDir = DisplaySpec.DESCENDING; // default sort option
            String dir = this.getDirection();
            if ("ascending".equals(dir)) {
                sortDir = DisplaySpec.ASCENDING;
            }
            this.spec.setSortOrder(sortDir);

            sortBy = DisplaySpecServices.ORDERDATESORT; // default sort option
            if (this.sortOption != null && !"".equals(this.sortOption)) 
            {
                try {
                   sortBy = Integer.parseInt(this.sortOption);
                } catch (NumberFormatException nfe) {
                   sortBy = DisplaySpecServices.ORDERDATESORT; // default sort option 
                }            
            }
            this.spec.setSortBy(sortBy);
        }
    }

    public void setFromRow(int fromRow) {
       this.fromRow = fromRow;
    }

    public int getFromRow() {
       return fromRow;
    }

    public void setToRow(int toRow) {
       this.toRow = toRow;
    }

    public int getToRow() {
       return toRow;
    }

    /**
    * Returns the form DisplaySpec after initting the spec
    * from the form attributes
    * @return the form DisplaySpec
    */
    public DisplaySpec getDisplaySpec() {
       this.initDisplaySpec();
       return this.spec;
    }

    public void setDisplaySpec(DisplaySpec spec) {
       this.spec = spec;
    }

    /**
    * Returns the form DisplaySpec without re-initting the spec
    * from the form attributes
    * @return the form DisplaySpec
    */
    public DisplaySpec getSpec() {
       return this.spec;
    }

    /**
    * Status is the main search page attribute that defines if the user is
    * viewing Open, Closed, or All purchases.  The int values for these constants
    * are defined in DisplaySpecServices as static constants.
    * 
    * @param s holds the main page search attribute.
    */
    public void setStatus(int s) {
       this.status = s;
       switch (s) {
           case 0: // open
             this.setSearchLabel(OrderHistoryActionForm.OPEN_ORDERS);
           break;
           case 1: // closed
             this.setSearchLabel(OrderHistoryActionForm.CLOSED_ORDERS);
           break;
           case 2: // all
             this.setSearchLabel(OrderHistoryActionForm.ALL_ORDERS);
           break;
       }
    }

    /**
    * Returns the order status attribute.  
    * @return the main search find page attribute
    */
    public int getStatus() 
    {
        return this.status;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
    
    public String getLastDir() {
        return direction;
    }

    public int getResultsPerPage() { return RESULTS_PER_PAGE; }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    /**
    * Set the current page and as a side affect also set the indices for the 
    * displayed order items.  The indices are held in this form's startItem
    * and endItem attributes.
    * 
    * @param currentPage is the current viewable display page
    */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        if (currentPage == 1) {
            startItem = 1;
            if (totalItems == 0) { startItem = 0; }
            endItem = RESULTS_PER_PAGE;
        } else {
            // Current page is 2 or greater
            startItem = (currentPage - 1) * RESULTS_PER_PAGE + 1;
            endItem = startItem + RESULTS_PER_PAGE - 1;
        }
        
        if (endItem > totalItems) {
           endItem = totalItems;
        }
    }

    public void setPg(int scrollPage) 
    {
       this.currentPage = scrollPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getPageAction() {
        return pageAction;
    }
    
    public int getOpen() { return OPEN; }
    public int getClosed() { return CLOSED; }
    public int getAll() { return ALL; }

    /**
    * Get the item range String for display in the Results header.  Calculates
    * the range based on the current page and the results per page.
    * @return a String of the form 'a-z' where a and z are numbers separated by
    * OrderHistoryActionForm.RESULTS_PER_PAGE - 1.  If the current page is 1 the 
    * returned String is "1-25" (for default results per page of 25).
    */
    public String getItemRange() 
    {
       int start = (currentPage - 1) * OrderHistoryActionForm.RESULTS_PER_PAGE + 1;
       String startStr = String.valueOf(start); 
       int end = start + OrderHistoryActionForm.RESULTS_PER_PAGE - 1;
       if (end > this.totalItems) { end = this.totalItems; }
       String endStr = String.valueOf(end);
       return startStr + "-" + endStr;
    }

    public void setSearchPage(boolean searchPage) {
        this.searchPage = searchPage;
    }

    public boolean isSearchPage() {
        return searchPage;
    }

    public String getSf() {
       if (this.searchPage) { return "true"; }
       else { return "false"; }
    }

    public void setTotalItems(int displayItems) {
        this.totalItems = displayItems;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setStartItem(int startItem) {
        this.startItem = startItem;
    }

    public int getStartItem() {
        return startItem;
    }

    public void setEndItem(int endItem) {
        this.endItem = endItem;
    }

    public int getEndItem() {
        return endItem;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setCartAcademic(boolean cartAcademic) {
        this.cartAcademic = cartAcademic;
    }

    public boolean isCartAcademic() {
        return cartAcademic;
    }

    public void setSearchLabel(String searchSelection) {
        this.searchLabel = searchSelection;
    }

    public String getSearchLabel() {
        return searchLabel;
    }
    
    public void updateSearchLabel()
    {
       if (this.isSearchPage()) {
          String searchType = WebUtils.getOHSearchLabel(this.searchOption);
          if (searchType == null) { 
             // default to confirmation number
             searchType = String.valueOf(DisplaySpecServices.CONFNUMFILTER); 
          } 
          String searchOpt = this.getSearchText();
          StringBuffer label = new StringBuffer(searchType);
          if (searchOpt != null && !"".equals(searchOpt)) {
             label.append(": '" + searchOpt + "'");
          }
          this.setSearchLabel(label.toString());
       } else {
          switch (this.status) {
              case DisplaySpecServices.ALLSTATES:
                this.setSearchLabel("All Orders");
              break;
              case DisplaySpecServices.OPENSTATE:
                this.setSearchLabel("Open Orders");
              break;
              case DisplaySpecServices.CLOSEDSTATE:
                this.setSearchLabel("Closed Orders");
              break;
          }
       }
    }

    public void setIsAdjustmentUser (boolean isAdjustmentUser) {
       _isAdjustmentUser = isAdjustmentUser;
    }
    
    public boolean getIsAdjustmentUser () {
       return _isAdjustmentUser;
    }

    public void setCartEmpty(boolean cartEmpty) {
       this.cartEmpty = cartEmpty;
    }

    public boolean isCartEmpty() {
       return cartEmpty;
    }
    
    public String getSearchEmptyString() 
    {
        String searchOpt = this.searchOption;
        int numOfItems = this.totalItems;

        StringBuffer buffer = new StringBuffer("Your search ");
 
        if (numOfItems == 0) 
        {
            if (searchOpt != null) 
            {
               buffer.append("for ");
        
               try {
                   int searchVal = Integer.parseInt(searchOpt);
                   switch (searchVal) {
                       case DisplaySpecServices.ORDERDATEFILTER:
                            buffer = this.getEmptyDateString("Order Date ", buffer);
                       break;
                       case DisplaySpecServices.STARTOFTERMFILTER:
                            buffer = this.getEmptyDateString("Start of Term ", buffer);
                       break;
                       case DisplaySpecServices.NOFILTER:
                       default:
                          String searchLabel = this.searchLabel;
                          buffer.append(searchLabel);
                       break;
                   }
               } catch (NumberFormatException nfe) {
                   buffer.append("unknown");
               }
               
               buffer.append(" found no matches");
           } else {
               buffer.append(" found no matches");
           }
        }
        
        return buffer.toString();
    }

    private StringBuffer getEmptyDateString(String dateType, StringBuffer buffer) 
    {
        buffer.append(dateType);       
        String beginDate = this.getBeginDate();
        String endDate = super.getEndDate();

        if (beginDate != null && !"".equals(beginDate)) {
           buffer.append("'" + beginDate);
           if (endDate != null && !"".equals(endDate)) {
               buffer.append(":");
               buffer.append(endDate + "'");
           } else { 
               buffer.append("'");
           }
        } else {
           if (endDate != null && !"".equals(endDate)) {
               buffer.append("'" + endDate + "'");
           } 
        }
        
        return buffer;
    }

    public String getQueryParm() {
        String qp = "";
        if (this.searchPage) {
           qp = "&rp=main";
        }

        return qp;
    }

    public void setNewSearch(boolean isNewSearch) {
        this.newSearch = isNewSearch;
    }

    public boolean isNewSearch() {
        return this.newSearch;
    }
    
    /*
    * Return the search flag query parm value as "true" or "false". This
    * flag is identified by the query string: sf=xxxx where xxxx = "true" or
    * "false".
    public String getSf() {
       String rtnValue = "false";
       if (this.newSearch == true) {
          rtnValue = "true"; 
       }
       
       return rtnValue; 
    } */
}
