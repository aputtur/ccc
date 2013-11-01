<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<s:if test="!processMessages.empty">
	<tr id='processMessagesView<s:property value="item.ID"/>'>
		<td style="background-color: #6F97BB;"></td>
		<td colspan="2" align="center">
		    <fieldset style="width: 800px;">
		    <legend><span style="font-weight: bold;">Processing Messages</span></legend>
			<ul style="text-align: left;">
			<s:iterator value="processMessages">
			  <s:if test="error">
			   <li style="color: red; font-weight: bold;">
			       <s:property value="briefMessage"/>
			   </li>
			  </s:if>
			  <s:else>
			   <li style="color: blue; font-weight: bold;">
			       <s:property value="briefFormattedMessage"/>
			   </li>
			   </s:else>
			</s:iterator>
		 	</ul>
		 	</fieldset>
		</td>
	</tr>
</s:if>


	<tr>
		<td style="background-color: #6F97BB;"></td>
		<td colspan="2">
			<table class="subTableNoColor" style="background-color: #EAE8E8;"
				width="100%">
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<tr><th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
					Tracking Information
				</th></tr>
				<s:if test="rightslink">
				<tr>
					<td class="odd-bold">Confirm #:</td>
					<td class="odd">
		        		<s:property	value="confNumber" /> 
					</td>
		        		<td class="odd-bold">Licensee Acct #:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.licenseeAccount"/></td>
					<td class="odd-bold">Created By:</td>
		        		<td class="odd">
		        		<s:property value="myConfirmation.confirmation.orderCreatedBy"/>
		        		</td>
					<td class="odd-bold">Order Source:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.orderSourceCd"/></td>
				</tr>
				<tr>
					<td class="odd-bold">RL License #:</td>
		        		<td class="odd"><s:property value="item.externalDetailId"/></td>
					<td class="odd-bold">Licensee Name:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.licenseeName"/></td>
					<td class="odd-bold">Internet Login:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.buyerUserIdentifier"/></td>
					<td class="odd-bold">Order Stored in</td>
		        		<td class="odd"><s:property value="item.orderDataSourceDisplay"/></td>
				</tr>
				<tr>
					<td class="odd-bold">RL Job Ticket #:</td>
		        		<td class="odd"><s:property value="item.jobTicketNumber"/></td>
					<td class="odd-bold">Payment Type:</td>
		        		<td class="odd"><s:property value="item.paymentTypeDisplay"/></td>
					<td class="odd-bold">Last Update Date:</td>
		        		<td class="odd"><s:property value="item.lastUpdatedDate"/></td>
		        	<td class="odd-bold">Permission Source:</td>
		        		<td class="odd"><s:property value="item.rightSourceCd"/></td>				
				</tr>
				<tr>
					<td class="odd-bold">Invoice #:</td>
		        		<td class="odd"><s:property value="item.invoiceId"/></td>
				    <td class="odd-bold">&nbsp;</td>
				    <td class="odd">&nbsp;</td>
				    <td class="odd-bold">&nbsp;</td>
				    <td class="odd">&nbsp;</td>
				    <td class="odd-bold">Permission Status:</td>
					<td class="odd"><s:property value="item.itemAvailabilityDescriptionInternal"/></td>  
		        </tr>
				<tr>
					<td class="odd-bold">Detail Status:</td>
		        	<td class="odd"><s:property value="item.itemStatusInternalDisplay"/></td>
	        		<s:if test="item.adjusted || item.distributionEvent != null || item.canceled">
							<s:if test="item.distributionEvent != null">
				       			<td class="odd-bold">Distribution ID:</td>
		        				<td class="odd"><s:property value="item.distributionEvent"/></td>
							</s:if>
		        			<s:else>
				       			<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
		        			</s:else>
							<s:if test="item.adjusted">
								<td class="odd-bold"></td>
								<td class="odd-bold" style="color:red; font-weight:bold; text-align: left;">
				      			Adjusted
								</td>
							</s:if>
		        			<s:else>
				        		<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
		        			</s:else>
							<s:if test="item.canceled">
								<td class="odd-bold" style="color:red; font-weight:bold; text-align: right;">
				    				Canceled
				    			</td>
				    			<td class="odd-bold">&nbsp;</td>
							</s:if>
							<s:else>
				       			<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
							</s:else>
					</s:if>
				</tr>

				</s:if>
				<s:else>
				<tr>
					<td class="odd-bold">Confirm #:</td>
					<td class="odd">
						<s:property	value="confNumber" /> 
					</td>
		        		<td class="odd-bold">Licensee Acct #:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.licenseeAccount"/></td>
					<td class="odd-bold">Created By:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.orderCreatedBy"/></td>
					<td class="odd-bold">Order Source:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.orderSourceCd"/></td>
				</tr>
				<tr>
					<td class="odd-bold">TF Order #:</td>
		        		<td class="odd"><s:property value="item.externalOrderId"/></td>
					<td class="odd-bold">Licensee Name:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.licenseeName"/></td>
					<td class="odd-bold">Internet Login:</td>
		        		<td class="odd"><s:property value="myConfirmation.confirmation.buyerUserIdentifier"/></td>
					<td class="odd-bold">Order Stored in:</td>
		        		<td class="odd"><s:property value="item.orderDataSourceDisplay"/></td>
				</tr>
				<tr>
					<td class="odd-bold">Invoice #:</td>
		        		<td class="odd"><s:property value="item.invoiceId"/></td>
					<td class="odd-bold">Payment Type:</td>
		        		<td class="odd"><s:property value="item.paymentTypeDisplay"/></td>
					<td class="odd-bold">Last Update Date:</td>
		        		<td class="odd"><s:property value="item.lastUpdatedDate"/></td>
		        	<td class="odd-bold">Permission Source:</td>
		        		<td class="odd"><s:property value="item.rightSourceCd"/></td>				
				</tr>
				<tr>
					<td class="odd-bold">Detail Status:</td>
		        		<td class="odd"><s:property value="item.itemStatusInternalDisplay"/></td>
					<td class="odd-bold">Detail Cycle:</td>
		        		<td class="odd"><s:property value="item.itemCycleDisplay"/></td>
					<td class="odd-bold">Error Desc:</td>
		        		<td class="odd"><s:property value="item.itemErrorDescriptionDisplay"/></td>
				    <td class="odd-bold">Permission Status:</td>
						<td class="odd"><s:property value="item.itemAvailabilityDescriptionInternal"/></td>  
				</tr>
				<s:if test="item.adjusted || item.distributionEvent != null || item.canceled">
					<tr>  <!--  extra row in these circumstances -->
						<s:if test="item.declineReason != null">
				        	<td class="odd-bold">Decline Reason:</td>
		        			<td class="odd"><s:property value="item.declineReason"/></td>
						</s:if>
						<s:else>
				    	    <td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
						</s:else>
						<s:if test="item.distributionEvent != null">
					       	<td class="odd-bold">Distribution ID:</td>
			        		<td class="odd"><s:property value="item.distributionEvent"/></td>
						</s:if>
		        		<s:else>
				    	   	<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
		        		</s:else>
						<s:if test="item.adjusted">
							<s:if test="item.adjustmentItems != null">
								<td class="odd-bold" style="color:red; font-weight:bold; text-align: right;">
				       				Adjustment Details:
								</td>
								<td class="odd" style="color:red; text-align: left;">
				       				<s:property value="item.adjustmentItems"/>
								</td>
							</s:if>
							<s:else>
								<td class="odd-bold"></td>	
								<td class="odd-bold" style="color:red; font-weight:bold; text-align: left;">
				       				Adjusted
								</td>
							</s:else>
						</s:if>
		        		<s:else>
				        	<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
		        		</s:else>
						<s:if test="item.canceled">
							<td class="odd-bold" style="color:red; font-weight:bold; text-align: right;">
				    			Canceled
				       		</td>
				    		<td class="odd-bold">&nbsp;</td>
						</s:if>
						<s:else>
				       		<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
						</s:else>	
					</tr>
				</s:if>
				</s:else>
			</table>
			
			<s:if test="rightslink">
				<table style="background-color: #EAE8E8;" class="subTableNoColor"
				width="100%">
				<tr><th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
					<s:if test="rL">
					RightsLink Purchase Information
					</s:if>
					<s:if test="rLR">
					RL Reprint Purchase Information
					</s:if>
				</th></tr>
				<tr>
				<td rowspan="2" style="width: 40px;">&nbsp;</td> 
				<td colspan="2">&nbsp;</td></tr>
				<tr><td><table>			
					<s:property value="item.rlDetailHtml" escape="false"/>
					<!--<s:property value="item.rLDetails" escape="false"/>-->
				</table></td></tr>
				<tr><td>&nbsp;</td></tr>
				</table>
			</s:if>
			
			<table class="subTableNoColor" style="background-color: #EAE8E8;"
				width="100%">
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<tr><th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
					Bibliographic &amp; Rights Information
				</th></tr>
				<tr>
					<td class="odd-bold">Title:</td>
		        		<td class="odd" colspan="5"><s:property value="item.publicationTitle"/></td>
		        	<td class="odd" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td class="odd-bold">Article:</td>
		        		<td class="odd" colspan="5"><s:property value="item.itemSubDescription"/></td>
		        	<td class="odd" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td class="odd-bold">IDNO Type:</td>
		        		<td class="odd"><s:property value="item.idnoLabel"/></td>
					<td class="odd-bold">IDNO:</td>
		        		<td class="odd"><s:property value="item.standardNumber"/></td>
		        	<td class="odd" colspan="2">&nbsp;</td>
					<s:if test="rightslink">
						<td class="odd-bold"></td>
						<td class="odd"></td>
					</s:if>
					<s:else>
						<td class="odd-bold">RH Ref #:</td>
			        		<td class="odd"><s:property value="item.rhRefNum"/></td>
		        	</s:else>
				</tr>
				<tr>
					<td class="odd-bold">Publisher:</td>
		        		<td class="odd" colspan="3"><s:property value="item.publisher"/></td>
					<s:if test="rightslink">
						<td class="odd-bold"></td>
						<td class="odd"></td>
						<td class="odd-bold"></td>
						<td class="odd"></td>
					</s:if>
					<s:else>
						<td class="odd-bold">WR Work ID:</td>
		        		<td class="odd"><s:property value="item.wrWorkInst"/></td>
						<td class="odd-bold">RH Account #:</td>
			        	<td class="odd"><s:property value="item.rightsholderAccount"/></td>
			        </s:else>
				</tr>
				<tr>
					<td class="odd-bold">Rightsholder:</td>
		        		<td class="odd" colspan="3"><s:property value="item.rightsholder"/></td>
					<s:if test="rightslink">
						<td class="odd-bold"></td>
						<td class="odd"></td>
						<td class="odd-bold"></td>
						<td class="odd"></td>
					</s:if>
					<s:else>
						<td class="odd-bold">TF Work ID:</td>
			        		<td class="odd"><s:property value="item.workInst"/></td>
						<td class="odd-bold">Right Inst:</td>
			        		<td class="odd"><s:property value="item.rgtInst"/></td>
		        	</s:else>
				</tr>
				<tr>
					<td class="odd-bold">Product:</td>
		        		<td class="odd" colspan="3"><s:property value="item.productAndCategoryName"/></td>
					<td class="odd-bold">Type of Use:</td>
		        		<td class="odd" colspan="3"><s:property value="item.typeOfUseDescription"/></td>
				</tr>
			</table>
				
			<s:if test="item.republication">
				<table class="subTableNoColor" style="background-color: #EAE8E8;"
					width="100%">
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
				<col style="width: 100px;"/>
					<tr><th colspan="8" style="background-color: #FFFFFF; color: #4A739A;">
						Republication Information
					</th></tr>
					<tr>
						<td class="odd-bold">Repub Work:</td>
		        			<td class="odd" colspan="5"><s:property value="item.newPublicationTitle"/></td>
						
						<td class="odd-bold">Circ/Dist:</td>
		        		<s:if test="preAdjustmentItem != null">
		        			<td class="odd">
		        				<s:property value="preAdjustmentItem.circulationDistribution"/>
	        					<span style="background-color:pink; text-align: left;">/&nbsp;<s:property value="item.circulationDistribution"/></span>
	        				</td>
						</s:if>
						<s:else>
		        			<td class="odd"><s:property value="item.circulationDistribution"/></td>
			        	</s:else>
		        			
					</tr>
					<tr>
						<td class="odd-bold">Repub Date:</td>
		        			<td class="odd" colspan="3"><s:property value="item.republicationDate"/></td>
						<td class="odd-bold">Repub Pub:</td>
		        			<td class="odd"><s:property value="item.republishingOrganization"/></td>
						<td class="odd-bold">For Profit:</td>
		        			<td class="odd"><s:property value="item.business"/></td>
					</tr>
				</table>
			</s:if>
			<s:if test="item.aPS || item.eCCS">
				<jsp:include
					page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailLongAcademic.jsp" />
			</s:if>
			<s:if test="item.photocopy">
				<jsp:include
					page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailLongPhotocopy.jsp" />		
			</s:if>
			<s:if test="item.digital">
				<jsp:include
					page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailLongDigital.jsp" />		
			</s:if>
			<s:if test="item.republication">
				<jsp:include
					page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailLongRepublish.jsp" />
			</s:if>


             <!-- <table style="width: 100%;"> 
             <tr>
             <td>--> 

		<jsp:include page="/WEB-INF/jsp-modules-s2/ordermgmt/orderhistory/include/viewOneDetailLongPermission.jsp" />		
		</td>
	</tr>
