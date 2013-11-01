<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="/jspError.do" %>

<bean:define id="RegisterGPOUserForm" name="RegisterGPOUserForm" type="com.copyright.ccc.web.forms.RegisterGPOUserForm"/>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">


<%
String accountNumber = "";
String mainLastName = "";
String status = "";

//COMMENT for now....
accountNumber = RegisterGPOUserForm.getAccount();
mainLastName = RegisterGPOUserForm.getMainContactLastName();
status = RegisterGPOUserForm.getStatus();

if (accountNumber == null)
{
    accountNumber = "0";
}

%>

<script type="text/javascript">
var submitBtnClick = "F";

function submitRegisterGPOUser() {
    //alert("Submit Outside");
  var myFrm = document.getElementById('frm');
  
  if (submitBtnClick == "F")
     {
        submitBtnClick = "T";
        if (document.RegisterGPOUserForm.submitFlag.value == "T")
        {
            //alert("submit inside");
            document.RegisterGPOUserForm.submitFlag.value = "F";
            document.forms[0].action.value = "showGPOAgreement";
            //return true;
            //document.forms[0].submit();
            document.getElementById("submitBtnLink").href = "#";
            myFrm.submit();
        }
        else
        {
            document.RegisterGPOUserForm.submitFlag.value = "F";
            return false;

        }
    }
}
function submitUpdateRegistration() {
  document.forms[0].action.value = "updateGPOUser";
  return true;
  //document.forms[0].submit();
}
function repaintPage()
{
    document.getElementById("RegisterGPOUserForm").action = "redisplayRegistration";
    document.getElementById("RegisterGPOUserForm").submit();
}
//This function loads the page depending on the Type of Acct chosen

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
    return (isPositiveInteger(s));
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
    if (fld.name.search("FAX") > -1) strPhoneOrPhax = "Fax"
    
    //  Strip out any non-numeric characters.  Also remove
    //  leading 0 or 1.
    
    strNum = stripCharsNotInBag(fld.value, digits);
    strNum = stripInitialCharsInBag(strNum, "01");

    switch(fmt.options[fmt.selectedIndex].value)
    {
        case "CANADA":
        case "PUERTO RICO":
        case "UNITED STATES":
            //  All U.S.A. and Canadian numbers follow the 3-3-4 format for
            //  telephone numbers.
            
            if (isUSPhoneNumber(strNum))
                fld.value = reformat(strNum, "1-", 3, "-", 3, "-", 4);
            else
            {
                if (displayError)
                {
                    //fld.focus();
                    //alert("in 10 digit TEST fld Name: " + fld.name);
                    alert("Your " + strPhoneOrPhax + " Number should contain 10 digits\nconsisting of your area code, exchange and number.")
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
            
            if (isInternationalPhoneNumber(strNum))
                fld.value = "011-" + strNum;
            else
            {
                if (displayError)
                {
                    fld.focus();
                    alert("Your international " + strPhoneOrPhax + " Number should consist of your country code, \ncity code and telephone number.");
                    fld.select();
                }
                return false;
            }
            break;
    }
    
    return true;
}
</script>

<html:form action="/showGPOAgreement" method="POST" onsubmit="submitRegisterGPOUser();" styleId="frm">
<html:hidden name="RegisterGPOUserForm" property="submitFlag" value="T"/>
 
<body>

<!--
<div id="content">
  
  <div id="none">
  
  -->
  
    <!--top Line-->
    <!-- <div id="topLine">&#160;</div> -->
    
    <%
        if (status == "CREATE")
            {
            %>
   <!-- <div id="onecolumn"> -->
      <h2>CREATE A GRANT PERMISSION ONLINE ACCOUNT</h2>
      <p style="text-align: right; color: #990000; margin-top: -20px">* indicates required field</p>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
      <%
              }
            else
              {
                %>
    <div id="onecolumn">
      <h2>UPDATE ACCOUNT INFORMATION</h2>
      <p style="text-align: right; color: #990000; margin-top: -20px">* indicates required field</p>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
      <%
              }
           %>
      
        <table width="664" style="margin-left: 12px">
          <%
          if (status == "CREATE")
              {
            %>
          <tr>
            <td width="140"><h4 style="font-size: 8pt; color: #336699; margin-top: 5px; margin-bottom: 0px;">Contact information</h4></td>
            <td>(<a href="/content/cc3/en/tools/footer/privacypolicy.html" target="_blank" style="color: #336699; margin-top: 5px; margin-bottom: 0px;"><bean:message key="label.registration.privacyPolicy" /></a>.)</td>
            <td></td><td></td>
          </tr>
          <%
              }
            else
              {
                %>
          <tr>
            <td width="140"><h4 style="font-size: 8pt; color: #336699; margin-top: 5px; margin-bottom: 0px;">Current contact information</h4></td>
              <td>(<a href="/content/cc3/en/tools/footer/privacypolicy.html" target="_blank" style="color: #336699; margin-top: 5px; margin-bottom: 0px;"><bean:message key="label.registration.privacyPolicy" /></a>.)</td>
              <td></td><td></td>
          </tr>
          <%
              }
           %>
          <tr>
            <td class="Label"><bean:message key="label.registration.title" /></td>
            <td width="194">
            <html:select name="RegisterGPOUserForm" property="title" tabindex="1">
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
           <%
            if (status == "UPDATE")
              {
               %>
            <td width="250" class="Label"><span style="color: #990000"></span><bean:message key="label.registration.account"/>:</td>
            <td style="color: #336699;"><%=accountNumber%></td>
            <%
              }
            else
              {
                %>
            <td></td>
            <td></td>
          <%
              }
                %>             
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.firstName"/></td>
            <td><html:text styleClass="inputs" name="RegisterGPOUserForm" property="firstName" tabindex="1" size="20" maxlength="15" onchange="ltrimField(this)"/>
            </td>
          <%
            if (status == "UPDATE")
              {
               %>
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.mainContactLastName"/>:</td>
            <td style="color: #336699;"><%=mainLastName%></td>
          <%
              }
            else
              {
                %>
            <td></td>
            <td></td>
          <%
              }
                %>    
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.middleName"/></td>
            <td>
            <html:text styleClass="inputs" name="RegisterGPOUserForm" property="middleName" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td></td>
            <td></td> 
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.lastName"/></td>
            <td>
            <html:text styleClass="inputs" name="RegisterGPOUserForm" property="lastName" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td></td>
            <td></td>  
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.phone"/></td>
            <td width="252">
            <html:text styleClass="inputs" name="RegisterGPOUserForm" property="phone" tabindex="1" size="20" maxlength="30" onchange=""/>
            </td>
            <td class="Label" align="left" width="20"><span style="color: #990000"></span><bean:message key="label.registration.extension"/></td>
            <td align="left" width="32">
            <html:text styleClass="inputs" name="RegisterGPOUserForm" property="phoneExtension" tabindex="1" size="5" maxlength="10" onchange=""/>
            </td>
            
            <td width="100"></td>
            <td width="100"></td>
            
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.fax"/></td>
            <td width="252">
            <html:text styleClass="inputs" name="RegisterGPOUserForm" property="fax" tabindex="1" size="20" maxlength="30" onchange=""/>
            </td>
            <td></td>
            <td></td> 
          </tr>
          
          <%
            if (status == "CREATE")
             {
               %>
            <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.email"/></td>
            <td rowspan="2" width="252">
            <div z-index="1" style="position: relative;">
            <html:text styleClass="inputs" name="RegisterGPOUserForm" property="userName" tabindex="1" size="20" maxlength="80" onchange="ltrimField(this)"/>
            </div>          
            <div z-index="2" style="position: relative; font-size: 7pt; margin-top: 0px;">Important! You will use this e-mail address to access your account </div>
            </td>
            <td></td>
            <td></td> 
            
            <td>&nbsp;</td>
          </tr>
            <%
              }
            %>     
          
          <tr>
          </tr>
          
          
          
          <tr>
                      
          <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.personType"/></td>
            <td><html:select name="RegisterGPOUserForm" property="personType" tabindex="1">
                <html:option value="">- Select One -</html:option>
                <html:option value="Business User"><bean:message key="label.registration.bus_user"/></html:option>
                <html:option value="Academic User"><bean:message key="label.registration.aca_user"/></html:option>
                <html:option value="Publisher"><bean:message key="label.registration.publisher"/></html:option>
                <html:option value="Author or Creator"><bean:message key="label.registration.auth_creator"/></html:option>
                <html:option value="Other"><bean:message key="label.registration.other"/></html:option>
              </html:select></td>
  
            <td></td>
            <td></td> 
          </tr>
          
          
          <tr>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          
          <!--
          <tr>
            <td colspan="4"><p class="boxLine">&nbsp;</p></td>
          </tr>
          -->
          
        </table>
        
        <div class="horiz-rule"></div>
        
        <%
            if (status == "CREATE")
             {
               %>
        <table width="664" style="margin-left: 12px">
          <tr>
            <td colspan="4"><h4 style="font-size: 8pt; color: #336699; margin-top: 3px; margin-bottom: 0px;">Rightsholder account Information</h4>
            <p style="margin-top: 4px; margin-bottom: 12px;">If you don't know your account number or primary contact last name, please <br /> contact
                  a Customer Relations Representative at 978-646-2800, Monday-Friday <br /> 8:00 AM to 5:30 PM (Eastern), or by e-mail at
                      <a href="mailto:rightsholders@copyright.com"> rightsholders@copyright.com.</a></p>
            </td>
          </tr>
          <tr></tr>
          <tr></tr>
          <tr>
            <td width="168" class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.account"/></td>
            <td width="194">
            <html:text styleClass="inputs" name="RegisterGPOUserForm" property="account" tabindex="1" size="20" maxlength="15" onchange="ltrimField(this)"/>
            </td>
            <td width="80" class="Label">&nbsp;</td>
            <td width="252">&nbsp;</td>
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.mainContactLastName"/></td>
            <td>
            <html:text styleClass="inputs" name="RegisterGPOUserForm" property="mainContactLastName" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
          <td>
          <div z-index="2" style="position: relative; font-size: 7pt; margin-top: 0px;">(Your organization's rights and permissions contact.) </div>
          </td>
          <td></td>
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
        
        <div class="horiz-rule"></div>
                
          <tr>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <!--
          <tr>
            <td colspan="4"><p class="boxLine">&nbsp;</p></td>
          </tr>
          -->
        </table>
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
            <html:password styleClass="inputs" name="RegisterGPOUserForm" property="password" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td width="80" class="Label">&nbsp;</td>
            <td width="252">&nbsp;</td>
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.confirmPassword"/></td>
            <td>
            <html:password styleClass="inputs" name="RegisterGPOUserForm" property="confirmPassword" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
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
        <%
              }
               %>
        <table width="664" style="margin-left: 12px">
          
          
  <!--
          <tr>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td colspan="4"></td>
          </tr>
          <tr>
            <td colspan="4"></td>
          </tr>
          <tr>
            <td colspan="4" align="center">&nbsp;</td>
          </tr>
          -->
          <tr>
            <td colspan="4" align="center">  
        <%--   <html:img src="/media/images/submit_button.gif" alt="Register" title="Register" onclick="submitRegisterGPOUser();"/> --%>
            <%-- <html:img src="/media/images/submit_button.gif" alt="Register" title="Register" onclick="submitRegisterGPOUser();this.disabled=true;"/> --%>
            <a href="javascript:submitRegisterGPOUser()" id="submitBtnLink" tabindex="8"><html:img src="/media/images/submit_button.gif" onclick="this.disabled=true;" styleId="submitBtn" alt="Register" title="Register" /></a>
            </td>
          </tr>
          
          <!--
          <tr>
            <td colspan="4" align="center">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="4"><p class="boxLine">&nbsp;</p></td>
          </tr>
          -->
        </table>
      
     <!--
      <p>&nbsp;</p>
    </div>
  </div>
</div>
-->

</body>
</html:form>
