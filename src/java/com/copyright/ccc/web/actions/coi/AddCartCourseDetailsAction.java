package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CourseDetails;
import com.copyright.ccc.business.services.cart.CourseDetailsImpl;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.CourseDetailsForm;
import com.copyright.ccc.web.transaction.coi.TransactionUtils;

public class AddCartCourseDetailsAction extends CourseDetailsAction
{
    private static final String CANCEL_BASIC = "cancelBasic";
    private static final String CANCEL_SPECIAL = "cancelSpecial";
    
    @Override
    protected void populateForm(CourseDetailsForm courseDetailsFormCOI, 
                                HttpServletRequest request)
    {
        CourseDetails courseDetails = CartServices.createCourseDetails();
                
        Object purchasablePermissionObj = request.getAttribute( WebConstants.RequestKeys.PURCHASABLE_PERMISSION_TO_ADD );
        if(purchasablePermissionObj != null)
        {
            PurchasablePermission purchasablePermission = (PurchasablePermission)purchasablePermissionObj;
            courseDetailsFormCOI.setPurchasablePermissionsToAdd( new PurchasablePermission[]{ purchasablePermission } );
            
            //TODO: gcuevas: throw some exception if purchasablePermission is not academic
            
            courseDetails.setNumberOfStudents( purchasablePermission.getNumberOfStudents() );
            courseDetails.setEstimatedQty(courseDetails.getNumberOfStudents());
        }
        else
        {
        	throw new CCRuntimeException("purchasablePermission was not found in the request attributes");
        }
        
        courseDetailsFormCOI.setCourseDetails( courseDetails );
        courseDetailsFormCOI.setCascadingChanges( false );
        courseDetailsFormCOI.setOrderPurchase( null );
        
    }

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response)
    {
        CourseDetailsForm courseDetailsFormCOI = castForm( CourseDetailsForm.class, form );
        
        //TODO: gcuevas: check purchasable permission to add array to make sure length == 1
        PurchasablePermission purchasablePermission = courseDetailsFormCOI.getPurchasablePermissionsToAdd()[0];
        
        request.setAttribute( WebConstants.RequestKeys.TRANSACTION_ITEM, purchasablePermission );
        
        if(purchasablePermission.isSpecialOrder() || 
            CartServices.changedFromRegularOrderToSpecialOrder(purchasablePermission) )
            return mapping.findForward( CANCEL_SPECIAL );
        else 
        {
            request.setAttribute( WebConstants.RequestKeys.RECALCULATE_PRICE, Boolean.TRUE );
            return mapping.findForward( CANCEL_BASIC );
        }
    }

    @Override
    public ActionForward submit(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response)
    {
        CourseDetailsForm courseDetailsFormCOI = castForm( CourseDetailsForm.class, form );
        
        CartServices.setCourseDetails( courseDetailsFormCOI.getCourseDetails() );
        
        //Need to get the CourseDetails to get the Bundle ID
        CourseDetailsImpl courseDetailsImpl = (CourseDetailsImpl) CartServices.getCourseDetails();
        
        try
        {
            //TODO: gcuevas: check array (purchasablPErmissionToAdd) to make sure length == 1
            PurchasablePermission purchasablePermission = courseDetailsFormCOI.getPurchasablePermissionsToAdd()[0];
            
            //Set the Bundle Id from the CourseDetails on the item before adding to cart
            purchasablePermission.getItem().setBundleId(courseDetailsImpl.getBundle().getBundleId());
         
            /*************************************************************************************
            Comment this block - This logic is preventing from adding an Academic item into the Cart
            //when we already have another non academic item with the same Work Inst 
             *************************************************************************************/
         /* ************************************************************************  
            boolean itemInCart = false;
            //List itemsInCart = CartServices.getItemsInCart();
            List<PurchasablePermission> itemsInCart = CartServices.getRegularOrderItemsInCart();
            if(itemsInCart != null)
            {
                Iterator<PurchasablePermission> iterator = itemsInCart.iterator();
                while(iterator.hasNext())
                {
                    PurchasablePermission pp = iterator.next();
                    // if(pp.getWorkInst() == purchasablePermission.getWorkInst())
                    if (purchasablePermission.getExternalItemId()!=null && 
                    		pp.getExternalItemId().compareTo(purchasablePermission.getExternalItemId()) == 0)
                        itemInCart = true;
                }
            }
            if(!itemInCart) 
            **************************************************************************/
                CartServices.addItemToCart( purchasablePermission );
        
        }
        catch( ItemCannotBePurchasedException e )
        {
        	_logger.info(LogUtil.getStack(e));
            CartServices.resetCourseDetails();
            
            ActionMessages itemCannotBePurchasedError = TransactionUtils.getItemCannotBePurchasedError(e);
            saveErrors( request, itemCannotBePurchasedError );
            
            return mapping.findForward( SHOW_MAIN );
        }
        catch (Exception e)
        {
            _logger.error( LogUtil.getStack(e) );
            //rollback the course details change if the adding to the cart is unsuccessful
            CartServices.resetCourseDetails();
        }
        
        return mapping.findForward( SUBMIT );
    }

}
