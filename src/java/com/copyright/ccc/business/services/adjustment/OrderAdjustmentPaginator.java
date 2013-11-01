package com.copyright.ccc.business.services.adjustment;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class responsible of the pagination of an instance of <code>OrderAdjustment</code>
 */
public class OrderAdjustmentPaginator  implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public final static int DEFAULT_ITEMS_PER_PAGE = 10;
  
  private int _numberOfItemsPerPage;
  private int _currentPageNumber = 1;
  private OrderAdjustment _adjustment ;
  private boolean _showModifiedAdjustmentsOnly = false;
  
  private OrderAdjustmentPaginator(){}
  
  public OrderAdjustmentPaginator( OrderAdjustment adjustment, int numberOfItemsPerPage )
  {
    if( adjustment == null || adjustment.getBody() == null )
    {
      throw new IllegalArgumentException( "A null instance of Map must not be provided" );
    }
    
    if( adjustment.getBody().size() == 0 )
    {
      throw new IllegalArgumentException( "An empty instance of Map must not provided" );
    }
    
    if( numberOfItemsPerPage < 1 )
    {
      throw new IllegalArgumentException( "\"numberOfItemsPerPage\" must be greater than zero" );
    }
    
    setAdjustment( adjustment );
    
    if( numberOfItemsPerPage <  adjustment.getBody().size() )
    {
      setNumberOfItemsPerPage( numberOfItemsPerPage );
    }else
    {
      setNumberOfItemsPerPage( adjustment.getBody().size() );
    }    
    
  }


  /**
   * Returns the number of items per page.
   */
  public int getNumberOfItemsPerPage()
  {
    return _numberOfItemsPerPage;
  }
  
  /**
   * Returns the number of pages.
   */
  public int getNumberOfPages()
  {
    int numberOfBodyItemsInAdjustment = getAdjustment().getBody().size();
    
    if( isShowModifiedAdjustmentsOnly() )
    {
      Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = getAdjustment().getBody().entrySet().iterator();
      
      if(iterator.hasNext())
      {
        numberOfBodyItemsInAdjustment = 0;
      }
      
      while( iterator.hasNext() )
      {
        Map.Entry<String,OrderAdjustmentBodyItem> entry = iterator.next();
        OrderAdjustmentBodyItem bodyItem = entry.getValue();
        if( bodyItem.isCurrentAdjustmentModified() )
        {
          ++numberOfBodyItemsInAdjustment;
        }
      }
      
    }

    int numberOfPages = (int) Math.ceil( (double)numberOfBodyItemsInAdjustment / (double)getNumberOfItemsPerPage() );
    
    return numberOfPages;
  }
  
  /**
   * Sets the current number of page. If the provided page number is greater
   * than this paginator's number of pages, last page will be set as current.
   * Conversely,  the provided page number is lower
   * than this paginator's number of pages, first page will be set as current.
   */
  public void setCurrentPageNumber(int pageNumber)
  {
    
    if ( pageNumber <= getNumberOfPages() )
    {
      if (pageNumber >= 1)
      {
        this._currentPageNumber = pageNumber;
      }else
      {
        this._currentPageNumber = 1;
      }
    }else
    {
      this._currentPageNumber = getNumberOfPages();
    }
  }

  /**
   * Returns the current page number.
   */
  public int getCurrentPageNumber()
  {
    return _currentPageNumber;
  }
  
  
  /**
   * Forwards paginator one page.
   */
  public void forwardPage()
  {
    if( getCurrentPageNumber() < getNumberOfPages() )
    {
      setCurrentPageNumber( getCurrentPageNumber() + 1 );
    }
  }
  
  
  /**
   * Forwards paginator to last page.
   */
  public void forwardToLastPage()
  {
    setCurrentPageNumber( getNumberOfPages() );
  }
  
  
  /**
   * Reverses paginator one page.
   */
  public void reversePage()
  {
    if( getCurrentPageNumber() > 1 )
    {
      setCurrentPageNumber( getCurrentPageNumber() - 1 );
    }
  }
  
  
  /**
   * Reverses paginator to first page.
   */
  public void reverseToFirstPage()
  {
    setCurrentPageNumber( 1 );
  }
  
  
  /**
   * Returns a <code>Map</code> containing items in the current page.
   */
  public Map<String,OrderAdjustmentBodyItem> getItemsInCurrentPage()
  {
    Map<String,OrderAdjustmentBodyItem> itemsInCurrentPage = 
    	new TreeMap<String,OrderAdjustmentBodyItem>( new OrderAdjustmentBodyItemComparator() );
    
    Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = getAdjustment().getBody().entrySet().iterator();
    
    if( isShowModifiedAdjustmentsOnly() )
    {
      iterator = getAdjustment().getModifiedAdjustableBodyItems().entrySet().iterator();
    }
    
    int currentItemIndex = 1;
    
    int firstItemIndex = 1; 
    
    if( getCurrentPageNumber() > 1 )
    {
      firstItemIndex = ( ( getCurrentPageNumber() - 1 ) * getNumberOfItemsPerPage() ) + 1;
    }
    
    int lastItemIndex = ( firstItemIndex + getNumberOfItemsPerPage() ) - 1;
    
    while( iterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentBodyItem> currentEntry = iterator.next();
      
      boolean mustIncludeCurrentBodyItem = currentItemIndex >= firstItemIndex &&
                                           currentItemIndex <= lastItemIndex;
                                           
      if( mustIncludeCurrentBodyItem )
      {
        itemsInCurrentPage.put( currentEntry.getKey(), currentEntry.getValue() );
      }
      
      currentItemIndex++;
    }
    
    return itemsInCurrentPage;
  }
  
  
  /**
   * Specifies that this paginator must show all items.
   */
  public void showAllAdjustments()
  {
    setShowModifiedAdjustmentsOnly( false );
  }


  /**
   * Specifies that this paginator must show only modified items only (adjustments).
   */
  public void showModifiedAdjustmentsOnly()
  {
    setShowModifiedAdjustmentsOnly( true );
  } 
  
  
  ///////////////////////////////////////////////////////////
  //Private methods
  ///////////////////////////////////////////////////////////
  
  private void setAdjustment(OrderAdjustment adjustment)
  {
    this._adjustment = adjustment;
  }

  private OrderAdjustment getAdjustment()
  {
    return _adjustment;
  }

  private void setNumberOfItemsPerPage(int itemsPerPage)
  {
    if( itemsPerPage < 1 )
    {
      throw new IllegalArgumentException("Items per page must be equal or greater than one");
    }
    
    this._numberOfItemsPerPage = itemsPerPage;
  }

  private void setShowModifiedAdjustmentsOnly( boolean showModifiedAdjustmentsOnly )
  {
    this._showModifiedAdjustmentsOnly = showModifiedAdjustmentsOnly;
  }

  private boolean isShowModifiedAdjustmentsOnly()
  {
    return _showModifiedAdjustmentsOnly;
  }
}
