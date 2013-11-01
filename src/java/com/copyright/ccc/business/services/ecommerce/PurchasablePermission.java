package com.copyright.ccc.business.services.ecommerce;

import java.util.Date;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.search.RightAdapter;
import com.copyright.data.order.PermissionRequest;

/**
 * @author Lucas Alberione
 * @created 25-Oct-2006 9:39:58 AM
 */

 /**
  * Represents an abstraction of permission that can be purchased in Copyright.com 2.0.
  */
public abstract class PurchasablePermission implements TransactionItem
{

	private static final long serialVersionUID = 1L;
	
  abstract public PermissionRequest getPermissionRequest();

  abstract void setPermissionRequest( PermissionRequest permissionRequest );

  abstract public void setPublicationStartDate( Date publicationStartDate );
  
  abstract public void setPublicationEndDate( Date publicationEndDate );
  
  abstract void setExternalCommentTerm( String term );
  
  public abstract void setRightholderFee( double rightholderFee );
  
  abstract public void setPushToTFFailed(boolean status);
  
  abstract public boolean isPushToTFFailed();
  
  abstract public RightAdapter getRightFromWeb();
  
  abstract public void setRightFromWeb(RightAdapter right);
  
  //abstract public void setRightFromWeb(RRRight rrright);
}
