<%@ page language="java"%>
<%@ page errorPage="/jspError.do" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page import="java.util.List" %>
<%@ page import="com.copyright.ccc.web.util.PermissionCategoryDisplay" %>
<%@ page import="com.copyright.ccc.web.util.PermSummaryTouDisplay" %>
<%@ page import="com.copyright.ccc.web.forms.SearchForm"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<script src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" type="text/javascript"></script>


<jsp:useBean id="searchForm" scope="session" class="com.copyright.ccc.web.forms.SearchForm"/>
<bean:define id="termsTitle">Rightsholder Terms</bean:define>
<bean:define id="emailTitle">Request This Work</bean:define>
<bean:define id="item" name="searchForm" property="selectedItem" />
<bean:define id="wrkInst" name="searchForm" property="selectedItem.wrkInst"/>

 <logic:equal name="item" property="isPublicDomainNotBiactive" value="false">
 <%
  String txtTermsApply = null;
 
 long currentTimeForPPU = System.currentTimeMillis();
 %>
 <logic:iterate name="searchForm" property="permCatDisplay" id="permCat" indexId="idx">
 <logic:notEqual name="permCat" property="isLicense" value="true">
  <logic:equal name="permCat" property="isValidCategory" value="true">

<%
    //  2009-02-19  MSJ
    //  Added logic around the creation of the termsBody, overriding the rhTerms
    //  for the republication and digital rights.
%>
    <logic:notEmpty name="permCat" property="rhTerms">
        <bean:define id="termsBody" name="permCat" property="rhTerms"/>
        <logic:equal name="permCat" property="isRepublication" value="true">
            <%
            txtTermsApply = "Special terms from the rightsholder may apply, depending on your type of use.<br /><br />Please continue order process and special terms will be provided during Order Review, if applicable.";
            pageContext.setAttribute("termsBody", txtTermsApply, PageContext.PAGE_SCOPE);
            %>
        </logic:equal>
        <logic:equal name="permCat" property="isDigital" value="true">
            <%
            txtTermsApply = "Special terms from the rightsholder may apply, depending on your type of use.<br /><br />Please continue order process and special terms will be provided during Order Review, if applicable.";
            pageContext.setAttribute("termsBody", txtTermsApply, PageContext.PAGE_SCOPE);
            %>
        </logic:equal>
    </logic:notEmpty>

<bean:define id="categoryImg">
	<logic:greaterThan value="7" name="permCat" property="categoryId" >
	<img width="19" height="19" class="align-left"  src="media/images/spacer.gif"/></logic:greaterThan>
	
	<logic:equal name="permCat" property="categoryId" value="1">
		<img width="19" height="19" class="align-left"  src="resources/commerce/images/ico2.png"/>
	</logic:equal>
	<logic:equal name="permCat" property="categoryId" value="2">
		<img width="19" height="19" class="align-left"  src="resources/commerce/images/ico3.png"/>
	</logic:equal>
	<logic:equal name="permCat" property="categoryId" value="3">
		<img width="19" height="19" class="align-left"  src="resources/commerce/images/ico4.png"/>
	</logic:equal>
	<logic:equal name="permCat" property="categoryId" value="4">
		<img width="19" height="19" class="align-left"  src="resources/commerce/images/ico5.png"/>
	</logic:equal>
	<logic:equal name="permCat" property="categoryId" value="5">
		<img width="19" height="19" class="align-left"  src="resources/commerce/images/ico6.png"/>
	</logic:equal>
	<logic:equal name="permCat" property="categoryId" value="6">
		<img width="19" height="19" class="align-left"  src="resources/commerce/images/ico7.png"/>
	</logic:equal>
	<logic:equal name="permCat" property="categoryId" value="7">
		<img width="19" height="19" class="align-left"  src="resources/commerce/images/ico8.png"/>
	</logic:equal>
</bean:define>
  <bean:define id="availabilityHelpId">
     <logic:equal name="permCat" property="isPublicDomain" value="true">32</logic:equal>
     <logic:notEqual name="permCat" property="isPublicDomain" value="true">
       <logic:match value="Special Order" name="permCat" property="availability">11</logic:match>
       <logic:notMatch value="Special Order" name="permCat" property="availability">9</logic:notMatch>
     </logic:notEqual>
  </bean:define>

  <bean:define id="availabilityIcon">
     <logic:equal name="permCat" property="isPublicDomain" value="true">icon-check</logic:equal>
     <logic:notEqual name="permCat" property="isPublicDomain" value="true">
       <logic:match value="Special Order" name="permCat" property="availability">icon-available</logic:match>
       <logic:notMatch value="Special Order" name="permCat" property="availability">icon-available</logic:notMatch>
     </logic:notEqual>
   </bean:define>


<bean:define id="tabIcon" >
 <logic:equal name="permCat" property="isNotAvailable" value="false">tab-block</logic:equal>
 <logic:equal name="permCat" property="isNotAvailable" value="true">tab-block-no</logic:equal>
 
   <logic:equal name="permCat" property="isContactRightsholder" value="true">
	<logic:equal name="permCat" property="integrates" value="true">
	  <logic:equal name="searchForm" property="teleSalesUp" value="true">tab-block-no</logic:equal>
	</logic:equal>
    </logic:equal>
	<logic:equal name="permCat" property="isNotAvailable" value="true">
        <logic:equal name="permCat" property="integrates" value="true">tab-block-no</logic:equal>
    </logic:equal>
    
    <logic:equal name="permCat" property="isSpecialOrder" value="true">
       <logic:equal name="permCat" property="isRepublication" value="true">
       		tab-block-no
       </logic:equal>
    </logic:equal>
    		  
   </bean:define>
   
   <div id="waiting_for_price" style="display: none;">
       	<div id="progress-widecontent_ppu">  
       			<h1>Processing... Please wait... </h1> 
		</div>  
   </div>
   
   <!--  <div id="waiting_for_search" style="display: none;">
 		<img src="<s:url includeParams='none' value='../resources/images/progressbar.gif'/>" alt=""/>&nbsp;Processing, please wait...    
	</div>   -->
<div class="<%=tabIcon%>">
  
     <div class="type">
<!-- TODO reintroduce biactive, after May, This logic would be moved to the TOU display or selection of date may eliminate bi-active display -->
<%= categoryImg%>
        <strong><bean:write name="permCat" property="categoryDescription"/></strong>
        <!-- ADD SUPERSCRIPT TERMS -->

<logic:equal name="permCat" property="isRepublication" value="true">

	<div style="margin-top:30px; margin-left: 30px; width: 240px; float:left;">

        <iframe frameborder="0" scrolling="no" align="right" src ="/content/cc3/en/tools/permission_acquisitionadvbox-bottom.html">
            <p>Your browser does not support iframes.</p>
        </iframe>

	</div>
	
	</logic:equal>
        
</div>
      
 <!-- START AVAILABILITY SECTION -->     
      <div class="availability" id="<%=idx %>" >
  
       <logic:equal name="permCat" property="gotTerms" value="false"></logic:equal>
       <logic:equal name="permCat" property="isAvailable" value="true">
         <logic:notEqual name="permCat" property="isContactRHOrSpecialOrder" value="true">
	        <div class="title">
	        <img width="17" height="17" class="align-left" alt="image description" src="media/images/icon-available.gif"/>
			<strong style="font-size: 11px;font-family: Verdana,Arial,sans-serif;font-weight:bold;">
			<logic:match name="permCat" property="availability" location="start" value="Available for Purchase">
			Available for purchase
			</logic:match>
			<logic:notMatch name="permCat" property="availability" location="start" value="Available for Purchase">
			<bean:write name="permCat" property="availability" />
			</logic:notMatch>
			</strong>
	        </div>
        </logic:notEqual> <!-- END OF IS NOT CONTACT RIGHTS HOLDER /SPECIAL ORDER -->
       </logic:equal>

        <logic:equal name="permCat" property="isNotAvailable" value="true">
            <logic:equal name="permCat" property="integrates" value="false">
               <!-- This publisher does not integrate with RIGHTSLINK -->
                <div class="title">
        <!-- TODO NEED TO DEAL WITH BIactive after May -->
                 <img width="16" height="16" class="align-left" alt="image description" src="media/images/icon-unavailable.gif" />
                   	<util:contextualHelp helpId="10" rollover="false" styleId="<%= String.valueOf(idx) %>">
                   	<strong style="font-size: 11px;font-family: Verdana,Arial,sans-serif;font-weight:bold;text-decoration:underline;cursor:pointer;cursor:hand;">
                        <bean:write name="permCat" property="availability" />
                        </strong>
					 </util:contextualHelp>
                        

                </div>
            </logic:equal>

            <logic:equal name="permCat" property="integrates" value="true">
                <!-- This publisher integrates with RIGHTSLINK -->
                
                    <span class="icon-rightslink">
        <!-- TODO NEED TO DEAL WITH BIactive after May -->
                        May be available through<br/>our RIGHTSLINK service<br/>
                        <a href="#rightslink">Check options below</a>
                    </span>
                
            </logic:equal>

            
        </logic:equal>
        
       
        <logic:equal name="permCat" property="isContactRightsholder" value="true">
			<logic:equal name="permCat" property="integrates" value="true">
			  <logic:equal name="searchForm" property="teleSalesUp" value="true">
				<span class="icon-rightslink">
        <!-- TODO NEED TO DEAL WITH BIactive after May -->
					May be available through<br/>our RIGHTSLINK service<br/>
					<a href="#rightslink">Check options below</a>
				</span>
			  </logic:equal>
			</logic:equal>
			<logic:equal name="permCat" property="integrates" value="false">
				<span class="icon-contact">
        <!-- TODO NEED TO DEAL WITH BIactive after May -->
				 <logic:equal name="searchForm" property="teleSalesUp" value="true">
<!-- 				    <util:contextualHelp helpId="12" rollover="false" styleId="<%= String.valueOf(idx) %>">
-->					  
					<strong style="font-size: 11px;font-family: Verdana,Arial,sans-serif;font-weight:bold;">
						<bean:write name="permCat" property="availability" />
				</strong>
<!--  				    </util:contextualHelp>
-->				 </logic:equal>
				 <logic:notEqual name="searchForm" property="teleSalesUp" value="true">
				 <strong style="font-size: 11px;font-family: Verdana,Arial,sans-serif;font-weight:bold;">
				     Contact rightsholder directly
				     </strong>
				 </logic:notEqual>
				</span>
			</logic:equal>
	  </logic:equal>
	  
	  <logic:equal name="permCat" property="isSpecialOrder" value="true">
       <logic:equal name="permCat" property="isRepublication" value="true">
        <div class="title">
             <img width="16" height="16" class="align-left" alt="image description" src="media/images/icon-unavailable.gif" />
                   	<util:contextualHelp helpId="71" rollover="false" styleId="<%= String.valueOf(idx) %>">
                   	<strong style="font-size: 11px;font-family: Verdana,Arial,sans-serif;font-weight:bold;text-decoration:underline;cursor:pointer;cursor:hand;">
                        Not Available
                        </strong>
					 </util:contextualHelp>
                        

                </div>
        </logic:equal>
       </logic:equal>
       
       <logic:equal name="permCat" property="isSpecialOrder" value="true">
       <logic:notEqual name="permCat" property="isRepublication" value="true">
        <div class="title">
        <!-- TODO NEED TO DEAL WITH BIactive after May -->
        <img width="17" height="17" class="align-left" alt="image description" src="media/images/icon-available.gif"/>
          <util:contextualHelp helpId="11" rollover="false" styleId="<%= String.valueOf(idx) %>">
          <strong style="font-size: 11px;font-family: Verdana,Arial,sans-serif;font-weight:bold;text-decoration:underline;cursor:pointer;cursor:hand;">
            <bean:write name="permCat" property="availability" />
            </strong>
          </util:contextualHelp>
        </div>
        </logic:notEqual>
       </logic:equal>

       
 <!--  div class="clearer"></div-->
 <!-- Rights Qualifying statement -->
    <!--   <logic:equal name="permCat" property="gotRQualStmt" value="true">
        <logic:equal name="permCat" property="globalRightsQualifier" value="false">
         <div class="icon-alert">
          <strong><bean:write name="permCat" property="rightsQualifyingStatement"/></strong>
         </div>
        </logic:equal>
      </logic:equal>
       -->
       
<!-- REPLACE WITH get list of TOUs and display as radio buttons -->    
     
               <logic:notEqual name="permCat" property="isContactRHOrSpecialOrder" value="true">
            
             <!-- Layout radio buttons followed by the price and order button -->
             <bean:define id="permSumTouCollection" name="permCat" property="permSummaryTouDisplays" />
             <div class="purchase-form" action="#">
             <fieldset>
             
                 
             <logic:iterate name="permCat" property="permSummaryTouDisplays" id="permTou" indexId="permTouIdx">
             
                          
             
             
                <logic:equal name="permTou" property="isValidTOU" value="true">
                
                   <logic:notEqual name="permTou" property="rgtInstRlOfferId" value="0">
                   
                   <!-- added for pub to pub -->
                   <bean:define id="rlPermissionType" name="permTou" property="rlPermissionType"/>
                   <bean:define id="offerChannel" name="permTou" property="offerChannel"/>
                   <bean:define id="rlPubCode" name="permTou" property="rlPubCode"/>
                   <bean:define id="permissionLevel" name="permTou" property="permissionLevel"/>
                  
	                <bean:define id="rgtInstRlOfferId" name="permTou" property="rgtInstRlOfferId"/>
	                <bean:define id="rightsholderInst" name="permTou" property="rightsholderInst"/>
	                <bean:define id="tpuInst" name="permTou" property="tpuInst"/>
	                <bean:define id="tpuDesc"   >
	                <bean:write name="permTou" property="description"  filter="true" />
	                </bean:define>
	                <bean:define id="tpuEscapedDesc"   >
	                <bean:write name="permTou" property="escapedDescription"  filter="true" />
	                </bean:define>
	                
	                <bean:define id="rrTouId" name="permTou" property="rrTouId"/>
	                <bean:define id="categoryId" name="permCat" property="categoryId"/>
	                	                         
	                <!-- ONLY SHOW THE RADIO BUTTONS IF AT LEAST ONE OF THEM IS AVAILABLE FOR PURCHASE -->
	                <logic:match name="permCat" property="availability" location="start" value="Available for Purchase">
	                	                	                	            
	                <div class="row">
		                <!-- Check if contact rights holder directly -->
		                <logic:equal name="permTou" property="isNotAvailable" value="false">
			                   <logic:equal name="permTou" property="isContactRightsholder" value="true">
			                      <logic:equal name="searchForm" property="teleSalesUp" value="true">
			                   <html:radio  styleId="radio_${wrkInst}_${categoryId}" name="searchForm"  property="selectedTou" value='${tpuDesc}'  onclick="setTouId('${permissionLevel}',${tpuInst}, ${rgtInstRlOfferId},'${rightsholderInst}','${tpuEscapedDesc}',${rrTouId},${categoryId}, '${rlPermissionType}', '${offerChannel}', '${rlPubCode}')"></html:radio>
			                   <bean:write name="permTou" property="description" />
		                          </logic:equal>
		                       </logic:equal>
		                       <logic:notEqual name="permTou" property="isContactRightsholder" value="true">
			                       <html:radio  styleId="radio_${wrkInst}_${categoryId}" name="searchForm"  property="selectedTou" value='${tpuDesc}'  onclick="setTouId('${permissionLevel}',${tpuInst},${rgtInstRlOfferId},'0','${tpuEscapedDesc}',${rrTouId},${categoryId}, '${rlPermissionType}', '${offerChannel}', '${rlPubCode}')"></html:radio>
		                       <bean:write name="permTou" property="description" />
		                       </logic:notEqual>	
	                       </logic:equal>	                       	                                     
		               
		                <% if (idx==0 && permTouIdx==0) { %><html:hidden styleId="pickme_radio_${wrkInst}_${categoryId}" name="searchForm" property="permissionDirectProduct"></html:hidden><% } %>
		                </div>
	                </logic:match>
	             </logic:notEqual>
	             
	           </logic:equal> <!-- END OF isValidTOU -->
	           
	           <logic:equal name="permCat" property="isRepublication" value="true">
	           
	         <% if (permTouIdx==(searchForm.getRepubTouCount() - 1)) { %>
             <logic:equal name="permTou" property="offerChannel" value="PUB_TO_PUB">
             	
             	<% if (searchForm.getIsEducational()) { %>
             		
             			<div class="calloutboxnobackground3">
             				<div style="font-family:verdana, sans-serif;font-size:5px">
             				 &nbsp;&nbsp;<li>For permission to republish in a multimedia product that is intended for educational use in K-12, college, and university settings, select<br><util:contextualHelp helpId="72" rollover="true"><b>Educational/instructional Program.</b></util:contextualHelp> Otherwise, select<br><util:contextualHelp helpId="68" rollover="true"><b> Other published product.</b></util:contextualHelp></li>
	                       	  	</div>
	                       	  	</div>
	                       	  	
	         		
	         		<% } else { %>
	         			
	         			<div class="calloutboxnobackground3">
             				<div style="font-family:verdana, sans-serif;font-size:5px">
             				&nbsp;&nbsp;<li>For permission to republish in any digital publishing medium, select<br><util:contextualHelp helpId="68" rollover="true"><b>Other published product.</b></util:contextualHelp></li>
	                       	   	</div>
	                       	  	</div>
  		         			         		
	         		<% } %>	         		         	             			         	
	         	
	         </logic:equal>
             <% } %>
             </logic:equal>
	           
	       </logic:iterate>                   	     	
	         	             			         		         		        
             </fieldset>
                   <!-- ADD SUPERSCRIPT TERMS -->
       <logic:equal name="permCat" property="gotTerms" value="true">
        <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true"><sup style="float:left">Terms apply</sup><br/></util:contextualHelp>
       </logic:equal>
             </div>
          
         </logic:notEqual>
        
<!-- End of display TOUs as radio buttons -->
       
       </div>
       
                  <logic:notEqual name="permCat" property="isContactRHOrSpecialOrder" value="true">
       
       <!-- BUTTON SECTION Choose one of these Price & Order Buttons -->
             <logic:notEqual name="permCat" property="isAvailableRightslink" value="true">
             
			     <logic:equal name="permCat" property="isAvailable" value="true">
			       <logic:equal name="permCat" property="isPublicDomain" value="false">
			         
			          
			           <a id="pr_<%= idx %>" class="btn-price" onclick="addToCart('',<bean:write name='permCat' property='categoryId'/>,<bean:write name='item' property='wrkInst'/>,<%= idx %>,'',<bean:write name='item' property='wrkInst'/>);"  >
			            Price &amp; Order
			           </a>
	         
			       </logic:equal>
			     </logic:equal>
		     </logic:notEqual>
		     <logic:equal name="permCat" property="isAvailableRightslink" value="true">
		       <logic:equal name="permCat" property="isPublicDomain" value="false">
		            <logic:equal name="permCat" property="isTitleLevelPermission" value="true">
		            <a id="pr_<%= idx %>" class="btn-price" onclick="addToCart('TitleLevelPermission',<bean:write name='permCat' property='categoryId'/>,'<bean:write name='item' property='wrkInst'/>',<%= idx %>,'',<bean:write name='item' property='wrkInst'/>);" >
		            Price &amp; Order
		           </a>
		            </logic:equal>
		            <logic:equal name="permCat" property="isChapterLevelPermission" value="true">
		            <a id="pr_<%= idx %>" class="btn-price" onclick="addToCart('ChapterLevelPermission',<bean:write name='permCat' property='categoryId'/>,'<bean:write name='item' property='wrkInst'/>',<%= idx %>,'',<bean:write name='item' property='wrkInst'/>);" >
		            Price &amp; Order
		           </a>
		            </logic:equal>
		            <logic:equal name="permCat" property="isArticleLevelPermission" value="true">
		           <a id="pr_<%= idx %>" class="btn-price" onclick="addToCart('ArticleLevelPermission' ,<bean:write name='permCat' property='categoryId'/>,'<bean:write name='item' property='wrkInst'/>',<%= idx %>,'',<bean:write name='item' property='wrkInst'/>);" >
		            Price &amp; Order
		           </a> 
		            </logic:equal>
  
		       </logic:equal>
		     </logic:equal>
   
		<!-- END BUttons -->
		</logic:notEqual>
     
      <logic:equal name="permCat" property="isContactRHOrSpecialOrder" value="true">
             <logic:equal name="permCat" property="isSpecialOrder" value="true">                          	            
             		<logic:notEqual name="permCat" property="isRepublication" value="true">
            		 	<!-- temporary marker -->
                       	<a id="pr_<%= idx %>" class="btn-special" href="search.do?operation=addToCartSpecial&item=<bean:write name='item' property='wrkInst'/>&idx=<%= idx %>&perm=" >
                        	special order
                       	</a>
                	</logic:notEqual>
              </logic:equal>
	  	      <logic:equal name="permCat" property="integrates" value="false">
		        <logic:equal name="permCat" property="isContactRightsholder" value="true">
		          <logic:equal name="searchForm" property="teleSalesUp" value="true">
		            <a id="pr_<%= idx %>" class="btn-contact" href="javascript:openContactInfoByRH(<%=((PermissionCategoryDisplay)permCat).getRightsholderInst()%>);">
		              contact info
		            </a>
		         </logic:equal>
		       </logic:equal>
	   	     </logic:equal>               
           </logic:equal>   
           
           <!-- View Details/Hide Details link, but TOU radio buttons show under availability -->
       <logic:equal name="permCat" property="gotTerms" value="true">
         <br/>
       </logic:equal>
  
   </div> 


      <logic:equal name="permCat" property="isRepublication" value="true">
      
     
    	<logic:equal name="searchForm" property="integrates" value="true"> 
    	         <%
        //  This segment of code is for RIGHTSLINK integration.
        //  We need to define some dynamic bits to fill in the
        //  the content of the popups and such.  PermissionCategoryDisplay
        //  returns a null string if these items are not available.
    %>
 
         <bean:define id="rlLearnMoreTitle">Using RIGHTSLINK</bean:define>
		    <bean:define id="rlLearnMoreBody">
			    <p class="larger">
			        <%= ((SearchForm) searchForm).getRLinkLearnMore() %>
			    </p>
			    <p class="larger">
			        <a href="<%= ((SearchForm) searchForm).getPublisherURL() %>" target="_blank">Go to the publisher&#39;s site now &#62;&#62;</a>
			    </p>
		    </bean:define>
		
		    <bean:define id="rlOptionsTitle">RIGHTSLINK offers a variety of options for using content from <%= ((SearchForm) searchForm).getRLinkPublisherName() %></bean:define>
		    <bean:define id="rlOptionsBody">
			    <p class="larger">
			        <%= ((SearchForm) searchForm).getRLinkPermissionOptions() %>
			    </p>
   	 		</bean:define>
    	
    	 <!-- +++++++++++++++++++++ RIGHTSLINK INTEGRATION ++++++++++++++++++++ -->
    	  <a name="rightslink"></a>
    	 <div class="tab-add-block" style="width:676px">
							<div class="add-info">
								<h4>Can't find what you're looking for?</h4>

								<p><util:contextualHelp titleName="rlOptionsTitle" bodyName="rlOptionsBody" rollover="false"  rolloverOnly="true">More permission options</util:contextualHelp> are available on the publisher's site using our RIGHTSLINK service.</p>
							</div>
							<a  class="btn-go" href="<bean:write name='searchForm' property='publisherURL'/>" target="_blank">go</a>
							<div class="add-link">
								<p>Find your article at the Publisher's site now.</p>
								<span>New to using RIGHTSLINK?&nbsp;&nbsp;<util:contextualHelp titleName="rlLearnMoreTitle" bodyName="rlLearnMoreBody" rollover="false">Learn more</util:contextualHelp></span>

							</div>
					</div>	
    	 <!-- --------------------- RIGHTSLINK INTEGRATION --------------------- -->
    	 <div class="clearer"></div>&nbsp;
    	</logic:equal>  	
    	
      </logic:equal>
      

  </logic:equal> <!-- end of check for isValidCategory -->
  </logic:notEqual>
 </logic:iterate>
 
</logic:equal>

<script>
// default select each radio
$(document).ready(function() {
	setUpContextualHelpRollovers();
	});
</script>