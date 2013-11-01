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
			        			<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
			        			<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
			        	</tr>
			        	<tr>
							<td class="odd-bold">Line Item Ref #:</td>
			        			<td class="odd"><s:property value="item.customerReference"/></td>
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
			        			<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
			        			<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
						</tr>
						<tr>
							<td class="odd-bold">Pub Date:</td>
			        		<td class="odd"><s:property value="item.publicationYearOfUse"/></td>
							<td class="odd-bold">No. of Copies:</td>
							<s:if test="preAdjustmentItem != null">
		        				<td class="odd">
		        					<s:property value="preAdjustmentItem.numberOfCopies"/>
		        					<span style="background-color:pink; text-align: left;">/&nbsp;<s:property value="item.numberOfCopies"/></span>
		        				</td>
							</s:if>
							<s:else>
			        			<td class="odd"><s:property value="item.numberOfCopies"/></td>
			        		</s:else>
			        		<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
			        		<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
						</tr>
					</table>