<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<div id="order_course_<s:property value="bundle.bundleId"/>">


	<div style="margin-top: 3px;"
		id="order_course_<s:property value="bundle.bundleId"/>_expandheader">
		<table class="editTable">
			<tr style="background-color: #E2E9F0;">
				<td valign="middle" style="width: 15px; text-align: center;">
					<a href="#"
						onclick="toggleCourseHeaderSection('<s:property value="bundle.bundleId"/>');return false;"><span
						style="margin-top: 5px;"> <img alt="Expand" border="0"
								src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
					</span> </a>
				</td>
				<td style="height: 30px; width: 942px;">
					<div id="sectionHeader" style="display: inline; font-size: 14px;">
						<s:if test="!bundle.courseName.empty">
							<s:property value="bundle.courseName" />
						</s:if>
						<s:else>Bundle Information</s:else>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					&nbsp;
				</td>
			</tr>
		</table>
	</div>
	<div
		id="order_course_<s:property value="bundle.bundleId"/>_collapseheader"
		style="margin-bottom: 15px; margin-top: 3px; display: none;">
		<table class="editTable" style="margin-bottom: 0px;">
			<tr style="background-color: #E2E9F0;">
				<td valign="top" style="width: 15px; text-align: center;">
					<a href="#"
						onclick="toggleCourseHeaderSection('<s:property value="bundle.bundleId"/>');return false;">
						<img alt="Collapse" border="0"
							src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" />
					</a>
				</td>
				<td id="sectionHeader" valign="middle" style="height: 26px; width: 942px;">
					<div id="sectionHeader" style="display: inline; padding-top: 4px;font-size: 14px;">
						<s:if test="!bundle.courseName.empty">
							<s:property value="bundle.courseName" />
						</s:if>
						<s:else>Bundle Information</s:else>
					</div>

					<div style="float: right;"
						id="order_course_<s:property value="bundle.bundleId"/>_header_href">
						<div id="detailAngleTabs">
							<ul>
								<li id='course_<s:property value="bundle.bundleId"/>_show_href'>
									<a class="detailAngleTabs" href="#"
										id='course_<s:property value="bundle.bundleId"/>_show_href_id'
										onclick="toggleBundle('<s:property value="bundle.bundleId"/>',this);return false;">Hide&nbsp;Section
									</a><span><img align="top" border="0" width="19" height="26"
											src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
									</span>
								</li>
								<s:if test="overrideAllowEdit">
						      <s:if test="!invoiceView && haveDetailsStoredInOMS">
								<li id='course_<s:property value="bundle.bundleId"/>_addDetail_href'>
									<s:url escapeAmp="false" action="addDetail"
														namespace="/om/neworder" id="lnkAddProjDet">
															<s:param name="selectedConfirmNumber" value="%{selectedOrder.confirmation.confirmationNumber}" />
															<s:param name="selectedBundleNumber" value="%{bundle.bundleId}" />
															<s:param name="selectedDetailNumber" value="%{selectedDetailNumber}" />
															<s:param name="selectedItem" value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@BUNDLE_DETAIL" />
															<s:param name="addingProject" value="%{false}" />
													</s:url>
									<a class="detailAngleTabs" href="#"
										onclick="javascript:showCourseAddDetail('<s:property value="bundle.bundleId"/>','<s:property value="courseNameJSP"/>','<s:property value="#lnkAddProjDet"/>');return false;">Add&nbsp;Detail
									</a><span><img align="top" border="0" width="19" height="26"
											src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
									</span>
								</li>
								</s:if>
							  </s:if>
        				      <s:if test="haveDetailsStoredInOMS && !haveAdjustedDetails">
								<li id='course_<s:property value="bundle.bundleId"/>_edit_href'>
									<a class="detailAngleTabs" href="#"
										onclick="javascript:showCourseEdit('<s:property value="bundle.bundleId"/>','<s:property value="escapedCourseName"/>');return false;">Edit&nbsp;Project
									</a><span><img align="top" border="0" width="19" height="26"
											src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
									</span>
								</li>
							  </s:if>
							 
							</ul>
						</div>
					</div>
				</td>
			</tr>
		</table>
		

		<table>
			<tr>
				<td rowspan="2" valign="top"
					style="background-color: #999999; width: 15px; text-align: center;">
					&nbsp;
				</td>
				<td>
					<div
						id="order_course_<s:property value="bundle.bundleId"/>_bundlecontent">
						<table class="editTable">
							<tr>
								<td>
									<jsp:include
										page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneBundleShow.jsp"></jsp:include>
								</td>
							</tr>
						</table>
						<table class="editTable">
							<tr>
								<td>
									<jsp:include
										page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneBundle.jsp"></jsp:include>

								</td>
							</tr>
						</table>
					</div>
				</td>

			</tr>
		<s:if test="!pagingDetails">
			<tr>
				<td>
					<jsp:include
						page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewBundleDetails.jsp"></jsp:include>
				</td>
			</tr>
		</s:if>
		</table>

	</div>
</div>

