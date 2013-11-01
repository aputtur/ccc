<%@ page language="java" 
import="java.io.*"
import="java.util.*"
import="org.apache.struts.action.*"

%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib uri="/WEB-INF/tld/struts-bean.tld"  prefix="bean" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>
<%              
String username="";
//username = (String) request.getSession().getAttribute("EMAIL");
username = (String) request.getAttribute(WebConstants.RequestKeys.EMAIL);
request.setAttribute(WebConstants.RequestKeys.EMAIL, "");
%> 
<bean:define id="help_url"><%= CC2Configuration.getInstance().getHelpURL() %></bean:define>
<h2>Thank You.</h2>
<%
    if (!username.equalsIgnoreCase(""))
    {
%>
    <p>An e-mail has been sent to <u><%=username%></u>. </p>
    <br>
<%
    }
%>
<p>Please follow the instructions in that e-mail to reset your Password.</p>      
<br>
<p><ul>
<li><a href="home.do"> Go to the copyright.com Home page</a>.</li>   	
</ul>
</p>
<p>
<br>
Need assistance?  Visit the <a href="<bean:write name="help_url" />">help</a> area or <a href="mailto:info@copyright.com">contact customer support</a>.
</p>		
      
     