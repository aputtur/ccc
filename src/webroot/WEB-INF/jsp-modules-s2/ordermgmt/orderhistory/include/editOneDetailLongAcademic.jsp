<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>



<script src="<s:url value="/resources/commerce/js/roman.js" includeParams="none"/>" type="text/javascript"></script>
<script src="<s:url value="/resources/commerce/js/parser.js" includeParams="none"/>" type="text/javascript"></script> 
<script type="text/javascript">

function validatePageRange(pageRangeField, detailId) {
	 
	 
      var pageCount = -1, flds;
  
      if (pageRangeField.value == null || pageRangeField.value == "") {
        alert("Page Range(s) is a required field.");
        pageRangeField.focus();
      }
      else {
        pageCount = getPageCount(pageRangeField.value);
        if (pageCount != -1) {
          flds = document.getElementById(detailId);
          flds.value = pageCount;
        }
        else {
          alert("You have entered an invalid page range.");
          pageRangeField.focus();
        }
      }
    }
</script>

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
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 440px;"
									theme="simple" name="editItem.chapterArticle"
									value="%{lastEdit.item.chapterArticle}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 400px;" name="editItem.chapterArticle"
									value="%{item.chapterArticle}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.chapterArticle" />
						</s:else>
					</td>
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
								<s:textfield cssClass="itype%{item.ID} required" cssStyle="width: 90px;"
									theme="simple" name="editItem.pageRange"
									value="%{lastEdit.item.pageRange}"  
									onblur="validatePageRange(this,'savedetailform_%{item.ID}_editItem_numberOfPages')"/>
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 90px;" name="editItem.pageRange"
									value="%{item.pageRange}"
									
									onblur="validatePageRange(this,'savedetailform_%{item.ID}_editItem_numberOfPages');" />
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
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID} required" cssStyle="width: 240px;"
									theme="simple" name="editItem.customAuthor"
									value="%{lastEdit.item.customAuthor}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 240px;" name="editItem.customAuthor"
									value="%{item.customAuthor}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.customAuthor" />
						</s:else>
					</td>
					<td class="odd-bold">
						Date of Issue:
					</td>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 90px;"
									theme="simple" name="editItem.dateOfIssue"
									value="%{lastEdit.item.dateOfIssue}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 90px;" name="editItem.dateOfIssue"
									value="%{item.dateOfIssue}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.dateOfIssue" />
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
					<td class="odd-bold">
						Editor:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 90px;"
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
							No. of Students:
						</td>
					</s:if>
					<s:else>
						<td class="odd-bold">
							No. of Students: </td>
					</s:else>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID} required"
									cssStyle="width: 90px;" theme="simple"
									name="editItem.numberOfStudents"
									value="%{lastEdit.item.numberOfStudents}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID} required" theme="simple"
									cssStyle="width: 90px;" name="editItem.numberOfStudents"
									value="%{item.numberOfStudents}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.numberOfStudents" />
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="odd-bold">
						Edition:
					</td>
					<td class="odd" colspan="3">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 140px;"
									theme="simple" name="editItem.customEdition"
									value="%{lastEdit.item.customEdition}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 140px;" name="editItem.customEdition"
									value="%{item.customEdition}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.customEdition" />
						</s:else>
					</td>
					<td class="odd-bold">
						Volume:
					</td>
					<td class="odd">
						<s:if test="editable">
							<s:if test="hasLastEdit">
								<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 90px;"
									theme="simple" name="editItem.customVolume"
									value="%{lastEdit.item.customVolume}" />
							</s:if>
							<s:else>
								<s:textfield cssClass="itype%{item.ID}" theme="simple"
									cssStyle="width: 90px;" name="editItem.customVolume"
									value="%{item.customVolume}" />
							</s:else>
						</s:if>
						<s:else>
							<s:property value="item.customVolume" />
						</s:else>
					</td>
					
					<td class="odd-bold">Reuse Entire Book:</td>
			        		
			        			<s:if test="item.licenseeRequestedEntireWork == true">
			        				<td class="odd"> Y </td>
			        			</s:if>

			        			
			        			<s:if test="item.licenseeRequestedEntireWork == false">
			        				<td class="odd"> N </td>
			        			</s:if>
				</tr>
			</table>
