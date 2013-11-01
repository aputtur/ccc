<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<html>
  <head>
  
    <script type="text/javascript">
        function lookup() {
            //after account number found from look up initialize fields
            document.getElementById("operation").value="lookup";
            document.getElementById("editPublisherActionForm").submit();
        }
        
        function doOperation(operationName){
           document.getElementById("operation").value = operationName;
           document.getElementById("editPublisherActionForm").submit();
        }
        
        function save() {
            if (validate()) {
               doOperation('save');
            }
        }
        
        function updateEditPage(url) {
            opener.location=url;
            return false;
        }
        
        function closeDown(url){
            opener.location=url;
            self.close();
            return false;
        }

        
        function validate()
        {
            var accountNumber = document.getElementById("addedSubAccountNum").value;
            if (accountNumber.length!=10) {
                alert("Please Look up a Valid Account, Before Attempting to Save.");
                return false;
            }
            return true;
        }
        //refresh parent page so added sub accounts appear immediately (this will cause an action on the parent page)
        opener.location='editPublisher.do?operation=refreshSubAccounts';
    </script>
    
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>Add SubAccount</title>
  </head>
  <body>
     <html:errors/>
     
     <html:form action="/admin/addPublisherSubaccount.do" method="post" styleId="editPublisherActionForm">
     <html:hidden property="operation" styleId="operation" value="save"/>
        <h3>Add Sub Account</h3>
        <table  width="100%" border="1" rules="none" frame="box">
        <tr>
            <td style="width:22%">
                <html:text name="editPublisherActionForm" property="addedSubAccountNum" styleId ="addedSubAccountNum"style="width:100%"/>
            </td>
            <td>
                <html:button property="lookupButton" value="Look Up" onclick="javascript:lookup()"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <html:text name="editPublisherActionForm" property="addedSubAccountPubName" style="width:75%" disabled="true"/> 
            </td>
        </tr>
        <tr>
            <td><html:button property="cancelButton" value="Close" onclick="javascript:closeDown('editPublisher.do?operation=refreshSubAccounts')"/></td>
            <logic:equal name="editPublisherActionForm" property="subAccountSaveAllowed" value="true">
                <td><html:button property="saveButton" value="Save" onclick="javascript:save()"/></td>
            </logic:equal>
            <logic:notEqual name="editPublisherActionForm" property="subAccountSaveAllowed" value="true">
                <td><html:button property="saveButton" value="Save" disabled="true" onclick="javascript:save()"/></td>
            </logic:notEqual>
        </tr>    
        </table>
        <table>
        <tr><td>
        <logic:notEmpty name="editPublisherActionForm" property="lastSaved">
            <h5>Sub Account: <bean:write name="editPublisherActionForm" property="lastSaved"/> Saved</h5>
        </logic:notEmpty>
        </td></tr>
        </table>
     </html:form>
  </body>
</html>