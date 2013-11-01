package com.copyright.ccc.web.actions.coi;

//import com.copyright.ccc.business.services.cart.CannotBeAddedToCartException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CheckoutServices;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.business.services.cart.CourseDetailsImpl;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.IncompatibleOrderAndPurchasablePermissionException;
import com.copyright.ccc.business.services.cart.OrderClosedException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.CourseDetailsForm;
import com.copyright.svc.order.api.data.Bundle;
import com.copyright.svc.order.api.data.OrderConsumerContext;

public class AddOrderCourseDetailsAction extends CourseDetailsAction
{
    private static final String CANCEL = "cancel";
    
    @Override
    protected void populateForm(CourseDetailsForm courseDetailsForm, 
                                HttpServletRequest request)
    {
        //get purchasable permissions from request
        Object purchasablePermissionsToAddObj = request.getAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_TO_ADD );

        PurchasablePermission purchasablePermissionsToAdd = (PurchasablePermission)purchasablePermissionsToAddObj;
        
        //PurchasablePermission[] purch = null;
        
        PurchasablePermission[] purch; 
        
        purch = new PurchasablePermission[1];
        
        purch[0] = purchasablePermissionsToAdd;
        courseDetailsForm.setPurchasablePermissionsToAdd( purch );

        // PDS 1/2008
        /* A not-so-slick "work around" so that the titles that couldn't be copied 
        // are maintained for informational display purposes.
        // This pulls the array of main titles from the request and puts it on the
        // form so that upon submit() - it can be re-added to the request and carried
        // to the cart form, which is what is used by the cart.jsp (currently the only
        // jsp that shows info about licenses that can't be copied.  Previously, 
        // the request parameter would just go bye-bye after this 'action'.
        */
        //Object purchasablePermissionsUnableToCopyObj = request.getAttribute( WebConstants.COPY_ORDER_ERROR_PUBLICATION_TITLES );
        //String[] ppCannotBeCopied = (String[])purchasablePermissionsUnableToCopyObj;
        //courseDetailsForm.setTitlesThatCannotBeCopied( ppCannotBeCopied );
        
        //Object rlPermissionsUnableToCopyObj = request.getSession().getAttribute( WebConstants.RL_COPY_ORDER_ERROR_PUBLICATION_TITLES );
        @SuppressWarnings("unchecked")
        //ArrayList<String> rlCannotBeCopied = (ArrayList<String>)rlPermissionsUnableToCopyObj;
        //courseDetailsForm.setRlTitlesThatCannotBeCopied( rlCannotBeCopied );
        //TODO
        
        
        //get course details from request
        /* Object courseDetailsObj = request.getAttribute( WebConstants.COURSE_DETAILS );
       
        if (courseDetailsObj != null)
        {
        	CourseDetails courseDetails = (CourseDetails)courseDetailsObj;
        	courseDetails.setNumberOfStudents(-1);
        	courseDetails.setStartOfTermDate(null);
        	courseDetailsForm.setCourseDetails( courseDetails );
        } */
        
        //CourseDetails courseDetails = CartServices.createCourseDetails() ;
        
        CourseDetailsImpl courseDetailsImpl = (CourseDetailsImpl) CartServices.getCourseDetails();
        //Set the Bundle Id from the CourseDetails on the item before adding to cart
        //purchasablePermission.getItem().setBundleId(courseDetailsImpl.getBundle().getBundleId());
        
        //Service
        
        courseDetailsImpl.setNumberOfStudents(purchasablePermissionsToAdd.getNumberOfStudents());
    	//courseDetailsImpl.setStartOfTermDate(null);
                
        //Bundle currBundle = courseDetailsImpl.getBundle();
                
        //Bundle newBundle = ServiceLocator.getOrderService().saveNewBundle(new OrderConsumerContext(), currBundle);
        
        //courseDetailsImpl.setBundle(newBundle);   
        
        courseDetailsForm.setCourseDetails(courseDetailsImpl);
                
        courseDetailsForm.setCascadingChanges( true );
        courseDetailsForm.setOrderPurchase( null );
        
    }

    @Override
    public ActionForward submit(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response) 
    {
        CourseDetailsForm courseDetailsForm = castForm( CourseDetailsForm.class, form );
        
        CourseDetails courseDetails = courseDetailsForm.getCourseDetails();
        CourseDetailsImpl courseDetailsImpl = (CourseDetailsImpl) courseDetailsForm.getCourseDetails();
        //CheckoutServices.setCourseDetails( courseDetails );
        
        //ActionForward forward = null;
        
        
        
        //add purchasable permissions to cart with number of students = entered number of students
        
        //int numberOfStudents = courseDetails.getNumberOfStudents();
      
        PurchasablePermission[] purchasablePermissionsToAdd = courseDetailsForm.getPurchasablePermissionsToAdd();
        
        purchasablePermissionsToAdd[0].getItem().setBundleId(courseDetails.getBundleId());
        
        purchasablePermissionsToAdd[0].setNumberOfStudents(courseDetails.getNumberOfStudents());
        
        //OrderBundle orderBundle = OrderPurchaseServices.
        
        String orderNum = request.getSession().getAttribute( WebConstants.SessionKeys.PURCHASE_ID ).toString();
        
        request.getSession().removeAttribute( WebConstants.SessionKeys.PURCHASE_ID );
        
        long confirmNumber = Long.parseLong(orderNum);
        
        //Bundle bundle = ServiceLocator.getOrderService().g
        Bundle currBundle = courseDetailsImpl.getBundle();
        
        Bundle newBundle = ServiceLocator.getOrderService().saveNewBundle(new OrderConsumerContext(), currBundle);
        
        purchasablePermissionsToAdd[0].getItem().setBundleId(newBundle.getBundleId());              
        
        try
        {
        	CheckoutServices.addItemToExistingOrder(purchasablePermissionsToAdd[0], confirmNumber, null);
        }
        catch (OrderClosedException e)
        {
          throw new CCRuntimeException(e);
        }
        
          catch (CourseNotDefinedException cnde)
        {
        	  throw new CCRuntimeException(cnde);
        }
        catch ( IncompatibleOrderAndPurchasablePermissionException iope)
        {
        	throw new CCRuntimeException(iope);
        }

        
        ActionForward originalForward = mapping.findForward( SUBMIT );
        String originalPath = "orderView.do" ;
        
        String pathWithPurchaseID = originalPath + "?" + WebConstants.RequestKeys.PURCHASE_ID + "=" + orderNum;
        
        ActionForward forward = null;
        
        forward =  new ActionForward("submit", pathWithPurchaseID, true, null);
        
        return forward;
        
        //item.setItemStatusCd(ItemStatusEnum.INVOICE_READY);
        
        
        
        //CheckoutServices.addItemToExistingOrder( purchasablePermission, orderPurchaseID, null );
    	
        //ServiceLocator.getOrderService().addItemToOrder(new OrderConsumerContext(), orderPurchase.getOrderHeaderId(), item);
        
        // PDS 1/2008
        /* pull the error titles off the form so they can be handled by the cart
        // form. The array as it exists at this point (String[]) is merely the titles that
        // couldn't be copied during the CopyOrderAction.  
        // This list may grow if invoking addToCart() below throws an error.
        */
        //String[] titlesThatCannotBeCopied = courseDetailsForm.getTitlesThatCannotBeCopied();
        
        //ArrayList<String> publicationPermissionTitlesUnableToBeCopied = new ArrayList<String>();
        
        
    /*    if( (null != titlesThatCannotBeCopied) && (titlesThatCannotBeCopied.length > 0) )
        {
          for( int i = 0; i < titlesThatCannotBeCopied.length; i++ )
          {
            publicationPermissionTitlesUnableToBeCopied.add( titlesThatCannotBeCopied[i] );
          }
        }
        ArrayList<String> rlTitlesThatCannotBeCopied = courseDetailsForm.getRlTitlesThatCannotBeCopied(); //TODO
        
        ArrayList<String> rlPublicationPermissionTitlesUnableToBeCopied = rlTitlesThatCannotBeCopied;
        
        
       // if( (null != rlTitlesThatCannotBeCopied) && (rlTitlesThatCannotBeCopied.length > 0) )
     //   {
      //    for( int i = 0; i < rlTitlesThatCannotBeCopied.length; i++ )
       //   {
       // 	  rlPublicationPermissionTitlesUnableToBeCopied.add( rlTitlesThatCannotBeCopied[i] );
      //    }
      //  } */
 
        
     /*   for(int i = 0; i < purchasablePermissionsToAdd.length; i++)
        {
            PurchasablePermission purchasablePermissionToCopy = purchasablePermissionsToCopy[i];
            purchasablePermissionToCopy.setNumberOfStudents( numberOfStudents );
            
            try
            {
            	if (purchasablePermissionToCopy.isAcademic()) {
                	CartServices.initBundleIdForPurchasablePermission(purchasablePermissionToCopy);            		
            	}
               CartServices.addItemToCart( purchasablePermissionToCopy );
            } 
            catch (CourseNotDefinedException e)
            {
                // TODO: gcuevas
//            } catch (InvalidAttributesException e)
//            {
//                 publicationPermissionTitlesUnableToBeCopied.add( e.getTransactionItem().getPublicationTitle() );
//            } catch (ChangedToRegularOrderException e)
//            {
//                try{
//                    CartServices.addItemToCart( purchasablePermissionToCopy );
//                }catch (Exception ex)
//                {
                    //TODO: gcuevas
//                }
//            } catch (ContactRHDirectlyLimitsExceededException e)
//            {
//                publicationPermissionTitlesUnableToBeCopied.add( e.getTransactionItem().getPublicationTitle() );
//            } catch (DeniedLimitsExceededException e)
//            {
//                 publicationPermissionTitlesUnableToBeCopied.add( e.getTransactionItem().getPublicationTitle() );
            } catch (CannotBeAddedToCartException e)
            {
                 publicationPermissionTitlesUnableToBeCopied.add( e.getPurchasablePermission().getPublicationTitle() );
//            } catch (SpecialOrderLimitsExceededException e)
//            {
//                purchasablePermissionToCopy.setSpecialOrderLimitsExceeded( true );
//                try
//                {
//                    CartServices.addItemToCart( purchasablePermissionToCopy );
//                } catch (Exception f)
//                {
                    // TODO: gcuevas
//                } 
            }
            catch (ItemCannotBePurchasedException e)
            {
                publicationPermissionTitlesUnableToBeCopied.add( e.getPurchasablePermission().getPublicationTitle() );
            }
        }
        
        if( !publicationPermissionTitlesUnableToBeCopied.isEmpty() )
            request.setAttribute( WebConstants.COPY_ORDER_ERROR_PUBLICATION_TITLES, publicationPermissionTitlesUnableToBeCopied );
        
        if(rlPublicationPermissionTitlesUnableToBeCopied != null && !rlPublicationPermissionTitlesUnableToBeCopied.isEmpty()){
        	 request.getSession().setAttribute( WebConstants.RL_COPY_ORDER_ERROR_PUBLICATION_TITLES, rlPublicationPermissionTitlesUnableToBeCopied  );
        }
        
        //Price the items before to avoid price update message in cart page
        CartServices.updateCartForTFAcademicPriceChange();
        CartServices.updateCartForTFNonAcademicPriceChange();
        
        if ((request.getSession().getAttribute(WebConstants.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT) != null) || (request.getSession().getAttribute(WebConstants.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_COPY) != null) )        	
        {
        	return mapping.findForward("editOrder");
        } */
            
        //return mapping.findForward( SUBMIT );
    }

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response)
    {
        return mapping.findForward( CANCEL );
    }
}
