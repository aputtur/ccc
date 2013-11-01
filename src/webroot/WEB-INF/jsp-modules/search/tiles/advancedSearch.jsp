<%@ page import="com.copyright.ccc.web.WebConstants" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>

<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>
<%@ page errorPage="/jspError.do" %>

<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/add_bookmark.advsearch.js"/>" type="text/javascript"></script>

<script type="text/javascript" language="javascript">
    //  Change our cursor when search is hit...

    function setCursor()
    {
        document.body.style.cursor='wait';
        return true;
    }
    
    function toggleDropdown(cls, elem) {
		var item = document.getElementById(elem + "_span");

		if (item.style["display"] != "block") {
			item.style["display"] = "block";
			cls.innerHTML = cls.innerHTML.replace("[+] View details", "[-] Hide details");
			cls.style["color"] = "#FF0000";
		}
		else {
			item.style["display"] = "none";
			cls.innerHTML = cls.innerHTML.replace("[-] Hide details", "[+] View details");
			cls.style["color"] = "#02387A";
		}
	}
	
	function clear_fields() {
	}

	function getSelectedPermissions() {
		var checkedPermCats = $('input[id^="permCat-"]');
		var checkedStr="";
		for(var i=0; i < checkedPermCats.length; i++) {
			var ckbox = checkedPermCats[i];
			var cked = checkedPermCats[i].checked;
			if (cked) {
				checkedStr = checkedStr+ckbox.name+",";	
			}
		}
		checkedStr=checkedStr.slice(0,-1);	
		return checkedStr;	
	}
    $(function() {
        // $( "#tokens" ).autocomplete({ autoFocus: true });
        $( "#tokens" ).attr("autocomplete", "off");
        $( "#tokens" ).focus();
    });
</script>

<style type="text/css">
    li { color: black; font-size: 11px; }
    a.viewdetail { font-size: smaller; }
</style>

<% if(request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO).equals("1")){ %>
 <div style="margin: 3px;">
 	<static:importURL url='<%= CC2Configuration.getInstance().getCemproHeaderURL() %>'  />
 </div>
 <% } %>

<jsp:useBean id="searchForm" scope="session" class="com.copyright.ccc.web.forms.SearchForm"/>
 <div style="margin: 3px;"><a href="search.do?operation=show&page=simple">
    <img src="/media/images/pointLeftCircle.gif" alt="basic" style="vertical-align:bottom" />
 </a>Return to basic search</div>
<div class="clearer"></div>
<div id="ecom-content">
 <div id="ecom-leftcontent-search">
  <div class="floatleft">
   <br />
   <h1>Advanced Search</h1>
  </div>

  <ul id="navlinks" class="floatright top">
   <li><html:link href="/content/cc3/en/tools/search_tips.html">View search tips</html:link></li>
   <li><a href="/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/copying_non-us_works.html">Need to copy a non-U.S. work?</a></li>
  </ul>

  <br/><br/><br/>
  <html:form action="/advancedSearch.do?operation=go" styleId="form" onsubmit="setCursor()">
   <html:hidden name="searchForm" property="searchType" value="1" />
   <html:hidden name="searchForm" property="lastSearch" value="advanced" />
   <html:hidden name="searchForm" property="aas" value="on" />
   <html:hidden name="searchForm" property="dra" value="on" />
   <html:hidden name="searchForm" property="ars" value="on" />
   <div class="clearer"></div>
   <div class="float:left">
   <table>
    <tr>
     <td width="25">&nbsp;</td>
     <td valign="middle">Publication Title or ISBN/ISSN:</td>
     <td>
        <html:text name="searchForm" property="titleOrStdNo" styleClass="normal"
            styleId="tokens" onkeyup="autosuggestnosubmit(this.value, 'tokens')" 
        />
     </td>
     <td><html:image srcKey="resource.button.search" alt="Search" align="right" border="0" styleId="999" /></td>
    </tr>
    <tr><td colspan="4"><h2>Additional Criteria: </h2></td></tr>
    <tr>
     <td width="25">&nbsp;</td>
     <td valign="middle">Series Name:</td>
     <td><html:text name="searchForm" property="series" styleClass="normal" /></td>
     <td>	
         <a href="clearAdvancedSearch.do?operation=clearParameters" >
	      <img src="/media/images/btn_clear.gif" alt="Clear"  border="0" />
	     </a>
      </td>
    </tr>
    <tr>
     <td width="25">&nbsp;</td>
     <td valign="middle">Author (books only) or Editor:</td>
     <td><html:text name="searchForm" property="authorEditor" styleClass="normal" /></td>
     <td>&nbsp;</td>
    </tr>
    <tr>
     <td width="25">&nbsp;</td>
     <td valign="middle">Publisher:</td>
     <td><html:text name="searchForm" property="publisher" styleClass="normal" /></td>
     <td>&nbsp;</td>
    </tr>
   </table>
</div>
   <div class="horiz-rule"></div>

<table border = "0">
   <tr><td style="padding-right:10px">

   <table border="0">
   <tr><td colspan="2">
      <strong>Limit your results:</strong>
   <br/><span class="smalltype">(Use Ctrl-click to select multiples)<br/><br/></span>
   </td></tr>
    <tr>
      <td align="right" width="60">Publication Type:</td>
      <td>
        <!-- dropdown of pub types -->
        <html:select multiple="true" name="searchForm" property="selectedPubTypes" size="4" style="width: 125px;">
            <logic:iterate name="searchForm" property="publicationTypes" id="pubType" type="java.lang.String">
                <option value="<%= pubType %>" title="<%= pubType %>" <%= searchForm.getIsSelected(searchForm.getSelectedPubTypes(), pubType) %>><bean:write name="pubType"/></option>
            </logic:iterate>
        </html:select>
      </td>
   </tr>
   <tr>
      <td align="right" width="60">Country of Publication:</td>
      <td>
        <!-- dropdown of countries -->
        <html:select multiple="true" name="searchForm" property="selectedCountries" size="4" style="width: 125px;">
            <logic:iterate name="searchForm" property="countries" id="cntry" type="java.lang.String">
                <option value="<%= cntry %>" title="<%= cntry %>" <%= searchForm.getIsSelected(searchForm.getSelectedCountries(), cntry) %>><bean:write name="cntry"/></option>
            </logic:iterate>
        </html:select>
      </td>
    </tr>
   <tr>
      <td align="right" width="60">Language:</td>
      <td>
        <!-- dropdown of languages -->
        <html:select multiple="true" name="searchForm" property="selectedLanguages" size="4" style="width: 125px;">
            <logic:iterate name="searchForm" property="languages" id="lang" type="java.lang.String">
                <option value="<%= lang %>" title="<%= lang %>" <%= searchForm.getIsSelected(searchForm.getSelectedLanguages(), lang) %>><bean:write name="lang"/></option>
            </logic:iterate>
        </html:select>
      </td>
   </tr>
   </table>
   
     </td>
     <td>
     <table style="border-left-style:solid; border-width:1px;border-color:#CCCCCC;">   
     <tr><td style="padding-left: 10px;">
     <strong>Pay-Per-Use Display Options:</strong>
   <p style="width: 270px; word-wrap: break-word;">Show the options for the following permission types on the Result List: </p>
   <table border="0" style="width: 270px; word-wrap: break-word;">
   <tbody id="permCatCheckboxes">
    <tr>
     <td><a name="ecc"></a><html:checkbox styleId="permCat-ecc" name="searchForm" property="ecc"/></td>
     <td>Use in electronic course materials. <util:contextualHelp helpId="3" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>
   
    <tr>
     <td width="10"><a name="aps"></a><html:checkbox styleId="permCat-aps" name="searchForm" property="aps" /></td>
     <td width="260">Use in print course materials. <util:contextualHelp helpId="1" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>

    <tr>
     <td><a name="trsIll"></a><html:checkbox styleId="permCat-trsi" name="searchForm" property="trsIll" /></td>
     <td>Deliver via Interlibrary Loan (ILL) or document delivery. <util:contextualHelp helpId="2a" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>
    
    <tr>
     <td><a name="trsPhoto"></a><html:checkbox styleId="permCat-trsp" name="searchForm" property="trsPhoto" /></td>
     <td>Photocopy for general business use or academic use. <util:contextualHelp helpId="2" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>

    <tr>
     <td><a name="dps"></a><html:checkbox styleId="permCat-dps" name="searchForm" property="dps" /></td>
     <td>Share content electronically. <util:contextualHelp helpId="4" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>

    <tr>
     <td><a name="rls"></a><html:checkbox styleId="permCat-rls" name="searchForm" property="rls" /></td>
     <td>Republish or display content. <util:contextualHelp helpId="5" rollover="true">More&hellip;</util:contextualHelp></td>
    </tr>

    <tr>
      <td>&nbsp;</td>
      <td><b>Note: </b>Annual License options display on the Result List by default.</td>
    </tr>
	</tbody>
   </table>
   <table border="0" width="300">
    <tr>
    <td><p id="addBookmarkContainer" style="width: 270px; word-wrap: break-word;"></p></td>
    <td></td>
    </tr>
   </table>
   </td>
   </tr>
     </table>
     
     
     </td>
    </tr>
   </table>

   <div class="horiz-rule"></div>
   <table style="float:right">
     <tr>
      <td>
         <a href="clearAdvancedSearch.do?operation=clearParameters" >
	      <img src="/media/images/btn_clear.gif" alt="Clear"  border="0" />
	     </a>
      </td>
      <td>
         <html:image srcKey="resource.button.search" alt="Search" border="0" styleId="999" />
	   </td>
	  </tr>
	</table>
  </html:form>
 </div>
</div>

<% if(request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO).equals("0")){ %>
<div id="ecom-rightcontent">
<table><tr><td>
 <p class="smalltype icon-alert">
  <strong>Note:</strong> Copyright.com supplies<br />
  permissions but not the copyrighted
  content itself.
 </p>
</td></tr>
<tr><td>
    <iframe frameborder="0" scrolling="no" align="right" src ="/advsearch/advsearch-ppu.html" width="195" height="100%">
      <p>Your browser does not support iframes.</p>
    </iframe>
</td></tr>
 <tr><td>
 <p class="smalltype icon-alert">
  <strong>Note:</strong>
   Limiting your results by country and language
   is useful when searching for publications
   covered by the Multinational Copyright
   License.  For more information, see the
   <a href="/content/cc3/en/toolbar/productsAndSolutions/annualLicenseBusiness/multinational_copyrightlicensestitlecoverage.html">Multinational Copyright Licenses Title Coverage Page</a>.
 </p>
</td></tr>
</table>
</div>
<% } %>

<% if(request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO).equals("1")){ %>
<div id="ecom-rightcontent">
 	<static:importURL url='<%= CC2Configuration.getInstance().getCemproRightURL() %>'  />
</div>
<% } %>

<div class="clearer"></div>
<script type="text/javascript">
// document.getElementsByName("titleOrStdNo")[1].focus();
// document.getElementsByName("titleOrStdNo")[1].setAttribute("autocomplete", "off");
</script>
<!-- POPUP -->
<script language="javascript" type="text/javascript">
   // popup_new_site_message();
</script>