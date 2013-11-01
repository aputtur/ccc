<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ page errorPage="/jspError.do" %>

<script type="text/javascript" language="javascript">
    $(function() {
        $( "#tokens" ).autocomplete({ autoFocus: true });
        $( "#tokens" ).attr("autocomplete", "off");
    });
</script>
<!-- NOTE:  
	 This might have to be further broken out, or conditional statements added
	 because this basic form is also used in a full page version of the simple
	 search.  The differences are in the title image and button image.
-->

<div class="clearer"></div>

<div id="ecom-content">

 <!-- Begin Left Content -->

<div id="ecom-content-tabs"><html:img srcKey="resource.image.linegrey" width="468" height="1" border="0" /></div>
<div id="ecom-leftcontent-search" class="tab-contentbox"> 
  <div class="floatleft">
   <br />
   <h1>Search&nbsp;</h1>
  </div>

  <ul id="navlinks" class="floatright top">
   <li><html:link href="/content/cc3/en/tools/search_tips.html">View search tips</html:link></li>
   <li><html:link href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/copying_non-us_works.html">Need to copy a non-U.S. work?</html:link></li>
  </ul>
 
  <br/><br/><br/><br/>
  <p>Enter all or part of the publication's title below or search by a standard number: ISBN or ISSN.</p>
  <br/><br/>
  <script type="text/javascript">
  function setCursor()
  {
      document.body.style.cursor='wait';
      return true;
  }
  </script>
  <html:form action="/basicSearch.do?operation=go" styleId="form" onsubmit="setCursor()">
   <p>
    <html:hidden name="searchForm" property="searchType" value="0" />
    <html:hidden name="searchForm" property="lastSearch" value="simple" />
    <html:hidden name="searchForm" property="all" value="on" />
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
   <html:link action="search.do?operation=show&page=advanced">Advanced Search Options</html:link>
   <html:image srcKey="resource.button.search" alt="Search" align="right" border="0" styleId="999" />
  </html:form>
</div>
</div>
<div id="ecom-rightcontent">
 <p class="smalltype icon-alert"><strong>Note:</strong> Copyright.com supplies<br />
  permissions but not the copyrighted
  content itself.
 </p>

 <div class="calloutbox">
  <h2>Need More Information?</h2>
  <p>See an overview of our<br>services for:<br />
   <ul id="navlinks">
   <li><html:link action="/viewPage.do?pageCode=bu12">Business Use</html:link></li>
   <li><html:link action="/viewPage.do?pageCode=ac14">Academic Use</html:link></li>
   </ul>
  </p>
 </div>
</div>

<div class="clearer"></div>
<script type="text/javascript">
document.getElementsByName("titleOrStdNo")[1].focus();
</script>