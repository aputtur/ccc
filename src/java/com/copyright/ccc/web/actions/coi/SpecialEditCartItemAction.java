package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.cart.CannotBeAddedToCartException;
import com.copyright.ccc.business.services.cart.CannotBeRemovedFromCartException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.web.transaction.ChangedToRegularFromSpecialUnavailableException;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;

public class SpecialEditCartItemAction extends SpecialOrderAction
{
    private static final String CANCEL_PATH = "/cart.do";
    private static final String SELECT_PERMISSION_PATH = "/specialPermissionTypeEditCoiCart.do?operation=defaultOperation";
    
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
        if( !TransactionUtils.validateInBiactiveDateRange( transactionItem ) )
            throw new OutsideBiactiveDateRangeException();
         
        PurchasablePermission purchasablePermission = (PurchasablePermission)transactionItem;
        
        try
        {
            purchasablePermission =
                TransactionItemTypeOfUseMapper.getPurchasablePermissionForTypeOfUse( typeOfUseCode, purchasablePermission );
            
            if( !purchasablePermission.isSpecialOrder() ) 
                throw new ChangedToRegularFromSpecialUnavailableException( purchasablePermission );
        } 
        catch (SpecialOrderUnavailableException e)
        {
            purchasablePermission = (PurchasablePermission)e.getTransactionItem();
        } 

        return purchasablePermission;
    }
    
    @Override
    protected ActionForward performTransaction( TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
                                                int orderPurchaseId, ActionMapping mapping, HttpServletRequest request, int scrollPage,
                                                String returnPage, boolean searchFlag ) 
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
                ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException, ItemCannotBePurchasedException
    {
        PurchasablePermission purchasablePermission = (PurchasablePermission) transactionItem;
        
        if( purchasablePermission.isDigital() || purchasablePermission.isRepublication() )
        {
            try
            {
                CartServices.removeItemFromCart( purchasablePermissionInCart );
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
            CartServices.updateCartItem( purchasablePermission );
            UserContextService.setCartItemEdited(true);
        }
        
        return mapping.findForward( SUBMIT );
    }

    @Override
    protected boolean isSpecialFirstAcademicItem(TransactionItem transactionItem)
    {
        return false;
    }
}
