<%@ page import="com.copyright.ccc.business.security.SecurityUtils" %>

<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>

<!-- <link href="<html:rewrite page="/resources/commerce/css/progressMeter.css"/>" rel="stylesheet" type="text/css" /> -->
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>
<!-- ************************************ M A N U A L   S T Y L E S H E E T ********************* -->
<style type="text/css">
    form#LogIn p.Button { text-align: right; }
    table#LogInForm { text-align: center; width: 500px; margin: 0px auto; }
    table#LogInForm td.Left { text-align: left; vertical-align: top; padding-right: 20px; width: 40%; }
    table#LogInForm td.verticalline { 
        text-align: left;
        border-left: 1px solid #E7E7E7; 
        padding-left: 20px; 
        width: 60%; }
    p {font-family:verdana,sans-serif; font-size:11px; color:#666666; margin:10px 0px 0px 0px;}
    p.dottedLine {margin:-5px 0px -10px 0px; border-bottom:2px #500000 dotted;}
    a {color:#990000; text-decoration:none;}
    a:visited {color:#000000; text-decoration:underline;}
    a:hover {color:#990000; text-decoration:underline;}
    li {margin:0px 0px 10px 0px;}
    li.nospace {margin:0px 0px 0px 0px;}
    li.halfspace {margin:0px 0px 5px 0px;}
    li {font-family:verdana,sans-serif; font-size:11px; color:#666666;}
</style>
<!-- ************************************ E N D   S T Y L E S H E E T *************************** -->

<bean:define id="loginForm" name="loginForm" type="com.copyright.ccc.web.forms.LoginForm" />

<center>

<logic:messagesPresent message="true">
    <div class="error"><html:errors /></div>
</logic:messagesPresent>




<% if ( SecurityUtils.hasSessionBeenInvalidated( request ) &&
    ! SecurityUtils.hasSessionBeenInvalidatedByApplication( request ) ) { %>
    <div class="error">
        You have been automatically logged out.  You may login below or use the site navigation to continue using copyright.com.
    </div>
<% } %>

<p align="left"><b>&nbsp;&nbsp;LOG IN</b></p>
<p class="dottedLine">&#160;</p>

<blockquote>

    <table id="LogInForm"
    <logic:present name="loginForm" property="autoLoginForward">
    style="display:none"
    </logic:present>
    >
                    <%
// First time Rightslink login screen message
if(request.getSession().getAttribute("RIGHTSLINK_LOGIN_MESSAGE")!=null){
out.println("<tr><td class='Left' colspan='2'><p><strong>" + request.getSession().getAttribute( WebConstants.SessionKeys.RIGHTSLINK_LOGIN_MESSAGE) +"</strong></p></td></tr>");
 request.getSession().setAttribute( WebConstants.SessionKeys.RIGHTSLINK_LOGIN_MESSAGE,"");
}
 %>
        <tr>
            <td class="Left" valign="top">
                
                <form action="j_security_check" method="POST" id="frm">
                    <p>
                        <strong>E-mail Address:</strong>
                        <br />
                        <input type="text" id="LoginUserName" name="j_username" value="<bean:write name="loginForm" property="autoLoginUsername"/>" size="30" maxlength="80" tabindex="1" onchange="ValidateUserName('E-Mail Address','LoginUserName')"/>
                    </p>
                    <p>
                        <strong>Password:</strong> (case sensitive)
                        <br />
                        <input type="password" name="j_password" value="<bean:write name="loginForm" property="autoLoginPassword"/>" size="30" maxlength="40" tabindex="2" autocomplete="off" />
                    </p>
                    <p class="Button">
                        <html:submit value="Log In" tabindex="3"/>
                    </p>
                    
                    <input type="hidden" name="autoLogin" value="false" id="autoLogin"/>

                </form>
                
                <p class="dottedLine">&#160;</p>
                <p>
                    <strong>Forgot your password?</strong>
                    <br />
                    Enter your account e-mail address below.  
                    We will send you instructions to reset your password.<br />
                </p>
                
                <html:form action="forgotPassword">
                    <input type="text" name="forgotPasswordUsername" id="forgotPasswordUsername" tabindex="4" size="30" onchange="ValidateUserName('E-Mail Address','forgotPasswordUsername')"/>
                    <p class="Button">
                        <html:submit value="Send" tabindex="5"/>
                    </p>
                </html:form>
            </td>
            
            <td class="verticalline" valign="top">
                <p>
                    <strong>First Time User?</strong>
                    <br />
                    <a href="<html:rewrite page='/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/welcome_to_copyrightcom.html' />">Learn more</a> about how we can help you.
                </p>
                <p>
                    <strong>Create a Pay-Per-Use Account</strong>
                    <br />
                    To get permission to use a specific title, create an account for:
                </p>
                    
                    <ul style="list-style:url(/media/images/arrow_red.gif);">
                        <li><a href="displayOrgRegistration.do">Create a new account</a></li>
                    </ul>
                
                <p>
                    <strong>Are you a RightsLink Customer?</strong>
                </p>
            
                <ul style="list-style:url(/media/images/arrow_red.gif);">
                    <li><a href="<html:rewrite page='/content/cc3/en/toolbar/productsAndSolutions/rightslink.html' />">What is RightsLink?</a></li>
                    <li><a href="http://myaccount.copyright.com" target="_blank">Log in to your account</a></li>
                 </ul>
                 
                 <p>
                	<strong>Did you know?</strong>
                	<br/>
                	CCC's RightsLink and copyright.com services are now integrated. Find
                	out how you can take advantage! 
               <util:contextualHelp helpId="34" rollover="false" styleId="1">
                        Learn More&hellip;
         	   </util:contextualHelp>
         	   </p>
            </td>
        </tr>
    </table>
    
</blockquote>


</center>

<logic:present name="loginForm" property="autoLoginForward">
    <script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/progressMeter.js"/>"></script>
    
    <h2>Your information is being processed...</h2>
    
    <div class="meterBar" id="meterBar"></div>
    <div class="progressBar" id="progressBar"></div>
    
    <script type="text/javascript">
        document.getElementById('autoLogin').value = "false";
        
        startProgressBar( 'meterBar', 'progressBar', 20, 200, true );
        setTimeout("document.getElementById('frm').submit()", 3000 );
    </script>
</logic:present>

<%
    loginForm.setAutoLoginPassword( null );
    loginForm.setAutoLoginUsername( null );
%>
<!-- Webtrends tags for capturing scenarios -->
    <META name="WT.si_n" content="Checkout">
    <META name="WT.si_x" content="5">
<!-- end Webtrends tags -->