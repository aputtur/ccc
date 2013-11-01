package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;

public class EditItemWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private EditItem item = new EditItem();
	
	private boolean toBeSaved = false;
	private boolean toBeReturned = false;
	private boolean updatePricingOnly = false;

	public EditItem getItem() {
		return item;
	}

	public void setItem(EditItem item) {
		this.item = item;
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

	public boolean isUpdatePricingOnly() {
		return updatePricingOnly;
	}

	public void setUpdatePricingOnly(boolean updPricingOnly) {
		this.updatePricingOnly = updPricingOnly;
		item.setUpdatePricingOnly(updPricingOnly);
	}


}
