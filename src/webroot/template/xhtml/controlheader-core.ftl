<#--
/*
 * $Id: controlheader-core.ftl 720258 2008-11-24 19:05:16Z musachy $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
-->
<#--
	Only show message if errors are available.
	This will be done if ActionSupport is used.
-->

<#if parameters.labelposition?default("") != 'top'>
	<#t/>
	<#t/><#-- labelposition is NOT top -->
	<#t/>
<#assign labelColumnSpan = parameters.labelcolspan?default(1) />
</#if>
<#assign hasFieldErrors = parameters.name?? && fieldErrors?? && fieldErrors[parameters.name]??/>
<#assign hasFieldErrors = false />
<#if hasFieldErrors>
	<#list fieldErrors[parameters.name] as error>
	<tr errorFor="${parameters.id}">
		<#if parameters.labelposition?default("") == 'top'>
    		<td align="left" valign="top" colspan="2"><#rt/>
		<#else>
    		<td align="center" valign="top" colspan="2"><#rt/>
		</#if>
    			<span class="errorMessage">${error?html}</span><#t/>
    		</td><#lt/>
	</tr>
	</#list>
</#if>
<#--
	if the label position is top,
	then give the label it's own row in the table
-->
<#if parameters.label??>
	<#if parameters.labelposition?default("") != 'top'>
    	<td class="${parameters.cssClass?default('tdLabel')?html}-bold" colspan="${labelColumnSpan}" ><#rt/>
	</#if>

    <label <#t/>
		<#if parameters.id??>
        	for="${parameters.id?html}" <#t/>
		</#if>
		<#if hasFieldErrors>
        	class="errorLabel"<#t/>
		<#else>
        	class="label"<#t/>
		</#if>
    ><#t/>
	<#if parameters.required?default(false) && parameters.requiredposition?default("right") != 'right'>
        <span class="required">*</span><#t/>
	</#if>
	${parameters.label?html}<#t/>
	<#if parameters.required?default(false) && parameters.requiredposition?default("right") == 'right'>
 		<span class="required">*</span><#t/>
	</#if>
		${parameters.labelseparator?default(":")?html}<#t/>
		<#include "/${parameters.templateDir}/xhtml/tooltip.ftl" /> 
	</label><#t/>
 	<#if parameters.labelposition?default("") != 'top'>
    </td><#lt/>
	</#if>
</#if>
