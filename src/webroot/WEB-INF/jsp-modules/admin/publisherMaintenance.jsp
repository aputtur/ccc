<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>

    <script type="text/javascript">
        function doOperation(operationName){
           document.getElementById("operation").value = operationName;
           document.getElementById("publisherMaintenanceForm").submit();
        }
        
    function deleteRow() {
        if (confirm("Are you sure you want to delete this RLink Publisher Account?")) {
            doOperation('deletePublisher');
        }
    }
    function rowSelected(indexVal) {
          //record the rows that are selected if there is more than one row selected then disable the edit button 
          var selectedCount=0;
          var checkboxes = document.getElementsByTagName("input");
          var inputTags = document.getElementsByTagName("input");
          var selectedPublishers = document.getElementById("selectedPublishers");
          var checkboxArr = new Array();
          var indexChk = 0;
          for (indexvar = 0; indexvar<inputTags.length; indexvar++) {
              if (checkboxes[indexvar].getAttribute("type")=="checkbox") {
                 checkboxArr[indexChk] = inputTags[indexvar];
                 indexChk++;
              }
          }
          for (index2 = 0; index2<checkboxArr.length; index2++) {
             //if (checkboxes[indexvar].getAttribute("type")=="checkbox") {
                 var selectedCheckbox = checkboxArr[index2];
                 if(selectedCheckbox.checked == 1){
                     document.getElementById("selectedRow").value=index2;
          //           alert("set the selected row to: "+ index2);
          //           selectedPublishers[index2].value="true";
                     selectedCount++;
                 }
            // }
          }
          var editButton = document.getElementById("editButton");
          var deleteButton = document.getElementById("deleteButton");
          if (selectedCount!=1) {
             editButton.disabled=true;
             deleteButton.disabled=true;
          }
          else {
             editButton.disabled=false;
             deleteButton.disabled=false;
          }
    }

    </script>
    <h1>Publisher Maintenance</h1>
    <html:form action="/admin/publisherMaintenance.do" method="post" styleId="publisherMaintenanceForm">
    <html:hidden property="operation" styleId="operation" value="findPublishers"/>
    <html:hidden property="publishersDisplay"/>
    <html:hidden property="numSelectedRows"/>
    <html:hidden property="colIndex"/>
    <html:hidden property="selectedRow" styleId="selectedRow"/>
    <html:hidden property="selectedPublishers" styleId="selectedPublishers"/>

    <div class="clearer"></div>
    <div style="border-style: solid; border-width:1px">
    <table border="1" rules="none" frame="box">
       <tr>
          <th>   </th>
          <th>Account # </th>
          <th>Name      </th>
          <th>URL       </th>
          <th>Perm Options Desc     </th>
          <th>Learn More       </th>
       </tr>
       <logic:iterate id="publisher" name="publisherMaintenanceForm" property="publishersDisplay" indexId="colIndex">
        <bean:define id="rowselected">rowSelected(<%=colIndex%>)</bean:define>
        <tr>
            <td><html:checkbox name="publisher" property="selected" indexed="true" onclick="<%=rowselected%>"/></td>
            <td><bean:write name="publisher" property="accountNum"/> </td>
            <td><bean:write name="publisher" property="name" /></td>
            <td> <bean:write name="publisher" property="abbrevPubURL"/></td>
            <td> <bean:write name="publisher" property="abbrevPermOptions"/></td>
            <td> <bean:write name="publisher" property="abbrevLearnMore"/></td>
        </tr>
       </logic:iterate>
    </table>
    </div>
    <div>
    <table>
    <tr>
        <!-- td><html:button property="edit" value="Edit" onclick='doOperation("editPublisher")' styleId='editButton'/></td>
        <td><html:button property="new" value="New" onclick='doOperation("newPublisher")' styleId='newButton'/></td>
        <td><html:button property="delete" value="Delete" onclick='doOperation("deletePublisher")' styleId='deleteButton'/></td-->
        <td><html:button property="edit" value="Edit" onclick="javascript:doOperation('editPublisher');" styleId='editButton' disabled="true"/></td>
        <td><html:button property="new" value="New" onclick="javascript:doOperation('newPublisher');" styleId='newButton'/></td>
        <td><html:button property="delete" value="Delete" onclick="deleteRow();" styleId='deleteButton' disabled="true"/></td>
    </tr>
    </table>
    </div>
    </html:form>