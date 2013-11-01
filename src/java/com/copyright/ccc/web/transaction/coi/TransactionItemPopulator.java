package com.copyright.ccc.web.transaction.coi;

import com.copyright.base.CCCRuntimeException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.transaction.TransactionConstants;

public class TransactionItemPopulator
{
    static void populatePurchasablePermissionWork( PurchasablePermission newPurchasablePermission, 
        PurchasablePermission originalPurchasablePermission)
    {
        if (originalPurchasablePermission.getExternalItemId() != null && originalPurchasablePermission.getExternalItemId() != 0) {
            newPurchasablePermission.setExternalItemId((originalPurchasablePermission.getExternalItemId()));
        }
        newPurchasablePermission.setPublicationTitle( originalPurchasablePermission.getPublicationTitle() );
        newPurchasablePermission.setStandardNumber( originalPurchasablePermission.getStandardNumber() );
        newPurchasablePermission.setPublicationYearOfUse( originalPurchasablePermission.getPublicationYearOfUse() );
        newPurchasablePermission.setPublisher( originalPurchasablePermission.getPublisher() );
        if( newPurchasablePermission.isRepublication() )
        {
            newPurchasablePermission.setCustomVolume( originalPurchasablePermission.getCustomVolume() );
            newPurchasablePermission.setEdition( originalPurchasablePermission.getEdition() );
        }
    }
    
    public static void populateDigitalPurchasablePermission( int typeOfUseCode, PurchasablePermission newPurchasablePermission, 
                                                                PurchasablePermission originalPurchasablePermission )
    {
        switch( typeOfUseCode )
        {
            case TransactionConstants.TYPE_OF_USE_EMAIL:
                populateEmailPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
                break;
            
            case TransactionConstants.TYPE_OF_USE_EXTRANET:
                populateNetPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
                break;
            
            case TransactionConstants.TYPE_OF_USE_INTRANET:
                populateNetPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
                break;
            
            case TransactionConstants.TYPE_OF_USE_INTERNET:
                populateInternetPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
                break;
            
            default:
                throw new CCCRuntimeException("Transaction Item does not have a valid type of use.");
        }
    }
    
    static void populatePurchasablePermission( PurchasablePermission newPurchasablePermission, 
                                                    PurchasablePermission originalPurchasablePermission)
    {
        if( newPurchasablePermission.isAcademic() )
            populateAcademicPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
        else if( newPurchasablePermission.isPhotocopy() )
            populatePhotocopyPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
        else if( newPurchasablePermission.isEmail() )
            populateEmailPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
        else if( newPurchasablePermission.isIntranet() || newPurchasablePermission.isExtranet() )
            populateNetPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
        else if( newPurchasablePermission.isInternet() )
            populateInternetPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
        else if( newPurchasablePermission.isRepublication() )
            populateRepublicationPurchasablePermission( newPurchasablePermission, originalPurchasablePermission );
    }
    
    static void populateAcademicPurchasablePermission( PurchasablePermission newPurchasablePermission, 
                                                            PurchasablePermission originalPurchasablePermission )
    {
        newPurchasablePermission.setPublicationYearOfUse( originalPurchasablePermission.getPublicationYearOfUse() );
        newPurchasablePermission.setNumberOfPages( originalPurchasablePermission.getNumberOfPages() );
        newPurchasablePermission.setNumberOfStudents( originalPurchasablePermission.getNumberOfStudents() );
        newPurchasablePermission.setChapterArticle( originalPurchasablePermission.getChapterArticle() );
        newPurchasablePermission.setPageRange( originalPurchasablePermission.getPageRange() );
        newPurchasablePermission.setCustomAuthor( originalPurchasablePermission.getCustomAuthor() );
        newPurchasablePermission.setDateOfIssue( originalPurchasablePermission.getDateOfIssue() );
        newPurchasablePermission.setCustomVolume( originalPurchasablePermission.getCustomVolume() );
        newPurchasablePermission.setCustomEdition( originalPurchasablePermission.getCustomEdition() );
        newPurchasablePermission.setCustomerReference( originalPurchasablePermission.getCustomerReference() );
    }
    
    static void populatePhotocopyPurchasablePermission( PurchasablePermission newPurchasablePermission, 
                                                            PurchasablePermission originalPurchasablePermission )
    {
        newPurchasablePermission.setPublicationYearOfUse( originalPurchasablePermission.getPublicationYearOfUse() );
        newPurchasablePermission.setNumberOfCopies( originalPurchasablePermission.getNumberOfCopies() );
        newPurchasablePermission.setNumberOfPages( originalPurchasablePermission.getNumberOfPages() );
        newPurchasablePermission.setChapterArticle( originalPurchasablePermission.getChapterArticle() );
        newPurchasablePermission.setCustomerReference( originalPurchasablePermission.getCustomerReference() );
    }
    
    static void populateEmailPurchasablePermission( PurchasablePermission newPurchasablePermission, 
                                                                PurchasablePermission originalPurchasablePermission )
    {
        newPurchasablePermission.setPublicationYearOfUse( originalPurchasablePermission.getPublicationYearOfUse() );
        newPurchasablePermission.setDateOfUse( originalPurchasablePermission.getDateOfUse() );
        newPurchasablePermission.setNumberOfRecipients( originalPurchasablePermission.getNumberOfRecipients() );
        newPurchasablePermission.setCustomAuthor( originalPurchasablePermission.getCustomAuthor() );
        newPurchasablePermission.setChapterArticle( originalPurchasablePermission.getChapterArticle() );
        newPurchasablePermission.setCustomerReference( originalPurchasablePermission.getCustomerReference() );
        newPurchasablePermission.setDuration(originalPurchasablePermission.getDuration());
        newPurchasablePermission.setCategoryName(originalPurchasablePermission.getCategoryName());
        newPurchasablePermission.setTouName(originalPurchasablePermission.getTouName());
    }
    
    static void populateNetPurchasablePermission( PurchasablePermission newPurchasablePermission,
                                                            PurchasablePermission originalPurchasablePermission )
    {
        newPurchasablePermission.setPublicationYearOfUse( originalPurchasablePermission.getPublicationYearOfUse() );
        newPurchasablePermission.setDateOfUse( originalPurchasablePermission.getDateOfUse() );
        newPurchasablePermission.setDuration( originalPurchasablePermission.getDuration() );
        newPurchasablePermission.setCustomAuthor( originalPurchasablePermission.getCustomAuthor() );
        newPurchasablePermission.setChapterArticle( originalPurchasablePermission.getChapterArticle() );
        newPurchasablePermission.setCustomerReference( originalPurchasablePermission.getCustomerReference() );
        newPurchasablePermission.setCategoryName(originalPurchasablePermission.getCategoryName());
        newPurchasablePermission.setTouName(originalPurchasablePermission.getTouName());
    }
    
    static void populateInternetPurchasablePermission( PurchasablePermission newPurchasablePermission,
                                                                PurchasablePermission originalPurchasablePermission )
    {
        newPurchasablePermission.setPublicationYearOfUse( originalPurchasablePermission.getPublicationYearOfUse() );
        newPurchasablePermission.setDateOfUse( originalPurchasablePermission.getDateOfUse());
        newPurchasablePermission.setDuration( originalPurchasablePermission.getDuration());
        newPurchasablePermission.setWebAddress( originalPurchasablePermission.getWebAddress());
        newPurchasablePermission.setCustomAuthor( originalPurchasablePermission.getCustomAuthor());
        newPurchasablePermission.setChapterArticle( originalPurchasablePermission.getChapterArticle());
        newPurchasablePermission.setCustomerReference( originalPurchasablePermission.getCustomerReference());
        newPurchasablePermission.setCategoryName(originalPurchasablePermission.getCategoryName());
        newPurchasablePermission.setTouName(originalPurchasablePermission.getTouName());
    }
    
    public static void populateRepublicationPurchasablePermission( PurchasablePermission newPurchasablePermission, 
                                                                    PurchasablePermission originalPurchasablePermission )
    {
        newPurchasablePermission.setCirculationDistribution( originalPurchasablePermission.getCirculationDistribution() );
        newPurchasablePermission.setBusiness( originalPurchasablePermission.getBusiness() );
        newPurchasablePermission.setTypeOfContent( originalPurchasablePermission.getTypeOfContent() );
        newPurchasablePermission.setSubmitterAuthor( originalPurchasablePermission.isSubmitterAuthor() );
        newPurchasablePermission.setContentsPublicationDate( originalPurchasablePermission.getContentsPublicationDate() );
        newPurchasablePermission.setNumberOfPages( originalPurchasablePermission.getNumberOfPages() );
        newPurchasablePermission.setRepublishingOrganization( originalPurchasablePermission.getRepublishingOrganization() );
        newPurchasablePermission.setNewPublicationTitle( originalPurchasablePermission.getNewPublicationTitle() );
        newPurchasablePermission.setRepublicationDate( originalPurchasablePermission.getRepublicationDate() );
        newPurchasablePermission.setTranslationLanguage( originalPurchasablePermission.getTranslationLanguage() );
        newPurchasablePermission.setYourReference( originalPurchasablePermission.getYourReference() );
        newPurchasablePermission.setChapterArticle( originalPurchasablePermission.getChapterArticle() );
        newPurchasablePermission.setCustomAuthor( originalPurchasablePermission.getCustomAuthor() );
        newPurchasablePermission.setPageRange( originalPurchasablePermission.getPageRange() );
        newPurchasablePermission.setCategoryName(originalPurchasablePermission.getCategoryName());
        newPurchasablePermission.setTouName(originalPurchasablePermission.getTouName());
    }
}
