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
						<s:if test="item.email">
							<tr>
							<td class="odd-bold">Chapter/Article:</td>
			        			<td class="odd" colspan="3"><s:property value="item.chapterArticle"/></td>
							<td class="odd-bold">Date Used:</td>
			        			<td class="odd"><s:property value="item.dateOfUse"/></td>
							<td class="odd-bold">No. of Recipients:</td>
			        		<s:if test="preAdjustmentItem != null">
		        				<td class="odd">
		        					<s:property value="preAdjustmentItem.numberOfRecipients"/>
		        						<span style="background-color:pink; text-align: left;">/&nbsp;<s:property value="item.numberOfRecipients"/></span>
		        					</td>
							</s:if>
							<s:else>
			        			<td class="odd"><s:property value="item.numberOfRecipients"/></td>
			        		</s:else>
						</tr>
						<tr>
							<td class="odd-bold">Author:</td>
			        			<td class="odd" colspan="3"><s:property value="item.customAuthor"/></td>
							<td class="odd-bold">Pub Date:</td>
			        			<td class="odd"><s:property value="item.publicationYearOfUse"/></td>						
						</tr>
						<tr>
							<td class="odd-bold">Editor:</td>
			        			<td class="odd"><s:property value="item.editor"/></td>
						</tr>
						<tr>
							<td class="odd-bold">Line Item Ref #:</td>
			        			<td class="odd"><s:property value="item.customerReference"/></td>
						</tr>
						</s:if>
						<s:elseif test="item.internet">
							<tr>
							<td class="odd-bold">Chapter/Article:</td>
			        			<td class="odd" colspan="3"><s:property value="item.chapterArticle"/></td>
							<td class="odd-bold">Date Used:</td>
			        			<td class="odd"><s:property value="item.dateOfUse"/></td>
							<td class="odd-bold">Duration:</td>
				        	<s:if test="preAdjustmentItem != null">
			        			<td class="odd">
			        				<s:property value="preAdjustmentItem.durationString"/>
		        					<span style="background-color:pink; text-align: left;">/&nbsp;<s:property value="item.durationString"/></span>
		        				</td>
							</s:if>
							<s:else>
				        		<td class="odd"><s:property value="item.durationString"/></td>
				        	</s:else>
						</tr>
						<tr>
							<td class="odd-bold">Author:</td>
			        			<td class="odd" colspan="3"><s:property value="item.customAuthor"/></td>
							<td class="odd-bold">Pub Date:</td>
			        			<td class="odd"><s:property value="item.publicationYearOfUse"/></td>
						</tr>
						<tr>
							<td class="odd-bold">Editor:</td>
			        			<td class="odd" colspan="3"><s:property value="item.editor"/></td>
							<td class="odd-bold">Web Address:</td>
			        			<td class="odd" colspan="3"><s:property value="item.webAddress"/></td>
			        			
						</tr>
						<tr>
							<td class="odd-bold">Line Item Ref #:</td>
			        			<td class="odd"><s:property value="item.customerReference"/></td>
						</tr>
						</s:elseif>
						<s:else> <!-- Intranet/Extranet -->
							<tr>
								<td class="odd-bold">Chapter/Article:</td>
			        			<td class="odd" colspan="3"><s:property value="item.chapterArticle"/></td>
								<td class="odd-bold">Date Used:</td>
			        			<td class="odd"><s:property value="item.dateOfUse"/></td>
								<td class="odd-bold">Duration:</td>
				        		<s:if test="preAdjustmentItem != null">
			        				<td class="odd">
			        					<s:property value="preAdjustmentItem.durationString"/>
		        						<span style="background-color:pink; text-align: left;">/&nbsp;<s:property value="item.durationString"/></span>
		        					</td>
								</s:if>
								<s:else>
				        			<td class="odd"><s:property value="item.durationString"/></td>
				        		</s:else>
			        		</tr>
						<tr>
							<td class="odd-bold">Author:</td>
			        			<td class="odd" colspan="3"><s:property value="item.customAuthor"/></td>
							<td class="odd-bold">Pub Date:</td>
			        			<td class="odd"><s:property value="item.publicationYearOfUse"/></td>
						</tr>
						<tr>
							<td class="odd-bold">Editor:</td>
			        			<td class="odd"><s:property value="item.editor"/></td>
						</tr>
						<tr>
							<td class="odd-bold">Line Item Ref #:</td>
			        			<td class="odd"><s:property value="item.customerReference"/></td>
						</tr>
						</s:else>
						
					</table>
