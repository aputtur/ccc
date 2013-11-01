<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page errorPage="/jspError.do" %>

<script type="text/javascript" language="javascript">
//  Change our cursor when search is hit...

function setCursor()
{
    document.body.style.cursor='wait';
    return true;
}

</script>

<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />

<html:link action="search.do?operation=show&page=simple" styleClass="back-link" style="float:left;">Return to basic search</html:link>
<div class="clearer"></div>
<div id="ecom-content">
 <div id="ecom-content-tabs"><html:link action="search.do?operation=show&page=ppu"><html:img srcKey="resource.tab.payperuseoff" alt="Pay-per-use Options" width="135" height="21" border="0" /></html:link><html:img srcKey="resource.tab.businesson" alt="Annual License Business" width="164" height="21" border="0" /><html:link action="search.do?operation=show&page=academic"><html:img srcKey="resource.tab.academicoff" alt="Annual License Academic" width="169" height="21" border="0" /></html:link></div>

 <!-- Begin Left Content -->

 <div id="ecom-leftcontent-search" class="tab-contentbox"> 
  <div class="floatleft">
  <br />
  <h1>Search</h1>
 </div>

 <ul id="navlinks" class="floatright top">
  <li><html:link href="/content/cc3/en/tools/search_tips.html">View search tips</html:link></li>
  <li><html:link action="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/copying_non-us_works.html">Need to copy a non-U.S. work?</html:link></li>
 </ul>

 <br/><br/><br/>
    
 <html:form action="/businessSearch.do?operation=go" styleId="form" onsubmit="setCursor()">
  <html:hidden name="searchForm" property="searchType" value="1" />
  <html:hidden name="searchForm" property="lastSearch" value="annual" />

  <table class="tblewidth">
   <tr>
    <td colspan="2"> <h2>Complete one or both of the following fields: </h2></td>
   </tr>

   <tr>
    <td style="vertical-align:middle;">Publication Title or ISBN/ISSN:</td>
    <td><html:text name="searchForm" property="titleOrStdNo" styleClass="normal" /></td>
   </tr>

   <tr>
    <td class="smalltype">and / or</td>
    <td>&nbsp;</td>
   </tr>

   <tr>
    <td style="vertical-align:middle;">Publisher:</td>
    <td><html:text name="searchForm" property="publisher" styleClass="normal" /></td>
   </tr>
  </table>

  <h2>Additional search criteria:</h2>
  <p>Permission Type: <span class="smalltype">(choose at least one) <util:contextualHelp helpId="21" rollover="false" styleClass="smalltype">What are these?</util:contextualHelp></span></p>
  <table border="0">
   <tr>
    <td><html:checkbox name="searchForm" property="aas" /></td>
    <td>Photocopy and share with co-workers. <util:contextualHelp helpId="6" rollover="true">More&hellip;</util:contextualHelp></td>
   </tr>

   <tr>
    <td><html:checkbox name="searchForm" property="dra" /></td>
    <td>E-mail to co-workers or post to an intranet. <util:contextualHelp helpId="7" rollover="true">More&hellip;</util:contextualHelp></td>
   </tr>
  </table>

  <span class="smalltype indent-3">Need to share information <util:contextualHelp helpId="22" rollover="false" styleClass="smalltype">outside your organization</util:contextualHelp>? Use our <html:link action="search.do?operation=show&page=ppu">Pay-per-use services</html:link></span>
  <p></p>
  <table>
   <tr>
    <td>Author (Books only) or Editor: <span class="smalltype">(optional)</span></td>
    <td><html:text name="searchForm" property="authorEditor" styleClass="normal" /></td>
   </tr>
  </table>

  <p>Do you have a Multinational License?&nbsp;&nbsp;<html:link action="/content/cc3/en/toolbar/productsAndSolutions/annualLicenseBusiness/multinational_copyrightlicensestitlecoverage.html" target="Exceptions">Please review title coverage</html:link>.</p>
  <div class="horiz-rule"></div>
  <html:image srcKey="resource.button.search" alt="Search" align="right" border="0" styleId="999" />
 </html:form>

</div>
</div>
<div id="ecom-rightcontent">
 <p class="smalltype icon-alert">
  <strong>Note:</strong> Copyright.com supplies<br />
  permissions but not the copyrighted
  content itself.
 </p>
<div>
<iframe frameborder="0" scrolling="no" align="right" src ="/advsearch/advsearch-business.html" width="195" height="355">
  <p>Your browser does not support iframes.</p>
</iframe>
</div>
</div>
<div class="clearer"></div>
<script type="text/javascript">
document.getElementsByName("titleOrStdNo")[1].focus();
</script>
<!-- POPUP -->
<script language="javascript" type="text/javascript">
   // popup_new_site_message();
</script>