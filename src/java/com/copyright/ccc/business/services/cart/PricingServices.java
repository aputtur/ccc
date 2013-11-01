package com.copyright.ccc.business.services.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.base.Constants;
import com.copyright.base.enums.ProductEnum;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.data.WRStandardWork;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ItemHelperServices;
import com.copyright.ccc.business.services.ItemParmEnum;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.order.OrderHeaderImpl;
import com.copyright.ccc.business.services.order.OrderItemImpl;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.ValidationException;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.order.UsageData;
import com.copyright.data.order.UsageDataAcademic;
import com.copyright.data.order.UsageDataEmail;
import com.copyright.data.order.UsageDataNet;
import com.copyright.data.order.UsageDataPhotocopy;
import com.copyright.data.order.UsageDataRepublication;
import com.copyright.domain.data.WorkExternal;
import com.copyright.persist.pricing.LimitsExceededExceptionFactory;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemDescriptionParm;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemParm;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.ItemStatusQualifierEnum;
import com.copyright.svc.order.api.data.SpecialOrderTypeEnum;
import com.copyright.svc.rights.api.data.RightsConsumerContext;
import com.copyright.svc.rightsResolver.api.data.CalcFeeReturnEnum;
import com.copyright.svc.rightsResolver.api.data.DataTypeEnum;
import com.copyright.svc.rightsResolver.api.data.ItemRequest;
import com.copyright.svc.rightsResolver.api.data.ItemRequestDescriptionParm;
import com.copyright.svc.rightsResolver.api.data.ItemRequestFees;
import com.copyright.svc.rightsResolver.api.data.ItemRequestParm;
import com.copyright.svc.rightsResolver.api.data.RightsResolverConsumerContext;
import com.copyright.svc.rightsResolver.api.data.RlSourceBeanEnum;
import com.copyright.svc.telesales.api.data.RightsholderOrganization;
import com.copyright.workbench.i18n.Money;



/**
 * Set of static methods to calculate price of <code>PurchasablePermission</code> instances.
 */
public final class PricingServices {

    private static final Logger LOGGER = Logger.getLogger( PricingServices.class );
	
  private PricingServices(){}
	
	/**
   * Calculates and updates the price of a <code>PurchasablePermission</code> for a specified licensee.
   * <p>
   * The price of a given item is affected by the licensee as well as the usual quantities set on the 
   * <code>PermissionRequest</code>.  This method exposes the 'licensee specific' PricingService call.
   * <code>updateItemPrice( PurchasablePermission purchasablePermission )</code> should normally be
   * utilized.  Refer to the PricingServiceAPI documentation for details.
   * 
   * @see com.copyright.service.pricing.PricingServiceAPI#calcQuickPrice(com.copyright.data.order.PermissionRequest, java.lang.Long)
   *  
   * @param purchasablePermission <code>PurchasablePermission</code> instance to calculate price for.
   * @param licenseePartyID <code>Long</code> value representing a licensee party ID 
   * 
   * @return <code>PurchasablePermission</code> instance with updated price.
   * 
   * @throws InvalidAttributesException
   * @throws DeniedLimitsExceededException
   * @throws SpecialOrderLimitsExceededException
   * @throws ContactRHDirectlyLimitsExceededException
   * @throws SystemLimitsExceededException
   */
  public static PurchasablePermission updateItemPriceForLicenseeID( PurchasablePermission purchasablePermission, Long licenseePartyID )
//	throws InvalidAttributesException, 
//	       DeniedLimitsExceededException,
//	       SpecialOrderLimitsExceededException, 
//	       ContactRHDirectlyLimitsExceededException, 
//	       SystemLimitsExceededException 
  	throws ServiceInvocationException
  {    
 	  
    if( purchasablePermission == null )
	  {
      LOGGER.error("No PurchasablePermission found");
      
	    throw new IllegalArgumentException("PurchasablePermission cannot be null");
	  }
  
      purchasablePermission = calcPriceAndUpdate( purchasablePermission, licenseePartyID );    
      return purchasablePermission;      
  }

  /**
	 * Calculates and updates the price of a <code>PurchasablePermission</code> instance.
   * 
	 * @param purchasablePermission <code>PurchasablePermission</code> instance to calculate price for.
	 */
	public static PurchasablePermission updateItemPrice( PurchasablePermission purchasablePermission )
//	throws InvalidAttributesException, 
//	       DeniedLimitsExceededException,
//	       SpecialOrderLimitsExceededException, 
//	       ContactRHDirectlyLimitsExceededException, 
	throws SystemLimitsExceededException, ServiceInvocationException
  {
	  
    if( purchasablePermission == null )
	  {
      LOGGER.error("No PurchasablePermission found");
      
	    throw new IllegalArgumentException("PurchasablePermission cannot be null");
	  }
    
    purchasablePermission = calcPriceAndUpdate( purchasablePermission, new Long(UserContextService.getActiveAppUser().getPartyID()));    
    return purchasablePermission;
	}

	  /**
	 * Calculates and updates the price of a <code>PurchasablePermission</code> instance.
   * 
	 * @param orderLicense <code>OrderLicense</code> instance to calculate price for.
	 */
	public static OrderLicense updateItemPrice( OrderLicense orderLicense )
//	throws InvalidAttributesException, 
//	       DeniedLimitsExceededException,
//	       SpecialOrderLimitsExceededException, 
//	       ContactRHDirectlyLimitsExceededException, 
//		   SystemLimitsExceededException, 
	throws ServiceInvocationException
  {
	  
    if( orderLicense == null )
	  {
      LOGGER.error("No OrderLicense found");
      
	    throw new IllegalArgumentException("OrderLicense cannot be null");
	  }
    
    orderLicense = calcPriceAndUpdate( orderLicense );    
    return orderLicense;
	}

	
	  /**
	   * Actual 'hook' into the RR PricingService.
	   */
	  private static PurchasablePermission calcPriceAndUpdate( PurchasablePermission purchasablePermission, Long partyIDToCalc )
//	  throws InvalidAttributesException, 
//	         DeniedLimitsExceededException,
//	         SpecialOrderLimitsExceededException, 
//	         ContactRHDirectlyLimitsExceededException, 
//	         SystemLimitsExceededException
	  throws ServiceInvocationException
	  {

	  RightsResolverConsumerContext rrConsumerContext = new RightsResolverConsumerContext(); 
//	  LOGGER.debug("calcPriceAndUpdate ");
//	  LOGGER.debug("isAcademic: " + purchasablePermission.isAcademic() + " Number of Students" + purchasablePermission.getNumberOfStudents());
	  
	  ItemRequest itemRequest = getItemRequestFromItem(purchasablePermission.getItem());
	  
	  // Supporting the generalization of some fields to "Circulation/Distribution"
	  boolean removeCirculationDistribution = false;
      if( purchasablePermission.isAcademic())
	  {
		  BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfStudents());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
		  removeCirculationDistribution = true;
	  }

	  if( purchasablePermission.isPhotocopy())
	  {
		  BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfCopies());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
		  removeCirculationDistribution = true;
	  }
	    
	  if(purchasablePermission.isEmail())
	  {
		  BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfRecipients());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
		  removeCirculationDistribution = true;
	  }
  
	  //set licensee party id
	  itemRequest.setLicenseePartyId(partyIDToCalc);   
	  
	  // This is the case where there is no TfWorkInst and pricing comes from WorkSet
	  PurchasablePermissionImpl purchasablePermissionImpl = (PurchasablePermissionImpl) purchasablePermission;
	  itemRequest.setWorkSetId(purchasablePermissionImpl.getTfWksInst());
	  
	  LOGGER.debug("Calling Pricing through RR");
	  LOGGER.debug("ItemRequest TF Work Id: " + itemRequest.getExternalItemId());
	  LOGGER.debug("ItemRequest TF Work Set Id: " + itemRequest.getWorkSetId());
	  LOGGER.debug("ItemRequest WR Work Id: " + itemRequest.getItemSourceKey());
	  LOGGER.debug("ItemRequest Right Id: " + itemRequest.getExternalRightId());
	  
	  //if (itemRequest.getItemSourceKey() != null && itemRequest.getItemSourceKey().compareTo(0L) > 0)
	  if (itemRequest.getExternalItemId() != null && itemRequest.getExternalItemId().compareTo(0L) > 0) 
	  {
		  try 
		  {
			  Boolean rePinRightFlag = true;
			  Boolean checkLimits = true;
			  
			  itemRequest  = ServiceLocator.getRightsResolverService().getCalculatedPriceForItemRequest(rrConsumerContext, itemRequest, rePinRightFlag, checkLimits);

			  if (itemRequest.getCalcFeeReturnCd().equals(CalcFeeReturnEnum.SYSTEM_LIMITS_EXCEEDED)) {
			      LOGGER.debug("System limits exceeded");
			      itemRequest.setCalculatedAvailabilityCd(ItemAvailabilityEnum.DENY.getStandardPermissionCd());
			  }
			  
			  if (removeCirculationDistribution) {
				  itemRequest.getItemRequestParms().remove(ItemParmEnum.CIRCULATION_DISTRIBUTION.name());
			  }
			  if (purchasablePermission.isEmail()) {
				  itemRequest.getItemRequestParms().remove(ItemParmEnum.DURATION.name());			  
				  itemRequest.getItemRequestParms().remove(ItemParmEnum.DURATION_CD.name());			  
			  }
			  if (purchasablePermission.isNet()) {
				  itemRequest.getItemRequestParms().remove(ItemParmEnum.NUM_RECIPIENTS.name());			  
			  }
	  
			  getItemFromItemRequest(purchasablePermission.getItem(), itemRequest);
			  
			  LOGGER.debug("Total Price: " + itemRequest.getTotalPrice());
//	  purchasablePermission.setSpecialOrderTypeCd(itemRequest.getSpecialOrderType().getCd());
	    	      
//	      purchasablePermission.setPermissionRequest( permissionRequest );
		  }	
		  catch ( Exception e )
		  {      
			  LOGGER.error("Service Invocation Exception: " + ExceptionUtils.getFullStackTrace(e));
			  throw new ServiceInvocationException();
		  }

	  
	  } else {  // Work not found
		  purchasablePermissionImpl.setSpecialOrderTypeCdToManualSpecialOrder();
		  purchasablePermissionImpl.setIsSpecialOrder(true);
		  purchasablePermissionImpl.setRightAvailabilityCd(null);
		  purchasablePermissionImpl.setItemAvailabilityCd(null);
		  purchasablePermissionImpl.setItemOrigAvailabilityCd(null);
	  }
	  
	  

	  LOGGER.debug("PurchasablePermission price updated successfully (" + purchasablePermission.getPrice() + ")");      
	  
	  return purchasablePermission;
	  
	  }

//	  public static ItemRequest calcPriceForAdjustment (OrderLicense orderLicense, AdjItem adjItem )
//	  {
//
//	  RightsResolverConsumerContext rrConsumerContext = new RightsResolverConsumerContext(); 
//
//	  OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
//	  //Item item = orderItemImpl.getItem();
//	  ItemRequest itemRequest = getItemRequestFromItem(orderItemImpl.getItem());
//	  AdjustmentItem adjustmentItem = adjItem.getAdjustmentItem();
//	  
//	  for (AdjustmentItemParm adjustmentItemParm : adjustmentItem.getAdjItemParms().values() ) {
//
//		  //TODO some parms need not be sent -
//		  //////////////////////////ACADAMIC
//		  if (orderLicense.isAcademic() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_PAGES.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemNumberOfPages()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.NUM_PAGES, stringValue, decimalValue, null,  itemRequest);
//		  }
//		  // COPIES is actually students //TODO remove unnecessary parms
//		  if (orderLicense.isAcademic() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_COPIES.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemNumberOfStudents()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.NUM_COPIES, stringValue, decimalValue, null,  itemRequest);
//		  }
//		  if (orderLicense.isAcademic() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_COPIES.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemNumberOfStudents()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
//
//		  }
//		  //ECCS need this.
//		  if (orderLicense.isAcademic() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_COPIES.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemNumberOfStudents()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.NUM_STUDENTS, stringValue, decimalValue, null,  itemRequest);
//
//		  }
//		  
//		  
//		  //////////////////////////PHOTOCOPY
//		  if (orderLicense.isPhotocopy() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_PAGES.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemNumberOfPages()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.NUM_PAGES, stringValue, decimalValue, null,  itemRequest);
//		  }
//		  if (orderLicense.isPhotocopy() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_COPIES.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemNumberOfCopies()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.NUM_COPIES, stringValue, decimalValue, null,  itemRequest);
//		  }
//		  if (orderLicense.isPhotocopy() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_COPIES.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemNumberOfCopies()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
//		  }
//		  
//		  //////////////////////////EMAIL
//		  if (orderLicense.isEmail() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_RECIPIENTS.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemRecipients()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.NUM_RECIPIENTS, stringValue, decimalValue, null,  itemRequest);
//		  }
//		  if (orderLicense.isEmail() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.NUM_RECIPIENTS.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemRecipients()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
//		  }
//		  
//		  //////////////////////////Republication
//		  if (orderLicense.isRepublication() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.CIRCULATION_DISTRIBUTION.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemCirculationDistribution()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
//		  }
//		  //TODO Only if RepublicationConstants.CONTENT_SELECTED_PAGES.equals(getOrderItemImpl().getTypeOfContent()))?
//		  if (orderLicense.isRepublication() && adjustmentItemParm.getParmName().equalsIgnoreCase(ItemParmEnum.RLS_PAGES.name())) {
//			  BigDecimal decimalValue = AdjHelper.add(AdjHelper.toBigDecimal(adjItem.getItemNumberOfRLSPages()), adjustmentItemParm.getParmValueNumeric());
//			  String stringValue = decimalValue.toString();
//			  updateItemRequestParm (ItemParmEnum.RLS_PAGES, stringValue, decimalValue, null,  itemRequest);
//		  }
//	  }
//	  // itemRequest.itemRequestParms has -ve values
//	  
//	  //set licensee party id
//	  itemRequest.setLicenseePartyId(orderLicense.getLicenseePartyId());   
//	  itemRequest.setPinningDtm(orderLicense.getPinningDate());
//	  
//	  try 
//	  {
//		  Boolean rePinRightFlag = true;
//		  Boolean checkLimits = true;
//		  
//		  itemRequest  = ServiceLocator.getRightsResolverService().getCalculatedPriceForItemRequest(rrConsumerContext, itemRequest, rePinRightFlag, checkLimits);
//	  }	
//	  catch ( Exception e )
//	  {      
//	      LOGGER.error("Pricing Request Exception " + LogUtil.appendableStack(e));
//	      itemRequest = null;
////	      throw new InvalidAttributesException( validationException, purchasablePermission );
//	  }
//	  
//	  if (itemRequest!=null) {
//		  LOGGER.debug("Total Price from new request: " + itemRequest.getTotalPrice());
//	  }
//	  
//	  return itemRequest;
//	  }
    
	  
	  private static OrderLicense calcPriceAndUpdate( OrderLicense orderLicense )
//	  throws InvalidAttributesException, 
//	         DeniedLimitsExceededException,
//	         SpecialOrderLimitsExceededException, 
//	         ContactRHDirectlyLimitsExceededException, 
//	         SystemLimitsExceededException
	  throws ServiceInvocationException
	  {

	  RightsResolverConsumerContext rrConsumerContext = new RightsResolverConsumerContext(); 
//	  LOGGER.debug("calcPriceAndUpdate ");
//	  LOGGER.debug("isAcademic: " + purchasablePermission.isAcademic() + " Number of Students" + purchasablePermission.getNumberOfStudents());
	      
	  OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
	  ItemRequest itemRequest = getItemRequestFromItem(orderItemImpl.getItem());

	  // Supporting the generalization of some fields to "Circulation/Distribution"
	  boolean removeCirculationDistribution = false;
      if( orderLicense.isAcademic())
	  {
		  BigDecimal decimalValue = new BigDecimal(orderLicense.getNumberOfStudents());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	      removeCirculationDistribution = true;
	  }

	  if( orderLicense.isPhotocopy())
	  {
		  BigDecimal decimalValue = new BigDecimal(orderLicense.getNumberOfCopies());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	      removeCirculationDistribution = true;
	  }
	    
	  if(orderLicense.isEmail())
	  {
		  BigDecimal decimalValue = new BigDecimal(orderLicense.getNumberOfRecipients());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	      removeCirculationDistribution = true;
	  }

	  
	  //set licensee party id
	  if (orderLicense.getOrderDataSource()==OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
		  try {
			OrderPurchase orderPurchase = OrderPurchaseServices.getCOIOrderPurchaseForOrderHeaderId(orderLicense.getOrderHeaderId());
			OrderHeaderImpl orderHeaderImpl = (OrderHeaderImpl) orderPurchase;
			itemRequest.setLicenseePartyId(orderHeaderImpl.getBuyerPartyId());
		  } catch (OrderPurchasesException e) {
			// This should never happen, if it does user will not see licensee specific pricing
			  LOGGER.warn(LogUtil.getStack(e));
				itemRequest.setLicenseePartyId(0L);
		}
	  } else {
		  itemRequest.setLicenseePartyId(orderLicense.getLicenseePartyId());   		  
	  }
	  
	    
	  //if (itemRequest.getItemSourceKey() != null && itemRequest.getItemSourceKey().compareTo(0L) > 0)
	  if (itemRequest.getExternalItemId() != null && itemRequest.getExternalItemId().compareTo(0L) > 0)
	  {
			  Boolean rePinRightFlag = true;
			  Boolean checkLimits = true;

			  Set<ItemFees> itemAllFeesCache = null;
			  
			  if (orderLicense.isAcademic() &&
				  !orderLicense.isUnresolvedSpecialOrder() &&
				  !orderLicense.isRequiresAcademicRepin()) {
					  rePinRightFlag = false;
					  checkLimits = false;
					  Item item = orderItemImpl.getItem();
					  itemAllFeesCache = item.getAllFees();
					  itemRequest.setCalculatedAvailabilityCd(orderLicense.getItemAvailabilityCd());
			  }
			  
			  try {
				  itemRequest  = ServiceLocator.getRightsResolverService().getCalculatedPriceForItemRequest(rrConsumerContext, itemRequest, rePinRightFlag, checkLimits);
			  } catch ( Exception e )
			  {      
				  LOGGER.error("Service Invocation Exception: " + ExceptionUtils.getFullStackTrace(e));
				  
				  ServiceInvocationException sie = new ServiceInvocationException(e);
				  
				  throw sie;
			  }

			  if (itemRequest.getCalcFeeReturnCd().equals(CalcFeeReturnEnum.SYSTEM_LIMITS_EXCEEDED)) {
			      LOGGER.debug("System limits exceeded");
			      itemRequest.setCalculatedAvailabilityCd(ItemAvailabilityEnum.DENY.getStandardPermissionCd());
			  } 
			  
//			  orderLicense.setSpecialOrderTypeCd(itemRequest.getSpecialOrderType().getCd());			  
			  orderItemImpl.setSpecialOrderTypeCd(itemRequest.getSpecialOrderType().name());			  
			  			  
			  if (removeCirculationDistribution) {
				  itemRequest.getItemRequestParms().remove(ItemParmEnum.CIRCULATION_DISTRIBUTION.name());
			  }
			  if (orderLicense.isEmail()) {
				  itemRequest.getItemRequestParms().remove(ItemParmEnum.DURATION.name());			  
				  itemRequest.getItemRequestParms().remove(ItemParmEnum.DURATION_CD.name());			  
			  }
			  if (orderLicense.isNet()) {
				  itemRequest.getItemRequestParms().remove(ItemParmEnum.NUM_RECIPIENTS.name());			  
			  }

			  LOGGER.debug("Total Price: " + itemRequest.getTotalPrice());

			  // Don't stomp on orig availabilty cd when re-pricing a license
			  String itemOrigAvailabiltyCd = orderItemImpl.getItemOrigAvailabilityCd();
			  getItemFromItemRequest(orderItemImpl.getItem(), itemRequest);

			  if (!rePinRightFlag) {
				  // Don't stomp on orig availabilty cd when only re-pricing a license
				  orderItemImpl.setItemOrigAvailabilityCd(itemOrigAvailabiltyCd);		  
			  }
	
			  // Item has been repinned, override is no longer in effect and it may have reverted back to special order
			  if (rePinRightFlag) {
				  orderItemImpl.setBaseInvoiceDate(null);
				  // If the item has changed to a special order, re-initialize the item status values 
				  if (orderItemImpl.getItemAvailabilityCd() == null ||
					  orderItemImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getStandardPermissionCd())
					  || orderItemImpl.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER.name())
			          || orderItemImpl.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER.name())) {
					  	orderItemImpl.setOverrideAvailabilityCd(null);
					  	orderItemImpl.setOverrideDate(null);
					  	orderItemImpl.setOverrideComment(null);
					  	orderItemImpl.setExternalItemStatusCd(null);
						orderItemImpl.setItemStatusCd(ItemStatusEnum.AWAITING_RESEARCH.name());
						orderItemImpl.setItemStatusDate(new Date());
				  } else if (orderItemImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd()) ||
						     orderItemImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd())) {
					  	orderItemImpl.setOverrideAvailabilityCd(null);
					  	orderItemImpl.setOverrideDate(null);
					  	orderItemImpl.setOverrideComment(null);
					  	orderItemImpl.setExternalItemStatusCd(null);
						if (orderItemImpl.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER.name()) ||
							orderItemImpl.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER.name())) {
							orderItemImpl.setItemStatusCd(ItemStatusEnum.AWAITING_RH.name());
							orderItemImpl.setItemStatusDate(new Date());
							orderItemImpl.setItemStatusQualifierCd(ItemStatusQualifierEnum.FIRST_REQUEST.name());
						} else {
							orderItemImpl.setItemStatusCd(ItemStatusEnum.AWAITING_RESEARCH.name());
							orderItemImpl.setItemStatusQualifierCd(ItemStatusQualifierEnum.ERROR.name());
							orderItemImpl.setItemStatusDate(new Date());
						}
				  } else if (orderItemImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
						orderItemImpl.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
						orderItemImpl.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
						orderItemImpl.setItemStatusDate(new Date());
					  	orderItemImpl.setOverrideAvailabilityCd(null);
					  	orderItemImpl.setOverrideDate(null);
					  	orderItemImpl.setOverrideComment(null);
					  	orderItemImpl.setExternalItemStatusCd(null);
				  } else if (orderItemImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.NO_CHARGE.getStandardPermissionCd())) {
						orderItemImpl.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
						orderItemImpl.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
						orderItemImpl.setItemStatusDate(new Date());
					  	orderItemImpl.setOverrideAvailabilityCd(null);
					  	orderItemImpl.setOverrideDate(null);
					  	orderItemImpl.setOverrideComment(null);
					  	orderItemImpl.setExternalItemStatusCd(null);
				  } else if (orderItemImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd())) {
						orderItemImpl.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
						orderItemImpl.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
						orderItemImpl.setItemStatusDate(new Date());
					  	orderItemImpl.setOverrideAvailabilityCd(null);
					  	orderItemImpl.setOverrideDate(null);
					  	orderItemImpl.setOverrideComment(null);
					  	orderItemImpl.setExternalItemStatusCd(null);
				  } 
				  else if (orderItemImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.DENY.getStandardPermissionCd())
						  && orderItemImpl.getRightAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) 
				  {
						orderItemImpl.setItemStatusCd(ItemStatusEnum.AWAITING_LCN_REPLY);
						orderItemImpl.setItemStatusQualifier(ItemStatusQualifierEnum.FIRST_REQUEST);					  
						orderItemImpl.setItemStatusDate(new Date());
					  	orderItemImpl.setOverrideAvailabilityCd(ItemAvailabilityEnum.DENY.getStandardPermissionCd());
					  	orderItemImpl.setOverrideDate(new Date());
					  	orderItemImpl.setOverrideComment(itemRequest.getResolutionTerms());
					  	orderItemImpl.setExternalItemStatusCd(null);
				  }
				  else if (orderItemImpl.getRightAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.DENY.getStandardPermissionCd())) 
				  {
						orderItemImpl.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
						orderItemImpl.setItemStatusQualifier(ItemStatusQualifierEnum.INVOICE_READY_INVOICE);					  
						orderItemImpl.setItemStatusDate(new Date());
					  	orderItemImpl.setOverrideAvailabilityCd(null);
					  	orderItemImpl.setOverrideDate(null);
					  	orderItemImpl.setOverrideComment(null);
					  	orderItemImpl.setExternalItemStatusCd(null);
				  }

			  }
			  
			  
			  // Did not re-pin (academic num students is only change) rightsholder values do not change
			  if (rePinRightFlag == false &&
				  itemAllFeesCache != null &&
				  itemAllFeesCache.size() == 1 &&
				  orderItemImpl.getItem().getAllFees() != null &&
				  orderItemImpl.getItem().getAllFees().size() == 1) {
				  Iterator<ItemFees> allFeesIterator = orderItemImpl.getItem().getAllFees().iterator();
				  Iterator<ItemFees> allFeesCacheIterator = itemAllFeesCache.iterator();

				  ItemFees itemFees = allFeesIterator.next();
				  ItemFees itemFeesCache = allFeesCacheIterator.next();

				  itemFees.setActualDistAccount(itemFeesCache.getActualDistAccount());
				  itemFees.setOrigDistAccount(itemFeesCache.getOrigDistAccount());
				  itemFees.setActualDistPartyId(itemFeesCache.getActualDistPartyId());
				  itemFees.setOrigDistPartyId(itemFeesCache.getOrigDistPartyId());
				  itemFees.setActualDistPtyInst(itemFeesCache.getActualDistPtyInst());
				  itemFees.setOrigDistPtyInst(itemFeesCache.getOrigDistPtyInst());
				  itemFees.setActualDistPartyName(itemFeesCache.getActualDistPartyName());
				  itemFees.setOrigDistPartyName(itemFeesCache.getOrigDistPartyName());
			  } else {
				  initRightsholdersForItem(orderItemImpl.getItem());
			  }

	  } else {
		  orderItemImpl.setSpecialOrderTypeCd(SpecialOrderTypeEnum.MANUAL_SPECIAL_ORDER.name());
		  orderItemImpl.setRightAvailabilityCd(null);
		  orderItemImpl.setItemAvailabilityCd(null);
	  }
	  

	    LOGGER.debug("Order Item price updated successfully (" + orderLicense.getPrice() + ")");      

	    return orderItemImpl;
//	    return orderLicense;
	  }
    
	  
	 /**
	  * Calculates (does not update) the price of a <code>PurchasablePermission</code> instance.
	  * 
	  * @param purchasablePermission <code>PurchasablePermission</code> instance to calculate price for.
	  */

	public static String getItemPrice( PurchasablePermission purchasablePermission)
	throws InvalidAttributesException, 
    DeniedLimitsExceededException,
    SpecialOrderLimitsExceededException,
    ContactRHDirectlyLimitsExceededException, 
    SystemLimitsExceededException,
    ServiceInvocationException
    {
	    String itemPrice = ECommerceConstants.PRICE_NOT_AVAILABLE;
		
		try 
		  {
			
			if (UserContextService.isUserAuthenticated())
			{
				itemPrice = getItemPrice(purchasablePermission, UserContextService.getActiveAppUser().getPartyID());
			}
			else
			{
				itemPrice = getItemPrice(purchasablePermission, 0L);
			}
				  
		  LOGGER.debug("Total Price: " + itemPrice);
		  
		  }
		  
		  catch ( com.copyright.data.pricing.DeniedLimitsExceededException deniedLimitsExceededException )
		  {
	      LOGGER.info("Denied limits exceeded");
	      
	      throw new DeniedLimitsExceededException( purchasablePermission, deniedLimitsExceededException );
	    }
	    
		  catch ( com.copyright.data.pricing.SpecialOrderLimitsExceededException specialOrderLimitsExceededException )
		  {
	      LOGGER.info("Special Order limits exceeded");
	      
	      throw new SpecialOrderLimitsExceededException( purchasablePermission, specialOrderLimitsExceededException );
	    }
	    
		  catch( com.copyright.data.pricing.ContactRHDirectlyLimitsExceededException contactRHDirectlyLimitsExceededException )
	    {
	      LOGGER.info("Contact Rightsholder Directly limits exceeded");
	      
	      throw new ContactRHDirectlyLimitsExceededException ( purchasablePermission, contactRHDirectlyLimitsExceededException );
	    }
	    
		  catch( com.copyright.data.pricing.SystemLimitsExceededException systemLimitsExceededException )
		  {
		    LOGGER.info("Shared Services Pricing limits exceeded");
		    
		    throw new com.copyright.ccc.business.services.cart.SystemLimitsExceededException( purchasablePermission, systemLimitsExceededException );
		  }
	    
		  catch ( ValidationException validationException )
		  {      
	      LOGGER.warn("Invalid PurchasablePermission attributes found");
	      
	      throw new InvalidAttributesException( validationException, purchasablePermission );
	    }
		return itemPrice;
	}
	
    public static String getItemPrice( PurchasablePermission purchasablePermission, Long partyIdToCalc ) 
    throws InvalidAttributesException, 
        DeniedLimitsExceededException,
        SpecialOrderLimitsExceededException,
        ContactRHDirectlyLimitsExceededException, 
        SystemLimitsExceededException,
        ServiceInvocationException
    {
        if (purchasablePermission == null)
        {
            LOGGER.error("No PurchasablePermission found");
            throw new IllegalArgumentException("PurchasablePermission cannot be null");
        }
        String itemPrice = ECommerceConstants.PRICE_NOT_AVAILABLE;
        RightsResolverConsumerContext rrConsumerContext = new RightsResolverConsumerContext(); 
        ItemRequest itemRequest = getItemRequestFromItem(purchasablePermission.getItem());

        // Supporting the generalization of some fields to "Circulation/Distribution"

        if (purchasablePermission.isAcademic())
        {
            BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfStudents());
            String stringValue = decimalValue.toString();
            updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
        }
        if (purchasablePermission.isPhotocopy())
        {
            BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfCopies());
            String stringValue = decimalValue.toString();
            updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
        }
        if (purchasablePermission.isEmail())
        {
            BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfRecipients());
            String stringValue = decimalValue.toString();
            updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
        }
        //set licensee party id
        itemRequest.setLicenseePartyId(partyIdToCalc);

        // This is the case where there is no TfWorkInst and pricing comes from WorkSet
        PurchasablePermissionImpl purchasablePermissionImpl = (PurchasablePermissionImpl) purchasablePermission;
        itemRequest.setWorkSetId(purchasablePermissionImpl.getTfWksInst());

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.info("Calling Pricing through RR");
            LOGGER.info("ItemRequest TF Work Id: " + itemRequest.getExternalItemId());
            LOGGER.info("ItemRequest TF Work Set Id: " + itemRequest.getWorkSetId());
            LOGGER.info("ItemRequest WR Work Id: " + itemRequest.getItemSourceKey());
            LOGGER.info("ItemRequest Right Id: " + itemRequest.getExternalRightId());
        }
        if (itemRequest.getItemSourceKey() != null && 
            itemRequest.getItemSourceKey().compareTo(0L) > 0)
        {
            try 
            {
                Boolean rePinRightFlag = true;
                Boolean checkLimits = true;

                itemRequest = 
                    ServiceLocator.getRightsResolverService()
                        .getCalculatedPriceForItemRequest( 
                            rrConsumerContext, 
                            itemRequest, 
                            rePinRightFlag, 
                            checkLimits
                        );
            }	
            catch (Exception e)
            {      
                LOGGER.error("Service Invocation Exception: " + ExceptionUtils.getFullStackTrace(e));
                throw new ServiceInvocationException();
            }
        } 
        else 
        {  
            // Work not found
            return ECommerceConstants.PRICE_NOT_AVAILABLE;
        }

        LOGGER.debug("Total Price: " + itemRequest.getTotalPrice());

        if (itemRequest.getCalculatedAvailabilityCd() != null) 
        {
            if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd()) ||
                itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PUBLIC_DOMAIN.getTfPermissionCd()) ||
                itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.NO_CHARGE.getTfPermissionCd())) 
            {
                if (itemRequest.getTotalPrice() != null) 
                {
                    Money price = new Money(itemRequest.getTotalPrice().doubleValue());
                    itemPrice = WebUtils.formatMoney( price );
                }
            }
            if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd()) ||
                itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getTfPermissionCd())) 
            {
                itemPrice = ItemConstants.COST_TBD;
            }	      
        }
        if (itemRequest.getCalcFeeReturnCd().equals(CalcFeeReturnEnum.SYSTEM_LIMITS_EXCEEDED)) 
        {
            LOGGER.debug("System limits exceeded");
            throw new SystemLimitsExceededException(
                purchasablePermission, 
                LimitsExceededExceptionFactory.create(
                    itemRequest.getSpecialOrderType().getCd(), 
                    itemRequest.getResolutionTerms()
                )
            );		  
        }
        if (itemRequest.getCalculatedAvailabilityCd() != null && 
            itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd())) 
        {
            return itemPrice;
        }
        //  2013-08-20  MSJ
        //  These are our special cases... where limits are applied based
        //  on the parameters we passed into the pricing engine via the
        //  itemRequest object.  The calculated availability code was being
        //  lost (CC-3729) so I added code to set the availability code on
        //  the purchasablePermission object to match the calculated
        //  availability code.

        if ((itemRequest.getCalculatedAvailabilityCd() != null) &&
            ((itemRequest.getRightAvailabilityCd() != null) && 
             (itemRequest.getRightAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd()))))
        {
            purchasablePermissionImpl.setItemAvailabilityCd(itemRequest.getCalculatedAvailabilityCd());

            if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getTfPermissionCd())) 
            {
                LOGGER.debug("Contact Rightsholder Directly limits exceeded");
                throw new ContactRHDirectlyLimitsExceededException(
                    purchasablePermission, 
                    LimitsExceededExceptionFactory.create(
                        itemRequest.getSpecialOrderType().getCd(),
                        itemRequest.getResolutionTerms()
                    )
                );
            }
            if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd()) &&
                itemRequest.getItemRequestParms().get(ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name()) != null &&
                itemRequest.getItemRequestParms().get(ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name()).getParmValueBoolean().equals(Boolean.TRUE))
            {
                LOGGER.debug("Entire Work Fee not set");
                throw new SpecialOrderLimitsExceededException(
                    purchasablePermission, 
                    LimitsExceededExceptionFactory.create(
                        itemRequest.getSpecialOrderType().getCd(), 
                        "We will need to contact the rightsholder to obtain permission to reuse the entire book."
                    )
                );
            }
            if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd())) 
            {
                LOGGER.debug("Special Order limits exceeded");
                throw new SpecialOrderLimitsExceededException(
                    purchasablePermission, 
                    LimitsExceededExceptionFactory.create(
                        itemRequest.getSpecialOrderType().getCd(), 
                        itemRequest.getResolutionTerms() == null ? "Unable to Price" : itemRequest.getResolutionTerms()
                    )
                );
            }
            if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getTfPermissionCd())) 
            {
                LOGGER.debug("Research Further limits exceeded");
                throw new SpecialOrderLimitsExceededException(
                    purchasablePermission, 
                    LimitsExceededExceptionFactory.create(
                        itemRequest.getSpecialOrderType().getCd(), 
                        itemRequest.getResolutionTerms() == null ? "Unable to Price" : itemRequest.getResolutionTerms()
                    )
                );
            }
            if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.DENY.getTfPermissionCd()))
            {
                LOGGER.debug("Denied limits exceeded");
                throw new DeniedLimitsExceededException(
                    purchasablePermission,
                    LimitsExceededExceptionFactory.create(
                        itemRequest.getSpecialOrderType().getCd(), 
                        itemRequest.getResolutionTerms() == null ? "Unable to Price" : itemRequest.getResolutionTerms()
                    )
                );
            }
        }
        return itemPrice;
    }
  
	
	public static String getItemPriceForUpdateCheck( PurchasablePermission purchasablePermission, Long partyIdToCalc ) 
    throws InvalidAttributesException, 
           DeniedLimitsExceededException,
           SpecialOrderLimitsExceededException,
           ContactRHDirectlyLimitsExceededException, 
           SystemLimitsExceededException
  {
	
		
    if( purchasablePermission == null )
    {
      LOGGER.error("No PurchasablePermission found");
      
      throw new IllegalArgumentException("PurchasablePermission cannot be null");
    }

      String itemPrice = ECommerceConstants.PRICE_NOT_AVAILABLE;

      RightsResolverConsumerContext rrConsumerContext = new RightsResolverConsumerContext(); 
	  
	  ItemRequest itemRequest = getItemRequestFromItem(purchasablePermission.getItem());

	  // Supporting the generalization of some fields to "Circulation/Distribution"
      if( purchasablePermission.isAcademic())
	  {
		  BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfStudents());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	  }

	  if( purchasablePermission.isPhotocopy())
	  {
		  BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfCopies());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	  }
	    
	  if(purchasablePermission.isEmail())
	  {
		  BigDecimal decimalValue = new BigDecimal(purchasablePermission.getNumberOfRecipients());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	  }
	  
	  //set licensee party id
	  itemRequest.setLicenseePartyId(partyIdToCalc);

	  // This is the case where there is no TfWorkInst and pricing comes from WorkSet
	  PurchasablePermissionImpl purchasablePermissionImpl = (PurchasablePermissionImpl) purchasablePermission;
	  itemRequest.setWorkSetId(purchasablePermissionImpl.getTfWksInst());
	  
	  LOGGER.debug("Calling Pricing through RR");
	  LOGGER.debug("ItemRequest TF Work Id: " + itemRequest.getExternalItemId());
	  LOGGER.debug("ItemRequest TF Work Set Id: " + itemRequest.getWorkSetId());
	  LOGGER.debug("ItemRequest WR Work Id: " + itemRequest.getItemSourceKey());
	  LOGGER.debug("ItemRequest Right Id: " + itemRequest.getExternalRightId());
	  
	  if (itemRequest.getItemSourceKey() != null && itemRequest.getItemSourceKey().compareTo(0L) > 0) {
		  try 
		  {
			  Boolean rePinRightFlag = true;
			  Boolean checkLimits = true;
			  
			  itemRequest  = ServiceLocator.getRightsResolverService().getCalculatedPriceForItemRequest(rrConsumerContext, itemRequest, rePinRightFlag, checkLimits);
		  }	
		  catch ( Exception e )
		  {      
			  LOGGER.error("Pricing Request Exception: " + ExceptionUtils.getFullStackTrace(e));
	      
//				throw e;
		  }

	  
	  } else {  // Work not found
		  return ECommerceConstants.PRICE_NOT_AVAILABLE;
	  }
  
	  LOGGER.debug("Total Price: " + itemRequest.getTotalPrice());

	  if (itemRequest.getCalculatedAvailabilityCd() != null) {
		  if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd()) ||
			  itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PUBLIC_DOMAIN.getTfPermissionCd()) ||
		      itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.NO_CHARGE.getTfPermissionCd())) {
		  	  if (itemRequest.getTotalPrice() != null) {
		  		  Money price = new Money(itemRequest.getTotalPrice().doubleValue());
		  		  itemPrice = WebUtils.formatMoney( price );		  
		  	  } else {
		  		  itemPrice = ECommerceConstants.PRICE_NOT_CALCULATED;
		  	  }
		  }
//		  if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd())) {
//			  itemPrice = ItemConstants.COST_TBD;
//		  }	      
		  return itemPrice;
	  }
	  	  
	  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd())) {
		  return itemPrice;
	  }
  
	  if (itemRequest.getRightAvailabilityCd() != null && itemRequest.getRightAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd())) {
		  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getTfPermissionCd())) {
		      LOGGER.debug("Contact Rightsholder Directly limits exceeded");
		      throw new ContactRHDirectlyLimitsExceededException ( purchasablePermission, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );
		  }
		  
		  if (itemRequest.getCalculatedAvailabilityCd() != null 
				  && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd()) 
				  && itemRequest.getItemRequestParms().get(ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name()) != null 
				    && itemRequest.getItemRequestParms().get(ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name()).getParmValueBoolean().equals(Boolean.TRUE))
		  {
			  LOGGER.debug("Entire Work Fee not set");
		      throw new SpecialOrderLimitsExceededException ( purchasablePermission, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), "We will need to contact the rightsholder to obtain permission to reuse the entire book.") );
		  }
		  
		  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd())) {
			  LOGGER.debug("Special Order limits exceeded");
		      throw new SpecialOrderLimitsExceededException ( purchasablePermission, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );
		  }

		  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getTfPermissionCd())) {
			  LOGGER.debug("Research Further limits exceeded");
		      throw new SpecialOrderLimitsExceededException ( purchasablePermission, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );
		  }
		  
		  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.DENY.getTfPermissionCd())) {
			  LOGGER.debug("Denied limits exceeded");
			  throw new DeniedLimitsExceededException ( purchasablePermission, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );
		  }
	  }

	  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getTfPermissionCd())) {
		  itemPrice = ECommerceConstants.CONTACT_RIGHTSHOLDER_DIRECTLY;
	  }
  

    return itemPrice;
	}
  
	
	
  /**
   * Calculates (does not update) the price of a <code>OrderLicense</code> instance.
   * 
   * @param orderLicense <code>OrderLicense</code> instance to calculate price for.
   */
	public static String getItemPrice( OrderLicense orderLicense ) 
    throws InvalidAttributesException, 
           DeniedLimitsExceededException,
           SpecialOrderLimitsExceededException,
           ContactRHDirectlyLimitsExceededException, 
           SystemLimitsExceededException,
           ServiceInvocationException
  {
		
		
	  if( orderLicense == null )
      {
		  LOGGER.error("No OrderLicense found");
      
		  throw new IllegalArgumentException("OrderLicense cannot be null");
      }
    
      String itemPrice = ECommerceConstants.PRICE_NOT_AVAILABLE;

      RightsResolverConsumerContext rrConsumerContext = new RightsResolverConsumerContext(); 
	  
      OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
      
	  ItemRequest itemRequest = getItemRequestFromItem(orderItemImpl.getItem());

	  // Supporting the generalization of some fields to "Circulation/Distribution"
      if( orderLicense.isAcademic())
	  {
		  BigDecimal decimalValue = new BigDecimal(orderLicense.getNumberOfStudents());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	  }

	  if( orderLicense.isPhotocopy())
	  {
		  BigDecimal decimalValue = new BigDecimal(orderLicense.getNumberOfCopies());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	  }
	    
	  if(orderLicense.isEmail())
	  {
		  BigDecimal decimalValue = new BigDecimal(orderLicense.getNumberOfRecipients());
		  String stringValue = decimalValue.toString();
		  updateItemRequestParm (ItemParmEnum.CIRCULATION_DISTRIBUTION, stringValue, decimalValue, null,  itemRequest);
	  }

	//set licensee party id
	  if (orderLicense.getOrderDataSource()==OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
		  try {
			OrderPurchase orderPurchase = OrderPurchaseServices.getCOIOrderPurchaseForOrderHeaderId(orderLicense.getOrderHeaderId());
			OrderHeaderImpl orderHeaderImpl = (OrderHeaderImpl) orderPurchase;
			itemRequest.setLicenseePartyId(orderHeaderImpl.getBuyerPartyId());
		  } catch (OrderPurchasesException e) {
			// This should never happen, if it does user will not see licensee specific pricing
			  LOGGER.warn(LogUtil.getStack(e));
				itemRequest.setLicenseePartyId(0L);
		}
	  } else {
		  itemRequest.setLicenseePartyId(orderLicense.getLicenseePartyId());   		  
	  }
	  
	  //itemRequest.setLicenseePartyId(orderItemImpl.getLicenseePartyID());

	  LOGGER.debug("Getting Price for Item " + orderItemImpl.getID());
	  
	  Boolean rePinRightFlag = true;
	  Boolean checkLimits = true;
	  
	  if (orderLicense.isAcademic() &&
		  !orderLicense.isRequiresAcademicRepin()) {
		  rePinRightFlag = false;
		  checkLimits = false;
		  itemRequest.setCalculatedAvailabilityCd(orderLicense.getItemAvailabilityCd());
	  }
	  try {
		  itemRequest  = ServiceLocator.getRightsResolverService().getCalculatedPriceForItemRequest(rrConsumerContext, itemRequest, rePinRightFlag, checkLimits);
	  }
	  catch ( Exception e )
	  {      
		  LOGGER.error("Service Invocation Exception: " + ExceptionUtils.getFullStackTrace(e));
		  throw new ServiceInvocationException();
	  }
	  
//	  orderLicense.setSpecialOrderTypeCd(itemRequest.getSpecialOrderType().getCd());
	  	  
	  LOGGER.debug("Total Price: " + itemRequest.getTotalPrice());

	  if (itemRequest.getCalcFeeReturnCd().equals(CalcFeeReturnEnum.SYSTEM_LIMITS_EXCEEDED)) {
	      LOGGER.debug("System limits exceeded");
	      throw new SystemLimitsExceededException ( orderLicense, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );		  
	  }

	  LOGGER.debug("OrderLicense price found successfully (" + itemPrice + ")");
  
	  if (itemRequest.getCalculatedAvailabilityCd() != null) {
		  if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd()) ||
			  itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PUBLIC_DOMAIN.getTfPermissionCd()) ||
		      itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.NO_CHARGE.getTfPermissionCd())) {
		  	  if (itemRequest.getTotalPrice() != null) {
		  		  Money price = new Money(itemRequest.getTotalPrice().doubleValue());
		  		  itemPrice = WebUtils.formatMoney( price );		  
		  	  }
		  }
		  if (itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd()) ||
			  itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getTfPermissionCd())) {
			  itemPrice = ItemConstants.COST_TBD;
		  }	      
//		  return itemPrice;
	  }
  
	  if (itemRequest.getRightAvailabilityCd() != null && itemRequest.getRightAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getTfPermissionCd())) {
		  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getTfPermissionCd())) {
		      LOGGER.debug("Contact Rightsholder Directly limits exceeded");
		      throw new ContactRHDirectlyLimitsExceededException ( orderLicense, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );
		  }
		  
		  if (itemRequest.getCalculatedAvailabilityCd() != null 
				  && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd()) 
				  && itemRequest.getItemRequestParms().get(ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name()) != null 
				    && itemRequest.getItemRequestParms().get(ItemParmEnum.LCN_REQUESTED_ENTIRE_WORK.name()).getParmValueBoolean().equals(Boolean.TRUE))
		  {
			  LOGGER.debug("Entire Work Fee not set");
		      throw new SpecialOrderLimitsExceededException ( orderLicense, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), "We will need to contact the rightsholder to obtain permission to reuse the entire book.") );
		  }
		  
		  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getTfPermissionCd())) {
			  LOGGER.debug("Special Order limits exceeded");
		      throw new SpecialOrderLimitsExceededException ( orderLicense, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );
		  }

		  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getTfPermissionCd())) {
			  LOGGER.debug("Research Further limits exceeded");
		      throw new SpecialOrderLimitsExceededException ( orderLicense, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );
		  }
		  
		  if (itemRequest.getCalculatedAvailabilityCd() != null && itemRequest.getCalculatedAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.DENY.getTfPermissionCd())) {
			  LOGGER.debug("Denied limits exceeded");
			  throw new DeniedLimitsExceededException ( orderLicense, LimitsExceededExceptionFactory.create(itemRequest.getSpecialOrderType().getCd(), itemRequest.getResolutionTerms()) );
		  }
	  } 
	  
	  return itemPrice;
	}
	
	public static WRStandardWork pushWorkToTf(PurchasablePermission purchasablePermission) {

//		PermissionRequest permissionRequest = PricingServices.getPermissionRequestFromTransactionItem(
//				purchasablePermission);

		PurchasablePermissionImpl purchasablePermissionImpl = (PurchasablePermissionImpl) purchasablePermission;
		
		WRStandardWork wrStandardWork = purchasablePermissionImpl.getWork();

	    if (purchasablePermissionImpl.getTfWksInst()!=null && purchasablePermissionImpl.getTfWksInst()>0) {
	    	List<Long> tfWksInstList = new ArrayList<Long>();
	    	tfWksInstList.add(purchasablePermissionImpl.getTfWksInst());
	    	wrStandardWork.setTfWksInstList(tfWksInstList);
	    }
		
		if (wrStandardWork.getTfWksInstList() != null && wrStandardWork.getTfWksInstList().size() !=0 && 
			wrStandardWork.getWrkInst()==0){
			//push to TF
			//try to push work to TF
			Long tfWrkInst;
			try {
				//technically if the work tk work set inst is null then an exception will be thrown as
				//we are only allowed to push works that have a tf work set inst and a null tf work inst
				//tfWrkInst=service.pushWorkToTF(wrStandardWork.getWrWrkInst());
				WorkExternal workExternal = new WorkExternal();
				workExternal.setTfWksInstList(wrStandardWork.getTfWksInstList());
				workExternal.setPages(Long.valueOf(wrStandardWork.getPages()));
				tfWrkInst = ServiceLocator.getRightsService().pushWorkToTF(new RightsConsumerContext(), wrStandardWork.getWrWrkInst(), workExternal);

				if (tfWrkInst == null) {
					//the pushing the work to TF failed, set the rights to NULL to force a manual special order
					LOGGER.error("tf push failed for WR WRK INST: " + wrStandardWork.getWrWrkInst()+ " Null return from call.");
					wrStandardWork.setWrkInst(0);
				}
				else{
					wrStandardWork.setWrkInst(tfWrkInst.longValue());
				}
			}
			catch (Exception exc) {
				//any exception signifies the pushing the work to TF failed so set the TF to null
				//and set the rights to NULL to force a manual special order
				LOGGER.error("tf push for failed for WR WRK INST: " + wrStandardWork.getWrWrkInst()+ " Cause: " + LogUtil.appendableStack(exc));
				tfWrkInst = null;
				wrStandardWork.setWrkInst(0); 
			}
		}
		
		return wrStandardWork;
		
	}
	
	
  public static PermissionRequest getPermissionRequestFromTransactionItem(TransactionItem transactionItem)
  {

	  PermissionRequest permissionRequest = new PermissionRequest(transactionItem.getRgtInst());

	  UsageData usageData = null;
	  
      LOGGER.debug("Transaction Item for Title: " + transactionItem.getPublicationTitle()); 
      LOGGER.debug("isAcademic: " + transactionItem.isAcademic()); 

	  if( transactionItem.isAcademic())
	  {
	      usageData = buildUsageDataAcademic( transactionItem );
	  }

	  if( transactionItem.isRepublication())
	  {
	      usageData = buildUsageDataRepublication( transactionItem );
	  }
	    
	  if( transactionItem.isPhotocopy())
	  {
	      usageData = buildUsageDataPhotocopy( transactionItem );
	  }
	    
	  if(transactionItem.isEmail())
	  {
	      usageData = buildUsageDataEmail( transactionItem );
	  }
	    
	  if( transactionItem.isNet() )
	  {
	      usageData = buildUsageDataNet( transactionItem );
	  }
	    
	  if( usageData == null )
	  {
	      throw new UnableToBuildPurchasablePermissionException( "Unable to build an instance of UsageData" );
	  }

	  permissionRequest.setUsageData(usageData);

      WRStandardWork work = new WRStandardWork();
	  
      work.setWrkInst( transactionItem.getWorkInst() );
      work.setStandardNumber( transactionItem.getStandardNumber() );
      work.setMainTitle( transactionItem.getPublicationTitle() );
      work.setMainAuthor( transactionItem.getAuthor() );
      work.setMainEditor( transactionItem.getEditor());
      work.setVolume( transactionItem.getVolume() );
      work.setEdition( transactionItem.getEdition() );
      work.setPublisher( transactionItem.getPublisher() );
      if (transactionItem.getTfWksInst()!=null && transactionItem.getTfWksInst()>0) {
    	  List<Long> tfWksInstList = new ArrayList<Long>();
    	  tfWksInstList.add(transactionItem.getTfWksInst());
    	  work.setTfWksInstList(tfWksInstList);
      }
      work.setWrwrkinst(transactionItem.getWrWorkInst());
      
      work.setSeries(transactionItem.getSeries());
      work.setSeriesNumber(transactionItem.getSeriesNumber());
      work.setPublicationType(transactionItem.getPublicationType());
      work.setCountry(transactionItem.getCountry());
      work.setLanguage(transactionItem.getLanguage());
      work.setIdnoLabel(transactionItem.getIdnoLabel());
      work.setIdnoTypeCd(transactionItem.getIdnoTypeCd());
      work.setPages(transactionItem.getPages());
	  
	  permissionRequest.setWork(work);

	  return permissionRequest;
	  
  }


 
  private static UsageDataAcademic buildUsageDataAcademic( TransactionItem transactionItem )
  {   
	      
    UsageDataAcademic usageDataAcademic = new UsageDataAcademic();
    
    populateCommonUsageData( transactionItem, usageDataAcademic );
	      	      
    usageDataAcademic.setNumPages(Long.valueOf(transactionItem.getNumberOfPages()));
	usageDataAcademic.setNumStudents(Long.valueOf(transactionItem.getNumberOfStudents()));
	usageDataAcademic.setPageRanges(transactionItem.getPageRange());
	usageDataAcademic.setChapterArticle(transactionItem.getChapterArticle());
	usageDataAcademic.setEdition(transactionItem.getEdition());
	usageDataAcademic.setVolume(transactionItem.getVolume() );
	      
	return usageDataAcademic;
  }

  private static UsageDataRepublication buildUsageDataRepublication( TransactionItem transactionItem )
  {
	         
	UsageDataRepublication usageDataRepublication = new UsageDataRepublication();
	      
	//common data
	populateCommonUsageData( transactionItem, usageDataRepublication );
	      
	//rlsPages
	usageDataRepublication.setRlsPages( transactionItem.getNumberOfPages() );
	      
	//chapterArticle
	usageDataRepublication.setChapterArticle( transactionItem.getChapterArticle() );
	      
	//circulation
	usageDataRepublication.setCirculation( transactionItem.getCirculationDistribution() );
	      
	//forProfit
	if( RepublicationConstants.BUSINESS_FOR_PROFIT.equals(transactionItem.getBusiness()) )
	{
		usageDataRepublication.setForProfit( RepublicationConstants.FOR_PROFIT );
	        
	}else
	{
		usageDataRepublication.setForProfit( RepublicationConstants.NON_FOR_PROFIT );
	}
	      
	//content types...
//	usageDataRepublication = ECommerceUtils.setTypeOfContent( usageDataRepublication, orderLicense.getTypeOfContent() );
	      
    usageDataRepublication = ECommerceUtils.zeroContentTypeQuantities( usageDataRepublication );
          
    if (!transactionItem.getTypeOfContent().equals(RepublicationConstants.CONTENT_SELECTED_PAGES)) {
        usageDataRepublication.setRlsPages( 0 );
    }
          
    usageDataRepublication = ECommerceUtils.setTypeOfContent( usageDataRepublication, transactionItem.getTypeOfContent() );

	      
	//origAuthor
	if( transactionItem.isSubmitterAuthor() )
	{
	  usageDataRepublication.setOrigAuthor( RepublicationConstants.SUBMITTER_IS_AUTHOR);           
	}else
	{
	  usageDataRepublication.setOrigAuthor( RepublicationConstants.SUBMITTER_IS_NOT_AUTHOR);
	}
	      
	//publicationDate
	usageDataRepublication.setPublicationDate( transactionItem.getContentsPublicationDate() );
	       
	//hdrRepubPub
	usageDataRepublication.setHdrRepubPub( StringUtils.upperCase(transactionItem.getRepublishingOrganization()) );
	      
	//repubTitle
//	usageDataRepublication.setRepublicationDestination( StringUtils.upperCase(transactionItem.getNewPublicationTitle()) );
	    
	//repubDate
	usageDataRepublication.setRepubDate( transactionItem.getRepublicationDate() );
	       
	//language & translated
	if( transactionItem.getTranslationLanguage().equals( RepublicationConstants.NO_TRANSLATION ) )
	{
		usageDataRepublication.setLanguage( Constants.EMPTY_STRING );
		usageDataRepublication.setTranslated( RepublicationConstants.NOT_TRANSLATED);
	}else
	{
		usageDataRepublication.setLanguage( transactionItem.getTranslationLanguage() );
	    usageDataRepublication.setTranslated( RepublicationConstants.TRANSLATED );
	}
	      
	//lcnHdrRefNum
	usageDataRepublication.setLcnHdrRefNum(transactionItem.getYourReference() );
	      
	//author
	usageDataRepublication.setAuthor( transactionItem.getCustomAuthor() );
	      
	usageDataRepublication.setPageRanges( transactionItem.getPageRange() );
	      
	return usageDataRepublication;
  }

  private static UsageDataPhotocopy buildUsageDataPhotocopy(TransactionItem transactionItem)
  {
	UsageDataPhotocopy usageDataPhotocopy = new UsageDataPhotocopy();
	      
	populateCommonUsageData( transactionItem, usageDataPhotocopy );
	      
	usageDataPhotocopy.setNumCopies( transactionItem.getNumberOfCopies() );
	usageDataPhotocopy.setNumPages( transactionItem.getNumberOfPages() );
	usageDataPhotocopy.setChapterArticle( transactionItem.getChapterArticle() );
	usageDataPhotocopy.setEditor( transactionItem.getEditor() );
	usageDataPhotocopy.setEdition( transactionItem.getEdition() );
	usageDataPhotocopy.setVolume( transactionItem.getVolume() );
	      
	return usageDataPhotocopy;
  }

  private static UsageDataEmail buildUsageDataEmail(TransactionItem transactionItem)
  {
     UsageDataEmail usageDataEmail = new UsageDataEmail();
	      
	 populateCommonUsageData( transactionItem, usageDataEmail );
	      
	 usageDataEmail.setChapterArticle( transactionItem.getChapterArticle() );
	 usageDataEmail.setNumRecipients( transactionItem.getNumberOfRecipients() );
	 usageDataEmail.setDateOfUse( transactionItem.getDateOfUse() );
	      
	 return usageDataEmail;
  }

  private static UsageDataNet buildUsageDataNet(TransactionItem transactionItem)
  {
	 UsageDataNet usageDataNet = new UsageDataNet();
	      
	 populateCommonUsageData( transactionItem, usageDataNet );
	      
	 usageDataNet.setChapterArticle( transactionItem.getChapterArticle() );
	 usageDataNet.setDateOfUse( transactionItem.getDateOfUse() );
	 usageDataNet.setDuration( transactionItem.getDuration() );
	 usageDataNet.setWebAddress( transactionItem.getWebAddress() );
	      
	 return usageDataNet;
  }

  private static void populateCommonUsageData( TransactionItem transactionItem, UsageData usageData )
  {      
	usageData.setProduct( transactionItem.getProductSourceKey() );
	usageData.setTpuInst( transactionItem.getExternalTouId() );
	usageData.setTypeOfUseDisplay( transactionItem.getTouName() );
  }

  private static ItemRequestParm getItemRequestParmFromItemParm(ItemRequestParm itemRequestParm, ItemParm itemParm) {

	  if(itemParm.getDataTypeCd()==null){
		  itemParm.setDataTypeCd(DataTypeEnum.STRING.toString());
	  	}
	  
	  if (itemParm.getDataTypeCd().equals(ItemConstants.STRING)) {
		  itemRequestParm.setDataType(DataTypeEnum.STRING);
		  itemRequestParm.setParmValueString(itemParm.getParmValue());
	  } else if (itemParm.getDataTypeCd().equals(ItemConstants.INTEGER)) {
		  itemRequestParm.setDataType(DataTypeEnum.LONG);
		  if (itemParm.getParmValueNumeric() != null) {
			  itemRequestParm.setParmValueLong(itemParm.getParmValueNumeric().longValue());			  
		  }
	  } else if (itemParm.getDataTypeCd().equals(ItemConstants.NUMBER)) {
		  itemRequestParm.setDataType(DataTypeEnum.NUMBER);
		  itemRequestParm.setParmValueBigDecimal(itemParm.getParmValueNumeric());			  
	  } else if (itemParm.getDataTypeCd().equals(ItemConstants.DATE)) {
		  itemRequestParm.setDataType(DataTypeEnum.DATE);		  
		  itemRequestParm.setParmValueDate(itemParm.getParmValueDate());
	  } else if (itemParm.getDataTypeCd().equals(ItemConstants.CLOB)) {
		  itemRequestParm.setDataType(DataTypeEnum.CLOB);		  
		  itemRequestParm.setParmValueClob(itemParm.getParmValueClob());
	  } else if (itemParm.getDataTypeCd().equals(ItemConstants.BOOLEAN)) {
		  itemRequestParm.setDataType(DataTypeEnum.BOOLEAN);
		  if (itemParm.getParmValue().equals(ItemConstants.YES_CD)) {
			  itemRequestParm.setParmValueBoolean(true);		
		  } else {
			  itemRequestParm.setParmValueBoolean(false);		
		  }
	  }

	  
//	  itemRequestParm..setDataTypeCd(itemParm.getDataTypeCd());
//	  itemRequestParm.setDisplaySequence(itemParm.getDisplaySequence());
//    itemRequestParm.setDisplayTextPost(itemParm.getDisplayTextPost());
//	  itemRequestParm.setDisplayTextPre(itemParm.getDisplayTextPre());
//	  itemRequestParm.setDisplayWidth(itemParm.getDisplayWidth());
//	  itemRequestParm.setFieldLength(itemParm.getFieldLength());
	  itemRequestParm.setItemId(itemParm.getItemId());
//	  itemRequestParm.setLabel(itemParm.getLabel());
//	  itemRequestParm.setLabelOnYn(itemParm.getLabelOn());
	  itemRequestParm.setParmDisplayValue(itemParm.getParmDisplayValue());
	  itemRequestParm.setParmName(itemParm.getParmName());
//	  itemRequestParm.setParmUseCd(itemParm.getParmUseCd());
	  itemRequestParm.setParmValueString(itemParm.getParmValue());

	  if (itemParm.getRlSourceBeanCd() != null) {
		  try {
			  itemRequestParm.setRlSourceBeanCd(RlSourceBeanEnum.valueOf(RlSourceBeanEnum.class, itemParm.getRlSourceBeanCd()));
		  } catch (Exception e) {
			  LOGGER.error("error raised; setting RlSourceBeanCd=null " + LogUtil.appendableStack(e));		  
			  itemRequestParm.setRlSourceBeanCd(null);
		  }
	  } else {
		  itemRequestParm.setRlSourceBeanCd(null);
	  }
//	  itemRequestParm.setParmValueClob(itemParm.getParmValueClob());
//	  itemRequestParm.setParmValueDate(itemParm.getParmValueDate());
//	  itemRequestParm.setParmValueNumeric(itemParm.getParmValueNumeric());
	  	  
/*		if (itemParm.getParmName().equals("PUBLICATION_YEAR_OF_USE")) {
		  GregorianCalendar calendar = new GregorianCalendar();
		  calendar.set( itemRequestParm.getParmValueLong().intValue(), 0, 1);
		  itemRequestParm.setDataType(itemRequestParm.getDataType().DATE);
		  itemRequestParm.setParmValueDate(calendar.getTime());
		 }
		if (itemParm.getParmName().equals("DURATION")) {
			  itemRequestParm.setDataType(DataTypeEnum.STRING);
			  if (itemParm.getParmValueNumeric().intValue() == 0) {
				  itemRequestParm.setParmValueString(DurationEnum.TO_30_DAYS.name());
			  } else if (itemParm.getParmValueNumeric().intValue() == 1) {
				  itemRequestParm.setParmValueString(DurationEnum.TO_180_DAYS.name());				  
			  } else if (itemParm.getParmValueNumeric().intValue() == 2) {
				  itemRequestParm.setParmValueString(DurationEnum.TO_365_DAYS.name());
			  }	else if (itemParm.getParmValueNumeric().intValue() == 3) {
				  itemRequestParm.setParmValueString(DurationEnum.UNLIMITED_DAYS.name());				  
			  }
		}
*/		
	  if(itemRequestParm.getDataType()!=null){
		  LOGGER.debug("Request Parm: " + itemRequestParm.getParmName() + " Data Type: " + itemRequestParm.getDataType().name() + 
				  " String Value: " + itemParm.getParmValue() +
				  " Long Value: " + itemRequestParm.getParmValueLong() +
				  " BigDecimal Value: " + itemRequestParm.getParmValueBigDecimal() +
				  " Date Value: " + itemRequestParm.getParmValueDate());
	  }
      
	  
	  return itemRequestParm;
  }
  
  private static void  updateItemRequestParm (ItemParmEnum itemEnum, String stringValue, 
		  					BigDecimal decimalValue, Date dateValue, ItemRequest itemRequest){
	  
	  
	  ItemRequestParm itemRequestParm = new ItemRequestParm();
	  itemRequestParm.setParmName(itemEnum.name());
	  
	  String dataTypeCd = null;
	  if(itemEnum.getDataTypeCd() == null){
		  dataTypeCd = DataTypeEnum.STRING.toString();
	  }else{
		  dataTypeCd = itemEnum.getDataTypeCd();
	  }
		  
	  if (dataTypeCd.equals(ItemConstants.STRING)) {
		  itemRequestParm.setDataType(DataTypeEnum.STRING);
		  itemRequestParm.setParmValueString(stringValue);
	  } else if (dataTypeCd.equals(ItemConstants.INTEGER)) {
		  itemRequestParm.setDataType(DataTypeEnum.LONG);
		  if (decimalValue != null) {
			  itemRequestParm.setParmValueLong(decimalValue.longValue());
		  }
	  } else if (dataTypeCd.equals(ItemConstants.NUMBER)) {
		  itemRequestParm.setDataType(DataTypeEnum.NUMBER);
		  itemRequestParm.setParmValueBigDecimal(decimalValue);			  
	  } else if (dataTypeCd.equals(ItemConstants.DATE)) {
		  itemRequestParm.setDataType(DataTypeEnum.DATE);		  
		  itemRequestParm.setParmValueDate(dateValue);
	  } else if (dataTypeCd.equals(ItemConstants.CLOB)) {
		  itemRequestParm.setDataType(DataTypeEnum.CLOB);		  
		  itemRequestParm.setParmValueClob(stringValue);
	  } else if (dataTypeCd.equals(ItemConstants.BOOLEAN)) {
		  itemRequestParm.setDataType(DataTypeEnum.BOOLEAN);
		  if (stringValue.equals(ItemConstants.YES_CD)) {
			  itemRequestParm.setParmValueBoolean(true);		
		  } else {
			  itemRequestParm.setParmValueBoolean(false);		
		  }
	  }
	  
	  itemRequest.getItemRequestParms().put(itemRequestParm.getParmName(), itemRequestParm);
	  
  }
  
  
  private static ItemParm getItemParmFromItemRequestParm(ItemParm itemParm, ItemRequestParm itemRequestParm) {

	  itemParm.setParmName(itemRequestParm.getParmName());

	  
	  // TEMP FIX SET DEFAULT VALUE FOR Rights Link- 
	  	if(itemRequestParm.getDataType()==null){
	  		itemRequestParm.setDataType(DataTypeEnum.STRING);
	  	}
	  	if (itemRequestParm.getParmValueString() != null) {
		  	itemParm.setParmValue(itemRequestParm.getParmValueString());	  		
	  	} else {
	  		itemParm.setParmValue(null);
	  	}

	  	if (itemRequestParm.getParmDisplayValue() != null) {
		  	itemParm.setParmDisplayValue(itemRequestParm.getParmDisplayValue());	  		
	  	} else {
	  		itemParm.setParmDisplayValue(null);
	  	}

	  	
	    if (itemRequestParm.getDataType().equals(DataTypeEnum.STRING)) {
			itemParm.setDataTypeCd(ItemConstants.STRING);
	    }else if (itemRequestParm.getDataType().equals(DataTypeEnum.NUMBER)) {
			itemParm.setDataTypeCd(ItemConstants.NUMBER);
		  	if (itemRequestParm.getParmValueBigDecimal() != null) {
		  		itemParm.setParmValueNumeric(itemRequestParm.getParmValueBigDecimal());
		  		if (itemRequestParm.getParmValueBigDecimal() != null) {
			  		itemParm.setParmValue(itemRequestParm.getParmValueBigDecimal().toString());
		  		}
		  	} else {
		  		itemParm.setParmValueNumeric(null);
		  		itemParm.setParmValue(null);		  		
		  	}
		} else if (itemRequestParm.getDataType().equals(DataTypeEnum.INTEGER) ||
				   itemRequestParm.getDataType().equals(DataTypeEnum.LONG)) {
			itemParm.setDataTypeCd(ItemConstants.INTEGER);
		  	if (itemRequestParm.getParmValueLong() != null) {
		  		itemParm.setParmValueNumeric(new BigDecimal(itemRequestParm.getParmValueLong()));
		  		if (itemRequestParm.getParmValueLong() != null) {
		  			itemParm.setParmValue(itemRequestParm.getParmValueLong().toString());
		  		}
		  	} else {
		  		itemParm.setParmValueNumeric(null);
		  		itemParm.setParmValue(null);		  		
		  	}
		} else if (itemRequestParm.getDataType().equals(DataTypeEnum.BOOLEAN)) {
				itemParm.setDataTypeCd(ItemConstants.BOOLEAN);
				if (itemRequestParm.getParmValueBoolean() == true) {
					itemParm.setParmValue(ItemConstants.YES_CD);
				} else {
					itemParm.setParmValue(ItemConstants.NO_CD);				
				}
		} else if (itemRequestParm.getDataType().equals(DataTypeEnum.DATE)) {
				itemParm.setDataTypeCd(ItemConstants.DATE);
				if (itemRequestParm.getParmValueDate() != null) {
					itemParm.setParmValueDate(itemRequestParm.getParmValueDate());
					GregorianCalendar calendar = new GregorianCalendar();
					calendar.setTime(itemParm.getParmValueDate());
					int month = calendar.get(GregorianCalendar.MONTH) + 1;
					int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
					int year = calendar.get(GregorianCalendar.YEAR);
					String mdy = Integer.valueOf(month).toString() + "/" + Integer.valueOf(day).toString() + "/" + Integer.valueOf(year).toString();
					itemParm.setParmValue(mdy);
				} else {
					itemParm.setParmValueDate(null);
					itemParm.setParmValue(null);
				}
		} else if (itemRequestParm.getDataType().equals(DataTypeEnum.CLOB)) {
			  	itemParm.setDataTypeCd(ItemConstants.CLOB);
			  	itemParm.setParmValueClob(itemRequestParm.getParmValueClob());
		}

/*		 if (itemRequestParm.getParmName().equals("PUBLICATION_YEAR_OF_USE")) {
			  GregorianCalendar calendar = new GregorianCalendar();
			  calendar.setTime( itemRequestParm.getParmValueDate());
			  itemParm.setDataTypeCd(ItemConstants.INTEGER);
			  itemParm.setParmValueNumeric(new BigDecimal(calendar.get(GregorianCalendar.YEAR)));
		 }
			if (itemRequestParm.getParmName().equals("DURATION")) {
				  itemParm.setDataTypeCd(ItemConstants.INTEGER);
				  if (itemParm.getParmValue().equalsIgnoreCase(DurationEnum.TO_30_DAYS.name())) {
					  itemParm.setParmValueNumeric(new BigDecimal(0));
					  itemParm.setParmValue(itemParm.getParmValueNumeric().toString());
				  } else if (itemParm.getParmValue().equalsIgnoreCase(DurationEnum.TO_180_DAYS.name())) {
					  itemParm.setParmValueNumeric(new BigDecimal(1));
					  itemParm.setParmValue(itemParm.getParmValueNumeric().toString());
				  } else if (itemParm.getParmValue().equalsIgnoreCase(DurationEnum.TO_365_DAYS.name())) {
					  itemParm.setParmValueNumeric(new BigDecimal(2));
					  itemParm.setParmValue(itemParm.getParmValueNumeric().toString());
				  }	else if (itemParm.getParmValue().equalsIgnoreCase(DurationEnum.UNLIMITED_DAYS.name())) {
					  itemParm.setParmValueNumeric(new BigDecimal(3));
					  itemParm.setParmValue(itemParm.getParmValueNumeric().toString());
				  }
			}
*/	  
	  
//	  itemParm.setDataTypeCd(itemRequestParm.getDataType().itemRequestParm.getDataTypeCd());
//	  itemParm.setDisplaySequence(itemRequestParm.getDisplaySequence());
//	  itemParm.setDisplayTextPost(itemRequestParm.getDisplayTextPost());
//	  itemParm.setDisplayTextPre(itemRequestParm.getDisplayTextPre());
//	  itemParm.setDisplayWidth(itemRequestParm.getDisplayWidth());
//	  itemParm.setFieldLength(itemRequestParm.getFieldLength());
//	  itemParm.setItemId(itemRequestParm.getItemId());
//	  itemParm.setLabel(itemRequestParm.getLabel());
//	  itemParm.setLabelOn(itemRequestParm.getLabelOnYn());
//	  itemParm.setParmUseCd(itemRequestParm.getParmUseCd());
	  if (itemRequestParm.getRlSourceBeanCd() != null) {
		  try {
			  itemParm.setRlSourceBeanCd(itemRequestParm.getRlSourceBeanCd().name());
		  } catch (Exception e) {
			  LOGGER.error("error raised; setting RlSourceBeanCd=null " + LogUtil.appendableStack(e));		  
			  itemParm.setRlSourceBeanCd(null);
		  }
	  } else {
		  itemParm.setRlSourceBeanCd(null);
	  }
	  
//	  itemParm.setParmValueClob(itemRequestParm.getParmValueClob());
//	  itemParm.setParmValueDate(itemRequestParm.getParmValueDate());
//	  itemRequestParm.setParmValueNumeric(itemRequestParm.getParmValueNumeric());

	  //TODO Blow this away
/*	  if (itemRequestParm.getParmName().equals("DURATION")) {
		  String durationString = itemRequestParm.getParmValue();
		  for (DurationEnum durationEnum : DurationEnum.values()) {
			  if (durationString.equals(durationEnum.name())) {
				  itemParm.setDataTypeCd("INTEGER");
				  itemParm.setParmValueNumeric(new BigDecimal(durationEnum.getId()));
				  itemParm.setParmValue(new BigDecimal(durationEnum.getId()).toString());
				  LOGGER.debug("Item Parm Reset: " + itemRequestParm.getParmName() + " Value: " + itemRequestParm.getParmValue());
			  }
		  }
	  }
*/	  
      LOGGER.debug("Returned ItemId: " + itemParm.getItemId() + " ItemParmId: " + itemParm.getItemParmId() + 
			  " Item Request Parm: " + itemParm.getParmName() + " String Value: " + itemParm.getParmValue());
  
	  return itemParm;
  }
  
  private static ItemRequestDescriptionParm getItemRequestDescriptionParmFromItemDescriptionParm(ItemRequestDescriptionParm itemRequestDescriptionParm, ItemDescriptionParm itemDescriptionParm) {

	  if (itemDescriptionParm.getDataTypeCd().equals(ItemConstants.STRING)) {
		  itemRequestDescriptionParm.setDataType(DataTypeEnum.STRING);
	  } else if (itemDescriptionParm.getDataTypeCd().equals(ItemConstants.INTEGER)) {
		  itemRequestDescriptionParm.setDataType(DataTypeEnum.LONG);	
		  itemRequestDescriptionParm.setParmValueLong(itemDescriptionParm.getParmValueNumeric().longValue());
	  } else if (itemDescriptionParm.getDataTypeCd().equals(ItemConstants.NUMBER)) {
		  itemRequestDescriptionParm.setDataType(DataTypeEnum.NUMBER);
		  itemRequestDescriptionParm.setParmValueBigDecimal(itemDescriptionParm.getParmValueNumeric());
	  } else if (itemDescriptionParm.getDataTypeCd().equals(ItemConstants.DATE)) {
		  itemRequestDescriptionParm.setDataType(DataTypeEnum.DATE);		  
		  itemRequestDescriptionParm.setParmValueDate(itemDescriptionParm.getParmValueDate());		  
	  } else if (itemDescriptionParm.getDataTypeCd().equals(ItemConstants.CLOB)) {
		  itemRequestDescriptionParm.setDataType(DataTypeEnum.CLOB);		  
		  itemRequestDescriptionParm.setParmValueClob(itemDescriptionParm.getParmValueClob());		  
	  }
//	  itemRequestDescriptionParm.setDataTypeCd(itemDescriptionParm.getDataTypeCd());
	  itemRequestDescriptionParm.setDisplaySequence(itemDescriptionParm.getDisplaySequence());
	  itemRequestDescriptionParm.setDisplayTextPost(itemDescriptionParm.getDisplayTextPost());
	  itemRequestDescriptionParm.setDisplayTextPre(itemDescriptionParm.getDisplayTextPre());
	  itemRequestDescriptionParm.setDisplayWidth(itemDescriptionParm.getDisplayWidth());
	  itemRequestDescriptionParm.setFieldLength(itemDescriptionParm.getFieldLength());
	  itemRequestDescriptionParm.setItemId(itemDescriptionParm.getItemId());
	  itemRequestDescriptionParm.setLabel(itemDescriptionParm.getLabel());
	  itemRequestDescriptionParm.setLabelOnYn(itemDescriptionParm.getLabelOn());
	  itemRequestDescriptionParm.setNewRowYn(itemDescriptionParm.getNewRow());
	  itemRequestDescriptionParm.setParmDisplayValue(itemDescriptionParm.getParmDisplayValue());
	  itemRequestDescriptionParm.setParmName(itemDescriptionParm.getParmName());
	  itemRequestDescriptionParm.setParmValue(itemDescriptionParm.getParmValue());
	  
//	  itemRequestDescriptionParm.setParmValueClob(itemDescriptionParm.getParmValueClob());
//	  itemRequestDescriptionParm.setParmValueDate(itemDescriptionParm.getParmValueDate());
//	  itemRequestDescriptionParm.setParmValueNumeric(itemDescriptionParm.getParmValueNumeric());

	  return itemRequestDescriptionParm;
  }
  
  private static ItemDescriptionParm getItemDescriptionParmFromItemRequestDescriptionParm(ItemDescriptionParm itemDescriptionParm, ItemRequestDescriptionParm itemRequestDescriptionParm) {
	  
	  itemDescriptionParm.setDataTypeCd(itemRequestDescriptionParm.getDataType().name());
	  itemDescriptionParm.setDisplaySequence(itemRequestDescriptionParm.getDisplaySequence());
	  itemDescriptionParm.setDisplayTextPost(itemRequestDescriptionParm.getDisplayTextPost());
	  itemDescriptionParm.setDisplayTextPre(itemRequestDescriptionParm.getDisplayTextPre());
	  itemDescriptionParm.setDisplayWidth(itemRequestDescriptionParm.getDisplayWidth());
	  itemDescriptionParm.setFieldLength(itemRequestDescriptionParm.getFieldLength());
	  itemDescriptionParm.setItemId(itemRequestDescriptionParm.getItemId());
	  itemDescriptionParm.setLabel(itemRequestDescriptionParm.getLabel());
	  itemDescriptionParm.setLabelOn(itemRequestDescriptionParm.getLabelOnYn());
	  itemDescriptionParm.setNewRow(itemRequestDescriptionParm.getNewRowYn());
	  itemDescriptionParm.setParmDisplayValue(itemRequestDescriptionParm.getParmDisplayValue());
	  itemDescriptionParm.setParmName(itemRequestDescriptionParm.getParmName());
	  itemDescriptionParm.setParmValue(itemRequestDescriptionParm.getParmValue());
	  itemDescriptionParm.setParmValueClob(itemRequestDescriptionParm.getParmValueClob());
	  itemDescriptionParm.setParmValueDate(itemRequestDescriptionParm.getParmValueDate());

	  if (itemRequestDescriptionParm.getDataType().equals(DataTypeEnum.STRING)) {
		itemDescriptionParm.setDataTypeCd(ItemConstants.STRING);
  	  }else if (itemRequestDescriptionParm.getDataType().equals(DataTypeEnum.NUMBER)) {
		itemDescriptionParm.setDataTypeCd(ItemConstants.NUMBER);
	  	itemDescriptionParm.setParmValueNumeric(itemRequestDescriptionParm.getParmValueBigDecimal());
	  } else if (itemRequestDescriptionParm.getDataType().equals(DataTypeEnum.INTEGER) ||
			     itemRequestDescriptionParm.getDataType().equals(DataTypeEnum.LONG)) {
		itemDescriptionParm.setDataTypeCd(ItemConstants.INTEGER);
		itemDescriptionParm.setParmValueNumeric(new BigDecimal(itemRequestDescriptionParm.getParmValueLong()));
	  } else if (itemRequestDescriptionParm.getDataType().equals(DataTypeEnum.BOOLEAN)) {
			itemDescriptionParm.setDataTypeCd(ItemConstants.STRING);
			if (itemRequestDescriptionParm.getParmValueBoolean() == true) {
				itemDescriptionParm.setParmValue(ItemConstants.YES_CD);
			} else {
				itemDescriptionParm.setParmValue(ItemConstants.NO_CD);				
			}
	  } else if (itemRequestDescriptionParm.getDataType().equals(DataTypeEnum.DATE)) {
			itemDescriptionParm.setDataTypeCd(ItemConstants.DATE);
			itemDescriptionParm.setParmValueDate(itemRequestDescriptionParm.getParmValueDate());
	  } else if (itemRequestDescriptionParm.getDataType().equals(DataTypeEnum.CLOB)) {
		  	itemDescriptionParm.setDataTypeCd(ItemConstants.CLOB);
		  	itemDescriptionParm.setParmValueClob(itemRequestDescriptionParm.getParmValueClob());
	  }

		  
		  return itemDescriptionParm;
  }

  private static ItemFees getAllFeesFromItemRequest (ItemFees itemFees, ItemRequestFees itemRequestFees) {

//	  itemFees.setCitySalesTax(itemRequestFees.getgetCitySalesTax());
//	  itemFees.setCountySalesTax(itemRequestFees.getCountySalesTax());
//	  itemFees.setStateSalesTax(itemRequestFees.getStateSalesTax());
//	  itemFees.setVat(itemRequestFees.getVat());
	  itemFees.setDiscount(itemRequestFees.getDiscount());
	  itemFees.setLicenseeFee(itemRequestFees.getLicenseeFee());
	  itemFees.setRightsholderFee(itemRequestFees.getRightsholderFee());
	  itemFees.setDistributionPayable(itemRequestFees.getRoyaltyPayable());
	  itemFees.setShippingFee1(itemRequestFees.getShippingFee1());
	  itemFees.setShippingFee2(itemRequestFees.getShippingFee2());			
	  itemFees.setShippingFee3(itemRequestFees.getShippingFee3());			
	  itemFees.setShippingFee4(itemRequestFees.getShippingFee4());			
	  itemFees.setShippingFee5(itemRequestFees.getShippingFee5());			
	  itemFees.setShippingFee6(itemRequestFees.getShippingFee6());		
	  itemFees.setHardCopyCost(itemRequestFees.getHardCopyCost());			
	  itemFees.setPriceAdjustment(itemRequestFees.getPriceAdjustment());	
	  itemFees.setTotalTax(itemRequestFees.getTax());	  					

	  
	  itemFees.setOrigDistAccount(itemRequestFees.getDistAccount());
	  itemFees.setOrigDistPartyId(itemRequestFees.getDistPartyId());
	  itemFees.setOrigDistPartyName(itemRequestFees.getDistPartyName());
	  itemFees.setOrigDistPtyInst(itemRequestFees.getDistPtyInst());
	  itemFees.setActualDistAccount(itemRequestFees.getDistAccount());
	  itemFees.setActualDistPartyId(itemRequestFees.getDistPartyId());
	  itemFees.setActualDistPartyName(itemRequestFees.getDistPartyName());
	  itemFees.setActualDistPtyInst(itemRequestFees.getDistPtyInst());

	  // TODO add royalty payable
	  //	  itemFees.setRoyaltyPayable(itemRequestFees.getRoyaltyPayable());

	  return itemFees;
  } 
  
  public static ItemRequestFees getItemRequestFees (ItemRequestFees itemRequestFees, ItemFees itemFees) {

//	  itemFees.setCitySalesTax(itemRequestFees.getCitySalesTax());
//	  itemFees.setCountySalesTax(itemRequestFees.getCountySalesTax());
//	  itemFees.setStateSalesTax(itemRequestFees.getStateSalesTax());
//	  itemFees.setVat(itemRequestFees.getVat());
	  itemRequestFees.setTax(itemFees.getTotalTax());
	  itemRequestFees.setDiscount(itemFees.getDiscount());
	  itemRequestFees.setLicenseeFee(itemFees.getLicenseeFee());
	  itemRequestFees.setRightsholderFee(itemFees.getRightsholderFee());
	  itemRequestFees.setDistAccount(itemFees.getOrigDistAccount());
	  itemRequestFees.setDistPartyId(itemFees.getOrigDistPartyId());
	  itemRequestFees.setDistPartyName(itemFees.getOrigDistPartyName());
	  itemRequestFees.setDistPtyInst(itemFees.getOrigDistPtyInst());
	  itemRequestFees.setRoyaltyPayable(itemFees.getDistributionPayable());
	  itemRequestFees.setPriceAdjustment(itemFees.getPriceAdjustment());
	  itemRequestFees.setShippingFee1(itemFees.getShippingFee1());
	  itemRequestFees.setShippingFee2(itemFees.getShippingFee2());
	  itemRequestFees.setShippingFee3(itemFees.getShippingFee3());
	  itemRequestFees.setShippingFee4(itemFees.getShippingFee4());
	  itemRequestFees.setShippingFee5(itemFees.getShippingFee5());
	  itemRequestFees.setShippingFee6(itemFees.getShippingFee6());
	  
	  // TODO add royalty payable
	  //	  itemFees.setRoyaltyPayable(itemRequestFees.getRoyaltyPayable());

	  return itemRequestFees;
  } 
  
  public static ItemRequest getRLItemRequestFromPurchasablePermission(PurchasablePermission purchasablePermission) {
	  
	  Item item = purchasablePermission.getItem();
	  
	  ItemRequest itemRequest = new ItemRequest(); 
	  itemRequest.setItemId(item.getItemId());
	  Map<String,ItemRequestDescriptionParm> itemRequestDescriptionParms = new HashMap<String, ItemRequestDescriptionParm>(0);
	  Map<String, ItemRequestParm> itemRequestParms = new HashMap<String, ItemRequestParm>(0);
	  List<ItemRequestFees> allRequestFees = new ArrayList<ItemRequestFees>(); 
	  
	  
	  for(ItemParm itemParm:item.getItemParms().values()){
		  ItemRequestParm itemRequestParm = new ItemRequestParm();
		  getItemRequestParmFromItemParm(itemRequestParm, itemParm);
		  itemRequestParms.put(itemRequestParm.getParmName(), itemRequestParm);
	  }
	  itemRequest.setItemRequestParms(itemRequestParms);
	  		
	  for (ItemDescriptionParm itemDescriptionParm : item.getItemDescriptionParms().values()) {
		  ItemRequestDescriptionParm itemRequestDescriptionParm = new ItemRequestDescriptionParm();
		  getItemRequestDescriptionParmFromItemDescriptionParm(itemRequestDescriptionParm, itemDescriptionParm);
		  itemRequestDescriptionParms.put(itemRequestDescriptionParm.getParmName(), itemRequestDescriptionParm);
		  LOGGER.debug("Item Request Description Parm: " + itemRequestDescriptionParm.getParmName() + " Value: " + itemRequestDescriptionParm.getParmValue());
	  }
	  itemRequest.setItemRequestDescriptionParms(itemRequestDescriptionParms);

	  for (ItemFees itemFees : item.getAllFees()) {
		  ItemRequestFees itemRequestFees = new ItemRequestFees();
		  getItemRequestFees (itemRequestFees, itemFees);
		  allRequestFees.add(itemRequestFees);
	  }
	  
	  itemRequest.setAllFees(allRequestFees);
	  
	  
	  
	  

			//tf right inst
//	  itemRequest.setRightId(item.getRightId());
			
			//tf wrk inst
//	  itemRequest.setExternalItemId(item.getExternalItemId());
			
			//wr wrk id
	  itemRequest.setItemSourceKey(item.getItemSourceKey());
			
			//set type of use
	  itemRequest.setExternalTouId(item.getExternalTouId());
	  
	  itemRequest.setCategoryName(item.getCategoryName());
	  itemRequest.setCategorySourceKey(item.getCategoryId()); 
	  itemRequest.setCategoryCd(item.getCategoryCd());
	  itemRequest.setTouName(item.getTouName());
	  itemRequest.setTouSourceKey(item.getTouSourceKey());	  
	  itemRequest.setPublisherCd(item.getPublisherCd());
//	  itemRequest.setRlCustomerId(item.getRightTierId());
	  itemRequest.setRlCustomerId(item.getRlCustomerId());
	  itemRequest.setTotalPrice(item.getTotalPrice());
	  
			//set product
	  for (ProductEnum productEnum : ProductEnum.values()) {
		  if (productEnum.name().equals(item.getProductCd())) {
			  itemRequest.setProduct(productEnum);
			  break;
		  }
	  }

//	  if (item.getPinningDtm() == null) {
//		  itemRequest.setPinningDtm(new Date()); 		  
//	  } else {
//		  itemRequest.setPinningDtm(item.getPinningDtm()); 
//	  }



	  return itemRequest;
	 
  }
    
  private static ItemRequest getItemRequestFromItem(Item item)
  {

	  ItemRequest itemRequest = new ItemRequest(); 
	  Map<String,ItemRequestDescriptionParm> itemRequestDescriptionParms = new HashMap<String, ItemRequestDescriptionParm>(0);
	  Map<String, ItemRequestParm> itemRequestParms = new HashMap<String, ItemRequestParm>(0);
	  
	  if (!item.getItemStatusCd().equals(ItemStatusEnum.CART)) {
		  itemRequest.setItemId(item.getItemId());	  
	  } else {
		  itemRequest.setItemId(null);
	  }
	  
	  itemRequest.setItemRequestDescriptionParms(itemRequestDescriptionParms);
	  itemRequest.setItemRequestParms(itemRequestParms);
	  		
	  for (ItemParm itemParm : item.getItemParms().values()) {
		  ItemRequestParm itemRequestParm = new ItemRequestParm();
		  getItemRequestParmFromItemParm(itemRequestParm, itemParm);
		  itemRequestParms.put(itemRequestParm.getParmName(), itemRequestParm);
	  }

	  for (ItemDescriptionParm itemDescriptionParm : item.getItemDescriptionParms().values()) {
		  ItemRequestDescriptionParm itemRequestDescriptionParm = new ItemRequestDescriptionParm();
		  getItemRequestDescriptionParmFromItemDescriptionParm(itemRequestDescriptionParm, itemDescriptionParm);
		  itemRequestDescriptionParms.put(itemRequestDescriptionParm.getParmName(), itemRequestDescriptionParm);
	  }
	  
			//tf right inst
	  itemRequest.setExternalRightId(item.getExternalRightId());
	  
//	  itemRequest.setRightId(item.getExternalRightId());
			
			//tf wrk inst
	  itemRequest.setExternalItemId(item.getExternalItemId());
	  	  			
			//wr wrk id
	  itemRequest.setItemSourceKey(item.getItemSourceKey());
			
			//set type of use
	  itemRequest.setExternalTouId(item.getExternalTouId());
	  
	  itemRequest.setCategoryName(item.getCategoryName());
	  itemRequest.setCategorySourceKey(item.getCategoryId());  
	  itemRequest.setCategoryCd(item.getCategoryCd());

	  itemRequest.setTouName(item.getTouName());
	  itemRequest.setTouSourceKey(item.getTouSourceKey());	  
	  itemRequest.setPublisherCd(item.getPublisherCd());
	  
			//set product
	  for (ProductEnum productEnum : ProductEnum.values()) {
		  if (productEnum.name().equals(item.getProductCd())) {
			  itemRequest.setProduct(productEnum);
			  break;
		  }
	  }

	  if (item.getPinningDtm() == null || (item.getItemStatusCd() != null && (item.getItemStatusCd().equals(ItemStatusEnum.CART)))) {
		  itemRequest.setPinningDtm(new Date()); 		  
	  } else {
		  itemRequest.setPinningDtm(item.getPinningDtm()); 
	  }
  
	  itemRequest.setCalculatedAvailabilityCd(item.getItemAvailabilityCd());
	  
	  LOGGER.debug("Right Id: " + itemRequest.getRightId());
	  LOGGER.debug("External Right Id: " + itemRequest.getExternalRightId());
	  LOGGER.debug("External Item Id: " + itemRequest.getExternalItemId());
	  LOGGER.debug("External Tou Id: " + itemRequest.getExternalTouId());
	  LOGGER.debug("Product: " + itemRequest.getProduct().name());
	  LOGGER.debug("Pinning DTM: " + itemRequest.getPinningDtm());

	  return itemRequest;
	  
  }
  
  private static ItemRequestFees getAllFeesFromItem (ItemFees itemFees, ItemRequestFees itemRequestFees) {

	  itemRequestFees.setDiscount(itemFees.getDiscount());
	  itemRequestFees.setLicenseeFee(itemFees.getLicenseeFee());
	  itemRequestFees.setRightsholderFee(itemFees.getRightsholderFee());
	  itemRequestFees.setRoyaltyPayable(itemFees.getDistributionPayable());
	  itemRequestFees.setPriceAdjustment(itemFees.getPriceAdjustment());
	  itemRequestFees.setHardCopyCost(itemFees.getHardCopyCost());
	  itemRequestFees.setTax(itemFees.getTotalTax());
	  
	  itemRequestFees.setShippingFee1(itemFees.getShippingFee1());
	  itemRequestFees.setShippingFee2(itemFees.getShippingFee2());
	  itemRequestFees.setShippingFee3(itemFees.getShippingFee3());
	  itemRequestFees.setShippingFee4(itemFees.getShippingFee4());
	  itemRequestFees.setShippingFee5(itemFees.getShippingFee5());
	  itemRequestFees.setShippingFee6(itemFees.getShippingFee6());

	  itemRequestFees.setDistAccount(itemFees.getOrigDistAccount());
	  itemRequestFees.setDistPartyId(itemFees.getOrigDistPartyId());
	  itemRequestFees.setDistPartyName(itemFees.getOrigDistPartyName());
	  itemRequestFees.setDistPtyInst(itemFees.getOrigDistPtyInst());
	  	  
	  return itemRequestFees;
  }

  public static ItemRequest getRLItemRequestFromOrderLicense(OrderPurchase orderPurchase, OrderLicense orderLicense)
  {

	  OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
	  Item item = orderItemImpl.getItem();
	  
	//  item.
	  
	  ItemRequest itemRequest = new ItemRequest(); 
	  itemRequest.setItemId(item.getItemId());
	  Map<String,ItemRequestDescriptionParm> itemRequestDescriptionParms = new HashMap<String, ItemRequestDescriptionParm>(0);
	  Map<String, ItemRequestParm> itemRequestParms = new HashMap<String, ItemRequestParm>(0);
	  
	  	  		
	  for (ItemParm itemParm : item.getItemParms().values()) {
		  ItemRequestParm itemRequestParm = new ItemRequestParm();
		  getItemRequestParmFromItemParm(itemRequestParm, itemParm);
		  itemRequestParms.put(itemRequestParm.getParmName(), itemRequestParm);
		  LOGGER.debug("Item Request Parm: " + itemRequestParm.getParmName() + " String Value: " + itemRequestParm.getParmValueString());
	  }

	  for (ItemDescriptionParm itemDescriptionParm : item.getItemDescriptionParms().values()) {
		  ItemRequestDescriptionParm itemRequestDescriptionParm = new ItemRequestDescriptionParm();
		  getItemRequestDescriptionParmFromItemDescriptionParm(itemRequestDescriptionParm, itemDescriptionParm);
		  itemRequestDescriptionParms.put(itemRequestDescriptionParm.getParmName(), itemRequestDescriptionParm);
		  LOGGER.debug("Item Request Description Parm: " + itemRequestDescriptionParm.getParmName() + " Value: " + itemRequestDescriptionParm.getParmValue());
	  }
	  
	  itemRequest.setItemRequestDescriptionParms(itemRequestDescriptionParms);
	  itemRequest.setItemRequestParms(itemRequestParms);
	  //itemRequest.getit

		//wr wrk id
	  itemRequest.setItemSourceCd(item.getItemSourceCd());
	  itemRequest.setItemSourceKey(item.getItemSourceKey());
			
			//set type of use
	  itemRequest.setExternalTouId(item.getExternalTouId());
	  
	  itemRequest.setCategoryName(item.getCategoryName());
	  itemRequest.setCategorySourceKey(item.getCategoryId()); 
	  itemRequest.setCategoryCd(item.getCategoryCd()); 
	  itemRequest.setTouName(item.getTouName());
	  itemRequest.setTouSourceKey(item.getTouSourceKey());	  
//	  itemRequest.setRlCustomerId(item.getRightTierId());
	  itemRequest.setRlCustomerId(item.getRlCustomerId());
	  itemRequest.setPublisherCd(item.getPublisherCd());

			//set product
	  for (ProductEnum productEnum : ProductEnum.values()) {
		  if (productEnum.name().equals(item.getProductCd())) {
			  itemRequest.setProduct(productEnum);
			  break;
		  }
	  }

	  itemRequest.setPinningDtm(item.getPinningDtm()); 

	  
	  // PLACEHOLDER FOR INJECTING VARIABLES INTO RIGHTSLINK BEANS (e.g. PO Number)
	  ItemRequestParm itemRequestParm = new ItemRequestParm();
	  itemRequestParm.setDataType(DataTypeEnum.STRING);
	  itemRequestParm.setRlSourceBeanCd(RlSourceBeanEnum.ORDER);
	  itemRequestParm.setParmName("CCC_PO_NUMBER");
	  if(orderPurchase!=null){
	  itemRequestParm.setParmValueString(orderPurchase.getPoNumber());
	  }
	  itemRequestParm.setItemId(item.getItemId());
	  itemRequestParms.put(itemRequestParm.getParmName(), itemRequestParm);
	  

	  // END PLACEHOLDER
	  
	  List<ItemRequestFees> allFees = new ArrayList<ItemRequestFees>(0);
	  //Set<ItemFees> allFees = new HashSet<ItemFees>(0);
	  
	  orderItemImpl.getItem().getAllFees();
	  Iterator<ItemFees> requestFeesIterator = orderItemImpl.getItem().getAllFees().iterator();
	  
	  if (requestFeesIterator.hasNext()) {
		  ItemFees itemFees = requestFeesIterator.next();
		  ItemRequestFees itemRequestFees = new ItemRequestFees();     
		  ItemRequestFees itemReqFees = getAllFeesFromItem (itemFees, itemRequestFees);
		 /* LOGGER.debug("Licensee Fee: " + itemFees.getLicenseeFee() +
				  	  " RH Fee: " + itemFees.getRightsholderFee() +
				  	  " Dist Payable: " + itemFees.getDistributionPayable()); */		  
		  allFees.add(itemReqFees);
	  }
	  
	  itemRequest.setAllFees(allFees);
	  
	  //itemRequest.setTotalPrice(orderItemImpl.getTotalPriceValue());
	  itemRequest.setTotalPrice(BigDecimal.valueOf(orderItemImpl.getPriceValueRaw()));
	  
	 
	  return itemRequest;
	  
  }

  public static void getPurchasablePermissionFromRLItemRequest(PurchasablePermission purchasablePermission, ItemRequest itemRequest)
  {

	  Item item = purchasablePermission.getItem();
	  
	  item.setTotalPrice(itemRequest.getTotalPrice());
	  LOGGER.debug("updateItemFromItemRequest - Total Price" + item.getTotalPrice());
	  
	  for (ItemRequestParm itemRequestParm : itemRequest.getItemRequestParms().values()) {
		  ItemParm itemParm = ItemHelperServices.getItemParm(item, itemRequestParm.getParmName(), true);
		  getItemParmFromItemRequestParm(itemParm, itemRequestParm);
		  itemParm.setItemId(item.getItemId());
		  LOGGER.debug("Item Parm: " + itemParm.getParmName() + " Value: " + itemParm.getParmValue());
	  }
  
	  // Remove parms that are no longer used
	  Iterator<ItemParm> i = item.getItemParms().values().iterator();
	  while (i.hasNext()) {
		  ItemParm itemParm = i.next();
		  if (itemRequest.getItemRequestParms().get(itemParm.getParmName()) == null) {
			  i.remove();
		  }
	  }
	  
	  Set<ItemFees> allFees = new HashSet<ItemFees>(0);
	  Iterator<ItemRequestFees> requestFeesIterator = itemRequest.getAllFees().iterator();
	  if (requestFeesIterator.hasNext()) {
		  ItemRequestFees itemRequestFees = requestFeesIterator.next();
		  ItemFees itemFees = new ItemFees();     
		  getAllFeesFromItemRequest (itemFees, itemRequestFees);
		  LOGGER.debug("Licensee Fee: " + itemFees.getLicenseeFee() +
				  	  " RH Fee: " + itemFees.getRightsholderFee() +
				  	  " Dist Payable: " + itemFees.getDistributionPayable());		  
		  allFees.add(itemFees);
	  }
	  
	  item.setAllFees(allFees);
	  
	  Map<String,ItemDescriptionParm> itemDescriptionParms = new HashMap<String, ItemDescriptionParm>(0);
	  
	  for (ItemRequestDescriptionParm itemRequestDescriptionParm : itemRequest.getItemRequestDescriptionParms().values()) {
		  ItemDescriptionParm itemDescriptionParm = ItemHelperServices.getItemDescriptionParm (item, itemRequestDescriptionParm.getParmName(), true);
		  getItemDescriptionParmFromItemRequestDescriptionParm(itemDescriptionParm,itemRequestDescriptionParm);
		  itemDescriptionParms.put(itemDescriptionParm.getParmName(), itemDescriptionParm);
		  LOGGER.debug("Item Request Description Parm: " + itemDescriptionParm.getParmName() + " Value: " + itemDescriptionParm.getParmValue());
	  }
	  
	  // Remove parms that are no longer used
	  Iterator<ItemDescriptionParm> id = item.getItemDescriptionParms().values().iterator();
	  while (id.hasNext()) {
		  ItemDescriptionParm itemDescriptionParm = id.next();
		  if (itemRequest.getItemRequestDescriptionParms().get(itemDescriptionParm.getParmName()) == null) {
			  id.remove();
		  }
	  }
	  
	  item.setItemDescriptionParms(itemDescriptionParms);
	  
//	  if (itemRequest.getRightSourceCd().equalsIgnoreCase(RightSourceEnum.TF.name())) {
//		  item.setRightAvailabilityCd(itemRequest.getRightAvailabilityCd());
//		  item.setItemAvailabilityCd(ItemAvailabilityEnum.getEnumForRLPermissionCd(itemRequest.getRightAvailabilityCd()).getStandardPermissionCd());
//	  } else {
//		  item.setRightAvailabilityCd(itemRequest.getRightAvailabilityCd());
//		  item.setItemAvailabilityCd(ItemAvailabilityEnum.getEnumForRLPermissionCd(itemRequest.getRightAvailabilityCd()).getStandardPermissionCd());		  
//	  }
	  
	  ItemRequestParm RlProductParm = itemRequest.getItemRequestParms().get(ItemConstants.RL_PRODUCT_PARM);
	  if (RlProductParm != null) {
		  if (RlProductParm.getParmValueString().equals(ItemConstants.RL_PRODUCT_REPRINT_PARM_VALUE)) {
			   item.setProductName(com.copyright.ccc.business.services.ProductEnum.RLR.getProductName()); 
			   item.setProductCd(com.copyright.ccc.business.services.ProductEnum.RLR.name()); 
			   try {
				   	Long rlRightId = new Long (RlProductParm.getParmValueString());
			   		item.setExternalRightId(rlRightId);
			   } catch (Exception e) {
					LOGGER.error("error raised; setting externalRightId=null " + LogUtil.appendableStack(e));				   
				    item.setExternalRightId(null);
			   }
		  }
	  }
	  
	  ItemRequestParm publisherProductParm = itemRequest.getItemRequestParms().get(ItemConstants.RL_PUBLISHERID_PARM);
	  ItemRequestParm typeOfUseProductParm = itemRequest.getItemRequestParms().get(ItemConstants.RL_TYPEOFUSEID_PARM);
	  if (publisherProductParm != null && typeOfUseProductParm != null &&
		  publisherProductParm.getParmValueString() != null && typeOfUseProductParm.getParmValueString() != null) {
		  if (publisherProductParm.getParmValueString().equals(ItemConstants.RL_BMJ_PUBLISHERID) &&
			  typeOfUseProductParm.getParmValueString().equals(ItemConstants.RL_BMJ_TYPEOFUSEID )) {
			   item.setProductName(com.copyright.ccc.business.services.ProductEnum.RL.getProductName()); 
			   item.setProductCd(com.copyright.ccc.business.services.ProductEnum.RL.name()); 			  
		  }
		  
		  if (publisherProductParm.getParmValueString().equals(ItemConstants.RL_NYT_PUBLISHERID) &&
				  typeOfUseProductParm.getParmValueString().equals(ItemConstants.RL_NYT_TYPEOFUSEID )) {
				   item.setProductName(com.copyright.ccc.business.services.ProductEnum.RL.getProductName()); 
				   item.setProductCd(com.copyright.ccc.business.services.ProductEnum.RL.name()); 			  
			  }
	  }
	  

	  if (item.getProductCd().equals(com.copyright.ccc.business.services.ProductEnum.RLR.name())) {
		  	item.setIsSpecialOrder(true);
		  	item.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
		  	item.setRightAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());		  
	  } else {
		if (itemRequest.getSpecialOrderType().getCd().equalsIgnoreCase(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER.getCode())) {
		  	item.setIsSpecialOrder(false);
		  	item.setItemAvailabilityCd(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd());
		  	item.setRightAvailabilityCd(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd());
	  	} else {
		  	item.setIsSpecialOrder(true);
		  	item.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
		  	item.setRightAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
	  	}
	  }

	  if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.FEE_FORMULA_INCALCULABLE)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.FEE_FORMULA_INCALCULABLE);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.BIBLIO_PAGES_MISSING_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.BIBLIO_PAGES_MISSING_SPECIAL_ORDER);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.MULTIPLE_RIGHTS_FOUND_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.MULTIPLE_RIGHTS_FOUND_SPECIAL_ORDER);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.NOT_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.RESEARCH_FURTHER_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.RESEARCH_FURTHER_SPECIAL_ORDER);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER);
	  }

//	  item.setSpecialOrderTypeCd(itemRequest.getSpecialOrderType().name());
  
//	  item.setRightId(itemRequest.getRightId());
	  item.setPinningDtm(itemRequest.getPinningDtm());
	  item.setCategoryCd(itemRequest.getCategoryCd());
	  item.setCategoryName(itemRequest.getCategoryName());
	  item.setCategoryId(itemRequest.getCategorySourceKey());
	  item.setCategoryCd(itemRequest.getCategoryCd());
	  item.setTouName(itemRequest.getTouName());
	  item.setTouSourceKey(itemRequest.getTouSourceKey());
	  item.setExternalTouId(itemRequest.getExternalTouId());
//	  item.setRightBeginDate(itemRequest.getRightPubBeginDate());
//	  item.setRightEndDate(itemRequest.getRightPubEndDate());
	
	  item.setLicenseTerms(itemRequest.getLicenseTerms());
	  item.setPublisherCd(itemRequest.getPublisherCd());
//	  item.setRightTierId(itemRequest.getRlCustomerId());
	  item.setRlCustomerId(itemRequest.getRlCustomerId());

	// TODO get license terms and special confirmation terms
	//	purchasablePermissionImpl.setLicenseTerms(LicenseTerms);
//	purchasablePermissionImpl.setSpecialConfirmationTerms(SpecialConfirmationTerms)
	  
	  item.setRlDetailHtml(itemRequest.getRlDetailHtml());
		
  }  


  
  private static void getItemFromItemRequest(Item item, ItemRequest itemRequest)
  {

	  item.setTotalPrice(itemRequest.getTotalPrice());
	  item.setRightTierId(itemRequest.getTierInst());
	  
	  if (item.getRightTierId() != null) {
		  item.setHasVolumePricing(true);	  
	  } else {
		  item.setHasVolumePricing(false);	  	  
	  }
	  
	  LOGGER.debug("updateItemFromItemRequest - Total Price" + item.getTotalPrice());
	  
	  for (ItemRequestParm itemRequestParm : itemRequest.getItemRequestParms().values()) {
		  ItemParm itemParm = ItemHelperServices.getItemParm(item, itemRequestParm.getParmName(), true);
		  getItemParmFromItemRequestParm(itemParm, itemRequestParm);
		  itemParm.setItemId(item.getItemId());
//		  LOGGER.debug("Item Parm: " + itemParm.getParmName() + " Value: " + itemParm.getParmValue());
	  }
  
	  // Remove parms that are no longer used
	  Iterator<ItemParm> i = item.getItemParms().values().iterator();
	  while (i.hasNext()) {
		  ItemParm itemParm = i.next();
		  if (itemRequest.getItemRequestParms().get(itemParm.getParmName()) == null) {
			  i.remove();
		  }
	  }
	  
	  Set<ItemFees> allFees = new HashSet<ItemFees>(0);
	  Iterator<ItemRequestFees> requestFeesIterator = itemRequest.getAllFees().iterator();
	  if (requestFeesIterator.hasNext()) {
		  ItemRequestFees itemRequestFees = requestFeesIterator.next();
		  ItemFees itemFees = new ItemFees();     
		  getAllFeesFromItemRequest (itemFees, itemRequestFees);
		  LOGGER.debug("Licensee Fee: " + itemFees.getLicenseeFee() +
				  	  " RH Fee: " + itemFees.getRightsholderFee() +
				  	  " Dist Payable: " + itemFees.getDistributionPayable());		  
		  allFees.add(itemFees);
	  }
	  
	  item.setAllFees(allFees);
	  
	  

//TODO Sort this out
//	  item.setRightTierId(itemRequest.getHasVolPriceTiers());
	  
//	  if (itemRequest.getRightSourceCd().equalsIgnoreCase(RightSourceEnum.TF.name())) {
		  item.setRightAvailabilityCd(itemRequest.getRightAvailabilityCd());
		  if (itemRequest.getCalculatedAvailabilityCd() != null) {
			  item.setItemAvailabilityCd(ItemAvailabilityEnum.getEnumForTFPermissionCd(itemRequest.getCalculatedAvailabilityCd()).getStandardPermissionCd());
			  item.setItemOrigAvailabilityCd(ItemAvailabilityEnum.getEnumForTFPermissionCd(itemRequest.getCalculatedAvailabilityCd()).getStandardPermissionCd());
		  } else {
			  item.setItemAvailabilityCd(null);
			  item.setItemOrigAvailabilityCd(null);
		  }
		  item.setRightSourceCd(RightSourceEnum.TF.name());
		  
		  //	  } else {
//		  item.setRightAvailabilityCd(itemRequest.getRightAvailabilityCd());
//		  item.setItemAvailabilityCd(ItemAvailabilityEnum.getEnumForRLPermissionCd(itemRequest.getRightAvailabilityCd()).getStandardPermissionCd());		  
//	  }
	  
		  
//	  item.setIsSpecialOrder(ItemAvailabilityEnum.getEnumForItemAvailabilityCd(item.getItemAvailabilityCd()).isSpecialOrder());
	  
	  if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.CONTACT_RIGHTSHOLDER_SPECIAL_ORDER);
		  item.setIsSpecialOrder(true);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.FEE_FORMULA_INCALCULABLE)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.FEE_FORMULA_INCALCULABLE);
		  item.setIsSpecialOrder(true);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER);
		  item.setIsSpecialOrder(true);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.BIBLIO_PAGES_MISSING_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.BIBLIO_PAGES_MISSING_SPECIAL_ORDER);
		  item.setIsSpecialOrder(true);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.MULTIPLE_RIGHTS_FOUND_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.MULTIPLE_RIGHTS_FOUND_SPECIAL_ORDER);
		  item.setIsSpecialOrder(true);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.NOT_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.NOT_SPECIAL_ORDER);
		  item.setIsSpecialOrder(false);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.RESEARCH_FURTHER_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.RESEARCH_FURTHER_SPECIAL_ORDER);
		  item.setIsSpecialOrder(true);
	  } else if (itemRequest.getSpecialOrderType().equals(com.copyright.svc.rightsResolver.api.data.SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER)) {
		  item.setSpecialOrderTypeCd(SpecialOrderTypeEnum.RIGHT_NOT_FOUND_SPECIAL_ORDER);
		  item.setIsSpecialOrder(true);
	  }

//	  Auto invoice flag is now initialized at checkout
//	  item.setAutoInvoice(true);
	  item.setExternalRightId(itemRequest.getExternalRightId());
	  item.setRightId(itemRequest.getRightId());
	  item.setPinningDtm(itemRequest.getPinningDtm());
	  item.setTfWksInst(itemRequest.getWorkSetId());
	  item.setCategoryCd(itemRequest.getCategoryCd());
	  item.setCategoryName(itemRequest.getCategoryName());
	  item.setCategoryId(itemRequest.getCategorySourceKey());
	  item.setCategoryCd(itemRequest.getCategoryCd());
	  item.setTouName(itemRequest.getTouName());
	  item.setTouSourceKey(itemRequest.getTouSourceKey());
	  item.setExternalTouId(itemRequest.getExternalTouId());
	  item.setRightBeginDate(itemRequest.getRightPubBeginDate());
	  item.setRightEndDate(itemRequest.getRightPubEndDate());
	  item.setPublisherCd(itemRequest.getPublisherCd());
	  item.setRightQualifierTerms(itemRequest.getRightExternalComments());
	  item.setWorkQualifierTerms(itemRequest.getRightQualifier());
	  
	// TODO get license terms and special confirmation terms
	//	purchasablePermissionImpl.setLicenseTerms(LicenseTerms);
//	purchasablePermissionImpl.setSpecialConfirmationTerms(SpecialConfirmationTerms)

		
  }  
  
  public static boolean isPriceDifferent(BigDecimal totalPrice, BigDecimal originalPrice){
	  
	  BigDecimal totalPriceValue= totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	  BigDecimal orginalPriceValue=  originalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN); 
	  
	  if(totalPriceValue.compareTo(orginalPriceValue)==0){
		  return false;
	  }
	  return true;
		 		
  	
  }
  
  private static void initRightsholdersForItem(Item item) {

  	TreeMap<Long, RightsholderOrganization> userMap = new TreeMap<Long, RightsholderOrganization>();
  	Long ptyInst = null;

 		for (ItemFees itemFees : item.getAllFees()) {
     		try {      	
     			//TODO getByrInst() sometimes throws an exception on null to long conversion
     			ptyInst = itemFees.getOrigDistPtyInst();
     			if (ptyInst != null) {
     				if (userMap.containsKey(ptyInst)) {
     			    	RightsholderOrganization rhOrg;
     					rhOrg = userMap.get(ptyInst);
     					String rhAcct = null;
     					if (rhOrg.getRhAccountNumber() != null) {
     						rhAcct = rhOrg.getRhAccountNumber().toString();
     					}
     					itemFees.setOrigDistAccount(rhAcct);
     					itemFees.setOrigDistPartyId(rhOrg.getOrgPartyId());
     					itemFees.setActualDistAccount(rhAcct);
     					itemFees.setActualDistPartyId(rhOrg.getOrgPartyId());
     				} else {
//                  		byrInstLong = new Long (98); // For Dev Testing
     			    	RightsholderOrganization rhOrg;
     					rhOrg = UserServices.getOrganizationByPtyInst(ptyInst);
     					userMap.put(ptyInst, rhOrg);
     					String rhAcct = null;
     					if (rhOrg.getRhAccountNumber() != null) {
     						rhAcct = rhOrg.getRhAccountNumber().toString();
     					}
     					itemFees.setOrigDistAccount(rhAcct);
     					itemFees.setOrigDistPartyId(rhOrg.getOrgPartyId());
     					itemFees.setActualDistAccount(rhAcct);
     					itemFees.setActualDistPartyId(rhOrg.getOrgPartyId());
     				}
     			}
     		}
     		catch (Exception e) {
     			LOGGER.error(LogUtil.getStack(e));
     		}
  			
 		}
 	}
}
