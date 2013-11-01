package com.copyright.ccc.business.services.cart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.CCRuntimeException;

/**
 * Exception class that indicates that an item
 * cannot be purchased (e.g. item is denied).
 */
public class ItemCannotBePurchasedException extends CCRuntimeException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PurchasablePermission _purchasblePermission = null;
  private String _message = null;
  
  private ItemCannotBePurchasedException(){};
  
  ItemCannotBePurchasedException(PurchasablePermission purchasablePermission, String message)
  {
    _purchasblePermission = purchasablePermission;
    _message = message;
  }

  /**
   * Returns the instance of <code>PurchasablePermission</code> that couldn't be purchased.
   */
  public PurchasablePermission getPurchasablePermission()
  {
    return _purchasblePermission;
  }
  
  /**
   * Returns a boolean indicating if the item cannot be purchased because it is in the public domain.
   */
   public boolean isInPublicDomain()
   {
        if( StringUtils.isEmpty( _message ) )
            return false;
    
        String publicDomainPattern = "permission=P";
        return matches( _message, publicDomainPattern );    
   }
   
    private boolean matches( String text, String textPattern ) 
    {
          
      Pattern pattern = Pattern.compile( textPattern, Pattern.CASE_INSENSITIVE );
      
      Matcher matcher = pattern.matcher( text );
      
      boolean matchFound = matcher.find();
          
      return matchFound;
    }
}
