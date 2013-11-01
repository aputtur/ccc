<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>


<bean:define scope="request" id="pageName" name="Page" type="java.lang.String" />
<bean:define scope="request" id="leftSidebar" name="LeftSidebar" type="java.lang.String" />
<bean:define scope="request" id="rightSidebar" name="RightSidebar" type="java.lang.String" />

<!-- Begin template/three-column.jsp -->
<div id="three-column">
  <% String sidebar = CC2Configuration.getInstance().getStaticContentRoot() + "/sidebars/"; %>
  <div id="three-column-left-sidebar">
    <static:import file="<%=leftSidebar%>" root="<%=sidebar%>" />
  </div>
  <div id="three-column-main">
    <static:importContent file="<%= pageName %>" />
  </div>
  <div id="three-column-right-sidebar">
    <static:import file="<%=rightSidebar%>" root="<%=sidebar%>" />
  </div>
 <div class="clearer"></div>
</div>
<div class="clearer"></div>
 
<!-- end template/three-column.jsp -->
 
