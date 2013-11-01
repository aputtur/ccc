package com.copyright.ccc.business.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;

public class OrderReportResults  implements Serializable {

    
	private static final long serialVersionUID = 1L;
	
	OrderLicenses licenses = new OrderLicenses();
    OrderPurchases purchases = new OrderPurchases();
    List<User> users = new ArrayList<User>();
    
    List<ItemWrapper> wrappedResults;

    long totalRows = 0;

	public OrderLicenses getLicenses() {
		return licenses;
	}

	public void setLicenses(OrderLicenses licenses) {
		this.licenses = licenses;
	}

	public OrderPurchases getPurchases() {
		return purchases;
	}

	public void setPurchases(OrderPurchases purchases) {
		this.purchases = purchases;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public List<ItemWrapper> getWrappedResults() {
		return wrappedResults;
	}

	public void setWrappedResults(List<ItemWrapper> wrappedResults) {
		this.wrappedResults = wrappedResults;
	}
    
}
