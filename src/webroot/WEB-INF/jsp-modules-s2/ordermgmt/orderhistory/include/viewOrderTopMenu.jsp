<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<div id="angleTabs">

  <div id="viewOrderTopMenuTabs">
   
    <ul>
        <s:if test="!invoiceView">
        <s:if test="hasManageOrdersPrivelege"> 
        <s:if test="haveOpenDetails && haveDetailsStoredInOMS && !haveGWDetails && !haveAdjustedDetails">
        <s:component theme="xhtml" template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Cancel Open Details'"></s:param> 
        	<s:param name="submitAction" value="'viewOrderHistory!cancelOpenItemDetails.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>
        	<s:param name="hrefId" value="'hideOnEdit_cancelTab'"></s:param>          
        </s:component>
        </s:if>
        <s:if test="overrideAllowEdit">
        <s:component theme="xhtml" template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Copy Confirmation'"></s:param> 
        	<s:param name="submitAction" value="'copyOrder.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>
        	<s:param name="hrefId" value="'hideOnEdit_copyConfTab'"></s:param>           
        </s:component>
       </s:if>
         <s:if test="haveDetailsStoredInOMS && haveNonRLDetails && !haveAdjustedDetails">
        <s:component template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Edit All Details'"></s:param> 
        	<s:param name="submitAction" value="'editEntireOrder.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
        	<s:param name="hrefId" value="'entireorder'"></s:param>         
        </s:component>
        </s:if>
        </s:if>
        </s:if>
        
        
        <s:if test="invoiceView">
        <s:if test="haveDetailsStoredInTF">
        <s:url id="tfadjustpurchaseurl" value="/adjustment/adjustment.do?operation=displayAdjustmentsForUser">
            <s:param name="operation" value="performPurchaseAdjustment"/>
            <s:param name="id" value="%{confirmNum}"/>
        </s:url>
        <!-- DEFERRED TO DECEMBER
        <s:component theme="xhtml" template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Adjust Invoice'"></s:param> 
        	<s:param name="submitValue" value="'%{#tfadjustpurchaseurl}'"></s:param>       
        </s:component>
        -->
        </s:if>
        <s:if test="haveDetailsStoredInOMS">
        <!-- DEFERRED TO DECEMBER
        <s:component theme="xhtml" template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Adjust Invoice'"></s:param> 
        	<s:param name="submitAction" value="'adjustInvoice.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
        </s:component>
        -->
        <s:if test="haveNonRLDetails && !haveAdjustedDetails">
        <s:component theme="xhtml" template="viewtopmenu.ftl">
            <s:param name="hrefValue" value="'Edit All Details'"></s:param> 
        	<s:param name="submitAction" value="'editEntireOrder.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
        	<s:param name="hrefId" value="'entireorder'"></s:param>         
        </s:component>
        </s:if>
        </s:if>
        </s:if>
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
	 function submitViewTopMenuForm( action ) {
	    var vJqFrmPrefix='#viewOrderTopMenu_form';
	    if ( action.indexOf('editEntireOrder.action') != -1 ) {
	        var isCan = jQuery('a#entireorder_href').html();
	        if ( isCan.indexOf('Discard') != -1 ) {
	        	discardEntireOrder();
	        } else {
	        	editEntireOrder();
	        }
	    } else {
	        if ( EDITSECTION.indexOf('detail') != -1) {
	            showMessageDialog('Already editing ' + EDITSECTION);
	        } else {
    			var msg ='Action='+action;
        	    if ( action.indexOf('cancelOpenItemDetails.action') != -1 ) {
         	          $(vJqFrmPrefix).attr('action',action);
        	          cancelOpenItemDetails(vJqFrmPrefix,'<s:property value="selectedConfirmNumber"/>', action);
        	          return false;
        	    }
	    		var msg ='Action='+action;
	        	msg += '<br/><br/><span style="font-weight: bold">Action not implemented yet</span>';
	    		showMessageDialog(msg);
	    	}
	    
	    //$('#viewOrderTopMenu').attr('action', action);
	    //$('#viewOrderTopMenu').submit();
	    }
	 }
 
	 
	 
	 
	</SCRIPT>						 
