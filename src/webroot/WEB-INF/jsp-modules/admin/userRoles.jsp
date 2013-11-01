<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>

<logic:messagesPresent message="true">
    <div class="error"><html:errors /></div>
</logic:messagesPresent>
<script type="text/javascript">    

function doOperation(operationName)
{
    document.getElementById("operation").value = operationName;
    document.getElementById("editForm").submit();
}

</script>
<br />
<br />
<br />


<html:form styleId="editForm" action="/admin/userRoles" >
Username of user to edit:<br>
<br />
<html:text property="username"/>&nbsp;<html:submit value="Find"/>
<html:hidden styleId="operation" property="operation" value="findUser"/>

<logic:notEmpty name="userRolesForm" property="user">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="tableTextLeft"><span class="fieldValueImportant">Roles:</span></td>
	</tr>
	<logic:iterate name="userRolesForm" property="applicationRoles" id="role">
		<tr>
			<td class="tableTextLeft">
				<html:multibox name="userRolesForm" property="savedUserRoleCodes">
					<bean:write name="role" property="roleCode.code"/>
				</html:multibox>
				<bean:write name="role" property="name" />
			</td>
		</tr>
	</logic:iterate>
	<tr>
		<td class="tableTextLeft" style="vertical-align:bottom">
			<input type="button" value="Save" onclick="javascript:doOperation('saveUserRoles')" />
			<input type="button" value="Cancel" onclick="javascript:doOperation('resetUser')" />
		</td>
	</tr>
</table>

</logic:notEmpty>

</html:form>
