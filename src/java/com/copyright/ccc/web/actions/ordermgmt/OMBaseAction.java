package com.copyright.ccc.web.actions.ordermgmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.web.actions.ordermgmt.data.CustomPageControl;
import com.copyright.ccc.web.actions.ordermgmt.data.EditBundle;
import com.copyright.ccc.web.actions.ordermgmt.data.EditConfirmation;
import com.copyright.ccc.web.actions.ordermgmt.data.EditItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ItemWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchResultWrapper;
import com.copyright.ccc.web.actions.ordermgmt.data.ProcessMessage;
import com.copyright.ccc.web.actions.ordermgmt.data.SearchWorksCriteriaWrapper;
import com.copyright.ccc.web.actions.ordermgmt.util.MiscUtils;
import com.copyright.ccc.web.actions.ordermgmt.util.SessionExpiredException;
import com.copyright.ccc.web.actions.ordermgmt.util.SessionExpiredExceptionOnPopUp;
import com.copyright.svc.worksremote.api.search.SearchServices;
import com.copyright.workbench.security.NotAuthorizedRuntimeException;
import com.copyright.workbench.security.UserContextHelperBase;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public abstract class OMBaseAction extends ActionSupport implements SessionAware, Preparable {
	
	protected static final String REDIRECT = "redirect";
    protected static final String SAVE_COMPLETE = "savecomplete";
    protected static final String SAVE_ALL_COMPLETE = "saveallcomplete";
    protected static final String SAVE_CONFIRM_HEADER_COMPLETE = "saveconfirmcomplete";
    protected static final String SAVE_INVOICE_COMPLETE = "saveinvoicecomplete";
    protected static final String SAVE_INVOICE_HEADER_COMPLETE = "saveinvoiceheadercomplete";
    protected static final String NOT_AUTHORIZED = "notAuthorized";

	private static final long serialVersionUID = 1L;
	
	private String selectedConfirmNumber;
	private String selectedBundleNumber;
	private String selectedDetailNumber;
	private String selectedInvoiceNumber;
	private String selectedOrderId;
	private String selectedBundleId;
	private String selectedItem;
	private boolean updatePricingOnly = false;
	private boolean selectedItemStoredInOMS = true;
	private boolean hideBackToSearchLink = false;
	private boolean resorting = false;
	private boolean resizing = false;
    private boolean searching;
    private boolean resetToPage1 = true;
    private boolean showDbTimings = StringUtils.defaultString(getText("show.db.timings")).equalsIgnoreCase("true");
    private boolean showRenderTimings = StringUtils.defaultString(getText("show.render.timings")).equalsIgnoreCase("true");
    private boolean includeCancelledOrders = false;
	private OrderSearchResultWrapper[] wrappedResultsArray;
	private boolean useWrappedResultsFromSession = false;
    
	private String savingConfirmation;
	private String savingBundle;
	private String savingInvoice;
	
    private boolean overrideAllowEdit = !StringUtils.defaultString(getText("override.edit.allowed")).equalsIgnoreCase("false");

	private int detailPagingThreshold = Integer.valueOf(StringUtils.defaultString(getText("paging.threshold"),"5")).intValue();

	public abstract QuickTabSelect getDefaultQuickTabSelect();
    // This is method that gets called each time any Order Mgmt Action gets instantiated.
    public void prepare (){
    }
    
    public boolean verifyPrivilege(CCPrivilegeCode pc) {
    	try {
    		UserContextService.checkPrivilege(pc);
    		return true;
    	}
    	catch ( NotAuthorizedRuntimeException nare )
        {
    		//addActionError("You do not have permission to access this page. " + nare.getMessage());
    		return false;
        }
    }

    public boolean isOverrideAllowEdit() {
		return overrideAllowEdit;
	}

	public boolean isSearching() {
		return searching;
	}
	
	public void setSearching(boolean searching) {
		this.searching = searching;
	}
	
	public boolean isHideBackToSearchLink() {
		return hideBackToSearchLink;
	}

	public void setHideBackToSearchLink(boolean hideBackToSearchLink) {
		this.hideBackToSearchLink = hideBackToSearchLink;
	}
	
	public String getSelectedConfirmNumber() {
		return selectedConfirmNumber;
	}

	public void setSelectedConfirmNumber(String selectedConfirmNumber) {
		this.selectedConfirmNumber = selectedConfirmNumber;
	}

	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	public boolean isResorting() {
		return resorting;
	}

	public void setResizing(boolean resizing) {
		this.resizing = resizing;
	}

	public boolean isResizing() {
		return resizing;
	}

	public void setResorting(boolean resorting) {
		this.resorting = resorting;
	}


	public boolean isIncludeCancelledOrders() {
		return includeCancelledOrders;
	}

	public void setIncludeCancelledOrders(boolean includeCancelledOrders) {
		this.includeCancelledOrders = includeCancelledOrders;
	}


	private List<String> quickTabSelected = new ArrayList<String>();
	private String currentQuickTab;
	
    private QuickTabSelect quickTabSelect = null;

	public QuickTabSelect getQuickTabSelect() {
		return quickTabSelect;
	}

	public void setQuickTabSelect(QuickTabSelect quickTabSelect) {
		this.quickTabSelect = quickTabSelect;
	}

	QuickTabUserCounts quickTabUserCounts;
	
	private int myOrders = -1;
	private int myAdjustments = -1;
	private int pendingAdjustments = -1;
	private int myResearch = -1;
	private int assignedResearch = -1;
	private int unassignedResearch = -1;

	public int getMyOrders() {
		return myOrders;
	}

	public void setMyOrders(int myOrders) {
		this.myOrders = myOrders;
	}

	public int getMyAdjustments() {
		return myAdjustments;
	}

	public void setMyAdjustments(int myAdjustments) {
		this.myAdjustments = myAdjustments;
	}

	public int getPendingAdjustments() {
		return pendingAdjustments;
	}

	public void setPendingAdjustments(int pendingAdjustments) {
		this.pendingAdjustments = pendingAdjustments;
	}

	public int getMyResearch() {
		return myResearch;
	}

	public void setMyResearch(int myResearch) {
		this.myResearch = myResearch;
	}

	public int getAssignedResearch() {
		return assignedResearch;
	}

	public void setAssignedResearch(int assignedResearch) {
		this.assignedResearch = assignedResearch;
	}

	public int getUnassignedResearch() {
		return unassignedResearch;
	}

	public void setUnassignedResearch(int unassignedResearch) {
		this.unassignedResearch = unassignedResearch;
	}

	public QuickTabUserCounts getQuickTabUserCounts() {
		return quickTabUserCounts;
	}

	public void setQuickTabUserCounts(QuickTabUserCounts quickTabUserCounts) {
		this.quickTabUserCounts = quickTabUserCounts;
	}

	public List<String> getQuickTabSelected() {
		return quickTabSelected;
	}

	public void setQuickTabSelected(List<String> quickTabSelected) {
		this.quickTabSelected = quickTabSelected;
	}

	/**
	 * Executed when user hits cancel.  
	 * Just redirects to execute so that all request
	 * and session parameters are cleared.
	 */
	@SkipValidation
	public String cancel()
	{
		return REDIRECT;
	}

	public static final String QUICK_TAB_SELECTED_KEY = "QUICK-TAB-SELECTED";
	
	protected void handleQuickTabSelected() {
		quickTabUserCounts = new QuickTabUserCounts(
			getMyOrders()	/* userMyOrders */ 
		   ,getMyAdjustments()	/* userMyAdjustments */ 
		   ,getPendingAdjustments()	/* userPendingAdjustments */ 
		   ,getMyResearch()	/* userMyResearch */ 
		   ,getAssignedResearch()	/* userAssignedResearch */ 
		   ,getUnassignedResearch()	/* userUnassignedResearch */ 
        );
		
		quickTabSelect = getQuickTabSelectedInSession( getDefaultQuickTabSelect() );
		
		QuickTabSelect quickTabSelected = QuickTabSelect.matching( getQuickTabSelected(), quickTabSelect, quickTabUserCounts );
        
		if ( !quickTabSelect.equals( quickTabSelected ) ) {
        	//resorting = false;
        	//searching = false;
         	quickTabSelect = quickTabSelected;
        }
		setCurrentQuickTab(quickTabSelect.getDotFormattedSelectedValue());
		setQuickTabSelected(new ArrayList<String>());
		getQuickTabSelected().add(quickTabSelect.getDotFormattedSelectedValue());
	
		if ( getSession().containsKey(QUICK_TAB_SELECTED_KEY)) {
			getSession().remove(QUICK_TAB_SELECTED_KEY);
		}
		//storeQuickTabSelectedInSession( quickTabSelect );
		
	}

	protected String getQuickTabSelectedInSessionDotFormattedSelectedValue() {
		if ( getSession().containsKey(QUICK_TAB_SELECTED_KEY)) {
			QuickTabSelect qTab = (QuickTabSelect)getSession().get(QUICK_TAB_SELECTED_KEY);
			return qTab.getDotFormattedSelectedValue();
		}
		return null;
	}
	
	protected QuickTabSelect getQuickTabSelectedInSession( QuickTabSelect defaultQuickTab) {
		if ( getSession().containsKey(QUICK_TAB_SELECTED_KEY)) {
			return (QuickTabSelect)getSession().get(QUICK_TAB_SELECTED_KEY);
		}
		return defaultQuickTab;
	}
	
	@SuppressWarnings("unchecked")
	private void storeQuickTabSelectedInSession( QuickTabSelect quickTab) {
		if ( quickTab != null ) {
			if ( getSession().containsKey(QUICK_TAB_SELECTED_KEY)) {
				getSession().remove(QUICK_TAB_SELECTED_KEY);
			}
			getSession().put(QUICK_TAB_SELECTED_KEY, quickTab );
		}
	}
	
	private Map<String, Object> _session;
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		_session = arg0;		
	}

	@SuppressWarnings("unchecked")
	public Map getSession() {
		return _session;
	}

	public static final String ORDER_SEARCH_CRITERIA_KEY = "ORDER-SEARCH-CRITERIA-KEY";
	public static final String VIEW_ORDER_SEARCH_CRITERIA_KEY = "VIEW-ORDER-SEARCH-CRITERIA-KEY";
	public static final String ADJUSTMENT_ORDER_SEARCH_CRITERIA_KEY = "ADJUSTMENT-ORDER-SEARCH-CRITERIA-KEY";
	public static final String ORDER_SEARCH_RESULTS_KEY = "ORDER-SEARCH-RESULTS-KEY";
	public static final String CUSTOM_PAGE_CONTROL_KEY = "CUSTOM-PAGE-CONTROL-KEY";
	public static final String PROCESS_MESSAGES_LIST_KEY  = "PROCESS-MESSAGES-LIST-KEY";
	public static final String ALL_PROCESS_MESSAGES_LIST_KEY  = "ALL_PROCESS-MESSAGES-LIST-KEY";
	public static final String PROCESS_EDITS_LIST_KEY  = "PROCESS-EDITS-LIST-KEY";
	public static final String CONFIRM_EDITS_LIST_KEY  = "CONFIRM-EDITS-LIST-KEY";
	public static final String EDIT_BUNDLES_KEY  = "EDIT-BUNDLES-KEY";
	public static final String SAVING_BUNDLE_KEY  = "SAVING-BUNDLE-KEY";
	public static final String OMS_SESSION_EXISTS_KEY = "OMS-SESSION-EXISTS-KEY";
	public static final String WORK_SEARCH_CRITERIA_KEY = "WORK-SEARCH-CRITERIA-KEY";
	public static final String ADDED_DETAILS_LIST_KEY  = "ADDED-DETAILS-LIST-KEY";
	public static final String WRAPPED_CRITERIA_MAP_KEY  = "WRAPPED-CRITERIA-MAP-KEY";
	public static final String PARTNER_ID_CODE_MAP_KEY = "PARTNER-ID-CODE-MAP-KEY";

	public static final String OMS_SESSION_EXPIRED_MSG = "Session expired.";
	public static final String OMS_SESSION_EXPIRED_REDIRECT = "sessionExpired";

	public void checkOmsSessionExpiredOnPopUp() {
		try{
			getOmsSessionExistsInSession();
		} catch (SessionExpiredException s){
			throw new SessionExpiredExceptionOnPopUp(s);
		}
	}
	
	protected void storeOmsSessionExistsInSession() { 
		if ( UserContextHelperBase.isAvailable() ) {
			storeOmsSessionExistsInSession( Long.toString(UserContextHelperBase.getCurrentAsInterface().getAuthenticatedAppUserID() ));
		} else {
			throw new SessionExpiredException(OMS_SESSION_EXPIRED_MSG,OMS_SESSION_EXPIRED_REDIRECT);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void storeOmsSessionExistsInSession( String currentUserGUID) {
		if ( StringUtils.isNotEmpty(currentUserGUID) ) {
			removeOmsSessionExistsFromSession();
			getSession().put(OMS_SESSION_EXISTS_KEY, currentUserGUID );
		}
	}
	
	protected void removeOmsSessionExistsFromSession() {
		if ( getSession().containsKey(OMS_SESSION_EXISTS_KEY)) {
			getSession().remove(OMS_SESSION_EXISTS_KEY);
		}
	}
	protected String getOmsSessionExistsInSession() {
		String omsSession = null;
		if ( getSession().containsKey(OMS_SESSION_EXISTS_KEY)) {
			Object sessionObj = getSession().get(OMS_SESSION_EXISTS_KEY);
			if ( sessionObj instanceof String ) {
				omsSession = (String)getSession().get(OMS_SESSION_EXISTS_KEY);
			}
		}
		if(getSession().get(OMS_SESSION_EXISTS_KEY)==null ) {
			throw new SessionExpiredException(OMS_SESSION_EXPIRED_MSG,OMS_SESSION_EXPIRED_REDIRECT);
		}
		return omsSession;
	}
	
	@SuppressWarnings("unchecked")
	protected List<ItemWrapper> getAddedDetailsInSession() {
		if ( getSession().containsKey(ADDED_DETAILS_LIST_KEY)) {
			Object sessionObj = getSession().get(ADDED_DETAILS_LIST_KEY);
			if ( sessionObj instanceof List ) {
				return (List<ItemWrapper>)getSession().get(ADDED_DETAILS_LIST_KEY);
			}
		}
		return new ArrayList<ItemWrapper>();
		//return new CustomPageControl(getDetailPagingThreshold());
	}
	
	@SuppressWarnings("unchecked")
	protected void storeAddedDetailsInSession( List<ItemWrapper> list) {
		if ( list != null ) {
			removeAddedDetailsFromSession();
			getSession().put(ADDED_DETAILS_LIST_KEY, list );
		}
	}
	
	protected void removeAddedDetailsFromSession() {
		if ( getSession().containsKey(ADDED_DETAILS_LIST_KEY)) {
			getSession().remove(ADDED_DETAILS_LIST_KEY);
		}
	}
	
	protected CustomPageControl getCustomPageControlInSession() {
		if ( getSession().containsKey(CUSTOM_PAGE_CONTROL_KEY)) {
			Object sessionObj = getSession().get(CUSTOM_PAGE_CONTROL_KEY);
			if ( sessionObj instanceof CustomPageControl ) {
				return (CustomPageControl)getSession().get(CUSTOM_PAGE_CONTROL_KEY);
			}
		}
		return new CustomPageControl(getDetailPagingThreshold());
	}
	
	@SuppressWarnings("unchecked")
	protected void storeCustomPageControlInSession( CustomPageControl customPageControl) {
		if ( customPageControl != null ) {
			removeCustomPageControlFromSession();
			getSession().put(CUSTOM_PAGE_CONTROL_KEY, customPageControl );
		}
	}
	
	protected void removeCustomPageControlFromSession() {
		if ( getSession().containsKey(CUSTOM_PAGE_CONTROL_KEY)) {
			getSession().remove(CUSTOM_PAGE_CONTROL_KEY);
		}
	}

	protected OrderSearchCriteriaWrapper getOrderSearchCriteriaInSession() {
		return  getOrderSearchCriteriaInSession(ORDER_SEARCH_CRITERIA_KEY);
	}
	
	protected OrderSearchCriteriaWrapper getOrderSearchCriteriaInSession(String sessionKey) {
		if ( getSession().containsKey(sessionKey)) {
			Object sessionObj = getSession().get(sessionKey);
			if ( sessionObj instanceof OrderSearchCriteriaWrapper ) {
				return (OrderSearchCriteriaWrapper)getSession().get(sessionKey);
			}
		}
		if ( sessionKey.equalsIgnoreCase(VIEW_ORDER_SEARCH_CRITERIA_KEY) ) {
			return new OrderSearchCriteriaWrapper(true, true /* use default sort, for view */);
		} else {
			return new OrderSearchCriteriaWrapper(true /* use default sort */);
		}
	}
	
	protected void storeOrderSearchCriteriaInSession( OrderSearchCriteriaWrapper orderSearchCriteria) {
		storeOrderSearchCriteriaInSession( orderSearchCriteria, ORDER_SEARCH_CRITERIA_KEY);
	}
	
	@SuppressWarnings("unchecked")
	protected void storeOrderSearchCriteriaInSession( OrderSearchCriteriaWrapper orderSearchCriteria, String sessionKey) {
		if ( orderSearchCriteria != null ) {
			removeOrderSearchCriteriaFromSession(sessionKey);
			getSession().put(sessionKey, orderSearchCriteria );
		}
	}
	protected void removeOrderSearchCriteriaFromSession() {
		removeOrderSearchCriteriaFromSession( ORDER_SEARCH_CRITERIA_KEY );
	}
	
	protected void removeOrderSearchCriteriaFromSession( String sessionKey ) {
		if ( getSession().containsKey(sessionKey)) {
			getSession().remove(sessionKey);
		}
	}
	
	protected SearchWorksCriteriaWrapper getWorkSearchCriteriaInSession() {
		if ( getSession().containsKey(WORK_SEARCH_CRITERIA_KEY)) {
			Object sessionObj = getSession().get(WORK_SEARCH_CRITERIA_KEY);
			if ( sessionObj instanceof SearchWorksCriteriaWrapper ) {
				return (SearchWorksCriteriaWrapper)getSession().get(WORK_SEARCH_CRITERIA_KEY);
			}
		}
		return new SearchWorksCriteriaWrapper();
	}
		
	@SuppressWarnings("unchecked")
	protected void storeWorkSearchCriteriaInSession( SearchWorksCriteriaWrapper workSearchCriteria) {
		if ( workSearchCriteria != null ) {
			removeWorkSearchCriteriaFromSession();
			getSession().put(WORK_SEARCH_CRITERIA_KEY, workSearchCriteria );
		}
	}
	protected void removeWorkSearchCriteriaFromSession() {
		removeOrderSearchCriteriaFromSession( WORK_SEARCH_CRITERIA_KEY );
	}
	
	
	protected OrderSearchResultWrapper getOrderSearchResultsWrapperInSession() {
		if ( getSession().containsKey(ORDER_SEARCH_RESULTS_KEY)) {
			Object sessionObj = getSession().get(ORDER_SEARCH_RESULTS_KEY);
			if ( sessionObj instanceof OrderSearchResultWrapper ) {
				return (OrderSearchResultWrapper)getSession().get(ORDER_SEARCH_RESULTS_KEY);
			}
		}
		return new OrderSearchResultWrapper();
	}
	
	@SuppressWarnings("unchecked")
	protected void storeOrderSearchResultsWrapperInSession( OrderSearchResultWrapper orderSearchResults) {
		if ( orderSearchResults != null ) {
			removeOrderSearchResultsWrapperFromSession();
			getSession().put(ORDER_SEARCH_RESULTS_KEY, orderSearchResults );
		}
	}
	
	protected void removeOrderSearchResultsWrapperFromSession() {
		if ( getSession().containsKey(ORDER_SEARCH_RESULTS_KEY)) {
			getSession().remove(ORDER_SEARCH_RESULTS_KEY);
		}
	}
	@SuppressWarnings("unchecked")
	protected List<ProcessMessage> getProcessMessagesListInSession() {
		if ( getSession().containsKey(PROCESS_MESSAGES_LIST_KEY)) {
			Object sessionObj = getSession().get(PROCESS_MESSAGES_LIST_KEY);
			if ( sessionObj instanceof List ) {
				return (List)getSession().get(PROCESS_MESSAGES_LIST_KEY);
			}
		}
		return new ArrayList<ProcessMessage>();
	}
	
	@SuppressWarnings("unchecked")
	protected void storeProcessMessagesListInSession( List<ProcessMessage> processMessages) {
		if ( processMessages != null ) {
			removeProcessMessagesListFromSession();
			getSession().put(PROCESS_MESSAGES_LIST_KEY, processMessages );
		}
	}

	protected void removeProcessMessagesListFromSession() {
		if ( getSession().containsKey(PROCESS_MESSAGES_LIST_KEY)) {
			getSession().remove(PROCESS_MESSAGES_LIST_KEY);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<ProcessMessage> getAllProcessMessagesListInSession() {
		if ( getSession().containsKey(ALL_PROCESS_MESSAGES_LIST_KEY)) {
			Object sessionObj = getSession().get(ALL_PROCESS_MESSAGES_LIST_KEY);
			if ( sessionObj instanceof List ) {
				return (List)getSession().get(ALL_PROCESS_MESSAGES_LIST_KEY);
			}
		}
		return new ArrayList<ProcessMessage>();
	}
	
	@SuppressWarnings("unchecked")
	protected void storeAllProcessMessagesListInSession( List<ProcessMessage> allProcessMessages) {
		if ( allProcessMessages != null ) {
			removeAllProcessMessagesListFromSession();
			getSession().put(ALL_PROCESS_MESSAGES_LIST_KEY, allProcessMessages );
		}
	}
	
	protected void removeAllProcessMessagesListFromSession() {
		if ( getSession().containsKey(ALL_PROCESS_MESSAGES_LIST_KEY)) {
			getSession().remove(ALL_PROCESS_MESSAGES_LIST_KEY);
		}
	}
	@SuppressWarnings("unchecked")
	protected List<EditItemWrapper> getProcessEditsListInSession() {
		if ( getSession().containsKey(PROCESS_EDITS_LIST_KEY)) {
			Object sessionObj = getSession().get(PROCESS_EDITS_LIST_KEY);
			if ( sessionObj instanceof List ) {
				return (List)getSession().get(PROCESS_EDITS_LIST_KEY);
			}
		}
		return new ArrayList<EditItemWrapper>();
	}
	
	@SuppressWarnings("unchecked")
	protected void storeProcessEditsListInSession( List<EditItemWrapper> processEdits) {
		if ( processEdits != null ) {
			removeProcessEditsListFromSession();
			getSession().put(PROCESS_EDITS_LIST_KEY, processEdits );
		}
	}
	
	protected void removeProcessEditsListFromSession() {
		if ( getSession().containsKey(PROCESS_EDITS_LIST_KEY)) {
			getSession().remove(PROCESS_EDITS_LIST_KEY);
		}
	}
	public static final String ORDER_SEARCH_RESULTS_ARRAY_KEY  = "ORDER-SEARCH-RESULTS-ARRAY-KEY";
	
	protected OrderSearchResultWrapper getPagingOrderSearchResultsWrapperInSession(int pagingIndex) {
		OrderSearchResultWrapper[] searchResults = getOrderSearchResultsWrapperArrayInSession();
		if ( searchResults != null ) {
			if ( pagingIndex <= searchResults.length ) {
				return searchResults[pagingIndex];
			}
		}
		return new OrderSearchResultWrapper(); 
	}
	protected OrderSearchResultWrapper[] getOrderSearchResultsWrapperArrayInSession() {
		if ( getSession().containsKey(ORDER_SEARCH_RESULTS_ARRAY_KEY)) {
			Object sessionObj = getSession().get(ORDER_SEARCH_RESULTS_ARRAY_KEY);
			if ( sessionObj instanceof OrderSearchResultWrapper[] ) {
				return (OrderSearchResultWrapper[])getSession().get(ORDER_SEARCH_RESULTS_ARRAY_KEY);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected void storeOrderSearchResultsWrapperArrayInSession( OrderSearchResultWrapper[] orderSearchResults) {
		if ( orderSearchResults != null ) {
			removeOrderSearchResultsWrapperArrayFromSession();
			getSession().put(ORDER_SEARCH_RESULTS_ARRAY_KEY, orderSearchResults );
		}
	}
	
	protected void removeOrderSearchResultsWrapperArrayFromSession() {
		if ( getSession().containsKey(ORDER_SEARCH_RESULTS_ARRAY_KEY)) {
			getSession().remove(ORDER_SEARCH_RESULTS_ARRAY_KEY);
		}
	}

	public OrderSearchResultWrapper[] getWrappedResultsArray() {
		return wrappedResultsArray;
	}
	public void setWrappedResultsArray(OrderSearchResultWrapper[] wrappedResultsArray) {
		this.wrappedResultsArray = wrappedResultsArray;
	}
	
	protected EditConfirmation getConfirmEditsListInSession() {
		if ( getSession().containsKey(CONFIRM_EDITS_LIST_KEY)) {
			Object sessionObj = getSession().get(CONFIRM_EDITS_LIST_KEY);
			if ( sessionObj instanceof EditConfirmation ) {
				return (EditConfirmation)getSession().get(CONFIRM_EDITS_LIST_KEY);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected void storeConfirmEditsListInSession( EditConfirmation confirmEdits) {
		if ( confirmEdits != null ) {
			removeConfirmEditsListFromSession();
			getSession().put(CONFIRM_EDITS_LIST_KEY, confirmEdits );
		}
	}
	
	protected void removeConfirmEditsListFromSession() {
		if ( getSession().containsKey(CONFIRM_EDITS_LIST_KEY)) {
			getSession().remove(CONFIRM_EDITS_LIST_KEY);
		}
	}
	
	protected List<EditBundle> getEditBundlesInSession() {
		if ( getSession().containsKey(EDIT_BUNDLES_KEY)) {
			Object sessionObj = getSession().get(EDIT_BUNDLES_KEY);
			if ( sessionObj instanceof List ) {
				return (List<EditBundle>) getSession().get(EDIT_BUNDLES_KEY);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected void storeEditBundlesInSession( List<EditBundle> editBundles) {
		if ( editBundles != null ) {
			removeEditBundlesFromSession();
			getSession().put(EDIT_BUNDLES_KEY, editBundles );
		}
	}
	
	protected void removeEditBundlesFromSession() {
		if ( getSession().containsKey(EDIT_BUNDLES_KEY)) {
			getSession().remove(EDIT_BUNDLES_KEY);
		}
	}
	
	protected String getSavingBundleInSession() {
		if ( getSession().containsKey(SAVING_BUNDLE_KEY)) {
			Object sessionObj = getSession().get(SAVING_BUNDLE_KEY);
			if ( sessionObj instanceof String ) {
				return (String) getSession().get(SAVING_BUNDLE_KEY);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected void storeSavingBundleInSession( String savingBundle) {
		if ( savingBundle != null ) {
			removeSavingBundleFromSession();
			getSession().put(SAVING_BUNDLE_KEY, savingBundle );
		}
	}
	
	protected void removeSavingBundleFromSession() {
		if ( getSession().containsKey(SAVING_BUNDLE_KEY)) {
			getSession().remove(SAVING_BUNDLE_KEY);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String,OrderSearchCriteriaWrapper> getWrappedSearchCriteriaMapsInSession() {
		if ( getSession().containsKey(WRAPPED_CRITERIA_MAP_KEY)) {
			Object sessionObj = getSession().get(WRAPPED_CRITERIA_MAP_KEY);
			if ( sessionObj instanceof Map ) {
				return (Map<String,OrderSearchCriteriaWrapper>)getSession().get(WRAPPED_CRITERIA_MAP_KEY);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected void storeWrappedSearchCriteriaMapsInSession( Map<String,OrderSearchCriteriaWrapper> wrappedSearchCriteriaMaps) {
		if ( wrappedSearchCriteriaMaps != null ) {
			removeWrappedSearchCriteriaMapsFromSession();
			getSession().put(WRAPPED_CRITERIA_MAP_KEY, wrappedSearchCriteriaMaps );
		}
	}
	
	protected void removeWrappedSearchCriteriaMapsFromSession() {
		if ( getSession().containsKey(WRAPPED_CRITERIA_MAP_KEY)) {
			getSession().remove(WRAPPED_CRITERIA_MAP_KEY);
		}
	}
	
	public String getSelectedBundleNumber() {
		return selectedBundleNumber;
	}

	public void setSelectedBundleNumber(String selectedBundleNumber) {
		this.selectedBundleNumber = selectedBundleNumber;
	}

	public String getSelectedDetailNumber() {
		return selectedDetailNumber;
	}

	public void setSelectedDetailNumber(String selectedDetailNumber) {
		this.selectedDetailNumber = selectedDetailNumber;
	}
	private Date startSearchTime;
	private Date endSearchTime;
	private double elapsedSeconds;
	private Date startRenderTime;
	private Date endRenderTime;
	
	public Date getStartSearchTime() {
		return startSearchTime;
	}
	public void setStartSearchTime(Date startSearchTime) {
		this.startSearchTime = startSearchTime;
	}
	public Date getEndSearchTime() {
		return endSearchTime;
	}
	public void setEndSearchTime(Date endSearchTime) {
		this.endSearchTime = endSearchTime;
	}

	public String getSearchTime() {
	     StringBuffer buf = new StringBuffer();
	     //buf.append("DB: ");
	     //buf.append("Start: ").append(getStartSearchTime().toString()).append(" ");
	     //buf.append("End: ").append(getEndSearchTime().toString()).append(" ");
	     Double hrs = (getElapsedSeconds() - (getElapsedSeconds() % 3600 ))/ 3600;
	     Double mins = (getElapsedSeconds() - ((getElapsedSeconds()- (hrs * 3600 ) ) % 60 ))/ 60;
	     Double secs = getElapsedSeconds() - (hrs * 3600 ) - (mins * 60 ) ;
	     buf.append(" (DB: ").append(getElapsedSeconds());
	     buf.append(" Sec").append((getElapsedSeconds()==1?"":"s"));
	     buf.append(" = ");
	     buf.append(hrs.intValue()).append(" Hr").append((hrs==1?"":"s")).append(", ");
	     buf.append(mins.intValue()).append(" Min").append((mins==1?"":"s")).append(", ");
	     buf.append(secs.intValue()).append(" Sec").append((secs==1?"":"s"));
	     buf.append(")");
	     return buf.toString();
	}
	public double getElapsedSeconds() {
		return elapsedSeconds;
	}
	public void setElapsedSeconds(double elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}
	public String getRenderTime() {
		setEndRenderTime(MiscUtils.getNow());
		long beginTime = getStartRenderTime().getTime(); //start time
		long endTime = getEndRenderTime().getTime(); //end time
		Long renderSeconds = (endTime - beginTime) / 1000;
	     StringBuffer buf = new StringBuffer();
	     //buf.append("Render: ");
	     //buf.append("Start: ").append(getStartRenderTime().toString()).append(" ");
	     //buf.append("End:   ").append(getEndRenderTime().toString()).append(" ");
	     Double hrs = (renderSeconds.doubleValue() - (renderSeconds.doubleValue() % 3600 ))/ 3600;
	     Double mins = (renderSeconds  - ((renderSeconds - (hrs * 3600 )) % 60 )) / 60;
	     Double secs = renderSeconds - (hrs * 3600 ) - (mins * 60 ) ;
	     buf.append(" (Render: ").append(renderSeconds).append(" ");
	     buf.append(" Sec").append((renderSeconds==1?"":"s"));
	     buf.append("= ");
	     buf.append(hrs.intValue()).append(" Hr").append((hrs==1?"":"s")).append(", ");
	     buf.append(mins.intValue()).append(" Min").append((mins==1?"":"s")).append(", ");
	     buf.append(secs.intValue()).append(" Sec").append((secs==1?"":"s"));
	     buf.append(")");
	     return buf.toString();
	}

	public Date getEndRenderTime() {
		return endRenderTime;
	}

	public void setEndRenderTime(Date endRenderTime) {
		this.endRenderTime = endRenderTime;
	}

	public Date getStartRenderTime() {
		return startRenderTime;
	}

	public void setStartRenderTime(Date startRenderTime) {
		this.startRenderTime = startRenderTime;
	}

	public boolean isShowDbTimings() {
		return showDbTimings;
	}

	public void setShowDbTimings(boolean showDbTimings) {
		this.showDbTimings = showDbTimings;
	}

	public boolean isShowRenderTimings() {
		return showRenderTimings;
	}

	public void setShowRenderTimings(boolean showRenderTimings) {
		this.showRenderTimings = showRenderTimings;
	}

	public int getDetailPagingThreshold() {
		return detailPagingThreshold;
	}

	public void setDetailPagingThreshold(int detailPagingThreshold) {
		this.detailPagingThreshold = detailPagingThreshold;
	}
	
	public static SearchServices getWrSearchSvc() {
		return ServiceLocator.getWorksRemoteSearchService();

	}

	public String getSelectedInvoiceNumber() {
		return selectedInvoiceNumber;
	}

	public void setSelectedInvoiceNumber(String selectedInvoiceNumber) {
		this.selectedInvoiceNumber = selectedInvoiceNumber;
	}

	public String getSelectedOrderId() {
		return selectedOrderId;
	}

	public void setSelectedOrderId(String selectedOrderId) {
		this.selectedOrderId = selectedOrderId;
	}

	public String getSelectedBundleId() {
		return selectedBundleId;
	}

	public void setSelectedBundleId(String selectedBundleId) {
		this.selectedBundleId = selectedBundleId;
	}

	public String getSavingConfirmation() {
		return savingConfirmation;
	}

	public void setSavingConfirmation(String savingConfirmation) {
		this.savingConfirmation = savingConfirmation;
	}

	public String getSavingBundle() {
		return savingBundle;
	}

	public void setSavingBundle(String savingBundle) {
		this.savingBundle = savingBundle;
	}

	public String getSavingInvoice() {
		return savingInvoice;
	}

	public void setSavingInvoice(String savingInvoice) {
		this.savingInvoice = savingInvoice;
	}
	
	public boolean isHasEnterAdjPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT));
	}

	public boolean isHasCommitAdjPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT));
	}

	public boolean isHasViewReportsPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.VIEW_ENTERPRISE_REPORTS));
	}

	public boolean isHasAutoDunningPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.AUTO_DUNNING));
	}

	public boolean isHasEmulatePrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.EMULATE_USER));
	}

	public boolean isHasManageRolesPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.MANAGE_ROLES));
	}

	public boolean isHasManagePubInfoPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.MANAGE_PUBLISHERINFO));
	}

	public boolean isHasOrderSearchPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.SEARCH_ORDERS));
	}

	public boolean isHasManageOrdersPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.MANAGE_ORDERS));
	}	
	
	public boolean isHasManageUserInfoPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.MANAGE_USERINFO));
	}
	
	public boolean isHasViewUserInfoPrivelege() {
		return (UserContextService.hasPrivilege(CCPrivilegeCode.VIEW_USERINFO));
	}

	public boolean isResetToPage1() {
		return resetToPage1;
	}

	public void setResetToPage1(boolean resetToPage1) {
		this.resetToPage1 = resetToPage1;
	}

	public boolean isSelectedItemStoredInOMS() {
		return selectedItemStoredInOMS;
	}

	public void setSelectedItemStoredInOMS(boolean selectedItemStoredInOMS) {
		this.selectedItemStoredInOMS = selectedItemStoredInOMS;
	}
	public String getCurrentQuickTab() {
		return currentQuickTab;
	}

	public void setCurrentQuickTab(String currentQuickTab) {
		this.currentQuickTab = currentQuickTab;
	}
	public boolean isUpdatePricingOnly() {
		return updatePricingOnly;
	}
	public void setUpdatePricingOnly(boolean updPricingOnly) {
		this.updatePricingOnly = updPricingOnly;
	}
	
	public static final String USE_WRAPPED_RESULTS_FROM_SESSION = "USE_WRAPPED_RESULTS_FROM_SESSION_KEY";
	
	protected boolean isUseWrappedResultsFromSession() {
		if ( getSession().containsKey(USE_WRAPPED_RESULTS_FROM_SESSION)) {
			Object sessionObj = getSession().get(USE_WRAPPED_RESULTS_FROM_SESSION);
			if ( sessionObj instanceof Boolean ) {
				Boolean useWrappedResultsBoolean = (Boolean) sessionObj;
				return useWrappedResultsBoolean.booleanValue();
			}
		}
		
		return false;
	}

	protected void removeUseWrappedResultsFromSession( ) {
		if ( getSession().containsKey(USE_WRAPPED_RESULTS_FROM_SESSION)) {
			getSession().remove(USE_WRAPPED_RESULTS_FROM_SESSION);
		}
	}
	@SuppressWarnings("unchecked")
	protected void storeUseWrappedResultsFromSession( Boolean useWrappedResultsFromSession) {
		if ( useWrappedResultsFromSession != null ) {
			removeUseWrappedResultsFromSession();
			getSession().put(USE_WRAPPED_RESULTS_FROM_SESSION, useWrappedResultsFromSession );
		}
	}
	
	protected void removePartnerIDCodeTableFromSession( ) {
		if ( getSession().containsKey(PARTNER_ID_CODE_MAP_KEY)) {
			getSession().remove(PARTNER_ID_CODE_MAP_KEY);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String,String> getPartnerIDCodeTableInSession() {
		if ( getSession().containsKey(PARTNER_ID_CODE_MAP_KEY)) {
			Object sessionObj = getSession().get(PARTNER_ID_CODE_MAP_KEY);
			if ( sessionObj instanceof Map<?,?> ) {
				return (Map<String,String>)getSession().get(PARTNER_ID_CODE_MAP_KEY);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected void storePartnerIDCodeTableInSession( Map<String,String> partnerIdCodeTable) {
		if ( partnerIdCodeTable != null ) {
			removePartnerIDCodeTableFromSession();
			getSession().put(PARTNER_ID_CODE_MAP_KEY, partnerIdCodeTable );
		}
	}


}
