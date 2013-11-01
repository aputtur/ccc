<#--
Uses: popupSearchWorks.ftl
    
Fields: (none)
    -searchFormId: search form - defaults to searchWorks
    -searchObject: search object for form fields - defaults to searchCriteria
    -searchTitle: popup title - default Search Works
    -searchMsgsId: id of processMessages section - defaults to searchWorksProcessMessagesView
    -includeSelectButton: true/false - default false
    -workFieldClass: class to identify work fields to be updated - default ltype
    -workDataObject: id of object containing work properties - default editItem
    -workFormPrefix: prefix of id of work properties - default savedetailform
    -workSetField: what to set when applying work fields either innerHTML or value - default innerHTML
    -workSetFieldCallback: script to be called when applying fields - default none

-->
<#assign sFormId="#searchWorks">
<#if parameters.searchFormId?exists>
	<#assign sFormId="#${parameters.searchFormId?html}">
</#if>
<#assign sPopTitle="Select Work">
<#if parameters.searchTitle?exists>
	<#assign sPopTitle="${parameters.searchTitle?html}">
</#if>
<#assign sMsgsId="#searchWorksProcessMessagesView">
<#if parameters.searchMsgsId?exists>
	<#assign sMsgsId="#${parameters.searchMsgsId?html}">
</#if>

<#assign sObjId="searchCriteria">
<#if parameters.searchObject?exists>
	<#assign sObjId="${parameters.searchObject?html}">
</#if>
<#assign sUseSelect="false">
<#if parameters.includeSelectButton?exists>
	<#assign sUseSelect="${parameters.includeSelectButton?html}">
</#if>

<#assign sWrkObjId="editItem">
<#if parameters.workDataObject?exists>
	<#assign sWrkObjId="${parameters.workDataObject?html}">
</#if>
<#assign sWrkPrefix="savedetailform">
<#if parameters.workFormPrefix?exists>
	<#assign sWrkPrefix="${parameters.workFormPrefix?html}">
</#if>
<#assign sWrkSetType="innerHTML">
<#if parameters.workSetField?exists>
	<#assign sWrkSetType="${parameters.workSetField?html}">
</#if>
<#assign sWrkFldClass="ltype">
<#if parameters.workFieldClass?exists>
	<#assign sWrkFldClass="${parameters.workFieldClass?html}">
</#if>
<#assign sWrkSetFldCall="">
<#if parameters.workSetFieldCallback?exists>
	<#assign sWrkSetFldCall="${parameters.workSetFieldCallback?html}">
</#if>

<div id="popUpSearchWorksContent_Div" title="">
	<div id="popUpSearchWorksContentDiv_Content"></div>
</div>	

<div style="display: none;" id="popUpSearchWorksApplyToId"></div>

<script  type="text/javascript">
	var popUpSearchWorks_Content_url_base='<@s.url action="searchWorks" namespace="/om/remote"/>';
	var popUpSearchWorks_Content_url=popUpSearchWorks_Content_url_base;
	
	$(document).ready(function(){	
		$('#popUpSearchWorksContent_Div').dialog({
	           autoOpen: false,
	           modal: true,
			   resizable: true,
			   width: 820,height:350,
			   open: function() {
			        $("#popUpSearchWorksContentDiv_Content").load(popUpSearchWorks_Content_url);
			        setTimeout('waitForLoad()','50');
			   },
			   buttons: { "Exit": function() {$('#popUpSearchWorksContent_Div').dialog('close'); }
			            <#if sUseSelect = "true">
			             ,"Select": function() {applySearchWorkSelection(0);$('#popUpSearchWorksContent_Div').dialog('close'); }
			            </#if>
			            },
			   close: function() { }
        });
	        
	});
	function waitForLoad() {
		 var ckdnar = $("#popUpSearchWorksContentDiv_Content").html();
		 if ( ckdnar == 'Loading...' ) {
		     setTimeout('waitForLoad()','50'); 
		 } else {
      	     if ( ckdnar.indexOf('_@ERROR@_') != -1 ) {
       		     adjustButtonsForERROR();
       	     } else {
       	         resetButtonsInCaseOfERROR();
       	     }
		 }
	     
	}
	function applySearchWorkSelection(workIndex) {
	   var vDT = jQuery.trim($('#popUpSearchWorksApplyToId').attr('innerHTML'));
	   var lch = '.${sWrkFldClass}'+vDT;
       var vir = '#changeddetail'+vDT;
       var vn = $(vir).val() + 1;
       $(vir).val(vn);
       $(lch).each(function(i){
           	// from Id wrfrom_0_standardNumber
       		// to ID savedetailform_37778363_editItem_standardNumber
          	var iid = $(this).attr('id');
           	iid = iid.replace('${sWrkPrefix}_'+vDT+'_${sWrkObjId}','wrfrom_'+workIndex);
           	iid = '#'+iid;
           	if ( '${sWrkSetType}' == 'innerHTML' ) {
           		$(this).attr('innerHTML', $(iid).attr('innerHTML') );
           	}
           	if ( '${sWrkSetType}' == 'value' ) {
           		$(this).val($(iid).attr('innerHTML'));
           	}
       });	  
       <#if sWrkSetFldCall != "">
       ${sWrkSetFldCall};
       </#if>
       
	}		        
	
	function clearSearchWorksFields() {
	    var objPrefix='${sFormId}'+'_'+'${sObjId}_';
	    var fld1 = objPrefix + 'idnoNumber';
	    var fld2 = objPrefix + 'title';
	    var fld3 = objPrefix + 'wrWorkId';
	    var fld4 = objPrefix + 'tfWorkId';
	    $(fld1).val('');
	    $(fld2).val('');
	    $(fld3).val('');
	    $(fld4).val('');
		$('${sMsgsId}').hide();
	}
	
	function doWorksSearch() {
	    var schflg = '${sFormId}'+'_searching';
	    $(schflg).val(true);
	    loadAjaxWorkSearch();
	}
	function popUpSearchWorksPageContent(detailId, tfjqid){
	    var tfid = '#'+tfjqid;
	    var loadparms='?searching=true&${sObjId}.tfWorkId='+jQuery.trim($(tfid).val());
	    popUpSearchWorks_Content_url = popUpSearchWorks_Content_url_base.replace('.action','!searchAgain.action') + loadparms;
		$("#popUpSearchWorksContentDiv_Content").html("Loading...");
		$('#popUpSearchWorksContent_Div').dialog(
		     'option', {buttons :
		      { "Exit": function() {$('#popUpSearchWorksContent_Div').dialog('close'); }
			     <#if sUseSelect = "true">
			    ,"Select": function() {applySearchWorkSelection(0);$('#popUpSearchWorksContent_Div').dialog('close'); }
			     </#if>
		       },
		       width: 820, height: 350 }
		);
 
		$('#popUpSearchWorksContent_Div').dialog('open');
		$('#ui-dialog-title-popUpSearchWorksContent_Div').html('${sPopTitle}');
		$('#popUpSearchWorksApplyToId').attr('innerHTML',detailId);
	}
	
	function adjustButtonsForERROR() {
		$('#popUpSearchWorksContent_Div').dialog(
          	 'option'
       	   , 'buttons'
       	   , {"Exit": function() {$('#popUpSearchWorksContent_Div').dialog('close'); }
       	 });
    }
		
	function resetButtonsInCaseOfERROR() {
		$('#popUpSearchWorksContent_Div').dialog(
          	 'option'
       	   , 'buttons'
       	   , {"Exit": function() {$('#popUpSearchWorksContent_Div').dialog('close'); }
 			 <#if sUseSelect = "true">
			 ,"Select": function() {applySearchWorkSelection(0);$('#popUpSearchWorksContent_Div').dialog('close'); }
	      </#if>
       	 });
    }
    
	function loadAjaxWorkSearch() {
		
	   var nurl = popUpSearchWorks_Content_url_base.replace('.action','!searchAgain.action');
	   nurl+='?'+$('${sFormId}').serialize();
	   $.ajax({
	   		type: "POST",
	   		url: nurl,
	   		success: function(data){
				$('#popUpSearchWorksContentDiv_Content').html(data);
       			if ( data.indexOf('_@ERROR@_') != -1 ) { 
       				adjustButtonsForERROR();
       			} else {
       			    resetButtonsInCaseOfERROR();
       			}
	   		}
		});
	   
	}

	function doReSizePageSearchWorkResults(formId,objId) {
	    // called by customPageSize
	    var frm='#' + formId;
	    var objPrefix='#' + formId+'_'+objId;
	    var psc=objPrefix+'_pageSizeChanged';
	    var act=popUpSearchWorks_Content_url_base.replace('.action','!reSizePageResults.action');
	    $(psc).val('true');
	    var nurl = act+'?'+$(frm).serialize();  
		$.ajax({
	   		type: "POST",
	   		url: nurl,
	   		success: function(data){
				$('#popUpSearchWorksContentDiv_Content').html(data);
	   		}
		});
	}
	function doAjaxRePageSearchWorksResults(formId,objId) {
	    var frm='#' + formId;
	    var objPrefix='#' + formId+'_'+objId;
	    var psc=objPrefix+'_pageSizeChanged';
	    var act=popUpSearchWorks_Content_url_base.replace('.action','!rePageResults.action');
	    $(psc).val('false');
	    var nurl = act+'?'+$(frm).serialize();  
		$.ajax({
	   		type: "POST",
	   		url: nurl,
	   		success: function(data){
				$('#popUpSearchWorksContentDiv_Content').html(data);
	   		}
		});
	}
		        
</script>