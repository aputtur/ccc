<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

			<table class="subTableNoColor" style="background-color: #EAE8E8;"
				width="100%">
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<col style="width: 100px;" />
				<col style="width: 90px;" />
				<col style="width: 90px;" />
				<col style="width: 90px;" />
				<col style="width: 130px;" />
				<col style="width: 100px;" />
				<tr>
					<th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
						Customer Entered Information
					</th>
				</tr>
				<s:if test="item.email">
				<tr>
					<td class="odd-bold">
						Chapter/Article:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 340px;"
									theme="simple" name="editItem.chapterArticle"
									value="%{lastEdit.item.chapterArticle}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 340px;" name="editItem.chapterArticle"
									value="%{item.chapterArticle}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.chapterArticle" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							Date Used:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Date Used:</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield 
									cssStyle="width: 70px;" cssClass="itype%{item.ID} required"
									theme="simple" name="editItem.dateOfUse"
									value="%{lastEdit.item.dateOfUse}" />
							</s:if>
							<s:else>
								<s:textfield 
									cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 70px;" name="editItem.dateOfUse"
									value="%{item.dateOfUse}" />
							</s:else>
							<s:component theme="xhtml" template="jquerydatepickerThreeParams.ftl">
								<s:param name="sDatePickId" value="'savedetailform'" />
								<s:param name="sDatePickInstId" value="item.ID" />
								<s:param name="sDatePickerFieldName" value="'editItem_dateOfUse'" />
							</s:component>
						</s:if>
						<s:else>
							<s:property value="item.dateOfUse" />
						</s:else>

					</td>
					<s:if test="editable">
						<td class="required-bold">
							No. of Recipients:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							No. of Recipients:
						</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssStyle="width: 90px;"
									cssClass="itype%{item.ID} required" theme="simple"
									name="editItem.numberOfRecipients"
									value="%{lastEdit.item.numberOfRecipients}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 90px;" name="editItem.numberOfRecipients"
									value="%{item.numberOfRecipients}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.numberOfRecipients" />
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="odd-bold">
						Author:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 240px;"
									theme="simple" name="editItem.customAuthor"
									value="%{lastEdit.item.customAuthor}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 240px;" name="editItem.customAuthor"
									value="%{item.customAuthor}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.customAuthor" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							Pub Date:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Pub Date:</td>
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
				</tr>
				<tr>
					<td class="odd-bold">
						Editor:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 240px;"
									theme="simple" name="editItem.editor"
									value="%{lastEdit.item.editor}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 240px;" name="editItem.editor"
									value="%{item.editor}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.editor" />
						</s:else>
					</td>
				</tr>				
				<tr>
					<td class="odd-bold">
						Line Item Ref #:
					</td>
					<td class="odd" colspan="3">
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
				</s:if>
				<s:elseif test="item.internet">
				<tr>
					<td class="odd-bold">
						Chapter/Article:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 340px;"
									theme="simple" name="editItem.chapterArticle"
									value="%{lastEdit.item.chapterArticle}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 340px;" name="editItem.chapterArticle"
									value="%{item.chapterArticle}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.chapterArticle" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							Date Used:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Date Used:</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield 
									cssStyle="width: 70px;" cssClass="itype%{item.ID} required"
									theme="simple" name="editItem.dateOfUse"
									value="%{lastEdit.item.dateOfUse}" />
							</s:if>
							<s:else>
								<s:textfield 
									cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 70px;" name="editItem.dateOfUse"
									value="%{item.dateOfUse}" />
							</s:else>
							<s:component theme="xhtml" template="jquerydatepickerThreeParams.ftl">
								<s:param name="sDatePickId" value="'savedetailform'" />
								<s:param name="sDatePickInstId" value="item.ID" />
								<s:param name="sDatePickerFieldName" value="'editItem_dateOfUse'" />
							</s:component>
						</s:if>
						<s:else>
							<s:property value="item.dateOfUse" />
						</s:else>

					</td>
					<s:if test="editable">
						<td class="required-bold">
							Duration:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Duration:</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:select list="durationList " headerKey=""
								headerValue="-- Select One --" listKey="value" listValue="label"
								cssStyle="width: 95px;" cssClass="itype%{item.ID}"
								theme="simple" name="editItem.duration"
								value="%{lastEdit.item.duration}" />
							</s:if>
							<s:else>
							<s:select list="durationList" headerKey=""
								headerValue="-- Select One --" listKey="value" listValue="label"
								cssClass="itype%{item.ID}" theme="simple"
								cssStyle="width: 95px;" name="editItem.duration"
								value="%{item.duration}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.duration" />
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="odd-bold">
						Author:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 240px;"
									theme="simple" name="editItem.customAuthor"
									value="%{lastEdit.item.customAuthor}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 240px;" name="editItem.customAuthor"
									value="%{item.customAuthor}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.customAuthor" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							Pub Date:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Pub Date:</td>
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
				</tr>
				<tr>
					<td class="odd-bold">
						Editor:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 240px;"
									theme="simple" name="editItem.editor"
									value="%{lastEdit.item.editor}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 240px;" name="editItem.editor"
									value="%{item.editor}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.editor" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							Web Address:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Web Address:</td>
					</s:else>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID} required" cssStyle="width: 323px;"
									theme="simple" name="editItem.webAddress"
									value="%{lastEdit.item.webAddress}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 323px;" name="editItem.webAddress"
									value="%{item.webAddress}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.webAddress" />
						</s:else>
					</td>
				</tr>				
				<tr>
					<td class="odd-bold">
						Line Item Ref #:
					</td>
					<td class="odd" colspan="3">
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
				</s:elseif>
				<s:else>
					<tr>
					<td class="odd-bold">
						Chapter/Article:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 340px;"
									theme="simple" name="editItem.chapterArticle"
									value="%{lastEdit.item.chapterArticle}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 340px;" name="editItem.chapterArticle"
									value="%{item.chapterArticle}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.chapterArticle" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							Date Used:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Date Used:</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield 
									cssStyle="width: 70px;" cssClass="itype%{item.ID} required"
									theme="simple" name="editItem.dateOfUse"
									value="%{lastEdit.item.dateOfUse}" />
							</s:if>
							<s:else>
								<s:textfield 
									cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 70px;" name="editItem.dateOfUse"
									value="%{item.dateOfUse}" />
							</s:else>
							<s:component theme="xhtml" template="jquerydatepickerThreeParams.ftl">
								<s:param name="sDatePickId" value="'savedetailform'" />
								<s:param name="sDatePickInstId" value="item.ID" />
								<s:param name="sDatePickerFieldName" value="'editItem_dateOfUse'" />
							</s:component>
						</s:if>
						<s:else>
							<s:property value="item.dateOfUse" />
						</s:else>

					</td>
					<s:if test="editable">
						<td class="required-bold">
							Duration:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Duration:</td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:select list="durationList " headerKey=""
								headerValue="-- Select One --" listKey="value" listValue="label"
								cssStyle="width: 95px;" cssClass="itype%{item.ID}"
								theme="simple" name="editItem.duration"
								value="%{lastEdit.item.duration}" />
							</s:if>
							<s:else>
							<s:select list="durationList" headerKey=""
								headerValue="-- Select One --" listKey="value" listValue="label"
								cssClass="itype%{item.ID}" theme="simple"
								cssStyle="width: 95px;" name="editItem.duration"
								value="%{item.duration}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.duration" />
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="odd-bold">
						Author:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 240px;"
									theme="simple" name="editItem.customAuthor"
									value="%{lastEdit.item.customAuthor}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 240px;" name="editItem.customAuthor"
									value="%{item.customAuthor}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.customAuthor" />
						</s:else>
					</td>
					<s:if test="editable">
						<td class="required-bold">
							Pub Date:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							Pub Date:</td>
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
				</tr>
				<tr>
					<td class="odd-bold">
						Editor:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 240px;"
									theme="simple" name="editItem.editor"
									value="%{lastEdit.item.editor}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 240px;" name="editItem.editor"
									value="%{item.editor}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.editor" />
						</s:else>
					</td>
				</tr>				
				<tr>
					<td class="odd-bold">
						Line Item Ref #:
					</td>
					<td class="odd" colspan="3">
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
				</s:else>

			</table>
