package com.copyright.ccc.web.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.user.User;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.CreditCardListForm;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo;
import com.copyright.svc.userInfo.api.cyberSource.data.SortCriteria;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo.PaymentProfileInfoKeys;
import com.copyright.svc.userInfo.api.data.UserInfoConsumerContext;
import com.copyright.workbench.logging.LoggerHelper;

public  class CreditCardListAction extends CCAction
{
    protected static final String SUCCESS = "success";
    protected static final String FAILURE = "failure";
	
	
	
 	private static final Logger _logger = LoggerHelper.getLogger();
 	
 	
 	  public ActionForward defaultOperation(ActionMapping mapping,
              ActionForm form,
              HttpServletRequest request,
              HttpServletResponse response )
{
 		  return retrieveCreditCardList( mapping,
               form,
               request,
               response);
}
 	
 	
     public ActionForward disableCreditCard(ActionMapping mapping, 
             ActionForm form, 
             HttpServletRequest request, 
             HttpServletResponse response ) throws IOException

             {
    	 		
    	 		String profileid = request.getParameter("profileid");
    	 		    	 		
    	 		try{
    	 			UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();
					PaymentProfileInfo query = new PaymentProfileInfo();
					query.setPaymentProfileId(profileid);
					List<PaymentProfileInfo> results = ServiceLocator.getCyberSourceService().findPaymentProfiles(consumerCtx,query, 0, 25, null);
					 if(results.size() == 1){
			            	PaymentProfileInfo updatedRecord = new PaymentProfileInfo();
			            	updatedRecord.setCccProfileId(results.get(0).getCccProfileId());
							updatedRecord.setAccountNumber(results.get(0).getAccountNumber());
							updatedRecord.setCardholderName(results.get(0).getCardholderName());
							updatedRecord.setPaymentProfileId(results.get(0).getPaymentProfileId());
							updatedRecord.setServiceProviderId(results.get(0).getServiceProviderId());
							updatedRecord.setUserId(results.get(0).getUserId());
							updatedRecord.setCardType(results.get(0).getCardType());
							updatedRecord.setExpirationDate(results.get(0).getExpirationDate());
							updatedRecord.setLastFourDigits(results.get(0).getLastFourDigits());
							updatedRecord.setCreateDate(results.get(0).getCreateDate());
							updatedRecord.setUpdateDate(results.get(0).getUpdateDate());
							updatedRecord.setStatus(results.get(0).getStatus());
							updatedRecord.setStatus("disabled");
							updatedRecord.setPaymentProfileId(profileid);
							updatedRecord.setUpdateDate(new Date());
							
							PaymentProfileInfo criteria = new PaymentProfileInfo();
							criteria.setCccProfileId(results.get(0).getCccProfileId());
							ServiceLocator.getCyberSourceService().updatePaymentProfiles(consumerCtx, criteria, updatedRecord, true);
							retrieveCreditCardList( mapping,form, request, response);
			            }
					
				}catch (CCRuntimeException e){
					_logger.error(ExceptionUtils.getFullStackTrace(e));
					addError(request, "errors.credit-card-general");
					// add error message here
					return mapping.findForward(FAILURE);
    	 		}
				return mapping.findForward(SUCCESS);
				
    	 	}
     
   
     
     public ActionForward retrieveCreditCardList(ActionMapping mapping, 
             ActionForm form, 
             HttpServletRequest request, 
             HttpServletResponse response ) 

             {
    	 		CreditCardListForm frm = castForm( CreditCardListForm.class, form );
    	 		User cccUser = UserContextService.getSharedUser();
    			String userid = "";
    			    			
    			InternetAddress email = cccUser.getEmailAddress();
    		    
    		    if ( email != null)
    		    {
    		        userid = email.getAddress() ;
    		    }
    		    else
    		    {
    		        userid = "";
    		    }
    	 		
    	 		UserInfoConsumerContext consumerCtx = new UserInfoConsumerContext();
    	 		try{
    	 			List<PaymentProfileInfo> creditCards = null;
        	 		PaymentProfileInfo query = new PaymentProfileInfo();
        	 		query.setUserId(userid);
        	 		query.setStatus("enabled");
        	 		List<SortCriteria> sort = new ArrayList<SortCriteria>();
        	 		SortCriteria sc = new SortCriteria(PaymentProfileInfoKeys.CREATE_DATE);
        	 		sc.setSequence(-1);
                    sort.add(sc);
                    List<String> fields = new ArrayList<String>();
                    
    	 			        	 		
        	 		fields.add(PaymentProfileInfoKeys.CCC_PROFILE_ID);
                    fields.add(PaymentProfileInfoKeys.ACCOUNT_NO);
                    fields.add(PaymentProfileInfoKeys.CARDHOLDER_NAME);
                    fields.add(PaymentProfileInfoKeys.PAYMENT_PROFILE_ID);
                    fields.add(PaymentProfileInfoKeys.CARD_TYPE);
                    fields.add(PaymentProfileInfoKeys.EXPIRATION_DATE);
                    fields.add(PaymentProfileInfoKeys.LAST_FOUR);
                    fields.add(PaymentProfileInfoKeys.CREATE_DATE);
                    fields.add(PaymentProfileInfoKeys.UPDATE_DATE);
                    fields.add(PaymentProfileInfoKeys.STATUS);
                    creditCards =ServiceLocator.getCyberSourceService().findPaymentProfiles(consumerCtx,query, 0, 25, sort);
                    frm.setCreditCards(creditCards);
        	 		
    	 		}catch (Exception e){
    	 			_logger.error(ExceptionUtils.getFullStackTrace(e));
    	 		      addError(request, "errors.credit-card-general");
    	 		     return mapping.findForward(FAILURE);
    	 		}
    	 		return mapping.findForward(SUCCESS);
    	 	}




     protected void addError(HttpServletRequest r, String s)
     {
         ActionErrors errors = new ActionErrors();

         errors.add(
             ActionMessages.GLOBAL_MESSAGE,
             new ActionMessage(s)
         );
         r.setAttribute(Globals.ERROR_KEY, errors);
     }




     
 
		
		  

	
}