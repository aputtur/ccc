package com.copyright.ccc.web.transaction.coi;

import java.util.Date;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.search.WebServiceSearch;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.data.inventory.LicenseeClass;
import com.copyright.data.inventory.Right;
import com.copyright.data.inventory.RightPermission;
import com.copyright.data.inventory.UsageDescriptor;
import com.copyright.service.inventory.MapperServiceAPI;
import com.copyright.service.inventory.MapperServiceFactory;
import com.copyright.service.inventory.RightServiceAPI;
import com.copyright.service.inventory.RightServiceFactory;

public class TransactionItemTypeOfUseMapper
{
    public static int getTypeOfUseCodeForTransactionItem( TransactionItem transactionItem )
    {
        if( transactionItem.isPhotocopy() )
            return TransactionConstants.TYPE_OF_USE_PHOTOCOPY;
        else if( transactionItem.isAPS() )
            return TransactionConstants.TYPE_OF_USE_APS;
        else if( transactionItem.isECCS() )
            return TransactionConstants.TYPE_OF_USE_ECCS;
        else if( transactionItem.isEmail() )
            return TransactionConstants.TYPE_OF_USE_EMAIL;
        else if( transactionItem.isExtranet() )
            return TransactionConstants.TYPE_OF_USE_EXTRANET;
        else if( transactionItem.isIntranet() )
            return TransactionConstants.TYPE_OF_USE_INTRANET;
        else if( transactionItem.isInternet() )
            return TransactionConstants.TYPE_OF_USE_INTERNET;
        else if( transactionItem.isRightsLink() )
            return TransactionConstants.TYPE_OF_USE_JOURNAL;
        else if( transactionItem.isRepublication() )
        {
            String republicationTypeOfUse = transactionItem.getRepublicationTypeOfUse();
            return getRepublicationTypeOfUseTransactionConstants( republicationTypeOfUse );
        }
        else
            throw new CCCRuntimeException("Transaction Item does not have a valid type of use.");
    }
    
    public static PurchasablePermission getPurchasablePermissionForTypeOfUse(int typeOfUseCode, PurchasablePermission originalPurchasablePermission)
        throws SpecialOrderUnavailableException, ContactRHDirectlyUnavailableException, DeniedUnavailableException
    {
        boolean isDigital = originalPurchasablePermission.isDigital();
        boolean isRepublication = originalPurchasablePermission.isRepublication();
        
        if( !isDigital && !isRepublication )
        {
            originalPurchasablePermission = checkPurchasablePermissionAvailability( originalPurchasablePermission );
            
            return originalPurchasablePermission;
        }
        
        if( originalPurchasablePermission.isSpecialOrderFromScratch() )
        {
            PurchasablePermission specialOrderFromScratch = 
                getPurchasablePermissionFromScratchForTypeOfUse( typeOfUseCode, originalPurchasablePermission );
            return specialOrderFromScratch;
        }
        else
            return getPurchasablePermissionWithTitleForTypeOfUse( typeOfUseCode, originalPurchasablePermission );
    }
    
    /** 
     * Invokes right service to check for right permissions based on rgt_inst.
     *  Returns true if either manual special order, or if right is known and the permission is available, 
     *  otherwise the appropriate exception is thrown.
    **/
    private static PurchasablePermission checkPurchasablePermissionAvailability( PurchasablePermission purchasablePermission )
        throws SpecialOrderUnavailableException, ContactRHDirectlyUnavailableException, DeniedUnavailableException
    {
        //long rgtInst = purchasablePermission.getRgtInst();
    	
    	Long lngRgtInst;
    	try
    	{
    		lngRgtInst = Long.valueOf(purchasablePermission.getRgtInst());
    	}
    	catch (Exception e)
    	{
    		lngRgtInst = null;
    	}
        
        if (lngRgtInst != null)
        {
        	RightServiceAPI rightService = RightServiceFactory.getInstance().getService();
        	Right right = rightService.getRightById( lngRgtInst, new Date());
        
        	if( right != null )
        	{
        		String rightPermissionCode = right.getPermission().getPermissionValueCode();
        		
        		if (rightPermissionCode.equalsIgnoreCase(RightPermission.PERMISSION_GRANT.getPermissionValueCode())) {
        			purchasablePermission.getItem().setIsSpecialOrder(false);
        		}

        		if( rightPermissionCode.equals( RightPermission.PERMISSION_DENY.getPermissionValueCode() ) ||
        				rightPermissionCode.equals( RightPermission.PERMISSION_HOLD_PENDING.getPermissionValueCode() ) )
        			throw new DeniedUnavailableException( purchasablePermission );
                
        		else if( rightPermissionCode.equals( RightPermission.PERMISSION_CONTACT_RH_DIRECTLY.getPermissionValueCode() ) )
        			throw new ContactRHDirectlyUnavailableException( purchasablePermission );
                
        		else if( rightPermissionCode.equals( RightPermission.PERMISSION_CONTACT_RH.getPermissionValueCode() )||
                     rightPermissionCode.equals( RightPermission.PERMISSION_RESEARCH_FURTHER.getPermissionValueCode() ) )
        			throw new SpecialOrderUnavailableException( purchasablePermission );
        	}
        }
        
        return purchasablePermission;
    }
    
    private static PurchasablePermission getPurchasablePermissionWithTitleForTypeOfUse( int typeOfUseCode, PurchasablePermission originalPurchasablePermission)
        throws SpecialOrderUnavailableException, ContactRHDirectlyUnavailableException, DeniedUnavailableException
    {
        PublicationPermission newPublicationPermission = null;
        PurchasablePermission newPurchasablePermission = null;
        
        if( originalPurchasablePermission.isDigital() )
        {
            newPublicationPermission = getCorrectPublicationPermissionForTypeOfUse( originalPurchasablePermission.getWorkInst(),
                typeOfUseCode, LicenseeClass.LIC_CLASS_ALL, originalPurchasablePermission.getPublicationDateOfUse() );
                
            if (newPublicationPermission == null) 
            {
                throw new DeniedUnavailableException( originalPurchasablePermission );
            }
            
            newPurchasablePermission = PurchasablePermissionFactory.createPurchasablePermission( newPublicationPermission );
            TransactionItemPopulator.populateDigitalPurchasablePermission( typeOfUseCode, newPurchasablePermission, originalPurchasablePermission );
        }
        else if( originalPurchasablePermission.isRepublication() )
        {
            int licenseeClass = (originalPurchasablePermission.getBusiness().equals(RepublicationConstants.BUSINESS_FOR_PROFIT)) ? 
                LicenseeClass.LIC_CLASS_FOR_PROFIT : LicenseeClass.LIC_CLASS_NOT_FOR_PROFIT;
            newPublicationPermission = getCorrectPublicationPermissionForTypeOfUse(
                originalPurchasablePermission.getWorkInst(), typeOfUseCode, licenseeClass, originalPurchasablePermission.getContentsPublicationDate() );

            if (newPublicationPermission == null) {
                throw new DeniedUnavailableException(originalPurchasablePermission);
            }

            newPurchasablePermission = PurchasablePermissionFactory.createPurchasablePermission( newPublicationPermission );
            TransactionItemPopulator.populateRepublicationPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
        }
        
        //check availability
        if(newPublicationPermission==null)
        	return newPurchasablePermission;
        else if(newPublicationPermission.isSpecialOrder() )
            throw new SpecialOrderUnavailableException( newPurchasablePermission );
        else if( newPublicationPermission.isContactRHDirectly() )
            throw new ContactRHDirectlyUnavailableException( newPurchasablePermission );
        else if( newPublicationPermission.isDenied() )
            throw new DeniedUnavailableException( newPurchasablePermission );
        else
            return newPurchasablePermission;
    }

    private static PublicationPermission getCorrectPublicationPermissionForTypeOfUse( long workInst, int typeOfUseCode, 
                                                                                        int licenseeClass, Date publicationDate)
        throws SpecialOrderUnavailableException, ContactRHDirectlyUnavailableException, DeniedUnavailableException
    {
        WebServiceSearch searchServices = new WebServiceSearch();
        
        //Publication publication = searchServices.getSingleItemByKey( workInst );
        Publication publication = searchServices.getSingleItemByTF(workInst);
        
        if( publication == null ) return null;
        
        int usageDescriptorTypeOfUseCode = getUsageDescriptorForTypeOfUseCode( typeOfUseCode );
        
        PublicationPermission[] publicationPermissions = 
            publication.getPublicationPermissions( usageDescriptorTypeOfUseCode, licenseeClass );
        
        if( publicationPermissions == null ) return null;
            
        for( int i = 0; i < publicationPermissions.length; i++ )
        {
            if ( (publicationPermissions[i].getUsageDescriptor().getTypeOfUse() == usageDescriptorTypeOfUseCode ) &&
                 ((publicationPermissions[i].getLicenseeClass() == licenseeClass) || licenseeClass == LicenseeClass.LIC_CLASS_ALL) &&
                 (publicationPermissions[i].getPubBeginDate().compareTo( publicationDate ) <= 0) &&
                 (publicationPermissions[i].getPubEndDate().compareTo(publicationDate) >= 0))
            {
                return publicationPermissions[i];
            }
        }
        
        return null;
    }
    
    static PurchasablePermission getPurchasablePermissionFromScratchForTypeOfUse(int typeOfUseCode, PurchasablePermission originalPurchasablePermission )
    {
        if( originalPurchasablePermission.isDigital() )
        {
            PurchasablePermission digitalPurchasablePermission = getCorrectDigitalSpecialOrderFromScratch( typeOfUseCode );
            TransactionItemPopulator.populatePurchasablePermissionWork( digitalPurchasablePermission, originalPurchasablePermission );
            TransactionItemPopulator.populateDigitalPurchasablePermission( typeOfUseCode, digitalPurchasablePermission, originalPurchasablePermission );
            return digitalPurchasablePermission;
        }
        else if( originalPurchasablePermission.isRepublication() )
        {
            PurchasablePermission republicationPurchasablePermission = getCorrectRepublicationSpecialOrderFromScratch( typeOfUseCode );
            TransactionItemPopulator.populatePurchasablePermissionWork( republicationPurchasablePermission, originalPurchasablePermission );
            TransactionItemPopulator.populateRepublicationPurchasablePermission( republicationPurchasablePermission, originalPurchasablePermission );
            return republicationPurchasablePermission;
        }
        
        else return originalPurchasablePermission;
    }
    
    static PurchasablePermission getCorrectDigitalSpecialOrderFromScratch( int typeOfUseCode )
    {
        switch( typeOfUseCode )
        {
            case TransactionConstants.TYPE_OF_USE_EMAIL:
                return PurchasablePermissionFactory.createEmailSpecialOrderPurchasablePermission();
            
            case TransactionConstants.TYPE_OF_USE_EXTRANET:
                return PurchasablePermissionFactory.createExtranetSpecialOrderPurchasablePermission();
            
            case TransactionConstants.TYPE_OF_USE_INTRANET:
                return PurchasablePermissionFactory.createIntranetSpecialOrderPurchasablePermission();
            
            case TransactionConstants.TYPE_OF_USE_INTERNET:
                return PurchasablePermissionFactory.createInternetSpecialOrderPurchasablePermission();
            
            default:
                throw new CCCRuntimeException("Transaction Item does not have a valid type of use.");
        }
    }
    
    static PurchasablePermission getCorrectRepublicationSpecialOrderFromScratch( int typeOfUseCode )
    {
        int usageDescriptorTypeOfUseCode = getUsageDescriptorForTypeOfUseCode( typeOfUseCode );
        MapperServiceAPI api = MapperServiceFactory.getInstance().getService();
        long tpuInst = api.getUnderlyingTpuInst( usageDescriptorTypeOfUseCode );
        
        return PurchasablePermissionFactory.createRepublicationSpecialOrderPurchasablePermission( tpuInst );
    }
    
    public static int getRepublicationTypeOfUseTransactionConstants( String republicationConstantsTypeOfUse )
    {
        if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_BROCHURE ) )
            return TransactionConstants.TYPE_OF_USE_BROCHURE;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_CDROM ) )
            return TransactionConstants.TYPE_OF_USE_CDROM;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_DISSERTATION ) )
            return TransactionConstants.TYPE_OF_USE_DISSERTATION;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_DVD ) )
            return TransactionConstants.TYPE_OF_USE_DVD;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_JOURNAL ) )
            return TransactionConstants.TYPE_OF_USE_JOURNAL;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_MAGAZINE ) )
            return TransactionConstants.TYPE_OF_USE_MAGAZINE;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_NEWSLETTER ) )
            return TransactionConstants.TYPE_OF_USE_NEWSLETTER;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_NEWSPAPER ) )
            return TransactionConstants.TYPE_OF_USE_NEWSPAPER;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_OTHERBOOK ) )
            return TransactionConstants.TYPE_OF_USE_OTHERBOOK;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_PAMPHLET ) )
            return TransactionConstants.TYPE_OF_USE_PAMPHLET;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_PRESENTATION ) )
            return TransactionConstants.TYPE_OF_USE_PRESENTATION;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_TEXTBOOK ) )
            return TransactionConstants.TYPE_OF_USE_TEXTBOOK;
        else if( republicationConstantsTypeOfUse.equals( RepublicationConstants.REPUBLICATION_TRADEBOOK ) )
            return TransactionConstants.TYPE_OF_USE_TRADEBOOK;
        else
            return TransactionConstants.TYPE_OF_USE_DEFAULT;
            //throw new CCCRuntimeException("Transaction Item does not have a valid type of use.");
    }
    
    public static String getRepublicationTypeOfUseDescription( int republicationConstantsTypeOfUse )
    {
        if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_BROCHURE) )
            return "Brochure";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_CDROM ) )
            return "CD-ROM";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_DISSERTATION) )
            return "Dissertation";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_DVD ) )
            return "DVD";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_JOURNAL ) )
            return "Journal";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_MAGAZINE ) )
            return "Magazine";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_NEWSLETTER ) )
            return "Newsletter/E-Newsletter";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_NEWSPAPER ) )
            return "Newspaper";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_OTHERBOOK ) )
            return "Other Book";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_PAMPHLET ) )
            return "Pamphlet" ;
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_PRESENTATION ) )
            return "PowerPoint Presentation";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_TEXTBOOK ) )
            return "Textbook";
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_TRADEBOOK ) )
            return "Trade Book" ;
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_EMAIL ) )
            return "E-mail" ;
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_INTRANET ) )
            return "Intranet posting" ;
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_EXTRANET ) )
            return "Extranet posting" ;
        else if( republicationConstantsTypeOfUse == ( TransactionConstants.TYPE_OF_USE_INTERNET ) )
            return "Internet posting" ;
        else
            return "";
            //throw new CCCRuntimeException("Transaction Item does not have a valid type of use.");
    } 
    static int getUsageDescriptorForTypeOfUseCode( int typeOfUseCode )
    {
        switch( typeOfUseCode )
        {
            case TransactionConstants.TYPE_OF_USE_PHOTOCOPY: return UsageDescriptor.NON_ACADEMIC_TRX_PHOTOCOPY;
            case TransactionConstants.TYPE_OF_USE_APS: return UsageDescriptor.ACADEMIC_TRX_PHOTOCOPY;
            case TransactionConstants.TYPE_OF_USE_ECCS: return UsageDescriptor.ACADEMIC_TRX_SCAN;
            case TransactionConstants.TYPE_OF_USE_EMAIL: return UsageDescriptor.NON_ACADEMIC_TRX_EMAIL;
            case TransactionConstants.TYPE_OF_USE_EXTRANET: return UsageDescriptor.NON_ACADEMIC_TRX_EXTRANET;
            case TransactionConstants.TYPE_OF_USE_INTRANET: return UsageDescriptor.NON_ACADEMIC_TRX_INTRANET;
            case TransactionConstants.TYPE_OF_USE_INTERNET: return UsageDescriptor.NON_ACADEMIC_TRX_INTERNET;
            case TransactionConstants.TYPE_OF_USE_BROCHURE: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_BROCHURE;
            case TransactionConstants.TYPE_OF_USE_CDROM: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_CDROM;
            case TransactionConstants.TYPE_OF_USE_DISSERTATION: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DISSERTATION;
            case TransactionConstants.TYPE_OF_USE_DVD: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_DVD;
            case TransactionConstants.TYPE_OF_USE_JOURNAL: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_JOURNAL;
            case TransactionConstants.TYPE_OF_USE_MAGAZINE: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_MAGAZINE;
            case TransactionConstants.TYPE_OF_USE_NEWSLETTER: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSLETTER;
            case TransactionConstants.TYPE_OF_USE_NEWSPAPER: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_NEWSPAPER;
            case TransactionConstants.TYPE_OF_USE_OTHERBOOK: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_OTHERBOOK;
            case TransactionConstants.TYPE_OF_USE_PAMPHLET: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PAMPHLET;
            case TransactionConstants.TYPE_OF_USE_PRESENTATION: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_PRESENTATION;
            case TransactionConstants.TYPE_OF_USE_TEXTBOOK: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TEXTBOOK;
            case TransactionConstants.TYPE_OF_USE_TRADEBOOK: return UsageDescriptor.NON_ACADEMIC_TRX_REPUB_TRADEBOOK;
            default: throw new CCCRuntimeException("Transaction Item does not have a valid type of use.");
        }
    }
    
    public static int getTypeOfUseInstForTransactionConstants( int transactionConstant )
    {
        
    	switch( transactionConstant )
    	{
    		case TransactionConstants.TYPE_OF_USE_EMAIL: return TransactionConstants.TPU_INST_EMAIL;
    		case TransactionConstants.TYPE_OF_USE_INTERNET: return TransactionConstants.TPU_INST_INTERNET;
    		case TransactionConstants.TYPE_OF_USE_INTRANET: return TransactionConstants.TPU_INST_INTRANET;
    		case TransactionConstants.TYPE_OF_USE_EXTRANET: return TransactionConstants.TPU_INST_EXTRANET;
    		case TransactionConstants.TYPE_OF_USE_BROCHURE: return TransactionConstants.TPU_INST_BROCHURE;
    		case TransactionConstants.TYPE_OF_USE_CDROM: return TransactionConstants.TPU_INST_CDROM;
    		case TransactionConstants.TYPE_OF_USE_DISSERTATION: return TransactionConstants.TPU_INST_DISSERTATION;
    		case TransactionConstants.TYPE_OF_USE_DVD: return TransactionConstants.TPU_INST_DVD;
    		case TransactionConstants.TYPE_OF_USE_JOURNAL: return TransactionConstants.TPU_INST_JOURNAL;
    		case TransactionConstants.TYPE_OF_USE_MAGAZINE: return TransactionConstants.TPU_INST_MAGAZINE;
    		case TransactionConstants.TYPE_OF_USE_NEWSLETTER: return TransactionConstants.TPU_INST_NEWSLETTER;
    		case TransactionConstants.TYPE_OF_USE_NEWSPAPER: return TransactionConstants.TPU_INST_NEWSPAPER;
    		case TransactionConstants.TYPE_OF_USE_OTHERBOOK: return TransactionConstants.TPU_INST_OTHERBOOK;
    		case TransactionConstants.TYPE_OF_USE_PAMPHLET: return TransactionConstants.TPU_INST_PAMPHLET;
    		case TransactionConstants.TYPE_OF_USE_PRESENTATION: return TransactionConstants.TPU_INST_PRESENTATION;
    		case TransactionConstants.TYPE_OF_USE_TEXTBOOK: return TransactionConstants.TPU_INST_TEXTBOOK;
    		case TransactionConstants.TYPE_OF_USE_TRADEBOOK: return TransactionConstants.TPU_INST_TRADEBOOK;
    		case TransactionConstants.TYPE_OF_USE_APS: return TransactionConstants.TPU_INST_APS;
    		case TransactionConstants.TYPE_OF_USE_ECCS: return TransactionConstants.TPU_INST_ECCS;
    		case TransactionConstants.TYPE_OF_USE_PHOTOCOPY: return TransactionConstants.TPU_INST_PHOTOCOPY;
    		default: throw new CCCRuntimeException("Transaction Constant does not have a valid type of use inst.");
    	
    	}
    }
}
