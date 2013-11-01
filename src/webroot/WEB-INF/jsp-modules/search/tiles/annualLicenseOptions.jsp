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
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.Calendar" %>


<jsp:useBean id="searchForm" scope="session" class="com.copyright.ccc.web.forms.SearchForm"/>
<bean:define id="termsTitle">Rightsholder Terms</bean:define>
<bean:define id="emailTitle">Request This Work</bean:define>
<bean:define id="item" name="searchForm" property="selectedItem" />
<bean:define id="wrkInst" name="searchForm" property="selectedItem.wrkInst"/>

 <logic:equal name="item" property="isPublicDomainNotBiactive" value="false">
 <%
 pageContext.setAttribute("showAnnBusinessHeader", "yes", PageContext.PAGE_SCOPE);
 pageContext.setAttribute("showAnnAcademicHeader", "yes", PageContext.PAGE_SCOPE);
 pageContext.setAttribute("displayAnnMultinational", "no", PageContext.PAGE_SCOPE);
 
 String txtTermsApply = null;
 String selectedYear = null;
 boolean isResponsive = false;
 
 long currentTimeForAnn = System.currentTimeMillis();
 %>
 <logic:iterate name="searchForm" property="permCatDisplay" id="permCat" indexId="idx">
 <bean:define id="moreId">more_<%= idx.intValue() + 1 %></bean:define>
 <logic:equal name="permCat" property="isLicense" value="true">
  <logic:equal name="permCat" property="isValidCategory" value="true">
   <logic:equal name="permCat" property="isNonAcademicPhotoCopyLicense" value="true">
   <logic:match name="permCat" property="availability" location="start" value="Covered">
    <% pageContext.setAttribute("displayAnnMultinational", "yes", PageContext.PAGE_SCOPE); %>
   </logic:match>
  </logic:equal>

<%
    selectedYear = searchForm.getSelectedPubYear();

    if (selectedYear == null || selectedYear == "")
    {
        Calendar c = Calendar.getInstance();
        int x = c.get(Calendar.YEAR);
        selectedYear = String.valueOf(x);
    }
    isResponsive = ((PermissionCategoryDisplay) permCat).getIsResponsiveRightDRA(selectedYear);
%>
<bean:define id="isResponsiveRight"><%= isResponsive %></bean:define>
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

  <logic:equal name="searchForm" property="isBusiness" value="true">
  <logic:equal name="showAnnBusinessHeader" value="yes">
   <logic:equal name="permCat" property="isNonAcademicLicense" value="true">
      <% pageContext.setAttribute("showAnnBusinessHeader", "no", PageContext.PAGE_SCOPE); %>
	<strong style="font-size: 16px">Business <span class="subtitle"><em>Don&rsquo;t have an annual business license? Not sure? <a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/are_you_licensed.html">Find out more.</a></em></span></strong>
	<div class="clearer"></div>
	<br/>
	   <div class="title">
        <h4 style="font-size: 13px;">Permission type </h4>
        <h4 style="font-size: 13px;">Coverage</h4>
      </div>
   </logic:equal>
  </logic:equal>
  </logic:equal>
  <logic:equal name="searchForm" property="isBusiness" value="false">
   <logic:equal name="permCat" property="isNonAcademicLicense" value="true">
      <% pageContext.setAttribute("showAnnBusinessHeader", "no", PageContext.PAGE_SCOPE); %>     
   </logic:equal>
  </logic:equal>
  <bean:define id="categoryImg">
	<img width="19" height="19" class="align-left"  src="media/images/spacer.gif"/>
</bean:define>
  
  <logic:equal name="searchForm" property="isAcademic" value="true">
  <logic:equal name="showAnnAcademicHeader" value="yes">
    <logic:equal name="permCat" property="isAcademicLicense" value="true">
      <% pageContext.setAttribute("showAnnAcademicHeader", "no", PageContext.PAGE_SCOPE); %>
      <logic:equal name="showAnnBusinessHeader" value="no">
        <logic:equal name="displayAnnMultinational" value="yes">
          <span class="smalltype"><a href="/content/cc3/en/toolbar/productsAndSolutions/annualLicenseBusiness/multinational_copyrightlicensestitlecoverage.html" target="_blank">Multinational exceptions to our annual license agreement</a></span><br /><br />
        </logic:equal>
        <br/><br/>
          <div class="srpaysummary-btm"></div>
       <br/>
      </logic:equal>
        
        
    
      <strong style="font-size:16px">Academic <span class="subtitle"><em>Don&rsquo;t have an annual academic license? Not sure? <a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/are_you_licensed.html">Find out more.</a></em></span></strong>
       <div class="clearer"></div>
       <br/>
      <div class="title">
        <h4 style="font-size: 13px;">Permission type </h4>
        <h4 style="font-size: 13px;">Coverage</h4>
        
      </div>
   </logic:equal>
  </logic:equal>
  </logic:equal>
  
  <logic:equal name="searchForm" property="isAcademic" value="false">
    <logic:equal name="permCat" property="isAcademicLicense" value="true">
      <% pageContext.setAttribute("showAnnAcademicHeader", "no", PageContext.PAGE_SCOPE); %>    
    </logic:equal>
  </logic:equal>
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

 
  <logic:equal name="searchForm" property="isBusiness" value="false">
   <logic:equal name="permCat" property="isNonAcademicLicense" value="true">
      <% pageContext.setAttribute("showAnnBusinessHeader", "no", PageContext.PAGE_SCOPE); %>     
   </logic:equal>
  </logic:equal>

  
  <logic:equal name="searchForm" property="isAcademic" value="false">
    <logic:equal name="permCat" property="isAcademicLicense" value="true">
      <% pageContext.setAttribute("showAnnAcademicHeader", "no", PageContext.PAGE_SCOPE); %>    
    </logic:equal>
  </logic:equal>
  
  <bean:define id="tabIcon">
  
 
   <logic:equal name="permCat" property="availability" value="Not covered">
            tab-block
       </logic:equal>
       <logic:equal name="permCat" property="availability" value="Not Covered">
            tab-block
       </logic:equal>
       
       <logic:notEqual name="permCat" property="availability" value="Not Covered">
       <logic:notEqual name="permCat" property="availability" value="Not covered">
       tab-block-no
       </logic:notEqual>
       </logic:notEqual>
       
          
   </bean:define>
   <bean:define id="coverageWidth">
      <logic:equal name="permCat" property="availability" value="Not covered">
           200
       </logic:equal>
       <logic:equal name="permCat" property="availability" value="Not Covered">
        200
       </logic:equal>
       
       <logic:notEqual name="permCat" property="availability" value="Not Covered">
       <logic:notEqual name="permCat" property="availability" value="Not covered">
       300
       </logic:notEqual>
       </logic:notEqual>
   
   </bean:define>
<div class="<%=tabIcon%>">
    
     <div class="type">
<!-- TODO reintroduce biactive, after May, This logic would be moved to the TOU display or selection of date may eliminate bi-active display -->

        <strong style="width:300px;"><bean:write name="permCat" property="categoryDescription"/>
      
        <logic:equal name="permCat" property="isNonAcademicPhotoCopyLicense" value="true">
            <util:contextualHelp helpId="6" rollover="true" styleId="<%= moreId %>">More...</util:contextualHelp>
		</logic:equal>
        <logic:equal name="permCat" property="isNonAcademicEmailLicense" value="true">
            <util:contextualHelp helpId="7" rollover="true" styleId="<%= moreId %>">More...</util:contextualHelp>
		</logic:equal>
        <logic:equal name="permCat" property="isAcademicLicense" value="true">
            <util:contextualHelp helpId="8" rollover="true" styleId="<%= moreId %>">More...</util:contextualHelp>
        </logic:equal>
 
        </strong>
   </div>
 <!-- START AVAILABILITY SECTION -->     
      <div class="availability" id="<%=idx %>" style="width:<%=coverageWidth%>px">
     
       <logic:equal name="permCat" property="gotTerms" value="false"></logic:equal>
       <logic:equal name="permCat" property="isAvailable" value="true">
         <logic:notEqual name="permCat" property="isContactRHOrSpecialOrder" value="true">
            <span class="icon-check">
            <nobr>
	        <!-- TODO NEED TO DEAL WITH BIactive after May -->
	         <logic:equal name="permCat" property="isPublicDomain" value="true">
	<!--           <util:contextualHelp helpId="32" rollover="false" styleId="<%= String.valueOf(idx) %>">
	-->              <bean:write name="permCat" property="availability" />
	<!--           </util:contextualHelp>
	-->         </logic:equal>
	         <logic:notEqual name="permCat" property="isPublicDomain" value="true">
	            <logic:equal name="permCat" property="isNonAcademicPhotoCopyLicense" value="true">
	              <util:contextualHelp helpId="14a" rollover="false" styleId="<%= String.valueOf(idx) %>">
	                  <bean:write name="permCat" property="availability"  />
	              </util:contextualHelp>
	            </logic:equal>
	            <logic:equal name="permCat" property="isNonAcademicEmailLicense" value="true">
	              <util:contextualHelp helpId="14b" rollover="false" styleId="<%= String.valueOf(idx) %>">
	                  <bean:write name="permCat" property="availability" />
	              </util:contextualHelp>
	            </logic:equal>
	            <logic:equal name="permCat" property="isAcademicLicense" value="true">
	              <util:contextualHelp helpId="15" rollover="false" styleId="<%= String.valueOf(idx) %>">
	                  <bean:write name="permCat" property="availability" />
	              </util:contextualHelp>
	            </logic:equal>
	          </logic:notEqual>
	          <logic:equal name="permCat" property="gotTerms" value="true">
	               &nbsp;&nbsp;&nbsp;
                   <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">TERMS</util:contextualHelp>
            </logic:equal>
              </nobr>
	        </span>
	        <logic:equal name="permCat" property="isNonAcademicEmailLicense" value="true">
          <span style="padding: 2px 0px 0px 25px; display: block; margin: 0px; min-height: 17px; _height: 17px;">
	          <nobr>
             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          <logic:equal name="isResponsiveRight" value="true">
	              <util:contextualHelp helpId="66" rollover="true" styleId="<%= String.valueOf(idx) %>"><img src="<html:rewrite page="/resources/commerce/images/icon_circle_green_check.png"/>">&nbsp;Digital Responsive Rights</util:contextualHelp>
	          </logic:equal>
	          <logic:equal name="isResponsiveRight" value="false">
	              <util:contextualHelp helpId="67" rollover="true" styleId="<%= String.valueOf(idx) %>"><img src="<html:rewrite page="/resources/commerce/images/icon_circle_red_x.png"/>">&nbsp;Digital Responsive Rights</util:contextualHelp>
                &nbsp;&nbsp;&nbsp;<a id="pr_<%= idx %>" href="javascript:go('makerequest.do?item=<bean:write name="item" property="wrkInst"/>&idx=<%= idx %>&perm=Digital%20Responsive%20Rights');">Request Coverage</a>
	          </logic:equal>
            </nobr>
            </span>
	       </logic:equal>
	       
        </logic:notEqual> <!-- END OF IS NOT CONTACT RIGHTS HOLDER /SPECIAL ORDER -->
       </logic:equal>

        <logic:equal name="permCat" property="isNotAvailable" value="true">
            <logic:equal name="permCat" property="integrates" value="false">
                <!-- This publisher does not integrate with RIGHTSLINK -->
                <span class="icon-unavailable">
                <logic:equal name="permCat" property="availability" value="Not available">
                
                   	<util:contextualHelp helpId="10" rollover="false" styleId="<%= String.valueOf(idx) %>">
                        <bean:write name="permCat" property="availability" />
					 </util:contextualHelp>
					 </logic:equal>
				<logic:equal name="permCat" property="availability" value="Not covered">
                   	<util:contextualHelp helpId="13" rollover="false" styleId="<%= String.valueOf(idx) %>">
                       <bean:write name="permCat" property="availability" />
					 </util:contextualHelp>
					 </logic:equal>
					<logic:equal name="permCat" property="availability" value="Not Covered">
                   	<util:contextualHelp helpId="13" rollover="false" styleId="<%= String.valueOf(idx) %>">
                       <bean:write name="permCat" property="availability" />
					 </util:contextualHelp>
					 </logic:equal>
                </span>
            </logic:equal>

           <logic:equal name="permCat" property="integrates" value="true">
                <!-- This publisher integrates with RIGHTSLINK -->
                    <span class="icon-unavailable">
        <!-- TODO NEED TO DEAL WITH BIactive after May -->
				         <logic:notMatch name="permCat" property="availability" location="start" value="Available for Purchase">
				             <util:contextualHelp helpId="13" rollover="false" styleId="<%= String.valueOf(idx) %>">
				                 <bean:write name="permCat" property="availability" />
				             </util:contextualHelp>
				         </logic:notMatch>
                    </span>
                
            </logic:equal>
        </logic:equal>
        <!-- ADD SUPERSCRIPT TERMS -->
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
-->					  <bean:write name="permCat" property="availability" />
<!--  				    </util:contextualHelp>
-->				 </logic:equal>
				 <logic:notEqual name="searchForm" property="teleSalesUp" value="true">
				     Contact rightsholder directly
				 </logic:notEqual>
				</span>
			</logic:equal>
	  </logic:equal>
       
       <logic:equal name="permCat" property="isSpecialOrder" value="true">
        <span class="icon-available">
        <!-- TODO NEED TO DEAL WITH BIactive after May -->
          <util:contextualHelp helpId="11" rollover="false" styleId="<%= String.valueOf(idx) %>">
            <bean:write name="permCat" property="availability" />
          </util:contextualHelp>
        </span>
       </logic:equal>

<!-- ADD SUPERSCRIPT TERMS -->
      
      

        
 <!--  div class="clearer"></div-->
 <!-- Rights Qualifying statement -->
      <logic:equal name="permCat" property="gotRQualStmt" value="true">
        <logic:equal name="permCat" property="globalRightsQualifier" value="false">
         <div class="icon-alert">
          <strong><bean:write name="permCat" property="rightsQualifyingStatement"/></strong>
         </div>
        </logic:equal>
      </logic:equal>
      <logic:equal name="permCat" property="gotRQualStmt" value="false">
       
      </logic:equal>
     
              
 
      
<!-- REPLACE WITH get list of TOUs and display as radio buttons -->    
     
       
       <logic:equal name="permCat" property="isNonAcademicPhotoCopyLicense" value="true">
            <div id="nonAcademicLicensePhotoContentOdd<%= currentTimeForAnn %>" style="display:none">
               <p></p>
               <table>
                 <tr><td>- Paper handouts</td></tr>
                 <tr><td>- Fax</td></tr>
               </table>
            </div>		
        </logic:equal>
        <logic:equal name="permCat" property="isNonAcademicEmailLicense" value="true">
            <div id="nonAcademicLicenseEmailContentOdd<%= currentTimeForAnn %>" style="display:none">
               <p></p>
              <table>
               <tr><td>- Email</td></tr>
               <tr><td>- Intranet Postings</td></tr>
             </table>
            </div>		
        </logic:equal>
        <logic:equal name="permCat" property="isAcademicLicense" value="true">
            <div id="academicLicensePhotoContentOdd<%= currentTimeForAnn %>" style="display:none">
             <p></p>
             <table>
               <tr><td>- Print coursepacks</td></tr>
               <tr><td>- Classroom handouts</td></tr>
               <tr><td>- Electronic reserves</td></tr>
               <tr><td>- Course management systems</td></tr>
               <tr><td>- Institution Intranets</td></tr>
               <tr><td>- CD ROM/DVD</td></tr>
               <tr><td>- Other electronic academic uses</td></tr>
             </table>
            </div>       
        </logic:equal>     
        
<!-- End of display TOUs as radio buttons -->
       
       </div>
       
      <logic:equal name="permCat" property="isContactRHOrSpecialOrder" value="true">
             
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
         <logic:equal name="permCat" property="isNotAvailable" value="true">
		       <logic:equal name="permCat" property="isLicense" value="true">
		        <span style="float:right"><a id="pr_<%= idx %>" href="javascript:go('makerequest.do?item=<bean:write name="item" property="wrkInst"/>&idx=<%= idx %>&perm=<bean:write name="permCat" filter="true" property="categoryDescription"   />');">Request Coverage</a></span>
		       </logic:equal>
		      </logic:equal>
           
      
      
  
   </div> 
    <%
        //  This segment of code is for RIGHTSLINK integration.
        //  We need to define some dynamic bits to fill in the
        //  the content of the popups and such.  PermissionCategoryDisplay
        //  returns a null string if these items are not available.
    %>
    <bean:define id="rlLearnMoreTitle">Using RIGHTSLINK</bean:define>
    <bean:define id="rlLearnMoreBody">
    <p class="larger">
        <%= ((PermissionCategoryDisplay) permCat).getLearnMore() %>
    </p>
    <p class="larger">
        <a href="<%= ((PermissionCategoryDisplay) permCat).getPublisherURL() %>" target="_blank">Go to the publisher&#39;s site now &#62;&#62;</a>
    </p>
    </bean:define>

    <bean:define id="rlOptionsTitle">RIGHTSLINK offers a variety of options for using content from <%= ((PermissionCategoryDisplay) permCat).getPublisherName() %></bean:define>
    <bean:define id="rlOptionsBody">
    <p class="larger">
        <%= ((PermissionCategoryDisplay) permCat).getPermissionOptions() %>
    </p>
    </bean:define>
 
  </logic:equal> <!-- end of check for isValidCategory -->
  </logic:equal>
 </logic:iterate>
</logic:equal>
 




<script>

<logic:greaterThan name="searchForm" property="selectedRightHolderInst" value="0">
        //showContactRightsholder();
</logic:greaterThan>
setUpContextualHelpRollovers();
</script>