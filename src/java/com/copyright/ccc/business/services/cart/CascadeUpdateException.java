package com.copyright.ccc.business.services.cart;

import java.util.List;

import com.copyright.base.CCCException;

/**
 * Represents exceptional cases occured during a cascade update e-commerce transaction.
 */
public final class CascadeUpdateException extends CCCException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  private List<IndexedECommerceExceptionWrapper> _invalidAttributesExceptions;
  private List<IndexedECommerceExceptionWrapper> _deniedLimitsExceededExceptions;
  private List<IndexedECommerceExceptionWrapper> _specialOrderLimitsExceededExceptions;
  private List<IndexedECommerceExceptionWrapper> _contactRHDirectlyLimitsExceededExceptions;
  private List<IndexedECommerceExceptionWrapper> _changedToRegularOrderExceptions;
  
  
  CascadeUpdateException( List<IndexedECommerceExceptionWrapper> invalidAttributesExceptions, 
                          List<IndexedECommerceExceptionWrapper> deniedLimitsExceededExceptions,
                          List<IndexedECommerceExceptionWrapper> specialOrderLimitsExceededExceptions,
                          List<IndexedECommerceExceptionWrapper> contactRHDirectlyLimitsExceededExceptions,
                          List<IndexedECommerceExceptionWrapper> changedToRegularOrderExceptions)
  {
    setInvalidAttributesExceptions( invalidAttributesExceptions );
    setDeniedLimitsExceededExceptions( deniedLimitsExceededExceptions );
    setSpecialOrderLimitsExceededExceptions( specialOrderLimitsExceededExceptions );
    setContactRHDirectlyLimitsExceededExceptions( contactRHDirectlyLimitsExceededExceptions );
    setChangedToRegularOrderExceptions( changedToRegularOrderExceptions );
  }
  
  private CascadeUpdateException(){}

  private void setInvalidAttributesExceptions(List<IndexedECommerceExceptionWrapper> invalidAttributesExceptions)
  {
    this._invalidAttributesExceptions = invalidAttributesExceptions;
  }

  /**
   * Returns a <code>List</code> of <code>InvalidAttributesException</code>
   * that occured during a cascade update e-commerce transaction. 
   */
  public List<IndexedECommerceExceptionWrapper> getInvalidAttributesExceptions()
  {
    return _invalidAttributesExceptions;
  }
  

  /**
   * Returns a <code>List</code> of <code>DeniedLimitsExceededException</code>
   * that occured during a cascade update e-commerce transaction. 
   */
  public List<IndexedECommerceExceptionWrapper> getDeniedLimitsExceededExceptions()
  {
    return _deniedLimitsExceededExceptions;
  }
  
  

  /**
   * Returns a <code>List</code> of <code>SpecialOrderLimitsExceededException</code>
   * that occured during a cascade update e-commerce transaction. 
   */
  public List<IndexedECommerceExceptionWrapper> getSpecialOrderLimitsExceededExceptions()
  {
    return _specialOrderLimitsExceededExceptions;
  }
  
  /**
   * Returns a <code>List</code> of <code>ContactRHDirectlyLimitsExceededException</code>
   * that occured during a cascade update e-commerce transaction. 
   */
  public List<IndexedECommerceExceptionWrapper> getContactRHDirectlyLimitsExceededExceptions()
  {
    return _contactRHDirectlyLimitsExceededExceptions;
  }
  
  public List<IndexedECommerceExceptionWrapper> getChangedToRegularOrderExceptions()
  {
    return _changedToRegularOrderExceptions;
  }
  
  private void setDeniedLimitsExceededExceptions(List<IndexedECommerceExceptionWrapper> deniedLimitsExceededExceptions)
  {
    this._deniedLimitsExceededExceptions = deniedLimitsExceededExceptions;
  }

  private void setSpecialOrderLimitsExceededExceptions(List<IndexedECommerceExceptionWrapper> specialOrderLimitsExceededExceptions)
  {
    this._specialOrderLimitsExceededExceptions = specialOrderLimitsExceededExceptions;
  }
  
  private void setContactRHDirectlyLimitsExceededExceptions(List<IndexedECommerceExceptionWrapper> contactRHDirectlyLimitsExceededExceptions)
  {
    this._contactRHDirectlyLimitsExceededExceptions = contactRHDirectlyLimitsExceededExceptions;
  }

  private void setChangedToRegularOrderExceptions(List<IndexedECommerceExceptionWrapper> changedToRegularOrderExceptions)
  {
    this._changedToRegularOrderExceptions = changedToRegularOrderExceptions;
  }

  
}
