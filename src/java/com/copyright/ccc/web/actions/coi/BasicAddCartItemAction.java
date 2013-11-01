package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.data.WRStandardWork;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.CannotBeAddedToCartException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.ChangedToRegularOrderException;
import com.copyright.ccc.business.services.cart.ContactRHDirectlyLimitsExceededException;
import com.copyright.ccc.business.services.cart.CourseDetailsImpl;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.DeniedLimitsExceededException;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.SpecialOrderLimitsExceededException;
import com.copyright.ccc.business.services.cart.SystemLimitsExceededException;
import com.copyright.ccc.business.services.search.WorkRightsAdapter;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.transaction.ContactRHDirectlyUnavailableException;
import com.copyright.ccc.web.transaction.DeniedUnavailableException;
import com.copyright.ccc.web.transaction.OutsideBiactiveDateRangeException;
import com.copyright.ccc.web.transaction.SpecialOrderUnavailableException;
import com.copyright.ccc.web.transaction.coi.TransactionItemTypeOfUseMapper;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;
import com.copyright.data.order.PermissionRequest;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.rights.api.data.RightsConsumerContext;


public class BasicAddCartItemAction extends BasicTransactionAction
{
	private static final String COURSE_DETAILS = "courseDetails";

	private static final String CANCEL_PATH = "/search.do?operation=show&page=last";


	//protected static final Logger _logger = Logger.getLogger( BasicAddCartItemAction.class );

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
			int scrollPage, String returnTab, boolean searchFlag) 
	throws InvalidAttributesException, DeniedLimitsExceededException, SpecialOrderLimitsExceededException,
	ContactRHDirectlyLimitsExceededException, ChangedToRegularOrderException, ItemCannotBePurchasedException
	{
		PurchasablePermission purchasablePermission = (PurchasablePermission)transactionItem;

		ActionForward forward = null;
		//TODO: aPPAI need to talk with Paul Shomo about this
		//first check if we need to push to TF
		PermissionRequest permissionRequest = PricingServices.getPermissionRequestFromTransactionItem(
				purchasablePermission);

		WRStandardWork wrStandardWork = (WRStandardWork)permissionRequest.getWork();

		//WRStandardWork wrStandardWork = (WRStandardWork)purchasablePermission.g
		if (wrStandardWork.getTfWksInstList() != null && wrStandardWork.getTfWksInstList().size() !=0 && 
				wrStandardWork.getWrkInst()==0){
			//push to TF
			//try to push work to TF
			Long tfWrkInst;
			try {
				//technically if the work tk work set inst is null then an exception will be thrown as
				//we are only allowed to push works that have a tf work set inst and a null tf work inst
				//tfWrkInst=service.pushWorkToTF(wrStandardWork.getWrWrkInst());
				WorkExternal workExternal = ((WorkRightsAdapter)purchasablePermission.getRightFromWeb().getPublication()).getWork();
				tfWrkInst = ServiceLocator.getRightsService().pushWorkToTF(new RightsConsumerContext(), wrStandardWork.getWrWrkInst(), workExternal);

				if (tfWrkInst == null) {
					//the pushing the work to TF failed, set the rights to NULL to force a manual special order
					_logger.error("tf push failed for WR WRK INST: " + wrStandardWork.getWrWrkInst()+ " Null return from call.");
					//force special order for now
					//NEED TO PUT CORRECT SPECIAL ORDER ACTION FORWARD HERE
					//return SPECIALORDERFORWARD;
					saveErrors( request, TransactionUtils.getPushToTFFailedError());
					request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM,transactionItem );
					purchasablePermission.getPermissionRequest().setManualSpecialOrder(true);
					purchasablePermission.setPushToTFFailed(true);
					return mapping.findForward(SPECIAL_ORDER);
				}
				else{
					purchasablePermission.getItem().setExternalItemId(tfWrkInst);
					wrStandardWork.setWrkInst(tfWrkInst);
					purchasablePermission.setExternalItemId(tfWrkInst);
				}
			}
			catch (Exception exc) {
				//any exception signifies the pushing the work to TF failed so set the TF to null
				//and set the rights to NULL to force a manual special order
				_logger.error("tf push for failed for WR WRK INST: " + wrStandardWork.getWrWrkInst()+ " Cause: " + exc.toString());
				tfWrkInst = null;
				wrStandardWork.setWrkInst(0); 
				//force special order for now
				//rights = null;
				//return;
				//NEED TO PUT CORRECT SPECIAL ORDER ACTION FORWARD HERE
				//return SPECIALORDERFORWARD;
				saveErrors( request, TransactionUtils.getPushToTFFailedError());
				request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM,transactionItem );
				purchasablePermission.getPermissionRequest().setManualSpecialOrder(true);
				purchasablePermission.setPushToTFFailed(true);
				return mapping.findForward(SPECIAL_ORDER);
			}
		}

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

		//If CourseDetail is already created, need to transfer the BundleId into the purchasablePermission
		if (purchasablePermission.isAcademic() && !CartServices.isCoursePackNotPresentInCart())
		{
			CourseDetailsImpl courseDetailsImpl = (CourseDetailsImpl) CartServices.getCourseDetails();
			//Set the Bundle Id from the CourseDetails on the item before adding to cart
			purchasablePermission.getItem().setBundleId(courseDetailsImpl.getBundle().getBundleId());
		}
		try {
			//CartServices.u        	    	
			CartServices.addItemToCart( purchasablePermission );


			forward = mapping.findForward( SUBMIT );
		} 
		catch (CannotBeAddedToCartException e)
		{
			saveErrors( request, TransactionUtils.getIncorrectCartItemTypeError() );

			forward = mapping.findForward( SHOW_MAIN );
		} 
		catch (CourseNotDefinedException e)
		{
			request.setAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_TO_ADD, purchasablePermission );

			forward = mapping.findForward( COURSE_DETAILS );
		}

		return forward;
	}

	@Override
	protected boolean isFirstAcademicItem(TransactionItem transactionItem)
	{
		//return transactionItem.isAcademic() && CartServices.getCourseDetails() == null;
		return transactionItem.isAcademic() && CartServices.getNumberOfAcademicOrderItemsInCart() == 0;
	}
}
