package com.copyright.ccc.business.services.cart;

/**
 * @author Lucas Alberione
 * @created 26-Oct-2006 1:40:50 PM
 */
 
 /**
  * Abstract class that defines an abstraction of the details
  * involved when using an invoice as payment method in an
  * e-commerce transaction.
  */
public abstract class InvoiceDetails {

  /**
   * Returns the PO Number asociated with this <code>InvoiceDetails</code>.
   */
  public abstract String getPONumber();

	 /**
	  * Sets the PO Number asociated with this <code>InvoiceDetails</code>.
	  */
	public abstract void setPONumber(String PONumber);
  
	/**
	 * Returns the Promotion Code asociated with this <code>InvoiceDetails</code>.
	 */
	public abstract String getPromotionCode();
  
  /**
   * Sets the Promotion Code asociated with this <code>InvoiceDetails</code>.
   */
  public abstract void setPromotionCode( String promotionCode );
  
//	abstract void setInvoiceRequest(InvoiceRequest invoiceRequest);
	
//	abstract InvoiceRequest getInvoiceRequest();
	

}