<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<s:form theme="simple" id="editOneBundle_%{bundle.bundleId}" action="viewOrderHistory!saveBundleHeader" namespace="/om/history" method="post">
	<s:hidden theme="simple" name="selectedConfirmNumber" value="%{searchConfirmNumber}"/>
	<s:hidden theme="simple" name="selectedBundleNumber" value="%{searchBundleNumber}"/>
	<s:hidden theme="simple" name="selectedDetailNumber" value="%{searchDetailNumber}"/>
	<s:hidden theme="simple" name="selectedBundleId" value="%{bundle.bundleId}"></s:hidden>
	<s:hidden theme="simple" name="editBundles[%{#bStatus.index}].bundleId" value="%{bundle.bundleId}"/>
   	<s:if test="hasLastEdit">
		<input type="hidden" id='changedbundle<s:property value="bundle.bundleId"/>' value="1"/>
   	</s:if>
   	<s:else>
		<input type="hidden" id='changedbundle<s:property value="bundle.bundleId"/>' value="0"/>
   	</s:else>
<table
	id="order_course_<s:property value="bundle.bundleId"/>_editheader"
	class="editTable" style="display: none;">
	    <col style="width: 10px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 320px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 320px;"/>
	    <col style="width: 10px;"/>

	<tr>
		<td colspan="6" align="right">
			<s:submit cssStyle="odd" theme="simple" value="Save" onclick="saveOneBundle('%{bundle.bundleId}', '%{courseNameJSP}'); return false;"></s:submit>
			<input type="button"
				onclick="cancelCourseEditHeader('<s:property value="bundle.bundleId"/>','<s:property value="courseNameJSP"/>');return false;"
				class="odd" value="Discard"/>
		</td>
	</tr>
	<tr>

		<td class="odd">&nbsp;</td>
		<td class="odd-bold">Est. No. of Students:&nbsp;
			<s:property value="bundle.estimatedCopies" />
		</td>
		<td class="odd">&nbsp;</td>		
		<td class="odd-bold">Instructor Name:&nbsp;</td>
		<td class="odd">
			<s:if test="hasLastEdit">
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].instructor"
					value="%{lastEdit.instructor}" />
			</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
					cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].instructor"
					value="%{bundle.instructor}" />
			</s:else>
		</td>
		<td class="odd">&nbsp;</td>
	</tr>
	<tr>
		<td class="odd">&nbsp;</td>
		<td class="required-bold">No. of Students:&nbsp;</td>
		<td class="required">
			<s:if test="hasLastEdit">
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].numberOfStudents"
					value="%{lastEdit.numberOfStudents}" />			
				</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId} required"
					cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].numberOfStudents"
					value="%{bundle.numberOfStudents}" />
			</s:else>
		</td>
		<td class="odd-bold">Contact:&nbsp;</td>
		<td class="odd">
			<s:if test="hasLastEdit">
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].orderEnteredBy"
					value="%{lastEdit.orderEnteredBy}" />
			</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
					cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].orderEnteredBy"
					value="%{bundle.orderEnteredBy}" />
			</s:else>
		</td>
		<td class="odd">&nbsp;</td>
	</tr>
	<tr>
		<td class="odd">&nbsp;</td>
		<td class="required-bold">University / Institution:&nbsp;</td>
		<td class="required">
			<s:if test="hasLastEdit">
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].organization"
					value="%{lastEdit.organization}" />			
				</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId} required"
					cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].organization"
					value="%{bundle.organization}" />
			</s:else>
		</td>
		<td class="required-bold">Course Name:&nbsp;</td>
		<td class="required">
			<s:if test="hasLastEdit">
				<s:textfield cssClass="ebtype%{bundle.bundleId} required"
				    cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].courseName"
					value="%{lastEdit.courseName}" />
			</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId} required"
				    cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].courseName"
					value="%{bundle.courseName}" />
			</s:else>
		</td>
		<td class="odd">&nbsp;</td>
	</tr>
	<tr>
		<td class="odd">&nbsp;</td>
		<td class="required-bold">Start of Term:</td>
		<td class="required">
			<s:if test="hasLastEdit">
				<s:textfield cssClass="ebtype%{bundle.bundleId} required"
				    id="startOfTermStr_%{bundle.bundleId}"
				    cssStyle="width: 70px;"
					theme="simple" name="editBundles[%{#bStatus.index}].startOfTermStr"
					value="%{lastEdit.startOfTermStr}" />
			</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId} required"
				    id="startOfTermStr_%{bundle.bundleId}"
				    cssStyle="width: 70px;"
					theme="simple" name="editBundles[%{#bStatus.index}].startOfTermStr"
					value="%{bundle.startOfTermStr}" />
			</s:else>

	<s:component theme="xhtml" template="jquerydatepicker">
		<s:param name="sDatePickId" value="'startOfTermStr'" />
		<s:param name="sDatePickInstId" value="bundle.bundleId" />
	</s:component>
		
		</td>
		<td class="odd-bold">Course Number:&nbsp;</td>
		<td class="odd">
			<s:if test="hasLastEdit">
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].courseNumber"
					value="%{lastEdit.courseNumber}" />
			</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
					cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].courseNumber"
					value="%{bundle.courseNumber}" />
			</s:else>
		</td>
		<td class="odd">&nbsp;</td>

	</tr>	
	<tr>
		<td class="odd">&nbsp;</td>
		<td class="odd-bold">Comments:&nbsp;</td>
		<td class="odd">
			<s:if test="hasLastEdit">
				<s:textarea cols="3" cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 280px;"
					theme="simple" name="editBundles[%{#bStatus.index}].comments"
					value="%{lastEdit.comments}" />
			</s:if>
			<s:else>
				<s:textarea cols="3" cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 280px;"
					theme="simple" name="editBundles[%{#bStatus.index}].comments"
					value="%{bundle.comments}" />
			</s:else>
		</td>
		<td class="odd-bold" >Document Ref Number:&nbsp;</td>
		<td class="odd">
			<s:if test="hasLastEdit">
				<s:textarea cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 280px;"
					theme="simple" name="editBundles[%{#bStatus.index}].yourReference"
					value="%{lastEdit.comments}" />
			</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
					cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].yourReference"
					value="%{bundle.yourReference}" />
			</s:else>
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
		<td class="odd-bold">Accounting Ref Number:&nbsp;</td>
		<td class="odd">
			<s:if test="hasLastEdit">
				<s:textarea cssClass="ebtype%{bundle.bundleId}"
				    cssStyle="width: 280px;"
					theme="simple" name="editBundles[%{#bStatus.index}].accountingReference"
					value="%{lastEdit.accountingReference}" />
			</s:if>
			<s:else>
				<s:textfield cssClass="ebtype%{bundle.bundleId}"
					cssStyle="width: 200px;"
					theme="simple" name="editBundles[%{#bStatus.index}].accountingReference"
					value="%{bundle.accountingReference}" />
			</s:else>
		</td>
	</tr>
	
	<tr>
		<td colspan="6">&nbsp;</td>
	</tr>
</table>
</s:form>