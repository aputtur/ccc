<%@ page import="com.copyright.ccc.config.CC2Configuration"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>
<br />
<table style="width: 972px;">
	<tr>
		<td>
			<div>
				<s:form action="searchOrdersForAdjustments" method="post" namespace="/om/history"
					cssStyle="padding-left: 10px;" validate="true">
	                <s:hidden theme="simple"  name="quickTabSelected" value="ordermgmt.menu.adjustment"></s:hidden>
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
								<s:hidden theme="simple" name="searching" value="false" />
								<table class="subTable" style="width: 100%">
									<tr>
										<td style="vertical-align: top">
											<table class="subTable" width="100%">
												<tr>
													<s:textfield cssClass="odd" cssStyle="width: 75px;"
														label="Licensee Acct #" name="searchCriteria.acctNumber"></s:textfield>
													<td style="vertical-align: top;" rowspan="4">
														<table class="subTable" height="100%">
															<tr>
																<s:textfield cssClass="odd" cssStyle="width: 75px;"
																	label="TF Order #" name="searchCriteria.orderNumber"></s:textfield>
															</tr>
															<tr>
																<s:textfield cssClass="odd" cssStyle="width: 75px;"
																			label="Order Detail #"
																			name="searchCriteria.orderDetailNumber"></s:textfield>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<s:textfield cssClass="odd" cssStyle="width: 75px;"
														label="Confirmation #" name="searchCriteria.confNumber"></s:textfield>
												</tr>
												<tr>
													<s:textfield cssClass="odd" cssStyle="width: 75px;"
														label="Invoice #" name="searchCriteria.invoiceNumber"></s:textfield>
												</tr>

											</table>
										</td>
										<%
										
										// Its pointless to show RL job ticket or RL license search options if  
										// the oms adjustments are disabled.
										if( !CC2Configuration.getInstance().isDisableOmsAdjustments() )
										{
										
										%>
										<td style="vertical-align: top;">
											<table class="subTable" width="100%">
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
										<% 
										
										} 
										
										%>
										<td style="width: 20px;">
											&nbsp;
										</td>
										<td style="vertical-align: top;">
											<table class="subTable" width="100%">
												<tr>
													<td align="right">
													  <div id="search_buttons">
														<s:submit theme="simple" cssClass="odd" value="Search"
															onclick="doSearch(); return false;"></s:submit>
														<s:submit theme="simple" value="Clear" cssClass="odd"
															onclick="resetCriteria(); return false;" />
													  </div>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						
						<%
						
						// There's no need for the disclaimer if OMS adjustments are disabled.
						if( !CC2Configuration.getInstance().isDisableOmsAdjustments() )
						{ 
						
						%>
						
						<tr>
							<td>

								<table style="width: 930px;" >
									<tr>
										<td style="text-align: left;"><span style="color: red; font-weight: bold;">*</span> Indicates fields not searchable in TF</td>
									</tr>
								</table>
							</td>
						</tr>
						
						<% } %>
					</table>

					<br />
					<s:if test="showResults">
						<table id="divSearchResults"
							style="border: 1px solid #CDCDCD; width: 972px;">
							<tr>
								<td>


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
																<s:param name="sortSelectList" value="'searchAdjList'"/>
																<s:param name="sortListKey" value="'value'"/>
																<s:param name="sortListValue" value="'label'"/>
																<s:param name="formId" value="'searchOrdersForAdjustments'"/>
																<s:param name="onChangeDefaultSortValue" value="'SORT_BY_DEFAULT'"/>
																<s:param name="sortByProperty" value="'searchCriteria.sortCriteriaBy'"/>
																<s:param name="sortOrderProperty" value="'searchCriteria.sortOrder'"/>
																<s:param name="defaultText" value="'search.order.default.sort.text.tf'"/>
														    </s:component>
														</s:if>
														<s:else>
														    <s:component template="customSortControl">
																<s:param name="sortSelectList" value="'searchAdjList'"/>
																<s:param name="sortListKey" value="'value'"/>
																<s:param name="sortListValue" value="'label'"/>
																<s:param name="formId" value="'searchOrdersForAdjustments'"/>
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
														<s:if test="searchResults.haveDetailsStoredInOMS">
														
														<td align="right">
															<s:url escapeAmp="false" id="#lnkSearchAdjNum" action="adjustmentMgmt!newSearchResultsAdjustment.action" includeParams="none" namespace="/om/adjust" >
																<s:param name="selectedAdjustmentEntityId" value="-1" />
																<s:param name="adjustmentMode"
																	value="@com.copyright.svc.adjustment.api.data.AdjustmentType@SEARCHRESULT.name()" />
															</s:url>
															<a href="#" 
																onclick="doCreateAdj('<s:property value="#lnkSearchAdjNum"/>');return false;">
																Adjust Search Results 
															</a>
														</td> 
														</s:if>
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
					       							<s:param name="pagingFormId" value="'searchOrdersForAdjustments'"/>
					       							<s:param name="pagingObject" value="'searchCriteria.pageControl'"/>
					       							<s:param name="pageSizeList" value="{'5', '10','25', '50', '150', '250'}"/>
											    </s:component>
											    <s:component template="customPageControl">
					       							<s:param name="pagingFormId" value="'searchOrdersForAdjustments'"/>
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

									<s:iterator value="searchResults.detailsList" status="iterStatus">
										<table style="width: 100%">
											<s:if test="item.adjustable">
												<s:if test= " (  (item.orderDataSourceDisplay == 'OMS') ||
																 ( (item.orderDataSourceDisplay == 'TF') && ( (item.paymentTypeDisplay == 'Credit Card') || (myConfirmation.confirmation.orderSourceCd == 'RSP') ) )
															  )
																" >
												<tr>
													<td style="background-color: #CCCCCC;">
													<jsp:include
														page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailShort.jsp" />
													</td>
												</tr>
												</s:if>
											</s:if>
										</table>
									</s:iterator>


								</td>
							</tr>
						</table>
						<table style="width: 100%">
										<tr>
											
											<td align="right">
												<s:component template="customPageControl">
					       							<s:param name="pagingFormId" value="'searchOrdersForAdjustments'"/>
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
					       							<s:param name="pagingFormId" value="'searchOrdersForAdjustments'"/>
					       							<s:param name="pagingObject" value="'customPageControl'"/>
					       							<s:param name="pageChangeScript" value="'doReSizePage'"/>
					       							<s:param name="pageSizeList" value="{'5', '10','25', '50', '150', '250'}"/>
											    </s:component>
											    <s:component template="customPageControl">
					       							<s:param name="pagingFormId" value="'searchOrdersForAdjustments'"/>
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

	<s:if test="completedAdjustment">
		$(window).load(function () {
			showAdjustmentCompleteMessage();
		});
	</s:if>	


 	$(document).ready(function(){

	 	$("#searchOrdersForAdjustments_searchCriteria_startDate").datepicker( {
			    showAnim : 'fadeIn',
				showOn: 'button',
				buttonImage: '<s:url value="/resources/ordermgmt/images/calendar.gif"/>',
				buttonImageOnly: true,
				buttonText: 'Calendar...'		});
	 	$("#searchOrdersForAdjustments_searchCriteria_endDate").datepicker( {
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
		
		
  	});

 	function showAdjustmentCompleteMessage(){
 		showMessageDialog('The adjustment has successfully completed');
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
	    $('#searchOrdersForAdjustments_searching').val(true);
	    $('#search_buttons').hide();
	    $('#search_buttons_bottom').hide();
	    showMessageDialogNB($('#waiting_for_search').html());
	    $('#searchOrdersForAdjustments').submit();   
	}

	function showProcessingOnHref() {
	    showMessageDialogNB($('#waiting_for_search').html());
	    return false;
	}
	   
	function showMessageDialogNB(message) {
		$('#messageDialogDiv_Content_NB').html(message);
		$('#messageDialog_Div_NB').dialog('open');
		   return false;	
	}

	function showMessageDialog(message) {
	  	   $('#messageDialogDiv_Content').html(message);
		   $('#messageDialog_Div').dialog('open');
		   return false;	
	}
	   
	function doReSort() {
	    $('#searchOrdersForAdjustments_searching').val(true);
	    $('#searchOrdersForAdjustments').attr('action','searchOrdersForAdjustments!resortSearchResults.action');
	    showMessageDialogNB($('#waiting_for_sort').html());
	    $('#searchOrdersForAdjustments').submit();   
	}
	   
	function resetCriteria() {
		var aForm = document.getElementById("searchOrdersForAdjustments");
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




