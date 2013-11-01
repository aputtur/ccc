<#--
Uses: viewtopmenu.ftl
	
Fields:
	-hrefValue: value of href
	-submitAction: sURL action to be submitted
	-submitValue: sURL value
	-submitNamespace: action name space with form, and submits and rendered button has type="submit".
	-submitInstance: current iterator status index
	-hrefTarget: href target
	-hrefId: href id
-->
<#assign showLink = true />

<#if showLink == true >
<li>
        <#if parameters.submitAction?exists>
        
		<a class="editDetailAngleTabs" <#if parameters.hrefId?exists>id="${parameters.hrefId}_${item.ID?c}" </#if>
		 href="#" <#if parameters.hrefTarget?exists>target="${parameters.hrefTarget}"</#if>
		 onclick="submitDetailTopMenuForm_${parameters.submitInstance}('<@s.url action="${parameters.submitAction}" 
		 namespace="${parameters.submitNamespace}"
		 includeParams="none"/>');return false;">
        ${parameters.hrefValue?html}
        </a><span><img align="top" border="0" width="19" height="26" src="<@s.url value="/resources/ordermgmt/images/spacer.gif"/>"></img></span> 
        <#else>
        	<#if parameters.submitValue?exists>
        	<a class="editDetailAngleTabs" <#if parameters.hrefId?exists>id="${parameters.hrefId}_${item.ID?c}" </#if>
        	 href="#" <#if parameters.hrefTarget?exists>target="${parameters.hrefTarget}"</#if>
       	     onclick="submitDetailTopMenuForm_${parameters.submitInstance}('<@s.url value="${parameters.submitValue}" includeParams="none"/>');return false;">
        	${parameters.hrefValue?html}
        	</a><span><img align="top" border="0" width="19" height="26" src="<@s.url value="/resources/ordermgmt/images/spacer.gif"/>"></img></span>
       		</#if> 	
        </#if>
</li>
</#if>