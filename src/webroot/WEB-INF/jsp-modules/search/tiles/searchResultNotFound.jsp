<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ page errorPage="/jspError.do" %>
<!-- NOTE:  
	 This might have to be further broken out, or conditional statements added
	 because this basic form is also used in a full page version of the simple
	 search.  The differences are in the title image and button image.
-->
<script type="text/javascript">
    $(function() {
		$( "#tokens" ).autocomplete({ autoFocus: true });
		$( "#tokens" ).attr("autocomplete", "off");
	});
</script>

 <logic:notEmpty name="searchForm" property="searchInstead">
    <div style="margin: 3px;">
        <logic:equal name="searchForm" property="lastSearch" value="simple">
        <a href="search.do?operation=show&page=simple">
            <img src="/media/images/pointLeftCircle.gif" alt="basic" style="vertical-align:bottom" />
        </a>Return to basic search
        </logic:equal>
        <logic:notEqual name="searchForm" property="lastSearch" value="simple">
        <a href="search.do?operation=show&page=advanced">
            <img src="/media/images/pointLeftCircle.gif" alt="basic" style="vertical-align:bottom" />
        </a>Return to advanced search
        </logic:notEqual>
    </div>
 </logic:notEmpty>
 
<div class="clearer"></div>

<div id="ecom-content">

 <!-- Begin Left Content -->

<div id="ecom-content-tabs"><html:img srcKey="resource.image.linegrey" width="468" height="1" border="0" /></div>

<div id="ecom-leftcontent-search" class="tab-contentbox">
    
  <logic:notEmpty name="searchForm" property="searchInstead">
  <html:form action="/basicSearch.do?operation=go" styleId="form">
      <p><font size="2">Your search for <strong><bean:write name="searchForm" property="searchText" /></strong> found no matches.</font></p>
      <p><font size="2">Do you want to search one of the following instead?</font></p>
      <ul style="list-style:none;">
       <logic:iterate name="searchForm" property="searchInstead" id="suggestion" type="java.lang.String">
        <li><a href='javascript:doSuggestedSearch("<bean:write name="suggestion" />");'><bean:write name="suggestion" /></a></li>
       </logic:iterate>
      </ul>
      <div class="horiz-rule"></div>
      <p><font size="2"><strong>Still can't find what you are looking for?</strong></font></p>
      <ul>
          <li>Be sure you're searching by the publication title (e.g. Wall Street Journal), not an article title.</li>
          <li>Place a <a href="search.do?operation=addSpecialFromScratch">Special Order</a> and Copyright Clearance Center will work on your behalf to obtain permission</li>
          <li>If you are not able to find a specific non-US title or publisher, we may be authorized to provide permission even though it is not listed in our catalog.  See the <html:link href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/copying_non-us_works.html">countries</html:link> for which we have general authorization.</li>
      </ul>
  </html:form>
  </logic:notEmpty>
  
  <logic:empty name="searchForm" property="searchInstead">
  <div class="floatleft">
   <br />
   <h1>Search&nbsp;</h1>
  </div>

  <ul id="navlinks" class="floatright top">
   <li><html:link href="/content/cc3/en/tools/search_tips.html">View search tips</html:link></li>
   <li><a href="search.do?operation=addSpecialFromScratch">Can't find the publication you're looking for?</a></li>
  </ul>
 
  <br/><br/><br/><br/>
  <p>Enter all or part of the publication's title below or search by a standard number: ISBN or ISSN.</p>
  <br/><br/>
  <html:form action="/basicSearch.do?operation=go" styleId="form">
   <html:hidden name="searchForm" property="searchType" value="0" />
   <html:hidden name="searchForm" property="lastSearch" value="simple" />
   <html:hidden name="searchForm" property="all" value="on" />
   <p>
   <html:hidden name="searchForm" property="last" value="simple" />
   Publication Title or ISBN/ISSN:
   <logic:notEmpty name="searchForm" property="titleOrStdNo">
     <html:text name="searchForm" property="titleOrStdNo" size="30" maxlength="255" styleClass="normal" onfocus="if (this.value=='Publication Title or ISBN/ISSN') {this.value=''}" onblur="if (this.value=='') {this.value='Publication Title or ISBN/ISSN'}" styleId="tokens" onkeyup="autosuggestnosubmit(this.value, 'tokens')" />
    </logic:notEmpty>
    <logic:empty name="searchForm" property="titleOrStdNo">
     <html:text name="searchForm" property="titleOrStdNo" size="30" maxlength="255" styleClass="normal" value="Publication Title or ISBN/ISSN" onfocus="if (this.value=='Publication Title or ISBN/ISSN') {this.value=''}" onblur="if (this.value=='') {this.value='Publication Title or ISBN/ISSN'}" styleId="tokens" onkeyup="autosuggestnosubmit(this.value, 'tokens')" />
    </logic:empty>
   </p>
   <br/><br/>
   <div class="horiz-rule"></div>
   <html:link action="/search.do?operation=show&page=advanced">Advanced Search Options</html:link>
   <html:image srcKey="resource.button.search" alt="Search" align="right" border="0" />
  </html:form>
  <br /><br /><br />
  
  <table cellpadding="5" cellspacing="0" width="400" align="center">
   <tr>
    <td bgcolor="#CCCCCC">
     <p><font size="2">Your search for <strong><bean:write name="searchForm" property="searchText" /></strong> found no matches.</font></p>
     <p>Try these suggestions:</p>
     <ul>
      <li>Be sure you're searching by the publication title (e.g. Wall Street Journal), not an article title.</li>
      <li>Check the spelling of your search term(s)</li>
      <li><html:link href="/content/cc3/en/tools/search_tips.html"><font color="red">View search tips</font></html:link> for ways to optimize your search</li>
      <li>Place a <a href="search.do?operation=addSpecialFromScratch"><font color="red">Special Order</font></a> and Copyright Clearance Center will work on your behalf to obtain permission</li>
      <li>If you are not able to find a specific non-US title or publisher, we may be authorized to provide permission even though it is not listed in our catalog.  See the <html:link action="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/copying_non-us_works.html"><font color="red">countries</font></html:link> for which we have general authorization.</li>
     </ul>
    </td>
   </tr>
  </table>
  </logic:empty>
 </div>
</div>

<div id="ecom-rightcontent">
 <p class="smalltype icon-alert"><strong>Note:</strong> Copyright.com supplies<br />
  permissions but not the copyrighted<br />
  content itself.
 </p>
 

 <div class="calloutbox">
  <h2>Need More Information?</h2>
  <p>See an overview of our<br>services for:<br />
   <a href="#">Business Use</a><br />
   <a href="#">Academic Use</a><br />
  </p>

 </div>
</div>

<div class="clearer"></div>

<!-- Webtrends tags for capturing search results -->
    <META name="WT.oss" content='<bean:write name="searchForm" property="searchTextWT" />'>
    <META name="WT.oss_r" content="0">
    <META name="WT.z_osstype" content='<bean:write name="searchForm" property="searchTypeWT" />'>
    
    <logic:notEqual name="searchForm" property="searchTypeWT" value="Basic">
    	<META name="WT.srchpub" content='<bean:write name="searchForm" property="selectedPubTypesWT" />'>
    	<META name="WT.srchperm" content='<bean:write name="searchForm" property="selectedPermissionTypesWT" />'>
    	<META name="WT.srchcty" content='<bean:write name="searchForm" property="selectedCountriesWT" />'>
    	<META name="WT.srchlang" content='<bean:write name="searchForm" property="selectedLanguagesWT" />'>
    	</logic:notEqual>
<!-- end Webtrends tags -->