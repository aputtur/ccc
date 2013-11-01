package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.cart.CannotBeAddedToCartException;
import com.copyright.ccc.business.services.cart.CannotBeRemovedFromCartException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.business.services.cart.SystemLimitsExceededException;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;

public class BasicEditCartItemAction extends BasicTransactionAction
{
    private static final String CANCEL_PATH = "/cancelEdit.do";
    
    @Override
    protected String getCancelPath(TransactionItem transactionItem, 
                                   int orderPurchaseId, int scrollPage,
                                   String tabPage, boolean searchFlag)
    {
        return CANCEL_PATH;
    }
     
    @Override
    protected TransactionItem getTransactionItemForTypeOfUseCode( TransactionItem transactionItem, int typeOfUseCode ) 
        throws SpecialOrderUnavailableException, ContactRHDirectlyUnavailableException, DeniedUnavailableException, OutsideBiactiveDateRangeException
    {
        if( !TransactionUtils.validateInBiactiveDateRange( transactionItem ) )
            throw new OutsideBiactiveDateRangeException();
            
        return TransactionItemTypeOfUseMapper.getPurchasablePermissionForTypeOfUse( typeOfUseCode, (PurchasablePermission)transactionItem );
    }
    
    @Override
    protected ActionForward performTransaction( TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
                                                int orderPurchaseID, ActionMapping mapping, HttpServletRequest request,
                                                int scrollPage, String returnPage, boolean searchFlag ) 
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
                ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException, ItemCannotBePurchasedException
    {
        PurchasablePermission purchasablePermission = (PurchasablePermission)transactionItem;
        
        boolean shouldRemoveAndAdd = purchasablePermission.isDigital() || purchasablePermission.isRepublication() ||
            transactionItem.getRgtInst() != purchasablePermissionInCart.getRgtInst();
        
        if( shouldRemoveAndAdd )
        {
            try
            {
                CartServices.removeItemFromCart( purchasablePermissionInCart );
                
                try
                {
                	purchasablePermission = PricingServices.updateItemPrice(purchasablePermission);
                }
                catch (SystemLimitsExceededException e)
                {
                    //To do
                }
                catch (ServiceInvocationException e)
                {
                    //To do
                }                
                CartServices.addItemToCart( purchasablePermission );
            } 
            catch (CannotBeRemovedFromCartException e)
            {
                 // TODO: gcuevas 11/17/06: figure out what do to here
            } 
            catch (CannotBeAddedToCartException e)
            {
                 // TODO: gcuevas 11/17/06: figure out what do to here
            } 
            catch (CourseNotDefinedException e)
            {
                 // TODO: gcuevas 11/17/06: figure out what do to here
            }
           
        }
        else
        {
        	//Make sure that Item price is populated
            try
            {
            	purchasablePermission = PricingServices.updateItemPrice(purchasablePermission);
            }
            catch (SystemLimitsExceededException e)
            {
                //To do
            }
            catch (ServiceInvocationException e)
            {
                //To do
            }           
            CartServices.updateCartItem( purchasablePermission );
            UserContextService.setCartItemEdited(true);
        }
        
        return mapping.findForward( SUBMIT );
    }

    @Override
    protected boolean isFirstAcademicItem(TransactionItem transactionItem)
    {
        return false;
    }
}
