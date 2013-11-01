
<%@ page errorPage="/jspError.do" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib prefix="oue" uri="/WEB-INF/tld/ouextensions.tld" %>
<!-- THIS NEEDS TO BE MOVED TO OWN JSP -->
<%
if (request.getParameter("backlink") == null) {
    pageContext.setAttribute("backtosearch", "yes", PageContext.PAGE_SCOPE);
} else {
    pageContext.setAttribute("backtosearch", "no", PageContext.PAGE_SCOPE);
}
%>

<!-- Begin Left Content -->

<bean:define id="landing" value="true" />
<bean:define id="item" name="searchForm" property="selectedItem" />

<p class="smalltype icon-alert"><strong>Note:</strong> Copyright.com supplies permission but not the copyrighted content itself.</p>

 <div class="floatleft">
  <logic:equal name="searchForm" property="source" value="normal">
   <h1>Permissions Summary</h1>
		<logic:equal name="searchForm" property="hasSearchResults" value="true">
			<logic:equal name="backtosearch" value="yes">
				<html:link action="search.do?operation=show&page=back"
					styleClass="icon-back">Back to results</html:link>
			</logic:equal>
		</logic:equal>
	</logic:equal>
  <logic:notEqual name="searchForm" property="source" value="normal">
   <h1>Get Copyright Permission</h1>
   <p>
    Copyright.com makes it easy for you to get permission to use copyrighted materials.<br>
    Simply review the permission types that are available below.  If you don't have an<br>
    Annual License, you can select "Price &amp; Order" below and purchase permission now.<br>
   </p>
  </logic:notEqual>
 </div>

 <ul id="navlinks" class="floatright top">
  <logic:notEqual name="searchForm" property="source" value="normal">
   <li><a href="search.do?operation=show&page=simple&start=1">Get permission for a different publication</a></li>
   <li><a href="http://support.copyright.com/index.php?action=category&id=16">Copyright FAQs</a></li>
  </logic:notEqual>
  <logic:equal name="searchForm" property="source" value="normal">
   <li><a href="search.do?operation=show&page=last&start=1">New search</a></li>
   <li><a href="search.do?operation=addSpecialFromScratch">Can't find the publication you're looking for?</a></li>
  </logic:equal>
 </ul>

<div class="clearer"></div>

<logic:equal name="searchForm" property="isSessionNew" value="true">
	<div style="color: red; padding-left: 10px; margin-right: 30%;">
		Your session has expired.
		<br>
		Please choose a type of use for this title or return to search to find
		another title
	</div>
	<br/>
</logic:equal>

<logic:equal name="searchForm" property="hasSearchResults" value="false">
	<a href="search.do?operation=show&page=simple&start=1" class="icon-back">Return to search</a> 
</logic:equal>

 <div class="horiz-rule"></div>
 
 <div class="item-details">


  <h2><bean:write name="item" property="mainTitle"/></h2>
     <div style="float: right; position: relative; right: 10px; top:20px">
    			<div style="background:white;display:none" align="right" id="previewbutton">
           	<a href="javascript:goPopup('googleBookPreview.do?isbn=<bean:write name="item" property="idnoWop"/>');">
                <html:img src="/media/images/perm-summ-gbs.gif" width="88" height="31" alt="Click for Google Book Search Preview" border="0" />
                </a>
                <br/>
                
            </div>
              <logic:equal name="searchForm" property="worldCatDisplay" value="true">
                <div style="background:white" align="right" id="previewbuttonWorldCat">
                    <a href="<bean:write name="searchForm" property="worldCatUrl"/>" target="_blank" >
                    <html:img src="/media/images/perm-summ-worldcat.gif" width="88" height="31" alt="Click on WorldCat to find in a library near you" border="0" />
                    </a>
                </div>
            </logic:equal>
  
  </div>
  
  <tiles:insert page="/WEB-INF/jsp-modules/search/tiles/publication.jsp"/>
 
  
 
  
  
  <div class="clearer"></div>
 </div>
 

 
<oue:renderDetails/>

<!-- END OF THIS NEEDS TO MOVED TO JSP  -->