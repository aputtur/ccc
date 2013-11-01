<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<br /><br />
<div>
<br/>
 
<s:form action="searchAdjustments" method="post" namespace="/om/adjust" cssStyle="padding-left: 10px;">	
						<h1
							style="display: inline; color: #6F97BB; padding: 0px; margin: 0px;">
							<s:if test="pendingAdjustmentsExist">
							Pending Adjustments
							</s:if>
							<s:else>
							No Pending Adjustments
							</s:else>
						</h1>
		&nbsp;
  <s:if test="pendingAdjustmentsExist">
	<table id="adjustmentHdr"   style="text-align: center; width: 900px;  background-color: #F3F6F9; border: 1px solid #808080; margin-top: 10px; margin-left: 10px; margin-right: 10px; margin-bottom: 10px; padding: 0px;">
 				<col style="width: 100px;"/>
 				<col style="width: 120px;"/>
				<col style="width: 100px;"/>
				<col style="width: 120px;"/>
				<col style="width: 120px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
                <tr style="border: 1px solid #808080;background-color: #CCCCCC;height: 40px">
                  <th>
                    Invoice Number
                  </th>
                  <th>
                    Adjustment ID
                  </th>
                  <th>
                    Adjustment Type
                  </th>
                  <th>
                    Adjustment Entity ID
                  </th>
                  <th>
                    Adjustments
                  </th>
                  <th>
                    Created On
                  </th>
                  <th>
                    Created By
                  </th>
                  <th>
                    Updated On
                  </th>
                  <th>
                    Updated By
                  </th>
				  <th>
                    Source
                  </th>
				  <th>
                    &nbsp;
                  </th>
                </tr>
		<s:iterator value="openAdjustmentList" status="iterStatus">
					<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/adjustment/include/viewOneAdjustmentShort.jsp" />
		</s:iterator>
	</table>						
  </s:if>
</s:form>

<s:component template='confirmdialog.ftl'>
	<s:param name="dialogTitle" value="' '" />
	<s:param name="cancelButton" value="'No'" />
	<s:param name="goButton" value="'Yes'" />
	<s:param name="goScript" value="'confirmDialogGo'" />
	<s:param name="confirmId" value="'PENDING'" />
</s:component>

</div>
 <script type="text/javascript">

 var _PENDING_;

 

	<s:if test="completedAdjustment">
	$(window).load(function () {
		showAdjustmentCompleteMessage();
	});
	</s:if>	

    function showAdjustmentCompleteMessage(){
    	showMessageDialog('The adjustment has successfully completed');
    }
    
    
    function editAdjustment( adjustmentID ){
      
      var editAdjustmentForm = document.getElementById("editAdjustmentForm");
      var selectedAdjustmentDescriptorID = document.getElementById("selectedAdjustmentDescriptorID");
      
      if( editAdjustmentForm && selectedAdjustmentDescriptorID ){
        
        selectedAdjustmentDescriptorID.value = adjustmentID;
        
        editAdjustmentForm.submit();
      }
    }
    
    function deleteAdjustment( adjustmentID ){
      
      var deleteAdjustmentForm = document.getElementById("deleteAdjustmentForm");
      var selectedAdjustmentDescriptorIDToDelete = document.getElementById("selectedAdjustmentDescriptorIDToDelete");
      
      if( deleteAdjustmentForm && selectedAdjustmentDescriptorIDToDelete ){
      
        if( confirm("Are you sure you want to cancel this adjustment?") ){
        
          selectedAdjustmentDescriptorIDToDelete.value = adjustmentID;
          
          deleteAdjustmentForm.submit();
        }
      }
      
    }
	function showProcessingOnHref() {
		showMessageDialogNB($('#waiting_for_search').html());
	    return true;
	}
	function showMessageDialogNB(message) {
		$('#messageDialogDiv_Content_NB').html(message);
		$('#messageDialog_Div_NB').dialog('open');
		return false;	
	}
	function showMessageDialog(message) {
	  	   $('#messageDialogDiv_Content').html(message);
		   $('#messageDialog_Div').dialog('open');
		   return false;	
	}

	//call back from the template
	function confirmDialogGo() {
		doDeleteAdjNoPrompt() ;
	}
	
	function showConfirmDialog(message) {

	  	$('#confirmDialogDiv_Content_PENDING').html(message);
		$('#confirmDialog_Div_PENDING').dialog('open');
		return false;	
	}
		
	function doDeleteAdj(adjustmentAction) {
		_PENDING_ = adjustmentAction;
		//alert('doDeleteAdj ' + _PENDING_);
		showConfirmDialog("Are you Sure you want to Cancel this Adjustment?");
		return;
	}
	function doDeleteAdjNoPrompt() {
		showProcessingOnHref();
		//alert('doDeleteAdjNoPrompt ' + _PENDING_);
		$('#searchAdjustments').attr('action', _PENDING_);
		$('#searchAdjustments').submit();
		return;
	}
  </script>


