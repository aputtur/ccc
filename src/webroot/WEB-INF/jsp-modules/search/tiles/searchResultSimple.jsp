<%@ page language="java" %>
<%@ page errorPage="/jspError.do" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ page import="com.copyright.ccc.business.data.Publication" %>

<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />

<script type="text/javascript">
function go(lnk) {
  openPopup(lnk, "request_coverage", "width=480,height=700,scrollbars,resizable");
}
</script>

<jsp:useBean id="searchForm" scope="session" class="com.copyright.ccc.web.forms.SearchForm"/>
<html:hidden name="searchForm" property="showPermissionTypes" value="false"/>

<bean:define id="termsBody">null</bean:define>
<bean:define id="termsTitle">Rightsholder Terms</bean:define>

<p class="smalltype icon-alert"><strong>Note:</strong> Copyright.com supplies permissions but not the copyrighted content itself.</p>

<!-- Begin Left Content -->

<div>
 <html:link action="search.do?operation=show&page=simple" styleClass="back-link floatleft">Back to search</html:link>
 <div class="search-result-options floatleft">
  <logic:greaterThan name="searchForm" property="count" value="1">
  <label>Sort results by:</label>
  <html:form action="search.do?operation=sort" styleId="form">
   <html:select name="searchForm" property="sort" size="1"  onchange="submit()">
    <html:optionsCollection name="searchForm" property="sortOptions" label="text" value="value"/>
   </html:select> 
  </html:form>
  </logic:greaterThan>

  <logic:greaterThan name="searchForm" property="count" value="6">
  <label>Results per page:</label>
  <html:form action="search.do?operation=perPage" styleId="form">
   <html:select name="searchForm" property="pageSize" size="1" onchange="submit()">
    <html:optionsCollection name="searchForm" property="pageOptions" label="text" value="value" />
   </html:select>
  </html:form>
  </logic:greaterThan>
 </div>

 <ul id="navlinks" class="floatright top">
  <li><html:link href="/content/cc3/en/tools/search_tips.html">View search tips</html:link></li>
  <li><a href="search.do?operation=addSpecialFromScratch">Can't find the publication you're looking for?</a></li>
 </ul>

 <div class="clearer"></div>

 <logic:equal name="searchForm" property="count" value="1">
  <h1 class="floatleft clearer">Results <span class="subtitle">1 match found for <strong><bean:write name="searchForm" property="searchText" /></strong></span></h1>
 </logic:equal>
 <logic:greaterEqual name="searchForm" property="count" value="1000">
  <h1 class="floatleft clearer">Results <span class="subtitle">Items <bean:write name="searchForm" property="lowItem" />-<bean:write name="searchForm" property="highItem" /> of top 1000 best matches for <strong><bean:write name="searchForm" property="searchText" /></strong> (<bean:write name="searchForm" property="count" /> total results found)</span></h1>
 </logic:greaterEqual>
 <logic:lessThan name="searchForm" property="count" value="1000">
  <logic:notEqual name="searchForm" property="count" value="1">
   <h1 class="floatleft clearer">Results <span class="subtitle">Items <bean:write name="searchForm" property="lowItem" />-<bean:write name="searchForm" property="highItem" /> of <bean:write name="searchForm" property="count" /> matches found for <strong><bean:write name="searchForm" property="searchText" /></strong></span></h1>
  </logic:notEqual>
 </logic:lessThan>
 <p class="floatright">
  <logic:equal name="searchForm" property="isLess" value="yes">
   <html:link action="search.do?operation=less">less</html:link>, 
  </logic:equal>

  <logic:notEqual name="searchForm" property="pageRangeTotal" value="1">
   <logic:iterate name="searchForm" property="pageRange" id="pp" indexId="index">
    <% if (index.intValue() > 0) { %>
	, 
    <% } %>
    <logic:equal name="pp" value="<%= searchForm.getPageNo() %>">
     <bean:write name="pp" />
    </logic:equal>
    <logic:notEqual name="pp" value="<%= searchForm.getPageNo() %>">
     <a href="search.do?operation=topage&pno=<bean:write name='pp' />"><bean:write name="pp" /></a>
    </logic:notEqual>
   </logic:iterate>
  </logic:notEqual>
  <logic:equal name="searchForm" property="isMore" value="yes">
    , <html:link action="search.do?operation=more">more</html:link>
  </logic:equal>
 </p>
 <!--
 <p class="greytype clearer">Frequently requested publications that match your search terms:</p>
 <p class="greytype">Additional publications that match your search terms:</p>
 -->
 <p class="greytype clearer"></p>
 <%
 pageContext.setAttribute("showFrequentText", "yes", PageContext.PAGE_SCOPE);
 pageContext.setAttribute("showAdditionalText", "no", PageContext.PAGE_SCOPE);
 %>
 <logic:iterate name="searchForm" property="results" id="item" indexId="index" type="com.copyright.ccc.business.data.Publication">
  <bean:define id="number"><%= index.intValue() + searchForm.getLowItem() %>.</bean:define>
  
  <logic:equal name="showFrequentText" value="yes">
   <logic:equal name="item" property="isFrequentlyRequested" value="true">
    <div class="search-result-basic">
     <p class="greytype" >Frequently requested publications that match your search terms:</p>
    </div>
    <% pageContext.setAttribute("showAdditionalText", "yes", PageContext.PAGE_SCOPE); %>
   </logic:equal>
   <% pageContext.setAttribute("showFrequentText", "no", PageContext.PAGE_SCOPE); %>
  </logic:equal>
  
  <logic:equal name="showAdditionalText" value="yes">
   <logic:equal name="item" property="isFrequentlyRequested" value="false">
    <div class="search-result-basic">
     <p class="greytype">Additional publications that match your search terms:</p>
    </div>
    <% pageContext.setAttribute("showAdditionalText", "no", PageContext.PAGE_SCOPE); %>
   </logic:equal>
  </logic:equal>
  
  <div class="search-result-wrapper">
    <hr />
    <div class="search-result-detail-wrapper">
      <div class="search-result-basic">
        <div class="search-result-title">
          <h2 class="floatleft" style="width: 490px;"><bean:write name="number" /> <a href="search.do?operation=detail&item=<bean:write name='item' property='wrkInst'/>&detailType=advancedDetail" > <bean:write name="item" property="mainTitle" /></a></h2>
        </div>
        <div class="item-list-details" style="clear: left;">
          <p>
<!-- Details of the work. -->
            <strong><bean:write name="item" property="idnoLabel"/>: </strong><bean:write name="item" property="mainIDNo"/><br/>
            <logic:notEqual name="item" property="publicationYearRange" value="Through present">
              <logic:notEqual name="item" property="publicationYearRange" value="-">
                <strong>Publication year(s): </strong><bean:write name="item" property="publicationYearRange"/><br/>
              </logic:notEqual>
            </logic:notEqual>
            <logic:notEmpty name="item" property="mainAuthor">
              <strong>Author/Editor: </strong>
              <bean:write name="item" property="mainAuthor"/>
              <br/>
            </logic:notEmpty>
            <logic:empty name="item" property="mainAuthor">
              <logic:notEmpty name="item" property="mainEditor">
                <strong>Author/Editor: </strong>
                <bean:write name="item" property="mainEditor"/>
                <br/>
              </logic:notEmpty>
            </logic:empty>
            <strong>Publication type: </strong><bean:write name="item" property="publicationType"/><br />
            <strong>Publisher: </strong><bean:write name="item" property="mainPublisher"/>
            <logic:notEmpty name="item" property="itemURL">
              <br /><br /><strong>URL: </strong><a href='<bean:write name="item" property="itemURL"/>' target="_blank"><bean:write name="item" property="itemURL"/></a>
            </logic:notEmpty>
          </p>
          <p>
            <logic:notEmpty name="item" property="volume">
              <strong>Volume: </strong><bean:write name="item" property="volume"/><br/>
            </logic:notEmpty>
            <logic:notEmpty name="item" property="edition">
              <strong>Edition: </strong><bean:write name="item" property="edition"/><br/>
            </logic:notEmpty>
            <logic:notEmpty name="item" property="pages">
              <strong>Pagination: </strong><bean:write name="item" property="pages"/><br/>
            </logic:notEmpty>
            <logic:notEmpty name="item" property="series">
              <strong>Series: </strong><bean:write name="item" property="series"/>
              <logic:notEmpty name="item" property="seriesNumber">
                ; <bean:write name="item" property="seriesNumber"/>
              </logic:notEmpty>
              <br/>
            </logic:notEmpty>
            <logic:notEmpty name="item" property="language">
              <strong>Language: </strong><bean:write name="item" property="language"/><br />
            </logic:notEmpty>
            <logic:notEmpty name="item" property="country">
              <strong>Country of publication: </strong><bean:write name="item" property="country"/>
            </logic:notEmpty>
          </p>
        </div>
      </div>
    </div>

<!-- License block: indicates annual license permissions. -->

    <table class="license-block" cellspacing="0" style="border: 1px solid #999; margin-top: 7px;">
      <caption align="bottom">Need an annual license? <a href="/content/cc3/en/toolbar/productsAndSolutions.html">Get info</a></caption>
      
      <logic:equal name="item" property="annualLicenseHelper.isBiactive" value="true">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" align="center" valign="middle" width="197">
            <a href="search.do?operation=detail&item=<bean:write name='item' property='wrkInst'/>&detailType=basic">Verify Coverage</a>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
      </logic:equal>
      <logic:equal name="item" property="annualLicenseHelper.isBiactive" value="false">
        <logic:equal name="item" property="annualLicenseHelper.isPublicDomain" value="true">
        <tr>
          <td colspan="2" align="center" valign="middle" width="197">
            <i>This work is in the public domain of the United States.  You may use the work without restriction.</i>
          </td>
        </tr>
        </logic:equal>
        <logic:equal name="item" property="annualLicenseHelper.isPublicDomain" value="false">
          <tr><th colspan="2">Annual License Holders:</th></tr>
          <tr>
            <td>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedAAS" value="true">
                <logic:equal name="item" property="annualLicenseHelper.hasTermsAAS" value="true">
                  <img src="<html:rewrite page="/resources/commerce/images/icon_green-check.png"/>">
                </logic:equal>
                <logic:equal name="item" property="annualLicenseHelper.hasTermsAAS" value="false">
                  <img src="<html:rewrite page="/resources/commerce/images/icon_green-check.png"/>">
                </logic:equal>
              </logic:equal>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedAAS" value="false">
                <img src="<html:rewrite page="/resources/commerce/images/icon_do-not.png"/>">
              </logic:equal>
            </td>
            <td>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedAAS" value="true">
                <util:contextualHelp helpId="60" rollover="true" styleId="<%= String.valueOf(index) %>">
                  Business License - Print
                </util:contextualHelp>
              </logic:equal>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedAAS" value="false">
                <util:contextualHelp helpId="63" rollover="true" styleId="<%= String.valueOf(index) %>">
                  Business License - Print
                </util:contextualHelp>
              </logic:equal>
            </td>
          </tr>
          <logic:equal name="item" property="annualLicenseHelper.isGrantedAAS" value="true">
            <logic:equal name="item" property="annualLicenseHelper.hasTermsAAS" value="true">
              <%
                pageContext.setAttribute("termsBody", item.getAnnualLicenseHelper().getTermsAAS(), PageContext.PAGE_SCOPE);
              %>
              <tr><td colspan="2" class="tsandcs">
                <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">TERMS</util:contextualHelp>
              </td></tr>
            </logic:equal>
          </logic:equal>
          <logic:equal name="item" property="annualLicenseHelper.isGrantedAAS" value="false">
            <tr><td colspan="2" class="tsandcs">
              <a id="pr_<%= index %>" href="javascript:go('makerequest.do?item=<bean:write name="item" property="wrkInst"/>&index=<%= index %>&perm=Photocopy%20and%20share%20with%20co-workers');">Request Coverage</a>
            </td></tr>
          </logic:equal>
          <tr>
            <td>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedDRA" value="true">
                <logic:equal name="item" property="annualLicenseHelper.hasTermsDRA" value="true">
                  <img src="<html:rewrite page="/resources/commerce/images/icon_green-check.png"/>">
                </logic:equal>
                <logic:equal name="item" property="annualLicenseHelper.hasTermsDRA" value="false">
                  <img src="<html:rewrite page="/resources/commerce/images/icon_green-check.png"/>">
                </logic:equal>
              </logic:equal>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedDRA" value="false">
                <img src="<html:rewrite page="/resources/commerce/images/icon_do-not.png"/>">
              </logic:equal>
            </td>
            <td>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedDRA" value="true">
                <util:contextualHelp helpId="61" rollover="true" styleId="<%= String.valueOf(index) %>">
                  Business License - Digital
                </util:contextualHelp>
              </logic:equal>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedDRA" value="false">
                <util:contextualHelp helpId="64" rollover="true" styleId="<%= String.valueOf(index) %>">
                  Business License - Digital
                </util:contextualHelp>
              </logic:equal>
            </td>
          </tr>
          <logic:equal name="item" property="annualLicenseHelper.isGrantedDRA" value="true">
            <tr>
                <logic:equal name="item" property="annualLicenseHelper.hasTermsDRA" value="true">
                 <td class="tsandcs" colspan=2>
                </logic:equal>
                <logic:equal name="item" property="annualLicenseHelper.hasTermsDRA" value="false">
                   <td class="drr" colspan=2> 
                </logic:equal>
                <logic:equal name="item" property="annualLicenseHelper.hasResponsiveRightDRA" value="true">
              	  <img src="<html:rewrite page="/resources/commerce/images/icon_circle_green_check.png"/>">
  	              <util:contextualHelp helpId="66" rollover="true" styleId="<%= String.valueOf(index) %>">Digital Responsive Rights&nbsp;</util:contextualHelp>
  	            </logic:equal>
                <logic:equal name="item" property="annualLicenseHelper.hasResponsiveRightDRA" value="false">
              	  <img src="<html:rewrite page="/resources/commerce/images/icon_circle_red_x.png"/>">
  	              <util:contextualHelp helpId="67" rollover="true" styleId="<%= String.valueOf(index) %>">Digital Responsive Rights&nbsp;</util:contextualHelp>
  	            </logic:equal>         

               <logic:equal name="item" property="annualLicenseHelper.hasTermsDRA" value="true">
                      <%
                  pageContext.setAttribute("termsBody", item.getAnnualLicenseHelper().getTermsDRA(), PageContext.PAGE_SCOPE);
                  %>
          	    <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">TERMS</util:contextualHelp>
  	            </td>
                </tr>
               </logic:equal>
               <logic:equal name="item" property="annualLicenseHelper.hasTermsDRA" value="false">
   	            </td>
                </tr>
               </logic:equal>
                <logic:equal name="item" property="annualLicenseHelper.hasResponsiveRightDRA" value="false">
                    <tr><td colspan="2" class="tsandcs">
                      <a id="pr_<%= index %>" href="javascript:go('makerequest.do?item=<bean:write name="item" property="wrkInst"/>&index=<%= index %>&perm=Digital%20Responsive%20Rights');">Request Coverage</a>
                    </td></tr>
                </logic:equal>
          </logic:equal>
          <logic:equal name="item" property="annualLicenseHelper.isGrantedDRA" value="false">
            <tr><td colspan="2" class="tsandcs">
              <a id="pr_<%= index %>" href="javascript:go('makerequest.do?item=<bean:write name="item" property="wrkInst"/>&index=<%= index %>&perm=E-mail%20to%20co-workers%20or%20post%20to%20an%20intranet');">Request Coverage</a>
            </td></tr>
          </logic:equal>
          <tr>
            <td>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedARS" value="true">
                <logic:equal name="item" property="annualLicenseHelper.hasTermsARS" value="true">
                  <img src="<html:rewrite page="/resources/commerce/images/icon_green-check.png"/>">
                </logic:equal>
                <logic:equal name="item" property="annualLicenseHelper.hasTermsARS" value="false">
                  <img src="<html:rewrite page="/resources/commerce/images/icon_green-check.png"/>">
                </logic:equal>
              </logic:equal>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedARS" value="false">
                <img src="<html:rewrite page="/resources/commerce/images/icon_do-not.png"/>">
              </logic:equal>
            </td>
            <td>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedARS" value="true">
                <util:contextualHelp helpId="62" rollover="true" styleId="<%= String.valueOf(index) %>">
                  Academic License - Digital/Print
                </util:contextualHelp>
              </logic:equal>
              <logic:equal name="item" property="annualLicenseHelper.isGrantedARS" value="false">
                <util:contextualHelp helpId="65" rollover="true" styleId="<%= String.valueOf(index) %>">
                  Academic License - Digital/Print
                </util:contextualHelp>
              </logic:equal>
            </td>
          </tr>
          <logic:equal name="item" property="annualLicenseHelper.isGrantedARS" value="true">
            <logic:equal name="item" property="annualLicenseHelper.hasTermsARS" value="true">
              <%
                pageContext.setAttribute("termsBody", item.getAnnualLicenseHelper().getTermsARS(), PageContext.PAGE_SCOPE);
              %>
              <tr><td colspan="2" class="tsandcs">
                <util:contextualHelp titleName="termsTitle" bodyName="termsBody" rollover="true">TERMS</util:contextualHelp>
              </td></tr>
            </logic:equal>
          </logic:equal>
          <logic:equal name="item" property="annualLicenseHelper.isGrantedARS" value="false">
            <tr><td colspan="2" class="tsandcs">
              <a id="pr_<%= index %>" href="javascript:go('makerequest.do?item=<bean:write name="item" property="wrkInst"/>&index=<%= index %>&perm=Photocopy%20or%20share%20content%20electronically');">Request Coverage</a>
            </td></tr>
          </logic:equal>
        </logic:equal>
      </logic:equal>
    </table>

    <div class="payPerUseOptionsButton" style="clear:left;">
      <a href="search.do?operation=detail&item=<bean:write name='item' property='wrkInst'/>&detailType=basic" >
        <html:img srcKey="resource.button.payperuseoptions" alt="Permission Options" />
      </a>
    </div>
  </div>
 </logic:iterate>

 <div class="horiz-rule"></div>
 <p class="floatleft">
  <logic:equal name="searchForm" property="count" value="1">
   1 match found for <strong><bean:write name="searchForm" property="searchText" /></strong>
  </logic:equal>
 <logic:greaterEqual name="searchForm" property="count" value="1000">
  <h1 class="floatleft clearer">Results <span class="subtitle">Items <bean:write name="searchForm" property="lowItem" />-<bean:write name="searchForm" property="highItem" /> of top 1000 best matches for <strong><bean:write name="searchForm" property="searchText" /></strong> (<bean:write name="searchForm" property="count" /> total results found)</span></h1>
 </logic:greaterEqual>
 <logic:lessThan name="searchForm" property="count" value="1000">
  <logic:notEqual name="searchForm" property="count" value="1">
   <h1 class="floatleft clearer">Results <span class="subtitle">Items <bean:write name="searchForm" property="lowItem" />-<bean:write name="searchForm" property="highItem" /> of <bean:write name="searchForm" property="count" /> matches found for <strong><bean:write name="searchForm" property="searchText" /></strong></span></h1>
  </logic:notEqual>
 </logic:lessThan>
 </p>
  <p class="floatright">
   <logic:equal name="searchForm" property="isLess" value="yes">
    <html:link action="search.do?operation=less">less</html:link>, 
   </logic:equal>
   <logic:notEqual name="searchForm" property="pageRangeTotal" value="1">
    <logic:iterate name="searchForm" property="pageRange" id="pp" indexId="index">
     <% if (index.intValue() > 0) { %>
	 , 
     <% } %>
     <logic:equal name="pp" value="<%= searchForm.getPageNo() %>">
      <bean:write name="pp" />
     </logic:equal>
     <logic:notEqual name="pp" value="<%= searchForm.getPageNo() %>">
      <a href="search.do?operation=topage&pno=<bean:write name='pp' />"><bean:write name="pp" /></a>
     </logic:notEqual>
    </logic:iterate>
   </logic:notEqual>
   <logic:equal name="searchForm" property="isMore" value="yes">
    , <html:link action="search.do?operation=more">more</html:link>
   </logic:equal>
  </p>
 <!-- </p> -->
 <div class="clearer"></div>

</div>

<div class="clearer"></div>

<!-- Webtrends tags for capturing search results -->
    <META name="WT.oss" content='<bean:write name="searchForm" property="searchText" />'>
    <META name="WT.oss_r" content='<bean:write name="searchForm" property="count" />'>
    <META name="WT.z_osstype" content='<bean:write name="searchForm" property="searchTypeWT" />'>
    
    <logic:notEqual name="searchForm" property="count" value="0">
    	<META name="WT.si_n" content="Checkout">
    	<META name="WT.si_x" content="1">
    </logic:notEqual>
    
<!-- end Webtrends tags -->
