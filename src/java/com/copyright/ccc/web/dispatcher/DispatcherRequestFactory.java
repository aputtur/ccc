package com.copyright.ccc.web.dispatcher;

/**
 * Generates instances of <code>DispatcherRequest</code>.
*/
public class DispatcherRequestFactory {

  private DispatcherRequestFactory(){}

  /**
   * Returns a new instance of <code>DispatcherRequest</code> for the input type and target
   * or null id the type/target match is not found
   */
  public static final DispatcherRequest getDispatcherRequestInstance( String type, String target ) {
  
    DispatcherRequest dispatcherRequest = null;
    
    if( ActionDispatcherRequest.DISPATCHER_ORDER_HISTORY_DETAIL_WITH_ID_REQUEST.isMyType( type, target ) )
    {
      return ActionDispatcherRequest.DISPATCHER_ORDER_HISTORY_DETAIL_WITH_ID_REQUEST;
    }
       
    else if( ActionDispatcherRequest.DISPATCHER_UPDATE_CART_FOR_EMULATED_USER.isMyType( type, target ) )
    {
      return ActionDispatcherRequest.DISPATCHER_UPDATE_CART_FOR_EMULATED_USER;
    }
       
    return dispatcherRequest;
   
  }
    
}
