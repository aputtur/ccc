<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>

<jsp:useBean 
    id="anonymousUnpaidInvoiceForm"
    scope="session" class="com.copyright.ccc.web.forms.AnonymousUnpaidInvoiceForm"/>

<link rel="stylesheet" type="text/css" href="<html:rewrite page='/resources/commerce/css/ccc-new.css'/>">

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

    <html:form action="anonymousInvoicePaymentStepTwo" method="post" styleId="anonymousUnpaidInvoiceForm">
        <p>
            Please enter your email address so we will be able 
            to send you confirmation of your payment:
        </p>
        <table>
            <tr>
                <td class="label" width="200">Email address</td>
                <td><html:text name="anonymousUnpaidInvoiceForm" property="emailAddress" /></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td class="label" width="200">Confirm email address</td>
                <td><html:text name="anonymousUnpaidInvoiceForm" property="emailAddressConfirm" /></td>
                <td>&nbsp;</td>
            </tr>
        </table>
        <p>
            Please enter the invoice number associated with the invoice(s) you want to pay, 
            as well as the numeric value of original amount due for that invoice:
        </p>
        <table>
            <tr>
                <td class="label" width="200">Invoice Number</td>
                <td><html:text name="anonymousUnpaidInvoiceForm" property="invoiceNumber" /></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td class="label" width="200">Original amount due</td>
                <td><html:text name="anonymousUnpaidInvoiceForm" property="originalInvoiceAmount" /></td>
                <td><html:submit /></td>
            </tr>
        </table>
    </html:form>
</div>