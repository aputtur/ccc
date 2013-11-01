package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;

public class BasicEditOrderItemAction extends BasicTransactionAction
{
    private static final String CANCEL_PATH = "/orderView.do";
    
    @Override
    protected String getCancelPath(TransactionItem transactionItem, 
                                   int orderPurchaseId,
                                   int scrollPage,
                                   String returnTab,
                                   boolean searchFlag)
    {
        OrderLicense orderLicense = (OrderLicense)transactionItem;
        
        String rpParm;
        if (returnTab != null && !"".equals(returnTab)) {
           rpParm = "&rp=" + returnTab;
        } else { 
           rpParm = "&rp=main"; // default to page one 
        }

        String cpParm;
        if (scrollPage > 0) {
           cpParm = "&cp=" + scrollPage;
        } else { 
           cpParm = "&cp=1"; // default to main OH tab 
        }
         
        String sfParm = "";
        if (searchFlag) { sfParm = "&sf=true"; }
        
        return CANCEL_PATH + "?" + WebConstants.RequestKeys.PURCHASE_ID + "=" + orderLicense.getPurchaseId() + rpParm + cpParm + sfParm;
    }
    
    @Override
    protected TransactionItem getTransactionItemForTypeOfUseCode( TransactionItem transactionItem, int typeOfUseCode ) 
        throws SpecialOrderUnavailableException, ContactRHDirectlyUnavailableException, DeniedUnavailableException, OutsideBiactiveDateRangeException
    {
        if( !TransactionUtils.validateInBiactiveDateRange( transactionItem ) )
            throw new OutsideBiactiveDateRangeException();
         
        return transactionItem;
    }
    
    @Override
    protected ActionForward performTransaction( TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
                                                int orderPurchaseID, ActionMapping mapping, HttpServletRequest request, int cp, String rp, boolean searchFlag ) 
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
                ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException
    {
    
        OrderLicense orderLicense = (OrderLicense) transactionItem;

        ActionForward forward = null;

        try
        {
            OrderLicenseServices.updateLicenseAndPricing( orderLicense );

            String rpParm;
            if (rp != null && !"".equals(rp)) {
               rpParm = "&rp=" + rp;
            } else { 
               rpParm = "&rp=main"; // default to page one 
            }

            String cpParm;
            if (cp > 0) {
               cpParm = "&cp=" + cp;
            } else { 
               cpParm = "&cp=1"; // default to main OH tab 
            }

            String sfParm = "";
            if (searchFlag) { sfParm = "&sf=true";  }

            forward = constructForwardWithQueryString( mapping.findForward( SUBMIT ), orderLicense, rpParm, cpParm, sfParm );
        }
        catch( Exception e )
        {
            _logger.error( LogUtil.getStack(e)); 
        }
     
        return forward;
    }

    private ActionForward constructForwardWithQueryString(ActionForward originalForward, OrderLicense orderLicense, String rp, String cp, String sf)
    {
        String originalPath = originalForward.getPath();
        String pathWithPurchaseID = originalPath + "?" + WebConstants.RequestKeys.PURCHASE_ID + "=" + orderLicense.getPurchaseId() + rp + cp + sf;
        
        return new ActionForward(originalForward.getName(), 
            pathWithPurchaseID,
            originalForward.getRedirect(),
            originalForward.getModule());
    }

    @Override
    protected boolean isFirstAcademicItem(TransactionItem transactionItem)
    {
        return false;
    }
}
