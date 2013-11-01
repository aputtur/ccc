<%@ page language="java" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.lang.Boolean" %>
<%@ page import="com.copyright.ccc.web.util.WebAdmin" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<%
WebAdmin webAdmin = new WebAdmin();
String userAlert = webAdmin.get(WebAdmin.USERALERT_FLAG);
String userAlertMsg = webAdmin.get(WebAdmin.USERALERT_MESSAGE_TEXT);
%>
<!-- In userAlert.jsp -->
<bean:define id="showAlertMsg" value="<%=userAlert%>"/>
<logic:equal name="showAlertMsg" value="ON">
    <p align="left">&nbsp;<br/>
    <font size="4"><%=userAlertMsg%></font></p>
</logic:equal>