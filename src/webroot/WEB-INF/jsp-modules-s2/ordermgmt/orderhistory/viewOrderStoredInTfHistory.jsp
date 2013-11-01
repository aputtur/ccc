<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div
	style="font-size: 10px;background-color: #E2E9F0; padding-top: 10px; padding-bottom: 10px; padding-left: 20px; padding-right: 20px;">
	&lt;&lt;&nbsp;
	<a href="#"
		onclick="submitTopMenuForm('<s:url value="searchOrder!returnToSearchResults.action" includeParams="none"/>','<s:property value="quickTabSelected"/>');return false;">back
		to search results</a>
</div>

<s:if test="!allProcessMessages.empty">
<table width="100%">
	<tr>
		<td style="width: 40px;"></td>
		<td align="center">
		    <fieldset style="width: 800px;">
		    <legend><span style="font-weight: bold;">Processing Messages</span></legend>
			<ul style="text-align: left;">
			<s:iterator value="allProcessMessages">
			  <s:if test="error">
			   <li style="color: red; font-weight: bold;">
			       <s:property value="formattedMessage"/>
			   </li>
			  </s:if>
			  <s:else>
			   <li style="color: blue; font-weight: bold;">
			       <s:property value="formattedMessage"/>
			   </li>
			   </s:else>
			</s:iterator>
		 	</ul>
		 	</fieldset>
		</td>
		<td style="width: 40px;"></td>
	</tr>
</table>
</s:if>

<s:form id="viewOrderTopMenu_form" theme="simple" action="viewOrderStoredInTfHistory" namespace="/om/history" >
   
	<s:hidden theme="simple" name="quickTabSelected"></s:hidden>
	<s:hidden theme="simple" name="selectedOrderId"></s:hidden>
    <s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"/>
    <s:hidden theme="simple" name="selectedBundleNumber" value="%{searchBundleNumber}"/>
    <s:hidden theme="simple" name="selectedDetailNumber" value="%{searchDetailNumber}"/>
	<s:hidden theme="simple" name="selectedItem"></s:hidden>
	<s:hidden theme="simple" name="includeCancelledOrders"></s:hidden>
	
	<input type="button" onclick="$('#viewOrderTopMenu_form').submit();" id="viewOrderTopMenu_form_submit" style="display: none;"/>
    
</s:form>

	<table width="100%">

		<tr>
			<td>
				<div id="order_header"
					style="width: 972px; border: 0px solid #000000; margin-top: 5px; margin-left: 10px; margin-right: 10px; margin-bottom: 0px; padding: 0px;">
					<div style="width: 30%; float: left; padding: 0px; margin: 0px;">
						<h1
							style="display: inline; color: #6F97BB; padding: 0px; margin: 0px;">
							Confirmation
						</h1>
						&nbsp;
						<span style="font-size: 18px; color: #6F97BB;" id="sectionHeaderInline">#&nbsp; <s:property
								value="selectedOrder.confirmation.confirmationNumber" />
						</span>
					</div>
					<div style="width: 60%; float: right; padding: 0px; margin: 0px;">
						<jsp:include
							page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOrderTopMenu.jsp" />
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div
					style="width: 972px; border: 0px solid #000000; margin-top: 0px; margin-left: 10px; margin-right: 10px; margin-bottom: 10px; padding: 0px;">
					<div id="order_collapseheader">			  
						<jsp:include
							page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/storedInTf/viewStoredInTfConfirmationHeader.jsp" />             
					</div>
					<div id="clearer"></div>

					<div>
						<table>
							<tr>
								<td align="left" style="width: 952px;">
									<s:iterator value="selectedOrder.myBundles" id="bundle" status="bStatus">
										<jsp:include
											page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/storedInTf/viewStoredInTfOneBundle.jsp" />
									</s:iterator>
								</td>
							</tr>
						</table>
						<div style="display: none;">
						<s:iterator value="selectedOrder.myBundles" id="bundle" status="bEditStatus">
						<s:form id="editOneBundle_%{bundle.bundleId}" action="viewOrderStoredInTfHistory!saveBundleHeader" namespace="/om/history" method="post">
    
		    				<s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"/>
		    				<s:hidden theme="simple" name="selectedBundleNumber" value="%{searchBundleNumber}"/>
		    				<s:hidden theme="simple" name="selectedDetailNumber" value="%{searchDetailNumber}"/>
							<s:hidden theme="simple" name="selectedBundleId" value="%{bundle.bundleId}"></s:hidden>
							<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editOneBundleFields.jsp" />
						</s:form>
						</s:iterator>
						</div>
						
					</div>

					<div id="clearer"></div>
					<s:if test="!selectedOrder.myItems.empty">
						<jsp:include
							page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/storedInTf/viewStoredInTfIndividualDetails.jsp" />
					</s:if>
				</div>
			</td>
		</tr>

	</table>

	<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/editSaveAllDetails.jsp" />

	<div id="clearer"></div>

	<s:if test="showDbTimings"><div><s:property value="searchTime" /></div></s:if>

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
</s:component>   

<script src="<s:url value="/resources/ordermgmt/scripts/viewOrderHistory.js" includeParams="none"/>" type="text/javascript"></script> 
	
<script type="text/javascript">

DETAILCOUNT=0;
_BASEFORM_='viewOrderStoredInTfHistory';
_PAGINGDT_=<s:property value="pagingIndividualDetails"/>;

	$(document).ready( function() {
        var d1 = '#'+_BASEFORM_+'_editBundle_startOfTerm';
		$(d1).datepicker( {
			showAnim : 'fadeIn'
		});
		
		showSelectedSection();

		addChangeResetEventsToEditForms();

	});
	
	function toggleDetail(obj, order, course, detail) {
		toggleOrderDetail(obj, order, course, detail);
	}
	
    function showSelectedSection() {
        // expandOrderHeader=<s:property value="expandOrderHeader"/>
        // expandFirstOrderDetail=<s:property value="expandFirstOrderDetail"/>
        // expandFirstBundleDetail=<s:property value="expandFirstBundleDetail"/>
        // pagingIndividualDetails=<s:property value="pagingIndividualDetails"/>
        // pagingBundleDetails=<s:property value="pagingBundleDetails"/>
        // pagingDetails=<s:property value="pagingDetails"/>
      var vScrollAgain=false;
    <s:if test="expandOrderHeader">
      <s:if test="selectedBundleNumber > 0" >
		  	toggleCourseHeaderSection('<s:property value="selectedBundleNumber"/>');
			toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>');
		 	<s:if test="!pagingDetails">
				toggleDetailHeaderSection();
		   </s:if>
     </s:if>
     <s:else>
 	   <s:if test="!pagingDetails">
			toggleDetailHeaderSection();
	   </s:if>
     </s:else>
    </s:if>
    <s:if test="expandFirstBundleDetail || expandFirstOrderDetail"> 
	    <s:if test="expandFirstBundleDetail">
	        var hrefObj1 = document.getElementById('order_show_href_id');
	    	toggleCourseHeaderSection('<s:property value="selectedBundleNumber"/>');
		    <s:if test="pagingIndividualDetails">
	    	//toggleSortDefaultAndRadioButtons(_BASEFORM_,'<s:property value="@com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum@SORT_BY_VIEW_DEFAULT.name()"/>');
			</s:if>
			toggleDetailHeaderSection();
			//toggleCourseItemsSection('<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>');
			var toHref = "#SEL" + <s:property value="selectedDetailNumber"/>;
			var fHrefId = 'ORG' + '<s:property value="selectedDetailNumber"/>';
			var fromHref1 = document.getElementById(fHrefId);
			toggleDetail(fromHref1,'<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>', '<s:property value="selectedDetailNumber"/>');
		</s:if>    
	    <s:if test="expandFirstOrderDetail">
	 		toggleDetailHeaderSection();
		    <s:if test="pagingIndividualDetails">
	    	//toggleSortDefaultAndRadioButtons(_BASEFORM_,'<s:property value="@com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum@SORT_BY_VIEW_DEFAULT.name()"/>');
			</s:if>
			var toHref = "#SEL" + <s:property value="selectedDetailNumber"/>;
			var fHrefId = 'ORG' + '<s:property value="selectedDetailNumber"/>';
			var fromHref2 = document.getElementById(fHrefId);
			toggleDetail(fromHref2,'<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>', '<s:property value="selectedDetailNumber"/>');
		</s:if> 
	</s:if>
	<s:else>    
    	<s:if test="pagingIndividualDetails">
    		//toggleSortDefaultAndRadioButtons(_BASEFORM_,'<s:property value="@com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum@SORT_BY_VIEW_DEFAULT.name()"/>');
			toggleDetailHeaderSection();
		</s:if> 
    	<s:if test="pagingBundleDetails">
			//toggleSortDefaultAndRadioButtons(_BASEFORM_,'<s:property value="@com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum@SORT_BY_VIEW_DEFAULT.name()"/>');
			toggleDetailHeaderSection();
	    </s:if> 
	</s:else>   
	<s:if test="pagingIndifivualDetails || pagingBundleDetails">
		toggleSortDefaultAndRadioButtons(_BASEFORM_,'<s:property value="@com.copyright.ccc.business.data.OrderSearchCriteriaSortColumnEnum@SORT_BY_VIEW_DEFAULT.name()"/>');
	</s:if>		
	<s:if test="expandFirstBundleDetail || expandFirstOrderDetail">
        var vBundle='<s:property value="selectedBundleNumber"/>';
        var vConfirm='<s:property value="selectedConfirmNumber"/>';
        var vDetail='<s:property value="selectedDetailNumber"/>';
        var vScrollHref;
        // expand details to edit mode with either errors or in the middle of edit
		$("input[id^='hasLastEdit']").each(function(i){
	          var vid = $(this).attr('id');
	          var vDT = findRegexValue('hasLastEdit([0-9]*$)',vid,'i',1);
	          var vil = '#hasLastEdit'+vDT;
	          if ( $(vil).val() == 'true' ) {
		        var showDetail = '#showing_detail_'+vBundle+'_order_'+vConfirm+'_detail_'+vDT;
		        var editDetail = '#editing_detail_'+vBundle+'_order_'+vConfirm+'_detail_'+vDT;
		        if ( jQuery(showDetail).is(':hidden') ) {
		      		var fHrefId = 'ORG' + vDT;
		    		var fromHref3 = document.getElementById(fHrefId);
		      		toggleDetail(fromHref3,vConfirm,vBundle, vDT);
		      		if ( vDetail == vDT ) { 
			      		vScrollAgain=true;
			      		vScrollHref=fromHref3; 
		      		}
		        }
		        if ( jQuery(editDetail).is(':hidden') ) {
        			$(showDetail).hide();
        			$(editDetail).show();
	    			jQuery('#editdetailhref_'+vDT).removeClass('editDetailAngleTabs');
        			jQuery('#editdetailhref_'+vDT).addClass('editingDetailAngleTabs');
		        }   
	          }
		});
		// expand details with completion messages
		$("input[id^='hasProcessMessages']").each(function(i){
	          var vid = $(this).attr('id');
	          var vDT = findRegexValue('hasProcessMessages([0-9]*$)',vid,'i',1);
	          var vil = '#hasProcessMessages'+vDT;
	          if ( $(vil).val() == 'true' ) {
		        var showDetail = '#showing_detail_'+vBundle+'_order_'+vConfirm+'_detail_'+vDT;
		        var editDetail = '#editing_detail_'+vBundle+'_order_'+vConfirm+'_detail_'+vDT;
		        if ( jQuery(showDetail).is(':hidden') && jQuery(editDetail).is(':hidden') ) {
		      		var fHrefId = 'ORG' + vDT;
		    		var fromHref4 = document.getElementById(fHrefId);
		      		toggleDetail(fromHref4,vConfirm,vBundle, vDT);
		      		if ( vDetail == vDT ) { 
			      		vScrollAgain=true;
			      		vScrollHref=fromHref4; 
		      		}	
		        }
	          }
		});
		// scroll again to get selected to top
		if ( vScrollAgain ) {
			toggleDetail(vScrollHref,vConfirm,vBundle, vDetail);
			toggleDetail(vScrollHref,vConfirm,vBundle, vDetail);
		}	
	</s:if>
   		setEditDetailAndCount();
		   
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

</script>