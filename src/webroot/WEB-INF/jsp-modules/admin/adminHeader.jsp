<%@ page contentType="text/html;charset=windows-1252"  language="java" %>
<%@ page errorPage="/jspError.do" %>

<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.lang.Boolean" %>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>

<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  

<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>

<link href="<html:rewrite page="/resources/commerce/css/admin.css"/>" rel="stylesheet" type="text/css" />

<script src="<html:rewrite page="/resources/commerce/js/admin-dropdown.js"/>" type="text/javascript"></script>

<% 
String logoutLink = "/ccc/do/logout";
%>

<%/* <tiles:insert template="/dcs_tag_js.jsp"/> */%>

  <bean:cookie id="firstName" name="firstNameCookie" value="Undefined"/>

<script type="text/javascript">

</script>

<security:ifUserEmulating>

    <div style="width:100%;background-color:#FFFF99;border:1px sold #999900;height:24px;text-align:center;color:red;padding-top:8px">
        An Emulation Session is Active
    </div>

</security:ifUserEmulating>

<!-- Begin Header -->
<a href="<html:rewrite page="/home.do"/>">
    <html:img src="/media/images/CCC_logo3_RGB_72dpi.jpg" alt="Copyright.com" style=" border: 0; float: left;margin: 4px 0 6px 8px;" />
</a>

<div id="ecom-nav">
    <tiles:insert page="/WEB-INF/jsp-modules/common/dynamicHeaderLinks.jsp"/>
</div>

<div id="navigation" style="height:24px">
    <div id="menuBar">
    <!-- menu bar definition -->
    <table>
        <tr>
            <td class="menuover" id="order_mgmt" style="vertical-align: middle">                
                Order Management
            </td>
            
            <td class="menuover" id="csr" style="vertical-align: middle">     
                CSR
            </td>
            
            <td class="menuover" id="roles" style="vertical-align: middle">
                Roles
            </td>
            
            <td class="menuover" style="vertical-align: middle; width: 100%">
                &nbsp;
            </td>
        </tr>
    </table>
    </div>
    <!-- menu definitions -->
    <div id="menu_order_mgmt" class="adminMenu">
        <div class="adminMenuItem" onclick="window.location.href='<html:rewrite page="/orderHistory.do"/>';">
            <span class="menuItemText">Adjust Order</span>
        </div>
        <div class="adminMenuItem" onclick='window.location.href="<html:rewrite page="/adjustment/adjustment.do?operation=displayAdjustmentsForUser"/>"'>
            <span class="menuItemText">Pending Adjustments</span>
        </div>
        <div class="adminMenuItem" onclick='window.location.href="<html:rewrite page="/viewReports.do"/>"'>
            <span class="menuItemText">Activity Report</span>
        </div>
        <!--
          <div class="adminMenuItem" onclick='window.location.href="#";'>
              <span class="menuItemText">Adjustment History</span>
          </div>
        -->
    </div>
    
    <div id="menu_csr" class="adminMenu">
        <div class="adminMenuItem" onclick="window.location.href='<html:rewrite page="/admin/startEmulationForm.do"/>';">
            <span class="menuItemText">Emulate User</span>
        </div>
    </div>
    
    <div id="menu_roles" class="adminMenu">
    </div>
    
</div> <!-- id=navigation -->


<script type="text/javascript">
    definemenu();
</script>


<div id="titlebar" >
    <span class="adminTitle">Copyright.com Administration</span>
</div>
