<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<s:if test="!processMessages.empty">
<table width="100%">
	<tr>
		<td style="width: 40px;"></td>
		<td align="center">
		    <fieldset style="width: 800px;">
		    <legend><span style="font-weight: bold;">Processing Messages</span></legend>
			<ul style="text-align: left;">
			<s:iterator value="processMessages">
			  <s:if test="error">
			   <li style="color: red; font-weight: bold;">
			       <s:property value="briefFormattedMessage"/>
			   </li>
			  </s:if>
			  <s:else>
			   <li style="color: blue; font-weight: bold;">
			       <s:property value="briefMessage"/>
			   </li>
			   </s:else>
			</s:iterator>
		 	</ul>
		 	</fieldset>
		</td>
		<td style="width: 40px;"></td>
	</tr>
</table>
</s:if>

<s:if test="bundleRLS">

<table
	id="order_course_<s:property value="bundle.bundleId"/>_viewheader"
	class="editTable">
	    <col style="width: 10px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 220px;"/>
	    <col style="width: 10px;"/>
	<tr>
		<td class="odd" colspan="6" align="right">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="odd">
			&nbsp;
		</td>
		<s:label cssClass="odd" 
			label="Republication Date" value="%{'06/01/1992'}">
		</s:label>
		<s:label label="Republication Vol/Edition" cssClass="odd"
			value="%{'12:3'}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
		<s:label label="Republication Publisher" cssClass="odd"
			value="%{'CCC Publishing Co.'}">
		</s:label>
		<s:label label="Circulation/Distribution" cssClass="odd"
			value="%{'20,000'}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="odd">
			&nbsp;
		</td>
		<s:label label="For Profit" cssClass="odd"
			value="%{'Yes'}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
		<td class="odd">
			&nbsp;
		</td>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
		<s:label label="Comments" cssClass="odd"
			value="%{bundle.comments}">
		</s:label>
		<s:label label="Course Number" cssClass="odd"
			value="%{bundle.courseNumber}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan="6">
			&nbsp;
		</td>
	</tr>
</table>
</s:if>

<s:else>

<table
	id="order_course_<s:property value="bundle.bundleId"/>_viewheader"
	class="editTable">
	    <col style="width: 10px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 10px;"/>
	<tr>
		<td class="odd" colspan="6" align="right">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="odd">
			&nbsp;
		</td>
		<s:label cssClass="odd"
			label="Est. No. of Students" value="%{bundle.estimatedCopies}">
		</s:label>
		<s:label label="Instructor Name" cssClass="odd" 
			value="%{bundle.instructor}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
		<s:label cssClass="odd"
			label="No. of Students" value="%{bundle.numberOfStudents}">
		</s:label>
		<!--<s:label label="Contact_old" cssClass="odd"
			value="%{bundle.yourReference}">
		</s:label> -->
		<s:label label="Contact" cssClass="odd"
			value="%{bundle.orderEnteredBy}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="odd">
			&nbsp;
		</td>
		<s:label label="University / Institution" cssClass="odd"
			value="%{bundle.organization}">
		</s:label>
		<s:label label="Course Name" cssClass="odd"
			value="%{bundle.courseName}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
		<s:label name="startTermDate" label="Start of Term" cssClass="odd"
			value="%{bundle.startOfTermStr}">
		</s:label>
		<s:label label="Course Number" cssClass="odd"
			value="%{bundle.courseNumber}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="odd">
			&nbsp;
		</td>
		<s:label label="Comments" cssClass="odd"
			value="%{bundle.comments}">
		</s:label>
		<s:label label="Document Reference Number" cssClass="odd"
				cssStyle="width: 120px;" value="%{bundle.yourReference}">
		</s:label>
		<td class="odd">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
		<td class="odd">
			&nbsp;
		</td>
		<td class="odd">
			&nbsp;
		</td>
		<s:label label="Accounting Ref Number" cssClass="odd"
				cssStyle="width: 120px;" value="%{bundle.accountingReference}">
		</s:label>
	</tr>
	<!-- <tr>
		<td colspan="6">
			&nbsp;
		</td>
	</tr> -->
</table>

</s:else>

