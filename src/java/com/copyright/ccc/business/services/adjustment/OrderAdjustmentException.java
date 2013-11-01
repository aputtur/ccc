package com.copyright.ccc.business.services.adjustment;

/**
 * Exception class to indicate exceptional order adjustment related events.
 */
public class OrderAdjustmentException extends RuntimeException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private OrderAdjustmentException(){}
    
  OrderAdjustmentException( String message )
  {
    super( message ); 
  }
  
  OrderAdjustmentException(String message, Throwable cause) 
  {
      super(message,cause);
  }
  
  public OrderAdjustmentException(Throwable cause)
  {
      super(cause);
  }
}
