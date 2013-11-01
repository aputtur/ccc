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
	-pagingIndex: index of paging section e.g., 0 - n 
	-pagingIterator: name of iterator status field used to control paging index values
	                      
-->

<#assign pgIndex="${parameters.pagingIndex?c}">

<#if parameters.reSortScript?exists>
	<#assign submitForm="${parameters.reSortScript?html}">
<#else>
	<#assign submitForm="doMultiReSort">
</#if>
<#assign submitForm="${submitForm}('${parameters.formId}','${pgIndex}')">

<#assign sortOrderObjId="${parameters.sortOrderProperty?replace('.','_')}">
<#assign sortOrderId="${parameters.formId}${pgIndex}_newSortOrder_${pgIndex}_">
<#assign sortByObjId="${parameters.sortByProperty?replace('.','_')}">
<#assign sortById="${parameters.formId}${pgIndex}_newSortBy_${pgIndex}_">
<#assign sortOrProp="${parameters.sortOrderProperty?replace('${parameters.pagingIterator}','[${pgIndex}]')}">
<#assign sortByProp="${parameters.sortByProperty?replace('${parameters.pagingIterator}','[${pgIndex}]')}">

<#assign sNoClearClass="cctlnoclear">
<#if parameters.noClearClass?exists>
	<#assign sNoClearClass="${parameters.noClearClass?html}">
</#if>

<@s.hidden cssClass="${sNoClearClass}" id="currentSortOrder${pgIndex}" theme="simple" value="%{${sortOrProp}}"/>
<@s.hidden cssClass="${sNoClearClass}" id="currentSortCriteriaBy${pgIndex}" theme="simple" value="%{${sortByProp}}"/>

Sort by:
<span>
	<@s.select cssClass="${sNoClearClass}" theme="simple" list="${parameters.sortSelectList?html}"
		listKey="${parameters.sortListKey?html}" listValue="${parameters.sortListValue?html}" id="sortBySelect${pgIndex}"
		onchange="toggleSortDefaultAndRadioButtons${pgIndex}();return false;"
		name="newSortBy[${pgIndex}]"/>
	&nbsp;
	<span id="sortDefaultText${pgIndex}" style="display: none;">
		<@s.text theme="simple" name="${parameters.defaultText}" />
	</span> 
	<span id="sortRadioButtons${pgIndex}" style="display: inline;">
		<@s.radio cssClass="${sNoClearClass}" theme="simple"
			onchange="toggleSortDefaultAndRadioButtons${pgIndex}();return false;"
			list="{'Ascending','Descending'}" name="newSortOrder[${pgIndex}]"/>
	</span> 
	<span id="sortSubmitButton${pgIndex}" style="display: none;">&nbsp;
		<@s.submit cssClass="${sNoClearClass}" theme="simple" value="Re-sort" onclick="${submitForm};return false;"/>
	</span> 
</span>
	<SCRIPT type="text/javascript">
	
 $(document).ready(function(){
        var qAsc = '#'+'${sortOrderId}'+'Ascending';
        var qDesc = '#'+'${sortOrderId}'+'Descending';
 		$(qAsc).click(function() {
			toggleResortButton${pgIndex}(qAsc);
			});
		$(qDesc).click(function() {
			toggleResortButton${pgIndex}(qDesc);
			});
			
		toggleSortDefaultAndRadioButtons${pgIndex}();
 	
 	});
 	
	function toggleSortDefaultAndRadioButtons${pgIndex}() {
	     var isDef = $('#sortBySelect${pgIndex}').val();
	     if ( isDef == '${parameters.onChangeDefaultSortValue}' ) {
	     	$('#sortRadioButtons${pgIndex}').hide();
	     	$('#sortDefaultText${pgIndex}').show();
	     } else {
	     	$('#sortRadioButtons${pgIndex}').show();
	     	$('#sortDefaultText${pgIndex}').hide();	     
	     }
		 $('#sortSubmitButton${pgIndex}').hide();
		 if ($('#sortBySelect${pgIndex}').val() !=  $('#currentSortCriteriaBy${pgIndex}').val()) {
			$('#sortSubmitButton${pgIndex}').show()  
	     } else {
			if ( isDef != '${parameters.onChangeDefaultSortValue}' ) {
       		   var qAsc = '#'+'${sortOrderId}'+'Ascending';
         	   var qDesc = '#'+'${sortOrderId}'+'Descending';
			   if ( $(qAsc).attr('checked') ) {
			     if ( $(qAsc).val() != $('#currentSortOrder${pgIndex}').val() ) {
				    $('#sortSubmitButton${pgIndex}').show() 
			     } 
			   } else {
			 	   if ( $(qDesc).attr('checked') ) {
				 	  if ( $(qDesc).val() != $('#currentSortOrder${pgIndex}').val() ) {
						   $('#sortSubmitButton${pgIndex}').show() 
				 	  } 			 	   
			       }
			   }
			}				 
	      }
	   }

	function toggleResortButton${pgIndex}(qBtnId) {
		   if ( qBtnId != null ) {
			   $('#sortSubmitButton${pgIndex}').hide();
		   	   if ( $(qBtnId).val() != $('#currentSortOrder${pgIndex}').val() ) {
		   			$('#sortSubmitButton${pgIndex}').show() 
		   	   } 
		   	   if ( $('#sortBySelect${pgIndex}').val() !=  $('#currentSortCriteriaBy${pgIndex}').val() ) {
		   			$('#sortSubmitButton${pgIndex}').show() 
		   	   } else {
			   	   if ( $(qBtnId).val() == $('#currentSortOrder${pgIndex}').val() ) {
			   			$('#sortSubmitButton${pgIndex}').hide() 
			   	   } 
		   	   }
		   }
	}	   
    
    </SCRIPT>
