<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<s:form action="newOrder" namespace="/om/neworder" method="post">

	<!--  <s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"/>
    <s:hidden theme="simple" name="selectedBundleNumber" value="%{searchBundleNumber}"/>
    <s:hidden theme="simple" name="selectedDetailNumber" value="%{searchDetailNumber}"/> -->

	<br />
	<table width="100%">

		<tr>
			<td>
				<div id="order_header"
					style="width: 972px; border: 0px solid #000000; margin-top: 5px; margin-left: 10px; margin-right: 10px; margin-bottom: 0px; padding: 0px;">
					<div style="width: 30%; float: left; padding: 0px; margin: 0px;">
						<h1
							style="display: inline; color: #6F97BB; padding: 0px; margin: 0px;">
							NEW ORDER: THIS PAGE IS UNDER CONSTRUCTION
						</h1>
						&nbsp;
						<!-- <span style="font-size: 18px; color: #6F97BB;" id="sectionHeaderInline">#&nbsp; <s:property
								value="editOrder.confirmationNumber" />
						</span> -->
					</div>
					<div style="width: 60%; float: right; padding: 0px; margin: 0px;">
						<jsp:include
							page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOrderTopMenu.jsp" />
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td style="background-color: grey" align="left" height="25px">
				<b><a id="hideAdd_href" href="#"
					onclick=
	newOrderShowHide(this);;
	returnfalse;;;
>Hide New Order</a> </b>
			</td>
		</tr>
		<tr id="newOrderSection">
			<td>
				NEW ORDER SECTION
			</td>

		</tr>
		<tr>
			<td style="background-color: grey" align="left" height="25px">
				<b><a id="hideAdd_href" href="#"
					onclick=
	addDetailShowHide(this);
	returnfalse;;;
>Hide Add a Detail</a> </b>
			</td>
		</tr>
		<tr id="addDetail">
			<td>
				<div style="width: 30%; float: left; padding: 0px; margin: 0px;">
					<h1
						style="display: inline; color: #6F97BB; padding: 0px; margin: 0px;">
						Add a Detail
					</h1>
				</div>
				<div style="width: 60%; float: right; padding: 0px; margin: 0px;">
					<s:submit theme="simple" value="Clear All" />
					<s:submit theme="simple" value="Exit" />
				</div>
				<table>
					<tr>
						<td width="486px">
							<table width="100%" class="subTable">
								<tr style="background-color: #E2E9F0; height: 25px;">
									<th colspan="2" align="left" style="padding-left: 10px;">
										Select Use
									</th>
								</tr>
								<tr>
									<s:select cssClass="odd" cssStyle="width: 140px;"
										list="productList" listKey="value" listValue="label"
										headerKey="" headerValue="-- Select One --" label="Product"
										name="searchCriteria.productTOU"></s:select>
								</tr>
								<tr>
									<s:select cssClass="odd" cssStyle="width: 140px;"
										list="touList" listKey="value" listValue="label" headerKey=""
										headerValue="-- Select One --" label="Type of Use"
										name="searchCriteria.productTOU"></s:select>
								</tr>
							</table>
						</td>
						<td width="486px">
							<table width="100%">
								<tr style="background-color: #E2E9F0; height: 25px;">
									<th align="left" style="padding-left: 10px;" width="100%">
										Select Work
									</th>
								</tr>
								<tr>
									<td style="padding-left: 20px;">
										<s:submit theme="simple" value="Search in Works Repository" />
									</td>
								</tr>
								<tr>
									<td style="padding-left: 20px;">
										<s:submit theme="simple" value="Manually Enter the Work"
											onclick="openManuallyAddDetail();return false;" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table width="972px">
								<tr style="background-color: #E2E9F0; height: 25px;">
									<th align="left" style="padding-left: 10px;">
										Detail Information
									</th>
								</tr>
								<tr>
									<td>
										TO BE FILLED UP
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
				<div id="addDetailManually" style="display: none;">
					<s:if test="addingProject">
						<table>
							<tr>
								<td>
									<!-- <s:hidden theme="simple" name="editBundleId"
										value="%{bundle.bundleId}"></s:hidden> -->
									<jsp:include
										page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneBundleEdit.jsp"></jsp:include>
								</td>
							</tr>
						</table>
					</s:if>
					<s:if test="addingDetail">
						<table width="100%" class="subTable">
							<col width="100px" />
							<col width="300px" />
							<col width="100px" />
							<col width="200px" />
							<tr style="background-color: #E2E9F0; height: 25px;">
								<th align="left" style="padding-left: 10px;" width="100%"
									colspan="4">
									Work Information
								</th>
							</tr>
							<tr>
								<s:textfield cssClass="odd" cssStyle="width: 680px;"
									label="Title" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
									<s:param name="inputcolspan" value="%{3}" />
								</s:textfield>
							</tr>
							<tr>
								<s:textfield cssClass="odd" cssStyle="width: 80px;" label="IDNO"
									name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="Editor" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
							</tr>
							<tr>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="Pub Date" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="Volume" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
							</tr>
							<tr>
								<s:textfield cssClass="odd" cssStyle="width: 480px;"
									label="Publisher" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="Edition" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
							</tr>
							<tr>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="Author" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
							</tr>
						</table>
						<table width="100%" class="subTable">
							<col width="100px" />
							<col width="100px" />
							<col width="100px" />
							<col width="100px" />
							<col width="100px" />
							<col width="100px" />
							<tr style="background-color: #E2E9F0; height: 25px;">
								<th align="left" style="padding-left: 10px;" width="100%"
									colspan="6">
									Usage Information
								</th>
							</tr>
							<tr>
								<s:textfield cssClass="odd" cssStyle="width: 680px;"
									label="Chapter/Article" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
									<s:param name="inputcolspan" value="%{5}" />
								</s:textfield>
							</tr>
							<tr>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="Page Range" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="No. of Copies" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="NLine Item Ref #" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
							</tr>
							<tr>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="No. of Pages" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="Est. no of Students" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
								<s:textfield cssClass="odd" cssStyle="width: 80px;"
									label="Date of Issue" name="editBundle.numberOfStudents"
									value="%{bundle.numberOfStudents}">
								</s:textfield>
							</tr>
						</table>
					</s:if>
			</td>

		</tr>
		<s:if test="addedDetails.size()>0">
			<tr>

				<td style="background-color: grey" align="left" height="25px">
					<b><a id="hideDetails_href" href="#"
						onclick=
	orderDetailsShowHide(this);
	returnfalse;;;
>Hide Order
						Details</a> </b>
				</td>
			</tr>
			<tr id="orderDetails">
				<td>
					<s:iterator value="addedDetails" status="iterStatus">
						<table style="width: 100%">
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
	<div id="clearer"></div>

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
<script type="text/javascript">
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
		document.getElementById('addDetailManually').style.display = '';
		$("table[id$='editheader']").show();
		//$('#_editheader').show();
		//$("table[id^='_editheader']").show();
	}

	/*
	 var _LASTORDERDTL = '';
	 var _LASTBUNDLEDTL = '';
	 var _BUNDLEALL = false;
	 var _ORDERALL = false;
	 var _OOBJ_;
	 var _OORD_;
	 var _OBDL_;
	 var _ODET_;
	 var _TGL_;
	 var DETAILCOUNT=0;

	 $(document).ready( function() {

	 $("#viewOrderHistory_editBundle_startOfTerm").datepicker( {
	 showAnim : 'fadeIn'
	 });
	
	 //showSelectedSection();

	 displayProductDetailsForExistingProductCodes();

	 });

	 function confirmDialogGo() {
	 if ( _TGL_ == 'B' ) {
	 toggleBundleDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
	 } else {
	 if ( _TGL_ == 'O' ) {
	 toggleOrderDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
	 }
	 }
	 }
	 function showSelectedSection() {
	 <s:if test="expandOrderHeader">
	 <s:if test="selectedBundleNumber > 0" >
	 toggleCourseHeaderSection('<s:property value="selectedBundleNumber"/>');
	 var hid = 'course_' + <s:property value="selectedBundleNumber"/> + '_show_href_id';
	 //alert('hid='+hid);
	 var hrefObj3 = document.getElementById(hid);
	 toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>');
	 </s:if>
	 <s:else>
	 toggleDetailHeaderSection();
	 </s:else>
	 </s:if>
	 <s:if test="expandFirstBundleDetail">
	 //alert('expandFirstBundleDetail');
	 var hrefObj1 = document.getElementById('order_show_href_id');
	 toggleCourseHeaderSection('<s:property value="selectedBundleNumber"/>');
	 var hid = 'course_' + <s:property value="selectedBundleNumber"/> + '_show_href_id';
	 var hrefObj3 = document.getElementById(hid);
	 toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>');
	 var toHref = "#SEL" + <s:property value="selectedDetailNumber"/>;
	 var fHrefId = 'ORG' + '<s:property value="selectedDetailNumber"/>';
	 var fromHref = document.getElementById(fHrefId);
	 toggleDetail(fromHref,'<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>', '<s:property value="selectedDetailNumber"/>');
	
	 </s:if>    
	 <s:if test="expandFirstOrderDetail">
	 //alert('expandFirstOrderDetail');
	 //var hrefObj2 = document.getElementById('order_show_href_id');
	 //toggleConfirmation(hrefObj2);
	 toggleDetailHeaderSection();
	 <s:if test="pagingDetails">
	 toggleSortDefaultAndRadioButtons('viewOrderHistory','<s:property value="@com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum@SORT_BY_VIEW_DEFAULT.name()"/>');
	 </s:if>
	 var toHref = "#SEL" + <s:property value="selectedDetailNumber"/>;
	 var fHrefId = 'ORG' + '<s:property value="selectedDetailNumber"/>';
	 var fromHref = document.getElementById(fHrefId);
	 toggleDetail(fromHref,'<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>', '<s:property value="selectedDetailNumber"/>');
	 </s:if> 
	 <s:else>    
	 <s:if test="pagingDetails">
	 toggleSortDefaultAndRadioButtons('viewOrderHistory','<s:property value="@com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum@SORT_BY_VIEW_DEFAULT.name()"/>');
	 toggleDetailHeaderSection();
	 </s:if> 
	 </s:else>   
	 }
	 */
	/*
	 function toggleSearch() {
	 var av = document.getElementById('advancedSearch');
	 //alert ('av id='+av.id);

	 $('#advancedSearch').toggle();
	 //alert($('#advancedSearch_href').attr('innerHTML'));
	 if ($('#advancedSearch_href').attr('innerHTML').indexOf('Hide') == -1) {
	 $('#advancedSearch_href').attr('innerHTML',
	 'Hide Advanced Search...');
	 } else {
	 $('#advancedSearch_href').attr('innerHTML', 'Advanced Search...');
	 }

	 }

	 function showConfirmationEdit(editing) {
	 if (EDITSECTION == '') {
	 $('#order_viewheader').hide();
	 $('#order_editheader').show();
	 $('#order_edit_href').hide();
	 $('#order_show_href').hide();
	 EDITSECTION = editing;
	 $("div[id^='pagingDetailsControls']").hide();
	 } else {
	 //alert('Already editing ' + EDITSECTION);
	 showMessageDialog('Already editing ' + EDITSECTION);
	 }
	 }

	 function showMessageDialog(message) {
	 $('#messageDialogDiv_Content').html(message);
	 $('#messageDialog_Div').dialog('open');
	 return false;	
	 }

	 function showNotImplemented(message) {
	 var msg = "";
	 if ( message != null ) {
	 msg = '<b>' + message + ' not implemented yet</b>';
	 } else {
	 msg ='<b>Action not implemented yet</b>';
	 }
	 showMessageDialog(msg);
	 }	
	
	 function cancelEditHeader() {
	 $('#order_viewheader').show();
	 $('#order_editheader').hide();
	 $('#order_edit_href').show();
	 $('#order_show_href').show();
	 $("div[id^='pagingDetailsControls']").show();
	 EDITSECTION = '';
	 }
	 function toggleBundle(course, obj) {
	 var showTable = '#order_course_' + course + '_bundlecontent';
	 var editdref = '#course_' + course + '_edit_href';
	 var objText = obj.innerHTML;
	 if (objText.indexOf('Hide') != -1) {
	 obj.innerHTML = objText.replace('Hide','Show');
	 $(editdref).hide()
	 } else {
	 obj.innerHTML = objText.replace('Show','Hide');
	 $(editdref).show()
	 }
	 $(showTable).toggle();
	 }
	 function toggleConfirmation(obj) {
	 var showTable = '#order_confirmcontent';
	 var editdref = '#order_edit_href';
	 var objText = obj.innerHTML;
	 if (objText.indexOf('Hide') != -1) {
	 obj.innerHTML = objText.replace('Hide','Show');
	 $(editdref).hide();
	 } else {
	 obj.innerHTML = objText.replace('Show','Hide');
	 $(editdref).show();
	 }
	 $(showTable).toggle();
	 }
	 function showBundle(course) {
	 var showTable = '#order_course_' + course + '_bundlecontent';
	 $(showTable).show();
	 }
	 function hideBundle(course) {
	 var showTable = '#order_course_' + course + '_bundlecontent';
	 $(showTable).hide();
	 }
	 function showCourseEdit(course, editing) {
	 if (EDITSECTION == '') {
	 var showTable = '#order_course_' + course + '_viewheader';
	 var editTable = '#order_course_' + course + '_editheader';
	 var hidehref = '#course_' + course + '_edit_href';
	 var showhref = '#course_' + course + '_show_href';
	 $(showTable).hide();
	 $(editTable).show();
	 $(hidehref).hide();
	 $(showhref).hide();
	 EDITSECTION = editing;
	 $("div[id^='pagingDetailsControls']").hide();
	 } else {
	 //alert('Already editing ' + EDITSECTION);
	 showMessageDialog('Already editing ' + EDITSECTION);
	 }
	 }
	 function cancelCourseEditHeader(course) {
	 var showTable = '#order_course_' + course + '_viewheader';
	 var editTable = '#order_course_' + course + '_editheader';
	 var hidehref = '#course_' + course + '_edit_href';
	 var showhref = '#course_' + course + '_show_href';
	 $(showTable).show();
	 $(editTable).hide();
	 $(hidehref).show();
	 $(showhref).show();
	 EDITSECTION = '';
	 $("div[id^='pagingDetailsControls']").show();
	 }
	
	 function toggleHeaderSection() {
	 $('#order_expandheader').toggle();
	 $('#order_collapseheader').toggle();
	 }
	
	 function toggleCourseHeaderSection(course) {
	 var exp = '#order_course_'+course+'_expandheader';
	 var clp = '#order_course_'+course+'_collapseheader';
	 $(exp).toggle();
	 $(clp).toggle();
	 }
	 function toggleDetailHeaderSection() {
	 var exp = '#order_details_expandheader';
	 var clp = '#order_details_collapseheader';
	 var opg = '#order_details_pagedresults';
	 if ( $(opg).html() == '' ) {
	 loadAjaxOrderDetailPage(opg);
	 } else {
	 $(exp).toggle();
	 $(clp).toggle();
	 }
	 }
	 function toggleCourseItemsSection(order,course) {
	 var exp = '#order_'+order+'_course_'+course+'_details_expandheader';
	 var clp = '#order_'+order+'_course_'+course+'_details_collapseheader';
	 $(exp).toggle();
	 $(clp).toggle();
	 }
	 function toggleSection(order, section) {
	 var sectionExpand = '#order_' + order + '_expand' + section;
	 var sectionCollapse = '#order_' + order + '_collapse' + section;
	 $(sectionExpand).toggle();
	 $(sectionCollapse).toggle();
	 var sectionId = '#order_' + order + '_' + section;
	 var editHref = '#order_' + order + '_' + section + '_href';
	 $(editHref).toggle();
	 $(sectionId).toggle();
	 }
	 function toggleCourseSection(course, section) {
	 var sectionExpand = '#order_course_' + course + '_expand'
	 + section;
	 var sectionCollapse = '#order_course_' + course
	 + '_collapse' + section;
	 $(sectionExpand).toggle();
	 $(sectionCollapse).toggle();
	 var sectionId = '#order_course_' + course + '_' + section;
	 var editHref = '#order_course_' + course + '_' + section
	 + '_href';
	 $(sectionId).toggle();
	 $(editHref).toggle();
	 }
	 function toggleCourse(order, course, section) {
	 var sectionExpand = '#order_' + order + '_course_' + course + '_expand';
	 var sectionCollapse = '#order_' + order + '_course_' + course
	 + '_collapse';
	 var courseDetails = '#order_' + order + '_course_' + course
	 + '_details';
	 $(sectionExpand).toggle();
	 $(sectionCollapse).toggle();
	 $(courseDetails).toggle();
	 }
	
	 function toggleDetail(obj, order, course, detail) {
	 //alert('obj id='+obj.id);
	 <s:if test="pagingDetails">
	 toggleOrderDetail(obj, order, course, detail);
	 </s:if>
	 <s:else>
	 if ( course > 0 ) {
	 toggleBundleDetail(obj, order, course, detail);
	 } else {
	 toggleOrderDetail(obj, order, course, detail);
	 }
	 </s:else>
	 }
	
	 function toggleBundleScript(obj, order, course, detail) {
	 toggleDetail(obj, order, course, detail);
	 }

	 function toggleOrderScript(obj, order, course, detail) {
	 toggleDetail(obj, order, course, detail);
	 }
	
	 function loadAjaxOrderDetailPage( htmlDiv ) {
	 var order = <s:property value="selectedConfirmNumber"/>;
	 var bundle = '-1';
	 <s:url id="viewPagedDetailURL" namespace="/om/history"  value="viewPagedOrderDetails.action" includeParams="none"/>
	 var viewPagedDetailURL =	'<s:property value="#viewPagedDetailURL"/>';
	 var showOrderPagingLoading = '#showOrderPagingLoading';
	 if ( $(htmlDiv).html()== '' ) {
	 $(showOrderPagingLoading).show();
	 $.ajax({
	 type: "POST",
	 url: viewPagedDetailURL,
	 data: {selectedConfirmNumber:order,selectedBundleNumber:bundle},
	 success: function(data){
	 $(htmlDiv).html(data);
	 $(showOrderPagingLoading).hide();
	 toggleDetailHeaderSection();
	 }
	 });
	 } 
	 }
	
	 function toggleAjaxOrderDetail(obj, order, course, detail) {
	 var sectionEdit = '#editdetail_order_' + course + '_order_' + order + '_detail_' + detail;
	 <s:url id="viewExpandedDetailURL" namespace="/om/history"  value="viewExpandedDetail.action" includeParams="none"/>
	 var viewExpandedDetailUrl =	'<s:property value="#viewExpandedDetailURL"/>';
	 var showExpandingLoading = '#showOrderDetailExpanding_'+order+ '_detail_' + detail;
	 if ( $(sectionEdit).html()== '' ) {
	 $(showExpandingLoading).show();
	 $.ajax({
	 type: "POST",
	 url: viewExpandedDetailUrl,
	 data: {selectedConfirmNumber:order,selectedBundleNumber:course,selectedDetailNumber:detail},
	 success: function(data){
	 var divId='#editdetail_order_'+course+'_order_'+order+'_detail_'+detail;
	 $(divId).html(data);
	 $(showExpandingLoading).hide();
	 toggleDetail(obj, order, course, detail);
	 }
	 });
	 } 
	 }
	
	 function toggleAjaxBundleDetail(obj, order, course, detail) {
	 var sectionEdit = '#editdetail_bundle_' + course + '_order_' + order + '_detail_' + detail;
	 <s:url id="viewExpandedDetailURL" namespace="/om/history"  value="viewExpandedDetail.action" includeParams="none"/>
	 var viewExpandedDetailUrl =	'<s:property value="#viewExpandedDetailURL"/>';
	 var showExpandingLoading = '#showBundleDetailExpanding_'+course + '_detail_' + detail;
	 if ( $(sectionEdit).html() == '' ) {
	 $(showExpandingLoading).show();
	 $.ajax({
	 type: "POST",
	 url: viewExpandedDetailUrl,
	 data: {selectedConfirmNumber:order,selectedBundleNumber:course,selectedDetailNumber:detail},
	 success: function(data){
	 var divId='#editdetail_bundle_'+course+'_order_'+order+'_detail_'+detail;
	 $(divId).html(data);
	 $(showExpandingLoading).hide();
	 toggleDetail(obj, order, course, detail);
	 }
	 });
	 } 
	 }
	
	 function showConfirmDialog(message) {
	 //alert('here message='+message);
	
	 $('#confirmDialogDiv_Content').html(message);
	 $('#confirmDialog_Div').dialog('open');
	 return false;	
	 }
	
	 function toggleBundleDetail(obj,order, course, detail) {
	 if ( _BUNDLEALL ) {
	 if ( obj == null ) {
	 var fromHref = "#ORG" + detail;
	 obj=$(fromHref);
	 } 
	 }
	 //if ( _LASTBUNDLEDTL != '') {
	 //	if ( _LASTBUNDLEDTL != detail ) {
	 //		toggleDetail(obj,order,course,_LASTBUNDLEDTL);
	 //	}
	 //}
	 var sectionShow = '#showdetail_bundle_' + course + '_order_' + order + '_detail_' + detail;
	 var sectionEdit = '#editdetail_bundle_' + course + '_order_' + order + '_detail_' + detail;
	 var obj = document.getElementById(sectionEdit.replace('#',''));
	 if ( $(sectionEdit).html() == '' ) {
	 toggleAjaxBundleDetail(obj, order, course, detail);
	 return;
	 } else {
	 $(sectionShow).toggle();
	 $(sectionEdit).toggle();
	 if ( obj.style.display != 'none' ) {
	 _LASTBUNDLEDTL=detail;
	 } else {
	 _LASTBUNDLEDTL='';
	 }
	 var toHref = "#SEL" + detail;
	 var fromHref = '#'+obj.id + detail;
	 if ( fromHref == toHref ) {
	 if ( $(fromHref).position().top > 100 ) {
	 window.scrollTo(0, $(fromHref).position().top - 5);
	 }
	 } else {
	 //alert('toHref='+toHref);
	 if ( $(toHref).position().top > 100 ) {
	 window.scrollTo(0, $(toHref).position().top - 5);
	 }
	 }
	 }
	 }
	
	 function toggleOrderDetail(obj,order, course, detail) {
	 if ( _ORDERALL ) {
	 if ( obj == null ) {
	 var fromHref = "#ORG" + detail;
	 obj=$(fromHref);
	 } 
	 }
	 //if ( _LASTORDERDTL != '') {
	 //	if ( _LASTORDERDTL != detail ) {
	 //		toggleDetail(obj,order,course,_LASTORDERDTL);
	 //	}
	 //}
	 var sectionShow = '#showdetail_order_' + course + '_order_' + order + '_detail_' + detail;
	 var sectionEdit = '#editdetail_order_' + course + '_order_' + order + '_detail_' + detail;
	 var obj = document.getElementById(sectionEdit.replace('#',''));
	 if ( $(sectionEdit).html() == '' ) {
	 toggleAjaxOrderDetail(obj, order, course, detail);
	 return;
	 } else {			
	 $(sectionShow).toggle();
	 $(sectionEdit).toggle();
	 if ( obj.style.display != 'none' ) {
	 _LASTORDERDTL=detail;
	 } else {
	 _LASTORDERDTL='';
	 }
	 var toHref = "#SEL" + detail;
	 var fromHref = '#'+obj.id + detail;
	 if ( fromHref == toHref ) {
	 if ( $(fromHref).position().top > 100 ) {
	 window.scrollTo(0, $(fromHref).position().top - 5);
	 }
	 } else {
	 //alert('toHref='+toHref);
	 if ( $(toHref).position().top > 100 ) {
	 window.scrollTo(0, $(toHref).position().top - 5);
	 }
	 }
	 }
	 }
	
	 function collapseAllBundleDetails(bundle) {
	 var edit = "div[id^='editdetail_bundle_"+bundle+"']";
	 var show = "div[id^='showdetail_bundle_"+bundle+"']";			
	 $(show).show();
	 $(edit).hide();
	 _LASTBUNDLEDTL='';
	 _BUNDLEALL = false;
	
	 }
	
	 function expandAllBundleDetails(bundle) {
	 var edit = "div[id^='editdetail_bundle_"+bundle+"']";
	 var show = "div[id^='showdetail_bundle_"+bundle+"']";			
	 $(show).hide();
	 $(edit).show();
	 _LASTBUNDLEDTL='';
	 _BUNDLEALL = true;
	 }
	 function collapseAllOrderDetails(bundle) {
	 var edit = "div[id^='editdetail_order_"+bundle+"']";
	 var show = "div[id^='showdetail_order_"+bundle+"']";			
	 $(show).show();
	 $(edit).hide();
	 _LASTORDERDTL='';
	 _ORDERALL = false;

	 }

	 function expandAllOrderDetails(bundle) {
	 var edit = "div[id^='editdetail_order_"+bundle+"']";
	 var show = "div[id^='showdetail_order_"+bundle+"']";			
	 $(show).hide();
	 $(edit).show();
	 _LASTORDERDTL='';
	 _ORDERALL = true;
	 }
	
	 function toggleSortDefaultAndRadioButtons(frm,def) {
	
	 var isDef = $('#sortBySelect').val();
	 if ( isDef == def ) {
	 $('#sortRadioButtons').hide();
	 $('#sortDefaultText').show();
	 } else {
	 $('#sortRadioButtons').show();
	 $('#sortDefaultText').hide();	     
	 }
	 $('#sortSubmitButton').hide();
	 if ($('#sortBySelect').val() !=  $('#currentSortCriteriaBy').val()) {
	 $('#sortSubmitButton').show()  
	 } else {
	 if ( isDef != def ) {
	 if ( $('#'+frm+'_searchCriteria_sortOrderAscending').attr('checked') ) {
	 if ( $('#'+frm+'_searchCriteria_sortOrderAscending').val() != $('#currentSortOrder').val() ) {
	 $('#sortSubmitButton').show() 
	 } 
	 } else {
	 if ( $('#'+frm+'_searchCriteria_sortOrderDescending').attr('checked') ) {
	 if ( $('#'+frm+'_searchCriteria_sortOrderDescending').val() != $('#currentSortOrder').val() ) {
	 $('#sortSubmitButton').show() 
	 } 			 	   
	 }
	 }
	 }				 
	 }
	 }
	 function doReSort() {
	 $('#viewOrderHistory').attr('action','viewOrderHistory!resortSearchResults.action');
	 $('#viewOrderHistory').submit();   
	 }

	 function cancelDetailEdit(order,bundle,detail) {
	 var showDetail = '#showing_detail_'+bundle+'_order_'+order+'_detail_'+detail;
	 var editDetail = '#editing_detail_'+bundle+'_order_'+order+'_detail_'+detail;
	 if ( jQuery(editDetail).not(':hidden') ) {
	 $(editDetail).hide();    
	 $(showDetail).show();           
	 jQuery('#editdetailhref_'+detail).removeClass('editingDetailAngleTabs');
	 setEditDetailAndCount(); 
	 }
	 }
	 function findRegexValue  ( regexString, searchString, ignoreCase, position ) {
	
	 //var FINDARGUMENTS = "\n";
	 var useMultiLineSearch="m";
	 var foundValue=null;
	 var regexInstance;
	 if ( ignoreCase == "i" ) { 
	 regexInstance = new RegExp(regexString, ignoreCase + useMultiLineSearch);
	 } else {
	 regexInstance = new RegExp(regexString, useMultiLineSearch);
	 }
	 //FINDARGUMENTS += "\nPattern=" + regexInstance.source;
	 //FINDARGUMENTS += "\nSearch=" + searchString;
	 //FINDARGUMENTS += "\nPosition=" + position;  
	 //FINDARGUMENTS += "\nIgnoreCase=" + regexInstance.ignoreCase;
	 //FINDARGUMENTS += ";MultiLine=" + regexInstance.multiline;
	
	 var matchAttempt = regexInstance.exec( searchString );
	
	 if ( matchAttempt != null ) {
	
	 foundValue = matchAttempt[position];
	
	 //FINDARGUMENTS += "\nFound=" + foundValue;
	
	 }
	 //alert(FINDARGUMENTS);
	 return foundValue;

	 }	
	 function editEntireOrder() {
	 var edit = "div[id^='editdetail_']";
	 var show = "div[id^='showdetail_']";			
	 var showDetail = "div[id^='showing_detail_']";
	 var editDetail = "div[id^='editing_detail_']";
	 $(show).hide();
	 $(edit).show();
	 $(showDetail).hide();
	 $(editDetail).show();
	 setEditDetailAndCount();
	 jQuery('a#entireorder').html('Cancel Edit Entire Order');
	 jQuery("span[id^='editdetailhref']").each(function(i){
	 $(this).addClass('editingDetailAngleTabs');
	 });
	 }
	 function cancelEntireOrder() {
	 var edit = "div[id^='editdetail_']";
	 var show = "div[id^='showdetail_']";			
	 var showDetail = "div[id^='showing_detail_']";
	 var editDetail = "div[id^='editing_detail_']";
	 $(show).show();
	 $(edit).hide();
	 $(showDetail).show();
	 $(editDetail).hide();
	 setEditDetailAndCount();
	 jQuery('a#entireorder').html('Edit Entire Order');
	 jQuery("a[span[id^='editdetailhref']").each(function(i){
	 $(this).removeClass('editingDetailAngleTabs');
	 });
	 }

	 function setEditDetailAndCount() {
	 var vDetails = [];
	 var editDetail = "div[id^='editing_detail_']";
	 jQuery(editDetail).not(':hidden').each(function(i){
	 var vid = $(this).attr('id');
	 var vlu = findRegexValue('.*_detail_([0-9]*$)',vid,'i',1);
	 vDetails[i]=vlu;
	 });
	 if ( vDetails.length == 0 ) {
	 EDITSECTION = '';
	 } else if ( vDetails.length == 1 ) {
	 EDITSECTION = 'Detail ' + vDetails[0];
	 } else {
	 EDITSECTION = 'Details ' + vDetails[0];
	 for (var x=1;x<vDetails.length;x++) {
	 EDITSECTION += ', ' + vDetails[x];
	 }
	 }
	 DETAILCOUNT = jQuery(editDetail).not(':hidden').length;
	 if ( DETAILCOUNT > 1 ) { 
	 $("div[id^='showDetailSaveAll_']").show();
	 $("div[id^='showDetailCancelAll_']").show();
	 } else {
	 $("div[id^='showDetailSaveAll_']").hide();
	 $("div[id^='showDetailCancelAll_']").hide();
	 }
	 if ( DETAILCOUNT > 0 ) {
	 $("div[id^='pagingDetailsControls']").hide();
	 } else {
	 $("div[id^='pagingDetailsControls']").show();
	 }    
	 }

	 function showProductDetails(obj,detail) {
	 if ( obj != null ) {
	 $("div[id^='productDetail_"+detail+"']").hide();
	 var oid = '#'+obj.id;
	 $('#productDetail_'+detail+'_'+obj.value+'_PCD').show();
	 }
	 }
	
	 function displayProductDetailsForExistingProductCodes() {
	 jQuery("div[id^='productDetail_']").hide();
	 jQuery("select[id^='editOrder_editItem_productCd']").each(function(i){		
	 if ( $(this).val() != '' ) {
	 var vid = $(this).attr('id');
	 var vlu = findRegexValue('editOrder_editItem_productCd_([0-9]*$)',vid,'i',1);
	 var pcd = '#productDetail_'+vlu+'_'+$(this).val()+'_PCD';
	 $(pcd).show();
	 }
	 });
	 }
	 */
</script>