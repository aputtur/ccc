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

<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>

<jsp:useBean id="searchForm" scope="session" class="com.copyright.ccc.web.forms.SearchForm"/>
<html:link action="search.do?operation=show&page=simple" styleClass="back-link" style="float:left;">Return to basic search</html:link>

<div class="clearer"></div>
<div id="ecom-content">
 <div id="ecom-content-tabs"><html:img srcKey="resource.tab.payperuseon" alt="Pay-per-use Options" width="135" height="21" border="0" /><html:link action="search.do?operation=show&page=annual"><html:img srcKey="resource.tab.businessoff" alt="Annual License Coverage" width="164" height="21" border="0" /></html:link><html:link action="search.do?operation=show&page=academic"><html:img srcKey="resource.tab.academicoff" alt="Annual License Coverage" width="169" height="21" border="0" /></html:link></div>
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
    <script language="javascript">
        function toggleAll(frm) {
            if (frm.ppu.checked == true) {
                frm.aps.checked = true;
                frm.ecc.checked = true;
                frm.dps.checked = true;
                frm.trs.checked = true;
                frm.rls.checked = true;
            } else {
                frm.aps.checked = false;
                frm.ecc.checked = false;
                frm.dps.checked = false;
                frm.trs.checked = false;
                frm.rls.checked = false;
            }
        }
        function toggleAps(frm) {
            if (frm.aps.checked == false) {
                frm.ppu.checked = false;
            }
        }
        function toggleDps(frm) {
            if (frm.dps.checked == false) {
                frm.ppu.checked = false;
            }
        }
        function toggleEcc(frm) {
            if (frm.ecc.checked == false) {
                frm.ppu.checked = false;
            }
        }
        function toggleRls(frm) {
            if (frm.rls.checked == false) {
                frm.ppu.checked = false;
            }
        }
        function toggleTrs(frm) {
            if (frm.trs.checked == false) {
                frm.ppu.checked = false;
            }
        }
    </script>
  <html:form action="/ppuSearch.do?operation=go" styleId="form" onsubmit="setCursor()">
   <html:hidden name="searchForm" property="searchType" value="1" />
   <html:hidden name="searchForm" property="lastSearch" value="ppu" />
   
   <table class="pertype"> 
    <tr>
     <td colspan="2"> <h2>Complete one or both of the following fields: </h2></td>
    </tr>
    <tr>
     <td>Publication Title or ISBN/ISSN:</td>
     <td><html:text name="searchForm" property="titleOrStdNo" styleClass="normal" /></td>
    </tr>
    <tr>
     <td class="smalltype">and / or</td>
     <td>&nbsp;</td>
    </tr>
    <tr>
     <td>Publisher:</td>
     <td><html:text name="searchForm" property="publisher" styleClass="normal" /></td>
    </tr>
   </table>

   <h2>Additional search criteria:</h2>
   <p>Permission Type: <span class="smalltype">(choose at least one)</span> <util:contextualHelp helpId="20" rollover="false" styleClass="smalltype">What are these?</util:contextualHelp></p>

   <table border="0">
    <tr>
     <td><html:checkbox name="searchForm" property="ppu" onclick="toggleAll(this.form)" /></td>
     <td>All permission types</td>
    </tr>

    <tr>
     <td><html:checkbox name="searchForm" property="aps" onclick="toggleAps(this.form)" /></td>
     <td>Photocopy for academic coursepacks, classroom handouts. <util:contextualHelp helpId="1" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>

    <tr>
     <td><html:checkbox name="searchForm" property="trs" onclick="toggleTrs(this.form)" /></td>
     <td>Photocopy for general business use, library reserves, ILL/document delivery. <util:contextualHelp helpId="2" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>

    <tr>
     <td><html:checkbox name="searchForm" property="ecc" onclick="toggleEcc(this.form)" /></td>
     <td>Posting e-reserves, course management systems, e-coursepacks. <util:contextualHelp helpId="3" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>

    <tr>
     <td><html:checkbox name="searchForm" property="dps" onclick="toggleDps(this.form)" /></td>
     <td>Use in e-mail, intranet/extranet/Internet postings. <util:contextualHelp helpId="4" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>

    <tr>
     <td><html:checkbox name="searchForm" property="rls" onclick="toggleRls(this.form)" /></td>
     <td>Republish into a book, journal, newsletter. <util:contextualHelp helpId="5" rollover="true">More&hellip;</util:contextualHelp> </td>
    </tr>
   </table>
   <script language="javascript">
   if (document.getElementsByName("aps")[0].checked &&
       document.getElementsByName("dps")[0].checked &&
       document.getElementsByName("ecc")[0].checked &&
       document.getElementsByName("rls")[0].checked &&
       document.getElementsByName("trs")[0].checked)
   {
       document.getElementsByName("ppu")[0].checked = true;
   }
   </script>

   <span class="smalltype indent-3">Are you an annual license holder? If so, check publication coverage for<br /></span>
   <span class="smalltype indent-3"><html:link action="search.do?operation=show&page=annual">business</html:link> or <html:link action="search.do?operation=show&page=academic">academic</html:link> use.</span>
   <p></p>
   <table>
    <tr>
     <td style="vertical-align:middle;">Author (Books only) or Editor: <span class="smalltype">(optional)</span></td>
     <td><html:text name="searchForm" property="authorEditor" styleClass="normal" /></td>
    </tr>
   </table>
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
</div>
<br></br><br></br>
<div>
<iframe frameborder="0" scrolling="no" align="right" src ="/advsearch/advsearch-ppu.html" width="195" height="441">
  <p>Your browser does not support iframes.</p>
</iframe>
</div>
<div class="clearer"></div>
<script type="text/javascript">
document.getElementsByName("titleOrStdNo")[1].focus();
</script>
<!-- POPUP -->
<script language="javascript" type="text/javascript">
   // popup_new_site_message();
</script>