   <h4 style="width:535px"><span>
   <logic:present name="item"  property="wrWorkInst">   
        <logic:notEmpty  name="item" property="wrWorkInst">
    <logic:notEqual name="item" property="wrWorkInst" value="0">
       <html:link action="/search.do?operation=detail" paramId="item" paramName="item" paramProperty="wrWorkInst">
       <bean:write name="item" property="publicationTitle"/>
       </html:link>
    </logic:notEqual>
    <logic:equal name="item" property="wrWorkInst" value="0">
          <label style="color:#000000;"><bean:write name="item" property="publicationTitle" /></label>
    </logic:equal>
    </logic:notEmpty>
    
    <logic:empty  name="item" property="wrWorkInst">
    <label style="color:#000000;"> <bean:write name="item" property="publicationTitle" /></label>
    </logic:empty>
    </logic:present>
    <logic:notPresent name="item"  property="wrWorkInst">
    <label style="color:#000000;"> <bean:write name="item" property="publicationTitle" /></label>   
    </logic:notPresent>
    
    
                    	  <logic:equal name="item" property="rightsLink" value="true">
                         	<logic:notEmpty name="item" property="previewPDFUrl">
                         		<bean:define name="item" property="previewPDFUrl" id="previewPDFUrlString" />
                         	              <br/> <a  style="font-weight:bold;text-decoration:underline"  href="javascript:void(0);" onclick="javascript:window.open('<%=previewPDFUrlString %>');">PREVIEW REPRINT</a>
                           	</logic:notEmpty>
                                  <logic:notEmpty name="item" property="externalRightId">
			                         <logic:equal name="item" property="externalRightId" value="2">
				                         <logic:empty name="item" property="previewPDFUrl">
				                         	&nbsp;&nbsp;&nbsp;&nbsp;<label style="color:#000000;font-weight:bold">No preview available</label>
				                           </logic:empty>
			                           </logic:equal>
                           		</logic:notEmpty>
                         </logic:equal>
                       </span></h4>
<span  style="width:175px;float:right;right:0px;text-align:right;top:8px;padding-right:1px;position:absolute">
<span class="billing_status" >
    	 <util:billingStatus bean="item">
       									<logic:notEmpty name="item" property="rightSourceCd">
      										 <logic:equal name="item" property="rightSourceCd" value="RL">
       												 <logic:notEmpty name="item" property="specialOrder">
    															<logic:equal name="item" property="specialOrder" value="true">
   																	<NOBR>  <util:differentCC bean="orderViewActionForm" license="item"/></NOBR>
   																 </logic:equal>
   														 </logic:notEmpty>
   												 </logic:equal>
    										</logic:notEmpty>
    						</util:billingStatus>
</span>	
</span>			
<div  class="parameter-list" >
                <ul class="list">
                	<li class="orderId"><strong>Order detail ID:</strong>
							<span>
							 <bean:write name="item" property="ID" />
							</span></li>         
							
                	<logic:equal name="item" property="rightsLink" value="true">
                				<logic:notEmpty name="item" property="itemSubDescription">
                                    		<logic:equal  name="item" property="itemTypeCd" value="Book">
	                                    	<li><strong>Chapter Title: </strong><span><bean:write name="item" property="itemSubDescription" /></span></li>
    	                                	</logic:equal>
                                    	<logic:notEqual  name="item" property="itemTypeCd" value="Book">
                                    	<li><strong>Article Title: </strong><span><bean:write name="item" property="itemSubDescription" /></span></li>
                                    	</logic:notEqual>
                                   		</logic:notEmpty>
                                    	<logic:notEmpty name="item" property="granularWorkAuthor">
                                    		<li><strong>Author(s): </strong><span><bean:write name="item" property="granularWorkAuthor" /></span></li>
                                    	</logic:notEmpty>
                                    	<logic:notEmpty name="item" property="granularWorkDoi">
                                    		<li><strong>DOI: </strong><span><bean:write name="item" property="granularWorkDoi" /></span></li>
                                    	</logic:notEmpty>
                                    	<logic:notEmpty name="item" property="granularWorkPublicationDate">                                  	
                                    		<li><strong>Date: </strong><span><bean:write name="item" property="granularWorkPublicationDate" format="MMM dd, yyyy"/></span></li>
                                    	</logic:notEmpty>
                 </logic:equal>
                                    <logic:notEmpty name="item" property="standardNumber">
                                        	<li><logic:notEmpty name="item" property="idnoLabel">
                                            	<strong><bean:write name="item" property="idnoLabel"/>: </strong>
                                            </logic:notEmpty>
                                            <logic:empty name="item" property="idnoLabel">
                                            	<strong>ISBN/ISSN: </strong>
                                            </logic:empty>
                                        <span><bean:write name="item" property="standardNumber" />
										</span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="publicationYear">
                                        <li><strong>Publication year: </strong><span><bean:write name="item" property="publicationYear" /></span></li>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="item" property="publicationType">
                                         <li><strong>Publication Type: </strong><span><bean:write name="item" property="publicationType" /></span></li>
                                    </logic:notEmpty>
                                    
                                  <logic:equal name="item" property="rightsLink" value="true">
		                                    	<logic:notEqual  name="item" property="itemTypeCd" value="Book">
		                                    	<li><strong>Volume: </strong><span><bean:write name="item" property="granularWorkVolume" /></span></li>
		                                    	<li><strong>Issue: </strong><span><bean:write name="item" property="granularWorkIssue" /></span></li>
		                                    	<li><strong>Start page: </strong><span><bean:write name="item" property="granularWorkStartPage" /></span></li>  
		                                    	</logic:notEqual>                                 	
                                   </logic:equal>
                                    <logic:notEmpty name="item" property="publisher">
                                        <li><strong>Publisher: </strong><span><bean:write name="item" property="publisher" /></span></li>
                                    </logic:notEmpty>
                                    
                                    <logic:notEmpty name="item" property="rightsholder">
                                    <logic:notEqual name="item" property="rightsholder" value="null">
                                        <li><strong>Rightsholder: </strong><span><bean:write name="item" property="rightsholder" /></span></li>
                                        </logic:notEqual>
                                    </logic:notEmpty>
                                              <logic:equal name="item" property="rightsLink" value="true">
                                        <logic:notEmpty name="item" property="author">
                                             <li>   <strong>Author/Editor: </strong><span><util:fieldWrap bean="item" property="author" width="20" filter="true" useWordBreak="true"/></span></li>
                                            </logic:notEmpty>
                                        </logic:equal>
                                    <logic:equal name="item" property="rightsLink" value="false">
                                        <logic:notEmpty name="item" property="customAuthor">
                                            <li><strong>Author/Editor: </strong><span><bean:write name="item" property="customAuthor" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="customVolume">
                                           <li> <strong>Volume: </strong><span><bean:write name="item" property="customVolume" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="customEdition">
                                           <li> <strong>Edition: </strong><span><bean:write name="item" property="customEdition" /></span></li>
                                        </logic:notEmpty>
                                    </logic:equal>
                                            <logic:equal name="item" property="photocopy" value="true">
										  <logic:notEmpty name="item" property="customerReference">
													<li><strong>Your line item reference: </strong><span><util:fieldWrap bean="item" property="customerReference" width="20" filter="true" useWordBreak="true"/></span></li>
										</logic:notEmpty>
						    </logic:equal>
							<logic:equal name="item" property="digital" value="true">
										<logic:notEmpty name="item" property="customerReference">
                                            <li><strong>Your reference: </strong><span><util:fieldWrap bean="item" property="customerReference" width="20" filter="true" useWordBreak="true"/></span></li>
                                        </logic:notEmpty>
							</logic:equal>
							<logic:equal name="item" property="republication" value="true">
    											<logic:notEmpty name="item" property="yourReference">
                                            		<li><strong>Your reference: </strong><span><util:fieldWrap bean="item" property="yourReference" width="20" filter="true" useWordBreak="true"/></span></li>
                                        		</logic:notEmpty>
							 </logic:equal>
                </ul>
                <ul>
                <li class="permStatus">
                    		<b><util:permStatus bean="item"/></b>
                </li>
                 <logic:notEmpty name="item" property="categoryName">
                                            	<li><strong>Permission type: </strong><span><util:fieldWrap bean="item" property="categoryName" width="20" filter="true" useWordBreak="true"/></span></li>
                                            </logic:notEmpty>
                                            <logic:notEmpty name="item" property="touName">
                                            	<li><strong>Type of use: </strong><span><util:fieldWrap bean="item" property="touName" width="20" filter="true" useWordBreak="true"/></span></li>
                                            </logic:notEmpty>
                                            <logic:equal name="item" property="specialOrder" value="false">
                                                  <logic:equal name="item" property="photocopy" value="true">
										                <logic:notEmpty name="item" property="perPageFeeMoneyFormat">
										                    <logic:notEqual name="item" property="perPageFeeMoneyFormat" value="$ 0.00">
										                        <li><strong>Per Page Fee:</strong><span> <bean:write name="item" property="perPageFeeMoneyFormat" /></span></li>
										                    </logic:notEqual>
										                </logic:notEmpty>
										                <logic:notEmpty name="item" property="baseFeeMoneyFormat">
										                    <logic:notEqual name="item" property="baseFeeMoneyFormat" value="$ 0.00">
										                        <li><strong>Per Copy Fee:</strong><span> <bean:write name="item" property="baseFeeMoneyFormat" /></span></li>
										                    </logic:notEqual>
										                </logic:notEmpty>
										       </logic:equal>
										      </logic:equal> 
                                            
                                            <logic:equal name="item" property="rightsLink" value="true">
                                            <li>
                                            <span  style="text-alignment:left;right:0px;text-align:right;padding-left:110px;">
                                            <table class="RLTable"  style="width:370px;border-collapse:collapse;">
                                            <col width="120px"/><col width="250px"/>
                                            	<logic:notEmpty name="item" property="jobTicketNumber">
												<tr><td align="left" ><b>Job Ticket:</b></td>
												<td align="left">
	                                 				<p style="padding:0px"><bean:write name="item" property="jobTicketNumber" /></p>
													</td></tr>
	                                  			</logic:notEmpty>
	                                  			<logic:notEmpty name="item" property="externalDetailId">
												<tr><td align="left"><b>Order License Id:</b></td>
												<td align="left">
	                                 			<p style="padding:0px"><bean:write name="item" property="externalDetailId" /></p>
													</td></tr>
	                                  			</logic:notEmpty>
	                                  				<logic:notEmpty name="item" property="orderRefNumber">
												<tr><td align="left"><b>Order ref number:</b></td>
												<td align="left">
	                                 				<p style="padding:0px"><util:fieldWrap bean="item" property="orderRefNumber" width="20" filter="true" useWordBreak="true"/>
	                                 				</p>
													</td></tr>
	                                  			</logic:notEmpty>
                                            </table>
            							 </span>
                                            </li>
                                         </logic:equal>   
                                		<logic:equal name="item" property="rightsLink" value="true">
                                           <bean:define id="viewDetailsId" >
                                           		<logic:equal name="item" property="reprint" value="false">rightslnk</logic:equal>
												<logic:equal name="item" property="reprint" value="true">reprint</logic:equal>
                                            </bean:define>
                                           <bean:define id="displayBlock">none</bean:define>
                                         <logic:equal name="processType" value="ORDER_PRINT">
                                         <bean:define id="displayBlock">block</bean:define>
                                         </logic:equal>
                                         <logic:equal name="processType" value="ORDER_VIEW">
                                         <bean:define id="displayBlock">none</bean:define>
                                         </logic:equal>
                                         <li class="collapseDetails"  id='li_more_info_<%=viewDetailsId%>_<%= index.intValue() + 1 %>'>
                                         <logic:equal name="processType" value="ORDER_VIEW"> 
                                                <a onclick="toggleDetails(this,'more_info_<%=viewDetailsId%>_<%= index.intValue() + 1 %>')"   href="javascript:void(0)">View details</a>
                                             </logic:equal>   
                                            <div id="more_info_<%=viewDetailsId%>_<%= index.intValue() + 1 %>"  style="display:<%=displayBlock%>;">
                                            <ul>
                                        		<li><span style="width:100%"><table class="RLTable">
                                        			<bean:write name="item" property="rlDetailHtml"  filter="false"/>
                                        			</table>
                                        		</span></li>
                                        	</ul>
                                            </div>
                                            </li>
                                        	</logic:equal> 
                                    <logic:equal name="item" property="photocopy" value="true">
                                        <logic:notEmpty name="item" property="chapterArticle">
                                            <li><strong>Article/Chapter: </strong><span><bean:write name="item" property="chapterArticle" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="numberOfPages">
                                            <li><strong>Total number of pages: </strong><span><bean:write name="item" property="numberOfPages" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="numberOfCopies">
                                            <li><strong>Number of copies: </strong><span><bean:write name="item" property="numberOfCopies" /></span></li>
                                        </logic:notEmpty>
                                    </logic:equal>
                                    <logic:equal name="item" property="digital" value="true">
                                        <bean:define id="digitalTypeOfUseString">
                                            <logic:equal name="item" property="email" value="true">E-mail</logic:equal>
                                            <logic:equal name="item" property="intranet" value="true">Intranet</logic:equal>
                                            <logic:equal name="item" property="extranet" value="true">Extranet</logic:equal>
                                            <logic:equal name="item" property="internet" value="true">Internet</logic:equal>
                                        </bean:define>
                                        <li><strong>Requested use: </strong><span><bean:write name="digitalTypeOfUseString"/></span></li>
                                        <logic:notEmpty name="item" property="chapterArticle">
                                            <li><strong>Article/Chapter: </strong><span><bean:write name="item" property="chapterArticle" /></span></li>
                                        </logic:notEmpty>
                                        <logic:equal name="item" property="email" value="true">
                                            <logic:notEmpty name="item" property="dateOfUse">
                                                <li><strong>Date e-mail will be sent: </strong><span><bean:write name="item" property="dateOfUse" format="MM/dd/yyyy" /></span></li>
                                            </logic:notEmpty>
                                            <logic:notEmpty name="item" property="numberOfRecipients">
                                              <li><strong>Number of recipients: </strong><span><bean:write name="item" property="numberOfRecipients" /></span></li>
                                            </logic:notEmpty>
                                        </logic:equal>
                                        <logic:equal name="item" property="net" value="true">
                                            <logic:notEmpty name="item" property="dateOfUse">
                                               <li><strong>Date to be posted: </strong><span><bean:write name="item" property="dateOfUse" format="MM/dd/yyyy" /></span></li>
                                            </logic:notEmpty>
                                            <logic:notEmpty name="item" property="durationString">
                                              <li><strong>Duration of posting: </strong><span><bean:write name="item" property="durationString" /></span></li>
                                            </logic:notEmpty>
                                            <logic:equal name="item" property="internet" value="true">
                                                <logic:notEmpty name="item" property="webAddress">
                                             <li><strong>URL of posting: </strong><span><bean:write name="item" property="webAddress" /></span></li>
                                                </logic:notEmpty>
                                            </logic:equal>
                                        </logic:equal>
                                    </logic:equal>
                                    <logic:equal name="item" property="republication" value="true">
                                        <logic:notEmpty name="item" property="republicationTypeOfUse">
                                            <bean:define id="republicationTypeOfUseString">
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_BROCHURE %>">Brochure</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_CDROM %>">CD-ROM</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_DISSERTATION %>">Dissertation</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_DVD %>">DVD</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_JOURNAL %>">Journal</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_MAGAZINE %>">Magazine</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_NEWSLETTER %>">Newsletter/E-Newsletter</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_NEWSPAPER %>">Newspaper</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_OTHERBOOK %>">Other Book</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_PAMPHLET %>">Pamphlet</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_PRESENTATION %>">PowerPoint Presentation</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_TEXTBOOK %>">Textbook</logic:equal>
                                                <logic:equal name="item" property="republicationTypeOfUse" value="<%= RepublicationConstants.REPUBLICATION_TRADEBOOK %>">Trade Book</logic:equal>
                                            </bean:define>
                                            <li><strong>Requested use: </strong><span><bean:write name="republicationTypeOfUseString" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="newPublicationTitle">
                                            <li><strong>Republication title: </strong><span><bean:write name="item" property="newPublicationTitle" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="republishingOrganization">
                                            <li><strong>Republishing organization: </strong><span><bean:write name="item" property="republishingOrganization" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="business">
                                            <bean:define id="businessString">
                                                <logic:equal name="item" property="business" value="<%= RepublicationConstants.BUSINESS_FOR_PROFIT %>">For-profit</logic:equal>
                                                <logic:notEqual name="item" property="business" value="<%= RepublicationConstants.BUSINESS_FOR_PROFIT %>">Non-profit 501(c)(3)</logic:notEqual>
                                            </bean:define>
                                            <li><strong>Organization status: </strong><span><bean:write name="businessString" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="republicationDate">
                                            <li><strong>Republication date: </strong><span><bean:write name="item" property="republicationDate" format="MM/dd/yyyy" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="circulationDistribution">
                                            <li><strong>Circulation/ Distribution: </strong><span><bean:write name="item" property="circulationDistribution" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="typeOfContent">
                                            <bean:define id="typeOfContentString">
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_FULL_ARTICLE_CHAPTER %>">Full article/chapter</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_EXCERPT %>">Excerpt</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_QUOTATION %>">Quotation</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>">Selected pages</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_CHART %>">Chart</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_GRAPH %>">Graph</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_FIGURE_DIAGRAM_TABLE %>">Figure/ diagram/ table</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_PHOTOGRAPH %>">Photograph</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_CARTOONS %>">Cartoon</logic:equal>
                                                <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_ILLUSTRATION %>">Illustration</logic:equal>
                                            </bean:define>
                                            <li><strong>Type of content: </strong><span><bean:write name="typeOfContentString" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="chapterArticle">
                                            <li><strong>Description of requested content: </strong><span><bean:write name="item" property="chapterArticle"/></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="pageRange">
                                            <li><strong>Page range(s): </strong><span><bean:write name="item" property="pageRange" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="numberOfPages">
                                            <logic:equal name="item" property="typeOfContent" value="<%= RepublicationConstants.CONTENT_SELECTED_PAGES %>">
                                                <li><strong>Number of pages: </strong><span><bean:write name="item" property="numberOfPages" /></span></li>
                                            </logic:equal>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="translationLanguage">
                                            <bean:define id="translationLanguageString">
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.NO_TRANSLATION %>">No Translation</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_CHINESE %>">Chinese</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_ENGLISH %>">English</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_FRENCH %>">French</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_GERMAN %>">German</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_ITALIAN %>">Italian</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_JAPANESE %>">Japanese</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_PORTUGUESE %>">Portuguese</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_RUSSIAN %>">Russian</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_SPANISH %>">Spanish</logic:equal>
                                                <logic:equal name="item" property="translationLanguage" value="<%= RepublicationConstants.TRANSLATED_LANGUAGE_OTHER %>">Other</logic:equal>
                                            </bean:define>
                                            <li><strong>Translating to: </strong><span><bean:write name="translationLanguageString" /></span></li>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="item" property="contentsPublicationDate">
                                            <li><strong>Requested content's publication date: </strong><span><bean:write name="item" property="contentsPublicationDate" format="MM/dd/yyyy" /></span></li>
                                        </logic:notEmpty>
                                    </logic:equal>
                                    
                                    <logic:notEmpty name="item" property="paymentMethod">
                                         <li><strong>Payment Method: </strong><span><bean:write name="item" property="paymentMethod" /></span></li>
                                    </logic:notEmpty>

                                 </ul>
				</div>
                     <logic:notEmpty name="item" property="rightsQualifyingStatement">
                                <p  class="icon-alert" style="padding:0px 20px 0px"><strong><bean:write name="item" property="rightsQualifyingStatement" /></strong>
                                <logic:notEmpty name="item" property="externalCommentTerm">
                                <b>Rightsholder terms apply </b>(see terms and conditions)
                            	</logic:notEmpty>
                                </p>
                                </logic:notEmpty>
			              <logic:empty name="item" property="rightsQualifyingStatement">                                
			              		<logic:notEmpty name="item" property="externalCommentTerm">
                            	    <p><b>Rightsholder terms apply </b><util:contextualHelp titleName="popupTitle" bodyName="item" bodyProperty="externalCommentTerm" rollover="false"></util:contextualHelp>(see terms and conditions)</p>
                              	</logic:notEmpty>
                            </logic:empty>
       <!-- item footer -->
    <security:ifAdminResourceRequested></security:ifAdminResourceRequested>
    <util:else>
    <logic:equal name="processType" value="ORDER_VIEW">
    <ul class="add-services">
    <logic:equal name="item" property="cancelable" value="true">
    	<logic:equal name="item" property="awaitingLicConfirm" value="false">
		<li>
    		<a href="javascript:cancel(<bean:write name='item' property='ID'/>, <bean:write name='orderViewActionForm' property='id'/>)">Cancel</a>
		</li>
    	</logic:equal>
    </logic:equal>
    
    <logic:notEqual name="item" property="rightSourceCd" value="RL">
    	<logic:notEqual name="item" property="productCd" value="RLS">
    		<logic:equal name="item" property="availableToCopy" value="true">
	    		<logic:equal name="orderViewActionForm" property="cartEmpty" value="false">
	     			<li>   <a href="javascript:copyNonacademicLicense(<bean:write name="orderViewActionForm" property="id"/>,<bean:write name='item' property='ID'/>);">Copy</a></li>
	    		</logic:equal>
        	<logic:equal name="orderViewActionForm" property="cartEmpty" value="true">
			    <li><a href="javascript:copyLic(<bean:write name="orderViewActionForm" property="id"/>,<bean:write name='item' property='ID'/>);">Copy</a></li>
    	</logic:equal>
    </logic:equal>
    </logic:notEqual>
    </logic:notEqual>
    <logic:equal name="item" property="rightsLink" value="false">
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
    
    </logic:equal>
   
</ul>    	
</logic:equal>
</util:else>
<!-- Rightslink related changes start here -->
<logic:equal name="item" property="rightsLink" value="false">
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
</logic:equal>          			
<logic:equal name="item" property="rightsLink" value="true">
<p></p>
<span style="bottom: 16px; position: absolute;">
		<logic:equal name="item" property="specialOrder" value="true">
		   	  <logic:empty name="item" property="paymentMethodCd">
		   	  <logic:equal name="item" property="reprint" value="true">
		   		  												<strong> Note:&nbsp;</strong> This item will be managed and fulfilled through CCC's <strong>RightsLink</strong> service.&nbsp;<util:contextualHelp helpId="39" rollover="false">&nbsp;More info</util:contextualHelp><
		   									   				</logic:equal>
		   											<logic:equal name="item" property="reprint" value="false">
		   		   												<strong> Note:&nbsp;</strong>This item will be managed through CCC's <strong>RightsLink service.</strong><util:contextualHelp helpId="38" rollover="false">&nbsp;More info</util:contextualHelp>
		   											</logic:equal>
		   	   </logic:empty>
		   	   <logic:notEmpty name="item" property="paymentMethodCd">
		         <!-- Case RLR with Payment Method As Credit Card-->
		           <logic:equal name="item" property="paymentMethodCd" value="CREDIT_CARD">
		                      <!-- Case RLR with unCharged Credit Card-->
		          <logic:equal name="item" property="billingStatusCd" value="NOT_BILLED">
		          <b>Note:&nbsp;</b> This item will be managed through CCC'S <strong>RightsLink service.</strong><util:contextualHelp helpId="44" rollover="false">&nbsp;More info</util:contextualHelp>
		          </logic:equal>
		           <!-- Case RLR with Charged Credit Card-->
		         <logic:equal name="item" property="billingStatusCd" value="BILLED">
		         <b>Note:&nbsp;</b> This item was charged to your credit card by CCC'S <strong>RightsLink service.</strong><util:contextualHelp helpId="42" rollover="false">&nbsp;More info</util:contextualHelp>
		          </logic:equal>
		         </logic:equal>
		         <!-- Case RLR with Payment Method As Invoice-->
		         <logic:equal name="item" property="paymentMethodCd" value="INVOICE">
		         <!-- Case RLR with Paid Invoice-->
		         <logic:equal name="item" property="billingStatusCd" value="NOT_BILLED">
		          <b>Note:&nbsp;</b>This item will be managed through CCC'S <strong>RightsLink service.</strong><util:contextualHelp helpId="44" rollover="false">&nbsp;More info</util:contextualHelp>
		          </logic:equal>
		          <!-- Case RLR with unPaid Invoice-->
		         <logic:equal name="item" property="billingStatusCd" value="BILLED">
		         <b>Note:&nbsp;</b>This item was invoiced through CCC'S <strong>RightsLink service.</strong><util:contextualHelp helpId="41" rollover="false">&nbsp;More info</util:contextualHelp>
		           </logic:equal>
		        </logic:equal>
		   	   </logic:notEmpty>
			   </logic:equal>
		    <logic:equal name="item" property="specialOrder" value="false">
				   <logic:empty name="item" property="paymentMethodCd">
				  <strong> Note:&nbsp;</strong>This item will be managed through CCC's <strong>RightsLink service.</strong><util:contextualHelp helpId="38" rollover="false">&nbsp;More info</util:contextualHelp>
						</logic:empty>
						<logic:notEmpty name="item" property="paymentMethodCd">
								 <logic:equal name="item" property="paymentMethodCd" value="CREDIT_CARD">
									 <!-- Case not Billed to CC -->
									    <logic:equal name="item" property="billingStatusCd" value="NOT_BILLED">
									    <b>Note:&nbsp;</b> This item will be charged to your credit card through our <strong>RightsLink service.</strong><util:contextualHelp helpId="40" rollover="false">&nbsp;More info</util:contextualHelp>
									    </logic:equal>
										    <!-- Case  Billed  to CC-->
										   <logic:equal name="item" property="billingStatusCd" value="BILLED">
										    <b>Note:&nbsp;</b> This item was charged to your credit card through our <strong>RightsLink service.</strong><util:contextualHelp helpId="42" rollover="false">&nbsp;More info</util:contextualHelp>
										    </logic:equal>
				   				</logic:equal>
				   <!-- Case payment with Invoice -->
				   <logic:equal name="item" property="paymentMethodCd" value="INVOICE">
						   <!-- Case not billed to Invoice -->
						   <logic:equal name="item" property="billingStatusCd" value="NOT_BILLED">
						    <b>Note:&nbsp;</b>This item will be invoiced separately through our <strong>RightsLink service.</strong><util:contextualHelp helpId="41" rollover="false">&nbsp;More info</util:contextualHelp>
						    </logic:equal>
						    <!-- Case billed to Invoice -->
						   <logic:equal name="item" property="billingStatusCd" value="BILLED">
						    <b>Note:&nbsp;</b>This item was invoiced separately through our <strong>RightsLink service.</strong><util:contextualHelp helpId="43" rollover="false">&nbsp;More info</util:contextualHelp>
						    </logic:equal>
				   </logic:equal>
						</logic:notEmpty>
		   </logic:equal>
		   </span>
		   <span class="price" style="color:#000000">
          					<bean:write name="item" property="price" />
          			</span>
		     </logic:equal> <!--  end rights link check -->