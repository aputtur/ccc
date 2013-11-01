<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>
	<table width="100%">
		<tr>
			<td style="width: 972px;">
				<div id="order_details_expandheader">
					<table width="100%">
						<tr style="background-color: #E2E9F0;">
							<td valign="middle" style="width: 15px; text-align: center;">
								<a href="#" onclick="toggleDetailHeaderSection();return false;"><span
									style="margin-top: 5px;"> <img alt="Expand" border="0"
											src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
								</span> </a>
							</td>
							<td style="height: 26px; width: 942px;">
								<div id="sectionHeader"
									style="display: inline; padding-top: 2px; font-size: 14px;">
									Individual Details
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
				<div id="order_details_collapseheader" style="margin-bottom: 10px; display: none;">
					<table style="margin-bottom: 0px; width: 100%;">
						<tr style="background-color: #E2E9F0;">
							<td valign="middle" style="width: 15px; text-align: center;">
								<a href="#" onclick="toggleDetailHeaderSection();return false;">
									<img alt="Collapse" border="0"
										src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" />
								</a>
							</td>
							<td valign="middle" style="height: 26px; width: 942px;">
								<div id="sectionHeader"
									style="display: inline; padding-top: 2px; font-size: 14px;">
									Individual Details
									<div style="display: inline; margin-left: 100px; font-size: 10px; vertical-align: middle;">
										<a href="#"
											onclick="expandAllOrderDetails('<s:property value="bundleId"/>');return false;"><span
											> <img border="0"
													src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
										</span> </a>&nbsp;Expand All &nbsp;
										<a href="#"
											onclick="collapseAllOrderDetails('<s:property value="bundleId"/>');return false;"><span
											> <img border="0"
													src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" />
										</span> </a>&nbsp;Collapse All
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td rowspan="2" valign="top"
								style="background-color: #999999; width: 10px; text-align: center;">
								&nbsp;
							</td>
							<td colspan="2">
								<s:if test="pagingDetails[individualDetailsIndex]">
								  <s:form theme="simple" id="viewOrderStoredInOmsHistory%{individualDetailsIndex}" action="viewOrderStoredInOmsHistory" method="post" namespace="/om/history">
								    <s:hidden cssClass="cctlnoclear" name="pagingGroupNumber" value="-1"/>
								    <div style="display: inline; margin-left: 3px;" id="pagingDetailsControlsTop">
										<table width="100%">
										<tr>
										    <td style="width: 4px;">&nbsp;</td>
											<td  style="padding-left: 20px; padding-top: 8px;">
												<s:component theme="xhtml" template="multipleCustomSortControl">
													<s:param name="sortSelectList" value="'viewOrderSortByList'"/>
													<s:param name="sortListKey" value="'value'"/>
													<s:param name="sortListValue" value="'label'"/>
													<s:param name="formId" value="'viewOrderStoredInOmsHistory'"/>
													<s:param name="onChangeDefaultSortValue" value="'SORT_BY_VIEW_DEFAULT'"/>
													<s:param name="sortByProperty" value="'searchCriteria[%{individualDetailsIndex}].sortCriteriaBy'"/>
													<s:param name="sortOrderProperty" value="'searchCriteria[%{individualDetailsIndex}].sortOrder'"/>
													<s:param name="defaultText" value="'view.order.default.sort.text'"/>
													<s:param name="pagingIndex" value="%{individualDetailsIndex}"></s:param>
													<s:param name="pagingIterator" value="'[%{individualDetailsIndex}]'"></s:param>
												</s:component>
											</td>
										    <td style="width: 10px;">&nbsp;</td>
										</tr>
										<tr>
										<td colspan="3"><hr style="margin: 0px; padding: 0px;"/></td>
										</tr>
										<tr>
										    <td style="width: 4px;">&nbsp;</td>
											<td style="padding-left: 20px; padding-bottom: 8px;">
											    <s:component theme="xhtml" template="multipleCustomPageSizeControl">
					       							<s:param name="pagingFormId" value="'viewOrderStoredInOmsHistory'"/>
					       							<s:param name="pagingObject" value="'pageControl[%{individualDetailsIndex}]'"/>
													<s:param name="pagingIndex" value="%{individualDetailsIndex}"></s:param>
													<s:param name="pagingIterator" value="'[%{individualDetailsIndex}]'"></s:param>
					    					    </s:component>
											    <s:component theme="xhtml" template="multipleCustomPageControl">
					       							<s:param name="pagingFormId" value="'viewOrderStoredInOmsHistory'"/>
					       							<s:param name="pagingObject" value="'pageControl[%{individualDetailsIndex}]'"/>
					       							<s:param name="inFooter" value="'false'"/>
													<s:param name="pagingIndex" value="%{individualDetailsIndex}"></s:param>
													<s:param name="pagingIterator" value="'[%{individualDetailsIndex}]'"></s:param>
											    </s:component>
											</td>
										    <td style="width: 4px;">&nbsp;</td>
													</tr>
												</table>
								    
								    </div>
									<div style="display: inline; margins: 0px; padding: 0px;"
										id="order_<s:property value="selectedConfirmNumber"/>_course_<s:property value="bundle.bundleId"/>_details">
											<s:iterator value="wrappedResults[individualDetailsIndex].detailsList" status="iterStatus">
				
												<table width="100%">
													<tr>
														<td style="background-color: #C6C6C6;" >
															<div
																id="showdetail_order_<s:property value="bundleId"/>_order_<s:property value="confNumber"/>_detail_<s:property value="item.iD"/>">
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
																id="editdetail_order_<s:property value="bundleId"/>_order_<s:property value="confNumber"/>_detail_<s:property value="item.iD"/>">
																<jsp:include
																	page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailEdit.jsp"></jsp:include>
															</div>
														</td>
													</tr>
												</table>
											</s:iterator>

									</div>
								    <div style="display: inline; margin-left: 3px;" id="pagingDetailsControlsBottom">
											    <s:component theme="xhtml" template="multipleCustomPageControl">
					       							<s:param name="pagingFormId" value="'viewOrderStoredInOmsHistory'"/>
					       							<s:param name="pagingObject" value="'pageControl[%{individualDetailsIndex}]'"/>
					       							<s:param name="inFooter" value="'true'"/>
													<s:param name="pagingIndex" value="%{individualDetailsIndex}"></s:param>
													<s:param name="pagingIterator" value="'[%{individualDetailsIndex}]'"></s:param>
											    </s:component>
								    
								    </div>
								    </s:form>
								</s:if>
								<s:else>
									<div style="display: inline; margins: 0px; padding: 0px;"
										id="order_<s:property value="selectedConfirmNumber"/>_course_<s:property value="bundle.bundleId"/>_details">
											<s:iterator value="wrappedResults[individualDetailsIndex].detailsList" status="iterStatus">
				
												<table width="100%">
													<tr>
														<td style="background-color: #C6C6C6;" >
															<div
																id="showdetail_order_<s:property value="bundleId"/>_order_<s:property value="confNumber"/>_detail_<s:property value="item.iD"/>">
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
																id="editdetail_order_<s:property value="bundleId"/>_order_<s:property value="confNumber"/>_detail_<s:property value="item.iD"/>">
																<jsp:include
																	page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailEdit.jsp"></jsp:include>
															</div>
														</td>
													</tr>
												</table>
											</s:iterator>
									</div>
								</s:else>
							</td>
						</tr>

					</table>
				</div>
			</td>
		</tr>
	</table>