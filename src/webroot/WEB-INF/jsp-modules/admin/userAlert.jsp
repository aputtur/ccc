<%@ page language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ page errorPage="/jspError.do" %>

<div class="clearer"></div>
<div id="ecom-content">
	
<h1>Manage User Alert Message</h1>

<p><html:link action="/admin/userAdmin.do?operation=show"><b>< < Return to the user lookup page.</b></html:link></p>

<b>Instructions</b>:  Select "on" or "off" to enable displaying the message.<br/>To save
a message, type it in the message box.<br/>When you are satisfied, click save.

<br/><br/>
<html:form action="/admin/userAlert.do?operation=alert" method="post" styleId="form">
<b>Alert Status</b>:&nbsp;&nbsp;
<html:radio name="userAlertForm" property="flag" value="ON" /> on&nbsp;&nbsp;
<html:radio name="userAlertForm" property="flag" value="OFF" /> off&nbsp;&nbsp;<br/><br/>
<b>Alert Message</b>:<br/>
<html:textarea name="userAlertForm" rows="5" cols="60" property="message" /><br/><br/>
<html:submit value="Save"/>
</html:form>

</div>
<div class="clearer"></div>