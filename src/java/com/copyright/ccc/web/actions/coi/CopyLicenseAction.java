package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
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
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.DisplaySpecServices;
import com.copyright.ccc.business.services.ItemAvailabilityEnum;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.business.services.cart.PurchasablePermissionFactory;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.WebConstants;
import com.copyright.workbench.logging.LoggerHelper;

public class CopyLicenseAction extends Action 
{
	private static Logger _logger = LoggerHelper.getLogger();
	
   public CopyLicenseAction() { }
   
    @Override
    public ActionForward execute( ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response ) 
    {
        ActionForward forward = mapping.findForward("continue");

        String idStr = request.getParameter("purid"); // Confirmation number of License
        String detailId = request.getParameter("id"); // Order Detail id of License
        
        Long detailIdLong;
        try {
        	detailIdLong = new Long (detailId); 
        } catch (Exception e) {
        	_logger.error("cannot convert detailId " + detailId + " to a Long" + LogUtil.appendableStack(e));
        	detailIdLong = new Long(0);
        }
        ActionMessages errors = new ActionMessages();
        
        //OrderSearchResult orderSearchResult = UserContextService.getOrderSearchResult();
        OrderSearchResult orderSearchResult = null;
        
        request.getSession().removeAttribute(WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES);
		request.getSession().removeAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_TO_COPY);
		request.getSession().removeAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_TO_EDIT);
		request.getSession().removeAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT);
		request.getSession().removeAttribute(WebConstants.SessionKeys.COPY_ORDER_ERROR_PUBLICATION_TITLES);
		
//		UserContextService.getLicenseDisplaySpec().setSearch(idStr);
        
        try
        {
//       	orderSearchResult = OrderLicenseServices.searchOrderDetailsForDisplaySpec();
        	
        	orderSearchResult = OrderLicenseServices.searchOrderLicensesForDetailNumber(detailIdLong);
        }
        catch(OrderLicensesException ole)
        {
        	_logger.warn( ExceptionUtils.getFullStackTrace(ole));
        }
                              
        
        OrderLicense orderLicense = null;
       	
        if (orderSearchResult != null) {
        	orderLicense = orderSearchResult.getOrderLicenseByItemId(detailIdLong);
        }
        
        if (orderLicense == null) {
        	mapping.findForward("error");
        	return forward;
        }
        OrderPurchase orderPurchase = orderSearchResult.getOrderPurchaseByConfirmId(idStr);
        OrderBundle orderBundle = null;
       	if (orderLicense.getBundleId() != null) {
                orderBundle = orderSearchResult.getOrderBundleByBundleId(orderLicense.getBundleId());        		
       	}
       	
        PurchasablePermission purchasablePermission = null;        
        
        if (orderLicense.isAcademic()) 
        {
           if (!CartServices.isAcademicCart()) 
           {
//               forward = mapping.findForward("academic"); 
               CourseDetails details = CartServices.createCourseDetails();
               if (details != null && orderBundle != null) 
               {
                   details.setAccountingReference(orderBundle.getAccountingReference());
                   details.setCourseName(orderBundle.getCourseName());
                   details.setCourseNumber(orderBundle.getCourseNumber());
                   details.setInstructor(orderBundle.getInstructor());
                   // TODO: Possible truncation from long to int
                   details.setNumberOfStudents((int)orderBundle.getNumberOfStudents());
                   details.setOrderEnteredBy(orderBundle.getOrderEnteredBy());
                   details.setReference(orderBundle.getYourReference()); // ????
                   details.setSchoolCollege(orderBundle.getOrganization());
                   details.setStartOfTermDate(orderBundle.getStartOfTerm());
                   request.setAttribute(WebConstants.RequestKeys.COURSE_DETAILS, details);
                   request.setAttribute(WebConstants.RequestKeys.IS_CANCEL_VIA_HISTORY_ENABLED,"true");						

                   forward = mapping.findForward("academic"); 
                } else {
                   forward = mapping.findForward("error");
                   ActionMessage error = new ActionMessage("ov.error.copy.not.academic.cart");
                   errors.add("general", error);
                   this.saveErrors(request, errors);
               }
           }
           purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(orderLicense,true);
//           CartServices.initBundleIdForPurchasablePermission(purchasablePermission);
        } else {
           purchasablePermission = PurchasablePermissionFactory.createPurchasablePermission(orderLicense,true);        	
        }
        
		ArrayList<String> rlItemTitlesThatCannotBeCopied = new ArrayList<String>();
        String rlItem = "";
        
		if (purchasablePermission.getItemAvailabilityCd() != null) {
			if (purchasablePermission.getItemAvailabilityCd().equals(ItemAvailabilityEnum.DENY.getStandardPermissionCd()) ||
					purchasablePermission.getItemAvailabilityCd().equals(ItemAvailabilityEnum.CONTACT_RH_DIRECTLY.getStandardPermissionCd())||
					purchasablePermission.getItemAvailabilityCd().equals(ItemAvailabilityEnum.PUBLIC_DOMAIN.getStandardPermissionCd())) 
			{
				rlItemTitlesThatCannotBeCopied.add(orderLicense.getPublicationTitle());

				rlItem = "<p>" + orderLicense.getPublicationTitle()+"</p>" ;

				request.getSession().setAttribute(
									WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES,
									rlItemTitlesThatCannotBeCopied);
				return forward;
			}
		}
        try {
            if (purchasablePermission.isAcademic()) {
                if (CartServices.isAcademicCart()) 
                {
                //Set the same number of students as in CoursePack header
                	purchasablePermission.setNumberOfStudents(CartServices.getCourseDetails().getNumberOfStudents());
                	CartServices.initBundleIdForPurchasablePermission(purchasablePermission);

                	CartServices.addItemToCart(purchasablePermission);
                 //Price the items before to avoid price update message in cart page
                   CartServices.updateCartForTFAcademicPriceChange();
                   CartServices.updateCartForTFNonAcademicPriceChange();
                } else {
                   PurchasablePermission[] permissions = new PurchasablePermission[1];
                   permissions[0] = purchasablePermission;
                   request.setAttribute(WebConstants.RequestKeys.PURCHASABLE_PERMISSIONS_TO_COPY, permissions);
                }
            } else {
                // Create a PurchasablePermission object from the license
                // Call the Cart factory
         	   
                boolean isCancelled = (orderLicense.getWithdrawnCode() != null);
                if( isCancelled )
                {
                    forward = mapping.findForward( "editOrder" );
                    PurchasablePermission[] permissions = new PurchasablePermission[]{ purchasablePermission };
                    request.getSession().setAttribute( WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT, permissions );
                }
                else
                  CartServices.addItemToCart(purchasablePermission);
                  //Price the items before to avoid price update message in cart page
                  CartServices.updateCartForTFAcademicPriceChange();
                  CartServices.updateCartForTFNonAcademicPriceChange();
            	}
            }        
        catch (Exception e) {
        	_logger.error( ExceptionUtils.getFullStackTrace(e));
        }
        
 
        return forward;
    }	

    
    /*
     * Initialize the PurchaseDisplaySpec to search by purchase id and license id.
     */
    private void initPurchaseSpec(String puridLong) 
    {
        DisplaySpec spec = UserContextService.getPurchaseDisplaySpec();
        
        spec.setSortOrder(DisplaySpec.DESCENDING);
        spec.setOrderStateFilter(DisplaySpecServices.ALLSTATES);  
        spec.setSortBy(DisplaySpecServices.ORDERDATESORT);

        int filterBy = DisplaySpecServices.CONFNUMFILTER; // default search option - NOFILTER
        spec.setSearchBy(filterBy);
        spec.setSearch(puridLong); // find by purchase ID/confirmation number

        DisplaySpecServices.setDisplayPage(spec, 1);
    }
    
    /*
    * Initialize the LicenseDisplaySpec to search by purchase id and license id.
    */
    private void initLicenseSpec(String detailId) 
    {
        DisplaySpec spec = UserContextService.getLicenseDisplaySpec();
        spec.setResultsPerPage(2000); // Limit to 2000 per Keith
        spec.setSortOrder(DisplaySpec.DESCENDING);
        spec.setOrderStateFilter(DisplaySpecServices.ALLSTATES);  
        spec.setSortBy(DisplaySpecServices.ORDERDATESORT);

        int filterBy = DisplaySpecServices.ORDERDETAILIDFILTER; // search by detail ID
        spec.setSearchBy(filterBy);
        spec.setSearch(detailId); // find by detail ID

        DisplaySpecServices.setDisplayPage(spec, 1);

    }
}
