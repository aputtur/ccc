<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

	<table
		id="order_<s:property value="confNumber"/>_course_<s:property value="bundleId"/>_detail_<s:property value="item.ID"/>_short"
		class="shortDetailTable" width="100%"
		style="<s:if test="#iterStatus.odd == true ">background-color: #F3F6F9;</s:if><s:else>background-color: #E2E9F0;</s:else>">
				<col style="width: 11px;"/>
				<col style="width: 85px;"/>
				<col style="width: 80px;"/>
				<col style="width: 90px;"/>
				<col style="width: 170px;"/>
				<col style="width: 90px;"/>
				<col style="width: 90px;"/>
				<col style="width: 100px;"/>
				<col style="width: 90px;"/>
		<tr>
		  <s:if test="includeExpandedResults">
				<td rowspan="8" class="odd">
				<div
					id="order_<s:property value="confNumber"/>_course_<s:property value="bundleId"/>_detail_<s:property value="item.ID"/>_expand">
					<s:if test="pagingIndividualDetails">
					<a href="#" id='ORG<s:property value="item.iD"/>'
						onclick="toggleOrderScript(this,'<s:property value="confNumber"/>','<s:property value="bundleId"/>', '<s:property value="item.ID"/>');return false;"><span
						style="margin-top: 5px;"> <img alt="Expand" border="0"
								src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
					</span> </a>
					</s:if>
					<s:else>
					<s:if test="bundleId > 0">
					<a href="#" id='ORG<s:property value="item.iD"/>'
						onclick="toggleBundleScript(this,'<s:property value="confNumber"/>','<s:property value="bundleId"/>', '<s:property value="item.ID"/>');return false;"><span
						style="margin-top: 5px;"> <img alt="Expand" border="0"
								src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
					</span> </a>
					</s:if>
					<s:else>
							<a href="#" id='ORG<s:property value="item.iD"/>'
								onclick="toggleOrderScript(this,'<s:property value="confNumber"/>','<s:property value="bundleId"/>', '<s:property value="item.ID"/>');return false;"><span
								style="margin-top: 5px;"> <img alt="Expand" border="0"
										src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
							</span> </a>
						</s:else>
					</s:else>
				</div>
			</td>
		  </s:if>
		  <s:else>
				<td rowspan="8" class="odd"><s:property value="#iterStatus.index + (( searchCriteria.pageControl.page - 1 ) * searchCriteria.pageControl.pageSize ) + 1"/></td>
		  </s:else>
		        <td class="odd-bold">Detail Date:</td>
		        <td class="odd"><s:property value="item.createDate"/></td>
		        <td class="odd-bold">Licensee Acct #:</td>
		        <td class="odd"><s:property value="myConfirmation.confirmation.licenseeAccount"/></td>
		        <td class="odd-bold">Created By:</td>
		        <td class="odd"><span style="width:12em; word-break: break-all; word-wrap: break-word;">
		            <s:property value="myConfirmation.confirmation.orderCreatedBy"/>
		            </span></td>
		        <td class="odd-bold">Order Stored in:</td>
		        <td class="odd"><s:property value="item.orderDataSourceDisplay"/></td>
		</tr>
		<tr>
			<s:if test="quickTabSelect.adjustmentSearch">
				<td class="odd-bold" style="color: red;">Adjust Confirm #:</td>
			</s:if>
			<s:else>
				<td class="odd-bold" >Confirm #:</td>
			</s:else>
			<td class="odd">
		        <s:if test="includeConfirmPageLinks">
		        <s:if test="!quickTabSelect.adjustmentSearch">
				  <s:url escapeAmp="false" action="viewOrderHistory" includeParams="none"
					namespace="/om/history" id="lnkConfNum">
					<s:param name="selectedConfirmNumber" value="%{confNumber}" />
					<s:param name="selectedBundleNumber" value="%{bundleId}" />
					<s:param name="selectedDetailNumber" value="%{item.ID}" />
					<s:param name="includeCancelledOrders" value="%{includeCancelledOrders}" />
					<s:param name="selectedItemStoredInOMS" value="%{storedInOMS}"/>
					<s:param name="selectedItem"
						value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@ORDER_HEADER" />
				  </s:url>
					 <a href="<s:property value="#lnkConfNum" />" 
						onclick="return showProcessingOnHref();">
						<s:property value="confNumber" /> 
					</a>
				</s:if>
				  <s:else>
					<s:url  escapeAmp="false" includeParams="none" 
						value="/adjustment/adjustment.do?"	id="#lnkConfNum">
						<s:param name="operation" value="'performPurchaseAdjustment'"/>
						<s:param name="id" value="%{confNumber}"/>
					</s:url>
					<a href="<s:property value="#lnkConfNum" />"  
						target="_blank">
						<s:property value="confNumber" /> 
					</a>
				  </s:else>			
			</s:if>
			<s:else>
			    <s:property	value="confNumber" /> 
			</s:else>
			</td>
		        <td class="odd-bold">Licensee Name:</td>
		        <td class="odd"><span style="width:14em; word-break: break-all; word-wrap: break-word;"><s:property value="myConfirmation.confirmation.licenseeName"/></span></td>
		        <td class="odd-bold">Internet Login:</td>
		        <td class="odd"><span style="width:13em; word-break: break-all; word-wrap: break-word;"><s:property value="myConfirmation.confirmation.buyerUserIdentifier"/></span></td>
		        <td class="odd-bold">Permission Source:</td>
		        <td class="odd"><s:property value="item.rightSourceCd"/></td>
		</tr>
		<tr>
		        <td class="odd-bold">TF Order #:</td>
		        <td class="odd"><s:property value="item.externalOrderId"/></td>
		        <td class="odd-bold">Title:</td>
		        <td class="odd"><span style="width:14em; word-break: break-all; word-wrap: break-word;"><s:property value="item.publicationTitle"/></span></td>
		        <td class="odd-bold">Last Upd. Date:</td>
		        <td class="odd"><s:property value="item.lastUpdatedDate"/></td>
		        <td class="odd-bold">Permission Status:</td>
		        <td class="odd"><s:property value="item.itemAvailabilityDescriptionInternal"/></td>
		</tr>
		<!-- additional row for RL details  -->
		<s:if test="rightslink">
			<tr>
		        <td class="odd-bold">RL License #:</td>
		        <td class="odd"><s:property value="item.externalDetailId"/></td>
		        <td class="odd-bold"></td>
		        <td class="odd"></td>
		        <td class="odd-bold">RL Job Ticket #:</td>
		        <td class="odd"><s:property value="item.jobTicketNumber"/></td>
		        <td class="odd-bold"></td>
		        <td class="odd"></td>
		</tr>
		</s:if>
		<tr>
			<s:if test="quickTabSelect.adjustmentSearch">
				<td class="odd-bold" style="color: red;">Adjust Detail #:</td>
			</s:if>
			<s:else>
				<td class="odd-bold" >Detail #:</td>
			</s:else>
			<td class="odd">
		        <s:if test="includeConfirmPageLinks">
		        <s:if test="!quickTabSelect.adjustmentSearch">
				<s:if test="bundleId > 0">
					<s:url escapeAmp="false" action="viewOrderHistory" includeParams="none"
						namespace="/om/history" id="#lnkDetNum">
						<s:param name="selectedConfirmNumber" value="%{confNumber}" />
						<s:param name="selectedDetailNumber" value="%{item.ID}" />
						<s:param name="selectedBundleNumber" value="%{bundleId}" />
						<s:param name="includeCancelledOrders" value="%{includeCancelledOrders}" />
					    <s:param name="selectedItemStoredInOMS" value="%{storedInOMS}"/>
						<s:param name="selectedItem"
							value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@BUNDLE_DETAIL" />
					</s:url>
				</s:if>
				<s:else>
					<s:url escapeAmp="false" action="viewOrderHistory" includeParams="none"
						namespace="/om/history" id="#lnkDetNum">
						<s:param name="selectedConfirmNumber" value="%{confNumber}" />
						<s:param name="selectedDetailNumber" value="%{item.ID}" />
						<s:param name="selectedBundleNumber" value="-1" />
						<s:param name="includeCancelledOrders" value="%{includeCancelledOrders}" />
					    <s:param name="selectedItemStoredInOMS" value="%{storedInOMS}"/>
						<s:param name="selectedItem"
							value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@ORDER_DETAIL" />
					</s:url>
				</s:else>
				<s:if test="!invoiceView">
					<a href="<s:property value="#lnkDetNum" />" 
						onclick="return showProcessingOnHref();">
					<s:property value="item.ID" /> </a>
				</s:if>	
				</s:if>
				<s:else>
					<s:url  escapeAmp="false" includeParams="none"
						value="/adjustment/adjustment.do?"	id="#lnkDetNum">
						<s:param name="operation" value="'performDetailAdjustment'"/>
						<s:param name="id" value="%{item.ID}"/>
					</s:url>
					<s:if test="!invoiceView">
						<a href="<s:property value="#lnkDetNum" />" 
							target="_blank">
						<s:property value="item.ID" /> </a>
					</s:if>
				</s:else>
				<s:if test="!invoiceView">
				<!--<a href="<s:property value="#lnkDetNum" />" 
					onclick="return showProcessingOnHref();">
				<s:property value="item.ID" /> </a> -->
				</s:if>
				<s:else>
				<a href="#" 
					onclick="toggleOrderScript($('#ORG<s:property value="item.iD"/>').get(),'<s:property value="confNumber"/>','<s:property value="bundleId"/>', '<s:property value="item.ID"/>');return false;">
				<s:property value="item.ID" /> </a>
				</s:else>
			</s:if>
			<s:else>
				<a href="#" 
					onclick="toggleOrderScript($('#ORG<s:property value="item.iD"/>').get(),'<s:property value="confNumber"/>','<s:property value="bundleId"/>', '<s:property value="item.ID"/>');return false;">
				<s:property value="item.ID" /> </a>
			</s:else>
			</td>
		        <td class="odd-bold">Article:</td>				
		        <td class="odd"><span style="width:14em; word-break: break-all; word-wrap: break-word;"><s:property value="item.itemSubDescription"/></span></td>
		        <td class="odd-bold">RH Ref #:</td>
		        <td class="odd"><s:property value="item.rhRefNum"/></td>
		        <td class="odd-bold">Assigned To:</td>
		        <td class="odd"><s:property value="item.researchUserIdentifier"/></td>
		</tr>
		<tr>
		        <td>&nbsp;</td><td class="odd">&nbsp;</td>
				<td class="odd-bold">Product/TOU:</td>
		        <td class="odd"><span style="width:14em; word-break: break-all; word-wrap: break-word;"><s:property value="item.productCd"/>/<s:property value="item.typeOfUseDescription"/></span></td>
			<s:if test="quickTabSelect.adjustmentSearch">
				<td class="odd-bold" style="color: red;">Adjust Invoice #:</td>
			</s:if>
			<s:else>
				<td class="odd-bold" >Invoice #:</td>
			</s:else>
			<td class="odd">
		        <s:if test="includeConfirmPageLinks && !invoiceView">
		        <s:if test="!quickTabSelect.adjustmentSearch">
				<s:if test="bundleId > 0">
					<s:url escapeAmp="false" action="viewInvoicedOrderHistory" includeParams="none"
						namespace="/om/history" id="#lnkInvNum">
						<s:param name="selectedConfirmNumber" value="%{confNumber}" />
						<s:param name="selectedDetailNumber" value="%{item.ID}" />
						<s:param name="selectedBundleNumber" value="%{bundleId}" />
						<s:param name="selectedInvoiceNumber" value="%{item.invoiceId}" />
						<s:param name="includeCancelledOrders" value="%{includeCancelledOrders}" />
						<s:param name="selectedItem"
							value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@BUNDLE_HEADER" />
					</s:url>
				</s:if>
				<s:else>
					<s:url escapeAmp="false" action="viewInvoicedOrderHistory" includeParams="none"
						namespace="/om/history" id="#lnkInvNum">
						<s:param name="selectedConfirmNumber" value="%{confNumber}" />
						<s:param name="selectedDetailNumber" value="%{item.ID}" />
						<s:param name="selectedBundleNumber" value="-1" />
						<s:param name="selectedInvoiceNumber" value="%{item.invoiceId}" />
						<s:param name="includeCancelledOrders" value="%{includeCancelledOrders}" />
						<s:param name="selectedItem"
							value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@ORDER_HEADER" />
					</s:url>
				</s:else>
				<a href="<s:property value="#lnkInvNum" />" 
					onclick="return showProcessingOnHref();">
					<s:property	value="item.invoiceId" /> 
				</a>
				</s:if>
				<s:else>
					<s:url  escapeAmp="false" includeParams="none"
						value="/adjustment/adjustment.do?"	id="#lnkInvNum">
						<s:param name="operation" value="'performInvoiceAdjustment'"/>
						<s:param name="id" value="%{item.invoiceId}"/>
					</s:url>
					<a href="<s:property value="#lnkInvNum" />" 
						target="_blank">
						<s:property	value="item.invoiceId" /> 
					</a>
				</s:else>
			</s:if>
			<s:else>
			    <s:property value="item.invoiceId" />
			</s:else>
			</td>
		        <td class="odd-bold">Detail Status:</td>
		        <td class="odd"><s:property value="item.itemStatusInternalDisplay"/></td>
		</tr>
			<s:if test="item.adjusted || item.distributionEvent != null">
				<tr>
			        <td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
					<s:if test="item.distributionEvent != null">
			        	<td class="odd-bold">Distribution ID:</td>
		       			<td class="odd"><s:property value="item.distributionEvent"/></td>
					</s:if>
		       		<s:else>
			        	<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
		       		</s:else>
					<s:if test="item.adjusted">
						<td class="odd-bold"></td>
						<td class="odd-bold" style="color:red; font-weight:bold; text-align: left;">
			       		Adjusted
						</td>
					</s:if>
		       		<s:else>
			        	<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
		       		</s:else>
			        <td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
				</tr>
			</s:if>

	
	 <s:if test="includeBundles">

			<s:if test="item.aPS || item.eCCS ">
			<s:if test="myBundle != null">
			<tr>
			        <td class="odd-bold">SOT:</td>
			        <td class="odd"><s:property value="myBundle.bundle.startOfTerm"/></td>
			        <td class="odd-bold">University:</td>
			        <td class="odd"><span style="width:14em; word-break: break-all; word-wrap: break-word;"><s:property value="myBundle.bundle.organization"/></span></td>
			        <td class="odd-bold">Course #:</td>
			        <td class="odd"><span style="width:12em; word-break: break-all; word-wrap: break-word;"><s:property value="myBundle.bundle.courseNumber"/></span></td>
			        <td class="odd-bold">No. of Students:</td>
			        <td class="odd"><s:property value="myBundle.bundle.numberOfStudentsLong"/></td>
			</tr>
			<tr>
		        	<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
			        <td class="odd-bold">Course Name:</td>
			        <td class="odd"><span style="width:14em; word-break: break-all; word-wrap: break-word;"><s:property value="myBundle.bundle.courseName"/></span></td>
			        <td class="odd-bold">Instructor Name:</td>
			        <td class="odd"><span style="width:12em; word-break: break-all; word-wrap: break-word;"><s:property value="myBundle.bundle.instructor"/></span></td>
				<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
			</tr>
			</s:if>
			</s:if>
			<s:if test="item.republication">
			<tr>
			        <td class="odd-bold">Repub Date:</td>
			        <td class="odd"><s:property value="item.republicationDate"/></td>
			        <td class="odd-bold">Repub Pub:</td>
			        <td class="odd"><span style="width:14em; word-break: break-all; word-wrap: break-word;"><s:property value="item.republishingOrganization"/></span></td>
			        <td class="odd-bold">Circ/Dist:</td>
			        <td class="odd"><s:property value="item.circulationDistribution"/></td>
					<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
				</tr>
			</s:if>

	 </s:if>
	</table>
