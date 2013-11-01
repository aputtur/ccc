<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/validators.js"/>" type="text/javascript"></script>

<!-- client side validation -->
<util:ccJavascript formName="editPublisherActionForm_save" />
<!-- end client side validation -->

    <script type="text/javascript">
        function lookup() {
            //after account number found from look up initialize fields
            document.getElementById("operation").value="lookup";
            document.forms[0].submit();
        }
        
        function doOperation(operationName){
           document.getElementById("operation").value = operationName;
           document.getElementById("editPublisherActionForm").submit();
        }
        
        function save() {
            var permissionOptions = document.getElementById("permOptionDesc");
            var learnMore = document.getElementById("learnMoreDesc");
            var pubUrl = document.getElementById("pubUrl");
            if ((pubUrl.value.length==0) || (pubUrl.value==null)) {
                alert("URL is a required field");
                return;
            }
            if ((permissionOptions.value.length==0) || (permissionOptions.value==null)) {
                alert("Permission Options are required");
                return;
            }
            if ((learnMore.value.length==0) || (learnMore.value==null)) {
                alert("Learn More is a required field");
                return;
            }
            
             var form = document.getElementById("editPublisherActionForm");
             ;
             var errors = validateEditPublisherActionForm_save(form);
             if(errors.length > 0)
             {
                displayValidationErrors(errors, "clientSideValidationErrorsSection", "clientSideValidationErrorsListSection");
             }
             else
             {
                hideValidationErrors("clientSideValidationErrorsSection");
                doOperation('save');
             }
        }
        
        function cancel() {
            //go back to Publisher Maintenance Page
            document.forms[0].reset();
            document.forms[0].action.value='/publisherMaintenance.do';
            document.forms[0].submit();
        }
        
        function addSubAccount() {
            goPopup('addPublisherSubaccount.do');
        }
        
        function deleteSubAccount() {
            if (confirm("Are you sure you want to delete this RLink Publisher Sub Account?")) {
                doOperation('deleteSubAccount');
            }
        }
        function goPopup(lnk) {     
            var load = window.open(lnk,'','scrollbars=yes,menubar=no,height=300,width=400,resizable=yes,toolbar=no,location=no,status=no, left=600, top=200');
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
                 var selectedCheckbox = checkboxArr[index2];
                 if(selectedCheckbox.checked == 1){
                     document.getElementById("selectedSubAccountRow").value=index2;
                     selectedCount++;
                 }
          }
          var deleteButton = document.getElementById("deleteButton");
          if (selectedCount!=1) {
             deleteButton.disabled=true;
          }
          else {
             deleteButton.disabled=false;
          }
    }
    </script>

    <html:form action="/admin/editPublisher.do" method="post" styleId="editPublisherActionForm">
    <html:hidden property="operation" styleId="operation" value="save"/>
    <html:hidden property="editMode"/>
    <html:hidden property="subAccounts"/>
    <html:hidden property="selectedSubAccountRow" styleId="selectedSubAccountRow"/>
    
    <div style="color:red;padding-bottom:40px;padding-top:10px;display:none" id="clientSideValidationErrorsSection">
        <b>THERE WAS A PROBLEM COMPLETING YOUR REQUEST</b>
        <div style="color:red;padding-left:20px" id="clientSideValidationErrorsListSection"></div>
    </div>
    
     <logic:equal name="editPublisherActionForm" property="editMode" value="true">
        <h1>Edit Publisher Account</h1>
    </logic:equal>
    <logic:notEqual name="editPublisherActionForm" property="editMode" value="true">
        <h1>Add New Publisher Account</h1>
    </logic:notEqual>
    
    <table width="100%">
        <tr>
            <td style="width:15%">Account Number*</td>

            <logic:equal name="editPublisherActionForm" property="editMode" value="true">
                <td style="width:9%" align="left"><html:text property="accountNum" style="width:100%" readonly="true" disabled="true"/></td>
                <td></td>
            </logic:equal>
            <logic:notEqual name="editPublisherActionForm" property="editMode" value="true">
                <td style="width:9%" align="left"><html:text property="accountNum" style="width:100%"/></td>
                <td align="left"><html:button property="lookupButton" value="Look Up" onclick="lookup()"/></td>
            </logic:notEqual>
        </tr>
        <tr>
            <td>Publisher*</td>
            <td colspan="2"><html:text property="pubName" readonly="true" disabled="true" style="width:50%"/></td>
        </tr>
        <tr>
            <td>Site URL*</td>
            <td colspan="4" style="width:200"><html:text property="pubUrl" style="width:50%" styleId="pubUrl"/></td>
        </tr>
    </table>
    <table cellspacing="10">
        <tr>
            <td><h3>Permission Options*</h3></td>
            <td><h3>Learn More*</h3><p style="text-align: right; color: #990000; margin-top: -20px">* indicates required field</p></td>
        </tr>
        <tr>
            <td><html:textarea property="permOptionDesc" rows="12" cols="40" styleId="permOptionDesc"/></td>
            <td><html:textarea property="learnMoreDesc" rows="12" cols="40" styleId="learnMoreDesc"/></td>
        </tr>
   </table>
    <table width="100%">
        <tr>
            <td align="right" style="width:45%"><html:button property="cancelButton" value="Close" onclick="doOperation('cancel')"/></td>
            <td style="width:5%"><html:button property="clearButton" value="Clear" onclick="doOperation('newPublisher')"/></td>
            <logic:equal name="editPublisherActionForm" property="editMode" value="true">
                <td><html:button property="saveButton" value="Save" onclick="save()"/></td>
            </logic:equal>
            <logic:notEqual name="editPublisherActionForm" property="editMode" value="true">
               <logic:equal name="editPublisherActionForm" property="saveAllowed" value="true">
                  <td><html:button property="saveButton" value="Save" onclick="save()"/></td>
               </logic:equal>
               <logic:notEqual name="editPublisherActionForm" property="saveAllowed" value="true">
                 <td><html:button property="saveButton" value="Save" disabled="true" onclick="save()"/></td>
               </logic:notEqual>
            </logic:notEqual>
        </tr>
    </table>
   <h3>Sub Accounts</h3>
        <table  border="1">
        <tr>
            <th>  </th><th>Account #</th><th>Name</th>
        </tr>
        <logic:iterate id="subAccount" name="editPublisherActionForm" property="subAccounts" indexId="index">
             <tr>
                <td><html:checkbox name="subAccount" property="selected" indexed="true" onclick="rowSelected()"/></td>
                <td><bean:write name="subAccount" property="accountNum"/> </td>
                <td colspan="4"><bean:write name="subAccount" property="name" /></td>
            </tr>
        </logic:iterate>
        </table>
    <logic:notEmpty name="editPublisherActionForm" property="publisher">
        <table>
           <tr>
              <td align="left"><html:button property="deleteButton" value="Delete Sub Account" onclick="deleteSubAccount()" styleId="deleteButton" disabled="true"/></td> 
              <td align="left"><html:button property="addButton" value="Add Sub Account" onclick="javascript:goPopup('addPublisherSubaccount.do')"/></td>
          </tr>
        </table>
    </logic:notEmpty>
    </html:form>
