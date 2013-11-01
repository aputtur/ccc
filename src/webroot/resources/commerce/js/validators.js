var FIELD_TEST_NULL = "NULL";
var FIELD_TEST_NOTNULL = "NOTNULL";
var FIELD_TEST_EQUAL = "EQUAL";
var FIELD_TEST_NOTEQUAL = "NOTEQUAL";
var FIELD_TEST_CONTAINS = "CONTAINS";
var JOIN_AND = "AND";

function cccValidateForCondition(form, validationFormName)
{  
    var isValid = true;

    var conditionValidator = eval('new ' + validationFormName + '_validateForCondition()');
    
    for (fieldIndex in conditionValidator) {
    
        var fieldName = conditionValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var isConditionSatisfied = true;
   
        var conditionFields = conditionValidator[fieldIndex][2]("conditionField");
        var conditionFieldTests = conditionValidator[fieldIndex][2]("conditionFieldTest");
        var conditionFieldValues = conditionValidator[fieldIndex][2]("conditionFieldValue");
        var conditionJoins = conditionValidator[fieldIndex][2]("conditionJoin");
        
        for(var i = 0; i < conditionFields.length; i++)
        {
            var conditionField, conditionFieldTest, conditionFieldValue, conditionJoin;
            
            conditionField = form[conditionFields[i]];
            conditionFieldTest = conditionFieldTests[i];
            conditionFieldValue = conditionFieldValues[i];
            conditionJoin = conditionJoins[i];

            if(conditionFieldValue == undefined)
                conditionFieldValue = "";
            if(conditionJoin == undefined)
                conditionJoin = JOIN_AND;
            
            var isCurrentConditionSatisfied = false;
            
            var value = getFieldValue(conditionField);
            
            if(conditionFieldTest == FIELD_TEST_NULL)
            {
                if(trim(value).length == 0) isCurrentConditionSatisfied = true;
                else isCurrentConditionSatisfied = false;
            }
            else if(conditionFieldTest == FIELD_TEST_NOTNULL)
            {
                if(trim(value).length > 0) isCurrentConditionSatisfied = true;
                else isCurrentConditionSatisfied = false;
            }
            else if(conditionFieldTest == FIELD_TEST_EQUAL)
            {
                if(trim(value) == conditionFieldValue) isCurrentConditionSatisfied = true;
                else isCurrentConditionSatisfied = false;
            }
            else if(conditionFieldTest == FIELD_TEST_NOTEQUAL)
            {
                if(trim(value) != conditionFieldValue) isCurrentConditionSatisfied = true;
                else isCurrentConditionSatisfied = false;
            }
            else if(conditionFieldTest == FIELD_TEST_CONTAINS)
            {
                if(trim(value).indexOf(conditionFieldValue) > -1) isCurrentConditionSatisfied = true;
                else isCurrentConditionSatisfied = false;
            }
            
            if(conditionJoin == JOIN_AND)
                isConditionSatisfied = isConditionSatisfied && isCurrentConditionSatisfied;
            else
                isConditionSatisfied = isConditionSatisfied || isCurrentConditionSatisfied;
        }
        
        isValid = isValid && isConditionSatisfied;
        
        if(!isConditionSatisfied)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + fieldName + "', false, '')");
    }

    return isValid;
}

function cccValidateRequired(form, validationFormName)
{
    var isValid = true;
   
    var requiredValidator = eval('new ' + validationFormName + '_required()');

    for (fieldIndex in requiredValidator) {
    
        var fieldName = requiredValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;

        var field = form[fieldName];
        
        var isCurrentValid = true;
        
        if(field == undefined) continue;

        if ((field.type == 'hidden' || field.type == 'text' || field.type == 'textarea' || field.type == 'file' ||
            field.type == 'checkbox' || field.type == 'select-one' || field.type == 'password') && field.disabled == false) {

            var value = getFieldValue(field);
            
            if (trim(value).length == 0) 
                isCurrentValid = false;
                
        } else if (field.type == "select-multiple") { 
            var numOptions = field.options.length;
            lastSelected=-1;
            for(loop=numOptions-1;loop>=0;loop--) {
                if(field.options[loop].selected) {
                    lastSelected = loop;
                    value = field.options[loop].value;
                    break;
                }
            }
            if(lastSelected < 0 || trim(value).length == 0) 
                isCurrentValid = false;
                
        } else if ((field.length > 0) && (field[0].type == 'radio' || field[0].type == 'checkbox')) {
            isChecked=-1;
            for (loop=0;loop < field.length;loop++) {
                if (field[loop].checked) {
                    isChecked=loop;
                    break; // only one needs to be checked
                }
            }
            if (isChecked < 0) 
                isCurrentValid = false;
                
        }
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + requiredValidator[fieldIndex][1] + "')");
            
        isValid = isValid && isCurrentValid;
    }
  
    return isValid;
}

function cccValidateInteger(form, validationFormName)
{
    return cccValidateNumber(form, validationFormName, "integer");
}

function cccValidateIntRange(form, validationFormName)
{   
    var isValid = true;
   
    var intRangeValidator = eval('new ' + validationFormName + '_intRange()');

    for (fieldIndex in intRangeValidator) {
    
        var fieldName = intRangeValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        if (value.length > 0) {
            var iMin = parseInt(intRangeValidator[fieldIndex][2]("min"));
            var iMax = parseInt(intRangeValidator[fieldIndex][2]("max"));
            var iValue = parseInt(value);
            if (!(iValue >= iMin && iValue <= iMax)) 
                isCurrentValid = false;
        }

        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + intRangeValidator[fieldIndex][1] + "')");
            
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

function cccValidateLong(form, validationFormName)
{ 
    return cccValidateNumber(form, validationFormName, "long");
}

function cccValidateDate(form, validationFormName, methodName)
{
    var isValid = true;
    
    var dateValidator = eval('new ' + validationFormName + methodName + ' ()');
    
    for (fieldIndex in dateValidator) {
    
        var fieldName = dateValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        var datePattern = dateValidator[fieldIndex][2]("datePatternStrict");
        if (datePattern == null)
            datePattern = dateValidator[fieldIndex][2]("datePattern"); // try loose pattern
        
        if (value.length > 0 && datePattern.length > 0 ) {
            var MONTH = "MM";
            var DAY = "dd";
            var YEAR = "yyyy";
            var orderMonth = datePattern.indexOf(MONTH);
            var orderDay = datePattern.indexOf(DAY);
            var orderYear = datePattern.indexOf(YEAR);
            if ((orderDay < orderYear && orderDay > orderMonth)) {
                var iDelim1 = orderMonth + MONTH.length;
                var iDelim2 = orderDay + DAY.length;
                var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
                var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
                if (iDelim1 == orderDay && iDelim2 == orderYear) {
                    dateRegexp = new RegExp("^(\\d{2})(\\d{2})(\\d{4})$");
                } else if (iDelim1 == orderDay) {
                    dateRegexp = new RegExp("^(\\d{2})(\\d{2})[" + delim2 + "](\\d{4})$");
                } else if (iDelim2 == orderYear) {
                    dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})(\\d{4})$");
                } else {
                    dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{4})$");
                }
                var matched = dateRegexp.exec(value);
                if(matched != null) {
                    if (!isValidDate(matched[2], matched[1], matched[3])) 
                       isCurrentValid = false;
                 } else isCurrentValid = false;
            } 
            else if ((orderMonth < orderYear && orderMonth > orderDay)) 
            {
                var iDelim1 = orderDay + DAY.length;
                var iDelim2 = orderMonth + MONTH.length;
                var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
                var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
                if (iDelim1 == orderMonth && iDelim2 == orderYear) {
                    dateRegexp = new RegExp("^(\\d{2})(\\d{2})(\\d{4})$");
                } else if (iDelim1 == orderMonth) {
                    dateRegexp = new RegExp("^(\\d{2})(\\d{2})[" + delim2 + "](\\d{4})$");
                } else if (iDelim2 == orderYear) {
                    dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})(\\d{4})$");
                } else {
                    dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{4})$");
                }
                var matched = dateRegexp.exec(value);
                if(matched != null) {
                    if (!isValidDate(matched[1], matched[2], matched[3]))
                        isCurrentValid = false;
                } else isCurrentValid = false;
            } else if ((orderMonth > orderYear && orderMonth < orderDay)) {
                var iDelim1 = orderYear + YEAR.length;
                var iDelim2 = orderMonth + MONTH.length;
                var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
                var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
                if (iDelim1 == orderMonth && iDelim2 == orderDay) {
                    dateRegexp = new RegExp("^(\\d{4})(\\d{2})(\\d{2})$");
                } else if (iDelim1 == orderMonth) {
                    dateRegexp = new RegExp("^(\\d{4})(\\d{2})[" + delim2 + "](\\d{2})$");
                } else if (iDelim2 == orderDay) {
                    dateRegexp = new RegExp("^(\\d{4})[" + delim1 + "](\\d{2})(\\d{2})$");
                } else {
                    dateRegexp = new RegExp("^(\\d{4})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{2})$");
                }
                var matched = dateRegexp.exec(value);
                if(matched != null) {
                    if (!isValidDate(matched[3], matched[2], matched[1])) 
                        isCurrentValid = false;
                } else isCurrentValid = false;
            } else isCurrentValid = false;
        }
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + dateValidator[fieldIndex][1] + "')");
            
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

function cccValidateCCDate(form, validationFormName, methodName)
{
    var isValid = true;
    
    var dateValidator = eval('new ' + validationFormName + '_ccDate()');
    
    for (fieldIndex in dateValidator) {
    
        var fieldName = dateValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        var strict = false;
        var datePattern = dateValidator[fieldIndex][2]("datePatternStrict");
        if (datePattern != null) strict = true;
        
        if (value.length > 0) 
            isCurrentValid = isValidCCDate( value, strict );
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + dateValidator[fieldIndex][1] + "')");
            
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

/* assumes that is in formats: "MM/dd/yyyy", "M/dd/yyyy", "M/d/yyyy", "MM/yyyy", "M/yyyy" */
function cccValidateCurrentOrFutureYear(form, validationFormName)
{
    var isValid = true;
    
    var currentOrFutureYearValidator = eval('new ' + validationFormName + '_currentOrFutureYear ()');
    
    for (fieldIndex in currentOrFutureYearValidator) {
    
        var fieldName = currentOrFutureYearValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        isCurrentValid = isDateInCurrentOrFutureYear( value );
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + currentOrFutureYearValidator[fieldIndex][1] + "')");
        
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

function cccValidateSixMonthsPrior(form, validationFormName)
{
    var isValid = true;
    
    var sixMonthsPriorValidator = eval('new ' + validationFormName + '_sixMonthsPrior ()');
    
    for (fieldIndex in sixMonthsPriorValidator) {
    
        var fieldName = sixMonthsPriorValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        isCurrentValid = isDateSixMonthsPrior( value );
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + sixMonthsPriorValidator[fieldIndex][1] + "')");
        
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

function cccValidateCCPriorDate(form, validationFormName)
{
    var isValid = true;
    
    var ccPriorDateValidator = eval('new ' + validationFormName + '_ccPriorDate ()');
    
    for (fieldIndex in ccPriorDateValidator) {
    
        var fieldName = ccPriorDateValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        isCurrentValid = isDatePriorToToday( value );
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + ccPriorDateValidator[fieldIndex][1] + "')");
        
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

function cccValidateMask(form, validationFormName)
{
    var isValid = true;
    
    var maskValidator = eval('new ' + validationFormName + '_mask ()');
    
    for (fieldIndex in maskValidator) {
    
        var fieldName = maskValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        var field = form[maskValidator[fieldIndex][0]];

        if (!matchPattern(field.value, maskValidator[fieldIndex][2]("mask"))) {
            isCurrentValid = false;
        }

        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + maskValidator[fieldIndex][1] + "')");
            
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

function cccValidateNumber(form, validationFormName, numberType)
{ 
    var isValid = true;
    
    var numberValidator = eval('new ' + validationFormName + '_' + numberType + '()');

    for (fieldIndex in numberValidator) {
    
        var fieldName = numberValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        if (value.length > 0) {

            if (!isAllDigits(value)) {
                isCurrentValid = false;
            } else {
                var iValue = parseInt(value);
                if (isNaN(iValue) || !(iValue >= -2147483648 && iValue <= 2147483647))
                    isCurrentValid = false;
            }
        }
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + numberValidator[fieldIndex][1] + "')");
            
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

function cccValidateStandardNumber(form, validationFormName)
{
    var isValid = true;
    
    var standardNumberValidator = eval('new ' + validationFormName + '_standardNumber ()');
    
    for (fieldIndex in standardNumberValidator) {
    
        var fieldName = standardNumberValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        isCurrentValid = isValidStandardNumber( trim(value) );
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + standardNumberValidator[fieldIndex][1] + "')");
        
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}

function isValidStandardNumber(standardNumber)
{
    var withoutHyphens = standardNumber.replace(/-/g,"");
    
    var standardNumberLength = withoutHyphens.length;
    
    if(standardNumberLength == 10)
        return isValidISBN10(withoutHyphens);
    else if(standardNumberLength == 13)
        return isValidISBN13(withoutHyphens);
    else if(standardNumberLength == 8)
        return isValidISSN(withoutHyphens);
    
    else return false;
}

function isValidISBN10(isbn)
{
    if( isbn.length != 10) return false;
        
    var sumOfFirstNine = 0;
    for(var i = 0; i < isbn.length - 1; i++)
    {
        var currentDigit = parseInt(isbn.substring(i, i+1));
        if( isNaN(currentDigit) ) return false;
        sumOfFirstNine += currentDigit * (10 -  i);
    }
        
    var calculatedCheckDigit = 11 - (sumOfFirstNine % 11);
        
    var checkDigit = (calculatedCheckDigit == 10) ? "X" : "" + calculatedCheckDigit;

    return checkDigit == isbn.substring(9,10);
}

function isValidISBN13(isbn)
{
    if( isbn.length != 13 ) return false;
        
    var sumOfFirstTwelve = 0;
    for(var i = 0; i < isbn.length - 1; i++)
    {
        var weight = (i % 2 == 0) ? 1 : 3;
        var currentDigit = parseInt(isbn.substring(i, i+1));
        if( isNaN(currentDigit) ) return false;
        sumOfFirstTwelve += currentDigit * weight;
    }
        
    var calculatedCheckDigit = 10 - (sumOfFirstTwelve % 10);
        
    var checkDigit = (calculatedCheckDigit == 10) ? "0" : "" + calculatedCheckDigit;
        
    return checkDigit == isbn.substring(12,13);
}

function isValidISSN(issn)
{
    if( issn.length != 8 ) return false;
        
    var sumOfFirstSeven = 0;
    for(var i = 0; i < issn.length - 1; i++)
    {
        var currentDigit = parseInt(issn.substring(i, i+1));
        if( isNaN(currentDigit) ) return false;
        sumOfFirstSeven += currentDigit * (8 - i);
    }
        
    var calculatedCheckDigit = 11 - (sumOfFirstSeven % 11);
        
    var checkDigit = (calculatedCheckDigit == 10) ? "X" : "" + calculatedCheckDigit;
        
    return checkDigit == issn.substring(7,8);
}

function isFieldValid(validationFormName, fieldName)
{
    return eval(validationFormName + "_ValidationResult.getIsValid('" + fieldName + "')");
}

function getFieldValue(field)
{
    if(field == undefined) return "";
    
    if(field.length > 0 && (field[0].type == 'radio' || field[0].type == 'checkbox'))
    {
        for(var i = 0; i < field.length; i++)
        {
            if(field[i].checked && field[i].disabled == false) return field[i].value;
        }
        return "";
    }
    else
    {
        if(field.disabled == false)
        {
            if(field.type == "select-one") 
            {
                var si = field.selectedIndex;
                if (si >= 0) return field.options[si].value;
            }
            else if (field.type == 'checkbox') 
            {
                if (field.checked) return field.value;
            }
            else return field.value;
        }
        else return "";
    }
}

//trims whitespace in start and end of string
function trim(stringToTrim)
{
    return stringToTrim.replace(/^\s*|\s*$/g,"");
}

function isAllDigits(argvalue) {
    argvalue = argvalue.toString();
    var validChars = "0123456789";
    var startFrom = 0;
    
    if(argvalue.substring(0,1) == "-") //negative numbers
        startFrom = 1;

    for (var n = startFrom; n < argvalue.length; n++) {
        if (validChars.indexOf(argvalue.substring(n, n+1)) == -1) return false;
    }
    return true;
}

function matchPattern(value, mask) {
    return mask.exec(value);
}

//trims validate user name as email address
function stripInitialCharsInBag(s, bag)
{   
    var i = 0;
    var returnString = "";

    // Search through string's characters one by one.
    // If character is in bag, append to returnString.

    while (i < s.length)
    {   
        // Check that current character isn't bad.
        
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) break;
        i++;
    }

    returnString = s.substr(i);
    return returnString;
}

function ValidateUserName(fieldName,fieldId)
{
	var LoginUser = document.getElementById(fieldId).value;
	//alert("By ElementId, UserName: " + LoginUser);
	//alert("User Name: " + fld.value);
	
	var strNum;
var containsAt = false;
var containsDot = false;

//alert("UserName: " + userName.value);

//if (LoginUser.length < 1) return true;
strNum = stripInitialCharsInBag(LoginUser, " ")
strNum = stripInitialCharsInBag(strNum, "@");
strNum = stripInitialCharsInBag(strNum, ".");

//alert("Stripped User Name: " + strNum);

	for (i = 0; i < strNum.length; i++)
    {
        if (strNum.charAt(i) == "@")
        {
            containsAt = true;
            //alert("Contains @ sign");
            break;
        }
    }
    
    for (j = 0; j < strNum.length; j++)
    {
        if (strNum.charAt(j) == ".")
        {
            containsDot = true;
            //alert("Contains DOT sign");
            break;
        }
    }
    
    if ( containsAt == false || containsDot == false)
    {
    	document.getElementById(fieldId).value = "";
    	alert("Invalid "+fieldName);
    	document.getElementById(fieldId).focus();
    	document.getElementById(fieldId).select();
    	return false;
    }
    
  /*  if (containsAt == false) 
    {
    	alert ("No @ sign.");
    	return false;
    }
    
    if ( containsDot == false)
    { 
    	alert ("No DOT");
    	return false;
    } */
	return true;
}

function cccValidateTenMonthsLater(form, validationFormName)
{
    var isValid = true;
    
    var tenMonthsLaterValidator = eval('new ' + validationFormName + '_tenMonthsLater()');
    
    for (fieldIndex in tenMonthsLaterValidator) {
    
        var fieldName = tenMonthsLaterValidator[fieldIndex][0];
        
        if(!isFieldValid(validationFormName, fieldName)) continue;
        
        var field = form[fieldName];
        
        if(field == undefined) continue;
        
        var isCurrentValid = true;
        
        var value = getFieldValue(field);
        
        isCurrentValid = isDateTenMonthsLater( value );
        
        if(!isCurrentValid)
            eval( validationFormName + "_ValidationResult.addValidationResult('" + field.name + "', false, '" + tenMonthsLaterValidator[fieldIndex][1] + "')");
        
        isValid = isValid && isCurrentValid;
    }
    
    return isValid;
}