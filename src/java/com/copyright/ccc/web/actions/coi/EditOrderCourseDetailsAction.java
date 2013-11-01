package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.CourseDetailsForm;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.order.api.data.OrderHeader;

public class EditOrderCourseDetailsAction extends CourseDetailsAction
{
    private static final String CANCEL = "cancel";
    
    private static final String ERROR_TRY_AGAIN = "Update error, please return to order search and try again.";
    
    @Override
    protected void populateForm(CourseDetailsForm courseDetailsForm, 
                                HttpServletRequest request)
    {
        CourseDetails courseDetails = CartServices.createCourseDetails();
        
        Object orderPurchaseObj = request.getAttribute( WebConstants.RequestKeys.ORDER_PURCHASE );
        
        OrderPurchase orderPurchase = (OrderPurchase)orderPurchaseObj;
        courseDetailsForm.setOrderPurchase( orderPurchase );
        
        OrderBundle orderBundle = null;
        
        if (orderPurchase.getOrderBundles() != null) {
        	for (OrderBundle ob : orderPurchase.getOrderBundles()) {
        		orderBundle = ob;
        		break;
        	}
        }
       
        if (orderBundle != null)
        {
	        courseDetails.setNumberOfStudents( (int) orderBundle.getNumberOfStudents() );
	        courseDetails.setSchoolCollege( orderBundle.getOrganization());
	        courseDetails.setCourseName( orderBundle.getCourseName() );
	        courseDetails.setCourseNumber( orderBundle.getCourseNumber() );
	        courseDetails.setInstructor( orderBundle.getInstructor() );
	        courseDetails.setStartOfTermDate( orderBundle.getStartOfTerm() );
	        courseDetails.setReference( orderBundle.getYourReference());
	        courseDetails.setAccountingReference( orderBundle.getAccountingReference() );
	        courseDetails.setOrderEnteredBy( orderBundle.getOrderEnteredBy() );
        }
        
        
        courseDetailsForm.setCourseDetails( courseDetails );
        courseDetailsForm.setPurchasablePermissionsToAdd( null );
        courseDetailsForm.setCascadingChanges( false );
    }

    @Override
    public ActionForward submit(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response)
    {
        CourseDetailsForm courseDetailsForm = castForm( CourseDetailsForm.class, form );
        
        CourseDetails courseDetails = courseDetailsForm.getCourseDetails();        
        OrderPurchase orderPurchase = courseDetailsForm.getOrderPurchase();
        List<OrderLicense> orderLicenseList = null;
        
        List<String> errorsList = new ArrayList<String>();
        List<String> closedOrdersList = new ArrayList<String>();
        
       	long orderPurchaseId = orderPurchase.getConfirmationNumber();
        
        OrderHeader orderHeader = null;
        OrderSearchResult orderSearchResult = null;
        OrderPurchase orderPurchaseForUpdate = null;
        		
    	if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
    		try {
    			orderSearchResult = OrderPurchaseServices.searchFullCOIOrderPurchaseByConfirmNumber(orderPurchaseId);
    			if (orderSearchResult != null) {
    				orderPurchaseForUpdate = orderSearchResult.getOrderPurchaseByConfirmId(new Long(orderPurchaseId));
    				orderLicenseList = orderSearchResult.getOrderLicenses();
    			} 
    		} catch (OrderPurchasesException e) {
        		_logger.error( "Error retrieving getting full COI purchase for cascade update, confirm number: " + orderPurchaseId+ " " +
						"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
		                "Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
    		}
    	} else {
    		try
        	{
    			orderPurchaseForUpdate = orderPurchase;
    			orderSearchResult = OrderLicenseServices.searchAllOrderLicensesForConfirmationNumber(new Long(orderPurchaseId ));
    			if (orderSearchResult != null) {
    				orderLicenseList = orderSearchResult.getOrderLicenses();
    			}
        	} 
        	catch (OrderLicensesException e)
        	{
        		_logger.error( "Error retrieving licenses for cascade update, confirm number: " + orderPurchaseId + " " +
						"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
		                "Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
        	}
    	} 
        
    	if (orderPurchaseForUpdate == null || orderLicenseList == null) {
    		errorsList.add( ERROR_TRY_AGAIN );  				
		} else {
			if (orderPurchaseForUpdate.getOrderBundles() != null) {
				for (OrderBundle orderBundle : orderPurchaseForUpdate.getOrderBundles()) {
					orderBundle.setNumberOfStudents( courseDetails.getNumberOfStudents() );
					orderBundle.setOrganization( courseDetails.getSchoolCollege() );
					orderBundle.setCourseName( courseDetails.getCourseName() );
					orderBundle.setCourseNumber( courseDetails.getCourseNumber() );
					orderBundle.setInstructor( courseDetails.getInstructor() );
					orderBundle.setStartOfTerm( courseDetails.getStartOfTermDate() );
					orderBundle.setYourReference( courseDetails.getReference() );
					orderBundle.setAccountingReference( courseDetails.getAccountingReference() );
					orderBundle.setOrderEnteredBy( courseDetails.getOrderEnteredBy() );
			    	if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.TF.getOrderDataSourceId().intValue()) {
			    		try
			    		{
			    			OrderPurchaseServices.updateOrderBundle( orderPurchase, orderBundle );
			    		} 
			    		catch (OrderPurchasesException e)
			    		{
			    			_logger.error( LogUtil.getStack(e) );
			    		}
			    	}
		    		break;
				}
        	}
        
			if( courseDetailsForm.isCascadingChanges() )
			{
				cascadeUpdateOrderLicensesForPurchase( request, courseDetails.getNumberOfStudents(), orderLicenseList, 
		            errorsList, closedOrdersList);
			}
    	
    		if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
    			try {
    			OrderPurchaseServices.updateFullCOIOrderPurchase(orderSearchResult.getOrderPurchaseByConfirmId(orderPurchaseId));
    			} catch (OrderPurchasesException e) {
    				_logger.error( "Error updating COI purchase/licenses for cascade update, confirm number: " + orderPurchaseId+ " " +
    					"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
    					"Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
    			}
    		}
        }

    	if( !closedOrdersList.isEmpty() )
    		request.setAttribute( WebConstants.RequestKeys.ORDER_CASCADE_UPDATE_CLOSED_STRINGS, closedOrdersList );
    	if( !errorsList.isEmpty() )
    		request.setAttribute( WebConstants.RequestKeys.ORDER_CASCADE_UPDATE_ERROR_STRINGS, errorsList );

        return constructForwardWithQueryString( mapping.findForward(SUBMIT), orderPurchase, "", "", "");
    }

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response)
    {
        CourseDetailsForm courseDetailsForm = castForm( CourseDetailsForm.class, form );
        String cpParm = courseDetailsForm.getCp();
        String rpParm = courseDetailsForm.getRp();
        String sfParm = courseDetailsForm.getSf();
        
        return constructForwardWithQueryString(mapping.findForward( CANCEL ), courseDetailsForm.getOrderPurchase(), cpParm, rpParm, sfParm );
    }
    
    private ActionForward constructForwardWithQueryString(ActionForward originalForward, OrderPurchase orderPurchase, String currentPage, String returnTab, String searchFlag)
    {
        String originalPath = originalForward.getPath();
        String cpParm = "";
        if (currentPage != null && !"".equals(currentPage)) {
           cpParm = "&cp=" + currentPage;
        }
        
        String rpParm = "";
        if (returnTab != null && !"".equals(returnTab)) {
           rpParm = "&rp=" + returnTab;
        }
        String sfParm = "";
        if ("true".equalsIgnoreCase(searchFlag)) {
           sfParm = "&sf=true";
        }

        String pathWithPurchaseID = originalPath + "?" + WebConstants.RequestKeys.PURCHASE_ID + "=" + orderPurchase.getPurInst() + cpParm + rpParm + sfParm;
        
        return new ActionForward(originalForward.getName(), 
            pathWithPurchaseID,
            originalForward.getRedirect(),
            originalForward.getModule());
    }
    
    private void cascadeUpdateOrderLicensesForPurchase(HttpServletRequest request, int numberOfStudents,
    		List<OrderLicense> orderLicenseList, 
            List<String> errorsList, List<String> closedOrdersList)
    {
  	
    	if( orderLicenseList != null)
        {
                        
            for(int i = 0; i < orderLicenseList.size(); i++)
            {
                OrderLicense orderLicense = orderLicenseList.get(i);
                
                if( orderLicense.isAcademic() && 
                   (orderLicense.getInvoiceId() != null || 
                	orderLicense.getItemStatusCd().equals(ItemConstants.CANCELED) ||
                	orderLicense.getItemStatusCd().equals(ItemStatusEnum.CANCELLED.name())))
                {
                    closedOrdersList.add( getOrderLicenseErrorString(orderLicense) );                    
                }
                else if (orderLicense.isAcademic())
                {                 
                    boolean updateSuccess = updateOrderLicenseNumberOfStudents( orderLicense, numberOfStudents );
                    if( !updateSuccess )
                    {
                        errorsList.add( getOrderLicenseErrorString(orderLicense) );
                    }
                }
            }

       }
    }
    
    /** returns error string for the order to be displayed in order screen if cascade update has errors **/
    private String getOrderLicenseErrorString(OrderLicense orderLicense)
    {
        return "ID " + orderLicense.getID() + ": " + orderLicense.getPublicationTitle();
    }
    
    /** returns true if order is closed (i.e. invoiced or cancelled) **/
    private boolean isOrderClosed(OrderLicense orderLicense)
    {
        //make sure the order hasn't been invoiced
        boolean isInvoiced = (orderLicense.getInvoiceId() != null || orderLicense.getCreditAuth() != null);
        if( isInvoiced ) 
            return true;
        
        //make sure the order isn't cancelled
        boolean isCancelled = (orderLicense.getWithdrawnCode() != null);
        if( isCancelled )
            return true;
        
        return false;
    }
    
    /** returns true if update was successful, false otherwise **/
    private boolean updateOrderLicenseNumberOfStudents(OrderLicense orderLicense, int numberOfStudents)
    {
        boolean hasErrors = false;
        
        int originalNumberOfStudents = orderLicense.getNumberOfStudents();
        orderLicense.setNumberOfStudents( numberOfStudents );
            
        try
        {
        	if (orderLicense.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
        		try {
        			orderLicense=PricingServices.updateItemPrice(orderLicense);
        		} catch (ServiceInvocationException e) {
        			_logger.error(LogUtil.getStack(e));
        			hasErrors = true;
        		}
        	} else {
        		OrderLicenseServices.updateOrderLicense( orderLicense );
        	}
        } 
        catch (OrderLicensesException e)
        {
    		_logger.error( "Error updating license for cascade update, licenseId: " + orderLicense.getID() + " " +
					"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
	                "Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
            hasErrors = true;
        }
        
        if( hasErrors )
            orderLicense.setNumberOfStudents( originalNumberOfStudents );
        
        return !hasErrors;
    }
   
}
