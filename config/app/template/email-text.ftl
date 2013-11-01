Dear <#if invoiceSummary.adminName?exists>${invoiceSummary.adminName?replace("  ", " ")}<#else><#if invoiceSummary.adminName?exists>${invoiceSummary.billToName?replace("  ", " ")}</#if></#if>,

Our records indicate that your account has a past due balance of ${invoiceSummary.totalInvoiceAmount?string.currency} for the invoice(s) listed below.  The invoice(s) are license(s) granting you or the customer you obtained permission for to use copyrighted material.		
All invoices are DUE UPON RECEIPT, including invoices for licenses for future uses of copyrighted material.  ALL LICENSES ARE NOT VALID UNTIL INVOICE BALANCES ARE PAID and unpaid invoices may result in automatic license cancellation and restrictions on your account.  Please make arrangements for payment immediately.  	

To pay by wire, go to:  http://www.copyright.com/wire.  
To pay online by MasterCard, VISA, or American Express, go to:  http://www.copyright.com/creditcard.

Payment by check or money order is also accepted.  Payments should be made payable to "COPYRIGHT CLEARANCE CENTER" and reference your account number (see Subject line) and the invoice number(s) being paid.  Mail the payment to:
Copyright Clearance Center
P.O. Box 843006
Boston, MA 02284-3006
USA 
	
INVOICE #	DATE		INVOICE AMOUNT	  BALANCE UNPAID	    DUE DATE	DAYS LATE
<#list overdueInvoices as invoice>
<#if invoice.invoiceNumber?exists>${invoice.invoiceNumber?string}</#if>     <#if invoice.invoiceDate?exists>${invoice.invoiceDate?string("dd-MMM-yy")}</#if>   <#if invoice.invoiceAmount?exists>${invoice.invoiceAmount?string.currency?right_pad(15)}</#if>     <#if invoice.outstandingAmount?exists>${invoice.outstandingAmount?string.currency?right_pad(15)}</#if>     <#if invoice.dueDate?exists>${invoice.dueDate?string("dd-MMM-yy")}</#if>     <#if invoice.daysOutstanding?exists>${invoice.daysOutstanding}</#if>
</#list>

Order Questions:

Copyright Clearance Center, Inc.
Customer Service
E-mail: info@copyright.com
Telephone (USA): +1-978-646-2600
Fax: +1-978-646-8600

Payment Questions:

Copyright Clearance Center, Inc.
Collections
E-mail: collection@copyright.com
Telephone (USA): +1-978-646-2583
Fax: +1-978-750-4904

Thank you for using Copyright Clearance Center for your copyright licensing needs. 

Sincerely,
Licensing Billing Administrator
Copyright Clearance Center

