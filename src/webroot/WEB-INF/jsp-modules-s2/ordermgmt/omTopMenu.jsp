<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<div id="mainTabs">

<s:form theme="simple" action="orderManagementHome" namespace="/om/home" >
   
	<s:hidden theme="simple"  name="quickTabSelected"></s:hidden>
	<s:hidden theme="simple"  name="myOrders" value="%{quickTabUserCounts.userMyOrders}"></s:hidden>
	<s:hidden theme="simple"  name="myAdjustments" value="%{quickTabUserCounts.userMyAdjustments}"></s:hidden>
	<s:hidden theme="simple"  name="pendingAdjustments" value="%{quickTabUserCounts.userPendingAdjustments}"></s:hidden>
	<s:hidden theme="simple"  name="myResearch" value="%{quickTabUserCounts.userMyResearch}"></s:hidden>
	<s:hidden theme="simple"  name="assignedResearch" value="%{quickTabUserCounts.userAssignedResearch}"></s:hidden>
	<s:hidden theme="simple"  name="unassignedResearch" value="%{quickTabUserCounts.userUnassignedResearch}"></s:hidden>

</s:form>
	<ul>
    	   
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.home'"></s:param> 
        	<s:param name="submitAction" value="'orderManagementLanding.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om'"></s:param>         
        </s:component>
        
        
        <s:if test="hasOrderSearchPrivelege"> 
	        <s:component template="topmenu.ftl">
	        	<s:param name="tabValue" value="'ordermgmt.menu.order'"></s:param> 
	        	<s:param name="submitAction" value="'searchOrder.action'"></s:param> 
	        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
	        </s:component>
	    </s:if>    
		<s:if test="false"> <!-- REMOVED FOR DECEMBER RELEASE -->	
	        <s:component template="topmenu.ftl">
	        	<s:param name="tabValue" value="'ordermgmt.menu.order.my'"></s:param> 
	        	<s:param name="submitAction" value="'searchOrder.action'"></s:param> 
	        	<s:param name="submitNamespace" value="'/om/history'"></s:param>         
	        	<s:param name="userCountEnabled" value="true"></s:param>         
	        	<s:param name="userCount" value="%{quickTabUserCounts.userMyOrders}"></s:param>         
	        </s:component>
        </s:if>
	<s:if test="overrideAllowEdit">	
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.order.new'"></s:param> 
        	<s:param name="submitAction" value="'newOrder.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/neworder'"></s:param>         
        </s:component>
	</s:if>
	<s:if test="hasEnterAdjPrivelege">	
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.adjustment'"></s:param> 
        	<s:param name="submitAction" value="'searchOrdersForAdjustments.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/history'"></s:param>          
        </s:component>
	</s:if>
	<s:if test="false"> <!-- REMOVED FOR DECEMBER RELEASE -->
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.adjustment.my'"></s:param> 
        	<s:param name="submitAction" value="'searchAdjustments.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/adjust'"></s:param>         
        	<s:param name="userCountEnabled" value="true"></s:param>         
        	<s:param name="userCount" value="%{quickTabUserCounts.userMyAdjustments}"></s:param>         
        </s:component>
	</s:if>
	<s:if test="hasCommitAdjPrivelege">
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.adjustment.pending'"></s:param> 
        	<s:param name="submitAction" value="'searchAdjustments.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/adjust'"></s:param>         
        	<s:param name="userCountEnabled" value="false"></s:param>         
        	<s:param name="userCount" value="%{quickTabUserCounts.userPendingAdjustments}"></s:param>         
        </s:component>
    </s:if>
	<s:if test="overrideAllowEdit">
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.research'"></s:param> 
        	<s:param name="submitAction" value="'searchSpecialOrders.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/research'"></s:param>      
        </s:component>
	
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.research.my'"></s:param> 
        	<s:param name="submitAction" value="'researchHome.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/research'"></s:param>       
        	<s:param name="userCountEnabled" value="true"></s:param>         
        	<s:param name="userCount" value="%{quickTabUserCounts.userMyResearch}"></s:param>         
        </s:component>

        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.research.assigned'"></s:param> 
        	<s:param name="submitAction" value="'researchHome.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/research'"></s:param>       
        	<s:param name="userCountEnabled" value="true"></s:param>         
        	<s:param name="userCount" value="%{quickTabUserCounts.userAssignedResearch}"></s:param>         
        </s:component>

        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.research.unassigned'"></s:param> 
        	<s:param name="submitAction" value="'researchHome.action'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/research'"></s:param>       
        	<s:param name="userCountEnabled" value="true"></s:param>         
        	<s:param name="userCount" value="%{quickTabUserCounts.userUnassignedResearch}"></s:param>         
        </s:component>
	</s:if>
	<s:if test="hasEmulatePrivelege">
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.emulate'"></s:param> 
        	<s:param name="submitValue" value="'/admin/startEmulationForm.do'"></s:param> 
        	<s:param name="hrefTarget" value="'_blank'"></s:param>         
        </s:component>
    </s:if>
	<s:if test="overrideAllowEdit">
        <s:component template="topmenu.ftl">
        	<s:param name="tabValue" value="'ordermgmt.menu.reports'"></s:param> 
        	<s:param name="submitAction" value="'orderManagementHome'"></s:param> 
        	<s:param name="submitNamespace" value="'/om/home'"></s:param>         
        </s:component>
    </s:if>
	</ul>	

</div>
<div id="submitTopMenuMessage" style="display: none;">
Before navigating away from this page, please note that you
currently have __EDITSECTION__ open for editing.
<p>
Continue anyway?
</p>
</div>
<s:component template='confirmdialog.ftl'>
	<s:param name="dialogTitle" value="' '" />
	<s:param name="cancelButton" value="'No'" />
	<s:param name="goButton" value="'Yes'" />
	<s:param name="goScript" value="'submitTopMenuOnConfirmDo'" />
	<s:param name="confirmId" value="'TOPMENU'" />
</s:component>	

<s:component template='confirmdialog.ftl'>
	<s:param name="dialogTitle" value="' '" />
	<s:param name="cancelButton" value="'No'" />
	<s:param name="goButton" value="'Yes'" />
	<s:param name="goScript" value="'confirmDialogGo'" />
	<s:param name="confirmId" value="'ADJUST'" />
</s:component>

	<SCRIPT type="text/javascript">
	
	 var EDITSECTION = '';
	 var DETAILCOUNT = 0;
	 var _TMNU_ACT_='';
	 var _TMNU_TAB_='';
	 var _ADJUST_ ;
	 
	 function submitTopMenuForm( action, tab ) {

	    var goToItem = true;
	    _TMNU_ACT_=action;
	    _TMNU_TAB_=tab;
	    var message = $('#submitTopMenuMessage').html();
	    if ( EDITSECTION != '' ) {
	          message = message.replace('__EDITSECTION__',EDITSECTION);
	        if ( DETAILCOUNT > 0 ) {
	        } else {
	          message = message.replace('__EDITSECTION__','the ' + EDITSECTION + ' section');
	        }
	  		$('#confirmDialogDiv_Content_TOPMENU').html(message);
			$('#confirmDialog_Div_TOPMENU').dialog('open');
			return false;	
	    }
	    if ( goToItem ) {
	    	$('#orderManagementHome').attr('action', action);
	    	$('#orderManagementHome_quickTabSelected').val(tab);
	    	$('#orderManagementHome').submit();
	    }
	 }
	 
	 function submitTopMenuOnConfirmDo() {
	    	$('#orderManagementHome').attr('action', _TMNU_ACT_);
	    	$('#orderManagementHome_quickTabSelected').val(_TMNU_TAB_);
	    	$('#orderManagementHome').submit();
	 }
	 
		//call back from the template
		function confirmDialogGo() {
			doCreateAdjNoPrompt() ;
		}
		
		function showConfirmDialog(message) {

		  	$('#confirmDialogDiv_Content_ADJUST').html(message);
			$('#confirmDialog_Div_ADJUST').dialog('open');
			return false;	
		}
			
		function doCreateAdj(adjustmentAction) {
			_ADJUST_ = adjustmentAction;
			showConfirmDialog("Do you want to create this adjustment?");
			return;
		}
		function doCreateAdjNoPrompt() {
			showProcessingOnHref();
			$('#orderManagementHome').attr('action', _ADJUST_);
			$('#orderManagementHome').submit();
			return;
		}
	 
	</SCRIPT>	
	
