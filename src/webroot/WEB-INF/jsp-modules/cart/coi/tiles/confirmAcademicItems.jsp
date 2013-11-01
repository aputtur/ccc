             
              <h4><span><bean:write name="item" property="publicationTitle" /></span></h4>

<div class="parameter-list">
							<ul class="list">
							<li class="orderId"><strong>Order detail ID:</strong>
							<span>
							 <logic:equal name="skipItemForDebug" value="false">
							 <bean:write name="item" property="ID" />
                   				</logic:equal>
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
                                    <logic:notEqual name="item" property="rightsholder" value="No Rightsholder">
                                        <li><strong>Rightsholder: </strong><span><bean:write name="item" property="rightsholder" /></span></li>
                                        </logic:notEqual>
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
									<logic:equal name="skipItemForDebug" value="false">
                   							<b><util:permStatus bean="item"/></b>
                   				</logic:equal></li>
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
                            
                                    <span class="price" style="color:#000000">
											<bean:write name="item" property="price" />
											<logic:notEqual name="item" property="price" value="$TBD" >
										<br/><span class="largetype bold"><util:pricePerStudent bean="item"/></span>
										</logic:notEqual>
                                    </span>
			                    
			     