
<logic:greaterThan value="0" name="cartFormCOI" property="numberOfAcademicCartItems">
            <!-- Cart Items -->
       		<bean:define id="academicProductBoxStyle">border-top:0px;</bean:define>
            <logic:iterate name="cartFormCOI" property="academicCartItems" id="item" indexId="index" type="PurchasablePermission">
                    <bean:define id="itemAcademicNumber"><%= index.intValue() + 1 %>.</bean:define>
                    
                    <logic:notEmpty name="item"  property="externalCommentTerm">
                        <bean:define id="termsBody" name="item" property="externalCommentTerm"/>
                    </logic:notEmpty>
                    
                      <!--  override bottom lin3 -->
		        <bean:define id="currentACount"> <%=index.intValue() + 1%></bean:define>
            
                
            <div class="product-box" style="<%=academicProductBoxStyle%>" >  
            
            <h4><bean:write name="itemAcademicNumber" /><span>
                         <logic:notEqual name="item" property="wrWorkInst" value="0">
                              <logic:equal name="item" property="manualSpecialOrder" value="false">
                                  <html:link action="/search.do?operation=detail&backlink=false" paramId="item" paramName="item" paramProperty="wrWorkInst"><bean:write name="item" property="publicationTitle" /></html:link>
                              </logic:equal>
                              <logic:equal name="item" property="manualSpecialOrder" value="true">
                                  <a href="#"><bean:write name="item" property="publicationTitle" /></a>
                              </logic:equal>
                        </logic:notEqual>
                        <logic:equal name="item" property="wrWorkInst" value="0">
                              <a href="#"><bean:write name="item" property="publicationTitle" /></a>
                        </logic:equal></span></h4>
                       <div class="parameter-list"> 
                       
                       <ul class="list">
                       
                         <logic:notEmpty name="item" property="standardNumber">
                         <li><strong>
                                    <logic:notEmpty name="item" property="idnoLabel"><bean:write name="item" property="idnoLabel"/>: </logic:notEmpty>
                                            <logic:empty name="item" property="idnoLabel">
                                            	ISBN/ISSN: 
                                            </logic:empty>
                                            </strong><span><bean:write name="item" property="standardNumber" /></span>
                            </li>                
                       </logic:notEmpty>
                          <logic:equal name="item" property="republication" value="true">
                                            <logic:notEmpty name="item" property="contentsPublicationDate">
                                                <li><strong>Publication year: </strong><span><bean:write name="item" property="contentsPublicationDate" format="yyyy" /></span></li>
                                            </logic:notEmpty>
                                        </logic:equal>
                             <logic:equal name="item" property="republication" value="false">
                                            <logic:notEmpty name="item" property="publicationYearOfUse">
                                                <li><strong>Publication year: </strong><span><bean:write name="item" property="publicationYearOfUse" /></span></li>
                                            </logic:notEmpty>
                                        </logic:equal>   
                                        
                                         <logic:notEmpty name="item" property="publicationType">
                                           <li><strong>Publication Type: </strong><span><util:fieldWrap bean="item" property="publicationType" width="20" filter="true" useWordBreak="true"/></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="publisher">
                                           <li><strong>Publisher: </strong><span><util:fieldWrap bean="item" property="publisher" width="20" filter="true" useWordBreak="true"/></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="rightsholder">
                                        <logic:notEqual name="item" property="rightsholder" value="null">
                                           <li><strong>Rightsholder: </strong><span><util:fieldWrap bean="item" property="rightsholder" width="20" filter="true" useWordBreak="true"/></span></li>
                                           </logic:notEqual>
                                        </logic:notEmpty>
                                        
                                            <logic:notEmpty name="item" property="customAuthor">
                                              <li><strong>Author/Editor: </strong><span><util:fieldWrap bean="item" property="customAuthor" width="20" filter="true" useWordBreak="true"/></span></li>
                                            </logic:notEmpty>
                                            <logic:notEmpty name="item" property="customVolume">
                                              <li><strong>Volume: </strong><span><util:fieldWrap bean="item" property="customVolume" width="20" filter="true" useWordBreak="true"/></span></li>
                                            </logic:notEmpty>
                                            <logic:notEmpty name="item" property="customEdition">
                                              <li><strong>Edition: </strong><span><util:fieldWrap bean="item" property="customEdition" width="20" filter="true" useWordBreak="true"/></span></li>
                                            </logic:notEmpty>
                                               <logic:notEmpty name="item" property="customerReference">
                                                <li><strong>Your line item reference: </strong><span><util:fieldWrap bean="item" property="customerReference" width="20" filter="true" useWordBreak="true"/></span></li>
                                            </logic:notEmpty>

                       </ul>
                       <ul>
                       
                       <li><strong>Permission type:</strong><span>
							<util:fieldWrap bean="item" property="categoryName" width="20" filter="true" useWordBreak="true"/></span>
					</li>
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
                                            <logic:notEmpty name="item" property="chapterArticle">
                                                <li><strong>Article/Chapter: </strong><span><util:fieldWrap bean="item" property="chapterArticle" width="20" filter="true" useWordBreak="true"/></span></li> 
                                            </logic:notEmpty>
                                            <logic:notEmpty name="item" property="dateOfIssue">
                                                <li><strong>Date of issue: </strong><span><bean:write name="item" property="dateOfIssue" format="MM/dd/yyyy" /></span></li> 
                                            </logic:notEmpty>
                                            <logic:notEmpty name="item" property="pageRange">
                                                <li><strong>Page range(s): </strong><span><util:fieldWrap bean="item" property="pageRange" width="20" filter="true" useWordBreak="true"/></span></li> 
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
                    <!-- Item Rightsholder Terms and qual stmt. -->
                    <logic:notEmpty name="item" property="rightsQualifyingStatement">
                                <p  class="icon-alert" style="padding:0px 20px 0px"><strong><bean:write name="item" property="rightsQualifyingStatement" /></strong>&nbsp;
                                <logic:notEmpty name="item" property="externalCommentTerm">
                                
                                <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">Rightsholder terms apply</util:contextualHelp>
                                </logic:notEmpty>
                                </p>
                        
                    </logic:notEmpty>
                    <logic:empty name="item" property="rightsQualifyingStatement">
                        <logic:notEmpty name="item" property="externalCommentTerm">
                                <p> <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">Rightsholder terms apply</util:contextualHelp></p>
                        </logic:notEmpty>
                    </logic:empty>
                    
                    <!-- item footer -->
                           <ul class="add-services">
							<li> <a href="javascript:editAcademicCartItem(<%= index %>)" id="edit_<%= index.intValue() + 1 %>">Edit</a></li>
							<li><a href="javascript:removeAcademicCartItem(<%= index %>)" id="remove_<%= index.intValue() + 1 %>">Remove</a></li>
						</ul>
         
                                     <span class="price" style="color:#000000">
                                    <!--  display price updated-->
												 <%if(updatedPriceCartItems.containsKey(Long.valueOf(item.getItem().getItemId()))){
												 out.println("<div class='priceUpdate' style='color:red;font-weight:bold'>");
												 out.println(updatedPriceCartItems.get(Long.valueOf(item.getItem().getItemId())));
												 out.println("</div>");
												 }
												 %>
											<bean:write name="item" property="price" />
                                            <logic:equal name="item" property="specialOrder" value="true">
                                            <em>Special Order</em>
                                            <i>Pending rightsholder review <util:contextualHelp helpId="70" rollover="true"><img src="<html:rewrite page="/resources/commerce/images/helpmark.gif"/>" style="vertical-align:middle;"></util:contextualHelp></i>
                                            </logic:equal>
                                            </span>

                
                
                </div>
			<bean:define id="academicProductBoxStyle">border-top:1px solid #CCCCCC;</bean:define>
                </logic:iterate>
        </logic:greaterThan>   
                    