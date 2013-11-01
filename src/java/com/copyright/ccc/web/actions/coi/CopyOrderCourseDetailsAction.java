package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.services.cart.CannotBeAddedToCartException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.CourseDetailsForm;

public class CopyOrderCourseDetailsAction extends CourseDetailsAction
{
    private static final String CANCEL = "cancel";
    
    @Override
    protected void populateForm(CourseDetailsForm courseDetailsForm, 
                                HttpServletRequest request)
    {
        //get purchasable permissions from request
        Object purchasablePermissionsToCopyObj = request.getAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSIONS_TO_COPY );

        PurchasablePermission[] purchasablePermissionsToCopy = (PurchasablePermission[])purchasablePermissionsToCopyObj;
        courseDetailsForm.setPurchasablePermissionsToAdd( purchasablePermissionsToCopy );

        // PDS 1/2008
        /* A not-so-slick "work around" so that the titles that couldn't be copied 
        // are maintained for informational display purposes.
        // This pulls the array of main titles from the request and puts it on the
        // form so that upon submit() - it can be re-added to the request and carried
        // to the cart form, which is what is used by the cart.jsp (currently the only
        // jsp that shows info about licenses that can't be copied.  Previously, 
        // the request parameter would just go bye-bye after this 'action'.
        */
        Object purchasablePermissionsUnableToCopyObj = request.getAttribute( WebConstants.RequestKeys.COPY_ORDER_ERROR_PUBLICATION_TITLES );
        String[] ppCannotBeCopied = (String[])purchasablePermissionsUnableToCopyObj;
        courseDetailsForm.setTitlesThatCannotBeCopied( ppCannotBeCopied );
        
        Object rlPermissionsUnableToCopyObj = request.getSession().getAttribute( WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES );
        @SuppressWarnings("unchecked")
        ArrayList<String> rlCannotBeCopied = (ArrayList<String>)rlPermissionsUnableToCopyObj;
        courseDetailsForm.setRlTitlesThatCannotBeCopied( rlCannotBeCopied );
        //TODO
        
        
        //get course details from request
        Object courseDetailsObj = request.getAttribute( WebConstants.RequestKeys.COURSE_DETAILS );
       
        if (courseDetailsObj != null)
        {
        	CourseDetails courseDetails = (CourseDetails)courseDetailsObj;
        	courseDetails.setNumberOfStudents(-1);
        	courseDetails.setStartOfTermDate(null);
        	courseDetailsForm.setCourseDetails( courseDetails );
        }
        
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
        CartServices.setCourseDetails( courseDetails );
        
        //add purchasable permissions to cart with number of students = entered number of students
        
        int numberOfStudents = courseDetails.getNumberOfStudents();
      
        PurchasablePermission[] purchasablePermissionsToCopy = courseDetailsForm.getPurchasablePermissionsToAdd();
        
        // PDS 1/2008
        /* pull the error titles off the form so they can be handled by the cart
        // form. The array as it exists at this point (String[]) is merely the titles that
        // couldn't be copied during the CopyOrderAction.  
        // This list may grow if invoking addToCart() below throws an error.
        */
        String[] titlesThatCannotBeCopied = courseDetailsForm.getTitlesThatCannotBeCopied();
        
        ArrayList<String> publicationPermissionTitlesUnableToBeCopied = new ArrayList<String>();
        
        
        if( (null != titlesThatCannotBeCopied) && (titlesThatCannotBeCopied.length > 0) )
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
      //  }

        if (purchasablePermissionsToCopy!=null) {
	        for(int i = 0; i < purchasablePermissionsToCopy.length; i++)
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
        }
        if( !publicationPermissionTitlesUnableToBeCopied.isEmpty() )
            request.setAttribute( WebConstants.RequestKeys.COPY_ORDER_ERROR_PUBLICATION_TITLES, publicationPermissionTitlesUnableToBeCopied );
        
        if(rlPublicationPermissionTitlesUnableToBeCopied != null && !rlPublicationPermissionTitlesUnableToBeCopied.isEmpty()){
        	 request.getSession().setAttribute( WebConstants.SessionKeys.RIGHTSLINK_COPY_ORDER_ERROR_PUBLICATION_TITLES, rlPublicationPermissionTitlesUnableToBeCopied  );
        }
        
        //Price the items before to avoid price update message in cart page
        CartServices.updateCartForTFAcademicPriceChange();
        CartServices.updateCartForTFNonAcademicPriceChange();
        
        if ((request.getSession().getAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT) != null) || (request.getSession().getAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_COPY) != null) )        	
        {
        	return mapping.findForward("editOrder");
        }
            
        return mapping.findForward( SUBMIT );
    }

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response)
    {
        return mapping.findForward( CANCEL );
    }
}
