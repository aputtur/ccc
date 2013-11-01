package com.copyright.ccc.web.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.WebConstants;
import com.copyright.ccc.web.forms.RegisterIndividualForm;
import com.copyright.data.account.RegistrationChannel;

public class ShowIndRegistrationAction extends CCAction
{
  public static final String TRUE = "T";
  public static final String FALSE = "F";
  
  public ActionForward defaultOperation(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException 
  {
  
    
  //Using this user's party id, get all the information including name, company name,
  //address, phone/fax, billing info etc.
  
  //logger.info("In ShowIndRegistrationAction.execute");
  //Comment this block for now.....
  
  if (mapping.getPath().equalsIgnoreCase("/showIndRegistration"))
  {
  /*
  try 
  {
    //logger.info("Inside ShowIndRegistrationAction.execute TRY");
    
        
    try 
    { 
    */
      
       // logger.info("Before findUserByName: ");
        
        User sessionUser = UserContextService.getSharedUser();
        
                
        if (sessionUser != null)
        {     
                            
              if (sessionUser.getUserChannel().equalsIgnoreCase(RegistrationChannel.COPYRIGHT_COM_IND.getCode())) 
              {
                                          
                  RegisterIndividualForm regFormInd = castForm( RegisterIndividualForm.class, form );
                  regFormInd.setRegisterIndividualForm(sessionUser);
                  regFormInd.setStatus("UPDATE");
                  regFormInd.setIsLoggedIn("TRUE");
                  request.setAttribute(mapping.getAttribute(), regFormInd);
                  request.getSession().setAttribute(WebConstants.SessionKeys.REGISTER_INDV_FORM, regFormInd);
                  return mapping.findForward("success"); 
              
              }
              
          }
          else
          {
              //logger.info("No Session User is available....");
          }                                            
    
   
  }
     return mapping.findForward("success");
  } 
     
}