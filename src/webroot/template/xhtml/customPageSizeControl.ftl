<#--
Uses: customPageSizeControl.ftl
	
Fields:
	-pagingFormId: Id of form to prefix fields
	-pagingObject: name of paging Object
	-pageChangeScript: javascript to carryout page change  - default is doReSizePageResults
	-pageSizeList: list for page size choices
	-pageSizeListKey: key property of pageSizeList
	-pageSizeListValue: value property of pageSizeList
	-noClearClass: class to be added to form fields not to be cleared on form reset default is cctlnoclear
-->

<#if parameters.pageChangeScript?exists>
	<#assign submitForm="${parameters.pageChangeScript?html}">
<#else>
	<#assign submitForm="doReSizePageResults">
</#if>
<#assign objId="${parameters.pagingObject?replace('.','_')}">
<#assign submitForm="${submitForm}('${parameters.pagingFormId}','${objId}')">
<#assign pageControl="${parameters.pagingObject}">
<#assign newPageSizeFieldId="${parameters.pagingFormId}_newPageSize">
<#assign sNoClearClass="cctlnoclear">
<#if parameters.noClearClass?exists>
	<#assign sNoClearClass="${parameters.noClearClass?html}">
</#if>

<#if (stack.findValue('${pageControl}')?exists)>
	<#assign pageSize=stack.findValue('${pageControl}.pageSize')>
<#else>
	<#assign pageSize=25>
</#if>

<b>Page Size:</b>&nbsp;&nbsp;&nbsp;

<#if parameters.pageSizeList?exists>
	<#if (parameters.pageSizeListKey?exists && parameters.pageSizeListValue?exists)>
		<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize"
			list="parameters.pageSizeList"  
			listKey="${parameters.pageSizeListKey}" 
			listValue="${parameters.pageSizeListValue}"
			onchange="${submitForm}" value="${pageSize}"
		/>
	<#elseif parameters.pageSizeListKey?exists>
		<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize"
			list="parameters.pageSizeList"  
			listKey="${parameters.pageSizeListKey}"
			onchange="${submitForm}" value="${pageSize}"
		/>
	<#elseif parameters.pageSizeListValue?exists>
		<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize"
			list="parameters.pageSizeList"  
			listValue="${parameters.pageSizeListValue}"
			onchange="${submitForm}" value="${pageSize}"
		/>
	<#else>
		<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize"
			list="parameters.pageSizeList"
			onchange="${submitForm}" value="${pageSize}"
		/>
	</#if>
<#else>
	<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize" onchange="${submitForm};" value="${pageSize}" 
	    list="{'5', '10','25', '50'}" >
	</@s.select>
</#if>
		
