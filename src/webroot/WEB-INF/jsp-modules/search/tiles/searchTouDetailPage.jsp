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

<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/contextualHelp.js"/>" type="text/javascript"></script>
<link href="<html:rewrite page="/resources/commerce/css/contextualHelp.css"/>" rel="stylesheet" type="text/css" />
<script src="<html:rewrite page="/resources/commerce/js/contentLoader.js"/>" type="text/javascript"></script>

<script type="text/javascript">//<![CDATA[
// Add custom parameters here.
//_tag.DCSext.param_name=param_value;
_tag.WT.si_n="RLSO";
_tag.WT.si_x="1";
//]]>
</script>


<bean:define id="item" name="searchForm" property="selectedItem" />
<bean:define id="wrkInst" name="searchForm" property="selectedItem.wrkInst"/>


<tiles:insert page="/WEB-INF/jsp-modules/search/tiles/permissionSummaryHeader.jsp"/>

 <bean:define id="wrkInstValue" name="item" property="wrkInst"/>

<logic:equal name="searchForm" property="isBiactive" value="true">
<div class="horiz-rule"></div>
<div style="margin-bottom:10px;margin-top:10px;">
 <strong>Permissions for this title vary by date.</strong><br/>
 <strong>Please enter the publication year of the work you want to use: </strong><input type="text"  name="searchForm" property="selectedPubYear" id="biactivePubYear_${wrkInstValue}" onkeypress="javascript:checkSubmit(event,'permOptions',<bean:write name='item' property='wrkInst'/>);" size="4" maxlength="4"/>
      <img src="/resources/commerce/images/btn-go.png" style="cursor:pointer;vertical-align:bottom;width:33px;height:24px" alt="pub year" border="0"  onclick="processBiactiveYear('permOptions',<bean:write name='item' property='wrkInst'/>);" />
  </div>
</logic:equal>
 
 <logic:equal name="searchForm" property="isBiactive" value="false">
     <tiles:insert page="/WEB-INF/jsp-modules/search/tiles/permissionOptionsPage.jsp"/>
 </logic:equal>
 <logic:equal name="searchForm" property="isBiactive" value="true">
    <div id="permOptions_<bean:write name='item' property='wrkInst'/>">
    <logic:notEmpty  name="searchForm" property="selectedPubYear">
      <tiles:insert page="/WEB-INF/jsp-modules/search/tiles/permissionOptionsPage.jsp"/>
      </logic:notEmpty>
    </div>
  </logic:equal>



    <%
        if (!pageContext.getRequest().isSecure()) {
        
    %>
    <script src="http://books.google.com/books/previewlib.js" type="text/javascript"></script>

    <script type="text/javascript">
        // The bibkeys we'll use to query this book
        // E.g., 'OXFORD:600089058', 'ISBN:0253215994', 'LCCN:99043362'
		<bean:define id="item" name="searchForm" property="selectedItem" />
		var bibkeys = ["ISBN:" + "<bean:write name="item" property="idnoWop"/>"];

        //alert("Item#" + "<bean:write name="item" property="wrkInst"/>");
		// Retrieve the info for these bibkeys

		var api_url = "http://books.google.com/books?jscmd=viewapi&bibkeys=" +  bibkeys.join();
		document.write(unescape("%3Cscript src=" + api_url + " type='text/javascript'%3E%3C/script%3E"));

		function showBookSearchLinkIfAvailable()
		{
            if (typeof(_GBSBookInfo) != "undefined") {
                for (var i = 0; i < bibkeys.length; i++) {
			        bookInfo = _GBSBookInfo[bibkeys[i]];
			        if (!bookInfo) continue;
                    var href = bookInfo.preview_url;

                    // alert("Book Preview: " + bookInfo.preview);

                    // Add the button to the preview page when it's available
                	if (bookInfo.preview == "full" || bookInfo.preview == "partial")
                    {
			            document.getElementById("previewbutton").style.display = "block";
                        WT.ad="GBS";
                        WT.pn_item = "<bean:write name="item" property="wrkInst"/>" ;
                        WT.z_issn = "<bean:write name="item" property="mainIDNo"/>" ;

				        break;
                    }
                    else
                    {
                        WT.ad="NoGBS";
                    }
                }
            }
            else
            {
                WT.ad="NoGBS";
            }
        }

        function goPopup(lnk) {
            //alert("URL: " + lnk);
            //openPopup(lnk);
            //window.open(lnk, 'Google Preview', 'width=400, scrollbars=no');

            var load = window.open(lnk,'','scrollbars=yes,menubar=no,height=660,width=487,resizable=yes,toolbar=no,location=no,status=no');
        }
  function checkSubmit(e, elem, wrkInst)
  {
	  if (e && e.keyCode == 13)
	  {
		  processBiactiveYear(elem, wrkInst);
	  }
  }
        
function processBiactiveYear(elem, wrkInst) {
      var item = document.getElementById(elem+"_"+wrkInst);
      var pubYear = document.getElementById("biactivePubYear_"+wrkInst).value;
      var firstDigit = pubYear.substring(0,1);
      if (isNaN(pubYear) || pubYear.length != 4 || (firstDigit != "1" && firstDigit != "2")) {
       alert("Please enter a valid 4 digit year");
       return;
      }
      $('#permOptions_${wrkInstValue}').html("");
      var jqitem = '#'+item.id;
      $(jqitem).load("search.do?operation=processOptions&com.copyright.isajax=true",{selectedWrkInst:wrkInst,selectedPubYear:pubYear});
}
        
    </script>

    <script type="text/javascript">
        showBookSearchLinkIfAvailable();
    </script>
    <%
        }
    %>