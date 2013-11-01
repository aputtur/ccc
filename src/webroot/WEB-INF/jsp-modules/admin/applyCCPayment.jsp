<%@ page language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.business.services.user.User"%>
<%@ page import="com.copyright.svc.ldapuser.api.data.LdapUser"%>
<%@ page import="com.copyright.ccc.business.data.CCUser"%>
<%@ page import="com.copyright.svc.telesales.api.data.Location"%>
<%@ page import="com.copyright.ccc.business.services.ServiceLocator"%>
<%@ page import="com.copyright.svc.userInfo.api.data.UserInfoConsumerContext"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo.PaymentProfileInfoKeys"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.SortCriteria"%>
<%@ page import="org.apache.commons.codec.binary.Base64"%>
<%@ page import="java.util.*"%>
<%@ page errorPage="/jspError.do" %>

<link href="<html:rewrite page="/resources/commerce/css/impromptu.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/rightslink_font.css"/>" rel="stylesheet" type="text/css" />
<!--
<script type="text/javascript" src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-1.3.2.min.js"/>" ></script>
<script type="text/javascript" src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-ui-1.7.2.custom.min.js"/>" ></script>
-->
<script type="text/javascript" src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-impromptu.2.8.js"/>" ></script>
<!--
<link href="<html:rewrite page="/resources/ordermgmt/style/smoothness/jquery-ui-1.7.2.custom.css"/>" rel="stylesheet" type="text/css" />
-->
<script src="<html:rewrite page="/resources/commerce/js/paymentScripts.js"/>" type="text/javascript"></script>

<jsp:useBean 
    id="unpaidInvoiceForm" 
    scope="session" class="com.copyright.ccc.web.forms.UnpaidInvoiceForm"/>
    
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">

<script type="text/javascript">
if (browserInfo.isNetscape4)
{
	document.write ("<link href='<html:rewrite page='/resources/commerce/css/rightslink_layout_ns4.css'/>' rel='stylesheet' type='text/css' />");
}
else if (browserInfo.isIE)
{
	document.write ("<link href='<html:rewrite page='/resources/commerce/css/rightslink_layout_msie.css'/>' rel='stylesheet' type='text/css' />");
}
else
{
	document.write ("<link href='<html:rewrite page='/resources/commerce/css/rightslink_layout_ns6.css'/>' rel='stylesheet' type='text/css' />");
}

  var eventLogger;
  function initializeEventLogger(){
    var eventLoggerServiceURL = "<html:rewrite page="/advisor/eventLogger.do?operation=logEvent" />";
    eventLogger = new EventLogger( eventLoggerServiceURL );
    var contactLibraryLink = document.getElementById("contactLibraryLink");
    
    if( contactLibraryLink ){
      contactLibraryLink.onclick = performLogContactLibrary;
    }
  }
  
  function performLogContactLibrary(){
     var contactLibraryLink = document.getElementById("contactLibraryLink");
     var communicationValue = new String( escape( contactLibraryLink.href ) );   
     eventLogger.logEvent( EVENT_CONTACT_SME_CHECKOUT , communicationValue );
  }
    
</script>

<style type="text/css">
    table.inv { width: 95%; border: 0; }
    th.inv    { border: 0; height: 40px; background-color: #CCCCCC; }
    tr.inv    { border: 0; }
    td.inv    { border: 0; }
    td.odd  { border: 0; background-color: #EDEFEF; }
    td.even { border: 0; background-color: #FFFFFF; }
    td.hdr  { height: 25px; }
    hr { height: 1px; border: 0px; background-color: #CCCCCC; margin: 0px 5px 0px 5px; color: #CCCCCC; } 
</style>

<div class="clearer"></div>
<div id="ecom-content">
  
<h1>Payment Information</h1>

<p>
    Select the type of credit card.  
    Press &#34;Continue&#34;.<br/><br/>You will be asked to review and confirm 
    the transaction on the next page.
</p>
<br />
<input type="hidden" id="defaultPaymentType" value="credit-card"/>

   <div id="mainPaymentTypeOptionSelectorBlock"></div>  
<logic:notEmpty name="unpaidInvoiceForm" property="hopURL">
	<bean:define id="hopURL" name="unpaidInvoiceForm" property="hopURL"/>
	<bean:define id="URL" name="unpaidInvoiceForm" property="cccURL"/>
	<bean:define id="userid" name="unpaidInvoiceForm" property="userName"/>
	<bean:define id="responseEmail" name="unpaidInvoiceForm" property="responseEmail"/>
</logic:notEmpty>
	<html:form action="confirmInvoicePayment.do" styleId="frm">
 		<!-- COMMON TEMPLATE -->
	<html:hidden styleId="frm_cardType" value="NONE" name="unpaidInvoiceForm" property="creditCardType" />
	<html:hidden styleId="frm_paymentProfileId" value="NONE" name="unpaidInvoiceForm" property="paymentProfileId" />
	<html:hidden styleId="frm_lastFourDigits" value="NONE" name="unpaidInvoiceForm" property="creditCardNumber" />
	<html:hidden styleId="frm_cardholderName" value="NONE" name="unpaidInvoiceForm" property="creditCardNameOn" />
	<html:hidden styleId="frm_expirationDate" value="NONE" name="unpaidInvoiceForm" property="expirationDate" />
	<html:hidden styleId="frm_cccPaymentProfileId" value="NONE" name="unpaidInvoiceForm" property="cccPaymentProfileId" />
	
	<!--  Custom fields -->
	<html:hidden name="unpaidInvoiceForm" property="mode" value="1" />

		<%@ include file="/WEB-INF/jsp-modules/payment/paymentMainContent.jsp"%>
</html:form>

<%@ include file="/WEB-INF/jsp-modules/payment/addNewCreditCard.jsp"%>
<!-- *************************************************************** -->
</div>

<div class="clearer"></div>
<script type="text/javascript">
$(document).ready(function(){
	// initialize modal property
	paymentScreenProps=new ccPaymentDefaultProps("viewPaymentForm.do","Normal","null","null","null");
	$('#credit-card-block').show();
	getCreditCards();
	$("#displayContinue_horz_ruler").show();
   $("#displayContinue").show();
			 
});

  
// Hook called from paymentscript.jsp
	function HOOK_IFrameShowEvent(iframeState){
		if(iframeState=='show'){
			$('#cancelButton').show();
		}else{
			$('#cancelButton').hide();
		}
	return;
	}
	function HOOK_BeforePaymentSubmit(){
		return;
	}

	
</script>


