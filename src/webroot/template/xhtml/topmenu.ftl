<#--
Uses: topmenu.ftl
	
Fields:
	-tabValue: value of selected tab
	-submitAction: sURL action to be submitted
	-submitValue: sURL value
	-submitNamespace: action name space with form, and submits and rendered button has type="submit".
	-hrefTarget: href target
	-userCountEnabled: true/false whether should be showing user counts (n)
	-userCount: user's counts
-->
<#assign userCountEnabled = parameters.userCountEnabled?default(false) />
<#assign userCountText = '' />
<#assign userCountValue = 0 />
<#assign showLink = true />

<#if userCountEnabled == true >
	<#assign userCountValue = parameters.userCount?default(0) /> 
	<#if userCountValue &gt; 0 >
	   <#assign userCountText = '(' + userCountValue + ')' />
	<#else>
	   <#assign showLink = false />
	</#if>	
</#if>

<#if showLink == true >
<li <@s.if test='currentQuickTab=="${parameters.tabValue}"'>class="first selected"</@s.if><@s.else>
class="first"</@s.else>>
        <#if parameters.submitAction?exists>
		<a href="#" <#if parameters.hrefTarget?exists>target="${parameters.hrefTarget}"</#if>
		 onclick="submitTopMenuForm('<@s.url action="${parameters.submitAction}" 
		 namespace="${parameters.submitNamespace}"
		 includeParams="none"/>','${parameters.tabValue}');return false;">
        <@s.text name="${parameters.tabValue}"/>${userCountText}</a> 
        <#else>
        	<#if parameters.submitValue?exists>     		
		        	<a href="<@s.url value="${parameters.submitValue}" includeParams="none"/>" 
		        	<#if parameters.hrefTarget?exists>target="${parameters.hrefTarget}"</#if>
		        	 >
		        	<@s.text name="${parameters.tabValue}"/></a>        	
       		</#if> 	
        </#if>
</li>
</#if>