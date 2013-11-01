package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.data.WRStandardWork;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.cart.CannotBeAddedToCartException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.CourseDetailsImpl;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.transaction.ChangedToRegularFromSpecialUnavailableException;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.coi.TransactionItemPopulator;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;

public class SpecialAddCartItemAction extends SpecialOrderAction
{
    private static final String COURSE_DETAILS = "courseDetails";
    protected final static String SPECIAL_ORDER = "specialOrder";
    
    private static final String CANCEL_PATH = "/search.do?operation=show&page=last";
    private static final String SELECT_PERMISSION_PATH = "/specialPermissionTypeAddCoiCart.do?operation=defaultOperation";

    
    @Override
    protected String getCancelPath(TransactionItem transactionItem, 
                                   int orderPurchaseId,
                                   int scrollPage,
                                   String returnTab,
                                   boolean searchFlag)
    {
        return CANCEL_PATH;
    }
    
    @Override
    protected String getSelectPermissionPath()
    {
        return SELECT_PERMISSION_PATH;
    }
    
    @Override
    protected TransactionItem getTransactionItemForTypeOfUseCode( TransactionItem transactionItem, int typeOfUseCode )
        throws ContactRHDirectlyUnavailableException, DeniedUnavailableException, 
                ChangedToRegularFromSpecialUnavailableException, OutsideBiactiveDateRangeException
    {
        if( !transactionItem.isRepublication() && !transactionItem.isDigital() && !TransactionUtils.validateInBiactiveDateRange( transactionItem ) )
            throw new OutsideBiactiveDateRangeException();
            
        PurchasablePermission purchasablePermission = (PurchasablePermission)transactionItem;
        
        try
        {
            purchasablePermission =
                TransactionItemTypeOfUseMapper.getPurchasablePermissionForTypeOfUse(typeOfUseCode, purchasablePermission );
            
            //if item is not a special order, then throw changed to regular exception
            if( !purchasablePermission.isSpecialOrder() )
                throw new ChangedToRegularFromSpecialUnavailableException( purchasablePermission );
        } 
        catch (SpecialOrderUnavailableException e)
        {
            purchasablePermission = (PurchasablePermission)e.getTransactionItem();
        }
        catch (DeniedUnavailableException de)
        {
        	purchasablePermission = (PurchasablePermission)de.getTransactionItem();
        	
        	if( purchasablePermission.isDigital() )
            {
        		TransactionItemPopulator.populateDigitalPurchasablePermission( typeOfUseCode, purchasablePermission, purchasablePermission );
            }
        	else if( purchasablePermission.isRepublication() )
            {
        		TransactionItemPopulator.populateRepublicationPurchasablePermission( purchasablePermission, purchasablePermission );
            }
        	
        }
        

        return purchasablePermission;
    }

    @Override
    protected ActionForward performTransaction( TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
                                                int orderPurchaseId, ActionMapping mapping, HttpServletRequest request,
                                                int scrollPage, String returnTab, boolean searchFlag ) 
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
                ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException, ItemCannotBePurchasedException
    {
        PurchasablePermission purchasablePermission = (PurchasablePermission) transactionItem;
        
        ActionForward forward = null;
        
       // Address situation where an RRO work that resolves to special order needs to be added to TF
        if (purchasablePermission.getTfWksInst() != null && purchasablePermission.getTfWksInst().longValue() > 0 && 
			(purchasablePermission.getWorkInst() == null || purchasablePermission.getWorkInst().longValue()==0)){
			WRStandardWork wrStandardWork = PricingServices.pushWorkToTf(purchasablePermission);
			if (wrStandardWork.getWrkInst() > 0 ) {
				// All set work has been provisioned in TF
				purchasablePermission.setExternalItemId(new Long(wrStandardWork.getWrkInst()));
			} else {
				//the pushing the work to TF failed, set the rights to NULL to force a manual special order
				// When pricing is called below it will resolve item to manual special order
				_logger.error("tf push failed for WR WRK INST: " + wrStandardWork.getWrWrkInst()+ " Null return from call.");
				saveErrors( request, TransactionUtils.getPushToTFFailedError());
				request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM,transactionItem );
//				purchasablePermission.getPermissionRequest().setManualSpecialOrder(true);
				purchasablePermission.setPushToTFFailed(true);
				purchasablePermission.setExternalItemId(null);
//				return mapping.findForward(SPECIAL_ORDER);				
			}
        }
        
      //If CourseDetail is already created, need to transfer the BundleId into the purchasablePermission
        if (purchasablePermission.isAcademic() && !CartServices.isCoursePackNotPresentInCart())
        {
        	CourseDetailsImpl courseDetailsImpl = (CourseDetailsImpl) CartServices.getCourseDetails();
        	//Set the Bundle Id from the CourseDetails on the item before adding to cart
            purchasablePermission.getItem().setBundleId(courseDetailsImpl.getBundle().getBundleId());
        }
   
        //Make sure that Item price is populated
        try {
        	if (UserContextService.getActiveAppUser() != null && UserContextService.getActiveAppUser().getPartyID() > 0 ){
        		purchasablePermission = PricingServices.updateItemPriceForLicenseeID(purchasablePermission, new Long(UserContextService.getActiveAppUser().getPartyID()));
        	} else {
        		purchasablePermission = PricingServices.updateItemPriceForLicenseeID(purchasablePermission, null);
        	}
        } catch (ServiceInvocationException e)
        {
            saveErrors( request, TransactionUtils.getPricingServiceInvocationError() );
            
            forward = mapping.findForward( SHOW_MAIN );
        } 



      /*  try
        {
        	purchasablePermission = PricingServices.updateItemPrice(purchasablePermission);
        }
        catch (SystemLimitsExceededException e)
        {
            saveErrors( request, TransactionUtils.getIncorrectCartItemTypeError() );
            
            forward = mapping.findForward( SHOW_MAIN );
        } */
        
        try
        {
            CartServices.addItemToCart( purchasablePermission );
            
            forward = mapping.findForward( SUBMIT );
        } 
        catch (CannotBeAddedToCartException e)
        {
             saveErrors( request, TransactionUtils.getIncorrectCartItemTypeError() );
             
             forward = mapping.findForward( SHOW_MAIN );
        } 
        catch (CourseNotDefinedException e)
        {
            request.setAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_TO_ADD, purchasablePermission );
            
            forward = mapping.findForward( COURSE_DETAILS );
        }
        
        return forward;

    }

    @Override
    protected boolean isSpecialFirstAcademicItem(TransactionItem transactionItem)
    {
        //return transactionItem.isAcademic() && CartServices.getCourseDetails() == null;
    	return transactionItem.isAcademic() && CartServices.getNumberOfAcademicOrderItemsInCart() == 0;
    }
}
