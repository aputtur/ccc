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
	-pagingIndex: index of paging section e.g., 0 - n 
	-pagingIterator: name of iterator status field used to control paging index values
-->
<#assign pgIndex="${parameters.pagingIndex?c}">

<#if parameters.pageChangeScript?exists>
	<#assign submitForm="${parameters.pageChangeScript?html}">
<#else>
	<#assign submitForm="doReSizeMultiPageResults">
</#if>
<#assign objId="${parameters.pagingObject?replace('.','_')}">
<#assign submitForm="${submitForm}('${parameters.pagingFormId}','${pgIndex}')">
<#assign pageControl="${parameters.pagingObject?replace('${parameters.pagingIterator}','[${pgIndex}]')}">
<#assign newPageSizeFieldId="${parameters.pagingFormId}_newPageSize">
<#assign sNoClearClass="cctlnoclear">
<#if parameters.noClearClass?exists>
	<#assign sNoClearClass="${parameters.noClearClass?html}">
</#if>

<#if (stack.findValue('${pageControl}')?exists)>
	<#assign pageSize=stack.findValue('${pageControl}.pageSize')>
<#else>
	<#assign pageSize=5>
</#if>

<b>Page Size:</b>&nbsp;&nbsp;&nbsp;

<#if parameters.pageSizeList?exists>
	<#if (parameters.pageSizeListKey?exists && parameters.pageSizeListValue?exists)>
		<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize[${pgIndex}]"
			list="parameters.pageSizeList"  
			listKey="${parameters.pageSizeListKey}" 
			listValue="${parameters.pageSizeListValue}"
			onchange="${submitForm}" value="${pageSize}"
		/>
	<#elseif parameters.pageSizeListKey?exists>
		<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize[${pgIndex}]"
			list="parameters.pageSizeList"  
			listKey="${parameters.pageSizeListKey}"
			onchange="${submitForm}" value="${pageSize}"
		/>
	<#elseif parameters.pageSizeListValue?exists>
		<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize[${pgIndex}]"
			list="parameters.pageSizeList"  
			listValue="${parameters.pageSizeListValue}"
			onchange="${submitForm}" value="${pageSize}"
		/>
	<#else>
		<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize[${pgIndex}]"
			list="parameters.pageSizeList"
			onchange="${submitForm}" value="${pageSize}"
		/>
	</#if>
<#else>
	<@s.select cssClass="${sNoClearClass}" theme="simple" name="newPageSize[${pgIndex}]" onchange="${submitForm};" value="${pageSize}" 
	    list="{'5', '10', '25', '50'}" >
	</@s.select>
</#if>
		
