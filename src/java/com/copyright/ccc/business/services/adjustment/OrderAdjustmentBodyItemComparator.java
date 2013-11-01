package com.copyright.ccc.business.services.adjustment;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator implementation used to compare instances of <code>OrderAdjustmentBodyItem</code>
 */
public class OrderAdjustmentBodyItemComparator implements Comparator<String>, Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public OrderAdjustmentBodyItemComparator()
  {
  }

  /**
   * See <code>public int java.util.Comparator.compare(Object, Object)</code>
   */
  public int compare(String o1, String o2)
  {
    if( o1 == null || o2 == null )
    {
      throw new IllegalArgumentException( "Non-null objects must be provided" );
    }
    
    long long1 = Long.parseLong(o1);
    long long2 = Long.parseLong(o2);
    
    if( long1 < long2)
    {
      return -1;
    }
    
    if( long1 == long2)
    {
      return 0;
    }
    
    if( long1 > long2)
    {
      return 1;
    }
    
    throw new IllegalArgumentException("Can only compare strings");
    
    
  }
}
