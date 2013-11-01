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

<!-- start of hosted order page Content --> 

<div id="IFrameBlock" style="display:none;">
    <div id="Iframe_payment_Header" align="center"><Strong>Add New Credit Card</Strong></div><br/>

    <table width="640" cellpadding="3" cellspacing="0" border="0" frame="box" rules="none" >
        <tr>
            <td valign="top">
                <iframe name="theHOPIFrame" width="600" height="510" id="theHOPIFrame" frameBorder="No" scrolling="yes"></iframe>
            </td>
        </tr>
        <tr>
            <td align="right" valign="top"><input type="button" value="Cancel" id="cancelButton" style="display:none"/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </td>   
        </tr>
    </table>
    
    <%@ include file="/WEB-INF/jsp-modules/payment/hostedOrderPage.jsp"%>
    
    <form action='${hopURL}' method="post" target="theHOPIFrame" id="theHOPDriverForm" name="theHOPDriverForm">
      
        <%
	  Map<String, String> subscriptionMap = new HashMap<String, String>();
	  subscriptionMap.put("amount", "0.00");
	  subscriptionMap.put("currency", "usd");
	  subscriptionMap.put("orderPage_transactionType", "subscription");
	  subscriptionMap.put("recurringSubscriptionInfo_amount", "0");
	  subscriptionMap.put("recurringSubscriptionInfo_numberOfPayments", "0");
	  subscriptionMap.put("recurringSubscriptionInfo_frequency", "on-demand");
	  subscriptionMap.put("recurringSubscriptionInfo_startDate", "20190731");
	  subscriptionMap.put("recurringSubscriptionInfo_automaticRenew", "false");
	
	%>
	<%=insertSignature(subscriptionMap)%>
        
        
        
        
        <input type="hidden" name="orderPage_receiptResponseURL" value="${URL}" />
        <input type="hidden" name="orderPage_declineResponseURL" value="${URL}" />
        <input type="hidden" name="billTo_firstName" value="" />
        <input type="hidden" name="billTo_lastName" value="" />
        <input type="hidden" name="billTo_street1" value="" />
        <input type="hidden" name="billTo_street2" value="" />
        <input type="hidden" name="billTo_city" value="" />
        <input type="hidden" name="billTo_state" value="" />
        <input type="hidden" name="billTo_postalCode" value="" />                   
        <input type="hidden" name="billTo_customerID" value="" />                   
        <input type="hidden" name="billTo_country" value="" />
        <input type="hidden" name="userid" value='${userid}' />
        <input type="hidden" name="orderPage_merchantEmailAddress" value='${responseEmail}' />
        <input type="hidden" name="orderPage_merchantEmailPostAddress" value='${responseEmail}' />
        <input type="hidden" name="billTo_email" value="" />
        <input type="hidden" name="billTo_phoneNumber" value="" />
    </form>
    
    <form action='${hopURL}' method="post" target="theHOPIFrame" id="theHOPUpdateDriverForm" name="theHOPUpdateDriverForm">

        <div id="modifySignatureOutput"></div>
        <input type="hidden" name="orderPage_receiptResponseURL" value="${URL}" />
        <input type="hidden" name="orderPage_declineResponseURL" value="${URL}" />
        <input type="hidden" name="orderPage_merchantEmailAddress" value='${responseEmail}' />
        <input type="hidden" name="orderPage_merchantEmailPostAddress" value='${responseEmail}' />
        <input type="hidden" name="billTo_email" value=" " />      
    </form>
</div>

<div class="clearer"></div>