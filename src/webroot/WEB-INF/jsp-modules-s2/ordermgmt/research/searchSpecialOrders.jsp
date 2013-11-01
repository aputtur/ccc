<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<br /><br />

<div>
<div id="formSearchCriteria"  >

<s:form action="searchSpecialOrders" method="post" namespace="/om/research" cssStyle="padding-left: 10px;">

<s:hidden theme="simple" name="searching" value="false"/>
<s:hidden theme="simple" name="quickTabSelected"/>
    <table class="subTable" >
    <tr>
	     <s:textfield cssClass="odd" cssStyle="width: 90px;" label="Order #" name="searchCriteria.invoiceNumber"></s:textfield>
	     <s:textfield cssClass="odd" cssStyle="width: 140px;" label="Order Detail #" name="searchCriteria.orderNumber"></s:textfield>
	    <s:select cssClass="odd" cssStyle="width: 140px;" list="{'Detail Status Date','Order Date','Start of Term Date'}" headerKey="" headerValue="-- Select One --" label="Date Range" name="searchCriteria.dateType">
	    	<s:param name="inputcolspan" value="%{2}" /> 
	    </s:select>
	    <s:submit cssClass="odd" value="Search" onclick="doSearch(); return false;"></s:submit>
    </tr>
    <tr>
    	    <s:select cssClass="odd" cssStyle="width: 140px;" list="{'a - allocated','b - banking','c - changing','d - done', 'Not Completely Entered'}" headerKey="" headerValue="-- Select One --"  label="Detail Status" name="searchCriteria.detailStatus"></s:select>
	    <s:select cssClass="odd" cssStyle="width: 140px;" list="{'photo copy','email','reprint'}" headerKey="" headerValue="-- Select One --"  label="Product (TOU) Status" name="searchCriteria.productTOU"></s:select>
	    <s:label></s:label>
	    <s:textfield cssClass="odd" cssStyle="width: 70px;" name="searchCriteria.startDate" label="From" labelposition="right"></s:textfield>
	    <s:label></s:label>
    </tr>
    <tr>
	    <s:select cssClass="odd" cssStyle="width: 140px;" list="{'','user 1','user 2','user 3', 'Essie'}" headerKey="" headerValue="-- Select One --"  label="Assigned To" name="searchCriteria.assignedTo"></s:select>
		<s:label></s:label>
		<s:label></s:label>
		<s:label></s:label>
		<s:textfield cssClass="odd" cssStyle="width: 70px;" name="searchCriteria.endDate" label="To" labelposition="right"></s:textfield>
	    <s:label></s:label>
    </tr>
     </table>     

     <table><tr><td align="left">
     <b><a id="advancedSearch_href" href="#" onclick="advancedSearchShowHide(this);return false;">Show Expanded Search</a></b>
     </td></tr></table>
     <br/>
<table class="subTable" id="advSearchTable" style="display: none;">
     <tr>
	     <s:textfield cssClass="odd" cssStyle="width: 350px;" label="Publication Name" name="searchCriteria.publicationName">
	    	<s:param name="inputcolspan" value="%{3}" />
	     </s:textfield>
	    	<s:textfield cssClass="odd" cssStyle="width: 350px;" label="Title" name="searchCriteria.title">
	    	<s:param name="inputcolspan" value="%{2}" />
	    </s:textfield>
	</tr>
	<tr>
		<s:textfield cssClass="odd" cssStyle="width: 350px;" label="RightsHolder Name" name="searchCriteria.rightsholderName">
    		<s:param name="inputcolspan" value="%{3}" />
    	</s:textfield>
 	    <s:textfield cssClass="odd" cssStyle="width: 350px;" label="Institution" name="searchCriteria.institution">
	    	<s:param name="inputcolspan" value="%{2}" />
	    </s:textfield>
	    
	    <s:label></s:label>
	</tr>
	<tr>	
				<s:textfield cssClass="odd" cssStyle="width: 120px" label="TF Work ID" name="searchCriteria.tfWorkID">
		</s:textfield>
		<s:textfield cssClass="odd" cssStyle="width: 120px;" label="WR Work ID" name="searchCriteria.wrWorkID">
		</s:textfield>	
    	<s:textfield cssClass="odd" cssStyle="width: 350px;" label="Course Name" name="searchCriteria.courseName">
    		<s:param name="inputcolspan" value="%{2}" />
    	</s:textfield>
     </tr>
     <tr>	
	<s:textfield cssClass="odd" cssStyle="width: 120px" label="Standard Number" name="searchCriteria.standardNumber">
		</s:textfield>
	 </tr>
     </table>     

</s:form>
</div>
     

<!-- <s:radio list="{'Ascending', 'Descending'}" name="Ascending"></s:radio> -->


<div style="border: 1px solid #CDCDCD; margin: 10px;" id="formResults" >



</div>

</div>

<script type="text/javascript">

 $(document).ready(function(){
	 
	 	$("#searchSpecialOrders_searchCriteria_startDate").datepicker( {
		    showAnim : 'fadeIn',
			showOn: 'button',
			buttonImage: '<s:url value="/resources/ordermgmt/images/calendar.gif"/>',
			buttonImageOnly: true,
			buttonText: 'Calendar...'		});
	 	$("#searchSpecialOrders_searchCriteria_endDate").datepicker( {
		    showAnim : 'fadeIn',
			showOn: 'button',
			buttonImage: '<s:url value="/resources/ordermgmt/images/calendar.gif"/>',
			buttonImageOnly: true,
			buttonText: 'Calendar...'		});
     
    if (<s:property value="showCriteria"/>) {
		$('#formSearchCriteria').show();
		$('#returnToSearch_href').hide();
	}
	else {
		$('#formSearchCriteria').hide();
		$('#returnToSearch_href').show();
	}
	if (<s:property value="showResults"/>)
		$('#formResults').show();
	else
		$('#formResults').hide();

	if ('<s:property value="quickTabSelected" />' == "ordermgmt.menu.research.my") {
		$('#searchSpecialOrders_searchCriteria_assignedTo').attr('disabled', 'disabled');
		$('#searchSpecialOrders_searchCriteria_detailStatus').attr('disabled', 'disabled');
	}
	if ('<s:property value="quickTabSelected" />' == "ordermgmt.menu.research.unassigned") {
		$('#searchSpecialOrders_searchCriteria_assignedTo').attr('disabled', 'disabled');
	}

	if (<s:property value="searchCriteria.advCriteriaEmpty" />) {
		$('#advSearchTable').hide();
	}
	else {
		alert("will show adv");
		$('#advSearchTable').show();
	}

});

	function showNotImplemented(message) {
		var msg = "";
		if ( message != null ) {
			msg = '<b>' + message + ' not implemented yet</b>';
		} else {
        	msg ='<b>Action not implemented yet</b>';
		}
        showMessageDialog(msg);
	}
		
	function showMessageDialog(message) {
	  	   $('#messageDialogDiv_Content').html(message);
		   $('#messageDialog_Div').dialog('open');
		   return false;	
		}

 	function returnToSearch() {
	   //hide search results, show serach criteria
		//$('#divSearchResults').hide();
		$('#formSearchCriteria').show();
		$('#returnToSearch_href').hide();
 	}

   function advancedSearchShowHide(ctrl) {
   	if (ctrl.innerHTML == 'Show Expanded Search') {
   		document.getElementById('advSearchTable').style.display = '';
   		ctrl.innerHTML = 'Hide Expanded Search';
   	}
   	else if (ctrl.innerHTML == 'Hide Expanded Search') {
   		document.getElementById('advSearchTable').style.display = 'none';
   		ctrl.innerHTML = 'Show Expanded Search';
   	}
   	else {}
   }


	function toggleDetail(order, course, detail) {
		var sectionExpand = '#order_' + order + '_course_' + course + '_detail_' + detail + '_expand';
		var sectionCollapse = '#order_' + order + '_course_' + course
				+  '_detail_' + detail + '_collapse';
		var longDetails = '#order_' + order + '_course_' + course
				+ '_detail_' + detail + '_long';
		$(sectionExpand).toggle();
		$(sectionCollapse).toggle();
		$(longDetails).toggle();
	}

	
	function collapseAll() {
		$("table[id$='_details']").hide();
		$("table[id$='_contents']").hide();
		$("div[id$='_details']").hide();
		$("div[id$='_contents']").hide();
		$("div[id$='_long']").hide();
		$("div[id$='_collapse']").hide();
		$("div[id$='_expand']").show();
	
		}
	
		function expandAll() {
			$("table[id^='order_']").show();
			$("div[id^='order_']").show();
			$("div[id$='_collapse']").show();
			$("div[id$='_expand']").hide();
		}

		function doSearch() {
			showNotImplemented('Special Order Search');
		    //$('#searchSpecialOrders_searching').val(true);
	        //$('#searchSpecialOrders').submit();   
		}
       
</script>