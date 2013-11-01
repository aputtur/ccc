package com.copyright.ccc.web.actions.adjustment;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.RedirectingActionForward;

import com.copyright.ccc.CCRuntimeException;
import com.copyright.ccc.business.security.CCPrivilegeCode;
import com.copyright.ccc.business.security.UserContextService;
import com.copyright.ccc.business.services.adjustment.AdjustmentServices;
import com.copyright.ccc.business.services.adjustment.OrderAdjustment;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentDescriptor;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentException;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentMultiplePriceCalculationException;
import com.copyright.ccc.business.services.adjustment.OrderAdjustmentPriceCalculationException;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.actions.admin.AdminAction;
import com.copyright.ccc.web.forms.adjustment.AdjustmentActionForm;
import com.copyright.data.ValidationException;
import com.copyright.data.order.CartStatusException;
import com.copyright.data.order.cashier.CashierCheckoutException;
import com.copyright.data.payment.AuthorizationException;

/**
 * Main action class used by <b>Adjustments Application</b>
 */
public class AdjustmentAction extends AdminAction
{
  
  private static final String ID_REQUEST_PARAM = "id";
  
  private static final String SHOW_MAIN = "showMain";
  private static final String SHOW_THANK_YOU = "showThankYou";
  private static final String SHOW_ERROR = "showError";
  private static final String SHOW_LIST = "showList";
  private static final String SHOW_ORDER_HISTORY = "showOrderHistory";
  
  private static final String UI_PAGE_NUMBER_URL_PARAM = "page";
  private static final boolean DO_NOT_RECALCULATE_ON_FEE_ONLY_MODIFIED_ITEMS = false;


  /**
   * Operation used to refresh adjustment's values.
   */
  public ActionForward refreshAdjustment( ActionMapping mapping, 
                                          ActionForm form, 
                                          HttpServletRequest request, 
                                          HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, mapping );
    }   
    
    return mapping.findForward( SHOW_MAIN );
  }
  
 
  
  /**
   * Operation used to edit an adjustment.
   */
  public ActionForward editAdjustment( ActionMapping mapping, 
                                          ActionForm form, 
                                          HttpServletRequest request, 
                                          HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("adj.error.freeform", "Missing Order Adjustment" ) );
      saveErrors(request, errors);      
      return mapping.findForward( SHOW_ERROR );
    }   
    
    adjustmentForm.setUIModeAdjustment();
    
    adjustmentForm.getPaginator().setCurrentPageNumber( adjustmentForm.getAdjustmentModeLastPageNumber() );
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to perform an invoice adjustment.
   */
  public ActionForward performInvoiceAdjustment( ActionMapping mapping, 
                                                 ActionForm form, 
                                                 HttpServletRequest request, 
                                                 HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    adjustmentForm.resetForm();
    
    String invoiceID = request.getParameter( ID_REQUEST_PARAM );
    
    boolean invoiceIDPresent = StringUtils.isNotEmpty( invoiceID );
    
    if( invoiceIDPresent )
    {
      OrderAdjustment adjustment;

      try
      {
        adjustment = AdjustmentServices.createNewInvoiceAdjustment( invoiceID );
      } catch (OrderLicensesException ole)
      {
         throw new CCRuntimeException( ole );
      }
      adjustmentForm.setAdjustment( adjustment );
      adjustmentForm.setUIModeAdjustment();
      
    }else
    {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("adj.error.freeform", "Missing Invoice ID" ) );
      saveErrors(request, errors);      
      
      return mapping.findForward( SHOW_ERROR );
    }
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to perform a purchase adjustment.
   */
  public ActionForward performPurchaseAdjustment( ActionMapping mapping, 
                                                  ActionForm form, 
                                                  HttpServletRequest request, 
                                                  HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    adjustmentForm.resetForm();
    
    String purchaseID = request.getParameter( ID_REQUEST_PARAM );
    
    boolean purchaseIDPresent = StringUtils.isNotEmpty( purchaseID );
    
    if( purchaseIDPresent )
    {
      OrderAdjustment adjustment;

      try
      {
        adjustment = AdjustmentServices.createNewPurchaseAdjustment( purchaseID );
      } catch ( OrderLicensesException ole )
      {
         throw new CCRuntimeException( ole );
      }
      adjustmentForm.setAdjustment( adjustment );
      adjustmentForm.setUIModeAdjustment();
    
    }else
    {
       ActionMessages errors = new ActionMessages();
       errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("adj.error.freeform", "Missing Purchase ID" ) );
       saveErrors(request, errors);      
       
       return mapping.findForward( SHOW_ERROR );
    }
    
    return mapping.findForward( SHOW_MAIN );
    
  }
  
  /**
   * Operation used to perform a detail adjustment.
   */
  public ActionForward performDetailAdjustment( ActionMapping mapping, 
                                                ActionForm form, 
                                                HttpServletRequest request, 
                                                HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    adjustmentForm.resetForm();
    
    String detailID = request.getParameter( ID_REQUEST_PARAM );
    
    boolean detailIDPresent = StringUtils.isNotEmpty( detailID );
    
    if( detailIDPresent )
    {
      OrderAdjustment adjustment;

      try
      {
        adjustment = AdjustmentServices.createNewDetailAdjustment( detailID );
      } catch (OrderLicensesException ole)
      {
         throw new CCRuntimeException( ole );
      }
      adjustmentForm.setAdjustment( adjustment );
      adjustmentForm.setUIModeAdjustment();
      
    }else
    {
       ActionMessages errors = new ActionMessages();
       errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("adj.error.freeform", "Missing Detail ID" ) );
       saveErrors(request, errors);      
       
       return mapping.findForward( SHOW_ERROR );
    }
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to complete an adjustment.
   */
  public ActionForward completeAdjustment( ActionMapping mapping, 
                                           ActionForm form, 
                                           HttpServletRequest request, 
                                           HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    adjustmentForm.setAdjustmentModeLastPageNumber( adjustmentForm.getPaginator().getCurrentPageNumber() );
    adjustmentForm.getPaginator().reverseToFirstPage();

    ActionMessages errors = new ActionMessages();
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.error.freeform", "Missing Order Adjustment" ) );
      saveErrors(request, errors);      
      
      return mapping.findForward( SHOW_ERROR );
    }
    
    if( adjustmentForm.getAdjustment().areThereModifiedAdjustableBodyItems() )
    {
      
      try
      {
        
        //Recalculte prices for adjustments whose quantites have been modified only
        adjustmentForm.getAdjustment().recalculateCurrentAdjustmentsPrices( DO_NOT_RECALCULATE_ON_FEE_ONLY_MODIFIED_ITEMS );
      
      } catch ( OrderAdjustmentMultiplePriceCalculationException multiplePriceCalculationException )
      {
    	  _logger.error(LogUtil.getStack(multiplePriceCalculationException));
         errors.add( createActionErrors( multiplePriceCalculationException ) );
         saveErrors(request, errors);      
         
         return mapping.findForward( SHOW_ERROR );
      }
      
      adjustmentForm.setUIModeConfirmation();
    }else
    {
      ActionMessages messages = new ActionMessages();
      messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.message.no.modified.adjustments", "completed" ) );
      saveMessages( request, messages );
    }
       
    return mapping.findForward( SHOW_MAIN );
  }
  
  

    
  /**
   * Operation used to save an adjustment.
   */
  public ActionForward saveAdjustment( ActionMapping mapping, 
                                       ActionForm form, 
                                       HttpServletRequest request, 
                                       HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("adj.error.freeform", "Missing Order Adjustment" ) );
      saveErrors(request, errors);      
      
      return mapping.findForward( SHOW_ERROR );
    }
    
    ActionMessages errors = new ActionMessages();
    
    //Operation only allowed in adjustment UI mode
    if( !adjustmentForm.isUIModeAdjustment() )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.action.invalid.invocation" ) );
      saveErrors( request, errors );
      return mapping.findForward( SHOW_ERROR );
    }
    
    OrderAdjustment adjustment = adjustmentForm.getAdjustment();
    
    if ( adjustment.areThereModifiedAdjustableBodyItems() )
    {
      try
      {
        AdjustmentServices.saveAdjustment( adjustment );
        
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.message.adjustment.saved.successfully" ) );
        saveMessages( request, messages );
      }
      catch ( ValidationException ve )
      {
    	_logger.error(LogUtil.getStack(ve));
        errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.cart.save.validation" ) );
      }
      catch ( AuthorizationException ae )
      {
      	_logger.error(LogUtil.getStack(ae));
        errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.cart.save.authorization" ) );
      }
      catch( CartStatusException cse )
      {
      	_logger.error(LogUtil.getStack(cse));
        errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.cart.status" ) );
      }
      catch( com.copyright.data.pricing.LimitsExceededException lee)
      {
      	_logger.warn(LogUtil.getStack(lee));
        errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.cart.limits" ) );
      }
      
      saveErrors( request, errors );
    }
    else
    {
      ActionMessages messages = new ActionMessages();
      messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.message.no.modified.adjustments", "saved" ) );
      saveMessages( request, messages );
    }
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to cancel an adjustment.
   */
  public ActionForward cancelAdjustment( ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    adjustmentForm.resetForm();
    
    return new RedirectingActionForward( mapping.findForward( SHOW_ORDER_HISTORY ).getPath() );
  }
  
  
  /**
   * Operation used to delete an adjustment.
   */
  public ActionForward deleteAdjustment( ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    ActionMessages errors = new ActionMessages();
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, errors, mapping );
    }
    
    //Operation only allowed in confirmation UI mode
    if( !adjustmentForm.isUIModeConfirmation() )
    {
       errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.action.invalid.invocation" ) );
       saveErrors( request, errors );
       return mapping.findForward( SHOW_ERROR );
    }    
    
    String bodyItemIDToDelete = adjustmentForm.getAdjustmentIDToDelete();
    
    adjustmentForm.getAdjustment().deleteCurrentAdjustment( bodyItemIDToDelete );
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to modify an adjustment.
   */
  public ActionForward modifyAdjustment( ActionMapping mapping, 
                                         ActionForm form, 
                                         HttpServletRequest request, 
                                         HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    ActionMessages errors = new ActionMessages();
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, errors, mapping );
    }    
    
    //Operation only allowed in confirmation UI mode
    if( !adjustmentForm.isUIModeConfirmation() )
    {
       errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.action.invalid.invocation" ) );
       saveErrors( request, errors );
       return mapping.findForward( SHOW_ERROR );
    }
    
    adjustmentForm.setUIModeIndividualAdjustmentEdit();
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to full credit an adjustment.
   */
  public ActionForward performFullCredit( ActionMapping mapping, 
                                          ActionForm form, 
                                          HttpServletRequest request, 
                                          HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    ActionMessages errors = new ActionMessages();
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, errors, mapping );
    }   
        
    //Operation only allowed in adjustment UI mode
    if( !adjustmentForm.isUIModeAdjustment() )
    {
       errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.action.invalid.invocation" ) );
       saveErrors( request, errors );
       return mapping.findForward( SHOW_ERROR );
    }
    
    long reasonCode = adjustmentForm.getReason();


    try
    {
      
      adjustmentForm.getAdjustment().performFullCredit( reasonCode );
    
    } catch ( OrderAdjustmentMultiplePriceCalculationException priceCalculationErrors )
    {
    	_logger.error(LogUtil.getStack(priceCalculationErrors));
       errors.add( createActionErrors( priceCalculationErrors ) );
       saveErrors( request, errors );
    }
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to perfrom a global adjustment.
   */
  public ActionForward performGlobalAdjustment( ActionMapping mapping, 
                                                ActionForm form, 
                                                HttpServletRequest request, 
                                                HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    ActionMessages errors = new ActionMessages();
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, errors, mapping );
    }   
    
    //Operation only allowed in adjustment UI mode
    if( !adjustmentForm.isUIModeAdjustment() )
    {
       errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.action.invalid.invocation" ) );
       saveErrors( request, errors );
       return mapping.findForward( SHOW_ERROR );
    }
    
    String fieldToAdjust = adjustmentForm.getFieldToAdjust();
    double currentValue = adjustmentForm.getCurrentValue();
    double newValue = adjustmentForm.getNewValue();
    long reasonCode = adjustmentForm.getReason();


    if( newValue > currentValue )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.freeform", "New Value cannot be greater than Current Value" ) );
      saveErrors( request, errors );
      return mapping.findForward( SHOW_MAIN );
    }
  

    try
    {
      int numberOfAdjustedOrderDetails = adjustmentForm.getAdjustment().performGlobalAdjustment( fieldToAdjust,
                                                                                                 currentValue,
                                                                                                 newValue,
                                                                                                 reasonCode );
                                                                                                 
      ActionMessages messages = new ActionMessages();
      messages.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.message.adjustment.globally.adjusted.successfully", String.valueOf( numberOfAdjustedOrderDetails ) ) );
      saveMessages( request, messages );
      
    } catch ( OrderAdjustmentMultiplePriceCalculationException priceCalculationErrors )
    {
    	_logger.error(LogUtil.getStack(priceCalculationErrors));
      errors.add( createActionErrors( priceCalculationErrors ) );
      saveErrors( request, errors );
    }
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to commit (checkout) an adjustment.
   */
  public ActionForward makeAdjustment( ActionMapping mapping, 
                                       ActionForm form, 
                                       HttpServletRequest request, 
                                       HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    ActionMessages errors = new ActionMessages();
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, errors, mapping );
    }   
    
    //Operation only allowed in confirmation UI mode
    if( !adjustmentForm.isUIModeConfirmation() )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.action.invalid.invocation" ) );
      saveErrors( request, errors );
      return mapping.findForward( SHOW_ERROR );
    }
    
    OrderAdjustment adjustment = adjustmentForm.getAdjustment();
    
    try
    {
      //long confirmationNumber = 
    	  AdjustmentServices.commitAdjustment( adjustment );
    }
    catch ( ValidationException e )
    {
    	_logger.warn(LogUtil.getStack(e));
    	errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.cart.checkout.validation" ) );
    	saveErrors( request, errors );
    	return mapping.findForward( SHOW_MAIN );
    }
    catch ( AuthorizationException e )
    {
    	_logger.warn(LogUtil.getStack(e));
      errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.cart.checkout.authorization" ) );
      saveErrors( request, errors );
      return mapping.findForward( SHOW_MAIN );
    }
    catch( CartStatusException e )
    {
    	_logger.warn(LogUtil.getStack(e));
      errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.cart.status" ) );
      saveErrors( request, errors );
      return mapping.findForward( SHOW_MAIN );
    }
    catch( CashierCheckoutException e )
    {
    	_logger.error(LogUtil.getStack(e));
      errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.freeform", e.getClass().getName() + " thrown by Cashier Shared Service: " + e.getMessage() ) );
      saveErrors( request, errors );
      return mapping.findForward( SHOW_ERROR );
    }
    catch( com.copyright.data.pricing.LimitsExceededException e)
    {
    	_logger.info(LogUtil.getStack(e));
      errors.add( ActionMessages.GLOBAL_MESSAGE , new ActionMessage( "adj.error.cart.limits" ) );
      saveErrors( request, errors );
      return mapping.findForward( SHOW_MAIN );
    }
    
    //After successful checkout, fully reset action form.
    adjustmentForm.resetForm();
    
    return mapping.findForward( SHOW_THANK_YOU );
  }
  
  
  /**
   * Operation used to recalculate the prices of an adjustment.
   */
  public ActionForward recalculateCurrentAdjustment( ActionMapping mapping, 
                                                     ActionForm form, 
                                                     HttpServletRequest request, 
                                                     HttpServletResponse response)
  {      
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
        
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, mapping );
    }   
    
    long adjustmentIDToRecalculate = adjustmentForm.getAdjustmentIDToRecalculate();

    try
    {
      adjustmentForm.getAdjustment().recalculateCurrentAdjustmentPrice( adjustmentIDToRecalculate );
    } catch ( OrderAdjustmentPriceCalculationException e )
    {
    	_logger.warn(LogUtil.getStack(e));
    	ActionMessages errors = new ActionMessages();
    	errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.error.freeform", "Adjustment with ID " + adjustmentIDToRecalculate + " could not be recalculated: " + e.getMessage() ) );
    	saveErrors( request, errors );
    }
    
    return mapping.findForward( SHOW_MAIN );
  }
  
  
  /**
   * Operation used to tell the current paginator to go to a specific page.
   */
  public ActionForward goToUIPage( ActionMapping mapping, 
                                   ActionForm form, 
                                   HttpServletRequest request, 
                                   HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, mapping );
    }   
    
    String pageParameter = request.getParameter( UI_PAGE_NUMBER_URL_PARAM );
    
    boolean pageParameterPresent = StringUtils.isNotEmpty( pageParameter );
    
    if( pageParameterPresent )
    {
      int uiPageNumber = Integer.parseInt( pageParameter );
      adjustmentForm.getPaginator().setCurrentPageNumber( uiPageNumber );
    }
    
    return refreshAdjustment( mapping, form, request, response);
  }
  
  /**
   * Operation used to tell the current paginator to reverse page.
   */
  public ActionForward reverseUIPage( ActionMapping mapping, 
                                   ActionForm form, 
                                   HttpServletRequest request, 
                                   HttpServletResponse response)
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
          
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, mapping );
    }   
      
    adjustmentForm.getPaginator().reversePage();
    
    return refreshAdjustment( mapping, form, request, response);
  }
  
  
  /**
   * Operation used to tell the current paginator to forward page.
   */
  public ActionForward forwardUIPage( ActionMapping mapping, 
                                      ActionForm form, 
                                      HttpServletRequest request, 
                                      HttpServletResponse response )
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    if ( isOrderAdjustmentMissing( adjustmentForm ) )
    {
      return handleMissingAdjustment( request, mapping );
    }   
      
    adjustmentForm.getPaginator().forwardPage();
    
    return refreshAdjustment( mapping, form, request, response );
  }
  
  
  /**
   * Operation used to display a saved adjustment for the current app's user.
   */  
  public ActionForward displayAdjustmentsForUser( ActionMapping mapping, 
                                                  ActionForm form, 
                                                  HttpServletRequest request, 
                                                  HttpServletResponse response )
  {
    
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    long partyID = UserContextService.getSharedUser().getPartyId().longValue();
    
    Map<String,OrderAdjustmentDescriptor> adjustmentDescriptors = AdjustmentServices.getAdjustmentDescriptorsForUser( partyID );
  
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    adjustmentForm.resetForm();
 
    adjustmentForm.setAdjustmentDescriptors( adjustmentDescriptors );
    
    return mapping.findForward( SHOW_LIST );
  }


  /**
   * Operation used to modify a previously saved adjustment.
   */
  public ActionForward modifySavedAdjustment( ActionMapping mapping, 
                                              ActionForm form, 
                                              HttpServletRequest request, 
                                              HttpServletResponse response )
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);

    String cartDescriptorID = adjustmentForm.getSelectedCartDescriptorID();
    OrderAdjustmentDescriptor descriptor = adjustmentForm.getAdjustmentDescriptors().get( cartDescriptorID );
    
    OrderAdjustment orderAdjustment = null;
    
    boolean descriptorFound = descriptor != null;
    
    if( descriptorFound )
    {
      try
      {
        orderAdjustment = AdjustmentServices.retrieveAdjustment( descriptor );
      } catch ( OrderLicensesException ole )
      {
        throw new OrderAdjustmentException( ole );
      }
      
    }else
    {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.error.freeform", "Missing Order Adjustment Descriptor" ) );
      saveErrors( request, errors ); 
      
      return mapping.findForward( SHOW_ERROR );
    }

    boolean orderAdjustmentPresent = orderAdjustment != null;
    
    if ( orderAdjustmentPresent )
    {
      adjustmentForm.setAdjustment( orderAdjustment );
      adjustmentForm.setUIModeAdjustment();
      
      return  mapping.findForward( SHOW_MAIN );
    }else
    {
      return handleMissingAdjustment( request, mapping );
    }
    

  }

  /**
   * This is the modified 'modifySavedAdjustment' action to work with OMS COI
   * OMS Adjustment app  needs to have a link to the TF Adjustment app.
   * The cart id is passes as a parameter. Fetch all  Cart Descriptors and
   * select the one passed in by OMS. 
   */
  public ActionForward modifySavedAdjustmentforOMS( ActionMapping mapping, 
                                              ActionForm form, 
                                              HttpServletRequest request, 
                                              HttpServletResponse response )
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    String tfAdjustmentCartId = request.getParameter("tfAdjustmentCartId");
    OrderAdjustmentDescriptor descriptor = null;

    long partyID = UserContextService.getSharedUser().getPartyId().longValue();
  	Map<String,OrderAdjustmentDescriptor> adjustmentDescriptors = AdjustmentServices
  			.getAdjustmentDescriptorsForUser(partyID);
  	adjustmentForm.setAdjustmentDescriptors( adjustmentDescriptors );
  	
  	Iterator<Entry<String,OrderAdjustmentDescriptor>> it = adjustmentDescriptors.entrySet().iterator();
  	while (it.hasNext()) {
  		Map.Entry<String,OrderAdjustmentDescriptor> pairs = it.next();
  		OrderAdjustmentDescriptor desc = pairs.getValue();
  		if (String.valueOf(desc.getCartDescriptor().getID()).equals(tfAdjustmentCartId)){
  			descriptor = desc; 
  			break;
  		}
  	}

    OrderAdjustment orderAdjustment = null;
    
    boolean descriptorFound = descriptor != null;
    
    if( descriptorFound )
    {
      try
      {
        orderAdjustment = AdjustmentServices.retrieveAdjustment( descriptor );
      } catch ( OrderLicensesException ole )
      {
        throw new OrderAdjustmentException( ole );
      }
      
    }else
    {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.error.freeform", "Missing Order Adjustment Descriptor" ) );
      saveErrors( request, errors ); 
      
      return mapping.findForward( SHOW_ERROR );
    }

    boolean orderAdjustmentPresent = orderAdjustment != null;
    
    if ( orderAdjustmentPresent )
    {
      adjustmentForm.setAdjustment( orderAdjustment );
      adjustmentForm.setUIModeAdjustment();
      
      return  mapping.findForward( SHOW_MAIN );
    }else
    {
      return handleMissingAdjustment( request, mapping );
    }
    

  }
  
  /**
   * Operation used to delete a previously saved adjustment.
   */
  public ActionForward deleteSavedAdjustment( ActionMapping mapping, 
                                              ActionForm form, 
                                              HttpServletRequest request, 
                                              HttpServletResponse response )
  {
    UserContextService.checkPrivilege( CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT );

    ActionMessages errors = new ActionMessages();
    
    AdjustmentActionForm adjustmentForm = castForm( AdjustmentActionForm.class, form);
    
    String cartDescriptorID = adjustmentForm.getSelectedCartDescriptorID();
    
    OrderAdjustmentDescriptor adjustmentDescriptor = adjustmentForm.getAdjustmentDescriptors().get( cartDescriptorID  );
    
    boolean adjustmentDescriptorFound = adjustmentDescriptor != null;
    
    if( adjustmentDescriptorFound )
    {
      try
      {
        AdjustmentServices.deleteSavedAdjustment( adjustmentDescriptor );
        ActionMessages messages = new ActionMessages();
        messages.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.message.adjustment.deleted.successfully" ) );
        saveMessages( request, messages );
      }
      catch ( OrderAdjustmentException e )
      {
      	_logger.warn(LogUtil.getStack(e));    	  
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.error.freeform", "There were problems deleting adjustment with ID " + cartDescriptorID + ": " + e.getCause().getMessage() + "." ) );
        saveErrors( request, errors );
      }
      
    }else
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.error.freeform", "There were problems deleting adjustment: adjustment with ID " + cartDescriptorID + " not found." ) );
      saveErrors( request, errors );
    }
    
    return displayAdjustmentsForUser( mapping, form, request, response );
    
  }


////////////////////////////
// Private Methods
////////////////////////////

  private boolean isOrderAdjustmentMissing( AdjustmentActionForm adjustmentForm )
  {
    return adjustmentForm.getAdjustment() == null;
  }
 
 
  private ActionMessages createActionErrors( OrderAdjustmentMultiplePriceCalculationException multiplePriceCalculationException )
  {
    ActionMessages errors = new ActionMessages();
    
    Iterator<Map.Entry<String,OrderAdjustmentPriceCalculationException>> iterator = 
    	multiplePriceCalculationException.getIndividualOrderAdjustmentPriceCalculationExceptions().entrySet().iterator();
    
    while( iterator.hasNext() )
    {
      Map.Entry<String,OrderAdjustmentPriceCalculationException> entry = iterator.next();
    
      String adjustmentID = entry.getKey();
      OrderAdjustmentPriceCalculationException priceCalculationException = entry.getValue();
      
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "adj.error.freeform", "Adjustment with ID " + adjustmentID + " could not be recalculated: " + priceCalculationException.getMessage() ) );
    }
    
    return errors;
  }
  
  
  private ActionForward handleMissingAdjustment( HttpServletRequest request, ActionMapping mapping )
  {
    return handleMissingAdjustment(request, null, mapping);
  }
  
  private ActionForward handleMissingAdjustment( HttpServletRequest request, ActionMessages errors, ActionMapping mapping )
  {
    if( errors == null )
    {
      errors = new ActionMessages();
    }
    
    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("adj.error.freeform", "Missing Order Adjustment" ) );
    saveErrors(request, errors);      
    return mapping.findForward( SHOW_ERROR );
  }
  
}
