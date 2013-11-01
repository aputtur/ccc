package com.copyright.ccc.web.actions;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.ItemParmEnum;
import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.articlesearch.ArticleSearchState;
import com.copyright.ccc.business.services.cart.CannotBeAddedToCartException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.rlnk.RlnkConstants;
import com.copyright.ccc.business.services.rlnk.RlnkRightsServices;
import com.copyright.ccc.business.services.rlnk.RlnkConstants.RlnkParmConstants;
import com.copyright.ccc.business.services.rlnk.RlnkConstants.RlnkSessionConstants;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.config.CC2Configuration;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.RightsLinkQuickPriceActionForm;
import com.copyright.ccc.web.forms.coi.RLinkSpecialOrderForm;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;
import com.copyright.ccc.web.util.ArticleDisplay;
import com.copyright.ccc.web.util.ChapterDisplay;
import com.copyright.ccc.web.util.RightsLnkQPriceUtil;
import com.copyright.domain.data.WorkExternal;
import com.copyright.domain.data.WorkExternal.IdnoTypeCodeValues;
import com.copyright.rightslink.base.data.RightResponse;
import com.copyright.rightslink.base.data.RlInitOrderFlow;
import com.copyright.rightslink.base.uifield.exception.TypeCodeException;
import com.copyright.rightslink.html.HtmlScreenBuilder;
import com.copyright.rightslink.html.data.HtmlScreenRequest;
import com.copyright.rightslink.html.data.HtmlScreenResponse;
import com.copyright.svc.order.api.data.ItemFees;
import com.copyright.svc.order.api.data.ItemParm;
import com.copyright.svc.rightsResolver.api.data.BootstrapRequest;
import com.copyright.svc.rightsResolver.api.data.ItemRequest;
import com.copyright.svc.rightsResolver.api.data.ItemRequestParm;
import com.copyright.svc.rightsResolver.api.data.ItemRequestResponse;
import com.copyright.workbench.util.StringUtils2;



public class RightsLinkQuickPriceAction extends CCAction{
	//private static final String CANCEL_PATH 	= "/articleSearch.do?operation=show&page=last";
	private static final String SHOW_MAIN 					= "showMain";
	private static final String SHOW_LOGIN					= "showLoginForRL";
	private static final String SHOW_ERROR					= "error";
	private static final String SHOW_REPRICE				= "showReprice";

	private static final int MAX_RETRIES = CC2Configuration.getInstance().getRightslinkRequestMaxRetries();
	private static final int MILLIS_BETWEEN_RETRIES = CC2Configuration.getInstance().getRightslinkRequestRetrySleepMillis();

	private static final Logger LOGGER = Logger.getLogger( RightsLinkQuickPriceAction.class );
	
	


	/**
	 * If session RL_REQUEST_PARM_IN_SESSION is not null then returning for Login check.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward defaultOperation(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response){
		RightsLinkQuickPriceActionForm rlForm=castForm( RightsLinkQuickPriceActionForm.class, form );
		rlForm.setErrors(null);
		rlForm.setQuickPriceErrorMessage(null);

		// fresh start
		rlForm.resetFormValues();
		request.getSession().setAttribute(RlnkSessionConstants.RL_REQUEST_PARM_IN_SESSION,null);
		if(SystemStatus.isRightslinkUp() && !UserContextService.isUserAuthenticated()){
			RlnkRightsServices.logout();
		}
		
		Boolean orderExists = (Boolean) request.getSession().getAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER);
        
        if (Boolean.TRUE.equals(orderExists)) 
        {
        	rlForm.setOrderExists(orderExists);
        }
        else
        {
        	rlForm.setOrderExists(false);
        }
        
        request.getSession().setAttribute(WebConstants.SessionKeys.PARM_EXISTING_ORDER, null);
        
        //Look for RLink Special Order from Scratch values        
        RLinkSpecialOrderForm rlinkSpecialForm = (RLinkSpecialOrderForm) request.getSession().getAttribute("rlinkSpecialOrderForm");
        
        if (rlinkSpecialForm != null && rlinkSpecialForm.isSpecialOrderFromScratch()) {
        	rlForm.setRLinkSpecialOrderFromScratch(true);
        	rlForm.setPublicationName(rlinkSpecialForm.getPublicationName());
        	rlForm.setIssn(rlinkSpecialForm.getIssn());
        	rlForm.setPublisher(rlinkSpecialForm.getPublisher());
        	rlForm.setVolume(rlinkSpecialForm.getVolume());
        	rlForm.setEdition(rlinkSpecialForm.getEdition());
        	rlForm.setPubDate(rlinkSpecialForm.getPubDate());
        	rlForm.setAuthor(rlinkSpecialForm.getAuthor());
        }
        
//        request.getSession().setAttribute("rlinkSpecialOrderForm", null);


		if(setUpWorkDetails(rlForm,request)){
			if (!SystemStatus.isRightslinkUp()) {
				rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.rightslnk.down.error"));
				return mapping.findForward(SHOW_ERROR);				
			} else {
				RightResponse rightResponse= runRightResponse(mapping,request, response, rlForm, true,false);
				if(rightResponse==null){
					return mapping.findForward(SHOW_ERROR);
				}
				generateHTMLScreen( request, response, rlForm, rightResponse );
			}
			request.getSession().setAttribute("rlForm", rlForm);
			return  mapping.findForward( SHOW_MAIN );
		}
		return mapping.findForward(SHOW_ERROR);

	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward refreshPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		RightsLinkQuickPriceActionForm rlForm = castForm( RightsLinkQuickPriceActionForm.class, form );

		/*
		 * if we don't have a selectedPublication then the session likely expired,
		 * redirect back to the permission summary page
		 */
		if (rlForm.getSelectedPubItem()==null) {
			return CCAction.sessionDataNotFound(mapping,request,String.valueOf(rlForm.getSelectedPubItemWrkInst()));				   
		}

		rlForm.setQuickPriceErrorMessage(null);
		if(!SystemStatus.isRightslinkUp()){
			rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.rightslnk.down.error"));
			return mapping.findForward(SHOW_ERROR );
		}
		RightResponse rightResponse= runRightResponse( mapping,request, response, rlForm, false,false);
		return renderQuickPricePage( mapping,  rlForm,  request,  response,rightResponse);
	}


	/**
	 * After successfully logging into C.com control flow is returned to this method.
	 * This happens via a redirect to this action from the 
	 * RightsLinkLoginAction. This actions primary task is to re-execute the
	 * runRightResponse method and tell it to use the request parameters that
	 * it finds in the session. These parameters contain the credentials
	 * which should have been put there by the RightsLinkLoginAction and they must be passed to 
	 * Rightslink in order to continue on with the next screen.
	 * 
	 * Note that if the user is a new user (and had to register), there is a chance that the asynchronous 
	 * parts of registration (invoked by central queue) haven't completed yet.
	 * If registration hasn't completed when we call runRightResponse, the RightResponse will contain:
	 * <p>
	 * hasErrors=true<br>
	 * loginRequired=false
	 * <p>
	 * It will also contain an error message intended to tell the user their login failed.
	 * We don't want to show this since it's misleading in the c.com context.
	 * At this point, if hasErrors==true we will check to see if Telesales is up. 
	 * If so we will reasonably assume that registration is still processing and will succeed in short order. 
	 * We will wait for that to finish by entering a polling loop that will retry the runRightResponse 
	 * until it either returns with hasErrors=false
	 * or the max number of polling requests is reached.
	 * <p>
	 * If the max number of polling requests is reached, we will replace the RightResponse error message with
	 * one that tells the user that registration may complete shortly. Something like:
	 * <p>"We could not continue processing this item. This could be due to an incomplete registration, please try again in a moment."
	 * <p>If Telesales is down, we will replace the message with something that is appropriate for a "telesales down"
	 * condition. Something like:
	 * <p>"We could not continue processing this item. Please try again later."
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward processLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, String> params = (Map<String, String>)request.getSession().getAttribute(RlnkSessionConstants.RL_REQUEST_PARM_IN_SESSION);
		RightsLinkQuickPriceActionForm rlForm = castForm( RightsLinkQuickPriceActionForm.class, form );
		
		rlForm.setRequestParameters(params);
		rlForm.setQuickPriceErrorMessage(null);
		
		if(!SystemStatus.isRightslinkUp()){
			rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.rightslnk.down.error"));
			return mapping.findForward(SHOW_ERROR );
		}

		/*
		 * try to get initial RightResponse after login
		 */
		RightResponse rightResponse= runRightResponse( mapping,request, response, rlForm, false,true);

		/*
		 * if it has errors and telesales is up, it's probably an incomplete registration
		 */
		if (rightResponse.hasErrors()) {
			if (SystemStatus.isTelesalesUp()) {
				RightResponse retriedRightResponse = runRightResponseWithRetry( mapping,request,response,rlForm);
				if (retriedRightResponse!=null) {
					rightResponse = retriedRightResponse;
				}
				replaceRightslinkAuthenticationFailedMessage(rightResponse,getResources(request).getMessage("rightslink.auth.failed.telesales.up"));
			} else {
				replaceRightslinkAuthenticationFailedMessage(rightResponse, getResources(request).getMessage("rightslink.auth.failed.telesales.down"));
			}
		}
		return renderQuickPricePage( mapping,  rlForm,  request,  response,rightResponse);
	}

	/*
	 * replaces the "Your login attempt failed..." message with a more c.com appropriate
	 * message
	 */
	private RightResponse replaceRightslinkAuthenticationFailedMessage(RightResponse rightResponse, String newMessage) {
		if (rightResponse != null && rightResponse.hasErrors()) {
			List<String> errorMsgs = rightResponse.getErrorList();
			if (errorMsgs!=null) {
				for(int i=0; i<errorMsgs.size(); i++) {
					if (errorMsgs.get(i).toLowerCase().startsWith("your login attempt failed")) {
						errorMsgs.set(i,newMessage); 
					}
				}
			}
		}
		return rightResponse;
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param rightResponse
	 * @return
	 */
	private ActionForward renderQuickPricePage(ActionMapping mapping,RightsLinkQuickPriceActionForm form,HttpServletRequest request, HttpServletResponse response,RightResponse rightResponse){

		if(rightResponse==null){
			return mapping.findForward(SHOW_ERROR );
		}else if(showLoginScreen(request, rightResponse)){
			if(rightsLinkLoginAttemptedWithError(request, rightResponse ,form)){
				return mapping.findForward(SHOW_ERROR );
			}else{
				request.getSession().setAttribute(RlnkSessionConstants.RL_REQUEST_PARM_IN_SESSION,form.getRequestParameters());
				request.getSession().setAttribute("RIGHTSLNK_LOGIN_MESSAGE", "Please log in to continue your transaction or create a new account.");
				return mapping.findForward( SHOW_LOGIN );
			}
		}else if(showAddToCart(rightResponse,form)){
			return addToCart( mapping,  form,request,  response);
		}
		generateHTMLScreen( request, response, form, rightResponse );
		return mapping.findForward( SHOW_MAIN );
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward addToCart(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response){


		RightsLinkQuickPriceActionForm rlForm = castForm( RightsLinkQuickPriceActionForm.class, form );
		WorkExternal 		subWork		    =	rlForm.getSubWork();
		PurchasablePermission purchasablePermission=null;
		ItemRequest 		itemRequest		=	null;
		ActionForward 		forward 		=	 mapping.findForward( SHOW_MAIN );

		/*
		 * if we don't have a selectedPublication then the session likely expired,
		 * redirect back to the permission summary page
		 */
		if (rlForm.getSelectedPubItem()==null) {
			return CCAction.sessionDataNotFound(mapping,request,String.valueOf(rlForm.getSelectedPubItemWrkInst()));				   
		}
		
		// update ponumber
		
	
		
		


		// get the purchasable permission
		if(rlForm.getIsUpdateableCartItem()){
			purchasablePermission 	= rlForm.getPurchasablePermission();
			purchasablePermission.setCategoryId(	UserContextService.getSearchState().getSelectedCategoryId());
			purchasablePermission.setCategoryName(rlForm.getSelectedCategory());
			purchasablePermission.setTouSourceKey(UserContextService.getSearchState().getSelectedRrTouId());
			purchasablePermission.setTouName(rlForm.getSelectedTou());
			purchasablePermission.getItem().setExternalTouId(UserContextService.getSearchState().getSelectedTouId());

		}
		else{
			purchasablePermission = PurchasablePermissionFactory.createRLPurchasablePermission(
					rlForm.getSelectedPubItem(),
					UserContextService.getSearchState().getSelectedCategoryId(),
					rlForm.getSelectedCategory(),
					UserContextService.getSearchState().getSelectedRrTouId(),
					rlForm.getSelectedTou(),
					UserContextService.getSearchState().getSelectedTouId()); // selectedTouId is RL Offer ID

		}

		if(purchasablePermission==null){
			throw new CCRuntimeException("PurchasablePermission is null.");
		}
		purchasablePermission.setItemSourceKey(rlForm.getSelectedPubItem().getWrkInst());

		if(subWork!=null){
			addArticleInforPurchasePermission( purchasablePermission, subWork);
		}
		// get RL  initial "itemrequest"
		itemRequest=PricingServices.getRLItemRequestFromPurchasablePermission(purchasablePermission);
		itemRequest.setItemTypeCd(RlnkConstants.ITEM_TYPECD_JOURNAL);

		ItemRequestResponse itemRequestResponse= RlnkRightsServices.getItemRequest("rightsLinkQuickPriceActionForm",rlForm.getRequestParameters(), itemRequest);
		
		
		
	
		

		if (itemRequestResponse.hasErrors()){

			forward= mapping.findForward( SHOW_REPRICE );
			// do something to return and display the RL page
		}else{
			//update itemRequest from rights link
			//itemRequestResponse.getItemRequest().getAllFees().get(0).setLicenseeFee(new BigDecimal("3.5"));
			PricingServices.getPurchasablePermissionFromRLItemRequest(purchasablePermission,  itemRequestResponse.getItemRequest());
			
			ItemParm offerItemParm = new ItemParm();
			offerItemParm.setParmName(ItemParmEnum.OFFERCHANNEL.name());
			offerItemParm.setDataTypeCd(ItemConstants.STRING);
			offerItemParm.setParmValue(rlForm.getRlInitOrderFlow().getSelectedOfferChannel());
			offerItemParm.setDisplayWidth(0);
			offerItemParm.setFieldLength(0);
			offerItemParm.setLabelOn(false);
			offerItemParm.setRlSourceBeanCd("ORDER");
    		
			purchasablePermission.getItem().getItemParms().put(offerItemParm.getParmName(), offerItemParm); 
								
			String nrlsUserFee = CC2Configuration.getInstance().getNrlsUserFeeValue();
								
			if ( purchasablePermission.getItem().getItemParms().get(ItemParmEnum.OFFERCHANNEL.name()) != null
					&& purchasablePermission.getItem().getItemParms().get(ItemParmEnum.OFFERCHANNEL.name()).getParmValue().equalsIgnoreCase(RlnkConstants.PUB_TO_PUB)
					&& (!UserContextService.isSuppressNRLSFee()) 
					&& purchasablePermission.getItem().getTotalPrice() != null 
					&& purchasablePermission.getItem().getTotalPrice().compareTo(BigDecimal.ZERO) > 0){
				purchasablePermission.getItem().setTotalPrice(purchasablePermission.getItem().getTotalPrice().add(new BigDecimal(nrlsUserFee)));
				purchasablePermission.setLicenseeFee(new Double(nrlsUserFee));
			} 
			else if ( purchasablePermission.getItem().getItemParms().get(ItemParmEnum.OFFERCHANNEL.name()) != null
					&& purchasablePermission.getItem().getItemParms().get(ItemParmEnum.OFFERCHANNEL.name()).getParmValue().equalsIgnoreCase(RlnkConstants.PUB_TO_PUB)
					&& (!UserContextService.isSuppressNRLSFee()) 
					&& purchasablePermission.getItem().getTotalPrice() != null 
					&& purchasablePermission.getItem().getTotalPrice().compareTo(BigDecimal.valueOf(ItemConstants.RL_NOT_PRICED))==0){
				
				ItemParm ppItemParm = new ItemParm();
	    		ppItemParm.setParmName(ItemParmEnum.LICENSEEFEEIFAPPLICABLE.name());
	    		ppItemParm.setDataTypeCd(ItemConstants.STRING);
	    		ppItemParm.setParmValue(nrlsUserFee);
	    		ppItemParm.setDisplayWidth(0);
	    		ppItemParm.setFieldLength(0);
	    		ppItemParm.setLabelOn(false);
	    		ppItemParm.setRlSourceBeanCd("ORDER");
	    		
				purchasablePermission.getItem().getItemParms().put(ppItemParm.getParmName(), ppItemParm);
			}
			
			//If Rl Special Order from scratch, pass this parm
			if (rlForm.getRlinkSpecialOrderFromScratch()){
				ItemParm spItemParm = new ItemParm();
				spItemParm.setParmName(ItemParmEnum.NRLS_SO.name());
				spItemParm.setDataTypeCd(ItemConstants.STRING);
				spItemParm.setParmValue(ItemParmEnum.NRLS_SO.name());
				spItemParm.setDisplayWidth(0);
				spItemParm.setFieldLength(0);
				spItemParm.setLabelOn(false);
				spItemParm.setRlSourceBeanCd("ORDER");
				
				purchasablePermission.getItem().getItemParms().put(spItemParm.getParmName(), spItemParm);
				
				purchasablePermission.getItem().getItemParms().get("ORDERCONTENTTITLE").setParmValue(rlForm.getPublicationName());
				purchasablePermission.getItem().getItemParms().get("ORDERCONTENTPUBLISHER").setParmValue(rlForm.getPublisher());						
				purchasablePermission.getItem().getItemParms().get("ORDERCONTENTPUBLICATION").setParmValue("");
				//purchasablePermission.getItem().getItemParms().get("PUBLICATION").setParmValue("");
				purchasablePermission.getItem().getItemParms().get("ORDERCONTENTDATE").setParmValue(rlForm.getPubDate());
					
				    if (rlForm.getAuthor() != null && !rlForm.getAuthor().equals("")) {
				    	if (purchasablePermission.getItem().getItemParms().get("ORDERCONTENTAUTHOR") == null) {
				    		ItemParm spItemParm2 = new ItemParm();
				    		spItemParm2.setParmName("ORDERCONTENTAUTHOR");
				    		spItemParm2.setDataTypeCd(ItemConstants.STRING);
				    		spItemParm2.setParmValue(rlForm.getAuthor());
				    		spItemParm2.setDisplayWidth(0);
				    		spItemParm2.setFieldLength(0);
				    		spItemParm2.setLabelOn(false);
				    		spItemParm2.setRlSourceBeanCd("ORDER");
						
				    		purchasablePermission.getItem().getItemParms().put(spItemParm2.getParmName(), spItemParm2);
				    		
				    	}
				    }
				    
				    if (rlForm.getVolume() != null && !rlForm.getVolume().equals("")) {
				    	if (purchasablePermission.getItem().getItemParms().get("VOLUMENUM") == null) {
				    		ItemParm spItemParm3 = new ItemParm();
				    		spItemParm3.setParmName("VOLUMENUM");
				    		spItemParm3.setDataTypeCd(ItemConstants.STRING);
				    		spItemParm3.setParmValue(rlForm.getVolume());
				    		spItemParm3.setDisplayWidth(0);
				    		spItemParm3.setFieldLength(0);
				    		spItemParm3.setLabelOn(false);
				    		spItemParm3.setRlSourceBeanCd("ORDER");
						
				    		purchasablePermission.getItem().getItemParms().put(spItemParm3.getParmName(), spItemParm3);
				    		purchasablePermission.setGranularWorkVolume(rlForm.getVolume());
				    		
				    	}
				    } 
				    
				    if (rlForm.getEdition() != null && !rlForm.getEdition().equals("")) {
				    	if (purchasablePermission.getItem().getItemParms().get("ISSUENUM") == null) {
				    		ItemParm spItemParm4 = new ItemParm();
				    		spItemParm4.setParmName("ISSUENUM");
				    		spItemParm4.setDataTypeCd(ItemConstants.STRING);
				    		spItemParm4.setParmValue(rlForm.getEdition());
				    		spItemParm4.setDisplayWidth(0);
				    		spItemParm4.setFieldLength(0);
				    		spItemParm4.setLabelOn(false);
				    		spItemParm4.setRlSourceBeanCd("ORDER");
						
				    		purchasablePermission.getItem().getItemParms().put(spItemParm4.getParmName(), spItemParm4);
				    		purchasablePermission.setGranularWorkIssue(rlForm.getEdition());
				    		
				    	}
				    } 
					
			}
			
			// update po number to licensee_ref_numner fix     * CC-2315
			ItemRequestParm itemRequestParm = itemRequestResponse.getItemRequest().getItemRequestParms().get(RlnkParmConstants.PARM_NAME_PONUMBER.toUpperCase());
			purchasablePermission.setYourReference(StringUtils.EMPTY);
			if(itemRequestParm!=null){
			purchasablePermission.setYourReference(	itemRequestParm.getParmValueString());
			}
			
			try{
				// Item is ready to add to the cart  
				
				
				if(rlForm.getIsUpdateableCartItem()){
					CartServices.updateRightslnkCart(purchasablePermission);	
					UserContextService.setCartItemEdited(true);
					//update the form with the updated purchasablePermission
					rlForm.setPurchasablePermission(CartServices.getPurchasablePermissionInCart(CartServices.getCart(), purchasablePermission.getItem().getItemId()));
				}else{
					CartServices.addRightslnkItemToCart( purchasablePermission );
				}

				forward = mapping.findForward( "submit" );
			} 
			catch (CannotBeAddedToCartException e)
			{
				rlForm.getErrors().add(TransactionUtils.getIncorrectCartItemTypeError());
				saveErrors( request, rlForm.getErrors() );
				forward = mapping.findForward( SHOW_MAIN );
			} 
			catch(Exception e){
				throw new CCRuntimeException(e);
			}
		}	
		return forward;
	}
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward editCartItem(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response){
		RightsLinkQuickPriceActionForm rlForm = castForm( RightsLinkQuickPriceActionForm.class, form );
		
		if(UserContextService.getSearchState()==null ){
			rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.rightslnk.down.error"));
			LOGGER.info("Exception : Search State is null.");
			return mapping.findForward(SHOW_ERROR );
		}
		
		rlForm.setQuickPriceErrorMessage(null);
		rlForm.setReadyForCart(false);
		request.getSession().setAttribute(RlnkSessionConstants.RL_REQUEST_PARM_IN_SESSION,null);
		rlForm.setUpdateableCartItem(true);
		rlForm.setShowGetPriceButton(false);
		request.getSession().setAttribute("rlForm", rlForm);
		return mapping.findForward(populateTransactionItem(response, request, rlForm ));

	}
	/**
	 * 
	 * @param response
	 * @param request
	 * @param rightsLinkQuickPriceActionForm
	 */
	private String  populateTransactionItem( HttpServletResponse response, HttpServletRequest request, RightsLinkQuickPriceActionForm rlForm )
	{
		BootstrapRequest bootstrapRequest	=	new BootstrapRequest();
		Publication  work					=	null;
		Publication  subWork					=	null;
		ItemRequest itemRequest			=	null;
		RightResponse rightResponse 		= 	null;

		TransactionItem transactionItem 	= (TransactionItem)request.getAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM );
		PurchasablePermission  purchasablePermission = (PurchasablePermission)transactionItem;
		rlForm.setPurchasablePermission(purchasablePermission);

		// work
		work = RightsLnkQPriceUtil.getPublication(transactionItem.getWrWorkInst());
		UserContextService.getSearchState().setSelectedWork(((WorkRightsAdapter)work).getWork());

		// subwork
		if(transactionItem.getItemSubSourceKey()!=null){
			subWork = RightsLnkQPriceUtil.getPublication(transactionItem.getItemSubSourceKey());
			ArticleSearchState articleSearchState = new ArticleSearchState();
			UserContextService.setArticleSearchState(articleSearchState); 
			UserContextService.getArticleSearchState().setSelectedSubWork(((WorkRightsAdapter)subWork).getWork());

			WorkExternal subWorkExternal=UserContextService.getArticleSearchState().getSelectedSubWork();

			if(getSubWorkPublicationType(subWorkExternal).compareToIgnoreCase(RlnkConstants.PUB_TYPE_ARTICLE)==0){
				rlForm.setSelectedArticle(new ArticleDisplay(subWorkExternal));
			}else if(getSubWorkPublicationType(subWorkExternal).compareToIgnoreCase(RlnkConstants.PUB_TYPE_CHAPTER)==0){
				rlForm.setSelectedChapter(new ChapterDisplay(subWorkExternal));

			}
			rlForm.setSubWork(UserContextService.getArticleSearchState().getSelectedSubWork());
		}else{
			rlForm.setSubWork(null);
		}

		// repopulate
		UserContextService.getSearchState().setSelectedTouId(purchasablePermission.getExternalTouId());
		UserContextService.getSearchState().setSelectedRrTouId(purchasablePermission.getTouSourceKey());
		UserContextService.getSearchState().setSelectedCategoryId(purchasablePermission.getCategoryId());
		UserContextService.getSearchState().setSelectedPermissionType(purchasablePermission.getCategoryName());
		UserContextService.getSearchState().setSelectedTou(purchasablePermission.getTouName());

		//repopulate form fields
		rlForm.setSelectedPubItem(work);
		
		if (rlForm.getRlinkSpecialOrderFromScratch()) {
			WorkExternal work2 = ((WorkRightsAdapter)rlForm.getSelectedPubItem()).getWork();
			rlForm.setWork(work2);
		}
		else {
			rlForm.setWork(UserContextService.getSearchState().getSelectedWork());
		}
		
		rlForm.getRlInitOrderFlow().setRlOfferID(UserContextService.getSearchState().getSelectedTouId().toString());
		rlForm.setSelectedCategory(UserContextService.getSearchState().getSelectedPermissionType());
		rlForm.setSelectedTou(UserContextService.getSearchState().getSelectedTou());

		// save IteId for Cart update
		rlForm.setItemId(purchasablePermission.getItem().getItemId());
		itemRequest=PricingServices.getRLItemRequestFromPurchasablePermission(purchasablePermission);

		// temp fix 
		if(itemRequest.getItemRequestParms().get(RlnkParmConstants.PARM_NAME_TARGET_PAGE.toUpperCase())!=null)
			itemRequest.getItemRequestParms().get(RlnkParmConstants.PARM_NAME_TARGET_PAGE.toUpperCase()).setParmValueString(RlnkParmConstants.PARM_VALUE_QUICKPRICE);

		bootstrapRequest.setItemRequest(itemRequest);
		bootstrapRequest.setSessionID(RlnkRightsServices.getRlnkSessionID());
		bootstrapRequest.setFormName("rightsLinkQuickPriceActionForm");
		if(!SystemStatus.isRightslinkUp()){
			rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.rightslnk.down.error"));
			LOGGER.info("Rights link service is down");
			return SHOW_ERROR;
		}else{

			rightResponse=RlnkRightsServices.editOrder(bootstrapRequest);
			if (rightResponse.isExceptionOccurred())
			{
				rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.pagenotfound.exception.error"));
				LOGGER.error(ExceptionUtils.getFullStackTrace(rightResponse.getThrowable()));
				return SHOW_ERROR;
			}
			else{
				generateHTMLScreen( request, response, rlForm, rightResponse );
			}
		}

		return SHOW_MAIN;
	}

	/**
	 * 
	 * @param rLArticleQuickPriceActionForm
	 * @param request
	 * @param init
	 */
	private boolean setUpWorkDetails(RightsLinkQuickPriceActionForm rlForm, HttpServletRequest request){
		//read work details
		// get work
		Publication  pub=null;
		boolean setUpComplete=true;
		WorkExternal subWork=null;
		if(UserContextService.getSearchState()==null ||UserContextService.getSearchState().getSelectedWork()==null ){
			rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("usercontext.publisher.empty.error"));
			return false;
		}

		pub = RightsLnkQPriceUtil.getPublication( UserContextService.getSearchState().getSelectedWork().getPrimaryKey());
 
		if (rlForm.getRlinkSpecialOrderFromScratch()) {
			((WorkRightsAdapter)pub).getWork().setAuthorName(rlForm.getAuthor());
			((WorkRightsAdapter)pub).getWork().setMainTitle(rlForm.getPublicationName());
			((WorkRightsAdapter)pub).getWork().setFullTitle(rlForm.getPublicationName());
			((WorkRightsAdapter)pub).getWork().setPublisherName(rlForm.getPublisher());
			
			if (rlForm.getIssn() != null && rlForm.getIssn() != "") {
				((WorkRightsAdapter)pub).getWork().setIdno(rlForm.getIssn());
			}
			((WorkRightsAdapter)pub).getWork().setRights(null);
			((WorkRightsAdapter)pub).getWork().setEdition(rlForm.getEdition());
			((WorkRightsAdapter)pub).getWork().setVolume(rlForm.getVolume());
			((WorkRightsAdapter)pub).getWork().setRunPubStartDate(Long.getLong(rlForm.getPubDate()));
			((WorkRightsAdapter)pub).getWork().setRunPubEndDate(Long.getLong(rlForm.getPubDate()));
			((WorkRightsAdapter)pub).getWork().setLanguage(null);
			((WorkRightsAdapter)pub).getWork().setCountry(null);
			((WorkRightsAdapter)pub).getWork().setPublicationType(null);
			
			if (((WorkRightsAdapter)pub).getAdaptedRights() != null) {
				if (((WorkRightsAdapter)pub).getAdaptedRights().size() > 0) {
					if (((WorkRightsAdapter)pub).getAdaptedRights().get(0) != null) {
						if (((WorkRightsAdapter)pub).getAdaptedRights().get(0).getRRRight() != null) {
							((WorkRightsAdapter)pub).getAdaptedRights().get(0).getRRRight().setRightsholderName("");
						}
					}
				}
			}
		}
		
		//pub = new WorkRightsAdapter(UserContextService.getSearchState().getSelectedWork(), false);
		rlForm.setSelectedPubItem(pub);
		rlForm.setSelectedPubItemWrkInst(pub.getWrkInst());
		rlForm.setSelectedChapter(null);
		rlForm.setSelectedArticle(null);
		if(UserContextService.getArticleSearchState()!=null && UserContextService.getArticleSearchState().getSelectedSubWork()!=null){
			subWork=UserContextService.getArticleSearchState().getSelectedSubWork();
			if(getSubWorkPublicationType(subWork).compareToIgnoreCase(RlnkConstants.PUB_TYPE_ARTICLE)==0){
				rlForm.setSelectedArticle(new ArticleDisplay(subWork));

			}else if(getSubWorkPublicationType(subWork).compareToIgnoreCase(RlnkConstants.PUB_TYPE_CHAPTER)==0){
				rlForm.setSelectedChapter(new ChapterDisplay(subWork));
			}else{
				UserContextService.getArticleSearchState().setSelectedSubWork(null);
			}
			rlForm.setSubWork(UserContextService.getArticleSearchState().getSelectedSubWork());
		}else{
			rlForm.setSubWork(null);
		}
		
		if (rlForm.getRlinkSpecialOrderFromScratch()) {
			WorkExternal work = ((WorkRightsAdapter)rlForm.getSelectedPubItem()).getWork();
			rlForm.setWork(work);
		}
		else {
			rlForm.setWork(UserContextService.getSearchState().getSelectedWork());
		}	

		rlForm.getRlInitOrderFlow().setRlOfferID(UserContextService.getSearchState().getSelectedTouId().toString());
		rlForm.setSelectedCategory(UserContextService.getSearchState().getSelectedPermissionType());
		rlForm.setSelectedTou(UserContextService.getSearchState().getSelectedTou());


		return setUpComplete;
	}


	/**
	 * This method will "retry" the runRightResponse method up to
	 * {@link CC2Configuration#getRightslinkRequestMaxRetries} times.
	 * Between each try this method will sleep for {@link CC2Configuration#getRightslinkRequestRetrySleepMillis}
	 * It will continue to retry under the following conditions:
	 * 
	 * <li>The number of tries performed <= {@link CC2Configuration#getRightslinkRequestMaxRetries}
	 * <li>The most recent RightResponse.hasErrors==true
	 */
	private RightResponse runRightResponseWithRetry(ActionMapping mapping,HttpServletRequest request,HttpServletResponse response,RightsLinkQuickPriceActionForm rlForm)
	{
		RightResponse retriedRightResponse = null;
		int maxRetries = MAX_RETRIES;
		long millisBetweenTries = MILLIS_BETWEEN_RETRIES;
		boolean hasErrors = true;

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("runRightResponseWithRetry will try up to " + maxRetries + " times; sleeping between " + millisBetweenTries + " millis");
		}
		for(int tries=1; tries<=maxRetries && hasErrors; tries++) 
		{
			retriedRightResponse = runRightResponse(mapping,request,response,rlForm,false,true);
			if (retriedRightResponse!=null) {
				hasErrors=retriedRightResponse.hasErrors();
				if (hasErrors) {
					try {
						Thread.sleep(millisBetweenTries);
					} catch (InterruptedException ie) {
						throw new CCRuntimeException(ie);
					}
				}
			}
			tries++;
		}

		return retriedRightResponse;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param rLArticleQuickPriceActionForm
	 * @param init
	 */
	private RightResponse  runRightResponse(ActionMapping mapping,HttpServletRequest request,HttpServletResponse response,RightsLinkQuickPriceActionForm rlForm, boolean initialize,boolean readFromSession){

		String 				formName	    =	"rightsLinkQuickPriceActionForm";
		WorkExternal 		work		    =	rlForm.getWork();
		WorkExternal 		subWork		    =	rlForm.getSubWork();
		RlInitOrderFlow 	rlInitOrderFlow	= rlForm.getRlInitOrderFlow();

		RightResponse rightResponse = null;
		// init
		//createActionErrors(request,null);
		//request.setAttribute("HTML_SCREEN_RESPONSE", null);
		rlForm.setHtmlScreenResponse(null);
		if (initialize) {
			rlForm.setReadyForCart(false);
			rightResponse=getInitialRightResponse( request, rlForm ,formName, work,subWork, rlInitOrderFlow);
		}else{
			rightResponse=getOrderFlowRightResponse( request,rlForm,formName,readFromSession);
		}
		
		if (!StringUtils2.isNullOrEmpty(rightResponse.getTerms())) 
		{
			rlForm.setTerms(rightResponse.getTerms());
		}
		
		if( rightsLinkLoginAttemptedWithError(request, rightResponse,rlForm)){
			rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.pagenotfound.exception.error"));
			return null;

		}else if (rightResponse.isExceptionOccurred()){
			LOGGER.error(ExceptionUtils.getFullStackTrace(rightResponse.getThrowable()));
			if (initialize) {
				rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.quick.price.failed.error"));
			}else{
				rlForm.setQuickPriceErrorMessage(getResources(request).getMessage("rightslink.pagenotfound.exception.error"));
			}
			return null;
		}
		//return isSuccess;
		return rightResponse;

	}

	/**
	 * 
	 * @param request
	 * @param formName
	 * @param work
	 * @param subWork
	 * @param rlInitOrderFlow
	 */
	private RightResponse getInitialRightResponse(HttpServletRequest request,RightsLinkQuickPriceActionForm rlForm,String formName,WorkExternal work,WorkExternal subWork, RlInitOrderFlow 	rlInitOrderFlow){
		RightResponse rightResponse=null;
		Integer selectedPubYear=CCAction.getSelectedPubYear(request);
		
		String selectedOfferChannel = CCAction.getSelectedOfferChannel(request);
		
		if (StringUtils2.isNullOrEmpty(selectedOfferChannel)) {
			
			selectedOfferChannel = UserContextService.getSearchState().getSelectedOfferChannel();
		}
		rlInitOrderFlow.setSelectedOfferChannel(selectedOfferChannel);
		
		String selectedRlPermissionType = CCAction.getSelectedRlPermissionType(request);
		rlInitOrderFlow.setSelectedRlPermissionType(selectedRlPermissionType);
		
		String selectedRlPubCode = CCAction.getSelectedRlPubCode(request);
		rlInitOrderFlow.setSelectedRlPubCode(selectedRlPubCode);
		
		
		
		rightResponse = RlnkRightsServices.runRightRequest(formName,work,subWork,rlInitOrderFlow,selectedPubYear);
		return rightResponse;
	}

	/**
	 * 
	 * @param request
	 * @param formName
	 */

	private RightResponse getOrderFlowRightResponse(HttpServletRequest request,RightsLinkQuickPriceActionForm rlForm,String formName,boolean readFromSession){
		RightResponse rightResponse=null;

		if(!readFromSession){
			rlForm.setRequestParameters(new HashMap<String, String>());
			@SuppressWarnings("unchecked")
			Enumeration<String> keys = request.getParameterNames();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = request.getParameter(key);
				//if(key.compareToIgnoreCase("termsandconditions")!=0){
				rlForm.getRequestParameters().put(key,value);
				//}
			}
		}
		rightResponse = RlnkRightsServices.runRightRequest(formName, rlForm.getRequestParameters());

		return rightResponse;
	}



	/**
	 * 	
	 * @param request
	 * @param response
	 * @param rlForm
	 * @param rightResponse
	 */
	protected void generateHTMLScreen(HttpServletRequest request,HttpServletResponse response,RightsLinkQuickPriceActionForm rlForm,RightResponse rightResponse ){

		// price can  be -9999999 when ther eis error or no error  mean price has not been calculated at all
		// price  can be -62543.89 that means it is special order... also mean price has been calculated
		rlForm.setRightResponse(rightResponse);
		//	rightResponse.getTotalPrice()
		rlForm.setIsPriceAvailable(false);
		rlForm.setShowUpdatePriceButton(false);

		if(rightResponse.getTotalPrice().compareTo(BigDecimal.valueOf(-1))>0){ 
			rlForm.setIsPriceAvailable(true);
		}else if(rightResponse.getTotalPrice().compareTo(BigDecimal.valueOf(ItemConstants.RL_NOT_PRICED))==0){ // negative number is special order
			rlForm.setIsPriceAvailable(true);
		}else if(rightResponse.getTotalPrice().compareTo(BigDecimal.valueOf(0))==0){
			rlForm.setIsPriceAvailable(true);
		}

		if(rightResponse.getCurrentTargetPage()!=null && rightResponse.getCurrentTargetPage().equalsIgnoreCase(RlnkConstants.RlnkParmConstants.PARM_VALUE_QUICKPRICE) ){
			if(rlForm.getIsPriceAvailable()){ // or special order (-1)
				if(rlForm.isShowGetPriceButton()){
					rlForm.setShowGetPriceButton(false);
				}	
			}
			if(!rlForm.isShowGetPriceButton()){
				rlForm.setShowUpdatePriceButton(true);
			}

		}
		
		String nrlsUserFee = CC2Configuration.getInstance().getNrlsUserFeeValue();
		
		if (rlForm.getRlInitOrderFlow().getSelectedOfferChannel().equalsIgnoreCase(RlnkConstants.PUB_TO_PUB)
				&& (!UserContextService.isSuppressNRLSFee()) 
				&& rlForm.getTotalPrice() != null 
				&& rightResponse.getTotalPrice().compareTo(BigDecimal.ZERO) > 0) {
			
			rightResponse.setTotalPrice(rightResponse.getTotalPrice().add(new BigDecimal(nrlsUserFee)));
		}
		rlForm.setDisplayPrice(getDisplayPrice(rightResponse.getTotalPrice(),rlForm.isShowUpdatePriceButton()));

		HtmlScreenRequest htmlScreenRequest = new HtmlScreenRequest();
		htmlScreenRequest.setPublisherID(rightResponse.getPublisherID());
		htmlScreenRequest.setRows(rightResponse.getScreenRows());
		htmlScreenRequest.setHasErrors(rightResponse.hasErrors());
		htmlScreenRequest.setErrorList(rightResponse.getErrorList());
		htmlScreenRequest.setTargetPage(rightResponse.getTargetPage());
		htmlScreenRequest.setCurrentTargetPage(rightResponse.getCurrentTargetPage());
		htmlScreenRequest.setScriptData(rightResponse.getScriptData());
		Map<String, String> hiddenVariables = rightResponse.getHiddenVariables();
		htmlScreenRequest.setHiddenVariables(hiddenVariables);

		HtmlScreenBuilder htmlScreenBuilder = new HtmlScreenBuilder();
		HtmlScreenResponse htmlScreenResponse = null;
		try {
			htmlScreenResponse = htmlScreenBuilder.execute(htmlScreenRequest);
			
			if (htmlScreenResponse != null && htmlScreenResponse.getScreenHtml()!=null) {
				String screenHTML = htmlScreenResponse.getScreenHtml();
				//Pattern btnPathPattern=Pattern.compile("(/App/Images/>)");

				if(rightResponse.isOrderFlowEnd() &&  rightResponse.getTargetPage().equalsIgnoreCase(RlnkConstants.RlnkParmConstants.PARM_VALUE_CONFIRM_ORDER)){
					screenHTML=replaceRightsLinkButtons( rlForm,screenHTML,true,rlForm.getIsUpdateableCartItem());	
					//rlForm.setReadyForCart(true);
				}
				else{
					screenHTML=replaceRightsLinkButtons( rlForm,screenHTML,false,false);
					//rlForm.setReadyForCart(false);
				}
				//
				// replace all helper images
				if(rlForm.isShowGetPriceButton()){
					screenHTML = screenHTML.replaceAll("Click Quick Price", "Click Get Price");
				}
				
				//Add 'Please Wait' message for certain javascript functions in Pricing page
				String scriptData = htmlScreenResponse.getScriptDataHtml();
				
				String callName = "offerChange(permissionChoice, targetValue)";
				scriptData = insertWaitMessage(scriptData, callName, 2);
				
				callName = "quickPrice(permissionChoice, targetValue, doCalc)";
				scriptData = insertWaitMessage(scriptData, callName, 2);
				
				callName = "placeOrder()";
				scriptData = insertWaitMessage(scriptData, callName, 2);
				
				callName = "continueOrder()";
				scriptData = insertWaitMessage(scriptData, callName, 2);
				
				htmlScreenResponse.setScriptDataHtml(scriptData);
				
				htmlScreenResponse.setScreenHtml(screenHTML);

			}

		} catch (TypeCodeException e) {
			throw new CCRuntimeException(e);
		}
		//request.setAttribute("HTML_SCREEN_RESPONSE", htmlScreenResponse);
		rlForm.setHtmlScreenResponse(htmlScreenResponse);
		if(htmlScreenResponse!=null){
			rlForm.setPreviousHiddenvariables(htmlScreenResponse.getHiddenVariableHtml());	
		}

	}
	
	protected String insertWaitMessage(String scriptData, String callName, int offset){
		
		int i = scriptData.indexOf(callName);
		
		if (i > 0) {
			String scriptDataBegin = scriptData.substring(0, i + callName.length() + offset);
			String scriptDataEnd = scriptData.substring(i + callName.length() + offset + 1, scriptData.length());
			//String scriptDataMiddle =  "\n\t" + "$(waiting_for_search).show();" + "\n\t";
			String scriptDataMiddle =  "\n\t" + "document.getElementById('waiting_for_search').style.display = '';" + "\n\t";
			scriptData = scriptDataBegin + scriptDataMiddle + scriptDataEnd ;
		}
		
		return scriptData;
	}

	/**
	 * 
	 * @param screenHTML
	 * @param showAddToCart
	 */
	protected String replaceRightsLinkButtons(RightsLinkQuickPriceActionForm rlForm,String screenHTML,boolean showAddToCart,boolean oldCart){

		// override javascript method
		screenHTML=screenHTML.replaceAll("updateShippingMethods\\(\\)\\;","onBeforeUpdateShippingMethods();");

		screenHTML=screenHTML.replaceAll("/App/Images/fedexlogo.gif", "/resources/commerce/images/fedexlogo.gif");
		screenHTML=screenHTML.replaceAll("/App/Images/un_global_upslogo.gif", "/resources/commerce/images/un_global_upslogo.gif");

		screenHTML=screenHTML.replaceAll("/App/Images/helpmark.gif", "/resources/commerce/images/helpmark.gif");
		// added this bcoz of bug in RL html screen
		screenHTML=screenHTML.replaceAll("App/Images/helpmark.gif", "/resources/commerce/images/helpmark.gif");




		// pattern to replace get price button
		Pattern pattern=Pattern.compile("(/\\w*/Images/)");
		Matcher matcher = pattern.matcher(screenHTML);
		screenHTML = matcher.replaceAll("/media/images/");




		//screenHTML=screenHTML.replaceAll("helpmark.gif", "spacer.gif");
		screenHTML=screenHTML.replaceAll("main_closewindow_over.gif", "spacer.gif");
		screenHTML=screenHTML.replaceAll("main_closewindow.gif", "spacer.gif");
		screenHTML=screenHTML.replaceAll("main_closewindow.gif", "spacer.gif");
		screenHTML=screenHTML.replaceAll("main_ordermore_over.gif", "spacer.gif");
		screenHTML=screenHTML.replaceAll("main_ordermore.gif", "spacer.gif");
		
		//screenHTML=screenHTML.replaceAll("btn_back.gif", "spacer.gif");
		//screenHTML=screenHTML.replaceAll("/media/images/btn_back.gif", "/resources/commerce/images/btn_back.gif");
		//screenHTML=screenHTML.replaceAll("/media/images/main_back.gif", "/resources/commerce/images/btn_back.gif");


		//if(!rlForm.getIsPriceAvailable()){
		if(rlForm.isShowGetPriceButton()){
			screenHTML=screenHTML.replaceAll("main_quickprice.gif", "btn_get_price.gif");
			screenHTML=screenHTML.replaceAll("main_quickprice_over.gif", "btn_get_price.gif");
			screenHTML=screenHTML.replaceAll("main_continue_over.gif", "spacer.gif");
			screenHTML=screenHTML.replaceAll("main_continue.gif", "spacer.gif");
		}else{
			screenHTML=screenHTML.replaceAll("main_quickprice.gif", "spacer.gif");
			screenHTML=screenHTML.replaceAll("main_quickprice_over.gif", "spacer.gif");
		}


		if(showAddToCart){
			//screenHTML=screenHTML.replaceAll("main_continue_over.gif", "btn_add-cart.gif");
			//screenHTML=screenHTML.replaceAll("main_continue.gif", "btn_add-cart.gif");
			if(!oldCart){
				screenHTML=screenHTML.replaceAll("main_continue_over.gif", "btn_add-cart.gif");
				screenHTML=screenHTML.replaceAll("main_continue.gif", "btn_add-cart.gif");


			}else{
				screenHTML=screenHTML.replaceAll("main_continue_over.gif", "btn_save-changes.gif");
				screenHTML=screenHTML.replaceAll("main_continue.gif", "btn_save-changes.gif");	

			}
			screenHTML=screenHTML.replaceAll("javascript:continueOrder\\(\\)\\;", "javascript:onBeforeAddToCart();");
		}else{
			screenHTML=screenHTML.replaceAll("main_continue_over.gif", "btn_continue.gif");
			screenHTML=screenHTML.replaceAll("main_continue.gif", "btn_continue.gif");	
		}

		screenHTML=screenHTML.replaceAll("/media/images/main_back_over.gif", "/resources/commerce/images/btn_back.gif");
		screenHTML=screenHTML.replaceAll("/media/images/main_back.gif", "/resources/commerce/images/btn_back.gif");
		screenHTML=screenHTML.replaceAll("javascript:history.back", "javascript:goBack");



		screenHTML=screenHTML.replaceAll("main_loginb_over.gif", "submit_button.gif");
		screenHTML=screenHTML.replaceAll("main_loginb.gif", "submit_button.gif");

		screenHTML=screenHTML.replaceAll("main_SUBMIT-REQUEST_button_.gif", "submit_button.gif");
		screenHTML=screenHTML.replaceAll("main_SUBMIT-REQUEST_over.gif", "submit_button.gif");
		
		
		
		
		screenHTML=screenHTML.replaceAll("/media/images/main_update_over.gif", "/resources/commerce/images/btn_updateaddress.gif");
		screenHTML=screenHTML.replaceAll("/media/images/main_update.gif", "/resources/commerce/images/btn_updateaddress.gif");

		screenHTML=screenHTML.replaceAll("/media/images/new_work_over.gif", "/resources/commerce/images/btn_newwork.gif");
		screenHTML=screenHTML.replaceAll("/media/images/new_work_mainbutton.gif", "/resources/commerce/images/btn_newwork.gif");

		// get the button section and create separate section for buttons
		//<td class="DefaultButtons"
		String buttonHTMLS = null;
		Pattern buttonPattern=Pattern.compile("(\\<td .* class=\\\"DefaultButtons.*\\<\\/td\\>)");
		Matcher buttonMatcher = buttonPattern.matcher(screenHTML);
		while (buttonMatcher.find()) {
			buttonHTMLS = buttonMatcher.group(1);
		}
		rlForm.setRightsLinkButtonHTML(buttonHTMLS);
		if(!StringUtils.isEmpty(buttonHTMLS)){
			screenHTML = buttonMatcher.replaceAll("");
		}
		return screenHTML; 
	}




	/**
	 * 
	 * @param purchasablePermission
	 * @param subWork
	 */
	private  void addArticleInforPurchasePermission(PurchasablePermission purchasablePermission,WorkExternal subWork){
		//populate article details 

		purchasablePermission.setItemSubDescription(subWork.getFullTitle());
		purchasablePermission.setItemSubSourceKey(subWork.getPrimaryKey());
		purchasablePermission.setItemSubSourceCd(subWork.getProviderKey());
		purchasablePermission.setItemSourceCd(ItemConstants.ITEM_SOURCE_CD_WR);
		purchasablePermission.setGranularWorkAuthor(subWork.getAuthorName());

		if(subWork.getRunPubStartDate()!=null){
			purchasablePermission.setGranularWorkPublicationDate(getRunPubStartDate(subWork.getRunPubStartDate()));
		}else{
			purchasablePermission.setGranularWorkPublicationDate(null);
		}

		if(subWork.getIdnoTypeCode()!=null && 
				subWork.getIdnoTypeCode().compareTo(IdnoTypeCodeValues.DOI)==0){
			purchasablePermission.setGranularWorkDoi(subWork.getIdno());
		}
		purchasablePermission.setGranularWorkStartPage(subWork.getItemStartPage());
		purchasablePermission.setGranularWorkEndPage(subWork.getItemEndPage());
		purchasablePermission.setGranularWorkPageRange(subWork.getItemPageRange());
		//purchasablePermission.setGranularWorkPublicationDate(granularWorkPublicationDate);
		purchasablePermission.setGranularWorkVolume(subWork.getItemVolume());
		purchasablePermission.setGranularWorkIssue(subWork.getItemIssue());
		purchasablePermission.setGranularWorkNumber(subWork.getItemNumber());
		purchasablePermission.setGranularWorkSeason(subWork.getItemSeason());
		purchasablePermission.setGranularWorkQuarter(subWork.getItemQuarter());
		purchasablePermission.setGranularWorkWeek(subWork.getItemWeek());
		purchasablePermission.setGranularWorkSection(subWork.getItemSection());
	}
	/**
	 * 
	 * @param longPubDate
	 */
	private  Date getRunPubStartDate(Long longPubDate) {
		String date = (longPubDate == null) ? "" : longPubDate.toString();
		if (date.trim().length() !=8) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		String strYear = date.substring(0, 4);
		String strMonth = date.substring(4,6);
		String strDay = date.substring(6);
		calendar.set(Integer.parseInt(strYear),Integer.parseInt(strMonth)-1,Integer.parseInt(strDay));
		return calendar.getTime();	
	}

	/**
	 * 
	 * @param rightResponse
	 * @param rightsLinkQuickPriceActionForm
	 */
	private  boolean showAddToCart(RightResponse rightResponse,RightsLinkQuickPriceActionForm rightsLinkQuickPriceActionForm){
		if(!rightResponse.hasErrors()  &&  rightsLinkQuickPriceActionForm.isReadyForCart()){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param request
	 * @param rightResponse
	 */
	private boolean showLoginScreen(HttpServletRequest request,RightResponse rightResponse){
		if(!rightResponse.isExceptionOccurred() &&  !rightResponse.hasErrors() &&  rightResponse.isLogInRequired()){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param request
	 * @param rightResponse
	 */
	private boolean rightsLinkLoginAttemptedWithError(HttpServletRequest request,RightResponse rightResponse,RightsLinkQuickPriceActionForm rlForm){
		// if log in is required-> Login and come back
		if(rightResponse.isLogInRequired()){
			// after log in request parms will have loginId and password ,
			//if log in is still required.. show error message
			if(rlForm.getRequestParameters().containsKey("loginID") && rlForm.getRequestParameters().containsKey("password")){
				return true;
			}
		}
		return false;
	}
	/**
	 * Used for having pub type=null
	 * @param WorkExternal
	 * @return String
	 */
	private String getSubWorkPublicationType(WorkExternal subWork){
		//PUBGET, NATURE, or NYT 
		if(subWork.getPublicationType()==null){
			if(subWork.getProviderKey()!=null && 
					(subWork.getProviderKey().equalsIgnoreCase("PUBGET") ||
							subWork.getProviderKey().equalsIgnoreCase("NATURE") ||
							subWork.getProviderKey().equalsIgnoreCase("NYT"))){
				return RlnkConstants.PUB_TYPE_ARTICLE;	
			}
		}else{
			return subWork.getPublicationType();
		}
		return "";

	}
	/**
	 * 
	 * @param totalPrice
	 * @return
	 */
	private String getDisplayPrice(BigDecimal totalPrice,boolean isUpdatedPrice){
		if(totalPrice.compareTo(BigDecimal.valueOf(-1))>0){ 
			return "$" + totalPrice.setScale(2).toString();
		}else if(totalPrice.compareTo(BigDecimal.valueOf(ItemConstants.RL_NOT_PRICED))==0){ // negative number is special order
			return "$TBD";
		}else if(totalPrice.compareTo(BigDecimal.valueOf(0))==0){
			return totalPrice.setScale(2).toString();
		}else if(isUpdatedPrice){
			return "$TBD";
		}
		return StringUtils.EMPTY;
	}




}
