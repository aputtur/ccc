<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

	<table class="subTableNoColor" style="background-color: #EAE8E8;"
		width="100%">
		<col style="width: 100px;" />
		<col style="width: 100px;" />
		<col style="width: 100px;" />
		<col style="width: 70px;" />
		<col style="width: 70px;" />
		<col style="width: 100px;" />
		<col style="width: 130px;" />
		<col style="width: 130px;" />
		<tr>
			<th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
				Customer Entered Information
			</th>
		</tr>
		<tr>		
		<s:if test="editable">
				<td class="required-bold">
					Content Type:
				</td>
			</s:if>
			<s:else>
				<td class="odd-bold">
					Content Type:
				</td>
			</s:else>
			<td class="odd" colspan="4">
				<s:if test="editable">
					<s:if test="hasLastEdit">
						<s:select list="contentTypeList" headerKey=""
							headerValue="-- Select One --" listKey="value" listValue="label"
							cssStyle="width: 95px;" cssClass="itype%{item.ID} required"
							theme="simple" name="editItem.typeOfContent"
							value="%{lastEdit.item.typeOfContent}" />
					</s:if>
					<s:else>
						<s:select list="contentTypeList" headerKey=""
							headerValue="-- Select One --" listKey="value" listValue="label"
							cssClass="itype%{item.ID} required" theme="simple"
							cssStyle="width: 95px;" name="editItem.typeOfContent"
							value="%{item.typeOfContent}" />
					</s:else>
				</s:if>
				<s:else>
					<s:property value="item.typeOfContentDescription" />
				</s:else>
			</td>
			
			<s:if test="editable">
				<td class="required-bold" colspan="2">
					Is perm requestor original author?
				</td>
			</s:if>
			<s:else>
				<td class="odd-bold" colspan="2">
					Is perm requestor original author?
				</td>
			</s:else>
			
			<td class="odd">
				<s:if test="editable">
					<s:if test="hasLastEdit">
						<s:select list="submitterAuthorList" headerKey=""
							headerValue="-- Select One --" listKey="value" listValue="label"
							cssStyle="width: 100px;" cssClass="itype%{item.ID} required"
							theme="simple" name="editItem.submitterAuthor"
							value="%{lastEdit.item.submitterAuthor}" />
					</s:if>
					<s:else>
						<s:select list="submitterAuthorList" headerKey=""
							headerValue="-- Select One --" listKey="value" listValue="label"
							cssClass="itype%{item.ID} required" theme="simple"
							cssStyle="width: 100px;" name="editItem.submitterAuthor"
							value="%{item.submitterAuthor}" />
					</s:else>
				</s:if>
				<s:else>
					<s:property value="item.submitterAuthor" />
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
						<s:textfield cssStyle="width: 70px;"
							cssClass="itype%{item.ID} required" theme="simple"
							name="editItem.publicationDateOfUse"
							value="%{lastEdit.item.publicationDateOfUse}" />
					</s:if>
					<s:else>
						<s:textfield cssClass="itype%{item.ID} required" theme="simple"
							cssStyle="width: 70px;" name="editItem.publicationDateOfUse"
							value="%{item.publicationDateOfUse}" />
					</s:else>
				</s:if>
				<s:else>
					<s:property value="item.publicationYearOfUse" />
				</s:else>
			</td>
			<s:if test="editable">
				<td class="required-bold" colspan="3">
					Content Desc:
				</td>
			</s:if>
			<s:else>
				<td class="odd-bold" colspan="3">
					Content Desc:
				</td>
			</s:else>
			<td class="odd">
				<s:if test="editable">
					<s:if test="hasLastEdit">
						<s:textfield cssStyle="width: 150px;" cssClass="itype%{item.ID} required"
							theme="simple" name="editItem.chapterArticle"
							value="%{lastEdit.item.chapterArticle}" />
					</s:if>
					<s:else>
						<s:textfield cssClass="itype%{item.ID} required" theme="simple"
							cssStyle="width: 150px;" name="editItem.chapterArticle"
							value="%{item.chapterArticle}" />
					</s:else>
				</s:if>
				<s:else>
					<s:property value="item.chapterArticle" />
				</s:else>
			</td>
			<s:if test="editable">
				<td class="required-bold">
					Page Range:
				</td>
			</s:if>
			<s:else>
				<td class="odd-bold">
					Page Range:
				</td>
			</s:else>
			<td class="odd">
				<s:if test="editable">
					<s:if test="hasLastEdit">
						<s:textfield cssStyle="width: 70px;" cssClass="itype%{item.ID} required"
							theme="simple" name="editItem.pageRange"
							value="%{lastEdit.item.pageRange}" />
					</s:if>
					<s:else>
						<s:textfield cssClass="itype%{item.ID} required" theme="simple"
							cssStyle="width: 70px;" name="editItem.pageRange"
							value="%{item.pageRange}" />
					</s:else>
				</s:if>
				<s:else>
					<s:property value="item.pageRange" />
				</s:else>
			</td>
		</tr>
		<tr>
		<s:if test="editable">
				<td class="required-bold">
					Author:
				</td>
			</s:if>
			<s:else>
				<td class="odd-bold">
					Author:
				</td>
			</s:else>
			<td class="odd" >
				<s:if test="editable">
					<s:if test="hasLastEdit">
						<s:textfield cssStyle="width: 150px;" cssClass="itype%{item.ID} required"
							theme="simple" name="editItem.customAuthor"
							value="%{lastEdit.item.customAuthor}" />
					</s:if>
					<s:else>
						<s:textfield cssClass="itype%{item.ID} required" theme="simple"
							cssStyle="width: 150px;" name="editItem.customAuthor"
							value="%{item.customAuthor}" />
					</s:else>
				</s:if>
				<s:else>
					<s:property value="item.customAuthor" />
				</s:else>
			</td>
			


			<td class="odd-bold"  colspan="3">
				Language:
			</td>
			<td class="odd">
				<s:if test="editable">
					<s:if test="hasLastEdit">
						<s:select list="translatedLanguageList" headerKey=""
							headerValue="-- Select One --" listKey="value" listValue="label"
							cssStyle="width: 95px;" cssClass="itype%{item.ID}"
							theme="simple" name="editItem.translationLanguage"
							value="%{lastEdit.item.translationLanguage}" />
					</s:if>
					<s:else>
						<s:select list="translatedLanguageList" headerKey=""
							headerValue="-- Select One --" listKey="value" listValue="label"
							cssClass="itype%{item.ID}" theme="simple"
							cssStyle="width: 95px;" name="editItem.translationLanguage"
							value="%{item.translationLanguage}" />
					</s:else>
				</s:if>
				<s:else>
					<s:property value="translationLanguageValue" />
				</s:else>
			</td>

		<s:if test="contentSelectedPages">
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
						<s:textfield cssStyle="width: 70px;" cssClass="itype%{item.ID} required"
							theme="simple" name="editItem.numberOfPages"
							value="%{lastEdit.item.numberOfPages}" />
					</s:if>
					<s:else>
						<s:textfield cssClass="itype%{item.ID} required" theme="simple"
							cssStyle="width: 70px;" name="editItem.numberOfPages"
							value="%{item.numberOfPages}" />
					</s:else>
				</s:if>
				<s:else>
					<s:property value="item.numberOfPages" />
				</s:else>
			</td>
			</s:if>
			<s:else>
				<td class="odd-bold">
					No. of Pages:
				</td>
				<td class="odd">
					<s:if test="editable">
						<s:if test="hasLastEdit">
							<s:textfield disabled="true" cssStyle="width: 70px;" cssClass="itype%{item.ID}"
								theme="simple" name="editItem.numberOfPages"
								value="%{lastEdit.item.numberOfPages}" />
						</s:if>
						<s:else>
							<s:textfield disabled="true" cssClass="itype%{item.ID}" theme="simple"
								cssStyle="width: 70px;" name="editItem.numberOfPages"
								value="%{item.numberOfPages}" />
						</s:else>
					</s:if>
					<s:else>
						<s:property value="item.numberOfPages" />
					</s:else>
				</td>
			</s:else>
		</tr>
		
		<tr>
			<td class="odd-bold">
						Line Item Ref #:
					</td>
					<td class="odd" >
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
		</tr>
	</table>

