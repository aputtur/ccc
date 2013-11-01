<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>
		<tr style="height: 30px;<s:if test="#iterStatus.odd == true ">background-color: #F3F6F9;</s:if><s:else>background-color: #E2E9F0;</s:else>">
			<td class="odd">
			    <s:property	value="invoiceNumber" /> 
			</td>
			<td class="odd">
				<s:if test="sourceOMS">
					<s:url escapeAmp="false" action="adjustmentMgmt" includeParams="none"
						namespace="/om/adjust" id="#lnkDetNum">
						<s:param name="selectedAdjustmentId" value="%{adjustmentId}" />
						<s:param name="selectedAdjustmentEntityId" value="%{adjustmentEntityId}" />
						<s:param name="selectedAdjustmentType" value="%{adjustment.adjustmentType.toString()}" />
						<s:param name="quickTabSelected" value="%{currentQuickTab}"/>
					</s:url>
					<a href="<s:property value="#lnkDetNum" />" 
						onclick="return showProcessingOnHref();">
						<s:property value="adjustmentId" /> 
					</a>
				</s:if>
				<s:else>
					<s:url  escapeAmp="false" includeParams="none"
						value="/adjustment/adjustment.do?"
						id="#tfadjustdetailurl">
						<s:param name="operation" value="'modifySavedAdjustmentforOMS'"/>
						<s:param name="tfAdjustmentCartId" value="%{adjustmentEntityId}"/>
					</s:url>
					<a href="<s:property value="#tfadjustdetailurl" />" 
						onclick="return showProcessingOnHref();">
						<s:property value="adjustmentId" /> 
					</a>
				</s:else>
			</td>
			<td class="odd">
			    <s:property	value="adjustmentTypeString" /> 
			</td>
			<td class="odd">
			    <s:property	value="adjustmentEntityId" /> 
			</td>
			<td class="odd">
			    <s:property	value="itemCount" /> 
			</td>
			<td class="odd">
			    <s:property	value="createDate" /> 
			</td>
			<td class="odd">
			    <s:property	value="createUser" /> 
			</td>
			<td class="odd">
			    <s:property	value="updateDate" /> 
			</td>
			<td class="odd">
			    <s:property	value="updateUser" /> 
			</td>
			<td class="odd">
			    <s:property	value="orderSource" /> 
			</td>
			<td class="odd">	
				<s:if test="sourceOMS">
					<s:url escapeAmp="false" action="searchAdjustments!deleteAdjustment" includeParams="none" namespace="/om/adjust" id="#lnkDetNum">
						<s:param name="selectedAdjustmentId" value="%{adjustmentId}" />
					</s:url>
					<a href="#" onclick="doDeleteAdj('<s:property value="#lnkDetNum"/>');return false;">
			        Cancel </a>
				</s:if>
				<s:else>
				     &nbsp;
				</s:else>
			</td>
		</tr>
