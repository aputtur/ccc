package com.copyright.ccc.web.forms.adjustment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.copyright.ccc.business.services.adjustment.OrderAdjustment;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentBodyItem;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentBodyItemComparator;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentDescriptor;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentPaginator;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentValidator;

/**
 * Main <code>ActionForm</code> use by the <b>Adjustments Application</b>
 */
public class AdjustmentActionForm extends ActionForm
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final String ADJUSTMENT_UI_MODE = "ADJUSTMENT_MODE";
  private static final String INDIVIDUAL_ADJUSTMENT_EDIT_UI_MODE = "INDIVIDUAL_ADJUSTMENT_EDIT";
  private static final String CONFIRMATION_UI_MODE = "CONFIRMATION_MODE";
  
  private String _UIMode = ADJUSTMENT_UI_MODE;
  
  private OrderAdjustment _adjustment;
  
  private OrderAdjustmentPaginator _paginator;
    
  private long _reason;
  private String _fieldToAdjust;
  private double _currentValue;
  private double _newValue;
  
  private long _adjustmentIDToModify;
  private String _adjustmentIDToDelete;
  private long _adjustmentIDToRecalculate;
  
  private Map<String,OrderAdjustmentDescriptor> _adjustmentsDescriptors;
  private String _selectedCartDescriptorID;
  
  private int _adjustmentModeLastPageNumber = 1;


  /**
   * Overrides <code>ActionForm.reset(ActionMapping, HttpServletRequest)</code>
   */
  @Override
  public void reset( ActionMapping ActionMapping, HttpServletRequest HttpServletRequest )
  {
    super.reset(ActionMapping, HttpServletRequest);
    
    setReason( -1 );
    setFieldToAdjust( null );
    setCurrentValue( 0 );
    setNewValue( 0 );
    
  }

  /**
   * Resets several member values.
   */
  public void resetForm()
  {
    setAdjustment( null );
    setPaginator( null );
    setReason( -1 );
    setFieldToAdjust( null );
    setCurrentValue( 0 );
    setNewValue( 0 );
    setAdjustmentDescriptors( null );
    setAdjustmentIDToDelete( null );
    setAdjustmentIDToModify( 0 );
    setAdjustmentIDToRecalculate( 0 );
    setSelectedCartDescriptorID( null );
    setUIModeAdjustment();
  }

  /**
   * Sets form in adjustment mode.
   */
  public void setUIModeAdjustment()
  {
    setUIMode( ADJUSTMENT_UI_MODE );

    boolean paginatorPresent = getPaginator() != null;
    
    if ( paginatorPresent )
    {
      getPaginator().showAllAdjustments();
    }
    
  }
  
  /**
   * Sets form in confirmation mode.
   */
  public void setUIModeConfirmation()
  {
    setUIMode( CONFIRMATION_UI_MODE );

    boolean paginatorPresent = getPaginator() != null;
    
    if ( paginatorPresent )
    {
      getPaginator().showModifiedAdjustmentsOnly();
    }
  }
  
  /**
   * Sets form in individual adjustment edit mode.
   */
  public void setUIModeIndividualAdjustmentEdit()
  {
    setUIMode( INDIVIDUAL_ADJUSTMENT_EDIT_UI_MODE );
  }
 
  /**
   * Determines if form is in adjustment mode.
   */ 
  public boolean isUIModeAdjustment()
  {
    return getUIMode().equals( ADJUSTMENT_UI_MODE );
  }
  
  /**
   * Determines if form is in confirmation mode.
   */ 
  public boolean isUIModeConfirmation()
  {
    return getUIMode().equals( CONFIRMATION_UI_MODE );
  }
  
  /**
   * Determines if form is in individual adjustment edit mode.
   */ 
  public boolean isUIModeIndividualAdjustmentEdit()
  {
    return getUIMode().equals( INDIVIDUAL_ADJUSTMENT_EDIT_UI_MODE );
  }
  
  /**
   * Determines if form is in either adjustment or individual adjustment edit mode.
   */ 
  public boolean isUIEditCurrentAdjustmentMode()
  {
    return isUIModeAdjustment() || isUIModeIndividualAdjustmentEdit();
  }


  /**
   * Sets the current <code>OrderAdjustment</code> to be handled by the application.
   */
  public void setAdjustment(OrderAdjustment _adjustment)
  {
    this._adjustment = _adjustment;
    
   if ( _adjustment != null )
   {
      setPaginator( new OrderAdjustmentPaginator( _adjustment, getNumberOfItemsPerPageForPaginator() ) );
   }
  }


  /**
   * Returns the current <code>OrderAdjustment</code> being handled by the application.
   */
  public OrderAdjustment getAdjustment()
  {
    return _adjustment;
  }

  /**
   * Explicitly sets UI mode.
   */
  public void setUIMode(String uIMode)
  {
    boolean validUIMode = StringUtils.isNotEmpty( uIMode ) &&
                          ( uIMode.equals( ADJUSTMENT_UI_MODE ) ||
                            uIMode.equals( CONFIRMATION_UI_MODE ) ||
                            uIMode.equals( INDIVIDUAL_ADJUSTMENT_EDIT_UI_MODE ) 
                          );
                          
                          
    if ( validUIMode )
    {
      this._UIMode = uIMode;
    }else
    {
      throw new IllegalArgumentException("Invalid UI mode provided");
    }
    
  }

  /**
   * Returns UI mode.
   */
  public String getUIMode()
  {
    return _UIMode;
  }


  /**
   * Sets reason
   */
  public void setReason(long reason)
  {
    this._reason = reason;
  }

  /**
   * Returns reason
   */
  public long getReason()
  {
    return _reason;
  }

  /**
   * Sets field to adjust
   */
  public void setFieldToAdjust(String fieldToAdjust)
  {
    this._fieldToAdjust = fieldToAdjust;
  }

  /**
   * Returns field to adjust
   */
  public String getFieldToAdjust()
  {
    return _fieldToAdjust;
  }

  /**
   * Sets current value
   */
  public void setCurrentValue(double currentValue)
  {
    this._currentValue = currentValue;
  }

  /**
   * Returns current value
   */
  public double getCurrentValue()
  {
    return _currentValue;
  }

  /**
   * Sets new value
   */
  public void setNewValue(double newValue)
  {
    this._newValue = newValue;
  }

  /**
   * Returns new value
   */
  public double getNewValue()
  {
    return _newValue;
  }

  /**
   * Sets adjustment Id to modify
   */
  public void setAdjustmentIDToModify(long adjustmentIDToModify)
  {
    this._adjustmentIDToModify = adjustmentIDToModify;
  }

  /**
   * Returns adjustment Id to modify
   */
  public long getAdjustmentIDToModify()
  {
    return _adjustmentIDToModify;
  }
  
  /**
   * Returns a <code>Map</code> the <code>OrderAdjustmentBOdyItems</code>s
   * to be displayed depending on UI mode.
   */
  public Map<String,OrderAdjustmentBodyItem> getUIModeAwareBody()
  {
    Map<String,OrderAdjustmentBodyItem> body = getPaginator().getItemsInCurrentPage();
    
    if( isUIModeIndividualAdjustmentEdit() )
    {
      
      String bodyItemIDToModify = String.valueOf( getAdjustmentIDToModify() );
      
      OrderAdjustmentBodyItem bodyItemToModify = getAdjustment().getBody().get( bodyItemIDToModify );
      
      if ( bodyItemToModify != null )
      {
        body = new HashMap<String,OrderAdjustmentBodyItem>();
        body.put( bodyItemIDToModify , bodyItemToModify );
      }
      
    }
    
    if( isUIModeConfirmation() )
    {
      Map<String,OrderAdjustmentBodyItem> bodyForConfirmationPage = 
    	  new TreeMap<String,OrderAdjustmentBodyItem>( new OrderAdjustmentBodyItemComparator() );
      
      Iterator<Map.Entry<String,OrderAdjustmentBodyItem>> iterator = body.entrySet().iterator();
      
      while( iterator.hasNext() )
      {
        Map.Entry<String,OrderAdjustmentBodyItem> entry = iterator.next();
        
        String bodyItemID = entry.getKey();
        
        OrderAdjustmentBodyItem bodyItem = entry.getValue();
        
        if( bodyItem.isAdjustable() && bodyItem.isCurrentAdjustmentModified() )
        {
          bodyForConfirmationPage.put( bodyItemID, bodyItem );
        }
        
      }
      
      body = bodyForConfirmationPage;
      
    }
   
    return body;
  }

  /**
   * Set adjustment ID to be deleted
   */
  public void setAdjustmentIDToDelete(String adjustmentIDToDelete)
  {
    this._adjustmentIDToDelete = adjustmentIDToDelete;
  }

  /**
   * Returns adjustment ID to be deleted
   */
  public String getAdjustmentIDToDelete()
  {
    return _adjustmentIDToDelete;
  }

  /**
   * Sets adjustment ID to be recalculated
   */
  public void setAdjustmentIDToRecalculate(long adjustmentIDToRecalculate)
  {
    this._adjustmentIDToRecalculate = adjustmentIDToRecalculate;
  }

  /**
   * Returns adjustment ID to be deleted
   */
  public long getAdjustmentIDToRecalculate()
  {
    return _adjustmentIDToRecalculate;
  }


  /**
   * Overrides <code>ActionForm.validate(ActionMapping, HttpServletRequest)</code>
   */
  @Override
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {

    boolean validate = true;

    String actionOperation = request.getParameter( mapping.getParameter() );

    boolean actionOperationPresent = StringUtils.isNotEmpty(actionOperation);
    
    if ( actionOperationPresent )
    {
      validate = actionOperation.equalsIgnoreCase("completeAdjustment") ||
                 actionOperation.equalsIgnoreCase("makeAdjustment") ||
                 actionOperation.equalsIgnoreCase("saveAdjustment") ||
                 actionOperation.equalsIgnoreCase("performFullCredit") ||
                 actionOperation.equalsIgnoreCase("performGlobalAdjustment") ||
                 actionOperation.equalsIgnoreCase("recalculateCurrentAdjustment");
    }
    
    if( !validate )
    {
      return new ActionErrors();
    }
    
    OrderAdjustment currentAdjustment = getAdjustment();
    
    ActionErrors errors = super.validate(mapping, request);
    
    if ( currentAdjustment != null )
    {
      
      OrderAdjustmentValidator validator = new OrderAdjustmentValidator( currentAdjustment );

      ActionErrors adjustmentValidationErrors = validator.validate();
      
      if(errors == null)
      {
        errors = new ActionErrors();
      }
      
      errors.add( adjustmentValidationErrors );
    }
  
    return errors; 
    
  }

  private void setPaginator(OrderAdjustmentPaginator paginator)
  {
    this._paginator = paginator;
  }

  /**
   * Returns current <code>OrderAdjustmentPaginator</code>
   */
  public OrderAdjustmentPaginator getPaginator()
  {
    return _paginator;
  }


  /**
   * Sets adjustment descriptors to be displayed by "Saved Adjustments" UI.
   */
  public void setAdjustmentDescriptors(Map<String,OrderAdjustmentDescriptor> adjustmentsDescriptors)
  {
    this._adjustmentsDescriptors = adjustmentsDescriptors;
  }

  /**
   * Returns adjustment descriptors to be displayed by "Saved Adjustments" UI.
   */
  public Map<String,OrderAdjustmentDescriptor> getAdjustmentDescriptors()
  {
    return _adjustmentsDescriptors;
  }

  /**
   * Sets the selected cart descriptor in "Saved Adjustments" UI.
   */
  public void setSelectedCartDescriptorID(String selectedCartDescriptorID)
  {
    this._selectedCartDescriptorID = selectedCartDescriptorID;
  }

  /**
   * Returns the selected cart descriptor in "Saved Adjustments" UI.
   */
  public String getSelectedCartDescriptorID()
  {
    return _selectedCartDescriptorID;
  }
  
  
  ///////////////////////////////////////////////////
  //Private Methods
  ///////////////////////////////////////////////////
  
    private int getNumberOfItemsPerPageForPaginator()
    {
      int itemsPerPage = OrderAdjustmentPaginator.DEFAULT_ITEMS_PER_PAGE;
      
      ResourceBundle adjustmentsProperties = null;
      
      try
      {
        adjustmentsProperties =  ResourceBundle.getBundle("com.copyright.ccc.business.services.adjustment.AdjustmentResources");
      }
      catch ( MissingResourceException  mre )
      {
        //We ignore this proble by using OrderAdjustmentPaginator.DEFAULT_ITEMS_PER_PAGE.
      }
      
      if( adjustmentsProperties != null )
      {
        String itemsPerPageString = adjustmentsProperties.getString("adj.paginator.items.per.page");
        
        if( StringUtils.isNotEmpty( itemsPerPageString ) )
        {
          try
          {
            itemsPerPage = Integer.parseInt( itemsPerPageString );
          }
          catch ( NumberFormatException nfe )
          {
            //We ignore this proble by using OrderAdjustmentPaginator.DEFAULT_ITEMS_PER_PAGE.
          }
        }
      }
      return itemsPerPage;
  }

  public void setAdjustmentModeLastPageNumber(int adjustmentModeLastPageNumber)
  {
    this._adjustmentModeLastPageNumber = adjustmentModeLastPageNumber;
  }

  public int getAdjustmentModeLastPageNumber()
  {
    return _adjustmentModeLastPageNumber;
  }
}
