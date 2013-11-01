package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.CheckoutServices;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.IncompatibleOrderAndPurchasablePermissionException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.OrderClosedException;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.business.services.cart.SystemLimitsExceededException;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.BasicTransactionForm;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;

public class BasicAddOrderItemAction extends BasicTransactionAction
{
    private static final String CANCEL_PATH = "/search.do?operation=show&page=last";
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
    protected TransactionItem getTransactionItemForTypeOfUseCode( TransactionItem transactionItem, int typeOfUseCode ) 
        throws SpecialOrderUnavailableException, ContactRHDirectlyUnavailableException, DeniedUnavailableException, OutsideBiactiveDateRangeException
    {
        if( !transactionItem.isRepublication() && !transactionItem.isDigital() && !TransactionUtils.validateInBiactiveDateRange( transactionItem ) )
            throw new OutsideBiactiveDateRangeException();
        
        return TransactionItemTypeOfUseMapper.getPurchasablePermissionForTypeOfUse( typeOfUseCode, (PurchasablePermission)transactionItem );
    }
    
    @Override
    protected ActionForward performTransaction( TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
                                                int orderPurchaseID, ActionMapping mapping, HttpServletRequest request,
                                                int scrollPage, String returnTab, boolean searchFlag ) 
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
                ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException, IncompatibleOrderAndPurchasablePermissionException
    {
        PurchasablePermission purchasablePermission = (PurchasablePermission) transactionItem;
         
        ActionForward forward = null;

        /*
         * part of fix for CC-1795 unable to add item to existing TF order
         * 
         * cause: price data wasn't being populated prior to attempting
         * to add new item to the order. This snippet was copied here
         * from BasicAddCartItemAction.
         */
		//Make sure that Item price is populated
		try
		{
			purchasablePermission = PricingServices.updateItemPrice(purchasablePermission);
		}
		catch (SystemLimitsExceededException e)
		{
			saveErrors( request, TransactionUtils.getIncorrectCartItemTypeError() );

			forward = mapping.findForward( SHOW_MAIN );
		}        
		catch (ServiceInvocationException e)
		{
			saveErrors( request, TransactionUtils.getPricingServiceInvocationError() );

			forward = mapping.findForward( SHOW_MAIN );
		}        
        
        try
        {             
            ActionForward originalForward = mapping.findForward( SUBMIT );
            String originalPath = originalForward.getPath();
            
            String pathWithPurchaseID = originalPath + "?" + WebConstants.RequestKeys.PURCHASE_ID + "=" + orderPurchaseID;
            
            forward =  new ActionForward(originalForward.getName(), pathWithPurchaseID, originalForward.getRedirect(), originalForward.getModule());
            
            CheckoutServices.addItemToExistingOrder( purchasablePermission, orderPurchaseID, null );
        }
      
        catch (OrderClosedException e)
        {
          throw new CCRuntimeException(e);
        }
        
          catch (CourseNotDefinedException cnde)
        {
        	request.setAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_TO_ADD, purchasablePermission );
        	request.getSession().setAttribute( WebConstants.SessionKeys.PURCHASE_ID, orderPurchaseID );

			forward = mapping.findForward( COURSE_DETAILS );
        } 

        return forward;
    }


    public ActionForward viewTermsAndConditions(ActionMapping mapping, ActionForm form, 
                                                 HttpServletRequest request, 
                                                 HttpServletResponse response) 
    {
        BasicTransactionForm basicTransactionForm = castForm( BasicTransactionForm.class, form );
        
        TransactionItem transactionItem = basicTransactionForm.getTransactionItem();
        request.setAttribute( WebConstants.RequestKeys.TERMS_ITEM, transactionItem );
        
        return mapping.findForward( VIEW_TERMS );
    }

    @Override
    protected boolean isFirstAcademicItem(TransactionItem transactionItem)
    {
        return false;
    }
}
