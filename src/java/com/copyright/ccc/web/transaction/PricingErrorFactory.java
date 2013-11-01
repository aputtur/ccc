package com.copyright.ccc.web.transaction;

import java.util.Date;

import com.copyright.ccc.business.data.TransactionItem;

/**
 * This factory class builds instances of <code>PricingError</code>.
 */
public class PricingErrorFactory
{
    /**
     * Returns an instance of <code>PricingError</code> associated with the <code>SpecialOrderLimitsExceededException</code>.
     * 
     * @param errorReason - the string representing the field that caused the SpecialOrderLimitsExceededException.
     * @return the PricingError associated with the SpecialOrderLimitsExceededException.
     */
    public static PricingError constructLimitsExceededSpecialOrderError(String errorReason)
    {
        return new PricingError(
            "icon-specialorder",
            "pricing.error.message.limitsExceededSpecialOrder",
            errorReason,
            TransactionConstants.PRICING_ERROR_ACTION_SPECIAL_ORDER);
    }
    
    //  2009-05-07  MSJ RIGHTSLINK
    //  I hotly debated this alternate method in my head for a solid day.  I wanted to 
    //  simply place a flag in session and read it to determine whether or not I should
    //  pick out an alternate resource key... but...  we already shove so much stuff into
    //  session I decided to go the far more verbose way.  This will also require me to
    //  make 10 or 20 or more changes in the *AddCartItem* action classes.  More opportunities
    //  for bugs to rear their ugly heads.  I am still, even while typing this, despising my
    //  choice.  Whoever encounters this, please forgive me.  Also, see BasicTransactionAction,
    //  and SpecialOrderAction.  Search for "rightslink" in CC2 code to see what other
    //  damage I've wrought.
    
    /**
     * Returns an instance of <code>PricingError</code> associated with the <code>SpecialOrderLimitsExceededException</code>.
     * 
     * @param errorReason - the string representing the field that caused the SpecialOrderLimitsExceededException.
     * @return the PricingError associated with the SpecialOrderLimitsExceededException.
     */
    public static PricingError constructLimitsExceededSpecialOrderErrorWithRightslink(String errorReason)
    {
        return new PricingError(
            "icon-specialorder",
            "pricing.error.message.limitsExceededSpecialOrder.rightslink",
            errorReason,
            TransactionConstants.PRICING_ERROR_ACTION_SPECIAL_ORDER);
    }

    /**
     * Returns an instance of <code>PricingError</code> associated with the <code>DeniedLimitsExceededException</code>.
     * 
     * @param errorReason - the string reprenting the field that caused the DeniedLimitsExceededException.
     * @return the PricingError associated with the DeniedLimitsExceededException.
     */
    public static PricingError constructLimitsExceededDeniedError(String errorReason)
    {
        return new PricingError(
            "icon-unavailable",
            "pricing.error.message.limitsExceededDenied",
            errorReason,
            TransactionConstants.PRICING_ERROR_ACTION_NONE);
    }
    
    /**
     * Returns an instance of <code>PricingError</code> associated with the <code>DeniedLimitsExceededException</code>.
     * 
     * @param errorReason - the string reprenting the field that caused the DeniedLimitsExceededException.
     * @return the PricingError associated with the DeniedLimitsExceededException.
     */
    public static PricingError constructLimitsExceededDeniedErrorWithRightslink(String errorReason)
    {
        return new PricingError(
            "icon-unavailable",
            "pricing.error.message.limitsExceededDenied.rightslink",
            errorReason,
            TransactionConstants.PRICING_ERROR_ACTION_NONE);
    }
    
    /**
     * Returns an instance of <code>PricingError</code> associated with the <code>DeniedUnavailableException</code>.
     * 
     * @return the PricingError associated with the DeniedUnavailableException.
     */
    public static PricingError constructNotAvailableDeniedError()
    {
        return new PricingError(
            "icon-unavailable", 
            "pricing.error.message.notAvailableDenied",
            TransactionConstants.PRICING_ERROR_ACTION_NONE);
    }
    
    /**
     * Returns an instance of <code>PricingError</code> associated with the <code>DeniedUnavailableException</code>.
     * 
     * @return the PricingError associated with the DeniedUnavailableException.
     */
    public static PricingError constructNotAvailableDeniedErrorWithRightslink()
    {
        return new PricingError(
            "icon-unavailable", 
            "pricing.error.message.notAvailableDenied.rightslink",
            TransactionConstants.PRICING_ERROR_ACTION_NONE);
    }

    /**
     * Returns an instance of <code>PricingError</code> associated with the <code>SpecialOrderUnavailableException</code>.
     * 
     * @return the PricingError associated with the SpecialOrderUnavailableException.
     */
    public static PricingError constructNotAvailableSpecialOrderError()
    {
        return new PricingError(
            "icon-specialorder", 
            "pricing.error.message.notAvailableSpecialOrder",
            TransactionConstants.PRICING_ERROR_ACTION_SPECIAL_ORDER);
    }
    
    /**
     * Returns an instance of <code>PricingError</code> associated with the <code>SpecialOrderUnavailableException</code>.
     * 
     * @return the PricingError associated with the SpecialOrderUnavailableException.
     */
    public static PricingError constructNotAvailableSpecialOrderErrorWithRightslink()
    {
        return new PricingError(
            "icon-specialorder", 
            "pricing.error.message.notAvailableSpecialOrder.rightslink",
            TransactionConstants.PRICING_ERROR_ACTION_SPECIAL_ORDER);
    }
 
    /**
     * Returns an instance of <code>PricingError</code> associated with either the <code>ContactRHDirectlyLimitsExceededException</code>
     * or the <code>ContactRHDirectlyUnavailableException</code>.  The error message for this <code>PricingError</code> instance contains
     * a link to a page displaying the <code>TransactionItem</code>'s rightsholder information.
     * 
     * @param transactionItem - the TransactionItem that caused the PricingError.
     * @return the PricingError associated with the Contact RH Directory exception.
     */
    public static PricingError constructContactRHDirectlyError( TransactionItem transactionItem )
    {
        long rightInst = transactionItem.getRgtInst();
        long dateOfUse = (new Date()).getTime();
        if( transactionItem.isDigital() )
            dateOfUse = transactionItem.getDateOfUse().getTime();
        
        return new PricingError(
            "icon-contact", 
            "pricing.error.message.contactRHDirectly",
            rightInst+","+dateOfUse,
            TransactionConstants.PRICING_ERROR_ACTION_NONE);
    }
    
    public static PricingError constructContactRHDirectlyErrorWithRightslink( TransactionItem transactionItem )
    {
        long rightInst = transactionItem.getRgtInst();
        long dateOfUse = (new Date()).getTime();
        if( transactionItem.isDigital() )
            dateOfUse = transactionItem.getDateOfUse().getTime();
        
        return new PricingError(
            "icon-contact", 
            "pricing.error.message.contactRHDirectly.rightslink",
            rightInst+","+dateOfUse,
            TransactionConstants.PRICING_ERROR_ACTION_NONE);
    }
    
    /**
     * Returns an instance of <code>PricingError</code> associated with either the <code>ChangedToRegularOrderException</code> or the
     * <code>ChangedToRegularFromSpecialUnavailableException</code>.
     * 
     * @return the PricingError associated with the Changed To Regular Order exception.
     */
    public static PricingError constructChangedToRegularOrderError()
    {
        return new PricingError(
            "icon-check", 
            "pricing.error.message.changedToRegularOrder",
            TransactionConstants.PRICING_ERROR_ACTION_REGULAR_ORDER);
    }
    
    /**
     * Returns and instance of <code>PricingError</code> associated with the <code>SystemLimitsExceededException</code>.
     * 
     * @return the PricingError associated with the SystemLimitsExceededException.
     */
    public static PricingError constructSystemLimitsExceededError()
    {
        return new PricingError(
            "icon-unavailable",
            "pricing.error.message.systemLimitsExceeded",
            TransactionConstants.PRICING_ERROR_ACTION_NONE);
    }
    
    public static PricingError constructServiceInvocationExceptionError()
    {
        return new PricingError(
            "icon-unavailable",
            "pricing.error.message.serviceInvocationException",
            TransactionConstants.PRICING_ERROR_ACTION_NONE);
    }
}
