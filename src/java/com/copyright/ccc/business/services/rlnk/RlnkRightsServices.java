package com.copyright.ccc.business.services.rlnk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.rlnk.RlnkConstants.RlnkParmConstants;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.domain.data.WorkExternal;
import com.copyright.domain.data.WorkTagExternal;
import com.copyright.rightslink.base.RightElementNames;
import com.copyright.rightslink.base.UiOverrideEnum;
import com.copyright.rightslink.base.data.CartResponse;
import com.copyright.rightslink.base.data.OrderResponse;
import com.copyright.rightslink.base.data.PriceData;
import com.copyright.rightslink.base.data.RepriceResponse;
import com.copyright.rightslink.base.data.RightRequest;
import com.copyright.rightslink.base.data.RightResponse;
import com.copyright.rightslink.base.data.RlInitOrderFlow;
import com.copyright.rightslink.base.data.ServiceResponse;
import com.copyright.rightslink.base.data.UiOverrideRequest;
import com.copyright.rightslink.base.uifield.exception.TypeCodeException;
import com.copyright.rightslink.base.util.CaseInsensitiveStringMap;
import com.copyright.rightslink.html.HtmlScreenBuilder;
import com.copyright.rightslink.html.data.HtmlScreenRequest;
import com.copyright.rightslink.html.data.HtmlScreenResponse;
import com.copyright.svc.rightsResolver.api.data.BootstrapRequest;
import com.copyright.svc.rightsResolver.api.data.ItemRequest;
import com.copyright.svc.rightsResolver.api.data.ItemRequestParm;
import com.copyright.svc.rightsResolver.api.data.ItemRequestResponse;
import com.copyright.svc.rightsResolver.api.data.OrderRequest;
import com.copyright.svc.rightsResolver.api.data.RightsResolverConsumerContext;
import com.copyright.svc.rightsResolver.api.data.processor.InitOrderFlow;

/**
 * Copyright.com service layer that fronts the Rights Resolver Rights service.
 */
public class RlnkRightsServices 
{
	private static final Logger LOGGER = Logger.getLogger( RlnkRightsServices.class );
	private static final boolean PREFETCH_LINKS_ENABLED = CC2Configuration.getInstance().isRightslinkLinkPrefetchEnabled();
	private static final String OVERRIDE_XML = "CcomOverrides.xml";
	private static final String OVERRIDE_KEY = "CcomOverrides";
	
	/**
	 * returns true if the testDateAsLong argument can be converted to a date and 
	 * falls between fromDate and toDate inclusively.
	 * testDateAsLong can be 4 digits (interpreted as yyyy), 6 digits (yyyyMM),
	 * or 8 digits (yyyyMMdd).
	 * If the fromDate is before Jan. 1st, 1001 and the toDAte is after Jan. 1st, 2999,
	 * then true is returned, regardless of the value of the testDateAsLong.
	 */
	private static boolean between(Date fromDate, Date toDate, Long testDateAsLong) {
		Date testDate = null;
		if (fromDate==null || toDate==null) {
			return false;
		}
		SimpleDateFormat dateFormat8 = new SimpleDateFormat("yyyyMMdd");
		/*
		 * if beginning of time to end of time testDate value doesn't matter,
		 * always true
		 */
		Date bot = null;
		Date eot = null;
		try {
			bot = dateFormat8.parse("10010101");
			eot = dateFormat8.parse("29990101");
		} catch (ParseException e) {
			throw new CCCRuntimeException("can't parse date",e);
		}
		if (fromDate.before(bot) && toDate.after(eot)) {
			return true;
		}

		String testDateString = String.valueOf(testDateAsLong);
		SimpleDateFormat dateFormat = null;
		if (testDateString.length()==8) {
			//we're good, we want an 8 digit date
			dateFormat = dateFormat8;
		} else if (testDateString.length()==6) {
			dateFormat = new SimpleDateFormat("yyyyMM");
		} else if (testDateString.length()==4) {
			dateFormat = new SimpleDateFormat("yyyy");
		} else {
			/*
			 * unrecognized date format, return false
			 */
			return false;
		}
		try {
			testDate = dateFormat.parse(testDateString);
		} catch (ParseException pe) {
			throw new CCCRuntimeException("can't parse date " + testDateString,pe);
		}
		/*
		 * testDate within the range?
		 */
		if ( (fromDate.before(testDate)||fromDate.equals(testDate)) 
				&& (toDate.after(testDate) || toDate.equals(testDate)) ) {
			return true;
		}
		return false;
	}

	public static boolean prefetchLinks(Long selectedTouId, WorkExternal work, List<WorkExternal> subWorks, Integer selectedPubYear) {
		boolean prefetchOkay = CC2Configuration.getInstance().isRightslinkLinkPrefetchEnabled();
		if (!prefetchOkay) {
			return false;
		}
		List<InitOrderFlow> initOrderFlows = new ArrayList<InitOrderFlow>();
		WorkTagExternal tag = identifyLinkBuilderTag(work,selectedPubYear);
		if (tag==null || StringUtils.isEmpty(tag.getTagName())) {
			throw new CCCRuntimeException("unable to determine which link-builder tag to use for work with wrWrkInst " + work.getPrimaryKey());						
		}	
		if (subWorks==null || subWorks.size()==0) {
			InitOrderFlow initOrderFlow = new InitOrderFlow();
			initOrderFlow.setLinkPrefetchingEnabled(true);
			initOrderFlow.setLinkingRuleId(tag.getTagName());
			initOrderFlow.setTypeOfUse(selectedTouId.toString());
			initOrderFlow.setWorkExternal(work);
			initOrderFlows.add(initOrderFlow);
		} else {
			for(WorkExternal subWork: subWorks) {
				InitOrderFlow initOrderFlow = new InitOrderFlow();
				initOrderFlow.setLinkPrefetchingEnabled(true);
				initOrderFlow.setLinkingRuleId(tag.getTagName());
				initOrderFlow.setTypeOfUse(selectedTouId.toString());
				initOrderFlow.setWorkExternal(subWork);
				initOrderFlows.add(initOrderFlow);
			}
		}
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();

		return ServiceLocator.getRightsProcessorService().prefetchRLLinks( ctx, initOrderFlows );			
	}

	public static boolean prefetchLink(Long selectedTouId, WorkExternal work, WorkExternal subWork, Integer selectedPubYear) {
		if ( ! CC2Configuration.getInstance().isRightslinkLinkPrefetchEnabled()) {
			return false;
		}
		WorkTagExternal tag = identifyLinkBuilderTag(work,selectedPubYear);
		if (tag==null || StringUtils.isEmpty(tag.getTagName())) {
			throw new CCCRuntimeException("unable to determine which link-builder tag to use for work with wrWrkInst " + work.getPrimaryKey());						
		}	
		InitOrderFlow initOrderFlow = new InitOrderFlow();
		initOrderFlow.setLinkPrefetchingEnabled(CC2Configuration.getInstance().isRightslinkLinkPrefetchEnabled());
		initOrderFlow.setLinkingRuleId(tag.getTagName());
		initOrderFlow.setTypeOfUse(selectedTouId.toString());
		if(subWork!=null){
			initOrderFlow.setWorkExternal(subWork);
		}else{
			initOrderFlow.setWorkExternal(work);
		}
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		return ServiceLocator.getRightsProcessorService().prefetchRLLink( ctx, initOrderFlow );			
	}

	/*
	 * identifies the correct link builder tag (rule) to use when calling 
	 * svc-RR. Returns null if it can't determine which one to use.
	 */
	private static WorkTagExternal identifyLinkBuilderTag(WorkExternal work, Integer selectedPubYear) {
		if (work==null) {
			throw new IllegalArgumentException("a non-null WorkExternal is required");
		}

		List<WorkTagExternal> lbTags = work.getLbTagList();

		long currentTime = System.currentTimeMillis();
		
		if (lbTags != null) {
			for(WorkTagExternal tag : lbTags)
			{
				if (LOGGER.isDebugEnabled()) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					LOGGER.debug("examining LB tag " + tag.getTagName() + 
						" vld-dates: " + 
						sdf.format(tag.getTagValidStart()) + 
						"-" + 
						sdf.format(tag.getTagValidEnd()) +
						" bi-act-dates: " + 
						sdf.format(tag.getBiactiveStart()) + "-" + 
						sdf.format(tag.getBiactiveEnd())
					);
				}
				if ((tag.getTagValidEnd().getTime() >= currentTime) &&
						(tag.getTagValidStart().getTime() <= currentTime))
				{
				/*
				 * compare the pub year for which the user wanted rights with biactive start/end dates
				 * on the tag. If the user supplied selectedPubYear is between the biactive dates on
				 * the tag, then it's the correct linkbuilder tag.
				 */

					Long pubStartDate = null;
					if (selectedPubYear==null) {
						pubStartDate=Long.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					} else {
						pubStartDate=Long.valueOf(selectedPubYear);
					}
					if (between(tag.getBiactiveStart(),tag.getBiactiveEnd(),pubStartDate)) {
						return tag;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Used for an initial pricing request.
	 */
	public static RightResponse runRightRequest(  String formName, WorkExternal work, WorkExternal subWork, RlInitOrderFlow rlInitOrderFlow, Integer selectedPubYear )
	{
		rlInitOrderFlow.setChannel( RlnkConstants.CHANNEL );

		InitOrderFlow initOrderFlow = new InitOrderFlow();
		initOrderFlow.setLinkPrefetchingEnabled(PREFETCH_LINKS_ENABLED);
		initOrderFlow.setRlInitOrderFlow(rlInitOrderFlow);
		initOrderFlow.setTypeOfUse(rlInitOrderFlow.getRlOfferID());
		
		//added for pub to pub
		initOrderFlow.setSelectedOfferChannel(rlInitOrderFlow.getSelectedOfferChannel());
		initOrderFlow.setSelectedRlPermissionType(rlInitOrderFlow.getSelectedRlPermissionType());
		initOrderFlow.setSelectedRlPubCode(rlInitOrderFlow.getSelectedRlPubCode());
		
		
		
		if(subWork!=null){
			initOrderFlow.setWorkExternal(subWork);
		}else{
			initOrderFlow.setWorkExternal(work);
		}

		WorkTagExternal tag = identifyLinkBuilderTag(work,selectedPubYear);
		if (tag==null || StringUtils.isEmpty(tag.getTagName())) {
			throw new CCCRuntimeException("unable to determine which link-builder tag to use for work with wrWrkInst " + initOrderFlow.getWorkExternal().getPrimaryKey());						
		}
		initOrderFlow.setLinkingRuleId(tag.getTagName());

		RightRequest rightRequest = new RightRequest( new HashMap<String,String>() );
		rightRequest.setFormName(formName);
		rightRequest.setSessionID(getRlnkSessionID());
		configureRightRequest( rightRequest );

		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		return ServiceLocator.getRightsProcessorService().runRightRequest( ctx, rightRequest, initOrderFlow );  
	}

	/**
	 * Used to continue the pricing request workflow (requests 2 through N).
	 */
	public static RightResponse runRightRequest( String formName, Map<String,String> parameters )
	{
		RightRequest rightRequest = new RightRequest( parameters );
		rightRequest.setSessionID( getRlnkSessionID() );
		rightRequest.setFormName(formName);
		configureRightRequest( rightRequest );

		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		return ServiceLocator.getRightsProcessorService().runRightRequest(ctx, rightRequest);
	}

	/**
	 * Get the ItemRequest object for carting.
	 * @param sessionId
	 * @param formName
	 * @param parameters
	 * @param itemRequest
	 */
	public static ItemRequestResponse getItemRequest( String formName, Map<String,String> parameters, ItemRequest itemRequest  )
	{
		RightRequest rightRequest = new RightRequest( parameters );
		rightRequest.setSessionID( getRlnkSessionID() );
		rightRequest.setFormName(formName);
		configureRightRequestForGetItem( rightRequest );

		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		ItemRequestResponse itemRequestResponse = ServiceLocator.getRightsProcessorService().getItemRequest(ctx, rightRequest, itemRequest);                
		if (!itemRequestResponse.hasErrors())
		{
			itemRequest = itemRequestResponse.getItemRequest();
			itemRequest.setRlDetailHtml(getDetailHtml(itemRequestResponse));
		}
		return itemRequestResponse;
	}

	/**
	 * Get the ItemRequest object for session.
	 * @param itemRequest
	 */
	public static ItemRequestResponse getItemRequestFromSession(ItemRequest itemRequest )
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		RightRequest rightRequest = new RightRequest(new CaseInsensitiveStringMap<String>());
		rightRequest.setSessionID( getRlnkSessionID() );
		configureRightRequestForGetItem( rightRequest );
		ItemRequestParm itemRequestParm = itemRequest.getItemRequestParms().get(RlnkParmConstants.PARM_NAME_TARGET_PAGE.toUpperCase());
		itemRequestParm.setParmValueString(RlnkParmConstants.PARM_VALUE_CONFIRM_ORDER);
		ItemRequestResponse itemRequestResponse = ServiceLocator.getRightsProcessorService().getItemRequestFromSession(ctx, rightRequest, itemRequest);
		if (!itemRequestResponse.hasErrors())
		{
			itemRequest = itemRequestResponse.getItemRequest();
			itemRequest.setRlDetailHtml(getDetailHtml(itemRequestResponse));
		}
		return itemRequestResponse;
	}

	public static OrderResponse createOrder(OrderRequest orderRequest)
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		return ServiceLocator.getRightsProcessorService().createOrder(ctx, orderRequest);               

	}

	public static RepriceResponse repriceOrder(BootstrapRequest bootstrapRequest)
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		UiOverrideRequest uiOverrideRequest = getUiOverrideRequest(UiOverrideEnum.QUICKPRICE);
		bootstrapRequest.setUiOverrideRequest(uiOverrideRequest);
		return ServiceLocator.getRightsProcessorService().repriceOrder(ctx, bootstrapRequest);              
	}

	public static RightResponse editOrder(BootstrapRequest bootstrapRequest)
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		UiOverrideRequest uiOverrideRequest = getUiOverrideRequest(UiOverrideEnum.QUICKPRICE);
		bootstrapRequest.setUiOverrideRequest(uiOverrideRequest);
		ItemRequest itemRequest = bootstrapRequest.getItemRequest();
		ItemRequestParm itemRequestParm = itemRequest.getItemRequestParms().get(RlnkParmConstants.PARM_NAME_TARGET_PAGE.toUpperCase());
		itemRequestParm.setParmValueString(RlnkParmConstants.PARM_VALUE_QUICKPRICE);
		return ServiceLocator.getRightsProcessorService().editOrder(ctx, bootstrapRequest);             
	}

	public static RightResponse getItemDetailDisplay(BootstrapRequest bootstrapRequest)
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		UiOverrideRequest uiOverrideRequest = getUiOverrideRequest(UiOverrideEnum.ITEM_DETAILS);
		bootstrapRequest.setUiOverrideRequest(uiOverrideRequest);
		ItemRequest itemRequest = bootstrapRequest.getItemRequest();
		ItemRequestParm itemRequestParm = itemRequest.getItemRequestParms().get(RlnkParmConstants.PARM_NAME_TARGET_PAGE.toUpperCase());
		itemRequestParm.setParmValueString(RlnkParmConstants.PARM_VALUE_CONFIRM_ORDER);
		return ServiceLocator.getRightsProcessorService().getSpecificPage(ctx, bootstrapRequest);               
	}

	public static CartResponse addCartItem(Long cartItemID, ItemRequest itemRequest)
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		return ServiceLocator.getRightsProcessorService().addCartItem(ctx, cartItemID, itemRequest);                
	}

	public static CartResponse updateCartItem(Long cartItemID, ItemRequest itemRequest)
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		return ServiceLocator.getRightsProcessorService().updateCartItem(ctx, cartItemID, itemRequest);             
	}

	public static ServiceResponse deleteCartItem(Long cartItemID)
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		return ServiceLocator.getRightsProcessorService().deleteCartItem(ctx, cartItemID);              
	}

	/**
	 * logout.
	 * @param sessionId
	 * @return boolean
	 */
	 public static boolean logout()
	{
		RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		boolean response =false;
		try{
			response = ServiceLocator.getRightsProcessorService().logout(ctx, getRlnkSessionID());  
		}catch(Exception e){
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
		}

		return response;
	}

	 /**
	  * removeSession.
	  * @param sessionId
	  * @return boolean
	  */
	 public static boolean removeSession( String sessionId )
	 {
		 RightsResolverConsumerContext ctx = new RightsResolverConsumerContext();
		 boolean response =false;
		 try{
			 response = ServiceLocator.getRightsProcessorService().removeSession(ctx, sessionId);   
		 }catch(Exception e){
			 LOGGER.error(ExceptionUtils.getFullStackTrace(e));
		 }

		 return response;
	 }

	private static void configureRightRequest( RightRequest rightRequest )
	{
		Map<String, String> requestParameters = rightRequest.getRequestParameters();
		String targetPage = null;
		UiOverrideRequest uiOverrideRequest = null;
		if (requestParameters != null)
		{
			targetPage = requestParameters.get(RlnkParmConstants.PARM_NAME_TARGET_PAGE);//  has to be targetPage not TARGETPAGE
			if ((targetPage == null) || (RightElementNames.QUICKPRICE.equalsIgnoreCase(targetPage)))
				uiOverrideRequest = getUiOverrideRequest(UiOverrideEnum.QUICKPRICE);
			else
				uiOverrideRequest = getUiOverrideRequest(UiOverrideEnum.OTHER);
		}
		else
			uiOverrideRequest = getUiOverrideRequest(UiOverrideEnum.QUICKPRICE);

		rightRequest.setUiOverrideRequest(uiOverrideRequest);
	}

	 private static void configureRightRequestForGetItem( RightRequest rightRequest )
	 {
		UiOverrideRequest uiOverrideRequest = 
			getUiOverrideRequest(UiOverrideEnum.ITEM_DETAILS);
		rightRequest.setUiOverrideRequest(uiOverrideRequest);
	 }   
	 
	 private static UiOverrideRequest getUiOverrideRequest(UiOverrideEnum uiOverrideType)
	 {
		UiOverrideRequest uiOverrideRequest = new UiOverrideRequest();
		uiOverrideRequest.setMemoryPersistenceKey(OVERRIDE_KEY);
		uiOverrideRequest.setXmlFileName(OVERRIDE_XML);
		uiOverrideRequest.setUiOverrideType(uiOverrideType);
		return uiOverrideRequest;
	 }

	 /**
	  * View Hide Rights Links details
	  * @param itemRequest
	  * @return
	  */
	 private static String getDetailHtml(ItemRequestResponse itemRequestResponse){
		 String displayDetailHTML="";
		 HtmlScreenRequest htmlScreenRequest = new HtmlScreenRequest();
		 htmlScreenRequest.setRows(itemRequestResponse.getScreenRows());
		 HtmlScreenBuilder htmlScreenBuilder = new HtmlScreenBuilder();
		 HtmlScreenResponse htmlScreenResponse = null;
		 try {
			 htmlScreenResponse = htmlScreenBuilder.execute(htmlScreenRequest);
			 if (htmlScreenResponse != null && htmlScreenResponse.getScreenHtml()!=null) {
				 displayDetailHTML = htmlScreenResponse.getScreenHtml();
				 return displayDetailHTML;
			 }

		 } catch (TypeCodeException e) {
			 LOGGER.error(ExceptionUtils.getFullStackTrace(e));
		 }
		 return displayDetailHTML;
	 }
	 public static void addLoginInfo(Map<String, String> requestParameters){
		 if(UserContextService.isUserAuthenticated()){
			 requestParameters.put(
					 RlnkParmConstants.PARM_NAME_LOGINID,
					 String.valueOf(UserContextService.getUserContext().getActiveUser().getUsername()));

			 requestParameters.put(
					 RlnkParmConstants.PARM_NAME_PASSWORD, 
					 CC2Configuration.getInstance().getRightslinkAuthenticationToken());

			 requestParameters.put(
					 RlnkParmConstants.PARM_NAME_TARGET_PAGE,
					 RlnkParmConstants.PARM_VALUE_AUTH_TRUSTED);
		 }

	 }
	 public static String getRlnkSessionID(){
		 return UserContextService.getRlnkSessionID();
	 }
	 public static boolean isPriceDifferent(PriceData priceData){
		 return PricingServices.isPriceDifferent(priceData.getTotalPrice(), priceData.getOriginalPrice());	 
		 
	 }
	 
	 
}
