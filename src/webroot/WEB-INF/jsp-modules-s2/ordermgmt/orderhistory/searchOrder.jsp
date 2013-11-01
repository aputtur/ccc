<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>
<br />
<table style="width: 972px;">
	<tr>
		<td>
			<div>
				<s:form action="searchOrder" method="post" namespace="/om/history"
					cssStyle="padding-left: 10px;" validate="true">
					<s:if test="quickTabSelect.notPredefinedQueryType">
						<table class="subTable" width="100%">
							<tr>
								<td>
									<a id="returnToSearch_href" href="#"
										onclick="returnToSearch(); return false;">Search again</a>
									<a style="display: none;" id="hideReturnToSearch_href" href="#"
										onclick="returnToSearchHide(); return false;">Hide search</a>
								</td>
							</tr>
						</table>
					</s:if>
					<table id="formSearchCriteria" style="width: 930px;">
						<tr>
							<td>
								<s:hidden theme="simple" name="quickTabSelected"></s:hidden>
								<s:hidden theme="simple" name="searching" value="false" />
								<table class="subTable" style="width: 100%">
									<tr>
										<td style="vertical-align: top">
											<table class="subTable" width="100%">
												<tr id="elt5_ordersOnly">													
													<s:textfield cssClass="odd" cssStyle="width: 365px;"
														label="Licensee Name" name="searchCriteria.acctName">	
														<s:param name="inputcolspan" value="%{3}" />																
													</s:textfield>
												</tr>
												<tr>
													<s:textfield cssClass="odd" cssStyle="width: 100px;"
														label="Licensee Acct #" name="searchCriteria.acctNumber"></s:textfield>
													<td style="vertical-align: top;" rowspan="4">
														<table class="subTable" height="100%">
															<tr id="elt3_ordersOnly">
																<td class="odd-bold">
																	<s:label theme="simple" cssStyle="width: 110px;" value="Permission Status:"></s:label>
																</td>
																<td>
																			<s:select cssClass="odd" cssStyle="width: 140px;" id="ddlPermStatus"
																			list="searchPermissionStatusList" listKey="value"
																			listValue="label" theme="simple"
																			headerKey="" headerValue="-- Select One --"
																			label="Permission Status" 
																			name="searchCriteria.permissionStatus"></s:select>
																</td>		
															</tr>
															<tr id="elt4_ordersOnly">
																<td class="odd-bold">
																	<s:label theme="simple" cssStyle="width: 110px;" value="Product:"></s:label>
																</td>
																<td>	
																			<s:select cssClass="odd" cssStyle="width: 140px;"
																			list="searchProductList" listKey="label"
																			listValue="label" theme="simple"
																			headerKey="" headerValue="-- Select One --"
																			label="Product" 
																			name="searchCriteria.productTOU"></s:select>
																</td>
															</tr>
															<tr>
																<s:textfield cssClass="odd" cssStyle="width: 100px;"
																	label="TF Order #" name="searchCriteria.orderNumber"></s:textfield>
															</tr>
															<tr>
																<s:textfield cssClass="odd" cssStyle="width: 100px;"
																			label="Order Detail #"
																			name="searchCriteria.orderDetailNumber"></s:textfield>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<s:textfield cssClass="odd" cssStyle="width: 100px;"
														label="Confirmation #" name="searchCriteria.confNumber"></s:textfield>
												</tr>
												<tr>
													<s:textfield cssClass="odd" cssStyle="width: 100px;"
														label="Invoice #" name="searchCriteria.invoiceNumber"></s:textfield>
												</tr>
												<tr>
													<td class="odd-bold">
														<s:label theme="simple" id="elt1_ordersOnly" cssStyle="width: 95px;" value="RH Ref #:"></s:label>
													</td>
													<td>
														<s:textfield cssClass="odd" cssStyle="width: 100px;" theme="simple"
																	id="elt2_ordersOnly"
																	label="RH Ref #" name="searchCriteria.rhRefNumber"></s:textfield>
													</td>
												</tr>
											</table>
										</td>
										<td style="vertical-align: top;">
											<table class="subTable" width="100%">
												<tr id="tr1_ordersOnly">
													<s:select cssClass="odd" cssStyle="width: 140px;"
														label="Date Range" list="searchDateList" listKey="value"
														listValue="label" headerKey=""
														headerValue="-- Select One --"
														name="searchCriteria.dateType">
														<s:param name="inputcolspan" value="%{3}" />
													</s:select>
												</tr>
												<tr id="tr2_ordersOnly">
													<td style="width: 60px;">
														&nbsp;
													</td>
													<s:textfield cssClass="odd" cssStyle="width: 75px;"
														name="searchCriteria.startDate" label="From"
														labelposition="right"></s:textfield>
												</tr>
												<tr id="tr3_ordersOnly">
													<td style="width: 60px;">
														&nbsp;
													</td>
													<s:textfield cssClass="odd" cssStyle="width: 75px;"
														name="searchCriteria.endDate" label="To"></s:textfield>
												</tr>
												<tr>
													<td class="odd-bold" style="width: 110px;"><span style="color: red;">* </span>RL Job Ticket #:</td>
													<td colspan="3">
														<s:textfield cssClass="odd" cssStyle="width: 140px;"
															name="searchCriteria.rlJobTicket" theme="simple">
														</s:textfield>
													</td>
												</tr>
												<tr>
													<td class="odd-bold"><span style="color: red;">* </span>RL License #:</td>
													<td colspan="3">
														<s:textfield cssClass="odd" cssStyle="width: 140px;"
															name="searchCriteria.rlLicenseNumber" theme="simple">
														</s:textfield>
													</td>
												</tr>
											</table>
										</td>
										<td style="width: 20px;">
											&nbsp;
										</td>
										<td style="vertical-align: top;">
											<table class="subTable" width="100%">
												<tr>
													<td align="right">
													  <div id="search_buttons">
														<s:submit theme="simple" cssClass="odd" value="Search"
															onclick="doSearch(); return true;"></s:submit>
														<s:submit theme="simple" value="Clear" cssClass="odd"
															onclick="resetCriteria(); return false;" />
													  </div>
													</td>
												</tr>
												<tr>
													<td style="width: 140px;" id="tdShowInResults">
														<fieldset>
															<legend
																style="font-weight: bold; color: black; margin-left: -5px; margin-right: 5px;">
																&nbsp;Show in Results&nbsp;
															</legend>
															<table width="100%">
																<tr>
																	<s:checkbox cssClass="odd"
																		name="searchCriteria.showCourseInfo" label="Project"
																		labelSeparator=""></s:checkbox>
																</tr>
																<tr>
																	<s:checkbox cssClass="odd"
																		name="searchCriteria.showCancelled"
																		label="Cancelled Details" labelSeparator=""></s:checkbox>
																</tr>
																<tr>
																	<s:checkbox cssClass="odd" id="show_tf" onclick="makeSureOneIsChecked('show_tf','show_oms');"
																		name="searchCriteria.showTfOrders"
																		label="Stored in TF" labelSeparator=""></s:checkbox>
																</tr>
																<tr>
																	<s:checkbox cssClass="odd" id="show_oms" onclick="makeSureOneIsChecked('show_oms','show_tf');"
																		name="searchCriteria.showCoiOrders"
																		label="Stored in OMS" labelSeparator=""></s:checkbox>
																</tr>
																<s:if test="showExpandableViewChoice">
																<tr>
																	<s:checkbox cssClass="odd"
																		name="searchCriteria.includeExpandedResults" label="Expandable view"
																		labelSeparator=""></s:checkbox>
																</tr>
																</s:if>
															</table>
														</fieldset>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>

								<table style="width: 930px;" >
									<tr>
											<td align="left" style="width: 140px;" id="advSearchLink">				
												<b><a id="advancedSearch_href" href="#"
													onclick="advancedSearchShowHide(this);return false;">Show 
													Expanded Search</a> </b>								
											</td>
										<td style="text-align: left;"><span style="color: red; font-weight: bold;">*</span> Indicates fields not searchable in TF</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table class="subTable" id="advSearchTable"
									style="width: 930px;">
									<tr>
										<s:select cssClass="odd" cssStyle="width: 120px;"
											list="searchRefNumList" listKey="value" listValue="label"
											headerKey="" headerValue="-- Select One --"
											label="Licensee Ref #" name="searchCriteria.refType_adv"></s:select>
										<s:textfield cssClass="odd" cssStyle="width: 175px;"
											name="searchCriteria.refData_adv"></s:textfield>
										<s:textfield cssClass="odd" cssStyle="width: 312px;"
											label="Title"
											name="searchCriteria.publicationName_adv">
											<s:param name="inputcolspan" value="%{3}" />
										</s:textfield>
									</tr>
									<tr>
										<s:textfield cssClass="odd" cssStyle="width: 75px;"
											label="Last 4 Digits of CC"
											name="searchCriteria.last4digits_adv">
											<s:param name="inputcolspan" value="%{5}" />
										</s:textfield>
									</tr>
									<tr>
										<s:textfield cssClass="odd" cssStyle="width: 312px;"
											label="Rightsholder Name"
											name="searchCriteria.rightsholderName_adv">
											<s:param name="inputcolspan" value="%{2}" />
										</s:textfield>
										<s:textfield cssClass="odd" cssStyle="width: 200px;"
											label="RH Account #"
											name="searchCriteria.rhAccountNumber_adv">
											<s:param name="inputcolspan" value="%{2}" />
										</s:textfield>
									</tr>
									<tr>
										<td class="odd-bold"><span style="color: red;">* </span>Course Name:</td>
													<td colspan="2">
														<s:textfield cssClass="odd" cssStyle="width: 312px;"
															name="searchCriteria.courseName_adv" theme="simple">
														</s:textfield>
													</td>
										<s:textfield cssClass="odd" cssStyle="width: 94px;"
											label="TF Work ID" name="searchCriteria.tfWorkID_adv">
										</s:textfield>
										<td class="odd-bold"><span style="color: red;">* </span>WR Work ID:</td>
													<td>
														<s:textfield cssClass="odd" cssStyle="width: 94px;"
															name="searchCriteria.wrWorkID_adv" theme="simple">
														</s:textfield>
													</td>
									</tr>
									<tr>
										<td class="odd-bold"><span style="color: red;">* </span>Institution Name:</td>
													<td colspan="2">
														<s:textfield cssClass="odd" cssStyle="width: 312px;"
															name="searchCriteria.institution_adv" theme="simple">
														</s:textfield>
													</td>
										<s:textfield cssClass="odd" cssStyle="width: 94px;"
											label="IDNO" name="searchCriteria.standardNumber_adv">
										</s:textfield>
										<s:select cssClass="odd" cssStyle="width: 140px;"
														list="searchOrderSourceList" headerKey="" 
														listValue="label" listKey="label"
														headerValue="-- Select One --" label="Order Source"
														name="searchCriteria.orderSource_adv"></s:select>		
									</tr>
									<tr>
										<td class="odd-bold"><span style="color: red;">* </span>Repub Work:</td>
													<td colspan="2">
														<s:textfield cssClass="odd" cssStyle="width: 312px;"
															name="searchCriteria.republWork_adv" theme="simple">
														</s:textfield>
													</td>
										<s:textfield cssClass="odd" cssStyle="width: 200px;"
											label="Internet Login"
											name="searchCriteria.internetLogin_adv">
											<s:param name="inputcolspan" value="%{2}" />
										</s:textfield>
									</tr>
									<tr>
										<td class="odd-bold"><span style="color: red;">* </span>Repub Pub:</td>
													<td colspan="2">
														<s:textfield cssClass="odd" cssStyle="width: 312px;"
															name="searchCriteria.republPublisher_adv" theme="simple">
														</s:textfield>
													</td>
										<s:textfield cssClass="odd" cssStyle="width: 200px;"
											label="Last Updated By"
											name="searchCriteria.lastUpdatedBy_adv">
											<s:param name="inputcolspan" value="%{2}" />
										</s:textfield>
										<td align="right">
										  <div id="search_buttons_bottom" style="margin-right: 3px;">
											<s:submit theme="simple" cssClass="odd" value="Search"
												onclick="doSearch(); return true;"></s:submit>
										  	<s:submit theme="simple" value="Clear" cssClass="odd"
												onclick="resetCriteria(); return false;" />
										  </div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>

					<br />
					<s:if test="showResults">
						<table id="divSearchResults"
							style="border: 1px solid #CDCDCD; width: 972px;">
							<tr>
								<td>

									<s:hidden theme="simple" name="quickTabSelected"></s:hidden>

									<table style="width: 100%">
										<tr>
											<td style="width: 50px;">
												&nbsp;
											</td>
											<td style="padding-top: 8px;">
												<table width="100%">
													<tr>
														<td align="left">
														<s:if test="searchResults.haveDetailsStoredInTF">			
														    <s:component template="customSortControl">
																<s:param name="sortSelectList" value="'searchOrderList'"/>
																<s:param name="sortListKey" value="'value'"/>
																<s:param name="sortListValue" value="'label'"/>
																<s:param name="formId" value="'searchOrder'"/>
																<s:param name="onChangeDefaultSortValue" value="'SORT_BY_DEFAULT'"/>
																<s:param name="sortByProperty" value="'searchCriteria.sortCriteriaBy'"/>
																<s:param name="sortOrderProperty" value="'searchCriteria.sortOrder'"/>
																<s:param name="defaultText" value="'search.order.default.sort.text.tf'"/>
														    </s:component>
														</s:if>
														<s:else>
														    <s:component template="customSortControl">
																<s:param name="sortSelectList" value="'searchOrderList'"/>
																<s:param name="sortListKey" value="'value'"/>
																<s:param name="sortListValue" value="'label'"/>
																<s:param name="formId" value="'searchOrder'"/>
																<s:param name="onChangeDefaultSortValue" value="'SORT_BY_DEFAULT'"/>
																<s:param name="sortByProperty" value="'searchCriteria.sortCriteriaBy'"/>
																<s:param name="sortOrderProperty" value="'searchCriteria.sortOrder'"/>
																<s:param name="defaultText" value="'search.order.default.sort.text.oms'"/>
														    </s:component>
														</s:else>

														</td>
														<td style="width: 5px;">
															&nbsp;
														</td>
														<td align="right" style="width: 300px;">
														  <s:if test="searchCriteria.includeExpandedResults">
															<a href="#" onclick="expandAll();return false;"><span
																style="margin-top: 5px;"> <img border="0"
																		src="<s:url value='/resources/ordermgmt/images/expand.gif'/>" />
															</span> </a>&nbsp;Expand All &nbsp;
															<a href="#" onclick="collapseAll();return false;"><span
																style="margin-top: 5px;"> <img border="0"
																		src="<s:url value='/resources/ordermgmt/images/collapse.gif'/>" />
															</span> </a>&nbsp;Collapse All
														  </s:if>
														</td>
													</tr>
												</table>
											</td>
											<td style="width: 50px;">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td colspan="3">
												<hr style="width: 90%;" />
											</td>
										</tr>
										<tr>
											<td style="width: 50px;">
												&nbsp;
											</td>
											<td align="left">
												<s:if test="showDbTimings"><b><s:property value="searchTime" /></b></s:if>
											    <s:component template="customPageSizeControl">
					       							<s:param name="pagingFormId" value="'searchOrder'"/>
					       							<s:param name="pagingObject" value="'searchCriteria.pageControl'"/>
					       							<s:param name="pageSizeList" value="{'5', '10','25', '50', '150', '250'}"/>
											    </s:component>
											    <s:component template="customPageControl">
					       							<s:param name="pagingFormId" value="'searchOrder'"/>
					       							<s:param name="pagingObject" value="'searchCriteria.pageControl'"/>
					       							<s:param name="inFooter" value="'false'"/>
					       							<s:param name="hideLast" value="'true'"/>
											    </s:component>
												
											</td>
											<td style="width: 50px;">
												&nbsp;
											</td>
										</tr>

									</table>

									<br />

									<s:hidden theme="simple" name="quickTabSelected"></s:hidden>
									<s:iterator value="searchResults.detailsList"status="iterStatus">
										<table style="width: 100%">
											<tr>
												<td style="background-color: #CCCCCC;">
										
													<jsp:include
														page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailShort.jsp" />
												</td>
											</tr>
										</table>
									</s:iterator>


								</td>
							</tr>
						</table>
						<table style="width: 100%">
										<tr>
											
											<td align="right">
												<s:component template="customPageControl">
					       							<s:param name="pagingFormId" value="'searchOrder'"/>
					       							<s:param name="pagingObject" value="'customPageControl'"/>
					       							<s:param name="inFooter" value="'true'"/>
					       							<s:param name="hideLast" value="'true'"/>
											    </s:component>
												
											</td>
											<td style="width: 50px;">
												&nbsp;
											</td>
										</tr>
						</table>
					</s:if>
					<s:else>
						<s:if test="searchExecuted">
							<div style="border: 1px solid #CDCDCD; margin: 1px;"
								id="divSearchResults">

								<table style="width: 972px;">
									<tr>
										<td style="width: 11px;">
											&nbsp;
										</td>
										<td align="left" style="width: 950px;">
											    <s:component template="customPageSizeControl">
					       							<s:param name="pagingFormId" value="'searchOrder'"/>
					       							<s:param name="pagingObject" value="'customPageControl'"/>
					       							<s:param name="pageChangeScript" value="'doReSizePage'"/>
					       							<s:param name="pageSizeList" value="{'5', '10','25', '50', '150', '250'}"/>
											    </s:component>
											    <s:component template="customPageControl">
					       							<s:param name="pagingFormId" value="'searchOrder'"/>
					       							<s:param name="pagingObject" value="'customPageControl'"/>
					       							<s:param name="pageChangeScript" value="'doRePage'"/>
					       							<s:param name="inFooter" value="'false'"/>
					       							<s:param name="hideLast" value="'true'"/>
											    </s:component>
										</td>
										<td style="width: 11px;">
											&nbsp;
										</td>
									</tr>
								</table>
							</div>
						</s:if>
					</s:else>
				</s:form>
			</div>
		</td>
	</tr>
</table>


<script type="text/javascript">

 	$(document).ready(function(){

	 	$("#searchOrder_searchCriteria_startDate").datepicker( {
				changeMonth: true,  
				changeYear: true,
				yearRange: '-2:+2',
			    showAnim : 'fadeIn',
				showOn: 'button',
				buttonImage: '<s:url value="/resources/ordermgmt/images/calendar.gif"/>',
				buttonImageOnly: true,
				buttonText: 'Calendar...'		});
	 	$("#searchOrder_searchCriteria_endDate").datepicker( {
				changeMonth: true,  
				changeYear: true,
				yearRange: '-2:+2',
			    showAnim : 'fadeIn',
				showOn: 'button',
				buttonImage: '<s:url value="/resources/ordermgmt/images/calendar.gif"/>',
				buttonImageOnly: true,
				buttonText: 'Calendar...'		});

		if (<s:property value="showCriteria"/>) {
			$('#formSearchCriteria').show();
			$('#returnToSearch_href').hide();
		} else {
			$('#formSearchCriteria').hide();
			$('#returnToSearch_href').show();
		}
		if (<s:property value="returningToSearch"/>) {
			$('#hideReturnToSearch_href').show();
		}
		if (<s:property value="showResults"/>)
			$('#formResults').show();
		else
			$('#formResults').hide();

		if ('<s:property value="quickTabSelected" />' == "ordermgmt.menu.order.my") {
			disableMyOrderFields();
		}
		
		if (!hasAdvancedSearchFieldsSet()) {
			$('#advSearchTable').hide();
		} else {
			$('#advancedSearch_href').attr('innerHTML','Hide Expanded Search');
		}
		
  	});

 	function disableMyOrderFields() {
 		$('#searchOrder_searchCriteria_internetLogin_adv').attr('disabled', 'disabled');
 	}

 	function hasAdvancedSearchFieldsSet() {
		var advFieldsList = $("[id$='_adv']");
		for (i = 0; i < advFieldsList.size(); i++) {
			if (advFieldsList[i].value != null && advFieldsList[i].value != "") {
				return true;
		   }
		}
		return false;
 	}

 	function makeSureOneIsChecked(one,two) {
 	 	var oid = '#'+one;
 	 	var tid = '#'+two;
 	 	if ( $(oid).attr('checked') == false ) {
 	 	 	if ( $(tid).attr('checked') == false ) {
 	 	 		$(tid).attr('checked','true');
 	 	 	}
	 	}
 	}

    function advancedSearchShowHide(ctrl) {
   	  if ( ctrl.innerHTML.indexOf('Show') != -1 ) {
   		 $('#advSearchTable').show();
   		 ctrl.innerHTML = ctrl.innerHTML.replace('Show','Hide');
	  } else if ( ctrl.innerHTML.indexOf('Hide') != -1 ) {
	   	 $('#advSearchTable').hide();
	   	 ctrl.innerHTML = ctrl.innerHTML.replace('Hide','Show');
	  } else {}
	}
   	   
	function returnToSearch() {
		$('#formSearchCriteria').show();
		$('#returnToSearch_href').hide();
		$('#hideReturnToSearch_href').show();
    }

	function returnToSearchHide() {
		$('#formSearchCriteria').hide();
		$('#returnToSearch_href').show();
		$('#hideReturnToSearch_href').hide();
	}

	function doSearch() {
	    $('#searchOrder_searching').val(true);
	    $('#search_buttons').hide();
	    $('#search_buttons_bottom').hide();
	    showMessageDialog($('#waiting_for_search').html());
	    $('#searchOrder').submit();   
	}

	function showProcessingOnHref() {
	    showMessageDialog($('#waiting_for_search').html());
	    return true;
	}
	   
	function showMessageDialog(message) {
		$('#messageDialogDiv_Content_NB').html(message);
		$('#messageDialog_Div_NB').dialog('open');
		   return false;	
	}
	   
	function doReSort() {
	    $('#searchOrder_searching').val(true);
	    $('#searchOrder').attr('action','searchOrder!resortSearchResults.action');
	    showMessageDialog($('#waiting_for_sort').html());
	    $('#searchOrder').submit();   
	}
	   
	function resetCriteria() {
		var aForm = document.getElementById("searchOrder");
    	var donotclear = isNotToBeClearedList();  
	    for (var i=0;i<aForm.length;i++) {
	    	var ele=aForm.elements[i];
	    	var eleid='@'+ele.id+'@';
	    	if (!ele.disabled && isToBeCleared(eleid, donotclear) ) {
	     		if ( ele.type == 'checkbox' ) { ele.checked=false; }
	     		if ( ele.type == 'text' ) { ele.value=''; }
	     		if ( ele.type == 'select-one' ) {
	        		  ele.value=ele.options[0].value;
	     		}
	    	}
	    }
	    $('#show_tf').attr('checked','true');
	    $('#show_oms').attr('checked','true');
	    $('#formResults').hide();
	}
    function isToBeCleared(eleid, donotclear) {
        return donotclear.indexOf(eleid) == -1;
    }
    
	function isNotToBeClearedList() {
		var fch = '.cctlnoclear';
		var nottobe = "@";
		$(fch).each(function(i){
        	nottobe +=$(this).attr('id')+'@';
    	  });
  	    return nottobe;
  	    		
	}
	function showHistory() {

	}
			
</script>




