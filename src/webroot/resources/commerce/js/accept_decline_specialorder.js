 	$(document).ready(function(){
 
		/********************************************/
 		// Modal Dialog to notify payment
 		/********************************************/
	    $("#paymentProcessNotifyModal").dialog({ autoOpen: false,
			modal: true,
			maxHeight: 200,
			maxWidth: 600 ,buttons: {
							OK: function() { 
										$(this).dialog("close"); 
										location.reload(true);
									} 
			}
	    });
	    $( "#paymentProcessNotifyModal" ).dialog({
	    open: function(event, ui) { $(this).parent().css('position', 'fixed'); }
	    });
	});
 	
 	/***********************************************************************************
	    * Always Invoice Special order Click Event
	    ***********************************************************************************/
	function alwaysInvoiceSpecialOrder(orderDetailId, itemPrice){
		if(!isUserLoggedIn()){
			return false;
		}
	    
		//alert("In always invoice");
		
		$( "#paymentProcessNotifyModal" ).dialog( "option", "title", "Accept License Confirmation" );
		
		$("#paymentProcessNotify_Content").html("Transaction in process.....");
		//  $("#accept_specialOrder_payment_processing_msg").html("");
      
	   $.ajax({
				type: 'post',
				url: "/viewSpecialOrderPaymentModal.do?operation=alwaysInvoice",
				data: "licenseId="+orderDetailId,
				dataType: "html" ,
				async:true, 
				success: 
				function(data){
		   			//alert("Date: " + data);
					$("#paymentProcessNotify_Content").html(data);
					},
					error:function(xhr, status, errorThrown) {
			            alert("Parse Error.");
			        }


		});	
	   
		   
		    //HOOK_BeforePaymentSubmit();
		   //$("#paymentProcessNotify_Content_Content").html(data);
		   //alert("before open");
		   $("#paymentProcessNotifyModal").dialog("open");
		   PositionModalDialog($("#paymentProcessNotifyModal"),$("#modalDialogPosition"));
	}


    /***********************************************************************************
	    * Accept Special order Click Event
	    ***********************************************************************************/
	function showAcceptSpecialOrder(orderDetailId, itemPrice){
		if(!isUserLoggedIn()){
			return false;
		}
	    $('#acceptSpecialOrderModal').dialog({
			autoOpen: false,
			 modal: true ,
			 autoResize:true,
			width: 700,
			buttons: {
	    	'Submit': function() {
					submitAcceptLicense();
				return false;
				},
				Cancel: function() {
					var btnState=$('.ui-dialog-buttonpane button:contains(Submit)').css("display");
						if($.trim(btnState) =='none'){
								$('#cancelButton').click();
							}
							else{
								$("#acceptSpecialOrderModal").dialog("close");
							}
					}
				}
		});
	    
         // bind beforeclose event
	    $( "#acceptSpecialOrderModal").bind( "dialogbeforeclose", function(event, ui) {
	    	$(this).dialog("destroy");
	    	});
	    
	    $( "#acceptSpecialOrderModal" ).dialog( "option", "title", "Accept License <label style='font-weight:normal'>(Order Detail ID: "+ orderDetailId +")</label><label style='padding-left:250px;'>Item Price: $"+itemPrice+"</label>" );
	    
	    //set foucus to submit buttons;
	    $('.ui-dialog-buttonpane button:contains(Submit)').focus();

		  $("#acceptSpecialOrderModal_Content").html("Loading.....");
		  $("#accept_specialOrder_payment_processing_msg").html("");
	   $.ajax({
				type: 'post',
				url: "/viewSpecialOrderPaymentModal.do",
				data: "licenseId="+orderDetailId,
				dataType: "html" ,
				async:true, 
				success: 
				function(data){
					$("#acceptSpecialOrderModal_Content").html(data);
					},
					error:function(xhr, status, errorThrown) {
			            alert("Parse Error.");
			        }


		});	
	   
		   $("#acceptSpecialOrderModal").dialog("open");
		   PositionModalDialog($("#acceptSpecialOrderModal"),$("#modalDialogPosition"));
	}
 	

   // Call Paymentscript.jsp aubmitPayment
	function submitAcceptLicense(){
		$( "#paymentProcessNotifyModal" ).dialog( "option", "title", "Accept License Confirmation" );
		submitPayment();
	}
	 

    /***********************************************************************************
	    * Decline Special order Click Event
	    ***********************************************************************************/
     // show decline modal dialog box  	
 	  function showDeclineSpecialOrder(orderDetailId,itemPrice){
 	 	//setter
 			/********************************************/
 	 		// Modal Dialog init for decline special order
 	 		/********************************************/
 		    $("#declineSpecialOrderModal").dialog({ autoOpen: false,
 			    									modal: true,
 			    									autoResize:true,
 			    									height:355,
 			    									width:400,
 			    										buttons: {
														 		    	'Submit': function() {
																			submitDeclineSpecialOrder();
																	return false;
																	},
 			    													Cancel: function() { 
 			    																$(this).dialog("close"); 
 			    															} 
 			    												
 			    									}
 		    });
 	 	$( "#declineSpecialOrderModal" ).dialog( "option", "title", "Decline License <label style='font-weight:normal'>(Order Detail ID: "+ orderDetailId +")</label>" );
        // bind beforeclose event
	    $( "#declineSpecialOrderModal").bind( "dialogbeforeclose", function(event, ui) {
	    	$(this).dialog("destroy");
	    	});
	    	$("#selectedSpecialOrderDetailId").val(orderDetailId);
	    	$("#selectedItemPrice").val(itemPrice);
		    //set foucus to submit buttons;
		    $('.ui-dialog-buttonpane button:contains(Submit)').focus();
	    	$("#declineSpecialOrderModal").dialog("open");
	  }
 	 
 	 
	  function submitDeclineSpecialOrder(){
		  var isSelected=false;
		 $("#declineSpecialOrderModal .declineSpOrderClass:checked").each(function(){
			 isSelected=true;
				$("#selectedDeclineReason").val($(this).val());
				  //$('#orderDetailActionForm').submit();
			});

			// in case not selected
			if(!isSelected){alert("please select atleast one reason.");return false;}
			var licenseId = $("#selectedSpecialOrderDetailId").val() ;
			var declineReason = $("#selectedDeclineReason").val() ;
			
			if(confirm('Are you sure you want to decline this item?')) {
				$( "#paymentProcessNotifyModal" ).dialog( "option", "title", "Decline License Confirmation" );
					var options = {
							target: '#paymentProcessNotify_Content',
							success: function() {
							$('#declineSpecialOrderModal').dialog("close");
							$('#paymentProcessNotifyModal').dialog("open");
							PositionModalDialog($("#paymentProcessNotifyModal"),$("#modalDialogPosition"));
							} 
					}
					$('#paymentProcessNotify_Content').html("Loading..");
					$('#licenseDeclineFrm').ajaxSubmit(options);
			}
	  }
	 
	  function PositionModalDialog(dilaogObject, referenceObject) {
		    
		    var myDialogX = $(referenceObject).position().left;
		    var myDialogY = $(referenceObject).position().top + $(referenceObject).outerHeight();
		    $(dilaogObject).dialog('option', 'position', [myDialogX, myDialogY]);
		}

	  
	  //** Implementing bubbled event from paymentscript.js
	   //TO Do display message if user  has not logged in  	
		function HOOK_ajaxUserIsNotLoggedIn(errormsg){
			  alert("user not logged in");
			  $("#acceptSpecialOrderModal").dialog("close");
			  return false;
		  }	  
		
		function HOOK_IFrameShowEvent(iframeState){
			if(iframeState=='show'){
				 $('.ui-dialog-buttonpane button:contains(Submit)').hide();
			}else{
				$('.ui-dialog-buttonpane button:contains(Submit)').show();
			}
		}
		function HOOK_BeforePaymentSubmit(){
			 $('.ui-dialog-buttonpane button:contains(Submit)').hide();
			 $('.ui-dialog-buttonpane button:contains(Cancel)').hide();
			 $("#accept_specialOrder_payment_processing_msg").html("<strong>Transaction in process.....</strong>")
			
		}
