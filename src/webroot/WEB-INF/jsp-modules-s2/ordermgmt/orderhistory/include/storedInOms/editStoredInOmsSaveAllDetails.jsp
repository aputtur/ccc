<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<div style="display: none;">
	<s:form theme="simple" id="saveAllDetails"
		action="viewOrderStoredInOmsHistory!saveDetails" namespace="/om/history"
		method="post">
			<s:hidden theme="simple" name="quickTabSelected"></s:hidden>
			<s:hidden theme="simple" name="selectedDetailNumber" value="%{item.ID}"></s:hidden>
			<s:if test="invoiceView">
			<s:hidden theme="simple" name="selectedInvoiceNumber" value="%{item.invoiceId}"></s:hidden>
			</s:if>
			<s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"></s:hidden>
			<s:if test="bundleId > 0">
			<s:hidden theme="simple" name="selectedBundleNumber" value="%{item.bundleId}"></s:hidden>
			</s:if>
			<s:else>
			<s:hidden theme="simple" name="selectedBundleNumber" value="-1"></s:hidden>
			</s:else>
			<s:hidden theme="simple" name="selectedItem"></s:hidden>
			<s:hidden theme="simple" name="includeCancelledOrders"></s:hidden>
		<br />
		
		<s:iterator value="wrappedResults" status="pgStatus">
			<s:iterator value="detailsList" status="iterStatus">
			<jsp:include
				page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailFields.jsp"></jsp:include>
			</s:iterator>
		</s:iterator>

	</s:form>
</div>
