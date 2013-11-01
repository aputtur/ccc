       <h4 style="width:535px"><span>      
     <logic:present name="item"  property="wrWorkInst">   
		   <logic:notEmpty  name="item" property="wrWorkInst">
		    <logic:notEqual name="item" property="wrWorkInst" value="0">
		        <html:link action="/search.do?operation=detail" paramId="item" paramName="item" paramProperty="wrWorkInst">
		      <bean:write name="item" property="publicationTitle"/>
		       </html:link>
		       <br/>
		    </logic:notEqual>
		    <logic:equal name="item" property="wrWorkInst" value="0">
		    <label style="color:#000000;"> <bean:write name="item" property="publicationTitle" /></label>
		    </logic:equal>
		    </logic:notEmpty>
		      <logic:empty  name="item" property="wrWorkInst">
		      <label style="color:#000000;"><bean:write name="item" property="publicationTitle" /></label>
		      </logic:empty>
		 </logic:present>
		 <logic:notPresent name="item"  property="wrWorkInst">  
		 <label style="color:#000000;"> <bean:write name="item" property="publicationTitle" /></label> 
		 </logic:notPresent>
    
    
    </span></h4>
    <span  style="width:175px;float:right;right:0px;text-align:right;top:8px;padding-right:1px;position:absolute">
<span class="billing_status" >
    	 <util:billingStatus bean="item"></util:billingStatus>
			</span>	
	</span>			
					<div class="parameter-list">
							<ul class="list">
							<li class="orderId"><strong>Order detail ID:</strong>
							<span>
							 <bean:write name="item" property="ID" />
							</span>
							</li>
							        <logic:notEmpty name="item" property="standardNumber">
							        <li>
                                        <logic:notEmpty name="item" property="idnoLabel">
                                            <strong><bean:write name="item" property="idnoLabel"/>: </strong>
                                            </logic:notEmpty>
                                            <logic:empty name="item" property="idnoLabel">
                                            	<strong>ISBN/ISSN: </strong>
                                            </logic:empty>
                                            <span>
                                        <bean:write name="item" property="standardNumber" /></span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="publicationYear">
                                        <li><strong>Publication year: </strong><span><bean:write name="item" property="publicationYear" /></span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="publicationType">
                                         <li><strong>Publication Type: </strong><span><bean:write name="item" property="publicationType" /></span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="publisher">
                                        <li><strong>Publisher: </strong><span><bean:write name="item" property="publisher" /></span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="rightsholder">
                                    <logic:notEqual name="item" property="rightsholder" value="null">
                                        <li><strong>Rightsholder: </strong><span><bean:write name="item" property="rightsholder" /></span></li>
                                        </logic:notEqual>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="customAuthor">
                                        <li><strong>Author/Editor: </strong><span><bean:write name="item" property="customAuthor" /></span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="customVolume">
                                        <li><strong>Volume: </strong><span><bean:write name="item" property="customVolume" /></span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="customEdition">
                                        <li><strong>Edition: </strong><span><bean:write name="item" property="customEdition" /></span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="customerReference">
                                            <li><strong>Your line item reference: </strong><span><util:fieldWrap bean="item" property="customerReference" width="20" filter="true" useWordBreak="true"/></span></li>
                                        </logic:notEmpty>
								</ul>
								<ul>
								<li class="permStatus">
                   							<b><util:permStatus bean="item"/></b>
                   				</li>
								<li><strong>Permission type:</strong><span>
                                                        <util:fieldWrap bean="item" property="categoryName" width="20" filter="true" useWordBreak="true"/></span></li>
                                            <logic:notEmpty name="item" property="touName">                                            
                                            	<li><strong>Type of use:</strong><span>
                                                        	<util:fieldWrap bean="item" property="touName" width="20" filter="true" useWordBreak="true"/></span></li>
                                            </logic:notEmpty>
						     			 <logic:equal name="item" property="specialOrder" value="false">
						     			 	<logic:notEqual name="item" property="licenseeRequestedEntireWork" value="true">
							                	<logic:notEmpty name="item" property="perPageFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="perPageFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Per Page Fee:</strong><span><bean:write name="item" property="perPageFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							                	<logic:notEmpty name="item" property="baseFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="baseFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Per Copy Fee:</strong> <span><bean:write name="item" property="baseFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							                	<logic:notEmpty name="item" property="flatFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="flatFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Flat Fee:</strong> <span><bean:write name="item" property="flatFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							                </logic:notEqual>
							                
							                <logic:equal name="item" property="licenseeRequestedEntireWork" value="true">
							            	<logic:notEmpty name="item" property="entireBookFeeMoneyFormat">
							            	<logic:equal name="item" property="entireBookFeeMoneyFormat" value="$ 0.00">
							                	<logic:notEmpty name="item" property="perPageFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="perPageFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Per Page Fee:</strong><span><bean:write name="item" property="perPageFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							                	<logic:notEmpty name="item" property="baseFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="baseFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Per Copy Fee:</strong> <span><bean:write name="item" property="baseFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							                	<logic:notEmpty name="item" property="flatFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="flatFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Flat Fee:</strong> <span><bean:write name="item" property="flatFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							                </logic:equal>
							                </logic:notEmpty>
							            </logic:equal>
							            
							            <logic:equal name="item" property="licenseeRequestedEntireWork" value="true">
							            	<logic:empty name="item" property="entireBookFeeMoneyFormat">
							            	   	<logic:notEmpty name="item" property="perPageFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="perPageFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Per Page Fee:</strong><span><bean:write name="item" property="perPageFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							                	<logic:notEmpty name="item" property="baseFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="baseFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Per Copy Fee:</strong> <span><bean:write name="item" property="baseFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							                	<logic:notEmpty name="item" property="flatFeeMoneyFormat">
							                    	<logic:notEqual name="item" property="flatFeeMoneyFormat" value="$ 0.00">
							                        	<li><strong>Flat Fee:</strong> <span><bean:write name="item" property="flatFeeMoneyFormat" /></span></li>
							                    	</logic:notEqual>
							                	</logic:notEmpty>
							               </logic:empty>
							            </logic:equal>
							                <logic:equal name="item" property="licenseeRequestedEntireWork" value="true">
							            		<logic:notEmpty name="item" property="entireBookFeeMoneyFormat">
							            			<logic:notEqual name="item" property="entireBookFeeMoneyFormat" value="$ 0.00">
                		               					<li><strong>Entire Book Fee:</strong><span><bean:write name="item" property="entireBookFeeMoneyFormat" /></span></li>
                									</logic:notEqual>
                								</logic:notEmpty>
                							</logic:equal>
						                </logic:equal>
                                     <!-- Hide show block Academic Reg. -->  
                                        <logic:notEmpty name="item" property="chapterArticle">
                                           <li><strong>Article/Chapter: </strong><span><bean:write name="item" property="chapterArticle" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="dateOfIssue">
                                            <li><strong>Date of issue: </strong><span><bean:write name="item" property="dateOfIssue" format="MM/dd/yyyy" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="pageRange">
                                            <li><strong>Page range(s): </strong><span><bean:write name="item" property="pageRange" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="numberOfPages">
                                            <li><strong>Total number of pages: </strong><span><bean:write name="item" property="numberOfPages" /></span></li>
                                        </logic:notEmpty>
                                        <logic:equal name="item" property="publicationType" value="Book">
                                            	<logic:equal name="item" property="licenseeRequestedEntireWork" value="true">
                                            		<li><strong>Reuse entire book: </strong><span>Yes</span></li> 
                                            	</logic:equal>                                            
                                            </logic:equal>
                                        <logic:notEmpty name="item" property="numberOfStudents">
                                            <li><strong>Number of students: </strong><span><bean:write name="item" property="numberOfStudents" /></span></li>
                                        </logic:notEmpty>
                                        
                                        <logic:notEmpty name="item" property="paymentMethod">
                                            <li><strong>Payment Method: </strong><span><bean:write name="item" property="paymentMethod" /></span></li>
                                        </logic:notEmpty>
                                             
  							</ul>
                 </div>   
                        <logic:notEmpty name="item" property="rightsQualifyingStatement">
                                <p  class="icon-alert" style="padding:0px 20px 0px"><strong><bean:write name="item" property="rightsQualifyingStatement" /></strong>
                                <logic:notEmpty name="item" property="externalCommentTerm">
                                <div><b>Rightsholder terms apply </b>(see terms and conditions)</div>
                            	</logic:notEmpty>
                                </p>
                                </logic:notEmpty>
			              <logic:empty name="item" property="rightsQualifyingStatement">                                
			              		<logic:notEmpty name="item" property="externalCommentTerm">
                            	    <div><b>Rightsholder terms apply </b>(see terms and conditions)</div>
                            	</logic:notEmpty>
                            </logic:empty>
                             <!-- item footer -->
    <logic:equal name="processType" value="ORDER_VIEW"> 
			<ul class="add-services">
		    <logic:equal name="item" property="cancelable" value="true">
		    	<logic:equal name="item" property="awaitingLicConfirm" value="false">
				<li>
		    		<a href="javascript:cancel(<bean:write name='item' property='ID'/>, <bean:write name='orderViewActionForm' property='id'/>)">Cancel</a>
				</li>
		    	</logic:equal>
		    </logic:equal>
		    
		        <logic:equal name="item" property="availableToCopy" value="true">
		        <logic:equal name="orderViewActionForm" property="cartEmpty" value="false">
			    	 <li> <a href="javascript:copyAcademicLicense(<bean:write name="orderViewActionForm" property="id"/>,<bean:write name='item' property='ID'/>);">Copy</a></li>
		    	</logic:equal>
		        <logic:equal name="orderViewActionForm" property="cartEmpty" value="true">
		    		<li><a href="javascript:copyLic(<bean:write name="orderViewActionForm" property="id"/>,<bean:write name='item' property='ID'/>);">Copy</a></li>
		    	</logic:equal>
		        </logic:equal>
			    <!-- Add Not billed test here -->
		    <logic:equal name="item" property="billingStatusCd" value="NOT_BILLED">
		      <!-- TODO: Should only be shown if payment was invoice -->
		      <logic:notEqual name="item" property="billingStatusCd" value="CANCELED">
		      <logic:equal name="item" property="unresolvedSpecialOrder" value="false">
		     <li><a href="editBasicLicense.do?purid=<bean:write name='item' property='purchaseId'/>&id=<bean:write name='item' property='ID'/>&rp=<bean:write name='orderViewActionForm' property='returnPage'/>&cp=<bean:write name='orderViewActionForm' property='currentPage'/>&sf=<bean:write name='orderViewActionForm' property='sf'/>">Edit</a></li>
		      </logic:equal>
		      <logic:equal name="item" property="unresolvedSpecialOrder" value="true">
		    <li><a href="editSpecialLicense.do?purid=<bean:write name='item' property='purchaseId'/>&id=<bean:write name='item' property='ID'/>&rp=<bean:write name='orderViewActionForm' property='returnPage'/>&cp=<bean:write name='orderViewActionForm' property='currentPage'/>&sf=<bean:write name='orderViewActionForm' property='sf'/>">Edit</a></li>
		      </logic:equal>
		      </logic:notEqual>
		    </logic:equal>
		    <logic:equal name="item" property="billingStatus" value="Canceled">
		      <logic:empty name="item" property="invoiceId">
		      <logic:notEqual name="item" property="specialOrder" value="true">
		      <li><a href="javascript:uncancel(<bean:write name='item' property='ID'/>, <bean:write name='orderViewActionForm' property='id'/>)">Uncancel</a></li>
		      </logic:notEqual>
		      </logic:empty>
		    </logic:equal>
		</ul>
	</logic:equal>	      	
						<logic:notEqual name="item" property="billingStatusCode" value="<%=ItemConstants.BILLING_STATUS_AWAITING_LCN_CONFIRM%>">
                                    <span class="price" style="color:#000000">
											<bean:write name="item" property="price" />
											<logic:notEqual name="item" property="price" value="$TBD" >
										<br/><span class="largetype bold"><util:pricePerStudent bean="item"/></span>
										</logic:notEqual>
                                    </span>
                                    </logic:notEqual>
                                    
                                    <logic:equal name="item" property="billingStatusCode" value="<%=ItemConstants.BILLING_STATUS_AWAITING_LCN_CONFIRM%>">
                                         <span class="price" style="color:#000000;bottom:25px">
											<bean:write name="item" property="price" />
											<logic:notEqual name="item" property="price" value="$TBD" >
										<br/><span class="largetype bold"><util:pricePerStudent bean="item"/></span>
										</logic:notEqual>
                                    </span>
                                       <span class="grantBtn" >
										<div style="clear:right;">
										
										<span style="float:right;margin: 3px 0;">
											<logic:equal name="orderViewActionForm" property="isAlwaysInvoice" value="true">
												<a id="btnAccept" style="cursor: pointer;" alt="Accept" onclick="alwaysInvoiceSpecialOrder(<bean:write name='item' property='ID'/>,'<bean:write name='item' property='totalPriceValue'/>');"><img src="/resources/commerce/images/btn_accept.png"/></a> &nbsp;|&nbsp;<a  style="cursor: pointer;" onclick="showDeclineSpecialOrder(<bean:write name='item' property='ID'/>,'<bean:write name='item' property='totalPriceValue'/>');">Decline</a>&nbsp;&nbsp;<small><util:contextualHelp helpId="47" rollover="true">more&nbsp;info</util:contextualHelp></small>
											</logic:equal>
										
											<logic:equal name="orderViewActionForm" property="isAlwaysInvoice" value="false">
												<a id="btnAccept" style="cursor: pointer;" alt="Accept" onclick="showAcceptSpecialOrder(<bean:write name='item' property='ID'/>,'<bean:write name='item' property='totalPriceValue'/>');"><img src="/resources/commerce/images/btn_accept.png"/></a> &nbsp;|&nbsp;<a  style="cursor: pointer;" onclick="showDeclineSpecialOrder(<bean:write name='item' property='ID'/>,'<bean:write name='item' property='totalPriceValue'/>');">Decline</a>&nbsp;&nbsp;<small><util:contextualHelp helpId="47" rollover="true">more&nbsp;info</util:contextualHelp></small>
											</logic:equal>
										</span>
										
									    </div>
										</span>
										
								</logic:equal>