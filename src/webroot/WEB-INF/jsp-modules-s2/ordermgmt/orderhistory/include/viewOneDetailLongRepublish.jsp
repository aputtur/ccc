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
							<td class="odd-bold">Content Type:</td>
			        			<td class="odd"><s:property value="item.typeOfContent"/></td>
			        			<td class="odd-bold" colspan="3">&nbsp;</td>
								<td class="odd-bold" colspan="2">Is perm requestor original author:</td>
								<td class="odd"><s:property value="item.submitterAuthor"/></td>
							</tr>
							<tr>
							<td class="odd-bold">Pub Date:</td>
			        			<td class="odd" colspan="3"><s:property value="item.publicationDateOfUse"/></td>
							<td class="odd-bold">Content Desc:</td>
			        			<td class="odd"><s:property value="item.chapterArticle"/></td>
							<td class="odd-bold">Page Range:</td>
			        			<td class="odd"><s:property value="item.pageRange"/></td>
						</tr>
						<tr>
							<td class="odd-bold">Author:</td>
			        			<td class="odd" colspan="3"><s:property value="item.customAuthor"/></td>
							<td class="odd-bold">Language:</td>
			        			<td class="odd"><s:property value="item.translationLanguage"/></td>
			        		
			        		<s:if test="contentSelectedPages"> <!-- Selected pages -->
			        			<td class="odd-bold">No. of Pages:</td>
			        			<td class="odd"><s:property value="item.numberOfPages"/></td>
			        		</s:if>
			        		<s:else>
			        			<td class="odd-bold" colspan="2">&nbsp;</td>
			        		</s:else>
						</tr>
						<tr>
							<td class="odd-bold">Line Item Ref #:</td>
			        			<td class="odd" colspan="3"><s:property value="item.customerReference"/></td>						
						</tr>
					</table>