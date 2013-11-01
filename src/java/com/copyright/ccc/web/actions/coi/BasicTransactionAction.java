package com.copyright.ccc.web.actions.coi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.CCUser;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderPurchases;
import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.IncompatibleOrderAndPurchasablePermissionException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.business.services.cart.SystemLimitsExceededException;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.business.services.user.RLinkPublisherServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.forms.coi.BasicTransactionForm;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.PricingError;
import com.copyright.ccc.web.transaction.PricingErrorFactory;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.transaction.coi.TransactionAJAXUtils;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;
import com.copyright.ccc.web.util.WebUtils;

public abstract class BasicTransactionAction extends CCAction
{
    //ActionForward names
    protected final static String SUBMIT = "submit";
    protected final static String SPECIAL_ORDER = "specialOrder";
    protected final static String RIGHTSLINK = "_rightslink";
    
    
    protected static final Logger _logger = Logger.getLogger( BasicTransactionAction.class );

    //abstract methods

    protected abstract ActionForward performTransaction(TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
        int orderPurchaseID, ActionMapping mapping, HttpServletRequest request, int scrollPage, String returnTab, boolean searchFlag )
        throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
                ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException, IncompatibleOrderAndPurchasablePermissionException,
                ItemCannotBePurchasedException;

    protected abstract TransactionItem getTransactionItemForTypeOfUseCode(TransactionItem transactionItem, int typeOfUseCode)
        throws DeniedUnavailableException, SpecialOrderUnavailableException, ContactRHDirectlyUnavailableException, OutsideBiactiveDateRangeException;

    protected abstract String getCancelPath( TransactionItem transactionItem, int orderPurchaseId, int scrollPage, String tabPage, boolean searchFlag );

    protected abstract boolean isFirstAcademicItem( TransactionItem transactionItem );

    //public methods

    public ActionForward defaultOperation( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
    {
        BasicTransactionForm basicTransactionFormCOI = castForm( BasicTransactionForm.class, form );

         basicTransactionFormCOI.setFormPath( mapping.getPath() );

        boolean fromSearch;
        
        if( request.getAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM) != null) {
            populateFormFromNonSearch( request, basicTransactionFormCOI );
            fromSearch = false;
            Boolean isPD = (Boolean)request.getAttribute(WebConstants.RequestKeys.PERMISSIONS_DIRECT_TRANSACTION_ITEM);
            if ( isPD != null ) {
            	fromSearch = true;
            	request.removeAttribute(WebConstants.RequestKeys.PERMISSIONS_DIRECT_TRANSACTION_ITEM);
            }
     	} else {
            populateFormFromSearch( request, basicTransactionFormCOI );
        	fromSearch = true;
    	}

        TransactionItem transactionItem = basicTransactionFormCOI.getTransactionItem();
        
        //  Prepopulate the form with the skipQuickprice value.

        CCUser ccUser = UserContextService.getActiveAppUser();
        //basicTransactionFormCOI.setSkipQuickprice(ccUser.skipQuickprice());
        //Look for the Skip Quick Price flag for Academic (APS/ECCS) and Photocopy (TRS) types
        if (transactionItem.isAcademic() || transactionItem.isPhotocopy())
        {
        	if (request.getSession().getAttribute(WebConstants.SessionKeys.SKIP_QUICKPRICE) != null)
        	{
        		boolean flag = (request.getSession().getAttribute(WebConstants.SessionKeys.SKIP_QUICKPRICE).toString().equalsIgnoreCase("true")) ? true : false;
        		basicTransactionFormCOI.setSkipQuickprice(flag);
        		basicTransactionFormCOI.setExpanded(flag);
        	}
        	else
        	{
        		basicTransactionFormCOI.setSkipQuickprice(false);
        		basicTransactionFormCOI.setExpanded(false);
        	}
        }
        else
        {
        	basicTransactionFormCOI.setSkipQuickprice(false);
        	basicTransactionFormCOI.setExpanded(false);
        }
        
     //   boolean photoCopy = transactionItem.isPhotocopy();

        //Added this flag for the check box
        if (transactionItem.getWorkInst() != null && (transactionItem.getWorkInst().longValue() == 4195313 || transactionItem.getWorkInst().longValue() == 3182308 ))
        {
            basicTransactionFormCOI.setUseOnlyInNA(true);
        }
        else
        {
            basicTransactionFormCOI.setUseOnlyInNA(false);
        }

        basicTransactionFormCOI.setCancelPath( this.getCancelPath( transactionItem, basicTransactionFormCOI.getOrderPurchaseID(), basicTransactionFormCOI.getCp(), basicTransactionFormCOI.getRp(), basicTransactionFormCOI.isSf() ));
        basicTransactionFormCOI.setFirstAcademicItem( this.isFirstAcademicItem( transactionItem ) );
        
        if (fromSearch) {
        	SearchForm searchForm = (SearchForm) request.getSession().getAttribute( WebConstants.SessionKeys.SEARCH_FORM );
        	transactionItem.setCategoryName( searchForm.getSelectedPermCatDisplay().getCategoryDescription());
        	transactionItem.setTouName(UserContextService.getSearchState().getSelectedTou());
        	
        	if (searchForm.getIsBiactive())
        	{
        		transactionItem.setPublicationYearOfUse(searchForm.getSelectedPubYear());
        		basicTransactionFormCOI.setBiactive(true);
        	}
        	else
        	{
        		basicTransactionFormCOI.setBiactive(false);
        	}
        } 
                       
        basicTransactionFormCOI.setTransactionItem(transactionItem);
        
        boolean test = transactionItem.isEmail();
        
        //For RLS/DPS instead of getting Type Of Use Code from dropdown in QP page, get it before hand by TOU selected previously
        if ( transactionItem.isRepublication() )
        {
            String republicationTypeOfUse = transactionItem.getRepublicationTypeOfUse();
            basicTransactionFormCOI.setTypeOfUseCode(  TransactionItemTypeOfUseMapper.getRepublicationTypeOfUseTransactionConstants(transactionItem.getRepublicationTypeOfUse()));
            //basicTransactionFormCOI.setTypeOfUseCode( TransactionItemTypeOfUseMapper.( republicationTypeOfUse ));
        }
                
        /* if (transactionItem instanceof PurchasablePermission && ((PurchasablePermission)transactionItem).getRightFromWeb() != null) {
            basicTransactionFormCOI.setRightPermissionType(((PurchasablePermission)transactionItem).getRightFromWeb().getPermission().getCode());
        }
        else if (!(transactionItem instanceof PurchasablePermission) && transactionItem.getRight() != null)
        {
            basicTransactionFormCOI.setRightPermissionType(transactionItem.getRight().getPermission().getPermissionValueCode());
        }*/
        basicTransactionFormCOI.setRightPermissionType(transactionItem.getItemAvailabilityCd());
        
        /* String price1 = transactionItem.getPrice();
        boolean specialOrder = transactionItem.isSpecialOrderFromScratch();
        boolean photocopy = transactionItem.isPhotocopy();
        long pages = transactionItem.getNumberOfPages();
        String pubYear = transactionItem.getPublicationYearOfUse();
        long numPages = transactionItem.getNumberOfPages();
        long numCopies = transactionItem.getNumberOfCopies(); */
       // String price2 = transactionItem.ge

        return mapping.findForward( SHOW_MAIN );

    }

    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
        BasicTransactionForm basicTransactionForm = castForm( BasicTransactionForm.class, form );

        TransactionItem transactionItem = basicTransactionForm.getTransactionItem();
        
       // boolean photoCopy = transactionItem.isPhotocopy();

        //  2009-05-07  MSJ
        //  Is this a RIGHTSLINK rightsholder?  I probably could have done
        //  the integration in about 5 lines of code, setting a flag in the
        //  session back in the search action, but decided we should, perhaps,
        //  make the integration with RIGHTSLINK a little more OBVIOUS...
        //  does that make sense?  Anyway, we basically check to see if the
        //  permission is owned by a rightsholder that is also listed in our
        //  RIGHTSLINK cross-reference table.  If so, we set a flag.  This is
        //  used to set error messages specific to RIGHTSLINK.  On the order
        //  pages we must also check for a RIGHTSLINK rightsholder in order
        //  to display the "blue box of RIGHTSLINK information."

        long ptyInst = transactionItem.getRightsholderInst();
        RLinkPublisher pub = null;
        boolean hasRightslink = false;

        try {
            pub = RLinkPublisherServices.getRLinkPublisherByPtyInst(ptyInst);
            if (pub != null) hasRightslink = true;
        }
        catch(Exception e) {
            _logger.error( LogUtil.getStack(e)); 
        }

        //get correct transaction item for given type of use
        try
        {
            transactionItem = getTransactionItemForTypeOfUseCode( transactionItem, basicTransactionForm.getTypeOfUseCode() );
        }
        catch( OutsideBiactiveDateRangeException e)
        {
            saveErrors( request, TransactionUtils.getDateNotInBiactiveRangeError() );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (ContactRHDirectlyUnavailableException e)
        {
            TransactionItem contactRHItem = e.getTransactionItem();
            if (hasRightslink) {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(contactRHItem) );
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyError(contactRHItem) );
            }
            basicTransactionForm.setTransactionItem( contactRHItem );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (SpecialOrderUnavailableException e)
        {
            if (hasRightslink) {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableSpecialOrderErrorWithRightslink() );
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableSpecialOrderError() );
            }
            basicTransactionForm.setTransactionItem( e.getTransactionItem() );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (DeniedUnavailableException e)
        {
            if (hasRightslink) {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() );
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedError() );
            }
            basicTransactionForm.setTransactionItem( e.getTransactionItem() );
            return mapping.findForward( SHOW_MAIN );
        }

        //attempt to first calculate price because if i try to just perform the transaction and there is a special order limits exceeded, it will
        //alter the isSpecialOrder boolean of the permission request, which I don't want to do, until I place the special order
        try
        {
            String test = TransactionUtils.getItemPrice( transactionItem ); 
        }
        catch (InvalidAttributesException e)
        {
            if( TransactionUtils.isSystemLimitsExceededError(e) )
            {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructSystemLimitsExceededError() );
                return mapping.findForward( SHOW_MAIN );
            }
            else
            {
                saveErrors( request, TransactionUtils.getInvalidAttributeValidationErrors(e) );
                return mapping.findForward( SHOW_MAIN );
            }
        }
        catch (ContactRHDirectlyLimitsExceededException e)
        {
            if (hasRightslink) {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(transactionItem) );
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyError(transactionItem) );
            }
            return mapping.findForward( SHOW_MAIN );
        }
        catch (DeniedLimitsExceededException e)
        {
            String reason = TransactionUtils.getReasonForLimitsExceededException( e );

            if( reason.equals( TransactionConstants.REASON_UNKNOWN) ) {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedError() );
                }
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededDeniedError(reason) );
            }
            return mapping.findForward( SHOW_MAIN );
        }
        catch (SpecialOrderLimitsExceededException e)
        {
            String reason = TransactionUtils.getReasonForLimitsExceededException( e );
            String reasonMessage = e.getLimitsExceededException().getMessage();

            if( reason.equals( TransactionConstants.REASON_UNKNOWN) ) {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableSpecialOrderErrorWithRightslink() );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableSpecialOrderError() );
                }
            }
            else {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededSpecialOrderErrorWithRightslink(reason) );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededSpecialOrderError(reasonMessage) );
                }
            }
            return mapping.findForward( SHOW_MAIN );
        }
        catch (SystemLimitsExceededException e)
        {
            basicTransactionForm.setPricingError( PricingErrorFactory.constructSystemLimitsExceededError() );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (ServiceInvocationException e)
        {
            _logger.error( LogUtil.getStack(e));         	
            basicTransactionForm.setPricingError( PricingErrorFactory.constructServiceInvocationExceptionError() );
            return mapping.findForward( SHOW_MAIN );
        }
        //  MSJ 2008/01/22
        //  If the "skip quickprice" flag was set in the transaction form, we want
        //  to flip the flag from false to true and save the user information.

       /* CCUser ccUser = UserContextService.getActiveAppUser();
        if (ccUser.skipQuickprice() != basicTransactionForm.getSkipQuickprice()) {
            UserServices.updateCurrentUserSkipQuickprice(basicTransactionForm.getSkipQuickprice());
        } */
        
        //If the Skip Quick Price flag is turned ON, then save it for the session
      //Look for the Skip Quick Price flag for Academic (APS/ECCS) and Photocopy (TRS) types
        if (transactionItem.isAcademic() || transactionItem.isPhotocopy())
        {
        	request.getSession().setAttribute(WebConstants.SessionKeys.SKIP_QUICKPRICE, basicTransactionForm.getSkipQuickprice());
        }

        //if reaches here, no limits exceeded or unavailable errors, so go ahead and perform the transaction.
        try
        {
            ActionForward forward = performTransaction( transactionItem, basicTransactionForm.getPurchasablePermissionInCart(),
                                                        basicTransactionForm.getOrderPurchaseID(), mapping, request,
                                                        basicTransactionForm.getCp(), basicTransactionForm.getRp(),
                                                        basicTransactionForm.isSf());

            WebUtils.clearActionFormFromSession( request, mapping );
            return forward;
        }
        //these exceptions should theoretically never be thrown because it should have been caught by the above call to calculateTranscationItemPrice
        //but show errors just in case
        catch (InvalidAttributesException e)
        {
            if( TransactionUtils.isSystemLimitsExceededError(e) )
            {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructSystemLimitsExceededError() );
                return mapping.findForward( SHOW_MAIN );
            }
            else
            {
                saveErrors( request, TransactionUtils.getInvalidAttributeValidationErrors(e) );
                return mapping.findForward( SHOW_MAIN );
            }
        }
        catch (ChangedToRegularOrderException e)
        {
            basicTransactionForm.setPricingError( PricingErrorFactory.constructChangedToRegularOrderError() );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (ContactRHDirectlyLimitsExceededException e)
        {
            if (hasRightslink) {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(transactionItem) );
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyError(transactionItem) );
            }
            return mapping.findForward( SHOW_MAIN );
        }
        catch (SpecialOrderLimitsExceededException e)
        {
            String reason = TransactionUtils.getReasonForLimitsExceededException( e );

            if( reason.equals( TransactionConstants.REASON_UNKNOWN) ) {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableSpecialOrderErrorWithRightslink() );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableSpecialOrderError() );
                }
            }
            else {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededSpecialOrderErrorWithRightslink(reason) );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededSpecialOrderError(reason) );
                }
            }
            return mapping.findForward( SHOW_MAIN );
        }
        catch (DeniedLimitsExceededException e)
        {
            String reason = TransactionUtils.getReasonForLimitsExceededException( e );
            if( reason.equals( TransactionConstants.REASON_UNKNOWN) ) {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedError() );
                }
            }
            else {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededDeniedErrorWithRightslink(reason) );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededDeniedError(reason) );
                }
            }
            return mapping.findForward( SHOW_MAIN );
        }
        catch (IncompatibleOrderAndPurchasablePermissionException e)
        {
            ActionMessages incorrectOrderItemTypeError = TransactionUtils.getIncorrectOrderItemTypeError();
            saveErrors( request, incorrectOrderItemTypeError );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (ItemCannotBePurchasedException e)
        {
            ActionMessages itemCannotBePurchasedError = TransactionUtils.getItemCannotBePurchasedError( e );
            saveErrors( request, itemCannotBePurchasedError );
            return mapping.findForward( SHOW_MAIN );
        }
    }

    public ActionForward placeSpecialOrder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
    {
        BasicTransactionForm basicTransactionForm = castForm( BasicTransactionForm.class, form );

        TransactionItem transactionItem = basicTransactionForm.getTransactionItem();

        //  2009-05-07  MSJ
        //  Is this a RIGHTSLINK rightsholder?

        long ptyInst = transactionItem.getRightsholderInst();
        RLinkPublisher pub = null;
        boolean hasRightslink = false;

        try {
            pub = RLinkPublisherServices.getRLinkPublisherByPtyInst(ptyInst);
            if (pub != null) hasRightslink = true;
        }
        catch(Exception e) {
            _logger.error( LogUtil.getStack(e)); 
        }

        //get correct transaction item for given type of use
        try
        {
            transactionItem = getTransactionItemForTypeOfUseCode( transactionItem, basicTransactionForm.getTypeOfUseCode() );
        }
        catch( OutsideBiactiveDateRangeException e)
        {
            saveErrors( request, TransactionUtils.getDateNotInBiactiveRangeError() );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (ContactRHDirectlyUnavailableException e)
        {
            TransactionItem contactRHItem = e.getTransactionItem();
            if (hasRightslink) {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(contactRHItem) );
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyError(contactRHItem) );
            }
            basicTransactionForm.setTransactionItem( contactRHItem );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (DeniedUnavailableException e)
        {
            if (hasRightslink) {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() );
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedError() );
            }
            basicTransactionForm.setTransactionItem( e.getTransactionItem() );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (SpecialOrderUnavailableException e)
        {
            TransactionItem specialOrderTransactionItem = e.getTransactionItem();

            request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM, specialOrderTransactionItem );
            request.setAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_IN_CART, basicTransactionForm.getPurchasablePermissionInCart() );
            request.setAttribute( WebConstants.RequestKeys.PURCHASE_ID, String.valueOf(basicTransactionForm.getOrderPurchaseID()) );

            WebUtils.clearActionFormFromSession( request, mapping );
            return mapping.findForward( SPECIAL_ORDER );
        }

        //if gets here, item is available so calculate price and see if it's a special order limits exceeded
        try
        {
            TransactionUtils.getItemPrice( transactionItem );

            //if gets to this point, it means it's a regular order so just return back to regular order and set errors to empty
            basicTransactionForm.setPricingError( null );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (InvalidAttributesException e)
        {
            if( TransactionUtils.isSystemLimitsExceededError(e) )
            {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructSystemLimitsExceededError() );
                return mapping.findForward( SHOW_MAIN );
            }
            else
            {
                saveErrors( request, TransactionUtils.getInvalidAttributeValidationErrors(e) );
                return mapping.findForward( SHOW_MAIN );
            }
        }
        catch (ContactRHDirectlyLimitsExceededException e)
        {
            if (hasRightslink) {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(transactionItem) );
            }
            else {
                basicTransactionForm.setPricingError( PricingErrorFactory.constructContactRHDirectlyError(transactionItem) );
            }
            return mapping.findForward( SHOW_MAIN );
        }
        catch (DeniedLimitsExceededException e)
        {
            String reason = TransactionUtils.getReasonForLimitsExceededException( e );
            if( reason.equals( TransactionConstants.REASON_UNKNOWN) ) {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructNotAvailableDeniedError() );
                }
            }
            else {
                if (hasRightslink) {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededDeniedErrorWithRightslink(reason) );
                }
                else {
                    basicTransactionForm.setPricingError( PricingErrorFactory.constructLimitsExceededDeniedError(reason) );
                }
            }
            return mapping.findForward( SHOW_MAIN );
        }
        catch (SpecialOrderLimitsExceededException e)
        {
            transactionItem.setSpecialOrderLimitsExceeded( true );

            request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM, transactionItem );
            request.setAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_IN_CART, basicTransactionForm.getPurchasablePermissionInCart() );
            request.setAttribute( WebConstants.RequestKeys.PURCHASE_ID, String.valueOf(basicTransactionForm.getOrderPurchaseID()) );

            WebUtils.clearActionFormFromSession( request, mapping );
            return mapping.findForward( SPECIAL_ORDER );
        }
        catch (SystemLimitsExceededException e)
        {
            basicTransactionForm.setPricingError( PricingErrorFactory.constructSystemLimitsExceededError() );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (ServiceInvocationException e)
        {
            _logger.error( LogUtil.getStack(e)); 
            basicTransactionForm.setPricingError( PricingErrorFactory.constructServiceInvocationExceptionError());
            return mapping.findForward( SHOW_MAIN );
        }
    }

    /**
     * This method will be called by both the "continue" and "update price" button.
     * It is the method called by the ajax function.
     **/
    public void calculatePrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        BasicTransactionForm basicTransactionForm = castForm( BasicTransactionForm.class, form );

        //from now on, display publication year range of current publication (i.e. even for biactive DPS and RLS)
        basicTransactionForm.setDisplayPublicationYearRange( true );

        String xmlResponse = "";

        TransactionItem transactionItem = basicTransactionForm.getTransactionItem();
        int typeOfUseCode = basicTransactionForm.getTypeOfUseCode();

        //  2009-05-07  MSJ
        //  Is this a RIGHTSLINK rightsholder?

        long ptyInst = transactionItem.getRightsholderInst();
        RLinkPublisher pub = null;
        boolean hasRightslink = false;

        try {
            pub = RLinkPublisherServices.getRLinkPublisherByPtyInst(ptyInst);
            if (pub != null) hasRightslink = true;
        }
        catch(Exception e) {
            _logger.error( LogUtil.getStack(e)); 
        }

        //get correct transaction item for given type of use
        try
        {
            transactionItem = getTransactionItemForTypeOfUseCode( transactionItem, typeOfUseCode );

            try
            {
                String price = TransactionUtils.getItemPrice( transactionItem );
                xmlResponse = TransactionAJAXUtils.generateSuccessResponse( price, transactionItem );
            }
            catch (InvalidAttributesException e)
            {
                if( TransactionUtils.isSystemLimitsExceededError(e) )
                {
                    xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                        PricingErrorFactory.constructSystemLimitsExceededError(), transactionItem );
                }
                else
                {
                    xmlResponse = TransactionAJAXUtils.generateXMLInvalidAttributesErrorResponse( e, transactionItem );
                }
            }
            catch (ContactRHDirectlyLimitsExceededException e)
            {
                if (hasRightslink) {
                    xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                        PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(transactionItem), transactionItem );
                }
                else {
                    xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                        PricingErrorFactory.constructContactRHDirectlyError(transactionItem), transactionItem );
                }
            }
            catch (DeniedLimitsExceededException e)
            {
                String reason = e.getLimitsExceededException().getMessage();
                PricingError pricingError = null;

                if (hasRightslink) {
                    pricingError = reason.equals( TransactionConstants.REASON_UNKNOWN) ?
                        PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() : PricingErrorFactory.constructLimitsExceededDeniedErrorWithRightslink(reason);
                }
                else {
                    pricingError = reason.equals( TransactionConstants.REASON_UNKNOWN) ?
                        PricingErrorFactory.constructNotAvailableDeniedError() : PricingErrorFactory.constructLimitsExceededDeniedError(reason);
                }
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request), pricingError, transactionItem );
            }
            catch (SpecialOrderLimitsExceededException e)
            {
                String reason = e.getLimitsExceededException().getMessage();
                PricingError pricingError = null;

                if (hasRightslink) {
                    pricingError = reason.equals( TransactionConstants.REASON_UNKNOWN ) ?
                        PricingErrorFactory.constructNotAvailableSpecialOrderErrorWithRightslink() : PricingErrorFactory.constructLimitsExceededSpecialOrderErrorWithRightslink(reason);
                }
                else {
                	pricingError = reason.equals( TransactionConstants.REASON_UNKNOWN ) ?
                        PricingErrorFactory.constructNotAvailableSpecialOrderError() : PricingErrorFactory.constructLimitsExceededSpecialOrderError(reason);
                }
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request), pricingError, transactionItem );
            }
            catch (SystemLimitsExceededException e)
            {
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                    PricingErrorFactory.constructSystemLimitsExceededError(), transactionItem );
            }
            catch (ServiceInvocationException e)
            {
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                    PricingErrorFactory.constructServiceInvocationExceptionError(), transactionItem );
            }
        }
        catch( OutsideBiactiveDateRangeException e )
        {
            xmlResponse = TransactionAJAXUtils.generateDateNotInBiactiveRangeXMLErrorResponse( getResources(request), transactionItem );
        }
        catch (ContactRHDirectlyUnavailableException e)
        {
            TransactionItem contactRHItem = e.getTransactionItem();
            if (hasRightslink) {
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                    PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(contactRHItem), contactRHItem);
            }
            else {
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                    PricingErrorFactory.constructContactRHDirectlyError(contactRHItem), contactRHItem);
            }
        }
        catch (DeniedUnavailableException e)
        {
            if (hasRightslink) {
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                    PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink(), e.getTransactionItem() );
            }
            else {
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                    PricingErrorFactory.constructNotAvailableDeniedError(), e.getTransactionItem() );
            }
        }
        catch (SpecialOrderUnavailableException e)
        {
            if (hasRightslink) {
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                    PricingErrorFactory.constructNotAvailableSpecialOrderErrorWithRightslink(), e.getTransactionItem() );
            }
            else {
                xmlResponse = TransactionAJAXUtils.generateXMLPricingErrorResponse( getResources(request),
                    PricingErrorFactory.constructNotAvailableSpecialOrderError(), e.getTransactionItem() );
            }
        }

        response.setContentType("text/xml");

        PrintWriter out = response.getWriter();
        out.write(xmlResponse);
        out.close();

    }

    //private methods

    private void populateFormFromSearch( HttpServletRequest request, BasicTransactionForm basicTransactionForm )
    {
        PublicationPermission publicationPermission = (PublicationPermission)request.getAttribute( WebConstants.RequestKeys.PUBLICATION_PERMISSION );
                
        PurchasablePermission purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission( publicationPermission );
        
        purchasablePermission.getItem().setIsSpecialOrder(false);
        
        basicTransactionForm.setTransactionItem( purchasablePermission );
        
        if (_logger.isDebugEnabled()) {
            _logger.debug("PopulateFormFromSearch -> purchasablePermission.getTypeOfContent:" +
                purchasablePermission.getTypeOfContent());   
        }

        //  MSJ 2008/01/22
        //  If the user is a "power user", that is, the SKIP_QUICKPRICE flag is set,
        //  we want to have the form pre-expanded.  Otherwise, go through the usual
        //  quickprice routine.

        if (basicTransactionForm.getSkipQuickprice()) {
            basicTransactionForm.setExpanded( true );
        }
        else {
            basicTransactionForm.setExpanded( false );
        }
        //basicTransactionForm.setExpanded( false );
        
        int typeOfUseCode = ( purchasablePermission.isRepublication() ) ?
            -1 : TransactionItemTypeOfUseMapper.getTypeOfUseCodeForTransactionItem( purchasablePermission );
        basicTransactionForm.setTypeOfUseCode( typeOfUseCode );

        boolean displayPublicationYearRange = TransactionUtils.displayInitialPublicationYearRange( publicationPermission, purchasablePermission );
        basicTransactionForm.setDisplayPublicationYearRange( displayPublicationYearRange );

        populateFormWithOtherRequestParameters( request, basicTransactionForm );
        populateNumberOfStudentsFromCourseHeader( basicTransactionForm );
    }

    private void populateFormFromNonSearch( HttpServletRequest request, BasicTransactionForm basicTransactionForm )
    {
        TransactionItem transactionItem = (TransactionItem)request.getAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM );

        basicTransactionForm.setTransactionItem( transactionItem );
        basicTransactionForm.setExpanded( true );
        basicTransactionForm.setTypeOfUseCode( TransactionItemTypeOfUseMapper.getTypeOfUseCodeForTransactionItem( transactionItem ) );
        basicTransactionForm.setDisplayPublicationYearRange( true );

        populateFormWithOtherRequestParameters( request, basicTransactionForm );
    }

    private void populateFormWithOtherRequestParameters( HttpServletRequest request, BasicTransactionForm basicTransactionForm )
    {
        Object purchasablePermissionInCartObj = request.getAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_IN_CART );
        PurchasablePermission purchasablePermissionInCart = (purchasablePermissionInCartObj == null) ? null : (PurchasablePermission)purchasablePermissionInCartObj;
        basicTransactionForm.setPurchasablePermissionInCart( purchasablePermissionInCart );

        Object orderPurchaseIdObj = request.getAttribute( WebConstants.RequestKeys.PURCHASE_ID );
        int orderPurchaseId = (orderPurchaseIdObj == null) ? 0 : Integer.parseInt( (String)request.getAttribute(WebConstants.RequestKeys.PURCHASE_ID) );
        basicTransactionForm.setOrderPurchaseID( orderPurchaseId );

        Object selectPermissionPathFromSpecialObj = request.getAttribute( WebConstants.RequestKeys.SELECT_PERMISSION_PATH );
        String selectPermissionPath = (selectPermissionPathFromSpecialObj == null ) ? "" : (String)selectPermissionPathFromSpecialObj;
        basicTransactionForm.setSelectPermissionPathFromSpecial( selectPermissionPath );

        Object pricingErrorObj = request.getAttribute( WebConstants.RequestKeys.PRICING_ERROR );
        if( pricingErrorObj == null )
        {
            basicTransactionForm.setPricingError( null );

            Object calculatePriceOnLoadObj = request.getAttribute( WebConstants.RequestKeys.RECALCULATE_PRICE );
            if( calculatePriceOnLoadObj != null && ((Boolean)calculatePriceOnLoadObj).booleanValue() )
                basicTransactionForm.setCalculatePriceOnLoad( true );
            else
                basicTransactionForm.setCalculatePriceOnLoad( false );
        }
        else
        {
            PricingError pricingError = (PricingError)pricingErrorObj;
            basicTransactionForm.setPricingError( pricingError );

            boolean calculatePriceOnLoad = (pricingError.getErrorAction() == TransactionConstants.PRICING_ERROR_ACTION_REGULAR_ORDER);
            basicTransactionForm.setCalculatePriceOnLoad( calculatePriceOnLoad );
        }
    }

    private void populateNumberOfStudentsFromCourseHeader( BasicTransactionForm basicTransactionForm )
    {
        TransactionItem transactionItem = basicTransactionForm.getTransactionItem();

        if( transactionItem.isAcademic() )
        {
            int orderPurchaseId = basicTransactionForm.getOrderPurchaseID();
            if( orderPurchaseId > 0 )
            {
                try
                {
                    OrderPurchases orderPurchases = OrderPurchaseServices.getOrderPurchasesForConfNum( orderPurchaseId );
                    if( orderPurchases != null )
                    {
                        OrderPurchase orderPurchase = orderPurchases.getOrderPurchase( 0 );
                        if( orderPurchase != null )
                            transactionItem.setNumberOfStudents( (int)orderPurchase.getNumberOfStudents() );
                    }
                }
                catch (OrderPurchasesException e)
                {
                    _logger.error( LogUtil.getStack(e)); 
                }
            }
            else
            {
//                CourseDetails courseDetails = CartServices.getCourseDetails();
//                if( courseDetails != null )
            	transactionItem.setNumberOfStudents( CartServices.getNumberOfStudentsFromCourseDetails() );
            }
        }

        basicTransactionForm.setTransactionItem( transactionItem );
    }

}
