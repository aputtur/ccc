package com.copyright.ccc.business.services.adjustment;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.business.services.user.UserServices;
import com.copyright.svc.telesales.api.data.ARAccount;
import com.copyright.svc.telesales.api.data.Location;
import com.copyright.svc.telesales.api.data.Organization;

/**
 * Class responsible for creating instances of <code>OrderAdjustment</code>.
 */
class OrderAdjustmentFactory
{
  //TODO lalberio 06/05/2007: confirm this value's correctness.
  private static final long PURCHASE_ID_MIN_VALUE = 0L;

  static OrderAdjustment createNewInvoiceOrderAdjustment( String sourceID, 
                                                          OrderLicenses orderLicenses )
  {
    return OrderAdjustmentFactory.createNewOrderAdjustment( OrderAdjustmentConstants.ADJUSTMENT_TYPE_INVOICE,
                                                            sourceID,
                                                            orderLicenses );
  }
  
  
  static OrderAdjustment createNewPurchaseOrderAdjustment( String sourceID, 
                                                           OrderLicenses orderLicenses )
  {
    return OrderAdjustmentFactory.createNewOrderAdjustment( OrderAdjustmentConstants.ADJUSTMENT_TYPE_PURCHASE,
                                                            sourceID,
                                                            orderLicenses );
  }
  
  
  static OrderAdjustment createNewDetailOrderAdjustment( String sourceID, 
                                                         OrderLicenses orderLicenses )
  {
    
    return OrderAdjustmentFactory.createNewOrderAdjustment( OrderAdjustmentConstants.ADJUSTMENT_TYPE_DETAIL,
                                                            sourceID,
                                                            orderLicenses );
  }
  
  private static OrderAdjustment createNewOrderAdjustment( String adjustmentType, 
                                                           String sourceID, 
                                                           OrderLicenses orderLicenses )
  {
    boolean isValidAdjustmentType = adjustmentType == null ||
                                    OrderAdjustmentConstants.ADJUSTMENT_TYPE_INVOICE.equals( adjustmentType ) ||
                                    OrderAdjustmentConstants.ADJUSTMENT_TYPE_PURCHASE.equals( adjustmentType ) ||
                                    OrderAdjustmentConstants.ADJUSTMENT_TYPE_DETAIL.equals( adjustmentType );
                                    
    if( !isValidAdjustmentType )
    {
      throw new IllegalArgumentException( "Invalid adjustment type: " + adjustmentType );
    }
    
    if( StringUtils.isEmpty( sourceID ) )
    {
      throw new IllegalArgumentException( "Source ID must be provided" );
    }

    boolean isInconsistentOrderLicenses =  orderLicenses == null || 
                                           orderLicenses.getOrderLicenseList() == null || 
                                           orderLicenses.getOrderLicenseList().isEmpty();
    if ( isInconsistentOrderLicenses )
    {
      throw new IllegalArgumentException( "A non-empty OrderLicenses instance must be provided" );
    }
    
    OrderAdjustment orderAdjustment = new OrderAdjustment();
    
    orderAdjustment.setAdjustmentType( adjustmentType );
    orderAdjustment.setSourceID( sourceID );

    Date now = new Date();
    orderAdjustment.setCreateDate( now );
    orderAdjustment.setModifyDate( now ); 
    
    String userDisplayName = UserContextService.getSharedUser().getDisplayName();
    orderAdjustment.setCreateUser( userDisplayName );
    orderAdjustment.setModifyUser( userDisplayName );
    
    //1- Create customer details
    long purchaseID = orderLicenses.getOrderLicense(0).getPurchaseId(); 
    OrderAdjustmentCustomerDetails customerDetails = createCustomerDetails( purchaseID );
    orderAdjustment.setCustomer(customerDetails);
        
    //2- Create body
    Map<String,OrderAdjustmentBodyItem> body = 
    	new TreeMap<String,OrderAdjustmentBodyItem>( new OrderAdjustmentBodyItemComparator() );
    
    Iterator<OrderLicense> orderLicensesIterator = orderLicenses.getOrderLicenseList().iterator();
    
    boolean customerFound = ( null != customerDetails );
    while( orderLicensesIterator.hasNext() )
    {
      OrderLicense orderLicense = orderLicensesIterator.next();
      OrderAdjustmentBodyItem oabi = new OrderAdjustmentBodyItem( orderLicense );
      if( customerFound ){
        oabi.setOriginalLicenseePartyId( orderAdjustment.getCustomer().getPartyId() );  
      } else {
        oabi.setOriginalLicenseePartyId( null );
      }
      
      body.put( String.valueOf( orderLicense.getID() ) , oabi );
    }
    
    orderAdjustment.setBody( body );
    
    orderAdjustment.loadPersistedSummaryIfAppropiate();
    
    return orderAdjustment;  
  }

  private static OrderAdjustmentCustomerDetails createCustomerDetails( long purchaseID )
  {

    if( purchaseID < PURCHASE_ID_MIN_VALUE )
    {
      throw new IllegalArgumentException( "Invalid purchase Id provided: " + purchaseID );
    }
    
    OrderAdjustmentCustomerDetails customerDetails = new OrderAdjustmentCustomerDetails();

    OrderPurchases orderPurchases = null;

    try
    {
      orderPurchases = OrderPurchaseServices.getOrderPurchasesForConfNum(purchaseID);
    } catch (OrderPurchasesException ope)
    {
      orderPurchases = null;
    }
    
    
    User user = null;

    boolean orderPurchasesFound = orderPurchases != null;
    
    if (orderPurchasesFound)
    {
      OrderPurchase orderPurchase = orderPurchases.getOrderPurchase(0);

      boolean purchasePresent = orderPurchase != null;
      
      if (purchasePresent)
      {
        long byrInst = orderPurchase.getByrInst();
        user = UserServices.getUserByPtyInst( byrInst );
      }
    }

    Organization organization = null;
    Location address = null;
    ARAccount account = null;

    boolean userFound = user != null;
    
    if ( userFound )
    {
      organization = user.getOrganization();
      address = user.getMailingAddress();
      account = user.getAccount();
      
      customerDetails.setPartyId( user.getPartyId() );
      customerDetails.setFullName( user.getName() );
    }
    
   boolean userAccountPresent = account != null;
    
    if ( userAccountPresent )
   {
     customerDetails.setAccountNumber( Long.valueOf(account.getAccountNumber()) );
   }
      
   

   boolean organizationPresent = organization != null;
   
   if ( organizationPresent )
   {
     customerDetails.setCompanyName( organization.getOrganizationName() );
   }

    boolean addressPresent = address != null;
    
    if ( addressPresent )
    {
     customerDetails.setAddress1( address.getAddress1() );
     customerDetails.setAddress2( address.getAddress2() );
     customerDetails.setAddress3( address.getAddress3() );
     customerDetails.setAddress4( address.getAddress4() );
     customerDetails.setCity( address.getCity() );
     customerDetails.setState( address.getState() );
     customerDetails.setZipCode( address.getPostalCode() );

      boolean countryPresent = address.getCountry() != null;
      
      if ( countryPresent )
     {
       customerDetails.setCountry( address.getCountry() );
     }
     
   }
   
   return customerDetails;
  }
}
