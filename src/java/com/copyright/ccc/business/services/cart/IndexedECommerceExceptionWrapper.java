package com.copyright.ccc.business.services.cart;

import com.copyright.ccc.CCException;

/**
 * Class that wraps an instance of <code>CCException</code> 
 * and asociates an index number to it.
 */
public class IndexedECommerceExceptionWrapper
{
  private int _index;
  private CCException _eCommerceException;

  private IndexedECommerceExceptionWrapper(){}

  IndexedECommerceExceptionWrapper( int index, 
                                    CCException eCommerceException )
  {
      setIndex( index );
      setECommerceException( eCommerceException );
  }


  /**
   * Returns the index.
   */
  public int getIndex()
  {
    return _index;
  }

  /**
   * Returns the wrapped <code>CCException</code> instance.
   */
  public CCException getECommerceException()
  {
    return _eCommerceException;
  }
  
  private void setIndex( int index )
  {
    this._index = index;
  }
  
  private void setECommerceException( CCException eCommerceException )   
  {
    this._eCommerceException = eCommerceException;
  }
}
