package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.copyright.ccc.business.services.DisplaySpecServices;

public class DisplaySpec implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ASCENDING = 0;
    public static final int DESCENDING = 1;
    public static final int PURCHASE = 1;
    public static final int LICENSE = 2;
    public static final int DEFAULT_RESULTS_PER_PAGE = 25;

    
    private int _displaySpecType; // 1 = Purchase, 2= License
     
    private boolean forceReRead = false;
    private int _sortBy;
    private int _sortOrder; // 0 = Asc, 1 = Desc
    private int _searchBy;
    private String _search;
    private long _purchaseId;
    private Date _searchFromDate;
    private Date _searchToDate;
    private int _orderStateFilter;
    private int _resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
    private int _displayFromRow;
    private int _displayToRow;
    private int _lastSortBy;
    private int _lastSortOrder;
    private int _lastSearchBy;
    private String _lastSearch;
    private long _lastPurchaseId;
    private Date _lastSearchFromDate;
    private Date _lastSearchToDate;
    private Date _defaultDate;
    private int _lastOrderStateFilter;
    private int _lastResultsPerPage = DEFAULT_RESULTS_PER_PAGE;
    private int _lastDisplayFromRow;
    private int _lastDisplayToRow;

    public DisplaySpec() {

        _sortBy = 0;
        _sortOrder = 1;
        _searchBy = 0;
        _search = "";
        _purchaseId = 0;
        _defaultDate = new Date();
        _searchFromDate = new Date();
        _searchFromDate.setTime(_defaultDate.getTime());
        _searchToDate = new Date();
        _searchToDate.setTime(_defaultDate.getTime());
        _orderStateFilter = 0;
        _resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
        _displayFromRow = 0;
        _displayToRow = DEFAULT_RESULTS_PER_PAGE - 1;
        _lastSortBy = 0;
        _lastSortOrder = 1;
        _lastSearchBy = 0;
        _lastSearch = "";
        _lastPurchaseId = 0;
        _lastSearchFromDate = new Date();
        _lastSearchFromDate.setTime(_defaultDate.getTime());
        _lastSearchToDate = new Date();
        _lastSearchToDate.setTime(_defaultDate.getTime());
        _lastOrderStateFilter = 0;
        _lastResultsPerPage = DEFAULT_RESULTS_PER_PAGE;
        _lastDisplayFromRow = 0;
        _lastDisplayToRow = DEFAULT_RESULTS_PER_PAGE - 1;

    }

    public void resetLastDisplaySpecAttributes() {
        _lastSortBy = _sortBy;
        _lastSortOrder = _sortOrder;
        _lastSearchBy = _searchBy;
        _lastSearch = _search;
        _lastPurchaseId = _purchaseId;
        _lastSearchFromDate = _searchFromDate;
        _lastSearchToDate = _searchToDate;
        _lastOrderStateFilter = _orderStateFilter;
        _lastResultsPerPage = _resultsPerPage;
        _lastDisplayFromRow = _displayFromRow;
        _lastDisplayToRow = _displayToRow;
    }

    public void resetSearchDefault() {
        //_searchBy = 0;
        _search = "";
        _purchaseId = 0;
        //_searchFromDate.setTime(_defaultDate.getTime());
        //_searchToDate.setTime(_defaultDate.getTime());
        _displayFromRow = 0;
        _resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
        _displayToRow = _resultsPerPage - 1;
    }

    public void resetSortDefault() {
        _sortBy = 0;
        _sortOrder = 1;
        _displayFromRow = 0;
        _resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
        _displayToRow = _resultsPerPage - 1;
    }

    public boolean isDisplaySpecParametersChanged() {
        if (_sortBy != _lastSortBy ||
            _searchBy != _lastSearchBy ||
            ! _search.equalsIgnoreCase(_lastSearch) ||
            _searchFromDate.getTime() != _lastSearchFromDate.getTime() ||
            _searchToDate.getTime() != _lastSearchToDate.getTime() ||
            _sortOrder != _lastSortOrder ||
            _orderStateFilter != _lastOrderStateFilter ||
            _purchaseId != _lastPurchaseId) {
                return true;
            } else {
                return false;
            }
    }

    public void setSortBy(int sortBy) {
//        if (_sortBy != sortBy) {
//            _lastSortBy = _sortBy;
//        }
        _sortBy = sortBy;
    }


    public int getSortBy() {
        return _sortBy;
    }


    public void setSearchBy(int searchBy) {
//        if (_searchBy != searchBy) {
//            _lastSearchBy = _searchBy;
//        }
        _searchBy = searchBy;
    }


    public int getSearchBy() {
        return _searchBy;
    }


    public void setSearch(String search) {

//        if (! _search.equalsIgnoreCase(search)) {
//            _lastSearch = new String(_search);
//        }
        _search = search;
    }

    public void setPurchaseId(long purchaseId) {
//        if (_purchaseId != purchaseId) {
//                _lastPurchaseId = _purchaseId;
//            }
            _purchaseId = purchaseId;
    }

    public long getPurchaseId() {
        return _purchaseId;
    }

    public String getSearch() {
        if (_searchBy == DisplaySpecServices.ORDERDATEFILTER ||
            _searchBy == DisplaySpecServices.REPUBLICATIONDATEFILTER) {
            SimpleDateFormat formatter
                        = new SimpleDateFormat ("dd-MMM-yyyy");
            String fromDateString = "";
            String toDateString = "";

            if (_searchFromDate != null) {
                    fromDateString = formatter.format(_searchFromDate);
            }
            if (_searchToDate != null) {
                    toDateString = formatter.format(_searchToDate);
            }
            
            _search = fromDateString + ":" + toDateString;

        }

        return _search;
    }
    
    
    public Date getFromSearchDate() {
        if (_searchBy == DisplaySpecServices.ORDERDATEFILTER ||
            _searchBy == DisplaySpecServices.REPUBLICATIONDATEFILTER) {
            SimpleDateFormat formatter
                        = new SimpleDateFormat ("dd-MMM-yyyy");
            String fromDateString = "";

            if (_searchFromDate != null) {
                    fromDateString = formatter.format(_searchFromDate);
                    _search = fromDateString;
            }
        }

        return _searchFromDate;
    }
    
    
    
    public Date getToSearchDate() {
        if (_searchBy == DisplaySpecServices.ORDERDATEFILTER ||
            _searchBy == DisplaySpecServices.REPUBLICATIONDATEFILTER) {
            SimpleDateFormat formatter
                        = new SimpleDateFormat ("dd-MMM-yyyy");
            String toDateString = "";

            if (_searchToDate != null) {
                    toDateString = formatter.format(_searchToDate);
                    _search = toDateString;
            }
        }

        return _searchToDate;
    }


    public void setSearchFromDate(Date searchFromDate) {
        // Bill Removed: _searchFromDate was null initially causing NP Exception
        //if (_searchFromDate.getTime() != searchFromDate.getTime()) {
        //    _lastSearchFromDate = _searchFromDate;
        // }
//        _lastSearchFromDate = _searchFromDate;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(searchFromDate);
 //       _searchFromDate = new Date();
        _searchFromDate = calendar.getTime();
//        _lastSearch = "";
    }


    public Date getSearchFromDate() {
        return _searchFromDate;
    }


    public void setSearchToDate(Date searchToDate) {
        // Bill Added: Test for _searchToDate is Null to prevent NP Exception
//        if (_searchToDate != null &&_searchToDate.getTime() != searchToDate.getTime()) {
//           _lastSearchToDate = _searchToDate;
//        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(searchToDate);
//        _searchToDate = new Date();
        _searchToDate = calendar.getTime();
//        _lastSearch = "";
    }


    public Date getSearchToDate() {
        return _searchToDate;
    }


    public void setOrderStateFilter(int orderStateFilter) {
//        if (_orderStateFilter != orderStateFilter) {
//            _lastOrderStateFilter = _orderStateFilter;
//        }
        _orderStateFilter = orderStateFilter;
    }


    public int getOrderStateFilter() {
        return _orderStateFilter;
    }


    public void setResultsPerPage(int resultsPerPage) {
//        if (_resultsPerPage != resultsPerPage) {
//            _lastResultsPerPage = _resultsPerPage;
//        }
        _resultsPerPage = resultsPerPage;
        _displayToRow = _displayFromRow + (_resultsPerPage - 1);
    }


    public int getResultsPerPage() {
        return _resultsPerPage;
    }


    public void setDisplayFromRow(int displayFromRow) {
//        if (_displayFromRow != displayFromRow) {
//            _lastDisplayFromRow = _displayFromRow;
//            _lastDisplayToRow = _displayToRow;
//        }
        _displayFromRow = displayFromRow;
        _displayToRow = _displayFromRow + (_resultsPerPage - 1);
    }


    public int getDisplayFromRow() {
        return _displayFromRow;
    }


    public void setDisplayToRow(int displayToRow) {
//        if (_displayToRow != displayToRow) {
//            _lastDisplayToRow = _displayToRow;
//        }
        _displayToRow = displayToRow;
    }


    public int getDisplayToRow() {
        return _displayToRow;
    }


    public int getLastSortBy() {
        return _lastSortBy;
    }

    public int getLastSortOrder() {
        return _lastSortOrder;
    }
    
    public int getLastSearchBy() {
        return _lastSearchBy;
    }


    public String getLastSearch() {
        return _lastSearch;
    }

    public long getLastPurchaseId() {
        return _lastPurchaseId;
    }

    public Date getLastSearchFromDate() {
        return _lastSearchFromDate;
    }


    public Date getLastSearchToDate() {
        return _lastSearchToDate;
    }


    public int getLastOrderStateFilter() {
        return _lastOrderStateFilter;
    }


    public int getLastResultsPerPage() {
        return _lastResultsPerPage;
    }


    public int getLastDisplayFromRow() {
        return _lastDisplayFromRow;
    }


    public int getLastDisplayToRow() {
        return _lastDisplayToRow;
    }

    public void setDisplaySpecType(int displaySpecType) {
        this._displaySpecType = displaySpecType;
    }

    public int getDisplaySpecType() {
        return _displaySpecType;
    }

    public void setSortOrder(int sortOrder) {
//        if (_sortOrder != sortOrder) {
//            _lastSortOrder = _sortOrder;
//        }
        this._sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return _sortOrder;
    }

    public void setForceReRead(boolean forceReRead) {
        this.forceReRead = forceReRead;
    }

    public boolean isForceReRead() {
        return forceReRead;
    }

    public Date getDefaultDate() {
        return _defaultDate;
    }
}
