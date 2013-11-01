/*
 * RLOrders.java
 * Wrapping up the list of RL orders in a data structure
 * to make it easier for the UI to deal with...
 */
package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.business.services.DisplaySpecServices;

public class RLOrders  implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RLOrder       _order = null;
    List<RLOrder> _orderList = new ArrayList<RLOrder>();
	int 		  _pageSize = 5;
	int 		  _state = DisplaySpecServices.RLALLSTATES;
	int           _sortBy = DisplaySpecServices.RLORDERDATESORT;
	int           _direction = DisplaySpecServices.SORTDESCENDING;

    //  Constructors.

    public RLOrders() {
        //  Do nothing.
    }

    public RLOrders(List<RLOrder> rlOrders) {
        _orderList = rlOrders;
    }

	public RLOrders(List<RLOrder> rlOrders, int state, int sortBy, int dir) {
		_orderList = rlOrders;
		_state = state;
		_sortBy = sortBy;
		_direction = dir;
	}

    public void addOrderList(List<RLOrder> orderList)     { _orderList.addAll(orderList);     }
	public void addOrderItem(RLOrder item)       { _orderList.add(item); }

    //  Destructors.

    public void clearOrderList()   { _orderList.clear();   }

    //  Operators.

    public void setOrderList(List<RLOrder> rlOrders) { _orderList = rlOrders; }
    public List<RLOrder> getOrderList()              { return _orderList;   }

    public RLOrder getItem(int i) {
        RLOrder item = null;

        if (i < _orderList.size()) {
            item = _orderList.get(i);
        }
        return item;
    }

    //  Helper methods used in display formatting.

    public int getTotalRowCount() { return _orderList.size(); }
	public int getPageSize()      { return _pageSize; }

	public int getTotalPages() {
		int tp = _orderList.size() / _pageSize;
		int rem = _orderList.size() % _pageSize;

		if (rem > 0) tp += 1;
		return tp;
	}

	public List<RLOrder> getPage(int pno) {
		int pageNumber = 0;
		int totalPages = getTotalPages();

		List<RLOrder> p = new ArrayList<RLOrder>();

		if (pno > totalPages) {
			if (totalPages > 0) { pageNumber = 1; }
		}
		else {
			pageNumber = pno;
		}

		if (pageNumber == 0) {
			return p;
		}
		else {
			int start_at = (pageNumber * _pageSize) - _pageSize;
			int end_at = start_at + _pageSize - 1;

			//	Make sure we don't go beyond what we have.

			if (end_at > _orderList.size() - 1) {
				end_at = _orderList.size() - 1;
			}

			//	Build our page.

			for (int i = start_at; i <= end_at; i++) {
				p.add(_orderList.get(i));
			}
		}
		return p;
	}

	//	Same as the getItems routine except we don't bother to
	//	build the list of items.

	public int getCountItemsOnPage(int pno) {
		int pageNumber = 0;
		int totalItems = 0;
		int totalPages = getTotalPages();

		if (pno > totalPages) {
			if (totalPages > 0) { pageNumber = 1; }
		}
		else {
			pageNumber = pno;
		}

		if (pageNumber != 0) {
			int start_at = (pageNumber * _pageSize) - _pageSize;
			int end_at = start_at + _pageSize - 1;

			if (end_at > _orderList.size() - 1) {
				end_at = _orderList.size() - 1;
			}

			totalItems = end_at - start_at + 1;
		}

		return totalItems;
	}

	public void setPageSize(int ps) { _pageSize = ps; }

	public void setCurrent(int i) {
		try {
			_order = _orderList.get(i);
		}
		catch (Exception e) {
			_order = null;
		}
	}

	public RLOrder getCurrent() {
		return _order;
	}

	public void setState(int i)     { _state = i;     }
	public void setSortBy(int i)    { _sortBy = i;    }
	public void setDirection(int i) { _direction = i; }

	public int getState()     { return _state;     }
	public int getSortBy()    { return _sortBy;    }
	public int getDirection() { return _direction; }

	public void clear() { _orderList.clear(); _order = null; }
}