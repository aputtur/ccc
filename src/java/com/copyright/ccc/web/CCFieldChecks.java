package com.copyright.ccc.web;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.CreditCardValidator;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.FieldChecks;
import org.apache.struts.validator.Resources;

import com.copyright.ccc.util.LogUtil;
import com.copyright.data.payment.CreditCard;
import com.copyright.workbench.time.DateUtils2;
import com.mindprod.entities.StripEntities;


/**
 * This class contains CCC-specific validations that are used in the
 * validator-rules.xml file.
 */
public class CCFieldChecks extends FieldChecks
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Logger _logger = Logger.getLogger( CCFieldChecks.class );
    
    private static final String FIELD_TEST_NOTEQUAL = "NOTEQUAL";
    private static final String FIELD_TEST_CONTAINS = "CONTAINS";
    private static final String CREDIT_CARD_TYPE_PROPERTY = "creditCardTypeProperty";

    /**
     * Determines whether or not the validation should continue based on 
     * condition.  If returns true, then validators listed after this validator 
     * will be run.  Thus, if condition is not satisfied, this validate method
     * returns false, but no errors are added to the ActionErrors
     * 
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being 
     * performed.
     * @param field The <code>Field</code> object associated with the current 
     * field being validated.
     * @param validator The <code>Validator</code> instance, used to access 
     * other field values.
     * @param request Current request object.
     * @return true if meets stated requirements, false otherwise.
     */
    public static boolean validateForCondition(Object bean,
                                             ValidatorAction va, Field field,
                                             org.apache.commons.validator.Validator validator,
                                             HttpServletRequest request) {
                                                 
        Object form = validator.getParameterValue(Validator.BEAN_PARAM);

        boolean isConditionSatisfied = true;
        String currConditionJoin = "AND";
        
        int i = 0;
       
        while (!GenericValidator.isBlankOrNull(field.getVarValue("conditionField[" + i + "]"))) {
            String conditionProp = field.getVarValue("conditionField[" + i + "]");
            String conditionTest = field.getVarValue("conditionFieldTest[" + i + "]");
            String conditionTestValue = field.getVarValue("conditionFieldValue[" + i + "]");
            String conditionIndexed = field.getVarValue("conditionFieldIndexed[" + i + "]");
            if (!GenericValidator.isBlankOrNull(field.getVarValue("conditionJoin[" + i + "]"))) {
                currConditionJoin = field.getVarValue("conditionJoin[" + i + "]");
            }
            
            if (conditionIndexed == null) {
                conditionIndexed = "false";
            }
            
            String dependVal = null;
            boolean isThisConditionSatisfied = false;
            if (field.isIndexed() && conditionIndexed.equalsIgnoreCase("true")) {
                String key = field.getKey();
                if ((key.indexOf("[") > -1) && (key.indexOf("]") > -1)) {
                    String ind = key.substring(0, key.indexOf(".") + 1);
                    conditionProp = ind + conditionProp;
                }
            }
            
            dependVal = ValidatorUtils.getValueAsString(form, conditionProp);
            if (conditionTest.equals(FIELD_TEST_NULL)) {
                if ((dependVal != null) && (dependVal.length() > 0)) {
                    isThisConditionSatisfied = false;
                } else {
                    isThisConditionSatisfied = true;
                }
            }
            
            else if (conditionTest.equals(FIELD_TEST_NOTNULL)) {
                if ((dependVal != null) && (dependVal.length() > 0)) {
                    isThisConditionSatisfied = true;
                } else {
                    isThisConditionSatisfied = false;
                }
            }
            
            else if (conditionTest.equals(FIELD_TEST_EQUAL)) {
                isThisConditionSatisfied = conditionTestValue.equalsIgnoreCase(dependVal);
            }
            
            else if (conditionTest.equals(FIELD_TEST_NOTEQUAL)) 
            {
                isThisConditionSatisfied = !conditionTestValue.equalsIgnoreCase(dependVal);
            }
            
            else if(conditionTest.equals(FIELD_TEST_CONTAINS))
            {
                if( dependVal != null)
                {
                  isThisConditionSatisfied = dependVal.indexOf(conditionTestValue) > -1;
                }
            }
            
            if (currConditionJoin.equalsIgnoreCase("AND")) {
                isConditionSatisfied = isConditionSatisfied && isThisConditionSatisfied;
            } else {
                isConditionSatisfied = isConditionSatisfied || isThisConditionSatisfied;
            }
           
            i++;
        }
        
        if(isConditionSatisfied)
            return true;
        else return false;
    }
    
    /**
     *  Checks if the field is a valid date. First it checks if the field is of 
     *  type java.util.Date.  If so, it passes validation.  If not, then it runs
     *  the FieldChecks validateDate method:  If the field has a datePattern variable,
     *  that will be used to format <code>java.text.SimpleDateFormat</code>. If the
     *  field has a datePatternStrict variable, that will be used to format <code>java.text.SimpleDateFormat</code>
     *  and the length will be checked so '2/12/1999' will not pass validation with
     *  the format 'MM/dd/yyyy' because the month isn't two digits. If no datePattern
     *  variable is specified, then the field gets the DateFormat.SHORT format for
     *  the locale. The setLenient method is set to <code>false</code> for all variations.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateCCDate(Object bean,
                                    ValidatorAction va, Field field,
                                    ActionMessages errors,
                                    Validator validator,
                                    HttpServletRequest request) {
                                    
        String allowDateType = field.getVarValue( "allowDateType" );
        
        if( !StringUtils.isEmpty(allowDateType) && allowDateType.equalsIgnoreCase("true") )
        {
            try
            {
                Object oValue = PropertyUtils.getProperty(bean, field.getProperty());
                
                if(oValue instanceof Date) return oValue;
                
            } catch (NoSuchMethodException e)
            {
                _logger.error(e.getMessage(), e);
            } catch (IllegalAccessException e)
            {
                _logger.error(e.getMessage(), e);
            } catch (InvocationTargetException e)
            {
                _logger.error(e.getMessage(), e);
            }
            
            
        }
        
        return FieldChecks.validateDate( bean, va, field, errors, validator, request);
    }
    
    /**
     * Checks whether or not the field value passed in is a date that is
     * either in the current or a future year.
     */
    public static boolean validateCurrentOrFutureYear( Object bean,
                                    ValidatorAction va, Field field,
                                    ActionMessages errors,
                                    Validator validator,
                                    HttpServletRequest request )
    {

        Object oValue;

        try
        {
            oValue = PropertyUtils.getProperty( bean, field.getProperty() );
            
            if( oValue instanceof Date )
            {
                Date dateValue = (Date)oValue;
                int dateYear = DateUtils2.getYear( dateValue.getTime() );
                
                Date today = new Date();
                int currentYear = DateUtils2.getYear( today.getTime() );
                
                if( dateYear < currentYear )
                {
                    errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                    return false;
                }
            }
        } 
        catch (Exception e)
        {
            _logger.error(e.getMessage(), e);
        }
        
        return true;
    }

    
    /**
     * Checks whether or not the field value passed in is a date that is
     * not more than 6 months prior to current date.
     */
    public static boolean validateSixMonthsPrior( Object bean,
                                    ValidatorAction va, Field field,
                                    ActionMessages errors,
                                    Validator validator,
                                    HttpServletRequest request )
    {

        Object oValue;

        try
        {
            oValue = PropertyUtils.getProperty( bean, field.getProperty() );
            
            if( oValue instanceof Date )
            {
                Date dateValue = (Date)oValue;
                
                //Set the date to 6 months prior
                 Calendar cal = Calendar.getInstance();
                 cal.add(Calendar.MONTH,-6);
                 Date sixMonths = DateUtils2.roundUp(cal.getTime(), Calendar.MONTH);
                 
                if( dateValue.before(sixMonths) )
                {
                    errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                    return false;
                }
            }
        } 
        catch (Exception e)
        {
            _logger.error(e.getMessage(), e);
        }
        
        return true;
    }

    /**
    * Checks if the field value contains any HTML script tags.  This includes 'javascript:', "&lt;script",
    * "&lt;/script" or any url encoded or html entity equivalent of these strings.
    * @return true if the query parameters contain no scripting elements
    */
    public static boolean validateNoScripts(String string) 
    {
        boolean rtnValue = false;
        try {
           string = URLDecoder.decode(string, "UTF-8"); 
        }
        catch (UnsupportedEncodingException uee) {
           _logger.warn("CCFieldChecks.validateNoScripts() Encoding Exception: " + LogUtil.appendableStack(uee));
        }
        catch (IllegalArgumentException iae) {
           _logger.warn("CCFieldChecks.validateNoScripts() Encoding Exception: " + LogUtil.appendableStack(iae));
        }
        
        string = StripEntities.stripEntities(string);
        String pattern = ".*(javascript(:|%3a)|(<|%3c)/?script).*";
        Pattern scriptPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);

        Matcher scriptMatcher = scriptPattern.matcher(string);

        rtnValue = !scriptMatcher.matches();

        return rtnValue;
    }
    
    /**
     * Checks if the field value contains a valid standard number (i.e. ISBN10, ISBN13, ISSN)
     */
    public static boolean validateStandardNumber( Object bean,
                                    ValidatorAction va, Field field,
                                    ActionMessages errors,
                                    Validator validator,
                                    HttpServletRequest request )
    {
        String value = null;
        if (isString(bean)) 
            value = (String) bean;
        else 
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        
        boolean isValidStandardNumber = false;
        
        if( value == null ) return isValidStandardNumber;
        
        String withHyphensRemoved = value.replaceAll("-","").trim();
        
        int valueLength = withHyphensRemoved.length();
        if( valueLength == 10)
            isValidStandardNumber = isValidISBN10( withHyphensRemoved );
        else if( valueLength == 13)
            isValidStandardNumber = isValidISBN13( withHyphensRemoved );
        else if( valueLength == 8)
            isValidStandardNumber = isValidISSN( withHyphensRemoved );
        
        if( !isValidStandardNumber)
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        
        return isValidStandardNumber;
    }
    
    private static boolean isValidISBN10(String isbn)
    {
        if( isbn.length() != 10) return false;
        
        int sumOfFirstNine = 0;
        for(int i = 0; i < isbn.length() - 1; i++)
        {
            try
            {
                sumOfFirstNine += Integer.parseInt(isbn.substring(i, i+1)) * (10 - i);
            }
            catch( NumberFormatException nfe )
            {
                return false;
            }
        }
        
        int calculatedCheckDigit = 11 - (sumOfFirstNine % 11);
        
        String checkDigit = (calculatedCheckDigit == 10) ? "X" : String.valueOf(calculatedCheckDigit);

        return checkDigit.equals(isbn.substring(9,10));
    }
    
    private static boolean isValidISBN13(String isbn)
    {
        if( isbn.length() != 13 ) return false;
        
        int sumOfFirstTwelve = 0;
        for(int i = 0; i < isbn.length() - 1; i++)
        {
            try
            {
                int weight = (i%2 == 0) ? 1 : 3;
                sumOfFirstTwelve += Integer.parseInt(isbn.substring(i, i+1)) * weight;
            }
            catch( NumberFormatException nfe)
            {
                return false;
            }
        }
        
        int calculatedCheckDigit = 10 - (sumOfFirstTwelve % 10);
        
        String checkDigit = (calculatedCheckDigit == 10) ? "0" : String.valueOf(calculatedCheckDigit);
        
        return checkDigit.equals(isbn.substring(12,13));
    }
    
    private static boolean isValidISSN(String issn)
    {
        if( issn.length() != 8 ) return false;
        
        int sumOfFirstSeven = 0;
        for(int i = 0; i < issn.length() - 1; i++)
        {
            try
            {
                sumOfFirstSeven += Integer.parseInt(issn.substring(i, i+1)) * (8 - i);
            }
            catch( NumberFormatException  nfe)
            {
                return false;
            }
        }
        
        int calculatedCheckDigit = 11 - (sumOfFirstSeven % 11);
        
        String checkDigit = (calculatedCheckDigit == 10) ? "X" : String.valueOf(calculatedCheckDigit);
        
        return checkDigit.equals(issn.substring(7,8));
    }
    
    /**
     * Checks if the field value contains a valid credit card number based on the credit card type.  The credit
     * card type is stored in the form property passed into the validator.
     */
    public static boolean validateCreditCardForType( Object bean,
                                    ValidatorAction va, Field field,
                                    ActionMessages errors,
                                    Validator validator,
                                    HttpServletRequest request )
    {
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return true;
        }
        
        Object form = validator.getParameterValue(Validator.BEAN_PARAM);
        String creditCardTypeProperty = field.getVarValue(CREDIT_CARD_TYPE_PROPERTY);
        String creditCardType = StringUtils.isEmpty( creditCardTypeProperty ) ? "" : ValidatorUtils.getValueAsString( form, creditCardTypeProperty );
        
        CreditCardValidator creditCardValidator = getCreditCardValidatorForType( creditCardType );
        
        boolean isValidCreditCardForType = creditCardValidator.isValid( value );
        
        if( !isValidCreditCardForType )
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        
        return isValidCreditCardForType;
    }
    
    private static CreditCardValidator getCreditCardValidatorForType( String creditCardType )
    {   
        if( creditCardType.equals( CreditCard.CC_TYPE_AMEX ) )
            return new CreditCardValidator( CreditCardValidator.AMEX );
        else if( creditCardType.equals( CreditCard.CC_TYPE_MASTERCARD ) )
            return new CreditCardValidator( CreditCardValidator.MASTERCARD );
        else if( creditCardType.equals( CreditCard.CC_TYPE_VISA ) )
            return new CreditCardValidator( CreditCardValidator.VISA );
        else
            return new CreditCardValidator();
    }
    
}
