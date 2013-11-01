<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

				<table class="subTableNoColor" style="background-color: #EAE8E8;" width="100%">				
					<col style="width: 550px;"/>
					<col style="width: 200px;"/>
					<col style="width: 150px;"/>
					<tr><th colspan="6" style="background-color: #FFFFFF; color: #4A739A;">
						Permission Information
					</th></tr>
					<tr>
					  	<td style="vertical-align: top;">
						<s:if test="item.aPS || item.eCCS || item.photocopy">
							<table style="vertical-align: top;">
								<col style="width: 105px;"/>
								<col style="width: 80px;"/>
								<col style="width: 120px;"/>
								<col style="width: 225px;"/>
								<tr>
									<td class="odd-bold">Pinned:</td>
		       						<td class="odd"><s:property value="item.pinningDate"/></td>
		       						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Override Perm:</td>
		       						<td class="odd"><s:property value="item.overrideAvailabilityDescription"/></td>									
									<td class="odd-bold">Override Comment:</td>
		        					<td class="odd" colspan="2" style="width: 220px;"><s:property value="item.overrideComment"/></td>
								</tr>
								<tr>
									<td class="odd-bold">Override Date:</td>
		       						<td class="odd"><s:property value="item.overrideDate"/></td>
		       						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>							
								<tr>
									<td class="odd-bold">Permission Type:</td>
		       						<td class="odd"><s:property value="item.itemOrigAvailabilityDescriptionInternal"/></td>
		        					<td class="odd-bold">Rights Qualifier:</td>
		        					<td class="odd" colspan="2" style="width: 220px; vertical-align: top;"><s:property value="item.rightQualifierTerms"/></td>		        						
								</tr>
								<tr>
									<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;" align="right">
										<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">						    
											<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<td class="odd-bold">Per Page Fee:</td>
											</tr>
											
											<s:if test="item.aPS || item.eCCS">

												<tr>
													<td class="odd-bold">Entire Book Fee:</td>
												</tr>
											</s:if>
												
											<tr>
												<td class="odd-bold">Base Fee:</td>
											</tr>	
		        							<tr>
												<td class="odd-bold">Flat Fee:</td>
		        							</tr>	       								
										</table>
									</td>
									<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
										<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">						    
											<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<td class="odd"><s:property value="perPageFeeFormatted"/>&nbsp;</td>
											</tr>
											<tr>
												<td class="odd"><s:property value="entireBookFeeFormatted"/>&nbsp;</td>
											</tr>	
											<tr>
												<td class="odd"><s:property value="baseFeeFormatted"/>&nbsp;</td>
											</tr>	
		        							<tr>
		        								<td class="odd"><s:property value="flatFeeFormatted"/>&nbsp;</td>
		        							</tr>	       								
										</table>
									</td>
									<s:if test="!item.externalCommentTerm.empty">
										<td class="odd-bold" style="vertical-align: top;">External Comment:</td>
										<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
											<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
		        									<s:textarea style="border: 0px #000000 solid; padding: 0px; vertical-align: top; background: none; font-family: Tahoma,sans-serif, Arial Unicode MS, Arial; font-size: 11px; color: #0000000;" name="item.externalCommentTerm"     		
		        									name="item.externalCommentTerm" readonly="true" cols="38" rows="4"/>		
		        								</tr>
		        							</table>
		        						</td>	
									</s:if>
									<s:else>
										<td class="odd-bold" style="vertical-align: top;">External Comment:</td>
										<td class="odd-bold">&nbsp;</td>
									</s:else>
								</tr>
								<tr>
									<td class="odd-bold">Max Royalty:</td>
		       						<td class="odd"><s:property value="maxRoyaltyFormatted"/></td>
		       						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Rightsholder %:</td>
		       						<td class="odd"><s:property value="item.rightsholderPercent"/>&nbsp;</td>						
	        						<td class="odd-bold">Volume Priced:</td>
		        					<td class="odd"><s:property value="item.hasVolPriceTiers"/>&nbsp;</td>
								</tr>
							</table>
						</s:if>
						<s:if test="item.digital">
							<table style="vertical-align: top;">
								<col style="width: 105px;"/>
								<col style="width: 80px;"/>
								<col style="width: 120px;"/>
								<col style="width: 225px;"/>
								<tr>
									<td class="odd-bold">Pinned:</td>
		        						<td class="odd"><s:property value="item.pinningDate"/></td>
		        						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Override Perm:</td>
		        						<td class="odd"><s:property value="item.overrideAvailabilityDescription"/></td>									
									<td class="odd-bold">Override Comment:</td>
		        						<td class="odd" colspan="1" style="width: 220px;"><s:property value="item.overrideComment"/></td>
								</tr>
								<tr>
									<td class="odd-bold">Override Date:</td>
		        						<td class="odd"><s:property value="item.overrideDate"/></td>
		        						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Permission Type:</td>
		        						<td class="odd"><s:property value="item.itemOrigAvailabilityDescriptionInternal"/></td>
									<td class="odd-bold">Rights Qualifier:</td>
		        						<td class="odd" colspan="1" style="width: 220px;"><s:property value="item.rightQualifierTerms"/></td>
								</tr>
								<tr>
									<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;" align="right">
										<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">						    
											<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<s:if test="item.email">
													<td class="odd-bold"><s:property value="numRecipientsLabel"/>:</td>
			        							</s:if>
												<s:else>
													<td class="odd-bold"><s:property value="item.durationString"/>:</td>
												</s:else>
											</tr>
											<tr>
												<td class="odd-bold">Base Fee:</td>
											</tr>	
											<tr>
												<td class="odd-bold">Flat Fee:</td>
											</tr>	
										</table>
									</td>
									<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
										<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">						    
											<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<s:if test="item.email">
			        								<td class="odd"><s:property value="numRecipientsFeeFormatted"/>&nbsp;</td>
												</s:if>
												<s:else>
			        								<td class="odd"><s:property value="durationFeeFormatted"/>&nbsp;</td>
												</s:else>
											</tr>
											<tr>
												<td class="odd"><s:property value="baseFeeFormatted"/>&nbsp;</td>
											</tr>	
											<tr>
												<td class="odd"><s:property value="flatFeeFormatted"/>&nbsp;</td>
											</tr>	
										</table>
									</td>
									<s:if test="!item.externalCommentTerm.empty">
										<td class="odd-bold" style="vertical-align: top;">External Comment:</td>
										<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
											<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
		        									<s:textarea style="border: 0px #000000 solid; padding: 0px; vertical-align: top; background: none; font-family: Tahoma,sans-serif, Arial Unicode MS, Arial; font-size: 11px; color: #0000000;" name="item.externalCommentTerm"     		
		        									name="item.externalCommentTerm" readonly="true" cols="38" rows="4"/>		
		        								</tr>
		        							</table>
		        						</td>	
									</s:if>
									<s:else>
										<td class="odd-bold" style="vertical-align: top;">External Comment:</td>
										<td class="odd-bold">&nbsp;</td>
									</s:else>
								</tr>
								<tr>
									<td class="odd-bold">Max Royalty:</td>
		       						<td class="odd"><s:property value="maxRoyaltyFormatted"/></td>
		       						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Rightsholder %:</td>
		        						<td class="odd"><s:property value="item.rightsholderPercent"/>&nbsp;</td>
		        					<td class="odd-bold">Volume Priced:</td>
		        						<td class="odd"><s:property value="item.hasVolPriceTiers"/>&nbsp;</td>
								</tr>
							</table>
						</s:if>
						<s:if test="item.republication">
							<table style="vertical-align: top;">
								<col style="width: 105px;"/>
								<col style="width: 80px;"/>
								<col style="width: 120px;"/>
								<col style="width: 225px;"/>
								<tr>
									<td class="odd-bold">Pinned:</td>
		        						<td class="odd"><s:property value="item.pinningDate"/></td>
		        					<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Override Perm:</td>
		        						<td class="odd"><s:property value="item.overrideAvailabilityDescription"/></td>									
									<td class="odd-bold">Override Comment:</td>
		        						<td class="odd" colspan="1" style="width: 220px;"><s:property value="item.overrideComment"/></td>
								</tr>
								<tr>
									<td class="odd-bold">Override Date:</td>
		        						<td class="odd"><s:property value="item.overrideDate"/></td>
		        						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Permission Type:</td>
		        						<td class="odd"><s:property value="item.itemOrigAvailabilityDescriptionInternal"/></td>
									<td class="odd-bold">Rights Qualifier:</td>
		        						<td class="odd" colspan="1" style="width: 220px;"><s:property value="item.rightQualifierTerms"/></td>
								</tr>	
								<tr>
									<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;" align="right">
										<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">						    
											<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<td class="odd-bold"><s:property value="perContentLabel"/>:</td>
											</tr>
											<tr>
												<td class="odd-bold">Base Fee:</td>
											</tr>	
											<tr>
												<td class="odd-bold">Flat Fee:</td>
											</tr>	
										</table>
									</td>
									<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
										<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">						    
											<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<td class="odd"><s:property value="perContentFeeFormatted"/>&nbsp;</td>
											</tr>
											<tr>
												<td class="odd"><s:property value="baseFeeFormatted"/>&nbsp;</td>
											</tr>	
											<tr>
												<td class="odd"><s:property value="flatFeeFormatted"/>&nbsp;</td>
											</tr>	
										</table>
									</td>
									<s:if test="!item.externalCommentTerm.empty">
										<td class="odd-bold" style="vertical-align: top;">External Comment:</td>
										<td style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
											<table style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
												<tr style="border: 0px #000000 solid; padding: 0px; vertical-align: top;">
		        									<s:textarea style="border: 0px #000000 solid; padding: 0px; vertical-align: top; background: none; font-family: Tahoma,sans-serif, Arial Unicode MS, Arial; font-size: 11px; color: #0000000;" name="item.externalCommentTerm"     		
		        									name="item.externalCommentTerm" readonly="true" cols="38" rows="4"/>		
		        								</tr>
		        							</table>
		        						</td>	
									</s:if>
									<s:else>
										<td class="odd-bold" style="vertical-align: top;">External Comment:</td>
										<td class="odd-bold">&nbsp;</td>
									</s:else>
								</tr>
								<tr>
									<td class="odd-bold">Max Royalty:</td>
		       						<td class="odd"><s:property value="maxRoyaltyFormatted"/></td>
		       						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Rightsholder %:</td>
		        						<td class="odd"><s:property value="item.rightsholderPercent"/>&nbsp;</td>
		        					<td class="odd-bold">Volume Priced:</td>
		        						<td class="odd"><s:property value="item.hasVolPriceTiers"/>&nbsp;</td>
								</tr>
							</table>
						</s:if>
						<s:if test="rightslink">
							<table style="vertical-align: top;">
								<col style="width: 100px;"/>
								<col style="width: 100px;"/>
								<col style="width: 100px;"/>
								<col style="width: 200px;"/>
								<tr>
									<td class="odd-bold">Pinned:</td>
		        						<td class="odd"><s:property value="item.pinningDate"/></td>
										<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Override Perm:</td>
		        						<td class="odd"><s:property value="item.overrideAvailabilityDescription"/></td>									
										<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>
								<tr>
									<td class="odd-bold">Override Date:</td>
		        						<td class="odd"><s:property value="item.overrideDate"/></td>
		        						<td class="odd-bold">&nbsp;</td><td class="odd">&nbsp;</td>
								</tr>							
								<tr>
									<td class="odd-bold">Permission Type:</td>
		        						<td class="odd"><s:property value="item.itemOrigAvailabilityDescriptionInternal"/></td>
		        						<td class="odd-bold" style="width: 100px;">&nbsp;</td>
		        						<td class="odd" style="width: 100px;">&nbsp;</td>
								</tr>
							</table>
						</s:if>
						</td>
		        		<td style="vertical-align: top;">
							<table style="vertical-align: top; border: 1px solid #C6C6C6; background-color: #FFFFFF; text-align: right;">
								<tr>
									<th colspan="2" style="background-color: F3F6F9; color: black; text-align: center;" align="center">
									    Fees in USD
									</th>
								</tr>
								<s:if test="preAdjustmentItem != null">
									<tr>
										<td class="odd-bold">Licensee Fee:</td>
		        							<td class="odd">
		        								<s:property value="preAdjustmentTotalLicenseeFeeValueFormatted"/>
		        								/
		        							</td>
		        							<td class="odd" style="background-color:pink; text-align: left;">
		        								<s:property  value="totalLicenseeFeeValueFormatted"/>
		        							</td>
									</tr>
									<tr>
										<td class="odd-bold">Discount:</td>
		        							<td class="odd">
		        								<s:property value="preAdjustmentTotalDiscountValueFormatted"/>
		        								/
		        							</td>
		        							<td class="odd" style="background-color:pink; text-align: left;">
		        								<s:property value="totalDiscountValueFormatted"/>
		        							</td>
									</tr>
									<tr>
										<td class="odd-bold">Royalty:</td>
		        							<td class="odd">
		        								<s:property value="preAdjustmentRoyaltyCompositeValueFormatted"/>
		        								/
		        							</td>
		        							<td class="odd" style="background-color:pink; text-align: left;">
		        								<s:property value="royaltyCompositeValueFormatted"/>
		        							</td>
									</tr>
									<tr style="height: 5px;" ><td colspan="2"><hr /></td></tr>
									<tr>
										<td class="odd-bold">Total:</td>
		        							<td class="odd">
		        								<s:property value="preAdjustmentPriceFormatted"/>
		        								/
		        							</td>
		        							<td class="odd" style="background-color:pink; text-align: left;">
		        								<s:property value="item.price"/>
		        							</td>
									</tr>		
									<tr>
										<td class="odd" style="color:red; text-align: right;">Difference:</td>
		        							<td class="odd" style="color:red; text-align: left;">
		        								<s:property value="preAdjustmentPriceDifferenceFormatted"/>
		        							</td>
									</tr>																	
								</s:if>
		        				<s:else>
									<tr>
										<td class="odd-bold">Licensee Fee:</td>
		        							<td class="odd"><s:property value="totalLicenseeFeeValueFormatted"/></td>
									</tr>
									<tr>
										<td class="odd-bold">Discount:</td>
		        							<td class="odd"><s:property value="totalDiscountValueFormatted"/></td>
									</tr>
									<tr>
										<td class="odd-bold">Royalty:</td>
		        							<td class="odd"><s:property value="royaltyCompositeValueFormatted"/></td>
									</tr>
									<tr style="height: 5px;" ><td colspan="2"><hr /></td></tr>
									<tr>
										<td class="odd-bold">Total:</td>
		        							<td class="odd"><s:property value="item.price"/></td>
									</tr>
								</s:else>
							</table>
						</td>
						<s:if test="usesForeignCurrency">
						<td style="vertical-align: top;">
							<table style="border: 1px solid #C6C6C6; background-color: #F3F6F9; text-align: left; width: 135px;">
								<tr>
									<th colspan="2" style="background-color: F3F6F9; color: black; text-align: center;" align="center">
									    &nbsp;
									</th>
								</tr>
								<tr>
									<td style="padding-left: 10px;">
										Credit Card <br/> charged in <br/> foreign currency: <br/><br/>
										currency:&nbsp; <s:property value="item.currencyType"/><br/>
										exch rate:&nbsp; <s:property value="item.exchangeRate"/>
										<br/>&nbsp;
									</td>
								</tr>
							</table>
						</td>
						</s:if>
					</tr>
				</table>