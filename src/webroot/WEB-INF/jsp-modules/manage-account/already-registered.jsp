<%@ page language="java" 
import="java.io.*"
import="java.util.*"
import="com.copyright.ccc.web.forms.LoginForm"
import="org.apache.struts.action.*" 
%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>


<jsp:useBean id="loginFormDeux" scope="session" class="com.copyright.ccc.web.forms.LoginForm"/>



<html>
<head>
<title>Already Registered - Copyright.com</title>
<link href="default.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">
<style type="text/css">
    form#LogIn p.Button { text-align: right; }
    table#LogInForm { text-align: left; width: 500px; margin: 0px auto; }
    table#LogInForm td.Left { text-align: left; vertical-align: top; padding-right: 20px; width: 40%; }
    table#LogInForm td.verticalline { border-left: 1px solid #E7E7E7; padding-left: 20px; width: 60%; }
</style>
</head>
<body>


<!--page content-->
 
<!--            		
<div id="ecom-boxcontent"> 
              		    
    <div id="none"> 
    
    -->
                		
	<!--top Line-->
                		
	<!-- <div id="topLine"></div> -->
	<!-- left column -->
                		
	<!-- <div id="onecolumn" style="margin-left: 12px;">  -->

<%              
String username="";
//username = (String) request.getSession().getAttribute("EMAIL");
username = (String) request.getAttribute(WebConstants.RequestKeys.EMAIL);
%>                  		    

	    <h2>You&#146;re Already registered with one of our services.</h2>
	    <p class="dottedLine" style="margin-left: 0px; margin-top: -12px;">&#160;</p>
	    
	    <p style="margin-top: 18px;">
	    <span style="color: #EE0000; font-weight: 600;">
	    	<%=username %>
	    </span> is already registered with copyright.com or with Rightslink.
	    </p>
	    
	    <p>Please <a href="<html:rewrite page="/loginBounce.do"/>">log into your existing account</a>.</p>
	    <p class="dottedLine" style="margin-left: 0px; margin-top: -12px;">&#160;</p>
	    
	    <p>(If you use our Rightslink service, use your Rightslink username and password to login to copyright.com.)</p>

<!--      <form name="loginFormDeux" action="<%=request.getContextPath()%>/do/sendResetLink" method="POST" id="LogIn">  -->
           <!-- <form name="loginForm" action="forgotPassword"> -->

		  <html:hidden name="loginFormDeux" property="forgotPasswordUsername" value="<%=username%>"/>

                    	 

                            				
		<h3>Forgot your password?</h3>
		<p>If you forgot your password, enter your account e-mail address below.<br />
		We will e-mail you instructions on how to reset your password.</p>
		<p style="margin-top: 8px">
        
        <html:form action="forgotPassword">
                    <input type="text" name="forgotPasswordUsername" tabindex="4" size="30"/>
                    <p class="Button">
                        <html:submit value="Send" tabindex="5"/>
                    </p>
                </html:form>
            </p>
            
        
<!--	</div>
    </div>
</div> -->
<!--end of page content-->

</body>
</html>
