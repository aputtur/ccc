package com.copyright.ccc.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.web.forms.OrderHistoryActionForm;

/**
 * This class holds the contents of the DisplaySpec settings necessary to
 * persist the last known search settings for the main and detail order history
 * tabs
 */
public class OHSearchSpec {
	private String searchOption;
	private int searchValue;
	private String searchText;

	private String beginDate;
	private String endDate;
	private Date beginDateObj;
	private Date endDateObj;
	private String sortOption;
	private int sortValue;
	private String direction;
	private String selectOption; // for detail table select options
	private SimpleDateFormat format;
	private int fromRow; // DisplaySpec result set from row
	private int toRow; // DisplaySpec result set to row

	public OHSearchSpec(DisplaySpec spec) {
		format = new SimpleDateFormat("MM/dd/yyyy");
		if (spec != null) {
			this.searchValue = spec.getSearchBy();
			if (this.searchValue == 0) {
				// Default to confirmation number search
				this.searchValue = DisplaySpecServices.CONFNUMFILTER;
			}
			this.searchText = spec.getSearch();
			this.searchOption = String.valueOf(this.searchValue);
			this.beginDateObj = spec.getSearchFromDate();
			this.endDateObj = spec.getSearchToDate();

			this.sortValue = spec.getSortBy();
			this.sortOption = String.valueOf(this.sortValue);
			int sortOrder = spec.getSortOrder();
			if (sortOrder == 0) {
				this.direction = OrderHistoryActionForm.ASCENDING;
			} else {
				this.direction = OrderHistoryActionForm.DESCENDING;
			}

			switch (this.searchValue) {
			case DisplaySpecServices.PERMISSIONSTATUSFILTER:
				this.selectOption = WebUtils
						.getODPermStatusLookup(this.searchText);
				break;
			case DisplaySpecServices.PERMISSIONTYPEFILTER:
			case DisplaySpecServices.BILLINGSTATUSFILTER:
				this.selectOption = spec.getSearch();
				break;
			}

			// Save the display row indices
			this.fromRow = spec.getDisplayFromRow();
			this.toRow = spec.getDisplayToRow();
		}
	}

	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}

	public String getSearchOption() {
		return searchOption;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSortOption(String sortOption) {
		this.sortOption = sortOption;
	}

	public String getSortOption() {
		return sortOption;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public String getBeginDate() {
		String rtnValue = "";
		if (this.beginDateObj != null) {
			rtnValue = this.format.format(this.beginDateObj);
		}

		return rtnValue;
	}

	public String getEndDate() {
		String rtnValue = "";
		if (this.endDateObj != null) {
			rtnValue = this.format.format(this.endDateObj);
		}

		return rtnValue;
	}

	public void setSearchValue(int searchValue) {
		this.searchValue = searchValue;
	}

	public int getSearchValue() {
		return searchValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;

		if (beginDate != null && !"".equals(beginDate)) {
			try {
				this.beginDateObj = format.parse(beginDate);
			} catch (ParseException pex) {
				Logger _logger = Logger.getLogger(this.getClass());
				_logger
						.error("OHSearchSpec.setBeginDate() unparsable begin date string: "
								+ this.beginDate);
				_logger.error(pex);
			}
		}
	}

	public String get_beginDate() {
		return beginDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;

		if (endDate != null && !"".equals(endDate)) {
			try {
				this.endDateObj = format.parse(endDate);
			} catch (ParseException pex) {
				Logger _logger = Logger.getLogger(this.getClass());
				_logger
						.error("OHSearchSpec.setEndDate() unparsable begin date string: "
								+ this.endDate);
				_logger.error(pex);
			}
		}
	}

	public String get_endDate() {
		return endDate;
	}

	public void setSelectOption(String selectOption) {
		this.selectOption = selectOption;
	}

	public String getSelectOption() {
		return selectOption;
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
}
