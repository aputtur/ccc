<!--  COMMON TEMPLATE -->

	<html:hidden styleId="frm_cardType" value="NONE" name="acceptSpecialOrderPaymentForm" property="creditCardType" />
	<html:hidden styleId="frm_paymentProfileId" value="NONE" name="acceptSpecialOrderPaymentForm" property="paymentProfileId" />
	<html:hidden styleId="frm_lastFourDigits" value="NONE" name="acceptSpecialOrderPaymentForm" property="creditCardNumber" />
	<html:hidden styleId="frm_cardholderName" value="NONE" name="acceptSpecialOrderPaymentForm" property="creditCardNameOn" />
	<html:hidden styleId="frm_expirationDate" value="NONE" name="acceptSpecialOrderPaymentForm" property="expirationDate" />
	<html:hidden styleId="frm_cccPaymentProfileId" value="NONE" name="acceptSpecialOrderPaymentForm" property="cccPaymentProfileId" />
	<!-- Comon Template Ends here -->

<div id="mainPaymentTypeOptionSelectorBlock">
<strong><label style="width:661px;">&nbsp;Please select your payment method:</label></strong>
<br/><br/>
    <html:radio name="acceptSpecialOrderPaymentForm"  styleId="invSelect" property="paymentType" value="invoice"  onclick="$('#credit-card-block').hide();" />
      <strong>Pay by invoice </strong><span class="importanttype"></span>
        <div style="display:none"><label style="margin-left:20px">PO #: (optional)
        <html:text name="acceptSpecialOrderPaymentForm" styleId="poNum" property="purchaseOrderNumber" size="30" maxlength="50" />
      </label></div>
  <br/>
<logic:equal name="acceptSpecialOrderPaymentForm" property="cybersourceSiteUp" value="true">
<html:radio name="acceptSpecialOrderPaymentForm" styleId="ccSelect" property="paymentType" value="credit-card" onclick="$('#credit-card-block').show();getCreditCards();" />
<strong>Pay by credit card </strong>
</logic:equal>
<logic:equal name="acceptSpecialOrderPaymentForm" property="cybersourceSiteUp" value="false">
<p class="indent-1"><br /> 
  <html:radio name="acceptSpecialOrderPaymentForm" styleId="ccSelect" property="paymentType" value="credit-card" onclick="$('#credit-card-block').show();getCreditCards();" disabled="true" />
  <strong><font color="red">We're sorry, the credit card service is currently unavailable. Please try again later.</font></strong>
</p>
</logic:equal>

<br/>
</div>