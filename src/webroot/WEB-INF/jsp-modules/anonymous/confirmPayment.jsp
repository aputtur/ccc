<%@ page language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>

<jsp:useBean 
    id="anonymousUnpaidInvoiceForm" 
    scope="session" class="com.copyright.ccc.web.forms.AnonymousUnpaidInvoiceForm"/>
    
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">

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

<script type="text/javascript" src="resources/commerce/js/numberUtil.js"></script>

<div class="clearer"></div>
<div id="ecom-content">
  
<table width="100%" style="border-bottom: 2px dotted #990000;">
    <tr>
        <td style="vertical-align: middle; height: 50px;" align="left" width="50%"><span style="font-weight: bold; font-size: 12pt;">Review and Confirm Payment</span></td>
        <td style="vertical-align: middle; height: 50px;" align="right">
            <table>
                <tr><td><strong>Don't want to pay by credit card?</strong></td></tr>
                <tr><td><a href="http://support.copyright.com/index.php?action=article&id=301&relid=11">View other payment options >></a></td></tr>
            </table>
        </td>
    </tr>
</table>

<p>
    Please review your payment information below and press 
    the &#34;Continue&#34; button to complete the transaction.<br/>Your 
    card will be charged in the next 24 hours.
</p>
<br />
<html:form action="anonymousInvoicePaymentStepFive.do" method="post" styleId="sbmtFrm">
  <span class="largertype bold" style="height: 10px; float:left;">Payment Information <a class="subtitle" href="viewPaymentForm.do" onClick="" id="997"><u>Edit</u></a></span><br /><br />
  <a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/credit_and_paymentpolicy.html" target="_blank"><u>Review Credit and Payment Policy</u></a>
  	<div class="item-details">
      <div class="biblo_left" style="float:left;width:300px;">
        <div><bean:write name="anonymousUnpaidInvoiceForm" property="userName" /></div>
          
        <logic:notEmpty name="anonymousUnpaidInvoiceForm" property="userCompany">
            <% if (!anonymousUnpaidInvoiceForm.getUserCompany().equals(anonymousUnpaidInvoiceForm.getUserName())) { %>
                <div><bean:write name="anonymousUnpaidInvoiceForm" property="userCompany" /></div>
            <% } %>
        </logic:notEmpty>
      
        <logic:notEmpty name="anonymousUnpaidInvoiceForm" property="userEmail">
        <div><bean:write name="anonymousUnpaidInvoiceForm" property="userEmail" /></div>
        </logic:notEmpty>
        
        <logic:notEmpty name="anonymousUnpaidInvoiceForm" property="userPhone">
        <div><bean:write name="anonymousUnpaidInvoiceForm" property="userPhone" /></div>
        </logic:notEmpty>
     </div>
     <div class="biblo_right" style="float:left;margin-left: 8px;">
        <div><bean:write name="anonymousUnpaidInvoiceForm" property="emailAddress" /></div>
        <div>Credit Card ending in <bean:write name="anonymousUnpaidInvoiceForm" property="cardLastFourDisplay" /></div>
     </div>
   </div>
   <div class="clearer"></div>
   <br />
   <p class="largertype bold" style="height: 10px;">Selected Invoices &nbsp;<a href="editUnpaidInvoices.do" class="normaltype"><u>Edit</u></a></p>
   
    <table class="inv">
        <tr class="inv"><td colspan="4" class="even"><hr /></td></tr>
        <tr class="inv">
            <td class="hdr" width="173"><u>Invoice Number</u></td>
            <td class="hdr" width="173"><u>Invoice Date</u></td>
            <td class="hdr" width="173"><u>Original Amount</u></td>
            <td class="hdr" width="173"><u>Balance To Be Paid</u></td>
        </tr>

    <logic:iterate name="anonymousUnpaidInvoiceForm" property="invoicesToCredit" id="invoice" indexId="idx" 
                   type="com.copyright.svc.artransaction.api.data.ARTransaction">
        <% if (idx.intValue() % 2 == 1) { %>
        <tr class="inv">
            <td class="odd"><bean:write name="invoice" property="transactionNumber" /></td>
            <td class="odd"><bean:write name="invoice" property="transactionDate" format="MM/dd/yyyy" /></td>
            <td class="odd">
	            <%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getTotalAmt(idx, true) %>' , '<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %>'));</script>
            <td class="odd">
	            <%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getBalanceDue(idx, true) %>' , '<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %>'));</script>
        </tr>
        <% } else { %>
        <tr class="inv">
            <td class="even"><bean:write name="invoice" property="transactionNumber" /></td>
            <td class="even"><bean:write name="invoice" property="transactionDate" format="MM/dd/yyyy" /></td>
            <td class="even">
	            <%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getTotalAmt(idx, true) %>' , '<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %>'));</script>
            <td class="even">
	            <%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getBalanceDue(idx, true) %>' , '<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %>'));</script>

        </tr>
        <% } %>
    </logic:iterate>
    
        <tr class="inv"><td colspan="4" class="even"><hr /></td></tr>
        <tr class="inv"><td class="even" colspan="4" align="right">
            <span class="largertype bold" id="sum">
                Total Amount: 
                <bean:write name="anonymousUnpaidInvoiceForm" property="totalAmountCurrency" />
                <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getTotalAmount(true) %>', '<%= anonymousUnpaidInvoiceForm.getTotalAmountCurrency() %>'));</script>
            </span>
        </td></tr>
        <tr class="inv"><td class="even" colspan="4" align="right">
            <html:image srcKey="resource.button.confpmt" onclick="this.disabled = true; this.form.submit();" />
        </td></tr>
    </table>

</html:form>
</div>
<div class="clearer">
</div>