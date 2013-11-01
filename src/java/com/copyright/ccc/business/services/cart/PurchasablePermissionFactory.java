package com.copyright.ccc.business.services.cart;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.copyright.base.Constants;
import com.copyright.base.enums.TypeOfUseEnum;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.data.WRStandardWork;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ProductEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.order.OrderItemImpl;
import com.copyright.ccc.business.services.search.MapperServices;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.config.ApplicationResources;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.data.inventory.LicenseeClass;
import com.copyright.data.inventory.StandardWork;
import com.copyright.data.inventory.Work;
import com.copyright.data.order.PermissionRequest;
import com.copyright.data.order.UsageData;
import com.copyright.data.order.UsageDataAcademic;
import com.copyright.data.order.UsageDataEmail;
import com.copyright.data.order.UsageDataNet;
import com.copyright.data.order.UsageDataPhotocopy;
import com.copyright.data.order.UsageDataRepublication;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemParm;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.rightsResolver.api.data.ItemParmEnum;
import com.copyright.svc.rightsResolver.api.data.RightFee;
import com.copyright.svc.rightsResolver.api.data.TouCategoryTypeEnum;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalConsumerContext;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalResult;


/**
 * Set of factory methods reponsible for building
 * instances of <code>PurchasablePermission</code>
 */
public final class PurchasablePermissionFactory {

  private static final Logger LOGGER = Logger.getLogger( PurchasablePermissionFactory.class );
  private static final String PUBLICATION_TYPE_BOOK = "Book";

  private PurchasablePermissionFactory(){}
  
  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, photocopy type.
   */	
	public static PurchasablePermission createPhotocopySpecialOrderPurchasablePermission(){
		
		PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl();

		purchasablePermission.setRightId(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
//		PermissionRequest permissionRequest = new PermissionRequest(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
		purchasablePermission.setSpecialOrderTypeCdToManualSpecialOrder();
		purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
		
//		purchasablePermission.setTouSourceKey(ECommerceConstants.PHOTOCOPY_TPU_CODE);
	    purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.RIGHT_TO_PHOTOCOPY.getId()));
		purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.TRS.getProductSourceKey()));
		purchasablePermission.setProductCd(ProductEnum.TRS.name());
		purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());
		purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());
		
		purchasablePermission.setCategoryId(TouCategoryTypeEnum.PHOTO_COPY_FOR_GENERAL_BUSINESS_OR_ACADEMIC_USE.getCategoryId());
		purchasablePermission.setCategoryCd(TouCategoryTypeEnum.PHOTO_COPY_FOR_GENERAL_BUSINESS_OR_ACADEMIC_USE.name());
		purchasablePermission.setCategoryName(TouCategoryTypeEnum.PHOTO_COPY_FOR_GENERAL_BUSINESS_OR_ACADEMIC_USE.getDesc());

		purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());

//		UsageDataPhotocopy usageDataPhotocopy =  new UsageDataPhotocopy();
//		usageDataPhotocopy.setTpuInst( ECommerceConstants.PHOTOCOPY_TPU_CODE );
//		usageDataPhotocopy.setProduct( ECommerceConstants.TRS_PRODUCT_CODE );
    
//    permissionRequest.setUsageData( usageDataPhotocopy );
  
		initPurchasablePermissionFromStandardWork ( purchasablePermission, new WRStandardWork());
		
		return purchasablePermission;
	}

   /**
    * Creates a special order instance of <code>PurchasablePermission</code>, e-mail type.
    */ 
	public static PurchasablePermission createEmailSpecialOrderPurchasablePermission(){
	  
		PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl();

		purchasablePermission.setRightId(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
		purchasablePermission.setSpecialOrderTypeCdToManualSpecialOrder();
		purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());

//		purchasablePermission.setTouSourceKey(ECommerceConstants.EMAIL_TPU_CODE);
//		purchasablePermission.setProductSourceKey(ECommerceConstants.DPS_PRODUCT_CODE);
	    purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.EMAIL_DIGITAL_TRANSMISSION.getId()));
		purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.DPS.getProductSourceKey()));
		purchasablePermission.setProductCd(ProductEnum.DPS.name());
		purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());

		purchasablePermission.setCategoryId(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.getCategoryId());
		purchasablePermission.setCategoryCd(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.name());
		purchasablePermission.setCategoryName(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.getDesc());
		
		purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());
		purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());

		
		initPurchasablePermissionFromStandardWork ( purchasablePermission, new WRStandardWork());
		
		return purchasablePermission;
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, republication type.
   */ 
	public static PurchasablePermission createRepublicationSpecialOrderPurchasablePermission( long tpuInst ){
  	      	  
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl();
	  purchasablePermission.setRightId(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
	  purchasablePermission.setSpecialOrderTypeCdToManualSpecialOrder();
	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
		
//	  purchasablePermission.setTouSourceKey(ECommerceConstants.RLS_PRODUCT_CODE);
//	  purchasablePermission.setProductSourceKey( tpuInst);
	  purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.OTHER_BOOK.getId()));
	  purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.RLS.getProductSourceKey()));
	  purchasablePermission.setProductCd(ProductEnum.RLS.name());
	  purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());
	  purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());

	  purchasablePermission.setCategoryId(TouCategoryTypeEnum.REPUBLISH_OR_DISPLAY_CONTENT.getCategoryId());
	  purchasablePermission.setCategoryCd(TouCategoryTypeEnum.REPUBLISH_OR_DISPLAY_CONTENT.name());
	  purchasablePermission.setCategoryName(TouCategoryTypeEnum.REPUBLISH_OR_DISPLAY_CONTENT.getDesc());

	  
	  initPurchasablePermissionFromStandardWork ( purchasablePermission, new WRStandardWork());
		
	  return purchasablePermission;
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, internet type.
   */
	public static PurchasablePermission createInternetSpecialOrderPurchasablePermission(){
   
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl();
	  purchasablePermission.setRightId(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
	  purchasablePermission.setSpecialOrderTypeCdToManualSpecialOrder();
	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
		
//	  purchasablePermission.setTouSourceKey(ECommerceConstants.INTERNET_TPU_CODE);
//	  purchasablePermission.setProductSourceKey(ECommerceConstants.DPS_PRODUCT_CODE);
	  purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.INTERNET.getId()));
	  purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.DPS.getProductSourceKey()));
	  purchasablePermission.setProductCd(ProductEnum.DPS.name());
	  purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());
	  purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());

	  purchasablePermission.setCategoryId(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.getCategoryId());
	  purchasablePermission.setCategoryCd(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.name());
	  purchasablePermission.setCategoryName(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.getDesc());

	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());

	  initPurchasablePermissionFromStandardWork ( purchasablePermission, new WRStandardWork());
		
	  return purchasablePermission;	  
	  
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, intranet type.
   */
	public static PurchasablePermission createIntranetSpecialOrderPurchasablePermission(){
    
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl();
	  purchasablePermission.setRightId(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
	  purchasablePermission.setSpecialOrderTypeCdToManualSpecialOrder();
	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
		
//	  purchasablePermission.setTouSourceKey(ECommerceConstants.INTRANET_TPU_CODE);
//	  purchasablePermission.setProductSourceKey(ECommerceConstants.DPS_PRODUCT_CODE);
	  purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.INTRANET.getId()));
	  purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.DPS.getProductSourceKey()));
	  purchasablePermission.setProductCd(ProductEnum.DPS.name());
	  purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());
	  purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());

	  purchasablePermission.setCategoryId(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.getCategoryId());
	  purchasablePermission.setCategoryCd(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.name());
	  purchasablePermission.setCategoryName(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.getDesc());

	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());

	  initPurchasablePermissionFromStandardWork ( purchasablePermission, new WRStandardWork());
		
	  return purchasablePermission;	  
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, extranet type.
   */
	public static PurchasablePermission createExtranetSpecialOrderPurchasablePermission(){
    
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl();
	  purchasablePermission.setRightId(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
	  purchasablePermission.setSpecialOrderTypeCdToManualSpecialOrder();
	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
		
//	  purchasablePermission.setTouSourceKey(ECommerceConstants.EXTRANET_TPU_CODE);
//	  purchasablePermission.setProductSourceKey(ECommerceConstants.DPS_PRODUCT_CODE);
	  purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.EXTRANET.getId()));
	  purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.DPS.getProductSourceKey()));
	  purchasablePermission.setProductCd(ProductEnum.DPS.name());
	  purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());
	  purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());

	  purchasablePermission.setCategoryId(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.getCategoryId());
	  purchasablePermission.setCategoryCd(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.name());
	  purchasablePermission.setCategoryName(TouCategoryTypeEnum.EMAIL_TO_CO_WORKERS_OR_POST_TO_INTRANET.getDesc());

	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());

	  initPurchasablePermissionFromStandardWork ( purchasablePermission, new WRStandardWork());
		
	  return purchasablePermission;	  
    
	}

  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, APS type.
   */
  public static PurchasablePermission createAPSSpecialOrderPurchasablePermission(){
  
	  PurchasablePermissionImpl purchasablePermission = createAcademicSpecialOrderPurchasablePermission();
	  purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.RIGHT_TO_PHOTOCOPY.getId()));
	  purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.APS.getProductSourceKey()));
	  purchasablePermission.setProductCd(ProductEnum.APS.name());
	  purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());
	  purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());
	  purchasablePermission.setCategoryId(TouCategoryTypeEnum.USE_IN_PRINT_COURSE_MATERIALS.getCategoryId());
	  purchasablePermission.setCategoryCd(TouCategoryTypeEnum.USE_IN_PRINT_COURSE_MATERIALS.name());
	  purchasablePermission.setCategoryName(TouCategoryTypeEnum.USE_IN_PRINT_COURSE_MATERIALS.getDesc());
	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());

	  return purchasablePermission;
  }
  
  /**
   * Creates a special order instance of <code>PurchasablePermission</code>, ECCS type.
   */
  public static PurchasablePermission createECCSSpecialOrderPurchasablePermission(){
    
    PurchasablePermissionImpl purchasablePermission = createAcademicSpecialOrderPurchasablePermission();
    
//	purchasablePermission.setTouSourceKey(ECommerceConstants.ECCS_TPU_CODE);
//	purchasablePermission.setProductSourceKey(ECommerceConstants.ECCS_PRODUCT_CODE);
    purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.RIGHT_TO_SCAN.getId()));
	purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.ECC.getProductSourceKey()));
	purchasablePermission.setProductCd(ProductEnum.ECC.name());
	purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());
	purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());
	purchasablePermission.setCategoryId(TouCategoryTypeEnum.USE_IN_ELECTRONIC_COURSE_MATERIAL.getCategoryId());
	purchasablePermission.setCategoryCd(TouCategoryTypeEnum.USE_IN_ELECTRONIC_COURSE_MATERIAL.name());
	purchasablePermission.setCategoryName(TouCategoryTypeEnum.USE_IN_ELECTRONIC_COURSE_MATERIAL.getDesc());
	purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());

    return purchasablePermission;
  }

  public static List<PurchasablePermission> createPurchasablePermissions( List<Item> items ){
  
  if( items == null )
  {
    LOGGER.error( "items list cannot be null" );
    throw new IllegalArgumentException( "PermissionRequest list cannot be null" );
  }
  
  List<PurchasablePermission> purchasablePermissions = new ArrayList<PurchasablePermission>();
  
  if( items.isEmpty() )
  {
    return purchasablePermissions;
  }
  
  if( !items.isEmpty() && items!= null )
  {
   Iterator<Item> itemsIterator = items.iterator();
    
    while( itemsIterator.hasNext() )
    {
      try
      {
        Item currentItem = itemsIterator.next();
        
        PurchasablePermission currentPurchasablePermission =
                PurchasablePermissionFactory.createPurchasablePermission( currentItem );
        
        purchasablePermissions.add( currentPurchasablePermission );
      }
      catch ( ClassCastException e )
      {
        LOGGER.error( "PurchasablePermissionFactory: could not cast to PermissionRequest" );
        throw new UnableToBuildPurchasablePermissionException("PurchasablePermissionFactory: could not cast to PermissionRequest", e);
      }
    }
  } 
  
  return purchasablePermissions;
}
  
  /**
   * Creates an instance of <code>PurchasablePermission</code>, based on an instance of <code>OrderLicense</code>.
   */
  public static PurchasablePermission createPurchasablePermission( OrderLicense orderLicense )
  {
      return createPurchasablePermission( orderLicense, false );
  }
  
  /**
   * Creates an instance of <code>PurchasablePermission</code>, based on an instance of <code>OrderLicense</code>.  If checkRightValidity
   * is true, then it checks to make sure that the rgtInst passed in is valid for the current date, i.e. if the rgtInst of the License
   * is no longer valid for today's date, then use -1 for the rgtInst.
   */
  public static PurchasablePermission createPurchasablePermission( OrderLicense orderLicense, boolean checkRightValidity )
  {
    if( orderLicense == null )
    {
      LOGGER.error( "OrderLicense cannot be null" );
      throw new IllegalArgumentException( "OrderLicense cannot be null" );
    }
    
    PurchasablePermissionImpl ppImpl = new PurchasablePermissionImpl();
    
	ppImpl.setItemStatusCd(ItemStatusEnum.CART.name());
	
    // Initialize Category / Product /Tou attributes
    ppImpl.setProductCd(orderLicense.getProductCd());
    ppImpl.setProductSourceKey(orderLicense.getProductSourceKey());
    ppImpl.setProductName(orderLicense.getProductName());
    ppImpl.setRightSourceCd(orderLicense.getRightSourceCd());
    
    ppImpl.setCategoryCd(orderLicense.getCategoryCd());
    ppImpl.setCategoryId(orderLicense.getCategoryId());
    ppImpl.setCategoryName(orderLicense.getCategoryName());
    
    ppImpl.setTouName(orderLicense.getTouName());
    ppImpl.setTouSourceKey(orderLicense.getTouSourceKey());
    ppImpl.setExternalTouId(orderLicense.getExternalTouId());
    
    if (orderLicense.getRightId() != null) {
    	ppImpl.setRightId(orderLicense.getRightId());
    }
    
    if (orderLicense.getExternalRightId() != null) {
    	ppImpl.setExternalRightId(orderLicense.getExternalRightId());
    }
    
    if (orderLicense.getPinningDate() != null) {
        ppImpl.setPinningDate(orderLicense.getPinningDate());    	
    }
   
    if (orderLicense.getTotalPriceValue() != null) {
    	ppImpl.setTotalPrice(orderLicense.getTotalPriceValue());
    }
    if (orderLicense.getSpecialOrderTypeCd() != null ) ppImpl.setSpecialOrderTypeCd(orderLicense.getSpecialOrderTypeCd());  
    
    // Initialize Pricing Attributes
    if (orderLicense.getItemAvailabilityCd() != null ) ppImpl.setItemAvailabilityCd(orderLicense.getItemAvailabilityCd());
    if (orderLicense.getItemOrigAvailabilityCd() != null ) ppImpl.setItemOrigAvailabilityCd(orderLicense.getItemOrigAvailabilityCd());
    if (orderLicense.getItemOrigAvailabilityCd() != null ) ppImpl.setRightAvailabilityCd(orderLicense.getItemOrigAvailabilityCd());
    
    ppImpl.setRightQualifierTerms(orderLicense.getRightQualifierTerms());
    ppImpl.setLicenseTerms(orderLicense.getLicenseTerms());
    ppImpl.setResolutionTerms(orderLicense.getResolutionTerms());
    ppImpl.setExternalCommentTerm(orderLicense.getExternalCommentTerm());
    
    if (orderLicense.getTotalPriceValue() != null) {
        ppImpl.setTotalPrice(orderLicense.getTotalPriceValue());    	
    }
    
    if (orderLicense.getHasVolPriceTiers() != null)
    {
    	if (orderLicense.getHasVolPriceTiers().equalsIgnoreCase(ItemConstants.YES_CD)) {
    		ppImpl.setHasVolPriceTiers(true);
    	} else {
    		ppImpl.setHasVolPriceTiers(false);    	
    	}
    }
    else
    {
    	ppImpl.setHasVolPriceTiers(false); 
    }
    
    // Initialize Work attributes
    ppImpl.setExternalItemId(orderLicense.getExternalItemId());
    ppImpl.setItemSourceCd(orderLicense.getItemSourceCd());
    ppImpl.setItemSourceKey(orderLicense.getItemSourceKey());
    ppImpl.setItemDescription(orderLicense.getItemDescription());
    ppImpl.setItemSubSourceCd(orderLicense.getItemSubSourceCd());
    ppImpl.setItemSubSourceKey(orderLicense.getItemSubSourceKey());
    ppImpl.setItemSubDescription(orderLicense.getItemSubDescription());
    ppImpl.setItemTypeCd(orderLicense.getItemTypeCd());
    
    ppImpl.setStandardNumber(orderLicense.getStandardNumber());
    ppImpl.setAuthor(orderLicense.getAuthor());
    ppImpl.setEditor(orderLicense.getEditor());
    ppImpl.setVolume(orderLicense.getVolume());
    ppImpl.setEdition(orderLicense.getEdition());
    ppImpl.setPublisher(orderLicense.getPublisher());
    ppImpl.setPublicationYear(orderLicense.getPublicationYear());
    
    ppImpl.setSeries(orderLicense.getSeries());
    ppImpl.setSeriesNumber(orderLicense.getSeriesNumber());
    ppImpl.setPublicationType(orderLicense.getPublicationType());
    ppImpl.setCountry(orderLicense.getCountry());
    ppImpl.setLanguage(orderLicense.getLanguage());
    ppImpl.setIdnoLabel(orderLicense.getIdnoLabel());
    ppImpl.setIdnoTypeCd(orderLicense.getIdnoTypeCd());
    ppImpl.setPages(orderLicense.getPages());
   
    // Initialize parm attributes
    if (orderLicense.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
    	OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
    	HashMap<String, ItemParm> ppItemParms = new HashMap<String, ItemParm>();
    	for (ItemParm itemParm : orderItemImpl.getItem().getItemParms().values()) {
    		ItemParm ppItemParm = new ItemParm();
    		ppItemParm.setParmName(itemParm.getParmName());
    		ppItemParm.setDataTypeCd(itemParm.getDataTypeCd());
    		ppItemParm.setParmValue(itemParm.getParmValue());
    		ppItemParm.setParmValueNumeric(itemParm.getParmValueNumeric());
    		ppItemParm.setParmValueDate(itemParm.getParmValueDate());
    		ppItemParm.setParmValueClob(itemParm.getParmValueClob());
    		if (itemParm.getRlSourceBeanCd() != null ) ppItemParm.setRlSourceBeanCd(itemParm.getRlSourceBeanCd());

    		ppItemParm.setDisplaySequence(itemParm.getDisplaySequence());
    		ppItemParm.setParmDisplayValue(itemParm.getParmDisplayValue());
    		ppItemParm.setParmUseCd(itemParm.getParmUseCd());
    		ppItemParm.setDisplayTextPre(itemParm.getDisplayTextPre());
    		ppItemParm.setDisplayTextPost(itemParm.getDisplayTextPost());
    		ppItemParm.setDisplayWidth(itemParm.getDisplayWidth());
    		ppItemParm.setFieldLength(itemParm.getFieldLength());
    		ppItemParm.setLabel(itemParm.getLabel());
    		ppItemParm.setLabelOn(itemParm.getLabelOn());
    		ppItemParm.setNewRow(itemParm.getNewRow());
    		
    		ppItemParms.put(ppItemParm.getParmName(), ppItemParm);
    	}
    	ppImpl.getItem().setItemParms(ppItemParms);

    	// Initialize Fee Attributes
    	Set<ItemFees> ppAllFees = new HashSet<ItemFees>(0);

    	for (ItemFees itemFees : orderItemImpl.getItem().getAllFees()) {
    		ItemFees ppItemFees = new ItemFees();
    		ppItemFees.setLicenseeFee(itemFees.getLicenseeFee());
    		ppItemFees.setRightsholderFee(itemFees.getRightsholderFee());
    		ppItemFees.setDiscount(itemFees.getDiscount());
    		ppItemFees.setDistributionPayable(itemFees.getDistributionPayable());
    		ppItemFees.setOrigDistAccount(itemFees.getOrigDistAccount());
    		ppItemFees.setOrigDistPartyId(itemFees.getOrigDistPartyId());
    		ppItemFees.setOrigDistPtyInst(itemFees.getOrigDistPtyInst());
    		ppItemFees.setOrigDistPartyName(itemFees.getOrigDistPartyName());
    		ppItemFees.setActualDistAccount(itemFees.getOrigDistAccount());
    		ppItemFees.setActualDistPartyId(itemFees.getOrigDistPartyId());
    		ppItemFees.setActualDistPtyInst(itemFees.getOrigDistPtyInst());
    		ppItemFees.setActualDistPartyName(itemFees.getOrigDistPartyName());

        	ppAllFees.add(ppItemFees);

    	}
    	ppImpl.getItem().setAllFees(ppAllFees);
    	
    	
    } else {
    	 
    	if (orderLicense.getExternalItemId() != null) {
    	WorkExternal wrWork = getWRWork(orderLicense.getExternalItemId());
        if (wrWork != null) {
              initPurchasablePermissionFromWorkExternal ( ppImpl, wrWork);
        }
    	}
 
     // Initialize usage data
    		buildUsageData( orderLicense, ppImpl ); 
    		
    		    		
   	    // Initialize fee attributes
    		
    		if (orderLicense.getRightsholderPartyId() != null) {
    			ppImpl.setPayeePartyId(orderLicense.getRightsholderPartyId());
    		}
    		if (orderLicense.getRightsholderPtyInst() != null) {
        		ppImpl.setPayeePtyInst(orderLicense.getRightsholderPtyInst());    			
    		}
    		ppImpl.setPayeePartyName(orderLicense.getRightsholder());
    		ppImpl.setPayeeAccount(orderLicense.getRightsholderAccount());
    		
     		ppImpl.setLicenseeFee(orderLicense.getTotalLicenseeFeeValue());    			
    		ppImpl.setRightsholderFee(orderLicense.getTotalRightsholderFeeValue());    			
    		ppImpl.setDiscount(orderLicense.getTotalDiscountValue());    			
    		ppImpl.setDistributionPayable(orderLicense.getTotalDistributionPayableValue());    			
    		    		
    		// Fee fields for display
    		ppImpl.setBaseFeeValue(orderLicense.getBaseFeeValue());    			
    		ppImpl.setFlatFeeValue(orderLicense.getFlatFeeValue());    			
    		ppImpl.setPerPageFeeValue(orderLicense.getPerPageFeeValue());    			
    		    		
    }
    
//TODO Verify this logic    
    if (checkRightValidity) {
    	ppImpl.setExternalRightId(0L);
		ppImpl.setPinningDate(null);
		try {
			PricingServices.updateItemPrice(ppImpl); 
		}
		catch (Exception e) {
		    LOGGER.error( "Error repricing item" );
		}
	}
    
    
    return ppImpl;

  }
     
  public static PurchasablePermission createPurchasablePermission( Item item ){
	    
	    if( item == null )
	    {
	      LOGGER.error( "Item cannot be null" );
	      throw new IllegalArgumentException( "Item cannot be null" );
	    }
	    if (LOGGER.isDebugEnabled()) {
	        LOGGER.debug("Creating PurchasablePermission via Item.");
	        LOGGER.debug(item.toString());
            if (item.getRightQualifierTerms() != null) {
	                LOGGER.debug("External Comment: " + item.getRightQualifierTerms());
	            }
	        }
            if (item.getRightQualifierTerms() != null) {
	                LOGGER.debug("Right Qualifier: " + item.getRightQualifierTerms());
	            }
	    return new PurchasablePermissionImpl( item );
  }
  
  static PurchasablePermissionImpl createAcademicSpecialOrderPurchasablePermission(){
    
	  PurchasablePermissionImpl purchasablePermission = new PurchasablePermissionImpl();
	  purchasablePermission.setExternalTouId(Long.valueOf(TypeOfUseEnum.RIGHT_TO_PHOTOCOPY.getId()));
	  purchasablePermission.setProductSourceKey(Long.valueOf(ProductEnum.APS.getProductSourceKey()));
	  purchasablePermission.setProductCd(ProductEnum.APS.name());
//	  purchasablePermission.setRightId(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
	  purchasablePermission.setExternalRightId(ECommerceConstants.SPECIAL_ORDER_RIGHT_INST);
	  purchasablePermission.setSpecialOrderTypeCdToManualSpecialOrder();
	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
	  purchasablePermission.setRightSourceCd(RightSourceEnum.TF.name());
	  purchasablePermission.setItemStatusCd(ItemStatusEnum.CART.name());
	  purchasablePermission.setItemAvailabilityCd(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd());
		    
	  initPurchasablePermissionFromStandardWork ( purchasablePermission, new WRStandardWork());
		
	  return purchasablePermission;
    
  }
  
  public static PurchasablePermission createPurchasablePermission(com.copyright.data.order.PermissionRequest permissionRequest) {
	  
	    if( permissionRequest == null )
	    {
	      LOGGER.error( "PermissionRequest cannot be null" );
	      throw new IllegalArgumentException( "OrderLicense cannot be null" );
	    }
	    
	    PurchasablePermissionImpl ppImpl = new PurchasablePermissionImpl();

	    ppImpl.setProductSourceKey(permissionRequest.getUsageData().getProduct());
	    ppImpl.setExternalTouId(permissionRequest.getUsageData().getTpuInst());
//	    ppImpl.setTouName(permissionRequest.getUsageData().getTypeOfUseDisplay());

	    // Initialize Category / Product /Tou attributes

	    for (ProductEnum productEnum : ProductEnum.values()) {
	    	if (permissionRequest.getUsageData().getProduct() == productEnum.getProductSourceKey().longValue()) {
	    		ppImpl.setProductName(productEnum.getProductName());
	    		ppImpl.setProductCd(productEnum.name());
	    		ppImpl.setCategoryId(productEnum.getCategoryId());
	    	}
	    }
	    
	    for (TouCategoryTypeEnum touCategoryTypeEnum : TouCategoryTypeEnum.values()) {
	    	if (ppImpl.getCategoryId().equals(touCategoryTypeEnum.getCategoryId())) {
	    	    ppImpl.setCategoryCd(touCategoryTypeEnum.name());
	    	    ppImpl.setCategoryName(touCategoryTypeEnum.getDesc());	    		
	    	}
	    }

	    ppImpl.setTouSourceKey(null);
	      
	    if (permissionRequest.getRight() != null) {
	    	ppImpl.setRightId(permissionRequest.getRight().getID());
	    }
	    
	    if (permissionRequest.getRight() != null) {
	    	ppImpl.setExternalRightId(permissionRequest.getRight().getID());
	    }

       	WorkExternal workExternal = getWRWork(permissionRequest.getWork().getWrkInst());
	    
       	initPurchasablePermissionFromWorkExternal(ppImpl, workExternal);
       		    
       	getUsageData(ppImpl, permissionRequest);
       	       	    
	   
	    // Initialize parm attributes

	    	// Initialize Fee Attributes
    	Set<ItemFees> ppAllFees = new HashSet<ItemFees>(0);

    	ItemFees ppItemFees = new ItemFees();
    	ppItemFees.setLicenseeFee(new BigDecimal(permissionRequest.getLicenseeFee()));
	   	ppItemFees.setRightsholderFee(new BigDecimal(permissionRequest.getRightsholderFee()));
	   	ppItemFees.setDiscount(new BigDecimal(permissionRequest.getDiscount()));
	   	ppItemFees.setDistributionPayable(new BigDecimal(permissionRequest.getRoyaltyPayable()));
//	   	ppItemFees.setOrigDistAccount(permissionRequest.getRight().itemFees.getOrigDistAccount());
	   	if (permissionRequest.getRight() != null) {
	   		ppItemFees.setOrigDistPartyId(permissionRequest.getRight().getRightsHolderPartyId());
		   	ppItemFees.setOrigDistPtyInst(permissionRequest.getRight().getRightsHolderInst());
//		   	ppItemFees.setOrigDistPartyName(permissionRequest.getRight().);
	   	}
	   	
       	ppAllFees.add(ppItemFees);
    	ppImpl.getItem().setAllFees(ppAllFees);
	    	
	    
	    // Initialize bundle attributes
	    


	//TODO Verify this logic    
		ppImpl.setPinningDate(null);
		try {
			PricingServices.updateItemPrice(ppImpl); 
		}
		catch (Exception e) {
		    LOGGER.error( "Error repricing item" );
		}
	  
	  return ppImpl;
  }
  
  
  /**
   * Creates an instance of <code>PurchasablePermission</code>
   * from an instance of <code>PublicationPermission</code>
   */
   public static PurchasablePermission createPurchasablePermission( PublicationPermission publicationPermission ) 
   throws UnableToBuildPurchasablePermissionException
   {
     
     if( publicationPermission == null )
     {
       LOGGER.error( "PublicationPermission cannot be null" );
       throw new IllegalArgumentException( "PublicationPermission cannot be null" );
     }
        
//     PermissionRequest permissionRequest = buildPermissionRequest( publicationPermission );
     
     Publication publication = publicationPermission.getPublication();
     
     PurchasablePermission purchasablePermission = createPurchasablePermission( publication, 
                                                                                publicationPermission.getRightsholderName(),
                                                                                publicationPermission.getRightsholderInst(),
                                                                                publicationPermission.getRightsQualifyingStatement(),
                                                                                publicationPermission.getPubBeginDate(), 
                                                                                publicationPermission.getPubEndDate(),
                                                                                publicationPermission.getRightAdapter());
     
     purchasablePermission.setExternalCommentTerm( publicationPermission.getRHTerms() );
    
     return purchasablePermission;
   }
   
  /**
   * Creates an instance of <code>PurchasablePermission</code>
   * from an instance of <code>PublicationPermission</code>
   */
   public static PurchasablePermission createPurchasablePermission( Publication publication, PublicationPermission publicationPermission ) 
   throws UnableToBuildPurchasablePermissionException
   {
     
     if( publicationPermission == null )
     {
       LOGGER.error( "PublicationPermission cannot be null" );
       throw new IllegalArgumentException( "PublicationPermission cannot be null" );
     }
        
//     PermissionRequest permissionRequest = buildPermissionRequest( publicationPermission );
    
     PurchasablePermission purchasablePermission = createPurchasablePermission( publication, 
                                                                                publicationPermission.getRightsholderName(),
                                                                                publicationPermission.getRightsholderInst(),
                                                                                publicationPermission.getRightsQualifyingStatement(),
                                                                                publicationPermission.getPubBeginDate(), 
                                                                                publicationPermission.getPubEndDate(),
                                                                                publicationPermission.getRightAdapter());
     
     purchasablePermission.setExternalCommentTerm( publicationPermission.getRHTerms() );
    
     return purchasablePermission;
   }
  
   public static PurchasablePermission createRLPurchasablePermission( Publication publication,
			   														   Long categoryId,
			   														   String categoryName,
		   															   Long touSourceKey,
		   															   String touName,
		   															   Long externalTouId)
   {

	   PurchasablePermissionImpl purchasablePermissionImpl = new PurchasablePermissionImpl();

	   initPurchasablePermissionFromPublication(purchasablePermissionImpl, publication);   

	   //purchasablePermissionImpl.setHasVolPriceTiers(rightAdapter.getHasVolumePrice());
	   //purchasablePermissionImpl.setRightQualifierTerms(rightsQualifyingStatement);
	   //purchasablePermissionImpl.setRightAvailabilityCd(rightAdapter.getPermission().getCode());
	   //purchasablePermissionImpl.setItemAvailabilityCd(ItemAvailabilityEnum.getEnumForTFPermissionCd(purchasablePermissionImpl.getRightAvailabilityCd()).getStandardPermissionCd());
	   purchasablePermissionImpl.setItemStatusCd(ItemStatusEnum.CART.name());
	   //purchasablePermissionImpl.setPayeePartyName( rightsholder );
	   //purchasablePermissionImpl.setPayeePtyInst( rightsholderInst );
	   //purchasablePermissionImpl.setPayeePartyId(  );
	   //purchasablePermissionImpl.setProductCd(rightAdapter.getProduct().getAbbreviation());
	   //purchasablePermissionImpl.setProductName(rightAdapter.getProduct().getFullName());
	   //purchasablePermissionImpl.setProductSourceKey(Long.valueOf(rightAdapter.getProduct().getId()));
	   purchasablePermissionImpl.setRightSourceCd(RightSourceEnum.RL.name());
	   purchasablePermissionImpl.setCategoryId(categoryId);
	   if (purchasablePermissionImpl.getCategoryId() != null) {
		  for (TouCategoryTypeEnum touCategoryTypeEnum : TouCategoryTypeEnum.values()) {
			if (purchasablePermissionImpl.getCategoryId().equals(touCategoryTypeEnum.getCategoryId())) {
				purchasablePermissionImpl.setCategoryCd(touCategoryTypeEnum.name());
				break;
			}
		  }  
	   }

	   purchasablePermissionImpl.setCategoryName(categoryName);
	   purchasablePermissionImpl.setProductSourceKey(null);
	   purchasablePermissionImpl.setProductName(TouCategoryTypeEnum.REPUBLISH_OR_DISPLAY_CONTENT.getDesc()); 
	   purchasablePermissionImpl.setProductCd(ProductEnum.RL.name()); 
	   purchasablePermissionImpl.setTouSourceKey(touSourceKey);
	   purchasablePermissionImpl.setTouName(touName);
	   purchasablePermissionImpl.setExternalTouId(externalTouId);

	   //purchasablePermissionImpl.setRightId(rightAdapter.getRgtInst());

	   return purchasablePermissionImpl;

} 
   
   
  private static PurchasablePermission createPurchasablePermission( Publication publication, 
                                                                    String rightsholder,
                                                                    long rightsholderInst,
                                                                    String rightsQualifyingStatement,
                                                                    Date publicationStartDate,
                                                                    Date publicationEndDate,
                                                                    RightAdapter rightAdapter)
  {
    
/*    if( permissionRequest == null )
    {
      LOGGER.error( "PermissionRequest cannot be null" );
      throw new IllegalArgumentException( "PermissionRequest cannot be null" );
    }
*/
   
    PurchasablePermissionImpl purchasablePermissionImpl = new PurchasablePermissionImpl();
    
    purchasablePermissionImpl.setRightFromWeb(rightAdapter);
 
    initPurchasablePermissionFromPublication(purchasablePermissionImpl, publication);   
    
    purchasablePermissionImpl.setHasVolPriceTiers(rightAdapter.getHasVolumePrice());
    purchasablePermissionImpl.setRightQualifierTerms(rightsQualifyingStatement);
    purchasablePermissionImpl.setRightsQualifyingStatement(rightsQualifyingStatement);
    purchasablePermissionImpl.setRightAvailabilityCd(rightAdapter.getPermission().getCode());
    if (purchasablePermissionImpl.getRightAvailabilityCd() != null) {
    	purchasablePermissionImpl.setItemAvailabilityCd(ItemAvailabilityEnum.getEnumForTFPermissionCd(purchasablePermissionImpl.getRightAvailabilityCd()).getStandardPermissionCd());
    } else {
    	purchasablePermissionImpl.setItemAvailabilityCd(null);    	
    }
	purchasablePermissionImpl.setItemStatusCd(ItemStatusEnum.CART.name());
    purchasablePermissionImpl.setPayeePartyName( rightsholder );
    purchasablePermissionImpl.setPayeePtyInst( rightsholderInst );
//    purchasablePermissionImpl.setPayeePartyId(  );
    if (rightAdapter.getPermissionSummaryCategory() != null) {
        purchasablePermissionImpl.setCategoryId(rightAdapter.getPermissionSummaryCategory().getCategoryId());
        purchasablePermissionImpl.setCategoryName(rightAdapter.getPermissionSummaryCategory().getCategory());    	
        if (purchasablePermissionImpl.getCategoryId() != null) {
		  for (TouCategoryTypeEnum touCategoryTypeEnum : TouCategoryTypeEnum.values()) {
			if (purchasablePermissionImpl.getCategoryId().equals(touCategoryTypeEnum.getCategoryId())) {
				purchasablePermissionImpl.setCategoryCd(touCategoryTypeEnum.name());
				break;
			}
		  }  
	   }
    }
    purchasablePermissionImpl.setProductCd(rightAdapter.getProduct().getAbbreviation());
    purchasablePermissionImpl.setProductName(rightAdapter.getProduct().getFullName());
    purchasablePermissionImpl.setProductSourceKey(Long.valueOf(rightAdapter.getProduct().getId()));
    purchasablePermissionImpl.setRightSourceCd(RightSourceEnum.TF.name());
    purchasablePermissionImpl.setExternalRightId(rightAdapter.getRgtInst());
     
    /*
     * avoid NPE for manual special order items that have no Right
     */
    if (rightAdapter.getRRRight() != null) {
	    for (RightFee rightFee : rightAdapter.getRRRight().getRightFees()) {
	    	if (rightFee.getFeeName().equalsIgnoreCase(ItemParmEnum.BASE_FEE.name())) {
	    		if ( rightFee.getFeeValue() != null) {
	    			purchasablePermissionImpl.setBaseFeeValue(new BigDecimal(rightFee.getFeeValue()));
	    		}
	    	}
	    	if (rightFee.getFeeName().equalsIgnoreCase(ItemParmEnum.FLAT_FEE.name())) {
	    		if ( rightFee.getFeeValue() != null) {
	    			purchasablePermissionImpl.setFlatFeeValue(new BigDecimal(rightFee.getFeeValue()));
	    		}
	    	}
	    	if (rightFee.getFeeName().equalsIgnoreCase(ItemParmEnum.PER_PAGE_FEE.name())) {
	    		if ( rightFee.getFeeValue() != null) {
	    			purchasablePermissionImpl.setPerPageFeeValue(new BigDecimal(rightFee.getFeeValue()));
	    		}
	    	}
	    	 if (rightFee.getFeeName().equalsIgnoreCase(ItemParmEnum.ENTIRE_BOOK_FEE.name())) {
	    		if ( rightFee.getFeeValue() != null) {
	    			purchasablePermissionImpl.setEntireBookFeeValue(new BigDecimal(rightFee.getFeeValue()));
	    		}
	    	}
	    }
    }
    
    if (purchasablePermissionImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PURCHASE.getStandardPermissionCd())) {
  		purchasablePermissionImpl.setSpecialOrderTypeCdToNotSpecialOrder();
    } else if (purchasablePermissionImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.CONTACT_RH.getStandardPermissionCd())) {
    	purchasablePermissionImpl.setSpecialOrderTypeCdToContactRightsholder();
    } else if (purchasablePermissionImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.RESEARCH_FURTHER.getStandardPermissionCd())) {
    	purchasablePermissionImpl.setSpecialOrderTypeCdToResearchFurtherSpecialOrder();
    } else if (purchasablePermissionImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.HOLD_PENDING.getStandardPermissionCd())) {
    	purchasablePermissionImpl.setSpecialOrderTypeCdToContactRightsholder();
    } else if (purchasablePermissionImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.NO_CHARGE.getStandardPermissionCd())) {
    	purchasablePermissionImpl.setSpecialOrderTypeCdToNotSpecialOrder();
    } else if (purchasablePermissionImpl.getItemAvailabilityCd().equalsIgnoreCase(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd())) {
    	purchasablePermissionImpl.setSpecialOrderTypeCdToNotSpecialOrder();
    } 

    LOGGER.debug( "Right Id selected: " + purchasablePermissionImpl.getExternalRightId() +
			 " ItemAvailabilityCd: " + purchasablePermissionImpl.getItemAvailabilityCd() +
			 " Special Order Type Cd: " + purchasablePermissionImpl.getSpecialOrderTypeCd());

    purchasablePermissionImpl.setExternalTouId(Long.valueOf(rightAdapter.getTypeOfUseEnum().getId()));

    //    purchasablePermissionImpl.setTouSourceKey(new Long(rightAdapter.getTypeOfUseEnum().getId()));
    purchasablePermissionImpl.setTouName(rightAdapter.getTypeOfUseEnum().getDesc());

    LOGGER.debug( "Product Cd: " + purchasablePermissionImpl.getProductCd() + 
    			 " Tou Id: " + purchasablePermissionImpl.getExternalTouId() +
    			 " Tou Name: " + purchasablePermissionImpl.getTouName() );

    purchasablePermissionImpl.setRightStartDate( publicationStartDate );
    purchasablePermissionImpl.setRightEndDate( publicationEndDate );
    purchasablePermissionImpl.setRightQualifierTerms(rightAdapter.getQualifyingStmt());
    
    if( purchasablePermissionImpl.isAcademic() )
    {
    	WorkRightsAdapter workRightsAdapter = (WorkRightsAdapter) publication;
    	
        purchasablePermissionImpl.setCustomAuthor(purchasablePermissionImpl.getAuthor());
        purchasablePermissionImpl.setCustomEdition(purchasablePermissionImpl.getEdition());
        purchasablePermissionImpl.setCustomVolume(purchasablePermissionImpl.getVolume());
        //initialize the Licensee out of print request flag for Academic TOUs
        purchasablePermissionImpl.setLicenseeRequestedEntireWork(false);
      
    }
  
    return purchasablePermissionImpl;

  }

    /*  2009-10-23  MSJ
     *  Problematic.  We are trying to carry through information that
     *  is not really available in all our "duplicated" objects.  I will
     *  not be able (at this point in time) to guarantee that the data
     *  we want to carry through regarding new metadata fields in our
     *  works retrieved during search will be available in all the objects
     *  we use to populate work data.  Because of this I'll attempt to
     *  look up the work repository item via the work retriever (this was
     *  already partially done to pull back the wrWrkInst, I'm just taking
     *  it a step farther).
     */
    
	private static Work buildWork( PublicationPermission publicationPermission )
    {
        WRStandardWork work = new WRStandardWork();

        WorkRightsAdapter publication = (WorkRightsAdapter) publicationPermission.getPublication();
        
        work.setWrkInst( publication.getTFWrkInst() );
        work.setTfWksInstList(publication.getTfWksInstList()); 
        work.setStandardNumber( publication.getMainIDNo() );
        work.setMainTitle( publication.getMainTitle() );
        work.setMainAuthor( publication.getMainAuthor() );
        work.setMainEditor( publication.getMainEditor() );
        work.setVolume( publication.getVolume() );
        work.setEdition( publication.getEdition() );
        work.setPublisher( publication.getMainPublisher() );
        work.setTfWksInstList(publication.getTfWksInstList()); 
        work.setWrwrkinst(publication.getWrkInst());

        //  2009-10-23  MSJ
        //  Additional fields mapped for summit project... to
        //  match new fields in works repository.  At some point
        //  we need to make this much more pervasive.  Since the
        //  publicationPermission object is the interface for the
        //  RightAdapter, and the RightAdapter was likely filled in
        //  by the WorkRightsAdapter, we should have a publication
        //  object containing the relevant data.
        
        work.setSeries(publication.getSeries());
        work.setSeriesNumber(publication.getSeriesNumber());
        work.setPublicationType(publication.getPublicationType());
        work.setCountry(publication.getCountry());
        work.setLanguage(publication.getLanguage());
        work.setIdnoLabel(publication.getIdnoLabel());
        work.setIdnoTypeCd(publication.getIdnoTypeCd());
        work.setPages(publication.getPages());
        
        return work;
    }
      
    private static WRStandardWork buildWork( OrderLicense orderLicense )
    {
        long tfWrkInst = orderLicense.getWorkInst();
        long wrWrkInst = 0;
        
        //  The WRStandardWork extends the StandardWork
        //  object which implements our shared library
        //  version of the "Work" object (as opposed to the
        //  WorkRetriever service version of Work).
        
        WRStandardWork work = new WRStandardWork();
        
        //  I am using the full path name as a reminder that
        //  this code should be revisited in the future.
        
        WorkExternal wrWork = getWRWork(tfWrkInst);
        
        if (wrWork != null) {
            wrWrkInst = wrWork.getPrimaryKey();
            work.setSeries(wrWork.getSeries());
            work.setSeriesNumber(wrWork.getSeriesNumber());
            work.setPublicationType(wrWork.getPublicationType());
            work.setCountry(wrWork.getCountry());
            work.setLanguage(wrWork.getLanguage());
            work.setIdnoLabel(ApplicationResources.getInstance().getIdnoLabel(wrWork.getIdnoTypeCode()));
            work.setIdnoTypeCd(wrWork.getIdnoTypeCode());
            work.setPages(wrWork.getPages() != null ? wrWork.getPages().toString() : null);
        }
        else {
            LOGGER.warn("Additional Publication metadata is not available for order license.");
            work.setSeries(null);
            work.setSeriesNumber(null);
            work.setPublicationType(null);
            work.setCountry(null);
            work.setLanguage(null);
            work.setIdnoLabel("ISBN/ISSN");
            work.setIdnoTypeCd(null);
            work.setPages(null);
        }
        work.setWrwrkinst(wrWrkInst);
        work.setWrkInst(tfWrkInst);
        work.setStandardNumber( orderLicense.getStandardNumber() );
        work.setMainTitle( StringUtils.upperCase(orderLicense.getPublicationTitle()) );
        work.setMainAuthor( orderLicense.getAuthor() );
        work.setMainEditor( orderLicense.getEditor() );
        work.setVolume( orderLicense.getVolume() );
        work.setEdition( orderLicense.getEdition() );
        work.setPublisher( orderLicense.getPublisher() );
        work.setPublicationYear(  orderLicense.getPublicationYear() );
         
        return work;
    }
    
    //  2010-01-05  MSJ
    //  Added this for special order cases.
    
    public static Work buildWork(Publication pub) {
        long tfWrkInst = pub.getTFWrkInst();
        long wrWrkInst = 0;
        
        //  The WRStandardWork extends the StandardWork
        //  object which implements our shared library
        //  version of the "Work" object (as opposed to the
        //  WorkRetriever service version of Work).
        
        WRStandardWork work = new WRStandardWork();
        
        //  I am using the full path name as a reminder that
        //  this code should be revisited in the future.
        
        WorkExternal wrWork = getWRWork(tfWrkInst);
        
        if (wrWork != null) {
            wrWrkInst = wrWork.getPrimaryKey();
            work.setSeries(wrWork.getSeries());
            work.setSeriesNumber(wrWork.getSeriesNumber());
            work.setPublicationType(wrWork.getPublicationType());
            work.setCountry(wrWork.getCountry());
            work.setLanguage(wrWork.getLanguage());
            work.setIdnoLabel(ApplicationResources.getInstance().getIdnoLabel(wrWork.getIdnoTypeCode()));
            work.setIdnoTypeCd(wrWork.getIdnoTypeCode());
            work.setPages(wrWork.getPages() != null ? wrWork.getPages().toString() : null);
        }
        else {
            LOGGER.warn("Additional metadata is not available for publication.");
            work.setSeries(null);
            work.setSeriesNumber(null);
            work.setPublicationType(null);
            work.setCountry(null);
            work.setLanguage(null);
            work.setIdnoLabel("ISBN/ISSN");
            work.setIdnoTypeCd(null);
            work.setPages(null);
        }
        work.setWrkInst(pub.getTFWrkInst());
        work.setStandardNumber(pub.getMainIDNo());
        work.setMainTitle(pub.getMainTitle());
        work.setMainAuthor(pub.getMainAuthor());
        work.setMainEditor(pub.getMainEditor());
        work.setPublisher(pub.getMainPublisher());
        work.setVolume(pub.getVolume());
        work.setEdition(pub.getEdition());
            
        return work;
    }
      
    public static WRStandardWork buildWRWork(StandardWork standardWork) {
        long tfWrkInst = standardWork.getWrkInst();
        long wrWrkInst = 0;
        
        //  The WRStandardWork extends the StandardWork
        //  object which implements our shared library
        //  version of the "Work" object (as opposed to the
        //  WorkRetriever service version of Work).
        
        WRStandardWork work = new WRStandardWork();
        
        //  I am using the full path name as a reminder that
        //  this code should be revisited in the future.
        
        WorkExternal wrWork = getWRWork(tfWrkInst);
          
        if (wrWork != null) {
            wrWrkInst = wrWork.getPrimaryKey();
            work.setSeries(wrWork.getSeries());
            work.setSeriesNumber(wrWork.getSeriesNumber());
            work.setPublicationType(wrWork.getPublicationType());
            work.setCountry(wrWork.getCountry());
            work.setLanguage(wrWork.getLanguage());
            work.setIdnoLabel(ApplicationResources.getInstance().getIdnoLabel(wrWork.getIdnoTypeCode()));
            work.setIdnoTypeCd(wrWork.getIdnoTypeCode());
            work.setPages(wrWork.getPages() != null ? wrWork.getPages().toString() : null);
        }
        else {
            LOGGER.warn("Additional Publication metadata is not available for standard work.");
            work.setSeries(null);
            work.setSeriesNumber(null);
            work.setPublicationType(null);
            work.setCountry(null);
            work.setLanguage(null);
            work.setIdnoLabel("ISBN/ISSN");
            work.setIdnoTypeCd(null);
            work.setPages(null);
        }
        work.setEdition(standardWork.getEdition());
        work.setMainAuthor(standardWork.getMainAuthor());
        work.setMainEditor(standardWork.getMainEditor());
        work.setMainTitle(standardWork.getMainTitle());
        work.setPublicationYear(standardWork.getPublicationYear());
        work.setPublisher(standardWork.getPublisher());
        work.setStandardNumber(standardWork.getStandardNumber());
        work.setVolume(standardWork.getVolume());
        work.setWrkInst(tfWrkInst);
        work.setTypeCd(standardWork.getTypeCd());
        work.setWrwrkinst(wrWrkInst);
        work.setTfWksInstList(null);
    
        return work;
    }
  
  public static long getWRWorkInst(long tfWorkInst) 
  {
	  List<String> sortList = new ArrayList<String>();
	  int page = 1;
	  int pageSize = 100;
	  
	  PublicationSearch publicationSearch = new PublicationSearch();
	  publicationSearch.setTfWrkInst(String.valueOf(tfWorkInst));
	  SearchRetrievalResult result = ServiceLocator.getSearchRetrievalService().searchPublication(new SearchRetrievalConsumerContext(), publicationSearch, sortList, page, pageSize);
	  List<WorkExternal> lstWork = result.getWorks();

      if (lstWork == null || lstWork.size() == 0) {
          //didn't find the WR work inst
          return 0;
      }
      
      return lstWork.get(0).getPrimaryKey();
  }
  
  //  2009-10-23  MSJ
  //  Looking for a more complete solution to populating the WRStandardWork.
  //  This will give me my metadata coming in from an order or any other direction
  //  I hope.
  
  public static WorkExternal getWRWork(long tfWorkInst) 
  {

	  List<String> sortList = new ArrayList<String>();
	  int page = 1;
	  int pageSize = 100;
	  
	  PublicationSearch publicationSearch = new PublicationSearch();
	  publicationSearch.setTfWrkInst(String.valueOf(tfWorkInst));
	  SearchRetrievalResult result = ServiceLocator.getSearchRetrievalService().searchPublication(new SearchRetrievalConsumerContext(), publicationSearch, sortList, page, pageSize);
	  List<WorkExternal> lstWork = result.getWorks();
      
      if (lstWork == null || lstWork.size() == 0) {
          //didn't find the WR work inst
          return null;
      }
      
      return lstWork.get(0);
  }
 
  private static void buildUsageData( OrderLicense orderLicense, PurchasablePermissionImpl purchasablePermissionImpl )
  {
    
    if( orderLicense.isAcademic() )
    {
      buildUsageDataAcademic( orderLicense, purchasablePermissionImpl );  
    }
    
    if( orderLicense.isRepublication() )
    {
      buildUsageDataRepublication( orderLicense, purchasablePermissionImpl );
    }
    
    if( orderLicense.isPhotocopy() )
    {
      buildUsageDataPhotocopy( orderLicense, purchasablePermissionImpl );
    }
    
    if( orderLicense.isEmail() )
    {
      buildUsageDataEmail( orderLicense, purchasablePermissionImpl );
    }
    
    if( orderLicense.isNet() )
    {
      buildUsageDataNet( orderLicense, purchasablePermissionImpl );
    }
    
//    if( usageData == null )
//    {
//     throw new UnableToBuildPurchasablePermissionException( "Unable to build an instance of UsageData" );
//    }
    
  }

  private static void buildUsageDataAcademic( OrderLicense orderLicense, PurchasablePermissionImpl purchasablePermissionImpl )
  {   
    
//    UsageDataAcademic usageDataAcademic = new UsageDataAcademic();
    
    populateCommonUsageData( orderLicense, purchasablePermissionImpl );
    
    purchasablePermissionImpl.setNumberOfPages( orderLicense.getNumberOfPages());
    purchasablePermissionImpl.setNumberOfStudents( orderLicense.getNumberOfStudents() );
    purchasablePermissionImpl.setChapterArticle( orderLicense.getChapterArticle());
    purchasablePermissionImpl.setCustomAuthor(orderLicense.getCustomAuthor());
    purchasablePermissionImpl.setCustomEdition(orderLicense.getCustomEdition());
    purchasablePermissionImpl.setCustomVolume(orderLicense.getVolume());
    purchasablePermissionImpl.setDateOfIssue(orderLicense.getDateOfIssue());
    purchasablePermissionImpl.setPageRange(orderLicense.getPageRange());
    
  }

  private static void buildUsageDataRepublication( OrderLicense orderLicense, PurchasablePermissionImpl purchasablePermissionImpl )
  {
       
//    UsageDataRepublication usageDataRepublication = new UsageDataRepublication();
    
    //common data
    populateCommonUsageData( orderLicense, purchasablePermissionImpl );
    
    //rlsPages
    purchasablePermissionImpl.setNumberOfPages( orderLicense.getNumberOfPages() );
    
    //chapterArticle
    if (orderLicense.getChapterArticle() != null ) purchasablePermissionImpl.setChapterArticle( orderLicense.getChapterArticle());
    
    //circulation
    purchasablePermissionImpl.setCirculationDistribution( orderLicense.getCirculationDistribution() );
    
    //forProfit
    if( RepublicationConstants.BUSINESS_FOR_PROFIT.equals(orderLicense.getBusiness()) )
    {
    	purchasablePermissionImpl.setBusiness( RepublicationConstants.FOR_PROFIT );
      
    }else
    {
    	purchasablePermissionImpl.setBusiness( RepublicationConstants.NON_FOR_PROFIT );
    }
    
    //content types...
//    usageDataRepublication = ECommerceUtils.setTypeOfContent( usageDataRepublication, orderLicense.getTypeOfContent() );
    purchasablePermissionImpl.setTypeOfContent(orderLicense.getTypeOfContent());
    
    purchasablePermissionImpl.setNumberOfCartoons(orderLicense.getNumberOfCartoons());
    purchasablePermissionImpl.setNumberOfExcerpts(orderLicense.getNumberOfExcerpts());
    purchasablePermissionImpl.setNumberOfFigures(orderLicense.getNumberOfFigures());
    purchasablePermissionImpl.setNumberOfGraphs(orderLicense.getNumberOfGraphs());
    purchasablePermissionImpl.setNumberOfIllustrations(orderLicense.getNumberOfIllustrations());
    purchasablePermissionImpl.setNumberOfLogos(orderLicense.getNumberOfLogos());
    purchasablePermissionImpl.setNumberOfPhotos(orderLicense.getNumberOfPhotos());
    purchasablePermissionImpl.setNumberOfQuotes(orderLicense.getNumberOfQuotes());

    
    
    //origAuthor
    if( orderLicense.isSubmitterAuthor() )
    {
      purchasablePermissionImpl.setSubmitterAuthor( true );           
    }else
    {
      purchasablePermissionImpl.setSubmitterAuthor( false );
    }
    
    //publicationDate
    purchasablePermissionImpl.setContentsPublicationDate( orderLicense.getContentsPublicationDate() );
     
    //hdrRepubPub
    purchasablePermissionImpl.setRepublishingOrganization(StringUtils.upperCase(orderLicense.getRepublishingOrganization()));
    
    //repubTitle
    if (orderLicense.getNewPublicationTitle() != null ) purchasablePermissionImpl.setRepublicationDestination(StringUtils.upperCase(orderLicense.getNewPublicationTitle()));
    
    //repubDate
    purchasablePermissionImpl.setRepublicationDate( orderLicense.getRepublicationDate() );
     
    //language & translated
    if( orderLicense.getTranslationLanguage().equals( RepublicationConstants.NO_TRANSLATION ) )
    {
      purchasablePermissionImpl.setLanguage( Constants.EMPTY_STRING );
      purchasablePermissionImpl.setTranslated( RepublicationConstants.NOT_TRANSLATED );
    }else
    {
      purchasablePermissionImpl.setLanguage( orderLicense.getTranslationLanguage() );
      purchasablePermissionImpl.setTranslated( RepublicationConstants.TRANSLATED );
    }
    
    //lcnHdrRefNum
    purchasablePermissionImpl.setYourReference(orderLicense.getYourReference());
    
    //author
    purchasablePermissionImpl.setCustomAuthor(orderLicense.getCustomAuthor());
    
    purchasablePermissionImpl.setPageRange(orderLicense.getPageRange());


  }

  private static void buildUsageDataPhotocopy(OrderLicense orderLicense, PurchasablePermissionImpl purchasablePermissionImpl)
  {
//    UsageDataPhotocopy usageDataPhotocopy = new UsageDataPhotocopy();
    
    populateCommonUsageData( orderLicense, purchasablePermissionImpl );
    
    purchasablePermissionImpl.setNumberOfCopies( orderLicense.getNumberOfCopies() );
    purchasablePermissionImpl.setNumberOfPages( orderLicense.getNumberOfPages() );
    purchasablePermissionImpl.setChapterArticle(orderLicense.getChapterArticle());
//    purchasablePermissionImpl.setEditor(orderLicense.getEditor());
//    purchasablePermissionImpl.setEdition(orderLicense.getEdition());
//    purchasablePermissionImpl.setVolume(orderLicense.getVolume());
        
  }

  private static void buildUsageDataEmail(OrderLicense orderLicense, PurchasablePermissionImpl purchasablePermissionImpl)
  {
//    UsageDataEmail usageDataEmail = new UsageDataEmail();
    
    populateCommonUsageData( orderLicense, purchasablePermissionImpl );
    
    purchasablePermissionImpl.setChapterArticle(orderLicense.getChapterArticle());
    purchasablePermissionImpl.setNumberOfRecipients( orderLicense.getNumberOfRecipients() );
    purchasablePermissionImpl.setDateOfUse(orderLicense.getDateOfUse());
      
  }

  private static void buildUsageDataNet(OrderLicense orderLicense, PurchasablePermissionImpl purchasablePermissionImpl)
  {
//    UsageDataNet usageDataNet = new UsageDataNet();
    
    populateCommonUsageData( orderLicense, purchasablePermissionImpl );
    
    purchasablePermissionImpl.setChapterArticle(orderLicense.getChapterArticle());
    purchasablePermissionImpl.setDateOfUse(orderLicense.getDateOfUse());
    purchasablePermissionImpl.setDuration( orderLicense.getDuration() );
    purchasablePermissionImpl.setWebAddress(orderLicense.getWebAddress());
    
  }

  private static void populateCommonUsageData( OrderLicense orderLicense, PurchasablePermissionImpl purchasablePermissionImpl )
  {
//    purchasablePermissionImpl.setProductSourceKey(new Long(orderLicense.getPrdInst()));
//    purchasablePermissionImpl.setExternalTouId(new Long(orderLicense.getExternalTouId()));
//    purchasablePermissionImpl.setTouName(new String(orderLicense.getTouName()));
    
	  purchasablePermissionImpl.setPublicationDateOfUse(orderLicense.getPublicationDateOfUse());
    
	  purchasablePermissionImpl.setItemStatusCd(ItemStatusEnum.CART.name());
  }

  
  
  private static void getUsageData( PurchasablePermissionImpl ppImpl, com.copyright.data.order.PermissionRequest permissionRequest )
  {
    
    if( ppImpl.isAcademic() )
    {
      getUsageDataAcademic( ppImpl, permissionRequest );  
    }
    
    if( ppImpl.isRepublication() )
    {
      getUsageDataRepublication( ppImpl, permissionRequest );
    }
    
    if( ppImpl.isPhotocopy() )
    {
      getUsageDataPhotocopy( ppImpl, permissionRequest );
    }
    
    if( ppImpl.isEmail() )
    {
      getUsageDataEmail( ppImpl, permissionRequest );
    }
    
    if( ppImpl.isNet() )
    {
      getUsageDataNet( ppImpl, permissionRequest );
    }
    
//    if( usageData == null )
//    {
//     throw new UnableToBuildPurchasablePermissionException( "Unable to build an instance of UsageData" );
//    }
    
  }

  private static void getUsageDataAcademic( PurchasablePermissionImpl ppImpl, com.copyright.data.order.PermissionRequest permissionRequest )
  {   
    	
    UsageDataAcademic usageDataAcademic = (UsageDataAcademic) permissionRequest.getUsageData();
    
    populateCommonUsageDataFromPermissionRequest( ppImpl, permissionRequest );
    
    ppImpl.setNumberOfPages( usageDataAcademic.getNumPages());
    ppImpl.setNumberOfCopies( usageDataAcademic.getNumStudents() );
    ppImpl.setChapterArticle( usageDataAcademic.getChapterArticle());
    ppImpl.setCustomAuthor(usageDataAcademic.getAuthor());
    ppImpl.setCustomEdition(usageDataAcademic.getEdition());
    ppImpl.setCustomVolume(usageDataAcademic.getVolume());
    ppImpl.setDateOfIssue(usageDataAcademic.getDateOfIssue());
    ppImpl.setPageRange(usageDataAcademic.getPageRanges());
    
  }

  private static void getUsageDataRepublication( PurchasablePermissionImpl ppImpl, com.copyright.data.order.PermissionRequest permissionRequest )
  {
       
    UsageDataRepublication usageDataRepublication = (UsageDataRepublication) permissionRequest.getUsageData();
    
    //common data
    populateCommonUsageDataFromPermissionRequest( ppImpl, permissionRequest );
    
    //rlsPages
    ppImpl.setNumberOfPages( usageDataRepublication.getRlsPages() );
    
    //chapterArticle
    ppImpl.setChapterArticle( usageDataRepublication.getChapterArticle());
    
    //circulation
    ppImpl.setCirculationDistribution( (int) usageDataRepublication.getCirculation() );
    
    //forProfit
    if( RepublicationConstants.BUSINESS_FOR_PROFIT.equals(usageDataRepublication.getForProfit()) )
    {
    	ppImpl.setBusiness( RepublicationConstants.FOR_PROFIT );
      
    }else
    {
    	ppImpl.setBusiness( RepublicationConstants.NON_FOR_PROFIT );
    }
    
    //content types...
//    usageDataRepublication = ECommerceUtils.setTypeOfContent( usageDataRepublication, orderLicense.getTypeOfContent() );
    ppImpl.setTypeOfContent(ECommerceUtils.getTypeOfContent( usageDataRepublication ));
    
    ppImpl.setNumberOfCartoons(usageDataRepublication.getNumCartoons());
    ppImpl.setNumberOfExcerpts(usageDataRepublication.getNumExcerpts());
    ppImpl.setNumberOfFigures(usageDataRepublication.getNumFigures());
    ppImpl.setNumberOfGraphs(usageDataRepublication.getNumGraphs());
    ppImpl.setNumberOfIllustrations(usageDataRepublication.getNumIllustrations());
    ppImpl.setNumberOfLogos(usageDataRepublication.getNumLogos());
    ppImpl.setNumberOfPhotos(usageDataRepublication.getNumPhotos());
    ppImpl.setNumberOfQuotes(usageDataRepublication.getNumQuotes());

    
    
    //origAuthor
    boolean submitterIsAuthor = usageDataRepublication.getOrigAuthor() != null && usageDataRepublication.getOrigAuthor().equalsIgnoreCase(RepublicationConstants.SUBMITTER_IS_AUTHOR);

    if( submitterIsAuthor )
    {
      ppImpl.setSubmitterAuthor( true );           
    }else
    {
      ppImpl.setSubmitterAuthor( false );
    }
    
    //publicationDate
    ppImpl.setContentsPublicationDate( usageDataRepublication.getPublicationDate() );
     
    //hdrRepubPub
    ppImpl.setRepublishingOrganization(StringUtils.upperCase(usageDataRepublication.getHdrRepubPub()));
    
    //repubTitle
    ppImpl.setRepublicationDestination(StringUtils.upperCase(usageDataRepublication.getRepubTitle()));
    
    //repubDate
    ppImpl.setRepublicationDate( usageDataRepublication.getRepubDate() );
     
    //language & translated
    if( usageDataRepublication.getLanguage().equals( RepublicationConstants.NO_TRANSLATION ) )
    {
      ppImpl.setLanguage( RepublicationConstants.NO_TRANSLATION );
      ppImpl.setTranslated( RepublicationConstants.NOT_TRANSLATED );
    }else
    {
      ppImpl.setLanguage( usageDataRepublication.getLanguage() );
      ppImpl.setTranslated( RepublicationConstants.TRANSLATED );
    }
    
    //lcnHdrRefNum
    ppImpl.setYourReference(usageDataRepublication.getLcnHdrRefNum());
    
    //author
    ppImpl.setCustomAuthor(usageDataRepublication.getAuthor());
    
    ppImpl.setPageRange(usageDataRepublication.getPageRanges());


  }

  private static void getUsageDataPhotocopy(PurchasablePermissionImpl ppImpl, com.copyright.data.order.PermissionRequest permissionRequest)
  {
    UsageDataPhotocopy usageDataPhotocopy = (UsageDataPhotocopy) permissionRequest.getUsageData();
    
	populateCommonUsageDataFromPermissionRequest( ppImpl, permissionRequest );
    
    ppImpl.setNumberOfCopies(usageDataPhotocopy.getNumCopies() );
    ppImpl.setNumberOfPages(usageDataPhotocopy.getNumPages() );
    ppImpl.setChapterArticle(usageDataPhotocopy.getChapterArticle());
//    ppImpl.setCustomAuthor(usageDataPhotocopy.getAuthor());
//    ppImpl.setCustomEdition(usageDataPhotocopy.getEdition());
//    ppImpl.setCustomVolume(usageDataPhotocopy.getVolume());

//    ppImpl.setEditor(orderLicense.getEditor());
//    ppImpl.setEdition(orderLicense.getEdition());
//    ppImpl.setVolume(orderLicense.getVolume());
        
  }

  private static void getUsageDataEmail(PurchasablePermissionImpl ppImpl, com.copyright.data.order.PermissionRequest permissionRequest)
  {
    UsageDataEmail usageDataEmail = (UsageDataEmail) permissionRequest.getUsageData();
    
	populateCommonUsageDataFromPermissionRequest( ppImpl, permissionRequest );
    
    ppImpl.setChapterArticle(usageDataEmail.getChapterArticle());
    ppImpl.setNumberOfRecipients( usageDataEmail.getNumRecipients() );
    ppImpl.setDateOfUse(usageDataEmail.getDateOfUse());
      
  }

  private static void getUsageDataNet(PurchasablePermissionImpl ppImpl, com.copyright.data.order.PermissionRequest permissionRequest)
  {
    UsageDataNet usageDataNet = (UsageDataNet) permissionRequest.getUsageData();
    
	populateCommonUsageDataFromPermissionRequest( ppImpl, permissionRequest );
    
    ppImpl.setChapterArticle(usageDataNet.getChapterArticle());
    ppImpl.setDateOfUse(usageDataNet.getDateOfUse());
    ppImpl.setDuration( usageDataNet.getDuration() );
    ppImpl.setWebAddress(usageDataNet.getWebAddress());
    
  }

  private static void populateCommonUsageDataFromPermissionRequest( PurchasablePermissionImpl ppImpl, com.copyright.data.order.PermissionRequest permissionRequest )
  {
//    ppImpl.setProductSourceKey(new Long(orderLicense.getPrdInst()));
//    ppImpl.setExternalTouId(new Long(orderLicense.getExternalTouId()));
//    ppImpl.setTouName(new String(orderLicense.getTouName()));
    
	  ppImpl.setPublicationDateOfUse(permissionRequest.getUsageData().getPublicationDate());
    
	  ppImpl.setItemStatusCd(ItemStatusEnum.CART.name());
  }
    
    private static PurchasablePermission getValidPurchasablePermissionForToday( OrderLicense orderLicense )
    {
        //  2009-09-18 MSJ
        //  The internal data WorkImpl class was eliminated and simply exposed
        //  as the Work class in the service's "api" classpath.
        //
        //  NEED TO Substitute since orderlicense does not carry a WR Work Inst
        //  first build a WorkImpl object which is associated with search, then 
        //  create a WorkRightAdapter
        
        //  2010-01-04  MSJ
        //  Adding code to look up the WR Work here because copies were failing
        //  during a call to buildWork from the faked publicationPermission.  If
        //  the work is STILL null, go ahead and build our fake.  In the future if
        //  TFWrkInst becomes only one of several possibilities of how work data
        //  is referenced (works from external sources, not in our database and
        //  ONLY referenced by a WR Work primary key) this will have to be
        //  revisited.
        
        WorkExternal work = null;
        work = getWRWork(orderLicense.getWorkInst());
        
        if (work == null) {
            work = new WorkExternal();
            work.setTfWrkInst(orderLicense.getWorkInst());
            //  fake a wr work inst since it is only used as a hash key and we know we
            //  will only get back one result
            work.setPrimaryKey(12345678L);
            work.setAuthorName(orderLicense.getAuthor());
            work.setIdno(orderLicense.getStandardNumber());
            work.setEdition(orderLicense.getEdition());
            work.setEditorName(orderLicense.getEditor());
            work.setPublisherName(orderLicense.getPublisher());
            work.setFullTitle(orderLicense.getPublicationTitle());
        }
        
        WorkRightsAdapter workRightAdapter = new WorkRightsAdapter(work, true);
        Publication publication = workRightAdapter;

        int usageDescriptorTypeOfUseCode = MapperServices.getUsageDescriptorFromTpuInstAndPrdInst( 
                                          orderLicense.getTpuInst(), orderLicense.getPrdInst() );
      
        int licenseeClass = LicenseeClass.LIC_CLASS_ALL;
        if( orderLicense.isRepublication() )
            licenseeClass = orderLicense.getBusiness().equals( RepublicationConstants.BUSINESS_FOR_PROFIT ) ?
                LicenseeClass.LIC_CLASS_FOR_PROFIT : LicenseeClass.LIC_CLASS_NOT_FOR_PROFIT;
          
        PublicationPermission[] publicationPermissions = 
            publication.getPublicationPermissions( usageDescriptorTypeOfUseCode, licenseeClass );
          
        if( publicationPermissions == null ) return null;
        
        Date publicationDate = orderLicense.isRepublication() ? orderLicense.getContentsPublicationDate() : orderLicense.getPublicationDateOfUse();
          
        PublicationPermission publicationPermission = null;
        
        for( int i = 0; i < publicationPermissions.length; i++ )
        {
            if ( (publicationPermissions[i].getUsageDescriptor().getTypeOfUse() == usageDescriptorTypeOfUseCode ) &&
                //(publicationPermissions[i].getLicenseeClass() == licenseeClass) &&
                 (publicationPermissions[i].getPubBeginDate().compareTo( publicationDate ) <= 0) &&
                 (publicationPermissions[i].getPubEndDate().compareTo(publicationDate) >= 0))
            {
                publicationPermission = publicationPermissions[i];
            }
        }
        
        if( publicationPermission == null ) return null;
        
        return createPurchasablePermission( publication, publicationPermission );
    }      
    
    public static void initPurchasablePermissionFromPublication ( PurchasablePermissionImpl purchasablePermissionImpl,
    		  													  Publication publication)
    {
        WorkRightsAdapter workRightsAdapter = (WorkRightsAdapter) publication;
        if (!StringUtils.isEmpty(workRightsAdapter.getTfWksInst())) {
            purchasablePermissionImpl.setTfWksInst(new Long(workRightsAdapter.getTfWksInst())); 
        }
           	
    	purchasablePermissionImpl.setTfWrkInst( publication.getTFWrkInst() );
    	purchasablePermissionImpl.setItemTypeCd(publication.getPublicationType());
    	purchasablePermissionImpl.setExternalItemId(publication.getTFWrkInst());
        purchasablePermissionImpl.setStandardNumber( publication.getMainIDNo() );
        purchasablePermissionImpl.setItemDescription( publication.getMainTitle() );
        purchasablePermissionImpl.setAuthor( publication.getMainAuthor() );
        purchasablePermissionImpl.setEditor( publication.getMainEditor() );
        purchasablePermissionImpl.setVolume( publication.getVolume() );
        purchasablePermissionImpl.setEdition( publication.getEdition() );
        purchasablePermissionImpl.setPublisher( publication.getMainPublisher() );
        
        purchasablePermissionImpl.setItemSourceKey(publication.getWrkInst());   
        purchasablePermissionImpl.setSeries(publication.getSeries());
        purchasablePermissionImpl.setSeriesNumber(publication.getSeriesNumber());
        purchasablePermissionImpl.setPublicationType(publication.getPublicationType());
        purchasablePermissionImpl.setCountry(publication.getCountry());
        purchasablePermissionImpl.setLanguage(publication.getLanguage());
        purchasablePermissionImpl.setIdnoLabel(publication.getIdnoLabel());
        purchasablePermissionImpl.setIdnoTypeCd(publication.getIdnoTypeCd());
        purchasablePermissionImpl.setPages(publication.getPages());
        
    }
    
    public static void initPurchasablePermissionFromWorkExternal ( PurchasablePermissionImpl ppImpl,
			  WorkExternal workExternal)
    {
 	
    	ppImpl.setItemTypeCd(workExternal.getPublicationType());
    	ppImpl.setExternalItemId(workExternal.getTfWrkInst());
    	ppImpl.setStandardNumber( workExternal.getIdnoWop());
    	ppImpl.setItemDescription( workExternal.getFullTitle());
    	ppImpl.setAuthor( workExternal.getAuthorName());
    	ppImpl.setEditor( workExternal.getEditorName());
    	ppImpl.setVolume( workExternal.getVolume() );
    	ppImpl.setEdition( workExternal.getEdition() );
    	ppImpl.setPublisher( workExternal.getPublisherName());

    	ppImpl.setItemSourceKey(workExternal.getPrimaryKey());   
    	ppImpl.setSeries(workExternal.getSeries());
    	ppImpl.setSeriesNumber(workExternal.getSeriesNumber());
    	ppImpl.setPublicationType(workExternal.getPublicationType());
    	ppImpl.setCountry(workExternal.getCountry());
    	ppImpl.setLanguage(workExternal.getLanguage());
    	ppImpl.setIdnoLabel(workExternal.getIdnoTypeCode());
    	ppImpl.setIdnoTypeCd(workExternal.getIdnoTypeCode());
    	try {ppImpl.setPages(workExternal.getPages().toString()); } catch (Exception e) {};

    }

    
    
    public static void initPurchasablePermissionFromStandardWork ( PurchasablePermissionImpl purchasablePermissionImpl,
			   WRStandardWork WrStandardWork)
    {
    	
    	purchasablePermissionImpl.setTfWrkInst( WrStandardWork.getWrkInst());
        purchasablePermissionImpl.setExternalItemId(WrStandardWork.getWrkInst() == 0 ? null : WrStandardWork.getWrkInst() );
    	purchasablePermissionImpl.setStandardNumber( WrStandardWork.getStandardNumber());
    	purchasablePermissionImpl.setItemDescription( WrStandardWork.getMainTitle());
    	purchasablePermissionImpl.setAuthor( WrStandardWork.getMainAuthor() );
    	purchasablePermissionImpl.setEditor( WrStandardWork.getMainEditor() );
    	purchasablePermissionImpl.setVolume( WrStandardWork.getVolume() );
    	purchasablePermissionImpl.setEdition( WrStandardWork.getEdition() );
    	purchasablePermissionImpl.setPublisher( WrStandardWork.getPublisher() );

        purchasablePermissionImpl.setItemSourceKey(WrStandardWork.getWrkInst());   
    	purchasablePermissionImpl.setSeries(WrStandardWork.getSeries());
    	purchasablePermissionImpl.setSeriesNumber(WrStandardWork.getSeriesNumber());
    	purchasablePermissionImpl.setPublicationType(WrStandardWork.getPublicationType());
    	purchasablePermissionImpl.setCountry(WrStandardWork.getCountry());
    	purchasablePermissionImpl.setLanguage(WrStandardWork.getLanguage());
    	purchasablePermissionImpl.setIdnoLabel(WrStandardWork.getIdnoLabel());
    	purchasablePermissionImpl.setIdnoTypeCd(WrStandardWork.getIdnoTypeCd());
    	purchasablePermissionImpl.setPages(WrStandardWork.getPages());

    }
    
    public static PermissionRequest buildPermissionRequest( PurchasablePermissionImpl ppImpl ) 
    throws UnableToBuildPurchasablePermissionException
    {

      LOGGER.info("\n* In buildPermissionRequest(publicationPermission)\n");
      
      long rightId = 0;
      if (ppImpl.getExternalRightId() != null) {
    	  rightId = ppImpl.getExternalRightId().longValue();
      }
      
      PermissionRequest permissionRequest = new PermissionRequest( rightId );
      
      permissionRequest.setContactRHPermission( ppImpl.isSpecialOrder() );
      
      Work work = buildWork( ppImpl );
      
      permissionRequest.setWork( work );
      
      UsageData usageData = buildUsageData( ppImpl );
      
      permissionRequest.setUsageData( usageData );
      
      return permissionRequest;
    }
    
    private static WRStandardWork buildWork( PurchasablePermissionImpl ppImpl )
    {
        //long tfWrkInst = ppImpl.getExternalItemId();
        //long wrWrkInst = ppImpl.getItemSourceKey();
    	
    	long tfWrkInst = 0;
        long wrWrkInst = 0;
        
        if (ppImpl.getExternalItemId() != null)
        {
        	tfWrkInst = ppImpl.getExternalItemId();
        }
        
        if (ppImpl.getItemSourceKey() != null)
        {
        	wrWrkInst = ppImpl.getItemSourceKey();
        }
        
        //  The WRStandardWork extends the StandardWork
        //  object which implements our shared library
        //  version of the "Work" object (as opposed to the
        //  WorkRetriever service version of Work).
        
        WRStandardWork work = new WRStandardWork();
        
        //  I am using the full path name as a reminder that
        //  this code should be revisited in the future.
        
//        WorkExternal wrWork = getWRWork(tfWrkInst);
        
        work.setSeries(ppImpl.getSeries());
        work.setSeriesNumber(ppImpl.getSeriesNumber());
        work.setPublicationType(ppImpl.getPublicationType());
        work.setCountry(ppImpl.getCountry());
        work.setLanguage(ppImpl.getLanguage());
        work.setIdnoLabel(ppImpl.getIdnoLabel());
        work.setIdnoTypeCd(ppImpl.getIdnoTypeCd());
        work.setPages(ppImpl.getPages());

        work.setWrwrkinst(wrWrkInst);
        work.setWrkInst(tfWrkInst);
        work.setStandardNumber( ppImpl.getStandardNumber() );
        work.setMainTitle( ppImpl.getItemDescription());
        work.setMainAuthor( ppImpl.getAuthor() );
        work.setMainEditor( ppImpl.getEditor() );
        work.setVolume( ppImpl.getVolume() );
        work.setEdition( ppImpl.getEdition() );
        work.setPublisher( ppImpl.getPublisher() );
        work.setPublicationYear(  ppImpl.getPublicationYear() );
         
        return work;
    }
    
    private static UsageData buildUsageData( PurchasablePermissionImpl ppImpl ) 
//    throws UnableToBuildPurchasablePermissionException
    {
      UsageData usageData = null;
      
      if( ppImpl.isAcademic() )
      {
        usageData = buildUsageDataAcademic( ppImpl );  
      }
      
      if( ppImpl.isRepublication() )
      {
        usageData = buildUsageDataRepublication( ppImpl );
      }
      
      if( ppImpl.isPhotocopy() )
      {
        usageData = buildUsageDataPhotocopy( ppImpl );
      }
      
      if( ppImpl.isEmail() )
      {
        usageData = buildUsageDataEmail( ppImpl );
      }
      
      if( ppImpl.isNet() )
      {
        usageData = buildUsageDataNet( ppImpl );
      }
      
//      if( usageData == null )
//      {
//        throw new UnableToBuildPurchasablePermissionException( "Unable to build an instance of UsageData" );
//      }
      
      return usageData;
    }

    private static UsageDataAcademic buildUsageDataAcademic( PurchasablePermissionImpl ppImpl )
    {
      UsageDataAcademic usageDataAcademic = new UsageDataAcademic();
      
      usageDataAcademic = (UsageDataAcademic) populateCommonUsageData( usageDataAcademic, ppImpl );
      
      usageDataAcademic.setNumPages(ppImpl.getNumberOfPages());
      usageDataAcademic.setNumStudents(Long.valueOf(ppImpl.getNumberOfStudents()));      

      usageDataAcademic.setChapterArticle(ppImpl.getChapterArticle());
      usageDataAcademic.setAuthor( ppImpl.getCustomAuthor());
      usageDataAcademic.setEdition(ppImpl.getCustomEdition());
      usageDataAcademic.setVolume( ppImpl.getCustomVolume());
      usageDataAcademic.setPageRanges(ppImpl.getPageRange());
      usageDataAcademic.setDateOfIssue(ppImpl.getDateOfIssue());
      usageDataAcademic.setPublicationDate(ppImpl.getPublicationDateOfUse());
      
      return usageDataAcademic;
    }
    
    private static UsageData buildUsageDataRepublication( PurchasablePermissionImpl ppImpl )
    {
      
    	UsageDataRepublication usageDataRepublication = new UsageDataRepublication();
      
      	usageDataRepublication = (UsageDataRepublication) populateCommonUsageData( usageDataRepublication, ppImpl );
      
      	usageDataRepublication.setChapterArticle(ppImpl.getChapterArticle());
      	//rlsPages
      	usageDataRepublication.setRlsPages(ppImpl.getNumberOfPages());
            
      	//circulation
      	usageDataRepublication.setCirculation(ppImpl.getCirculationDistribution() );
      
      	//forProfit
      	if( RepublicationConstants.BUSINESS_FOR_PROFIT.equals(ppImpl.getBusiness()) )
      	{
      		usageDataRepublication.setForProfit( RepublicationConstants.FOR_PROFIT );
        
      	}else
      	{
      		usageDataRepublication.setForProfit( RepublicationConstants.NON_FOR_PROFIT );
      	}
      
      	usageDataRepublication = ECommerceUtils.zeroContentTypeQuantities( usageDataRepublication );
      
      	if (!ppImpl.getTypeOfContent().equals(RepublicationConstants.CONTENT_SELECTED_PAGES)) {
          usageDataRepublication.setRlsPages( 0 );
      	}
            
      	usageDataRepublication = ECommerceUtils.setTypeOfContent( usageDataRepublication, ppImpl.getTypeOfContent() );
  	      
      	usageDataRepublication.setNumCartoons(ppImpl.getNumberOfCartoons());
      	usageDataRepublication.setNumExcerpts(ppImpl.getNumberOfExcerpts());
      	usageDataRepublication.setNumFigures(ppImpl.getNumberOfFigures());
      	usageDataRepublication.setNumGraphs(ppImpl.getNumberOfGraphs());
      	usageDataRepublication.setNumIllustrations(ppImpl.getNumberOfIllustrations());
      	usageDataRepublication.setNumLogos(ppImpl.getNumberOfLogos());
      	usageDataRepublication.setNumPhotos(ppImpl.getNumberOfPhotos());
      	usageDataRepublication.setNumQuotes(ppImpl.getNumberOfQuotes());

      
  	//origAuthor
  		if( ppImpl.isSubmitterAuthor() )
  		{
  			usageDataRepublication.setOrigAuthor( RepublicationConstants.SUBMITTER_IS_AUTHOR);           
  		}else
  		{
  			usageDataRepublication.setOrigAuthor( RepublicationConstants.SUBMITTER_IS_NOT_AUTHOR);
  		}
  	      
  	//publicationDate
//  	usageDataRepublication.setPublicationDate( ppImpl.getContentsPublicationDate() );  // Points to same place as below
  		usageDataRepublication.setPublicationDate(ppImpl.getPublicationDateOfUse());
  	       
  		//hdrRepubPub
  		usageDataRepublication.setHdrRepubPub( StringUtils.upperCase(ppImpl.getRepublishingOrganization()) );
  	      
  	//repubTitle
//  	usageDataRepublication.setRepublicationDestination( StringUtils.upperCase(ppImpl.getNewPublicationTitle()) );
  	    
  		//repubDate
  		usageDataRepublication.setRepubDate( ppImpl.getRepublicationDate() );
  	       
  		//language & translated
  		if( ppImpl.getTranslationLanguage().equals( RepublicationConstants.NO_TRANSLATION ) )
  		{
  			usageDataRepublication.setLanguage( Constants.EMPTY_STRING );
  			usageDataRepublication.setTranslated( RepublicationConstants.NOT_TRANSLATED);
  		}else
  		{
  			usageDataRepublication.setLanguage( ppImpl.getTranslationLanguage() );
  			usageDataRepublication.setTranslated( RepublicationConstants.TRANSLATED );
  		}
  	      
  		//lcnHdrRefNum
  		usageDataRepublication.setLcnHdrRefNum(ppImpl.getYourReference() );
  	      
  		//author
  		usageDataRepublication.setAuthor( ppImpl.getCustomAuthor() );
  	      
  		usageDataRepublication.setPageRanges( ppImpl.getPageRange() );
  	
      
  		return usageDataRepublication;
    }
    
    private static UsageData buildUsageDataEmail(PurchasablePermissionImpl ppImpl )
    {
      UsageDataEmail usageDataEmail = new UsageDataEmail();
      
      usageDataEmail = (UsageDataEmail) populateCommonUsageData( usageDataEmail, ppImpl );
      
      usageDataEmail.setChapterArticle(ppImpl.getChapterArticle());
      usageDataEmail.setNumRecipients(ppImpl.getNumberOfRecipients());
      usageDataEmail.setDateOfUse(ppImpl.getDateOfUse());
      usageDataEmail.setPublicationDate(ppImpl.getPublicationDateOfUse());
          
      return usageDataEmail;
    }
    
    private static UsageData buildUsageDataNet(PurchasablePermissionImpl ppImpl)
    {
      UsageDataNet usageDataNet = new UsageDataNet();
      
      usageDataNet = (UsageDataNet) populateCommonUsageData( usageDataNet, ppImpl );

      usageDataNet.setChapterArticle(ppImpl.getChapterArticle());
      usageDataNet.setDateOfUse(ppImpl.getDateOfUse());
      usageDataNet.setDuration(ppImpl.getDuration());
      usageDataNet.setWebAddress(ppImpl.getWebAddress());
      usageDataNet.setPublicationDate(ppImpl.getPublicationDateOfUse());
            
      return usageDataNet;
    }
    
    private static UsageData buildUsageDataPhotocopy(PurchasablePermissionImpl ppImpl )
    {
      UsageDataPhotocopy usageDataPhotocopy = new UsageDataPhotocopy();
      
      usageDataPhotocopy = (UsageDataPhotocopy) populateCommonUsageData( usageDataPhotocopy, ppImpl );

      usageDataPhotocopy.setNumPages(ppImpl.getNumberOfPages());
      usageDataPhotocopy.setNumCopies(ppImpl.getNumberOfCopies());      

      usageDataPhotocopy.setChapterArticle(ppImpl.getChapterArticle());
//      usageDataPhotocopy.setAuthor( ppImpl.getCustomAuthor());
//      usageDataPhotocopy.setEdition(ppImpl.getCustomEdition());
//      usageDataPhotocopy.setVolume( ppImpl.getCustomVolume());
      usageDataPhotocopy.setPublicationDate(ppImpl.getPublicationDateOfUse());
      
      return usageDataPhotocopy;
    }
    
    private static UsageData populateCommonUsageData( UsageData usageData, PurchasablePermissionImpl ppImpl )
    {
      UsageData updatedUsageData = usageData;
      
      updatedUsageData.setProduct( ppImpl.getProductSourceKey() );
      updatedUsageData.setTpuInst( ppImpl.getExternalTouId() );
      updatedUsageData.setTypeOfUseDisplay( ppImpl.getTouName());
             
      return updatedUsageData;
    }
    
    

}
