<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page import="org.apache.commons.codec.binary.Base64, javax.crypto.*, javax.crypto.spec.*"%>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.copyright.ccc.util.*" %>
<%@ page import="com.copyright.ccc.config.*" %>
<%@ page import="com.copyright.ccc.business.services.payment.CyberSourceUtils" %>

<!-- 
	# This jsp generates the digital signatures for cybersource IFRAME invocation
-->

<%!
  		public String insertSignature(String amount, String currency)
  		{
    		 return CyberSourceUtils.insertSignature(amount, currency);	
		}

		public String insertSignature(String amount, String currency, String orderPage_transactionType) 
		{
    		return CyberSourceUtils.insertSignature(amount, currency, orderPage_transactionType);	
		}

		public String insertSubscriptionSignature(String subscriptionAmount, 
		       String subscriptionStartDate, String subscriptionFrequency,
               String subscriptionNumberOfPayments, String subscriptionAutomaticRenew)
        {

    		return CyberSourceUtils.insertSubscriptionSignature(subscriptionAmount, 
		       subscriptionStartDate, subscriptionFrequency,
               subscriptionNumberOfPayments, subscriptionAutomaticRenew);
               	
		}
		
		public String insertSubscriptionIDSignature(String subscriptionID)
		{
    		return CyberSourceUtils.insertSubscriptionIDSignature(subscriptionID);	
		}
/*
		public boolean verifySignature(String data, String signature)
		{
    		return CyberSourceUtils.verifySignature(data, signature);	
		}

		public boolean verifyTransactionSignature(Map<String,String> map)
		{
    		return CyberSourceUtils.verifyTransactionSignature(map);	
		}
*/		
%>
