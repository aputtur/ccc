<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>
				<div
					style="width: 972px; border: 1px solid #808080; margin-top: 0px; margin-left: 10px; margin-right: 10px; margin-bottom: 10px; padding: 0px;">
<table class="editTable" width="100%" style="margin-botton: 0px;">
	<tr style="background-color: #E2E9F0;">
		<td valign="top" style="width: 5px; text-align: center;">
			&nbsp;
		</td>
		<td valign="middle" style="height: 26px; width: 959px;">
			<div id="sectionHeader"
				style="display: inline; padding-top: 2px; font-size: 14px;">
				Customer Information
			</div>
		</td>
	</tr>
</table>

<div id="order_confirmcontent" >
	<table class="editTable" id="order_viewheader" width="100%">
	    <col style="width: 30px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 30px;"/>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label cssClass="odd" cssStyle="width: 80px;" label="Licensee Name"
				name="Licensee Name" value="%{adjHeader.licenseeName}">
			</s:label>
			<s:label label="Address" cssClass="odd"
				cssStyle="width: 120px;" value="%{adjHeader.licenseeName}">
			</s:label>
			<td class="odd" style="width: 150px;">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Licensee Account Number" cssClass="odd"
				cssStyle="width: 120px;" value="%{adjHeader.licenseeAccount}">
			</s:label>
			<td class="odd">&nbsp;</td>
			<s:label name="add1Label" cssClass="odd"
				cssStyle="width: 120px;" value="%{adjHeader.user.mailingAddress.address1}">
			</s:label>
			<td class="odd">
				&nbsp;
			</td>
		</tr>
		<s:if test="adjHeader.user.mailingAddress.address2 != null">
		<tr>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<s:label name="add2Label" cssClass="odd"
				cssStyle="width: 120px;" value="%{adjHeader.user.mailingAddress.address2}">
			</s:label>
			<td class="odd">&nbsp;</td>

		</tr>
		</s:if>
		<s:if test="adjHeader.user.mailingAddress.address3 != null">
		<tr>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<s:label name="add3Label" cssClass="odd"
				cssStyle="width: 120px;" value="%{adjHeader.user.mailingAddress.address3}">
			</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
		</s:if>
		<s:if test="adjHeader.user.mailingAddress.address4 != null">
		<tr>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<s:label label="" cssClass="odd"
				cssStyle="width: 120px;" value="%{adjHeader.user.mailingAddress.address4}">
			</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
		</s:if>
		<tr>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<s:label name="cityLabel" cssClass="odd"
				cssStyle="width: 120px;" value="%{adjHeader.user.mailingAddress.city 
													+ ' , ' 
													+ adjHeader.user.mailingAddress.state
													+ ' ' 
													+ adjHeader.user.mailingAddress.postalCode}">
			</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<s:label name="countryLabel" cssClass="odd"
				cssStyle="width: 120px;" value="%{adjHeader.user.mailingAddress.country}">
			</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
	</table>
	<div class="odd-bold">
		<label style="text-align: right; font-weight:bold; color:#FF0000" class="odd-bold">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="adjHeader.detailsDistributionMessage"/></label>
	</div>
	<div class="odd-bold">
		<label style="text-align: right; font-weight:bold; color:#FF0000" class="odd-bold">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="adjHeader.detailsAdjustedMessage"/></label>
	</div>
	<div id="clearer"></div>
	<div id="clearer"></div>
</div>
</div>


