<%@ page language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.business.services.user.User"%>
<%@ page import="com.copyright.svc.ldapuser.api.data.LdapUser"%>
<%@ page import="com.copyright.ccc.business.data.CCUser"%>
<%@ page import="com.copyright.svc.telesales.api.data.Location"%>
<%@ page import="com.copyright.ccc.web.forms.AcceptSpecialOrderPaymentForm"%>

<%@ page import="com.copyright.ccc.business.services.ServiceLocator"%>
<%@ page import="com.copyright.svc.userInfo.api.data.UserInfoConsumerContext"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.PaymentProfileInfo.PaymentProfileInfoKeys"%>
<%@ page import="com.copyright.svc.userInfo.api.cyberSource.data.SortCriteria"%>
<%@ page import="org.apache.commons.codec.binary.Base64"%>
<%@ page import="java.util.*"%>
<%@ page errorPage="/jspError.do" %>
<style>
#popup_inner {border-bottom: 1px solid #B2B2B2;}

.indent-2{ margin-left:10px;}
</style>

    <logic:notEmpty name="acceptSpecialOrderPaymentForm" property="hopURL">
		<bean:define id="hopURL" name="acceptSpecialOrderPaymentForm" property="hopURL"/>
		<bean:define id="URL" name="acceptSpecialOrderPaymentForm" property="cccURL"/>
		<bean:define id="userid" name="acceptSpecialOrderPaymentForm" property="userName"/>
		<bean:define id="responseEmail" name="acceptSpecialOrderPaymentForm" property="responseEmail"/>
	</logic:notEmpty>

<br/>
<div class="clearer"></div>
<div id="ecom-content">
<div id="paymentContent">
<html:form action="viewSpecialOrderPaymentModal.do?operation=processPayment" styleId="frm">
<html:hidden styleId="frm_itemId" name="acceptSpecialOrderPaymentForm" property="licenseId" />
		<%@ include file="/WEB-INF/jsp-modules/payment/tiles/specialOrderPaymentOptions.jsp"%>
		<%@ include file="/WEB-INF/jsp-modules/payment/paymentMainContent.jsp"%>
</html:form>

<%@ include file="/WEB-INF/jsp-modules/payment/addNewCreditCard.jsp"%>

</div>
</div>



<script type="text/javascript">

$(document).ready(function(){
	// initialize modal property
	paymentScreenProps=new ccPaymentDefaultProps("viewSpecialOrderPaymentModal.do","Modal","paymentProcessNotify_Content","paymentProcessNotifyModal","acceptSpecialOrderModal");
	//override with modal window
	$("#displayContinue").hide();
	paymentType_RadioToggle();
			 
});





</script>

  



