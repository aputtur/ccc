<%@ page contentType="text/html;charset=windows-1252"  language="java" %>
<%@ page import="com.copyright.workbench.util.StringUtils2"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<script language="JavaScript" type="text/JavaScript">

$(function() {
	titleSearchBoxLabel='Publication Title or ISBN/ISSN';
	initBasicSearchField();
});

function initBasicSearchField() {
	field = document.getElementById("titlesearchbox");
	if (field!=null) {
		if (field.value==titleSearchBoxLabel) {
			field.style.color="#999999";		
		} else if (field.value=='') {
			field.style.color="#999999";		
			field.value=titleSearchBoxLabel;
		} else {
			field.style.color="black";
		}
	}
}
function searchFieldGotFocus(field) {
	if (field.value==titleSearchBoxLabel) {
		field.value='';
	}
	field.style.color="black";
}
function searchFieldLostFocus(field) {
	if (field.value=='') {
		field.style.color="#999999";
		field.value=titleSearchBoxLabel;
	}	
}
function doSuggestedSearch(title)
{
    frm = document.getElementById("form");
    frm.titleOrStdNo.value = title;
    frm.submit();
}
function autosuggest(term, wid)
{
	var query = "q=" + escape(term);
    var url = "/autosuggest.do?" + query;

    $.getJSON(url, function(terms)
    {
        // iterate over terms
        
        var list = new Array();
        
        if (terms.response != null && terms.response.docs != null) {
            for(var i=0; i < terms.response.docs.length; i++)
            {
                var trm = terms.response.docs[i];
                list[i] = trm.title;
            }
            $( '#' + wid ).autocomplete({source: list, select: function(event, ui) { 
                doSuggestedSearch(ui.item.value);
            }});
        }
    });
}
function autosuggestnosubmit(term, wid)
{
	var query = "q=" + escape(term);
    var url = "/autosuggest.do?" + query;

    $.getJSON(url, function(terms) 
    {
        // iterate over terms
        
        var list = new Array();
        
        if (terms.response != null && terms.response.docs != null) {
            for(var i=0; i < terms.response.docs.length; i++)
            {
                var trm = terms.response.docs[i];
                list[i] = trm.title;
            }
            $( '#' + wid ).autocomplete({source: list});
        }
    });
}
$(function() {
    // $( "#titlesearchbox" ).autocomplete({ autoFocus: true });
    $( "#titlesearchbox" ).attr("autocomplete", "off");
});
</script>

<div id="titlesearch"><img src="<html:rewrite href="/media/images/search_getpermission.gif"/>" alt="Get Permission / Find Title"  title="Get Permission / Find Title" width="147" height="14" /><br />
 <html:form action="/basicSearch.do?operation=go" styleId="form">
  <html:hidden name="searchForm" property="searchType" value="0" />
  <html:hidden name="searchForm" property="lastSearch" value="simple" />
  <html:hidden name="searchForm" property="all" value="on" />
  <html:text name="searchForm" property="titleOrStdNo" maxlength="255" styleClass="input01" 
    onfocus="searchFieldGotFocus(this)" onblur="searchFieldLostFocus(this)" styleId="titlesearchbox"
    onkeyup="autosuggest(this.value, 'titlesearchbox')" /> 
  <input name="imageField" type="image" id="titlesearchbutton" src="<html:rewrite href="/media/images/search_go.gif"/>" alt="Go" title="Go" width="25" height="19" border="0" />
 </html:form>
 <html:link action="/search.do?operation=show&page=advanced">Advanced Search Options</html:link>
</div>
