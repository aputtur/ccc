package com.copyright.ccc.web.forms;

import java.util.Collection;
import java.util.List;

import com.copyright.ccc.business.data.RLOrder;
import com.copyright.ccc.business.data.RLOrders;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.util.DropDownElement;
import com.copyright.ccc.web.util.WebUtils;

public class RLOrderHistoryForm extends CCValidatorForm
{
	private static final long serialVersionUID = 1L;
	
	//	Save our sort/display specs in the form.

        //according to the spec the completed is the default state
	private int _state = DisplaySpecServices.RLCOMPLETEDSTATE;
	private int _sortBy = DisplaySpecServices.RLORDERDATESORT;
	private int _direction = DisplaySpecServices.SORTDESCENDING;
	private int _pageSize = 25;
	private int _currentPage = 1;
	private int _reqState = DisplaySpecServices.RLCOMPLETEDSTATE;
	private int _reqSortBy = DisplaySpecServices.RLORDERDATESORT;
	private int _reqDirection = DisplaySpecServices.SORTDESCENDING;
	private int _reqPageSize = 25;
        
	private int _startPage = 1;

	private boolean _reload = false;
	private boolean _rlServiceUp = true;


	private Collection<DropDownElement> _sortOptions = WebUtils.getRLOHSortOptions();

	//	The stuff we need to display.

	private RLOrders _orders = new RLOrders();

	public RLOrderHistoryForm() {
		// Should be no need to do anything here.
	}

	public RLOrderHistoryForm(RLOrders orders) {
		_orders = orders;
		_state = _orders.getState();
		_sortBy = _orders.getSortBy();
		_direction = _orders.getDirection();
	}

	public int getState()         { return _orders.getState();      }
	public int getSortBy()        { return _orders.getSortBy();     }
	public int getDirection()     { return _orders.getDirection();  }
	public int getTotalPages()    { return _orders.getTotalPages(); }
	public RLOrder getItem(int i) { return _orders.getItem(i);      }
	public int getPageSize()      { return _pageSize;               }
	public int getCurrentPage()   { return _currentPage;            }

	public int getSavedState()     { return _state;     }
	public int getSavedSortBy()    { return _sortBy;    }
	public int getSavedDirection() { return _direction; }

	public void setOrders(RLOrders orders) {
		saveState();
		_orders = orders;
		_reload = false;
	}

	public void saveState() {
		_state = _orders.getState();
		_sortBy = _orders.getSortBy();
		_direction = _orders.getDirection();
	}

	public void setCurrentPage(int pn) {
		_currentPage = pn;
		if (pn < 1) _currentPage = 1;
		if (pn > _orders.getTotalPages()) _currentPage = _orders.getTotalPages();
		if (_currentPage == 0) _currentPage = 1;
        setStartPage(((_currentPage-1)/5)*5+1);
		_reload = true;
	}

	public void setNextPage(int i) {
		setCurrentPage(_currentPage + 1);
	}

	public void setPreviousPage(int i) {
		setCurrentPage(_currentPage - 1);
	}

	//	getItems returns the current page-full of rightslink orders.

	public List<RLOrder> getItems() {
		return _orders.getPage(_currentPage);
	}

	public int getItemCount() {
		return _orders.getCountItemsOnPage(_currentPage);
	}

	public int getTotalItemsCount() {
		return _orders.getTotalRowCount();
	}

	public Collection<DropDownElement> getSortOptions() { return _sortOptions;        }
	public String getPageAction()      { return "rlOrderHistory.do"; }

	public boolean isEmpty() {
		return _orders.getTotalRowCount() == 0;
	}

	//	These are for specifically handling changes in sort and load data.

	public void setState(int stateFlag) {
		_reqState = stateFlag; // Ha ha!  State flag.
		if (_reqState != _state) _reload = true;
	}

	public void setSortBy(int sortFlag) {
		_reqSortBy = sortFlag;
		if (_reqSortBy != _sortBy) _reload = true;
	}

	public void setDirection(int dirFlag) {
		_reqDirection = dirFlag;
		if (_reqDirection != _direction) _reload = true;
	}

	public void setPageSize(int ps) {
		_reqPageSize = ps;
		// _orders.setPageSize(ps);
		_reload = true;
	}

	public int getReqState()     { return _reqState;     }
	public int getReqSortBy()    { return _reqSortBy;    }
	public int getReqDirection() { return _reqDirection; }
	public int getReqPageSize()  { return _reqPageSize;  }

	public boolean hasChanged() { return _reload; }

	public void reset() {
		_state = DisplaySpecServices.RLCOMPLETEDSTATE;
		_sortBy = DisplaySpecServices.RLORDERDATESORT;
		_direction = DisplaySpecServices.SORTDESCENDING;
		_pageSize = 5;
		_currentPage = 1;

		//	The stuff we need to display.

		_orders.clear();
	}

    public void setStartPage(int startPage) {
        this._startPage = startPage;
    }

    public int getStartPage() {
        return _startPage;
    }
    
	public boolean isRlServiceUp() {
		return _rlServiceUp;
	}

	public void setRlServiceUp(boolean serviceUp) {
		_rlServiceUp = serviceUp;
	}
}
