<#--
Uses: jquerydatepickerThreeParams.ftl
    
Fields:
    -sDatePickId:			the prefix of the field to using the date picker
    -sDatePickInstId:		counter to make unique - the item.ID
    -sDatePickerFieldName:	the suffix of the field to using the date picker
    
    example text field:
		<input type="text" name="editItem.dateOfUse" value="04/01/2011" id="savedetailform_42961356_editItem_dateOfUse" class="itype42961356 required" style="width: 70px;"/>
	jsp would look like:
		<s:component theme="xhtml" template="jquerydatepicker.ftl">
			<s:param name="sDatePickId" value="'savedetailform'" />
			<s:param name="sDatePickInstId" value="item.ID" />
			<s:param name="sDatePickerFieldName" value="'editItem_dateOfUse'" />
		</s:component>
    
-->  
  
<#assign sDatePicker="#">
<#assign sDatePickId="#">
<#assign sDatePickInstId="#">
<#assign sDatePickerFieldName="#">

<#if parameters.sDatePickId?exists>
	<#if parameters.sDatePickInstId?exists>
		<#if parameters.sDatePickerFieldName?exists>
			<#assign sDatePicker="#${parameters.sDatePickId?html}_${parameters.sDatePickInstId?c}_${parameters.sDatePickerFieldName?html}">
		</#if>
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