package com.copyright.ccc.web.forms.coi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCValidatorForm;

public class EditCopiedOrderForm extends CCValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PurchasablePermission[] _permissionsToEdit;
    private PurchasablePermission[] _permissionsToCopy;
    private int _indexOfPermissionToRemove;

    public void setPermissionsToEdit(PurchasablePermission[] permissionsToEdit)
    {
        this._permissionsToEdit = permissionsToEdit;
    }

    public PurchasablePermission[] getPermissionsToEdit()
    {
        return _permissionsToEdit;
    }

    public void setPermissionsToCopy(PurchasablePermission[] permissionsToCopy)
    {
        this._permissionsToCopy = permissionsToCopy;
    }

    public PurchasablePermission[] getPermissionsToCopy()
    {
        return _permissionsToCopy;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, 
                                 HttpServletRequest request)
    {
        ActionErrors errors = super.validate(mapping, request);
        
        if( getOperation().equalsIgnoreCase("copyOrder") )
        {
            for( int i = 0; i < _permissionsToEdit.length; i++ )
            {
                PurchasablePermission currentPermission = _permissionsToEdit[i];
                
                String publicationTitle = currentPermission.getPublicationTitle();
                
                if( currentPermission.isPhotocopy() && currentPermission.getNumberOfCopies() <= 0)
                {
                    errors.add( ActionMessages.GLOBAL_MESSAGE, 
                                new ActionMessage("errors.mustBeGreaterThanZero", "number of copies", publicationTitle));
                }
                else if( currentPermission.isRepublication() && currentPermission.getCirculationDistribution() <= 0 )
                {
                    errors.add( ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage("errors.mustBeGreaterThanZero", "circulation/distribution", publicationTitle));
                }
                else if( currentPermission.isEmail() && currentPermission.getNumberOfRecipients() <= 0 )
                {
                    errors.add( ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage("errors.mustBeGreaterThanZero", "number of recipients", publicationTitle));
                }
                else if( currentPermission.isNet() && currentPermission.getDuration() == -1)
                {
                    errors.add( ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage("errors.mustBeGreaterThanZero", "duration of posting", publicationTitle));
                }
            }
        }
        
        return errors;
    }

    public void setIndexOfPermissionToRemove(int indexOfPermissionToRemove)
    {
        this._indexOfPermissionToRemove = indexOfPermissionToRemove;
    }

    public int getIndexOfPermissionToRemove()
    {
        return _indexOfPermissionToRemove;
    }
}
