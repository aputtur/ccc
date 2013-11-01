
/*************** JAVASCRIPT for payment screens************/
function ccPaymentDefaultProps(viewPaymentActionName,windowType,ajaxSubmitTarget,ajaxSubmitTargetWindowId,MainModalWindowId) {
	this.viewPaymentActionName=viewPaymentActionName;
	this.windowType=windowType;
	this.ajaxSubmitTarget=ajaxSubmitTarget;
	this.ajaxSubmitTargetWindowId=ajaxSubmitTargetWindowId;
	this.MainModalWindowId=MainModalWindowId;
	this.displayRadioButtons=true;
}
var paymentScreenProps=null;


//var paymentActionForm="viewPaymentModal.do";  // or it can be viewPaymentForm.do
//var windowType="Modal"; // Modal==>floating Div  Wnormal==>normal same parent winodw
// override this in your parent
paymentScreenProps=new ccPaymentDefaultProps("viewPaymentForm.do","Normal","paymentProcessNotify_Content","paymentProcessNotifyModal","acceptSpecialOrderModal");


function handleCreditCardTxFailure(){
	$('#IFrameBlock').hide();
	$('#errorSection').html("An error has occurred in processing the Credit Card information. Please confirm the Credit Card data is correct and <a href='manageCreditCards.do'>resend the request</a>. If you continue to experience difficulties please contact Rightslink Customer Service at <br>+1-877/622-5543 (toll free) or <br>+1-978/646-2777 or email at " +
	"<a href='mailto:customercare@copyright.com?subject=Rightslink Change Credit Card Error'>customercare@copyright.com</a>.<br/>");
}
function saveProfileValues(selectedRadio) {
		var prf = selectedRadio.id;
		var frm = 'span[id^='+selectedRadio.id+"]'";
		$(frm).each(function(i){
			var frmid = $(this).attr('id');
			var tov = '#'+frmid.replace(prf,'frm');
			$(tov).val($(this).attr('innerHTML'));
		});
}


// check if credit card is selected
function selectCreditCardFromList() {
	// set default to none
	$('#frm_paymentProfileId').val("NONE");
	if($(".selectable:checked").length == 0){// is no radio button then set to none
		$('#frm_paymentProfileId').val("NONE");
		return false;
	}else{
		$(".selectable:checked").each(function(){
			$(this).click();
			//	 saveProfileValues($(this));
				 return false;
		});	
	}
}

function init(){
	$('#errorSection').html("");
	$('table.striped tbody tr:not([th]):even').css("background-color", "#d0d9de");
	$('#cancelButton').attr("disabled", true);
	$('#cancelButton').css("font-size","14px").css("font-weight","bold");
	$('a.deleteable').bind("click", function(){
		if(paymentScreenProps.windowType=="Modal" && !isUserLoggedIn()){
		return false;
		}
		$('#errorSection').html("");
		toggleFrame('hide');
	//	clearPaymentMethod();
		var profileid = $(this).attr("value");
		if(paymentScreenProps.windowType=="Modal"){
			if (confirm('Are you sure you want to delete this credit card?')) {
				$.ajax({
					type: "POST",
					url:  "/listCreditCards.do?operation=disableCreditCard",
					data: "profileid=" + profileid + "&displayRadioButtons="+paymentScreenProps.displayRadioButtons ,
					success: 
						function(data){								
							getCreditCards();
						}
				});	
	        }
		}else{
			$.prompt('Are you sure you want to delete this credit card?',{
	      		buttons:{Ok:true,Cancel:false},
	      		prefix:'cleanblue',
				callback: function(data){
					if(data){
						$.ajax({
							type: "POST",
							url:  "/listCreditCards.do?operation=disableCreditCard",
							data: "profileid=" + profileid + "&displayRadioButtons="+paymentScreenProps.displayRadioButtons ,
							success: 
								function(data){								
									getCreditCards();
								}
						});					
					}
	   			}				
			});
		}
	});
	
	$('.selectable').bind("click", function(){
		if(!isUserLoggedIn()){
			return false;
			}
		$('#errorSection').html("");		
	});		
	
	$('a.modifiable').bind("click", function(){
		if(paymentScreenProps.windowType=="Modal" && !isUserLoggedIn()){
			return false;
			}
	
		
		$('#errorSection').html("");
		var profileid = $(this).attr("value");
		$.ajax({
   			type: "POST",
   			url: "/"+ paymentScreenProps.viewPaymentActionName +"?operation=buildSubscriptionSignature&PaymentFormAction="+paymentScreenProps.viewPaymentActionName,
   			data: "profileid=" + profileid,
   			success: 
				function(data){
					$('#modifySignatureOutput').replaceWith("<div id='modifySignatureOutput'>" + data + "</div>");
					toggleFrame('show', 'update');
   				}
 		});
 		//$("#displayContinue").show();
	});
	$('#addCardButton').css("font-size", "10px").css("font-weight", "bold");
	
	$('#cancelButton').click(function(){
		toggleFrame('hide');
	});
			
	$('#addCardButton').click(function(){
		//implement this in your parent window to do any javascript before calling this event
		if(paymentScreenProps.windowType=="Modal" && !isUserLoggedIn()){
			return false;
			}
		$('#errorSection').html("");
		toggleFrame('show', 'new');
	});	
}




//Call this method to fetch new credit card list
//called directly from delete script above
function getCreditCards(){
	if(paymentScreenProps.windowType=="Modal" && !isUserLoggedIn()){
		return false;
	}
	togglePaymentAndIFrameBlock(false);
	
		$('#cancelButton').attr("disabled", true);
		$.ajax({
 			type: "POST",
 			url: "/listCreditCards.do?operation=retrieveCreditCardList",
 			data: "displayRadioButtons="+paymentScreenProps.displayRadioButtons,
 			success: 
				function(data){		
						$('#ccRows').html(data);
						$("#profileRow1").attr("checked", "checked");
					    $("#profileRow1").click();
						init();	
					}
		});	
		//highlightExpiredCards();			
}

/*
 * Implement HOOK_ajaxUserIsNotLoggedIn in you parent window to display custom messages
 */
function isUserLoggedIn(){

	 $.ajax({
			type: "POST",
			url: "/checkUserLoggedInAJAX.do",
			data: "userid=0",
			async:false, 
			success: 
			function(data){			
					if(jQuery.trim(data)=="false"){

						userLoggedIn_authenticated=false;
						return HOOK_ajaxUserIsNotLoggedIn("not authenticated");
					}else{
						userLoggedIn_authenticated=true;
						return true;
					}
				},
			error: function() {	userLoggedIn_authenticated=false; },
			complete: function() { return userLoggedIn_authenticated;}
			
	
	});	

	return userLoggedIn_authenticated;
	// return ajaxUserIsNotLoggedIn("not authenticated");


}

function toggleFrame(mode, txtype){	
	
	if(mode=='show'){
		if(txtype=="update"){
			$("#Iframe_payment_Header").html("<strong>Update Credit Card</strong>");
			$('#theHOPUpdateDriverForm').submit();
			togglePaymentAndIFrameBlock(true);
			$('#cancelButton').attr("disabled", false); 
		}else if (txtype=="new"){
			$("#Iframe_payment_Header").html("<strong>Add New Credit Card</strong>");
			$('#theHOPDriverForm').submit();
			togglePaymentAndIFrameBlock(true);	
			$('#cancelButton').attr("disabled", false);
			
		}			
	}else if(mode=='hide'){
		togglePaymentAndIFrameBlock(false);
		$('#cancelButton').attr("disabled", true); 				
	}
}

// this is not required
//clearPaymentMethod when other action occurs
function clearPaymentMethod() {
	$(".selectable").each(function(){
		$(this).attr("checked", false);			
		isPaymentMethodSelected = false;
		paymentProfileId = "0";
	});	
}

// Show credit card list or Iframe block
function togglePaymentAndIFrameBlock(showIFrame){
	if(showIFrame){
		$('#mainPaymentBlock').hide();
		$('#mainPaymentTypeOptionSelectorBlock').hide();
		$('#IFrameBlock').show(1400);
		HOOK_IFrameShowEvent("show");
		//	2011-12-27 MSJ
		//	IE 7 fix.  IE7 fix.
		//$("#IFrameBlock")[0].innerHTML += ''; 
	}else{
		$('#mainPaymentBlock').show();
		$('#mainPaymentTypeOptionSelectorBlock').show();
		$('#IFrameBlock').hide();	
		HOOK_IFrameShowEvent("hide");
	}
	
}

function submitPayment() {
  var myFrm = document.getElementById('frm');
  var paymentType=$.trim($("#frm input[type='radio']:checked").val());
  // if no radio buttons to select payment type 
  //add hidden field to you payment screen (jsp)with default payment type
  if(paymentType==""){
	  if($("#defaultPaymentType").length != 0){
		  paymentType=$.trim($("#defaultPaymentType").val());
	  }
  }
  // double check
    if(paymentType==""){
		if(paymentScreenProps.windowType=="Modal"){
	  	  	alert('You must select a specific payment type place your order.\nPlease make a selection.');
	  	  	return false;
	  	}else{
	  $.prompt(
				'You must select a specific payment type place your order.\nPlease make a selection.',
				{
					buttons:{Ok:true},
					prefix:'cleanblue'
				}
			);
	  	}
		return false;
  }
  // proceed only if payment type is selected
  if(paymentType == "credit-card"){
	  selectCreditCardFromList();
	  if($('#frm_paymentProfileId').val() == 'NONE'){
  	  	  //$("#displayContinue").hide();		  
			  	  	if(paymentScreenProps.windowType=="Modal"){
					  	  	alert('You must select a specific credit card to place your order.\nPlease make a selection.');
					  	  	return false;
			  	  	}else{
					  $.prompt(
								'You must select a specific credit card to place your order.\nPlease make a selection.',
								{
									buttons:{Ok:true},
									prefix:'cleanblue'
								}
							);
			  	  	}
		}else{
			if(paymentScreenProps.windowType=="Modal"){
				var options = {
						target: '#'+paymentScreenProps.ajaxSubmitTarget,
						success: function() {
						$('#'+paymentScreenProps.MainModalWindowId).dialog("close");
						$('#'+paymentScreenProps.ajaxSubmitTargetWindowId).dialog("open");
						PositionModalDialog($('#'+paymentScreenProps.ajaxSubmitTargetWindowId),$("#modalDialogPosition"));
						} 
				}
				if(isUserLoggedIn()){
					 HOOK_BeforePaymentSubmit();// implement this in parent window if using modal
					 $("#paymentContent").hide();
					$('#frm').ajaxSubmit(options);
				}
				 
			}else{
				myFrm.submit();
			}
		}
  		}else{
  			if(paymentScreenProps.windowType=="Modal"){
  				var options = {
						target: '#'+paymentScreenProps.ajaxSubmitTarget,
						success: function() {
						$('#'+paymentScreenProps.MainModalWindowId).dialog("close");
						$('#'+paymentScreenProps.ajaxSubmitTargetWindowId).dialog("open");
						PositionModalDialog($('#'+paymentScreenProps.ajaxSubmitTargetWindowId),$("#modalDialogPosition"));
						} 
				}
				if(isUserLoggedIn()){
					 $("#paymentContent").hide();
					 HOOK_BeforePaymentSubmit();
					$('#frm').ajaxSubmit(options);
				}
				 
			}else{
	  		myFrm.submit();
			}
    	}
  
	}


function getSelectedCurrency(val,totalAmountInUSD){
	var currencyType = val;
	var appender;
	if(currencyType =="EUR"){
		appender = "&nbsp;EUR";
	}else if(currencyType =="GBP"){
		appender = "&nbsp;GBP";
	}else if(currencyType =="JPY"){
		appender = "&nbsp;JPY";
	}else if(currencyType =="CAD"){
		appender = "&nbsp;CAD";
	}else if(currencyType =="USD"){
		appender = "&nbsp;USD";
	}else if(currencyType =="CHF"){
		appender = "&nbsp;CHF";
	}
	
	$.ajax({
			type: "POST",
			url: "/selectCoiPaymentType.do?operation=multiCurrencyRates",
			data: "currencyType=" +val+"&totalAmountInUSD="+totalAmountInUSD,
			success: 
			function(data){
				if(currencyType !="USD"){
					data = data+appender;
					//$("#fundingCurrencyAmountFormatedString").html(data);
					$("#displayTotalPriceIdNonUSD").html(data);
					$("#displayTotalPriceIdNonUSD").show();
					$("#displayTotalPriceIdUSD").hide();
					$("#displayCartTotalPriceIdNonUSD").html(data);
					$("#displayCartTotalPriceIdNonUSD").show();
					$("#displayCartTotalPriceIdUSD").hide();
					$("#displayRefCartTotalPriceIdUSD").show();
					$("#ccInfo").show();
				}else{
					$("#displayTotalPriceIdNonUSD").hide();
					$("#displayTotalPriceIdUSD").show();
					$("#displayCartTotalPriceIdNonUSD").hide();
					$("#displayCartTotalPriceIdUSD").show();
					$("#displayRefCartTotalPriceIdUSD").hide();
					$("#ccInfo").hide();
				}
											
			}
		});
}
// call from parent on document.ready
function paymentType_RadioToggle(){
	 var paymentType=$.trim($("#frm input[type='radio']:checked").val());
	  if(paymentType == "credit-card"){
	  $("#ccSelect").click();
	  }else{
		  $("#invSelect").click();
	  }
}


