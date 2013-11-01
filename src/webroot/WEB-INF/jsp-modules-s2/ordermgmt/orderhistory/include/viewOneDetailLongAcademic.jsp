<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

				<table class="subTableNoColor" style="background-color: #EAE8E8;"
						width="100%">
					<col style="width: 100px;"/>
					<col style="width: 100px;"/>
					<col style="width: 100px;"/>
					<col style="width: 100px;"/>
					<col style="width: 100px;"/>
					<col style="width: 100px;"/>
					<col style="width: 100px;"/>
					<col style="width: 100px;"/>
						<tr><th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
							Customer Entered Information
						</th></tr>
						<tr>
							<td class="odd-bold">Chapter/Article:</td>
			        			<td class="odd" colspan="3"><s:property value="item.chapterArticle"/></td>
							<td class="odd-bold">Line Item Ref #:</td>
			        			<td class="odd"><s:property value="item.customerReference"/></td>
							<td class="odd-bold">Page Range:</td>
			        			<td class="odd"><s:property value="item.pageRange"/></td>
						</tr>
						<tr>
							<td class="odd-bold">Author:</td>
			        			<td class="odd" colspan="3"><s:property value="item.customAuthor"/></td>
							<td class="odd-bold">Date of Issue:</td>
			        			<td class="odd"><s:property value="item.dateOfIssue"/></td>
							<td class="odd-bold">No. of Pages:</td>
			        		<s:if test="preAdjustmentItem != null">
		        				<td class="odd">
		        					<s:property value="preAdjustmentItem.numberOfPages"/>
		        						<span style="background-color:pink; text-align: left;">/&nbsp;<s:property value="item.numberOfPages"/></span>
		        					</td>
							</s:if>
							<s:else>
			        			<td class="odd"><s:property value="item.numberOfPages"/></td>
			        		</s:else>
						</tr>
						<tr>
							<td class="odd-bold">Editor:</td>
			        			<td class="odd" colspan="3"><s:property value="item.editor"/></td>
							<td class="odd-bold">Pub Date:</td>
			        			<td class="odd"><s:property value="item.publicationYearOfUse"/></td>
							<td class="odd-bold">No. of Students:</td>	
			        		<s:if test="preAdjustmentItem != null">
		        				<td class="odd">
		        					<s:property value="preAdjustmentItem.numberOfStudents"/>
		        						<span style="background-color:pink; text-align: left;">/&nbsp;<s:property value="item.numberOfCopies"/></span>
		        					</td>
							</s:if>
							<s:else>
			        			<td class="odd"><s:property value="item.numberOfStudents"/></td>
			        		</s:else>
						</tr>
						<tr>
							<td class="odd-bold">Edition:</td>
			        			<td class="odd" colspan="3"><s:property value="item.customEdition"/></td>
							<td class="odd-bold">Volume:</td>
			        			<td class="odd"><s:property value="item.customVolume"/></td>
			        		<td class="odd-bold">Reuse Entire Book:</td>
			        		
			        			<s:if test="item.licenseeRequestedEntireWork == true">
			        				<td class="odd"> Y </td>
			        			</s:if>

			        			
			        			<s:if test="item.licenseeRequestedEntireWork == false">
			        				<td class="odd"> N </td>
			        			</s:if>
						</tr>
				 </table>
