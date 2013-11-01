<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld" prefix="util"%>
<style type="text/css">
#searchBar { 
    padding: 4px 0 0 0;
  }
#searchBar td { 
  vertical-align:middle; 
  font-size: 11px;
  }
  #dateFlds td { 
  vertical-align:middle; 
  font-size: 11px;
  }
.rule {
   border-top: solid 1px #999;
   height: 2px;
   padding: 0 0 0 0;
   clear:both;
}
</style>
<script language="javascript" type="text/javascript" src="<html:rewrite page='/resources/commerce/js/orderHistory.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/resources/commerce/js/util.js'/>"></script>
<link href="<html:rewrite page="/resources/ordermgmt/style/smoothness/jquery-ui-1.7.2.custom.css"/>" rel="stylesheet" type="text/css" />

 <html:messages id="rlError"  message="true" > 
            <div style="width:100%" align="center">
                <div class="calloutbox" style="width:80%" align="left">
                    <p>
                       
                        <bean:write name="rlError" filter="false"/>
                    
                    </p>
                </div>
                <br>
            </div>
 </html:messages> 
       
<h1>Order History</h1>  

<br clear="all" />
<!-- Begin History Order -->
<logic:equal name="orderHistoryActionForm" property="rlUser" value="true">
	<div id="tabs">
	  <ul>
	    <li><a href="#" id="link_view_orders" class="active"><span>View Orders</span></a></li>
	    <li><a href="orderDetail.do" id="link_view_order_details"><span>View Order Details</span></a></li>
	    <li><a href="rlOrderHistory.do" id="link_view_RightsLink_orders"><span>View RIGHTSLINK Orders</span></a></li>
	  </ul>
	</div>
</logic:equal>
<logic:notEqual name="orderHistoryActionForm" property="rlUser" value="true">
	<div id="tabs">
	  <ul>
	    <li><a href="#" id="link_view_orders" class="active"><span>View Orders</span></a></li>
	    <li><a href="orderDetail.do" id="link_view_order_details"><span>View Order Details</span></a></li>
	  </ul>
	</div>
</logic:notEqual>
<div style="padding:15px"></div>
<div id="order-history-widecontent">


<form id="orderHistoryActionForm" name="orderHistoryActionForm" action="processOrderHistory.do" method="post">
       



<html:hidden styleId="status" name="orderHistoryActionForm" property="status"/>
<html:hidden styleId="currPage" name="orderHistoryActionForm" property="currentPage"/>
<html:hidden name="orderHistoryActionForm" property="startPage"/>
<html:hidden styleId="srch" name="orderHistoryActionForm" property="searchPage"/>
<html:hidden styleId="lastDir" name="orderHistoryActionForm" property="lastDir"/>
<html:hidden styleId="cartEmpty" name="orderHistoryActionForm" property="cartEmpty"/>
<html:hidden styleId="cartAcademic" name="orderHistoryActionForm" property="cartAcademic"/>


<!-- Search section -->
<div class="order-history-line" style="display:inline;">
  <logic:equal name="orderHistoryActionForm" property="searchPage" value="false">
  <div class="inbox01" style="padding: 9px 0 3px 0;">
     <logic:equal name="orderHistoryActionForm" property="status" value="0">
       <b>View: </b> <a href="javascript:view(2);">All</a> | <b>Open</b> | <a href="javascript:view(1);">Closed</a>  
     </logic:equal>
     <logic:equal name="orderHistoryActionForm" property="status" value="1">
       <b>View: </b> <a href="javascript:view(2);">All</a> | <a href="javascript:view(0);">Open</a> | <b>Closed</b>  
     </logic:equal>
     <logic:equal name="orderHistoryActionForm" property="status" value="2">
       <b>View: </b> <b>All</b> | <a href="javascript:view(0);">Open</a> | <a href="javascript:view(1);">Closed</a>  
     </logic:equal>
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Or</b>
  </div>
  </logic:equal>
  <logic:equal name="orderHistoryActionForm" property="searchPage" value="true">
  <div class="inbox01" style="padding: 3px 0 3px 0;"><a href="orderHistory.do"><< Return to main view</a></div>
  </logic:equal>
<table id="searchBar" class="floatleft" style="display:inline; padding: 0 0 0 0;">
  <tr>
    <td>Search all orders by:</td>
    <td>
    <html:select styleId="srchFld" size="1" name="orderHistoryActionForm" property="searchOption" onchange="updateInputField();">
      <html:optionsCollection name="orderHistoryActionForm" property="searchOptions" label="text" value="value"/>
    </html:select>
    </td>
    <td id="srchTxt">
    <html:text name="orderHistoryActionForm" property="searchText" size="18" maxlength="250" styleId="inputTxt"/>
    </td>
    <td id="dateFlds">
    <table style="border-collapse:collapse; padding:0;">
        <tr>
           <td align="right" nowrap>Start Date:&nbsp;</td>
           <td>
           <html:text styleId="beginDate" name="orderHistoryActionForm" property="beginDate" size="10" maxlength="10"/>
           </td>
        </tr>
        <tr>
           <td align="right" nowrap>End Date:&nbsp;</td>
           <td>
           <html:text styleId="endDate" name="orderHistoryActionForm" property="endDate" size="10" maxlength="10"/>
           </td>
        </tr>
    </table>
    </td>
    <td>
    <input id="goBtn" type="image" name="goBtn" style="vertical-align:middle;" value="submit" src="<html:rewrite href='/media/images/btn_go2.gif'/>" width="40" height="26"/>
    </td>
</tr></table>
</div>
<div class="rule"></div>
<!-- End Search section -->
<logic:greaterThan name="orderHistoryActionForm" property="totalItems" value="1">
<!-- Sort section -->
<table width="700"><tr>
    <td width="90">Sort orders by:</td>
    <td width="125">
    <html:select styleId="sortFld" size="1" name="orderHistoryActionForm" property="sortOption" onchange="javascript:setSortOrder(this);">
       <html:optionsCollection name="orderHistoryActionForm" property="sortOptions" label="text" value="value"/>
    </html:select>
    </td><td width="90">
    <html:radio name="orderHistoryActionForm" property="direction" value="ascending" accesskey="a" styleId="ASC" onclick="javascript:newSort(this);"/>Ascending&nbsp;
    </td><td width="90">
    <html:radio name="orderHistoryActionForm" property="direction" value="descending" accesskey="d" styleId="DSC" onclick="javascript:newSort(this);"/>Descending
    </td>
    <td align="right">
    <util:scroller name="orderHistoryActionForm" action="pageAction" currentPage="currentPage" startPage="startPage" resultsPerPage="resultsPerPage" totalPages="totalPages" scope="request"/>
    </td>
</tr></table>
<div class="rule"></div>
<!-- End Sort section -->
</logic:greaterThan>
</form>

<logic:greaterThan name="orderHistoryActionForm" property="totalItems" value="0">
<logic:equal name="orderHistoryActionForm" property="searchPage" value="true">
<util:searchResult bean="orderHistoryActionForm"/>
<div class="rule"></div>
</logic:equal>
</logic:greaterThan>

<logic:equal name="orderHistoryActionForm" property="totalItems" value="0">
<div style="text-align: center; font-size: 13px;">
<bean:write name="orderHistoryActionForm" property="searchEmptyString"/>
</div>
</logic:equal>

<logic:iterate id="aPurchase" indexId="index" name="orderHistoryActionForm" property="displayPurchases">
<div class="order-history-line" style="clear:right;">
    <div class="inbox01" style="padding-top:5px;">
       <b>CONFIRMATION #:</b> <a id="link_order_detail_<%=index%>" href="orderView.do?id=<bean:write name='aPurchase' property='purInst'/>&rp=main&sf=<bean:write name='orderHistoryActionForm' property='sf'/>&cp=<bean:write name='orderHistoryActionForm' property='currentPage'/>"><bean:write name='aPurchase' property='purInst'/></a><br />
       <b>Order Date:</b> <bean:write name="aPurchase" property="orderDateStr"/><br />
       <logic:notEmpty name="aPurchase" property="poNumber">
       <b>PO#:</b><util:fieldWrap bean="aPurchase" property="poNumber"/><br />
       </logic:notEmpty>
       <br />
    </div>
<div class="floatleft" style="padding-top:5px;">

    <logic:equal name="aPurchase" property="academic" value="false">
    <logic:greaterThan name="aPurchase" property="detailCount" value="0">
    <b><bean:write name="aPurchase" property="detailCount"/> 
    <logic:equal name="aPurchase" property="detailCount" value="1"> Single Item: </logic:equal>
    <logic:greaterThan name="aPurchase" property="detailCount" value="1"> Single items: </logic:greaterThan>
    <util:msglink bean="aPurchase" property="purchaseTitles" confirmNumber="aPurchase.purInst"/>
    </b>
    </logic:greaterThan>
    </logic:equal>
<logic:equal name="aPurchase" property="academic" value="true">
  <logic:iterate id="aBundle" indexId="bundIndex" name="aPurchase" property="orderBundles">
	<NOBR>
	<b>
	<logic:greaterThan name="aBundle" property="detailCount" value="0">
    <bean:write name="aBundle" property="detailCount"/> 
    <logic:equal name="aBundle" property="detailCount" value="1">Course item: </logic:equal>
    <logic:greaterThan name="aBundle" property="detailCount" value="1">Course items: </logic:greaterThan>
    <util:msglink bean="aBundle" property="bundleTitles" confirmNumber="aPurchase.purInst"/>
    </logic:greaterThan>
    </b>
    </NOBR>

<br/>
<br/>
 
   
	<div class="inbox02">
	 <logic:notEmpty name="aBundle" property="organization">
	       <b>University/Institution:</b> <bean:write name="aBundle" property="organization"/><br /><br />
            </logic:notEmpty>
            
         <logic:notEmpty name="aBundle" property="startOfTermStr">
	       <b>Start of Term:</b> <bean:write name="aBundle" property="startOfTermStr"/><br />
	       
	       <util:dateCheck bean="aBundle" property="startOfTerm"/><br/>
	       <br />
            </logic:notEmpty>
	    <logic:notEmpty name="aBundle" property="courseName">
	       <b>Course name:</b> <bean:write name="aBundle" property="courseName"/>  
	       <br /><br />
	    </logic:notEmpty>
	    <logic:notEmpty name="aBundle" property="courseNumber">
               <b>Course number:</b> <bean:write name="aBundle" property="courseNumber"/>
	       <br />
	    </logic:notEmpty>
	    
            <logic:notEqual name="aBundle" property="numberOfStudents" value="0">
	       <b>Number of Students:</b> <bean:write name="aBundle" property="numberOfStudents"/><br /><br />
            </logic:notEqual>
	</div>



	<div class="inbox01">
	<logic:notEmpty name="aBundle" property="instructor">
              <b>Instructor:</b> <bean:write name="aBundle" property="instructor"/><br />
            </logic:notEmpty>
	   
            <logic:notEmpty name="aBundle" property="yourReference">
              <b>Your reference:</b> <bean:write name="aBundle" property="yourReference"/> 
              <br />
            </logic:notEmpty>
            <logic:notEmpty name="aBundle" property="accountingReference">
              <b>Accounting reference:</b> <bean:write name="aBundle" property="accountingReference"/>
              <br />
            </logic:notEmpty>
            <logic:notEmpty name="aBundle" property="orderEnteredBy">
              <b>Order entered by:</b> <bean:write name="aBundle" property="orderEnteredBy"/>
            </logic:notEmpty>
	</div>
	<br/>

	 </logic:iterate>
	<div class="clearer"></div>
    <logic:greaterThan name="aPurchase" property="detailCount" value="0">
    <b><bean:write name="aPurchase" property="detailCount"/> 
    <logic:equal name="aPurchase" property="detailCount" value="1"> Single item: </logic:equal>
    <logic:greaterThan name="aPurchase" property="detailCount" value="1"> Single items: </logic:greaterThan>
    <util:msglink bean="aPurchase" property="purchaseTitles" confirmNumber="aPurchase.purInst"/>
    </b>
    </logic:greaterThan>
	</logic:equal>  
    </div>

    <div class="clearer"></div>
<table width="100%" align="center">
<tr><td width="210" border="0" cellpadding="0">
            <a href="orderView.do?id=<bean:write name='aPurchase' property='purInst'/>&rp=main&sf=<bean:write name='orderHistoryActionForm' property='sf'/>&cp=<bean:write name='orderHistoryActionForm' property='currentPage'/>&viewOrder=1">View order</a>&nbsp;|
            <logic:equal name="aPurchase" property="closed" value="false">
              <a href="orderView.do?id=<bean:write name='aPurchase' property='purInst'/>&rp=main&sf=<bean:write name='orderHistoryActionForm' property='sf'/>&cp=<bean:write name='orderHistoryActionForm' property='currentPage'/>">Edit</a>&nbsp;|
            </logic:equal>

            <logic:equal name="aPurchase" property="closed" value="true">
              <a href="javascript:deleteOrder(<bean:write name='aPurchase' property='purInst'/>);">Remove from view</a>&nbsp;|
            </logic:equal>

            <logic:equal name="orderHistoryActionForm" property="cartEmpty" value="false">
              <logic:equal name="aPurchase" property="academic" value="true">
                <a href="javascript:copyAcademic(<bean:write name='aPurchase' property='purInst'/>)">Copy order</a>
              </logic:equal>
              
              <logic:equal name="aPurchase" property="academic" value="false">
                <a href="javascript:copyNonacademic(<bean:write name='aPurchase' property='purInst'/>)">Copy order</a>
              </logic:equal>
            </logic:equal>
            <logic:equal name="orderHistoryActionForm" property="cartEmpty" value="true">
              <a href="javascript:copy(<bean:write name='aPurchase' property='purInst'/>)">Copy order</a>
            </logic:equal>

    
 <!--        <logic:equal name="orderHistoryActionForm" property="isAdjustmentUser" value="true">
            <br><br>
            <a href="adjustment/adjustment.do?operation=performPurchaseAdjustment&id=<bean:write name='aPurchase' property='purInst'/>">Adjust Purchase</a>&nbsp
        </logic:equal>  -->
</td></tr>
</table>
</div>
</logic:iterate>

<!-- End Iterative Purchase output -->
<div class="floatright" style="padding: 3px 10px 3px 0;">
<util:scroller name="orderHistoryActionForm" action="pageAction" currentPage="currentPage" startPage="startPage" resultsPerPage="resultsPerPage" totalPages="totalPages" scope="request"/>
</div>
<br />
</div> <!-- End order-history-widecontent -->
<!-- End Right Content -->
<div class="clearer"></div>
</div>
<script type="text/JavaScript">
  if( typeof addOnLoadEvent == 'function' && typeof initPage == 'function' ) addOnLoadEvent( initPage );
 	
 	$(document).ready(function(){

	 	$("#beginDate").datepicker( {
	 			changeMonth: true,
				changeYear: true,
				yearRange: '-4:+4',
			    showAnim : 'fadeIn',
				showOn: 'button',
				buttonImage: '<html:rewrite href='/media/images/calendar.gif'/>',
				buttonImageOnly: true,
				buttonText: 'Calendar...'		});
	 	$("#endDate").datepicker( {
	 		    changeMonth: true,
			    changeYear: true,
			    yearRange: '-4:+4',
			    showAnim : 'fadeIn',
				showOn: 'button',
				buttonImage: '<html:rewrite href='/media/images/calendar.gif'/>',
				buttonImageOnly: true,
				buttonText: 'Calendar...'		});

	 	
	});


 	
</script>
