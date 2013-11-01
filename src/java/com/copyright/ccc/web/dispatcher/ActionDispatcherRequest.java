package com.copyright.ccc.web.dispatcher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.copyright.base.Constants;
import com.copyright.ccc.CCRuntimeException;


/**
 * Dispatcher class for hadling Struts Action requests.
 */
public class ActionDispatcherRequest implements DispatcherRequest
{


  private static final String[] LOGIN_PARMS = new String[] { DispatcherConstants.ND_ARG_USERNAME, DispatcherConstants.ND_ARG_PASSWORD };
  
  private static final String[] REQUIRED_PARMS_TYPE_AND_TARGET = new String[] { DispatcherConstants.ND_ARG_TYPE, DispatcherConstants.ND_ARG_TARGET };
  
  private static final boolean USE_REDIRECTION = true;
  private static final boolean USE_FORWARDING = false;
  
  private static final String COMMON_PATH = "/" ;

  private String _type = Constants.EMPTY_STRING;
  private String _target = Constants.EMPTY_STRING;
  private String _module = Constants.EMPTY_STRING;
  private String _doAction = Constants.EMPTY_STRING;
  private String _doOperation = Constants.EMPTY_STRING;
  private String _doArgument = Constants.EMPTY_STRING;
  private String[] _optionalParms = null;
  private String[] _requiredParms = null;
  private String[] _atLeastOneParms = null;
  private String[] _args = null;
  private boolean _useRedirect = true;


  //Constructors
  private ActionDispatcherRequest(){}

  private ActionDispatcherRequest( String type, 
                                   String target, 
                                   String module, 
                                   String doAction, 
                                   String doOperation, 
                                   String doArgument, 
                                   String[] optionalParms, 
                                   String[] requiredParms, 
                                   String[] atLeastOneParms,
                                   boolean useRedirection )
  {
    setType(type);
    setTarget(target);
    setModule(module);
    setDoAction(doAction);
    setDoOperation(doOperation);
    setDoArgument(doArgument);
    setOptionalParms(optionalParms);
    setRequiredParms(requiredParms);
    setAtLeastOneParms(atLeastOneParms);
    setUseRedirect( useRedirection );
  }

  //Constants

  /**
   * Dispatcher to access Order History for an specific order ID.
   */
  public static final ActionDispatcherRequest DISPATCHER_ORDER_HISTORY_DETAIL_WITH_ID_REQUEST = 
    new ActionDispatcherRequest(  DispatcherConstants.TYPE_ORDER, 
                                  DispatcherConstants.TARGET_ORDER_HISTDETID, 
                                  "", 
                                  "/orderView.do?", 
                                  "", 
                                  "id=$0", 
                                  DispatcherConstants.OPTIONAL_PARMS_NOT_SPECIFIED, 
                                  new String[] { DispatcherConstants.ND_ARG_TYPE, DispatcherConstants.ND_ARG_TARGET, DispatcherConstants.ND_ARG_ORDERID },
                                  DispatcherConstants.AT_LEAST_ONE_PARMS_NOT_SPECIFIED,
                                  USE_REDIRECTION );
 
  
 /**
  * See <code>DispatcherRequest.isMyType( String type, String target )</code>
  */
  public boolean isMyType(String type, String target)
  {
    return type.equalsIgnoreCase(getType()) && 
      target.equalsIgnoreCase(getTarget());
  }
  
  /**
   * See <code>DispatcherRequest.generateTargetURL(HttpServletRequest request)</code>
   */
   public String generateTargetURL(HttpServletRequest request)
   {
     String targetURL = DispatcherConstants.ND_DEFAULT_URL;

     if (hasAllNeededParms(request))
     {
       String doArguments = getDoArgument();
       if (getArgumentsValues() != null)
       {
         for (int idx = 2; idx < getArgumentsValues().length; idx++)
         {
           String rpStr = "\\$" + (idx - 2);
           doArguments = doArguments.replaceFirst(rpStr, getArgumentsValues()[idx]);
         }
       }
       targetURL = getModule() + getDoAction() + getDoOperation() + doArguments;

     }
     return targetURL;

   }
   
   /**
    * See <code>DispatcherRequest.isUseRedirect()</code>
    */
   public boolean isUseRedirect()
   {
     return _useRedirect;
   }
   
   /**
    * See <code>DispatcherRequest.isExternalResource()</code>
    */
   public boolean isExternalResource()
   {
     return false;
   }
 

  /**
     * Looks for the required parms in the request as well as the at-least-one parms
     * and any optional parms.
     * @param request
     * @return true if all the required parms are found in the request.
     */
  private boolean hasAllNeededParms(HttpServletRequest request)
  {
    boolean hasAllNeededParms = true;
    int allParmsIndex = -1;
    
    setArgumentsValues( new String[ getTotalNumberOfParamaters() ] );

    if (getRequiredParms() != null)
    {
      int numberOfRequiredParmsToBePresent = getRequiredParms().length;
      
      for (int requiredParmsIndex = 0; requiredParmsIndex < getRequiredParms().length; requiredParmsIndex++)
      {
        String currentRequiredParm = getRequiredParms()[requiredParmsIndex];
        String currentArgumentValue = getArgumentValue( request, currentRequiredParm );
        allParmsIndex++;
        getArgumentsValues()[allParmsIndex] = currentArgumentValue;
        
        if ( StringUtils.isNotBlank(currentArgumentValue) ) 
        { 
          numberOfRequiredParmsToBePresent--; 
        }
      }
      
      hasAllNeededParms = numberOfRequiredParmsToBePresent == 0;
    }

    if (getAtLeastOneParms() != null)
    {
      int atLeastOneParms = 0;
      for (int atLeastOneParmIndex = 0; atLeastOneParmIndex < getAtLeastOneParms().length; atLeastOneParmIndex++)
      {
        String currentAtLeastOneParm = getAtLeastOneParms()[atLeastOneParmIndex];
        String currentArgumentValue = getArgumentValue( request, currentAtLeastOneParm );
        allParmsIndex++;
        getArgumentsValues()[allParmsIndex] = currentArgumentValue;
        if (StringUtils.isNotBlank(currentArgumentValue))
        {
          atLeastOneParms++;
        }
      }
      hasAllNeededParms = atLeastOneParms > 0;
    }

    if (getOptionalParms() != null)
    {
      for (int odx = 0; odx < getOptionalParms().length; odx++)
      {
        String currentOptionalParm = getOptionalParms()[odx];
        String currentArgumentValue = getArgumentValue( request, currentOptionalParm );
        allParmsIndex++;
        getArgumentsValues()[allParmsIndex] = currentArgumentValue;
      }
    }

    return hasAllNeededParms;
  }


  private int getTotalNumberOfParamaters()
  {
    return ( getRequiredParms()   != null ? getRequiredParms().length   : 0 ) + 
           ( getOptionalParms()   != null ? getOptionalParms().length   : 0 ) + 
           ( getAtLeastOneParms() != null ? getAtLeastOneParms().length : 0 );
  }

  
    private String getArgumentValue( HttpServletRequest request, String argumentName )
    {
        // The parameter from the request will not be URL encoded.  So we 
        // re-encode, since RightsAdvisorAction.lookupHref() takes an argument 
        // (href) that must be URL encoded.
        
        try
        {
        
            String value = StringUtils.defaultString(request.getParameter( argumentName ));
            String encodedValue = URLEncoder.encode( value, "UTF-8" );
            String argumentValue = StringUtils.defaultString( encodedValue );
            
            return argumentValue;
        }
        catch ( UnsupportedEncodingException uee )
        {
            throw new CCRuntimeException( uee );
        }
    }

 

  private void setType(String type)
  {
    this._type = type;
  }

  private String getType()
  {
    return _type;
  }

  private void setTarget(String target)
  {
    this._target = target;
  }

  private String getTarget()
  {
    return _target;
  }

  public void setModule(String module)
  {
    this._module = module;
  }

  private String getModule()
  {
    return _module;
  }

  private void setDoAction(String doAction)
  {
    this._doAction = doAction;
  }

  private String getDoAction()
  {
    return _doAction;
  }

  private void setDoOperation(String doOperation)
  {
    this._doOperation = doOperation;
  }

  private String getDoOperation()
  {
    return _doOperation;
  }

  private void setDoArgument(String doArgument)
  {
    this._doArgument = doArgument;
  }

  private String getDoArgument()
  {
    return _doArgument;
  }

  private void setOptionalParms(String[] optionalParms)
  {
    this._optionalParms = optionalParms;
  }

  private String[] getOptionalParms()
  {
    return _optionalParms;
  }

  private void setRequiredParms(String[] requiredParms)
  {
    this._requiredParms = requiredParms;
  }

  private String[] getRequiredParms()
  {
    return _requiredParms;
  }

  private void setAtLeastOneParms(String[] atLeastOneParms)
  {
    this._atLeastOneParms = atLeastOneParms;
  }

  private String[] getAtLeastOneParms()
  {
    return _atLeastOneParms;
  }
  

  private void setArgumentsValues(String[] args)
  {
    this._args = args;
  }

  private String[] getArgumentsValues()
  {
    return _args;
  }

  
  private void setUseRedirect(boolean useRedirect)
  {
    this._useRedirect = useRedirect;
  }

  
  /**
   * Dispatcher to update cart for an emulated user.
   */
  public static final ActionDispatcherRequest DISPATCHER_UPDATE_CART_FOR_EMULATED_USER = 
	    new ActionDispatcherRequest(  DispatcherConstants.TYPE_UPDATE_CART, 
	                                  DispatcherConstants.TARGET_USER_ID, 
	                                  "", 
	                                  "/updateCart.do?", 
	                                  "", 
	                                  "cartId=$0", 
	                                  DispatcherConstants.OPTIONAL_PARMS_NOT_SPECIFIED, 
	                                  new String[] { DispatcherConstants.ND_ARG_TYPE, DispatcherConstants.ND_ARG_TARGET, DispatcherConstants.ND_ARG_CARTID },
	                                  DispatcherConstants.AT_LEAST_ONE_PARMS_NOT_SPECIFIED,
	                                  USE_REDIRECTION );
  
}
