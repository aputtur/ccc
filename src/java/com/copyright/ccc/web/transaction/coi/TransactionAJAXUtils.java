package com.copyright.ccc.web.transaction.coi;

import java.util.Iterator;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ResponseUtils;

import com.copyright.ccc.business.data.TransactionItem;
import com.copyright.ccc.business.services.cart.InvalidAttributesException;
import com.copyright.ccc.web.transaction.PricingError;
import com.copyright.data.SharedAppResources;

/**     pricing error - valid status values: success, error, validationError **/
    
public class TransactionAJAXUtils
{
    
    
    public static String generateSuccessResponse( String itemPrice, TransactionItem transactionItem )
    {
       String successResponse = "<status>success</status><price>" + itemPrice + "</price>";
       return generateCompleteResponse( successResponse, transactionItem );
    }
    
    public static String generateDateNotInBiactiveRangeXMLErrorResponse( MessageResources messageResources, TransactionItem transactionItem )
    {
        String dateNotInBiactiveRangeError = messageResources.getMessage( "errors.dateNotInRange", "Publication year of title being used" );
        
        String errorXmlResponse = "<status>validationError</status>" +
            "<validationErrorMessage>" + ResponseUtils.filter( dateNotInBiactiveRangeError ) + "</validationErrorMessage>";
        
        return generateCompleteResponse( errorXmlResponse, transactionItem );
    }
    
    public static String generateXMLPricingErrorResponse( MessageResources messageResources, PricingError pricingError, TransactionItem transactionItem )
    {
        String errorMessage = messageResources.getMessage( pricingError.getErrorMessageKey(), pricingError.getErrorMessageArg());
        
        String errorXmlResponse = "<status>error</status>" + 
            "<errorIcon>" + pricingError.getErrorIcon() + "</errorIcon>" +
            "<errorMessage>" + ResponseUtils.filter(errorMessage) + "</errorMessage>" + 
            "<errorAction>" + pricingError.getErrorAction() + "</errorAction>";
        
        return generateCompleteResponse(errorXmlResponse, transactionItem);
    }
    
    public static String generateXMLInvalidAttributesErrorResponse( InvalidAttributesException iae, TransactionItem transactionItem )
    {
        StringBuffer validationErrorResponse = new StringBuffer();
        
        Iterator<String> validationErrorsIterator = iae.getValidationMessageCodes().iterator();
        validationErrorResponse.append("<status>validationError</status>");
        
        SharedAppResources sharedAppResources = SharedAppResources.getInstance();
        while( validationErrorsIterator.hasNext() )
        {
            validationErrorResponse.append(
                "<validationErrorMessage>" + sharedAppResources.getValue( validationErrorsIterator.next() ) + "</validationErrorMessage>");
        }
        
        return generateCompleteResponse( validationErrorResponse.toString(), transactionItem );
    }
    
    private static String generateCompleteResponse( String innerXMLResponse, TransactionItem transactionItem )
    {
        String publicationYearRange = TransactionUtils.getExistingPublicationYearRange( transactionItem );
        
        StringBuffer completeResponse = new StringBuffer();
        completeResponse.append( "<ajaxResponse>" );
        completeResponse.append( innerXMLResponse );
        completeResponse.append( "<publicationYearRange>" + publicationYearRange + "</publicationYearRange>");
        completeResponse.append( "</ajaxResponse>" );
        
        return completeResponse.toString();
    }
}
