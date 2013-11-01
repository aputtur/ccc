package com.copyright.ccc.web.transaction.coi;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.cart.CCLimitsExceededException;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.business.services.cart.SystemLimitsExceededException;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.ccc.web.WebConstants;

import com.copyright.ccc.web.transaction.ChangedToRegularFromSpecialUnavailableException;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.util.PubDateDisplayUtil;
import com.copyright.data.SharedAppResources;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.service.inventory.MapperServiceAPI;
import com.copyright.service.inventory.MapperServiceFactory;
import com.copyright.workbench.time.DateUtils2;

import java.util.Date;

import java.util.Iterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class TransactionUtils
{
    private static final String LIMITS_EXCEEDED_SUBSTRING = "cannot be greater than";
    
    private TransactionUtils(){}
    
    public static boolean isSystemLimitsExceededError( InvalidAttributesException iae )
    {
        SharedAppResources sharedAppResources = SharedAppResources.getInstance();
        
        Iterator<String> validationErrorsIterator = iae.getValidationMessageCodes().iterator();
        
        Pattern pattern = Pattern.compile( LIMITS_EXCEEDED_SUBSTRING, Pattern.CASE_INSENSITIVE );
        
        while( validationErrorsIterator.hasNext() )
        {
            String validationErrorsMessage = sharedAppResources.getValue( validationErrorsIterator.next() );
            
            Matcher matcher = pattern.matcher( validationErrorsMessage );
            
            boolean matchFound = matcher.find();
                
            if( matchFound )
                return true;
        }
        
        return false;
    }
    
    public static boolean displayInitialPublicationYearRange( PublicationPermission publicationPermission, PurchasablePermission purchasablePermission )
    {
        boolean isDigital = purchasablePermission.isDigital();
        boolean isRepublication = purchasablePermission.isRepublication();
        
        if( isDigital || isRepublication )
        {
            Publication publication = publicationPermission.getPublication();
            boolean isBiactive = isDigital ? publication.isDigitalBiactive() : publication.isRepublicationBiactive();
            
            if( isBiactive ) return false;
            else return true;
        }
        else
        {
            return true;
        }
    }
    
    public static String getExistingPublicationYearRange( TransactionItem transactionItem )
    {
        return PubDateDisplayUtil.computeYearRangeDisplay( 
            transactionItem.getPublicationStartDate(), transactionItem.getPublicationEndDate() );
    }
    
    public static boolean validateInBiactiveDateRange( TransactionItem transactionItem )
    {
        if( !transactionItem.isSpecialOrderFromScratch() )
        {
            Date startDate = transactionItem.getPublicationStartDate();
            Date endDate = transactionItem.getPublicationEndDate();
            
            Date publicationDateOfUse;
            if( transactionItem.isRepublication())
                publicationDateOfUse = transactionItem.getContentsPublicationDate();
            else
                publicationDateOfUse = transactionItem.getPublicationDateOfUse();
            
            //TODO: gcuevas: handle cases when dates are null

            int startYear = startDate==null?1000 : DateUtils2.getYear( startDate.getTime() );
            int endYear = endDate==null?3000 : DateUtils2.getYear( endDate.getTime() );
            int publicationYearOfUse;
            
            if (publicationDateOfUse != null)
            {
            	publicationYearOfUse = DateUtils2.getYear( publicationDateOfUse.getTime() );
            }
            else
            {
            	try {
            		publicationYearOfUse = Integer.parseInt(transactionItem.getPublicationYearOfUse());
            	} catch (Exception e) {
            		publicationYearOfUse = 0;
            	}
            	
            }
            
            if( publicationYearOfUse < startYear || publicationYearOfUse > endYear )
                return false;
        }
        
        return true;
    }
    
    public static String getReasonForLimitsExceededException( CCLimitsExceededException clee )
    {
        String exceedingAttribute = clee.getExceedingAttribute();
        
        if( exceedingAttribute.equals(TransactionConstants.ATTRIBUTE_CIRCULATION) )
            return TransactionConstants.REASON_CIRCULATION;
        else if( exceedingAttribute.equals(TransactionConstants.ATTRIBUTE_DURATION) )
            return TransactionConstants.REASON_DURATION;
        else if( exceedingAttribute.equals(TransactionConstants.ATTRIBUTE_NUMBER_OF_COPIES) )
            return TransactionConstants.REASON_NUMBER_OF_COPIES;
        else if ( exceedingAttribute.equals(TransactionConstants.ATTRIBUTE_NUMBER_OF_PAGES) )
            return TransactionConstants.REASON_NUMBER_OF_PAGES;
        else if( exceedingAttribute.equals(TransactionConstants.ATTRIBUTE_NUMBER_OF_RECIPIENTS) )
            return TransactionConstants.REASON_NUMBER_OF_RECIPIENTS;
        else if( exceedingAttribute.equals(TransactionConstants.ATTRIBUTE_NUMBER_OF_STUDENTS) )
            return TransactionConstants.REASON_NUMBER_OF_STUDENTS;
        else
            return TransactionConstants.REASON_UNKNOWN;
    }
    
    public static PurchasablePermission createSpecialOrderPurchasablePermission(String permissionType)
    {
        if(permissionType.equals(WebConstants.PERMISSION_TYPE_APS))
            return PurchasablePermissionFactory.createAPSSpecialOrderPurchasablePermission();
        else if(permissionType.equals(WebConstants.PERMISSION_TYPE_ECCS))
            return PurchasablePermissionFactory.createECCSSpecialOrderPurchasablePermission();
        else if(permissionType.equals(WebConstants.PERMISSION_TYPE_TRS))
            return PurchasablePermissionFactory.createPhotocopySpecialOrderPurchasablePermission();
        else if(permissionType.equals(WebConstants.PERMISSION_TYPE_DPS))
            return PurchasablePermissionFactory.createEmailSpecialOrderPurchasablePermission();
        else if(permissionType.equals(WebConstants.PERMISSION_TYPE_RLS))
        {
            //make it a brochure
            MapperServiceAPI api = MapperServiceFactory.getInstance().getService();
            long tpuInst = api.getUnderlyingTpuInst(UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE);
            return PurchasablePermissionFactory.createRepublicationSpecialOrderPurchasablePermission(tpuInst);
        }
        else return null;
    }
    
    public static ActionMessages getIncorrectCartItemTypeError()
    {
        ActionMessages incorrectCartItemTypeError = new ActionMessages();
        incorrectCartItemTypeError.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.incorrectCartItemType" ) );
        return incorrectCartItemTypeError;
    }
    
    public static ActionMessages getPricingServiceInvocationError()
    {
        ActionMessages incorrectCartItemTypeError = new ActionMessages();
        incorrectCartItemTypeError.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.pricingServiceInvocationError" ) );
        return incorrectCartItemTypeError;
    }
    
    public static ActionMessages getIncorrectOrderItemTypeError()
    {
        ActionMessages incorrectOrderItemTypeError = new ActionMessages();
        incorrectOrderItemTypeError.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.incorrectOrderItemType" ) );
        return incorrectOrderItemTypeError;
    }

    public static ActionMessages getItemCannotBePurchasedError( ItemCannotBePurchasedException itemCannotBePurchasedException )
    {
        String messageKey = null;
        if(itemCannotBePurchasedException.isInPublicDomain())
            messageKey = "errors.itemInPublicDomain";
        else 
            messageKey = "errors.itemCannotBePurchased";
     
        ActionMessages itemCannotBePurchasedError = new ActionMessages();       
        itemCannotBePurchasedError.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( messageKey ) );
        return itemCannotBePurchasedError;
    }
    
    public static ActionMessages getDateNotInBiactiveRangeError()
    {
        ActionMessages dateNotInBiactiveRangeError = new ActionMessages();
        dateNotInBiactiveRangeError.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "errors.dateNotInRange" ) );
        return dateNotInBiactiveRangeError;
    }
    
    public static ActionMessages getInvalidAttributeValidationErrors( InvalidAttributesException iae )
    {
        Iterator<String> messageCodesIterator = iae.getValidationMessageCodes().iterator();
        
        SharedAppResources sharedAppResources = SharedAppResources.getInstance();
        ActionMessages validationErrors = new ActionMessages();
        while( messageCodesIterator.hasNext() )
        {
            validationErrors.add( ActionMessages.GLOBAL_MESSAGE, 
                new ActionMessage( sharedAppResources.getValue( messageCodesIterator.next() ), false ) );
        }
        
        return validationErrors;
    }
    
    public static ActionMessages getPushToTFFailedError()
    {
        ActionMessages pushToTFFailedError = new ActionMessages();
        pushToTFFailedError.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "pricing.error.message.notAvailableSpecialOrder" ) );
        return pushToTFFailedError;
    }
    
    public static String getItemPrice( TransactionItem transactionItem ) 
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException, 
                ContactRHDirectlyLimitsExceededException, SystemLimitsExceededException, ServiceInvocationException
    {
        String itemPrice = "";
        
        if( transactionItem instanceof OrderLicense ) {
        	OrderLicense orderLicense = (OrderLicense) transactionItem;
        	if (orderLicense.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
            	itemPrice = PricingServices.getItemPrice( orderLicense );        		
        	} else {
        		try {
        		itemPrice = com.copyright.ccc.business.services.ecommerce.PricingServices.getItemPrice(orderLicense);
        		} 
        		  catch ( com.copyright.ccc.business.services.ecommerce.DeniedLimitsExceededException deniedLimitsExceededException )
        		  {     	      
        	      throw new DeniedLimitsExceededException( orderLicense, deniedLimitsExceededException.getLimitsExceededException() );
        	    }
        	    
        		  catch ( com.copyright.ccc.business.services.ecommerce.SpecialOrderLimitsExceededException specialOrderLimitsExceededException )
        		  {      	      
        	      throw new SpecialOrderLimitsExceededException( orderLicense, specialOrderLimitsExceededException.getLimitsExceededException() );
        	    }
        	    
        		  catch( com.copyright.ccc.business.services.ecommerce.ContactRHDirectlyLimitsExceededException contactRHDirectlyLimitsExceededException )
        	    {
        	      
        	      throw new ContactRHDirectlyLimitsExceededException ( orderLicense, contactRHDirectlyLimitsExceededException.getLimitsExceededException() );
        	    }
        	    
        		  catch( com.copyright.ccc.business.services.ecommerce.SystemLimitsExceededException systemLimitsExceededException )
        		  {
     		    
       		      throw new SystemLimitsExceededException( orderLicense, systemLimitsExceededException.getLimitsExceededException() );
        		  }
        	    
        		  catch ( com.copyright.ccc.business.services.ecommerce.InvalidAttributesException invalidAttributesException )
        		  {      
      	      
        	      throw new InvalidAttributesException( invalidAttributesException.getValidationException(), orderLicense );
        		  }
        	}
        } else if( transactionItem instanceof PurchasablePermission ) {
            itemPrice = PricingServices.getItemPrice( (PurchasablePermission)transactionItem );
        }	
        return itemPrice;
    }
    
    /**
     * This method will attempt to find a match for the special order from scratch item, based
     * on the item values that the user entered.  If a match is found, the correct special order item is returned.
     * Note that if there is a publication match and the publication is biactive, it will attempt to return
     * the one that falls in the user entered date.  Otherwise, it a "date" match is not found, it will just
     * return the first right returned that matches the type of use, regardless of the date.
     * 
     * @param specialOrderItem - special order item from scratch
     */
    public static TransactionItem pinSpecialOrderFromScratch( TransactionItem specialOrderItem, int typeOfUseCode ) 
        throws DeniedUnavailableException, ContactRHDirectlyUnavailableException, ChangedToRegularFromSpecialUnavailableException
    {
        if( !specialOrderItem.isSpecialOrderFromScratch() )
            return specialOrderItem;
        
        if( specialOrderItem instanceof OrderLicense )
            return specialOrderItem;
        
        PurchasablePermission specialPurchasablePermission = (PurchasablePermission)specialOrderItem;
        int usageDescriptorCode = TransactionItemTypeOfUseMapper.getUsageDescriptorForTypeOfUseCode( typeOfUseCode );
        
        
        if( specialPurchasablePermission.isDigital() || specialPurchasablePermission.isRepublication() )
        {
            specialPurchasablePermission = TransactionItemTypeOfUseMapper.getPurchasablePermissionFromScratchForTypeOfUse( typeOfUseCode, specialPurchasablePermission );
        }
        
        String standardNumber = specialPurchasablePermission.getStandardNumber();
        
        WebServiceSearch searchServices = new WebServiceSearch();
        
        Publication publication = searchServices.getSingleItem( standardNumber );
        
        if( publication == null || publication.getAdaptedRights() == null || publication.getAdaptedRights().size()==0)
            return specialPurchasablePermission;
        
        PublicationPermission[] publicationPermissions = publication.getPublicationPermissionsByMostPermissable( usageDescriptorCode );
        
        if( publicationPermissions == null || publicationPermissions.length <= 0 )
            return specialPurchasablePermission;
        
        PublicationPermission publicationPermission = null;
        
        for( int i = 0; i < publicationPermissions.length; i++ )
        {
            Date publicationDate = specialPurchasablePermission.isRepublication() ? 
                specialPurchasablePermission.getContentsPublicationDate() : specialPurchasablePermission.getPublicationDateOfUse();
            
            if ( publicationPermissions[i].getPubBeginDate() != null && publicationPermissions[i].getPubEndDate() != null )
            {
            	if ( (publicationPermissions[i].getPubBeginDate().compareTo( publicationDate ) <= 0) &&
            			(publicationPermissions[i].getPubEndDate().compareTo(publicationDate) >= 0))
            	{
            		publicationPermission = publicationPermissions[i];
            	}
            }
        }
        
        if( publicationPermission == null )
            return specialPurchasablePermission;
        
                
      PurchasablePermission pinnedPurchasablePermission = 
            PurchasablePermissionFactory.createPurchasablePermission( publicationPermission ); 
            
        
        TransactionItemPopulator.populatePurchasablePermission( pinnedPurchasablePermission, specialPurchasablePermission );
        
        if( publicationPermission.isDenied() )
            throw new DeniedUnavailableException( pinnedPurchasablePermission );
        else if( publicationPermission.isContactRHDirectly() )
            throw new ContactRHDirectlyUnavailableException( pinnedPurchasablePermission );
        else if( publicationPermission.isAvailable() )
            throw new ChangedToRegularFromSpecialUnavailableException( pinnedPurchasablePermission );
        
        return pinnedPurchasablePermission;
    }
}
