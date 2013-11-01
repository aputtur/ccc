<#--
Uses: chargesTable.ftl
	
Fields:
-->
<table style="border: 1px solid #C6C6C6; background-color: #FFFFFF; text-align: right;">
	<tr>
		<th colspan="2" style="background-color: F3F6F9; color: black; text-align: center;"
			align="center">
			Charges in USD
		</th>
	</tr>
	<tr>
		<@s.label label="License Fee" cssClass="odd" name="item.totalLicenseeFeeValue" labelSeparator=": $">
		</@s.label>
	</tr>
	<tr>
		<@s.label label="Discount" cssClass="odd" name="item.totalDiscountValue" labelSeparator=": $">
		</@s.label>
	</tr>
	<tr>
		<@s.label label="Royalty" cssClass="odd" name="item.totalDistributionPayableValue" labelSeparator=": $">
		</@s.label>
	</tr>
	<tr style="height: 5px;" ><td colspan="2">	<hr />	</td></tr>
	<tr>
		<@s.label label="Total" cssClass="odd" name="item.totalPriceValue" labelSeparator=": $" > 
		</@s.label>
	</tr>
</table>