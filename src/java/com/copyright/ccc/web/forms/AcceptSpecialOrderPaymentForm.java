package com.copyright.ccc.web.forms;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.svc.order.api.data.Item;

/**
 * Submit a user email address to perform user information lookup.
 * This is a user administrator form.
 * @author Jessop
 */
public class AcceptSpecialOrderPaymentForm extends BasePaymentForm
{
    /**
     * Form to handle the display/selection and payment
     * of unpaid invoices.
     */
    private static final long serialVersionUID = 1L;
    //private static final Logger _logger = Logger.getLogger(UnpaidInvoiceForm.class);

   

	private OrderLicense orderLicense;
	private Item item;
	
	
	private BigDecimal totalAmount=new BigDecimal(0);
	
    public OrderLicense getOrderLicense() {
		return orderLicense;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public void setOrderLicense(OrderLicense orderLicense) {
		this.orderLicense = orderLicense;
	}

	private String licenseId;
    public AcceptSpecialOrderPaymentForm() {
       super();
    }
    public String getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}



  

  

    @Override
    public String toString()
    {
        StringBuffer obuf = new StringBuffer();

        obuf.append("\n\nUnpaidInvoiceForm:\n");
        if (getPaymentMethod() == null && getPaymentType().equals(PAYMENT_TYPE_CREDIT_CARD))
        {
            obuf.append("\nCreditCardInfo is null.");
        }
        else
        {
            obuf.append("\nCardholder: ")
                .append(this.getCreditCardNameOn())
                .append("\nExp. Date: ")
                .append(this.getExpirationDate())
                .append("\nCard Type:  ")
                .append(this.getCcType());

                obuf.append("\nMode     :  ").append(getPaymentType());
        }
      /*  if (_selectedInvoices != null)
        {
            obuf.append("\n\nSelected Invoices:");

            for (int i = 0; i < _selectedInvoices.length; i++)
            {
                obuf.append("\nTransaction ID: ")
                    .append(_selectedInvoices[i]);
            }
        }*/
        obuf.append("\n");
        return obuf.toString();
    }

    @Override
    public void reset( ActionMapping      mapping
                     , HttpServletRequest request )
    {
       // _logger.info("\nUnpaidInvoiceForm.reset()\n");
       // _selectedInvoices = null;
//
        //((CreditCard) _creditCardInfo).clearSecureData();
        //_mode = MODE_INVOICES;
    }
	public void setItem(Item item) {
		this.item = item;
	}
	public Item getItem() {
		return item;
	}
    
   // public void clearSaveSelectedInvoices() { _saveSelectedInvoices = null; }

 
}