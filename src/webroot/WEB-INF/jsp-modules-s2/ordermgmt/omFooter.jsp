<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.Calendar" %>
<%
 String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
%>

<!-- Begin Footer -->
 <div style="width: 992px">

        <div style="float: left; font-size: 10px;padding-top: 5px;">
			Copyright <%=currentYear%> Copyright Clearance Center<s:if test="showRenderTimings">&nbsp;<s:property value="renderTime"/></s:if>
		</div>
</div>

<!-- End Footer -->

	
