<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>


<br>

<%
	// The fields to search by are:  IDNO, Publication Title, WR Work ID, TF Work ID
%>
	<s:form action="searchWorks" namespace="/om/remote">
	
	<s:hidden id="searchWorksForDetailId"/>

   <div>
    
    <div style="margin: 20px; background-color: white; width: 650px;">
    
								
		<s:hidden theme="simple" name="searching" value="false" />

<s:if test="!processMessages.empty">
  <div style="display: none">_@ERROR@_MESSAGES</div>
  <div id="searchWorksProcessMessagesView">
		<table class="subTable"  style="width: 750px; background-color: white;">
	<tr>
	    <td style="width: 50px;">&nbsp;</td>
		<td style="width: 550px;">
		    <fieldset style="width: 100%">
		    <legend><span style="font-weight: bold;">Please correct the following...</span></legend>
			<ul style="text-align: left;">
			<s:iterator value="processMessages">
			  <s:if test="error">
			   <li style="color: red; font-weight: bold;">
			       <s:property value="errorMessage"/>
			   </li>
			  </s:if>
			  <s:else>
			   <li style="color: blue; font-weight: bold;">
			       <s:property value="errorMessage"/>
			   </li>
			   </s:else>
			</s:iterator>
		 	</ul>
		 	</fieldset>
		</td>
	    <td style="width: 50px;">&nbsp;</td>
	</tr>
	</table>
	</div>
</s:if>

		<table class="subTable"  style="width: 750px; background-color: white;">
		    <col width="150px"/>
		    <col width="500px"/>
		    <s:if test="!limitCriteriaToOnlyTfWorkId">
			<tr>
				<td class="odd-bold">
					Title:
				</td>
				<td>
					<s:textfield theme="simple" cssClass="odd" size="60" name="searchCriteria.title" />
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="odd-bold">
					IDNO:
				</td>
				<td>
					<s:textfield theme="simple" cssClass="odd" size="16" name="searchCriteria.idnoNumber" />
				</td>
			</tr>
			<tr>
				<td class="odd-bold">
					WR Work ID:
				</td>
				<td>
					<s:textfield theme="simple" cssClass="odd" maxlength="15" size="10"
						name="searchCriteria.wrWorkId" />
				</td>
			</tr>
			</s:if>
			<tr>
				<td class="odd-bold">
					TF Work ID:
				</td>
				<td>
					<s:textfield theme="simple" cssClass="odd" maxlength="15" size="10"
						name="searchCriteria.tfWorkId" />
		    	<s:if test="limitCriteriaToOnlyTfWorkId">
					&nbsp;
					<input type="button" value="Search" onclick="doWorksSearch();" />
				</s:if>
				</td>
			</tr>
		    <s:if test="!limitCriteriaToOnlyTfWorkId">
			<tr>
			    <td class="odd-bold">&nbsp;</td>
				<td class="odd" align="left">
					<input type="button" value="Search" onclick="doWorksSearch();" />
					&nbsp;
					<input type="button" value="Clear" onclick="clearSearchWorksFields();return false;"/>
				</td>
			</tr>
			</s:if>
		</table>

	
	</div>


<%
	// The result fields to return are: 
	//DNAR, WR Work ID, IDNO Type, IDNO, Main Title, Publisher, Contributor, Pub Date, Pub Type, Edition.
%>
<s:if test="showResults">

    <div style="margin: 20px; background-color: white; width: 750px;">

	<s:if test="!limitCriteriaToOnlyTfWorkId">
		<table id="divSearchResults" style="border: 1px solid #CDCDCD; width: 100%;">

			<tr>
				<td style="width: 5px;">
					&nbsp;
				</td>
				<td align="left">
					<s:component theme="xhtml" template="customPageSizeControl">
						<s:param name="pagingFormId" value="'searchWorks'" />
						<s:param name="pagingObject" value="'searchCriteria.pageControl'" />
						<s:param name="pageChangeScript" value="'doReSizePageSearchWorkResults'" />
						
					</s:component><br/>
					<s:component theme="xhtml" template="customPageControl">
						<s:param name="pagingFormId" value="'searchWorks'" />
						<s:param name="pagingObject" value="'searchCriteria.pageControl'" />
						<s:param name="inFooter" value="'false'" />
						<s:param name="itemType" value="'Works'" />
						<s:param name="pageChangeScript" value="'doAjaxRePageSearchWorksResults'" />
					</s:component>
				</td>
				<td style="width: 5px;">
					&nbsp;
				</td>
			</tr>

		</table>
	</s:if>

		<table style="width: 100%">
			<s:iterator value="searchResults.worksList" status="workStatus">
			
			  <span id="wrfrom_<s:property value="#workStatus.index" />_publicationTitle" style="display: none;"><s:property value="work.mainTitle" /></span>
			  <span id="wrfrom_<s:property value="#workStatus.index" />_wrWorkInst" style="display: none;"><s:property value="work.wrWrkInst" /></span>
			  <span id="wrfrom_<s:property value="#workStatus.index" />_workInst" style="display: none;"><s:property value="work.tfWrkInst" /></span>
			  <span id="wrfrom_<s:property value="#workStatus.index" />_itemSubDescription" style="display: none;"><s:property value="work.subTitle" /></span>
			  <span id="wrfrom_<s:property value="#workStatus.index" />_idnoLabel" style="display: none;"><s:property value="work.mainIdnoType" /></span>
			  <span id="wrfrom_<s:property value="#workStatus.index" />_standardNumber" style="display: none;"><s:property value="work.mainIdno" /></span>
			  <span id="wrfrom_<s:property value="#workStatus.index" />_publisher" style="display: none;"><s:property value="work.publisherName" /></span>
			  <span id="wrfrom_<s:property value="#workStatus.index" />_rightsholder" style="display: none;"></span>
			  <span id="wrfrom_<s:property value="#workStatus.index" />_rgtInst" style="display: none;"></span>

			  <div style="display: none" id="wrdnar_<s:property value="#workStatus.index" />"><s:property value="dnar"/></div>
			  <tr >
			    <td style="background-color: #CDCDCD;">
			      <table width="100%" style="width: 100%; border: none;
			      	<s:if test="#workStatus.odd == true ">background-color: #F3F6F9;</s:if><s:else>background-color: #E2E9F0;</s:else>">
				<tr>
		          <s:if test="!limitCriteriaToOnlyTfWorkId">
   					<td valign="middle" rowspan="4">
						<s:if test="!work.doNotUseInd">
						<s:submit theme="simple" onclick="applySearchWorkSelection('%{#workStatus.index}');return false;"
							value="Select"></s:submit>
						</s:if>
						<s:else>
							<img src="<s:url value="/WEB-INF/resources/ordermgmt/images/unavailable-for-ra.png"/>" alt="Do not add rights"/>
						</s:else>
					</td>
				 </s:if>
					<td class="odd">
					  <b>
					  <s:if test="!work.publicationType.empty">
					    <s:property value="work.publicationType" />:&nbsp;
					  </s:if>
					  <s:else>
						Title:&nbsp;
					  </s:else></b>
					  </td>
					  <td colspan="5">
						<s:property value="work.mainTitle" />
					</td>
				</tr>
				<tr>
					<td class="odd">
					  <b>
					    <s:property value="idnoTypeDisplay" />:&nbsp;
					  </b>
					  </td>
					  <td>
						<s:property value="work.mainIdno" />
					  </td>
					  <td>
					  <b>
						WR Work ID:&nbsp;
					  </b>
					  </td>
					  <td>
						<s:property value="work.wrWrkInst" />
					  </td>
					  <td>
					  <b>
						TF Work ID:&nbsp;
					  </b>
					  </td>
					  <td>
					    <s:if test="work.tfWrkInst!=null">
						<s:property value="work.tfWrkInst" />
						</s:if>
						<s:else>(none)
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="odd">
					  <b>
						Publisher:&nbsp;
					  </b>
					  </td>
					  <td>
					    <s:if test="!work.publisherName.empty">
						<s:property value="work.publisherName" />
						</s:if>
						<s:else>(none)
						</s:else>
					  </td>
					  <td>
					  <b>
						Contributor:&nbsp;
					  </b>
					  </td>
					  <td colspan="3">
					    <s:if test="!mainContributor.empty">
						<s:property value="mainContributor" />
						</s:if>
						<s:else>(none)
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="odd">
					  <b>
						Pub Dates:&nbsp;
					  </b>
					  </td>
					  <td>
					  <s:if test="!publicationDates.empty">
						<s:property value="publicationDates" />
						</s:if>
						<s:else>(none)
						</s:else>
						</td>
						<td>
					  <b>
						Edition:&nbsp;
					  </b>
					  </td>
					  <td colspan="3">
					    <s:if test="!work.edition.empty">
						<s:property value="work.edition" />
						</s:if>
						<s:else>(none)
						</s:else>
					</td>
				</tr>
				</table>
			 </td></tr>
			</s:iterator>
		</table>
		<s:if test="!limitCriteriaToOnlyTfWorkId">
		<br />
		<table style="width: 100%">
			<tr>
				<td style="width: 5px;">
					&nbsp;
				</td>
				<td align="left">
					<s:component theme="xhtml" template="customPageControl">
						<s:param name="pagingFormId" value="'searchWorks'" />
						<s:param name="pagingObject" value="'searchCriteria.pageControl'" />
						<s:param name="inFooter" value="'true'" />
						<s:param name="itemType" value="'Works'" />
						<s:param name="pageChangeScript" value="'doAjaxRePageSearchWorksResults'" />
					</s:component>

				</td>
				<td style="width: 5px;">
					&nbsp;
				</td>
			</tr>
		</table>
		</s:if>

</div>
</s:if>
<s:else>
<s:if test="processMessages.empty && searching">

    <div style="margin: 20px; background-color: white; width: 750px;">

		<s:if test="!limitCriteriaToOnlyTfWorkId">

		<table id="divSearchResults" style="border: 1px solid #CDCDCD; width: 100%;">

			<tr>
				<td style="width: 5px;">
					&nbsp;
				</td>
				<td align="left">
					<s:component theme="xhtml" template="customPageControl">
						<s:param name="pagingFormId" value="'searchWorks'" />
						<s:param name="pagingObject" value="'searchCriteria.pageControl'" />
						<s:param name="inFooter" value="'false'" />
						<s:param name="itemType" value="'Works'" />
						<s:param name="pageChangeScript" value="'doAjaxRePageSearchWorksResults'" />
					</s:component>
				</td>
				<td style="width: 5px;">
					&nbsp;
				</td>
			</tr>

		</table>
		</s:if>
		<s:else>
		<b>Unable to find any results matching specified criteria.</b>
		</s:else>
	</div>
</s:if>
</s:else>
</div>


	</s:form>

