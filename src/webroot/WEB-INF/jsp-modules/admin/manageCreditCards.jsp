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
<%@ page import="com.copyright.ccc.web.forms.ManageCreditCardsForm"%>
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
   <logic:notEmpty name="manageCreditCardsForm" property="hopURL">
		<bean:define id="hopURL" name="manageCreditCardsForm" property="hopURL"/>
		<bean:define id="URL" name="manageCreditCardsForm" property="cccURL"/>
		<bean:define id="userid" name="manageCreditCardsForm" property="userName"/>
		<bean:define id="responseEmail" name="manageCreditCardsForm" property="responseEmail"/>
	</logic:notEmpty>
<div class="clearer"></div>
<div id="ecom-content">
  
<h1>Manage Credit Cards</h1>

<p>
    You may add, delete and modify your list of credit cards.
</p>
<br />
<div id="mainPaymentTypeOptionSelectorBlock"></div>

<%@ include file="/WEB-INF/jsp-modules/payment/paymentMainContent.jsp"%>
<%@ include file="/WEB-INF/jsp-modules/payment/addNewCreditCard.jsp"%>


<div class="horiz-rule"></div>
<div class="txtRight" style="display:block;">            
      	<html:link action="/manageAccount.do">Return to Manage Account</html:link>
</div>

</div>
<div class="clearer">
</div>

<script type="text/javascript">
$(document).ready(function(){
	// initialize modal property
	paymentScreenProps=new ccPaymentDefaultProps("manageCreditCards.do","Normal","null","null","null");
	paymentScreenProps.displayRadioButtons=false;
	$('#credit-card-block').show();
	getCreditCards();
			 
});

//Hook called from paymentscript.jsp
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
