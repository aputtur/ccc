<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<div id="angleTabs">


    
   <div id="viewOrderTopMenuTabs" style="color:6F97BB">
   
    <ul>
        <s:component theme="xhtml" template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Cancel Adjustment'"></s:param> 
        	<s:param name="hrefOnClick" value="'doCancelAdj()'"></s:param> 
        	<s:param name="hrefId" value="'adj_cancelTab'"></s:param>          
        </s:component>
         <s:component theme="xhtml" template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Complete Adjustment'"></s:param> 
        	<s:param name="hrefOnClick" value="'doCompleteAdj()'"></s:param> 
        	<s:param name="hrefId" value="'adj_completeTab'"></s:param>          
        </s:component>
         <s:component theme="xhtml" template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Save Adjustment'"></s:param> 
        	<s:param name="hrefOnClick" value="'doSaveAdj()'"></s:param> 
        	<s:param name="hrefId" value="'adj_saveTab'"></s:param>          
        </s:component>
	</ul>
    
    </div>
   <div id="viewOrderTopMenuButtons" style="display: none;">
     <ul>
       <li><s:submit id="detailSaveAll_%{item.ID}"
			    onclick="saveAllEditedDetails();return false;"
				cssStyle="odd" theme="simple" value="Save All Edits"></s:submit>
		</li>
		<li><s:submit id="detailDiscardAll_%{item.ID}"
			    onclick="discardEntireOrder();return false;"
				cssStyle="odd" theme="simple" value="Discard All Edits"></s:submit>
		</li>
	 </ul>
   </div>

</div>

	<SCRIPT type="text/javascript">


function doSaveAdjNoPrompt(itemId) {
	var doNotChargeBack = '';
	var doNotChargeBackAttr = document.getElementById("chbDoNotCharge1");
	if (doNotChargeBackAttr != null){
		doNotChargeBack = doNotChargeBackAttr.checked;
	}
	showProcessingOnHref();
	var actionValue = 'adjustmentMgmt!saveAdjustment.action?'
						+ 'doNotChargeBack=' + doNotChargeBack
						;
											;						
	$('#adjustmentMgmt').attr('action', actionValue);
	$('#adjustmentMgmt').submit();
}	

function doCompleteAdjNoPrompt(itemId) {
	var doNotChargeBack = '';
	var doNotChargeBackAttr = document.getElementById("chbDoNotCharge1");
	if (doNotChargeBackAttr != null){
		doNotChargeBack = doNotChargeBackAttr.checked;
	}
	showProcessingOnHref();
	var actionValue = 'adjustmentMgmt!completeAdjustment.action?'
						+ 'doNotChargeBack=' + doNotChargeBack
						;
						
	$('#adjustmentMgmt').attr('action', actionValue);
	$('#adjustmentMgmt').submit();
}	

function doCancelAdjNoPrompt(itemId) {
	showProcessingOnHref();
	var actionValue = 'adjustmentMgmt!cancelAdjustment.action';
						
	$('#adjustmentMgmt').attr('action', actionValue);
	$('#adjustmentMgmt').submit();
}	
	 
	</SCRIPT>						 
