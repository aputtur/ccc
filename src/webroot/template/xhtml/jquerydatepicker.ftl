<#--
Uses: jquerydatepicker.ftl
    
Fields:
    -datePickerId: id of the field to use the date picker
    -instanceId: counter to make unique
-->  
  
<#assign sDatePicker="#">
<#assign sDatePickId="#">
<#assign sDatePickInstId="#">

<#if parameters.sDatePickId?exists>
	<#if parameters.sDatePickInstId?exists>
		<#assign sDatePicker="#${parameters.sDatePickId?html}_${parameters.sDatePickInstId?c}">
	</#if>
</#if>

<#if sDatePicker != "#" >
    
<script  type="text/javascript">

 	$(document).ready(function(){

	 	$('${sDatePicker}').datepicker( {
			    showAnim : 'fadeIn',
				showOn: 'button',
				buttonImage: '<@s.url value="/resources/ordermgmt/images/calendar.gif"/>',
				buttonImageOnly: true,
				buttonText: 'Calendar...'		});
                    
});

</script>

</#if>