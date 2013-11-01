package com.copyright.ccc.web.actions.coi;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.services.cart.CannotBeAddedToCartException;
import com.copyright.ccc.business.services.cart.CartServices;
import com.copyright.ccc.business.services.cart.CourseNotDefinedException;
import com.copyright.ccc.business.services.cart.ItemCannotBePurchasedException;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.EditCopiedOrderForm;
import com.copyright.ccc.web.util.WebUtils;

public class EditCopiedOrderAction extends CCAction
{

    private static final String CART = "cart";
    private static final String CART_WITH_ERRORS = "cartWithErrors";
    
    public ActionForward defaultOperation(ActionMapping mapping, ActionForm form, 
                                      HttpServletRequest request, 
                                      HttpServletResponse response)
    {
        EditCopiedOrderForm editCopiedOrderForm = castForm( EditCopiedOrderForm.class, form );
        
        PurchasablePermission[] permissionsToEdit = 
            getPermissionsArrayFromRequest( request, WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT );
        editCopiedOrderForm.setPermissionsToEdit( permissionsToEdit );
        
        PurchasablePermission[] permissionsToCopy = 
            getPermissionsArrayFromRequest( request, WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_COPY );
        editCopiedOrderForm.setPermissionsToCopy( permissionsToCopy );
        
        request.getSession().setAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_EDIT , null);
        request.getSession().setAttribute(WebConstants.SessionKeys.PURCHASABLE_PERMISSIONS_NON_ACADEMIC_TO_COPY , null);
        
        return mapping.findForward( SHOW_MAIN );
    }
    
    public ActionForward removePermission(ActionMapping mapping, ActionForm form, 
                                      HttpServletRequest request, 
                                      HttpServletResponse response)
    {
        EditCopiedOrderForm editCopiedOrderForm = castForm( EditCopiedOrderForm.class, form );
        
        PurchasablePermission[] oldPermissionsToEdit = editCopiedOrderForm.getPermissionsToEdit();
        int indexOfPermissionToRemove = editCopiedOrderForm.getIndexOfPermissionToRemove();
        
        if( indexOfPermissionToRemove < oldPermissionsToEdit.length )
        {
            PurchasablePermission[] newPermissionsToEdit = new PurchasablePermission[ oldPermissionsToEdit.length - 1];
            int newPermissionsToEditIndex = 0;
            for(int i = 0; i < oldPermissionsToEdit.length; i++)
            {
                if( i != indexOfPermissionToRemove)
                {
                    newPermissionsToEdit[newPermissionsToEditIndex] = oldPermissionsToEdit[i];
                    newPermissionsToEditIndex++;
                }
            }
            
            editCopiedOrderForm.setPermissionsToEdit( newPermissionsToEdit );
        }
        
        return mapping.findForward( SHOW_MAIN );
    }
    
    public ActionForward copyOrder(ActionMapping mapping, ActionForm form, 
                                      HttpServletRequest request, 
                                      HttpServletResponse response)
    {
        EditCopiedOrderForm editCopiedOrderForm = castForm( EditCopiedOrderForm.class, form );
        
        PurchasablePermission[] purchasablePermissionsToCopy = 
            getAllPermissionsToCopy( editCopiedOrderForm.getPermissionsToEdit(), editCopiedOrderForm.getPermissionsToCopy() );
        
        ArrayList<String> publicationPermissionTitlesUnableToBeCopied = new ArrayList<String>();
        
        for(int i = 0; i < purchasablePermissionsToCopy.length; i++)
        {
            PurchasablePermission purchasablePermissionToCopy = purchasablePermissionsToCopy[i];
            
            try
            {
                CartServices.addItemToCart( purchasablePermissionToCopy );
            } 
            catch (CourseNotDefinedException e)
            {
                //TODO: gcuevas
                //shouldn't be thrown
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
        
        WebUtils.clearActionFormFromSession( request, mapping );
        
        //Price the items before to avoid price update message in cart page
        CartServices.updateCartForTFAcademicPriceChange();
        CartServices.updateCartForTFNonAcademicPriceChange();
        
        if( !publicationPermissionTitlesUnableToBeCopied.isEmpty() )
        {
            request.setAttribute( WebConstants.RequestKeys.COPY_ORDER_ERROR_PUBLICATION_TITLES, publicationPermissionTitlesUnableToBeCopied );
            return mapping.findForward( CART_WITH_ERRORS );
        }
        else
            return mapping.findForward( CART );
    }
    
    private PurchasablePermission[] getPermissionsArrayFromRequest( HttpServletRequest request, String attributeName )
    {
        Object permissionObject = request.getSession().getAttribute( attributeName );
        
        if( permissionObject == null )
            return new PurchasablePermission[0];
        
        else return (PurchasablePermission[])permissionObject;
    }
    
    private PurchasablePermission[] getAllPermissionsToCopy( PurchasablePermission[] editedPermissions, PurchasablePermission[] copiedPermissions )
    {
        PurchasablePermission[] allPermissions = new PurchasablePermission[ editedPermissions.length + copiedPermissions.length ];
        
        System.arraycopy( editedPermissions, 0, allPermissions, 0, editedPermissions.length );
        System.arraycopy( copiedPermissions, 0, allPermissions, editedPermissions.length, copiedPermissions.length );
        
        return allPermissions;
    }
}
