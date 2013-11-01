<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.data.account.ZipLocation" %>
<%@ page import="java.util.*" %>
<%@ page import="com.copyright.ccc.web.forms.*" %>
<%@ page import="org.apache.struts.action.*" %>

<bean:define id="RegisterIndividualForm" name="RegisterIndividualForm" type="com.copyright.ccc.web.forms.RegisterIndividualForm"/>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">
<script src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/cityStatePopulator.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<bean:define id="disable">
    <logic:equal name="RegisterIndividualForm" property="country" value="US">true</logic:equal>
    <logic:notEqual name="RegisterIndividualForm" property="country" value="US">false</logic:notEqual>
</bean:define>


<%
String strcntry = "";
String strState = "";

if (!(RegisterIndividualForm.getCountry() == null) &&
     (RegisterIndividualForm.getCountry().length() > 0))
{
    strcntry = RegisterIndividualForm.getCountry();
}

if (!(RegisterIndividualForm.getState() == null) &&
     (RegisterIndividualForm.getState().length() > 0))
{
    strState = RegisterIndividualForm.getState();
}

String status = "";
status = RegisterIndividualForm.getStatus();

String autoCity = "";

%>

<script type="text/javascript">
//var submitFlag = "T";
var submitBtnClick = "F";

function sbmtPage() {
  
    if (submitBtnClick == "F")
     {
         //alert("Sub clicked.....");
         var myFrm = document.getElementById('frm');
         var myState = document.getElementById("state");
         var myCity = document.getElementById("city");
         
         submitBtnClick = "T";
         
         // If we are registering from some other country,
         // state is null.
         
         if (myState != null) myState.disabled = false;
         //if (myCity != null) myCity.disabled = false;
         myCity.disabled = false;
         document.getElementById("state").disabled = false;
         document.getElementById("city").disabled = false;
         document.getElementById("submitBtnLink").href = "#";
         myFrm.submit();
     }
}

function submitRegisterUser() {
	
	//alert("Submit Click: " + submitFlag);
  
  if (document.RegisterIndividualForm.submitFlag.value == "T")
  {
	document.RegisterIndividualForm.submitFlag.value = "F";
	//document.RegisterIndividualForm.middleName.value = "X";
	//submitFlag = "F";  
  	document.forms[0].action.value = "registerIndividual";
  	return true;
  	//document.forms[0].submit();
  }
  else
  {
	document.RegisterIndividualForm.submitFlag.value = "F";
	//document.RegisterIndividualForm.middleName.value = "Y";
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

     strZipPlus4 = fldZipPlus4.value;


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

    if ( fldCountry.value == "US")
    {
        if (isZipValid(fld,fld2) == false)
        {
            fld.select();
            return false;
            
        }
        
        var cityStatePop = new CityStatePopulator("IND");
        
        //alert("Field: " + fld.name);
        if (fld.name == "zipcode")
        {
            cityStatePop.populateCityAndState(fld.value, "city", "state", "phone");           
            //document.getElementById("phone").select();
            
            /* var mailCity = document.getElementById("city");
            
            if (mailCity.value.length > 0)
            {
                document.getElementById("phone").select();
            }
            else
            {
                mailCity.select();
            } */
        }
    
    }
    
}

function getCityState(fld, fldCountry)
{
    if ( fldCountry.value == "US")
    {
      if (isZipValid(fld) == false)
      {
       return false;
      }
    
      if (fld.name == "zipcode")
      {
        //alert("Field Name: " + fld.name);
        document.RegisterIndividualForm.zipCodeChanged.value = "MAIL";
      }
      repaintPage();
    }      
}
function checkCountry(fld)
{
	/* if (fld.value == "CA")
	{
		alert("The reproduction right organization (RRO) in your country asks that you contact it for assistance in obtaining permission to use copyrighted content. To locate your RRO, please see our RROs page: www.copyright.com/rro.");
		fld.value = "UNITED STATES";
    } */
        	    
      if (fld.value == "US" && fld.name == "country")
      {
      	document.getElementById("city").disabled = true;
        strState = "";
        document.getElementById("state").value = "";
        document.getElementById("zipcode").value = "";
        document.getElementById("city").value = "";
         //alert("In state reset");
        /*
		<%
        if (strState.length() > 2)
        {
            strState = "";
        %>
            document.getElementById("state").value = "";
        <%
        }
        %> */
      }
      if (fld.value != "US" && fld.name == "country")
      { 
      	var myCity = document.getElementById("city");
      	myCity.disabled = false;
      }
	//alert("Test CANADA");
	repaintPage();

}
function repaintPage()
{
		//Referencing the form as below does not work in Firefox. The method below that works in both IE and FireFox
		
    //document.getElementById("RegisterIndividualForm").action = "redisplayIndRegistration";
    //document.getElementById("RegisterIndividualForm").submit();
    
    var myState = document.getElementById("state");
    var myCity = document.getElementById("city");
    
    if (myState != null) myState.disabled = false;
    myCity.disabled = false;
    document.RegisterIndividualForm.action = "redisplayIndRegistration.do";
    document.RegisterIndividualForm.submit();
}
//This function loads the page depending on the Type of Acct chosen

function displayPage(choice)
{
    //alert("In reload Page " + choice);
    if (choice == "O")
    {
        userChoice = "O";
        //Referencing the form as below does not work in Firefox. The method below that works in both IE and FireFox
        
        //document.getElementById("RegisterIndividualForm").action = "displayOrgRegistration"; 
        document.RegisterIndividualForm.action = "displayOrgRegistration.do";
        //document.getElementById("RegisterIndividualForm").submit();
        document.RegisterIndividualForm.submit();
    
    }
    else if (choice == "I")
    {
       userChoice = "I";
       //document.getElementById("RegisterIndividualForm").action = "displayIndRegistration";
       //document.getElementById("RegisterIndividualForm").submit();
    }
}

function CopyAlwaysInvoice(fld)
{
    if (fld.checked)
    {
        document.RegisterIndividualForm.alwaysInvoice.value = "T";
        document.RegisterIndividualForm.alwaysInvoiceFlag.value = "T";  
    }
    else
    {
        document.RegisterIndividualForm.alwaysInvoice.value = "F";
        document.RegisterIndividualForm.alwaysInvoiceFlag.value = "F"; 
    }



}

function CopyAutoInvoiceSpecialOrder(fld)
{
    if (fld.checked)
    {
        document.RegisterIndividualForm.autoInvoiceSpecialOrder.value = "T";
        document.RegisterIndividualForm.autoInvoiceSpecialOrderFlag.value = "T";  
    }
    else
    {
        document.RegisterIndividualForm.autoInvoiceSpecialOrder.value = "F";
        document.RegisterIndividualForm.autoInvoiceSpecialOrderFlag.value = "F"; 
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
var validUSPhoneChars = digits + phoneNumberDelimiters;
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

    for (var i = 1; i < reformat.arguments.length; i++) {
       arg = reformat.arguments[i];
       if (i % 2 == 1) resultString += arg;
       else {
           resultString += s.substring(sPos, sPos + arg);
           sPos += arg;
       }
    }
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

function CopyBilling(fld)
{
    
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
               // fld.value = "011-" + strNum;
               fld.value = strNum;
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

<html:form action="/registerIndividual" method="POST" onsubmit="submitRegisterUser();" styleId="frm">
<html:hidden name="RegisterIndividualForm" property="zipCodeChanged"/>
<html:hidden name="RegisterIndividualForm" property="submitFlag" value="T"/>
<html:hidden name="RegisterIndividualForm" property="alwaysInvoiceFlag"/>
<html:hidden name="RegisterIndividualForm" property="autoInvoiceSpecialOrderFlag"/>

 
<body>


<!-- <div id="ecom-boxcontent"> -->

    
    <logic:equal name="RegisterIndividualForm" property="isLoggedIn" value="FALSE">
  <!--  <div id="onecolumn"> -->
      <h2>Create a Pay-per-use Account</h2>
      <p style="text-align: right; color: #990000; margin-top: -20px">* indicates required field</p>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
       <b>If you already use our Rightslink service,</b> you don't need to create an account. 
        <util:contextualHelp helpId="35" rollover="false" styleId="1">
           Learn More&hellip;
        </util:contextualHelp>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
    </logic:equal>
    <logic:equal name="RegisterIndividualForm" property="isLoggedIn" value="TRUE">
 <!--   <div id="onecolumn"> -->
      <h2>UPDATE ACCOUNT INFORMATION</h2>
      <p style="text-align: right; color: #990000; margin-top: -20px">* indicates required field</p>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
    </logic:equal>
    <logic:equal name="RegisterIndividualForm" property="isLoggedIn" value="FALSE">
        <table width="664" cellpadding="0" cellspacing="0" border="0" style="margin-left: 12px">
          <tr>
            <td colspan="2"><h4 style="font-size: 8pt; color: #336699; margin-top: 5px; margin-bottom: 6px;">Type of account<span style="color: #990000">*</span></h4></td>
          </tr>
          <tr>
            <td class="Label" width="21"><input type="radio" name="typeOfAccount" value="O" onclick="displayPage(this.value)">
            </td>
            <td class="Label" width="643"><p style="text-align: left; margin-top: 0px;">Organizational (If you are obtaining permission on behalf of an academic institution, company or organization.) </p></td>
          </tr>
          <tr>
            <td class="Label" width="21"><input type="radio" name="typeOfAccount" checked="checked" value="I">
            </td>
            <td class="Label" width="643"><p style="text-align: left; margin-top: 0px;">Individual (If you are obtaining permission for your own individual use.) </p></td>
          </tr>
                                        
        </table>
        
        <div class="horiz-rule"></div>
        
    </logic:equal>
    
    
    <!-- -------- NEW TABLE BLOCK -->
    
    <table width="664" style="margin-left: 12px">
    <logic:equal name="RegisterIndividualForm" property="isLoggedIn" value="FALSE">
          <tr>
            <td colspan="2"><h4 style="font-size: 8pt; color: #336699; margin-top: 5px; margin-bottom: 0px;">Contact information</h4>
              <p style="margin-top: 4px; margin-bottom: 12px;"><bean:message key="label.registration.addressExplanation" /> (<a href="/content/cc3/en/tools/footer/privacypolicy.html" target="_blank" style="color: #336699;"><bean:message key="label.registration.privacyPolicy" /></a>.)</p></td>
          </tr>
          </logic:equal>
          <logic:equal name="RegisterIndividualForm" property="isLoggedIn" value="TRUE">
          <tr>
            <td colspan="2"><h4 style="font-size: 8pt; color: #000000; margin-top: 5px; margin-bottom: 0px;">Current contact information</h4>
              <p style="margin-top: 4px; margin-bottom: 12px;"><bean:message key="label.registration.addressExplanation" /> (<a href="/content/cc3/en/tools/footer/privacypolicy.html" target="_blank" style="color: #336699;"><bean:message key="label.registration.privacyPolicy" /></a>.)</p></td>
          </tr>
          </logic:equal>
          
          <tr>
          <td style="vertical-align:top">
			          <table id="leftColumn">
			          <tr>
			          		<td width="183" class="Label"><bean:message key="label.registration.title" /></td>
            				<td width="149">
            						<html:select name="RegisterIndividualForm" property="title" tabindex="1" onchange="">
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
           				    <td><html:text styleClass="inputs" name="RegisterIndividualForm" property="firstName" tabindex="1" size="20" maxlength="15" onchange="ltrimField(this)"/></td>
			          </tr>
				           <tr>
				          		<td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.middleName"/></td>
         						   <td><html:text styleClass="inputs" name="RegisterIndividualForm" property="middleName" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/></td>
            		          </tr>
            		           <tr>
				          		 <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.lastName"/></td>
				          		 <td><html:text styleClass="inputs" name="RegisterIndividualForm" property="lastName" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/></td>
				          </tr>
				          <tr>
				          		<td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.address1"/></td>
            					<td><html:text styleClass="inputs" name="RegisterIndividualForm" property="address1" tabindex="1" size="20" maxlength="60" onchange="ltrimField(this)"/></td>
				          </tr>
				          <tr>
				          <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.address2"/></td>
            				<td><html:text styleClass="inputs" name="RegisterIndividualForm" property="address2" tabindex="1" size="20" maxlength="60" onchange="ltrimField(this)"/></td>
				          </tr>
				          <tr>
				            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.address3"/></td>
            				<td><html:text styleClass="inputs" name="RegisterIndividualForm" property="address3" tabindex="1" size="20" maxlength="60" onchange="ltrimField(this)"/></td>   
				          </tr>
				          
			          
			          </table><!--  end left column table-->
          </td>
          <td style="vertical-align:top"><!--  begin right column table-->
				          <table id="rightColumn">
				         <tr>
				            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.country"/></td>
          				 <td colspan="3">
	              				<html:select name="RegisterIndividualForm" property="country" tabindex="1"  style="width:150px" onchange="checkCountry(this)">
	              				<logic:iterate name="RegisterIndividualForm" property="countryList" id="countryObject">
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
  								</td>
				           
				          </tr>
				          <tr>
					            <%if (strcntry.equalsIgnoreCase("US")){%>
	            				<td width="120" class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.zipcode"/></td>
		        				<%}else if (strcntry.equalsIgnoreCase("CA")){%>
	            				<td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.postalcode"/></td>
	            				<%}else{%>
	            				<td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.postalcode"/></td>
	            				<%}%>
							<%if (strcntry.equalsIgnoreCase("US")){%>
		                        <td witdh="20%"><html:text styleClass="inputs" name="RegisterIndividualForm" property="zipcode" styleId="zipcode" tabindex="7" size="5" maxlength="5" onchange="zip4=document.getElementById('zipPlus4');getCityStateAjax(this,RegisterIndividualForm.country,zip4)"/></td>
							    <td width="20px" class="Label"><span style="color: #990000"></span><bean:message key="label.registration.plus4"/></td>
            				    <td width="52"><html:text styleClass="inputs" name="RegisterIndividualForm" property="zipPlus4" styleId="zipPlus4" tabindex="7" size="4" maxlength="4" onchange="zip=document.getElementById('zipcode');isZipValid(zip,this)"/></td>
							       
							<%}else{%>
	            				<td ><html:text styleClass="inputs" name="RegisterIndividualForm" property="zipcode" styleId="zipcode" tabindex="7" size="20" maxlength="20" onchange="javascript:getCityStateAjax(this,RegisterIndividualForm.country)"/></td>
						    <%}%>
				          		 
				          </tr>
				          <tr>
				   <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.city"/></td>
				          		 <td colspan="3"><html:text disabled="${disable}" styleClass="city" name="RegisterIndividualForm" property="city" styleId="city" tabindex="7" size="20" maxlength="60" onchange="ltrimField(this)"/></td>
			          </tr>
			        
			          <tr>
			          	      			<%if (strcntry.equalsIgnoreCase("US")){%>
	                				<td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.state"/></td>
	                				<td colspan="3">
	                					<%if (strState.length() > 0){%>
	                   						<html:select name="RegisterIndividualForm" property="state" styleId="state" disabled="true" tabindex="7" onchange="">
	                   							<logic:iterate name="RegisterIndividualForm" property="stateList" id="stateObject">
								                    <logic:match name="stateObject" property="code" value="<%=strState%>">
								                    <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
								                    </logic:match>
								                    <logic:notMatch name="stateObject" property="code" value="<%=strState%>">
								                    <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
								                    </logic:notMatch>       
								                    </logic:iterate>
			                	<%if ((status == "CREATE") && (strState.length() == 0)){%>
	    			                <option selected="true" value=""/></option>
	                		  <%}else{%>
	                    			<option value=""/></option>
	                  				<%}%>
	                				</html:select>
	                <%}else{%>
	                    <html:select name="RegisterIndividualForm" property="state" styleId="state" tabindex="7" onchange="">
	                    <logic:iterate name="RegisterIndividualForm" property="stateList" id="stateObject" >
	                  
	                    <logic:match name="stateObject" property="code" value="<%=strState%>">
	                    <option selected="true" value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
	                    </logic:match>
	                    <logic:notMatch name="stateObject" property="code" value="<%=strState%>">
	                    <option value='<bean:write name="stateObject" property="code"/>'><bean:write name="stateObject" property="code"/></option>
	                    </logic:notMatch>
	                  </logic:iterate>
	                	<%if ((status == "CREATE") && (strState.length() == 0)){%>
	                    <option selected="true" value=""/></option>
	                  <%}else if ((status == "UPDATE") && (strState.length() == 0)){%>
	                    <option selected="true" value=""/></option>
	                  <%}else{%>
	                    <option value=""/></option>
	                  <%}%>
	                </html:select>
	                    <%}%>              
	                </td>          
	                <%}else if (strcntry.equalsIgnoreCase("CA")){%>
	                    <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.province"/></td>
	                    <td>
	                    <html:select name="RegisterIndividualForm" property="state" styleId="state" style="width:50px" tabindex="7" onchange="">
	                        <logic:iterate name="RegisterIndividualForm" property="provinceList" id="stateObject">
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
	                <td><html:text styleClass="inputs" name="RegisterIndividualForm" property="state" styleId="state" tabindex="7" style="width:50px" size="20" maxlength="60" onchange="ltrimField(this)"/></td>            
	                <%}%>    
				          </tr>
				          <tr>
				            <td class="Label" ><span style="color: #990000">* </span><bean:message key="label.registration.phone"/></td>
           				    <td colspan="3"><html:text styleClass="inputs" name="RegisterIndividualForm" styleId="phone"  property="phone" tabindex="7" size="20" maxlength="30" onchange="ValidPhoneNumber(RegisterIndividualForm.phone, RegisterIndividualForm.country)"/></td>
                            <td class="Label" width="20"><span style="color: #990000"></span><bean:message key="label.registration.extension"/></td>
                            <td width="32"><html:text styleClass="inputs" name="RegisterIndividualForm" property="phoneExtension" tabindex="7" size="5" maxlength="10" onchange=""/></td>
				          </tr>
				          <tr>
				            
					           <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.fax"/></td>
					            <td colspan="3">
					            <html:text styleClass="inputs" name="RegisterIndividualForm" property="fax" tabindex="7" size="20" maxlength="30" onchange="ValidPhoneNumber(RegisterIndividualForm.fax, RegisterIndividualForm.country)"/>
					            </td>
				          </tr>
				          
				           				<logic:equal property="status" name="RegisterIndividualForm" value="CREATE">  
				           				<tr>  
										            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.email"/></td>
										            <td rowspan="2" colspan="3">
										            <div z-index="1" style="position: relative;">
										            <html:text styleClass="inputs" name="RegisterIndividualForm" property="userName" tabindex="7" size="20" maxlength="80" onchange="ltrimField(this)"/>
										            </div>          
										            <div z-index="2" style="position: relative; font-size: 7pt; margin-top: 0px;">Important! You will use this e-mail address to access your account </div>
										            </td>
										    </tr>        
										    	<tr><td></td><td></td></tr>
            							</logic:equal>
            							
            							<tr>    
									            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.personType"/></td>
									            <td colspan="3"><html:select name="RegisterIndividualForm" property="personType" tabindex="7">
									                <html:option value="">- Select One -</html:option>
									                <html:option value="Business User"><bean:message key="label.registration.bus_user"/></html:option>
									                <html:option value="Academic User"><bean:message key="label.registration.aca_user"/></html:option>
									                <html:option value="Publisher"><bean:message key="label.registration.publisher"/></html:option>
									                <html:option value="Author or Creator"><bean:message key="label.registration.auth_creator"/></html:option>
									                <html:option value="Other"><bean:message key="label.registration.other"/></html:option>
									              </html:select></td> 
									              </tr>
            						
					
												<logic:equal property="status" name="RegisterIndividualForm" value="UPDATE">
       											 <logic:equal property="isRegisteredRlnkUser" name="RegisterIndividualForm"  value="true">
       														<tr><td class="Label"><bean:message key="label.registration.taxCertificate"/>#</td>
		            										<td colspan="3"><html:text styleClass="inputs" name="RegisterIndividualForm" property="taxId" tabindex="1" size="20" maxlength="80" onchange="ltrimField(this)"/></td>
		            										</tr>
       											</logic:equal>
       											</logic:equal>		          
								            <logic:equal property="status" name="RegisterIndividualForm" value="UPDATE">       
								          			  <tr><td width="250" class="Label"><span style="color: #990000"></span><bean:message key="label.registration.account"/>:</td>
								            		<td colspan="3" style="color: #000000;"><bean:write name="RegisterIndividualForm" property="account" /></td>
								            		 </tr>
								            		 <tr>
								            		 <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.mainContactLastName"/>:</td>
            										<td colspan="3" style="color: #000000;"><bean:write name="RegisterIndividualForm" property="mainContactLastName" /></td> 
								            		 </tr>
								            </logic:equal>
					
				          </table><!--  end right column table-->
          
          </td>
          </tr>
          <tr>
			
          
          </table>
          
          
          
          
          
    
    </table>
    
    <!--  END NEW TABLE BLOCK -->
    
                    
   
        <logic:equal name="RegisterIndividualForm" property="isLoggedIn" value="FALSE">
        
        <div class="horiz-rule"></div>
        
        <table width="664" style="margin-left: 12px">
          <tr>
            <td colspan="4"><h4 style="font-size: 8pt; color: #336699; margin-top: 3px; margin-bottom: 0px;">Account password </h4>
              <p style="margin-top: 4px; margin-bottom: 12px;">Your password must be at least 6 characters. We recommend using upper and <br />
              lowercase characters and at least one digit.</p>              
            </td>
          </tr>
          <tr>
            <td width="168" class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.password"/></td>
            <td width="194">
            <html:password styleClass="inputs" name="RegisterIndividualForm" property="password" tabindex="8" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td width="80" class="Label">&nbsp;</td>
            <td width="252">&nbsp;</td>
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.confirmPassword"/></td>
            <td>
            <html:password styleClass="inputs" name="RegisterIndividualForm" property="confirmPassword" tabindex="8" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          
          <tr>
            <td colspan="4">&nbsp;</td>
          </tr>
          
        </table>
        </logic:equal>
        
        <logic:equal name="RegisterIndividualForm" property="status" value="UPDATE">
            <table width="664" style="margin-left: 12px">
                <tr>
                    <logic:equal name="RegisterIndividualForm" property="alwaysInvoice" value="T">
                        <td><html:checkbox  name="RegisterIndividualForm" value="T" property="alwaysInvoice" onclick="CopyAlwaysInvoice(this)" style="margin-right:-2px;" /></td>
                        <td>Always invoice me<br/></br>
                            (If you select this option, you will skip the Payment Information step when you purchase  <br>
                            permission through copyright.com and your account will be automatically invoiced.)</td><td></td>        
                        
                    </logic:equal>
    
                    <logic:equal name="RegisterIndividualForm" property="alwaysInvoice" value="F">
                    <!-- <p style="margin-top: 0px; margin-bottom: 12px;">  -->
                    <td><html:checkbox  name="RegisterIndividualForm" property="alwaysInvoice" onclick="CopyAlwaysInvoice(this)" style="margin-right:-2px;" /></td>
                    <td>Always invoice me<br/><br/>
                        (If you select this option, you will skip the Payment Information step when you purchase  <br>
                        permission through copyright.com and your account will be automatically invoiced.)</td><td></td>
            
                    
                    </logic:equal>
                </tr>
           <tr><td colspan="3">&nbsp;</td></tr>
                         <tr>
                  <logic:equal name="RegisterIndividualForm" property="autoInvoiceSpecialOrder" value="T">
                      <td><html:checkbox  name="RegisterIndividualForm" value="T" property="autoInvoiceSpecialOrder" onclick="CopyAutoInvoiceSpecialOrder(this)" style="margin-right:-2px;" /></td>
                       <td>Always automatically accept special orders when they are granted<br/><br/>
                      (If you select this option, you will skip the option to accept or decline a special order when a rightsholder <br>
                      grants permission and your account will be automatically billed.)</td><td></td>     
                      
                  </logic:equal>
  
                  <logic:equal name="RegisterIndividualForm" property="autoInvoiceSpecialOrder" value="F">
                  <!-- <p style="margin-top: 0px; margin-bottom: 12px;">  -->
                  <td><html:checkbox  name="RegisterIndividualForm" property="autoInvoiceSpecialOrder" onclick="CopyAutoInvoiceSpecialOrder(this)" style="margin-right:-2px;" /></td>
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
            <a href="javascript:sbmtPage()" id="submitBtnLink" tabindex="8"><html:img src="/media/images/submit_button.gif" onclick="this.disabled=true;" styleId="submitBtn" alt="Register" title="Register" /></a>
            </td>
          </tr>
          
          
        </table>
      
     <!-- <p>&nbsp;</p> -->
    
    
    <!-- ************************************************************************************** -->
    
 <!--   </div> -->

</body>
</html:form>

<!-- Webtrends tags for capturing scenarios -->
<logic:equal name="RegisterIndividualForm" property="isLoggedIn" value="FALSE">
    <%
        Object isRedisplayObj = request.getAttribute( "isRedisplay" );
        if( isRedisplayObj == null || !(((Boolean)isRedisplayObj).booleanValue()) )
        {
    %>
            <META name="WT.si_n" content="Indv_reg">
            <META name="WT.si_p" content="Indv_reg_page">
    <%
        }
    %>
</logic:equal>
<!-- end Webtrends tags -->
