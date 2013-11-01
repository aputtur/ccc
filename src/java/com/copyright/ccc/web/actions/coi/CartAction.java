package com.copyright.ccc.web.actions.coi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.UpdatedCartItem;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ItemParmEnum;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.cart.CannotBeRemovedFromCartException;
import com.copyright.ccc.business.services.cart.CartNotAvailableException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CascadeUpdateException;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.ECommerceConstants;
import com.copyright.ccc.business.services.cart.IndexedECommerceExceptionWrapper;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.business.services.cart.SystemLimitsExceededException;
import com.copyright.ccc.business.services.rlnk.RlnkConstants;
import com.copyright.ccc.business.services.rlnk.RlnkRightsServices;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.CartForm;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.rightslink.base.RepriceErrorTypeEnum;
import com.copyright.rightslink.base.data.RepriceResponse;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.SpecialOrderTypeEnum;

public class CartAction extends CCAction
{
    private static final Logger LOGGER = Logger.getLogger( CartAction.class );
    private static final String BASIC_EDIT_CART_ITEM = "basicEditCoiCartItem";
    private static final String RIGHTSLINK_EDIT_CART_ITEM = "rightsLinkEditCoiCartItem";
    
    private static final String SPECIAL_EDIT_CART_ITEM = "specialEditCoiCartItem";
    private static final String SHOW_MAIN_REDIRECT = "showMainRedirect";
    private static final String ACADEMIC = "academic";
    
    
   
    

    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
    	CartForm cartForm = castForm( CartForm.class, form );
    	resetFormValus(cartForm);
        // if authenticated, then there will be RL reprice call.
        // if RL down do not reprice and do not allow to checkout
    	loadCart(cartForm,request);
    	// if authenticated reprice the cart
    	cartForm.setChkRightsLnkDown(Boolean.FALSE);
    	
    	if(UserContextService.isUserAuthenticated()){
    		// copy cart  items to compare after price change
    		for(PurchasablePermission  purchasableCartItem:cartForm.getCartItems()){
    			Item cloneItem=new Item();
    			PurchasablePermission clonedPurchasablePermission = null;
    			
    			//cloneItem.setTotalPrice(cartItem.getItem().getTotalPrice());
    			/// compare with totalPricevalue instead of total price
    			cloneItem.setTotalPrice(purchasableCartItem.getTotalPriceValue());
    			cloneItem.setPrimaryKey(purchasableCartItem.getItem().getItemId());
    			cloneItem.setIsSpecialOrder(purchasableCartItem.getItem().getIsSpecialOrder());
    			cloneItem.setProductCd(purchasableCartItem.getItem().getProductCd());
    			cloneItem.setProductSourceKey(purchasableCartItem.getProductSourceKey());
    			clonedPurchasablePermission=PurchasablePermissionFactory.createPurchasablePermission( cloneItem);
    			clonedPurchasablePermission.setPublicationTitle(purchasableCartItem.getPublicationTitle());
    			clonedPurchasablePermission.setCategoryId(purchasableCartItem.getCategoryId());
    			clonedPurchasablePermission.setCategoryName(purchasableCartItem.getCategoryName());
    			cartForm.getPreUpdatedCartItems().add(clonedPurchasablePermission);
    	          }
//    		cartForm.getPreUpdatedCartItems().add(CartServices.getPurchasablePermissionsInCart());
    	          /*END initliazie cart items to update any changes */ 
    		try{
    	    	if(CartServices.updateCartForRightsLinkPriceChange() || CartServices.updateCartForTFAcademicPriceChange()|| CartServices.updateCartForTFNonAcademicPriceChange() ){
    	        	loadCart(cartForm,request);
    	        }
    		}
    		catch( Exception e){
    			cartForm.setHasCartErrors(true);
    			LOGGER.error("RIGHTSLINK REPRICING EXCEPTION");
    			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
    			return mapping.findForward( SHOW_MAIN );
    		}
    	    	// identify cart mode to show appropriate messaging header
    	    	UserContextService.setCartItemEdited(false);
    	    	cartForm.setIsCartItemEdited(UserContextService.isCartItemEdited());

    	    	getCartItemPriceChangeMessages(cartForm);
    	    
    	    	if(!SystemStatus.isRightslinkUp() && CartServices.getRightsLinkItemsInCart().size()>0){
    	    		cartForm.setChkRightsLnkDown(Boolean.TRUE);
    			     	LOGGER.info("Rights link service is down");
    	        }
    	}
    	
        request.setAttribute("emulating",UserContextService.isEmulating());
        
        if(!cartForm.getIsRemovedIdnoWT()){
        	cartForm.setRemovedIdnoWT(null);
        }
        cartForm.setIsRemovedIdnoWT(Boolean.FALSE);
        return mapping.findForward( SHOW_MAIN );
    }
	    
    private void loadCart(ActionForm form, HttpServletRequest request){
    	   CartForm cartForm = castForm( CartForm.class, form );
           
           
           if(!CartServices.isCartEmpty() && SystemStatus.isRightslinkUp()) {
        	   CartServices.checkAndRemoveInvalidCartItems();
               
           }
       
           
           //Set Non academic items and total in cart
           List<PurchasablePermission> itemsInCart = CartServices.getPurchasablePermissionsInCart();
           cartForm.setCartItems( itemsInCart );
           cartForm.intializeCartFormItems();           
           setUnableToModifyCartError( cartForm, request );
           
    }
    private void  resetFormValus(CartForm cartForm){
    	/*initliazie cart items to update any changes */
        cartForm.getPreUpdatedCartItems().clear();
        cartForm.getUpdatedAcademicCartItems().clear();
        cartForm.getUpdatedNonAcademicCartItems().clear();
        cartForm.getPriceUpdatedItem().clear();
        cartForm.setChkRightsLnkDown(Boolean.FALSE);
        cartForm.setHasCartErrors(Boolean.FALSE);
    }
    public ActionForward editCartItem(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        CartForm cartForm = castForm( CartForm.class, form );
        cartForm.setIsCartItemEdited(Boolean.TRUE);
        
        int cartItemIndex = cartForm.getSelectedItemIndex();
        
        PurchasablePermission purchasablePermission = null;
        
        if (cartForm.getSelectedItem().equalsIgnoreCase(ACADEMIC))
        {        
        	purchasablePermission = cartForm.getAcademicCartItems().get(cartItemIndex);
        }
        else
        {
        	purchasablePermission = cartForm.getNonAcademicCartItems().get(cartItemIndex);
        }
     
        
        if(purchasablePermission != null)
        {
            request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM, purchasablePermission);
            request.setAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_IN_CART, purchasablePermission);
            
            WebUtils.clearActionFormFromSession(request, mapping);
            
            if(purchasablePermission.isRightsLink())
            	return mapping.findForward(RIGHTSLINK_EDIT_CART_ITEM);
            else if(purchasablePermission.isSpecialOrder())
                return mapping.findForward(SPECIAL_EDIT_CART_ITEM);
            else return mapping.findForward(BASIC_EDIT_CART_ITEM);
        }
        else
        {
            //TODO: gcuevas 11/10/06: figure out what to do here if pp is null
            return null;
        }
    }
    
    public ActionForward removeCartItem(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        CartForm cartForm = castForm( CartForm.class, form );
        
        int cartItemIndex = cartForm.getSelectedItemIndex();
        cartForm.setIsCartItemEdited(Boolean.FALSE);
        
        PurchasablePermission purchasablePermission = null;
        
        try
        {
            if (cartForm.getSelectedItem().equalsIgnoreCase(ACADEMIC))
            {        
            	purchasablePermission = cartForm.getAcademicCartItems().get(cartItemIndex);
            }
            else
            {
            	purchasablePermission = cartForm.getNonAcademicCartItems().get(cartItemIndex);
            }
        }
        catch(IndexOutOfBoundsException e)
        {
             LOGGER.error( LogUtil.getStack(e)); 
             return mapping.findForward(SHOW_MAIN);
        }
        
        if(purchasablePermission != null)
        {
            try
            {
                CartServices.removeItemFromCart( purchasablePermission );
                cartForm.setRemovedIdnoWT(purchasablePermission.getStandardNumber());
                cartForm.setIsRemovedIdnoWT(Boolean.TRUE);
                return mapping.findForward(SHOW_MAIN_REDIRECT);
            }
            catch(CannotBeRemovedFromCartException e)
            {
                LOGGER.error( LogUtil.getStack(e)); 
                 return mapping.findForward(SHOW_MAIN);
            }  
        }
        else
        {
            LOGGER.error( "purchasablePermission is null"); 
            return mapping.findForward(SHOW_MAIN);
        }

    }
    
    public ActionForward removeCourse(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response)
    {
    	CartServices.removeCourse();
    	ActionForward forward = mapping.findForward( SHOW_MAIN_REDIRECT );
    	return forward;
    }
    
    public ActionForward emptyCart(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
 //       LOGGER.info("CartServices.isCartCheckedOut(): "+CartServices.isCartCheckedOut());
    	try
        {
    		//reset the rl titles message
       request.getSession().setAttribute( WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES,null );
        CartServices.emptyCart();
        }
        catch(CartNotAvailableException e)
        {
            LOGGER.error( LogUtil.getStack(e)); 
        	LOGGER.info("Cart is not available");
        }
        
        return mapping.findForward(SHOW_MAIN_REDIRECT);
    }
  

    
    private void setUnableToModifyCartError( CartForm cartForm, HttpServletRequest request )
    {
        Object cascadeUpdateExceptionObj = request.getAttribute( WebConstants.RequestKeys.CASCADE_UPDATE_EXCEPTION );
        Object copyOrderErrorTitlesObj = request.getAttribute( WebConstants.RequestKeys.COPY_ORDER_ERROR_PUBLICATION_TITLES );
        Object rlCopyOrderErrorTitlesObj = request.getSession().getAttribute( WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES );
        
        
        if( cascadeUpdateExceptionObj != null && cascadeUpdateExceptionObj instanceof CascadeUpdateException )
            setCascadeUpdateError( cartForm, (CascadeUpdateException)cascadeUpdateExceptionObj );
        else if( copyOrderErrorTitlesObj != null && copyOrderErrorTitlesObj instanceof List ) {
            @SuppressWarnings("unchecked")
        	List<String> copyOrderErrorTitlesList = (List<String>) copyOrderErrorTitlesObj; 
            setCopyOrderUpdateError( cartForm, copyOrderErrorTitlesList );
        }
        else if(rlCopyOrderErrorTitlesObj != null && rlCopyOrderErrorTitlesObj instanceof List){
            @SuppressWarnings("unchecked")
        	List<String> rlCopyOrderErrorTitlesList = (List<String>) rlCopyOrderErrorTitlesObj;
          //  setCopyOrderUpdateError( cartForm, rlCopyOrderErrorTitlesList );
           setRLCopyOrderUpdateError( cartForm, rlCopyOrderErrorTitlesList );
            request.getSession().setAttribute( WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES,null );
            
        }
        else
        {
            cartForm.setUnableToModifyCartErrorHeaderMessageKey( null );
            cartForm.setUnableToModifyCartErrorDisplayStrings( null );
        }
    }
    
    private void setCascadeUpdateError( CartForm cartForm, CascadeUpdateException cascadeUpdateException )
    {
        TreeMap<Integer,String> indexTitleSortedMap = new TreeMap<Integer,String>();
        
        //invalid attributes exception
        Iterator<IndexedECommerceExceptionWrapper> invalidAttributesWrapperIterator = cascadeUpdateException.getInvalidAttributesExceptions().iterator();
        while( invalidAttributesWrapperIterator.hasNext() )
        {
            IndexedECommerceExceptionWrapper invalidAttributesWrapper = invalidAttributesWrapperIterator.next();
            InvalidAttributesException invalidAttributesException = (InvalidAttributesException)invalidAttributesWrapper.getECommerceException();
            indexTitleSortedMap.put( invalidAttributesWrapper.getIndex(), invalidAttributesException.getTransactionItem().getPublicationTitle() );
        }
        
        //denied limits exceeded exception
        Iterator<IndexedECommerceExceptionWrapper> deniedWrapperIterator = cascadeUpdateException.getDeniedLimitsExceededExceptions().iterator();
        while(deniedWrapperIterator.hasNext())
        {
            IndexedECommerceExceptionWrapper deniedWrapper = deniedWrapperIterator.next();
            DeniedLimitsExceededException deniedException = (DeniedLimitsExceededException)deniedWrapper.getECommerceException();
            indexTitleSortedMap.put( deniedWrapper.getIndex(), deniedException.getTransactionItem().getPublicationTitle() );
        }
        
        //special order limits exceeded exception
        Iterator<IndexedECommerceExceptionWrapper> specialOrderIterator = cascadeUpdateException.getSpecialOrderLimitsExceededExceptions().iterator();
        while(specialOrderIterator.hasNext())
        {
            IndexedECommerceExceptionWrapper specialOrderWrapper = specialOrderIterator.next();
            SpecialOrderLimitsExceededException specialOrderException = (SpecialOrderLimitsExceededException)specialOrderWrapper.getECommerceException();
            indexTitleSortedMap.put( specialOrderWrapper.getIndex(), specialOrderException.getTransactionItem().getPublicationTitle() );
        }
        
        //contact rh directly limits exceeded exception
        Iterator<IndexedECommerceExceptionWrapper> contactRHIterator = cascadeUpdateException.getContactRHDirectlyLimitsExceededExceptions().iterator();
        while(contactRHIterator.hasNext())
        {
            IndexedECommerceExceptionWrapper contactRHWrapper = contactRHIterator.next();
            ContactRHDirectlyLimitsExceededException contactRHException = (ContactRHDirectlyLimitsExceededException)contactRHWrapper.getECommerceException();
            indexTitleSortedMap.put( contactRHWrapper.getIndex(), contactRHException.getTransactionItem().getPublicationTitle() );
        }
        
        //changed to regular order exception
        Iterator<IndexedECommerceExceptionWrapper> changedToRegularIterator = cascadeUpdateException.getChangedToRegularOrderExceptions().iterator();
        while(changedToRegularIterator.hasNext())
        {
            IndexedECommerceExceptionWrapper changedToRegularWrapper = changedToRegularIterator.next();
            ChangedToRegularOrderException changedToRegularException = (ChangedToRegularOrderException)changedToRegularWrapper.getECommerceException();
            indexTitleSortedMap.put( changedToRegularWrapper.getIndex(), changedToRegularException.getPurchasablePermission().getPublicationTitle() );
        }
        
        //create array list out of sorted map
        ArrayList<String> cascadeUpdateErrorItemDisplayStrings = new ArrayList<String>();
        
        Iterator<Map.Entry<Integer,String>> indexIterator = indexTitleSortedMap.entrySet().iterator();
        
        for ( Iterator<Map.Entry<Integer,String>> paramItr = indexIterator; paramItr.hasNext(); )
        {
        	Map.Entry<Integer,String> entry = paramItr.next();
        	 Integer index = entry.getKey();
             String publicationTitle = entry.getValue();
             cascadeUpdateErrorItemDisplayStrings.add( index.intValue() + ". " + publicationTitle );
        }
    
        
        cartForm.setUnableToModifyCartErrorHeaderMessageKey( "cart.error.cascadeUpdate" );
        cartForm.setUnableToModifyCartErrorDisplayStrings( cascadeUpdateErrorItemDisplayStrings );
    }
    
    private void setCopyOrderUpdateError( CartForm cartForm, List<String> copyOrderErrorTitles )
    {   
        cartForm.setUnableToModifyCartErrorHeaderMessageKey( "cart.error.copyOrder" );
        
        ArrayList<String> copyOrderErrorDisplayStrings = generateErrorStringsFromTitlesList( copyOrderErrorTitles );
        cartForm.setUnableToModifyCartErrorDisplayStrings( copyOrderErrorDisplayStrings );
    }
    
    private void setRLCopyOrderUpdateError( CartForm cartForm, List<String> copyOrderErrorTitles )
    {   
        cartForm.setUnableToModifyCartErrorHeaderMessageKey( "ov.error.copy.license.rlink.cart" );
        
        ArrayList<String> copyOrderErrorDisplayStrings = generateErrorStringsFromTitlesList( copyOrderErrorTitles );
        cartForm.setUnableToModifyCartErrorDisplayStrings( copyOrderErrorDisplayStrings );
    }
    
    private ArrayList<String> generateErrorStringsFromTitlesList( List<String> titlesList )
    {
        ArrayList<String> errorDisplayStrings = new ArrayList<String>();
        
        for( int i = 0; i < titlesList.size(); i++ )
        {
            errorDisplayStrings.add( (i+1) + ". " + titlesList.get(i) );
        }
        
        return errorDisplayStrings;
    }
    
    private int getNumberOfSpecialOrders()
    {
    	List<PurchasablePermission> items = new ArrayList<PurchasablePermission>(0);
    	int i = 0;
    	
    	items= CartServices.getPurchasablePermissionsInCart();
    	
    	for (PurchasablePermission perm : items) {
			if (perm.isSpecialOrder()) {
				i++;
				return i;
			}
		}
    	return 0;
    }
    

    
    /**
     * 
     * @param cartForm
     */
    private void getCartItemPriceChangeMessages(CartForm cartForm) {
    	
    	List<PurchasablePermission> itemsInCart= cartForm.getCartItems();
		PurchasablePermission cartItemReadyToDisplay = null;
		Integer nonAcadamicCount = 1;
		Integer acadamicCount = 1;
		boolean updatedToSpecialOrder=false;
		boolean updatedToPriceChange=false;
		boolean updatedToRemoveItem=false;
		Map<Long, PurchasablePermission> itemsInCartReadyToDisplayMap = new HashMap<Long, PurchasablePermission>();
		
		// read all cart items into map
		for (PurchasablePermission p : itemsInCart) {
			PurchasablePermission cartItems = p;
			itemsInCartReadyToDisplayMap.put(cartItems.getItem().getItemId(), cartItems);
		}
		//isUpdatedToSpecialOrder,boolean isUpdatedToPriceChange,boolean isUpdatedToRemoveItems
		List<PurchasablePermission> preUpdatedCartItems=cartForm.getPreUpdatedCartItems();
			//loop through preUpdateCartItes and compare with ready to display cart items
		for (int index=0; index< preUpdatedCartItems.size(); index++){
				updatedToSpecialOrder=false;
				updatedToPriceChange=false;
				updatedToRemoveItem=false;
			if (itemsInCartReadyToDisplayMap.containsKey(preUpdatedCartItems.get(index).getItem().getItemId())) {
				// add to appropriate list. know if this is updated
				cartItemReadyToDisplay = itemsInCartReadyToDisplayMap.get(preUpdatedCartItems.get(index).getItem().getItemId());
				if(isCartItemChangedToSpecial(preUpdatedCartItems.get(index),cartItemReadyToDisplay)){
					updatedToSpecialOrder=true;
				//if (cartItemReadyToDisplay.isSpecialOrder()) {
					// commented below line since we don't show price updated message for change to special order
					//cartForm.getPriceUpdatedItem().put(cartItemReadyToDisplay.getItem().getItemId(), "UPDATED<br/>PRICE");
					if (cartItemReadyToDisplay.isAcademic()) {
						cartForm.getUpdatedAcademicCartItems().add(new UpdatedCartItem(cartItemReadyToDisplay,updatedToSpecialOrder,updatedToPriceChange,updatedToRemoveItem,acadamicCount++));
					} else {
						cartForm.getUpdatedNonAcademicCartItems().add(new UpdatedCartItem(cartItemReadyToDisplay,updatedToSpecialOrder,updatedToPriceChange,updatedToRemoveItem,nonAcadamicCount++));
					}
				} else if(isCartItemPriceChanged(preUpdatedCartItems.get(index),cartItemReadyToDisplay)){ 
					// then price change
					updatedToPriceChange=true;
					cartForm.getPriceUpdatedItem().put(cartItemReadyToDisplay.getItem().getItemId(), "UPDATED<br/>PRICE");
					if (cartItemReadyToDisplay.isAcademic()) {
						cartForm.getUpdatedAcademicCartItems().add(new UpdatedCartItem(cartItemReadyToDisplay,updatedToSpecialOrder,updatedToPriceChange,updatedToRemoveItem,acadamicCount++));
					} else {
						cartForm.getUpdatedNonAcademicCartItems().add(new UpdatedCartItem(cartItemReadyToDisplay,updatedToSpecialOrder,updatedToPriceChange,updatedToRemoveItem,nonAcadamicCount++));
					}
				}

			} else {
				// added to the removed list
				updatedToRemoveItem=true;
				//if (preUpdatedCartItems.get(index).isAcademic()) {
					if (preUpdatedCartItems.get(index).isAcademic()) {
						cartForm.getUpdatedAcademicCartItems().add(new UpdatedCartItem(preUpdatedCartItems.get(index),updatedToSpecialOrder,updatedToPriceChange,updatedToRemoveItem,0));
					} else {
						cartForm.getUpdatedNonAcademicCartItems().add(new UpdatedCartItem(preUpdatedCartItems.get(index),updatedToSpecialOrder,updatedToPriceChange,updatedToRemoveItem,0));
					}

				//}
			}

		}
		// clean up null ivalue to retain index
	/*	for (int index=cartForm.getUpdatedToRemovedCartItems().size()-1; index>=0;index--){
			if(cartForm.getUpdatedToRemovedCartItems().get(index)==null)
				cartForm.getUpdatedToRemovedCartItems().remove(index);
    	}*/
		

	}

    
    private boolean isCartItemChangedToSpecial(PurchasablePermission oldCartItem,PurchasablePermission newCartItem){
    	if (newCartItem.isSpecialOrder() && !oldCartItem.isSpecialOrder()) {
    		return true;
    	}
    	// for testing
    	/*if (newCartItem.getItem().getItemId() % 2 == 0){
        	return true;
        	}*/
        return false;
    }
    private boolean isCartItemPriceChanged(PurchasablePermission oldCartItem,PurchasablePermission newCartItem){
    	if (!newCartItem.isSpecialOrder() && newCartItem.getTotalPriceValue().compareTo(oldCartItem.getItem().getTotalPrice()) != 0){
    		return true;
    	}
    	// for testing
    	/*if (newCartItem.getItem().getItemId() % 2 != 0){
    	return true;
    	}*/
    	return false;
    }
    
    
    
    public static boolean checkForCartUpdates(){
    	 
    	 Double totalPriceBeforeUpdate		=	Double.valueOf(0.00);
    	 Double totalPriceAfterUpdate		=	Double.valueOf(0.00);
    	 Boolean isSpecialOrderBeforeUpdate		=	true;
    	 
    	 
	    	 for(PurchasablePermission tfPurchasablePermission:CartServices.getTFItemsInCart()){
	    		 totalPriceBeforeUpdate=Double.valueOf(0.00);
	    		 isSpecialOrderBeforeUpdate=tfPurchasablePermission.isSpecialOrder();
	    		 
	    		 if(tfPurchasablePermission.getItem().getTotalPrice()!=null){
	    			 	totalPriceBeforeUpdate=Double.valueOf(tfPurchasablePermission.getTotalPriceValue().toString());
	    		 }
	    		 
	     		try{
	     		String itemPrice=PricingServices.getItemPriceForUpdateCheck(tfPurchasablePermission,UserContextService.getActiveAppUser().getPartyID());
	     				if(itemPrice.equals(ECommerceConstants.CONTACT_RIGHTSHOLDER_DIRECTLY)) {
	     					return true;
	     				}
	     				if(!tfPurchasablePermission.isSpecialOrder() && !itemPrice.equalsIgnoreCase(ECommerceConstants.PRICE_NOT_AVAILABLE)){
             				itemPrice = itemPrice.replaceAll( "\\$", "" ).trim().replace(",", "");
		        			try {
		        				totalPriceAfterUpdate=Double.valueOf(itemPrice); 
		        			} catch (Exception e) { 
		        	             LOGGER.error( LogUtil.getStack(e)); 
		        	             totalPriceAfterUpdate = new Double(-1); 
		        			}
		        			if(totalPriceAfterUpdate.compareTo(totalPriceBeforeUpdate)!=0){
		        				return true;
		        			}
		        		}else if(tfPurchasablePermission.isSpecialOrder()) {
		        			if (!itemPrice.equals(ECommerceConstants.PRICE_NOT_AVAILABLE)) {
		        				return true;
		        			}
		        		} else if(!tfPurchasablePermission.isSpecialOrder()) {
		        			if (itemPrice.equals(ECommerceConstants.PRICE_NOT_AVAILABLE)) {
		        				return true;
		        			}
		        		}
	     		}
	     		catch(InvalidAttributesException i){
	     			return true;
	     		}
	     		catch(DeniedLimitsExceededException d){
	     			return true;
	     		}
	     		catch(SpecialOrderLimitsExceededException s){
	     			if (!tfPurchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.LIMITS_EXCEEDED_SPECIAL_ORDER.name()) &&
	     				!tfPurchasablePermission.getSpecialOrderTypeCd().equals(SpecialOrderTypeEnum.BIBLIO_PAGES_MISSING_SPECIAL_ORDER.name())) {
	     				return true;
	      			}
	       		}
	     		catch(ContactRHDirectlyLimitsExceededException c){
	     			return true;
	     		}
	     		catch(SystemLimitsExceededException s){
	     			return true;							
	     		}
	     	}
	    	 // for rights link
	    	 
	    	 // if service is down for Rightslink item then return false;
	       if(!SystemStatus.isRightslinkUp() && CartServices.getRightsLinkItemsInCart().size()>0){
	    	   LOGGER.info("Rights link service is down");
	       		return true;
	       	}
	      	 for(PurchasablePermission rlPurchaseablePermission:CartServices.getRightsLinkItemsInCart()){
	      		 try{
	      			 
	            		RepriceResponse repriceResponse=CartServices.rePriceRightsLinkItem(rlPurchaseablePermission);
	            			            		
	            		String nrlsUserFee = CC2Configuration.getInstance().getNrlsUserFeeValue();
	            		
	            		if ( rlPurchaseablePermission.getItem().getItemParms().get(ItemParmEnum.OFFERCHANNEL.name()) != null
	            				&& rlPurchaseablePermission.getItem().getItemParms().get(ItemParmEnum.OFFERCHANNEL.name()).getParmValue().equalsIgnoreCase(RlnkConstants.PUB_TO_PUB)
	            				&& (!UserContextService.isSuppressNRLSFee()) 
	            				&& rlPurchaseablePermission.getItem().getTotalPrice() != null 
	            				&& rlPurchaseablePermission.getItem().getTotalPrice().compareTo(BigDecimal.ZERO) > 0) {
	            			
	            			repriceResponse.getPriceData().setTotalPrice(repriceResponse.getPriceData().getTotalPrice().add(new BigDecimal(nrlsUserFee)));
	            		}
	            		
	            		
	            		if(repriceResponse.isExceptionOccurred() && repriceResponse.getErrorType()==null){
		      				throw new Exception(ExceptionUtils.getFullStackTrace(repriceResponse.getThrowable()));
	            			}
	            		
	            		if( repriceResponse.getErrorType()!=null ){
	            			if(repriceResponse.getErrorType().equals(RepriceErrorTypeEnum.NO_ERROR) && 
	            					RlnkRightsServices.isPriceDifferent(repriceResponse.getPriceData())){
            				return true;
    	        			
            				}else if(repriceResponse.getErrorType().equals(RepriceErrorTypeEnum.NON_RECOVERABLE) || repriceResponse.getErrorType().equals(RepriceErrorTypeEnum.RECOVERABLE)){
            					return true;
            				}
	            		}
	      		 }catch(Exception e){
	      			LOGGER.error("RIGHTSLINK REPRICING EXCEPTION " + LogUtil.appendableStack(e));
	      			return true;
         		}
	            			
	      	 }
    	return false;
    }
    
    
   
  
}


