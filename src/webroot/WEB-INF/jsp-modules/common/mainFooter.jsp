<%@ page contentType="text/html;charset=windows-1252"
import="java.util.Calendar"
%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>
<%@ page import="com.copyright.workbench.util.StringUtils2" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>


<% String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
%>

<% if(StringUtils2.isNullOrEmpty((String)request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO))){ %>
<!-- Begin Footer -->
   <static:importURL url='<%= CC2Configuration.getInstance().getFooterLinksURL() %>'  />
<!-- End Footer -->
<% } else {%>
	<% if(request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO).equals("0")){ %>
	<!-- Begin Footer -->
	   <static:importURL url='<%= CC2Configuration.getInstance().getFooterLinksURL() %>'  />
	<!-- End Footer -->
	<% } %>
	
	<% if(request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO).equals("1")){ %>
	<!-- Begin Footer -->
	   <static:importURL url='<%= CC2Configuration.getInstance().getCemproFooterURL() %>'  />
	
	<!-- End Footer -->
	<% } %>
<% } %>
	
