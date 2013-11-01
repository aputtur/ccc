<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/cc2-util.tld" prefix="util"%>
<%@ page errorPage="/jspError.do" %>
<%@ page import="com.copyright.ccc.business.services.ItemConstants" %>
<%@ page import="com.copyright.svc.order.api.data.DeclineReasonEnum" %>
<link type="text/css" href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" />


<style type="text/css">
hr { height:1px; color: #999999; }
#searchBar td {
   vertical-align:middle; 
}
</style>
<script type="text/javascript" language="javascript" src="<html:rewrite page='/resources/commerce/js/dropdown.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/resources/commerce/js/util.js'/>"></script>
<script type="text/javascript" language="javascript" src="<html:rewrite page='/resources/commerce/js/orderDetails.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>"></script>

<link href="<html:rewrite page="/resources/commerce/css/impromptu.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/jquery.form.js"/>"></script>
<script src="<html:rewrite page="/resources/commerce/js/paymentScripts.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/accept_decline_specialorder.js"/>" type="text/javascript"></script>
<script type="text/javascript" src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-impromptu.2.8.js"/>" ></script>

<div id="calendarBox" STYLE="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
<script type="text/javascript">
 if( typeof addOnLoadEvent == 'function' && typeof initPage == 'function' ) addOnLoadEvent( initPage );

</script>

<h1>Order History</h1>
<!--a href="#" class="icon-back floatleft">Back to Manage Account</a> <br /-->
  <%
                  //Cannot copy popup
                  String txtCopy  = null;
                 txtCopy = "This order detail cannot be copied. To purchase this type of use for this item again, please search for the publication and view the permission options.";
                  pageContext.setAttribute("txtCopyBody", txtCopy, PageContext.PAGE_SCOPE);
                  
                  
               //Credit Card Uncharged
              String txtCreditCardWillMoreInfo = null;
              txtCreditCardWillMoreInfo = "RightsLink is a rights management service from Copyright Clearance Center. You will receive a charge on your credit card from RightsLink within 24 hours.";
              pageContext.setAttribute("ccBodyWill", txtCreditCardWillMoreInfo, PageContext.PAGE_SCOPE);
              
              //Invoice Uncharged
              String txtInvoiceWillMoreInfo = null;
              txtInvoiceWillMoreInfo = "RightsLink is a rights management service from Copyright Clearance Center. The invoice for this item will be sent directly from RightsLink within 24 hours.";
               pageContext.setAttribute("invBodyWill", txtInvoiceWillMoreInfo, PageContext.PAGE_SCOPE);
               
               //Credit Card Charged
              String txtCreditCardWasMoreInfo = null;
              txtCreditCardWasMoreInfo = "RightsLink is a rights management service from Copyright Clearance Center.Your credit card will show a charge from RightsLink";
              pageContext.setAttribute("ccBodyWas", txtCreditCardWasMoreInfo, PageContext.PAGE_SCOPE);
              
              //Invoice Charged
              String txtInvoiceWasMoreInfo = null; 
              txtInvoiceWasMoreInfo = "RightsLink is a rights management service from Copyright Clearance Center. The invoice for this item was sent directly from RightsLink";
              pageContext.setAttribute("invBodyWas", txtInvoiceWasMoreInfo, PageContext.PAGE_SCOPE);
              
              
              //RLS No Payment Specified & RLS no payment specified
              String txtRLSWillMoreInfo = null;
              txtRLSWillMoreInfo = "RightsLink is a separate rights management service from Copyright Clearance Center. All notifications about the status of your reprint order will come from the RightsLink Service."+
              "  However you can always view your order details under Order History on copyright.com.(Note: You will be able to choose whether to pay via credit card or invoice when your order is fulfilled.) If you need to alter this item while it is still in \"Not billed\" status please contact Customer Service: by phone: (978)646-2600(Monday- Friday, 8:00am - 6:00pm ET), by email:info@copyright.com or start a Live chat session ";
               pageContext.setAttribute("rlsBodyWill", txtRLSWillMoreInfo, PageContext.PAGE_SCOPE);
               
             //RLR No Payment Specified & RLS no payment specified
              String txtRLRWillMoreInfo = null;
              txtRLRWillMoreInfo = "RightsLink is a separate rights management service from Copyright Clearance Center. All notifications about the status of your reprint order will come from the RightsLink Service."+
              "  However you can always view your order details under Order History on copyright.com. If you need to alter this item while it is still in \"Not billed\" status please contact Customer Service: by phone: (978)646-2600(Monday- Friday, 8:00am - 6:00pm ET), by email:info@copyright.com or start a Live chat session ";
               pageContext.setAttribute("rlrBodyWill", txtRLRWillMoreInfo, PageContext.PAGE_SCOPE);
               

            
            %>
<div class="clearer"></div>
<a href="#" id="modalDialogPosition">&nbsp;</a>
<logic:equal name="orderDetailActionForm" property="rlUser" value="true">
	<div id="tabs">
	  <ul>
	    <li><a href="orderHistory.do" id="link_view_orders"><span>View Orders</span></a></li>
	    <li><a href="#" id="link_view_order_details" class="active"><span>View Order Details</span></a></li>
	    <li><a href="rlOrderHistory.do" id="link_view_RightsLink_orders"><span>View RIGHTSLINK Orders</span></a></li>
	  </ul>
	</div>
</logic:equal>

<logic:notEqual name="orderDetailActionForm" property="rlUser" value="true">
	<div id="tabs">
	  <ul>
	    <li><a href="orderHistory.do" id="link_view_orders"><span>View Orders</span></a></li>
	    <li><a href="#" id="link_view_order_details" class="active"><span>View Order Details</span></a></li>
	  </ul>
	</div>
</logic:notEqual>
<!-- Begin History Order -->
<div style="padding:15px"></div>
<div id="order-history-widecontent">
<form id="orderDetailActionForm" name="orderDetailActionForm" action="processOrderDetail.do" method="post">
<html:hidden styleId="currPage" name="orderDetailActionForm" property="currentPage"/>
<html:hidden name="orderDetailActionForm" property="startPage"/>
<html:hidden styleId="lastDir" name="orderDetailActionForm" property="lastDir"/>
<html:hidden name="orderDetailActionForm" property="searchPage" value="true"/>
<html:hidden styleId="cartAcademic" name="orderDetailActionForm" property="cartAcademic"/>
<bean:define id="termsTitle">More Info</bean:define>
<bean:define id="copyTitle">Copy Title</bean:define>


<br/>
<div class="clearer"></div>
<div class="history-order-box txtRight">
<table id="searchBar" align="center" style="padding: 10px 0 0 0;">
<tr><td></td></tr>
  <tr><td>Search Order details by:&nbsp;</td>
    <td>
      <html:select styleId="srchFld" size="1" name="orderDetailActionForm" property="searchOption" onchange="updateInputField();">
        <html:optionsCollection name="orderDetailActionForm" property="searchOptionList" label="text" value="value"/>
      </html:select>
    </td>
    <td>
      <html:text styleId="srchTxt" name="orderDetailActionForm" property="searchText" size="18" maxlength="250"/>
      <html:select styleId="permType" size="1" name="orderDetailActionForm" property="permissionType" onchange="focusGo();">
        <html:optionsCollection name="orderDetailActionForm" property="permissionTypeList" label="text" value="value"/>
      </html:select>
      <html:select styleId="permStatus" size="1" name="orderDetailActionForm" property="permissionStatus" onchange="focusGo();">
        <html:optionsCollection name="orderDetailActionForm" property="permissionStatusList" label="text" value="value"/>
      </html:select>
      <html:select styleId="billStatus" size="1" name="orderDetailActionForm" property="billingStatus" onchange="focusGo();">
        <html:optionsCollection name="orderDetailActionForm" property="billingStatusList" label="text" value="value"/>
      </html:select>
      <html:select styleId="specialOrderUpdate" size="1" name="orderDetailActionForm" property="specialOrderUpdate" onchange="focusGo();">
        <html:optionsCollection name="orderDetailActionForm" property="specialOrderUpdateList" label="text" value="value"/>
      </html:select>
      <table id="dateFlds" style="border-collapse:collapse; padding:0;">
        <tr>
          <td style="vertical-align:middle; padding-right:3px;">Start Date</td>
          <td style="vertical-align:middle;">
            <html:text styleId="beginDate" name="orderDetailActionForm" property="beginDate" size="10" maxlength="12"/>
          </td>
        </tr>
        <tr>
             <td style="vertical-align:middle; padding-right:3px;">End Date</td>
             <td style="vertical-align:middle;">
               <html:text styleId="endDate" name="orderDetailActionForm" property="endDate" size="10" maxlength="12"/>
             </td>
        </tr>
      </table>
    </td>
    <td>
      <input id="goBtn" type="image" style="vertical-align:middle;" value="submit" src="<html:rewrite href='/media/images/btn_go2.gif'/>" width="40" height="26"/>
    </td>
    </tr></table>
</div>
<hr>
<logic:greaterThan name="orderDetailActionForm" property="totalItems" value="1">
<!-- Sort section -->
<div class="order-detail-line" style="padding:0 0 5px 0;">
    Sort orders by:
    <html:select size="1" styleId="sortFld" name="orderDetailActionForm" property="sortOption" onchange="javascript:setSortOrder(this);sortList();">
       <html:optionsCollection name="orderDetailActionForm" property="sortOptionList" label="text" value="value"/>
    </html:select>
    <html:radio name="orderDetailActionForm" property="direction" value="ascending" styleId="ASC" onclick="javascript:newSort(this);"/>Ascending&nbsp;
    <html:radio name="orderDetailActionForm" property="direction" value="descending" styleId="DSC" onclick="javascript:newSort(this);"/>Descending
</div>
<hr>
<!-- End Sort section -->
</logic:greaterThan>
<div class="order-history-line">

<logic:notEqual name="orderDetailActionForm" property="searchPage" value="true">
<div style="text-align:center;"><b>Enter search terms for the items you want to find.</b></div>
</logic:notEqual>
<div class="clearer"></div>
<br/>
<logic:equal name="orderDetailActionForm" property="searchPage" value="true">
<logic:greaterThan name="orderDetailActionForm" property="totalItems" value="0">
<util:searchResult bean="orderDetailActionForm"/>
</logic:greaterThan>
<logic:equal name="orderDetailActionForm" property="totalItems" value="0">
<div style="text-align: center; font-size: 13px;">
<bean:write name="orderDetailActionForm" property="searchEmptyString"/>
</div>
</logic:equal>
</logic:equal>

</div>
<div style="float:right; font-weight: normal;">
<util:scroller name="orderDetailActionForm" action="pageAction" currentPage="currentPage" startPage="startPage" resultsPerPage="resultsPerPage" totalPages="totalPages" scope="request"/>
</div>

</form>
<div class="clearer"></div>
</div>

<!-- Start of license iteration -->
<logic:iterate id="aLicense" name="orderDetailActionForm" property="displayLicenses" indexId="licenseIndex">
<logic:notEmpty name="aLicense" property="productCd">
<logic:notEqual name="aLicense" property="productCd" value="RLR">
<bean:define id="rlrProductCD" value='""'/>
</logic:notEqual>
<logic:equal name="aLicense" property="productCd" value="RLR">
<bean:define id="rlrProductCD" name="aLicense" property="productCd"/>
</logic:equal>
</logic:notEmpty>


<logic:empty name="aLicense" property="productCd">


<bean:define id="rlrProductCD" value='""'/>

</logic:empty>


<div class="order-detail-line"><!-- use detail line because of IE border-bottom render bug -->
    <h2 style="margin-top: 5px;">

    <logic:present name="aLicense"  property="wrWorkInst">
    <logic:notEmpty  name="aLicense" property="wrWorkInst">
	    <logic:notEqual name="aLicense" property="wrWorkInst" value="0">
	       <html:link action="/search.do?operation=detail" paramId="item" paramName="aLicense" paramProperty="wrWorkInst">
	       <bean:write name="aLicense" property="publicationTitle"/>
	       </html:link>
	    </logic:notEqual>
	    <logic:equal name="aLicense" property="wrWorkInst" value="0">
	           <a href="#"><bean:write name="aLicense" property="publicationTitle" /></a>
	    </logic:equal>
    </logic:notEmpty>
     <logic:empty  name="aLicense" property="wrWorkInst">
    		<a href="#"><bean:write name="aLicense" property="publicationTitle" /></a>
    </logic:empty>
    </logic:present>
    <logic:notPresent name="aLicense"  property="wrWorkInst">
   		 <a href="#"><bean:write name="aLicense" property="publicationTitle" /></a>
    </logic:notPresent>
    </h2>
    <div class="inbox01">
    Order Detail ID: <bean:write name="aLicense" property="ID"/>
    <br /><br />
    Part of order:
    <br /><b>CONFIRMATION #:</b> <a id="link_order_detail_<%=licenseIndex%>" href="orderView.do?id=<bean:write name='aLicense' property='purchaseId'/>&rp=detail&cp=<bean:write name='orderDetailActionForm' property='currentPage'/>"><bean:write name="aLicense" property="purchaseId"/></a>
    <br /><b>Order Date:</b> <bean:write name="aLicense" property="createDateStr"/>
    <!-- TODO: How do I get PO number? -->
    <!--br /><b>PO#:</b> <beanwrite name="aLicense" property="orderId"/-->
    
    </div>

    

    <div class="inbox04">
    <util:permStatus bean="aLicense"/><br />

<b>Permission type:&nbsp;</b><bean:write name="aLicense" property="categoryName" /><br />
        
   


<logic:equal name="aLicense" property="republication" value="true">

    <logic:notEmpty name="aLicense" property="republicationTitle">
    <b>Republication Title:</b> <bean:write name="aLicense" property="republicationTitle"/><br />
    </logic:notEmpty>
    <logic:notEmpty name="aLicense" property="republishingOrganization">
    <b>Republishing organization:</b> <bean:write name="aLicense" property="republishingOrganization"/><br />
    </logic:notEmpty>
    <logic:notEmpty name="aLicense" property="republicationDate">
    <b>Republication date:</b> <bean:write name="aLicense" property="republicationDateStr"/><br />
    </logic:notEmpty>
</logic:equal>

<logic:notEmpty name="aLicense" property="touName" >
   
   <b>Type Of Use:&nbsp;</b><bean:write name="aLicense" property="touName"/>
      
</logic:notEmpty>



    </div>

    <div class="inbox05">
    <b>Billing status:</b> <bean:write name="aLicense" property="billingStatus"/>
     <logic:notEmpty name="aLicense" property="paymentMethodCd">
      <logic:equal name="aLicense" property="paymentMethodCd" value="<%=ItemConstants.PAYMENT_METHOD_INVOICE %>">
    <logic:notEmpty name="aLicense" property="invoiceId">
    <br /><b>Invoice number:</b> <bean:write name="aLicense" property="invoiceId"/>
    </logic:notEmpty>
    </logic:equal>
    </logic:notEmpty>
    
    <logic:notEmpty name="aLicense" property="yourReference">
    <br />
    <logic:equal name="aLicense" property="academic" value="true"><b>Your line item reference:</b> </logic:equal>
    <logic:equal name="aLicense" property="academic" value="false"><b>Your reference:</b> </logic:equal>
    <bean:write name="aLicense" property="yourReference"/>
    </logic:notEmpty>
    <logic:equal name="aLicense" property="rightSourceCd" value="RL">
     <logic:notEmpty name="aLicense" property="jobTicketNumber">
     <br />
    <b>Job Ticket Number:</b>&nbsp;<bean:write name="aLicense" property="jobTicketNumber"/>
    </logic:notEmpty>
         <logic:notEmpty name="aLicense" property="externalDetailId">
         <br />
    <b>License Number:</b>&nbsp;<bean:write name="aLicense" property="externalDetailId"/>
    </logic:notEmpty>
        	<logic:notEmpty name="aLicense" property="orderRefNumber">
											<br/><b>Order ref number: </b>
	                                    		<bean:write name="aLicense" property="orderRefNumber" />
			</logic:notEmpty>
    
    </logic:equal>
  
    </div>

<br clear="all" /><br clear="all" />


    
    <logic:notEmpty name="aLicense" property="rightsQualifyingStatement">
    <img src="<html:rewrite href='/media/images/icon_alert.gif'/>" border="0" alt="alert">&nbsp;<b style="text-align: justify"><bean:write name="aLicense" property="rightsQualifyingStatement"/></b>&nbsp;&nbsp;
    </logic:notEmpty>

    <logic:notEmpty name="aLicense" property="externalCommentTerm">
    <util:contextualHelp titleName="orderDetailActionForm" titleProperty="popupTitle" bodyName="aLicense" bodyProperty="externalCommentTerm" rollover="false">Rightsholder terms apply</util:contextualHelp>
    </logic:notEmpty>
    

<div style="clear:left;">
<span style="float:right;font-size:13px;font-weight:bold;margin: 3px 0;">
<util:price bean="aLicense" label="Item price: "/>
</span>

<span style="float:left;margin: 3px 0;">
<logic:equal name="aLicense" property="rightSourceCd" value="RL">

<logic:equal name="rlrProductCD" value="RLR">
<nobr><b>Note:</b> This item will be managed and fulfilled through CCC's RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="rlrBodyWill" rollover="false">More info<br/></util:contextualHelp></nobr>
</logic:equal>


<logic:notEqual  name="rlrProductCD" value="RLR">
  <logic:notEmpty name="aLicense" property="specialOrder">
   <!-- Case RLS -->
    <logic:equal name="aLicense" property="specialOrder" value="true">
     <br/>
     <br/>
     <!-- Case RLS without Payment Method -->
     <logic:empty name="aLicense" property="paymentMethodCd">
     <nobr><b>Note:</b> This item will be managed through CCC's RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="rlsBodyWill" rollover="false">More I\info<br/></util:contextualHelp></nobr>
     </logic:empty>
     
     <!-- Case RLS with Payment Method -->
      <logic:notEmpty name="aLicense" property="paymentMethodCd">
      <!-- Case RLR with Payment Method As Credit Card-->
     <logic:equal name="aLicense" property="paymentMethodCd" value="<%=ItemConstants.PAYMENT_METHOD_CREDIT_CARD %>">
     
     <!-- Case RLS with unCharged Credit Card-->
    <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_NOT_BILLED_CD %>">
    <b>Note:</b> This item will be managed through CCC'S RightsLink service.<util:contextualHelp titleName="termsTitle" bodyName="rlsBodyWill" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
    
     <!-- Case RLS with Charged Credit Card-->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_BILLED_CD %>">
    <b>Note:</b> This item was charged to your credit card by CCC'S RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="ccBodyWas" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
   </logic:equal>
   
   <!-- Case RLS with Payment Method As Invoice-->
   <logic:equal name="aLicense" property="paymentMethodCd" value="<%=ItemConstants.PAYMENT_METHOD_INVOICE %>">
   
   <!-- Case RLS with Paid Invoice-->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_NOT_BILLED_CD %>">
    <b>Note:</b>This item will be managed through CCC'S RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="rlsBodyWill" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
    <!-- Case RLS with unPaid Invoice-->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_BILLED_CD %>">
    <b>Note:</b>This item was invoiced through CCC'S RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="invBodyWas" rollover="false">More info<br/></util:contextualHelp>
     </logic:equal>
    </logic:equal>
     </logic:notEmpty>
     </logic:equal>
     
     <!-- Case non RLS -->
     <logic:equal name="aLicense" property="specialOrder" value="false">
     <!-- Case payment with credit card -->
 <logic:equal name="aLicense" property="paymentMethodCd" value="<%=ItemConstants.PAYMENT_METHOD_CREDIT_CARD %>">
 <!-- Case not Billed to CC -->
    <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_NOT_BILLED_CD %>">
    <b>Note:</b> This item will be charged to your credit card through our RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="ccBodyWill" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
    <!-- Case  Billed  to CC-->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_BILLED_CD %>">
    <b>Note:</b> This item was charged to your credit card through our RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="ccBodyWas" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
   </logic:equal>
   <!-- Case payment with Invoice -->
   <logic:equal name="aLicense" property="paymentMethodCd" value="<%=ItemConstants.PAYMENT_METHOD_INVOICE %>">
   <!-- Case not billed to Invoice -->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_NOT_BILLED_CD %>">
    <b>Note:</b>This item will be invoiced separately through our RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="invBodyWill" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
    <!-- Case billed to Invoice -->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_BILLED_CD %>">
    <b>Note:</b>This item was invoiced separately through our RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="invBodyWas" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
   </logic:equal>
     </logic:equal>
</logic:notEmpty>
    	
    
   <br/>
     <br/>	
       <!-- Case non RLS -->    
   <logic:empty name="aLicense" property="specialOrder">
 
     <!-- Case payment with credit card -->
 <logic:equal name="aLicense" property="paymentMethodCd" value="<%=ItemConstants.PAYMENT_METHOD_CREDIT_CARD %>">
 <!-- Case not Billed to CC -->
    <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_NOT_BILLED_CD %>">
    <b>Note:</b> This item will be charged to your credit card through our RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="ccBodyWill" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
    <!-- Case  Billed  to CC-->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_BILLED_CD %>">
    <b>Note:</b> This item was charged to your credit card through our RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="ccBodyWas" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
   </logic:equal>
   <!-- Case payment with Invoice -->
   <logic:equal name="aLicense" property="paymentMethodCd" value="<%=ItemConstants.PAYMENT_METHOD_INVOICE %>">
   <!-- Case not billed to Invoice -->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_NOT_BILLED_CD %>">
    <b>Note:</b>This item will be invoiced separately through our RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="invBodyWill" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
    <!-- Case billed to Invoice -->
   <logic:equal name="aLicense" property="billingStatusCd" value="<%=ItemConstants.BILLING_STATUS_BILLED_CD %>">
    <b>Note:</b>This item was invoiced separately through our RightsLink service. <util:contextualHelp titleName="termsTitle" bodyName="invBodyWas" rollover="false">More info<br/></util:contextualHelp>
    </logic:equal>
   </logic:equal>
  
   </logic:empty>
   </logic:notEqual>
     
     
     
</logic:equal>

    <br/>
    <a href="orderView.do?id=<bean:write name='aLicense' property='purchaseId'/>&rp=detail&cp=<bean:write name='orderDetailActionForm' property='currentPage'/>">View entire order </a>
    <logic:notEqual name="aLicense" property="rightSourceCd" value="RL">
    
    <logic:equal name="aLicense" property="availableToCopy" value="true">
        <logic:equal name="orderDetailActionForm" property="cartEmpty" value="false">
            <logic:equal name="aLicense" property="academic" value="true">
               &nbsp;|&nbsp; <a href="javascript:copyLicAcademic(<bean:write name='aLicense' property='purchaseId'/>,<bean:write name='aLicense' property='ID'/>);">Copy</a>
            </logic:equal>
    
            <logic:equal name="aLicense" property="academic" value="false">
            	<logic:notEqual name="aLicense" property="productCd" value="RLS">
                	&nbsp;|&nbsp;<a href="javascript:copyLicNonacademic(<bean:write name='aLicense' property='purchaseId'/>,<bean:write name='aLicense' property='ID'/>);">Copy</a>
                </logic:notEqual>
            </logic:equal>
        </logic:equal>
    
        <logic:equal name="orderDetailActionForm" property="cartEmpty" value="true">
            &nbsp;|&nbsp;<a href="javascript:copy(<bean:write name='aLicense' property='purchaseId'/>,<bean:write name='aLicense' property='ID'/>);">Copy</a>
        </logic:equal>
        </logic:equal>
</logic:notEqual>
         <logic:equal name="aLicense" property="rightSourceCd" value="RL">
         &nbsp;|&nbsp; <util:contextualHelp titleName="copyTitle" bodyName="txtCopyBody" rollover="false">Why Can't I copy this order detail?<br/></util:contextualHelp>
         </logic:equal>
       <logic:notEqual name="aLicense" property="rightSourceCd" value="RL">
        <logic:equal name="aLicense" property="billingStatusCd" value="NOT_BILLED">
         
        <logic:equal name="aLicense" property="unresolvedSpecialOrder" value="false">
        &nbsp;|&nbsp;<a href="editBasicLicense.do?id=<bean:write name='aLicense' property='ID'/>&rp=detail&cp=<bean:write name='orderDetailActionForm' property='currentPage'/>">Edit</a>
        </logic:equal>
    
        <logic:equal name="aLicense" property="unresolvedSpecialOrder" value="true">
        &nbsp;|&nbsp;<a href="editSpecialLicense.do?id=<bean:write name='aLicense' property='ID'/>&rp=detail&cp=<bean:write name='orderDetailActionForm' property='currentPage'/>">Edit</a>
        </logic:equal>
        </logic:equal> 
        </logic:notEqual>
        
               
        
</span>
</div>

<logic:equal name="aLicense" property="billingStatusCode" value="<%=ItemConstants.BILLING_STATUS_AWAITING_LCN_CONFIRM%>">
<div style="clear:right;">
<span style="float:right;margin: 3px 0;">
<logic:equal name="orderDetailActionForm" property="isAlwaysInvoice" value="true">
	<a id="btnAccept" style="cursor: pointer;" alt="Accept" onclick="alwaysInvoiceSpecialOrder(<bean:write name='aLicense' property='ID'/>,'<bean:write name='aLicense' property='totalPriceValue'/>');"><img src="/resources/commerce/images/btn_accept.png"/></a> &nbsp;|&nbsp;<a  style="cursor: pointer;" onclick="showDeclineSpecialOrder(<bean:write name='aLicense' property='ID'/>,'<bean:write name='aLicense' property='totalPriceValue'/>');">Decline</a>&nbsp;&nbsp;<small><util:contextualHelp helpId="47" rollover="true">more&nbsp;info</util:contextualHelp></small>
</logic:equal>

<logic:equal name="orderDetailActionForm" property="isAlwaysInvoice" value="false">
	<a id="btnAccept" style="cursor: pointer;" alt="Accept" onclick="showAcceptSpecialOrder(<bean:write name='aLicense' property='ID'/>,'<bean:write name='aLicense' property='totalPriceValue'/>');"><img src="/resources/commerce/images/btn_accept.png"/></a> &nbsp;|&nbsp;<a  style="cursor: pointer;" onclick="showDeclineSpecialOrder(<bean:write name='aLicense' property='ID'/>,'<bean:write name='aLicense' property='totalPriceValue'/>');">Decline</a>&nbsp;&nbsp;<small><util:contextualHelp helpId="47" rollover="true">more&nbsp;info</util:contextualHelp></small>
</logic:equal>
</span>
</div>
</logic:equal>


<div class="horiz-rule"></div>
</div>
<br/>
</logic:iterate>
<!-- End of license iteration -->
<div class="clearer"></div>
<br />
  <div style="float:right; font-weight: normal;">
  <util:scroller name="orderDetailActionForm" action="pageAction" currentPage="currentPage" startPage="startPage" resultsPerPage="resultsPerPage" totalPages="totalPages" scope="request"/>
  </div>
<div class="clearer"></div>
<br />

<div class="clearer"></div>


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
	<jsp:useBean id="licenseDeclineForm" scope="session" class="com.copyright.ccc.web.forms.LicenseDeclineForm"/>
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

<script language="JavaScript" type="text/JavaScript">

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
