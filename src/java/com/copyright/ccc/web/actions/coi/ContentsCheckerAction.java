package com.copyright.ccc.web.actions.coi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;

public class ContentsCheckerAction extends CCAction
{
    
    public void checkOrderContentType(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response ) throws IOException
    {
        response.setContentType("text/xml");
        response.setHeader( WebConstants.IS_AJAX_RESPONSE, "true" );
        
        String xmlResponse = "";
        xmlResponse = "<ajaxResponse>";
        
        String permissionType = request.getParameter(WebConstants.RequestKeys.SPECIAL_PERMISSION_TYPE);
        long orderPurchaseId = Long.parseLong(request.getParameter(WebConstants.RequestKeys.PURCHASE_ID));
        
        try
        {
            OrderPurchase orderPurchase = OrderPurchaseServices.getOrderPurchasesForConfNum( orderPurchaseId ).getOrderPurchase(0);
            
            boolean isSelectedAcademic =  
                permissionType.equals(WebConstants.PERMISSION_TYPE_APS) || permissionType.equals(WebConstants.PERMISSION_TYPE_ECCS);
                
            boolean isCompatibleWithOrder = (orderPurchase.isAcademic() == isSelectedAcademic);
                 
            if( isCompatibleWithOrder )
                xmlResponse += "<status>success</status>";
            else
                xmlResponse += "<status>failure</status>";
        }
        catch( Exception e )
        { 
        	_logger.error(LogUtil.getStack(e));
            xmlResponse += "<status>failure</status>";
        }
        
        xmlResponse += "</ajaxResponse>";
        
        PrintWriter out = response.getWriter();
        out.write(xmlResponse);
        out.close();
         
    }
}
