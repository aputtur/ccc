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
import com.copyright.ccc.web.transaction.ChangedToRegularFromSpecialUnavailableException;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;

public class SpecialEditOrderItemAction extends SpecialOrderAction
{
    private static final String CANCEL_PATH = "/orderView.do";
    
    @Override
    protected String getCancelPath(TransactionItem transactionItem, 
                                   int orderPurchaseId,
                                   int scrollPage,
                                   String returnTab,
                                   boolean searchFlag)
    {
        OrderLicense orderLicense = (OrderLicense) transactionItem;

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
    protected String getSelectPermissionPath()
    {
        return "";
    }
    
    @Override
    protected TransactionItem getTransactionItemForTypeOfUseCode( TransactionItem transactionItem, int typeOfUseCode )
        throws ContactRHDirectlyUnavailableException, DeniedUnavailableException, 
                ChangedToRegularFromSpecialUnavailableException, OutsideBiactiveDateRangeException
    {
        if( !TransactionUtils.validateInBiactiveDateRange( transactionItem ) )
            throw new OutsideBiactiveDateRangeException();
            
        return transactionItem;
    }
    
    @Override
    protected ActionForward performTransaction( TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
                                                int orderPurchaseId, ActionMapping mapping, HttpServletRequest request, 
                                                int scrollPage, String returnTab, boolean searchFlag ) 
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
                ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException
    {
    
        OrderLicense orderLicense = (OrderLicense) transactionItem;
          
        try
        {
            OrderLicenseServices.updateLicenseAndPricing(orderLicense);
        }
        catch( Exception e )
        {
            _logger.error( LogUtil.getStack(e) );
        }
     
        return constructForwardWithQueryString( mapping.findForward( SUBMIT ), orderLicense, scrollPage, returnTab, searchFlag  );
    }
 
    private ActionForward constructForwardWithQueryString(ActionForward originalForward, OrderLicense orderLicense, int scrollPage, String returnTab, boolean searchFlag)
    {
        String originalPath = originalForward.getPath();
        if (scrollPage < 1) { scrollPage = 1; }
        String cpParm = "&cp=" + scrollPage;
        
        String rpParm = "&rp=";
        if (returnTab != null && !"".equals(returnTab)) {
           rpParm = rpParm + returnTab;
        } else {
            rpParm = "";
        }
        
        String sfParm = "";
        if (searchFlag == true) { sfParm = "&sf=true"; }
        
        String pathWithPurchaseID = originalPath + "?" + WebConstants.RequestKeys.PURCHASE_ID + "=" + orderLicense.getPurchaseId() + cpParm + rpParm + sfParm;
        
        return new ActionForward(originalForward.getName(), 
            pathWithPurchaseID,
            originalForward.getRedirect(),
            originalForward.getModule());
    }

    @Override
    protected boolean isSpecialFirstAcademicItem(TransactionItem transactionItem)
    {
        return false;
    }
}
