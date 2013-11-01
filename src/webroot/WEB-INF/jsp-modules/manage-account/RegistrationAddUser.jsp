<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page errorPage="/jspError.do" %>

<bean:define id="RegisterAddUserForm" name="RegisterAddUserForm" type="com.copyright.ccc.web.forms.RegisterAddUserForm"/>

<%
String accountNumber = "";
String mainLastName = "";
String status = "";

//COMMENT for now....
accountNumber = RegisterAddUserForm.getAccount();
mainLastName = RegisterAddUserForm.getMainContactLastName();
status = RegisterAddUserForm.getStatus();

if (accountNumber == null)
{
    accountNumber = "0";
}

%>

<script type="text/javascript">
var submitBtnClick = "F";

function sbmtPage() {
  if (submitBtnClick == "F")
     {
         //alert("Sub clicked.....");
         var myFrm = document.getElementById('frm');
         submitBtnClick = "T";
         document.getElementById("submitBtnLink").href = "#";
         myFrm.submit();
     }
}

function submitRegisterAddUser() {
  if (document.RegisterAddUserForm.submitFlag.value == "T")
  {
	document.RegisterAddUserForm.submitFlag.value = "F";
	document.forms[0].action.value = "registerAddUser.do";
  	return true;
  	//document.forms[0].submit();
  }
  else
  {
	document.RegisterAddUserForm.submitFlag.value = "F";
      	return false;
  }
}
function submitUpdateRegistration() {
  document.forms[0].action.value = "updateAddUser.do";
  return true;
  //document.forms[0].submit();
}
function repaintPage()
{
    document.getElementById("RegisterAddUserForm").action = "displayAddUserRegistration.do";
    document.getElementById("RegisterAddUserForm").submit();
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
   // return (isPositiveInteger(s));
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

//  This function checks a domestic or foreign phone number for
//  a minimal validity.

function ValidPhoneNumber(fld)
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
    
    		strNum = stripCharsNotInBag(fld.value, validWorldPhoneChars);
            strNum = stripInitialCharsInBag(strNum, "0");
            if (isInternationalPhoneNumber(strNum))
               // fld.value = "011-" + strNum;
               fld.value = strNum;
            else
            {
                if (displayError)
                {
                    alert("Your " + strPhoneOrPhax + " Number should consist of your country code, \ncity code and telephone number.");
                    fld.value = "";
                    fld.focus();
                    fld.select();
                }
                return false;
            }
    
    return true;
}

function CopyAlwaysInvoice(fld)
{
    if (fld.checked)
    {
        document.RegisterAddUserForm.alwaysInvoice.value = "T";
        document.RegisterAddUserForm.alwaysInvoiceFlag.value = "T";  
    }
    else
    {
        document.RegisterAddUserForm.alwaysInvoice.value = "F";
        document.RegisterAddUserForm.alwaysInvoiceFlag.value = "F"; 
    }



}

function CopyAutoInvoiceSpecialOrder(fld)
{
    if (fld.checked)
    {
        document.RegisterAddUserForm.autoInvoiceSpecialOrder.value = "T";
        document.RegisterAddUserForm.autoInvoiceSpecialOrderFlag.value = "T";  
    }
    else
    {
        document.RegisterAddUserForm.autoInvoiceSpecialOrder.value = "F";
        document.RegisterAddUserForm.autoInvoiceSpecialOrderFlag.value = "F"; 
    }

}
</script>

<html:form action="/registerAddUser" method="POST" onsubmit="submitRegisterAddUser();" styleId="frm">
<html:hidden name="RegisterAddUserForm" property="submitFlag" value="T"/>
<html:hidden name="RegisterAddUserForm" property="alwaysInvoiceFlag"/>
<html:hidden name="RegisterAddUserForm" property="autoInvoiceSpecialOrderFlag"/>

 
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
   
      <h2>ADD AN ADDITIONAL USER TO YOUR ACCOUNT</h2>
      <p style="text-align: right; color: #990000; margin-top: -20px">* indicates required field</p>
      <p style="position:relative; width:728px; margin: -5px 0px 0px 0px; border-bottom:2px #990000 dotted;z-index:-1000">&#160;</p>
      <%
              }
            else
              {
                %>
 <!--   <div id="onecolumn"> -->
 
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
            <td width="140"><h4 style="font-size: 8pt; color:  #000000; margin-top: 5px; margin-bottom: 0px;">Current contact information</h4></td>
            <td>(<a href="/content/cc3/en/tools/footer/privacypolicy.html" target="_blank" style="color: #336699;"><bean:message key="label.registration.privacyPolicy" /></a>.)</td>
            <td></td><td></td>  
          </tr>
          <%
              }
           %>
         <tr>
            <td class="Label"><bean:message key="label.registration.title" /></td>
            <td width="194">
            <html:select name="RegisterAddUserForm" property="title" tabindex="1">
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
            <td style="color: #000000;"><%=accountNumber%></td>
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
            <td><html:text styleClass="inputs" name="RegisterAddUserForm" property="firstName" tabindex="1" size="20" maxlength="15" onchange="ltrimField(this)"/>
            </td>
          <%
            if (status == "UPDATE")
              {
               %>
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.mainContactLastName"/>:</td>
            <td style="color: #000000;"><%=mainLastName%></td>
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
            <html:text styleClass="inputs" name="RegisterAddUserForm" property="middleName" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td></td>
            <td></td> 
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.lastName"/></td>
            <td>
            <html:text styleClass="inputs" name="RegisterAddUserForm" property="lastName" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td></td>
            <td></td>  
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.phone"/></td>
            <td width="252">
            <html:text styleClass="inputs" name="RegisterAddUserForm" property="phone" tabindex="1" size="20" maxlength="30" onchange="ValidPhoneNumber(RegisterAddUserForm.phone)"/>
            </td>
            <td class="Label" align="left" width="20"><span style="color: #990000"></span><bean:message key="label.registration.extension"/></td>
            <td align="left" width="32">
            <html:text styleClass="inputs" name="RegisterAddUserForm" property="phoneExtension" tabindex="1" size="5" maxlength="10" onchange=""/>
            </td>
            
            <td width="100"></td>
            <td width="100"></td>
            
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000"></span><bean:message key="label.registration.fax"/></td>
            <td width="252">
            <html:text styleClass="inputs" name="RegisterAddUserForm" property="fax" tabindex="1" size="20" maxlength="30" onchange="ValidPhoneNumber(RegisterAddUserForm.fax)"/>
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
            <td rowspan="2" width="200">
            <div z-index="1" style="position: relative;">
            <html:text styleClass="inputs" name="RegisterAddUserForm" property="userName" tabindex="1" size="20" maxlength="80" onchange="ltrimField(this)"/>
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
            <td><html:select name="RegisterAddUserForm" property="personType" tabindex="1">
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
           <logic:equal property="status" name="RegisterAddUserForm" value="UPDATE">
       			<logic:equal property="isRegisteredRlnkUser" name="RegisterAddUserForm"  value="true">
       			<td class="Label"><bean:message key="label.registration.taxCertificate"/>#</td>
		            <td>
		              <html:text styleClass="inputs" name="RegisterAddUserForm" property="taxId" tabindex="1" size="20" maxlength="80" onchange="ltrimField(this)"/>
		            </td>
       		</logic:equal>
       	</logic:equal>
          <logic:equal property="status" name="RegisterAddUserForm" value="CREATE">
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
          </logic:equal>    
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
            <td colspan="4"><h4 style="font-size: 8pt; color: #336699; margin-top: 3px; margin-bottom: 0px;">Existing account Information</h4>
                <p style="margin-top: 4px; margin-bottom: 12px;">Your account number is displayed in the "Update address and billing information"<br/>
screen in "Manage Account". It can also be found in order confirmations, invoices and
<br/>within account activity reports.
</p>
            </td>
          </tr>
          <tr></tr>
          <tr></tr>
          <tr>
            <td width="168" class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.account"/></td>
            <td width="194">
            <html:text styleClass="inputs" name="RegisterAddUserForm" property="account" tabindex="1" size="20" maxlength="15" onchange="ltrimField(this)"/>
            </td>
            <td width="80" class="Label">&nbsp;</td>
            <td width="252">&nbsp;</td>
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.mainContactLastName"/></td>
            <td>
            <html:text styleClass="inputs" name="RegisterAddUserForm" property="mainContactLastName" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td class="Label">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
          <td>
          <div z-index="2" style="position: relative; font-size: 7pt; margin-top: 0px;">(Person who originally created the account.) </div>
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
            <html:password styleClass="inputs" name="RegisterAddUserForm" property="password" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
            </td>
            <td width="80" class="Label">&nbsp;</td>
            <td width="252">&nbsp;</td>
          </tr>
          <tr>
            <td class="Label"><span style="color: #990000">* </span><bean:message key="label.registration.confirmPassword"/></td>
            <td>
            <html:password styleClass="inputs" name="RegisterAddUserForm" property="confirmPassword" tabindex="1" size="20" maxlength="20" onchange="ltrimField(this)"/>
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
        
        
    <logic:equal name="RegisterAddUserForm" property="status" value="UPDATE">
            <table width="664" style="margin-left: 12px">
                <tr>
                    <logic:equal name="RegisterAddUserForm" property="alwaysInvoice" value="T">
                        <td><html:checkbox  name="RegisterAddUserForm" value="T" property="alwaysInvoice" onclick="CopyAlwaysInvoice(this)" style="margin-right:-2px;" /></td>
                        <td>Always invoice me<br/></br>
                            (If you select this option, you will skip the Payment Information step when you purchase  <br>
                            permission through copyright.com and your account will be automatically invoiced.)</td><td></td>        
                        
                    </logic:equal>
    
                    <logic:equal name="RegisterAddUserForm" property="alwaysInvoice" value="F">
                    <!-- <p style="margin-top: 0px; margin-bottom: 12px;">  -->
                    <td><html:checkbox  name="RegisterAddUserForm" property="alwaysInvoice" onclick="CopyAlwaysInvoice(this)" style="margin-right:-2px;" /></td>
                    <td>Always invoice me<br/><br/>
                        (If you select this option, you will skip the Payment Information step when you purchase  <br>
                        permission through copyright.com and your account will be automatically invoiced.)</td><td></td>
            
                    
                    </logic:equal>
                </tr>
                <tr><td colspan="3">&nbsp;</td></tr>
                          <tr>
                  <logic:equal name="RegisterAddUserForm" property="autoInvoiceSpecialOrder" value="T">
                      <td><html:checkbox  name="RegisterAddUserForm" value="T" property="autoInvoiceSpecialOrder" onclick="CopyAutoInvoiceSpecialOrder(this)" style="margin-right:-2px;" /></td>
                       <td>Always automatically accept special orders when they are granted<br/><br/>
                      (If you select this option, you will skip the option to accept or decline a special order when a rightsholder <br>
                      grants permission and your account will be automatically billed.)</td><td></td>     
                      
                  </logic:equal>
  
                  <logic:equal name="RegisterAddUserForm" property="autoInvoiceSpecialOrder" value="F">
                  <!-- <p style="margin-top: 0px; margin-bottom: 12px;">  -->
                  <td><html:checkbox  name="RegisterAddUserForm" property="autoInvoiceSpecialOrder" onclick="CopyAutoInvoiceSpecialOrder(this)" style="margin-right:-2px;" /></td>
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
          
</body>
</html:form>
