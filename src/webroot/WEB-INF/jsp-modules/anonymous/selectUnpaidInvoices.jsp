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

<script type="text/javascript">
    var sum = 0.0;
    var x = 0;
    var values = new Array();
    var origCurrencies = new Array();
	
    function toggleSelected(chkall)
    {
        var items = document.getElementsByName("selectedInvoices");
        var sumdiv = document.getElementById("sum");
        var compareCurrencyCode;
        var isMixedCurrency;
        
        for (i = 0; i < origCurrencies.length; i++)
        {
        	if (i == 0){
            	compareCurrencyCode = origCurrencies[i];
        	}
        	else {
            	if (compareCurrencyCode != origCurrencies[i]){
            		isMixedCurrency = true;
            		break;
            	}
            }
        }

		if (!isMixedCurrency){		        
	        if (chkall.checked)
	        {
	            sum = 0.0;
	            
	            for (i = 0; i < items.length; i++)
	            {
	                items[i].checked = true;
	                sum += values[i];
	            }
	        }
	        else
	        {
	            for (i = 0; i < items.length; i++)
	            {
	                items[i].checked = false;
	            }
	            sum = 0.0;
	        }
		        
		    sumdiv.innerHTML = "Total Amount: " + compareCurrencyCode + " " + sum.toFixed(2);
        }
        else {
	            for (i = 0; i < items.length; i++)
	            {
	                items[i].checked = false;
	            }
	            
	            chkall.checked = false;
	            sum = 0.0;
	            sumdiv.innerHTML = "You can not pay multiple invoices in different currencies";	            
        }
    }
    
    function addToTotal(chk, idx)
    {
        var sumdiv = document.getElementById("sum");
        var chkAll = document.getElementById("selectAll");
        var items = document.getElementsByName("selectedInvoices");
        var compareCurrencyCode;
        var isMixedCurrency = false;
        var firstCheck = true;
        
        for (i = 0; i < items.length; i++)
        {
        	if (items[i].checked){
	        	if (firstCheck){
	            	compareCurrencyCode = origCurrencies[i];	            	 
	            	firstCheck = false;     	
	        	}
	        	else {
	            	if (compareCurrencyCode != origCurrencies[i]){
	            		isMixedCurrency = true;
	            		break;
	            	}
	            }
            }
        }
		
		if (!isMixedCurrency){		
	        if (isNaN(values[idx])) {
	            sum = 0.00;
	        }
	        else {
	            if (chk.checked) {
	                sum += values[idx];
	            }
	            else {
	                sum -= values[idx];
	                chkAll.checked = false;
	            }
	        }
	        sum = Math.abs(sum);
	        if (origCurrencies[idx] == 'JPY'){
	        	sumdiv.innerHTML = "Total Amount: " + origCurrencies[idx] + " " + sum.toFixed(0);
	        } else {
	        	sumdiv.innerHTML = "Total Amount: " + origCurrencies[idx] + " " + sum.toFixed(2);
	        }
        }
        else {
        	sum = 0.00;
        	
            for (i = 0; i < items.length; i++){
            	items[i].checked = false;
            }
       
	    	sumdiv.innerHTML = "You can not pay multiple invoices in different currencies - please re-selected your invoices";	            
        }
    }
    
    function sumAll()
    {
        var items = document.getElementsByName("selectedInvoices");
        var sumdiv = document.getElementById("sum");
        var allChecked = 0;
        var currencyCode;
        
        for (i = 0; i < items.length; i++)
        {
            if (items[i].checked == true) {
                sum += values[i];
                currencyCode = origCurrencies[i];
                allChecked += 1;
            }
        }
        if (sumdiv != null) {
	        if (currencyCode == 'JPY'){
	        	sumdiv.innerHTML = "Total Amount: " + currencyCode + " " + sum.toFixed(0);
	        } else {
	        	sumdiv.innerHTML = "Total Amount: " + currencyCode + " " + sum.toFixed(2);
	        }
        }
        
        if (allChecked == values.length) {
            document.getElementById("selectAll").checked = true;   
        }
        else document.getElementById("selectAll").checked = false;
    }

    function checkInitial()
    {
        var items = document.getElementsByName("selectedInvoices");
        var originalInvoice = '<bean:write name="anonymousUnpaidInvoiceForm" property="invoiceNumber" />';
        var originalValue = '<bean:write name="anonymousUnpaidInvoiceForm" property="originalInvoiceAmount" />';

        for (i = 0; i < items.length; i++)
        {
            if (items[i].value == originalInvoice)
            {
                items[i].checked = true;
            }
        }
    }
</script>

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

<logic:notEmpty name="anonymousUnpaidInvoiceForm" property="emailAddress">
<p>Email address: <bean:write name="anonymousUnpaidInvoiceForm" property="emailAddress" /></p>
</logic:notEmpty>

<logic:empty name="anonymousUnpaidInvoiceForm" property="invoices">
    <p style="font-weight: bold;">No unpaid invoices were found.</p>
</logic:empty>

<logic:notEmpty name="anonymousUnpaidInvoiceForm" property="invoices">
<p>
    Below are all unpaid invoices for the account associated with the invoice you queried.<br/>
    Select additional invoices you would like to pay and then select &#34;Continue&#34;.
</p>

<html:form action="anonymousInvoicePaymentStepThree.do" method="post" styleId="sbmtFrm">
    <table class="inv">
        <tr class="inv"><td colspan="5" class="even"><hr /></td></tr>
        <tr class="inv">
            <td class="hdr" width="30">
                <html:checkbox styleId="selectAll" property="selectAll" onclick="toggleSelected(this)"></html:checkbox>
            </td>
            <td class="hdr" width="165"><u>Invoice Number</u></td>
            <td class="hdr" width="165"><u>Invoice Date</u></td>
            <td class="hdr" width="165"><u>Original Amount</u></td>
            <td class="hdr" width="165"><u>Balance Due</u></td>
        </tr>

    <logic:iterate name="anonymousUnpaidInvoiceForm" property="invoices" id="invoice" indexId="idx" 
                   type="com.copyright.svc.artransaction.api.data.ARTransaction">
                   
        <script type="text/javascript">
            x = <%= idx.intValue() %>;
            values.push(<%= anonymousUnpaidInvoiceForm.getCurrentAmount(idx, false).doubleValue() %>);
            origCurrencies.push('<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false) %>');
        </script>
        <bean:define id="subvalue"><%= anonymousUnpaidInvoiceForm.getCurrentAmount(idx, false).toString() %></bean:define>
                   
        <% if (idx.intValue() % 2 == 1) { %>
        <tr class="inv">
            <td class="odd">
                 <html:multibox property="selectedInvoices" onclick="<%= "addToTotal(this," + idx.toString() + ")" %>" >
                    <bean:write name="invoice" property="transactionNumber" />
                </html:multibox>
            </td>
            <td class="odd"><bean:write name="invoice" property="transactionNumber" /></td>
            <td class="odd"><bean:write name="invoice" property="transactionDate" format="MM/dd/yyyy" /></td>
            <td class="odd">
	            <%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getTotalAmt(idx, false) %>' , '<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false) %>'));</script>
            <td class="odd">
	            <%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getBalanceDue(idx, false) %>' , '<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false)%>'));</script>
        </tr>
        <% } else { %>
        <tr class="inv">
            <td class="even">
                <html:multibox property="selectedInvoices" onclick="<%= "addToTotal(this," + idx.toString() + ")" %>" >
                    <bean:write name="invoice" property="transactionNumber" />
                </html:multibox>
            </td>
            <td class="even"><bean:write name="invoice" property="transactionNumber" /></td>
            <td class="even"><bean:write name="invoice" property="transactionDate" format="MM/dd/yyyy" /></td>
            <td class="even">
	            <%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false) %> <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getTotalAmt(idx, false) %>' , '<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false) %>'));</script>
            <td class="even">
	            <%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false) %>  <script language="JavaScript" type="text/javascript">document.write(formatNumber('<%= anonymousUnpaidInvoiceForm.getBalanceDue(idx, false) %>' , '<%= anonymousUnpaidInvoiceForm.getOriginalCurrencyCode(idx, false)%>'));</script>
        </tr>
        <% } %>
    </logic:iterate>
    
        <tr class="inv"><td colspan="5" class="even"><hr /></td></tr>
        <tr class="inv"><td class="even" colspan="5" align="right">
            <span class="largertype bold" id="sum">Total Amount: 0.00</span>
        </td></tr>
        <tr class="inv"><td class="even" colspan="5" align="right">
            <html:image srcKey="resource.button.continue" />
        </td></tr>
    </table>
    
</html:form>
</logic:notEmpty>
</div>
<div class="clearer">
</div>
<script type="text/javascript">
    //  Sum if any are checked.
    
    checkInitial();
    sumAll();
</script>