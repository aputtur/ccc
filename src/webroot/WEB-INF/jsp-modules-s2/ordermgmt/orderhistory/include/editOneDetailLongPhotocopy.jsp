<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

			<table class="subTableNoColor" style="background-color: #EAE8E8;"
				width="100%">
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<tr>
					<th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
						Customer Entered Information
					</th>
				</tr>
				<tr>
					<td class="odd-bold">
						Chapter/Article:
					</td>
					<td class="odd" colspan="7">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 440px;"
									theme="simple" name="editItem.chapterArticle"
									value="%{lastEdit.item.chapterArticle}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 440px;" name="editItem.chapterArticle"
									value="%{item.chapterArticle}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.chapterArticle" />
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="odd-bold">
						Line Item Ref #:
					</td>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 90px;"
									theme="simple" name="editItem.customerReference"
									value="%{lastEdit.item.customerReference}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 90px;" name="editItem.customerReference"
									value="%{item.customerReference}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.customerReference" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							No. of Pages:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							No. of Pages:
						</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID} required"
									cssStyle="width: 90px;" theme="simple"
									name="editItem.numberOfPages"
									value="%{lastEdit.item.numberOfPages}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 90px;" name="editItem.numberOfPages"
									value="%{item.numberOfPages}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.numberOfPages" />
						</s:else>
					</td>
				</tr>
				<tr>
					<s:if test="editable">
						<td class="required-bold">
							Pub Date:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Pub Date:
						</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssStyle="width: 90px;"
									cssClass="itype%{item.ID} required" theme="simple"
									name="editItem.publicationYearOfUse"
									value="%{lastEdit.item.publicationYearOfUse}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 90px;" name="editItem.publicationYearOfUse"
									value="%{item.publicationYearOfUse}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.publicationYearOfUse" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							No. of Copies:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							No. of Copies:</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID} required"
									cssStyle="width: 90px;" theme="simple"
									name="editItem.numberOfCopies"
									value="%{lastEdit.item.numberOfCopies}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 90px;" name="editItem.numberOfCopies"
									value="%{item.numberOfCopies}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.numberOfCopies" />
						</s:else>
					</td>
				</tr>
			</table>
