<#--
Uses: customSortControl.ftl
	
Fields:
	-sortSelectList: list object of sort field choices
	-sortListKey: listKey property
	-sortListValue: listValue property
	-formId: sort form id
	-onChangeDefaultSortValue: e.g., SORT_BY_DEFAULT
	-sortByProperty: name property
	-sortOrderProperty: name property
	-defaultText: either text or text key
	-reSortScript: javascript to carryout page change - default is doReSort
	             - will be passed formId and sortByProperty values as strings
	-noClearClass: class to be added to form fields not to be cleared on form reset default is cctlnoclear
	                     
-->

<#if parameters.reSortScript?exists>
	<#assign submitForm="${parameters.reSortScript?html}">
<#else>
	<#assign submitForm="doReSort">
</#if>
<#assign submitForm="${submitForm}()">

<#assign sortOrderObjId="${parameters.sortOrderProperty?replace('.','_')}">
<#assign sortOrderId="${parameters.formId}_${sortOrderObjId}">
<#assign sortByObjId="${parameters.sortByProperty?replace('.','_')}">
<#assign sortById="${parameters.formId}_${sortByObjId}">
<#assign sNoClearClass="cctlnoclear">
<#if parameters.noClearClass?exists>
	<#assign sNoClearClass="${parameters.noClearClass?html}">
</#if>

<@s.hidden cssClass="${sNoClearClass}" id="currentSortOrder" theme="simple" value="%{${parameters.sortOrderProperty}}"/>
<@s.hidden cssClass="${sNoClearClass}" id="currentSortCriteriaBy" theme="simple" value="%{${parameters.sortByProperty}}"/>

Sort by:
<span>
	<@s.select cssClass="${sNoClearClass}" theme="simple" list="${parameters.sortSelectList?html}"
		listKey="${parameters.sortListKey?html}" listValue="${parameters.sortListValue?html}" id="sortBySelect"
		onchange="toggleSortDefaultAndRadioButtons('${parameters.formId}','${parameters.onChangeDefaultSortValue}');return false;"
		name="${parameters.sortByProperty}"/>
	&nbsp;
	<span id="sortDefaultText" style="display: none;">
		<@s.text name="${parameters.defaultText}" />
	</span> 
	<span id="sortRadioButtons" style="display: inline;">
		<@s.radio cssClass="${sNoClearClass}" theme="simple"
			onchange="toggleSortDefaultAndRadioButtons('${parameters.formId}','${parameters.onChangeDefaultSortValue}');return false;"
			list="{'Ascending','Descending'}" name="${parameters.sortOrderProperty}"/>
	</span> 
	<span id="sortSubmitButton" style="display: none;">&nbsp;
		<@s.submit cssClass="${sNoClearClass}" theme="simple" value="Re-sort" onclick="${submitForm};return false;"/>
	</span> 
</span>
	<SCRIPT type="text/javascript">
	
 $(document).ready(function(){
 	    
        var qAsc = '#'+'${sortOrderId}'+'Ascending';
        var qDesc = '#'+'${sortOrderId}'+'Descending';
		$(qAsc).click(function() {
			toggleResortButton(qAsc);
			});
		$(qDesc).click(function() {
			toggleResortButton(qDesc);
			});
			
		toggleSortDefaultAndRadioButtons();
 	
 	});
 	
	function toggleSortDefaultAndRadioButtons() {
	     var isDef = $('#sortBySelect').val();
	     if ( isDef == '${parameters.onChangeDefaultSortValue}' ) {
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
			if ( isDef != '${parameters.onChangeDefaultSortValue}' ) {
       		   var qAsc = '#'+'${sortOrderId}'+'Ascending';
        	   var qDesc = '#'+'${sortOrderId}'+'Descending';
			   if ( $(qAsc).attr('checked') ) {
			     if ( $(qAsc).val() != $('#currentSortOrder').val() ) {
				    $('#sortSubmitButton').show() 
			     } 
			   } else {
			 	   if ( $(qDesc).attr('checked') ) {
				 	  if ( $(qDesc).val() != $('#currentSortOrder').val() ) {
						   $('#sortSubmitButton').show() 
				 	  } 			 	   
			       }
			   }
			}				 
	      }
	   }

	function toggleResortButton(qBtnId) {
		   if ( qBtnId != null ) {
			   $('#sortSubmitButton').hide();
		   	   if ( $(qBtnId).val() != $('#currentSortOrder').val() ) {
		   			$('#sortSubmitButton').show() 
		   	   } 
		   	   if ( $('#sortBySelect').val() !=  $('#currentSortCriteriaBy').val() ) {
		   			$('#sortSubmitButton').show() 
		   	   } else {
			   	   if ( $(qBtnId).val() == $('#currentSortOrder').val() ) {
			   			$('#sortSubmitButton').hide() 
			   	   } 
		   	   }
		   }
	}	   
    
    </SCRIPT>
