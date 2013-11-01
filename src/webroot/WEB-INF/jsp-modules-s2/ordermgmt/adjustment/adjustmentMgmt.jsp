<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%-- 
  This is the flow of jsp includes from here onwards
  adjustmentMgmt.jsp
    adjustmentViewOrderTopMenu.jsp
    adjustmentViewCustomerInfo.jsp
    overallAdjustment.jsp
    calcAdjustment.jsp (for each item)
    adjustmentSummary.jsp
    adjViewConfirmationDetails.jsp
    adjustmentViewOrderTopMenu.jsp
    
--%>


<div
	style="font-size: 10px;background-color: #E2E9F0; padding-top: 10px; padding-bottom: 10px; padding-left: 20px; padding-right: 20px;">
	<s:if test='currentQuickTab.equals("ordermgmt.menu.adjustment.pending")'>
		&lt;&lt;&nbsp;
		<s:url id="backToURL" action="/searchAdjustments" namespace="/om/adjust" includeParams="none">
			<s:param name="quickTabSelected" value="%{currentQuickTab}"/>
		</s:url>
	</s:if>
	<s:else>
		&lt;&lt;&nbsp;<s:url id="backToURL" value="/om/history/searchOrdersForAdjustments!returnToSearchResults.action" namespace="/om/history" includeParams="none"/>
	</s:else>
	<s:url id="saveAdjustmentURL" action="adjustmentMgmt!saveAdjustment.action"  namespace="/om/adjust" includeParams="none">
		<s:param name="quickTabSelected" value="%{currentQuickTab}"/>
	</s:url>
	
	<a href="#"
		onclick="saveOrDeleteAdjustmentOnBackTo('<s:property value="#backToURL"/>','<s:property value="#saveAdjustmentURL"/>','<s:property value="quickTabSelected"/>','<s:property value="%{adjustmentVersion}"/>');return false;">back to search results</a>
</div>

<s:if test="!unableToCreateOrLocateAdjustment">

<s:form action="adjustmentMgmt" namespace="/om/adjust" method="post">
    
	<table width="100%">

		<tr>
			<td>
				<div style="width: 60%; float: right; padding: 0px; margin: 0px;">
					<jsp:include
						page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/adjustmentViewOrderTopMenu.jsp" />
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="order_header"
					style="width: 972px; border: 0px solid #000000; margin-top: 5px; margin-left: 10px; margin-right: 10px; margin-bottom: 0px; padding: 0px;">
					<div style="width: 30%; float: left; padding: 0px; margin: 0px;">
						<h1
							style="display: inline; color: #6F97BB; padding: 0px; margin: 0px;">
							<s:property	value="adjustMessage" />
						</h1>
					</div>
					<div id="aaaa" style="width: 90%; float: center; padding: 0px; margin: 0px;">  &nbsp;
					</div>
					<s:if test="adjHeader.creditCardPayment">
					<div style="width: 25%; float: right; padding: 0px; margin: 0px;">		 
						<s:checkbox cssClass="odd"	theme="simple" name="doNotChargeBack" label="Do not chargeback to credit card" 
						labelSeparator="" onclick="doNotChargeClick(this);" id="chbDoNotCharge1"></s:checkbox>
						<label class="label" style="text-align:left; font-weight:bold">Do not chargeback to credit card</label>
				  	</div>
					</s:if>
				</div>
			</td>
		</tr>
		<tr>
			<td>
					<div id="order_collapseheader">
						<jsp:include
							page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/adjustmentViewCustomerInfo.jsp" />
					</div>
					<s:if test="!detailView">
						<div id="order_overall">
							<jsp:include
								page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/overallAdjustment.jsp" />
						</div>
					</s:if>
					<div id="clearer"></div>
					<s:hidden id='numberOfItems' name='numberOfItems'  value="%{adjHeader.adjItems.size}"/>	
					<s:iterator value="adjHeader.adjItems" id="item" status="rowStatus">	
					<div id="order_collapseheader">
						<jsp:include
							page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/calcAdjustment.jsp" />
					</div>
					</s:iterator>
					
					<div id="clearer"></div>
					<div id="order_collapseheader">
						<jsp:include
							page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/adjustmentSummary.jsp" />
					</div>
					<%-- 
					<div id="clearer"></div>
					<s:if test="!pagingDetails">
						<s:if test="!selectedOrder.myItems.empty">
							<jsp:include
								page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/adjViewConfirmationDetails.jsp" />
						</s:if>
					</s:if>
					<s:else>
						<jsp:include
							page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/adjViewConfirmationDetails.jsp" />
					</s:else>
					--%>
			</td>
		</tr>

	</table>
	<div id="clearer"></div>

	    
	<table width="100%">
		<tr>
			<td>
				<div style="width: 60%; float: right; padding: 0px; margin: 0px;">
					<jsp:include
						page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/adjustmentViewOrderTopMenu.jsp" />
				</div>
			</td>
		</tr>
		<!-- comment this until the javascript to match the other is here -->
		<s:if test="adjHeader.creditCardPayment">
		<tr>
			<td>
					<!-- <div style="width: 70%; float: center; padding: 0px; margin: 0px;">  &nbsp
					</div>  -->
					<div style="width: 25%; float: right; padding: 0px; margin: 0px;">
					<s:checkbox cssClass="odd"	theme="simple" name="doNotChargeBack" label="Do not chargeback to credit card" 
						labelSeparator="" onclick="doNotChargeClick(this);" id="chbDoNotCharge2"></s:checkbox>
						<label class="label" style="text-align:left; font-weight:bold">Do not chargeback to credit card</label>
						<!-- <input type="checkbox" name="doNotChargeBack" value="doNotChargeBack" id="doNotChargeBack" id="chbDoNotCharge2">
						<label for="doNotChargeBack" class="label" style=" font-weight:bold" >Do not chargeback to credit card </label>  -->
					</div>
		   </td>
		</tr>
		</s:if>
		
	</table>
	
	<s:hidden name="quickTabSelected" value="%{currentQuickTab}"/>
		
</s:form>

</s:if>

<s:component template='confirmdialog.ftl'>
	<s:param name="dialogTitle" value="' '" />
	<s:param name="cancelButton" value="'No'" />
	<s:param name="goButton" value="'Yes'" />
	<s:param name="goScript" value="'confirmDialogGo'" />
</s:component>

<s:component template='confirmdialogYesNo.ftl'>
	<s:param name="dialogTitle" value="' '" />
	<s:param name="cancelButton" value="'No'" />
	<s:param name="goButton" value="'Yes'" />
	<s:param name="goScript" value="'confirmDialogGo'" />
	<s:param name="cancelGoScript" value="'confirmDialogNoGo'" />
</s:component>
	
<script type="text/javascript">
var _OOBJ_;
var _OORD_;
var _OBDL_;
var _ODET_;
var _OACT_;
var _TGL_;
var _ADJ_;
var _ACTION_;
var _TAB_;

DETAILCOUNT=0;

<s:if test="fieldErrors.empty && actionErrors.empty">
	$(document).ready( function() {
		$("#viewOrderHistory_editBundle_startOfTerm").datepicker( {
			showAnim : 'fadeIn'
		});
		
		showSelectedSection();

		//Product no to be editied - commenting out call
		//displayProductDetailsForExistingProductCodes();

		addChangeResetEventsToEditForms();

	});
</s:if>
	function confirmDialogGo() {
		if ( _TGL_ == 'B' ) {
			toggleBundleDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
		}
		if ( _TGL_ == 'O' ) {
		    	toggleOrderDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
		}
		if ( _TGL_ == 'OE' ) {
		        toggleOrderDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
		}
		if ( _TGL_ == 'BE' ) {
		        toggleBundleDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
		}
		if ( _TGL_ == 'CD' ) {
		        discardDetailEditNoPrompt(_OORD_,_OBDL_,_ODET_);
		}
		if ( _TGL_ == 'CC' ) {
			cancelCourseEditHeaderNoPrompt(_OBDL_);
		}
		if ( _TGL_ == 'OH' ) {
			cancelEditHeaderNoPrompt();
		}
		if ( _TGL_ == 'DEO' ) {
			discardEntireOrderNoPrompt();
		}
		if ( _TGL_ == 'SAD' ) {
			saveAllDetailsNoPrompt();
		}
		if ( _TGL_ == 'S1D' ) {
			saveOneDetailNoPrompt(_ODET_);
		}
		if ( _TGL_ == 'C1D' ) {
			submitDetailTopMenuNoPrompt(_OACT_);
		}
		if ( _TGL_ == 'U1D' ) {
			submitDetailTopMenuNoPrompt(_OACT_);
		}
		if ( _TGL_ == 'CNCL' ) {
			doCancelAdjNoPrompt(_ADJ_);
		}
		if ( _TGL_ == 'SV' ) {
			doSaveAdjNoPrompt(_ADJ_);
		}
		if ( _TGL_ == 'CMPLT' ) {
			doCompleteAdjNoPrompt(_ADJ_);
		}
		if ( _TGL_ == 'DELETE' ) {
			doSaveAdjNoPromptAndGo(_ADJ_);
		}
		
	}
	function confirmDialogNoGo() {
		if ( _TGL_ == 'DELETE' ) {
			doDeletedAdjNoPromptGo(_ADJ_);
		}
		
	}
	
	
	function saveOrDeleteAdjustmentOnBackTo(actionBackTo, actionSaveAdj , tab, adjVersion){
		if (adjVersion == 1){
			doDeleteAdj(actionBackTo, actionSaveAdj , tab, adjVersion);
		}else
		{
			submitTopMenuForm(actionBackTo, tab);
		}
	}
	function doDeleteAdj(actionBackTo, actionSaveAdj , tab, adjVersion) {
		_TGL_='DELETE'; 
		_ACTION_ = actionBackTo;
		_TAB_= tab;
		showConfirmDialogYesNo("Do you want to save this adjustment?");
		return;
	}	
	function showConfirmDialogYesNo(message) {
	  	$('#confirmDialogYesNoDiv_Content').html(message);
		$('#confirmDialogYesNo_Div').dialog('open');
		return false;	
	}
	
	function doSaveAdjNoPromptAndGo(itemId) {
		showProcessingOnHref();
		var actionValue = 'adjustmentMgmt!saveAdjustmentAndGo.action?' + 'gotoTab=' + _TAB_;
		
		$('#adjustmentMgmt').attr('action', actionValue);
		$('#adjustmentMgmt').submit();
	}
	
	function doDeletedAdjNoPromptGo(itemId) {
	
		showProcessingOnHref();
		var actionValue = 'adjustmentMgmt!deleteAdjustmentAndGo.action?' + 'gotoTab=' + _TAB_;
		
		$('#adjustmentMgmt').attr('action', actionValue);
		$('#adjustmentMgmt').submit();

	}

	function resetDetailFormFields(itemId) {
		showProcessingOnHref();
		var actionValue = 'adjustmentMgmt!clearAdjustment.action?'
							+ '&selectedDetailId=' + itemId
							;
		$('#adjustmentMgmt').attr('action', actionValue);
		$('#adjustmentMgmt').submit();
	}
	
	function doCalcAdj(itemId) {
		showProcessingOnHref();
		var creditDetailFull = document.getElementById("creditDetailFull_" + itemId).checked;

		var parmOneValueAdj = '';
		var parmOneValueAdj1 = document.getElementById("parmOneValueAdj_" + itemId);
		if (parmOneValueAdj1 != null){
			parmOneValueAdj = parmOneValueAdj1.value;
		}

		var parmTwoValueAdj = '';
		var parmTwoValueAdj1 = document.getElementById("parmTwoValueAdj_" + itemId);
		if (parmTwoValueAdj1 != null){
			parmTwoValueAdj = parmTwoValueAdj1.value;
		}

		var licenseeFeeAdj = document.getElementById("licenseeFeeAdj_" + itemId).value;
		var royaltyAdj = document.getElementById("royaltyAdj_" + itemId).value;
		var reasonCode = document.getElementById("reasonCode_" + itemId).value;
		
		var actionValue = 'adjustmentMgmt!calculateAdjustment.action?'
							+ 'parmOneValueAdj=' + parmOneValueAdj
							+ '&parmTwoValueAdj=' + parmTwoValueAdj
							+ '&licenseeFeeAdj=' + licenseeFeeAdj
							+ '&royaltyAdj=' + royaltyAdj
							+ '&reasonCode=' + reasonCode
							+ '&creditDetailFull=' + creditDetailFull
							+ '&selectedDetailId=' + itemId
							;
							
	   	$('#adjustmentMgmt').attr('action', actionValue);
	   	$('#adjustmentMgmt').submit();
}
	
	

		function showProcessingOnHref() {
	    showMessageDialog($('#waiting_for_search').html());
	    return false;
	}
	
	function doNotChargeClick(chb) {
		if (chb.checked) {
			$('#chbDoNotCharge1').attr('checked', true);
		 	$('#chbDoNotCharge2').attr('checked', true);
		 		}
		else {
			$('#chbDoNotCharge1').attr('checked', false);
		 	$('#chbDoNotCharge2').attr('checked', false);
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
			
	function showConfirmDialog(message) {

	  	$('#confirmDialogDiv_Content').html(message);
		$('#confirmDialog_Div').dialog('open');
		return false;	
	}
	
	function doReSort() {
		    $('#viewOrderHistory').attr('action','adjustmentMgmt!resortSearchResults.action');
		    $('#viewOrderHistory').submit();   
	}


	
	function alertEditingOnClose(detail) {

		var vDetails = [];
        var editDetail = "div[id^='editing_detail_']";
        jQuery(editDetail).not(':hidden').each(function(i){
			var vid = $(this).attr('id');
			var vlu = findRegexValue('.*_detail_([0-9]*$)',vid,'i',1);
			if ( vlu == detail ) {
				vDetails[i]=vlu;
			}
		});
		if ( vDetails.length > 0 ) {
		   var vir = '#changeddetail'+detail;
		   if ( parseInt($(vir).val()) > 0 ) {
		   		return true;
		   }
		} 
		return false;
	}
	
	function showMessageDialogNB(message) {
		$('#messageDialogDiv_Content_NB').html(message);
		$('#messageDialog_Div_NB').dialog('open');
		return false;	
	}
	
    
    function doSaveAdj(itemId) {
		_TGL_='SV'; 
		_ADJ_=itemId;
		showConfirmDialog("Are you sure you want to save this adjustment?");
		return;
	}	
	
	function doCompleteAdj(itemId) {
		_TGL_='CMPLT'; 
		_ADJ_=itemId;
		var modified = checkForModifications();
		if (modified == true){
			showConfirmDialog("There are unsaved changes. Do you still want to complete this adjustment?");
		}else{
			showConfirmDialog("Are you sure you want to complete this adjustment?");
		}
		return;		
	}	
	
	function checkForModifications() {
	
		var modified = false;
		var numberOfItems = document.getElementById("numberOfItems").value;
		
		for (var loop = 0; loop < numberOfItems; loop++)
		{
			var itemId = document.getElementById("rowItemId_" + loop).value;	
			
			//parmOneValueAdj
			var parmOneValueAdj = document.getElementById("parmOneValueAdj_" + itemId);
			if (parmOneValueAdj != null){
				parmOneValueAdj = parmOneValueAdj.value;
				var parmOneValueAdjOrg = document.getElementById("parmOneValueAdjOrg_" + itemId).value;
				//alert ( ' doCompleteAdjTest 5 parmOneValueAdj  ' + parmOneValueAdj + ' , ' + parmOneValueAdjOrg );	
				if (parmOneValueAdj != parmOneValueAdjOrg){
					modified = true;
					//alert ( ' doCompleteAdjTest parmOneValueAdj  modified '  + modified + ' , ' + loop);
				}
			}
			
			//parmTwoValueAdj
			var parmTwoValueAdj = document.getElementById("parmTwoValueAdj_" + itemId);
			if (parmTwoValueAdj != null){
				parmTwoValueAdj = parmTwoValueAdj.value;
				var parmTwoValueAdjOrg = document.getElementById("parmTwoValueAdjOrg_" + itemId).value;
				if (parmTwoValueAdj != parmTwoValueAdjOrg){
					modified = true;
				}
			}
			
			//licenseeFeeAdj
			var licenseeFeeAdj = document.getElementById("licenseeFeeAdj_" + itemId);
			if (licenseeFeeAdj != null){
				licenseeFeeAdj = licenseeFeeAdj.value;
				var licenseeFeeAdjOrg = document.getElementById("licenseeFeeAdjOrg_" + itemId).value;
				if (licenseeFeeAdj != licenseeFeeAdjOrg){
					modified = true;
				}
			}
			
			//royaltyAdj
			var royaltyAdj = document.getElementById("royaltyAdj_" + itemId);
			if (royaltyAdj != null){
				royaltyAdj = royaltyAdj.value;
				var royaltyAdjOrg = document.getElementById("royaltyAdjOrg_" + itemId).value;
				if (royaltyAdj != royaltyAdjOrg){
					modified = true;
				}
			}
		}
		return modified;

	}
	
	function doCancelAdj(itemId) {
		_TGL_='CNCL'; 
		_ADJ_=itemId;
		showConfirmDialog("Are you sure you want to cancel this adjustment?");
		return;
	}	
	
</script>