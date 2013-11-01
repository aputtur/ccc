package com.copyright.ccc.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.web.forms.ManageCreditCardsForm;
import com.copyright.ccc.web.util.WebUtils;


public class ManageCreditCardsAction extends BasePaymentAction
{

	
	@Override
    public ActionForward execute( ActionMapping       mapping
                                , ActionForm          form
                                , HttpServletRequest  request
                                , HttpServletResponse response )
    {
		ManageCreditCardsForm manageCreditCardFrm = WebUtils.castForm( ManageCreditCardsForm.class, form );
        
        	return super.execute(mapping, manageCreditCardFrm, request, response);

	}
	
	@Override
	public String getPaymentFormActionPath() {
		return "manageCreditCards.do";
	}

}