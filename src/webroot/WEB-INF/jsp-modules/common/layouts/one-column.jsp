<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page errorPage="/jspError.do" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<!-- Begin template/one-column.jsp -->
<bean:define scope="request" id="pageName" name="Page" type="java.lang.String" />
<div id="one-column">
    <static:importContent file="<%= pageName %>" />
</div>
<div class="clearer"></div>
 
<!-- end template/one-column.jsp -->
