<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>
<table class="editTable" width="100%" style="margin-botton: 0px;">
	<tr style="background-color: #E2E9F0;">
		<td valign="top" style="width: 5px; text-align: center;">
			&nbsp;
		</td>
		<td valign="middle" style="height: 26px; width: 959px;">
			<div id="sectionHeader"
				style="display: inline; padding-top: 2px; font-size: 14px;">
				Confirmation Header
			</div>

			<div id="order_header_href" style="display: inline; float: right;">
				<div id="detailAngleTabs">
					<ul>
						<li id='order_show_href'>
							<a class="detailAngleTabs" href="#" id="order_show_href_id"
								onclick="toggleConfirmation(this);return false;">Hide&nbsp;Section
							</a><span><img align="top" border="0" width="19" height="26"
									src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
							</span>
						</li>
						<s:if test="overrideAllowEdit">
						<li id='order_addProject_href'>
													<s:url escapeAmp="false" action="addDetail"
														namespace="/om/neworder" id="lnkAddProj">
														<s:param name="selectedConfirmNumber"
																value="%{selectedOrder.confirmation.confirmationNumber}" />
															<s:param name="selectedBundleNumber" value="%{-1}" />
															<s:param name="addingProject" value="%{true}" />
													</s:url>
													<a class="detailAngleTabs" href="<s:property value="lnkAddProj" />"
														onclick="showNotImplemented('Add project to confirmation');return false;">Add&nbsp;Project</a><span><img
															align="top" border="0" width="20" height="26"
															src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
													</span>
												</li> 
												
						<s:if test="!invoiceView && haveDetailsStoredInOMS">
						<li id='order_addDetail_href'>
							<s:url escapeAmp="false" action="addDetail"
								namespace="/om/neworder" id="lnkAddDet">
								<s:param name="selectedConfirmNumber" value="%{selectedConfirmNumber}" />
								<s:param name="selectedBundleNumber" value="%{-1}" />
								<s:param name="selectedDetailNumber" value="%{selectedDetailNumber}" />
								<s:param name="selectedItem" value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@ORDER_HEADER" />
								<s:param name="addingProject" value="%{false}" />
							</s:url>
							<a class="detailAngleTabs"
								href="#"
								onclick="showConfirmationAddDetail('<s:property value="selectedConfirmNumber"/>','<s:property value="lnkAddDet" />');return false;">Add&nbsp;Detail</a><span><img
									align="top" border="0" width="20" height="26"
									src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
							</span>
						</li>
						</s:if>
        				<s:if test="haveDetailsStoredInOMS && !haveAdjustedDetails">
						<li id='order_edit_href'>
							<a class="detailAngleTabs" href="#"
								onclick="showConfirmationEdit('the confirmation header section');return false;">Edit&nbsp;Section</a><span>
							<img
									align="top" border="0" width="20" height="26"
									src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
							</span>
						</li>
						</s:if>
						</s:if>
					</ul>
				</div>
			</div>
		</td>
	</tr>
</table>

<jsp:include
	page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewConfirmation.jsp" />

