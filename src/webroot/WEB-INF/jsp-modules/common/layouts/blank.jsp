<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page errorPage="/jspError.do" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<!-- Begin template/blank.jsp -->
<bean:define scope="request" id="pageName" name="Page" type="java.lang.String" />
<static:importContent file="<%= pageName %>" />
<!-- end template/blank.jsp -->
