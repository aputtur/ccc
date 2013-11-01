package com.copyright.ccc.business.services;

import java.util.Date;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.security.UserContextService;

public class DisplaySpecServices {

    public static final int SORTASCENDING = 0;
    public static final int SORTDESCENDING = 1;
    
 // Order Purchase status filter
    public static final int OPENSTATE= 0;
    public static final int CLOSEDSTATE = 1;
    public static final int ALLSTATES = 2;
    
 // Order Purchase Sorts
    public static final int ORDERDATESORT = 0;
    public static final int CONFNUMSORT = 1;
    public static final int SCHOOLSORT = 2;
    public static final int STARTOFTERMSORT = 3;
    public static final int COURSENAMESORT = 4;
    public static final int COURSENUMBERSORT = 5;
    public static final int INSTRUCTORSORT = 6;
    public static final int YOURREFERENCESORT = 7;
    public static final int YOURACCTREFERENCESORT = 17;
    
// Order License Sorts
//  ORDERDATESORT = 0; 
    public static final int PUBLICATIONTITLESORT = 8;
    public static final int ORDERDETAILIDSORT = 9;
    public static final int PERMISSIONTYPESORT = 10;
    public static final int PERMISSIONSTATUSSORT = 11;
    public static final int BILLINGSTATUSSORT = 12;
// YOURREFERENCESORT = 7;
    public static final int INVOICENUMBERSORT = 13;
    public static final int REPUBLICATIONTITLESORT = 14;
    public static final int REPUBLICATIONDATESORT = 15;
    public static final int REPUBLICATIONPUBLISHERSORT = 16;
    public static final int LASTUPDATEDATESORT = 18;
    
    
// OrderPurchase Searches
    public static final int NOFILTER = 0;
    public static final int CONFNUMFILTER = 1;
    public static final int ORDERDATEFILTER = 2;
    public static final int SCHOOLFILTER = 3;
    public static final int COURSENAMEFILTER = 4;
    public static final int COURSENUMBERFILTER = 5;    
    public static final int INSTRUCTORFILTER = 6;
    public static final int YOURREFERENCEFILTER = 7;
    public static final int STARTOFTERMFILTER = 17;
    public static final int YOURACCTREFERENCEFILTER = 18;
    public static final int INVOICEDATEFILTER = 19;
    public static final int JOB_TCKT_NUM = 20;
    public static final int LIC_NUM = 21;
    public static final int CUST_REF_NUM = 22;
    
//  OrderLicense Searches
//  public static final int NOFILTER = 0;
//  public static final int CONFNUMFILTER = 1;
    public static final int PUBLICATIONTITLEFILTER = 8;
//    public final int ORDERDATEFILTER = 2;
    public static final int ORDERDETAILIDFILTER = 9;
    public static final int PERMISSIONTYPEFILTER = 10;
    public static final int PERMISSIONSTATUSFILTER = 11;
    public static final int BILLINGSTATUSFILTER = 12;
//    public final int YOURREFERENCEFILTER = 7;
    public static final int INVOICENUMBERFILTER = 13;
    public static final int REPUBLICATIONTITLEFILTER = 14;
    public static final int REPUBLICATIONDATEFILTER = 15;
    public static final int REPUBLICATIONPUBLISHERFILTER = 16;
    public static final int ARTICLETITLEFILTER = 23;
    public static final int TYPEOFUSEFILTER = 24;
    public static final int JOBTICKETNUMBERFILTER = 25;
    public static final int LICENSENUMBERFILTER = 26;
    public static final int YOURLINEITEMREFERENCEFILTER = 27;
    public static final int CHAPTERTITLEFILTER = 28;
    public static final int SPECIALORDERFILTER = 29;
    
    

    //  RIGHTSLINK constants.  Most of them are the same or similar
    //  to the afore-defined constants.  But because the orders do
    //  contain different data, I thought it would be safer to give
    //  RIGHTSLINK their own definitions.  Start with RL status
    //  constants...

    public static final int RLCOMPLETEDSTATE= 0;
    public static final int RLPENDINGSTATE = 1;
    public static final int RLCANCELEDSTATE = 2;
    public static final int RLCREDITEDSTATE = 3;
	public static final int RLDENIEDSTATE = 4;
    public static final int RLALLSTATES = 5;

    //  ...RL Sort Orders...

    public static final int RLORDERDATESORT = 0;
    public static final int RLPUBLICATIONTITLESORT = 1;
    public static final int RLARTICLETITLESORT = 2;
    public static final int RLTYPEOFUSESORT = 3;
    public static final int RLFEESORT = 4;

    //  ...And there are no search specifications for RIGHTSLINK.

    private DisplaySpecServices() {
    
    }

    private static void updateUserContext(DisplaySpec displaySpec) {
        switch (displaySpec.getDisplaySpecType()) {
            case 1: UserContextService.setPurchaseDisplaySpec(displaySpec); break;
            case 2: UserContextService.setLicenseDisplaySpec(displaySpec); break;
        }        
    }
    
    public static void resetSearchDefault (DisplaySpec displaySpec) {
        displaySpec.setSearchBy(0);
        displaySpec.setSearch("");
        displaySpec.setSearchFromDate(displaySpec.getDefaultDate());
        displaySpec.setSearchToDate(displaySpec.getDefaultDate());
        displaySpec.setDisplayFromRow(0);
        displaySpec.setDisplayToRow(displaySpec.getResultsPerPage() - 1);    
 
        updateUserContext(displaySpec);
    }
    
    public static void resetSortDefault (DisplaySpec displaySpec) {
        displaySpec.setSortBy(SORTDESCENDING);
        displaySpec.setDisplayFromRow(0);
        displaySpec.setDisplayToRow(displaySpec.getResultsPerPage() - 1);        

        updateUserContext(displaySpec);
    }
    
    public static void updateDisplaySpec(DisplaySpec displaySpec) {
        displaySpec.resetLastDisplaySpecAttributes();
        
        updateUserContext(displaySpec);
    }
    
    public static void setPreviousDisplayPage(DisplaySpec displaySpec) {
        if (displaySpec.getResultsPerPage() >= displaySpec.getDisplayFromRow()) {
            displaySpec.setDisplayFromRow(displaySpec.getDisplayFromRow() - displaySpec.getResultsPerPage());
            displaySpec.setDisplayToRow((displaySpec.getDisplayFromRow() + displaySpec.getResultsPerPage()) - 1);
        } else {
            displaySpec.setDisplayFromRow(0);
            displaySpec.setDisplayToRow((displaySpec.getDisplayFromRow() + displaySpec.getResultsPerPage()) - 1);      
        }

        updateUserContext(displaySpec);
    }
    
    public static void setNextDisplayPage(DisplaySpec displaySpec) {
        displaySpec.setDisplayFromRow(displaySpec.getDisplayFromRow() + displaySpec.getResultsPerPage());
        displaySpec.setDisplayToRow((displaySpec.getDisplayFromRow() + displaySpec.getResultsPerPage()) - 1);              

        updateUserContext(displaySpec);
    }
    
    public static void setMinDisplayRow(DisplaySpec displaySpec) {
        displaySpec.setDisplayFromRow(0);              
        displaySpec.setDisplayToRow(displaySpec.getResultsPerPage() - 1);

        updateUserContext(displaySpec);
    }

    public static void setDisplayPage(DisplaySpec displaySpec, int displayPage) {
        displaySpec.setDisplayFromRow((displayPage - 1) * displaySpec.getResultsPerPage());
        displaySpec.setDisplayToRow((displaySpec.getDisplayFromRow() + displaySpec.getResultsPerPage()) - 1);                      
        
        updateUserContext(displaySpec);        
    }

    public static void setMaxDisplayRow(DisplaySpec displaySpec, int lastDisplayToRow) {
        displaySpec.setDisplayToRow(lastDisplayToRow);              
        displaySpec.setDisplayFromRow(lastDisplayToRow - displaySpec.getResultsPerPage() + 1);

        updateUserContext(displaySpec);
    }

    public static void setOrderStateFilter(DisplaySpec displaySpec, int orderStateFilter) {
        displaySpec.setOrderStateFilter(orderStateFilter);
        
        updateUserContext(displaySpec);        
    }

    public static void setSortBy(DisplaySpec displaySpec, int sortBy) {
        displaySpec.setSortBy(sortBy);
        
        updateUserContext(displaySpec);        
    }

    public static void setSortOrder(DisplaySpec displaySpec, int sortOrder) {
        displaySpec.setSortOrder(sortOrder);
        
        updateUserContext(displaySpec);        
    }

    public static void setSearch(DisplaySpec displaySpec, int searchBy, String search) {
        displaySpec.setSearchBy(searchBy);
        displaySpec.setSearch(search);
        
        updateUserContext(displaySpec);        
    }

    public static void setSearch(DisplaySpec displaySpec, int searchBy, int search) {
        displaySpec.setSearchBy(searchBy);
        displaySpec.setSearch(String.valueOf(search));
        
        updateUserContext(displaySpec);        
    }

    public static void setSearch(DisplaySpec displaySpec, int searchBy, long search) {
        displaySpec.setSearchBy(searchBy);
        displaySpec.setSearch(String.valueOf(search));
        
        updateUserContext(displaySpec);        
    }

    public static void setSearch(DisplaySpec displaySpec, int searchBy, Long search) {
        displaySpec.setSearchBy(searchBy);
        displaySpec.setSearch(search.toString());
        
        updateUserContext(displaySpec);        
    }

    public static void setSearch(DisplaySpec displaySpec, int searchBy, Date searchFromDate, Date searchToDate ) {
        displaySpec.setSearchBy(searchBy);
        displaySpec.setSearchFromDate(searchFromDate);
        displaySpec.setSearchToDate(searchToDate);
//  No longer needed because the search string is now created from the dates when you do a displaySpec.getSearch
//        SimpleDateFormat formatter
//                    = new SimpleDateFormat ("dd-MM-yyyy");
//        String fromDateString = new String ("");
//        String toDateString = new String ("");

//        if (searchFromDate != null) {
//                fromDateString = formatter.format(searchFromDate);
//        }
//        if (searchToDate != null) {
//                toDateString = formatter.format(searchToDate);
//        }
        
//        displaySpec.setSearch(fromDateString + ":" + toDateString);
        
        updateUserContext(displaySpec);        
    }

}
