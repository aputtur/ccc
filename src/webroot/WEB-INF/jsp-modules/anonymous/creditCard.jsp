<%@ page language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="org.apache.commons.codec.binary.Base64"%>
<%@ page import="java.util.*"%>
<%@ page import="com.copyright.ccc.config.CybersourceConfiguration"%>
<%@ page errorPage="/jspError.do" %>
<%@ include file="HOP.jsp"%>
<%
    //  Enter our credit card information.  This page then forwards
    //  to the confirmation page which, in turn, sends the request
    //  off to cybersource.

    //building base of response url from cybersource

    StringBuffer url = new StringBuffer();
    String responseURL="";
    String scheme = request.getScheme();
    int port = request.getServerPort();

    if (port < 0) port = 80;

    url.append( "https" ).append( "://" );
    url.append( request.getServerName() );

    if ("localhost".equals( request.getServerName() ) ||
        "127.0.0.1".equals( request.getServerName() ))
    {
        url = new StringBuffer();
        url.append("http://localhost:8080");
    }

    responseURL = url.toString();
%>
<jsp:useBean 
    id="anonymousUnpaidInvoiceForm" 
    scope="session" class="com.copyright.ccc.web.forms.AnonymousUnpaidInvoiceForm"/>
    
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

<div class="clearer"></div>
<div id="ecom-content">
  
<table width="100%" style="border-bottom: 2px dotted #990000;">
    <tr>
        <td style="vertical-align: middle; height: 50px;" align="left" width="50%"><span style="font-weight: bold; font-size: 12pt;">Pay Invoices</span></td>
        <td style="vertical-align: middle; height: 50px;" align="right">
            <table>
                <tr><td><strong>Don't want to pay by credit card?</strong></td></tr>
                <tr><td><a href="http://support.copyright.com/index.php?action=article&id=301&relid=11">View other payment options >></a></td></tr>
            </table>
        </td>
    </tr>
</table>

<p>
    Enter credit card information and Press &#34;Save&#34;.<br />
    Once your card has been verified, select &#34;Continue&#34;.
</p>

<form action="<%= CybersourceConfiguration.getInstance().getHopUrl() %>" method="POST" target="theHOPIFrame" id="theHOPDriverForm" name="theHOPDriverForm">
    <%
    Map<String,String> map=new HashMap<String,String>();
    String refNum = anonymousUnpaidInvoiceForm.getArAccountNumber() + "_" + String.valueOf( System.currentTimeMillis() );
    String totalAmount = anonymousUnpaidInvoiceForm.getTotalAmount(true) == null ? "0.00" : anonymousUnpaidInvoiceForm.getTotalAmount(true).toString();
    String currencyCode = anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(0, true);

    map.put( "amount", totalAmount );
    map.put( "currency", currencyCode);
    map.put( "orderPage_transactionType", "authorization" );
    map.put( "orderNumber", refNum );
    %>
    <%= insertSignature(map) %>

    <input type="hidden" name="orderPage_receiptResponseURL" value="<%= responseURL %>/anonymousInvoicePaymentStepFour.do?operation=cybersourceResponse" />
    <input type="hidden" name="orderPage_declineResponseURL" value="<%= responseURL %>/anonymousInvoicePaymentStepFour.do?operation=cybersourceResponse" />
    <input type="hidden" name="paymentOption" value="card">
</form>

<iframe name="theHOPIFrame" id="theHOPIFrame" frameborder="No" height="540" scrolling="yes" width="600"></iframe><br /><br />

<html:link action="anonymousInvoicePaymentStepThree.do" title="Re-enter Credit Card">Re-enter Credit Card</html:link>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<span style="display: none;" id="displayContinue">
    <html:link action="anonymousInvoicePaymentStepFour.do" title="Confirm Payment">
        <html:img src="/media/images/btn_continue.gif" alt="Continue"  width="75" height="18" styleId="999" />
    </html:link>
</span><!-- continue button -->
</div><!-- ecom content -->

<script type="text/javascript">
//  We auto submit the payment authorization, which returns the credit
//  card entry form back to the iFrame.  At that point the user can enter
//  credit card information, hit "SAVE" and then the one of our response
//  pages should be invoked.

document.getElementById('theHOPDriverForm').submit();
</script>