package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.DisplaySpec;
import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderLicenses;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.RightSourceEnum;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.cart.UnableToBuildPurchasablePermissionException;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.web.WebConstants;

public class CopyOrderAction extends Action {
	protected Logger _logger = Logger.getLogger(this.getClass());
	protected final static String SUBMIT = "submit";

	public CopyOrderAction() {
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = mapping.findForward("continue");
		ActionMessages errors = new ActionMessages();
		ActionMessages rlErrors = new ActionMessages();
		String idStr = request.getParameter("id"); // Confirmation number of
													// License
		Long confirmationNumber = new Long(idStr);
		OrderLicenses licenses = null;
		int rlCounter = 0;
		int nonRlCounter = 0;
		int totalCounter = 0;
		String rlItem = "";
		
		//ActionForward originalForward = mapping.findForward( SUBMIT );
        //String originalPath = originalForward.getPath();
		
		//remove all session attr.
		
		request.getSession().removeAttribute(WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES);
		request.getSession().removeAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_TO_COPY);
		request.getSession().removeAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT);
		request.getSession().removeAttribute(WebConstants.SessionKeys.COPY_ORDER_ERROR_PUBLICATION_TITLES);

		try {

			OrderLicense aLicense = null;
			long puridLong = Long.parseLong(idStr);
			boolean purchaseIsAcademic = false;
			boolean purchaseFound = false;

			DisplaySpec licSpec = UserContextService.getLicenseDisplaySpec();
			licSpec.setResultsPerPage(2000); // Limit to 2000 per Keith
			licSpec.setPurchaseId(puridLong);
			licSpec.setSearch(idStr);
			licSpec.setSearchBy(DisplaySpecServices.CONFNUMFILTER);

			// DisplaySpec spec = UserContextService.getPurchaseDisplaySpec();
			// spec.setSearchBy(DisplaySpecServices.CONFNUMFILTER); // Copy by
			// Conf Number
			// spec.setSearch(idStr);

			// spec.setPurchaseId(puridLong);

			try {
				OrderPurchase purchase = OrderPurchaseServices
						.getOrderPurchaseForConfirmationNumber(confirmationNumber);
				OrderSearchResult orderSearchResult = OrderLicenseServices
						.searchOrderDetailsForDisplaySpec();
				licenses = orderSearchResult.getOrderLicensesObject();
				Long bundleId = null;
				OrderBundle orderBundle = null;
				for (OrderLicense orderLicense : orderSearchResult
						.getOrderLicenses()) {
					if (orderLicense.isAcademic()) {
						purchaseIsAcademic = true;
						bundleId = orderLicense.getBundleId();
						orderBundle = orderSearchResult
								.getOrderBundleByBundleId(orderLicense
										.getBundleId());
						break;
					}
				}

				// OrderPurchases purchases =
				// OrderPurchaseServices.getOrderPurchases();
				// OrderPurchase purchase = purchases.getOrderPurchase(0);
				purchaseFound = true;
				if (purchase != null) {
					if (!CartServices.isAcademicCart() && purchaseIsAcademic) {
						CourseDetails details = CartServices
								.createCourseDetails();
						details.setAccountingReference(orderBundle
								.getAccountingReference());
						details.setCourseName(orderBundle.getCourseName());
						details.setCourseNumber(orderBundle.getCourseNumber());
						details.setInstructor(orderBundle.getInstructor());
						// TODO: Possible truncation from long to int
						details.setNumberOfStudents((int) orderBundle
								.getNumberOfStudents());
						details.setOrderEnteredBy(orderBundle
								.getOrderEnteredBy());
						details.setReference(orderBundle.getYourReference()); // ????
						details.setSchoolCollege(orderBundle.getOrganization());
						details
								.setStartOfTermDate(orderBundle
										.getStartOfTerm());
						request.setAttribute(WebConstants.RequestKeys.COURSE_DETAILS,
								details);
						forward = mapping.findForward("editCourse");
	                   	request.setAttribute(WebConstants.RequestKeys.IS_CANCEL_VIA_HISTORY_ENABLED,"true");						
						// CartServices.setCourseDetails(details);
					}
				} else {
					ActionMessage error = new ActionMessage(
							"ov.error.copy.license.general");
					errors.add("general", error);
					this.saveErrors(request, errors);
					forward = mapping.findForward("error");
				}
			} catch (OrderLicensesException opx) {
				ActionMessage error = new ActionMessage(
						"ov.error.copy.license.general");
				errors.add("general", error);
				this.saveErrors(request, errors);
				forward = mapping.findForward("error");
			} catch (OrderPurchasesException opx) {
				ActionMessage error = new ActionMessage(
						"ov.error.copy.license.general");
				errors.add("general", error);
				this.saveErrors(request, errors);
				forward = mapping.findForward("error");
			}

			ArrayList<String> itemTitlesThatCannotBeCopied = new ArrayList<String>();
			ArrayList<String> rlItemTitlesThatCannotBeCopied = new ArrayList<String>();
			// try
			// {

			// OrderLicenses licenses = OrderLicenseServices.getOrderLicenses();
			List<OrderLicense> orderLicenses = licenses.getOrderLicenseList();

			if (orderLicenses != null && orderLicenses.size() > 0) {
				ArrayList<PurchasablePermission> permissionsToCopy = new ArrayList<PurchasablePermission>();
				ArrayList<PurchasablePermission> academicPermissionsToCopy = new ArrayList<PurchasablePermission>();
				// ArrayList nonAcademicPermissionsToCopy = new ArrayList();
				ArrayList<PurchasablePermission> nonAcademicPermissionsToEdit = new ArrayList<PurchasablePermission>();
				ArrayList<PurchasablePermission> nonAcademicPermissionsToCopy = new ArrayList<PurchasablePermission>();

				Iterator<OrderLicense> itr = orderLicenses.iterator();

				while (itr.hasNext()) {
					aLicense = itr.next();

					if (aLicense != null
							&& aLicense.getRightSourceCd().equals(
									RightSourceEnum.TF.name())) {
						
						//Check for DENY or CRD before and discard
						if ((aLicense.getItemAvailabilityCd() != null) || (aLicense.getProductCd().equalsIgnoreCase("RLS"))) {
							if (aLicense.getProductCd().equalsIgnoreCase("RLS") ||
							   (aLicense.getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd()) ||
								   aLicense.getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd()) ||
								   aLicense.getItemAvailabilityCd().equals(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd())))
								   {
								rlItemTitlesThatCannotBeCopied.add(aLicense
										.getPublicationTitle());
							
								rlCounter++;
							
								if (rlItem.compareTo("") == 0)
								{
									rlItem = "<p>" +rlCounter + ". " + aLicense.getPublicationTitle()+"</p>" ;
								}
								else
								{
									rlItem = rlItem + "<p>" + rlCounter + ". " + aLicense.getPublicationTitle() +"</p>" ;
								}
								continue;
							}
						}
						
						// Create a PurchasablePermission object from the
						// license
						PurchasablePermission permission = null;
						try {
							permission = PurchasablePermissionFactory
									.createPurchasablePermission(aLicense, true);

							if (permission.getItemAvailabilityCd() != null) {
								if (permission.getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd()) ||
									   permission.getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd())||
									   permission.getItemAvailabilityCd().equals(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd())) {
									rlItemTitlesThatCannotBeCopied.add(aLicense
											.getPublicationTitle());
								
									rlCounter++;
								
									if (rlItem.compareTo("") == 0)
									{
										rlItem = "<p>" +rlCounter + ". " + aLicense.getPublicationTitle()+"</p>" ;
									}
									else
									{
										rlItem = rlItem + "<p>" + rlCounter + ". " + aLicense.getPublicationTitle() +"</p>" ;
									}
									continue;
								}
							}
						//	}
						} catch (UnableToBuildPurchasablePermissionException ppe) {
							itemTitlesThatCannotBeCopied.add(aLicense
									.getPublicationTitle());
							continue;
						} catch (Exception e) {
							_logger.error("\n\nError building work:\n\n", e);
							forward = mapping.findForward("error"); // empty
																	// license
																	// id parm
							ActionMessage error = new ActionMessage(
									"ov.error.copy.license.missing", idStr);
							errors.add("general", error);
							this.saveErrors(request, errors);
							forward = mapping.findForward("error");
							return forward;
						}
						if (permission.isAcademic()) {
							// CartServices.initBundleIdForPurchasablePermission(permission);
							academicPermissionsToCopy.add(permission);
							permissionsToCopy.add(permission);
						} else {
							boolean isCancelled = (aLicense.getWithdrawnCode() != null);
							if (isCancelled)
								nonAcademicPermissionsToEdit.add(permission);
							else
								nonAcademicPermissionsToCopy.add(permission);
						}
					} else if (aLicense != null
							&& aLicense.getRightSourceCd().equals(
									RightSourceEnum.RL.name())) {
						
						rlCounter++;
						
						if (rlItem.compareTo("") == 0)
						{
							rlItem = "<p>" +rlCounter + ". " + aLicense.getPublicationTitle()+"</p>" ;
						}
						else
						{
							rlItem = rlItem + "<p>" + rlCounter + ". " + aLicense.getPublicationTitle() +"</p>" ;
						}
						
						
						rlItemTitlesThatCannotBeCopied.add(aLicense
								.getPublicationTitle());
					}

				}

				if (rlItemTitlesThatCannotBeCopied.size() > 0) {
					request
							.getSession()
							.setAttribute(
									WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES,
									rlItemTitlesThatCannotBeCopied);
				}

				if (academicPermissionsToCopy.size() > 0) {
					forward = mapping.findForward("editCourse");
					// if (permissionsToCopy.size() > 0) {
					// request.setAttribute(WebConstants.PURCHASABLE_PERMISSIONS_TO_COPY,
					// (PurchasablePermission[])permissionsToCopy.toArray( new
					// PurchasablePermission[permissionsToCopy.size()]) );
					// 
					request
							.setAttribute(
									WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_TO_COPY,
									permissionsToCopy
											.toArray(new PurchasablePermission[permissionsToCopy
													.size()]));
					// }
					// if( nonAcademicPermissionsToEdit.size() > 0 ) {
					/* request
							.setAttribute(
									WebConstants.PURCHASABLE_PERMISSIONS_TO_EDIT,
									nonAcademicPermissionsToEdit
											.toArray(new PurchasablePermission[nonAcademicPermissionsToEdit
													.size()])); */
					// }

				}

				// if( permissionsToCopy.size() > 0 )
				// {
				// forward = mapping.findForward( "editOrder" );
				// request.setAttribute(WebConstants.PURCHASABLE_PERMISSIONS_TO_COPY,
				// (PurchasablePermission[])permissionsToCopy.toArray( new
				// PurchasablePermission[permissionsToCopy.size()]) );
				// }
				if (nonAcademicPermissionsToEdit.size() > 0) {
						forward = mapping.findForward("editOrder");
						request.getSession()
								.setAttribute(
										WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT,
										nonAcademicPermissionsToEdit
												.toArray(new PurchasablePermission[nonAcademicPermissionsToEdit
														.size()]));
					/*	request.getSession()
								.setAttribute(
										WebConstants.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_COPY,
										permissionsToCopy
												.toArray(new PurchasablePermission[nonAcademicPermissionsToCopy
														.size()])); */
					} 
				
				if ( nonAcademicPermissionsToCopy.size() > 0 && nonAcademicPermissionsToEdit.size() == 0) 
					{
						Iterator<PurchasablePermission> iterator = nonAcademicPermissionsToCopy
								.iterator();
						while (iterator.hasNext()) {
							PurchasablePermission permission = iterator.next();
							try {
								CartServices.addItemToCart(permission);
							} catch (ItemCannotBePurchasedException e) {
								itemTitlesThatCannotBeCopied.add(e
										.getPurchasablePermission()
										.getPublicationTitle());
							} catch (Exception ex) {
								ActionMessage error = new ActionMessage(
										"ov.error.copy.license.general");
								errors.add("general", error);
								this.saveErrors(request, errors);
								forward = mapping.findForward("error");
							}
						}
						//Price the items before to avoid price update message in cart page
						CartServices.updateCartForTFNonAcademicPriceChange();
					}
				
				if (academicPermissionsToCopy.size() > 0) {
					forward = mapping.findForward("editCourse");
				}
				else if (nonAcademicPermissionsToEdit.size() > 0) 
				{
					forward = mapping.findForward("editOrder");
				}
				else
				{
					forward = mapping.findForward("continue");
				}
				

				// if( itemTitlesThatCannotBeCopied.size() > 0 )
				// {
				request
						.setAttribute(
								WebConstants.RequestKeys.COPY_ORDER_ERROR_PUBLICATION_TITLES,
								itemTitlesThatCannotBeCopied
										.toArray(new String[itemTitlesThatCannotBeCopied
												.size()]));
				// }

				//totalCounter = rlCounter + nonRlCounter;
				if (rlCounter == orderLicenses.size()) {
					ActionMessage error = new ActionMessage(
							"ov.error.copy.license.rlink", rlItem);
					rlErrors.add("rlError", error);
					this.saveMessages(request, rlErrors);
					forward = mapping.findForward("error");
					//String path = "orderView.do" + "?" + "id" + "=" + idStr + "rp=main&sf=false&cp=1&viewOrder=1";
					//forward =  new ActionForward("error", path, true, null);
					return forward;
				}

			} else {
				forward = mapping.findForward("error"); // couldn't find the
														// license by idStr
				ActionMessage error = new ActionMessage(
						"ov.error.copy.license.missing", idStr);
				errors.add("general", error);
				forward = mapping.findForward("error");
			}
			// } catch (OrderLicensesException olex) {
			// forward = mapping.findForward("error"); // couldn't find the
			// license by idStr
			// ActionMessage error = new
			// ActionMessage("ov.error.copy.license.missing", idStr);
			// errors.add("general", error);
			// forward = mapping.findForward("error");
			// }
		} catch (NumberFormatException nfe) {
			forward = mapping.findForward("error"); // invalid non-numeric
													// license id or Conf Number
			ActionMessage error = new ActionMessage(
					"ov.error.copy.license.missing", idStr);
			errors.add("general", error);
			this.saveErrors(request, errors);
			forward = mapping.findForward("error");

		} catch (NullPointerException npe) {
			forward = mapping.findForward("error"); // empty license id parm
			ActionMessage error = new ActionMessage(
					"ov.error.copy.license.missing", idStr);
			errors.add("general", error);
			this.saveErrors(request, errors);
			forward = mapping.findForward("error");
		}
		
		return forward;
	}
}