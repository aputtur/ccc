package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;

public class ConfirmationWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OrderPurchase confirmation = null;
	
	private List<BundleWrapper> myBundles = new ArrayList<BundleWrapper>();
	private List<ItemWrapper> myItems = new ArrayList<ItemWrapper>();
	
	private Map<Long,BundleWrapper> myBundlesMap = new HashMap<Long,BundleWrapper>();
	
	private List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
	
	private EditConfirmation lastEdit = null;

	public ConfirmationWrapper ( OrderPurchase confirmation ) {
		super();
		this.confirmation = confirmation;
	}
	
	public boolean isHasLastEdit() {
		if ( getLastEdit() != null ) {
			return true;
		}
		return false;
	}

	public String getOrderDateFMT() {
		return MiscUtils.formatMMddyyyyDateSlashed(getConfirmation().getOrderDate());
	}
	
	public OrderPurchase getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(OrderPurchase confirmation) {
		this.confirmation = confirmation;
	}

	public List<BundleWrapper> getMyBundles() {
		return myBundles;
	}

	public void setMyBundles(List<BundleWrapper> myBundles) {
		this.myBundles = myBundles;
	}

	public List<ItemWrapper> getMyItems() {
		return myItems;
	}

	public void setMyItems(List<ItemWrapper> myItems) {
		this.myItems = myItems;
	}
	
	public void addBundleWrapper( BundleWrapper bundle , Long bundleId ) {
		getMyBundles().add(bundle);
		if ( !myBundlesMap.containsKey(bundleId) ) {
			myBundlesMap.put(bundleId, bundle);
		}
	}

	protected Map<Long, BundleWrapper> getMyBundlesMap() {
		return myBundlesMap;
	}

	protected void setMyBundlesMap(Map<Long, BundleWrapper> myBundlesMap) {
		this.myBundlesMap = myBundlesMap;
	}
	
	//*************  MISSING CONFIRMATION FIELDS *************/

	private MissingConfirmationFields missing = new MissingConfirmationFields();

	public MissingConfirmationFields getMissing() {
		return missing;
	}

	public void setMissing(MissingConfirmationFields missing) {
		this.missing = missing;
	}

	public List<ProcessMessage> getProcessMessages() {
		return processMessages;
	}

	public void setProcessMessages(List<ProcessMessage> processMessages) {
		this.processMessages = processMessages;
	}

	public EditConfirmation getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(EditConfirmation lastEdit) {
		this.lastEdit = lastEdit;
	}


}
