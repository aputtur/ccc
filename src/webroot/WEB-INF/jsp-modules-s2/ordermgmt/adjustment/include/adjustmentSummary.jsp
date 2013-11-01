<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustmentConstants" %>
<%@ page import="java.util.*, java.lang.*"%>
<div style="width: 972px; border: 1px solid #808080; margin-top: 0px; margin-left: 10px; margin-right: 10px; margin-bottom: 10px; padding: 0px;">
<table class="editTable" width="100%" style="margin-botton: 0px;">
	<tr style="background-color: #E2E9F0;">
		<td valign="top" style="width: 5px; text-align: center;">
			&nbsp;
		</td>
		<td valign="middle" style="height: 26px; width: 959px;">

			<div id="orderDetailSectionHeader"
				style="display: inline; padding-top: 2px; font-size: 14px; color:#6F97BB ; font-weight:bold"">
					<s:property	value="summaryHeaderLabel" /> 
			</div>
		</td>
	</tr>
</table>

<table id="adjustmentHdr"   style="text-align: center; width: 972px; background-color: #FFFFFF; border: 1px solid #808080; margin-top: 0px; margin-left: 0px; margin-right: 0px; margin-bottom: 0px; padding: 0px;">
 				<col style="width: 200px;"/>
 				<col style="width: 200px;"/>
 				<col style="width: 200px;"/>
 				<col style="width: 200px;"/>
				<s:if test="adjHeader.shippingAndHandling">
					<col style="width: 200px;"/>
					<col style="width: 200px;"/>
 					<col style="width: 200px;"/>
				</s:if>
 				<col style="width: 200px;"/>
				<s:if test="adjHeader.localCurrency">
					<col style="width: 200px;"/>
				</s:if>
                <tr style="text-align: center; background-color: #FFFFFF; border: 1px solid #808080; color:#808080" >			  
                  <th style="text-align: left;"><s:property	value="summaryLabel" />  </th>
                  <th> Licensee Fee </th>
                  <th> Royalty </th>
                  <th> Discount </th>
				<s:if test="adjHeader.shippingAndHandling">
					<th> Hard Copy </th>
					<th> Tax </th>
					<th> Shipping </th>
				</s:if>
                  <th> Total </th>
				<s:if test="adjHeader.localCurrency">
                  	<th> Total Local Currency </th>
				</s:if>
                </tr>
                <tr style="background-color: #F3F6F9;  color:#000000" >		
                  <td style="text-align: left;"> <s:property	value="summaryOriginalLabel" /> </td>
                  <td> <s:property	value="adjHeader.adjTotals.originalTotal.licenseeFee" /> </td>
                  <td> <s:property	value="adjHeader.adjTotals.origRoyaltyPrice" /> </td>
                  <td> <s:property	value="adjHeader.adjTotals.originalTotal.discount" /> </td>
				  <s:if test="adjHeader.shippingAndHandling">
				    <td> <s:property	value="adjHeader.adjTotals.originalTotal.totalHardCopyCost" /> </td>
				    <td> <s:property	value="adjHeader.adjTotals.originalTotal.totalTax" /> </td>
				    <td> <s:property	value="adjHeader.adjTotals.originalTotal.totalShipping" /> </td>
				  </s:if>
                  <td> <s:property	value="adjHeader.adjTotals.originalTotal.totalPrice" /> </td>
                  <s:if test="adjHeader.localCurrency">
                  	<td> <s:property	value="adjHeader.adjTotals.originalTotal.totalLocalCurrency" /> </td>
                  </s:if>
			  </tr>
                <tr style="background-color: #E2E9F0;   color:#000000" >		
                  <td style="text-align: left;">Adjustments </td>
                  <td> <s:property	value="adjHeader.adjTotals.adjustmentTotal.licenseeFee" /> </td>
                  <td> <s:property	value="adjHeader.adjTotals.adjRoyaltyPrice" /> </td>
                  <td> <s:property	value="adjHeader.adjTotals.adjustmentTotal.discount" /> </td>
				  <s:if test="adjHeader.shippingAndHandling">
				  	<td> <s:property	value="adjHeader.adjTotals.adjustmentTotal.totalHardCopyCost" /> </td>
				  	<td> <s:property	value="adjHeader.adjTotals.adjustmentTotal.totalTax" /> </td>
				    <td> <s:property	value="adjHeader.adjTotals.adjustmentTotal.totalShipping" /> </td>
				  </s:if>
                  <td> <s:property	value="adjHeader.adjTotals.adjustmentTotal.totalPrice" /> </td>
                  <s:if test="adjHeader.localCurrency">
                  	<td> <s:property	value="adjHeader.adjTotals.adjustmentTotal.totalLocalCurrency" /> </td>	
                  </s:if>
                </tr>
                <tr style="background-color: #CCCCCC;  color:#000000;  font-weight:bold"" >
                  <td style="text-align: left;"> <s:property	value="summaryTotalLabel" /></td>
                  <td> <s:property	value="adjHeader.adjTotals.netTotal.licenseeFee" /> </td>
                  <td> <s:property	value="adjHeader.adjTotals.netRoyaltyPrice" /> </td>
                  <td> <s:property	value="adjHeader.adjTotals.netTotal.discount" /> </td>
				  <s:if test="adjHeader.shippingAndHandling">
				  	<td> <s:property	value="adjHeader.adjTotals.netTotal.totalHardCopyCost" /> </td>
				  	<td> <s:property	value="adjHeader.adjTotals.netTotal.totalTax" /> </td>
				    <td> <s:property	value="adjHeader.adjTotals.netTotal.totalShipping" /> </td>
				  </s:if>
                  <td> <s:property	value="adjHeader.adjTotals.netTotal.totalPrice" /> </td>
                  <s:if test="adjHeader.localCurrency">
                 	<td> <s:property	value="adjHeader.adjTotals.netTotal.totalLocalCurrency" /> </td>
                  </s:if>
                </tr>
                <tr style="background-color: #FFFFFF; border: 1px solid #808080;  color:#000000" >		
                  <td style="height: 26px; "> &nbsp;</td>
                </tr>
</table>
</div>