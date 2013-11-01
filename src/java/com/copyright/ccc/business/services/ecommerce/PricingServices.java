package com.copyright.ccc.business.services.ecommerce;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.WRStandardWork;
import com.copyright.ccc.business.services.cart.ECommerceConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.data.ValidationException;
import com.copyright.data.order.PermissionRequest;
import com.copyright.service.pricing.PricingServiceAPI;
import com.copyright.service.pricing.PricingServiceFactory;
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
  
      purchasablePermission = calcPriceAndUpdate( purchasablePermission, licenseePartyID );    
      return purchasablePermission;      
  }

  /**
   * Actual 'hook' into the Shared PricingService.
   */
  private static PurchasablePermission calcPriceAndUpdate( PurchasablePermission purchasablePermission, Long partyIDToCalc )
  throws InvalidAttributesException, 
         DeniedLimitsExceededException,
         SpecialOrderLimitsExceededException, 
         ContactRHDirectlyLimitsExceededException, 
         SystemLimitsExceededException
  {
    PricingServiceAPI pricingService = PricingServiceFactory.getInstance().getService();
    PermissionRequest permissionRequest = purchasablePermission.getPermissionRequest();
    try 
    {
      permissionRequest = pricingService.calcQuickPrice( permissionRequest, partyIDToCalc ) ;
      purchasablePermission.setPermissionRequest( permissionRequest );
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
      LOGGER.info("Contact Rightsholder Directly limits exceeded");
        
      throw new SystemLimitsExceededException ( purchasablePermission, systemLimitsExceededException );
    }
      
    catch ( ValidationException validationException )
    {      
      LOGGER.warn("Invalid PurchasablePermission attributes found");
      
      throw new InvalidAttributesException( validationException, purchasablePermission );
    }
      
    LOGGER.info("PurchasablePermission price updated successfully (" + purchasablePermission.getPrice() + ")");      
  
    return purchasablePermission;
  }

	 /**
	  * Calculates (does not update) the price of a <code>PurchasablePermission</code> instance.
	  * 
	  * @param purchasablePermission <code>PurchasablePermission</code> instance to calculate price for.
	  */
    private static String getItemPrice( PurchasablePermission purchasablePermission ) 
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
    
    PermissionRequest permissionRequest = purchasablePermission.getPermissionRequest();
       
	  PricingServiceAPI pricingService = PricingServiceFactory.getInstance().getService();
    
	  try
	  {
	   LOGGER.debug("Call to PricingServiceAPI.getQuickPrice()");
	   Money price = pricingService.getQuickPrice( permissionRequest, ((WRStandardWork)permissionRequest.getWork()).getWrWrkInst() );
           LOGGER.debug("Returned From Call to PricingServiceAPI.getQuickPrice()");
	   itemPrice = WebUtils.formatMoney( price );
	  
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
	    
	    throw new com.copyright.ccc.business.services.ecommerce.SystemLimitsExceededException( purchasablePermission, systemLimitsExceededException );
	  }
    
	  catch ( ValidationException validationException )
	  {      
      LOGGER.warn("Invalid PurchasablePermission attributes found");
      
      throw new InvalidAttributesException( validationException, purchasablePermission );
    }
    
	  LOGGER.info("PurchasablePermission price updated successfully (" + itemPrice + ")");
    
    return itemPrice;
	}
  
  /**
   * Calculates (does not update) the price of a <code>OrderLicense</code> instance.
   * 
   * @param orderLicense <code>OrderLicense</code> instance to calculate price for.
   */
  public static String getItemPrice( OrderLicense orderLicense ) throws InvalidAttributesException, 
                                                                        DeniedLimitsExceededException, 
                                                                        SpecialOrderLimitsExceededException, 
                                                                        ContactRHDirectlyLimitsExceededException,
                                                                        SystemLimitsExceededException
  {
    
    if( orderLicense == null )
    {
      LOGGER.error("No PurchasablePermission found");
      
      throw new IllegalArgumentException("OrderLicense cannot be null");
    }
              
    PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission( orderLicense );
    
    String price = ECommerceConstants.PRICE_NOT_AVAILABLE;
    
    price = PricingServices.getItemPrice( purchasablePermission );
    
    return price;
      
  }

}
