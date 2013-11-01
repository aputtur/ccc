<%@ page language="java" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>

<jsp:useBean 
    id="unpaidInvoiceForm" 
    scope="session" class="com.copyright.ccc.web.forms.UnpaidInvoiceForm"/>
    
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
  
<h1>Review and Confirm Payment</h1>

<p>
    Please review your payment information below and press 
    the &#34;Continue&#34; button to complete the transaction.<br/>Your 
    card will be charged in the next 24 hours.
</p>
<br />
<html:form action="payInvoiceAndNotify.do" method="post" styleId="sbmtFrm">
  <span class="largertype bold" style="height: 10px; float:left;">Payment Information <a class="subtitle" href="viewPaymentForm.do" onClick="" id="997"><u>Edit</u></a></span><br /><br />
  <a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/credit_and_paymentpolicy.html" target="_blank"><u>Review Credit and Payment Policy</u></a>
  	<div class="item-details">
	  <p class="indent-1">
        <br />
        <bean:write name="unpaidInvoiceForm" property="userName" /> <br />
          
        <logic:notEmpty name="unpaidInvoiceForm" property="userCompany">
            <% if (!unpaidInvoiceForm.getUserCompany().equals(unpaidInvoiceForm.getUserName())) { %>
                <bean:write name="unpaidInvoiceForm" property="userCompany" /> <br />
            <% } %>
        </logic:notEmpty>
      
        <logic:notEmpty name="unpaidInvoiceForm" property="userEmail">
        <bean:write name="unpaidInvoiceForm" property="userEmail" /> <br />
        </logic:notEmpty>
        
        <logic:notEmpty name="unpaidInvoiceForm" property="userPhone">
        <bean:write name="unpaidInvoiceForm" property="userPhone" /> <br />
        </logic:notEmpty>
        
        Credit Card ending in <bean:write name="unpaidInvoiceForm" property="creditCardNumber" />
      </p>
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

    <logic:iterate name="unpaidInvoiceForm" property="invoicesToCredit" id="invoice" indexId="idx" 
                   type="com.copyright.svc.artransaction.api.data.ARTransaction">
        <% if (idx.intValue() % 2 == 1) { %>
        <tr class="inv">
            <td class="odd"><bean:write name="invoice" property="transactionNumber" /></td>
            <td class="odd"><bean:write name="invoice" property="transactionDate" format="MM/dd/yyyy" /></td>
            <td class="odd">
	            <%= unpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= unpaidInvoiceForm.getTotalAmt(idx, true) %>' , '<%= unpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %>'));</script>
            <td class="odd">
	            <%= unpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= unpaidInvoiceForm.getBalanceDue(idx, true) %>' , '<%= unpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %>'));</script>
        </tr>
        <% } else { %>
        <tr class="inv">
            <td class="even"><bean:write name="invoice" property="transactionNumber" /></td>
            <td class="even"><bean:write name="invoice" property="transactionDate" format="MM/dd/yyyy" /></td>
            <td class="even">
	            <%= unpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= unpaidInvoiceForm.getTotalAmt(idx, true) %>' , '<%= unpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %>'));</script>
            <td class="even">
	            <%= unpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= unpaidInvoiceForm.getBalanceDue(idx, true) %>' , '<%= unpaidInvoiceForm.getOriginalCurrencyCode(idx, true) %>'));</script>
        </tr>
        <% } %>
    </logic:iterate>
    
        <tr class="inv"><td colspan="4" class="even"><hr /></td></tr>
        <tr class="inv"><td class="even" colspan="4" align="right">
            <span class="largertype bold" id="sum">
                Total Amount: 
                <bean:write name="unpaidInvoiceForm" property="totalAmountCurrency" />
                <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= unpaidInvoiceForm.getTotalAmount(true) %>', '<%= unpaidInvoiceForm.getTotalAmountCurrency() %>'));</script>
            </span>
        </td></tr>
        <tr class="inv"><td class="even" colspan="4" align="right">
            <html:image srcKey="resource.button.continue" onclick="this.disabled = true; this.form.submit();" />
        </td></tr>
    </table>

</html:form>
</div>
<div class="clearer">
</div>