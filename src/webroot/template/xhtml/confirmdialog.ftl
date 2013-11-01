<#--
Uses: confirmdialog.ftl
    
Fields:
    -dialogTitle: value dialog title or will be "Message"
    -cancelButton: value of the cancel dialog action button
    -goButton: value of the go dialog action button
    -goScript: script to execute
    -confirmId: adds to div and dialog names to make unique
-->
<div id="confirmDialog_Div<#if parameters.confirmId?exists>_${parameters.confirmId}</#if>" title="<#if parameters.dialogTitle?exists>${parameters.dialogTitle}<#else>Message</#if>">
    <div id="confirmDialogDiv_Content<#if parameters.confirmId?exists>_${parameters.confirmId}</#if>"></div>
</div>    
    
    
<script  type="text/javascript">

$(document).ready(function(){    
    $('#confirmDialog_Div<#if parameters.confirmId?exists>_${parameters.confirmId}</#if>').dialog({
         autoOpen: false,
            modal: true,
        resizable: false,
          buttons: {
                     "<#if parameters.cancelButton?exists>${parameters.cancelButton}<#else>No</#if>": function() {
                     $(this).dialog("close");
                     return false;
                     },
                    "<#if parameters.goButton?exists>${parameters.goButton}<#else>Yes</#if>": function() {
                     $(this).dialog("close");
                     <#if parameters.goScript?exists>${parameters.goScript}();</#if>
                     return false;
                     }
                   }
                   
    });
                    
});

</script>