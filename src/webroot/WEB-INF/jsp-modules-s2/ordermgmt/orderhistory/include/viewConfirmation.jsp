<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="order_confirmcontent">

<s:if test="invoiceView">
   <s:url id="hdrSaveUrl" action="viewOrderHistory!saveInvoiceHeader.action" namespace="/om/history"></s:url>
</s:if>
<s:else>
   <s:url id="hdrSaveUrl" action="viewOrderHistory!saveConfirmationHeader.action" namespace="/om/history"></s:url>
</s:else>

<s:if test="!selectedOrder.processMessages.empty">
<div id="confirmProcessMessages">
<table width="100%">
	<tr>
		<td style="width: 40px;"></td>
		<td align="center">
		    <fieldset style="width: 800px;">
		    <legend><span style="font-weight: bold;">Processing Messages</span></legend>
			<ul style="text-align: left;">
			<s:iterator value="selectedOrder.processMessages">
			  <s:if test="error">
			   <li style="color: red; font-weight: bold;">
			       <s:property value="briefFormattedMessage"/>
			   </li>
			  </s:if>
			  <s:else>
			   <li style="color: blue; font-weight: bold;">
			       <s:property value="briefMessage"/>
			   </li>
			   </s:else>
			</s:iterator>
		 	</ul>
		 	</fieldset>
		</td>
		<td style="width: 40px;"></td>
	</tr>
</table>
</div>
</s:if>
   
<form id="editConfirmation_<s:property value="searchConfirmNumber"/>" name="editConfirmation_<s:property value="searchConfirmNumber"/>" action="<s:property value="hdrSaveUrl"/>" method="post">
    <s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"/>
    <s:hidden theme="simple" name="selectedBundleNumber" value="%{searchBundleNumber}"/>
    <s:hidden theme="simple" name="selectedDetailNumber" value="%{searchDetailNumber}"/>
    <s:if test="invoiceView">
    <s:hidden theme="simple" name="selectedInvoiceNumber" value="%{searchInvoiceNumber}"/>
    </s:if>
	<input type="hidden" id='changedconfirmation<s:property value="searchConfirmNumber"/>' value="0"/>

	<table class="editTable" id="order_viewheader" width="100%">
	    <col style="width: 10px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 10px;"/>
		<tr>
			<td colspan="6">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label cssClass="odd" cssStyle="width: 80px;" label="Order Date"
				name="orderDate" value="%{selectedOrder.confirmation.orderDate}">
			</s:label>
			<s:label label="Order Status" cssClass="odd" cssStyle="width: 220px;"
				value="%{selectedOrder.confirmation.orderStatusDisplay}">
			</s:label>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Licensee Account Number" cssClass="odd"
				cssStyle="width: 120px;" value="%{selectedOrder.confirmation.licenseeAccount}">
			</s:label>
			<s:label label="Billing Ref Number" cssClass="odd"
				cssStyle="width: 120px;" value="%{selectedOrder.confirmation.billingReference}">
			</s:label>
			<td class="odd">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Licensee Name" cssClass="odd" cssStyle="width: 220px;"
				value="%{selectedOrder.confirmation.licenseeName}">
			</s:label>
			<s:label label="Purchase Order Number" cssClass="odd"
				cssStyle="width: 120px;" value="%{selectedOrder.confirmation.poNumber}">
			</s:label>
			<td class="odd">&nbsp;</td>

		</tr>
		<tr>
			<td colspan="6">&nbsp;</td>
		</tr>
	</table>
		
	<table class="editTable" id="order_editheader" style="display: none;" width="100%">
	    <col style="width: 10px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 10px;"/>
		<tr>
			<td class="odd" colspan="6" align="right" style="padding-right: 20px;">
				<s:submit theme="simple" value="Save" 
					onclick="saveConfirmationEditHeader('%{searchConfirmNumber}');return false;" />
				<s:submit theme="simple" value="Discard"
					onclick="discardConfirmationEditHeader('%{searchConfirmNumber}');return false;" />
			</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label cssClass="odd" label="Order Date"
				name="selectedOrder.confirmation.orderDate">
			</s:label>
			<s:label label="Order Status" cssClass="odd"
				value="%{selectedOrder.confirmation.orderStatusDisplay}">
			</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Licensee Account Number" cssClass="odd"
				value="%{selectedOrder.confirmation.licenseeAccount}">
			</s:label>
			<td class="odd-bold">Billing Ref Number:&nbsp;</td>
			<td class="odd">
				<s:if test="selectedOrder.hasLastEdit">
						<s:textfield cssClass="ectype%{searchConfirmNumber}"
							theme="simple" name="editConfirm.billingReference"
							value="%{selectedOrder.lastEdit.billingReference}" />
				</s:if>
				<s:else>
						<s:textfield cssClass="ectype%{searchConfirmNumber}"
							theme="simple" name="editConfirm.billingReference"
							value="%{selectedOrder.confirmation.billingReference}" />
				</s:else>
				<input type="hidden" id="resetConfirm_billingReference" value='<s:property value="selectedOrder.confirmation.billingReference"/>' />
			</td>
			<td class="odd">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Licensee Name" cssClass="odd"
				value="%{selectedOrder.confirmation.licenseeName}">
			</s:label>
			<td class="odd-bold">Purchase Order Number:&nbsp;</td>
			<td class="odd">
				<s:if test="selectedOrder.hasLastEdit">
						<s:textfield cssClass="ectype%{searchConfirmNumber}"
							theme="simple" name="editConfirm.poNumber"
							value="%{selectedOrder.lastEdit.poNumber}" />
				</s:if>
				<s:else>
						<s:textfield cssClass="ectype%{searchConfirmNumber}"
							theme="simple" name="editConfirm.poNumber"
							value="%{selectedOrder.confirmation.poNumber}" />
				</s:else>
				<input type="hidden" id="resetConfirm_poNumber" value='<s:property value="selectedOrder.confirmation.poNumber"/>' />
			</td>
			<td class="odd">&nbsp;</td>
		</tr>
</table>	
</form>
</div>