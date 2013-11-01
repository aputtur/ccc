<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>


<div id="order_course_<s:property value="bundle.bundleId"/>">


	<div style="margin-top: 3px;"
		id="order_course_<s:property value="bundle.bundleId"/>_pg_<s:property value="#bStatus.index"/>_expandheader">
		<table class="editTable">
			<tr style="background-color: #E2E9F0;">
				<td valign="middle" style="width: 15px; text-align: center;">
					<a href="#"
						onclick="toggleBundleHeaderSection('<s:property value="#bStatus.index"/>');return false;"><span
						style="margin-top: 5px;"> <img alt="Expand" border="0"
								src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
					</span> </a>
				</td>
				<td style="height: 30px; width: 972px;">
					<div id="sectionHeader" style="display: inline; font-size: 14px;">
						<s:if test="!bundle.courseName.empty">
							<s:property value="bundle.courseName" />
						</s:if>
						<s:else>Project Information</s:else>
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
		id="order_course_<s:property value="bundle.bundleId"/>_pg_<s:property value="#bStatus.index"/>_collapseheader"
		style="margin-bottom: 15px; margin-top: 3px; display: none;">


		<table border="0" class="editTable" style="margin-bottom: 0px;">
			<tr style="background-color: #E2E9F0;">
				<td valign="top" style="width: 15px; text-align: center;">
					<a href="#"
						onclick="toggleBundleHeaderSection('<s:property value="#bStatus.index"/>');return false;">
						<img alt="Collapse" border="0"
							src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" />
					</a>
				</td>
				<td id="sectionHeader" valign="middle"
					style="height: 26px; width: 542px;">
					<div id="sectionHeader"
						style="display: inline; padding-top: 4px; font-size: 14px;">
						<s:if test="!bundle.courseName.empty">
							<s:property value="bundle.courseName" />
						</s:if>
						<s:else>Project Information</s:else>
					</div>
					</td>
					
					<td>
						<a href="#" theme="simple" style=" float: left; padding-top: 4px; font-size: 14px;  color: #6F97BB; " onClick='showAuditBundleHistoryWindow(<s:property value="bundle.bundleId"/>, <s:property value="%{searchConfirmNumber}"/>, "<s:property value="bundle.courseName"/>" )'>Project History</a>
					</td>
					
					<td>
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
										<li
											id='course_<s:property value="bundle.bundleId"/>_addDetail_href'>
											<s:url escapeAmp="false" action="addDetail"
												namespace="/om/neworder" id="lnkAddProjDet">
												<s:param name="selectedConfirmNumber"
													value="%{selectedOrder.confirmation.confirmationNumber}" />
												<s:param name="selectedBundleNumber"
													value="%{bundle.bundleId}" />
												<s:param name="selectedDetailNumber"
													value="%{selectedDetailNumber}" />
												<s:param name="selectedItem"
													value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@BUNDLE_DETAIL" />
												<s:param name="addingProject" value="%{false}" />
											</s:url>
											<a class="detailAngleTabs" href="#"
												onclick="javascript:showCourseAddDetail('<s:property value="bundle.bundleId"/>','<s:property value="courseNameJSP"/>','<s:property value="#lnkAddProjDet"/>');return false;">Add&nbsp;Detail
											</a><span><img align="top" border="0" width="19"
													height="26"
													src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
											</span>
										</li>
									</s:if>
								</s:if>

								<s:if
									test="hasManageOrdersPrivelege && haveOpenDetails && haveDetailsStoredInOMS && !haveAdjustedDetails">
									<li id='course_<s:property value="bundle.bundleId"/>_edit_href'>
										<a class="detailAngleTabs" href="#"
											onclick="javascript:showCourseEdit('<s:property value="bundle.bundleId"/>','<s:property value="escapedCourseName"/>');return false;">Edit&nbsp;Project
										</a><span><img align="top" border="0" width="19"
												height="26"
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

					<div id="clearer"></div>
					<jsp:include
						page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/storedIn/viewStoredInBundleDetails.jsp" />
				</td>
			</tr>
		</table>
	</div>
</div>

<script language="javascript">
	
	function showAuditBundleHistoryWindow(bundleId, confirmNumber, projectName)
	{
		//showAuditBundleHistoryWindow(bundleId, confirmNumber, projectName)
		
		//var url = "viewBundleAuditHistory.action?bundleId=" + bundleId + "&confirmNumber=" + confirmNumber+ "&projectName=" + projectName;
		var url = "viewBundleAuditHistory.action?bundleId=" + bundleId + "&confirmNumber=" + confirmNumber + "&projectName=" + projectName;
		//auditHistoryWindow = window.open(url,'Audit History','height=900, width=1200, scrollbars=1, location=0, modal=yes');
		
		
		if (window.showModalDialog) 
		{
			auditHistoryWindow = window.showModalDialog(url,'Project History',
									"dialogWidth:1200px;dialogHeight:900px");
		} 
		else 
		{
			auditHistoryWindow = window.open(url,'Audit History','height=900, width=1200, scrollbars=1, location=0, modal=yes');
		}
		
		
	}
</script>