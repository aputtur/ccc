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
import com.copyright.ccc.web.forms.RegisterAddUserForm;
import com.copyright.ccc.web.forms.RegisterGPOUserForm;
import com.copyright.ccc.web.forms.RegisterOrganizationForm;
import com.copyright.data.account.RegistrationChannel;

public class ShowRegistrationAction extends CCAction
{
	
  public ActionForward defaultOperation(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException 
  {
  
    
      
  if (mapping.getPath().equalsIgnoreCase("/showRegistration"))
  {
                          
                User sessionUser = UserContextService.getSharedUser();
                                                               
                if (sessionUser != null)
                {
                    String sessionChannel, actChannel;
                    
                    sessionChannel = sessionUser.getUserChannel();
                    actChannel = RegistrationChannel.COPYRIGHT_COM_IND.getCode();
                    
                    if (sessionChannel != null)
                    {
                    if (sessionChannel.equalsIgnoreCase(actChannel))
                      {
                    	_logger.info("Redirecting to Ind Registration.....");
                        return mapping.findForward("showIndRegistration");
                      }
                      
                      if (sessionUser.getUserChannel().equalsIgnoreCase( 
                                                          RegistrationChannel.COPYRIGHT_COM_ORG.getCode()) ) 
                        {
                                RegisterOrganizationForm regForm = new RegisterOrganizationForm( sessionUser );
                                regForm.setStatus("UPDATE");
                                regForm.setIsLoggedIn("TRUE");
                                                                 
                                request.getSession().setAttribute(WebConstants.SessionKeys.REGISTER_ORG_FORM, regForm);
                                request.setAttribute(mapping.getAttribute(), regForm);
                          
                                return mapping.findForward("success");
                        }
                      else if (sessionUser.getUserChannel().equalsIgnoreCase( 
                                                          RegistrationChannel.COPYRIGHT_COM_ORG_ADDLN_USER.getCode()) )
                        {
                          //Do Add User stuff
                          RegisterAddUserForm   regForm = new RegisterAddUserForm(sessionUser);
                          regForm.setStatus("UPDATE");
                          regForm.setIsLoggedIn("TRUE");
                          
                          request.getSession().setAttribute(WebConstants.SessionKeys.REGISTER_NEW_USER_FORM, regForm);
                          request.setAttribute(mapping.getAttribute(), regForm);
                          return mapping.findForward("displayAddUser");
                        }
                      else if (sessionUser.getUserChannel().equalsIgnoreCase( 
                                                          RegistrationChannel.COPYRIGHT_COM_GPO.getCode()) )
                        {
                          //Do GPO User stuff
                          RegisterGPOUserForm   regForm = new RegisterGPOUserForm(sessionUser);
                          regForm.setStatus("UPDATE");
                          regForm.setIsLoggedIn("TRUE");
                          
                          request.getSession().setAttribute(WebConstants.SessionKeys.REGISTER_GPO_USER_FORM, regForm);
                          request.setAttribute(mapping.getAttribute(), regForm);
                          return mapping.findForward("displayGPOUser");
                        }
                      
                  }
                  else
                    {
                      //logger.debug("No Session user available...");
                    }
              
              
            }
  		}
                                                    
            return mapping.findForward("success");
             
        }
        
    
  }
  