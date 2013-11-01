package com.copyright.ccc.web.actions.coi;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.CCAction;
import com.copyright.ccc.web.forms.coi.AddNewCardActionForm;

public class AddNewCardAction extends  CCAction{
	 /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    
    public ActionForward defaultOperation( ActionMapping mapping, 
            ActionForm form, 
            HttpServletRequest request, 
            HttpServletResponse response )throws Exception{
    		try{
    			AddNewCardActionForm addNewCardForm = (AddNewCardActionForm)form;
    			return mapping.findForward(SUCCESS);
    		}catch(Exception e){
            _logger.error(LogUtil.getStack(e));
            return mapping.findForward(FAILURE);    
        }
    	//return selectPayment( mapping, form, request, response );
    }
    
   }
