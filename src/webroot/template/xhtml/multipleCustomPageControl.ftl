<#--
Uses: customPageControl.ftl
	
Fields:
	-pagingFormId: Id of form to prefix fields
	-pagingObject: name of paging Object
	-pageChangeScript: javascript to carryout page change - default is doRePageResults
	-inFooter: boolean indicating whether in the footer or not
	-itemType: e.g., Details or Works, etc.
	-noClearClass: class to be added to form fields not to be cleared on form reset default is cctlnoclear
	-pagingIndex: index of paging section e.g., 0 - n 
	-pagingIterator: name of iterator status field used to control paging index values
-->


<#assign pgIndex="${parameters.pagingIndex?c}">
<#assign pageControl="${parameters.pagingObject?replace('${parameters.pagingIterator}','[${pgIndex}]')}">
<#assign newPageFieldId="${parameters.pagingFormId}${pgIndex}_newPage_${pgIndex}_">
<#assign sNoClearClass="cctlnoclear">
<#if parameters.noClearClass?exists>
	<#assign sNoClearClass="${parameters.noClearClass?html}">
</#if>
<#if parameters.itemType?exists>
	<#assign itemName="${parameters.itemType?html}">
<#else>
	<#assign itemName="Details">
</#if>

<#if parameters.pageChangeScript?exists>
	<#assign submitForm="${parameters.pageChangeScript?html}">
<#else>
	<#assign submitForm="doReMultiPageResults">
</#if>
<#assign submitForm="${submitForm}('${parameters.pagingFormId}','${pgIndex}')">

<#if (stack.findValue('${pageControl}')?exists)>
	<#assign totalPages=stack.findValue('${pageControl}.totalPages')>
	<#assign currentPage=stack.findValue('${pageControl}.page')>
<#else>
	<#assign totalPages=1>
	<#assign currentPage=1>
</#if>
<#assign pageSize=stack.findValue('${pageControl}.pageSize')>
<#assign listSize=stack.findValue('${pageControl}.listSize')>

<#if !parameters.inFooter?? || parameters.inFooter!='true'>
	<@s.hidden theme="simple" cssClass="${sNoClearClass}" name="requestedPagingIndex" value="${pgIndex}"/>
	<@s.hidden theme="simple" cssClass="${sNoClearClass}" name="newPage[${pgIndex}]" value="${currentPage}"/>
	<@s.hidden theme="simple" cssClass="${sNoClearClass}" name="${pageControl}.page"/>
	<@s.hidden theme="simple" cssClass="${sNoClearClass}" name="${pageControl}.pageSize"/>
	<@s.hidden theme="simple" cssClass="${sNoClearClass}" name="${pageControl}.lastPageSize" value="${pageSize?c}"/>
	<@s.hidden theme="simple" cssClass="${sNoClearClass}" name="${pageControl}.listSize"/>
	<@s.hidden theme="simple" cssClass="${sNoClearClass}" name="${pageControl}.pageSizeChanged"/>
</#if>

<#if (listSize > 0)>
	<#if (currentPage * pageSize > listSize)>
		<#assign endRecordNum=listSize>
	<#else>
		<#assign endRecordNum=(currentPage * pageSize)>
	</#if>
	<b>Results:&nbsp;showing&nbsp;</b>
	${((currentPage-1) * pageSize + 1)} - ${endRecordNum} of ${listSize}
	
	&nbsp;<b>${itemName}</b>&nbsp;-&nbsp;
	
	<b>Page:</b>&nbsp;&nbsp;

      <#if (currentPage < 11)>
         <#if (totalPages < 11)>
            <#assign endPage = totalPages>
         <#else>
            <#assign endPage=11>
         </#if>
         <#assign startPage=1>
      <#else>
	    <#if (currentPage + 5 > totalPages)>
		<#assign endPage=totalPages>
	    <#else>
		<#assign endPage=(currentPage + 5)>
	    </#if>
	    <#if (currentPage - 5 < 1)>
		<#assign startPage=1>
	    <#else>
		<#assign startPage=(currentPage - 5)>
	    </#if>
	 </#if>
	
	<#if (totalPages > 1 )>
	    <#if (currentPage != 1)>
	       <#if (startPage != 1)>
			 <a href="#" onclick="document.getElementById('${newPageFieldId}').value='1';${submitForm};return false;">First</a>
	    	 &nbsp;
	       </#if>
	    </#if>
	</#if>
	
	<#if (currentPage > 1)>
		<#assign prevPage=(currentPage - 1)>
		<a href="#" onclick="document.getElementById('${newPageFieldId}').value='${prevPage?c}';${submitForm};return false;">Prev</a>
		&nbsp;
	</#if>
			 
	<#list startPage..endPage as i>
		<#if i == currentPage>
			<span style="font-size: 10px; color: #003471;font-weight: bold;">${i}</span>
		<#else>
			<a href="#" onclick="document.getElementById('${newPageFieldId}').value='${i?c}';${submitForm};return false;">${i}</a>
		</#if>
	  	&nbsp;
	</#list>
	
	<#if (totalPages > currentPage)>
		<#assign nextPage=(currentPage + 1)>
		<a href="#" onclick="document.getElementById('${newPageFieldId}').value='${nextPage?c}';${submitForm};return false;">Next</a>
	</#if>
	
	<#if (totalPages > 1 )>
	    <#if (currentPage != totalPages)>
	       <#if (endPage != totalPages)>
	    		&nbsp;
				<a href="#" onclick="document.getElementById('${newPageFieldId}').value='${totalPages?c}';${submitForm};return false;">Last</a>
		   </#if>
		</#if>
	</#if>
	
<#else>
	<b>Unable to find any results matching specified criteria.</b>
</#if>

 
