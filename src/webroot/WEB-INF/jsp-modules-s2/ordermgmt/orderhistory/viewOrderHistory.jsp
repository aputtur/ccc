<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<div
	style="font-size: 10px;background-color: #E2E9F0; padding-top: 10px; padding-bottom: 10px; padding-left: 20px; padding-right: 20px;">
	&lt;&lt;&nbsp;
	<s:if test="hideBackToSearchLink">
		<a href="#"
		onclick="window.close();">close</a>
	</s:if>
	<s:else>
		<a href="#"
		onclick="submitTopMenuForm('<s:url value="searchOrder!returnToSearchResults.action" includeParams="none"/>','<s:property value="quickTabSelected"/>');return false;">back
		to search results</a>		
	</s:else>
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

<s:form id="viewOrderTopMenu_form" theme="simple" action="viewOrderHistory" namespace="/om/history" >
	<s:hidden theme="simple" name="quickTabSelected"></s:hidden>
	<s:hidden theme="simple" name="selectedOrderId"></s:hidden>
    <s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"/>
    <s:hidden theme="simple" name="selectedBundleNumber" value="%{searchBundleNumber}"/>
    <s:hidden theme="simple" name="selectedDetailNumber" value="%{searchDetailNumber}"/>
	<s:hidden theme="simple" name="selectedItem"></s:hidden>
	<s:hidden theme="simple" name="includeCancelledOrders"></s:hidden>
	
	<input type="button" onclick="$('#viewOrderTopMenu_form').submit();" id="viewOrderTopMenu_form_submit" style="display: none;"/>
    
</s:form>
<div>
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
							page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/storedIn/viewStoredInConfirmationHeader.jsp" />             
					</div>
					<div id="clearer"></div>

					<div style="margin-left: 10px;width: 972px;">
						<table width="100%">
							<tr>
								<td align="left">
									<s:iterator value="selectedOrder.myBundles" id="bundle" status="bStatus">
										<jsp:include
											page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/storedIn/viewStoredInOneBundle.jsp" />
									</s:iterator>
								</td>
							</tr>
						</table>
					</div>
					<s:if test="wrappedResultsArray[individualDetailsIndex].detailsList.size > 0">
					<div id="clearer"></div>
					<div style="width: 972px; border: 0px solid #000000; margin-top: 0px; margin-left: 10px; margin-right: 10px; margin-bottom: 10px; padding: 0px;">
							<div>
								<table width="100%">
									<tr>
									<td align="left">
									<jsp:include
										page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/storedIn/viewStoredInIndividualDetails.jsp" />
									</td>
									</tr>
								</table>
							</div>
					</div>
					</s:if>
			 </div>
			</td>
		</tr>

	</table>
</div>


	<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/storedIn/editStoredInSaveAllDetails.jsp" />

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
_BASEFORM_='viewOrderHistory';


	$(document).ready( function() {
        var d1 = '#'+_BASEFORM_+'_editBundle_startOfTermStr';
		$(d1).datepicker( {
			showAnim : 'fadeIn'
		});
		
		showSelectedHeaders();

		showSelectedDetails();
		
		addChangeResetEventsToEditForms();

	});
	
	function toggleDetail(obj, order, course, detail) {

		if ( course > 0 ) {
			toggleBundleDetail(obj, order, course, detail);
		} else {
			toggleOrderDetail(obj, order, -1, detail);
		}
	}
	function showSelectedHeaders() {
		var vBundleIndexCount = <s:property value="bundleDetailsIndexCount"/>;
		var savingBundle = '<s:property value="savingBundle"/>';
		for ( var vB=0;vB<vBundleIndexCount;vB++) {
			toggleBundleHeaderSection(vB);
			toggleCourseItemsSection(vB);
		}
		if (savingBundle != null && savingBundle != '') {
			showCourseEdit(savingBundle, '');
		}
				
		//showSelectedSection();
		toggleDetailHeaderSection()
	}
	
	function showSelectedDetails() {
	  var expanded = false;
	  var toHref = "#SEL" + <s:property value="selectedDetailNumber"/>;
	  var fHrefId = 'ORG' + '<s:property value="selectedDetailNumber"/>';
	  var fromHref = document.getElementById(fHrefId);
		
		if (fromHref == null)
		{
			setEditDetailAndCount();
			return;
		}

	    <s:if test="expandAnyBundleDetail">
	    if ( !expanded ) { 
			toggleDetail(fromHref,'<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>', '<s:property value="selectedDetailNumber"/>');
			expanded = true;
	    }
        </s:if>
        <s:if test="expandFirstOrderDetail">
        if ( !expanded ) {
			toggleDetail(fromHref,'<s:property value="selectedConfirmNumber"/>','<s:property value="selectedBundleNumber"/>', '<s:property value="selectedDetailNumber"/>');
			expanded = true;
        }
        </s:if>
      if ( expanded ) {
    	  showExpandDetailsInEditMode();
      }
	  setEditDetailAndCount();    
	}
	
    function showExpandDetailsInEditMode() {  
      var vScrollAgain=false;
      var vOrderIndex = <s:property value="individualDetailsIndex"/>;
      var vBundleIndexCount = <s:property value="bundleDetailsIndexCount"/>;
      var vSelectedBundleIndex = <s:property value="selectedBundleIndex"/>;
      var vBundle='<s:property value="selectedBundleNumber"/>';
      if (vBundle == null || vBundle == "") {
      		vBundle = "-1";
      }  
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
		        } else if (vBundle != '-1'){
		        	showDetail = '#showing_detail_'+'-1'+'_order_'+vConfirm+'_detail_'+vDT;
		        	editDetail = '#editing_detail_'+'-1'+'_order_'+vConfirm+'_detail_'+vDT;
		        	if ( jQuery(showDetail).is(':hidden') && jQuery(editDetail).is(':hidden') ) {
		      			var fHrefId = 'ORG' + vDT;
		    			var fromHref4 = document.getElementById(fHrefId);
		      			toggleDetail(fromHref4,vConfirm,'-1', vDT);
		      			if ( vDetail == vDT ) { 
			      			vScrollAgain=true;
			      			vScrollHref=fromHref4; 
		      			}	
		        	}
	          	}
	          }
		});
		// scroll again to get selected to top
		if ( vScrollAgain ) {
			toggleDetail(vScrollHref,vConfirm,vBundle, vDetail);
			toggleDetail(vScrollHref,vConfirm,vBundle, vDetail);
		}	  
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

	function priceOneDetail(detail, detailIndex) {
		   var vir = '#changeddetail'+detail;
		   var detInd = detailIndex;
		   var saveFormElement = document.getElementById("savedetailform_" + detail);
//		   saveFormElement.updatePricingOnly.value=true;
//		   var doc = document;
//		   saveFormElement.editItem.updatePricingOnly.value=true;

//		   var updatePricingOnly = document.getElementById("updatePricingOnly" + detail);
//		   document.getElementById("updatePricingOnly" + detail).value=true;
//		   var updatePricingOnly2 = document.getElementById("updatePricingOnly" + detail);
//		   var updatePricingOnly = document.getElementById("saveAllDetails_editDetails_0__updatePricingOnly");
//		   document.getElementById("saveAllDetails_editDetails_0__updatePricingOnly").value=true;
//		   var updatePricingOnly2 = document.getElementById("saveAllDetails_editDetails_0__updatePricingOnly");
		   var updatePricingOnly = document.getElementById("saveAllDetails_editDetails_" + detailIndex + "__updatePricingOnly");
		   document.getElementById("saveAllDetails_editDetails_" + detailIndex + "__updatePricingOnly").value=true;
		   var updatePricingOnly2 = document.getElementById("saveAllDetails_editDetails_" + detailIndex + "__updatePricingOnly");
		   
//		   document.getElementById("updatePricingOnly_42929971").value=true;
		   	   
//		   var detailSequence = getDetailSequenceForDetail(saveFormElement, detail);
		   	   
		   if ( parseInt($(vir).val()) > 0 ) {
				_TGL_='S1D'; 
				_ODET_=detail;
				showConfirmDialog('Price edited detail #' + detail +'?');
				return;
		   } else {
			   showMessageDialog('No changes to price');
		   }
		
	}

	function getDetailSequenceForDetail(saveFormElement, detail) {
		var fstDT = detail;
//        $("form[id^='savedetailform_']").each(function(i){
//	          var vid = $(this).attr('id');
	          var vid = saveFormElement.id;
	          var vDT = findRegexValue('savedetailform_([0-9]*$)',vid,'i',1);
	          var fch = '.itype'+vDT;
	          var lch = '.ltype'+vDT;
        	  var vir = '#changeddetail'+vDT;
//        	  var iDT = i;
        	  if ( parseInt($(vir).val()) > 0 ) {
            	  //add changes
              	  $(fch).each(function(i){
              		// from ID savedetailform_37778363_editItem_standardNumber
              		// to Id saveAllDetails_editDetails_1__item_standardNumber
      	        	var iid = $(this).attr('id');
      	        	iid = iid.replace('savedetailform_'+vDT,'saveAllDetails');
      	        	iid = iid.replace('editItem_','editDetails_'+iDT+'__item_');
      	        	iid = '#'+iid;
      	        	$(iid).val($(this).val());
            	  });
              	  $(lch).each(function(i){
                		// from ID savedetailform_37778363_editItem_standardNumber
                		// to Id saveAllDetails_editDetails_1__item_standardNumber
        	        	var iid = $(this).attr('id');
        	        	iid = iid.replace('savedetailform_'+vDT,'saveAllDetails');
        	        	iid = iid.replace('editItem_','editDetails_'+iDT+'__item_');
        	        	iid = '#'+iid;
        	        	$(iid).val($(this).attr('innerHTML'));
              	  });
            	  if ( vDT == detail ) {	  
                	// if is detail to be saved 
    	          	var ts='#saveAllDetails_editDetails_'+iDT+'__toBeSaved';
      	          	$(ts).val('true');
            	  } else {
                	// if user made changes capture and return the changes
      	          	var rt='#saveAllDetails_editDetails_'+iDT+'__toBeReturned';
      	          	$(rt).val('true');
            	  }
        	  }
//         });
         $('#saveAllDetails_selectedDetailNumber').val(fstDT);
         return iDT;
	}



</script>