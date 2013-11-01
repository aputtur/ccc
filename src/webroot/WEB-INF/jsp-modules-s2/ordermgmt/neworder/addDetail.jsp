<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div
	style="font-size: 10px; background-color: #E2E9F0; padding-top: 10px; padding-bottom: 10px; padding-left: 20px; padding-right: 20px;">
	&lt;&lt;&nbsp;
	<a href="#" onclick="exitAddDetail(); return false;"> back to Confirmation</a>
</div>

<s:form action="addDetail" namespace="/om/neworder" method="post" id="addDetail_0">

	<s:hidden theme="simple" name="selectedConfirmNumber"
		value="%{searchConfirmNumber}" />
	<s:hidden theme="simple" name="selectedBundleNumber"
		value="%{searchBundleNumber}" />
	<s:hidden theme="simple" name="selectedDetailNumber"
		value="%{searchDetailNumber}" />

	<s:hidden theme="simple" name="disableSelections"
		value="%{disableSelections}" />
	<s:hidden theme="simple" name="addingDetail" value="false" />
	<s:hidden theme="simple" name="numAddedDetails" />

	<br />
	<div style="width: 30%; float: left; padding: 0px; margin: 0px;">
		<h1
			style="display: inline; color: #6F97BB; padding: 0px; margin: 0px;">
			Add a Detail
		</h1>
	</div>

	<table width="942px">

		<tr id="parentInfo">
			<td>
				<div id="divConfInfo" align="left">
					<table class="subTable">
						<s:if test="!showBundleInfo">
							<tr>
								<s:label label="Confirmation #" cssClass="odd"
									cssStyle="text-align: left;"
									value="%{selectedOrder.confirmation.confirmationNumber}">
									<s:param name="inputcolspan" value="%{3}" />
								</s:label>
							</tr>
						</s:if>
						<s:else>
							<tr>
								<s:label label="Project Name" cssClass="odd"
									cssStyle="text-align: left;" value="%{bundle.courseName}">
									<s:param name="inputcolspan" value="%{3}" />
								</s:label>
							</tr>
						</s:else>
						<tr>
							<s:label label="Licensee Account Number" cssClass="odd"
								cssStyle="width: 120px;" value="%{selectedOrder.confirmation.acctNumber}">
							</s:label>
							<s:label label="Licensee Name" cssClass="odd"
								cssStyle="width: 120px;" value="%{selectedOrder.confirmation.licenseeName}">
							</s:label>

						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr id="selectUse">
			<td>
				<table width="942px" class="subTable">
					<tr style="background-color: #E2E9F0; height: 25px;">
						<th colspan="2" align="left" style="padding-left: 10px;">
							Select Use
						</th>
					</tr>
					<tr>
						<s:if test="showBundleInfo">
							<s:select cssClass="required" cssStyle="width: 140px;"
							id="selectProduct" list="acadProductList" listKey="value"
							listValue="label" headerKey="" headerValue="-- Select One --"
							label="Product" name="newItem.product"></s:select>
						</s:if>
						<s:else>
							<s:select cssClass="required" cssStyle="width: 140px;"
							id="selectProduct" list="nonAcadProductList" listKey="value"
							listValue="label" headerKey="" headerValue="-- Select One --"
							label="Product" name="newItem.product"
							onchange="productSelected(this); false;"></s:select>
						</s:else>
					</tr>
					<tr>
					<td class="required-bold">
								Type of Use:
							</td>
							<td class="required">
						<s:select cssClass="required" cssStyle="width: 140px; display: none;" id="selectTouAPS"
							list="touListAPS" listKey="value" listValue="label" headerKey=""
							headerValue="-- Select One --" label="Type of Use" theme="simple"
							name="newItem.tou" onchange="touSelected(this); false;"></s:select>
						<s:select cssClass="required" cssStyle="width: 140px; display: none;" id="selectTouDPS"
							list="touListDPS" listKey="value" listValue="label" headerKey=""
							headerValue="-- Select One --" label="Type of Use" theme="simple"
							name="newItem.tou" onchange="touSelected(this); false;"></s:select>
						<s:select cssClass="required" cssStyle="width: 140px; display: none;" id="selectTouTRS"
							list="touListTRS" listKey="value" listValue="label" headerKey=""
							headerValue="-- Select One --" label="Type of Use" theme="simple"
							name="newItem.tou" onchange="touSelected(this); false;"></s:select>
						<s:select cssClass="required" cssStyle="width: 140px; display: none;" id="selectTouRLS"
							list="touListRLS" listKey="value" listValue="label" headerKey=""
							headerValue="-- Select One --" label="Type of Use" theme="simple"
							name="newItem.tou" onchange="touSelected(this); false;"></s:select>
						<s:select cssClass="required" cssStyle="width: 140px; display: none;" id="selectTouECCS"
							list="touListECCS" listKey="value" listValue="label" headerKey=""
							headerValue="-- Select One --" label="Type of Use" theme="simple"
							name="newItem.tou" onchange="touSelected(this); false;"></s:select>
						<s:select cssClass="required" cssStyle="width: 140px; display: none;" id="selectTouRLR"
							list="touListRLR" listKey="value" listValue="label" headerKey=""
							headerValue="-- Select One --" label="Type of Use" theme="simple"
							name="newItem.tou" onchange="touSelected(this); false;"></s:select>
						<s:select cssClass="required" cssStyle="width: 140px; display: none;" id="selectTouRL"
							list="touListRL" listKey="value" listValue="label" headerKey=""
							headerValue="-- Select One --" label="Type of Use" theme="simple"
							name="newItem.tou" onchange="touSelected(this); false;"></s:select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<s:if test="showBundleInfo">
			<tr id="projectInfo">
				<td>

					<div id="sectionHeader" style="display: inline; font-size: 14px;">
						<s:if test="!bundle.courseName.empty">
							<s:property value="bundle.courseName" />
						</s:if>
						<s:else>Bundle Information</s:else>
						&nbsp;
						<s:property value="bundle.bundleId" />
					</div>
					<jsp:include
						page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneBundleShow.jsp"></jsp:include>

				</td>
			</tr>
		</s:if>
		<tr id="detailEntry">
			<td>

				<table class="editTable" style="margin-bottom: 0px;">
					<tr style="background-color: #E2E9F0;">
						<!-- <td valign="top" style="width: 15px; text-align: center;">
					<a href="#"
						onclick="toggleCourseHeaderSection('<s:property value="bundle.bundleId"/>');return false;">
						<img alt="Collapse" border="0"
							src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" />
					</a>
				</td> -->
						<td id="sectionHeader" valign="middle" style="height: 26px; width: 942px;">
							<div id="sectionHeader"
								style="display: inline; padding-top: 4px; font-size: 14px;">
								Detail Entry Template
							</div>

							<div style="float: right;"
								id="order_course_<s:property value="bundle.bundleId"/>_header_href">
								<div id="detailAngleTabs">
									<ul>
										<li id='simpleAdd'>
											<a class="detailAngleTabs" href="#"
												onclick="addDetailClient(); return false;">Add&nbsp;to&nbsp;Order
											</a><span><img align="top" border="0" width="19"
													height="26"
													src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
											</span>
										</li>
										<li id='copyAdd'>
											<a class="detailAngleTabs" href="#"
												onclick="addDetailClient(); return false;">Copy&nbsp;and&nbsp;Add&nbsp;to&nbsp;Order
											</a><span><img align="top" border="0" width="19"
													height="26"
													src="<s:url value="/resources/ordermgmt/images/spacer.gif"/>"></img>
											</span>
										</li>
									</ul>
								</div>
							</div>
						</td>
					</tr>
				</table>
				<div id="addDetailManually">

					<!-- 	<s:if test="addingProject">
							<table>
								<tr>
									<td>
										<!-- <s:hidden theme="simple" name="editBundleId"
										value="%{bundle.bundleId}"></s:hidden> -->
					<!-- 	<jsp:include
											page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneBundleEdit.jsp"></jsp:include>
									</td>
								</tr>
							</table>
						</s:if> -->
					<!-- <tr style="background-color: #E2E9F0; height: 25px;">
							<th align="left" style="padding-left: 10px;" width="100%"
								colspan="4">
								Work Information
							</th>
						</tr> -->
					<table width="942px">
						<tr style="background-color: #6F97BB; color: white;">
							<!-- <td valign="middle" style="width: 15px; text-align: center;">
								<a href="#"
									onclick="toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="bundle.bundleId"/>');return false;"><span
									style="margin-top: 5px;"> <img alt="Expand" border="0"
											src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
								</span> </a>
							</td> -->
							<td style="height: 20px; width: 942px;">
								<div
									style="display: inline; font-size: 12px; padding-left: 25px; font-weight: bold;">
									Work Information
								</div>
							</td>
						</tr>
					</table>
					<table width="942px" class="subTable" id="tblWorkInfo">
						<!-- <col width="100px" />
						<col width="300px" />
						<col width="100px" />
						<col width="200px" />
						 -->
						<tr>
							<td rowspan="6" style="background-color: #6F97BB; width: 15px;">
							</td>
							<td colspan="3">
							</td>
					<s:textfield label="TF Work ID" cssClass="odd" cssStyle="width: 80px;" id="TfToSearch">
					</s:textfield>
					<s:submit cssStyle="odd" value="Search for a Work"
						onclick="popUpSearchWorksPageContent(0,'TfToSearch');return false;"
						 />
					<s:submit  value="Clear"
						onclick="clearWorkInfo(); return false;" />							
						</tr>
						<tr>
							<td class="required-bold">
								Publication Title:
							</td>
							<td class="required" colspan="3">
							<s:textfield cssClass="ltype0" cssStyle="width: 530px; background-color: FFFF9C; color: blue;" theme="simple"
								name="newItem.publicationTitle" >
							</s:textfield>
							</td>
							<td class="odd-bold">
								WR Work ID:
							</td>
							<td class="odd">
								<s:textfield cssClass="ltype0" cssStyle="width: 120px;"
									name="newItem.wrWorkInst" theme="simple">
								</s:textfield>
							</td>
						</tr>
						<tr>
							<s:textfield cssClass="odd" cssStyle="width: 530px;"
								label="Article Title" name="newItem.articleTitle">
								<s:param name="inputcolspan" value="%{3}" />
							</s:textfield>
							<td class="odd-bold">
								TF Work ID:
							</td>
							<td class="odd">
								<s:textfield cssClass="ltype0" cssStyle="width: 120px;"
									name="newItem.workInst" theme="simple">
								</s:textfield>
							</td>
						</tr>
						<tr>
							<td class="odd-bold">
								IDNO Type:
							</td>
							<td class="odd">
								<s:textfield cssClass="ltype0" cssStyle="width: 200px;"
									name="newItem.idnoLabel" theme="simple">
								</s:textfield>
							</td>
							<td class="odd-bold">
								IDNO:
							</td>
							<td class="odd">
								<s:textfield cssClass="ltype0" cssStyle="width: 200px;"
									name="newItem.standardNumber" theme="simple">
								</s:textfield>
							</td>
							<!--<s:label label="RH Ref #" cssClass="odd" cssStyle="width: 120px;"
								value="%{TBD}">
							</s:label> -->
						</tr>
						<tr>
							<td class="odd-bold">
								Publisher:
							</td>
							<td class="odd" colspan="3">
								<s:textfield cssClass="ltype0" cssStyle="width: 480px;"
									name="newItem.publisher" theme="simple">
								</s:textfield>
							</td>
							<!-- <s:label label="RH Account #" cssClass="odd"
								cssStyle="width: 120px;" value="%{TBD}">
							</s:label> -->
						</tr>
						<tr>
							<s:textfield cssClass="odd" cssStyle="width: 480px;"
								label="Rightsholder" name="newItem.rightsholder">
								<s:param name="inputcolspan" value="%{3}" />
							</s:textfield>
							<!-- <s:label label="Right Inst" cssClass="odd"
								cssStyle="width: 120px;" value="%{TBD}">
							</s:label> -->
						</tr>
					</table>
		<!-- <tr style="background-color: #E2E9F0; height: 25px;">
							<th align="left" style="padding-left: 10px;" width="100%"
								colspan="6">
								Usage Information
							</th>
						</tr> -->
		<table width="942px">
			<tr style="background-color: #6F97BB; color: white;">
				<!-- <td valign="middle" style="width: 15px; text-align: center;">
								<a href="#"
									onclick="toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="bundle.bundleId"/>');return false;"><span
									style="margin-top: 5px;"> <img alt="Expand" border="0"
											src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
								</span> </a>
							</td> -->
				<td style="height: 20px; width: 942px;">
					<div
						style="display: inline; font-size: 12px; padding-left: 25px; font-weight: bold;">
						Usage Information
					</div>
				</td>
			</tr>

		</table>
		<table width="942px" class="subTable" id="tblUsageInfo">
			<!-- <col width="100px" />
						<col width="100px" />
						<col width="100px" />
						<col width="100px" />
						<col width="100px" />
						<col width="100px" />  -->
			<tr>
				<td rowspan="6" style="background-color: #6F97BB; width: 15px;">
				</td>
				<td colspan="6">
					&nbsp;
				</td>
				<s:submit value="Clear"
						onclick="clearUsageInfo(); return false;" />
			</tr>
			<s:if test="true">
			<!-- TRS  -->
			<tr>
				<s:textfield cssClass="odd" cssStyle="width: 680px;"
					label="Chapter/Article" name="newItem.chapterArticle">
					<s:param name="inputcolspan" value="%{5}" />
				</s:textfield>
			</tr>
			<tr>
				<s:textfield cssClass="required" cssStyle="width: 180px;" label="Author"
					name="newItem.author">
				</s:textfield>
				<s:textfield cssClass="odd" cssStyle="width: 80px;"
					label="Line Item Ref #" name="newItem.refNum">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="No. of Pages" name="newItem.numberOfPages">
				</s:textfield>
			</tr>
			<tr>
				<s:textfield cssClass="odd" cssStyle="width: 180px;" label="Editor"
					name="newItem.editor">
				</s:textfield>
				<s:textfield cssClass="odd" cssStyle="width: 80px;"
					label="Date of Issue" name="newItem.dateOfIssue">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="No. of Copies" name="newItem.numberOfCopies">
				</s:textfield>
			</tr>
			<tr>
				<s:textfield cssClass="odd" cssStyle="width: 180px;" label="Edition"
					name="newItem.edition">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;" label="Pub Date"
					name="newItem.publicationDate">
				</s:textfield>
			</tr>
			<tr>
				<s:label></s:label>
				<s:label></s:label>
				<s:textfield cssClass="odd" cssStyle="width: 80px;" label="Volume"
					name="newItem.volume">
				</s:textfield>
			</tr>
			</s:if>
			<s:if test="false">
			<!-- DPS -->
			<tr>
				<s:textfield cssClass="odd" cssStyle="width: 680px;"
					label="Chapter/Article" name="newItem.chapterArticle">
					<s:param name="inputcolspan" value="%{5}" />
				</s:textfield>
			</tr>
			<tr>
				<s:textfield cssClass="required" cssStyle="width: 180px;" label="Author"
					name="newItem.author">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="Date Used" name="newItem.dateUsed">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="Duration" name="newItem.duration">
				</s:textfield>
			</tr>
			<tr>
				<s:textfield cssClass="odd" cssStyle="width: 180px;" label="Line Item ref #"
					name="newItem.lineItemRefNum">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="Pub Date" name="newItem.publicationDate">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="No. of Recipients" name="newItem.numberOfRecipients">
				</s:textfield>
			</tr>
			<tr>
				<s:label></s:label>
				<s:label></s:label>
				<s:textfield cssClass="odd" cssStyle="width: 285px;" label="Web Address"
					name="newItem.webAddress">
					<s:param name="inputcolspan" value="%{3}" />
				</s:textfield>
			</tr>
			</s:if>
			<s:if test="false">
			<!-- APS/ECCS -->
			<tr>
				<s:textfield cssClass="odd" cssStyle="width: 680px;"
					label="Chapter/Article" name="newItem.chapterArticle">
					<s:param name="inputcolspan" value="%{5}" />
				</s:textfield>
			</tr>
			<tr>
				<s:textfield cssClass="required" cssStyle="width: 180px;" label="Author"
					name="newItem.author">
				</s:textfield>
				<s:textfield cssClass="odd" cssStyle="width: 80px;"
					label="Line Item Ref #" name="newItem.refNum">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="Page Range" name="newItem.pageRange">
				</s:textfield>
			</tr>
			<tr>
				<s:textfield cssClass="odd" cssStyle="width: 180px;" label="Editor"
					name="newItem.editor">
				</s:textfield>
				<s:textfield cssClass="odd" cssStyle="width: 80px;"
					label="Date of Issue" name="newItem.dateOfIssue">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="No. of Pages" name="newItem.numberOfPages">
				</s:textfield>
			</tr>
			<tr>
				<s:textfield cssClass="odd" cssStyle="width: 180px;" label="Edition"
					name="newItem.edition">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;" label="Pub Date"
					name="newItem.publicationDate">
				</s:textfield>
				<s:textfield cssClass="required" cssStyle="width: 80px;"
					label="No. of Copies" name="newItem.numberOfCopies">
				</s:textfield>
			</tr>
			<tr>
				<s:label></s:label>
				<s:label></s:label>
				<s:textfield cssClass="odd" cssStyle="width: 80px;" label="Volume"
					name="newItem.volume">
				</s:textfield>
				<s:label label="Est. No. of Students" cssClass="odd"
								cssStyle="width: 120px;" value="%{TBD}">
				</s:label>
			</tr>
			</s:if>
		</table>
		</div>
		</td>
		</tr>
		<s:if test="addedDetails.size()>0">
			<tr id="addedDetails">
				<td>
					<table width="942px">
						<tr style="background-color: #E2E9F0;">
							<td valign="middle" style="width: 15px; text-align: center;">
								<a href="#"
									onclick="toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="bundle.bundleId"/>');return false;"><span
									style="margin-top: 5px;"> <img alt="Expand" border="0"
											src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
								</span> </a>
							</td>
							<td style="height: 26px; width: 942px;">
								<div id="sectionHeader"
									style="display: inline; font-size: 14px;">
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
					<s:iterator value="addedDetails" status="iterStatus">
						<table style="width: 942px">
							<tr>
								<td style="background-color: #CCCCCC;">

									<jsp:include
										page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailShort.jsp" />
								</td>
							</tr>
						</table>
					</s:iterator>
				</td>
			</tr>
		</s:if>
	</table>


	<!-- <div style="width: 15%; float: right; padding: 0px; margin: 0px;">
					<s:submit theme="simple" value="Add"
						onclick="addDetailClient(); return false;" />
					<s:submit theme="simple" value="Clear All" />
					<s:submit theme="simple" value="Exit"
						onclick="exitAddDetail(); return false;" />
				</div>  
				
				<div id="clearer"></div>
				
				-->
	<!-- <td style="background-color: grey" align="left" height="25px">
								<b><a id="hideDetails_href" href="#"
									onclick="orderDetailsShowHide(this); return false;">Hide Order Details</a>
								</b>
							</td> -->

	<div id="clearer"></div>




	<s:url escapeAmp="false" action="viewOrderHistory"
			namespace="/om/history" id="lnkConfNum">
			<s:param name="selectedConfirmNumber"
				value="%{selectedConfirmNumber}" />
			<s:param name="selectedBundleNumber" value="%{selectedBundleNumber}" />
			<s:param name="selectedDetailNumber" value="%{selectedDetailNumber}" />
			<s:param name="selectedItem"
				value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@ORDER_HEADER" />
	</s:url>

	<a href="<s:property value="lnkConfNum" />" id="linkToExit"
		style="display: none;">dummy</a>
	<s:if test="showDbTimings">
		<div>
			<s:property value="searchTime" />
		</div>
	</s:if>
</s:form>

<s:component template='confirmdialog.ftl'>
	<s:param name="dialogTitle" value="' '" />
	<s:param name="cancelButton" value="'No'" />
	<s:param name="goButton" value="'Yes'" />
	<s:param name="goScript" value="'confirmDialogGo'" />
</s:component>

<s:component template='popupSearchWorks.ftl'>
	<s:param name="searchFormId" value="'searchWorks'" />
	<s:param name="searchTitle" value="'Select Work'" />
	<s:param name="searchMsgsId" value="'searchWorksProcessMessagesView'" />
	<s:param name="searchObject" value="'searchCriteria'" />
	<s:param name="includeSelectButton" value="'true'" />
	<s:param name="workDataObject" value="'newItem'" />
	<s:param name="workSetField" value="'value'" />
	<s:param name="workFormPrefix" value="'addDetail'" />
	<s:param name="workFieldClass" value="'ltype'" />
	<s:param name="workSetFieldCallback" value="'enableWorkInfo(false)'"/>
	
</s:component> 

<script type="text/javascript">
	var _TGL_;

	function confirmDialogGo() {
		if (_TGL_ == 'EX') {
			exitNoPrompt();
		}
	}

	$(document).ready(function(){
		//alert("in page loaded");
		enableWorkInfo(false);
		enableUsageInfo(false);
	});
	
	function orderDetailsShowHide(ctrl) {

		if (ctrl.innerHTML == 'Hide Order Details') {
			alert("will hide");
			document.getElementById('orderDetails').style.display = 'none';
			ctrl.innerHTML = 'Show Order Details';
		} else if (ctrl.innerHTML == 'Show Order Details') {
			alert("will show");
			document.getElementById('orderDetails').style.display = '';
			ctrl.innerHTML = 'Hide Order Details';
		} else {
		}
	}

	function addDetailShowHide(ctrl) {
		if (ctrl.innerHTML == 'Hide Add a Detail') {
			document.getElementById('addDetail').style.display = 'none';
			ctrl.innerHTML = 'Show Add a Detail';
		} else if (ctrl.innerHTML == 'Show Add a Detail') {
			document.getElementById('addDetail').style.display = '';
			ctrl.innerHTML = 'Hide Add a Detail';
		} else {
		}
	}

	function newOrderShowHide(ctrl) {
		if (ctrl.innerHTML == 'Hide New Order') {
			document.getElementById('newOrderSection').style.display = 'none';
			ctrl.innerHTML = 'Show New Order';
		} else if (ctrl.innerHTML == 'Show New Order') {
			document.getElementById('newOrderSection').style.display = '';
			ctrl.innerHTML = 'Hide New Order';
		} else {
		}
	}

	function openManuallyAddDetail() {
		//alert('opening');
		document.getElementById('addDetailManually').style.display = '';
		$("table[id$='editheader']").show();
		//$('#_editheader').show();
		//$("table[id^='_editheader']").show();
	}

	function disableSelectingDetailType() {
		alert('disabling');
		//$("element[id='btnAddWork']").disable();
	}

	function exitAddDetail() {
		if (showExitPrompt()) {
			//alert("add detail is visible!");
			_TGL_ = 'EX';
			//showMessageDialog("Will abandon unsaved new detail?");
			showConfirmDialog("Discard detail information in the template and return to the confirmation page?");
			return;
		} else
			exitNoPrompt();
	}

	function showExitPrompt() {
		var cnt = 0;
		$('#tblWorkInfo input:text').each(function(i){
			  if ($(this).val() != '') {
	          	if (jQuery.trim($(this).val())!='') {
		        	  //alert("Non-empty value: " + this.value);
		          	cnt = cnt +1;
	          	}
			  }
		});
		//alert("cnt = "+cnt);
		if (cnt > 0) {
	          return true;
		} else {
			return false;
		}
	}

	function exitNoPrompt() {
		window.location = $('#linkToExit').attr('href');
	}

	function touSelected(ddl) {
		//alert("tou selected: " + ddl.selectedIndex);
		if (ddl.selectedIndex > 0) {
			//alert("will enable both");
			enableWorkInfo(true);
			enableUsageInfo(true);
		}		
		else {
			enableWorkInfo(false);
			enableUsageInfo(false);
		}
	}

	function productSelected(ddl) {
		//alert("product selected");
		//var touDdl= document.getElementById("selectTouAPS");
		var w = ddl.selectedIndex;
		var selected_text = ddl.options[w].text;
		hideTouDropdowns();
		//alert("Selected value is " + selected_text);
		if (selected_text == "TRS")
			$("#selectTouTRS").show();
		else if (selected_text == "DPS")
			$("#selectTouDPS").show();
		else if (selected_text == "APS")
			$("#selectTouAPS").show();
		else if (selected_text == "ECCS")
			$("#selectTouECCS").show();
		else if (selected_text == "RLS")
			$("#selectTouRLS").show();
		else if (selected_text == "RL")
			$("#selectTouRL").show();
		else if (selected_text == "RLR")
			$("#selectTouRLR").show();
	}

	function hideTouDropdowns() {
		//alert("hide tou's");
		//$("input:select[id*='selectTou']").attr("display", "none");
		$("#selectTouRL").hide();
		$("#selectTouRLS").hide();
		$("#selectTouRLR").hide();
		$("#selectTouTRS").hide();
		$("#selectTouDPS").hide();
		$("#selectTouAPS").hide();
		$("#selectTouECCS").hide();
	}


	function clearWorkInfo() {
		$('#tblWorkInfo input:text').val("");
		enableWorkInfo(true);
	}

	function enableWorkInfo(bool) {
		if (bool) {
			//alert("will enable"); 
			$('#tblWorkInfo input:text').attr("disabled","");
			//in either case disable TF and WR work ID's
			//$('element[id$=orkInst]').attr("disabled","disabled");
			$("[id$='orkInst']").attr("disabled","disabled");
		}
		else {
			//alert("will disable")
			$('#tblWorkInfo input:text').attr("disabled","disabled");
		}
		
		
	}

	function enableUsageInfo(bool) {
		if (bool) {
			//alert("will enable");
			$('#tblUsageInfo input:text').attr("disabled","");
		}
		else {
			//alert("will disable")
			$('#tblUsageInfo input:text').attr("disabled","disabled");
		}
	}

	function clearUsageInfo() {
		//alert("will clear usage info");
		//$("#tblUsageInfo tbody tr td input:textbox").value ='';
		$('#tblUsageInfo input:text').val("");
	}

	function addDetailClient() {
		if (showExitPrompt()) {
			//alert("will add detail!");
			$('#addDetail_0_addingDetail').val(true);
			$('#addDetail_0').submit();
		} else {
			showMessageDialog("Nothing to save.");
		}
	}

	function showMessageDialog(message) {
		$('#messageDialogDiv_Content').html(message);
		$('#messageDialog_Div').dialog('open');
		return false;
	}

	function showConfirmDialog(message) {
		$('#confirmDialogDiv_Content').html(message);
		$('#confirmDialog_Div').dialog('open');
		return false;
	}
</script>