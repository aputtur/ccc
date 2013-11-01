package com.copyright.ccc.business.services.cart;

import com.copyright.data.ValidationException;

public class InvoiceValidationException extends ECommerceValidationException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private InvoiceDetails _invoiceDetails;
  
  private InvoiceValidationException(){}
  
  InvoiceValidationException( ValidationException validationException,
                              InvoiceDetails invoiceDetails )
  {
    super( validationException );
    
    setInvoiceDetails( invoiceDetails );
  }

  private void setInvoiceDetails(InvoiceDetails invoiceDetails)
  {
    this._invoiceDetails = invoiceDetails;
  }

  public InvoiceDetails getInvoiceDetails()
  {
    return _invoiceDetails;
  }
}
