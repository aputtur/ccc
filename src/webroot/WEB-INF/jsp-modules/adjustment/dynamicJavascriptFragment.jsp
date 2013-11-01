<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>

<%
    boolean canEnterAdjustment = com.copyright.ccc.business.security.UserContextService.hasPrivilege( com.copyright.ccc.business.security.CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT ); 
    boolean canCommitAdjustment = com.copyright.ccc.business.security.UserContextService.hasPrivilege( com.copyright.ccc.business.security.CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT ); 
%>


<%//Common - BEGIN%>
<script type="text/javascript" >
      
      <%if( canEnterAdjustment ){%>  
        function cancelAdjustment(){
          if( confirm( "Are you sure you want to cancel this adjustment, lose changes and go back to order history?" ) ){ 
            document.location.href = '<html:rewrite action="/adjustment/adjustment.do?operation=cancelAdjustment"/>';
          }
        }
      <%}%>
       
</script>

<script type="text/javascript" >
    function refresh(){
      var adjustmentsForm = document.getElementById("adjustmentsForm");
      
      if( adjustmentsForm ){
      
        var adjustmentsFormAction = addScrollToURLParameter( '<html:rewrite action="/adjustment/adjustment.do?operation=refreshAdjustment"/>' );
                    
        adjustmentsForm.action =  adjustmentsFormAction;
      
        adjustmentsForm.submit();
      }
    }
</script> 

<%//Common - END%>

<%//==============================================================================%>

<%//UI Mode Adjustment - BEGIN%>

<logic:equal value="true" name="adjustmentForm" property="UIModeAdjustment">
 
  <script  type="text/javascript" >
    <%if( canEnterAdjustment ){%>
      
      function saveAdjustment(){
        
        var adjustmentsForm = document.getElementById("adjustmentsForm");
        
        if( adjustmentsForm ){
                 
          adjustmentsForm.action = '<html:rewrite action="/adjustment/adjustment.do?operation=saveAdjustment"/>';
          
          adjustmentsForm.submit();
        }
      }
    
    <%}%>
  </script>

</logic:equal>

<%//UI Mode Adjustment - END%>

<%//==============================================================================%>

<%//UI Mode Confirmation - BEGIN%>

<logic:equal value="true" name="adjustmentForm" property="UIModeConfirmation">
   
  <html:form action="/adjustment/adjustment.do?operation=deleteAdjustment" styleId="deleteAdjustmentForm" >
    <html:hidden name="adjustmentForm" property="adjustmentIDToDelete" styleId="adjustmentIDToDelete" />
  </html:form>
  
  
        
  <script type="text/javascript" >

  
    <%if( canCommitAdjustment ){%>
      
      function makeAdjustment(){
      
        var adjustmentsForm = document.getElementById("adjustmentsForm");
        
        if( adjustmentsForm && confirm("Are you sure you want to commit this adjustment?") ){
                        
          adjustmentsForm.action = '<html:rewrite action="/adjustment/adjustment.do?operation=makeAdjustment"/>';
          
          adjustmentsForm.submit();
        }
      }
      
    <%}%>
    
    <%if( canEnterAdjustment ){%>
    
      function deleteAdjustment( id ){
      
        var deleteAdjustmentForm = document.getElementById("deleteAdjustmentForm");
        
        if( id && deleteAdjustmentForm ){
          
          var adjustmentIDToDelete = document.getElementById("adjustmentIDToDelete");
          
          if( adjustmentIDToDelete && confirm("Are you sure you want to delete this adjustment?") ){
            
            adjustmentIDToDelete.value = id;
            deleteAdjustmentForm.submit();
          }
        }
      }
    
      
      function editAdjustment(){
        document.location.href = '<html:rewrite action="/adjustment/adjustment.do?operation=editAdjustment"/>';
      }
    
    <%}%>
          
  </script> 

        
  <html:form action="/adjustment/adjustment.do?operation=modifyAdjustment" styleId="modifyAdjustmentForm" >
    <html:hidden name="adjustmentForm" property="adjustmentIDToModify" styleId="adjustmentIDToModify" />
  </html:form>
  
  <script type="text/javascript" >
  
    <%if( canEnterAdjustment ){%>
  
      function modifyAdjustment( id ){
      
        var modifyAdjustmentForm = document.getElementById("modifyAdjustmentForm");
        
        if( id && modifyAdjustmentForm ){
          
          var adjustmentIDToModify = document.getElementById("adjustmentIDToModify");
          
          if( adjustmentIDToModify ){
            adjustmentIDToModify.value = id;
            modifyAdjustmentForm.submit();
          }
        }
      }
  
    <%}%>
  
  </script>

</logic:equal>

<%//UI Mode Confirmation - END%>

<%//==============================================================================%>

<%//UI Mode Single Adjustment - BEGIN%>

<logic:equal value="true" name="adjustmentForm" property="UIEditCurrentAdjustmentMode">
  
    <script  type="text/javascript" >
    
      <%if( canCommitAdjustment ){%>
    
        function completeAdjustment(){
        
          var adjustmentsForm = document.getElementById("adjustmentsForm");
        
          if( adjustmentsForm ){
            
            adjustmentsForm.action = '<html:rewrite action="/adjustment/adjustment.do?operation=completeAdjustment"/>';
            
            adjustmentsForm.submit();
          }
        }
      
      <%}%>
      
      <%if( canEnterAdjustment ){%>
      
        function recalculateAdjustment( adjustmentID ){
        
          var adjustmentsForm = document.getElementById("adjustmentsForm");
          var adjustmentIDToRecalculate = document.getElementById("adjustmentIDToRecalculate");
          
        
          if( adjustmentsForm && adjustmentIDToRecalculate ){
            
            adjustmentIDToRecalculate.value = adjustmentID;
            
            var adjustmentsFormAction = addScrollToURLParameter( '<html:rewrite action="/adjustment/adjustment.do?operation=recalculateCurrentAdjustment"/>' );
            
            adjustmentsForm.action =  adjustmentsFormAction;
            
            adjustmentsForm.submit();
          }else{
            alert("Unable to recalculate adjustment price");
          }
        }
      
            
        function performGlobalAdjustment(){
        
          var adjustmentsForm = document.getElementById("globalAdjustmentForm");
        
          if( adjustmentsForm ){
            
            adjustmentsForm.action = '<html:rewrite action="/adjustment/adjustment.do?operation=performGlobalAdjustment"/>';
            
            adjustmentsForm.submit();
          }
        }
      
      <%}%>
      
    </script>
    
  </logic:equal>


<%//UI Mode Single Adjustment - END%>