<#--
Uses: confirmdialogYesNo.ftl
    
Fields:
    -dialogTitle: value dialog title or will be "Message"
    -cancelButton: value of the cancel dialog action button
    -goButton: value of the go dialog action button
    -goScript: script to execute
	-cancelGoScript: script to execute on No
    -confirmId: adds to div and dialog names to make unique
	This is the confirmdialog template with one change. It will go to some where on 'Yes' and some where else on 'No'.
	
-->
<div id="confirmDialogYesNo_Div<#if parameters.confirmId?exists>_${parameters.confirmId}</#if>" title="<#if parameters.dialogTitle?exists>${parameters.dialogTitle}<#else>Message</#if>">
    <div id="confirmDialogYesNoDiv_Content<#if parameters.confirmId?exists>_${parameters.confirmId}</#if>"></div>
</div>    
    
    
<script  type="text/javascript">

$(document).ready(function(){    
	//alert('In confirm YesNo Dialog');
    $('#confirmDialogYesNo_Div<#if parameters.confirmId?exists>_${parameters.confirmId}</#if>').dialog({
         autoOpen: false,
            modal: true,
        resizable: false,
          buttons: {
                     "<#if parameters.cancelButton?exists>${parameters.cancelButton}<#else>No</#if>": function() {
                     $(this).dialog("close");
					<#if parameters.goScript?exists>${parameters.cancelGoScript}();</#if>
                     return false;
                     },
                    "<#if parameters.goButton?exists>${parameters.goButton}<#else>Yes</#if>": function() {
                     $(this).dialog("close");
                     <#if parameters.cancelGoScript?exists>${parameters.goScript}();</#if>
                     return false;
                     }
                   }
                   
    });
                    
});

</script>