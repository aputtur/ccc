<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.data.account.ZipLocation" %>
<%@ page import="java.util.*" %>
<%@ page import="com.copyright.ccc.web.forms.*" %>
<%@ page import="org.apache.struts.action.*" %>
<%@ page import="com.copyright.data.account.Location" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>


<bean:define id="RegisterOrganizationForm" name="RegisterOrganizationForm" type="com.copyright.ccc.web.forms.RegisterOrganizationForm"/>
<bean:define id="help_url"><%= CC2Configuration.getInstance().getHelpURL() %></bean:define>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">
<script src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/cityStatePopulator.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<bean:define id="disable">
    <logic:equal name="RegisterOrganizationForm" property="country" value="US">true</logic:equal>
    <logic:notEqual name="RegisterOrganizationForm" property="country" value="US">false</logic:notEqual>
</bean:define>

<bean:define id="disableBill">
    <logic:equal name="RegisterOrganizationForm" property="billingCountry" value="US">true</logic:equal>
    <logic:notEqual name="RegisterOrganizationForm" property="billingCountry" value="US">
    	<logic:equal name="RegisterOrganizationForm" property="sameBilling" value="T">
    	true
    	</logic:equal>
    	<logic:equal name="RegisterOrganizationForm" property="sameBilling" value="F">
    	false
    	</logic:equal>
    </logic:notEqual>
</bean:define>


<%

String strcntry = "";
String strState = "";
String strBillingCountry = "";
String strBillingState = "";
String strSameBilling = "";

strSameBilling = RegisterOrganizationForm.getSameBillingFlag();

if (!(RegisterOrganizationForm.getCountry() == null) &&
     (RegisterOrganizationForm.getCountry().length() > 0))
{
    strcntry = RegisterOrganizationForm.getCountry();
}

if (!(RegisterOrganizationForm.getState() == null) &&
     (RegisterOrganizationForm.getState().length() > 0))
{
    strState = RegisterOrganizationForm.getState();
}

if (!(RegisterOrganizationForm.getBillingCountry() == null) &&
     (RegisterOrganizationForm.getBillingCountry().length() > 0))
{
    strBillingCountry = RegisterOrganizationForm.getBillingCountry();
}

if (!(RegisterOrganizationForm.getBillingState() == null) &&
     (RegisterOrganizationForm.getBillingState().length() > 0))
{
    strBillingState = RegisterOrganizationForm.getBillingState();
}

String status = "";
status = RegisterOrganizationForm.getStatus();

String autoCity = "";
String autoBillingCity = "";

%>

<script type="text/javascript">
//var submitClickedFirstTime = "T";
var submitBtnClick = "F";

function copyMailToBill(fld)
{
    //Copies the value from Mailing field to the corresponding
    //Billing field if the Same Bill flag is checked
    
    //alert("Field Name before: " + fld.name);
    var copyFld = document.getElementById("billing" + fld.name);
    var flagTrue2 = document.getElementById("sameBill").checked;
    //alert("Field Name: " + copyFld.name);
    //alert("Same Bill Flag: " + flagTrue2);
    
    if (flagTrue2 == true)
    {
        copyFld.disabled = false;
        copyFld.value = fld.value;
        copyFld.disabled = true;
    }

}

function disableEnableBilling()
{
    var flagTrue = document.getElementById("sameBill").checked;
    //alert(flagTrue);
    var billDivChildren = document.getElementsByTagName("input");
    for(var i = 0; i < billDivChildren.length; i++)
    {
        if(billDivChildren[i].getAttribute("type") == "text" && billDivChildren[i].className == "readonlycontent")
        {
            //alert(billDivChildren[i].className);
            billDivChildren[i].disabled = flagTrue;
        }
    }
    
    var billDivChildrenSelect = document.getElementsByTagName("select");
    for(var i = 0; i < billDivChildrenSelect.length; i++)
    {
        if(billDivChildrenSelect[i].className == "readonlycontent")
        {
            //alert(billDivChildrenSelect[i].className + " select");
            //alert(billDivChildrenSelect[i].name + " select2");
            
            billDivChildrenSelect[i].disabled = flagTrue;
        }
    }
    
    //Disbale the billingState field if the bill State has value
    var billState = document.getElementById("billingState");
    var billCountry = document.getElementById("billingCountry");
    
   // if (billState.value.length > 0 && billCountry.value == "US")
    /* if (flagTrue && billCountry.value == "US")
    {
        //alert("Bill State not null" + billState.value);
        document.getElementById("billingState").value = document.getElementById("state").value;
        billState.disabled = true;
    } */
           
    if (flagTrue)
    {
        document.getElementById("billingState").value = document.getElementById("state").value;
    }
    
    if (billState.value.length > 0 && billCountry.value == "US")
    {
        billState.disabled = true;
    }
    
   /* if (flagTrue == false)
    {
        document.getElementById("billingState").value = "";
        billState.disabled = flagTrue;
    } */
        
}

function sbmtPage() {
  var myFrm = document.getElementById('frm');
  
  var myState = document.getElementById("state");
  var myBillingState = document.getElementById("billingState");
  
  var myZipCode = document.getElementById("zipcode");
  var myBillingZipCode = document.getElementById("billingZipCode");
  
  var myCity = document.getElementById("city");
  var myBillingCity = document.getElementById("billingCity");
  
  if (myState != null) myState.disabled = false;
  if (myBillingState != null) myBillingState.disabled = false;
  
  if (myCity != null) myCity.disabled = false;
  if (myBillingCity != null) myBillingCity.disabled = false;
  
  if (myZipCode != null) myZipCode.disabled = false;
  if (myBillingZipCode != null) myBillingZipCode.disabled = false;
  
  
  
  //Enable the disabled Billing fields before the submit
  var flagTrue = document.getElementById("sameBill").checked;
    //alert(flagTrue);
    
    if (flagTrue)
    {
        var billDivChildren = document.getElementsByTagName("input");
        for(var i = 0; i < billDivChildren.length; i++)
        {
            if(billDivChildren[i].getAttribute("type") == "text" && billDivChildren[i].className == "readonlycontent")
            {
            //alert(billDivChildren[i].className);
                billDivChildren[i].disabled = false;
            }
        }
        
        var billDivChildrenSelect = document.getElementsByTagName("select");
        for(var i = 0; i < billDivChildrenSelect.length; i++)
        {
            if(billDivChildrenSelect[i].className == "readonlycontent")
            {                  
                billDivChildrenSelect[i].disabled = false;
            }
        }
    }
  
  if (submitBtnClick == "F")
     {
        submitBtnClick = "T";
        document.getElementById("state").disabled = false;
        document.getElementById("submitBtnLink").href = "#";
        myFrm.submit();
     }  
  //myFrm.submit();
}

function submitRegisterUser() {
  if (document.RegisterOrganizationForm.submitFlag.value == "T")
  {
	document.RegisterOrganizationForm.submitFlag.value = "F";
	//submitClickedFirstTime = "F";
  	document.forms[0].action.value = "registerOrganization";
  	return true;
  	//document.forms[0].submit();
  }
  else
  {
	document.RegisterOrganizationForm.submitFlag.value = "F";
      	return false;
  }
}


function isZipValid(fldZip,fldZipPlus4)
{
    var validChars = "0123456789-";
    var isNumber = true;
    var c;
    var strZip;
 
     strZip = fldZip.value;
     if (fldZipPlus4.value.length>0 && fldZipPlus4.value.length<4) 
     {
    	 fldZipPlus4.value = "";
         alert("Zip Code +4 cannot be less than 4 digits");
         return false;
     }

    if (fldZip.value.length > 0 && fldZip.value.length < 5)
    {
      fldZip.focus();
      fldZip.value = "";
      alert("Zip Code can not be less than 5 digits");
      return false;
    }
 
    for (i = 0; i < strZip.length && isNumber; i++) 
    { 
      c = fldZip.value.charAt(i);
      //alert("Valid Char: " + c); 
      if (validChars.indexOf(c) == -1) 
      {
        isNumber = false;
        alert("Zip Code is inValid");
        fldZip.value = "";
        fldZip.focus();
        fldZip.select();
	
      }
    }
    return isNumber;
}

function getCityStateAjax(fld,fldCountry,fld2)
{
    if (fldCountry.value == "US")
    {
        if (isZipValid(fld,fld2) == false)
        {
            fld.select();
            return false;
        }
      
        var cityStatePop = new CityStatePopulator("ORG");
        
        //alert("Field: " + fld.name);
    
        if (fld.name == "zipcode")
        {
                        
            cityStatePop.populateCityAndState(fld.value, "city", "state", "phone");
            //document.getElementById("phone").select();
                                   
            var mailCity = document.getElementById("city");
            
            if (mailCity.value.length > 0)
            {
                //document.getElementById("phone").select();
            }
            else
            {
                //mailCity.select();
            }
                                        
        }
        else
        {
            cityStatePop.populateCityAndState(fld.value, "billingCity", "billingState", "billingPhone");
            //document.getElementById("billingPhone").select();
            
            var billCity = document.getElementById("billingCity");
            
            if (billCity.value.length > 0)
            {
                //document.getElementById("billingPhone").select();
            }
            else
            {
                //billCity.select();
            }
        }                  
    
    }
    
}

function getCityState(fld,fldCountry)
{
    if ( fldCountry.value == "US")
    {
      if (isZipValid(fld) == false)
      {
       return false;
      }  
      if (fld.name == "zipcode")
      {
	       document.RegisterOrganizationForm.zipCodeChanged.value = "MAIL";
      }
      else
      {
          document.RegisterOrganizationForm.zipCodeChanged.value = "BILL";
      }     
      repaintPage();
    }
}
function checkCountry(fld)
{
	/* if (fld.value == "CA" && fld.name == "country")
	{
		alert("The reproduction right organization (RRO) in your country asks that you contact it for assistance in obtaining permission to use copyrighted content. To locate your RRO, please see our RROs page: www.copyright.com/rro.");
		fld.value = "US";
    } */
	//alert("Test CA");
        //alert("same bill: " + document.getElementById("sameBill").checked);
	 if (fld.name == "country" && document.getElementById("sameBill").checked == true)
	{
        //alert("In copy country...");
		document.RegisterOrganizationForm.billingCountry.value = document.RegisterOrganizationForm.country.value;
        document.getElementById("billingCountry").value = document.getElementById("country").value;
        document.getElementById("billingCountry").disabled = false;
	}
    
    if (fld.value == "US" && fld.name == "country")
      {
        //alert("In USA");
        strState = "";
        document.getElementById("state").value = "";
        document.getElementById("zipcode").value = "";
        document.getElementById("city").value = "";
      }
      
    if (fld.value == "US" && fld.name == "billingCountry")
      {
        strBillingState = "";
        document.getElementById("billingState").value = "";
        document.getElementById("billingZipCode").value = "";
        document.getElementById("billingCity").value = "";
      }
	repaintPage();
    
    //if (fld.name == "country")
    //{
        //document.getElementById("zipcode").select();
    
    
    //}

}
function repaintPage()
{	
    var myState = document.getElementById("state");
    var myCity = document.getElementById("city");
    var myBillingState = document.getElementById("billingState");
    var myBillingCity = document.getElementById("billingCity");
  
    if (myState != null) myState.disabled = false;
    if (myCity != null) myCity.disabled = false;
    if (myBillingState != null) myBillingState.disabled = false;
    if (myBillingCity != null) myBillingCity.disabled = false;
  
    document.RegisterOrganizationForm.action = "redisplayOrgRegistration.do";
    document.RegisterOrganizationForm.submit();
    
    //myTest();
}
//This function loads the page depending on the Type of Acct chosen

function displayPage(choice)
{
    //alert("In displayPage " + choice);
    
    
    if (choice == "O")
    {
        userChoice = "O";
    
    }
    else if (choice == "I")
    {
    	
       userChoice = "I";
       document.RegisterOrganizationForm.action = "displayIndRegistration.do";
       document.RegisterOrganizationForm.submit();
    }
}
</script>

<script type="text/javascript">
//  Declare our local variables.  These will be used in determining
//  our required fields based on the current version of the Registration
//  form that has been requested.

var ReqFlds;
var FldNames;
var intFldCount = 0;
var strKind;
var strCountry;
var strCountry_Billing;
var digits = "0123456789";
var phoneNumberDelimiters = "()- ";
var validUSPhoneChars = digits + phoneNumberDelimiters + "+";
var validWorldPhoneChars = digits + phoneNumberDelimiters + "+";
var digitsInUSPhoneNumber = 10;
var ZIPCodeDelimiters = "-";
var ZIPCodeDelimeter = "-";
var validZIPCodeChars = digits + ZIPCodeDelimiters;
var digitsInZIPCode1 = 5;
var digitsInZIPCode2 = 9;
var defaultEmptyOK = false;
var whitespace = " \t\n\r";

// Check whether string s is empty.

function isEmpty(s)
{   
    return ((s == null) || (s.length == 0))
}

function ltrimField(fld) 
{
    fld.value = stripInitialCharsInBag(fld.value, " ")
}

// Returns true if character c is a digit 
// (0 .. 9).

function isDigit(c)
{   
    return ((c >= "0") && (c <= "9"));
}

// Returns true if string s is empty or 
// whitespace characters only.

function isWhitespace(s)
{   
    var i;

    // Is s empty?
    if (isEmpty(s)) return true;

    // Search through string's characters one by one
    // until we find a non-whitespace character.
    // When we do, return false; if we don't, return true.

    for (i = 0; i < s.length; i++)
    {   
        // Check that current character isn't whitespace.
        var c = s.charAt(i);

        if (whitespace.indexOf(c) == -1) return false;
    }

    // All characters are whitespace.
    return true;
}

// isInteger (STRING s [, BOOLEAN emptyOK])
// 
// Returns true if all characters in string s are numbers.
//
// Accepts non-signed integers only. Does not accept floating 
// point, exponential notation, etc.
//
// We don't use parseInt because that would accept a string
// with trailing non-numeric characters.
//
// By default, returns defaultEmptyOK if s is empty.
// There is an optional second argument called emptyOK.
// emptyOK is used to override for a single function call
//      the default behavior which is specified globally by
//      defaultEmptyOK.
// If emptyOK is false (or any value other than true), 
//      the function will return false if s is empty.
// If emptyOK is true, the function will return true if s is empty.
//
// EXAMPLE FUNCTION CALL:     RESULT:
// isInteger ("5")            true 
// isInteger ("")             defaultEmptyOK
// isInteger ("-5")           false
// isInteger ("", true)       true
// isInteger ("", false)      false
// isInteger ("5", false)     true

function isInteger (s)
{   
    var i;

    if (isEmpty(s)) 
       if (isInteger.arguments.length == 1) return defaultEmptyOK;
       else return (isInteger.arguments[1] == true);

    // Search through string's characters one by one
    // until we find a non-numeric character.
    // When we do, return false; if we don't, return true.

    for (i = 0; i < s.length; i++)
    {   
        // Check that current character is number.
        var c = s.charAt(i);

        if (!isDigit(c)) return false;
    }

    // All characters are numbers.
    return true;
}

// isSignedInteger (STRING s [, BOOLEAN emptyOK])
// 
// Returns true if all characters are numbers; 
// first character is allowed to be + or - as well.
//
// Does not accept floating point, exponential notation, etc.
//
// We don't use parseInt because that would accept a string
// with trailing non-numeric characters.
//
// For explanation of optional argument emptyOK,
// see comments of function isInteger.
//
// EXAMPLE FUNCTION CALL:          RESULT:
// isSignedInteger ("5")           true 
// isSignedInteger ("")            defaultEmptyOK
// isSignedInteger ("-5")          true
// isSignedInteger ("+5")          true
// isSignedInteger ("", false)     false
// isSignedInteger ("", true)      true

function isSignedInteger (s)
{   
    if (isEmpty(s)) 
       if (isSignedInteger.arguments.length == 1) return defaultEmptyOK;
       else return (isSignedInteger.arguments[1] == true);
    else {
        var startPos = 0;
        var secondArg = defaultEmptyOK;

        if (isSignedInteger.arguments.length > 1)
            secondArg = isSignedInteger.arguments[1];

        // skip leading + or -
        if ( (s.charAt(0) == "-") || (s.charAt(0) == "+") )
           startPos = 1;    
        return (isInteger(s.substring(startPos, s.length), secondArg));
    }
}


// isPositiveInteger (STRING s [, BOOLEAN emptyOK])
// 
// Returns true if string s is an integer > 0.
//
// For explanation of optional argument emptyOK,
// see comments of function isInteger.

function isPositiveInteger(s)
{   
    var secondArg = defaultEmptyOK;

    if (isPositiveInteger.arguments.length > 1)
        secondArg = isPositiveInteger.arguments[1];

    // The next line is a bit byzantine.  What it means is:
    // a) s must be a signed integer, AND
    // b) one of the following must be true:
    //    i)  s is empty and we are supposed to return true for
    //        empty strings
    //    ii) this is a positive, not negative, number

    return (isSignedInteger(s, secondArg)
         && ( (isEmpty(s) && secondArg)  || (parseInt(s) > 0) ) );
}

// Removes all characters which appear in string bag from string s.

function stripCharsInBag(s, bag)
{   
    var i;
    var returnString = "";

    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.

    for (i = 0; i < s.length; i++)
    {   
        // Check that current character isn't whitespace.
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }

    return returnString;
}

// Removes all characters which do NOT appear in string bag 
// from string s.

function stripCharsNotInBag(s, bag)
{   
    var i;
    var returnString = "";

    // Search through string's characters one by one.
    // If character is in bag, append to returnString.

    for (i = 0; i < s.length; i++)
    {   
        // Check that current character isn't whitespace.
        var c = s.charAt(i);
        if (bag.indexOf(c) != -1) returnString += c;
    }

    return returnString;
}

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

function reformat(s)
{   
    var arg;
    var sPos = 0;
    var resultString = "";
    
   // alert("s: " + s);

    for (var i = 1; i < reformat.arguments.length; i++) {
       arg = reformat.arguments[i];
       if (i % 2 == 1) { 
       resultString += arg; 
       //alert("result string1: " + resultString);
       }
       //alert("result string1: " + resultString);
       else {
           resultString += s.substring(sPos, sPos + arg);
        //   alert("result string2: " + resultString);
           sPos += arg;
       }
    }
    //alert("result string: " + resultString);
    return resultString;
}

// isUSPhoneNumber (STRING s [, BOOLEAN emptyOK])
// 
// isUSPhoneNumber returns true if string s is a valid U.S. Phone
// Number.  Must be 10 digits.
//
// NOTE: Strip out any delimiters (spaces, hyphens, parentheses, etc.)
// from string s before calling this function.
//
// For explanation of optional argument emptyOK,
// see comments of function isInteger.

function isUSPhoneNumber (s)
{   
    if (isEmpty(s)) 
       if (isUSPhoneNumber.arguments.length == 1) return defaultEmptyOK;
       else return (isUSPhoneNumber.arguments[1] == true);
    return (isInteger(s) && s.length == digitsInUSPhoneNumber);
}

// isInternationalPhoneNumber (STRING s [, BOOLEAN emptyOK])
// 
// isInternationalPhoneNumber returns true if string s is a valid 
// international phone number.  Must be digits only; any length OK.
// May be prefixed by + character.
//
// NOTE: A phone number of all zeros would not be accepted.
// I don't think that is a valid phone number anyway.
//
// NOTE: Strip out any delimiters (spaces, hyphens, parentheses, etc.)
// from string s before calling this function.  You may leave in 
// leading + character if you wish.
//
// For explanation of optional argument emptyOK,
// see comments of function isInteger.

function isInternationalPhoneNumber (s)
{   
    if (isEmpty(s)) 
       if (isInternationalPhoneNumber.arguments.length == 1) return defaultEmptyOK;
       else return (isInternationalPhoneNumber.arguments[1] == true);
    //return (isPositiveInteger(s));
    return (true);
}

//  This function strips phone numbers of the extraneous format
//  characters (like dashes and parentheses).  It also removes
//  long-distance prefices.

function FixPhone(pn)
{
    var strCheck = "0123456789";
    var strReturn = "";
    var ch;
    
    //  Extract all numeric values and create a new string.
    
    for (i = 0; i < pn.length; i++)
    {
        ch = pn.charAt(i);
      
        if ((ch >= '0' ) && (ch <= '9'))
        {
            strReturn += pn.charAt(i);
        }
    }
    
    //  Now cut out any long-distance prefices.
    
    for (i = 0; i < strReturn.length; i++)
    {
        if (strReturn.charAt(i) > "1")
        {
            strReturn = strReturn.slice(i);
            return strReturn;
        }
    }
    return strReturn;
}


//This function copies the value of first field into the second field

function CopyValue(fld1, fld2)

{
    ltrimField(fld1) ;
    fld2.value = fld1.value ;
    return true ;

}

function CopyAlwaysInvoice(fld)
{
    if (fld.checked)
    {
        document.RegisterOrganizationForm.alwaysInvoice.value = "T";
        document.RegisterOrganizationForm.alwaysInvoiceFlag.value = "T";  
    }
    else
    {
        document.RegisterOrganizationForm.alwaysInvoice.value = "F";
        document.RegisterOrganizationForm.alwaysInvoiceFlag.value = "F"; 
    }



}
function CopyAutoInvoiceSpecialOrder(fld)
{
    if (fld.checked)
    {
        document.RegisterOrganizationForm.autoInvoiceSpecialOrder.value = "T";
        document.RegisterOrganizationForm.autoInvoiceSpecialOrderFlag.value = "T";  
    }
    else
    {
        document.RegisterOrganizationForm.autoInvoiceSpecialOrder.value = "F";
        document.RegisterOrganizationForm.autoInvoiceSpecialOrderFlag.value = "F"; 
    }



}



function CopyBilling(fld)
{
  if (fld.checked)
  {
    //alert("It was checked...." + fld.value);
        
    document.RegisterOrganizationForm.sameBilling.value = "T";
    document.RegisterOrganizationForm.sameBillingFlag.value = "T";
    strSameBilling = "T";
    
    document.getElementById("state").disabled = false;
    document.getElementById("billingState").disabled = false;
    
    //alert("Before Billing State value: " + document.RegisterOrganizationForm.billingState.value);
    //alert("Before Mail State value: " + document.RegisterOrganizationForm.state.value);
          
    //strBillingState = document.RegisterOrganizationForm.state.value; 
    
    document.RegisterOrganizationForm.billingTitle.value = document.RegisterOrganizationForm.title.value;
    document.RegisterOrganizationForm.billingFirstName.value = document.RegisterOrganizationForm.firstName.value;
    document.RegisterOrganizationForm.billingMiddleName.value = document.RegisterOrganizationForm.middleName.value;
    document.RegisterOrganizationForm.billingLastName.value = document.RegisterOrganizationForm.lastName.value;
    document.RegisterOrganizationForm.billingJobTitle.value = "";
    document.RegisterOrganizationForm.billingDepartment.value = "";
    document.RegisterOrganizationForm.billingAddress1.value = document.RegisterOrganizationForm.address1.value;
    document.RegisterOrganizationForm.billingAddress2.value = document.RegisterOrganizationForm.address2.value;
    document.RegisterOrganizationForm.billingAddress3.value = document.RegisterOrganizationForm.address3.value;
    document.RegisterOrganizationForm.billingCountry.value = document.RegisterOrganizationForm.country.value;
    document.RegisterOrganizationForm.billingZipcode.value = document.RegisterOrganizationForm.zipcode.value;

    document.RegisterOrganizationForm.billingCity.value = document.RegisterOrganizationForm.city.value;
    document.RegisterOrganizationForm.billingState.value = document.RegisterOrganizationForm.state.value;
    document.RegisterOrganizationForm.billingPhone.value = document.RegisterOrganizationForm.phone.value;
    document.RegisterOrganizationForm.billingPhoneExtension.value = document.RegisterOrganizationForm.phoneExtension.value;
    document.RegisterOrganizationForm.billingFax.value = document.RegisterOrganizationForm.fax.value;

    if (document.RegisterOrganizationForm.country.value == "US") {
       	  document.getElementById("billingZipPlus4").style.display='';
          document.getElementById("billingPlus4Label").style.display='';
    }
    else {
    	  document.getElementById("billingZipPlus4").style.display='none';
    	  document.getElementById("billingPlus4Label").style.display='none';
     }
    if (document.getElementById("zipPlus4") != undefined) { 
    	document.getElementById("billingZipPlus4").value = document.getElementById("zipPlus4").value;
    }
<%
    if (status == "CREATE")
   {
%>
      document.RegisterOrganizationForm.billingEmail.value = document.RegisterOrganizationForm.userName.value;
<%
   }
%>

    repaintPage();

    document.RegisterOrganizationForm.billingState.value = document.RegisterOrganizationForm.state.value;
    document.getElementById("billingState").value = document.getElementById("state").value;
           
    //disableEnableBilling();
    
    }
  else
    {
      //alert("It was NOT checked...." + fld.value);
      
    document.RegisterOrganizationForm.sameBilling.value = "F";
    document.RegisterOrganizationForm.sameBillingFlag.value = "F";
    strSameBilling = "F";
    
    document.getElementById("state").disabled = false;
    document.getElementById("billingState").disabled = false;
    
    document.getElementById("billingState").value = "";
  
    document.RegisterOrganizationForm.billingTitle.value = "";
    document.RegisterOrganizationForm.billingFirstName.value = "";
    document.RegisterOrganizationForm.billingMiddleName.value = "";
    document.RegisterOrganizationForm.billingLastName.value = "";
    document.RegisterOrganizationForm.billingJobTitle.value = "";
    document.RegisterOrganizationForm.billingDepartment.value = "";
    document.RegisterOrganizationForm.billingAddress1.value = "";
    document.RegisterOrganizationForm.billingAddress2.value = "";
    document.RegisterOrganizationForm.billingAddress3.value = "";
    document.RegisterOrganizationForm.billingCountry.value = "US";
    document.RegisterOrganizationForm.billingZipcode.value = "";
    if (document.RegisterOrganizationForm.billingZipPlus4 != undefined) 
    {
    	document.RegisterOrganizationForm.billingZipPlus4.value = "";
    }
    document.RegisterOrganizationForm.billingCity.value = "";
    document.RegisterOrganizationForm.billingState.value = "";
    document.RegisterOrganizationForm.billingPhone.value = "";
    document.RegisterOrganizationForm.billingPhoneExtension.value = "";
    document.RegisterOrganizationForm.billingFax.value = "";
        
    <%
        if (status == "CREATE")
        {
            //strBillingState = "";
    %>
            document.RegisterOrganizationForm.billingEmail.value = "";
    <%
        }
      /*  else if (status == "UPDATE")
        {
            strBillingState = "";
        } */
    %>
        
    disableEnableBilling();
    repaintPage();
    
    }
    
  
}

//  This function checks a domestic or foreign phone number for
//  a minimal validity.

function ValidPhoneNumber(fld, fmt)
{
    var strNum;
    var strPhoneOrPhax = "Phone";
    var displayError = true;
    var valchanged = false;
    
    if (ValidPhoneNumber.arguments.length > 2)
        displayError = ValidPhoneNumber.arguments[2].value;
    
    if (fld.value.length < 1) return true;
    if (fld.name.search("fax") > -1) strPhoneOrPhax = "Fax"
    
    //  Strip out any non-numeric characters.  Also remove
    //  leading 0 or 1.
    
    //strNum = stripCharsNotInBag(fld.value, digits);
    //  strNum = stripCharsNotInBag(fld.value, validUSPhoneChars);
    //strNum = stripInitialCharsInBag(strNum, "01");

    switch(fmt.options[fmt.selectedIndex].value)
    {
        case "CA":
        case "PUERTO RICO":
        case "US":
            //  All U.S.A. and Canadian numbers follow the 3-3-4 format for
            //  telephone numbers.
           strNum = stripCharsNotInBag(fld.value, digits);
           strNum = stripInitialCharsInBag(strNum, "01"); 
            if (isUSPhoneNumber(strNum))
                fld.value = reformat(strNum, "+1 (", 3, ") ", 3, "-", 4);
               // alert("Fld Value: " + fld.value);
            else
            {
                if (displayError)
                {
                    //fld.focus();
                    //alert("in 10 digit TEST fld Name: " + fld.name);
                    alert("Your " + strPhoneOrPhax + " Number should contain 10 digits\nconsisting of your area code, exchange and number.")
                    fld.value = "";
                 //   setFocus();
                    fld.focus();
                    fld.select();
                }
                return false;
            }
            break;
        
        default:
            //  Foreign numbers can range from 8 to 15 in length, depending on the
            //  country and city.  I can only test for the apparent, bare minimum.
            strNum = stripCharsNotInBag(fld.value, validWorldPhoneChars);
            strNum = stripInitialCharsInBag(strNum, "0");
            if (isInternationalPhoneNumber(strNum))
                //strNum = stripInitialCharsInBag(strNum, "0");
                //fld.value = "011-" + strNum;
                fld.value = strNum;
                //alert("For Fld Value: " + strNum);
            else
            {
                if (displayError)
                {
                    alert("Your international " + strPhoneOrPhax + " Number should consist of your country code, \ncity code and telephone number.");
                    fld.value = "";
                    fld.focus();
                    fld.select();
                }
                return false;
            }
            break;
    }
        
    return true;
}
</script>

<html:form action="/registerOrganization" method="POST" onsubmit="submitRegisterUser();" styleId="frm">


<html:hidden name="RegisterOrganizationForm" property="sameBillingFlag"/>
<html:hidden name="RegisterOrganizationForm" property="zipCodeChanged"/>
<html:hidden name="RegisterOrganizationForm" property="submitFlag" value="T"/>
<html:hidden name="RegisterOrganizationForm" property="alwaysInvoiceFlag"/>
<html:hidden name="RegisterOrganizationForm" property="autoInvoiceSpecialOrderFlag"/>
<html:hidden name="RegisterOrganizationForm" property="billingJobTitle" value=""/>
<html:hidden name="RegisterOrganizationForm" property="billingDepartment" value=""/>

<!-- <div id="content"> -->

    <%-- urlBase: <bean:write name='cccForm' property='urlBase'/> --%>
  <!-- <div id="none"> -->
    
    <!-- <div id="topLine">&#160;</div> -->
    <!-- <div id="onecolumn"> -->
    
    <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="FALSE"> 
      <h2>Create a Pay-per-use Account</h2>
      <p style="text-align: right; color: #990000; margin-top: -20px">* indicates required field</p>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
      <b>If you already use our Rightslink service,</b> you don't need to create an account. 
        <util:contextualHelp helpId="35" rollover="false" styleId="1">
           Learn More&hellip;
        </util:contextualHelp>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
    </logic:equal>
    
    <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="TRUE"> 
      <h2>UPDATE ACCOUNT INFORMATION</h2>
      <p style="text-align: right; color: #990000; margin-top: -20px">* indicates required field</p>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
    </logic:equal>
    <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="FALSE">  
        <table width="664" cellpadding="0" cellspacing="0" border="0" style="margin-left: 12px">
          <tr>
            <td colspan="2"><h4 style="font-size: 8pt; color: #336699; margin-top: 5px; margin-bottom: 6px;">Type of account<span style="color: #990000">*</span></h4></td>
          </tr>
          <tr>
            <td class="Label" width="21"><input type="radio" name="typeOfAccount" checked="checked" value="O">
            </td>
            <td class="Label" width="643"><p style="text-align: left; margin-top: 0px;">Organizational (If you are obtaining permission on behalf of an academic institution, company or organization.) </p></td>
          </tr>
          <tr>
            <td class="Label" width="21"><input type="radio" name="typeOfAccount" value="I" onclick="displayPage(this.value)">
            </td>
            <td class="Label" width="643"><p style="text-align: left; margin-top: 0px;">Individual (If you are obtaining permission for your own individual use.) </p></td>
          </tr>
          
          <!--
          <tr>
            <td colspan="2">&nbsp;</td>
          </tr>
          
          <tr>
            <td colspan="2"><p class="boxLine">&nbsp;</p></td>
          </tr>
          -->
          
        </table>
        
        <div class="horiz-rule"></div>
        
    </logic:equal>
    
       
    
    <!-- ******************************************************************************** -->
    
    <table width="664" border="0" style="margin-left: 12px">
       <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="FALSE">
          <tr>
            <td colspan="4"><h4 style="font-size: 8pt; color: #336699; margin-top: 5px; margin-bottom: 0px;">Contact information</h4>
              <p style="margin-top: 4px; margin-bottom: 12px;"><bean:message key="label.registration.addressExplanation" /> (<a href="/content/cc3/en/tools/footer/privacypolicy.html" target="_blank" style="color: #336699;"><bean:message key="label.registration.privacyPolicy" /></a>.)</p></td>
          </tr>
        </logic:equal>
        <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="TRUE">
          <tr>
            <td colspan="4"><h4 style="font-size: 8pt; color:  #000000; margin-top: 5px; margin-bottom: 0px;">Current contact information</h4>
              <p style="margin-top: 4px; margin-bottom: 12px;"><bean:message key="label.registration.addressExplanation" /> (<a href="/content/cc3/en/tools/footer/privacypolicy.html" target="_blank" style="color: #336699;"><bean:message key="label.registration.privacyPolicy" /></a>.)</p></td>
          </tr>
        </logic:equal>
        <tr>
         <td style="vertical-align:top" colspan="2">
			          <table id="leftColumn">
			          <tr><td class="Label" width="183"><bean:message key="label.registration.title" /></td>
            		<td width="149">
            				<html:select name="RegisterOrganizationForm" style="title" property="title" tabindex="1" onchange="copyMailToBill(this)">
					              <html:option value=""></html:option>
					              <html:option value="Mr."><bean:message key="label.registration.mr"/></html:option>
					              <html:option value="Mrs."><bean:message key="label.registration.mrs"/></html:option>
					              <html:option value="Ms."><bean:message key="label.registration.ms"/></html:option>
					              <html:option value="Miss"><bean:message key="label.registration.miss"/></html:option>
					              <html:option value="Dr."><bean:message key="label.registration.dr"/></html:option>
					              <html:option value="Prof."><bean:message key="label.registration.prof"/></html:option>
					              <html:option value="Sir"><bean:message key="label.registration.sir"/></html:option>
					            </html:select>
            		</td></tr>
            		<tr>
            		 <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.firstName"/></td>
            		<td><html:text styleId="firstName" styleClass="firstName" name="RegisterOrganizationForm" property="firstName" tabindex="1" size="20" maxlength="15" onchange="copyMailToBill(this)"/></td>
            		</tr>
            		<tr>
            		<td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.middleName"/></td>
            		<td><html:text styleClass="middleName" name="RegisterOrganizationForm" property="middleName" tabindex="1" size="20" maxlength="20" onchange="copyMailToBill(this)"/></td>
            		</tr>
            		<tr>
            		<td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.lastName"/></td>
            		<td><html:text styleClass="inputs" name="RegisterOrganizationForm" property="lastName" styleId="lastName" tabindex="1" size="20" maxlength="20" onchange="copyMailToBill(this)"/></td>
            		</tr>
            		<tr>
            			<td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.jobTitle"/></td>
            			<td><html:text styleClass="inputs" name="RegisterOrganizationForm" property="jobTitle" styleId="jobTitle" tabindex="1" size="20" maxlength="30" onchange="copyMailToBill(this)"/></td>
            		</tr>
            		<tr>
		            	
            		</tr>
            		
			          <tr>
			          <logic:equal property="status" name="RegisterOrganizationForm" value="CREATE">
				   	 		 <td class="Label">
			                   <span style="color: #990000">* </span><bean:message key="label.registration.company"/></td>
			                <td>
			                <html:text styleClass="inputs" name="RegisterOrganizationForm" property="company" tabindex="1" size="20" maxlength="80" onchange="ltrimField(this)"/>
		                	</td>
			   			 </logic:equal>
			    
				    <logic:equal property="status" name="RegisterOrganizationForm" value="UPDATE">
					    <td class="Label" valign="top">
			    	    <span style="color: #990000">* </span><bean:message key="label.registration.company"/></td>
					    <td rowspan="2">
					    <div z-index="1" style="position: relative;">
			        	    <html:text styleClass="inputs" name="RegisterOrganizationForm" property="company" style="color: #808080" readonly="TRUE" tabindex="1" size="20" maxlength="80" onchange="ltrimField(this)"/>
				    	</div>          
				            <div z-index="2" style="position: relative; font-size: 7pt; margin-top: 0px;">To update Organization,<br> please <a href="<bean:write name="help_url" />" target="_blank">contact us</a> directly. </div>
			    	        </td>
		  			</logic:equal>
			          </tr>
			            <logic:equal property="status" name="RegisterOrganizationForm" value="UPDATE">
			            <tr><td></td><td></td></tr>
			            </logic:equal>
			          
			          <tr>
            				<td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.department"/></td>
            			<td><html:text styleClass="inputs" name="RegisterOrganizationForm" property="department" styleId="department" tabindex="1" size="20" maxlength="60" onchange="copyMailToBill(this)"/></td>
            		</tr>
			                 
			<logic:equal property="isRegisteredRlnkUser" name="RegisterOrganizationForm"  value="true">
		       <logic:equal property="status" name="RegisterOrganizationForm" value="UPDATE">
                        <tr><td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.industryType"/></td>
		            <td>
		              <html:select name="RegisterOrganizationForm" property="industryType" styleId="IndustryList"  tabindex="7" style="width:150px" >
			              <html:option value="">- Select One -</html:option>
		              	<html:optionsCollection property="industryList" name="RegisterOrganizationForm"  label="value" value="key"/>
		              </html:select>
		            </td></tr>
		            <tr>
		            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.orgStatus"/></td>
							            <td>
							              <html:select name="RegisterOrganizationForm" property="orgStatus" styleId="OrgStatus"  tabindex="7" style="width:150px" >
								              <html:option value="">- Select One -</html:option>
							                	<html:optionsCollection property="orgStatusList" name="RegisterOrganizationForm"  label="value" value="key"/>
							              </html:select>
							            </td>
		            </tr>
		         
                 </logic:equal>
           </logic:equal>
           
            <tr>
	             <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.address1"/></td>
	            <td><html:text styleClass="inputs" name="RegisterOrganizationForm" property="address1" styleId="address1" tabindex="1" size="20" maxlength="60" onchange="copyMailToBill(this)"/></td>
            </tr>
            <tr>
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.address2"/></td>
            <td><html:text styleClass="inputs" name="RegisterOrganizationForm" property="address2" styleId="address2" tabindex="1" size="20" maxlength="60" onchange="copyMailToBill(this)"/></td>
            </tr>
            <tr>
               <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.address3"/></td>
            <td><html:text styleClass="inputs" name="RegisterOrganizationForm" property="address3" styleId="address3" tabindex="1" size="20" maxlength="60" onchange="ltrimField(this);copyMailToBill(this)"/></td>
            </tr>
			          
			          </table><!--  end of left column -->
		</td>			          
         <td style="vertical-align:top" colspan="2">
			          <table id="rightColumn">
			          			          <tr>
			           <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.country"/></td>
          				<td colspan="3">
						          <html:select name="RegisterOrganizationForm" property="country" styleId="country" tabindex="1" onchange="checkCountry(this);copyMailToBill(this)">
						          <logic:iterate name="RegisterOrganizationForm" property="countryList" id="countryObject">
						      <%if (strcntry.length() > 0){%>
						                <logic:equal name="countryObject" property="abbrev" value="<%=strcntry%>">
						                <option selected="true" value='<bean:write name="countryObject" property="abbrev"/>'><bean:write name="countryObject" property="name"/></option>
						                </logic:equal>
						                <logic:notEqual name="countryObject" property="abbrev" value="<%=strcntry%>">
						                <option value='<bean:write name="countryObject" property="abbrev"/>'><bean:write name="countryObject" property="name"/></option>
						                </logic:notEqual>
						            <%}else{%>
						                <logic:equal name="countryObject" property="abbrev" value="<%=strcntry%>">
						                <option selected="true" value='<bean:write name="countryObject" property="abbrev"/>'><bean:write name="countryObject" property="name"/></option>
						                </logic:equal>
						                <logic:notEqual name="countryObject" property="abbrev" value="<%=strcntry%>">
						                <option value='<bean:write name="countryObject" property="abbrev"/>'><bean:write name="countryObject" property="name"/></option>
						                </logic:notEqual>
						           <%}%>
						      
						      
						      </logic:iterate>  
						      </html:select>
			          </tr>
			          <tr>
			          
			                     <%if (strcntry.equalsIgnoreCase("US")){%>
						            <td width="120" class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.zipcode"/></td>
						            <%}else if (strcntry.equalsIgnoreCase("CA")){%>
						            <td width="120" class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.postalcode"/></td>
						            <%}else{%>
						            <td width="120" class="Label"><span style="color: #990000"></span><bean:message key="label.registration.postalcode"/></td>
						            <%}%>
						            
						            <%if (strcntry.equalsIgnoreCase("US")){%>

							       <td width="40px">
							          <html:text styleClass="zipcode" styleId="zipcode" name="RegisterOrganizationForm" property="zipcode" tabindex="7" size="5" maxlength="5" onchange="var zipPlus4=document.getElementById('zipPlus4');getCityStateAjax(this,RegisterOrganizationForm.country,zipPlus4);copyMailToBill(this)"/>
							       </td>
							       <td id="billingPlus4Label" width="20px"class="Label"><span style="color: #990000"></span><bean:message key="label.registration.plus4"/></td>
            				       <td width="52"><html:text styleClass="inputs" name="RegisterOrganizationForm" property="zipPlus4" styleId="zipPlus4" tabindex="7" size="4" maxlength="4" onchange="zip=document.getElementById('zipcode');isZipValid(zip,this);copyMailToBill(this)"/></td>
							       
							       <%}else{%>
							       <td><html:text styleClass="zipcode" styleId="zipcode" name="RegisterOrganizationForm" property="zipcode" tabindex="7" size="20" maxlength="20" onchange="getCityStateAjax(this,RegisterOrganizationForm.country);copyMailToBill(this)"/></td>
							       <td id="billingPlus4Label" width="20px"class="Label" style="display:none"><span style="color: #990000;"></span><bean:message key="label.registration.plus4"/></td>
            				       <td width="52"><html:text style="display:none" styleClass="inputs" name="RegisterOrganizationForm" property="zipPlus4" styleId="zipPlus4" tabindex="7" size="4" maxlength="4" onchange="zip=document.getElementById('zipcode');isZipValid(zip,this);copyMailToBill(this)"/></td>
							        <%}%>
			          </tr>
			          <tr>
			          			          
			          	<td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.city"/></td>
	                	<td colspan="3"><html:text disabled="${disable}" styleClass="city" name="RegisterOrganizationForm" styleId="city" property="city" tabindex="7" size="20" maxlength="60" onchange="copyMailToBill(this)"/></td>
			          
			          
			          </tr>
			          <tr>
			          
			           <%if (strcntry.equalsIgnoreCase("US")){            %>
	                <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.state"/></td>
	                <td>
	                <%if (strState.length() > 0){%>
	                    <html:select name="RegisterOrganizationForm" property="state" styleId="state" disabled="true" tabindex="7" onchange="copyMailToBill(this)">
	                        <logic:iterate name="RegisterOrganizationForm" property="stateList" id="stateObject">
	                            <logic:match name="stateObject" property="code" value="<%=strState%>">
	                                <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
	                            </logic:match>
	                            <logic:notMatch name="stateObject" property="code" value="<%=strState%>">
	                                <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
	                            </logic:notMatch>
	                        </logic:iterate>
	                        <%if ((status == "CREATE") && (strState.length() == 0)){%>
	                            <option selected="true" value=""/>
	                        <%}else{%>
	                            <option value=""/>
	                        <%}%>
	                    </html:select>
	                <%}else{%>
	                    <html:select name="RegisterOrganizationForm" property="state" styleId="state" tabindex="7" onchange="copyMailToBill(this)">                                                         
	                        <logic:iterate name="RegisterOrganizationForm" property="stateList" id="stateObject">
	                            <logic:match name="stateObject" property="code" value="<%=strState%>">
	                                <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
	                            </logic:match>
	                            <logic:notMatch name="stateObject" property="code" value="<%=strState%>">
	                                <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
	                            </logic:notMatch>
	                        </logic:iterate>                                                                    
	                    
	                        <%if ((status == "CREATE") && (strState.length() == 0)){%>
	                                <option selected="true" value=""/>
	                        <%}else if ((status == "UPDATE") && (strState.length() == 0)){%>
	                                <option selected="true" value=""/></option>
	                        <%}else{%>
	                                <option value=""/>
	                        <%}%>
	                    </html:select>
	                                     
	                  <%}%>
	                        
	                </td>
	          <!--*****************************************-->      
	                <%}else if (strcntry.equalsIgnoreCase("CA")){%>
	                    <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.province"/></td>
	                    <td>
	    	
	                    <html:select name="RegisterOrganizationForm" property="state" styleId="state" tabindex="7" onchange="copyMailToBill(this)">
	                
	                        <logic:iterate name="RegisterOrganizationForm" property="provinceList" id="stateObject">
	                    <%if (strState.length() > 0){%>
	                            <logic:match name="stateObject" property="code" value="<%=strState%>">
	                                <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="name"/></option>
	                            </logic:match>
	                            
	                            <logic:notMatch name="stateObject" property="code" value="<%=strState%>">
	                                <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="name"/></option>
	                            </logic:notMatch>
	                    <%}else{%>
	                            <logic:match name="stateObject" property="code" value="<%=strState%>">
	                               <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="name"/></option>
	                            </logic:match>
	                            
	                            <logic:notMatch name="stateObject" property="code" value="<%=strState%>">
	                                <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="name"/></option>
	                            </logic:notMatch>
	                    <%}%>
	                        </logic:iterate>
	                    <%if ((status == "CREATE") && (strState.length() == 0)){%>
	                            <option selected="true" value=""/></option>
	                    <%}else{%>
	                            <option value=""/></option>
	                    <%}%>
	                  </html:select>                     
	                </td>
	                <%}else{%>
	                <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.provinceregion"/></td>
	                <td><html:text styleClass="inputs" styleId="state" name="RegisterOrganizationForm" property="state" tabindex="7" size="20" maxlength="60" onchange="copyMailToBill(this)"/></td>
	                <%}%>
			             
			          </tr>
			          <tr>
			           <td class="Label" width="120" ><span style="color: #990000">* </span><bean:message key="label.registration.phone"/></td>
            				<td colspan="3"><html:text styleClass="inputs" name="RegisterOrganizationForm" property="phone" styleId="phone" tabindex="7" size="20" maxlength="30" onchange="ValidPhoneNumber(RegisterOrganizationForm.phone, RegisterOrganizationForm.country);copyMailToBill(this)"/></td>
            				<td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.extension"/></td>
            				<td width="52"><html:text styleClass="inputs" name="RegisterOrganizationForm" property="phoneExtension" styleId="phoneExtension" tabindex="7" size="5" maxlength="10" onchange="copyMailToBill(this)"/></td>
			          </tr>
			          <tr>
			             <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.fax"/></td>
            			<td colspan="3"><html:text styleClass="inputs" name="RegisterOrganizationForm" property="fax" styleId="fax" tabindex="7" size="20" maxlength="30" onchange="ValidPhoneNumber(RegisterOrganizationForm.fax, RegisterOrganizationForm.country);copyMailToBill(this)"/></td>
			          </tr>
			          
			           <logic:equal property="status" name="RegisterOrganizationForm" value="CREATE">
						           <tr>
				               		 <td class="Label">
				                <span style="color: #990000">* </span><bean:message key="label.registration.email"/></td>
				                <td rowspan="2" colspan="3">
				                <div z-index="1" style="position: relative;">
				                <html:text styleClass="inputs" name="RegisterOrganizationForm" property="userName" styleId="email" tabindex="7" size="20" maxlength="80" onchange="ltrimField(this);copyMailToBill(this)"/>
				                </div>          
				                <div z-index="2" style="position: relative; font-size: 7pt; margin-top: 0px;">Important! You will use this e-mail address to access your account </div>
				                </td>
				                </tr> 
				                <tr><td></td><td></td></tr>           
	     				</logic:equal>
	     				<tr>
	     				 <td class="Label">
	                			<span style="color: #990000">* </span><bean:message key="label.registration.personType"/></td>
	                		<td colspan="3">
	                		<html:select name="RegisterOrganizationForm" property="personType" styleId="personType" tabindex="7" onchange="copyMailToBill(this)">
	                    		<html:option value="">- Select One -</html:option>
	                    		<html:option value="Business User"><bean:message key="label.registration.bus_user"/></html:option>
	                    		<html:option value="Academic User"><bean:message key="label.registration.aca_user"/></html:option>
	                    		<html:option value="Publisher"><bean:message key="label.registration.publisher"/></html:option>
	                    		<html:option value="Author or Creator"><bean:message key="label.registration.auth_creator"/></html:option>
	                    		<html:option value="Other"><bean:message key="label.registration.other"/></html:option>
	                  		</html:select>
	                  		</td>
	     				</tr>
	     				
			        	<logic:equal property="isRegisteredRlnkUser" name="RegisterOrganizationForm"  value="true">
		       			<logic:equal property="status" name="RegisterOrganizationForm" value="UPDATE">
		                <tr>
			            	<td class="Label"><bean:message key="label.registration.taxCertificate"/>#</td>
		    	        	<td><html:text styleClass="inputs" name="RegisterOrganizationForm" property="taxId" tabindex="1" size="20" maxlength="80" onchange="ltrimField(this)"/></td>
		            	</tr>
                 		</logic:equal>
           				</logic:equal>
			          <logic:equal property="status" name="RegisterOrganizationForm" value="UPDATE">
						<tr>
        		     			 <td width="250" class="Label"><span style="color: #990000"></span><bean:message key="label.registration.account"/>:</td>
	              				  <td style="color: #000000;"><bean:write name="RegisterOrganizationForm" property="account" /></td>
	                	</tr>
                  		 <tr>
        		    		 <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.mainContactLastName"/>:</td>
	               		 <td style="color: #000000;"><bean:write name="RegisterOrganizationForm" property="mainContactLastName" /></td>
	                		</tr>
             				</logic:equal>
        
			          </table><!--  end of right column -->
		</td>
        
       </tr>
        
    
    </table>
    <logic:equal property="status" name="RegisterOrganizationForm" value="CREATE">
        		    <html:hidden property="industryType" name="RegisterOrganizationForm" value="0"/>
        		     <html:hidden property="orgStatus" name="RegisterOrganizationForm" value="0"/>
    </logic:equal>
    <logic:equal property="status" name="RegisterOrganizationForm" value="UPDATE">
    <logic:equal property="isRegisteredRlnkUser" name="RegisterOrganizationForm"  value="false">
    				<html:hidden property="industryType" name="RegisterOrganizationForm" value="0"/>
        		     <html:hidden property="orgStatus" name="RegisterOrganizationForm" value="0"/>
    </logic:equal>
    </logic:equal>
    

        <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="FALSE">
        
        <div class="horiz-rule"></div>
        
        <table width="664" style="margin-left: 12px">
          <tr>
            <td colspan="4"><h4 style="font-size: 8pt; color: #336699; margin-top: 3px; margin-bottom: 0px;">Account password </h4>
              <p style="margin-top: 4px; margin-bottom: 12px;">Your password must be at least 6 characters. We recommend using upper and <br />
              lowercase characters and at least one digit.</p>              
            </td>
          </tr>
          <tr>
            <td width="123" class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.password"/></td>
            <td width="174">
            <html:password styleClass="inputs" name="RegisterOrganizationForm" property="password" tabindex="7"  size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td width="80" class="Label">&nbsp;</td>
            <td width="252">&nbsp;</td>
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.confirmPassword"/></td>
            <td>
            <html:password styleClass="inputs" name="RegisterOrganizationForm" property="confirmPassword" tabindex="7" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          
          
          <tr>
            <td colspan="4">&nbsp;</td>
          </tr>
          
          <!--
          <tr>
            <td colspan="4"><p class="boxLine">&nbsp; </p></td>
          </tr>
          -->
          
        </table>
        </logic:equal>
        
        <div class="horiz-rule"></div>
        
        <table width="664" style="margin-left: 12px">
          
            <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="FALSE">
            <tr><td colspan="4"><h4 style="font-size: 8pt; color: #336699; margin-top: 3px; margin-bottom: 0px;">Billing information</h4>
              <p style="margin-top: 4px; margin-bottom: 3px;">Please provide information for the billing contact at your organization. </p>
              </td></tr>
            </logic:equal>
            <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="TRUE">
            <tr><td colspan="4"><h4 style="font-size: 8pt; color:  #000000; margin-top: 3px; margin-bottom: 0px;">Current billing information</h4>
              <p style="margin-top: 4px; margin-bottom: 3px;">This is the current billing information for your organization. </p>
              </td></tr>
            </logic:equal>
		<%if (strSameBilling.equalsIgnoreCase("T")){%>
              <tr><td colspan="4"><p style="margin-top: 0px; margin-bottom: 12px;">
              <html:checkbox styleId="sameBill"  name="RegisterOrganizationForm" value="T" property="sameBilling" onclick="CopyBilling(this)" style="margin-right:2px;" />Same as contact information.
              </p></td></tr>
		<%}else{%>
		<p style="margin-top: 0px; margin-bottom: 12px;">
               <tr><td colspan="4"><html:checkbox styleId="sameBill"  name="RegisterOrganizationForm" property="sameBilling" onclick="CopyBilling(this)" style="margin-right:2px;" />Same as contact information.
              </p></td></tr>
		<%}%>  
            
          <tr>
                   <td style="vertical-align:top" colspan="2">
			          <table id="leftColumn">
			          <tr>
			           <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="FALSE">
           				 <td width="183" class="Label"><bean:message key="label.registration.title" /></td>
         				 </logic:equal>
         				 <logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="TRUE">
          				  <td width="123" class="Label"><bean:message key="label.registration.title" /></td>
          				  
         			 </logic:equal>
         			 <td width="149">
         			  <html:select name="RegisterOrganizationForm" styleClass="readonlycontent" property="billingTitle" styleId="billingTitle" tabindex="8">
						              <html:option value=""></html:option>
						              <html:option value="Mr."><bean:message key="label.registration.mr"/></html:option>
						              <html:option value="Mrs."><bean:message key="label.registration.mrs"/></html:option>
						              <html:option value="Ms."><bean:message key="label.registration.ms"/></html:option>
						              <html:option value="Miss"><bean:message key="label.registration.miss"/></html:option>
						              <html:option value="Dr."><bean:message key="label.registration.dr"/></html:option>
						              <html:option value="Prof."><bean:message key="label.registration.prof"/></html:option>
						              <html:option value="Sir"><bean:message key="label.registration.sir"/></html:option>
						            </html:select>
         			 </td>
         			 </tr>
         	       <tr>
            			<td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.firstName"/></td>
            			<td><html:text styleId="billingFirstName" styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingFirstName" tabindex="8" size="20" maxlength="15" onchange="ltrimField(this)"/></td>
			          </tr>
         			 <tr>
         			  <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.middleName"/></td>
            			<td><html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingMiddleName" styleId="billingMiddleName" tabindex="8" size="20" maxlength="20" onchange="ltrimField(this)"/></td>
         			 </tr>
         			 <tr>
         			  <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.lastName"/></td>
            			<td><html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingLastName" styleId="billingLastName" tabindex="8" size="20" maxlength="20" onchange="ltrimField(this)"/></td>
         			 </tr>
                     <!--
         			 <tr>
         			 	<td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.jobTitle"/></td>
            			<td><html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingJobTitle" styleId="billingJobTitle" tabindex="8" size="20" maxlength="30" onchange="ltrimField(this)"/></td>
         			 </tr>
         			 <tr>
         			 <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.department"/></td>
                      <td><html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingDepartment" styleId="billingDepartment" tabindex="8" size="20" maxlength="60" onchange="ltrimField(this)"/></td>
         			 </tr>
         			 -->
         			 
			          <tr>
			            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.address1"/></td>
			            <td>
			            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingAddress1" styleId="billingAddress1" tabindex="8" size="20" maxlength="60" onchange="ltrimField(this)"/>
			            </td>
			          </tr>
			          
			          <tr>
			            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.address2"/></td>
			            <td>
			            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingAddress2" styleId="billingAddress2" tabindex="8" size="20" maxlength="60" onchange="ltrimField(this)"/>
			            </td>        
			          </tr>
			          <tr>
			            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.address3"/></td>
			            <td>
			            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingAddress3" styleId="billingAddress3" tabindex="8" size="20" maxlength="60" onchange="ltrimField(this)"/>
			            </td>
			          </tr>
			          </table>
			          </td>
          
                   <td style="vertical-align:top" colspan="2">
                   <table id="rightColumn"><!-- RIGHT COLUMNS -->
                   			                   <tr>
          <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.country"/></td>
          <td colspan="3">
          <html:select name="RegisterOrganizationForm" styleClass="readonlycontent" property="billingCountry" styleId="billingCountry" tabindex="8" onchange="checkCountry(this)">
          <logic:iterate name="RegisterOrganizationForm" property="countryList" id="countryObject">
          <%
            if (strBillingCountry.length() > 0)
              {
               %>
                <logic:equal name="countryObject" property="abbrev" value="<%=strBillingCountry%>">
                <option selected="true" value='<bean:write name="countryObject" property="abbrev"/>'><bean:write name="countryObject" property="name"/></option>
                </logic:equal>
                <logic:notEqual name="countryObject" property="abbrev" value="<%=strBillingCountry%>">
                <option value='<bean:write name="countryObject" property="abbrev"/>'><bean:write name="countryObject" property="name"/></option>
                </logic:notEqual>
            <%
              }
            else
              {
                %>
                <logic:equal name="countryObject" property="abbrev" value="<%=strBillingCountry%>">
                <option selected="true" value='<bean:write name="countryObject" property="abbrev"/>'><bean:write name="countryObject" property="name"/></option>
                </logic:equal>
                <logic:notEqual name="countryObject" property="abbrev" value="<%=strBillingCountry%>">
                <option value='<bean:write name="countryObject" property="abbrev"/>'><bean:write name="countryObject" property="name"/></option>
                </logic:notEqual>
                <%
              }
                %>
      
      
      </logic:iterate>  
      </html:select>

            </td>
            </tr
			          <tr>
							<%if (strBillingCountry.equalsIgnoreCase("US")){%>
					            <td width="170" class="Label">
					            <span style="color: #990000">* </span><bean:message key="label.registration.zipcode"/>
					            </td>
					            <td width="40">
					            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingZipcode" styleId="billingZipCode" tabindex="12" size="5" maxlength="5" onchange="var billingZipPlus4=document.getElementById('billingZipPlus4');getCityStateAjax(this,RegisterOrganizationForm.billingCountry,billingZipPlus4)"/>
					            </td>
					            <td width="20" class="Label" id="billingPlus4Label"><span style="color: #990000"></span><bean:message key="label.registration.plus4"/></td>
            				    <td width="52"><html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingZipPlus4" styleId="billingZipPlus4" tabindex="12" size="5" maxlength="4" onchange="var zip=document.getElementById('billingZipCode');isZipValid(zip,this);copyMailToBill(this)"/></td>
					            
					            <%}else if (strBillingCountry.equalsIgnoreCase("CA")){%>
					            <td width="170" class="Label">
					            <span style="color: #990000">* </span><bean:message key="label.registration.postalcode"/>
					            </td>
					            <td width="242">
					            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingZipcode" styleId="billingZipCode" tabindex="12" size="20" maxlength="20" onchange=""/>
					            </td>  
					            <td width="52"><html:text style="display:none" styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingZipPlus4" styleId="billingZipPlus4" tabindex="7" size="5" maxlength="4" onchange="var zip=document.getElementById('billingZipCode');isZipValid(zip,this);copyMailToBill(this)"/></td>       
					            <%}else{%>
						    <td width="170" class="Label"><span style="color: #990000">
					        </span><bean:message key="label.registration.postalcode"/></td>
						    <td width="242">
					        <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingZipcode" styleId="billingZipCode" tabindex="9" size="20" maxlength="20" onchange=""/></td>
					        <td width="52"><html:text style="display:none" styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingZipPlus4" styleId="billingZipPlus4" tabindex="12" size="5" maxlength="4" onchange="var zip=document.getElementById('billingZipCode');isZipValid(zip,this);copyMailToBill(this)"/></td>                    
						    <%}%>
			          
			          </tr>
			          <tr>
			          <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.city"/></td>
            <td colspan="3">
            <html:text styleClass="readonlycontent" disabled="${disableBill}" name="RegisterOrganizationForm" property="billingCity" styleId="billingCity" tabindex="12" size="20" maxlength="60" onchange="ltrimField(this)"/>
            </td>
			          </tr>
			          
			          <tr>
			          
			           <%if (strBillingCountry.equalsIgnoreCase("US")){ %>
                <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.state"/></td>
                <td>
                <%if (strBillingState.length() > 0){%>
                    <html:select name="RegisterOrganizationForm" styleClass="readonlycontent" property="billingState" styleId="billingState" disabled="true" tabindex="12" onchange="">
                    
                        <logic:iterate name="RegisterOrganizationForm" property="stateList" id="stateObject">
            
                            <logic:match name="stateObject" property="code" value="<%=strBillingState%>">
                                <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
                            </logic:match>
                            
                            <logic:notMatch name="stateObject" property="name" value="<%=strBillingState%>">
                                <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
                            </logic:notMatch>
                
                        </logic:iterate>
                        <%if ((status == "CREATE") && (strBillingState.length() == 0)){%>
                                <option selected="true" value=""/></option>
                        <%}else{%>
                                <option value=""/></option>
                        <%}%>
                    </html:select>
    
                <%}else{%>
                    <html:select name="RegisterOrganizationForm" styleClass="readonlycontent" property="billingState" styleId="billingState" tabindex="12" onchange="">
                        
                        <logic:iterate name="RegisterOrganizationForm" property="stateList" id="stateObject">
            
                            <logic:match name="stateObject" property="code" value="<%=strBillingState%>">
                                <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
                            </logic:match>
                            
                            <logic:notMatch name="stateObject" property="code" value="<%=strBillingState%>">
                                <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
                            </logic:notMatch>
                
                        </logic:iterate>
                    <%if ((status == "CREATE") && (strBillingState.length() == 0)){%>
                            <option selected="true" value=""/></option>
                    <%}else if ((status == "UPDATE") && (strBillingState.length() == 0)){%>
                            <option selected="true" value=""/></option>
                    <%}else{%>
                            <option value=""/></option>
                    <%}%>
                    </html:select>
                <%}%>
              </td>          
              <%}else if (strBillingCountry.equalsIgnoreCase("CA")){%>
                <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.province"/></td>
                <td>
	
                <html:select name="RegisterOrganizationForm" styleClass="readonlycontent" property="billingState" styleId="billingState" tabindex="12" onchange="">
            
                    <logic:iterate name="RegisterOrganizationForm" property="provinceList" id="stateObject">
                <%if (strBillingState.length() > 0){%>
                        <logic:match name="stateObject" property="code" value="<%=strBillingState%>">
                            <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="name"/></option>
                        </logic:match>
                        
                        <logic:notMatch name="stateObject" property="code" value="<%=strBillingState%>">
                            <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="name"/></option>
                        </logic:notMatch>
                <%}else{%>
                        <logic:match name="stateObject" property="code" value="<%=strBillingState%>">
                           <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="name"/></option>
                        </logic:match>
                        
                        <logic:notMatch name="stateObject" property="code" value="<%=strBillingState%>">
                            <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="name"/></option>
                        </logic:notMatch>
                <%}%>
                    </logic:iterate>
                <%if ((status == "CREATE") && (strBillingState.length() == 0)){%>
                        <option selected="true" value=""/></option>
                <%}else{%>
                        <option value=""/></option>
                <%}%>
              </html:select>                     
            
            </td>

              <%}else{%>
              <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.provinceregion"/></td>
              <td>
              <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingState" styleId="billingState" disabled="false" tabindex="12" size="20" maxlength="60" onchange="ltrimField(this)"/>
             </td>
              <%}%>
			          
			          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.phone"/></td>
            <td colspan="3">
            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingPhone" styleId="billingPhone" tabindex="12" size="20" maxlength="30" onchange="ValidPhoneNumber(RegisterOrganizationForm.billingPhone, RegisterOrganizationForm.billingCountry)"/>
            </td>
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.extension"/></td>
            <td width="52">
            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingPhoneExtension" styleId="billingPhoneExtension" tabindex="12" size="5" maxlength="10" onchange=""/>
            </td>  
          </tr>
          
          <tr>
            
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.fax"/></td>
            <td colspan="3">
            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingFax" styleId="billingFax" tabindex="12" size="20" maxlength="30" onchange="ValidPhoneNumber(RegisterOrganizationForm.billingFax, RegisterOrganizationForm.billingCountry)"/>
            </td>
          </tr>
            <%if (status == "CREATE"){%>
            <tr>
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.email"/></td>
            <td colspan="3">
            <div z-index="1" style="position: relative;">
            <html:text styleClass="readonlycontent" name="RegisterOrganizationForm" property="billingEmail" styleId="billingUsername" tabindex="12" size="20" maxlength="80" onchange="ltrimField(this)"/>
            </div>          
            </td>
            </tr>
            <%}%>
			          
			          </table>
                   </td>
          </tr>
          </table>
          
    

  
        <logic:equal name="RegisterOrganizationForm" property="status" value="UPDATE">
          <table width="664" style="margin-left: 12px">
              <tr>
                  <logic:equal name="RegisterOrganizationForm" property="alwaysInvoice" value="T">
                      <td><html:checkbox  name="RegisterOrganizationForm" value="T" property="alwaysInvoice" onclick="CopyAlwaysInvoice(this)" style="margin-right:-2px;" /></td>
                      <td>Always invoice me<br/></br>
                          (If you select this option, you will skip the Payment Information step when you purchase  <br>
                          permission through copyright.com and your account will be automatically invoiced.)</td><td></td>        
                      
                  </logic:equal>
  
                  <logic:equal name="RegisterOrganizationForm" property="alwaysInvoice" value="F">
                  <!-- <p style="margin-top: 0px; margin-bottom: 12px;">  -->
                  <td><html:checkbox  name="RegisterOrganizationForm" property="alwaysInvoice" onclick="CopyAlwaysInvoice(this)" style="margin-right:-2px;" /></td>
                  <td>Always invoice me<br/><br/>
                      (If you select this option, you will skip the Payment Information step when you purchase  <br>
                      permission through copyright.com and your account will be automatically invoiced.)</td><td></td>
          
                  
                  </logic:equal>
              </tr>
              <tr><td colspan="3">&nbsp;</td></tr>
                         <tr>
                  <logic:equal name="RegisterOrganizationForm" property="autoInvoiceSpecialOrder" value="T">
                      <td><html:checkbox  name="RegisterOrganizationForm" value="T" property="autoInvoiceSpecialOrder" onclick="CopyAutoInvoiceSpecialOrder(this)" style="margin-right:-2px;" /></td>
                       <td>Always automatically accept special orders when they are granted<br/><br/>
                      (If you select this option, you will skip the option to accept or decline a special order when a rightsholder <br>
                      grants permission and your account will be automatically billed.)</td><td></td>     
                      
                  </logic:equal>
  
                  <logic:equal name="RegisterOrganizationForm" property="autoInvoiceSpecialOrder" value="F">
                  <!-- <p style="margin-top: 0px; margin-bottom: 12px;">  -->
                  <td><html:checkbox  name="RegisterOrganizationForm" property="autoInvoiceSpecialOrder" onclick="CopyAutoInvoiceSpecialOrder(this)" style="margin-right:-2px;" /></td>
                  <td>Always automatically accept special orders when they are granted<br/><br/>
                      (If you select this option, you will skip the option to accept or decline a special order when a rightsholder <br>
                      grants permission and your account will be automatically billed.)</td><td></td>
          

                  
                  </logic:equal>
              </tr>

          </table>
    </logic:equal>
   
          <table width="664" style="margin-left: 12px">
          <tr>&nbsp;</tr>
          <tr>
            <td colspan="4" align="center">  
            <a href="javascript:sbmtPage()" id="submitBtnLink" tabindex="12"><html:img src="/media/images/submit_button.gif" onclick="this.disabled=true;" styleId="submitBtn" alt="Register" title="Register" /></a>
            </td>
          </tr>
          
          </table>
          
                    
        </table>
        
</html:form>

<script type="text/javascript">disableEnableBilling()</script>

<!-- Webtrends tags for capturing scenarios -->
<logic:equal name="RegisterOrganizationForm" property="isLoggedIn" value="FALSE"> 
    <%
        Object isRedisplayObj = request.getAttribute( "isRedisplay" );
        if( isRedisplayObj == null || !(((Boolean)isRedisplayObj).booleanValue()) )
        {
    %>
        <META name="WT.si_n" content="Org_reg">
        <META name="WT.si_p" content="Org_reg_page">
    <%
        }
    %>
</logic:equal>
<!-- end Webtrends tags -->
