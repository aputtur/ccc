package com.copyright.ccc.web.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.copyright.base.Constants;
import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.data.order.OrderMgmtSearchSortConstants;
import com.copyright.svc.rightsResolver.api.data.TouCategoryTypeEnum;
import com.copyright.workbench.i18n.Money;

public class WebUtils {
	static Logger _logger = Logger.getLogger(WebUtils.class);

	public final static int SELECT_ONE = -1;

	private static final String DECIMAL_MASK = "0.00";
	
    /**
     * Casts an ActionForm to a subtype.  Should be used in our Struts actions instead
     * of a direct downcast, since the latter will result in an unchecked/unconfirmed
     * cast warning in various static analysis tools.  We would prefer those warnings
     * are only shown in other, potentially more dangerous scenarios.
     * <b>
     * All Actions should be subclasses of CCAction, but since they are not, we have 
     * to provide this method in a helper class.  
     */
	public static <T extends ActionForm> T castForm( Class<T> desired, ActionForm form )
	{
  	    if ( desired.isInstance( form ) )
  	    {
  		    @SuppressWarnings("unchecked")
  		    T typedForm = (T) form; 
  		    return typedForm;
  	    }
  	    else
  	    {
  	    	throw new CCRuntimeException( 
  	    		"Expected an instance of " + desired.getSimpleName() + 
  	    		" but received an instance of " + form.getClass().getSimpleName() );
  	  }
	}
	
	/*
	 * A utility method for building the Order History Search Select options
	 * list suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getOHSearchOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		LinkedHashMap<Integer,String> map = WebUtils.getOHSearchMap();

		Iterator<Map.Entry<Integer,String>> itr = map.entrySet().iterator();

		Integer key;
		String label;
		DropDownElement option;

		while (itr.hasNext()) {
			Map.Entry<Integer, String> mapEntry = itr.next();
			key = mapEntry.getKey();
			label = mapEntry.getValue();
			option = new DropDownElement(key.toString(), label);
			options.add(option);
		}

		return options;
	}

	public static String getOHSearchLabel(String value) {
		String rtnValue = "";
		LinkedHashMap<Integer,String> map = WebUtils.getOHSearchMap();
		try {
			Integer key = Integer.valueOf(value);
			int keyInt = key.intValue();
			if (keyInt == DisplaySpecServices.ORDERDATEFILTER
					|| keyInt == DisplaySpecServices.STARTOFTERMFILTER) {
				DisplaySpec spec = UserContextService.getPurchaseDisplaySpec();

				Date from = spec.getSearchFromDate();
				Date to = spec.getSearchToDate();
				if (from != null && to != null) {
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
					String fromStr = format.format(from);
					String toStr = format.format(to);
					rtnValue = map.get(key) + ": " + fromStr + ":" + toStr;
				}
			} else {
				rtnValue = map.get(key);
			}
		} catch (NumberFormatException nfe) {
			rtnValue = "unknown";
		}

		return rtnValue;
	}

	private static LinkedHashMap<Integer, String> getOHSearchMap() {
		// TreeMap map = new TreeMap(new OHSearchComparator());

		// Got rid of the comparator by changing the collections type to
		// LinkedHashMap
		// which maintains
		// The insertion order
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();

		map.put(Integer.valueOf(DisplaySpecServices.ORDERDATEFILTER),
				"Order Date");
		map.put(Integer.valueOf(DisplaySpecServices.CONFNUMFILTER),
				"Confirmation Number");
		map.put(Integer.valueOf(DisplaySpecServices.SCHOOLFILTER),
				"University/Institution");
		map.put(Integer.valueOf(DisplaySpecServices.STARTOFTERMFILTER),
				"Start of Term");
		map.put(Integer.valueOf(DisplaySpecServices.COURSENAMEFILTER),
				"Course Name");
		map.put(Integer.valueOf(DisplaySpecServices.COURSENUMBERFILTER),
				"Course Number");
		map.put(Integer.valueOf(DisplaySpecServices.INSTRUCTORFILTER),
				"Instructor");
		//map.put(Integer.valueOf(DisplaySpecServices.JOB_TCKT_NUM),
			//	"Job Ticket Number");
		map.put(Integer.valueOf(DisplaySpecServices.LICENSENUMBERFILTER), "License Number");
		map.put(Integer.valueOf(DisplaySpecServices.YOURREFERENCEFILTER),
				"Your Reference");
		map.put(Integer.valueOf(DisplaySpecServices.YOURACCTREFERENCEFILTER),
				"Your Accounting Ref.");
		map.put(Integer.valueOf(DisplaySpecServices.INVOICENUMBERFILTER), "Invoice Number");
		//map.put(Integer.valueOf(DisplaySpecServices.CUST_REF_NUM),
			//	"Order Ref. Number");

		return map;
	}

	/*
	 * A utility method for building the Order History Sort Select options list
	 * suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getOHSortOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option = new DropDownElement(
				DisplaySpecServices.ORDERDATESORT, "Order Date");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.CONFNUMSORT, "Confirmation Number");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.SCHOOLSORT, "University/Institution");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.STARTOFTERMSORT, "Start of Term");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.COURSENAMESORT, "Course Name");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.COURSENUMBERSORT, "Course Number");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.INSTRUCTORSORT, "Instructor");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.YOURREFERENCESORT, "Your Reference");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.YOURACCTREFERENCESORT, "Your Accounting Reference");
		options.add(option);

		return options;
	}

	/*
	 * A utility method for building the Order Detail Search Select options list
	 * suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getODSearchOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();
		// DropDownElement option = new DropDownElement(SELECT_ONE,
		// "Choose One");
		// options.add(option);
		DropDownElement option = new DropDownElement(
				DisplaySpecServices.ORDERDATEFILTER, "Order Date");
		options.add(option);
		//option = new DropDownElement(
		//		DisplaySpecServices.CONFNUMFILTER, "Confirmation Number");
		//options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.PUBLICATIONTITLEFILTER, "Publication Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.ARTICLETITLEFILTER, "Article Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.CHAPTERTITLEFILTER, "Chapter Title");
        options.add(option);
		option = new DropDownElement(DisplaySpecServices.ORDERDETAILIDFILTER, "Order Detail ID");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.PERMISSIONTYPEFILTER, "Permission Type");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.PERMISSIONSTATUSFILTER, "Permission Status");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.BILLINGSTATUSFILTER, "Billing Status");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.SPECIALORDERFILTER, "Special Order Update");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.YOURLINEITEMREFERENCEFILTER, "Your Line Item Reference");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.INVOICENUMBERFILTER, "Invoice Number");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.JOBTICKETNUMBERFILTER, "Job Ticket Number");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.LICENSENUMBERFILTER, "License Number");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONTITLEFILTER, "Republication Title");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONDATEFILTER, "Republication Date");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONPUBLISHERFILTER, "Republication Organization");
		options.add(option);

		return options;
	}

	/*
	 * A utility method for building the Order Detail View page Search Select
	 * options list suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getOVSearchOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();
		DropDownElement option = new DropDownElement(SELECT_ONE, "Choose One");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.NOFILTER, "View All Details");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.ORDERDATEFILTER, "Order Date");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.PUBLICATIONTITLEFILTER, "Publication Title");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.ARTICLETITLEFILTER, "Article Title");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.CHAPTERTITLEFILTER, "Chapter Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.ORDERDETAILIDFILTER, "Order Detail ID");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.PERMISSIONTYPEFILTER, "Permission Type");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.PERMISSIONSTATUSFILTER, "Permission Status");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.BILLINGSTATUSFILTER, "Billing Status");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.YOURLINEITEMREFERENCEFILTER, "Your Line Item Reference");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.INVOICENUMBERFILTER, "Invoice Number");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.JOBTICKETNUMBERFILTER, "Job Ticket Number");
        options.add(option);
		option = new DropDownElement(DisplaySpecServices.LICENSENUMBERFILTER, "License Number");
        options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONTITLEFILTER,
				"Republication Title");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONDATEFILTER,
				"Republication Date");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONPUBLISHERFILTER,
				"Republication Organization");
		options.add(option);

		return options;
	}

	/*
	 * A utility method for building the Order Detail Sort Select options list
	 * suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getODSortOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option = new DropDownElement(
				DisplaySpecServices.PUBLICATIONTITLESORT, "Publication Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.ORDERDATESORT,
				"Order Date");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.ORDERDETAILIDSORT,
				"Order Detail ID");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.PERMISSIONTYPESORT,
				"Permission Type");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.PERMISSIONSTATUSSORT,
				"Permission Status");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.BILLINGSTATUSSORT,
				"Billing Status");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.YOURREFERENCESORT,
				"Your Reference");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.INVOICENUMBERSORT,
				"Invoice Number");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONTITLESORT,
				"Republication Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.REPUBLICATIONDATESORT,
				"Republication Date");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONPUBLISHERSORT,
				"Republication Organization");
		options.add(option);

		return options;
	}

	/*
	 * A utility method for building the Order Detail View Sort Select options
	 * list suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getOVSortOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		// DropDownElement option = new DropDownElement(SELECT_ONE,
		// "Choose One");
		// options.add(option);
		DropDownElement option = new DropDownElement(
				DisplaySpecServices.ORDERDATESORT, "Order Date");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.PUBLICATIONTITLESORT,
				"Publication Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.ORDERDETAILIDFILTER,
				"Order Detail ID");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.PERMISSIONTYPESORT,
				"Permission Type");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.PERMISSIONSTATUSSORT,
				"Permission Status");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.BILLINGSTATUSSORT,
				"Billing Status");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.YOURREFERENCESORT,
				"Your Reference");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.INVOICENUMBERSORT,
				"Invoice Number");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONTITLESORT,
				"Republication Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.REPUBLICATIONDATESORT,
				"Republication Date");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.REPUBLICATIONPUBLISHERSORT,
				"Republication Organization");
		options.add(option);

		return options;
	}

	public static TreeMap<String,String> getODSearchMap() {
		TreeMap<String,String> map = new TreeMap<String,String>();

		map.put(String.valueOf(SELECT_ONE), "Choose One");
		map.put(OrderMgmtSearchSortConstants.SEARCH_PRODUCT_APS,
				"Photocopy for academic coursepacks, classroom handouts...");
		map.put(OrderMgmtSearchSortConstants.SEARCH_PRODUCT_TRS,
				"Photocopy for general business use, library reserves, ILL/document delivery...");
		map.put(OrderMgmtSearchSortConstants.SEARCH_PRODUCT_ECC,
				"Posting e-reserves, course management systems, e-coursepacks...");
		map.put(OrderMgmtSearchSortConstants.SEARCH_PRODUCT_DPS,
				"Use in e-mail, intranet/extranet/Internet postings...");
		map.put(OrderMgmtSearchSortConstants.SEARCH_PRODUCT_RLS,
				"Republish into a book, journal, newsletter...");

		return map;
	}

	/*
	 * A utility method for building the Order Detail Permission Type Select
	 * options list suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getODPermTypeOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option;
        	String key;
		String label;
		for (TouCategoryTypeEnum touCatEnum : TouCategoryTypeEnum.values()){
			if(touCatEnum.getIsTransactional()== true && touCatEnum.getCategoryId() != 8L){
				key = touCatEnum.getCategoryId().toString();
				label = touCatEnum.getDesc();
				option = new DropDownElement(key, label);
				options.add(option);
				
			}
		}
			
		

		return options;
	}

	public static String getODPermStatusLookup(String code) {
		TreeMap<String,String> map = new TreeMap<String,String>();

		map.put(OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_GRANT, "0"); // grant
		map.put(OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_CHECKING_AVAILABILITY, "1"); // special order
		map.put(OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_DENY, "2"); // denied
		// map.put(Integer.valueOf(3), "Contact Rightsholder");
		map.put(OrderMgmtSearchSortConstants.SEARCH_PERMISSON_VALUE_CANCELED, "4"); // canceled

		String rtnValue = map.get(code);

		if (rtnValue == null) {
			rtnValue = "";
		}

		return rtnValue;
	}

	public static TreeMap<Integer,String> getODPermStatusMap() {
		TreeMap<Integer,String> map = new TreeMap<Integer,String>();

		map.put(Integer.valueOf(SELECT_ONE), "Choose One");
		map.put(Integer.valueOf(0), "Granted");
		map.put(Integer.valueOf(1), "Special Order");
		map.put(Integer.valueOf(2), "Denied");
		map.put(Integer.valueOf(3), "Contact Rightsholder");
		map.put(Integer.valueOf(4), "Request has been canceled");

		return map;
	}

	/*
	 * A utility method for building the Order Detail Permission Status Select
	 * options list suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getODPermStatusOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option;
		TreeMap<Integer,String> map = WebUtils.getODPermStatusMap();
		Iterator<Map.Entry<Integer,String>> itr = map.entrySet().iterator();

		Integer key;
		String label;

		while (itr.hasNext()) {
			Map.Entry<Integer, String> mapEntry = itr.next();
			key = mapEntry.getKey();
			label = mapEntry.getValue();
			option = new DropDownElement(key.toString(), label);
			options.add(option);
		}

		return options;
	}

	public static TreeMap<String,String> getODBillingStatusMap() {
		TreeMap<String,String> map = new TreeMap<String,String>();

		map.put("", "Choose One");
		map.put(OrderMgmtSearchSortConstants.SEARCH_BLLING_STATUS_INVOICED,
				"Invoiced");
		map.put(OrderMgmtSearchSortConstants.SEARCH_BLLING_STATUS_CHARGED,
				"Charged to Credit Card");
		map.put(OrderMgmtSearchSortConstants.SEARCH_BLLING_STATUS_CANCELED,
				"Canceled");
		map.put(OrderMgmtSearchSortConstants.SEARCH_BLLING_STATUS_NOT_BILLED,
				"Not Billed");
		map.put("AWAITING_LCN_CONFIRM", 
				"Awaiting Customer Acceptance");
		
		return map;
	}

	/*
	 * A utility method for building the Order Detail Billing Status Select
	 * options list suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getODBillStatusOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option;
		TreeMap<String,String> map = WebUtils.getODBillingStatusMap();
		Iterator<Map.Entry<String,String>> itr = map.entrySet().iterator();

		String key;
		String label;

		while (itr.hasNext()) {
			Map.Entry<String, String> mapEntry = itr.next();
			key = mapEntry.getKey();
			label = mapEntry.getValue();
			option = new DropDownElement(key, label);
			options.add(option);
		}

		return options;
	}
	
	public static TreeMap<String,String> getODSpecialOrderUpdateMap() {
		TreeMap<String,String> map = new TreeMap<String,String>();

		map.put("", "Choose One");
		/* map.put(OrderMgmtSearchSortConstants.SEARCH_BLLING_STATUS_INVOICED,
				"Invoiced");
		map.put(OrderMgmtSearchSortConstants.SEARCH_BLLING_STATUS_CHARGED,
				"Charged to Credit Card");
		map.put(OrderMgmtSearchSortConstants.SEARCH_BLLING_STATUS_CANCELED,
				"Canceled");
		map.put(OrderMgmtSearchSortConstants.SEARCH_BLLING_STATUS_NOT_BILLED,
				"Not Billed"); */
		map.put("AWAITING_LCN_CONFIRM", 
				"Awaiting Customer Acceptance");
		map.put("AWAITING_RH", 
		"Forwarded to Rightsholder");
		map.put("AWAITING_LCN_REPLY", 
		"Requires more customer information");
		
		return map;
	}

	/*
	 * A utility method for building the Order Detail Billing Status Select
	 * options list suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getODSpecialOrderUpdateOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option;
		TreeMap<String,String> map = WebUtils.getODSpecialOrderUpdateMap();
		Iterator<Map.Entry<String,String>> itr = map.entrySet().iterator();

		String key;
		String label;

		while (itr.hasNext()) {
			Map.Entry<String, String> mapEntry = itr.next();
			key = mapEntry.getKey();
			label = mapEntry.getValue();
			option = new DropDownElement(key, label);
			options.add(option);
		}

		return options;
	}

	/***********************************************
	 * Dropdowns for search results pages. mood: In the groove song: She's got
	 * the look - Roxette tv: n/a favorite quote:
	 * "What kind?  African or European?"
	 */

	public static Collection<DropDownElement> getSRSortOptions() {
		ArrayList<DropDownElement> options = new ArrayList<DropDownElement>(3);
		DropDownElement option = null;

		option = new DropDownElement("0", "Relevance");
		options.add(0, option);
		option = new DropDownElement("1", "Title");
		options.add(1, option);
		option = new DropDownElement("2", "Publisher");
		options.add(2, option);

		return options;
	}

	public static Collection<DropDownElement> getArticleSortOptions() {
		ArrayList<DropDownElement> options = new ArrayList<DropDownElement>(3);
		DropDownElement option = null;

		option = new DropDownElement("0", "Relevance");
		options.add(0, option);
		option = new DropDownElement("1", "Title");
		options.add(1, option);
		option = new DropDownElement("2", "Date");
		options.add(2, option);

		return options;
	}

	public static Collection<DropDownElement> getArticlePageSizeOptions() {
		ArrayList<DropDownElement> options = new ArrayList<DropDownElement>(5);
		DropDownElement option = null;

		option = new DropDownElement("5", "5");
		options.add(0, option);
		option = new DropDownElement("10", "10");
		options.add(1, option);
		option = new DropDownElement("25", "25");
		options.add(2, option);

		return options;
	}

	public static Collection<DropDownElement> getSRPageSizeOptions() {
		ArrayList<DropDownElement> options = new ArrayList<DropDownElement>(5);
		DropDownElement option = null;

		option = new DropDownElement("5", "5");
		options.add(0, option);
		option = new DropDownElement("10", "10");
		options.add(1, option);
		option = new DropDownElement("25", "25");
		options.add(2, option);
		option = new DropDownElement("50", "50");
		options.add(3, option);
		option = new DropDownElement("100", "100");
		options.add(4, option);

		return options;
	}

	public static Collection<DropDownElement> getTitleOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option = new DropDownElement(
				String.valueOf(SELECT_ONE), "Choose One");
		options.add(option);
		option = new DropDownElement("0", "Mr.");
		options.add(option);
		option = new DropDownElement("1", "Mrs.");
		options.add(option);
		option = new DropDownElement("2", "Ms.");
		options.add(option);
		option = new DropDownElement("3", "Dr.");
		options.add(option);

		return options;
	}

	/*
	 * Get a list of all 50 states with corresponding value for use with a
	 * Struts select tag
	 */
	public static Collection<DropDownElement> getStateOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option = new DropDownElement(
				String.valueOf(SELECT_ONE), "Choose One");
		options.add(option);
		option = new DropDownElement("0", "Alabama");
		options.add(option);
		option = new DropDownElement("1", "Alaska");
		options.add(option);
		option = new DropDownElement("2", "Arizona");
		options.add(option);
		option = new DropDownElement("3", "Arkansas");
		options.add(option);
		option = new DropDownElement("4", "California");
		options.add(option);
		option = new DropDownElement("5", "Colorado");
		options.add(option);
		option = new DropDownElement("6", "Connecticut");
		options.add(option);
		option = new DropDownElement("7", "Delaware");
		options.add(option);
		option = new DropDownElement("8", "District of Columbia");
		options.add(option);
		option = new DropDownElement("9", "Florida");
		options.add(option);
		option = new DropDownElement("10", "Georgia");
		options.add(option);
		option = new DropDownElement("11", "Hawaii");
		options.add(option);
		option = new DropDownElement("12", "Idaho");
		options.add(option);
		option = new DropDownElement("13", "Illinois");
		options.add(option);
		option = new DropDownElement("14", "Indiana");
		options.add(option);
		option = new DropDownElement("15", "Iowa");
		options.add(option);
		option = new DropDownElement("16", "Kansas");
		options.add(option);
		option = new DropDownElement("17", "Kentucky");
		options.add(option);
		option = new DropDownElement("18", "Louisiana");
		options.add(option);
		option = new DropDownElement("19", "Maine");
		options.add(option);
		option = new DropDownElement("20", "Maryland");
		options.add(option);
		option = new DropDownElement("21", "Massachusetts");
		options.add(option);
		option = new DropDownElement("22", "Michigan");
		options.add(option);
		option = new DropDownElement("23", "Minnesota");
		options.add(option);
		option = new DropDownElement("24", "Mississippi");
		options.add(option);
		option = new DropDownElement("25", "Missouri");
		options.add(option);
		option = new DropDownElement("26", "Montana");
		options.add(option);
		option = new DropDownElement("27", "Nebraska");
		options.add(option);
		option = new DropDownElement("28", "Neveda");
		options.add(option);
		option = new DropDownElement("29", "New Hampshire");
		options.add(option);
		option = new DropDownElement("30", "New Jersey");
		options.add(option);
		option = new DropDownElement("31", "New Mexico");
		options.add(option);
		option = new DropDownElement("32", "New York");
		options.add(option);
		option = new DropDownElement("33", "North Carolina");
		options.add(option);
		option = new DropDownElement("34", "North Dakota");
		options.add(option);
		option = new DropDownElement("35", "Ohio");
		options.add(option);
		option = new DropDownElement("36", "Oklahoma");
		options.add(option);
		option = new DropDownElement("37", "Oregon");
		options.add(option);
		option = new DropDownElement("38", "Pennsylvania");
		options.add(option);
		option = new DropDownElement("39", "Rhode Island");
		options.add(option);
		option = new DropDownElement("40", "South Carolina");
		options.add(option);
		option = new DropDownElement("41", "South Dakota");
		options.add(option);
		option = new DropDownElement("42", "Tennesee");
		options.add(option);
		option = new DropDownElement("43", "Texas");
		options.add(option);
		option = new DropDownElement("44", "Utah");
		options.add(option);
		option = new DropDownElement("45", "Vermont");
		options.add(option);
		option = new DropDownElement("46", "Virginia");
		options.add(option);
		option = new DropDownElement("47", "Washington");
		options.add(option);
		option = new DropDownElement("48", "West Virginia");
		options.add(option);
		option = new DropDownElement("49", "Wisconsin");
		options.add(option);
		option = new DropDownElement("50", "Wyoming");
		options.add(option);

		return options;
	}

	/*
	 * TODO: Find out what the real user list is
	 */
	public static Collection<DropDownElement> getUserOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option = new DropDownElement(
				String.valueOf(SELECT_ONE), "Choose One");
		options.add(option);
		option = new DropDownElement("0", "Rabbit");
		options.add(option);
		option = new DropDownElement("1", "Squirrel");
		options.add(option);
		option = new DropDownElement("2", "Kangaroo");
		options.add(option);
		option = new DropDownElement("3", "Shrew");
		options.add(option);
		option = new DropDownElement("4", "Vole");
		options.add(option);

		return options;
	}

	/*
	 * TODO: Find out what the real user list is
	 */
	public static Collection<DropDownElement> getCountryOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option = new DropDownElement(
				String.valueOf(SELECT_ONE), "Choose One");
		options.add(option);
		option = new DropDownElement("0", "Albania");
		options.add(option);
		option = new DropDownElement("1", "Austrailia");
		options.add(option);
		option = new DropDownElement("2", "France");
		options.add(option);
		option = new DropDownElement("3", "Serbia");
		options.add(option);
		option = new DropDownElement("4", "Waziristan");
		options.add(option);

		return options;
	}

	public static Collection<DropDownElement> getMonthOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		// option = new DropDownElement(String.valueOf(SELECT_ONE),
		// "Choose One");
		// options.add(option);
		DropDownElement option = new DropDownElement("0", "January");
		options.add(option);
		option = new DropDownElement("1", "February");
		options.add(option);
		option = new DropDownElement("2", "March");
		options.add(option);
		option = new DropDownElement("3", "April");
		options.add(option);
		option = new DropDownElement("4", "May");
		options.add(option);
		option = new DropDownElement("5", "June");
		options.add(option);
		option = new DropDownElement("6", "July");
		options.add(option);
		option = new DropDownElement("7", "August");
		options.add(option);
		option = new DropDownElement("8", "September");
		options.add(option);
		option = new DropDownElement("9", "October");
		options.add(option);
		option = new DropDownElement("10", "November");
		options.add(option);
		option = new DropDownElement("11", "December");
		options.add(option);

		return options;
	}

	public static Collection<DropDownElement> getDayOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option; // = new
		// DropDownElement(String.valueOf(SELECT_ONE),
		// "Choose One");
		for (int i = 1; i < 32; i++) {
			String day = String.valueOf(i);
			option = new DropDownElement(day, day);
			options.add(option);
		}

		return options;
	}

	/**
	 * Getter for obtaining a selection list of years to choose for viewing
	 * reports. TODO: In the absense of more specific requirements start with
	 * the year 2004 and go to the current year.
	 * 
	 * @return a Collection of DropDownElement objects with years as Strings for
	 *         both the text and value.
	 */
	public static Collection<DropDownElement> getYearOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);

		DropDownElement option; // = new
		// DropDownElement(String.valueOf(SELECT_ONE),
		// "Choose One");
		for (int i = 2004; i <= year; i++) {
			String yearStr = String.valueOf(i);
			option = new DropDownElement(yearStr, yearStr);
			options.add(option);
		}

		return options;
	}

	/**
	 * Utility to trim a text String to length on word boundaries to less than
	 * or equal to some length argument. The string will be trimmed to the last
	 * word less than or equal to the trim length.
	 * 
	 * @param string
	 *            to be trimmed.
	 * @param length
	 *            to trim the String to. If the String exceeds length the String
	 *            will be trimmed to less than the length on a word boundary.
	 * @return the trimmed String.
	 */
	public static String trimString(String string, int length) {
		String blank = " ";
		if (string == null) {
			string = "";
		} else {
			if (string.length() > length) {
				StringBuffer buffer = new StringBuffer();
				String[] words = string.split(" ");
				int count = words.length;
				int idx = 0;
				boolean loop = true;
				// Loop through the words and add them to the buffer
				// until the buffer length exceeds 'length'.
				while (loop) {
					int l = words[idx].length();
					int bufLength = buffer.length();
					if (l + bufLength < length) {
						buffer.append(words[idx]);
						if (buffer.length() < (length - 1)) {
							buffer.append(blank);
						} else {
							loop = false;
						}
					} else {
						loop = false;
					}

					if (idx++ >= count) {
						loop = false;
					}
				}

				string = buffer.toString().trim();
			}
		}

		return string;
	}

/**
    * Replace all occurances of '<' or '>' in a String with the HTML
    * escape entities &lt; and &gt; respectively.
    * @param input is the input String to escape for bracket characters.
    * @return the escaped String.
    */
	public static String escapeBrackets(String input) {
		String escapeString = "";
		if (input != null && !"".equals(input)) {
			escapeString = input.replaceAll(">", "&gt;");
			escapeString = escapeString.replaceAll("<", "&lt;");
		}

		return escapeString;
	}

	/**
	 * Replace all occurances of CR or LF in a String with null string.
	 * 
	 * @param input
	 *            is the input String to escape for CR or LF.
	 * @return the escaped String.
	 */
	public static String stripCRLF(String input) {
		String escapeString = "";
		if (input != null && !"".equals(input)) {
			escapeString = input.replaceAll("\n", "");
			escapeString = escapeString.replaceAll("\r", "");
		}

		return escapeString;
	}

	/**
	 * Clears the current Action Form from the session.
	 * 
	 * To use, in the dispatchMethod of your action, call:
	 * WebUtils.clearActionFormFromSession( request, mapping ) right before
	 * returning the ActionForward/
	 * 
	 * @param request
	 * @param mapping
	 */
	public static void clearActionFormFromSession(HttpServletRequest request,
			ActionMapping mapping) {
		request.getSession().removeAttribute(mapping.getName());
	}

	/**
	 * Returns the URL of the specified <code>HttpServletRequest</code>.
	 */
	public static String getRequestedURL(HttpServletRequest request) {
		String query = StringUtils.isEmpty(request.getQueryString()) ? Constants.EMPTY_STRING
				: "?" + request.getQueryString();

		String url = request.getRequestURI() + query;

		return url;
	}

	/**
	 * Returns the URL of the specified <code>ActionMapping</code> along with
	 * any query parameters from the <code>HttpServletRequest</code>. Will
	 * return a context-relative path.
	 */
	public static String getRequestedAction(ActionMapping mapping,
			HttpServletRequest request) {
		String query = StringUtils.isEmpty(request.getQueryString()) ? Constants.EMPTY_STRING
				: "?" + request.getQueryString();

		String url = mapping.getPath() + ".do" + query;

		return url;
	}

	/**
	 * Returns a <code>String</code> representation of an instance of
	 * <code>Money</code>, previously translated into decimal format and
	 * rounded.
	 */
	public static String formatMoney(Money money) {
		return formatMoney(money, true);
	}

	/**
	 * Returns a <code>String</code> representation of an instance of
	 * <code>Money</code>, previously translated into decimal format with the
	 * option to be rounded.
	 */
	public static String formatMoney(Money money, boolean roundAmount) {
		String moneyString = null;

		if (money == null)
			return null;

		Currency moneyCurrency = Currency.getInstance(money.getCurrencyCode());

		DecimalFormat moneyDecimalFormat = new DecimalFormat(DECIMAL_MASK);

		moneyDecimalFormat.setGroupingUsed(true);
		moneyDecimalFormat.setGroupingSize(3);

		if (roundAmount) {

			String formattedAmount = moneyDecimalFormat
					.format(money.getValue());

			moneyString = moneyCurrency.getSymbol() + " " + formattedAmount;
		} else {
			double value = money.getValue();
			String stringValue = String.valueOf(value);

			if (stringValue.substring(stringValue.indexOf(".") + 1).length() < 2)
				stringValue = moneyDecimalFormat.format(value);

			moneyString = moneyCurrency.getSymbol() + " " + stringValue;
		}

		return moneyString;
	}

	public static String getBaseURLFromRequest(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		StringBuffer requestURLLower = new StringBuffer(requestURL.toString()
				.toLowerCase());
		int lastPart;

		int firstPart = requestURLLower.indexOf("http://");
		if (firstPart > -1) {
			requestURLLower = requestURLLower.replace(0, firstPart + 7, "");
		} else {
			firstPart = requestURLLower.indexOf("https://");
			if (firstPart > -1) {
				requestURLLower = requestURLLower.replace(0, firstPart + 8, "");
			}
		}
		lastPart = requestURLLower.indexOf("/cc2");
		if (lastPart > -1) {
			lastPart = lastPart + 4;
		} else {
			lastPart = requestURLLower.indexOf("/ccc");
			if (lastPart > -1) {
				lastPart = lastPart + 4;
			} else {
				lastPart = requestURLLower.indexOf("/");
			}
		}
		if (lastPart > -1) {
			requestURLLower = requestURLLower.replace(lastPart, requestURLLower
					.length(), "");
		}

		return requestURLLower.toString();
	}

	/*
	 * Convert the case of an arbitrary String to be lower case except for the
	 * first character which is capitalized.
	 */
	public static String makeDisplayable(String word) {
		String rtnValue = "";
		if (word != null && !"".equals(word)) {
			char firstCharacter = word.charAt(0);
			String firstLetter = String.valueOf(firstCharacter);
			firstLetter = firstLetter.toUpperCase();
			int length = word.length();
			if (length > 1) {
				rtnValue = word.toLowerCase();
				rtnValue = firstLetter + rtnValue.substring(1);
			} else {
				rtnValue = firstLetter;
			}
		}

		return rtnValue;
	}

	/*
	 * check if the entered String represents a number
	 */
	public static boolean isAllDigit(String strNumber) {
		// eliminate whitespace before conversion
		String numberString = strNumber.trim();
		try {
			Long.parseLong(numberString);
			return true;
		} catch (NumberFormatException numberFormatException) {
			return false;
		}
	}
	
	/*
	 * check if the enter String is ASCII-7
	 * 
	 */
	public static boolean isUSAscii(String asciiString){
		if (asciiString == null){
			//To return true might seem counter-intuitive, but if the check is not on a required field which was left
			//null then the user would probably get an error message requiring non-null input
			return true;
		}
		for (int index=0; index<asciiString.length(); index++) {
			int codePoint = asciiString.codePointAt(index);
			if (!(asciiString.codePointAt(index)>=0 && asciiString.codePointAt(index) < 128)){
				return false;
			}
		}
		return true;
	}

	public static String convertYYYYMMDDtoMediumDate(String strYYYYMMDD) {
		
		if (strYYYYMMDD.trim().length() != 8) {
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		String strYear = strYYYYMMDD.substring(0, 4);
		String strMonth = strYYYYMMDD.substring(4, 6);
		String strDay = strYYYYMMDD.substring(6);
		if(strMonth.compareTo("00")==0){
			strMonth="01";
		}
		if(strDay.compareTo("00")==0){
			strDay="01";
		}
		calendar.set(Integer.parseInt(strYear), Integer.parseInt(strMonth) - 1,
				Integer.parseInt(strDay));
		Date pubDate = calendar.getTime();
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String strRunPubDate = dateFormat.format(pubDate);
		
		return strRunPubDate;
	}

	public static String convertYYYYMMtoMediumDate(String strYYYYMM) {
		if (strYYYYMM.trim().length() != 6) {
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		String strYear = strYYYYMM.substring(0, 4);
		String strMonth = strYYYYMM.substring(4, 6);
		calendar.set(Calendar.YEAR, Integer.parseInt(strYear));
		calendar.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1);
		Date pubDate = calendar.getTime();
		SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");// DateFormat.getDateInstance(DateFormat.MEDIUM);
		String strRunPubMonth = monthFormat.format(pubDate);
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		String strRunPubYear = yearFormat.format(pubDate);
		return strRunPubMonth + " " + strRunPubYear;
	}
     
     public static Date convertMMDDYYYYtoDate(String strMMDDYYYY){
  		Calendar calendar = Calendar.getInstance();
		String strMonth = strMMDDYYYY.substring(0, 2);
		String strDay = strMMDDYYYY.substring(2,4);
		String strYear = strMMDDYYYY.substring(4);
		calendar.set(Integer.parseInt(strYear),Integer.parseInt(strMonth)-1,Integer.parseInt(strDay));
		Date date = calendar.getTime();
		return date;
     }

	/*
	 * A utility method for building the RIGHTSLINK Order History Sort Select
	 * options list suitable for use with the struts html.optionsCollection tag.
	 * 
	 * @return a Collection of DropDownElement objects for each label/value pair
	 * in the Select Option list.
	 */
	public static Collection<DropDownElement> getRLOHSortOptions() {
		Collection<DropDownElement> options = new ArrayList<DropDownElement>();

		DropDownElement option = new DropDownElement(
				DisplaySpecServices.RLORDERDATESORT, "Order Date");
		options.add(option);
		option = new DropDownElement(
				DisplaySpecServices.RLPUBLICATIONTITLESORT, "Publication Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.RLARTICLETITLESORT,
				"Article Title");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.RLTYPEOFUSESORT,
				"Type Of Use");
		options.add(option);
		option = new DropDownElement(DisplaySpecServices.RLFEESORT, "Fee");
		options.add(option);

		return options;
	}
	  public static Date getEndOfTime(Integer Year) {
			//12/31/3000 at 12:00:00.000 am
			Calendar calendar = Calendar.getInstance();
			calendar.set( Calendar.HOUR_OF_DAY, 0 );
			calendar.set( Calendar.MINUTE, 0 );
			calendar.set( Calendar.SECOND, 0 );
			calendar.set( Calendar.MILLISECOND, 0 );
			calendar.set( Calendar.DATE , 31);
			calendar.set( Calendar.MONTH , Calendar.DECEMBER);
			calendar.set( Calendar.YEAR , Year);
	        return calendar.getTime();
		}
		
		public static Date getBeginningOfTime(Integer Year) {
			//1/1/1000 at 12:00:00.000 am
			Calendar calendar = Calendar.getInstance();
			calendar.set( Calendar.HOUR_OF_DAY, 0 );
			calendar.set( Calendar.MINUTE, 0 );
			calendar.set( Calendar.SECOND, 0 );
			calendar.set( Calendar.MILLISECOND, 0 );
			calendar.set( Calendar.DATE , 1);
			calendar.set( Calendar.MONTH , Calendar.JANUARY);
			calendar.set( Calendar.YEAR , Year);
	        return calendar.getTime();
		}
}
