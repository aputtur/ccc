package com.copyright.ccc.web.actions;

//import com.copyright.ccc.config.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.CCCState;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.RegisterIndividualForm;
import com.copyright.ccc.web.forms.RegisterOrganizationForm;
import com.copyright.svc.tf.api.TFService;
import com.copyright.svc.tf.api.data.Province;
import com.copyright.svc.tf.api.data.State;
import com.copyright.svc.tf.api.data.TFConsumerContext;

public class RedisplayRegistrationAction extends CCAction {

    public ActionForward defaultOperation(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

        //logger.debug("in RedisplayRegistrationAction.execute");
    	
//    	String myTest = request.getMethod();
    	
    	if (request.getMethod().equalsIgnoreCase("GET"))
    	{
    		return (mapping.findForward("accessDenied"));
    	}
         TFService tfService = ServiceLocator.getTFService();          
         
          List<State> stateList = tfService.getAllStates(new TFConsumerContext());
          List<Province> provinceList = tfService.getAllProvinces(new TFConsumerContext());
          List<CCCState> stateList2 = new ArrayList <CCCState>() ;
          List<CCCState> provinceList2 = new ArrayList <CCCState>() ;
         
          State state = null;
          Province province = null;
         
            for (Iterator <State> i = stateList.iterator(); i.hasNext();)
            {          
                state = i.next();
                
                if (state != null)
                {
                     CCCState cccState = new CCCState();
                     cccState.setCode(state.getStateCode());
                     cccState.setName(state.getStateName());
                
                     stateList2.add(cccState);
                     
                }    
             }  
             
         for (Iterator <Province> j = provinceList.iterator(); j.hasNext();)
         {
             //    RLinkPublisher singlePub = (RLinkPublisher) i.next();
             
             province = j.next();
             
             if (province != null)
             {
                  CCCState cccProvince = new CCCState();
                  cccProvince.setCode(province.getProvinceCode());
                  cccProvince.setName(province.getProvinceName());
             
                  provinceList2.add(cccProvince);
                  
             }    
          }  
        
        if (mapping.getPath().equalsIgnoreCase("/redisplayIndRegistration"))
      {
        
        //logger.debug("in redisplay IND");
        RegisterIndividualForm registrationForm = castForm( RegisterIndividualForm.class, form );
                            
        registrationForm.setCountryList( tfService.getAllCountries(new TFConsumerContext()) ) ;
        registrationForm.setStateList( stateList2 );
        registrationForm.setProvinceList(provinceList2);
        
        request.setAttribute( "isRedisplay", Boolean.TRUE );
        request.getSession().setAttribute("RegisterIndividualForm", registrationForm);
        //request.getSession().setAttribute(CopConstants.COP_FORM, registrationForm);
        return (mapping.findForward("success"));
    }
    else if (mapping.getPath().equalsIgnoreCase("/redisplayOrgRegistration"))
    {
        //logger.debug("in redisplay ORG");
        RegisterOrganizationForm registrationForm = castForm( RegisterOrganizationForm.class, form );
                            
        registrationForm.setCountryList( tfService.getAllCountries(new TFConsumerContext()) ) ;
        registrationForm.setStateList( stateList2 );
        registrationForm.setProvinceList( provinceList2 );

        request.setAttribute( "isRedisplay", Boolean.TRUE );
        request.getSession().setAttribute("RegisterOrganizationForm", registrationForm);
        //request.getSession().setAttribute(CopConstants.COP_FORM, registrationForm);  
        return (mapping.findForward("success"));
    }
    return (mapping.findForward("success"));
  }
}