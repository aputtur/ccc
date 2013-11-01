<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>
<div>
	<table width="100%">
		<tr>
			<td style="width: 952px;">
				<div
					id="order_<s:property value="selectedConfirmNumber"/>_course_<s:property value="bundle.bundleId"/>_details_expandheader">
					<table width="100%">
						<tr style="background-color: #E2E9F0;">
							<td valign="middle" style="width: 15px; text-align: center;">
								<a href="#"
									onclick="toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="bundle.bundleId"/>');return false;"><span
									style="margin-top: 5px;"> <img alt="Expand" border="0"
											src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
								</span> </a>
							</td>
							<td style="height: 26px; width: 942px;">
								<div id="sectionHeader" style="display: inline; font-size: 14px;">
									Details
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
					id="order_<s:property value="selectedConfirmNumber"/>_course_<s:property value="bundle.bundleId"/>_details_collapseheader"
					style="margin-bottom: 10px; display: none;">
					<table style="margin-bottom: 0px;" width="100%">
						<tr style="background-color: #E2E9F0;">
							<td valign="top" style="width: 15px; padding-top: 5px; text-align: center;">
								<a href="#"
									onclick="toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="bundle.bundleId"/>');return false;">
									<img alt="Collapse" border="0"
										src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" />
								</a>
							</td>
							<td valign="middle" style="height: 26px; width: 942px;">
								<div id="sectionHeader"
									style="display: inline; padding-top: 2px; font-size: 14px;">
									Details
									<div style="display: inline; margin-left: 100px; font-size: 10px; vertical-align: middle;">
										<a href="#"
											onclick="expandAllBundleDetails('<s:property value="bundle.bundleId"/>');return false;">
											<img border="0"
												src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
										</a>&nbsp;Expand All &nbsp;
										<a href="#"
											onclick="collapseAllBundleDetails('<s:property value="bundle.bundleId"/>');return false;">
											<img border="0"
												src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" />
										</a>&nbsp;Collapse All
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
									<div style="display: inline; margins: 0px; padding: 0px;"
										id="order_<s:property value="selectedConfirmNumber"/>_course_<s:property value="bundle.bundleId"/>_details">

										<s:iterator value="myItems" status="iterStatus">
											<table width="100%">
												<tr>
													<td style="background-color: #C6C6C6">
														<div
															id="showdetail_bundle_<s:property value="bundleId"/>_order_<s:property value="confNumber"/>_detail_<s:property value="item.iD"/>">
															<jsp:include
																page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailShort.jsp"></jsp:include>
														</div>
													</td>
												</tr>
											</table>
											<table width="100%">
												<tr>
													<td>
														<div style="background-color: #FFFFFF; display: none;"
															id="editdetail_bundle_<s:property value="bundleId"/>_order_<s:property value="confNumber"/>_detail_<s:property value="item.iD"/>">
															<jsp:include
																page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailEdit.jsp"></jsp:include>
															</div>
													</td>
												</tr>
											</table>
										</s:iterator>										
									</div>

							</td>
						</tr>

					</table>
				</div>
			</td>
		</tr>
	</table>

</div>