package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CascadeUpdateException;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.CourseDetailsForm;


public class EditCartCourseDetailsAction extends CourseDetailsAction
{

    private static final String CANCEL = "cancel";

    @Override
    protected void populateForm(CourseDetailsForm courseDetailsForm, 
                                HttpServletRequest request)
    {
        CourseDetails courseDetails = CartServices.getCourseDetails();
        
        courseDetailsForm.setCourseDetails( courseDetails );
        courseDetailsForm.setPurchasablePermissionsToAdd( null );
        courseDetailsForm.setOrderPurchase( null );
        courseDetailsForm.setCascadingChanges( false );
    }

    @Override
    public ActionForward submit(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response)
    {
        CourseDetailsForm courseDetailsForm = castForm( CourseDetailsForm.class, form );
        
        CourseDetails courseDetails = courseDetailsForm.getCourseDetails();
        
        CartServices.setCourseDetails( courseDetails );
        
        if(courseDetailsForm.isCascadingChanges())
        {
            try
            {
                CartServices.cascadeUpdateCartItems( courseDetails.getNumberOfStudents() );
                
                //Re-price the academic items
                CartServices.updateCartForTFAcademicPriceChange();
                UserContextService.setCartItemEdited(true);
            } 
            catch (CascadeUpdateException cue)
            {
                request.setAttribute( WebConstants.RequestKeys.CASCADE_UPDATE_EXCEPTION, cue );
                /*
                 * This refreshes the Cart in the UserContext,(UC) to a clean DB
                 * copy.  Above, the call to cascadeUpdateCartItems actually 
                 * updates the UC's Cart (via object reference) before calling
                 * the DB update - and if any of the items fail to be updated, 
                 * they weren't properly represented in the UC cart any longer.
                 * This should be 'properly handled' by disalowing any updates
                 * to Cart outside of CartService service calls.
                 */
                CartServices.refreshCart();
            }
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
