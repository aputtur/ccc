<#--
Uses: viewtopmenu.ftl
	
Fields:
	-hrefValue: value of href
	-submitAction: sURL action to be submitted
	-submitValue: sURL value
	-submitNamespace: action name space with form, and submits and rendered button has type="submit".
	-hrefTarget: href target
	-hrefId: href id
	-hrefOnClick: script to add to onclick
	
-->
<#assign showLink = true />

<#if showLink == true >
<li <#if parameters.hrefId?exists>id="${parameters.hrefId}_li"</#if> >
        <#if parameters.submitAction?exists>
		<a class="angleTabs" href="#" <#if parameters.hrefTarget?exists>target="${parameters.hrefTarget}"</#if>
		 <#if parameters.hrefId?exists>id="${parameters.hrefId}_href"</#if>
		 onclick="submitViewTopMenuForm('<@s.url action="${parameters.submitAction}" 
		 namespace="${parameters.submitNamespace}"
		 includeParams="none"/>');return false;">
        ${parameters.hrefValue?html}</a><span><img align="top" border="0" width="19" height="26" src="<@s.url value="/resources/ordermgmt/images/spacer.gif"/>"></img></span> 
        <#else>
        	<#if parameters.submitValue?exists>
        	<a class="angleTabs" href="#" <#if parameters.hrefTarget?exists>target="${parameters.hrefTarget}"</#if>
 		    <#if parameters.hrefId?exists>id="${parameters.hrefId}_href"</#if>
        	 onclick="submitViewTopMenuForm('<@s.url value="${parameters.submitValue}" includeParams="none"/>');return false;">
        	${parameters.hrefValue?html}</a><span><img align="top" border="0" width="19" height="26" src="<@s.url value="/resources/ordermgmt/images/spacer.gif"/>"></img></span>
       		<#else>
       			<#if parameters.hrefOnClick?exists>
        		<a class="angleTabs" href="#" <#if parameters.hrefTarget?exists>target="${parameters.hrefTarget}"</#if>
 		    	<#if parameters.hrefId?exists>id="${parameters.hrefId}_href"</#if>
        	 		onclick="${parameters.hrefOnClick};;return false;">
        			${parameters.hrefValue?html}</a><span><img align="top" border="0" width="19" height="26" src="<@s.url value="/resources/ordermgmt/images/spacer.gif"/>"></img></span>
       			</#if>
       		</#if>
       		 	
        </#if>
</li>
</#if>