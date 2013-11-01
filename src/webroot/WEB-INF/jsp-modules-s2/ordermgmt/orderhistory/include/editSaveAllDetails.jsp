<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<div style="display: none;">
	<s:form theme="simple" id="saveAllDetails"
		action="viewOrderHistory!saveDetails" namespace="/om/history"
		method="post">
			<s:hidden theme="simple" name="quickTabSelected"></s:hidden>
			<s:hidden theme="simple" name="selectedDetailNumber" value="%{item.ID}"></s:hidden>
			<s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"></s:hidden>
			<s:if test="bundleId > 0">
			<s:hidden theme="simple" name="selectedBundleNumber" value="%{item.bundleId}"></s:hidden>
			</s:if>
			<s:else>
			<s:hidden theme="simple" name="selectedBundleNumber" value="-1"></s:hidden>
			</s:else>
			<s:hidden theme="simple" name="selectedItem"></s:hidden>
			<s:hidden theme="simple" name="includeCancelledOrders"></s:hidden>
			<s:if test="invoiceView">
    			<s:hidden theme="simple" name="selectedInvoiceNumber" value="%{searchInvoiceNumber}"/>
    		</s:if>
			
		<br />


		<s:if test="pagingDetails">
		<s:iterator value="wrappedResults.detailsList"status="iterStatus">
			<jsp:include
				page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailFields.jsp"></jsp:include>
		</s:iterator>
		</s:if>
		<s:else>
		<s:iterator value="wrappedResults.detailsList" status="iterStatus">
				<jsp:include
					page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailFields.jsp"></jsp:include>
		</s:iterator>

<!--  
		<s:iterator value="wrappedResultsArray.orderListInHierarchicalOrder"status="cwStatus">
		   <s:iterator value="myBundles">
		      <s:iterator value="myItems" status="iterStatus">
				<jsp:include
					page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailFields.jsp"></jsp:include>
		   	  </s:iterator>
		   </s:iterator>
		   <s:iterator value="myItems" status="iterStatus">
				<jsp:include
					page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailFields.jsp"></jsp:include>
		   </s:iterator>
		</s:iterator>
-->		
		</s:else>
	</s:form>
</div>
