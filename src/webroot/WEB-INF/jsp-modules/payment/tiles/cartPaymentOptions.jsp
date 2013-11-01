		<!-- COMMON TEMPLATE -->
	<html:hidden styleId="frm_cardType" value="NONE" name="selectCartPaymentActionForm" property="creditCardType" />
	<html:hidden styleId="frm_paymentProfileId" value="NONE" name="selectCartPaymentActionForm" property="paymentProfileId" />
	<html:hidden styleId="frm_lastFourDigits" value="NONE" name="selectCartPaymentActionForm" property="creditCardNumber" />
	<html:hidden styleId="frm_cardholderName" value="NONE" name="selectCartPaymentActionForm" property="creditCardNameOn" />
	<html:hidden styleId="frm_expirationDate" value="NONE" name="selectCartPaymentActionForm" property="expirationDate" />
	<html:hidden styleId="frm_cccPaymentProfileId" value="NONE" name="selectCartPaymentActionForm" property="cccPaymentProfileId" />
	
	
<div id="mainPaymentTypeOptionSelectorBlock">
<logic:equal name="selectCartPaymentActionForm" property="canOnlyBeInvoiced" value="false">
  <span class="largertype bold">Billing Method</span>
<table border="0" cellpadding="0" cellspacing="0" class="indent-1">
  <tr>
    <td><br />If you choose to be invoiced, you can change or cancel your order until the invoice is sent. <br />
    If you pay by credit card, your order will be finalized and your card will be charged within 24 hours. <br /></td>
  </tr>
  
</table>

<div class="horiz-rule"></div>
</logic:equal>

<br></br>


<logic:equal name="selectCartPaymentActionForm" property="hasOnlySpecialOrders" value="true">
<br></br>
<span class="largertype bold">Pay by invoice options:</span>
</logic:equal>

<logic:equal name="selectCartPaymentActionForm" property="canOnlyBeInvoiced" value="false">

<bean:define id="currencyAmountInUSD"><bean:write name="selectCartPaymentActionForm" property="cartTotalWithNoDollarSign" />
    </bean:define>
    
    <bean:define id="currType"><bean:write name="selectCartPaymentActionForm" property="currencyType" />
    </bean:define>

<logic:equal name="selectCartPaymentActionForm" property="cybersourceSiteUp" value="true">
<p class="indent-1"><br /> 
  <html:radio name="selectCartPaymentActionForm" styleId="ccSelect" property="paymentType" value="credit-card" onclick="$('#credit-card-block').show();$('#multi-currency-block').show();getSelectedCurrency('${currType}','${currencyAmountInUSD}');getCreditCards();" />
  <strong>Pay with a Credit Card</strong>
  <span id="req1" class="smalltype icon-alert" style="position: relative; bottom: 15px; left: 175px;">Special Orders in your order may still be invoiced.<util:contextualHelp helpId="45" rollover="true">&nbsp;&nbsp;More info</util:contextualHelp></span> <!--<span id="req1" class="required">*Required</span>--> 
</p>
</logic:equal>
<logic:equal name="selectCartPaymentActionForm" property="cybersourceSiteUp" value="false">
<p class="indent-1"><br /> 
  <html:radio name="selectCartPaymentActionForm" styleId="ccSelect" property="paymentType" value="credit-card" onclick="$('#credit-card-block').show();$('#multi-currency-block').show();getSelectedCurrency('${currType}','${currencyAmountInUSD}');getCreditCards();" disabled="true" />
  <strong><font color="red">We're sorry, the credit card service is currently unavailable. Please try again later.</font></strong>
</p>
</logic:equal>

<table border="0" cellpadding="0" cellspacing="0" class="indent-1">
  <tr>
    <td width="239">      
      <html:radio name="selectCartPaymentActionForm" styleId="invSelect" property="paymentType" value="invoice"  onclick="$('#credit-card-block').hide();$('#multi-currency-block').hide();" />
      <strong>Pay by invoice </strong><span class="importanttype"></span>
    </td>
    <td>PO #: (optional)
      <html:text name="selectCartPaymentActionForm" styleId="poNum" property="purchaseOrderNumber" size="30" maxlength="50" />
    </td>
    <td rowspan="2" align="right" width="140">
      <a href="http://clicktoverify.truste.com/pvr.php?page=validate&url=www.copyright.com&sealid=101" target="_blank"><img src="https://www.copyright.com/media/images/clickseal.gif"></a>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <div class="indent-2 smalltype">
        <logic:equal name="selectCartPaymentActionForm" property="alwaysInvoiceFlag" value="true">
        <html:checkbox name="selectCartPaymentActionForm" styleId="alwaysInvoice" property="alwaysInvoice" value="true" onclick="ClickCheckBox(this)" />
        </logic:equal>
        <logic:equal name="selectCartPaymentActionForm" property="alwaysInvoiceFlag" value="false">
        <html:checkbox name="selectCartPaymentActionForm" styleId="alwaysInvoice" property="alwaysInvoice" onclick="ClickCheckBox(this)" />
        </logic:equal>
        Always invoice me and skip this step for future orders 
        <br>(You can change this billing preference in <a href="manageAccount.do"><u>Manage Account</u></a>)
      </div>
    </td>
  </tr>
</table>
</logic:equal>


<logic:equal name="selectCartPaymentActionForm" property="canOnlyBeInvoiced" value="true">
<br></br>
<table border="0" cellpadding="0" cellspacing="0" class="indent-1">

  <tr>
    <td>PO #: (optional)
        <html:text name="selectCartPaymentActionForm" styleId="poNum" property="purchaseOrderNumber" size="30" maxlength="50" />
      </td>
  </tr>
  <tr>
    <td colspan="2"><div class="indent-2 smalltype">
    
    <logic:equal name="selectCartPaymentActionForm" property="alwaysInvoiceFlag" value="true">
    <html:checkbox name="selectCartPaymentActionForm" styleId="alwaysInvoice" property="alwaysInvoice" value="true" onclick="ClickCheckBox(this)" />
    </logic:equal>
    <logic:equal name="selectCartPaymentActionForm" property="alwaysInvoiceFlag" value="false">
    <html:checkbox name="selectCartPaymentActionForm" styleId="alwaysInvoice" property="alwaysInvoice" onclick="ClickCheckBox(this)" />
    </logic:equal>
      Always invoice me and skip this step for future orders </div>
      	<logic:equal name="selectCartPaymentActionForm" property="teleSalesUp" value="true">
      (You can change this billing preference in <a href="manageAccount.do"><u>Manage Account</u></a>)</logic:equal></td>
    </tr>
</table>
<input type="hidden" id="defaultPaymentType" value="invoice"/>
</logic:equal>
<br />

<table border="0" cellpadding="0" cellspacing="0" class="indent-1" id="test3">

<tr>

<logic:equal name="selectCartPaymentActionForm" property="userChannel" value="ORGADD">
<td><br><strong>Billing address:</strong>
	Please contact your account administrator to edit the billing address. <br>Don't know your account administrator? <a href="mailto:info@copyright.com" ><u>Contact us</u></a> </td>
</logic:equal>

<logic:notEqual name="selectCartPaymentActionForm" property="userChannel" value="ORGADD">
  <logic:equal name="selectCartPaymentActionForm" property="teleSalesUp" value="true">
  <logic:notEmpty name="selectCartPaymentActionForm" property="billToContactName">
    <td><br><strong>Billing address:</strong>
        Edit in <a href="manageAccount.do" ><u>Manage Account</u></a> </td>
  </logic:notEmpty>
  </logic:equal>
</logic:notEqual>


</tr>
<tr>
<td>
<logic:equal name="selectCartPaymentActionForm" property="teleSalesUp" value="true">
<br/>
<table border="0" cellpadding="0" cellspacing="0" class="indent-2">
<logic:notEmpty name="selectCartPaymentActionForm" property="billToContactName">
<tr><td><bean:write name="selectCartPaymentActionForm" property="billToContactName"/></td></tr>
</logic:notEmpty>
<logic:notEmpty name="selectCartPaymentActionForm" property="userCompany">
    <tr><td><bean:write name="selectCartPaymentActionForm" property="userCompany"/></td></tr>
</logic:notEmpty>
<tr><td><bean:write name="selectCartPaymentActionForm" property="address1"/></td></tr>
<logic:notEmpty name="selectCartPaymentActionForm" property="address2">
<tr><td><bean:write name="selectCartPaymentActionForm" property="address2"/></td></tr>
</logic:notEmpty>
<tr><td><bean:write name="selectCartPaymentActionForm" property="city"/>,
<bean:write name="selectCartPaymentActionForm" property="state"/>
<bean:write name="selectCartPaymentActionForm" property="zip"/></td></tr>
<logic:notEmpty name="selectCartPaymentActionForm" property="country">
    <tr><td><bean:write name="selectCartPaymentActionForm" property="country"/></td></tr>
</logic:notEmpty>
<logic:notEmpty name="selectCartPaymentActionForm" property="billToEmail">
    <tr><td><bean:write name="selectCartPaymentActionForm" property="billToEmail"/></td></tr>
</logic:notEmpty>
<logic:notEmpty name="selectCartPaymentActionForm" property="billToPhone">
    <tr><td><bean:write name="selectCartPaymentActionForm" property="billToPhone"/></td></tr>
</logic:notEmpty>

</table>

</logic:equal>

</td>

</tr>

</table>

<logic:equal name="selectCartPaymentActionForm" property="canOnlyBeInvoiced" value="false">
<logic:equal name="selectCartPaymentActionForm" property="cybersourceSiteUp" value="true">
<table border="0" class="indent-2" id="multi-currency-block" style="display:none">
  <tr>
    <td><b>Select Currency:</b><span id="test" class="importanttype">*</span>
    <bean:define id="currencyAmountInUSD"><bean:write name="selectCartPaymentActionForm" property="cartTotalWithNoDollarSign" />
    </bean:define>
    <bean:define id="cartCurrencyAmountInUSD"><bean:write name="selectCartPaymentActionForm" property="cartChargeTotalWithNoDollarSign" />
    </bean:define>
    
    <html:select name="selectCartPaymentActionForm" property="currencyType" styleId="cType"  onchange ="getSelectedCurrency(this.value,'${cartCurrencyAmountInUSD}');">
    	<html:option value="USD">USD - $</html:option>
    	<html:option value="EUR">EUR - &#8364;</html:option>
    	<html:option value="GBP">GBP - &#163;</html:option>
    	<html:option value="JPY">JPY - &#165;</html:option>
    	<html:option value="CAD">CAD - $</html:option>
      <html:option value="CHF">CHF - &#8355;</html:option>
    </html:select>&nbsp;&nbsp;
    
    <logic:equal value="true" name="selectCartPaymentActionForm"  property="displayChargeTotal">
        <b>Charge Total:</b>&nbsp;
		<span id="displayTotalPriceIdUSD" class="price"><bean:write name="selectCartPaymentActionForm" property="cartChargeTotalWithNoDollarSign" /> USD</span>
	    <span style="display: none;" id="displayTotalPriceIdNonUSD" class="price"><bean:write name="selectCartPaymentActionForm" property="cartChargeTotal" /></span>
	    </logic:equal>
	    <logic:equal value="false" name="selectCartPaymentActionForm"  property="displayChargeTotal">
	    <b>Order Total:</b>&nbsp;
		<span id="displayTotalPriceIdUSD" class="price"><bean:write name="selectCartPaymentActionForm" property="cartTotalWithNoDollarSign" /> USD</span>
	    <span style="display: none;" id="displayTotalPriceIdNonUSD" class="price"><bean:write name="selectCartPaymentActionForm" property="cartTotal" /></span>
	    </logic:equal>
	    	<logic:equal name="selectCartPaymentActionForm" property="showExcludeTBDItemText" value="true">
								    <span class="smalltype defaultweight">(Excludes TBD items)</span>
			</logic:equal>
	    
    <span id="ccInfo" style="display: none;">&nbsp;&nbsp;&nbsp;<util:contextualHelp helpId="69" rollover="true">Terms & Conditions</util:contextualHelp></span> <!--<span id="req1" class="required">*Required</span>--> 
    </td>
    
    </tr>
      
  
</table>
</logic:equal>

</logic:equal>


</div>