package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
//import com.copyright.data.order.License;
//import com.copyright.service.order.OrderMgmtServiceAPI;
//import com.copyright.service.order.OrderMgmtServiceFactory;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.TFConsumerContext;

public class TFOdtStatusAction extends CCAction
{

    public void fetchOdtStatus( ActionMapping ActionMapping, 
                                  ActionForm ActionForm, 
                                  HttpServletRequest request, 
                                  HttpServletResponse response ) throws IOException
    {
        String orderDetail = request.getParameter("odtInst");
        String Status = "";
                               
        TFService tfService = ServiceLocator.getTFService();
                      
        Status = tfService.getOderDetailStatusByOdtInst(new TFConsumerContext(), Long.valueOf(orderDetail)).toString();
                       
        PrintWriter out = response.getWriter();
        out.println(Status.trim());
        out.flush();
    }
}
