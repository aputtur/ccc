<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld" prefix="util"%>
<%@ page errorPage="/jspError.do" %>
<style type="text/css">
.rule {
   border-top: solid 1px #999;
   height: 2px;
   padding: 0 0 0 0;
   clear:both;
}
</style>
<script language="javascript" type="text/javascript" src="<html:rewrite page='/resources/commerce/js/rlOrderHistory.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/resources/commerce/js/util.js'/>"></script>

<h1>Order History</h1>  
<div id="tabs">
  <ul>
    <li><a href="orderHistory.do" id="link_view_orders"><span>View Orders</span></a></li>
    <li><a href="orderDetail.do" id="link_view_order_details"><span>View Order Details</span></a></li>
    <li><a href="#" id="link_view_RightsLink_orders" class="active"><span>View RIGHTSLINK Orders</span></a></li>
  </ul>
</div>
<br clear="all" />
<!-- Begin History Order -->
<div id="order-history-widecontent">
<form id="rlOrderHistoryForm" name="rlOrderHistoryForm" action="rlOrderHistory.do" method="post">
<html:hidden styleId="currPage" name="rlOrderHistoryForm" property="currentPage"/>
<html:hidden styleId="state" name="rlOrderHistoryForm" property="state"/>

<logic:equal name="rlOrderHistoryForm" property="rlServiceUp" value="true">

<div class="order-history-line" style="display:inline;">

  <!-- div class="inbox01" style="padding: 9px 0 3px 0;"-->
     <logic:equal name="rlOrderHistoryForm" property="state" value="0">
       <b>View: </b> <b>Completed</b> | <a href="javascript:view(1);">Pending</a> | <a href="javascript:view(2);">Canceled</a> | <a href="javascript:view(3);">Credited</a> | <a href="javascript:view(4);">Denied</a>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="1">
       <b>View: </b> <a href="javascript:view(0);">Completed</a> | <b>Pending</b> | <a href="javascript:view(2);">Canceled</a> | <a href="javascript:view(3);">Credited</a> | <a href="javascript:view(4);">Denied</a>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="2">
       <b>View: </b> <a href="javascript:view(0);">Completed</a> | <a href="javascript:view(1);">Pending</a> | <b>Canceled</b> | <a href="javascript:view(3);">Credited</a> | <a href="javascript:view(4);">Denied</a>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="3">
       <b>View: </b> <a href="javascript:view(0);">Completed</a> | <a href="javascript:view(1);">Pending</a> | <a href="javascript:view(2);">Canceled</a> | <b>Credited</b> | <a href="javascript:view(4);">Denied</a>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="4">
       <b>View: </b> <a href="javascript:view(0);">Completed</a> | <a href="javascript:view(1);">Pending</a> | <a href="javascript:view(2);">Canceled</a> | <a href="javascript:view(3);">Credited</a> | <b>Denied</b>
     </logic:equal>     
  <!--/div -->

</div>

<!-- <div class="rule"></div> -->
<div style="clear:both"></div>

<logic:greaterThan name="rlOrderHistoryForm" property="totalItemsCount" value="1">
<!-- Sort section -->
<table width="700"><tr>
    <td width="90">Sort orders by:</td>
    <td width="125">
    <html:select styleId="sortFld" size="1" name="rlOrderHistoryForm" property="sortBy" onchange="javascript:setSortOrder(this);">
       <html:optionsCollection name="rlOrderHistoryForm" property="sortOptions" label="text" value="value"/>
    </html:select>
    </td><td width="90">
    <html:radio name="rlOrderHistoryForm" property="direction" value="0" accesskey="a" styleId="ASC" onclick="javascript:newSort(this);"/>Ascending&nbsp;
    </td><td width="90">
    <html:radio name="rlOrderHistoryForm" property="direction" value="1" accesskey="d" styleId="DSC" onclick="javascript:newSort(this);"/>Descending
    </td>
    <td align="right">
    <util:scroller name="rlOrderHistoryForm" action="pageAction" currentPage="currentPage" startPage="startPage" resultsPerPage="pageSize" totalPages="totalPages" scope="session"/>
    </td>
</tr></table>
<div class="rule"></div>
<!-- End Sort section -->
</logic:greaterThan>
<logic:lessThan name="rlOrderHistoryForm" property="totalItemsCount" value="1">
	 <logic:equal name="rlOrderHistoryForm" property="state" value="0">
         <br/><b>Your search for Completed Orders found no matches.</b>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="1">
	     <br/><b>Your search for Pending Orders found no matches.</b>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="2">
	     <br/><b>Your search for Canceled Orders found no matches.</b>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="3">
	     <br/><b>Your search for Credited Orders found no matches.</b>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="4">
	     <br/><b>Your search for Denied Orders found no matches.</b>
     </logic:equal>
     <logic:equal name="rlOrderHistoryForm" property="state" value="5">
	     <br/><b>Your search for All Orders found no matches.</b>
     </logic:equal>
</logic:lessThan>

<logic:iterate id="aRLOrder" indexId="index" name="rlOrderHistoryForm" property="items">
<div class="order-history-line" style="clear:right;">
    <div class="inbox01" style="padding-top:5px;">
       <logic:equal name="aRLOrder" property="licenseNo" value="-1">
       	 <logic:equal name="rlOrderHistoryForm" property="state" value="0">
         	<b>JOB TICKET ID #:</b> <a id="link_printable_rlorder_<%=index%>" href="<bean:write name='aRLOrder' property='rightslinkURL'/>" target="_blank"><bean:write name='aRLOrder' property='jobTicketID'/></a><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="1">
         	<b>JOB TICKET ID #:</b> <a id="link_printable_rlorder_<%=index%>" href="<bean:write name='aRLOrder' property='rightslinkURL'/>" target="_blank"><bean:write name='aRLOrder' property='jobTicketID'/></a><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="2">
         	<b>JOB TICKET ID #:</b><bean:write name='aRLOrder' property='jobTicketID'/><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="3">
         	<b>JOB TICKET ID #:</b> <a id="link_printable_rlorder_<%=index%>" href="<bean:write name='aRLOrder' property='rightslinkURL'/>" target="_blank"><bean:write name='aRLOrder' property='jobTicketID'/></a><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="4">
         	<b>JOB TICKET ID #:</b><bean:write name='aRLOrder' property='jobTicketID'/><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="5">
         	<b>JOB TICKET ID #:</b><bean:write name='aRLOrder' property='jobTicketID'/><br />
         </logic:equal>
       </logic:equal>
       <logic:notEqual name="aRLOrder" property="licenseNo" value="-1">
         <logic:equal name="rlOrderHistoryForm" property="state" value="0">
         	<b>LICENSE #:</b> <a id="link_printable_rlorder_<%=index%>" href="<bean:write name='aRLOrder' property='rightslinkURL'/>" target="_blank"><bean:write name='aRLOrder' property='licenseNo'/></a><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="1">
         	<b>ORDER #:</b> <a id="link_printable_rlorder_<%=index%>" href="<bean:write name='aRLOrder' property='rightslinkURL'/>" target="_blank"><bean:write name='aRLOrder' property='licenseNo'/></a><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="2">
         	<b>ORDER #:</b><bean:write name='aRLOrder' property='licenseNo'/><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="3">
         	<b>LICENSE #:</b><bean:write name='aRLOrder' property='licenseNo'/><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="4">
         	<b>ORDER #:</b><bean:write name='aRLOrder' property='licenseNo'/><br />
         </logic:equal>
         <logic:equal name="rlOrderHistoryForm" property="state" value="5">
         	<b>LICENSE #:</b><bean:write name='aRLOrder' property='licenseNo'/><br />
         </logic:equal>
       </logic:notEqual>
       <b>Order Date:</b> <bean:write name="aRLOrder" property="createDate"/><br />
       <br />
       <br />
       <br />
       <logic:equal name="rlOrderHistoryForm" property="state" value="0">
       		<a id="link_printable_rlorder_<%=index%>" href="<bean:write name='aRLOrder' property='rightslinkURL'/>" target="_blank">View printable order</a>
       </logic:equal> 
       <logic:equal name="rlOrderHistoryForm" property="state" value="1">
       		<a id="link_printable_rlorder_<%=index%>" href="<bean:write name='aRLOrder' property='rightslinkURL'/>" target="_blank">View printable order</a>
       </logic:equal>
    </div>
    <div class="floatleft" style="padding-top:5px;">
	<div class="inbox02">
            <bean:write name='aRLOrder' property='publication'/><br /><br />
            <b>Title: </b><bean:write name='aRLOrder' property='title'/><br />
            <b>Type of use: </b><bean:write name='aRLOrder' property='typeOfUse'/>
	</div>
        
        <div class="inbox01">
        <br />
        <br />
        <br />
        <br />
        <b>Fee: </b>$<bean:write name='aRLOrder' property='totalFee'/>  <bean:write name='aRLOrder' property='currencyType'/> 
        </div>
    </div>

    <div class="clearer"></div>
</div>
</logic:iterate>

<!-- End Iterative Purchase output -->
<div class="floatright" style="padding: 3px 10px 3px 0;">
<util:scroller name="rlOrderHistoryForm" action="pageAction" currentPage="currentPage" startPage="startPage" resultsPerPage="pageSize" totalPages="totalPages" scope="session"/>
</div>
<br />

</logic:equal>
</form>
</div> <!-- End order-history-widecontent -->
<!-- End Right Content -->
<div class="clearer"></div>
<script type="text/JavaScript">
  if( typeof addOnLoadEvent == 'function' && typeof initPage == 'function' ) addOnLoadEvent( initPage );
</script>
