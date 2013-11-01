package com.copyright.ccc.web.forms;

import java.util.Collection;
import java.util.TreeMap;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.web.util.DropDownElement;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.order.OrderMgmtSearchSortConstants;
import com.copyright.svc.ldapuser.api.data.LdapUser;
import com.copyright.svc.rightsResolver.api.data.TouCategoryTypeEnum;

public class OrderDetailActionForm extends OrderHistoryValidatorForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Collection<DropDownElement> searchOptionList;
	private Collection<DropDownElement> sortOptionList;
	private Collection<DropDownElement> permissionTypeList;
	private Collection<DropDownElement> permissionStatusList;
	private Collection<DropDownElement> billingStatusList;
	private Collection<DropDownElement> specialOrderUpdateList;
	private OrderLicenses orderLicenseList;

	private String id;
	private String confirmationNumber;
	private String searchText; // Search display lable for search result tag
	private String searchOption; // Search select input
	private String searchLabel; // search input text
	private String sortOption; // Sort select input
	private String permissionStatus;
	private String permissionType;
	private String billingStatus;
	private String specialOrderUpdate;

	private String direction; // User sort direction
	private int status; // main search page attribute - open/closed/all

	private DisplaySpec spec;
	private String pageAction;
	private int totalPages; // total display pages for displayable items
	private int totalItems; // all items in result set
	private int displayItems; // displayable items in result set
	private int currentPage; // The current displayed scroll page
	private int startPage; // The first visible scroll page link
	private int startItem;
	private int endItem;
	private int scrollPage;
	private boolean _isAdjustmentUser; // If true display the adjustment
	private boolean _isAlwaysInvoice = false; // If true Accept Special Order Invoice
	// navigation options
	private boolean searchPage; // form control for search display
	private boolean cartAcademic; // form control copy license
	private boolean cartEmpty; // form control copy license
	private boolean searchNotSort; // form control for determining sort settings

	private String firstName;
	private String lastName;
	private String companyName;
	private String email;
	private String phone;
	private String payMethod;
	private String promCode;
	private String univInst;
	private String startOfTerm;
	private String courseNum;
	private String numOfStud;
	private String instructor;
	private String yourRef;
	private String acctRef;
	private String ordEntBy;
	private OrderBundle orderBundle;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(String courseNum) {
		this.courseNum = courseNum;
	}

	public String getNumOfStud() {
		return numOfStud;
	}

	public void setNumOfStud(String numOfStud) {
		this.numOfStud = numOfStud;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getYourRef() {
		return yourRef;
	}

	public void setYourRef(String yourRef) {
		this.yourRef = yourRef;
	}

	public String getAcctRef() {
		return acctRef;
	}

	public void setAcctRef(String acctRef) {
		this.acctRef = acctRef;
	}

	public String getOrdEntBy() {
		return ordEntBy;
	}

	public void setOrdEntBy(String ordEntBy) {
		this.ordEntBy = ordEntBy;
	}

	private String courseName;

	public String getUnivInst() {
		return univInst;
	}

	public void setUnivInst(String univInst) {
		this.univInst = univInst;
	}

	public String getStartOfTerm() {
		return startOfTerm;
	}

	public void setStartOfTerm(String startOfTerm) {
		this.startOfTerm = startOfTerm;
	}

	private LdapUser user;

	private final static String popupTitle = "Rightsholder Terms";
	private boolean rlUser = false;

	public boolean isRlUser() {
		return rlUser;
	}

	public void setRlUser(boolean rluser) {
		this.rlUser = rluser;
	}

	public OrderDetailActionForm() {
		this.searchOption = String.valueOf(DisplaySpecServices.NOFILTER);
		this.sortOption = "0";
		this.direction = OrderHistoryActionForm.DESCENDING;
		this.currentPage = 1;
		this.startPage = 1;
		this.pageAction = "orderDetail.do"; // paging link action
		this.status = DisplaySpecServices.OPENSTATE;
		this.orderLicenseList = new OrderLicenses();
		super.setBeginDateFailed(false);
		super.setEndDateFailed(false);
		this.setSearchNotSort(false); // default to /orderDetail.do
	}

	/*
	 * Method called to populate this form's internal DisplaySpec object from
	 * all of the form's internal attributes. Called by struts action method
	 * after the struts controller has populated the form.
	 */
	private void initDisplaySpec() {
		this.spec = UserContextService.getLicenseDisplaySpec();
		this.spec.setResultsPerPage(OrderHistoryActionForm.RESULTS_PER_PAGE);
		this.spec.setPurchaseId(0L);
		this.spec.setForceReRead(true); // for DB read in case user added a new
		// order

		int sortDir = DisplaySpec.DESCENDING; // default sort option
		String dir = this.getDirection();
		if ("ascending".equals(dir)) {
			sortDir = DisplaySpec.ASCENDING;
		}
		this.spec.setSortOrder(sortDir);

		// if (DisplaySpecServices.OPENSTATE == this.status) {
		// this.spec.setOrderStateFilter(DisplaySpecServices.OPENSTATE);
		// } else if (DisplaySpecServices.CLOSEDSTATE == this.status) {
		// this.spec.setOrderStateFilter(DisplaySpecServices.CLOSEDSTATE);
		// } else {
		// this.spec.setOrderStateFilter(DisplaySpecServices.ALLSTATES);
		// }

		this.spec.setOrderStateFilter(DisplaySpecServices.ALLSTATES);

		int sortBy = this.getSortValue();
		this.spec.setSortBy(sortBy);

		int filterBy = DisplaySpecServices.NOFILTER; // default search option -
		// NOFILTER

		try {
			filterBy = Integer.parseInt(this.searchOption);
		} catch (NumberFormatException nfe) {
			filterBy = DisplaySpecServices.NOFILTER; // default search option
		}

		this.spec.setSearchBy(filterBy);
		String text = this.getSearchText();

		// Now set the search String based on user input
		switch (filterBy) {
		case DisplaySpecServices.ORDERDATEFILTER: // 2 = Order Date
		case DisplaySpecServices.REPUBLICATIONDATEFILTER: // 15 = Repub Date
			if (super.getBeginDate() == null || super.getEndDate() == null) {
				// One or more date fields was empty so default to NOFILTER
				filterBy = DisplaySpecServices.NOFILTER;
				this.spec.setSearchBy(filterBy);
			} else {
				boolean goodDates = super.validateDates(this.spec);
				if (!goodDates) {
					ActionMessages errors = new ActionMessages();
					String startOrEnd;
					String dateType;
					if (this.isBeginDateFailed()) {
						ActionMessage error;
						startOrEnd = "start";
						if (filterBy == DisplaySpecServices.ORDERDATEFILTER) {
							dateType = "Order Date";
						} else {
							dateType = "Republication Date";
						}
						error = new ActionMessage("oh.error.invalid.date",
								startOrEnd, dateType);
						errors.add("begin date parse", error);
					}

					if (this.isEndDateFailed()) {
						ActionMessage error;
						startOrEnd = "end";
						if (filterBy == DisplaySpecServices.ORDERDATEFILTER) {
							dateType = "Order Date";
						} else {
							dateType = "Republication Date";
						}
						error = new ActionMessage("oh.error.invalid.date",
								startOrEnd, dateType);
						errors.add("end date parse", error);
					}
					super.setErrors(errors);
				}
			}
			this.setSearchLabel("Date range: '" + super.getBeginDate() + ":"
					+ super.getEndDate() + "'");
			break;

		case DisplaySpecServices.YOURLINEITEMREFERENCEFILTER: // 7 = Your Ref.
			String txt = this.getLabel("Your Line Item Reference", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.CONFNUMFILTER: // 1 Confirmation Number.
			txt = this.getLabel("Confirmation Number", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.PUBLICATIONTITLEFILTER: // 8 = Pub title
			txt = this.getLabel("Publication Title", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.ORDERDETAILIDFILTER:
			txt = this.getLabel("Order Detail ID", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.JOBTICKETNUMBERFILTER:
			txt = this.getLabel("Job Ticket Number", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.TYPEOFUSEFILTER:
			txt = this.getLabel("Type Of Use", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.LICENSENUMBERFILTER:
			txt = this.getLabel("License Number", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.INVOICENUMBERFILTER: // 13 = Invoice Number
			txt = this.getLabel("Invoice Number", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.REPUBLICATIONTITLEFILTER: // 14 = Repub Title
			txt = this.getLabel("Republication Title", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.ARTICLETITLEFILTER:
			txt = this.getLabel("Article Title", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.REPUBLICATIONPUBLISHERFILTER: // 16 = Repub Org
			txt = this.getLabel("Republication Organization", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;

		case DisplaySpecServices.CHAPTERTITLEFILTER: // 28 = Repub Title
			txt = this.getLabel("Chapter Title", text);
			this.setSearchLabel(txt);
			this.spec.setSearch(text);
			break;
		case DisplaySpecServices.PERMISSIONTYPEFILTER: // 10 = Permission Type
			this.spec.setSearch(this.getPermissionType());
			String key = this.getPermissionType();
			String permTypeText = "";
			Long typeEnumKey = Long.parseLong(key);
			// TreeMap<String,String> permTypeTextMap =
			// WebUtils.getODPermTypeOptions();
			// Collection permTypeTextMap = WebUtils.getODPermTypeOptions();
			for (TouCategoryTypeEnum touCatEnum : TouCategoryTypeEnum.values()) {
				if (touCatEnum.getCategoryId() == typeEnumKey) {
					permTypeText = touCatEnum.getDesc();
				}
			}

			txt = this.getLabel("Permission Type", permTypeText);

			this.setSearchLabel(txt);
			break;
		case DisplaySpecServices.PERMISSIONSTATUSFILTER: // 11 = Permission
			// Status
			String status = this.getPermissionStatus();
			if (status != null) {
				try {
					Integer statusCode = new Integer(status);
					int code = statusCode.intValue();
					switch (code) {
					case 0: // GRANT
						status = OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_GRANT;
						break;
					case 1: // SPECIAL ORDER
						status = OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_CHECKING_AVAILABILITY;
						break;
					case 3: // CONTACT RIGHTSHOLDER
						status = OrderMgmtSearchSortConstants.SEARCH_PERMISSION_VALUE_CONTACT_DIRECTLY;
						break;
					case 2: // DENY
						status = OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_DENY;
						break;
					case 4: // CANCELED
						status = OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_CANCELED;
						break;
					}

					this.spec.setSearch(status);

					TreeMap<Integer, String> permStatusMap = WebUtils
							.getODPermStatusMap();
					Integer iKey = new Integer(this.getPermissionStatus());
					String permStatusText = permStatusMap.get(iKey);
					txt = this.getLabel("Permission Status", permStatusText);

					this.setSearchLabel(txt);
				} catch (NumberFormatException nfe) {
					// TODO: Log this
				}
			}
			break;
		case DisplaySpecServices.BILLINGSTATUSFILTER: // 12 = Billing Status
			this.spec.setSearch(this.getBillingStatus());
			TreeMap<String, String> billingStatusMap = WebUtils
					.getODBillingStatusMap();
			key = this.getBillingStatus();
			String billingStatusText = billingStatusMap.get(key);
			txt = this.getLabel("Billing Status", billingStatusText);

			this.setSearchLabel(txt);
			break;
		case DisplaySpecServices.SPECIALORDERFILTER: // 29 = Special Order Update
			this.spec.setSearch(this.getSpecialOrderUpdate());
			TreeMap<String, String> specialOrderUpdateMap = WebUtils
					.getODSpecialOrderUpdateMap();					
			key = this.getSpecialOrderUpdate();
			String specialOrderUpdateText = specialOrderUpdateMap.get(key);
			txt = this.getLabel("Special Order Update", specialOrderUpdateText);

			this.setSearchLabel(txt);
			break;
		}

		this.spec.setResultsPerPage(OrderHistoryActionForm.RESULTS_PER_PAGE);
		DisplaySpecServices.setDisplayPage(this.spec, this.currentPage);
	}

	/*
	 * Helper to set the default sort setting based on the Search selection We
	 * came to this action because the user clicked "Go" to initiate a sort so
	 * the sort settings must be over-ridden with default sort values
	 */
	private int getSortValue() {
		int sortBy = DisplaySpecServices.ORDERDATESORT; // default sort option -
		// ORDERDATE
		if (this.searchNotSort == false) {
			try {
				sortBy = Integer.parseInt(this.sortOption);
			} catch (NumberFormatException nfe) {
				sortBy = DisplaySpecServices.ORDERDATESORT; // default sort
				// option
			}
		} else {
			String searchOption = this.getSearchOption();

			try {
				int searchOptValue = Integer.parseInt(searchOption);
				switch (searchOptValue) {
				case DisplaySpecServices.ORDERDATEFILTER:
					sortBy = DisplaySpecServices.ORDERDATESORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.REPUBLICATIONDATEFILTER:
					sortBy = DisplaySpecServices.REPUBLICATIONDATESORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.PUBLICATIONTITLEFILTER:
					sortBy = DisplaySpecServices.PUBLICATIONTITLESORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.ORDERDETAILIDFILTER:
					sortBy = DisplaySpecServices.ORDERDETAILIDSORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.PERMISSIONTYPEFILTER:
					sortBy = DisplaySpecServices.PERMISSIONTYPESORT; // 2ndary
					// sort
					// Order
					// Date
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.PERMISSIONSTATUSFILTER:
					sortBy = DisplaySpecServices.PERMISSIONSTATUSSORT; // 2ndary
					// sort
					// Order
					// Date
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.BILLINGSTATUSFILTER:
					sortBy = DisplaySpecServices.ORDERDATESORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.YOURREFERENCEFILTER:
					sortBy = DisplaySpecServices.YOURREFERENCESORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.INVOICENUMBERFILTER:
					sortBy = DisplaySpecServices.INVOICENUMBERSORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.REPUBLICATIONTITLEFILTER:
					sortBy = DisplaySpecServices.REPUBLICATIONTITLESORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.REPUBLICATIONPUBLISHERFILTER:
					sortBy = DisplaySpecServices.REPUBLICATIONPUBLISHERSORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				case DisplaySpecServices.LASTUPDATEDATESORT:
					sortBy = DisplaySpecServices.LASTUPDATEDATESORT;
					this.sortOption = String.valueOf(sortBy);
					break;
				default:
					sortBy = DisplaySpecServices.ORDERDATESORT; // default
					this.sortOption = String.valueOf(sortBy);
					break;
				}
			} catch (NumberFormatException nfe) {
				sortBy = DisplaySpecServices.ORDERDATESORT; // default
				this.sortOption = String.valueOf(sortBy);
			}
		}
		return sortBy;
	}

	/*
	 * Construct part of the empty search result string to present to the user.
	 * If the user made a selection of "Choose One" remove it from the result
	 * String
	 */
	private String getLabel(String label, String text) {
		StringBuffer buffer = new StringBuffer(label);
		if (text != null && !"".equals(text)) {
			if (!"Choose One".equals(text)) {
				buffer.append(": '" + text + "'");
			}
		}

		return buffer.toString();
	}

	public Collection<DropDownElement> getSearchOptionList() {
		return this.searchOptionList;
	}

	public void setSearchOptionList(Collection<DropDownElement> options) {
		this.searchOptionList = options;
	}

	public Collection<DropDownElement> getSortOptionList() {
		return this.sortOptionList;
	}

	public void setSortOptionList(Collection<DropDownElement> options) {
		this.sortOptionList = options;
	}

	public Collection<DropDownElement> getPermissionTypeList() {
		return this.permissionTypeList;
	}

	public void setPermissionTypeList(Collection<DropDownElement> options) {
		this.permissionTypeList = options;
	}

	public Collection<DropDownElement> getPermissionStatusList() {
		return this.permissionStatusList;
	}

	public void setPermissionStatusList(Collection<DropDownElement> type) {
		this.permissionStatusList = type;
	}

	public Collection<DropDownElement> getBillingStatusList() {
		return this.billingStatusList;
	}

	public void setBillingStatusList(Collection<DropDownElement> options) {
		this.billingStatusList = options;
	}
	
	public Collection<DropDownElement> getspecialOrderUpdateList() {
		return this.specialOrderUpdateList;
	}

	public void setSpecialOrderUpdateList(Collection<DropDownElement> options) {
		this.specialOrderUpdateList = options;
	}

	/**
	 * Set the License List object in this form and as a side affect also set
	 * the total pages based on the list.
	 * 
	 * @param licenses
	 *            for this form to be displayed.
	 */
	public void setLicenseList(OrderLicenses licenses) {
		this.orderLicenseList = licenses;
		// Save totalPages for access later by calling class
		int count = licenses.getTotalRowCount();
		this.displayItems = licenses.getDisplayRowCount();

		int resultsPerPage = spec.getResultsPerPage();
		this.totalPages = count / resultsPerPage;
		int mod = count % resultsPerPage;
		if (mod > 0) {
			this.totalPages += 1;
		}
		this.setTotalPages(this.totalPages);
	}

	public OrderLicenses getLicenseList() {
		return this.orderLicenseList;
	}

	public Collection<OrderLicense> getDisplayLicenses() {
		return this.orderLicenseList.getDisplayLicenseList();
	}

	public String getId() {
		String rtnValue = this.id;
		if (rtnValue == null) {
			rtnValue = "";
		}
		return rtnValue;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConfirmationNumber() {
		String rtnValue = this.confirmationNumber;
		if (rtnValue == null) {
			rtnValue = "";
		}
		return rtnValue;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getSearchOption() {
		return this.searchOption;
	}

	public void setSearchOption(String opt) {
		this.searchOption = opt;
	}

	public int getSearchSwitch() {
		int searchVal = -1;
		if (this.searchOption != null) {
			try {
				searchVal = Integer.parseInt(this.searchOption);
			} catch (NumberFormatException nfe) {
				searchVal = -1;
			}
		}

		return searchVal;
	}

	public String getSortOption() {
		return this.sortOption;
	}

	public void setSortOption(String opt) {
		this.sortOption = opt;
	}

	public int getSortSwitch() {
		int sortVal = -1;
		if (this.sortOption != null) {
			try {
				sortVal = Integer.parseInt(this.sortOption);
			} catch (NumberFormatException nfe) {
				sortVal = -1;
			}
		}

		return sortVal;
	}

	public void setPermissionType(String type) {
		this.permissionType = type;
	}

	public String getPermissionType() {
		return this.permissionType;
	}

	public String getPermissionStatus() {
		return this.permissionStatus;
	}

	public void setPermissionStatus(String type) {
		this.permissionStatus = type;
	}

	public String getBillingStatus() {
		return this.billingStatus;
	}

	public void setBillingStatus(String status) {
		this.billingStatus = status;
	}
	
	public String getSpecialOrderUpdate() {
		return this.specialOrderUpdate;
	}

	public void setSpecialOrderUpdate(String update) {
		this.specialOrderUpdate = update;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public void setDisplaySpec(DisplaySpec spec) {
		this.spec = spec;
	}

	public DisplaySpec getDisplaySpec() {
		this.initDisplaySpec();
		return spec;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		if (currentPage == 1) {
			startItem = 1;
			if (displayItems == 0) {
				startItem = 0;
			}
			endItem = OrderHistoryActionForm.RESULTS_PER_PAGE;
		} else {
			// Current page is 2 or greater
			startItem = (currentPage - 1)
					* OrderHistoryActionForm.RESULTS_PER_PAGE + 1;
			endItem = startItem + OrderHistoryActionForm.RESULTS_PER_PAGE - 1;
		}

		if (endItem > displayItems) {
			endItem = displayItems;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	/*
	 * Helper setter to decode the current page from a query parm (cp)
	 */
	public void setCp(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * Needed for utililty scroller tag
	 * 
	 * @return the number of displayable items per page as an int
	 */
	public int getResultsPerPage() {
		return OrderHistoryActionForm.RESULTS_PER_PAGE;
	}

	public void setPageAction(String pageAction) {
		this.pageAction = pageAction;
	}

	public String getPageAction() {
		return pageAction;
	}

	/**
	 * Get the item range String for display in the Results header. Calculates
	 * the range based on the current page and the results per page.
	 * 
	 * @return a String of the form 'a-z' where a and z are numbers separated by
	 *         24. If the current page is 1 the returned String is "1-25".
	 */
	public String getItemRange() {
		int start = (currentPage - 1) * OrderHistoryActionForm.RESULTS_PER_PAGE
				+ 1;
		String startStr = String.valueOf(start);
		int end = start + OrderHistoryActionForm.RESULTS_PER_PAGE - 1;
		if (end > this.totalItems) {
			end = this.totalItems;
		}
		String endStr = String.valueOf(end);
		return startStr + "-" + endStr;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setDisplayItems(int displayItems) {
		this.displayItems = displayItems;
	}

	public int getDisplayItems() {
		return displayItems;
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

	public void setPg(int scrollPage) {
		this.scrollPage = scrollPage;
		this.currentPage = this.scrollPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public Collection<DropDownElement> get_permissionStatusList() {
		return permissionStatusList;
	}

	public String getLastDir() {
		return direction;
	}

	public void setIsAdjustmentUser(boolean isAdjustmentUser) {
		_isAdjustmentUser = isAdjustmentUser;
	}

	public boolean getIsAdjustmentUser() {
		return _isAdjustmentUser;
	}

	public void setSearchPage(boolean searchPage) {
		this.searchPage = searchPage;
	}

	public boolean isSearchPage() {
		return searchPage;
	}

	public void setSearchText(String searchText) {
		if (searchText != null) {
			searchText = searchText.trim();
		}

		this.searchText = searchText;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchLabel(String searchLabel) {
		this.searchLabel = searchLabel;
	}

	public String getSearchLabel() {
		return searchLabel;
	}

	public void setCartAcademic(boolean cartAcademic) {
		this.cartAcademic = cartAcademic;
	}

	public boolean isCartAcademic() {
		return cartAcademic;
	}

	public void setCartEmpty(boolean cartEmpty) {
		this.cartEmpty = cartEmpty;
	}

	public boolean isCartEmpty() {
		return cartEmpty;
	}

	public String getPopupTitle() {
		return popupTitle;
	}

	public String getSearchEmptyString() {
		String searchOpt = this.searchOption;
		int numOfItems = this.totalItems;

		StringBuffer buffer = new StringBuffer("Your search ");

		if (numOfItems == 0) {
			if (searchOpt != null) {
				buffer.append("for ");

				try {
					int searchVal = Integer.parseInt(searchOpt);
					switch (searchVal) {
					case DisplaySpecServices.ORDERDATEFILTER:
						buffer = this.getEmptyDateString("Order Date ", buffer);
						break;
					case DisplaySpecServices.REPUBLICATIONDATEFILTER:
						buffer = this.getEmptyDateString("Republication Date ",
								buffer);
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

	private StringBuffer getEmptyDateString(String dateType, StringBuffer buffer) {
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

	public void setSearchNotSort(boolean searchNotSort) {
		this.searchNotSort = searchNotSort;
	}

	public boolean isSearchNotSort() {
		return searchNotSort;
	}

	public LdapUser getUser() {
		return user;
	}

	public void setUser(LdapUser user) {
		this.user = user;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPromCode() {
		return promCode;
	}

	public void setPromCode(String promCode) {
		this.promCode = promCode;
	}

	public OrderBundle getOrderBundle() {
		return orderBundle;
	}

	public void setOrderBundle(OrderBundle orderBundle) {
		this.orderBundle = orderBundle;
	}

	public void setIsAlwaysInvoice(boolean _isAlwaysInvoice) {
		this._isAlwaysInvoice = _isAlwaysInvoice;
	}

	public boolean getIsAlwaysInvoice() {
		return _isAlwaysInvoice;
	}
}
