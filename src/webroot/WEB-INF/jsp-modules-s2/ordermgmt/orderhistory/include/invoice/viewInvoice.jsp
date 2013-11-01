<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="order_confirmcontent">
	<table class="editTable" id="order_viewheader" width="100%">
	    <col style="width: 10px;"/>
	    <col style="width: 120px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 10px;"/>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Invoice Date" cssClass="odd"
				cssStyle="width: 120px;" value="%{firstDetail.item.invoiceDate}">
			</s:label>
			<td class="odd" colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Licensee Account Number" cssClass="odd"
				cssStyle="width: 120px;" value="%{selectedOrder.confirmation.licenseeAccount}">
			</s:label>
			<td class="odd" colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Licensee Name" cssClass="odd" cssStyle="width: 220px;"
				value="%{selectedOrder.confirmation.licenseeName}">
			</s:label>
			<td class="odd" colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6">&nbsp;</td>
		</tr>
	</table>
		
	<table class="editTable" id="order_editheader" style="display: none;" width="100%">
	    <col style="width: 30px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 30px;"/>
		<tr>
			<td class="odd" colspan="6" align="right" style="padding-right: 20px;">
				<s:submit theme="simple" value="Save" onclick="showNotImplemented('Invoice Save');return false;" />
				<s:submit theme="simple" value="Discard"
					onclick="cancelInvoiceEditHeader();return false;" />
			</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:textfield label="Licensee Account Number" cssClass="odd"
				name="editOrder.acctNumber" value="%{selectedOrder.confirmation.acctNumber}">
			</s:textfield>
			<s:textfield label="Payment Method" cssClass="odd"
				name="editOrder.paymentMethod" value="%{selectedOrder.missing.paymentMethod}">
			</s:textfield>
			<td class="odd">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<td class="odd-bold">
				Licensee Name:
			</td>
			<td class="odd">
				<s:textfield theme="simple" cssClass="odd" cssStyle="width: 200px;"
					name="editOrder.licenseeName" value="%{selectedOrder.confirmation.licenseeName}">
				</s:textfield>
				<s:submit onclick="showNotImplemented('Licensee Name Search');return false;" theme="simple" cssStyle="odd" value="Search" />
			</td>
			<td colspan="2" class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>

		</tr>
		<tr>
			<td colspan="6">&nbsp;</td>
		</tr>
	</table>
</div>
