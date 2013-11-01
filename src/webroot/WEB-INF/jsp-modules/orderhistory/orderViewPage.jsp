<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld" prefix="util"%>
<%@ taglib uri="/WEB-INF/tld/cc2-security.tld" prefix="security" %>
<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants" %>
<%@ page import="com.copyright.svc.order.api.data.DeclineReasonEnum" %> 
<%@ page errorPage="/jspError.do" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.copyright.ccc.business.services.ItemConstants" %>
<link type="text/css" rel="stylesheet" href="<html:rewrite page='/resources/commerce/css/noPrint.css'/>">
<style type="text/css">
#dateFlds img{
vertical-align: middle;
}
#ecom-content-wrapper{
padding:0px;
}
h3{
text-transform:none;
}
#bottomcorners{
background:none;
}
hr { height:1px; color: #999999; }
#searchBar td {
   vertical-align:middle;
}
.rule {
   border-top: solid 1px #999;
   height: 2px;
   padding: 0 0 0 0;
   clear:both;
}
.box01 {
  padding: 5px 0 5px 0;
  border-bottom: 1px solid #999999;
}
.results {
font-family:Verdana, Arial, Helvetica, sans-serif;
font-size:13px;
text-align:center;
margin:5px 0;
}
.product-box .grantBtn {
    bottom: -2px;
    padding-right: 4px;
    position: absolute;
    right: 0;
    text-align: right;
}
</style>
<script type="text/javascript" src="<html:rewrite page='/resources/commerce/js/util.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/orderViewPage.js"/>"></script>

<link type="text/css" rel="stylesheet" href="/resources/commerce/css/cart_all.css"/>
<link type="text/css" rel="stylesheet" href="/resources/commerce/css/contextualHelp.css"/>
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" /> 
<link href="<html:rewrite page="/resources/commerce/css/impromptu.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/jquery.form.js"/>"></script>
<script src="<html:rewrite page="/resources/commerce/js/paymentScripts.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/accept_decline_specialorder.js"/>" type="text/javascript"></script>  
<script type="text/javascript" src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-impromptu.2.8.js"/>" ></script>

<!--[if lt IE 8]><link rel="stylesheet" type="text/css" href="/resources/commerce/css/css/cart_ie.css"/><![endif]-->
<script type="text/javascript">
 if( typeof addOnLoadEvent == 'function' && typeof initPage == 'function' ) addOnLoadEvent( initPage );
</script>
<script type="text/javascript">
if (browserInfo.isIE){
    document.write ("<LINK REL='stylesheet' type='text/css' href='/resources/commerce/css/rightslink_layout_msie.css'>");
}
else{
    document.write ("<LINK REL='stylesheet' type='text/css' href='/resources/commerce/css/rightslink_layout_ns6.css'>");
}
</script>
<script type="text/javascript">
    var currentOrder = null;
    var status = null;
     
function copy() {
  document.orderViewActionForm.action = '<html:rewrite page="/copyOrder.do"/>' + '?id=' + <bean:write name='orderViewActionForm' property='id'/>;
  document.orderViewActionForm.submit();
}
function copyLic(purid, id) {
  var cartStatus = document.getElementById('crtEmpty');
  var isEmpty = cartStatus.value;
  if (isEmpty == 'true') {
    document.forms[0].action = '<html:rewrite page="/copyLicense.do"/>' + '?purid=' + purid + '&id=' + id;
    document.forms[0].submit();
  } else {
    alert('You may only copy an order to an empty cart. Please purchase or empty the existing items in your cart.'); // debug
  }
}
function copyNonacademicLicense(purid, id) {
  var cartStatus = document.getElementById('cartAcademic');
  var isAcademic = cartStatus.value;
    document.forms[0].action = '<html:rewrite page="/copyLicense.do"/>' + '?purid=' + purid + '&id=' + id;
    document.forms[0].submit();
}
function copyAcademicLicense(purid, id) {
  var cartStatus = document.getElementById('cartAcademic');
  var isAcademic = cartStatus.value;
    document.forms[0].action = '<html:rewrite page="/copyLicense.do"/>' + '?purid=' + purid + '&id=' + id;
    document.forms[0].submit();

}
function itemStatus(licId) {

$.ajax({
			type: "GET",
			url: "/tfOdtStatus.do?operation=fetchOdtStatus",
			data: "odtInst=" +licId,
			async: false, // async * false = !async = sync
			success: 
			function(data){
			//alert("DataInvoiced: " + data);
			status = data;
			
		}
	});
	return status;
}
function cancel(licenseId, purchId)
{

var status1 = itemStatus(licenseId);
	//alert("Status2: " + status1);
	
			
		if (status1==2000) {
			alert("Item# " + licenseId + " can not be cancelled. It is already invoiced");
			return;
			}
			
    $("#dialog-confirm-cancellineitem").dialog("option", "buttons", [
        {
            text: "Yes",
            click: function() {
                var cpVal = '1'; // default page to first in sequence
                var currPage = document.getElementById('cp');
                if (currPage) {
                   cpVal = currPage.value;
                }
                var tabVal = 'main'; // default to main order history tab
                var rtnPage = document.getElementById('rp');
                if (rtnPage) {
                  tabVal = rtnPage.value;
                }
                var sfString = 'false';
                var searchFlag = document.getElementById('sf');
                if (searchFlag) {
                   if (searchFlag == 'true') { sfString = 'true'; }
                }
                document.forms[0].action = '<html:rewrite page="/licenseCancel.do"/>' + '?licId=' + licenseId + '&id=' + purchId + '&rp=' + tabVal + '&cp=' + cpVal + '&sf=' + sfString;
                document.forms[0].submit();
                $(this).dialog("close");
            }
        },
        {
            text: "No",
            click: function() { 
                $(this).dialog("close");
            }
        }
    ]);
    $("#dialog-confirm-cancellineitem").dialog("open");
}
function uncancel(licenseId, purchId) 
{
    $("#dialog-confirm-uncancellineitem").dialog("option", "buttons", [
        {
            text: "Yes",
            click: function() {
                document.forms[0].action = '<html:rewrite page="/licenseUncancel.do"/>' + '?licId=' + licenseId + "&id=" + purchId;
                document.forms[0].submit();
                $(this).dialog("close");
            }
        },
        {
            text: "No",
            click: function() { 
                $(this).dialog("close");
            }
        }
    ]);
    $("#dialog-confirm-uncancellineitem").dialog("open");
}
$(function() {
    $("#dialog-confirm-cancellineitem").dialog({
        autoOpen: false, 
        modal: true, 
        resizable: false,
        height: 140
    });
    $("#dialog-confirm-uncancellineitem").dialog({
        autoOpen: false, 
        modal: true, 
        resizable: false,
        height: 140
    });
    $("#dialog-confirm-cancelopenitems").dialog({
        autoOpen: false,
        resizable: false,
        height: 140,
        modal: true,
        buttons: {
            "Yes": function() {
                var cpVal = '1'; // default page to first in sequence
                var currPage = document.getElementById('cp');
                if (currPage) {
                   cpVal = currPage.value;
                }
                var tabVal = 'main'; // default to main order history tab
                var rtnPage = document.getElementById('rp');
                if (rtnPage) {
                  tabVal = rtnPage.value;
                }
                var sfString = 'false';
                var searchFlag = document.getElementById('sf');
                if (searchFlag) {
                   if (searchFlag == 'true') { sfString = 'true'; }
                }
                var idVal = 0;
                var orderId = document.getElementById('oid');
                if (orderId) {
                    idVal = orderId.value;
                }
                document.forms[0].action = '<html:rewrite page="/orderCancel.do"/>' + '?id=' + idVal + '&rp=' + tabVal + '&cp=' + cpVal + '&sf=' + sfString;
                document.forms[0].submit();
                $(this).dialog("close");
            },
            "No": function() {
                $(this).dialog("close");
            }
        }
    });
    $("#cancelopenitems").click(function() {
        $("#dialog-confirm-cancelopenitems").dialog("open");
        return false;
    });
});
</script>
<div>
<div class="holder">
<div id="dialog-confirm-cancelopenitems" title="Cancel Open Items?">
    <p>Are you sure you want to cancel this order?</p>
</div>
<div id="dialog-confirm-cancellineitem" title="Cancel Line Item?">
    <p>Are you sure you want to cancel this line item?</p>
</div>
<div id="dialog-confirm-uncancellineitem" title="Uncancel Line Item?">
    <p>Are you sure you want to uncancel this line item?</p>
</div>
<div id="main">
<a id="link_back_to_view_orders" href="<bean:write name='orderViewActionForm' property='backLink' />" class="icon-back">Back to view <bean:write name='orderViewActionForm' property='backLinkText' /></a>
<br /><br />
<logic:greaterThan value="0" name="orderViewActionForm" property="numberOfOrderItems">
<div class="inbox07">
<bean:define id="processType">ORDER_VIEW</bean:define>
<bean:define id="popupTitle">Rightsholder Terms</bean:define>
  <security:ifAdminResourceRequested></security:ifAdminResourceRequested>
  <util:else>	
     <logic:equal name="orderViewActionForm" property="cartEmpty" value="false">
        <logic:equal name="orderViewActionForm" property="academic" value="true">
            <a href="javascript:copyAcademic()" class="btn-yellow2 pad001">Copy order</a>
         </logic:equal>
         <logic:equal name="orderViewActionForm" property="academic" value="false">
            <a href="javascript:copyNonacademic(<bean:write name='orderViewActionForm' property='id'/>)" class="btn-yellow2 pad001">Copy order</a>
         </logic:equal>
     </logic:equal>
     <!-- TODO: What do I do if the user adds something to the cart after loading this page? -->
     <logic:equal name="orderViewActionForm" property="cartEmpty" value="true">
        <a href="javascript:copy(<bean:write name='orderViewActionForm' property='id'/>)" class="btn-yellow2 pad001">Copy order</a>
     </logic:equal>
      <logic:equal name="orderViewActionForm" property="cancelable" value="true">
        <a href="#" class="btn-yellow2 pad001" id="cancelopenitems">Cancel Open Items</a>
      </logic:equal>
      <logic:equal name="orderViewActionForm" property="closed" value="false">
        <a href="<html:rewrite page="/search.do?operation=show&page=simple&exists=1"/>&id=<bean:write name='orderViewActionForm' property='id'/>" class="btn-yellow2">Add another item to this order</a><br clear="all" />
      </logic:equal>      
    </util:else>
    </logic:greaterThan>
<!-- Cascade Errors -->
<logic:equal name="orderViewActionForm" property="hasAnyCascadeError" value="true">
<br />
    <div style="width:100%" align="center">
        <div class="calloutbox" style="width:80%" align="left">
            <p>
                <logic:notEmpty name="orderViewActionForm" property="cascadeErrors">
                    <b>NOTE:</b> <bean:message key="ov.error.cascadeUpdate"/>
                    <br><br>
                    <logic:iterate name="orderViewActionForm" property="cascadeErrors" id="errorString">
                        <bean:write name="errorString" /><br>
                    </logic:iterate>
                    <br>
                </logic:notEmpty>
                <logic:notEmpty name="orderViewActionForm" property="cascadeClosedErrors">
                    <b>NOTE:</b> <bean:message key="ov.error.cascadeUpdateClosed"/>
                    <br><br>
                    <logic:iterate name="orderViewActionForm" property="cascadeClosedErrors" id="closedErrorString">
                        <bean:write name="closedErrorString" /><br>
                    </logic:iterate>
                </logic:notEmpty>
                <logic:notEmpty name="orderViewActionForm" property="cascadeCancelErrors">
                    <b>NOTE:</b> <bean:message key="ov.error.cascadeItemInvoiced"/>
                    <br><br>
                    <logic:iterate name="orderViewActionForm" property="cascadeCancelErrors" id="invoicedErrorString">
                        <bean:write name="invoicedErrorString" /><br>
                    </logic:iterate>
                </logic:notEmpty>
                <logic:notEmpty name="orderViewActionForm" property="cascadeEditErrors">
                    <b>NOTE:</b> <bean:message key="ov.error.cascadeEditItemInvoiced"/>
                    <br><br>
                    <logic:iterate name="orderViewActionForm" property="cascadeEditErrors" id="editInvoicedErrorString">
                        <bean:write name="editInvoicedErrorString" /><br>
                    </logic:iterate>
                </logic:notEmpty>
            </p>
        </div>
        <br>
    </div>
</logic:equal>
<!-- END Cascade Errors -->
<logic:greaterThan value="0" name="orderViewActionForm" property="numberOfOrderItems">
    <h2><br /><br /><span class="bold">Confirmation Number:</span> <bean:write name="orderViewActionForm" property="id"/><br>
    <span class="bold">Order Date:</span> <bean:write name="orderViewActionForm" property="orderDateStr"/></h2></div>
<div class="inbox08">
<img src="<html:rewrite href='/media/images/printer.gif'/>" width="23" height="18" /><a href="printOrder.do?id=<bean:write name="orderViewActionForm" property="id" />" target="_blank">Print this page</a><br></div>
<div class="inbox09"><a href="reviewCoiTermsConfirm.do?confirmNum=<bean:write name="orderViewActionForm" property='id'/>" target="_blank"> Print terms &amp; conditions</a><br>
<a href="showCoiCitation.do?confirmNum=<bean:write name="orderViewActionForm" property='id'/>&showCitation=TRUE" target="_blank">Print citation information</a>
<util:contextualHelp titleName="orderViewActionForm" titleProperty="citationTitle" bodyName="orderViewActionForm" bodyProperty="citationText" rollover="true"><u>(What's this?)</u></util:contextualHelp>
</div>
<div class="horiz-rule"></div>
<div class="clearer"></div>
<a href="#" id="modalDialogPosition">&nbsp;</a>
<h2>Customer Information</h2>
<div class="item-details">
    <p class="indent-1">
      <logic:notEmpty name="orderViewActionForm" property="custName">
      <b>Customer:</b> <bean:write name="orderViewActionForm" property="custName"/><br />
      </logic:notEmpty>
      <logic:notEmpty name="orderViewActionForm" property="acctNumber">
      <b>Account Number:</b> <bean:write name="orderViewActionForm" property="acctNumber"/><br />
      </logic:notEmpty>
      <logic:notEmpty name="orderViewActionForm" property="custCompany">
      <b>Organization:</b> <bean:write name="orderViewActionForm" property="custCompany"/> <br />
      </logic:notEmpty>
      <logic:notEmpty name="orderViewActionForm" property="custEmail">
      <b>Email:</b> <bean:write name="orderViewActionForm" property="custEmail"/><br />
      </logic:notEmpty>
      <logic:notEmpty name="orderViewActionForm" property="custPhone">
      <b>Phone:</b> <bean:write name="orderViewActionForm" property="custPhone"/><br />
      </logic:notEmpty>
      <logic:notEmpty name="orderViewActionForm" property="poNumber">
      <b>PO#:</b> <bean:write name="orderViewActionForm" property="poNumber"/><br />
      </logic:notEmpty>
      <logic:notEmpty name="orderViewActionForm" property="promoCode">
      <b>Promotion code:</b> <bean:write name="orderViewActionForm" property="promoCode"/>
      </logic:notEmpty>
    </p>
</div>
</logic:greaterThan>
<logic:equal name="orderViewActionForm" property="specialOrder" value="true">
<logic:equal name="orderViewActionForm" property="CCOrder" value="true">
<logic:notEmpty name="orderViewActionForm" property="billingAddress">
<div class="item-details indent1">
   <p>
   <b>Special Order Invoices billed to:</b><br />
   <!-- TODO: Ask Gen how to filter the billingAddress for scripts -->
   <bean:write name="orderViewActionForm" property="billingAddress" filter="false"/>
   </p>
</div>
</logic:notEmpty>
</logic:equal>
</logic:equal>
<div class="horiz-rule"></div>
<bean:define id="termsTitle">More Info</bean:define>
<form id="orderViewActionForm" name="orderViewActionForm" action="processOrderView.do" method="post">
<html:hidden name="orderViewActionForm" property="search"/>
<html:hidden styleId="oid" name="orderViewActionForm" property="id"/>
<html:hidden styleId="crtEmpty" name="orderViewActionForm" property="cartEmpty"/>
<html:hidden styleId="cartAcademic" name="orderViewActionForm" property="cartAcademic"/>
<html:hidden name="orderViewActionForm" property="specIndex"/>
<html:hidden styleId="lastDir" name="orderViewActionForm" property="lastDir"/>
<html:hidden styleId="rp" name="orderViewActionForm" property="returnPage"/>
<html:hidden styleId="cp" name="orderViewActionForm" property="currentPage"/>
<html:hidden styleId="sf" name="orderViewActionForm" property="sf"/>
<!-- dynamic search/sort -->
<div class="box001 txtCenter" style="padding: 0 0 0 0;">
<table id="searchBar" align="center" style="padding: 10px 0 0 0;">
  <tr><td>Search order details by:&nbsp;</td>
    <td>
      <html:select styleId="srchFld" size="1" name="orderViewActionForm" property="searchOption" onchange="updateInputField();">
        <html:optionsCollection name="orderViewActionForm" property="searchOptionList" label="text" value="value"/>
      </html:select>
    </td>
    <td>
      <html:text styleId="srchTxt" name="orderViewActionForm" property="searchText" size="18" maxlength="250"/>
      <html:select styleId="permType" size="1" name="orderViewActionForm" property="permissionType">
        <html:optionsCollection name="orderViewActionForm" property="permissionTypeList" label="text" value="value"/>
      </html:select>
      <html:select styleId="permStatus" size="1" name="orderViewActionForm" property="permissionStatus">
        <html:optionsCollection name="orderViewActionForm" property="permissionStatusList" label="text" value="value"/>
      </html:select>
      <html:select styleId="billStatus" size="1" name="orderViewActionForm" property="billingStatus">
        <html:optionsCollection name="orderViewActionForm" property="billingStatusList" label="text" value="value"/>
      </html:select>
      <table id="dateFlds" style="border-collapse:collapse; padding:0;">
        <tr>
          <td align="right" style="vertical-align:middle; padding-right:3px;">Start Date:&nbsp;</td>
          <td style="vertical-align:middle;">
            <html:text styleId="beginDate" name="orderViewActionForm" property="beginDate" size="10" maxlength="10"/>
          </td>
        </tr>
        <tr>
             <td align="right" style="vertical-align:middle; padding-right:3px;">End Date:&nbsp;</td>
             <td style="vertical-align:middle;">
               <html:text styleId="endDate" name="orderViewActionForm" property="endDate" size="10" maxlength="10"/>
             </td>
        </tr>
      </table>
    </td>
    <td>
      <input type="image" style="vertical-align:middle;" value="submit" src="<html:rewrite href='/media/images/btn_go.gif'/>" width="40" height="26"/>
    </td>
    </tr></table></div>
<logic:greaterThan name="orderViewActionForm" property="detailCount" value="1">
<div class="box01">
    Sort order details by:
    <html:select styleId="sortFld" size="1" name="orderViewActionForm" property="sortOption" onchange="javascript:showSortRadios();">
       <html:optionsCollection name="orderViewActionForm" property="sortOptionList" label="text" value="value"/>
    </html:select>
    <span id="radio"><html:radio name="orderViewActionForm" property="direction" value="ascending" onclick="javascript:newSort(this);"/>Ascending&nbsp;
    <html:radio styleId="DSC" name="orderViewActionForm" property="direction" value="descending" onclick="javascript:newSort(this);"/>Descending</span>
</div>
</logic:greaterThan>
<!-- END dynamic search/sort -->
</form>
<logic:empty name="orderViewActionForm" property="orderDetails">
<div class="results">Your search selection found no matches</div>
<!--div class="rule"/-->
</logic:empty>

<div class="clearer"></div>
<logic:greaterThan value="0" name="orderViewActionForm" property="numberOfOrderItems">
<div style="text-align:center"><h1><b>This not an invoice</b></h1></div>
<P class="largertype bold" style="HEIGHT: 10px">Order Details</P>
</logic:greaterThan>
<!-- Begin Order License Detail Iteration -->
<logic:equal name="orderViewActionForm" property="showHeaderSection" value="true"> 
<h3>Course items</h3>
</logic:equal>
<logic:equal value="true" name="orderViewActionForm" property="hasAcademicItems">
<logic:notEmpty  name="orderViewActionForm" property="orderBundle">
   	<div class="choise-block">
						<div class="tilte">
							<strong>Course: <logic:notEmpty name="orderViewActionForm" property="orderBundle.courseName">
							<bean:write name="orderViewActionForm" property="orderBundle.courseName" />
							</logic:notEmpty> </strong>
							<logic:equal name="orderViewActionForm" property="closed" value="false">
							<logic:equal name="orderViewActionForm" property="courseInfo" value="true">
							<ul>
								<li>
								<html:link action="/editCourseInfo.do" name="orderViewActionForm" property="editCourseInfoMap" >Edit Course</html:link>
								</li></ul>
						</logic:equal>
							</logic:equal>
						</div>
							<div class="parameter-list">
							<dl>
							  <dt>University/Institution: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.organization">&nbsp;</logic:empty>
							  <bean:write name="orderViewActionForm" property="orderBundle.organization" />
							  </dd>
                        	 <dt>Start of term: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.startOfTerm">&nbsp;</logic:empty>
                        	 <bean:write name="orderViewActionForm" property="orderBundle.startOfTerm" format="MM/dd/yyyy"/>
                        	 </dd>
                             <dt>Course number: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.courseNumber">&nbsp;</logic:empty>
                             <bean:write name="orderViewActionForm" property="orderBundle.courseNumber" /></dd>
                             <dt>Number of students: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.numberOfStudents">&nbsp;</logic:empty>
                             <bean:write name="orderViewActionForm" property="orderBundle.numberOfStudents" /></dd>
							</dl>
							<dl>
							<dt>Instructor: </dt><dd><logic:empty name="orderViewActionForm" property="orderBundle.instructor">&nbsp;</logic:empty>
							<bean:write name="orderViewActionForm" property="orderBundle.instructor" /></dd>
                        <dt>Your reference: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.yourReference">&nbsp;</logic:empty>
                        <bean:write name="orderViewActionForm" property="orderBundle.yourReference" /></dd>
                        <dt>Accounting reference: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.accountingReference">&nbsp;</logic:empty>
							  <bean:write name="orderViewActionForm" property="orderBundle.accountingReference" /></dd>
                        <dt>Order entered by: </dt><dd>
							  <logic:empty name="orderViewActionForm" property="orderBundle.orderEnteredBy">&nbsp;</logic:empty>
							  <bean:write name="orderViewActionForm" property="orderBundle.orderEnteredBy" />
                        			</dd>
							</dl>
						</div>
						</div>
</logic:notEmpty>
</logic:equal>
<bean:define id="academicRegularBoxStyle">border-top:0px;</bean:define>
<logic:equal value="true" name="orderViewActionForm" property="hasAcademicItems">
<logic:iterate id="item" name="orderViewActionForm" property="orderDetails" indexId="index">
<logic:equal name="item" property="academic" value="true"> 
	            <logic:equal name="item" property="unresolvedSpecialOrder" value="false"> <!-- Filter Out Special Order -->
	            <div class="product-box" style="<%=academicRegularBoxStyle%>">
	                <%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/academicOrder.jsp" %>
	                </div>	
				 <bean:define id="academicRegularBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	                </logic:equal> <!--Filter Out Special Orders -->
	               </logic:equal> 
</logic:iterate>
	            <logic:equal value="true" name="orderViewActionForm" property="hasAcademicSpecialOrders">
	            <bean:define id="academicSpecialBoxStyle">border-top:0px;</bean:define>
	                <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Special Orders</strong>
						</div>      
	            <logic:iterate name="orderViewActionForm" property="orderDetails" id="item" indexId="index">
	            <logic:equal name="item" property="academic" value="true">
	            <logic:equal name="item" property="unresolvedSpecialOrder" value="true">  <!-- Filter in Special Order -->
	            <div class="product-box" style="width:688px;<%=academicSpecialBoxStyle%>">
	             <%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/academicOrder.jsp" %>
	             </div>	
	             <bean:define id="academicSpecialBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	             <br/>
	                </logic:equal> <!--Filter Out Special Orders -->
	                </logic:equal>
	            </logic:iterate>
	                </div>        
	            </logic:equal>
	   </logic:equal> 
            <logic:equal value="true" name="orderViewActionForm" property="showHeaderSection">
			<logic:equal value="false" name="orderViewActionForm" property="isFilterOn">
                     <div class="total" style="border-top:1px solid #CCCCCC;">
						<strong class="number" style="color:#666666">Course items: <span><bean:write name="orderViewActionForm" property="numberOfAcademicOrders" /></span></strong>
                    			<strong class="total-price" style="color:#666666">Course total: <span ><bean:write name="orderViewActionForm" property="courseTotal" /></span>
                    			<logic:equal name="orderViewActionForm" property="containsTBDAcademicOrder" value="true">
                        		<br/><div style="text-align:right;font-size:12px;color:#666666;">(Excludes $TBD items)</div>
                    			</logic:equal>
                    			</strong>
					</div>
					</logic:equal>
     		<h3>Single Items</h3>
        	</logic:equal> 
<logic:equal value="true" name="orderViewActionForm" property="hasNonAcademicItems">
            <!-- *************** BEGIN Regular Order Items *********************** -->         
		<bean:define id="nonAcademicRegularBoxStyle">border-top:0px;</bean:define>
        <logic:iterate name="orderViewActionForm" property="orderDetails" id="item" indexId="index">
	            <logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
	              <logic:equal name="item" property="unresolvedSpecialOrder" value="false"> <!-- Filter Out Special Order -->
                	<logic:equal name="item" property="reprint" value="false">  <!-- Filter Out Non  Special Order -->
	              <div class="product-box" style="<%=nonAcademicRegularBoxStyle%>">
	  					<%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/nonAcademicOrder.jsp" %>
	  					</div>
	  					<bean:define id="nonAcademicRegularBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
	  				</logic:equal> <!--Filter Out Reprints -->
	  			   </logic:equal> <!--Filter Out Special Orders -->
	             </logic:equal>                
	        </logic:iterate>
            <logic:equal value="true" name="orderViewActionForm" property="hasNonReprintSpecialOrders">
           <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Special Orders</strong>
						</div>
						<bean:define id="nonAcademicSpecialBoxStyle">border-top:0px;</bean:define>
        <logic:iterate name="orderViewActionForm" property="orderDetails" id="item" indexId="index">
        <logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
             <logic:equal name="item" property="unresolvedSpecialOrder" value="true">  <!-- Filter Out Special Order -->
                <logic:equal name="item" property="reprint" value="false">  <!-- Filter Out Non  Special Order -->
	             <div class="product-box" style="width:688px;<%=nonAcademicSpecialBoxStyle%>">   
								<%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/nonAcademicOrder.jsp" %>
                 </div>
                 <bean:define id="nonAcademicSpecialBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
                 <br/>
                </logic:equal> 
                </logic:equal> 
			</logic:equal>               
            </logic:iterate>
                </div>   
            </logic:equal>
            <logic:equal value="true" name="orderViewActionForm" property="hasReprintOrders">
            <div class="choise-block" style="padding: 4px 0px 0px;"> 
						<div class="tilte" style="padding-left: 4px; margin: 0px;">
							<strong>Reprints</strong>
						</div>                               
            <bean:define id="rePrintBoxStyle">border-top:0px;</bean:define>
           <logic:iterate name="orderViewActionForm" property="orderDetails" id="item" indexId="index">
        		<logic:equal name="item" property="academic" value="false"> <!-- Non Academic Orders -->
                <logic:equal name="item" property="reprint" value="true">  <!-- Filter Out Non  Special Order -->
                <div class="product-box" style="width:688px;<%=rePrintBoxStyle%>">
                <%@ include file = "/WEB-INF/jsp-modules/orderhistory/tiles/nonAcademicOrder.jsp" %>
                  </div>
                  <bean:define id="rePrintBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
                  <br/>
                </logic:equal> <!--Filter Out Reprints -->
                </logic:equal>
                                                                       
            </logic:iterate>
                </div>   
            </logic:equal>
 </logic:equal>
		<logic:equal value="true" name="orderViewActionForm" property="showHeaderSection">
    		<logic:equal value="false" name="orderViewActionForm" property="isFilterOn">
          			<div class="total" style="border-top:1px solid #CCCCCC;">
						<strong class="number"  style="color:#666666">Single items: <span><bean:write name="orderViewActionForm" property="numberOfNonAcademicOrders" /></span></strong>
                    			<strong class="total-price" style="color:#666666">Single item total : <span ><bean:write name="orderViewActionForm" property="singleItemTotal" /></span>
 								<logic:equal name="orderViewActionForm" property="containsTBDNonAcademicOrder" value="true">
                        		<br/><div style="text-align:right;font-size:12px;color:#666666;">(Excludes $TBD items)</div>
                    			</logic:equal>                   			
                    			</strong> 
					</div>
        </logic:equal>
		</logic:equal> 
<logic:greaterThan value="0" name="orderViewActionForm" property="numberOfOrderItems">
<div class="total-sum">

<logic:equal value="false" name="orderViewActionForm" property="isFilterOn">
<div class="order_total">
<div class="floatleft">
<h2 style="margin-top:0px">Total order items:&nbsp;	<label class="largetype"><bean:write name="orderViewActionForm" property="numberOfOrderItems" /></label>	
                		</h2>
</div>
<div class="floatright largertype bold" style="font-size:16px;margin-top:0px;">
Order Total: <span class="largetype"><bean:write name="orderViewActionForm" property="orderTotal"/></span>
<logic:equal name="orderViewActionForm" property="containsTBDOrder" value="true">
                        		<br/><span style="font-size:12px;color:#666666;">(Excludes $TBD items)</span>
                    			</logic:equal>
</div>
<div class="clearer"></div>	
</div>
</logic:equal>
</div>
</logic:greaterThan>
</div><!--  class="holder">-->
 </div><!-- id="main">-->
</div>

<jsp:useBean id="licenseDeclineForm" scope="session" class="com.copyright.ccc.web.forms.LicenseDeclineForm"/>

<div id="declineSpecialOrderModal" title="Decline License (Order Detail Id:)" style="display:none" >
<p align="left"><strong>Please select the reason that best applies for declining the license:</strong></p>
	<p align="left"><input type="radio" name="declineSpOrder"  class="declineSpOrderClass"  value="<%= DeclineReasonEnum.TOO_EXPENSIVE.name() %>"/>Too expensive</p>
	<p align="left"><input type="radio" name="declineSpOrder"  class="declineSpOrderClass"  value="<%= DeclineReasonEnum.TOOK_TOO_LONG.name() %>"/>Response took too long</p>
	<p align="left"><input type="radio" name="declineSpOrder"  class="declineSpOrderClass"  value="<%= DeclineReasonEnum.USED_ALTERNATE.name() %>"/>Used alternate work</p>
	<p  align="left"><input type="radio" name="declineSpOrder"  class="declineSpOrderClass" value="<%= DeclineReasonEnum.NO_LONGER_NEED.name() %>"/>Permission no longer needed</p>
	<p  align="left"><input type="radio" name="declineSpOrder"  class="declineSpOrderClass" value="<%= DeclineReasonEnum.RIGHTS_NO_GOOD.name() %>"/>The rights available did not meet my needs</p>
	<p  align="left"><input type="radio" name="declineSpOrder"  class="declineSpOrderClass" value="<%= DeclineReasonEnum.GOT_DIRECTLY.name() %>"/>Obtained permission directly from the rightsholder</p>
	<p  align="left"><input type="radio" name="declineSpOrder"  class="declineSpOrderClass" value="<%= DeclineReasonEnum.ALTER_REQUEST.name() %>"/>I need to alter my original request</p>
	<p  align="left"><input type="radio" name="declineSpOrder"  class="declineSpOrderClass" value="<%= DeclineReasonEnum.OTHER.name() %>"/>Other</p>
	
	<html:form action="licenseDecline.do" styleId="licenseDeclineFrm">
	 <html:hidden styleId="selectedSpecialOrderDetailId" value="" name="licenseDeclineForm" property="licenseId" />
	  <html:hidden styleId="selectedDeclineReason" value="" name="licenseDeclineForm" property="declineCd" />
	   <html:hidden styleId="selectedItemPrice" value="" name="licenseDeclineForm" property="totalAmount" />
	</html:form>
</div>

<!-- Dialog to show Accept -->
<div id="acceptSpecialOrderModal" title="Accept License (Order Detail Id:)"  style="display:none" >
<div id="acceptSpecialOrderModal_Content" align="left">
<p>Loading.....</p>
</div>
<div id="accept_specialOrder_payment_processing_msg" align="left"></div>
</div>


<div id="paymentProcessNotifyModal" title="Accept License Confirmation"   style="display:none" >
<div id="paymentProcessNotify_Content"  align="left">
<p>Loading.....</p>
</div>
</div>


<script type="text/JavaScript">
  //window.onload = initPage;
  $(document).ready(function() {
      setUpContextualHelpRollovers();
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
   function toggleDetails(link,divBlock){
		var liBlock='li_'+divBlock;
		if($('#'+divBlock).css('display')=='none'){
			$('#'+liBlock).removeClass("collapseDetails");
			$('#'+liBlock).addClass("expandDetails");
			$(link).text('Hide details');
		}else{
			$('#'+liBlock).removeClass("expandDetails");
			$('#'+liBlock).addClass("collapseDetails");
			$(link).text('View details');
		}
				$('#'+divBlock).toggle();
	}
  </script>
