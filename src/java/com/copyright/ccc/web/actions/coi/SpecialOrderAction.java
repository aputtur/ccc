package com.copyright.ccc.web.actions.coi;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.PublicationPermission;
import com.copyright.ccc.business.data.RLinkPublisher;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.ServiceInvocationException;
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
import com.copyright.ccc.business.services.user.RLinkPublisherServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.SearchForm;
import com.copyright.ccc.web.forms.coi.SpecialOrderForm;
import com.copyright.ccc.web.transaction.ChangedToRegularFromSpecialUnavailableException;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.PricingError;
import com.copyright.ccc.web.transaction.PricingErrorFactory;
import com.copyright.ccc.web.transaction.TransactionConstants;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;
import com.copyright.ccc.web.util.WebUtils;

public abstract class SpecialOrderAction extends CCAction
{
    //ActionForward names
    protected final static String CANCEL = "cancel";
    protected final static String SUBMIT = "submit";
    protected final static String BASIC_TRANSACTION = "basicTransaction";
    protected final static String SHOW_MAIN_DEFAULT = "showMainDefault";

    //abstract methods

     protected abstract TransactionItem getTransactionItemForTypeOfUseCode( TransactionItem transactionItem, int typeOfUseCode )
         throws DeniedUnavailableException, ContactRHDirectlyUnavailableException, ChangedToRegularFromSpecialUnavailableException, OutsideBiactiveDateRangeException;

    protected abstract ActionForward performTransaction(TransactionItem transactionItem, PurchasablePermission purchasablePermissionInCart,
                                                        int orderPurchaseId, ActionMapping mapping, HttpServletRequest request, int scrollPage,
                                                        String returnTab, boolean searchFlag)
         throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
            ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException, IncompatibleOrderAndPurchasablePermissionException,
            ItemCannotBePurchasedException;

    protected abstract String getCancelPath( TransactionItem transactionItem, int orderPurchaseId, int scrollPage, String tabPage, boolean searchFlag );

    protected abstract boolean isSpecialFirstAcademicItem( TransactionItem transactionItem );

    protected abstract String getSelectPermissionPath();

    //public methods

    public ActionForward defaultOperation( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
    	SearchForm searchForm = (SearchForm) request.getSession().getAttribute( WebConstants.SessionKeys.SEARCH_FORM );
        SpecialOrderForm specialOrderForm = castForm( SpecialOrderForm.class, form );

        specialOrderForm.setSpecialFormPath( mapping.getPath() );
        
        boolean fromSearch;

        if( request.getAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM ) != null )
        {
            populateFormFromNonSearchOrNonScratch( request, specialOrderForm );
        	fromSearch = false;
        }
        
        else if ( request.getAttribute( WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE ) != null ||
            request.getParameter( WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE ) != null )
        {
            populateFormFromScratch( request, specialOrderForm );
        	fromSearch = false;
        }
        else
        { populateFormFromSearch( request, specialOrderForm );
        	fromSearch = true;
        }
        	
        	
        TransactionItem specialOrderItem = specialOrderForm.getSpecialOrderItem();
        boolean specialOrder = true;
        

        //specialOrderItem.setCategoryName( searchForm.getSelectedPermCatDisplay().getCategoryDescription());
        //specialOrderItem.setCategoryName(UserContextService.getSearchState().getSelectedPermissionType());
        //specialOrderItem.setTouName(UserContextService.getSearchState().getSelectedTou());
        
        if (fromSearch && searchForm.getSelectedPermCatDisplay() != null ) {
        	specialOrderItem.setCategoryName( searchForm.getSelectedPermCatDisplay().getCategoryDescription());
        }
        
        if (fromSearch) 
        {            	       	
        	if (searchForm.getIsBiactive())
        	{
        		specialOrderItem.setPublicationYearOfUse(searchForm.getSelectedPubYear());
        		specialOrderForm.setBiactive(true);
        	}
        } 
        
        if ((request.getParameter("radioButtonText") != null) && !(request.getParameter("radioButtonText").isEmpty()))
        {
        	specialOrderItem.setCategoryName(request.getParameter("radioButtonText"));
        }
        
        specialOrderForm.setSpecialOrderItem(specialOrderItem);
        
        //if( !purch.isSpecialOrder() )
        if (!specialOrder)
        {
            request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM, specialOrderItem );
            WebUtils.clearActionFormFromSession( request, mapping );
            return mapping.findForward( BASIC_TRANSACTION );
        }
        else
        {
            String cancelPath = this.getCancelPath( specialOrderItem, specialOrderForm.getSpecialOrderPurchaseID(), specialOrderForm.getCp(), specialOrderForm.getRp(), specialOrderForm.isSf() );
            specialOrderForm.setSpecialCancelPath( cancelPath );

            String selectPermissionPath = this.getSelectPermissionPath();
            specialOrderForm.setSelectPermissionPath( selectPermissionPath );

            boolean isSpecialFirstAcademicItem = this.isSpecialFirstAcademicItem( specialOrderItem );
            specialOrderForm.setSpecialFirstAcademicItem( isSpecialFirstAcademicItem );

            return mapping.findForward( SHOW_MAIN );
        }

    }

    public ActionForward submit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
    {
        SpecialOrderForm specialOrderForm = castForm( SpecialOrderForm.class, form );

        specialOrderForm.setSpecialDisplayPublicationYearRange( true );

        TransactionItem newTransactionItem = specialOrderForm.getSpecialOrderItem();
        TransactionItem transactionItem  = null;
        
        if (specialOrderForm.getSpecialTypeOfUseCode() != -1 )
        {
        	if (newTransactionItem instanceof PurchasablePermission)
        	{	
        		newTransactionItem.setTouName(TransactionItemTypeOfUseMapper.getRepublicationTypeOfUseDescription(specialOrderForm.getSpecialTypeOfUseCode()));
        		PurchasablePermission purchasablePermission = (PurchasablePermission) newTransactionItem;
        	   	purchasablePermission.getItem().setExternalTouId(new Long(TransactionItemTypeOfUseMapper.getTypeOfUseInstForTransactionConstants(specialOrderForm.getSpecialTypeOfUseCode())));
        	        	
        	   	transactionItem = purchasablePermission;
        	}
        	else
        	{
        		transactionItem = specialOrderForm.getSpecialOrderItem();
        	}
        }

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
            _logger.error(LogUtil.getStack(e));
        }

        try
        {
            if( transactionItem.isSpecialOrderFromScratch() && ((transactionItem instanceof PurchasablePermission && !((PurchasablePermission)transactionItem).isPushToTFFailed()) ||
                    !(transactionItem instanceof PurchasablePermission)))
                transactionItem =  TransactionUtils.pinSpecialOrderFromScratch( transactionItem, specialOrderForm.getSpecialTypeOfUseCode() );
        }
        catch( DeniedUnavailableException e )
        {
            if (hasRightslink) {
                putErrorItemsInRequest(e.getTransactionItem(), PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink(), specialOrderForm, request);
            }
            else {
                putErrorItemsInRequest(e.getTransactionItem(), PricingErrorFactory.constructNotAvailableDeniedError(), specialOrderForm, request);
            }
            //return mapping.findForward( BASIC_TRANSACTION );
            return mapping.findForward( SHOW_MAIN_DEFAULT );
        }
        catch( ContactRHDirectlyUnavailableException e )
        {
            TransactionItem contactRHItem = e.getTransactionItem();
            if (hasRightslink) {
                putErrorItemsInRequest(contactRHItem, PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(contactRHItem), specialOrderForm, request);
            }
            else {
                putErrorItemsInRequest(contactRHItem, PricingErrorFactory.constructContactRHDirectlyError(contactRHItem), specialOrderForm, request);
            }
            //return mapping.findForward( BASIC_TRANSACTION );
            return mapping.findForward( SHOW_MAIN_DEFAULT );
        } 
        catch( ChangedToRegularFromSpecialUnavailableException e )
        {
            //only do this if not digital or repub because the pinning returns most permissable right which is not necessarily the correct
            //right.  we won't fetch the correct right until getTransactionItemForTypeOfUseCode is called.  Thus, we don't want to call
            //the pricing service with the wrong right.
            if( !transactionItem.isDigital() && !transactionItem.isRepublication() )
            {
                TransactionItem regularTransactionItem = e.getTransactionItem();

                //check if limits exceeded: if deny or contact rh, throw error, if special order, set limits exceeded flag, else go to regular order screen
                try
                {
                    TransactionUtils.getItemPrice( regularTransactionItem );

                    //no pricing errors, so go to regular order screen
                    putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructChangedToRegularOrderError(), specialOrderForm, request );
                    return mapping.findForward( BASIC_TRANSACTION );
                }
                catch (InvalidAttributesException iae)
                {
                    if (TransactionUtils.isSystemLimitsExceededError(iae))
                	{
                        putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructSystemLimitsExceededError(), specialOrderForm, request);
                        WebUtils.clearActionFormFromSession( request, mapping );
                        return mapping.findForward( BASIC_TRANSACTION );
                    }
                    else
                    {
                    	saveErrors( request, TransactionUtils.getInvalidAttributeValidationErrors(iae) );
                    	specialOrderForm.setSpecialOrderItem( transactionItem );
                        return mapping.findForward( SHOW_MAIN );
                    }
                }
                catch (ContactRHDirectlyLimitsExceededException crdlee)
                {
                    if (hasRightslink) {
                        putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(regularTransactionItem), specialOrderForm, request);
                    }
                    else {
                        putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructContactRHDirectlyError(regularTransactionItem), specialOrderForm, request);
                    }
                    WebUtils.clearActionFormFromSession( request, mapping );
                    return mapping.findForward( BASIC_TRANSACTION );
                }
                catch (DeniedLimitsExceededException dlee)
                {
                    String reason = TransactionUtils.getReasonForLimitsExceededException( dlee );
                    PricingError pricingError = null;
                    if (hasRightslink) {
                        pricingError = ( reason.equals( TransactionConstants.REASON_UNKNOWN) ) ?
                            PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() : PricingErrorFactory.constructLimitsExceededDeniedErrorWithRightslink( reason );
                    }
                    else {
                        pricingError = ( reason.equals( TransactionConstants.REASON_UNKNOWN) ) ?
                            PricingErrorFactory.constructNotAvailableDeniedError() : PricingErrorFactory.constructLimitsExceededDeniedError( reason );
                    }
                    putErrorItemsInRequest( regularTransactionItem, pricingError, specialOrderForm, request);
                    WebUtils.clearActionFormFromSession( request, mapping );
                    return mapping.findForward( BASIC_TRANSACTION );
                }
                catch (SpecialOrderLimitsExceededException solee)
                {
                    transactionItem = regularTransactionItem;
                    transactionItem.setSpecialOrderLimitsExceeded( true );
                }
                catch (SystemLimitsExceededException slee)
                {
                    putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructSystemLimitsExceededError(), specialOrderForm, request);
                    WebUtils.clearActionFormFromSession( request, mapping );
                    return mapping.findForward( BASIC_TRANSACTION );
                }
                catch (ServiceInvocationException sie)
                {
                	_logger.error(LogUtil.getStack(sie));
                	putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructServiceInvocationExceptionError(), specialOrderForm, request);
                    WebUtils.clearActionFormFromSession( request, mapping );
                    return mapping.findForward( BASIC_TRANSACTION );
                }
            }
            else
                transactionItem = e.getTransactionItem();
        }
        try
        {
            transactionItem = getTransactionItemForTypeOfUseCode( transactionItem, specialOrderForm.getSpecialTypeOfUseCode() );
        }
        catch( OutsideBiactiveDateRangeException e )
        {
            saveErrors( request, TransactionUtils.getDateNotInBiactiveRangeError() );
            specialOrderForm.setSpecialOrderItem( transactionItem );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (ChangedToRegularFromSpecialUnavailableException e)
        {
            TransactionItem regularTransactionItem = e.getTransactionItem();

            //check if limits exceeded: if deny or contact rh, throw error, if special order, set limits exceeded flag, else go to regular order screen
            try
            {
                TransactionUtils.getItemPrice( regularTransactionItem );

                //no pricing errors, so go to regular order screen
                putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructChangedToRegularOrderError(), specialOrderForm, request );
                return mapping.findForward( BASIC_TRANSACTION );
            }
            catch (InvalidAttributesException iae)
            {
                if ( TransactionUtils.isSystemLimitsExceededError(iae) )
                {
                    putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructSystemLimitsExceededError(), specialOrderForm, request);
                    WebUtils.clearActionFormFromSession( request, mapping );
                    return mapping.findForward( BASIC_TRANSACTION );
                }
                else
                {
                    saveErrors( request, TransactionUtils.getInvalidAttributeValidationErrors(iae)  );
                    specialOrderForm.setSpecialOrderItem( transactionItem );
                    return mapping.findForward( SHOW_MAIN );
                }
            }
            catch (ContactRHDirectlyLimitsExceededException crdlee)
            {
                if (hasRightslink) {
                    putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(regularTransactionItem), specialOrderForm, request);
                }
                else {
                    putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructContactRHDirectlyError(regularTransactionItem), specialOrderForm, request);
                }
                WebUtils.clearActionFormFromSession( request, mapping );
                return mapping.findForward( BASIC_TRANSACTION );
            }
            catch (DeniedLimitsExceededException dlee)
            {
                String reason = TransactionUtils.getReasonForLimitsExceededException( dlee );
                PricingError pricingError = null;
                if (hasRightslink) {
                    pricingError = ( reason.equals( TransactionConstants.REASON_UNKNOWN) ) ?
                        PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() : PricingErrorFactory.constructLimitsExceededDeniedErrorWithRightslink( reason );
                }
                else {
                    pricingError = ( reason.equals( TransactionConstants.REASON_UNKNOWN) ) ?
                        PricingErrorFactory.constructNotAvailableDeniedError() : PricingErrorFactory.constructLimitsExceededDeniedError( reason );
                }
                putErrorItemsInRequest( regularTransactionItem, pricingError, specialOrderForm, request);
                WebUtils.clearActionFormFromSession( request, mapping );
                return mapping.findForward( BASIC_TRANSACTION );
            }
            catch (SpecialOrderLimitsExceededException solee)
            {
                transactionItem = regularTransactionItem;
                transactionItem.setSpecialOrderLimitsExceeded( true );
            }
            catch (SystemLimitsExceededException slee)
            {
                putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructSystemLimitsExceededError(), specialOrderForm, request);
                WebUtils.clearActionFormFromSession( request, mapping );
                return mapping.findForward( BASIC_TRANSACTION );
            }
            catch (ServiceInvocationException sie)
            {
            	_logger.error(LogUtil.getStack(sie));            	
                putErrorItemsInRequest( regularTransactionItem, PricingErrorFactory.constructServiceInvocationExceptionError(), specialOrderForm, request);
                WebUtils.clearActionFormFromSession( request, mapping );
                return mapping.findForward( BASIC_TRANSACTION );
            }
        }
        catch (ContactRHDirectlyUnavailableException e)
        {
            TransactionItem contactRHItem = e.getTransactionItem();
            if (hasRightslink) {
                putErrorItemsInRequest(contactRHItem, PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(contactRHItem), specialOrderForm, request);
            }
            else {
                putErrorItemsInRequest(contactRHItem, PricingErrorFactory.constructContactRHDirectlyError(contactRHItem), specialOrderForm, request);
            }
            //return mapping.findForward( BASIC_TRANSACTION );
            return mapping.findForward( SHOW_MAIN_DEFAULT );
        }
        catch (DeniedUnavailableException e)
        {
        /*    if (hasRightslink) {
                putErrorItemsInRequest(e.getTransactionItem(), PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink(), specialOrderForm, request);
            }
            else {
                putErrorItemsInRequest(e.getTransactionItem(), PricingErrorFactory.constructNotAvailableDeniedError(), specialOrderForm, request);
            }
            //return mapping.findForward( BASIC_TRANSACTION );
            return mapping.findForward( SHOW_MAIN_DEFAULT ); */
        }
        try
        {
            ActionForward forward =
                    performTransaction( transactionItem, specialOrderForm.getSpecialPurchasablePermissionInCart(),
                                                    specialOrderForm.getSpecialOrderPurchaseID(), mapping, request,
                                                    specialOrderForm.getCp(), specialOrderForm.getRp(), specialOrderForm.isSf());

            WebUtils.clearActionFormFromSession( request, mapping );

            return forward;
        }
        catch (InvalidAttributesException e)
        {
        	if( TransactionUtils.isSystemLimitsExceededError(e) ) 
            {
                putErrorItemsInRequest( transactionItem, PricingErrorFactory.constructSystemLimitsExceededError(), specialOrderForm, request);
                WebUtils.clearActionFormFromSession( request, mapping );
                return mapping.findForward( BASIC_TRANSACTION );
            }
            else
            {
                saveErrors( request, TransactionUtils.getInvalidAttributeValidationErrors(e) );
                specialOrderForm.setSpecialOrderItem( transactionItem );
                return mapping.findForward( SHOW_MAIN );
            }
        }
        catch (ChangedToRegularOrderException e)
        {
            //actually get transaction item from exception, because the service changes the special order flags to false
            putErrorItemsInRequest( e.getPurchasablePermission(), PricingErrorFactory.constructChangedToRegularOrderError(), specialOrderForm, request );
            WebUtils.clearActionFormFromSession( request, mapping );
            return mapping.findForward( BASIC_TRANSACTION );
        }
        catch (ContactRHDirectlyLimitsExceededException e)
        {
            if (hasRightslink) {
                putErrorItemsInRequest( transactionItem, PricingErrorFactory.constructContactRHDirectlyErrorWithRightslink(transactionItem), specialOrderForm, request);
            }
            else {
                putErrorItemsInRequest( transactionItem, PricingErrorFactory.constructContactRHDirectlyError(transactionItem), specialOrderForm, request);
            }
            WebUtils.clearActionFormFromSession( request, mapping );
            return mapping.findForward( BASIC_TRANSACTION );
        }
        catch (DeniedLimitsExceededException e)
        {
            String reason = TransactionUtils.getReasonForLimitsExceededException( e );
            PricingError pricingError = null;
            if (hasRightslink) {
                pricingError = ( reason.equals( TransactionConstants.REASON_UNKNOWN) ) ?
                    PricingErrorFactory.constructNotAvailableDeniedErrorWithRightslink() : PricingErrorFactory.constructLimitsExceededDeniedErrorWithRightslink( reason );
            }
            else {
                pricingError = ( reason.equals( TransactionConstants.REASON_UNKNOWN) ) ?
                    PricingErrorFactory.constructNotAvailableDeniedError() : PricingErrorFactory.constructLimitsExceededDeniedError( reason );
            }
            putErrorItemsInRequest( transactionItem, pricingError, specialOrderForm, request);
            WebUtils.clearActionFormFromSession( request, mapping );
            return mapping.findForward( BASIC_TRANSACTION );
        }
        catch (SpecialOrderLimitsExceededException e)
        {
            //this should never happen
            specialOrderForm.setSpecialOrderItem( transactionItem );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (IncompatibleOrderAndPurchasablePermissionException e)
        {
        	_logger.warn(LogUtil.getStack(e));
            ActionMessages incorrectOrderItemTypeError = TransactionUtils.getIncorrectOrderItemTypeError();
            saveErrors( request, incorrectOrderItemTypeError );
            specialOrderForm.setSpecialOrderItem( transactionItem );
            return mapping.findForward( SHOW_MAIN );
        }
        catch (ItemCannotBePurchasedException e)
        {
        	_logger.warn(LogUtil.getStack(e));
            ActionMessages itemCannotBePurchasedError = TransactionUtils.getItemCannotBePurchasedError(e);
            saveErrors( request, itemCannotBePurchasedError );
            specialOrderForm.setSpecialOrderItem( transactionItem );
            return mapping.findForward( SHOW_MAIN );
        }
    }

    private void putErrorItemsInRequest(TransactionItem transactionItem, PricingError pricingError, SpecialOrderForm specialOrderForm,
                                        HttpServletRequest request)
    {
        request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM, transactionItem );
        request.setAttribute( WebConstants.RequestKeys.PRICING_ERROR, pricingError );
        request.setAttribute( WebConstants.RequestKeys.SELECT_PERMISSION_PATH, specialOrderForm.getSelectPermissionPath() );
        request.setAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_IN_CART, specialOrderForm.getSpecialPurchasablePermissionInCart() );
        request.setAttribute( WebConstants.RequestKeys.PURCHASE_ID, String.valueOf(specialOrderForm.getSpecialOrderPurchaseID()) );
    }

    private void populateFormFromSearch( HttpServletRequest request, SpecialOrderForm specialOrderForm )
    {
        PublicationPermission specialPublicationPermission = (PublicationPermission)request.getAttribute( WebConstants.RequestKeys.PUBLICATION_PERMISSION );
        PurchasablePermission specialPurchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(specialPublicationPermission);
        specialOrderForm.setSpecialOrderItem( specialPurchasablePermission );

        int specialTypeOfUseCode = ( specialPurchasablePermission.isRepublication() ) ?
            -1 : TransactionItemTypeOfUseMapper.getTypeOfUseCodeForTransactionItem( specialPurchasablePermission );
        specialOrderForm.setSpecialTypeOfUseCode( specialTypeOfUseCode );

        boolean displayPublicationYearRange = TransactionUtils.displayInitialPublicationYearRange( specialPublicationPermission, specialPurchasablePermission );
        specialOrderForm.setSpecialDisplayPublicationYearRange( displayPublicationYearRange );

        populateFormWithOtherRequestParameters( request, specialOrderForm );
    }

    private void populateFormFromScratch( HttpServletRequest request, SpecialOrderForm specialOrderForm )
    {
        String permissionType;

        Object permissionTypeObj = request.getAttribute( WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE );
        if( permissionTypeObj != null )
            permissionType = (String)permissionTypeObj;
        else permissionType = request.getParameter( WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE );
        
        String radioBtn = request.getParameter("radioButton");

        PurchasablePermission specialPurchasablePermission = TransactionUtils.createSpecialOrderPurchasablePermission( permissionType );
        
        //specialPurchasablePermission.setCategoryName(categoryName)
        specialOrderForm.setSpecialOrderItem( specialPurchasablePermission );

        int specialTypeOfUseCode = ( specialPurchasablePermission.isRepublication() ) ?
            -1 : TransactionItemTypeOfUseMapper.getTypeOfUseCodeForTransactionItem( specialPurchasablePermission );
        specialOrderForm.setSpecialTypeOfUseCode( specialTypeOfUseCode );

        specialOrderForm.setSpecialDisplayPublicationYearRange( false );

        populateFormWithOtherRequestParameters( request, specialOrderForm );
    }

    private void populateFormFromNonSearchOrNonScratch( HttpServletRequest request, SpecialOrderForm specialOrderForm )
    {
        TransactionItem specialOrderItem = (TransactionItem)request.getAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM );
        specialOrderForm.setSpecialOrderItem( specialOrderItem );

        specialOrderForm.setMissingTF((Boolean)request.getAttribute( WebConstants.RequestKeys.MISSING_TF));

        int specialTypeOfUseCode = TransactionItemTypeOfUseMapper.getTypeOfUseCodeForTransactionItem( specialOrderItem );

        specialOrderForm.setSpecialTypeOfUseCode( specialTypeOfUseCode );

        specialOrderForm.setSpecialDisplayPublicationYearRange( true );

        populateFormWithOtherRequestParameters( request, specialOrderForm );
    }

    private void populateFormWithOtherRequestParameters( HttpServletRequest request, SpecialOrderForm specialOrderForm )
    {
        Object specialPurchasablePermissionInCartObj = request.getAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_IN_CART );
        PurchasablePermission specialPurchasablePermissionInCart = (specialPurchasablePermissionInCartObj == null) ?
            null : (PurchasablePermission)specialPurchasablePermissionInCartObj;
        specialOrderForm.setSpecialPurchasablePermissionInCart( specialPurchasablePermissionInCart );

        Object specialOrderPurchaseIdObj = request.getAttribute( WebConstants.RequestKeys.PURCHASE_ID );
        int specialOrderPurchaseId = (specialOrderPurchaseIdObj == null) ? 0 : Integer.parseInt( (String)request.getAttribute(WebConstants.RequestKeys.PURCHASE_ID) );
        specialOrderForm.setSpecialOrderPurchaseID( specialOrderPurchaseId );
        
        Object selectPermissionPathFromSpecialObj = request.getAttribute( WebConstants.RequestKeys.SELECT_PERMISSION_PATH );
        String selectPermissionPath = (selectPermissionPathFromSpecialObj == null ) ? "" : (String)selectPermissionPathFromSpecialObj;
        specialOrderForm.setSelectPermissionPathFromSpecial( selectPermissionPath );

        Object pricingErrorObj = request.getAttribute( WebConstants.RequestKeys.PRICING_ERROR );
        if( pricingErrorObj == null )
        {
            specialOrderForm.setPricingError( null );

            Object calculatePriceOnLoadObj = request.getAttribute( WebConstants.RequestKeys.RECALCULATE_PRICE );
            if( calculatePriceOnLoadObj != null && ((Boolean)calculatePriceOnLoadObj).booleanValue() )
            	specialOrderForm.setCalculatePriceOnLoad( true );
            else
            	specialOrderForm.setCalculatePriceOnLoad( false );
        }
        else
        {
            PricingError pricingError = (PricingError)pricingErrorObj;
            specialOrderForm.setPricingError( pricingError );

            boolean calculatePriceOnLoad = (pricingError.getErrorAction() == TransactionConstants.PRICING_ERROR_ACTION_REGULAR_ORDER);
            specialOrderForm.setCalculatePriceOnLoad( calculatePriceOnLoad );
        }
    }

}
