package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.List;

import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.payment.CyberSourceUtils;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo;

/**
 * 
 * @author APuttur
 */
public class CreditCardListForm extends CCValidatorForm
{
    /**
     * Form to handle the display/selection and payment
     * of unpaid invoices.
     */
    private static final long serialVersionUID = 1L;
  
    //private static final Logger _logger = Logger.getLogger(UnpaidInvoiceForm.class);
    private boolean cybersourceSiteUp = SystemStatus.isCybersourceSiteUp();
    private boolean displayRadioButtons=true;
	public void setCybersourceSiteUp(boolean cybersourceSiteUp) {
		this.cybersourceSiteUp = cybersourceSiteUp;
	}
	public boolean isCybersourceSiteUp() {
		return cybersourceSiteUp;
	}
    
	private List<PaymentProfileInfo> creditCards;
   

	public void setCreditCards(List<PaymentProfileInfo> creditCards) {
		this.creditCards = creditCards;
	}



	public List<PaymentProfileInfo> getCreditCards() {
		if(creditCards==null) {
			creditCards=new ArrayList<PaymentProfileInfo>();
		}
		return creditCards;
	}
	
	
	public boolean getCreditCardExpired(String expiryDate) {
		return CyberSourceUtils.isCreditCardExpired(expiryDate);
	}
	public void setDisplayRadioButtons(boolean displayRadioButtons) {
		this.displayRadioButtons = displayRadioButtons;
	}
	public boolean getDisplayRadioButtons() {
		return displayRadioButtons;
	}

   


}