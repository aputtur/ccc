package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.web.actions.ordermgmt.util.WebUtils;

public class BundleWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OrderBundle bundle = null;

	private List<ItemWrapper> myItems = new ArrayList<ItemWrapper>();
		
	private EditBundle lastEdit = null;
	private boolean toBeSaved = false;
	private boolean toBeReturned = false;
	
	private List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();

	public BundleWrapper ( OrderBundle bundle ) {
		super();
		this.bundle = bundle;
	}

	public String getCourseNameJSP() {
		if ( getBundle() != null ) {
			if ( StringUtils.isNotEmpty( getBundle().getCourseName() ) ) {
				return OrderServicesHelper.escapeForJavascript(getBundle().getCourseName());
			}
		}
		return "Project Header";
	}
	public boolean isHasLastEdit() {
		if ( getLastEdit() != null ) {
			return true;
		}
		return false;
	}
	
	public EditBundle getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(EditBundle lastEdit) {
		this.lastEdit = lastEdit;
	}

	public OrderBundle getBundle() {
		return bundle;
	}

	public void setBundle(OrderBundle bundle) {
		this.bundle = bundle;
	}

	public List<ItemWrapper> getMyItems() {
		return myItems;
	}

	public void setMyItems(List<ItemWrapper> myItems) {
		this.myItems = myItems;
	}
	
	public String getEscapedCourseName() {
		String escaped = "Bundle Information";
		if ( getBundle() != null ) {
			if ( StringUtils.isNotEmpty(getBundle().getCourseName())) {
				escaped = WebUtils.escapeForJavascript(getBundle().getCourseName());
			}
		}
		return escaped;
	}
	
	public boolean isBundleRLS() {
		boolean isRLS = false;
		if ( getMyItems() != null && !getMyItems().isEmpty() ) {
			if ( getMyItems().get(0).getItem() != null ) {
				if ( StringUtils.isNotEmpty(getMyItems().get(0).getItem().getCategoryCd()) ) {
					isRLS = getMyItems().get(0).getItem().getCategoryCd().equalsIgnoreCase("RLS");
				}
			}
		}
		return isRLS;
	}

	public List<ProcessMessage> getProcessMessages() {
		return processMessages;
	}

	public void setProcessMessages(List<ProcessMessage> processMessages) {
		this.processMessages = processMessages;
	}

	public boolean isToBeSaved() {
		return toBeSaved;
	}

	public void setToBeSaved(boolean toBeSaved) {
		this.toBeSaved = toBeSaved;
	}

	public boolean isToBeReturned() {
		return toBeReturned;
	}

	public void setToBeReturned(boolean toBeReturned) {
		this.toBeReturned = toBeReturned;
	}

}
