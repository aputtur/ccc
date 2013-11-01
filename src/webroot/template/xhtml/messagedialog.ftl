<#--
Uses: messagedialog.ftl
    
Fields:
    -dialogTitle: value dialog title or will be "Message"
    -closeButton: value of the close dialog action button
-->

<div id="messageDialog_Div" title="<#if parameters.dialogTitle?exists>${parameters.dialogTitle}<#else>Message</#if>">
    <div id="messageDialogDiv_Content"></div>
</div>    

<div id="messageDialog_Div_NB" title="<#if parameters.dialogTitle?exists>${parameters.dialogTitle}<#else>Message</#if>">
    <div id="messageDialogDiv_Content_NB"></div>
</div>    
    
    
<script  type="text/javascript">

$(document).ready(function(){    
    $('#messageDialog_Div').dialog({
         autoOpen: false,
            modal: true,
        resizable: false,
          buttons: {
                     "<#if parameters.closeButton?exists>${parameters.closeButton}<#else>OK</#if>": function() {
                     $(this).dialog("close");
                     return false;
                     }
                   }
    });
    
    $('#messageDialog_Div_NB').dialog({
         autoOpen: false,
            modal: true,
        resizable: false
    });
                    
});

</script>