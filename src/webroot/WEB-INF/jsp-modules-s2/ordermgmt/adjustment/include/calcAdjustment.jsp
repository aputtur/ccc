<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustmentConstants" %>
<%@ page import="java.util.*, java.lang.*"%>
<div style="width: 972px; border: 1px solid #808080; margin-top: 0px; margin-left: 10px; margin-right: 10px; margin-bottom: 10px; padding: 0px;">
<table class="editTable" width="100%" style="margin-botton: 0px;">
	<tr style="background-color: #6F97BB;">
		<td valign="top" style="width: 5px; text-align: center;">
			&nbsp;
		</td>
		<td valign="middle" style="height: 26px; width: 959px;">
			<div id="orderDetailSectionHeader"
				style="display: inline; padding-top: 2px; font-size: 14px; color:#FFFFFF">
					Order Detail Number : 
					<s:property	value="itemId" />
			</div>
		</td>
	</tr>
</table>


<s:hidden id='rowItemId_%{#rowStatus.index}' name='rowItemId_%{#rowStatus.index}'  value="%{item.itemId}"/>	
	
<div id="orderDetailcontent" >
	<table class="editTable" id="orderDetailcontentTable" width="100%">
	    <col style="width: 1px;"/>
	    <col style="width: 200px;"/>
	    <col style="width: 150px;"/>
	    <col style="width: 200px;"/>
		<col style="width: 30px;"/>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label cssClass="odd" label="Confirm Number"
				name="Confirm Number" value="%{confirmNumber}">
			</s:label>
			<td class="odd-bold">Distributed:</td>
			<s:if test="distributed">
				<td style="color:red;"><s:label label="Distributed" cssClass="odd" theme="simple"
				value="%{distributedYN}">
			</s:label></td>
			</s:if>
			<s:else>
				<td><s:label label="Distributed" cssClass="odd" theme="simple"
				value="%{distributedYN}">
			</s:label></td>
			</s:else>
			<s:label label="Product" cssClass="odd"
				value="%{productCd}">
			</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Invoice Number" cssClass="odd"
				 value="%{invoiceNumber}">
			</s:label>
			<td class="odd-bold">Adjusted:</td>
			<s:if test="adjusted">
				<td style="color:red;"><s:label label="Adjusted" cssClass="odd" theme="simple" 
				value="%{adjustedYN}">
			</s:label></td>
			</s:if>
			<s:else>
				<td><s:label label="Adjusted" cssClass="odd" theme="simple" 
				value="%{adjustedYN}">
			</s:label></td>
			</s:else>
			<s:label label="Payment Method" cssClass="odd"
				 value="%{paymentMethod}">
			</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
		<tr>
			<td class="odd">&nbsp;</td>
			<s:label label="Order Number" cssClass="odd" 
				value="%{orderNumber}">
			</s:label>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<s:label label="Currency Type" cssClass="odd"
				value="%{currencyType}">
			</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
		<s:if test="localCurrency">
		<tr>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
			<td class="odd">&nbsp;</td>
				<s:label label="Exchange Rate" cssClass="odd" value="%{itemExchangeRate} ">	</s:label>
			<td class="odd">&nbsp;</td>
		</tr>
		</s:if>
		<tr>
			<td colspan="6" align="center"> 
				<label for="enter totals" class="label" style="font-weight: bold;">&nbsp;&nbsp;Reason:&nbsp;</label>
				<s:if test="rightsLink">
					 <s:select 						 		 	
						id="reasonCode_%{item.itemId}"					 
						cssStyle="width: 450px; font-weight: bold;  " 
						theme="simple"
						label=""
						name="reasonCode1"
						headerKey="-1" headerValue="Select One"
						list="RLReasonList"
						listKey="label"
						listValue="value"
						multiple="false"
						size="1"					
						value="adjustmentItem.adjustmentReason.code"
						labelSeparator=""
						>
						</s:select>
					</s:if>
					<s:else>
					 <s:select 
						id="reasonCode_%{item.itemId}"					 
						cssStyle="width: 450px; font-weight: bold;  " 
						theme="simple"
						label=""
						name="reasonCode1"
						headerKey="-1" headerValue="Select One"
						list="TFReasonList"
						listKey="label"
						listValue="value"
						multiple="false"
						size="1"						
						value="adjustmentItem.adjustmentReason.code"
						labelSeparator=""
						>
						</s:select>
					</s:else>
			</td>
		</tr>
	</table>
</div>
<table id="adjustmentHdr"   style="text-align: center; width: 972px; background-color: #F3F6F9; 
	border: 1px solid #808080; margin-top: 0px; margin-left: 0px; margin-right: 0px; margin-bottom: 0px; padding: 0px;">
 				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 80px;"/>
				<s:if test="localCurrency">
					<col style="width: 80px;"/>
				</s:if>
				<col style="width: 80px;"/ >
				<col style="width: 80px;"/>
				<col style="width: 80px;"/>
				<s:if test="showParmOne">
					<col style="width: 80px;"/>
				</s:if>
				<s:if test="shippingAndHandling">
					<col style="width: 80px;"/>
					<col style="width: 80px;"/>
					<col style="width: 80px;"/>
				</s:if>
				<s:if test="showParmTwo">
					<col style="width: 80px;"/>
				</s:if>
				<col style="width: 200px;"/>
                <tr style="background-color: #FFFFFF; "  >			  
                  <th style="border-bottom: 1px solid #808080;"> Detail No.</th>
                  <th style="border-bottom: 1px solid #808080;"> Date </th>
                  <th style="border-bottom: 1px solid #808080;">Status </th>
				  <s:if test="showParmOne">
                  	<th style="border-bottom: 1px solid #808080;"> <s:property	value="adjustItemParmOneName" /> </th>
                  </s:if>
				  <s:if test="showParmTwo">
                  	<th style="border-bottom: 1px solid #808080;"> <s:property	value="adjustItemParmTwoName" /> </th>
                  </s:if>
                  <th style="border-bottom: 1px solid #808080;"> Licensee Fee </th>
                  <th style="border-bottom: 1px solid #808080;">Royalty </th>
                  <th style="border-bottom: 1px solid #808080;"> Discount </th>
				  <s:if test="shippingAndHandling">
                  	<th style="border-bottom: 1px solid #808080;"> Hard Copy </th>
                  	<th style="border-bottom: 1px solid #808080;"> Tax </th>
                  	<th style="border-bottom: 1px solid #808080;"> Shipping </th>
				  </s:if>
                  <th style="border-bottom: 1px solid #808080;"> Total</th>
				  <s:if test="localCurrency">
					<th style="border-bottom: 1px solid #808080;">Total Local Currency</th>
				  </s:if>	
				  <th style="border-bottom: 1px solid #808080;"> &nbsp;</th>
                </tr>
                <tr style="margin-top: 10px; border-top: 1px solid #808080; font-weight:bold" >	
                	<s:if test="(bundleId!= null) && (bundleId > 0)">
						<s:url escapeAmp="false" action="viewOrderHistory" includeParams="none"
							namespace="/om/history" id="#lnkDetNum">
							<s:param name="selectedConfirmNumber" value="%{confirmNumber}" />
							<s:param name="selectedDetailNumber" value="%{itemId}" />
							<s:param name="selectedBundleNumber" value="%{bundleId}" />
							<s:param name="includeCancelledOrders" value="true" />
							<s:param name="hideBackToSearchLink" value="true" />
							<s:param name="selectedItem"
								value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@BUNDLE_DETAIL" />
						</s:url>
				   </s:if>
				   <s:else>
						<s:url escapeAmp="false" action="viewOrderHistory" includeParams="none"
							namespace="/om/history" id="#lnkDetNum">
							<s:param name="selectedConfirmNumber" value="%{confirmNumber}" />
							<s:param name="selectedDetailNumber" value="%{itemId}" />
							<s:param name="selectedBundleNumber" value="-1" />
							<s:param name="includeCancelledOrders" value="true" />
							<s:param name="hideBackToSearchLink" value="true" />
							<s:param name="selectedItem"
								value="@com.copyright.ccc.web.actions.ordermgmt.OrderManagementConstants@ORDER_DETAIL" />
						</s:url>
					</s:else>	
					<td> <a href="<s:property value="#lnkDetNum" />" target="_blank"
					> <s:property	value="itemId" /> </a></td>
                  <!--  <td> <s:property	value="itemId" /></td>--> 
                  <td> <s:property	value="createDateStr" /></td>
                  <td>  <s:property	value="status" /></td>
                  <s:if test="showParmOne">
                  	<td> <s:property	value="itemParmOneValue" /> </td>
                  </s:if>
				  <s:if test="showParmTwo">
                  	<td> <s:property	value="itemParmTwoValue" /> </td>
                  </s:if>
                  <td> <s:property	value="itemLicenseeFee" /> </td>
                  <td> <s:property	value="itemRoyalty" /> </td>
                  <td> <s:property	value="itemDiscount" /> </td>
				  <s:if test="shippingAndHandling">
				  	<td> <s:property	value="itemHardCopyCost" /> </td>
				  	<td> <s:property	value="itemTotalTax" /> </td>
				  	<td> <s:property	value="itemTotalShipping" /> </td>
				  </s:if>	
                  <td> <s:property	value="itemTotalPrice" /> </td>
				  <s:if test="localCurrency">
					<td> <s:property value="itemTotalPriceLocalCurrencyDisplay" /> </td>
				  </s:if>	
				  <td align="left">
				  	<s:checkbox id="creditDetailFull_%{item.itemId}" cssClass="odd"	theme="simple" name="creditDetailFull2" label="Credit Detail in full" labelSeparator=""></s:checkbox>
					<label class="label" style="text-align:left; font-weight:bold">Credit Detail in full</label>
				  </td>
                </tr>                
                <tr style="margin-top: 5px;  " >	
					<td colspan="3"> &nbsp;
					</td>
					<s:if test="showParmOne">
						<s:hidden id='parmOneValueAdjOrg_%{item.itemId}' name='parmOneValueAdjOrg_%{item.itemId}'  value="%{parmOneValueAdj}"/>
						<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 40px;" name="parmOneValueAdj1" value="%{parmOneValueAdj}" id="parmOneValueAdj_%{item.itemId}"
						disabled="DPSDuration"/>
					</s:if>
					<s:if test="showParmTwo">
						<s:hidden id='parmTwoValueAdjOrg_%{item.itemId}' name='parmTwoValueAdjOrg_%{item.itemId}'  value="%{parmTwoValueAdj}"/>
						<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 40px;" name="parmTwoValueAdj1" value="%{parmTwoValueAdj}" id="parmTwoValueAdj_%{item.itemId}"/>
					</s:if>
					<s:hidden id='licenseeFeeAdjOrg_%{item.itemId}' name='licenseeFeeAdjOrg_%{item.itemId}'  value="%{licenseeFeeAdjStr}"/>
					<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 60px;" name="licenseeFeeAdj1" value="%{licenseeFeeAdjStr}" id="licenseeFeeAdj_%{item.itemId}"
						 disabled="rightsLink"/>
					<s:hidden id='royaltyAdjOrg_%{item.itemId}' name='royaltyAdjOrg_%{item.itemId}'  value="%{royaltyAdjStr}"/>
					<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 60px;" name="royaltyAdj1" value="%{royaltyAdjStr}" id="royaltyAdj_%{item.itemId}" 
						disabled="rightsLink"/>
					<td> <s:property	value="discountAdj" /> </td>
					<s:if test="shippingAndHandling">
				  		<td> <s:property	value="hardCopyCostAdj" /> </td>
						<td> <s:property	value="totalTaxAdj" /> </td>
						<td> <s:property	value="totalShippingAdj" /> </td>
					</s:if>
					<td> <s:property	value="totalPriceAdjStr" /> </td>
				    <s:if test="localCurrency">
						<td> <s:property	value="totalPriceAdjLocalCurrencyDisplay" /> </td>
				    </s:if>
					<s:submit align="left" onclick="resetDetailFormFields(%{item.itemId}); return false;" value="Clear" id="submitClear_%{item.itemId}"/>
                </tr>
                <tr style=";margin-top: 10px;  " >		
                    <td style="text-align: right; font-style:italic; font-weight:bold" class="odd-bold" colspan="3">
						<label for="reason" class="label">Totals after Adjustment:</label>
					</td>
				  <s:if test="showParmOne">
                  	<td> <s:property	value="parmOneValueNet" /> </td>
                  </s:if>
                  <s:if test="showParmTwo">
                  	<td> <s:property	value="parmTwoValueNet" /> </td>
                  </s:if>
                  <td> <s:property	value="licenseeFeeNet" /> </td>
                  <td> <s:property	value="royaltyNet" /> </td>
                  <td> <s:property	value="discountNet" /> </td>
				  <s:if test="shippingAndHandling">
				  		<td> <s:property	value="hardCopyCostNet" /> </td>
				  		<td> <s:property	value="totalTaxNet" /> </td>
						<td> <s:property	value="totalShippingNet" /> </td>
				  </s:if>
                  <td> <s:property	value="totalPriceNet" /> </td>
				  <s:if test="localCurrency">
						<td> <s:property	value="totalPriceNetLocalCurrencyDisplay" /> </td>
				  </s:if>
					<s:submit id="submitCalculate_%{item.itemId}" align="left" onclick="doCalcAdj(%{item.itemId}); return false;" value="Calculate Adjustment" />
					
                </tr>			
</table>
</div>
