<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>

<%@ page import="com.copyright.data.order.UsageDataNet"%>
<%@ page import="com.copyright.ccc.business.services.adjustment.OrderAdjustmentConstants" %>

<%
  boolean enterAdjustmentAllowed = com.copyright.ccc.business.security.UserContextService.hasPrivilege( com.copyright.ccc.business.security.CCPrivilegeCode.ENTER_ORDER_ADJUSTMENT ); 
  boolean commitAdjustmentAllowed = com.copyright.ccc.business.security.UserContextService.hasPrivilege( com.copyright.ccc.business.security.CCPrivilegeCode.COMMIT_ORDER_ADJUSTMENT ); 
%>

<div id="originalDetailsContainer" class="container">
       
 <fieldset>
    <legend>Customer Information</legend>
    <div class="container" style="line-height: 140%">
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.fullName">
          <span style="font-weight: bold;">
            <bean:write name="adjustmentForm"
                        property="adjustment.customer.fullName"/>
          </span>
          &nbsp;&nbsp;&nbsp;&nbsp;
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.accountNumber">
          <span style="font-weight: bold;">
            Account Number:&nbsp;
            <bean:write name="adjustmentForm"
                        property="adjustment.customer.accountNumber"/>
            <br/>
          </span>
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.companyName">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.companyName"/>
          <br/>
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.address1">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.address1"/>
          <br/>
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.address2">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.address2"/>
          <br/>
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.address3">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.address3"/>
          <br/>
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.address4">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.address4"/>
          <br/>
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.city">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.city"/>
          ,&nbsp;
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.state">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.state"/>
          &nbsp;
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.zipCode">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.zipCode"/>
          <br/>
        </logic:notEmpty>
        <logic:notEmpty name="adjustmentForm"
                        property="adjustment.customer.country">
          <bean:write name="adjustmentForm"
                      property="adjustment.customer.country"/>
        </logic:notEmpty>
    </div>
  </fieldset>
         
</div>


<%//Full credit and global adjustment - BEGIN%>
<logic:equal value="true" name="adjustmentForm" property="UIModeAdjustment">
      
  <div id="fullCreditContainer" class="container">
    
    <fieldset>
      
      <legend>
      
          Full Credit this 
          <logic:equal value="true" name="adjustmentForm" property="adjustment.invoiceAdjustment">Invoice</logic:equal>
          <logic:equal value="true" name="adjustmentForm" property="adjustment.purchaseAdjustment">Purchase</logic:equal>
          <logic:equal value="true" name="adjustmentForm" property="adjustment.detailAdjustment">Detail</logic:equal>
      
      </legend>
      <div class="container">
        <html:form action="/adjustment/adjustment.do?operation=performFullCredit"
                   styleId="fullCreditForm">
          <table style="width: 100%; border-collapse: collapse;">
            <tr>
              <td style="text-align: left; width: 105px;">
                <span style="font-weight: bold;">
                  Reason:
                </span>
              </td>
              <td style="text-align: left; width: 483px;">
                <html:select name="adjustmentForm" property="reason">
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_NUM_COPIES_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_REVISED_NUM_COPIES%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_NUM_PAGES_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_REVISED_NUM_PAGES%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_DUPLICATE_ORDER_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_DUPLICATE_ORDER%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_MATERIAL_NOT_USED_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_MATERIAL_NOT_USED%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_ONGOING_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_ONGOING%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_ONGOING_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_ONGOING%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_LICENSEE_FEE_ADJUSTMENT_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_LICENSEE_FEE_ADJUSTMENT%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_REPORTING_ERROR_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_REVISED_REPORTING_ERROR%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_DISCONTINUED_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_DISCONTINUED%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_DISCONTINUED_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_DISCONTINUED%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_ROYALTY_FEE_ADJUSTMENT_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_ROYALTY_FEE_ADJUSTMENT%>
                  </html:option>
                </html:select>
              </td>
              <td style="text-align: left; width: 203px; padding-left: 15px;">
                <%if(enterAdjustmentAllowed){%>
                <!--<html:submit value="Perform Full Credit"/><br/><br/>-->
                <img src='<html:rewrite page="/resources/adjustment/images/perform_full_credit.png"/>'
                     onclick="javascript:if(document.getElementById('fullCreditForm'))document.getElementById('fullCreditForm').submit()"/>
                <%}%>
              </td>
            </tr>
          </table>
        </html:form>
      </div>
    </fieldset>
    
  </div>
  
  <div id="globalAdjustmentContainer" class="container">
  
    <fieldset>
      
      <legend>Global Adjustment</legend>
      <div class="container">
        <form method="post" action="" id="globalAdjustmentForm">
          <table style="text-align: left; width: 100%; border-collapse: collapse;">
            <tr>
              <td style="text-align: left;" nowrap="nowrap">
                <span style="font-weight: bold;">
                  Field to Adjust:
                </span>
              </td>
              <td style="text-align: left;">
                <html:select name="adjustmentForm" property="fieldToAdjust"
                             onchange="javascript:handleFieldToAdjustChange()"
                             styleId="fieldToAdjustDropDown" >
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_PAGES%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_PAGES%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_COPIES%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_COPIES%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_RECIPIENTS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_RECIPIENTS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.DURATION%>">
                    <%=OrderAdjustmentConstants.DURATION%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_STUDENTS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_STUDENTS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_EXCERPTS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_EXCERPTS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_QUOTATIONS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_QUOTATIONS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_CHARTS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_CHARTS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_GRAPHS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_GRAPHS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_FIGURE_DIAGRAM_TABLES%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_FIGURE_DIAGRAM_TABLES%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_PHOTOGRAPHS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_PHOTOGRAPHS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_ILLUSTRATIONS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_ILLUSTRATIONS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_CARTOONS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_CARTOONS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.NUMBER_OF_LOGOS%>">
                    <%=OrderAdjustmentConstants.NUMBER_OF_LOGOS%>
                  </html:option>
                  <html:option value="<%=OrderAdjustmentConstants.LICENSEE_FEE%>">
                    <%=OrderAdjustmentConstants.LICENSEE_FEE%>
                  </html:option>
                </html:select>
              </td>
              <td>
                <span style="font-weight: bold;">
                  Current Value:
                </span>
              </td>
              <td>
                <html:text name="adjustmentForm" property="currentValue"
                           styleClass="adjustmentInput"
                           styleId="currentValueInput"
                           onkeypress="var isAllowDecimalPoint = document.getElementById('fieldToAdjustDropDown').value == 'Licensee Fee'; return numbersOnly(event, this, isAllowDecimalPoint);"/>
                <html:select name="adjustmentForm" property="currentValue"
                             styleId="currentValueDropDown"
                             style="display: none; width: 75px;">
                  <html:option value="<%= String.valueOf(UsageDataNet.TO_30_DAYS_FEE)  %>">
                    30 Days
                  </html:option>
                  <html:option value="<%= String.valueOf(UsageDataNet.TO_180_DAYS_FEE)  %>">
                    180 Days
                  </html:option>
                  <html:option value="<%= String.valueOf(UsageDataNet.TO_365_DAYS_FEE)  %>">
                    365 Days
                  </html:option>
                  <html:option value="<%= String.valueOf(UsageDataNet.UNLIMITED_DAYS_FEE) %>">
                    Unlimited
                  </html:option>
                </html:select>
              </td>
              <td>
                <span style="font-weight: bold;">
                  New Value:
                </span>
              </td>
              <td>
                <html:text name="adjustmentForm" property="newValue"
                           styleClass="adjustmentInput" styleId="newValueInput"
                           onkeypress="var isAllowDecimalPoint = document.getElementById('fieldToAdjustDropDown').value == 'Licensee Fee'; return numbersOnly(event, this, isAllowDecimalPoint);" />
                <html:select name="adjustmentForm" property="newValue"
                             styleId="newValueDropDown" style="display: none;  width: 75px;">
                  <html:option value="<%= String.valueOf(UsageDataNet.TO_30_DAYS_FEE)  %>">
                    30 Days
                  </html:option>
                  <html:option value="<%= String.valueOf(UsageDataNet.TO_180_DAYS_FEE)  %>">
                    180 Days
                  </html:option>
                  <html:option value="<%= String.valueOf(UsageDataNet.TO_365_DAYS_FEE)  %>">
                    365 Days
                  </html:option>
                  <html:option value="<%= String.valueOf(UsageDataNet.UNLIMITED_DAYS_FEE) %>">
                    Unlimited
                  </html:option>
                </html:select>
              </td>
              <td style="text-align: left; padding-top: 15px; padding-left: 7px;"
                  rowspan="2">
                <%if(enterAdjustmentAllowed){%>
                <!--<input type="button" value="Perform Global Adjustment" onclick="javascript:performGlobalAdjustment()">-->
                <img src='<html:rewrite page="/resources/adjustment/images/perform_global_adjustment.png"/>'
                     onclick="javascript:performGlobalAdjustment()"
                     align="middle"/>
                <%}%>
              </td>
            </tr>
            <tr>
              <td style="text-align: left;">
                <span style="font-weight: bold;">
                  Reason:
                </span>
              </td>
              <td style="text-align: left;" colspan="5">
                <html:select name="adjustmentForm" property="reason">
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_NUM_COPIES_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_REVISED_NUM_COPIES%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_NUM_PAGES_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_REVISED_NUM_PAGES%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_DUPLICATE_ORDER_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_DUPLICATE_ORDER%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_MATERIAL_NOT_USED_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_MATERIAL_NOT_USED%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_ONGOING_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_ONGOING%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_ONGOING_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_ONGOING%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_LICENSEE_FEE_ADJUSTMENT_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_LICENSEE_FEE_ADJUSTMENT%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_REVISED_REPORTING_ERROR_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_REVISED_REPORTING_ERROR%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_DISCONTINUED_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_PARTIAL_INVOICE_PAYMENT_DISCONTINUED%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_DISCONTINUED_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_NON_PAYMENT_INVOICE_DISCONTINUED%>
                  </html:option>
                  <html:option value="<%=String.valueOf(OrderAdjustmentConstants.REASON_ROYALTY_FEE_ADJUSTMENT_CD)%>">
                    <%=OrderAdjustmentConstants.REASON_ROYALTY_FEE_ADJUSTMENT%>
                  </html:option>
                </html:select>
              </td>
            </tr>
          </table>
        </form>
      </div>
    </fieldset>
    
  </div>

</logic:equal>
<%//Full credit and global adjustment - END%>