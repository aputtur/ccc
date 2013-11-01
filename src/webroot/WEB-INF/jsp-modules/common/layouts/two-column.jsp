<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>


<bean:define scope="request" id="pageName" name="Page" type="java.lang.String" />
<bean:define scope="request" id="rightSidebar" name="RightSidebar" type="java.lang.String" />

<!-- Begin template/two-column.jsp -->
<div id="two-column">
  <div id="two-column-main">
    <static:importContent file="<%= pageName %>" />
  </div>
  <div id="two-column-sidebar">
    <% String sidebar = CC2Configuration.getInstance().getStaticContentRoot() + "/sidebars/"; %>
    <static:import file="<%=rightSidebar%>" root="<%=sidebar %>" />
  </div>
 <div class="clearer"></div>
</div> 
<div class="clearer"></div>

<!-- end template/two-column.jsp -->
