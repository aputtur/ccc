<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>
<tr id='savesubmitbuttons_top_<s:property value="item.ID"/>'>
	<td style="background-color: #6F97BB;"></td>
	<td colspan="2" align="right">
		<s:hidden name="hasProcessMessageErrors"
			id="hasProcessMessageErrors%{item.ID}" theme="simple"
			value="%{hasProcessMessageErrors}" />
		<s:hidden name="hasProcessMessages" id="hasProcessMessages%{item.ID}"
			theme="simple" value="%{hasProcessMessages}" />
		<s:hidden name="hasLastEdit" id="hasLastEdit%{item.ID}" theme="simple"
			value="%{hasLastEdit}" />
			
		<div id="angleTabs">
			<ul>
				<li>
					<s:submit
						onclick="discardDetailEdit($(this),'%{confNumber}','%{bundleId}','%{item.ID}');return false;"
						cssStyle="odd" theme="simple" value="Discard"></s:submit>
				</li>
				<li>
					<s:submit onclick="saveOneDetail('%{item.ID}');return false;"
						cssStyle="odd" theme="simple" value="Save"></s:submit>
				</li>
				<li>
					<div id='showDetailSaveAll_<s:property value="item.ID"/>'
						style="display: none;">
						<s:submit id="detailSaveAll_%{item.ID}"
							onclick="saveAllEditedDetails();return false;" cssStyle="odd"
							theme="simple" value="Save All Edits"></s:submit>
					</div>
				</li>
				<li>
					<div id='showDetailDiscardAll_<s:property value="item.ID"/>'
						style="display: none;">
						<s:submit id="detailDiscardAll_%{item.ID}"
							onclick="discardEntireOrder();return false;" cssStyle="odd"
							theme="simple" value="Discard All Edits"></s:submit>
					</div>
				</li>
			</ul>
		</div>
	</td>
</tr>

<s:if test="!processMessages.empty">
	<tr id='processMessagesEdit<s:property value="item.ID"/>'>
		<td style="background-color: #6F97BB;"></td>
		<td colspan="2" align="center">
			<fieldset style="width: 800px;">
				<legend>
					<span style="font-weight: bold;">Processing Messages</span>
				</legend>
				<ul style="text-align: left;">
					<s:iterator value="processMessages">
						<s:if test="error">
							<li style="color: red; font-weight: bold;">
								<s:property value="briefMessage" />
							</li>
						</s:if>
						<s:else>
							<li style="color: blue; font-weight: bold;">
								<s:property value="briefFormattedMessage" />
							</li>
						</s:else>
					</s:iterator>
				</ul>
			</fieldset>
		</td>
	</tr>
</s:if>

<tr>
	<td style="background-color: #6F97BB;"></td>
	<td colspan="2">
		<table class="subTableNoColor" style="background-color: #EAE8E8;"
			width="100%">
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<tr>
				<th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
					Tracking Information
				</th>
			</tr>
			<tr>
				<td class="odd-bold">
					Confirm #:
				</td>
				<td class="odd">
					<s:if test="includeConfirmPageLinks">
						<s:url escapeAmp="false" action="viewOrderHistory"
							namespace="/om/history" id="lnkConfNum">
							<s:param name="selectedConfirmNumber" value="%{confNumber}" />
							<s:param name="selectedBundleNumber" value="-1" />
							<s:param name="selectedDetailNumber" value="%{item.ID}" />
							<s:param name="selectedItem"
								value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@ORDER_HEADER" />
						</s:url>
						<a href="<s:property value="#lnkConfNum" />"><s:property
								value="confNumber" /> </a>
					</s:if>
					<s:else>
						<s:property value="confNumber" />
					</s:else>
				</td>
				<td class="odd-bold">
					Licensee Acct #:
				</td>
				<td class="odd">
					<s:property value="myConfirmation.confirmation.licenseeAccount" />
				</td>
				<td class="odd-bold">
					Entered By:
				</td>
				<td class="odd">
					<s:property value="myConfirmation.confirmation.orderCreatedBy" />
				</td>
				<td class="odd-bold">
					Order Source:
				</td>
				<td class="odd">
					<s:property value="myConfirmation.confirmation.orderSourceCd" />
				</td>
			</tr>
			<tr>
				<td class="odd-bold">
					TF Order #:
				</td>
				<td class="odd">
					<s:property value="item.externalOrderId" />
				</td>
				<td class="odd-bold">
					Licensee Name:
				</td>
				<td class="odd">
					<s:property value="myConfirmation.confirmation.licenseeName" />
				</td>
				<td class="odd-bold">
					Internet Login:
				</td>
				<td class="odd">
					<s:property value="myConfirmation.confirmation.buyerUserIdentifier" />
				</td>
				<td class="odd-bold">
					Order Stored in:
				</td>
				<td class="odd">
					<s:property value="item.orderDataSourceDisplay" />
				</td>			
			</tr>
			<tr>
				<td class="odd-bold">
					Invoice #:
				</td>
				<td class="odd">
					<s:property value="item.invoiceId" />
				</td>
				<td class="odd-bold">
					Payment Type:
				</td>
				<td class="odd">
					<s:property value="item.paymentTypeDisplay" />
				</td>
				<td class="odd-bold">
					Last Update Date:
				</td>
				<td class="odd">
					<s:property value="item.lastUpdatedDate" />
				</td>
				<td class="odd-bold">
					Permission Source:
				</td>
				<td class="odd">
					<s:property value="item.rightSourceCd" />
				</td>
			</tr>
			<tr>
				<td class="odd-bold">
					Detail Status:
				</td>
				<td class="odd">
					<s:property value="item.itemStatusInternalDisplay" />
				</td>
				<td class="odd-bold">
					Detail Cycle:
				</td>
				<td class="odd">
					<s:property value="item.itemCycleDisplay" />
				</td>
				<td class="odd-bold">
					Error Desc:
				</td>
				<td class="odd">
					<s:property value="item.itemErrorDescriptionDisplay" />
				</td>
				<td class="odd-bold">
					Permission Status:
				</td>
				<td class="odd">
		        	<s:property value="item.itemAvailabilityDescriptionInternal"/>
				</td>
			</tr>
				<s:if test="item.adjusted || item.distributionEvent != null || item.canceled">
					<tr>
						<s:if test="item.declineReason != null">
				        	<td class="odd-bold">Decline Reason:</td>
		        			<td class="odd"><s:property value="item.declineReason"/></td>
						</s:if>
		        		<s:else>
				        	<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
		        		</s:else>
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
						<s:if test="item.canceled">
							<td class="odd-bold" style="color:red; font-weight:bold; text-align: right;">
				   				Canceled
							</td>
				   			<td class="odd-bold">&nbsp;</td>
						</s:if>
						<s:else>
				   			<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
						</s:else>
					</tr>
				</s:if>
		</table>
		<table class="subTableNoColor" style="background-color: #EAE8E8;"
			width="100%">
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<col style="width: 100px;" />
			<tr>
				<th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
					Bibliographic &amp; Rights Information
				</th>
			</tr>
			<s:if test="overrideAllowEdit">
				<tr>
					<td class="odd" colspan="5">
						&nbsp;
					</td>
					<td class="odd-bold">
						TF Work ID:
					</td>
					<td colspan="2" class="odd">
						<s:textfield id="user_wr_tf_workid%{item.ID}"
							cssStyle="width: 70px;" theme="simple" />
						&nbsp;
						<s:submit theme="simple"
							onclick="popUpSearchWorksPageContent('%{item.ID}','user_wr_tf_workid%{item.ID}');return false;"
							theme="simple" cssStyle="odd" value="Update Work" />
					</td>
				</tr>
			</s:if>
			<tr>
				<td class="odd-bold">
					Title:
				</td>
				<td class="odd" colspan="5">
					<s:if test="hasLastEdit">
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.publicationTitle"
							value="%{lastEdit.item.publicationTitle}" />
					</s:if>
					<s:else>
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.publicationTitle" value="%{item.publicationTitle}" />
					</s:else>
				</td>
				<td class="odd" colspan="2">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="odd-bold">
					Article:
				</td>
				<td class="odd" colspan="5">
					<s:property value="item.itemSubDescription" />
				</td>
				<td class="odd" colspan="2">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="odd-bold">
					IDNO Type:
				</td>
				<td class="odd">
					<s:if test="hasLastEdit">
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.idnoLabel" value="%{lastEdit.item.idnoLabel}" />
					</s:if>
					<s:else>
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.idnoLabel" value="%{item.idnoLabel}" />
					</s:else>
				</td>
				<td class="odd-bold">
					IDNO:
				</td>
				<td class="odd">
					<s:if test="hasLastEdit">
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.standardNumber"
							value="%{lastEdit.item.standardNumber}" />
					</s:if>
					<s:else>
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.standardNumber" value="%{item.standardNumber}" />
					</s:else>
				</td>
				<td class="odd" colspan="2">
					&nbsp;
				</td>
			<s:if test="item.rightSourceCd != 'RL'">
				<td class="odd-bold">
					RH Ref #:
				</td>
				<td class="odd">
					<s:if test="hasLastEdit">
						<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 90px;"
							theme="simple" name="editItem.rhRefNum"
							value="%{lastEdit.item.rhRefNum}" />
					</s:if>
					<s:else>
						<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 90px;"
							theme="simple" name="editItem.rhRefNum" value="%{item.rhRefNum}" />
					</s:else>
				</td>
			</s:if>
			<s:else>
				<td class="odd-bold">
					&nbsp;
				</td>
				<td class="odd">
					&nbsp;
				</td>
			</s:else>
			</tr>
			<tr>
				<td class="odd-bold">
					Publisher:
				</td>
				<td class="odd" colspan="3">
					<s:if test="hasLastEdit">
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.publisher" value="%{lastEdit.item.publisher}" />
					</s:if>
					<s:else>
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.publisher" value="%{item.publisher}" />
					</s:else>
				</td>
				<td class="odd-bold">
					WR Work ID:
				</td>
				<td class="odd">
					<s:if test="hasLastEdit">
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.wrWorkInst" value="%{lastEdit.item.wrWorkInst}" />
					</s:if>
					<s:else>
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.wrWorkInst" value="%{item.wrWorkInst}" />
					</s:else>
				</td>
				<td class="odd-bold">
					RH Account #:
				</td>
				<td class="odd">
					<s:property value="item.rightsholderAccount" />
				</td>
			</tr>
			<tr>
				<td class="odd-bold">
					Rightsholder:
				</td>
				<td class="odd" colspan="3">
					<s:if test="hasLastEdit">
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.rightsholder"
							value="%{lastEdit.item.rightsholder}" />
					</s:if>
					<s:else>
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.rightsholder" value="%{item.rightsholder}" />
					</s:else>
				</td>
				<td class="odd-bold">
					TF Work ID:
				</td>
				<td class="odd">
					<s:if test="hasLastEdit">
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.workInst" value="%{lastEdit.item.workInst}" />
					</s:if>
					<s:else>
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.workInst" value="%{item.workInst}" />
					</s:else>
				</td>
				<td class="odd-bold">
					Right Inst:
				</td>
				<td class="odd">
					<s:if test="hasLastEdit">
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.rgtInst"
							value="%{lastEdit.item.rgtInst}" />
					</s:if>
					<s:else>
						<s:label cssClass="ltype%{item.ID}" theme="simple"
							name="editItem.rgtInst" value="%{item.rgtInst}" />
					</s:else>
				</td>
			</tr>
			<tr>
				<td class="odd-bold">
					Product:
				</td>
				<td class="odd" colspan="3">
					<s:property value="item.productAndCategoryName" />
				</td>
				<td class="odd-bold">
					Type of Use:
				</td>
				<td colspan="3" class="odd">
					<s:property value="item.typeOfUseDescription" />
				</td>
			</tr>
		</table>

		<s:if test="item.republication">
			<table class="subTableNoColor" style="background-color: #EAE8E8;"
				width="100%">
			<col style="width: 100px;"/>
			<col style="width: 100px;"/>
			<col style="width: 100px;"/>
			<col style="width: 100px;"/>
			<col style="width: 100px;"/>
			<col style="width: 100px;"/>
			<col style="width: 100px;"/>
			<col style="width: 100px;"/>
				<tr><th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
					Republication Information
				</th></tr>
				<tr>
					<td class="odd-bold">
						Repub Work:
					</td>

					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssStyle="width: 150px;"
									cssClass="itype%{item.ID} required" theme="simple"
									name="editItem.repubWork"
									value="%{lastEdit.item.newPublicationTitle}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 150px;" name="editItem.repubWork"
									value="%{item.newPublicationTitle}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.newPublicationTitle" />
						</s:else>
					</td>	
										
					<s:if test="editable">
						<td class="required-bold" colspan="3" >
							Circ/Dist:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold" colspan="3">
							Circ/Dist:
						</td>
					</s:else>	
					
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssStyle="width: 150px;"
									cssClass="itype%{item.ID} required" theme="simple"
									name="editItem.circulationDistribution"
									value="%{lastEdit.item.circulationDistribution}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 150px;" name="editItem.circulationDistribution"
									value="%{item.circulationDistribution}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.circulationDistribution" />
						</s:else>
					</td>									
				</tr>
							
				<tr>				
					<s:if test="editable">
						<td class="required-bold">
							Repub Date:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Repub Date:
						</td>
					</s:else>					
					
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssStyle="width: 150px;"
									cssClass="itype%{item.ID} required" theme="simple"
									name="editItem.republicationDate"
									value="%{lastEdit.item.republicationDate}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 150px;" name="editItem.republicationDate"
									value="%{item.republicationDate}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.republicationDate" />
						</s:else>
					</td>					
					

					<s:if test="editable">
						<td class="required-bold" colspan="3">
							Repub Pub:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold" colspan="3">
							Repub Pub:
						</td>
					</s:else>
					
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssStyle="width: 150px;" cssClass="itype%{item.ID} required"
									theme="simple" name="editItem.republishingOrganization"
									value="%{lastEdit.item.republishingOrganization}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 150px;" name="editItem.republishingOrganization"
									value="%{item.republishingOrganization}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.republishingOrganization" />
						</s:else>
					</td>
					
					<td class="odd-bold">
						For Profit:
					</td>
					
					<td class="odd">
							<s:property value="item.business" />
					</td>
				</tr>
			</table>
		</s:if>
				
 		<s:if test="item.aPS || item.eCCS">
			<jsp:include
				page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailLongAcademic.jsp" />
		</s:if>
		<s:if test="item.photocopy">
			<jsp:include
				page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailLongPhotocopy.jsp" />		
		</s:if>
		<s:if test="item.digital">
			<jsp:include
				page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailLongDigital.jsp" />		
		</s:if>
		<s:if test="item.republication">
			<jsp:include
				page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailLongRepublish.jsp" />
		</s:if>
		
		<s:if test="overrideAllowEdit">
		<table class="subTableNoColor" style="background-color: #EAE8E8; width: 100%;">
			<tr>
					<td width="100%" align="right"><s:submit onclick="priceOneDetail('%{item.ID}','%{detailIndex}');return false;"
						cssStyle="odd" theme="simple" value="Check pricing"></s:submit>
					</td>			
			</tr>
		</table>
		</s:if>

		<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneDetailLongPermission.jsp" />		
						 			
	</td>
</tr>
<tr id='savesubmitbuttons_bottom_<s:property value="item.ID"/>'>
	<td style="background-color: #6F97BB;"></td>
	<td colspan="2" align="right">
		<div id="angleTabs">
			<ul>
				<li>
					<s:submit
						onclick="discardDetailEdit($(this),'%{confNumber}','%{bundleId}','%{item.ID}');return false;"
						cssStyle="odd" theme="simple" value="Discard"></s:submit>
				</li>
				<li>
					<s:submit onclick="saveOneDetail('%{item.ID}');return false;"
						cssStyle="odd" theme="simple" value="Save"></s:submit>
				</li>
				<li>
					<div id='showDetailSaveAll_<s:property value="item.ID"/>'
						style="display: none;">
						<s:submit id="detailSaveAll_%{item.ID}"
							onclick="saveAllEditedDetails();return false;" cssStyle="odd"
							theme="simple" value="Save All Edits"></s:submit>
					</div>
				</li>
				<li>
					<div id='showDetailDiscardAll_<s:property value="item.ID"/>'
						style="display: none;">
						<s:submit id="detailDiscardAll_%{item.ID}"
							onclick="discardEntireOrder();return false;" cssStyle="odd"
							theme="simple" value="Discard All Edits"></s:submit>
					</div>
				</li>
			</ul>
		</div>
	</td>
</tr>


