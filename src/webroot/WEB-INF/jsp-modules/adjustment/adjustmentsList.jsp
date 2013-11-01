<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>

<%@ page import="com.copyright.ccc.web.transaction.RepublicationConstants"  %>
<%@ page import="com.copyright.data.order.UsageDataNet"%>
<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustmentConstants" %>

<style type="text/css">
  td{ padding: 5px; }
  div#validationErrorsSection{ display: none };
</style>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>Adjustments&nbsp;&gt;&nbsp;Pending Adjustments</title>
    
    <link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />
    
    <link href="<html:rewrite page="/resources/adjustment/css/adjustment.css"/>" rel="stylesheet" type="text/css" />
    
    <script src="<html:rewrite page="/resources/adjustment/script/adjustments.js"/>"></script>
    
    <script type="text/javascript">
    
         function initPage(){
          organizePageLayout();
         }
        
        if( typeof addOnLoadEvent == 'function' && typeof initPage == 'function' ) addOnLoadEvent( initPage );
        
        
        function formatDate( date ){
    
          if(!date) return;
          
          var dateStr = new String( date );
          var formattedDateStr = dateStr.replace(/\s\d{2}:\d{2}:\d{2}/,"");
          
          document.write( formattedDateStr );
        }
    </script>
    
  </head>

    <body>
  
    <div id="mainContainer" style="padding: 10px; width: 850px;">
      
        <h1 style="padding-bottom: 10px;">
            Adjustments&nbsp;&gt;&nbsp;Pending Adjustments
        </h1>
        
        <logic:messagesPresent >

            <span style="color: red;">
              <html:errors bundle="adjustment" />
            </span>
            
        </logic:messagesPresent>
        
        <logic:messagesPresent message="true">
          
          <div style="padding-left: 10px; padding-right: 10px;">
            <div id="messages" class="container" style="color: #3F493D; background-color:#D1D8CC; padding:10px; border:1px solid gray;">
              <script type="text/javascript">function hideMessages(){ var messages = document.getElementById("messages"); if(messages){ messages.style.display="none"; } }</script>
              <a id="closeMessagesLink" href="javascript:hideMessages()" style="position: relative; float: right; color: gray; font-weight: bold;">Close</a>
              <html:messages bundle="adjustment" id="message" message="true">
                  <bean:write name="message"/>
              </html:messages>
            </div>
          </div>        
        
        </logic:messagesPresent>  
        
         <div id="linksContainer" class="container">
          <a href='<html:rewrite page="/admin/orderHistory.do"/>'><html:image page="/resources/adjustment/images/hourglass_go.png" style="vertical-align: middle;" title="Go to Order History"/></a>&nbsp;<a href='<html:rewrite page="/admin/orderHistory.do"/>'>Go to Order History</a>
        </div>
        
         <div id="originalDetailsContainer" class="container">
       
           <fieldset>
              <legend>Pending Adjustments</legend>
          <div class="container">
            <logic:empty name="adjustmentForm" property="adjustmentDescriptors">
              There are no pending adjustments.
            </logic:empty><logic:notEmpty name="adjustmentForm"
                                          property="adjustmentDescriptors">
              <table style="border-collapse: collapse; width: 100%;">
                <tr>
                  <th>
                    Adjustment ID
                  </th>
                  <th>
                    Adjustment Type
                  </th>
                  <th>
                    Adjustments
                  </th>
                  <th>
                    Created On
                  </th>
                  <th>
                    Created By
                  </th>
                  <th>
                    Updated On
                  </th>
                  <th>
                    Updated By
                  </th>
                  <th>
                    &nbsp;
                  </th>
                </tr>
                <nested:iterate name="adjustmentForm"
                                property="adjustmentDescriptors"
                                id="descriptorEntry" indexId="descriptorIndex">
                  <tr onmouseover="javascript:highlight(this)"
                      onmouseout="javascript:removeHighlight(this)">
                    <td>
                      <a href='javascript:editAdjustment( <bean:write name="descriptorEntry" property="value.ID" /> )'>
                        <bean:write name="descriptorEntry"
                                    property="value.cartDescriptor.adjustmentID"/>
                      </a>
                    </td>
                    <td>
                      <bean:write name="descriptorEntry"
                                  property="value.adjustmentType"/>
                    </td>
                    <td>
                      <bean:write name="descriptorEntry"
                                  property="value.cartDescriptor.numberOfRequests"/>
                    </td>
                    <td>
                      <script type="text/javascript"> formatDate("<bean:write name="descriptorEntry" property="value.createDate" />"); </script>
                    </td>
                    <td>
                      <bean:write name="descriptorEntry"
                                  property="value.createUserName"/>
                    </td>
                    <td>
                      <script type="text/javascript"> formatDate("<bean:write name="descriptorEntry" property="value.lastUpdateDate" />"); </script>
                    </td>
                    <td>
                      <bean:write name="descriptorEntry"
                                  property="value.lastUpdateUserName"/>
                    </td>
                    <td>
                      <a href='javascript:deleteAdjustment( <bean:write name="descriptorEntry" property="value.ID" /> )'>
                        Delete
                      </a>
                    </td>
                  </tr>
                </nested:iterate>
              </table>
            </logic:notEmpty>
          </div>
        </fieldset>
           
         </div>

        
    </div>
  
    <html:form action="/adjustment/adjustment.do?operation=modifySavedAdjustment" styleId="editAdjustmentForm" >
      <html:hidden name="adjustmentForm" property="selectedCartDescriptorID" styleId="selectedAdjustmentDescriptorID" />
    </html:form>
    
    <html:form action="/adjustment/adjustment.do?operation=deleteSavedAdjustment" styleId="deleteAdjustmentForm" >
      <html:hidden name="adjustmentForm" property="selectedCartDescriptorID" styleId="selectedAdjustmentDescriptorIDToDelete" />
    </html:form>
  
  </body>
  
  <script type="text/javascript">
    
    function editAdjustment( adjustmentID ){
      
      var editAdjustmentForm = document.getElementById("editAdjustmentForm");
      var selectedAdjustmentDescriptorID = document.getElementById("selectedAdjustmentDescriptorID");
      
      if( editAdjustmentForm && selectedAdjustmentDescriptorID ){
        
        selectedAdjustmentDescriptorID.value = adjustmentID;
        
        editAdjustmentForm.submit();
      }
    }
    
    function deleteAdjustment( adjustmentID ){
      
      var deleteAdjustmentForm = document.getElementById("deleteAdjustmentForm");
      var selectedAdjustmentDescriptorIDToDelete = document.getElementById("selectedAdjustmentDescriptorIDToDelete");
      
      if( deleteAdjustmentForm && selectedAdjustmentDescriptorIDToDelete ){
      
        if( confirm("Are you sure you want to delete this adjustment?") ){
        
          selectedAdjustmentDescriptorIDToDelete.value = adjustmentID;
          
          deleteAdjustmentForm.submit();
        }
      }
      
    }

  </script>
  
  <script type="text/javascript">
    function highlight( element ){
      element.style.backgroundColor = "#dcdcdc";
    }
    
    function removeHighlight( element ){
      element.style.backgroundColor = "";
    }
    
    function formatDate( date ){
    
      if(!date) return;
      
      var dateStr = new String( date );
      var formattedDateStr = dateStr.replace(/\s\d{2}:\d{2}:\d{2}/,"");
      
      document.write( formattedDateStr );
    }
  </script>

</html>