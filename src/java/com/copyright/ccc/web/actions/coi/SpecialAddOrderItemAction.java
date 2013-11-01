package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.CheckoutException;
import com.copyright.ccc.business.services.cart.CheckoutServices;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.IncompatibleOrderAndPurchasablePermissionException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.OrderClosedException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.SpecialOrderForm;
import com.copyright.ccc.web.transaction.ChangedToRegularFromSpecialUnavailableException;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;

public class SpecialAddOrderItemAction extends SpecialOrderAction
{
    private static final String CANCEL_PATH = "/search.do?operation=show&page=last";
    private static final String SELECT_PERMISSION_PATH = "/specialCoiPermissionTypeAddOrder.do?operation=defaultOperation";
    private static final String VIEW_TERMS = "viewTerms";
    private static final String COURSE_DETAILS = "courseDetails";
    
    @Override
    protected String getCancelPath(TransactionItem transactionItem, 
                                   int orderPurchaseId,
                                   int scrollPage,
                                   String returnTab,
                                   boolean searchFlag)
    {
        return CANCEL_PATH + "&" + WebConstants.RequestKeys.PURCHASE_ID + "=" + orderPurchaseId;
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
                TransactionItemTypeOfUseMapper.getPurchasablePermissionForTypeOfUse( typeOfUseCode, purchasablePermission );
            
             //if item is not a special order, then throw changed to regular exception
            if( !purchasablePermission.isSpecialOrder() )
                throw new ChangedToRegularFromSpecialUnavailableException( purchasablePermission );
        } 
        catch (SpecialOrderUnavailableException e)
        {
        	_logger.warn(LogUtil.getStack(e));
            purchasablePermission = (PurchasablePermission)e.getTransactionItem();
        } 

        return purchasablePermission;
    }
    
    @Override
    protected ActionForward performTransaction( TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
                                                int orderPurchaseId, ActionMapping mapping, HttpServletRequest request, int scrollPage,
                                                String returnPage, boolean searchFlag ) 
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
                ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException, IncompatibleOrderAndPurchasablePermissionException
    {
        PurchasablePermission purchasablePermission = (PurchasablePermission) transactionItem;
        
        ActionForward forward = null;
        
        try
        {
            CheckoutServices.addItemToExistingOrder( purchasablePermission, orderPurchaseId, null );
            
            ActionForward originalForward = mapping.findForward( SUBMIT );
            String originalPath = originalForward.getPath();
            
            String pathWithPurchaseID = originalPath + "?" + WebConstants.RequestKeys.PURCHASE_ID + "=" + orderPurchaseId;
            
            forward =  new ActionForward(originalForward.getName(), pathWithPurchaseID, originalForward.getRedirect(), originalForward.getModule());
        }
        catch (CourseNotDefinedException cnde)
        {
        	_logger.info(LogUtil.getStack(cnde));
        	request.setAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_TO_ADD, purchasablePermission );
        	
        	request.getSession().setAttribute( WebConstants.SessionKeys.PURCHASE_ID, orderPurchaseId );

			forward = mapping.findForward( COURSE_DETAILS );
        } 
        catch(CheckoutException ce)
        {
            //TODO: gcuevas 12/15/06: catch more specific exceptions when ready and figure out what to do
        	_logger.error(LogUtil.getStack(ce));        	
            forward = mapping.findForward( SHOW_MAIN );
        } catch (OrderClosedException e)
        {
        	_logger.error(LogUtil.getStack(e));        	
        }
        
        return forward;
        
    }
    
    public ActionForward viewTermsAndConditions(ActionMapping mapping, ActionForm form, 
                                                 HttpServletRequest request, 
                                                 HttpServletResponse response) 
    {
        SpecialOrderForm specialOrderForm = castForm( SpecialOrderForm.class, form );
        
        TransactionItem transactionItem = specialOrderForm.getSpecialOrderItem();
        request.setAttribute( WebConstants.RequestKeys.TERMS_ITEM, transactionItem );
        
        return mapping.findForward( VIEW_TERMS );
    }

    @Override
    protected boolean isSpecialFirstAcademicItem(TransactionItem transactionItem)
    {
        return false;
    }
}
