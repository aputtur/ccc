// Scripts to use with 
//    CustomPageControl, customPageControl.ftl and customPageSizeControl.ftl

	function doMultiReSort(formId,pgIndex) {
	    var frm='#' + formId + pgIndex;
	    var act=formId+'!resortSearchResults.action?pagingIndex='+pgIndex;
	    $(frm).attr('action',act);
	    if ( $('#waiting_for_page').html() != '' ) {
	    	showCustomPageMessageDialog($('#waiting_for_page').html());	    	
	    }
	    $(frm).submit();   		
	}

	function doReMultiPageResults(formId,pgIndex) {
	    var frm='#' + formId + pgIndex;
	    var act=formId+'!rePageResults.action';
	    $(frm).attr('action',act);
	    if ( $('#waiting_for_page').html() != '' ) {
	    	showCustomPageMessageDialog($('#waiting_for_page').html());	    	
	    }
	    $(frm).submit();   		
	}
	
	function doRePageResults(formId,objId) {
	    var frm='#' + formId;
	    var objPrefix='#' + formId+'_'+objId;
	    var psc=objPrefix+'_pageSizeChanged';
	    var pg=objPrefix+'_page';
	    var pgz=objPrefix+'_pageSize';
	    var pglz=objPrefix+'_lastPageSize';
	    var npg=frm+'_newPage';
	    var act=formId+'!rePageResults.action';
	    $(frm).attr('action',act);
	    $(psc).val('false');
	    if ( $('#waiting_for_page').html() != '' ) {
		    showCustomPageMessageDialog($('#waiting_for_page').html());	    	
	    }
	    $(frm).submit();   
	}
	
	function doReSizeMultiPageResults(formId,pgIndex) {
	    var frm='#' + formId + pgIndex;
	    var act=formId+'!reSizePageResults.action?pagingIndex='+pgIndex;
	    $(frm).attr('action',act);
	    if ( $('#waiting_for_page').html() != '' ) {
	    	showCustomPageMessageDialog($('#waiting_for_page').html());	    	
	    }
	    $(frm).submit();   		
	}
	   
	function doReSizePageResults(formId,objId) {
	    var frm='#' + formId;
	    var objPrefix='#' + formId+'_'+objId;
	    var psc=objPrefix+'_pageSizeChanged';
	    var pg=objPrefix+'_page';
	    var pgz=objPrefix+'_pageSize';
	    var pglz=objPrefix+'_lastPageSize';
	    var npgs=frm+'_newPageSize';
	    var act=formId+'!reSizePageResults.action';
	    $(frm).attr('action',act);
	    $(psc).val('true');
	    if ( $('#waiting_for_page').html() != '' ) {
	    	showCustomPageMessageDialog($('#waiting_for_page').html());	    	
	    }
	    $(frm).submit();   
	}
	function showCustomPageMessageDialog(message) {
	  	   $('#messageDialogDiv_Content_NB').html(message);
		   $('#messageDialog_Div_NB').dialog('open');
		   return false;	
	}
