<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

 
<div id="auditItemHistory" >
	<a href="#" theme="simple"  style="padding-top: 4px; font-size: 14px; color: white; width: 30%; float: right;" onClick="showAuditItemHistoryWindow(<s:property value="%{item.ID}"/>, <s:property value="%{searchConfirmNumber}"/>)"> Detail History</a>
</div>


<div id="editDetailAngleTabs" style="width: 55%; float: right;">

<s:form id="viewDetailTopMenu_%{detailIndex}" theme="simple" action="viewOrderHistory" namespace="/om/home" >
   
	<s:hidden theme="simple" name="quickTabSelected"></s:hidden>
	<s:hidden theme="simple" name="selectedDetailNumber" value="%{item.ID}"></s:hidden>
	<s:if test="invoiceView">
	<s:hidden theme="simple" name="selectedInvoiceNumber" value="%{item.invoiceId}"></s:hidden>
	</s:if>
	<s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"></s:hidden>
	<s:if test="bundleId > 0">
	<s:hidden theme="simple" name="selectedBundleNumber" value="%{item.bundleId}"></s:hidden>
	</s:if>
	<s:else>
	<s:hidden theme="simple" name="selectedBundleNumber" value="-1"></s:hidden>
	</s:else>
	<s:hidden theme="simple" name="selectedItem"></s:hidden>
	<s:hidden theme="simple" name="includeCancelledOrders"></s:hidden>
	<input type="button" onclick="$('#viewDetailTopMenu_<s:property value="detailIndex"/>').submit();" id='viewDetailTopMenu_<s:property value="detailIndex"/>_submit' style="display: none;"/>
    
    
</s:form>

    <ul>
        <s:if test="invoiced">
        <s:if test="haveDetailsStoredInOMS">
        <!-- DEFERRED TO DECEMBER
        <s:component theme="xhtml" template="viewdetailtopmenu.ftl">
            <s:param name="hrefValue" value="'Adjust Header'"></s:param> 
        	<s:param name="submitAction" value="'adjustmentMgmt.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/adjust'"></s:param>
        	<s:param name="submitInstance" value="%{#iterStatus.index}"></s:param>
        	<s:param name="submitDetailId" value="%{item.ID}"></s:param>       
        </s:component>
        -->
        <!-- DEFERRED TO DECEMBER
        <s:component theme="xhtml" template="viewdetailtopmenu.ftl">
            <s:param name="hrefValue" value="'Adjust Invoice'"></s:param> 
        	<s:param name="submitAction" value="'adjustmentMgmt.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/adjust'"></s:param>
        	<s:param name="submitInstance" value="%{#iterStatus.index}"></s:param>
        	<s:param name="submitDetailId" value="%{item.ID}"></s:param>       
        </s:component>
        -->
        </s:if>
        <s:if test="haveDetailsStoredInTF">
        <s:url id="tfadjustinvoiceurl" value="/adjustment/adjustment.do?operation=displayAdjustmentsForUser">
            <s:param name="operation" value="performInvoiceAdjustment"/>
            <s:param name="id" value="%{item.invoiceId}"/>
        </s:url>
        <s:url id="tfadjustdetailurl" value="/adjustment/adjustment.do?operation=displayAdjustmentsForUser">
            <s:param name="operation" value="performDetailAdjustment"/>
            <s:param name="id" value="%{item.iD}"/>
        </s:url>
        <!-- DEFERRED TO DECEMBER
        <s:component theme="xhtml" template="viewdetailtopmenu.ftl">
            <s:param name="hrefValue" value="'Adjust Invoice'"></s:param> 
        	<s:param name="submitValue" value="'%{#tfadjustinvoiceurl}'"></s:param>       
         	<s:param name="submitInstance" value="%{detailIndex}"></s:param>         
        	<s:param name="submitDetailId" value="%{item.ID}"></s:param>       
        </s:component>
        -->
        <!-- DEFERRED TO DECEMBER
        <s:component theme="xhtml" template="viewdetailtopmenu.ftl">
            <s:param name="hrefValue" value="'Adjust Detail'"></s:param> 
        	<s:param name="submitValue" value="'%{#tfadjustdetailurl}'"></s:param>       
        	<s:param name="submitInstance" value="%{detailIndex}"></s:param>
        	<s:param name="submitDetailId" value="%{item.ID}"></s:param>       
        </s:component>
        -->
        </s:if>
        </s:if>
        
        <s:if test="hasManageOrdersPrivelege"> 
        <s:if test="cancelable && storedInOMS && !haveAdjustedDetails">
	        <s:if test="canceled">
        <!-- No uncancel
	        <s:component theme="xhtml" template="viewdetailtopmenu.ftl">
	            <s:param name="hrefValue" value="'Un-Cancel'"></s:param> 
	        	<s:param name="submitAction" value="'viewOrderHistory!unCancelOrderDetail.action'"></s:param> 
	        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
	        	<s:param name="submitInstance" value="%{detailIndex}"></s:param>
	        	<s:param name="submitDetailId" value="%{item.ID}"></s:param>
	        	<s:param name="hrefId" value="'hideOnEdit_uncancelTab'"></s:param>       
	        </s:component>     
	     -->   
	        </s:if>
        	<s:else>
        	<s:component theme="xhtml" template="viewdetailtopmenu.ftl">
            	<s:param name="hrefValue" value="'Cancel'"></s:param> 
        		<s:param name="submitAction" value="'viewOrderHistory!cancelOrderDetail.action'"></s:param> 
        		<s:param name="submitNamespace" value="'/om/history'"></s:param>         
        		<s:param name="submitInstance" value="%{detailIndex}"></s:param>
        		<s:param name="submitDetailId" value="%{item.ID}"></s:param>
        		<s:param name="hrefId" value="'hideOnEdit_cancelTab'"></s:param>         
        	</s:component>
        	</s:else>
        </s:if>
        <s:if test="overrideAllowed">
        <s:component theme="xhtml" template="viewdetailtopmenu.ftl">
            <s:param name="hrefValue" value="'Override'"></s:param> 
        	<s:param name="submitAction" value="'overrideOrderDetail.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
        	<s:param name="submitInstance" value="%{detailIndex}"></s:param>
        	<s:param name="submitDetailId" value="%{item.ID}"></s:param>       
        </s:component>
        </s:if>
        <s:if test="rePinAllowed">
        <s:component theme="xhtml" template="viewdetailtopmenu.ftl">
            <s:param name="hrefValue" value="'Re-Pin'"></s:param> 
        	<s:param name="submitAction" value="'repinOrderDetail.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
        	<s:param name="submitInstance" value="%{detailIndex}"></s:param>
        	<s:param name="submitDetailId" value="%{item.ID}"></s:param>       
        </s:component>
        </s:if>
        <s:if test="storedInOMS && !rightslink && !haveAdjustedDetails">
        <s:component theme="xhtml" template="viewdetailtopmenu.ftl">
            <s:param name="hrefValue" value="'Edit'"></s:param> 
        	<s:param name="submitAction" value="'editOrderDetail.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
        	<s:param name="submitInstance" value="%{detailIndex}"></s:param>
        	<s:param name="submitDetailId" value="%{item.ID}"></s:param>       
         	<s:param name="hrefId" value="'editdetailhref'"></s:param>         
        </s:component>
        </s:if>
        </s:if>
    </ul>

</div>

	<script language="javascript">
	
	function showAuditItemHistoryWindow(itemId, confirmNumber)
	{
		var url = "viewDetailAuditHistory.action?itemId=" + itemId + "&confirmNumber=" + confirmNumber;
		if (window.showModalDialog) 
		{
			auditHistoryWindow = window.showModalDialog(url,'Audit History',
									"dialogWidth:1200px;dialogHeight:900px");
		} 
		else 
		{
			auditHistoryWindow = window.open(url,'Audit History','height=900, width=1200, scrollbars=1, location=0, modal=yes');
		}
	}
	</script>


	<SCRIPT type="text/javascript">
	 function submitDetailTopMenuForm_<s:property value="detailIndex"/>_<s:property value="item.iD"/>( action ) {
	        var vIter=<s:property value="#iterStatus.index"/>;
	        var vJqFrmPrefix='#viewDetailTopMenu_'+vIter;
	        var vDetail='<s:property value="item.iD"/>';
	        var vBundle='<s:property value="bundleId"/>';
	        var vConfirm='<s:property value="confNumber"/>';
	        var showDetail = '#showing_detail_'+vBundle+'_order_'+vConfirm+'_detail_'+vDetail;
	        var editDetail = '#editing_detail_'+vBundle+'_order_'+vConfirm+'_detail_'+vDetail;
	        if ( action.indexOf('editOrderDetail.action') != -1 ) {
	            if ( jQuery(editDetail).is(':hidden') ) {
		            if ( EDITSECTION != '' ) {
	                    if ( EDITSECTION.indexOf('detail') == -1 ) {
	                        showMessageDialog('Already editing ' + EDITSECTION);
	                    } else {
		                	if ( EDITSECTION.indexOf(vDetail) != -1 ) {
	    	                	showMessageDialog('Already editing detail '+ vDetail );
	        	        	} else {
	            				$(showDetail).hide();
	            				$(editDetail).show();
	            				jQuery('#editdetailhref_'+vDetail).removeClass('editDetailAngleTabs');
	            				jQuery('#editdetailhref_'+vDetail).addClass('editingDetailAngleTabs');
	            				jQuery('#editdetailhref_'+vDetail).hide();	            				
	            				//jQuery('#editdetailhref_'+vDetail).attr('color','white');
	            				setEditDetailAndCount();
	        	        	}
	        	        }
	            	} else {
	            		$(showDetail).hide();
	            		$(editDetail).show();
     	    			jQuery('#editdetailhref_'+vDetail).removeClass('editDetailAngleTabs');
	            		jQuery('#editdetailhref_'+vDetail).addClass('editingDetailAngleTabs');
           				jQuery('#editdetailhref_'+vDetail).hide();
    	                //jQuery('#editdetailhref_'+vDetail).attr('color','white');
	            		setEditDetailAndCount();
	            	}
	            } else {
	               showMessageDialog('Already editing detail '+ vDetail );
	            }    
	        } else {
	        
		        if ( EDITSECTION != '' ) {
                    showMessageDialog('Already editing ' + EDITSECTION);
                } else {
	        		$(vJqFrmPrefix+'_selectedDetailNumber').val('<s:property value="item.iD"/>');
	        		$(vJqFrmPrefix+'_selectedConfirmNumber').val('<s:property value="confNumber"/>');
	        		$(vJqFrmPrefix+'_selectedBundleNumber').val('<s:property value="bundleId"/>');
	    			var msg ='Action='+action;
	        	    if ( action.indexOf('cancelOrderDetail.action') != -1 ) {
	        	          $(vJqFrmPrefix).attr('action',action);
	        	          cancelOrderDetail(vJqFrmPrefix,'<s:property value="item.iD"/>', action);
	        	          return false;
	        	    }
	        	    if ( action.indexOf('unCancelOrderDetail.action') != -1 ) {
	        	          $(vJqFrmPrefix).attr('action',action); 
	        	          unCancelOrderDetail(vJqFrmPrefix,'<s:property value="item.iD"/>', action);
	        	          return false;
	        	    }
	        	    if ( action.indexOf('adjustmentMgmt.action') != -1 ) {
	        	          $(vJqFrmPrefix).attr('action',action); 
	        	          submitAdjustment(vJqFrmPrefix,'<s:property value="item.iD"/>', action);
	        	          return false;
	        	    }
	        	    msg +='<br/>ConfirmationId='+ $(vJqFrmPrefix+'_selectedConfirmNumber').val();
	        		msg +='<br/>BundleId='+ $(vJqFrmPrefix+'_selectedBundleNumber').val();
	        		msg +='<br/>DetailId='+ $(vJqFrmPrefix+'_selectedDetailNumber').val();
	        		msg +='<br/><br/><b>Action not implemented yet</b>';
	        		msg +='<br/><br/>';
	    			showMessageDialog(msg);
	    		}
	        }    
	    
	 }
	 
	</SCRIPT>						 
