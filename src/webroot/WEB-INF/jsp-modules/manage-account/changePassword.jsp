<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/cc2-security.tld" prefix="security" %>

<%@page import="com.copyright.ccc.business.security.UserContextService"%>

<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>

<logic:messagesPresent message="true">
    <div class="error"><html:errors /></div>
</logic:messagesPresent>

<script type="text/javascript">
	function changePassword() {
	    var password = document.getElementById("password");
	    var passwordConfirmation = document.getElementById("passwordConfirmation");
	    
            if ((!password)|| (password.value.length==0)) {
               alert("Password is required");
               return;
            }
            else if (password.value.length < 6)
            {
            	alert("A password must be a least 6 characters");
            	return;
            }
            else if ((!passwordConfirmation) || (passwordConfirmation.value.length==0)) {
               alert("The Confirmation of the Password is required");
               return;
            }
            else if (passwordConfirmation.value.length < 6)
            {
            	alert("The password confirmation does not match the password");
            	return;
            }
            if (typeof document.getElementById("changePasswordActionForm").enforcePwdChg=="undefined"){
            	document.getElementById("changePasswordActionForm").action="/changePassword.do";
            }
            else {
                document.getElementById("changePasswordActionForm").action="/changePassword.do?enforcePwdChg="
					+document.getElementById("changePasswordActionForm").enforcePwdChg.checked;
            }
		document.getElementById("changePasswordActionForm").submit();
	}
	
	var submitBtnClick = "F";
	
	function sbmtPage() {
		  if (submitBtnClick == "F")
		     {
		         //alert("Sub clicked.....");
		         var myFrm = document.getElementById('updateEmailAddressForm');
		         submitBtnClick = "T";
		         document.getElementById("emailUpdateBtn").disabled = true;
		         myFrm.submit();
		     }
	}
</script>

<h2>UPDATE E-MAIL ADDRESS OR PASSWORD</h2>

<h3>Update Password</h3>

<html:form action="changePassword" method="POST" styleId="changePasswordActionForm">
<table width="442" style="margin-left: 12px">
    <tr>
        <td colspan="2">
        Your password must be at least 6 characters. We recommend using<br />
        upper and lowercase characters and at least one digit.
        </td>
    </tr>
    <tr>
        <td>New Password</td><td><html:password styleId="password" property="password" size="20" maxlength="20"/></td>
    </tr>
    <tr>
        <td>Re-enter Password</td><td><html:password styleId="passwordConfirmation" property="passwordConfirmation" size="20" maxlength="20"/></td>
    </tr>
    <tr>
        <!--  td colspan="2" align="right"><input type="submit" value="Update Password"/></td-->
<security:ifUserEmulating>
        <td width="45%">
            <input type="checkbox" name="enforcePwdChg" checked="checked" />
            		 Enforce Password Change
       </td>
</security:ifUserEmulating>
        <td colspan="2" align="right"><html:button property="updatePassword" value="Update Password" onclick="changePassword()"/></td>
    </tr>
</table>
</html:form>

<% if(!UserContextService.getAuthenticatedAppUser().getEnforcePwdChg())
{ %>
<h3>Update e-mail Address </h3>


<html:form action="updateEmailAddress?operation=updateEmail" method="POST" styleId="updateEmailAddressForm">
<html:hidden property="updateable"/>
<html:hidden property="samePartyID"/>
<logic:equal name="updateEmailAddressForm" property="updateable" value="true">
<table width="442" style="margin-left: 12px">
    <tr>
        <td>New E-mail Address</td><td><html:text property="emailAddress" styleId="LoginUserName" size="20" maxlength="80" onchange="ValidateUserName('E-Mail Address','LoginUserName')"/></td>
    </tr>
    <tr>
        <td colspan="2" align="right"><input type="button" id="emailUpdateBtn"
                                             value="Update E-mail Address" onclick="sbmtPage();"/></td>
    </tr>
</table>
<br/><br/>
   	<logic:equal name="updateEmailAddressForm" property="rlUser" value="true">
		<logic:equal name="updateEmailAddressForm" property="samePartyID" value="true">
			<b>If you use our Rightslink service,</b> updating your e-mail address on copyright.com<br/>will update your Rightslink UserID and your Rightslink e-mail address.
		</logic:equal>
		<logic:equal name="updateEmailAddressForm" property="samePartyID" value="false">
			<b>If you use our Rightslink service,</b> updating your e-mail address on copyright.com<br/>will update your Rightslink UserID but not your Rightslink e-mail address.
		</logic:equal>
	</logic:equal>
</logic:equal>
<logic:equal name="updateEmailAddressForm" property="updateable" value="false">
    <br/><br/>
    <table width="442" style="margin-left: 12px">
    <tr>
        <td>We're sorry: it's not possible to update your username/e-mail address right now. Please try again later.</td>
    </tr>
    </table>
</logic:equal>
</html:form>
<%} %>
