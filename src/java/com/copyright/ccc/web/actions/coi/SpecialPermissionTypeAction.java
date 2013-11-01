package com.copyright.ccc.web.actions.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.coi.SpecialPermissionTypeForm;

public class SpecialPermissionTypeAction extends CCAction
{
    private static final String SPECIAL_ORDER = "specialOrder";
    
    public ActionForward defaultOperation(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response )
    {
        SpecialPermissionTypeForm specialPermissionTypeForm = castForm( SpecialPermissionTypeForm.class, form );
        
        specialPermissionTypeForm.setFormPath( mapping.getPath() );
        
        specialPermissionTypeForm.setPermissionType( WebConstants.PERMISSION_TYPE_ECCS );
        specialPermissionTypeForm.setRadioButton(WebConstants.PERMISSION_TYPE_ECCS);
        
        Object purchaseId = request.getAttribute( WebConstants.RequestKeys.PURCHASE_ID );
        if( purchaseId != null )
            specialPermissionTypeForm.setPurchaseId( (String)purchaseId );
        
        return mapping.findForward(SHOW_MAIN);
    }
    
    public ActionForward goToSpecialOrder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
    {
        SpecialPermissionTypeForm specialPermissionTypeForm = castForm( SpecialPermissionTypeForm.class, form );
        
        if (specialPermissionTypeForm.getPermissionType().equalsIgnoreCase("rls")) {
        	//return mapping.findForward("rlinkSpecialOrder");
        	String path = "/specialOrderRLink.do?operation=defaultOperation";
            
            ActionForward forward = null;
            
            forward =  new ActionForward("submit", path, true, null);
            
            return forward;
        }
                        
        String purchaseId = specialPermissionTypeForm.getPurchaseId();
        if( StringUtils.isNotEmpty(purchaseId) )
            request.setAttribute( WebConstants.RequestKeys.PURCHASE_ID, purchaseId );
        
        request.setAttribute( WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE, specialPermissionTypeForm.getPermissionType() );
        
        return mapping.findForward( SPECIAL_ORDER );
    }

}
