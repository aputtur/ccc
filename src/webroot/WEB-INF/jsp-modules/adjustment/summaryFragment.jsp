<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>

<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustmentSummary" %> 
<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustment" %>

<div id="summaryContainer" class="container">
      
    <bean:define id="summary" name="adjustmentForm" property="adjustment.summary" type="OrderAdjustmentSummary"/>
    <bean:define id="adjForm" name="adjustmentForm" type="com.copyright.ccc.web.forms.adjustment.AdjustmentActionForm" />
  
    <%
      String summaryLabel = "";
      
      boolean isInvoiceAdjustment = adjForm.getAdjustment().isInvoiceAdjustment();
      boolean isPurchaseAdjustment = adjForm.getAdjustment().isPurchaseAdjustment();
      boolean isDetailAdjustment = adjForm.getAdjustment().isDetailAdjustment();
      
      if( isInvoiceAdjustment )
      {
        summaryLabel = "Invoice";
      }
      
      if( isPurchaseAdjustment )
      {
        summaryLabel = "Purchase";
      }
      
   %>
  
  <fieldset>
    <legend><%=summaryLabel%>&nbsp;Summary</legend>
    
    <div class="container">
    
      <% boolean isInvoiceOrPurchaseAdjustment = isInvoiceAdjustment || isPurchaseAdjustment; %><% if( isInvoiceOrPurchaseAdjustment ){%>
      <table style="border-collapse: collapse; width: 100%;">
        <tr>
          <th style="width: 180px;">
            &nbsp;
          </th>
          <th style="width: 150px;">
            Licensee Fee
          </th>
          <th style="width: 150px;">
            Royalty
          </th>
          <th style="width: 150px;">
            Discount
          </th>
          <th style="width: 150px;">
            Total
          </th>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold; "
              class="summaryCell">
            Original 
            <%=summaryLabel%>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="originalDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="originalDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="originalDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="originalDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold;"
              class="summaryCell">Prior Adjustments</td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="adjustmentsNetDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="adjustmentsNetDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="adjustmentsNetDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="adjustmentsNetDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold;"
              class="summaryCell">Current Adjustment</td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="currentAdjustmentDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="currentAdjustmentDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="currentAdjustmentDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="currentAdjustmentDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left;" class="summaryTotalsCell">
            <%=summaryLabel%>
            Total
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="total.licenseeFee"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="total.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="total.discount"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="summary" property="total.total"/> ); </script>
          </td>
        </tr>
      </table>
      <%}%>
      
      <% if( isDetailAdjustment ){%>
      <table style="border-collapse: collapse; width: 100%;">
        <tr>
          <th style="width: 180px; text-align: left;">
            Invoice
          </th>
          <th style="width: 150px">
            Licensee Fee
          </th>
          <th style="width: 150px">
            Royalty
          </th>
          <th style="width: 150px">
            Discount
          </th>
          <th style="width: 150px">
            Total
          </th>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold;"
              class="summaryCell">Original Invoice</td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.originalDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.originalDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.originalDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.originalDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold;"
              class="summaryCell">Prior Adjustments</td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.adjustmentsNetDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.adjustmentsNetDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.adjustmentsNetDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.adjustmentsNetDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold;"
              class="summaryCell">Current Adjustment</td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.currentAdjustmentDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.currentAdjustmentDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.currentAdjustmentDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.currentAdjustmentDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left;" class="summaryTotalsCell">Invoice Total</td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.total.licenseeFee"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.total.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.total.discount"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.invoiceSummary.total.total"/> ); </script>
          </td>
        </tr>
      </table>
      <br/><br/>
      
      <table style="border-collapse: collapse; width: 100%;">
        <tr>
          <th style="width: 180px; text-align: left;">
            Purchase
          </th>
          <th style="width: 150px">
            Licensee Fee
          </th>
          <th style="width: 150px">
            Royalty
          </th>
          <th style="width: 150px">
            Discount
          </th>
          <th style="width: 150px">
            Total
          </th>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold;"
              class="summaryCell">Original Purchase</td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.originalDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.originalDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.originalDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.originalDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold;"
              class="summaryCell">Prior Adjustments</td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.adjustmentsNetDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.adjustmentsNetDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.adjustmentsNetDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.adjustmentsNetDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left; font-weight: bold;"
              class="summaryCell">Current Adjustment</td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.currentAdjustmentDetails.licenseeFee"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.currentAdjustmentDetails.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.currentAdjustmentDetails.discount"/> ); </script>
          </td>
          <td class="summaryCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.currentAdjustmentDetails.total"/> ); </script>
          </td>
        </tr>
        <tr>
          <td style="text-align: left;" class="summaryTotalsCell">Purchase Total</td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.total.licenseeFee"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.total.royaltyComposite"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.total.discount"/> ); </script>
          </td>
          <td class="summaryTotalsCell">
            <script type="text/javascript">decimalFormat( <bean:write name="adjustmentForm" property="adjustment.summaries.purchaseSummary.total.total"/> ); </script>
          </td>
        </tr>
      </table><%}%>
    
    </div>
    
  </fieldset>
  
</div>