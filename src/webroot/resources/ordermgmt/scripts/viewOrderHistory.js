var _OOBJ_;
var _OORD_;
var _OBDL_;
var _ODET_;
var _OACT_;
var _OFRM_;
var _TGL_;
var _BASEFORM_='viewOrderHistory';


	function confirmDialogGo() {
		if ( _TGL_ == 'B' ) {
			toggleBundleDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
		}
		if ( _TGL_ == 'O' ) {
		    	toggleOrderDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
		}
		if ( _TGL_ == 'OE' ) {
		        toggleOrderDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
		}
		if ( _TGL_ == 'BE' ) {
		        toggleBundleDetail(_OOBJ_, _OORD_, _OBDL_, _ODET_);
		}
		if ( _TGL_ == 'CD' ) {
		        discardDetailEditNoPrompt(_OORD_,_OBDL_,_ODET_);
		}
		if ( _TGL_ == 'CC' ) {
			cancelCourseEditHeaderNoPrompt(_OBDL_);
		}
		if ( _TGL_ == 'OH' ) {
			discardConfirmationEditHeaderNoPrompt(_OORD_);
		}
		if ( _TGL_ == 'OHS' ) {
			saveConfirmationEditHeaderNoPrompt(_OORD_);
		}
		if ( _TGL_ == 'DEO' ) {
			discardEntireOrderNoPrompt();
		}
		if ( _TGL_ == 'SAD' ) {
			saveAllDetailsNoPrompt();
		}
		if ( _TGL_ == 'S1D' ) {
			saveOneDetailNoPrompt(_ODET_);
		}
		if ( _TGL_ == 'S1B' ) {
			saveOneBundleNoPrompt(_OBDL_);
		}
		if ( _TGL_ == 'C1D' ) {
			submitDetailTopMenuNoPrompt(_OFRM_, _OACT_ );
		}
		if ( _TGL_ == 'U1D' ) {
			submitDetailTopMenuNoPrompt(_OFRM_, _OACT_);
		}
		if ( _TGL_ == 'COD' ) {
			submitOrderTopMenuNoPrompt(_OFRM_, _OORD_, _OACT_);
		}
		if ( _TGL_ == 'OAD' ) {
			showConfirmationAddDetailNoPrompt(_OORD_,_OOBJ_)
		}
		if ( _TGL_ == 'BAD' ) {
			showCourseAddDetailNoPrompt(_OBDL_,_OOBJ_)
		}		
		
	}
	function showProcessingOnHref() {
		showMessageDialogNB($('#waiting_for_search').html());
	    return true;
	}
	
	function toggleSearch() {
		var av = document.getElementById('advancedSearch');
		$('#advancedSearch').toggle();
		if ($('#advancedSearch_href').attr('innerHTML').indexOf('Hide') == -1) {
			$('#advancedSearch_href').attr('innerHTML',
					'Hide Advanced Search...');
		} else {
			$('#advancedSearch_href').attr('innerHTML', 'Advanced Search...');
		}

	}

	function showConfirmationEdit(editing) {
		if (EDITSECTION == '') {
			$('#order_viewheader').hide();
			$('#order_editheader').show();
			$('#order_edit_href').hide();
		    $('#order_show_href').hide();
			$('#order_addDetail_href').hide();
		    $('#order_addProject_href').hide();
			EDITSECTION = editing;
			$("div[id^='pagingDetailsControls']").hide();
		} else {
			showMessageDialog('Already editing ' + EDITSECTION);
		}
	}
	
	function showConfirmationAddDetail(order,hrefurl) {
		if (EDITSECTION == '') {
			_TGL_ = 'OAD';
			_OORD_ = order;
			_OOBJ_ = hrefurl;
			showConfirmDialog('Add Detail to Confirmation #'+order+'?');
		} else {
			showMessageDialog('Already editing ' + EDITSECTION);
		}
	}

	function showConfirmationAddDetailNoPrompt(order,hrefurl) {
		window.location=hrefurl;
	}
	
	/* Not used currenly -- leaving it here, since might be decided later to allow that
	function showConfirmationAddProject(editing) {
		if (EDITSECTION == '') {
			return true;
		} else {
			showMessageDialog('Already editing ' + EDITSECTION);
			return false;
		}
	}
	*/

	function showMessageDialog(message) {
  	   $('#messageDialogDiv_Content').html(message);
	   $('#messageDialog_Div').dialog('open');
	   return false;	
	}

	function showNotImplemented(message) {
		var msg = "";
		if ( message != null ) {
			msg = '<b>' + message + ' not implemented yet</b>';
		} else {
        	msg ='<b>Action not implemented yet</b>';
		}
        showMessageDialog(msg);
	}	
	function discardConfirmationEditHeader(confirm) {
		_TGL_='OH';
		_OORD_ = confirm; 
		var vic = '#changedconfirmation'+confirm;
		var ised = $(vic).val();
		if ( ised != 0 ) {  
		   	showConfirmDialog('Confirmation Header not saved, discard anyway?');
		} else {
			discardConfirmationEditHeaderNoPrompt(confirm)
		}
	}

	function saveConfirmationEditHeader(confirm) {
		   var vir = '#changedconfirmation'+confirm;
		   if ( parseInt($(vir).val()) > 0 ) {
				_TGL_='OHS'; 
				_OORD_ = confirm; 
				showConfirmDialog('Save Confirm Header #' + confirm +'?');
				return;
		   } else {
			   showMessageDialog('No changes to save');
		   }
		
	}
	
	function saveConfirmationEditHeaderNoPrompt(confirm) {
		$('#editConfirmation_' + confirm).submit();
		$('#order_edit_href').show();
		$('#order_show_href').show();
		$('#order_addDetail_href').show();
	    $('#order_addProject_href').show();
		$("div[id^='pagingDetailsControls']").show();
		$('#confirmProcessMessages').hide();
		var vic = '#changedconfirmation'+confirm;
		$(vic).val(0);
        $("input[id^='resetConfirm_']").each(function(i){
	          var vid = $(this).attr('id');
	          var vCN = '#editConfirmation_'+confirm+'_editConfirm'+vid.replace('resetConfirm','');
	          $(vCN).val($(this).val());
        });
//		$('#order_viewheader').show();
//		$('#order_editheader').hide();

		EDITSECTION = '';
	}
	
	function discardConfirmationEditHeaderNoPrompt(confirm) {
		$('#order_viewheader').show();
		$('#order_editheader').hide();
		$('#order_edit_href').show();
		$('#order_show_href').show();
		$('#order_addDetail_href').show();
	    $('#order_addProject_href').show();
		$("div[id^='pagingDetailsControls']").show();
		$('#confirmProcessMessages').hide();
		
		var dfrm = document.getElementById('editConfirmation_'+ confirm);
	    dfrm.reset();
		
		/*
		var vic = '#changedconfirmation'+confirm;
		$(vic).val(0);
        $("input[id^='resetConfirm_']").each(function(i){
	          var vid = $(this).attr('id');
	          var vCN = '#editConfirmation_'+confirm+'_editConfirm'+vid.replace('resetConfirm','');
	          $(vCN).val($(this).val());
        });
        */
		EDITSECTION = '';
	}
	function toggleBundle(course, obj) {
		var showTable = '#order_course_' + course + '_bundlecontent';
		var editdref = '#course_' + course + '_edit_href';
		var objText = obj.innerHTML;
		if (objText.indexOf('Hide') != -1) {
			obj.innerHTML = objText.replace('Hide','Show');
			$(editdref).hide()
		} else {
			obj.innerHTML = objText.replace('Show','Hide');
			$(editdref).show()
		}
		$(showTable).toggle();
	}
	function toggleConfirmation(obj) {
		var showTable = '#order_confirmcontent';
		var editdref = '#order_edit_href';
		var objText = obj.innerHTML;
		if (objText.indexOf('Hide') != -1) {
			obj.innerHTML = objText.replace('Hide','Show');
			$(editdref).hide();
		} else {
			obj.innerHTML = objText.replace('Show','Hide');
			$(editdref).show();
		}
		$(showTable).toggle();
	}
	function showBundle(course) {
		var showTable = '#order_course_' + course + '_bundlecontent';
		$(showTable).show();
	}
	function hideBundle(course) {
		var showTable = '#order_course_' + course + '_bundlecontent';
		$(showTable).hide();
	}
	function showCourseEdit(course, editing) {
		if (EDITSECTION == '') {
			var showTable = '#order_course_' + course + '_viewheader';
			var editTable = '#order_course_' + course + '_editheader';
			var hidehref = '#course_' + course + '_edit_href';
			var showhref = '#course_' + course + '_show_href';
			var addhref = '#course_' + course + '_addDetail_href';
			$(showTable).hide();
			$(editTable).show();
			$(hidehref).hide();
			$(showhref).hide();
			$(addhref).hide();
			EDITSECTION = editing;
			$("div[id^='pagingDetailsControls']").hide();
		} else {
			showMessageDialog('Already editing ' + EDITSECTION);
		}
	}
	

	function showCourseAddDetail(course,name,hrefurl) {
		if (EDITSECTION == '') {
			_TGL_ = 'BAD';
			_OBDL_ = course;
			_OOBJ_ = hrefurl;
			if ( name != null ) {
				showConfirmDialog('Add Detail to '+name+'?');
			} else {
				showConfirmDialog('Add Detail to Project #'+course+'?');
			}
		} else {
			showMessageDialog('Already editing ' + EDITSECTION);
		}
	 }
	 
	function showCourseAddDetailNoPrompt(course,hrefurl) {
		window.location=hrefurl;
	}
	 
	function cancelCourseEditHeader(bundleId, bundleName) {
		_TGL_='CC'; 
		_OBDL_ = bundleId;
		  
		//removed as this is called after the pop-up prompt is confirmed below
	    //cancelCourseEditHeaderNoPrompt(bundleName);
		var vic = '#changedbundle'+bundleId;
		var ised = $(vic).val();
		if ( ised != 0 ) {  
		   	showConfirmDialog(bundleName +' not saved, discard anyway?');
		} else {
			cancelCourseEditHeaderNoPrompt(bundleId);
		}
		     
		return;
	}
	
	function cancelCourseEditHeaderNoPrompt(course) {
		var showTable = '#order_course_' + course + '_viewheader';
		var editTable = '#order_course_' + course + '_editheader';
		var hidehref = '#course_' + course + '_edit_href';
		var showhref = '#course_' + course + '_show_href';
		var addhref = '#course_' + course + '_addDetail_href';
		$(showTable).show();
		$(editTable).hide();
		$(hidehref).show();
		$(showhref).show();
		$(addhref).show();
		EDITSECTION = '';
    	$("div[id^='pagingDetailsControls']").show();
    	
    	//added so edited fields actually get discarded
    	var dfrm = document.getElementById('editOneBundle_'+ course);
	    dfrm.reset();
    	
	}
	
	function toggleHeaderSection() {
		$('#order_expandheader').toggle();
		$('#order_collapseheader').toggle();
	}
	
	function toggleCourseHeaderSection(course) {
	   var exp = '#order_course_'+course+'_expandheader';
	   var clp = '#order_course_'+course+'_collapseheader';
		$(exp).toggle();
		$(clp).toggle();
	}
	
	function toggleBundleHeaderSection(pgIndex) {
		   var exp = "div[id$='_pg_"+pgIndex+"_expandheader']";
		   var clp = "div[id$='_pg_"+pgIndex+"_collapseheader']";
			$(exp).toggle();
			$(clp).toggle();
		}
    function toggleDetailHeaderSection() {
	   var exp = '#order_details_expandheader';
	   var clp = '#order_details_collapseheader';
	   var opg = '#order_details_pagedresults';
	   if ( $(opg).html() == '' ) {
			loadAjaxOrderDetailPage(opg);
	   } else {
			$(exp).toggle();
			$(clp).toggle();
	   }
	}
    function toggleCourseItemsSection(pgIndex) {
		var exp = "div[id$='_pg_"+pgIndex+"_details_expandheader']";
		var clp = "div[id$='_pg_"+pgIndex+"_details_collapseheader']";
		$(exp).toggle();
		$(clp).toggle();
    }
	function toggleSection(order, section) {
		var sectionExpand = '#order_' + order + '_expand' + section;
		var sectionCollapse = '#order_' + order + '_collapse' + section;
		$(sectionExpand).toggle();
		$(sectionCollapse).toggle();
		var sectionId = '#order_' + order + '_' + section;
		var editHref = '#order_' + order + '_' + section + '_href';
		$(editHref).toggle();
		$(sectionId).toggle();
	}
	function toggleCourseSection(course, section) {
		var sectionExpand = '#order_course_' + course + '_expand'
				+ section;
		var sectionCollapse = '#order_course_' + course
				+ '_collapse' + section;
		$(sectionExpand).toggle();
		$(sectionCollapse).toggle();
		var sectionId = '#order_course_' + course + '_' + section;
		var editHref = '#order_course_' + course + '_' + section
				+ '_href';
		$(sectionId).toggle();
		$(editHref).toggle();
	}
	function toggleCourse(order, course, section) {
		var sectionExpand = '#order_' + order + '_course_' + course + '_expand';
		var sectionCollapse = '#order_' + order + '_course_' + course
				+ '_collapse';
		var courseDetails = '#order_' + order + '_course_' + course
				+ '_details';
		$(sectionExpand).toggle();
		$(sectionCollapse).toggle();
		$(courseDetails).toggle();
	}
			
	function toggleBundleScript(obj, order, course, detail) {
		toggleDetail(obj, order, course, detail);
	}

	function toggleOrderScript(obj, order, course, detail) {
		toggleDetail(obj, order, course, detail);
	}
				
	function showConfirmDialog(message) {
	  	$('#confirmDialogDiv_Content').html(message);
		$('#confirmDialog_Div').dialog('open');
		return false;	
	}
	
	function toggleBundleDetail(obj,order, course, detail) {
		if ( obj == undefined ) {
			var fromHref5 = "#SEL" + detail;
			obj=$(fromHref5);
		} 
		var sectionShow = '#showdetail_bundle_' + course + '_order_' + order + '_detail_' + detail;
		var sectionEdit = '#editdetail_bundle_' + course + '_order_' + order + '_detail_' + detail;
	    var obj = document.getElementById(sectionEdit.replace('#',''));
	    if ( $(sectionEdit).html() == '' ) {
			toggleAjaxBundleDetail(obj, order, course, detail);
			return;
		} else {
			$(sectionShow).toggle();
			$(sectionEdit).toggle();
			var toHref = "#SEL" + detail;
			var fromHref6 = '#'+obj.id + detail;
			if ( fromHref6 == toHref ) {
				if ( $(fromHref6).position().top > 100 ) {
					window.scrollTo(0, $(fromHref6).position().top - 5);
				}
			} else {
				if ( $(toHref).position().top > 100 ) {
					window.scrollTo(0, $(toHref).position().top - 5);
				}
			}
		}
	}
	
	function toggleOrderDetail(obj,order, course, detail) {
		if ( obj == undefined ) {
			var fromHref7 = "#SEL" + detail;
 			obj=$(fromHref7);
		} 
		var sectionShow = '#showdetail_order_' + course + '_order_' + order + '_detail_' + detail;
		var sectionEdit = '#editdetail_order_' + course + '_order_' + order + '_detail_' + detail;
	    var obj = document.getElementById(sectionEdit.replace('#',''));
		if ( $(sectionEdit).html() == '' ) {
			toggleAjaxOrderDetail(obj, order, course, detail);
			return;
		} else {			
			$(sectionShow).toggle();
			$(sectionEdit).toggle();
			var toHref = "#SEL" + detail;
			var fromHref8 = '#'+obj.id + detail;
			if ( fromHref8 == toHref ) {
				if ( $(fromHref8).position().top > 100 ) {
					window.scrollTo(0, $(fromHref8).position().top - 5);
				}
			} else {
				if ( $(toHref).position().top > 100 ) {
					window.scrollTo(0, $(toHref).position().top - 5);
				}
			}
		}

	}
			
	function collapseAllBundleDetails(bundle) {
        var enumb = jQuery("div[id^='editing_detail_']").length;
		var edit = "div[id^='editdetail_bundle_"+bundle+"']";
		var show = "div[id^='showdetail_bundle_"+bundle+"']";			
		$(show).show();
		$(edit).hide();
	
	}
	
	function expandAllBundleDetails(bundle) {
		var edit = "div[id^='editdetail_bundle_"+bundle+"']";
		var show = "div[id^='showdetail_bundle_"+bundle+"']";			
		$(show).hide();
		$(edit).show();
	}
	function collapseAllOrderDetails(bundle) {
		var edit = "div[id^='editdetail_order_"+bundle+"']";
		var show = "div[id^='showdetail_order_"+bundle+"']";			
		$(show).show();
		$(edit).hide();

	}

	function expandAllOrderDetails(bundle) {
		var edit = "div[id^='editdetail_order_"+bundle+"']";
		var show = "div[id^='showdetail_order_"+bundle+"']";			
		$(show).hide();
		$(edit).show();
	}
	
	function toggleSortDefaultAndRadioButtons(frm,def) {
		
	     var isDef = $('#sortBySelect').val();
	     if ( isDef == def ) {
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
			if ( isDef != def ) {
			   if ( $('#'+frm+'_searchCriteria_sortOrderAscending').attr('checked') ) {
			     if ( $('#'+frm+'_searchCriteria_sortOrderAscending').val() != $('#currentSortOrder').val() ) {
				    $('#sortSubmitButton').show() 
			     } 
			   } else {
			 	   if ( $('#'+frm+'_searchCriteria_sortOrderDescending').attr('checked') ) {
				 	  if ( $('#'+frm+'_searchCriteria_sortOrderDescending').val() != $('#currentSortOrder').val() ) {
						   $('#sortSubmitButton').show() 
				 	  } 			 	   
			       }
			   }
			}				 
	      }
	}
	function doReSort() {
		    var bf = '#'+_BASEFORM_;
		    $(bf).attr('action',_BASEFORM_+'!resortSearchResults.action');
		    $(bf).submit();   
	}

	function discardDetailEdit(qobj,order,bundle,detail) {
        var showDetail = '#showing_detail_'+bundle+'_order_'+order+'_detail_'+detail;
        var editDetail = '#editing_detail_'+bundle+'_order_'+order+'_detail_'+detail;
		if ( alertEditingOnClose(detail) ) {
		     _TGL_='CD'; 
		     _OOBJ_=qobj.parent().parent().parent().parent().get(); 
		     _OORD_=order; _OBDL_ = bundle; _ODET_=detail;
		     showConfirmDialog('Detail #' + detail +' not saved, discard anyway?');
		     return;
		} else {
			discardDetailEditNoPrompt(order,bundle,detail);
		}
	}
	function discardDetailEditNoPrompt(order,bundle,detail) {
        var showDetail = '#showing_detail_'+bundle+'_order_'+order+'_detail_'+detail;
        var editDetail = '#editing_detail_'+bundle+'_order_'+order+'_detail_'+detail;
        //TODO Add function to clear any entered/changed data
        if ( jQuery(editDetail).not(':hidden') ) {
            $(editDetail).hide();    
            $(showDetail).show();           
    	    jQuery('#editdetailhref_'+detail).removeClass('editingDetailAngleTabs');
   	        jQuery('#editdetailhref_'+detail).addClass('editDetailAngleTabs');
			jQuery('#editdetailhref_'+detail).show();	            				
  	        var dfrm = document.getElementById('savedetailform_'+detail);
  	        dfrm.reset();
  	        resetOneDetailFromSaveAllForm(detail);
 	    }
        setEditDetailAndCount(); 
        
	}
	
	function findRegexValue  ( regexString, searchString, ignoreCase, position ) {
	    var useMultiLineSearch="m";
	    var foundValue=null;
	    var regexInstance;
	    if ( ignoreCase == "i" ) { 
	      regexInstance = new RegExp(regexString, ignoreCase + useMultiLineSearch);
	    } else {
	      regexInstance = new RegExp(regexString, useMultiLineSearch);
	    }
	    var matchAttempt = regexInstance.exec( searchString );
	    if ( matchAttempt != null ) {
	        foundValue = matchAttempt[position];
	    }
	    return foundValue;
	}	
	function editEntireOrder() {
	    if ( EDITSECTION != '' ) {
	       if ( EDITSECTION.indexOf('detail') == -1 ) {
	           showMessageDialog('Already editing ' + EDITSECTION);
	           return;
	       }
	    }
		var edit = "div[id^='editdetail_']";
		var show = "div[id^='showdetail_']";			
        var showDetail = "div[id^='showing_detail_']";
        var editDetail = "div[id^='editing_detail_']";
 		$(show).hide();
		$(edit).show();
		$(showDetail).hide();
		$(editDetail).show();
		
	    setEditDetailAndCount();
	    jQuery("a[id^='editdetailhref_']").removeClass('editDetailAngleTabs');
	    jQuery("a[id^='editdetailhref_']").addClass('editingDetailAngleTabs');
	    jQuery("a[id^='editdetailhref_']").hide();

	}
	function discardEntireOrder() {
		var edc = 0;
		var edts = "";
		$("input[id^='changeddetail']").each(function(i){
			if ( parseInt($(this).val()) > 0 ) {
				edc += parseInt($(this).val());
				var vDT = findRegexValue('changeddetail([0-9]*$)',$(this).attr('id'),'i',1);
				if ( edts != '') { edts += ', '; }
				edts += vDT;
			}
		});
		if ( edc > 0 ) {
			_TGL_='DEO'; 
			showConfirmDialog('Edited details ' + edts +' not saved, discard anyway?');
			return;
		} else {
			discardEntireOrderNoPrompt() ;
		}
	}
	
	function discardEntireOrderNoPrompt() {
		var edit = "div[id^='editdetail_']";
		var show = "div[id^='showdetail_']";			
        var showDetail = "div[id^='showing_detail_']";
        var editDetail = "div[id^='editing_detail_']";
		$(showDetail).show();
		$(editDetail).hide();
	    setEditDetailAndCount();
	    jQuery("a[id^='editdetailhref_']").removeClass('editingDetailAngleTabs');
	    jQuery("a[id^='editdetailhref_']").addClass('editDetailAngleTabs');  
	    jQuery("a[id^='editdetailhref_']").show();  

		jQuery("form[id^='savedetailform_']").each(function(i){
		   var vid = $(this).attr('id');
  	       var dfrm = document.getElementById(vid);
  	       dfrm.reset();
		});
		resetChangesFromSaveAllForm();
		setEditDetailAndCount(); 
	}
	
	function alertEditingOnClose(detail) {
		var vDetails = [];
        var editDetail = "div[id^='editing_detail_']";
        jQuery(editDetail).not(':hidden').each(function(i){
			var vid = $(this).attr('id');
			var vlu = findRegexValue('.*_detail_([0-9]*$)',vid,'i',1);
			if ( vlu == detail ) {
				vDetails[i]=vlu;
			}
		});
		if ( vDetails.length > 0 ) {
		   var vir = '#changeddetail'+detail;
		   if ( parseInt($(vir).val()) > 0 ) {
		   		return true;
		   }
		} 
		return false;
	}

	function setEditDetailAndCount() {
		var vDetails = [];
        var editDetail = "div[id^='editing_detail_']";
        jQuery(editDetail).not(':hidden').each(function(i){
			var vid = $(this).attr('id');
			var vlu = findRegexValue('.*_detail_([0-9]*$)',vid,'i',1);
			vDetails[i]=vlu;
		});
		if ( vDetails.length == 0 ) {
			EDITSECTION = '';
		} else if ( vDetails.length == 1 ) {
			EDITSECTION = 'detail ' + vDetails[0];
		} else {
			EDITSECTION = 'details ' + vDetails[0];
			for (var x=1;x<vDetails.length;x++) {
		    	EDITSECTION += ', ' + vDetails[x];
		    }
		}
		DETAILCOUNT = jQuery(editDetail).not(':hidden').length;
        if ( DETAILCOUNT == 1  ) { 
			$("div[id^='showDetailSaveAll_']").hide();
			$("div[id^='showDetailDiscardAll_']").hide();
			jQuery('#viewOrderTopMenuTabs').hide();
			$("li[id^='hideOnEdit_']").hide();
			jQuery('#viewOrderTopMenuButtons').hide();	
        } else if ( DETAILCOUNT > 1  ) { 
			$("div[id^='showDetailSaveAll_']").show();
			$("div[id^='showDetailDiscardAll_']").show();
			jQuery('#viewOrderTopMenuTabs').hide();
			$("li[id^='hideOnEdit_']").hide();
			jQuery('#viewOrderTopMenuButtons').show();	
        } else {
			$("div[id^='showDetailSaveAll_']").hide();
			$("div[id^='showDetailDiscardAll_']").hide();
		    jQuery('#viewOrderTopMenuTabs').show();
		    $("li[id^='hideOnEdit_']").show();
		    jQuery('#viewOrderTopMenuButtons').hide();
        }
        if ( DETAILCOUNT > 0 ) {
        	$("div[id^='pagingDetailsControls']").hide();
			$("li[id^='hideOnEdit_']").hide();
        } else {
        	$("div[id^='pagingDetailsControls']").show();
        	$("li[id^='hideOnEdit_']").show();
        } 
	}

	function showProductDetails(obj,detail) {
		if ( obj != null ) {
			$("div[id^='productDetail_"+detail+"']").hide();
			var oid = '#'+obj.id;
			$('#productDetail_'+detail+'_'+obj.value+'_PCD').show();
		}
	}
	function logFieldChange(fldid) {
		var fid = '#'+fldid;
		var fct = parseInt($(fid).val()) + 1;
		$(fid).val(fct+"");
	}
	
	function addChangeResetEventsToEditForms() {
	        $("form[id^='savedetailform_']").each(function(i){
		          var vid = $(this).attr('id');
		          var vDT = findRegexValue('savedetailform_([0-9]*$)',vid,'i',1);
		          var fch = '.itype'+vDT;
	          	  var vir = 'changeddetail'+vDT;
	              var oCH='function() { logFieldChange(\''+vir+'\');}';
	        	  $(fch).each(function(i){
		        	var iid = $(this).attr('id');
	          		var vob = document.getElementById(iid);
	          		
	          		if (iid.indexOf('typeOfContent') != -1)
	          		{
	          			vob.onchange= function () {logFieldChange(vir); toggleNoOfPages(iid, vDT)}	          			
	          		}
	          		else
	          		{
	          			vob.onchange= function () {logFieldChange(vir);}
	          		}
	        	  });
	        });
	        $("form[id^='editConfirmation_']").each(function(i){
		          var vid = $(this).attr('id');
		          var vCN = findRegexValue('editConfirmation_([0-9]*$)',vid,'i',1);
		          var fch = '.ectype'+vCN;
	          	  var vir = 'changedconfirmation'+vCN;
	              var oCH='function() { logFieldChange(\''+vir+'\');}';
	        	  $(fch).each(function(i){
		        	var iid = $(this).attr('id');
	          		var vob = document.getElementById(iid);
	          		vob.onchange=function () {logFieldChange(vir);}
	        	  });
	        });
	        $("form[id^='editOneBundle_']").each(function(i){
		          var vid = $(this).attr('id');
		          var vCN = findRegexValue('editOneBundle_([0-9]*$)',vid,'i',1);
		          var fch = '.ebtype'+vCN;
	          	  var vir = 'changedbundle'+vCN;
	              var oCH='function() { logFieldChange(\''+vir+'\');}';
	        	  $(fch).each(function(i){
		        	var iid = $(this).attr('id');
	          		var vob = document.getElementById(iid);
	          		vob.onchange=function () {logFieldChange(vir);}
	        	  });
	        });
	        
	        
	}

	/* NOT USED AT THIS TIME
	function displayProductDetailsForExistingProductCodes() {
	    var enumb = jQuery("div[id^='editing_detail_']").length;
	    if ( enumb > 0 ) {
			jQuery("div[id^='productDetail_']").hide();
			jQuery("select[id^='editOrder_editItem_productCd']").each(function(i){		
				if ( $(this).val() != '' ) {
					var vid = $(this).attr('id');
					var vlu = findRegexValue('editOrder_editItem_productCd_([0-9]*$)',vid,'i',1);
					var pcd = '#productDetail_'+vlu+'_'+$(this).val()+'_PCD';
					$(pcd).show();
				}
			});
		}
		if ( enumb == 1 ) { 
		    jQuery('li#entireorder_li').hide();
		}
	}
    */
	function cancelOpenItemDetails(jqfrm,order,action) {
		_TGL_='COD'; 
		_OFRM_=jqfrm; 
		_OORD_=order;
		_OACT_=action;
		showConfirmDialog('Cancel all open details?');
		return;		
    }
    
	function cancelOrderDetail(jqfrm,detail,action) {
		_TGL_='C1D'; 
		_OFRM_=jqfrm; 
		_ODET_=detail;
		_OACT_=action;
		showConfirmDialog('Cancel detail #' + detail +'?');
		return;		
	}

	function submitAdjustment(jqfrm,detail,action) {
		_TGL_='U1D';
		_OFRM_=jqfrm; 
		_ODET_=detail;
		_OACT_=action;
		showConfirmDialog('Adjust detail #' + detail +'?');
		return;		
	}
	
	function unCancelOrderDetail(jqfrm,detail,action) {
		_TGL_='U1D';
		_OFRM_=jqfrm; 
		_ODET_=detail;
		_OACT_=action;
		showConfirmDialog('Uncancel detail #' + detail +'?');
		return;		
	}
	
	function submitDetailTopMenuNoPrompt(jqfrm, action ) {
		showMessageDialogNB($('#waiting_for_search').html());
		$(jqfrm).attr('action',action);
		var jqsub = jqfrm + '_submit';
		$(jqsub).click();
	}

	function submitOrderTopMenuNoPrompt(jqfrm, order, action ) {
		showMessageDialogNB($('#waiting_for_search').html());
		var ordid = jqfrm+'_selectedOrderId';
		$(ordid).val(order);
		var jqsub = jqfrm + '_submit';
		$(jqsub).click();
	}
	
	function saveOneDetail(detail) {
		   var vir = '#changeddetail'+detail;
		   if ( parseInt($(vir).val()) > 0 ) {
				_TGL_='S1D'; 
				_ODET_=detail;
				showConfirmDialog('Save edited detail #' + detail +'?');
				return;
		   } else {
			   showMessageDialog('No changes to save');
		   }
		
	}
	
	function saveOneBundle(bundle, course) {
			var vir = '#changedbundle'+bundle;
		    if ( parseInt($(vir).val()) > 0 ) {
				_TGL_='S1B'; 
				_OBDL_=bundle;
				showConfirmDialog('Save edited course ' + course +'?');
				return;
		   } else {
			   showMessageDialog('No changes to save');
		   }
	}
	
	function saveAllEditedDetails() {
		var edc = 0;
		var edts = "";
		$("input[id^='changeddetail']").each(function(i){
			if ( parseInt($(this).val()) > 0 ) {
				edc += parseInt($(this).val());
				var vDT = findRegexValue('changeddetail([0-9]*$)',$(this).attr('id'),'i',1);
				if ( edts != '') { edts += ', '; }
				edts += vDT;
			}
		});
		if ( edc > 0 ) {
			_TGL_='SAD'; 
			showConfirmDialog('Save edited details ' + edts +'?');
			return;
		} else {
			showMessageDialog('No details chages to save');
		}
	}

	function hideSaveSubmitButtonsAndShowProcessing() {
		$("tr[id^='savesubmitbuttons']").hide();
		showMessageDialogNB($('#waiting_for_search').html());
	}
	
	function showMessageDialogNB(message) {
		$('#messageDialogDiv_Content_NB').html(message);
		$('#messageDialog_Div_NB').dialog('open');
		return false;	
	}
	
	function saveAllDetailsNoPrompt() {
		   copyChangesToSaveAllForm();
		   hideSaveSubmitButtonsAndShowProcessing();
		   $('#saveAllDetails').submit();
	}
	
	function saveOneDetailNoPrompt(detail) {
		   copyOneDetailChangesToSaveAllForm(detail);
		   hideSaveSubmitButtonsAndShowProcessing();
 		   $('#saveAllDetails').submit();
	}

	function saveOneBundleNoPrompt(bundle) {
	//	   copyOneDetailChangesToSaveAllForm(detail);
		   hideSaveSubmitButtonsAndShowProcessing();
		   $('#editOneBundle_' + bundle).submit();
	}
	
	function copyChangesToSaveAllForm() {
		var fstDT = '';
        $("form[id^='savedetailform_']").each(function(i){
	          var vid = $(this).attr('id');
	          var vDT = findRegexValue('savedetailform_([0-9]*$)',vid,'i',1);
	          var fch = '.itype'+vDT;
	          var lch = '.ltype'+vDT;
        	  var vir = '#changeddetail'+vDT;
        	  var iDT = i;
        	  if ( parseInt($(vir).val()) > 0 ) {
    	          if ( fstDT == '' ) { fstDT = vDT; }
            	  //add changes
              	  $(fch).each(function(i){
              		// from ID savedetailform_37778363_editItem_standardNumber
              		// to Id saveAllDetails_editDetails_1__item_standardNumber
      	        	var iid = $(this).attr('id');
      	        	iid = iid.replace('savedetailform_'+vDT,'saveAllDetails');
      	        	iid = iid.replace('editItem_','editDetails_'+iDT+'__item_');
      	        	iid = '#'+iid;
      	        	$(iid).val($(this).val());
            	  });
            	  // get the work ltypes	  
              	  $(lch).each(function(i){
                		// from ID savedetailform_37778363_editItem_standardNumber
                		// to Id saveAllDetails_editDetails_1__item_standardNumber
        	        	var iid = $(this).attr('id');
        	        	iid = iid.replace('savedetailform_'+vDT,'saveAllDetails');
        	        	iid = iid.replace('editItem_','editDetails_'+iDT+'__item_');
        	        	iid = '#'+iid;
        	        	$(iid).val($(this).attr('innerHTML'));
              	  });	  
    	          var ts='#saveAllDetails_editDetails_'+iDT+'__toBeSaved';
      	          $(ts).val('true');
        	  }
         });
         $('#saveAllDetails_selectedDetailNumber').val(fstDT);
	}
	
	function copyOneDetailChangesToSaveAllForm(detail) {
		var fstDT = detail;
        $("form[id^='savedetailform_']").each(function(i){
	          var vid = $(this).attr('id');
	          var vDT = findRegexValue('savedetailform_([0-9]*$)',vid,'i',1);
	          var fch = '.itype'+vDT;
	          var lch = '.ltype'+vDT;
        	  var vir = '#changeddetail'+vDT;
        	  var iDT = i;
        	  if ( parseInt($(vir).val()) > 0 ) {
            	  //add changes
              	  $(fch).each(function(i){
              		// from ID savedetailform_37778363_editItem_standardNumber
              		// to Id saveAllDetails_editDetails_1__item_standardNumber
      	        	var iid = $(this).attr('id');
      	        	iid = iid.replace('savedetailform_'+vDT,'saveAllDetails');
      	        	iid = iid.replace('editItem_','editDetails_'+iDT+'__item_');
      	        	iid = '#'+iid;
      	        	$(iid).val($(this).val());
            	  });
              	  $(lch).each(function(i){
                		// from ID savedetailform_37778363_editItem_standardNumber
                		// to Id saveAllDetails_editDetails_1__item_standardNumber
        	        	var iid = $(this).attr('id');
        	        	iid = iid.replace('savedetailform_'+vDT,'saveAllDetails');
        	        	iid = iid.replace('editItem_','editDetails_'+iDT+'__item_');
        	        	iid = '#'+iid;
        	        	$(iid).val($(this).attr('innerHTML'));
              	  });
            	  if ( vDT == detail ) {	  
                	// if is detail to be saved 
    	          	var ts='#saveAllDetails_editDetails_'+iDT+'__toBeSaved';
      	          	$(ts).val('true');
            	  } else {
                	// if user made changes capture and return the changes
      	          	var rt='#saveAllDetails_editDetails_'+iDT+'__toBeReturned';
      	          	$(rt).val('true');
            	  }
        	  }
         });
         $('#saveAllDetails_selectedDetailNumber').val(fstDT);
	}
	
	function resetChangesFromSaveAllForm() {
       $("form[id^='savedetailform_']").each(function(i){
	          var vid = $(this).attr('id');
	          var vDT = findRegexValue('savedetailform_([0-9]*$)',vid,'i',1);
	          resetDetailEditFromSaveAllForm(vDT, i);
         });
	}
	
	function resetOneDetailFromSaveAllForm(detail) {
	       $("form[id^='savedetailform_']").each(function(i){
		          var vid = $(this).attr('id');
		          var vDT = findRegexValue('savedetailform_([0-9]*$)',vid,'i',1);
		          if ( vDT == detail ) {
		          	resetDetailEditFromSaveAllForm(vDT, i);
		          }
	         });
		}
	function toggleNoOfPages(iid, vDT) {
		var contentTypeSelect = document.getElementById(iid);
		var noOfPages = document.getElementById('savedetailform_'+vDT+'_editItem_numberOfPages');
		if (contentTypeSelect.value == 'SELECTED_PAGES')
		{
			noOfPages.disabled = false;
			noOfPages.className = noOfPages.className + ' required';
		}
		else
		{
			noOfPages.disabled = true;
			noOfPages.className = 'itype'+vDT;
		}

	}	
    function resetDetailEditFromSaveAllForm(detail, i) {
      var fch = '.itype'+detail;
      var lch = '.ltype'+detail;
  	  var vir = '#hasProcessMessageErrors'+detail;
	  var vic = '#changeddetail'+detail;
	  var vem = '#processMessagesEdit'+detail;
	  var vvm = '#processMessagesView'+detail;
  	  var iDT = i;
      $(lch).each(function(i){
   		// from Id saveAllDetails_editDetails_1__item_standardNumber
   		// to ID savedetailform_37778363_editItem_standardNumber
	        	var iid = $(this).attr('id');
	        	iid = iid.replace('savedetailform_'+detail,'saveAllDetails');
	        	iid = iid.replace('editItem_','editDetails_'+iDT+'__item_');
	        	iid = '#'+iid;
	        	$(this).attr('innerHTML',$(iid).val());
 	  });	  
  	  if ( $(vir).val() == 'true' ) {
         $(fch).each(function(i){
        		// from Id saveAllDetails_editDetails_1__item_standardNumber
        		// to ID savedetailform_37778363_editItem_standardNumber
	        	var iid = $(this).attr('id');
	        	iid = iid.replace('savedetailform_'+detail,'saveAllDetails');
	        	iid = iid.replace('editItem_','editDetails_'+iDT+'__item_');
	        	iid = '#'+iid;
	        	$(this).val($(iid).val());
      	  });
	      var ts='#saveAllDetails_editDetails_'+iDT+'__toBeSaved';
	      $(ts).val('false');
	      $(vir).val('false');
	      $(vic).val(0);
	      $(vem).hide();
	      $(vvm).hide();
  	  }
    }



	
    