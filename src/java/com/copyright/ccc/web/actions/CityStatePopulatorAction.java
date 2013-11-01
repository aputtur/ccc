package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.TFConsumerContext;
import com.copyright.svc.tf.api.data.ZipCode;

public class CityStatePopulatorAction extends CCAction
{

    public void fetchCityAndState( ActionMapping ActionMapping, 
                                  ActionForm ActionForm, 
                                  HttpServletRequest request, 
                                  HttpServletResponse response ) throws IOException
    {
        String zipCode = request.getParameter("zipCode");
        String city = "Trial City";
        String state = "MA";
         
         ZipCode zip = new ZipCode();
        
        TFService tfService = ServiceLocator.getTFService();

      
            //fix for cc-911 - we do not handle more than 5 digits in the database. Thus we can not differentiate  towns in
            // the same zip code (ex. 03833 Exeter, Brentwood, & Kingston, New Hampshire, the database will
            // return Exeter.)
            
            //limit the zipcode to the first 5 digits
            String limitedZipcode;
            limitedZipcode = WebUtils.isAllDigit(zipCode.substring(0, 5)) ? zipCode.substring(0, 5) : zipCode;
            try
              {
                  zip = tfService.getZipCodeByZipCode(new TFConsumerContext(), limitedZipcode);                         
              } 
            catch (Exception e)
              {
                  ActionMessages errors = new ActionMessages();
                  errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("ZipCode Data not available", false));
                  saveErrors(request, errors);
              }
                    
                     if (zip != null)
                     {
                     
                        if (zip.getCity() != null)
                        {
                            city = zip.getCity();
                        }
                        else
                        {
                            city = "N/A";
                        }
                        
                        if (zip.getStateCode() != null)
                        {
                            state = zip.getStateCode();
                        }
                        else
                        {
                            state = "NA";
                        }
                     }
        
                
        StringBuffer xmlResponseBuffer = new StringBuffer();
        xmlResponseBuffer.append("<ajaxResponse>");
        xmlResponseBuffer.append("<city>" + city + "</city><state>" + state + "</state>");
        xmlResponseBuffer.append( "</ajaxResponse>" );
        
        response.setContentType("text/xml");
        response.setHeader( WebConstants.IS_AJAX_RESPONSE, "true" );
         
        PrintWriter out = response.getWriter();
        out.write(xmlResponseBuffer.toString());
        out.close();
    }
}
