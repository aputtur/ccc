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
			<div id="sectionHeader"
				style="display: inline; padding-top: 2px; font-size: 14px;">
				Overall Adjustment
			</div>
		</td>
	</tr>
</table>

<table id="adjustmentHdr"   style="text-align: center; width: 972px; background-color: #FFFFFF; border: 1px solid #808080; margin-top: 0px; margin-left: 0px; margin-right: 0px; margin-bottom: 0px; padding: 0px;">
                <tr style="background-color: #FFFFFF;border: 1px solid #808080; color:#808080 ; text-align: left" >			  
                  <th>Select a Reason</th>
                </tr>
         
                <tr style=" margin-top: 10px; margin-bottom: 10px;  text-align: center; height: 50px" >	
                <td style="height: 50px; width: 100px;"> &nbsp;</td>	
                <s:if test="adjHeader.containsTFItems">
					<td style="text-align: center; font-weight: bold;  ">
					 TF Reason
					 <s:select  cssStyle="width: 280px;  font-weight: bold; background-color: rgb(243, 246, 249);  " 
						label="Reason"
						theme="simple"
				        name="globalAdjustmentReasonCode"
				        headerKey="-1" headerValue="Select One"
				        list="TFReasonList"
				        listKey="label"
				        listValue="value"
				        multiple="false"
				        size="1"
				        value="globalAdjustmentReasonCode">
					</s:select>
					</td>
				</s:if>
				<td style="height: 20px; width: 30px;"> &nbsp;</td>
				<s:if test="adjHeader.containsRLItems">
					<td style="text-align: center; font-weight: bold;  ">
					 RL Reason
					 <s:select  cssStyle="width: 280px;  font-weight: bold; background-color: rgb(243, 246, 249);  " 
						label="Reason"
						theme="simple"
				        name="globalRLAdjustmentReasonCode"
				        headerKey="-1" headerValue="Select One"
				        list="RLReasonList"
				        listKey="label"
				        listValue="value"
				        multiple="false"
				        size="1"
				        value="globalRLAdjustmentReasonCode">
					</s:select>
					</td>
				</s:if>
				<td style="height: 50px; width: 100px;"> &nbsp;</td>	
				</tr>
                <tr style="background-color: #FFFFFF;border: 1px solid #808080; color:#808080; text-align: left" >			  
                  <th>Apply the Adjustment</th>
                </tr>
</table>

<div id="orderDetailcontent" >
	<table class="" id="orderDetailcontentTable" width="100%">
	    <col style="width: 30%;"/>
	    <col style="width: 70%;"/>
        <tr style="font-weight: bold; background-color: #E2E9F0;border: 1px solid #808080; color:#000000" >		
			<td style="text-align: center; border: 1px solid #808080" class="odd"><s:property	value="fullCreditLabel" /> </td>
			<td style="text-align: center; border: 1px solid #808080" class="odd">Global adjustment</td>
		</tr>
        <tr style="background-color: #F3F6F9; border: 1px solid #808080; color:#808080; " >		
			<s:submit align="center" onclick="performFullCredit('adjustmentMgmt!performFullCredit.action'); return false;" value="Perform Full Credit" />
			<td class="odd" style="font-weight: bold; border: 1px solid #808080; color:#000000; padding: 0px;" >
			
				<table  id="orderDetailcontentTable1" width="100%">
				<tr>
					<td style="border-right: 1px solid #808080;" >
					<table id="adjustmentHdr"   style="width: 100%; background-color: #F3F6F9; ">
						<col style="width: 150px;"/>
						<col style="width: 250px;"/>
						<tr style=";margin-bottom: 0px;" >	
							 <td style="text-align: right;"> Field to Adjust :</td>
							 <td style="text-align: left;">
							 <s:select  cssStyle="width: 150px;  font-weight: bold; background-color: rgb(243, 246, 249); text-align: left; " 
								label="" theme="simple" 	name="globalAdjustmentFieldName" headerKey="-1" headerValue="Select One"
								list="globalAdjustmentFieldList" listKey="label" listValue="value"	multiple="false"size="1" value="globalAdjustmentFieldName">
							</s:select>
							</td>
						</tr>
						<tr style=";margin-top: 0px;" >
							<td style="text-align: right;"> Current Value :</td>
							<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 60px; text-align: left;" align="left" name="globalAdjustmentFieldCurrentValue" value="%{globalAdjustmentFieldCurrentValue}" />
						</tr>
						<tr style=";margin-top: 0px;" >
							<td style="text-align: right;"> New Value :</td>
							<s:textfield cssClass="itype%{item.ID}" cssStyle="width: 60px; text-align: left;" align="left" name="globalAdjustmentFieldNewValue" value="%{globalAdjustmentFieldNewValue}" />
						</tr>
						<tr style=";margin-top: 0px;" >
							<td style="text-align: right;"> &nbsp; </td>
							<s:submit align="left" onclick="performGlobalFieldAdjustment('adjustmentMgmt!performGlobalFieldAdjustment.action'); return false;" value="Perform Global Adjustment" />
						</tr>
					</table>
					</td>
				
					<td>
					<table id="adjustmentHdr"   style="width: 100%; background-color: #F3F6F9;">
						<col style="width: 150px;"/>
						<col style="width: 250px;"/>
						<tr style=";margin-bottom: 0px;" >	
							 <td style="text-align: right;"> Charges to Adjust :</td>
							 <td style="text-align: left;">
							 <s:select  cssStyle="width: 150px;  font-weight: bold; background-color: rgb(243, 246, 249); text-align: left; " 
								label="" theme="simple" 	name="globalAdjustmentFeeName" headerKey="-1" headerValue="Select One"
								list="globalAdjustmentFeeList" listKey="label" listValue="value"	multiple="false"size="1" value="globalAdjustmentFeeName">
							</s:select>
							</td>
						</tr>
						<tr style=";margin-top: 0px;" >
							<td>&nbsp</td>
							<td>&nbsp</td>
						</tr>
						<tr style=";margin-top: 0px;" >
							<td>&nbsp</td>
							<td>&nbsp</td>
						</tr>
						<tr style=";margin-top: 0px;" >
							<td style="text-align: right;"> &nbsp; </td>
							<s:submit align="left" onclick="performGlobalFeeAdjustment('adjustmentMgmt!performGlobalFeeAdjustment.action'); return false;" value="Perform Global Adjustment" />
						</tr>
					</table>
					</td>
					
				</tr>
				</table>
			</td>
			
		</tr>
	</table>
</div>
	
</div>

<script type="text/javascript">

function resetFormFields() {
		var aForm = document.getElementById("adjustmentMgmt");
	    for (var i=0;i<aForm.length;i++) {
	    	var ele=aForm.elements[i];  
	    	if (!ele.disabled) {
	     		if ( ele.type == 'checkbox' ) { ele.checked=false; }
	     		if ( ele.type == 'text' ) { ele.value=''; }
	     		if ( ele.type == 'select-one' ) { 
	        		  ele.value=ele.options[0].value;
	     		}
	    	}
	    }
	}

function performFullCredit(action) {
	   	$('#adjustmentMgmt').attr('action', action);
	   	$('#adjustmentMgmt').submit();
}

function performGlobalFieldAdjustment(action) {
	   	$('#adjustmentMgmt').attr('action', action);
	   	$('#adjustmentMgmt').submit();
}

function performGlobalFeeAdjustment(action) {
   	$('#adjustmentMgmt').attr('action', action);
   	$('#adjustmentMgmt').submit();
}




	
</script>