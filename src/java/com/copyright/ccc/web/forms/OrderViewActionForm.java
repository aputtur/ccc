package com.copyright.ccc.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.web.util.DropDownElement;
import com.copyright.data.order.OrderMgmtSearchSortConstants;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.Organization;
import com.copyright.workbench.logging.LoggerHelper;

/**
 * @author vrumao
 * 
 */
public class OrderViewActionForm extends OrderHistoryValidatorForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger sLogger = LoggerHelper.getLogger();
	
	public static final String CREDIT_CARD = "Credit Card ending in ";
	public static final String INVOICE = "Invoice";
	public static final String MAIN = "main"; // main tab page target
	public static final String DETAIL = "detail"; // detail tab page target

	private String id; // The order id aka purInst aka confirmation number
	private String lastId = null; // The order id aka purInst aka confirmation number
	private String licId; // order detail ID - for cancel order
	private Date orderDate;
	private String custName;
	private long acctNumber;
	private String custCompany;
	private String custEmail;
	private String custPhone;
	private String payMethod;
	private String poNumber;
	private String promoCode;

	private int totalItems;
	private OrderBundle orderBundle= null;
	private List<OrderLicense> orderDetails; // Contains OrderLicense objects

	private String billToPerson;
	private String company;
	private String street1;
	private String street2;
	private String street3;
	private String street4;
	private String city;
	private String state;
	private String zipcode;

	private Collection<DropDownElement> searchOptionList;
	private String searchOption;
	private String searchText;
	private Collection<DropDownElement> permissionTypeList;
	private String permissionType;
	private Collection<DropDownElement> permissionStatusList;
	private String permissionStatus;
	private String billingStatus;
	private Collection<DropDownElement> billingStatusList;

	private Collection<DropDownElement> sortOptionList;
	private String sortOption;
	private String direction; // User sort direction

	private List<String> cascadeErrors; // Holds error messages from edit
	private List<String> cascadeClosedErrors; // Holds "closed" orders from cascade
	private List<String> cascadeCancelErrors; // Holds "Invoiced" items from cancelling
	private List<String> cascadeEditErrors; // Holds "Invoiced" items from Edit
	private String statusColor; // indicates green, orange, or red for status
								// icon
	private boolean academic; // academic purchase attribute
	private boolean cartAcademic; // academic cart attribute
	private boolean closed; // from OrderPurchase object
	private boolean cancelable; // from OrderPurchase object
	private boolean cartEmpty; // If true user is warned by JS that copy order
								// is disbled.
	private boolean hasSpecialOrder; // from OrderPurchase object
	private boolean _isAlwaysInvoice = false; // If true Accept Special Order Invoice

	private int search = 0; // page control. value = 1 when the user clicks "go"
							// (search)
	private int specIndex; // The order detail List index where Special Orders
							// start

	private int acadSpecIndex; // The order detail List index where Special
								// Orders start
	private int singRegIndex; // The order detail List index where Special
								// Orders start
	private int singSpecIndex; // The order detail List index where Special
								// Orders start
	private int acadRegIndex;
	private int numberOfAcademicOrders=0;
	private int numberOfNonAcademicOrders=0;
	private int numberOfOrderItems=0;
	
	private String courseTotal; //Total Price of all the courses
	
	private String singleItemTotal; // Total Price of all the single items

	private String orderTotal; // Total Price of all the single items

	private boolean showHeaderSection=false;
	private boolean hasAcademicItems=false;
	private boolean hasNonAcademicItems=false;
	private boolean hasAcademicSpecialOrders=false;
	private boolean hasNonAcademicSpecialOrders=false;
	private boolean hasNonReprintSpecialOrders=false;
	private boolean hasReprintOrders=false;
	private boolean containsTBDAcademicOrder=false;//contains is true only when order has also priced item
	private boolean containsTBDNonAcademicOrder=false;//contains is true only when order has also priced item
	private boolean containsTBDOrder=false;
	
	 
	
	public String getCourseTotal() {
		return courseTotal;
	}

	public void setCourseTotal(String courseTotal) {
		this.courseTotal = courseTotal;
	}

	public String getSingleItemTotal() {
		return singleItemTotal;
	}

	public void setSingleItemTotal(String singleItemTotal) {
		this.singleItemTotal = singleItemTotal;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}
	
	public int getAcadRegIndex() {
		return acadRegIndex;
	}

	public void setAcadRegIndex(int acadRegIndex) {
		this.acadRegIndex = acadRegIndex;
	}

	private int singleIndex = -1;

	public int getSingleIndex() {
		return singleIndex;
	}

	public void setSingleIndex(int singleIndex) {
		this.singleIndex = singleIndex;
	}

	private int totalSize;

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	private final static String popupTitle = "Rightsholder Terms";
	private String citationTitle = "What's citation information?";
	private String citationText = "We suggest that you print and use the provided citation information.  Sometimes referred to as the \"attribution\" or \"credit line\", these guidelines can be used to ensure that you properly give credit to the rightsholder of the content you are using.";

	private String returnPage; // Designates the main or detail tab for return
								// link
	private String currentPage; // The current scroll page on the main or detail
								// tab
	private String searchFlag; // Deisgnates if the main page is a search page
								// or a default page

	public OrderViewActionForm() {
		this.direction = OrderHistoryActionForm.DESCENDING;
		this.currentPage = "1";
	}

	/**
	 * Method called to populate this form's internal DisplaySpec object from
	 * all of the form's internal attributes. Called by struts action method
	 * after the struts controller has populated the form.
	 */
	private DisplaySpec initDisplaySpec() {
		DisplaySpec spec = UserContextService.getPurchaseDisplaySpec();

		int sortBy = DisplaySpecServices.ORDERDATESORT; // default sort option
		if (this.sortOption != null && !"".equals(this.sortOption)) {
			try {
				sortBy = Integer.parseInt(this.sortOption);
			} catch (NumberFormatException nfe) {
				sortBy = DisplaySpecServices.ORDERDATESORT; // default sort
															// option
			}
		}
		spec.setSortBy(sortBy);

		int filterBy = DisplaySpecServices.CONFNUMFILTER; // search option

		spec.setSearchBy(filterBy);

		if (this.id != null) {
			spec.setSearch(this.id);
			try {
				long purchId = Integer.parseInt(this.id);
				spec.setPurchaseId(purchId);
			} catch (NumberFormatException nfe) {
				// DisplaySpec doesn't like null search attribute
				spec.setSearch("");
			}
		}

		return spec;
	}

	public DisplaySpec getLicenseSpec(DisplaySpec spec) {
//		DisplaySpec spec = UserContextService.getLicenseDisplaySpec();
		// 2000 per Keith to avoid truncation on purchase detail page
		spec.setResultsPerPage(2000);
		// Must force a license read in case the user has added a new license
		// or it will not show up in the view detail page.
		spec.setForceReRead(true);

		int sortDir = DisplaySpec.DESCENDING; // default sort option
		String dir = this.getDirection();
		if ("ascending".equals(dir)) {
			sortDir = DisplaySpec.ASCENDING;
		}
		spec.setSortOrder(sortDir);

		int sortBy = DisplaySpecServices.ORDERDATESORT; // default sort option
		if (this.sortOption != null && !"".equals(this.sortOption)) {
			try {
				sortBy = Integer.parseInt(this.sortOption);
			} catch (NumberFormatException nfe) {
				sortBy = DisplaySpecServices.ORDERDATESORT; // default sort
															// option
			}
		}
		spec.setSortBy(sortBy);

		int filterBy = DisplaySpecServices.CONFNUMFILTER; // search option
		if (this.searchOption != null && !"".equals(this.searchOption)) {
			try {
				filterBy = Integer.parseInt(this.searchOption);
			} catch (NumberFormatException nfe) {
				filterBy = DisplaySpecServices.CONFNUMFILTER; // default search
																// option
			}
		}

		spec.setSearchBy(filterBy);

		// Order View always passes in ALL states to Order Management
		spec.setOrderStateFilter(DisplaySpecServices.ALLSTATES);

		if (this.id != null) {
			try {
				long purchId = Integer.parseInt(this.id);
				spec.setPurchaseId(purchId);
			} catch (NumberFormatException nfe) {
				// DisplaySpec doesn't like null search attribute
				spec.setSearch("");
			}
		} else {
			spec.setSearch(""); // DisplaySpec doesn't like null search
								// attribute
		}

		switch (filterBy) {
		case DisplaySpecServices.ORDERDATEFILTER: // 2 = Order Date
		case DisplaySpecServices.REPUBLICATIONDATEFILTER: // 15 = Repub Date
			// Convert the beginDate and endDate to Date objects parsing the
			// input Strings
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			try {
				Date dateBegin = format.parse(super.getBeginDate());
				Date dateEnd = format.parse(super.getEndDate());
				boolean goodDates = super.validateDates(spec);
				if (!goodDates) {
					ActionMessages errors = new ActionMessages();
					if (this.isBeginDateFailed()) {
						ActionMessage error = new ActionMessage(
								"oh.errors.date.parse", "Start Date");
						errors.add("begin date parse", error);
					}

					if (this.isEndDateFailed()) {
						ActionMessage error = new ActionMessage(
								"oh.errors.date.parse", "End Date");
						errors.add("end date parse", error);
					}
					super.setErrors(errors);
				}

				// Set search date range as search objects
				spec.setSearchFromDate(dateBegin);
				spec.setSearchToDate(dateEnd);
			} catch (ParseException pex) {
				// Show an error that the Dates do not represent valid Dates
				this.searchText = "";
			}
			break;
		case DisplaySpecServices.YOURREFERENCEFILTER: // 7 = Your Ref.
		case DisplaySpecServices.PUBLICATIONTITLEFILTER: // 8 = Pub title
		case DisplaySpecServices.ORDERDETAILIDFILTER: // 9 = Order Detail Id
		case DisplaySpecServices.INVOICENUMBERFILTER: // 13 = Invoice Number
		case DisplaySpecServices.REPUBLICATIONTITLEFILTER: // 14 = Repub Title
		case DisplaySpecServices.REPUBLICATIONPUBLISHERFILTER: // 16 = Repub Org
		case DisplaySpecServices.ARTICLETITLEFILTER:
		case DisplaySpecServices.CHAPTERTITLEFILTER: 
		case DisplaySpecServices.YOURLINEITEMREFERENCEFILTER:

			// Make sure the spec search value is set if the user passed in a
			// value
			spec.setSearch(this.getSearchText());
			break;
		case DisplaySpecServices.PERMISSIONTYPEFILTER: // 10 = Permission Type
		case DisplaySpecServices.TYPEOFUSEFILTER: 
			spec.setSearch(this.getPermissionType());
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
					case 3: // CONTACT RIGHTSHOLDER - TODO: Is this ok or does
							// Tom F have a different constant?
						status = OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_CHECKING_AVAILABILITY;
						break;
					case 2: // DENY
						status = OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_DENY;
						break;
					case 4: // CANCELED
						status = OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_CANCELED;
						break;
					}

					spec.setSearch(status);
				} catch (NumberFormatException nfe) {
					// TODO: Log this
				}
			}
			break;
		case DisplaySpecServices.BILLINGSTATUSFILTER: // 12 = Billing Status
			spec.setSearch(this.billingStatus);
			break;
		case DisplaySpecServices.LICENSENUMBERFILTER: // 26 = Billing Status
			spec.setSearch(this.searchText);
			break;
		case DisplaySpecServices.JOBTICKETNUMBERFILTER: // 25 = Billing Status
			spec.setSearch(this.searchText);
			break;
		default:
			spec.setSearch(this.getId());
			break;
		}

		return spec;
	}

	public int getAcadSpecIndex() {
		return acadSpecIndex;
	}

	public void setAcadSpecIndex(int acadSpecIndex) {
		this.acadSpecIndex = acadSpecIndex;
	}

	public int getSingRegIndex() {
		return singRegIndex;
	}

	public void setSingRegIndex(int singRegIndex) {
		this.singRegIndex = singRegIndex;
	}

	public int getSingSpecIndex() {
		return singSpecIndex;
	}
	


	public void setSingSpecIndex(int singSpecIndex) {
		this.singSpecIndex = singSpecIndex;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setLastId(String id) {
		this.lastId = id;
	}

	public String getLastId() {
		return this.lastId;
	}

	
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public String getOrderDateStr() {
		String dateStr = "";
		if (this.orderDate != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			dateStr = format.format(this.orderDate);
		}

		return dateStr;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustCompany(String custCompany) {
		this.custCompany = custCompany;
	}

	public String getCustCompany() {
		return custCompany;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}

	public String getCustPhone() {
		return custPhone;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public boolean isCCOrder() {
		if (OrderViewActionForm.INVOICE.equals(this.payMethod)) {
			return false;
		} else {
			return true;
		}
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getPromoCode() {
		return promoCode;
	}

	
	public void setOrderDetails(List<OrderLicense> orderDetails) {
		this.orderDetails = this.sortDetails(orderDetails);
		if (orderDetails != null) {
			this.setTotalItems(orderDetails.size());
		}
	}

	public List<OrderLicense> getOrderDetails() {
		return orderDetails;
	}

	public void setSearchOptionList(Collection<DropDownElement> searchOptionList) {
		this.searchOptionList = searchOptionList;
	}

	public Collection<DropDownElement> getSearchOptionList() {
		return searchOptionList;
	}

	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}

	public String getSearchOption() {
		return searchOption;
	}

	/*
	 * The search option case as an int.
	 * 
	 * @returns the search option cast as an int or -1 if the value cannot be
	 * cast to an integer.
	 */
	public int getSearchSwitch() {
		int rtnValue = -1;
		try {
			rtnValue = Integer.parseInt(this.searchOption);
		} catch (Exception exc) {
			rtnValue = -1;
		}

		return rtnValue;
	}

	public void setSortOption(String sortOption) {
		this.sortOption = sortOption;
	}

	public String getSortOption() {
		return sortOption;
	}

	public void setSortOptionList(Collection<DropDownElement> sortOptionList) {
		this.sortOptionList = sortOptionList;
	}

	public Collection<DropDownElement> getSortOptionList() {
		return sortOptionList;
	}

	public void setStatusColor(String statusColor) {
		this.statusColor = statusColor;
	}

	public String getStatusColor() {
		return statusColor;
	}

	public void setAcademic(boolean academic) {
		this.academic = academic;
	}

	public boolean isAcademic() {
		return academic;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public void setLicId(String id) {
		this.licId = id;
	}

	public String getLicId() {
		return this.licId;
	}

	public DisplaySpec getDisplaySpec() {
		return this.initDisplaySpec();
	}

	/**
	 * Returns a boolean true if any of the course info data is not empty or
	 * null. Course information includes school name, start of term, course
	 * name, course number, number of students, instructor, your reference,
	 * accounting reference, and the order by person.
	 * 
	 * @return true if there is any course information. If all course
	 *         information is null or empty return false.
	 */
	public boolean isCourseInfo() {
		if(getOrderBundle()==null)
			return false;
		
		if (getOrderBundle().getOrganization() != null && !"".equals(getOrderBundle().getOrganization())) {
			return true;
		}
		if (getOrderBundle().getStartOfTerm() != null) {
			return true;
		}
		if (getOrderBundle().getCourseName() != null && !"".equals(getOrderBundle().getCourseName() )) {
			return true;
		}
		if (getOrderBundle().getCourseNumber()!= null && !"".equals(getOrderBundle().getCourseNumber())) {
			return true;
		}
		if (getOrderBundle().getNumberOfStudents() > 0L) {
			return true;
		}
		if (getOrderBundle().getInstructor() != null && !"".equals(getOrderBundle().getInstructor())) {
			return true;
		}
		if (getOrderBundle().getYourReference() != null && !"".equals(getOrderBundle().getYourReference())) {
			return true;
		}
		if (getOrderBundle().getAccountingReference()!= null && !"".equals(getOrderBundle().getAccountingReference())) {
			return true;
		}
		if (getOrderBundle().getOrderEnteredBy()!= null && !"".equals(getOrderBundle().getOrderEnteredBy())) {
			return true;
		}

		return false;
	}

	public void setSearchText(String sText) {
		if (sText != null) {
			sText = sText.trim(); // trim for detail ID integers
		}
		this.searchText = sText;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setBillingStatus(String billingStatus) {
		this.billingStatus = billingStatus;
	}

	public String getBillingStatus() {
		return billingStatus;
	}

	public void setPermissionTypeList(Collection<DropDownElement> permissionTypeList) {
		this.permissionTypeList = permissionTypeList;
	}

	public Collection<DropDownElement> getPermissionTypeList() {
		return permissionTypeList;
	}

	public void setPermissionStatusList(Collection<DropDownElement> permissionStatusList) {
		this.permissionStatusList = permissionStatusList;
	}

	public Collection<DropDownElement> getPermissionStatusList() {
		return permissionStatusList;
	}

	public void setPermissionStatus(String permissionStatus) {
		this.permissionStatus = permissionStatus;
	}

	public String getPermissionStatus() {
		return permissionStatus;
	}

	public void setBillingStatusList(Collection<DropDownElement> billingStatusList) {
		this.billingStatusList = billingStatusList;
	}

	public Collection<DropDownElement> getBillingStatusList() {
		return billingStatusList;
	}

	public void setSearch(int search) {
		this.search = search;
	}

	public int getSearch() {
		return search;
	}

	public void setCartEmpty(boolean cartEmpty) {
		this.cartEmpty = cartEmpty;
	}

	public boolean isCartEmpty() {
		return cartEmpty;
	}

	/*
	 * This method takes the complete list of details and sorts it in to a list
	 * with regular orders first and special orders second.
	 * 
	 * @returns a List with all regular orders followed by all special orders
	 */
	private List<OrderLicense> sortDetails(List<OrderLicense> allDetails) {
		// Collection regular = new ArrayList();
		// Collection special = new ArrayList();
		Collection<OrderLicense> academicRegular = new ArrayList<OrderLicense>();
		Collection<OrderLicense> academicSpecial = new ArrayList<OrderLicense>();
		Collection<OrderLicense> singleRegular = new ArrayList<OrderLicense>();
		Collection<OrderLicense> singleSpecial = new ArrayList<OrderLicense>();

		if (allDetails != null && allDetails.size() > 0) {
			Iterator<OrderLicense> itr = allDetails.iterator();
//			double courseTot = 0D;
//			double singTot = 0D;
			while (itr.hasNext()) {
				OrderLicense license = itr.next();
				// if (license.isSpecialOrder()) { special.add(license); }
				// else { regular.add(license); }
              
				if (license.isAcademic()) { // academic and special
					if (license.isSpecialOrder()) {
						academicSpecial.add(license);
						
					}

					else {
						academicRegular.add(license); // academic and regular

					}
//					courseTot += license.getPriceValue();

				} else {
					if (license.isSpecialOrder()) { // Single special
						singleSpecial.add(license);
						long confNum = license.getPurchaseId();
						String billStatus = license.getBillingStatus();
						if ( sLogger.isDebugEnabled() )
						{
							sLogger.debug(billStatus);
						}
					} else {
						singleRegular.add(license); // Single regular
					}
//					singTot += license.getPriceValue();
				}
			}
			
//			courseTotal = NumberFormat.getCurrencyInstance().format(courseTot);
//			singItemTotal = NumberFormat.getCurrencyInstance().format(singTot);

			// int regSize = regular.size();
			// int specSize = special.size();
			int acadRegSize = academicRegular.size();
			int acadSpecSize = academicSpecial.size();
			int singRegSize = singleRegular.size();
			int singSpecSize = singleSpecial.size();
			allDetails.clear();
			/*
			 * if (regSize > 0) { allDetails.addAll(regular); } if (specSize >
			 * 0) { allDetails.addAll(special); this.specIndex = regSize; } else
			 * { this.specIndex = -1; }
			 */
			if (acadRegSize > 0) {
				allDetails.addAll(academicRegular);
				this.acadRegIndex = acadRegSize;            //for the reqular style

			}
			if (acadSpecSize > 0) {
				allDetails.addAll(academicSpecial);
				this.acadSpecIndex = acadRegSize;
			} else {
				this.acadSpecIndex = -1;
			}

			if (singRegSize > 0) {
				allDetails.addAll(singleRegular);
				this.singRegIndex = acadRegSize + acadSpecSize;
				this.singleIndex = singRegIndex;           //Single heading index
			} else {
				this.singRegIndex = -1;
			}

			if (singSpecSize > 0) {
				allDetails.addAll(singleSpecial);
				this.singSpecIndex = acadRegSize + acadSpecSize + singRegSize;
				if (this.singRegIndex < 0) {
					this.singleIndex = singSpecIndex;
				}

			} else {
				this.singSpecIndex = -1;
			}

		}

		return allDetails;
	}

	public void setSpecIndex(int specIndex) {
		this.specIndex = specIndex;
	}

	public int getSpecIndex() {
		return specIndex;
	}

	public int getDetailCount() {
		int count = 0;
		if (this.orderDetails != null) {
			count = this.orderDetails.size();
		}

		return count;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public String getLastDir() {
		return direction;
	}

	public void setCartAcademic(boolean cartAcademic) {
		this.cartAcademic = cartAcademic;
	}

	public boolean isCartAcademic() {
		return cartAcademic;
	}

	public void setIsSpecialOrder(boolean hasSpecialOrder) {
		this.hasSpecialOrder = hasSpecialOrder;
	}

	public boolean isSpecialOrder() {
		return hasSpecialOrder;
	}

	public void setCascadeErrors(List<String> cascadeErrors) {
		this.cascadeErrors = cascadeErrors;
	}

	public List<String> getCascadeErrors() {
		return cascadeErrors;
	}

	public String getPopupTitle() {
		return popupTitle;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setBillingAddress(ARAccount account, Organization organization,
			Location billAddress) {
		if (account != null) {
			// Account doesn't give us a person in the new APIs. We need to
			// extract our Person data.

			String bpName = account.getName().getFirstName() + " "
					+ account.getName().getLastName();

			this.setBillToPerson(bpName);

			String company = "";
			if (organization != null) {
				company = this.scrubString(organization.getOrganizationName());
			}
			this.setCompany(company);
		}

		// Filter Address Strings for active scripting since filter = false on
		// output
		if (billAddress != null) {
			String s = billAddress.getAddress1();
			if (s != null && !"".equals(s)) {
				this.setStreet1(this.scrubString(s)); // remove 'javascript:' or
														// <script> tags
			}
			s = billAddress.getAddress2();
			if (s != null && !"".equals(s)) {
				this.setStreet2(this.scrubString(s)); // remove 'javascript:' or
														// <script> tags
			}
			s = billAddress.getAddress3();
			if (s != null && !"".equals(s)) {
				this.setStreet3(this.scrubString(s)); // remove 'javascript:' or
														// <script> tags
			}
			s = billAddress.getAddress4();
			if (s != null && !"".equals(s)) {
				this.setStreet4(this.scrubString(s)); // remove 'javascript:' or
														// <script> tags
			}
			s = billAddress.getCity();
			if (s != null && !"".equals(s)) {
				this.setCity(this.scrubString(s)); // remove 'javascript:' or
													// <script> tags
			}
			s = billAddress.getState();
			if (s != null && !"".equals(s)) {
				this.setState(this.scrubString(s)); // remove 'javascript:' or
													// <script> tags
			}
			s = billAddress.getPostalCode();
			if (s != null && !"".equals(s)) {
				this.setZipcode(this.scrubString(s)); // remove 'javascript:' or
														// <script> tags
			}
		}
	}

	/**
	 * Return the billing address information line by line separated by HTML
	 * break tags for display in the UI. If an address field is empty do not
	 * display it. The following fields are output if they exist: The Bill To
	 * Person, the organization name, the four street address fields, the city,
	 * state, and zipcode. Note that in order to display properly the filter
	 * attribute on a bean tag must be set to false.
	 * 
	 * @return Billing address with each line separated by an HTML break tag.
	 */
	public String getBillingAddress() {
		String brk = "<br />";
		StringBuffer address = new StringBuffer();

		if (this.billToPerson != null && !"".equals(this.billToPerson)) {
			address.append(this.billToPerson);
			address.append(brk);
		}

		if (this.company != null && !"".equals(this.company)) {
			address.append(this.company);
			address.append(brk);
		}

		if (this.street1 != null && !"".equals(this.street1)) {
			address.append(this.street1);
			address.append(brk);
		}

		if (this.street2 != null && !"".equals(this.street2)) {
			address.append(this.street2);
			address.append(brk);
		}

		if (this.street3 != null && !"".equals(this.street3)) {
			address.append(this.street3);
			address.append(brk);
		}

		if (this.street4 != null && !"".equals(this.street4)) {
			address.append(this.street4);
			address.append(brk);
		}

		if (this.city != null && !"".equals(this.city)) {
			address.append(this.city);
		}

		if (this.state != null && !"".equals(this.state)) {
			if (this.city != null && !"".equals(this.city)) {
				address.append(", ");
			}
			address.append(this.state);
		}

		if (this.zipcode != null && !"".equals(this.zipcode)) {
			address.append(" ");
			address.append(this.zipcode);
			address.append(brk);
		}

		return address.toString();
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet3(String street3) {
		this.street3 = street3;
	}

	public String getStreet3() {
		return street3;
	}

	public void setStreet4(String street4) {
		this.street4 = street4;
	}

	public String getStreet4() {
		return street4;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return company;
	}

	public void setBillToPerson(String billToPerson) {
		this.billToPerson = billToPerson;
	}

	public String getBillToPerson() {
		return billToPerson;
	}

	public void setCitationTitle(String citationTitle) {
		this.citationTitle = citationTitle;
	}

	public String getCitationTitle() {
		return citationTitle;
	}

	public void setCitationText(String citationText) {
		this.citationText = citationText;
	}

	public String getCitationText() {
		return citationText;
	}

	public void setAcctNumber(long acctNumber) {
		this.acctNumber = acctNumber;
	}

	public long getAcctNumber() {
		return acctNumber;
	}

	public void setCascadeClosedErrors(List<String> cascadeClosedErrors) {
		this.cascadeClosedErrors = cascadeClosedErrors;
	}

	public List<String> getCascadeClosedErrors() {
		return cascadeClosedErrors;
	}
	
	public List<String> getCascadeCancelErrors() {
		return cascadeCancelErrors;
	}
	public void setCascadeCancelErrors(List<String> cascadeCancelErrors) {
		this.cascadeCancelErrors = cascadeCancelErrors;
	}
	
	public List<String> getCascadeEditErrors() {
		return cascadeEditErrors;
	}
	public void setCascadeEditErrors(List<String> cascadeEditErrors) {
		this.cascadeEditErrors = cascadeEditErrors;
	}

	public boolean getHasAnyCascadeError() {
		return ((cascadeErrors != null && !cascadeErrors.isEmpty()) || (cascadeClosedErrors != null && !cascadeClosedErrors
				.isEmpty()) || (cascadeCancelErrors != null && !cascadeCancelErrors
						.isEmpty()) || (cascadeEditErrors != null && !cascadeEditErrors
								.isEmpty()));
	}

	/**
	 * Decodes the rp query parameter that designates where the return link
	 * should go. Valid options are "main" and "detail" since the order view
	 * page could have been called from either location.
	 * 
	 * @param returnPage
	 *            is a String value representing the main or detail tab pages.
	 */
	public void setRp(String returnPage) {
		this.returnPage = returnPage;
	}

	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}

	public String getReturnPage() {
		return returnPage;
	}

	/**
	 * Returns the struts action for the "Back to view" link on
	 * orderViewPage.jsp
	 * 
	 * @return "orderHistory.do" if the orderViewPage.jsp was called from the
	 *         main Order History tab otherwise return "orderDetail.do".
	 */
	public String getBackLink() {
		StringBuffer backLink = new StringBuffer();

		String cp = this.currentPage;
		if (OrderViewActionForm.MAIN.equals(this.returnPage)) {
			backLink.append("refreshOrderHistory.do");
			if ("true".equals(this.searchFlag)) {
				backLink.append("?searchPage=true&pg=" + cp);
			} else {
				backLink.append("?pg=" + cp);
			}
		} else if (OrderViewActionForm.DETAIL.equals(this.returnPage)) {
			backLink.append("refreshOrderDetail.do?pg=" + cp);
		} else {
			// Default back to main tab page 1 even if it isn't where we came
			// from
			backLink.append("refreshOrderHistory.do?pg=" + cp);
		}

		return backLink.toString();
	}

	/**
	 * Returns text specific to where the orderViewPage.jsp was called from. If
	 * the page was called from the main order history tab the text is "orders".
	 * If the page was called from the detail order history tab the text is
	 * "order details".
	 * 
	 * @return text specific to where this page was loaded from so the user has
	 *         a cue that the link will return to the main or detail tab.
	 */
	public String getBackLinkText() {
		String text = "orders";
		if (OrderViewActionForm.DETAIL.equals(this.returnPage)) {
			text = "order details";
		}

		return text;
	}

	/**
	 * Inspect a String and remove all occurrances of "javascript:",
	 * "javascript%3a", "<script", "</script", "%3cscript", and "%3c/script".
	 * 
	 * @param field
	 * @return field after removing active scripting elements if necessary
	 */
	private String scrubString(String field) {
		if (field != null && !"".equals(field)) {
			Pattern scriptPattern = Pattern.compile(
					".*(javascript(:|%3a)|(<|%3c)/?script).*",
					Pattern.CASE_INSENSITIVE);
			Matcher scriptMatcher = scriptPattern.matcher(field);
			boolean scriptsPresent = scriptMatcher.matches();

			if (scriptsPresent) {
				String blank = "";
				scriptPattern = Pattern.compile(".*javascript:.*",
						Pattern.CASE_INSENSITIVE);
				scriptMatcher = scriptPattern.matcher(field);
				if (scriptMatcher.matches()) {
					field = field.replaceAll("javascript:", blank);
				}

				scriptPattern = Pattern.compile(".*javascript%3a.*",
						Pattern.CASE_INSENSITIVE);
				scriptMatcher = scriptPattern.matcher(field);
				if (scriptMatcher.matches()) {
					field = field.replaceAll("javascript%3a", blank);
				}

				scriptPattern = Pattern.compile(".*</script.*",
						Pattern.CASE_INSENSITIVE);
				scriptMatcher = scriptPattern.matcher(field);
				if (scriptMatcher.matches()) {
					field = field.replaceAll("</script", blank);
				}

				scriptPattern = Pattern.compile(".*<script.*",
						Pattern.CASE_INSENSITIVE);
				scriptMatcher = scriptPattern.matcher(field);
				if (scriptMatcher.matches()) {
					field = field.replaceAll("<script", blank);
				}

				scriptPattern = Pattern.compile(".*%3c/script.*",
						Pattern.CASE_INSENSITIVE);
				scriptMatcher = scriptPattern.matcher(field);
				if (scriptMatcher.matches()) {
					field = field.replaceAll("%3c/script", blank);
				}

				scriptPattern = Pattern.compile(".*%3cscript.*",
						Pattern.CASE_INSENSITIVE);
				scriptMatcher = scriptPattern.matcher(field);
				if (scriptMatcher.matches()) {
					field = field.replaceAll("%3cscript", blank);
				}
			}
		}
		return field;
	}

	/**
	 * Decoded from query parm on order history tab to determine whether to
	 * return to the View Orders default tab or the View Orders search result
	 * tab
	 */
	public void setSf(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getSf() {
		String rtnValue = "false";
		if (searchFlag != null && !"".equals(searchFlag)) {
			if ("true".equalsIgnoreCase(searchFlag)) {
				rtnValue = "true";
			}
		}
		return rtnValue;
	}

	/**
	 * Decoded from query parm on one of the main tabs to determine what scroll
	 * page to return to the originating tab when the "back to" link is clicked.
	 */
	public void setCp(String cp) {
		this.currentPage = cp;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public Map<String,String> getEditCourseInfoMap() {
		Map<String,String> paramMap = new HashMap<String,String>();

		if (this.id != null && !"".equals(this.id)) {
			paramMap.put("id", this.id);
		}
		if (this.currentPage != null && !"".equals(this.currentPage)) {
			paramMap.put("cp", this.getCurrentPage());
		}
		if (this.returnPage != null && !"".equals(this.returnPage)) {
			paramMap.put("rp", this.getReturnPage());
		}
		if (this.searchFlag != null && !"".equals(this.searchFlag)) {
			paramMap.put("sf", this.searchFlag);
		}

		return paramMap;
	}

	public void setShowHeaderSection(boolean showHeaderSection) {
		this.showHeaderSection = showHeaderSection;
	}

	public boolean getShowHeaderSection() {
		return showHeaderSection;
	}

	public void setHasAcademicItems(boolean hasAcademicItems) {
		this.hasAcademicItems = hasAcademicItems;
	}

	public boolean getHasAcademicItems() {
		return hasAcademicItems;
	}

	public void setHasNonAcademicItems(boolean hasNonAcademicItems) {
		this.hasNonAcademicItems = hasNonAcademicItems;
	}

	public boolean getHasNonAcademicItems() {
		return hasNonAcademicItems;
	}

	public void setHasAcademicSpecialOrders(boolean hasAcademicSpecialOrders) {
		this.hasAcademicSpecialOrders = hasAcademicSpecialOrders;
	}

	public boolean getHasAcademicSpecialOrders() {
		return hasAcademicSpecialOrders;
	}

	public void setHasNonAcademicSpecialOrders(boolean hasNonAcademicSpecialOrders) {
		this.hasNonAcademicSpecialOrders = hasNonAcademicSpecialOrders;
	}

	public boolean getHasNonAcademicSpecialOrders() {
		return hasNonAcademicSpecialOrders;
	}

	public void setHasReprintOrders(boolean hasReprintOrders) {
		this.hasReprintOrders = hasReprintOrders;
	}

	public boolean getHasReprintOrders() {
		return hasReprintOrders;
	}

	public void setNumberOfAcademicOrders(int numberOfAcademicOrders) {
		this.numberOfAcademicOrders = numberOfAcademicOrders;
	}

	public int getNumberOfAcademicOrders() {
		return numberOfAcademicOrders;
	}

	public void setNumberOfNonAcademicOrders(int numberOfNonAcademicOrders) {
		this.numberOfNonAcademicOrders = numberOfNonAcademicOrders;
	}

	public int getNumberOfNonAcademicOrders() {
		return numberOfNonAcademicOrders;
	}

	public void setNumberOfOrderItems(int numberOfOrderItems) {
		this.numberOfOrderItems = numberOfOrderItems;
	}

	public int getNumberOfOrderItems() {
		return numberOfOrderItems;
	}

	public void setHasNonReprintSpecialOrders(boolean hasNonReprintSpecialOrders) {
		this.hasNonReprintSpecialOrders = hasNonReprintSpecialOrders;
	}

	public boolean getHasNonReprintSpecialOrders() {
		return hasNonReprintSpecialOrders;
	}

	public void setContainsTBDNonAcademicOrder(boolean containsTBDNonAcademicOrder) {
		this.containsTBDNonAcademicOrder = containsTBDNonAcademicOrder;
	}

	public boolean getContainsTBDNonAcademicOrder() {
		return containsTBDNonAcademicOrder;
	}

	public void setContainsTBDAcademicOrder(boolean containsTBDAcademicOrder) {
		this.containsTBDAcademicOrder = containsTBDAcademicOrder;
	}

	public boolean getContainsTBDAcademicOrder() {
		return containsTBDAcademicOrder;
	}

	public void setContainsTBDOrder(boolean containsTBDOrder) {
		this.containsTBDOrder = containsTBDOrder;
	}

	public boolean getContainsTBDOrder() {
		return containsTBDOrder;
	}
	
	public void clearForm(){
		
		setNumberOfAcademicOrders(0);
		setNumberOfNonAcademicOrders(0);
		setNumberOfOrderItems(0);
		

		setContainsTBDAcademicOrder(Boolean.FALSE);
		setShowHeaderSection(Boolean.FALSE);
		
		setHasAcademicSpecialOrders(Boolean.FALSE);
		setHasAcademicItems(Boolean.FALSE);
		setHasNonAcademicItems(Boolean.FALSE);
		setHasNonReprintSpecialOrders(Boolean.FALSE);
		setHasReprintOrders(Boolean.FALSE);
		setHasNonAcademicSpecialOrders(Boolean.FALSE);
		setContainsTBDNonAcademicOrder(Boolean.FALSE);
		setContainsTBDAcademicOrder(Boolean.FALSE);
		setContainsTBDOrder(Boolean.FALSE);
		setOrderDetails(new ArrayList<OrderLicense>());
		setAcademic(false);
		setPayMethod(null);
		setIsSpecialOrder(false);
		setCourseTotal("");
		setSingleItemTotal("");
		setOrderTotal("");

		
		
		
	}
	
	public boolean haveCriteria(){
		if(!StringUtils.isEmpty(getSearchOption())){
			Integer searchId=Integer.valueOf(getSearchOption());
			if (searchId == DisplaySpecServices.BILLINGSTATUSFILTER && !this.getBillingStatus().isEmpty()) {
				return true;
			}
			if (searchId == DisplaySpecServices.PERMISSIONSTATUSFILTER) {
				Integer permissionStatusId = null;
				if (!this.getPermissionStatus().isEmpty()) {
					try {permissionStatusId=Integer.valueOf(getPermissionStatus()); } 
						catch (Exception e) { permissionStatusId = new Integer(-1); }	
					if (permissionStatusId.intValue() > -1) {
						return true;
					}
				}
			}
			if (searchId == DisplaySpecServices.PERMISSIONTYPEFILTER) {
				Integer permissionTypeId = null;
				if (!this.getPermissionType().isEmpty()) {
					try {permissionTypeId=Integer.valueOf(getPermissionType()); } 
						catch (Exception e) { permissionTypeId = new Integer(-1); }	
					if (permissionTypeId.intValue() > -1) {
						return true;
					}
				}
			}			
			if (searchId == DisplaySpecServices.ORDERDATEFILTER && (!this.getBeginDate().isEmpty() && !this.getEndDate().isEmpty())) {
				return true;
			}
			if(searchId>0 && StringUtils.isEmpty(getSearchText()) ){
				return false;
			}
		}
		
		return true;
		
	}
	public boolean getIsFilterOn(){
		if(!StringUtils.isEmpty(getSearchOption())){
			Integer searchId=Integer.valueOf(getSearchOption());
			if(searchId>0 && !StringUtils.isEmpty(getSearchText()) ){
				return true;
			}
		}
		
		return false;
		
	}

	public void setOrderBundle(OrderBundle orderBundle) {
		this.orderBundle = orderBundle;
	}

	public OrderBundle getOrderBundle() {
		return orderBundle;
	}
	
	public void setIsAlwaysInvoice(boolean _isAlwaysInvoice) {
		this._isAlwaysInvoice = _isAlwaysInvoice;
	}

	public boolean getIsAlwaysInvoice() {
		return _isAlwaysInvoice;
	}

	/*
	 * public String getEditCourseInfoLink() { String idParm = ""; if (this.id
	 * != null && !"".equals(this.id)) { idParm = this.id; }
	 * 
	 * String cpParm = ""; if (this.currentPage != null &&
	 * !"".equals(this.currentPage)) { cpParm = "&cp=" + this.currentPage; }
	 * String rpParm = ""; if (this.returnPage != null &&
	 * !"".equals(this.returnPage)) { rpParm = "&rp=" + this.returnPage; }
	 * 
	 * String sfParm = ""; if (this.searchFlag != null &&
	 * !"".equals(this.searchFlag)) { sfParm = "&sf=" + this.searchFlag; }
	 * 
	 * return idParm + cpParm + rpParm + sfParm; }
	 */
}
