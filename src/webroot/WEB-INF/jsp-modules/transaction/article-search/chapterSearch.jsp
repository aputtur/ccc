<%@ taglib prefix="bean" uri="/WEB-INF/tld/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/tld/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/tld/struts-logic.tld" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
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

<%-- chapter search --%>

<jsp:useBean id="chapterSearchActionForm" scope="session" class="com.copyright.ccc.web.forms.ChapterSearchActionForm"/>
<html:form action="/chapterSearch.do?operation=chapterSearch" styleId="form" onsubmit="setCursor()">
<html:hidden name="chapterSearchActionForm" property="hostIDno"/>

 <div class="item-details">
  <h2><bean:write name="chapterSearchActionForm" property="selectedPublication.mainTitle"/></h2>
  <tiles:insert page="/WEB-INF/jsp-modules/search/tiles/publication.jsp"/>
  <div class="clearer"></div>
 </div>
 <div class="horiz-rule"></div>
  <table style="width: 100%;">
        <tbody><tr>
        	<td style="width: 175px;">
                <h2 class="floatleft"><strong>Permission type selected: </strong></h2>
            </td>
            <td>
            	<h2 style="margin-top: 11px;">
                    <span class="normaltype">
            	<bean:write name="chapterSearchActionForm" property="selectedCategory"/>
            	</span>
            </h2>
            </td>
        </tr>
        <tr>
        	<td style="width: 145px;">
                <h2 class="floatleft"><strong>Type of use selected: </strong></h2>
            </td>
            <td>
            	<h2 style="margin-top: 11px;">
                    <span class="normaltype">
            	<bean:write name="chapterSearchActionForm" property="selectedTou" />
            	</span>
            </h2>
            </td>
        </tr>
    </tbody></table>

 
   <p class="clearer">
                <a href="search.do?operation=detail&item=<bean:write name='chapterSearchActionForm' property='selectedPublication.wrkInst'/>&selectedTou=<bean:write name="chapterSearchActionForm" property="selectedTou" filter="true"/>" class="icon-back">Select different permission</a>
     </p>
 
 
  <p class="clearer"><strong> Search for the chapter you want to use from:</strong> <bean:write name="chapterSearchActionForm" property="selectedPublication.mainTitle"/></p>
  <table style="width:720px;">
   	<col style="width:100px">
	<col style="width:355px">
	<col style="width:145px">
   <tr>
      <td>Chapter title: </td>
      <td><html:text name="chapterSearchActionForm" property="chapterTitle" style="width:400px"/></td> 
      <td><input type="image" align="left" alt="Search" id="999" src="/media/images/btn_search.gif" name=""></td>
      <td></td> 
   </tr>
   <tr>
      <td>Chapter author:</td>
      <td><html:text name="chapterSearchActionForm" property="chapterAuthor" style="width:400px"/></td>
      <td align="right">(last name, first name)</td>
      <td></td>
   </tr>
   <tr>
      <td>DOI: </td>
      <td><html:text name="chapterSearchActionForm" property="chapterIDno" style="width:60%"/></td>
      <td><a href="noValidationChapterSearch.do?operation=clearParameters" >
	          <img align="left" src="/media/images/btn_clear.gif" alt="Clear"  border="0" />
	       </a></td>
      <td>	 
           
      </td>
   </tr>
  </table>
  </html:form>
<br/>

 <div style="border-width:1px;border-style:solid">
 <div class="search-result-options floatleft">
 <logic:notEmpty name="chapterSearchActionForm" property="searchParameters">
  <logic:greaterThan name="chapterSearchActionForm" property="count" value="1">
  <label style="padding-left:0px">Sort results by:</label>
  <html:form action="noValidationChapterSearch.do?operation=sort" styleId="form">
   <html:select name="chapterSearchActionForm" property="sort" size="1"  onchange="submit()">
    <html:optionsCollection name="chapterSearchActionForm" property="sortOptions" label="text" value="value"/>
   </html:select> 
  </html:form>
  </logic:greaterThan>

  <logic:greaterThan name="chapterSearchActionForm" property="count" value="6">
  <label>Results per page:</label>
  <html:form action="noValidationChapterSearch.do?operation=perPage" styleId="form">
   <html:select name="chapterSearchActionForm" property="pageSize" size="1" onchange="submit()">
    <html:optionsCollection name="chapterSearchActionForm" property="pageOptions" label="text" value="value" />
   </html:select>
  </html:form>
    
  </logic:greaterThan>

 <br/><br/><h1 class="floatleft clearer">Results:  <span class="subtitle">
 <logic:lessThan name="chapterSearchActionForm" property="resultCountPageSizeCompare" value="1">
    <logic:lessThan name="chapterSearchActionForm" property="count" value="1">
        <bean:write name="chapterSearchActionForm" property="count" />
    </logic:lessThan>
    <logic:greaterThan name="chapterSearchActionForm" property="count" value="0">
 	   <bean:write name="chapterSearchActionForm" property="lowItem" /> 
 	</logic:greaterThan>
 	 to <bean:write name="chapterSearchActionForm" property="count" /> of <bean:write name="chapterSearchActionForm" property="count" />
 </logic:lessThan>
 <logic:greaterEqual name="chapterSearchActionForm" property="resultCountPageSizeCompare" value="1">
 	<bean:write name="chapterSearchActionForm" property="lowItem" /> to <bean:write name="chapterSearchActionForm" property="highItem" />
 	  of <bean:write name="chapterSearchActionForm" property="count" />
 </logic:greaterEqual>
  
  <logic:greaterThan name="chapterSearchActionForm" property="count" value="0"> for 
  <logic:notEmpty name="chapterSearchActionForm" property="chapterTitle">
  <strong>Title: </strong>
  	<bean:write name="chapterSearchActionForm" property="chapterTitle"/>
  </logic:notEmpty> 
  <logic:notEmpty name="chapterSearchActionForm" property="chapterAuthor">
    <b>Author: </b>
    <bean:write name="chapterSearchActionForm" property="chapterAuthor"/>
  </logic:notEmpty>
  <logic:notEmpty name="chapterSearchActionForm" property="chapterIDno">
    <b>IDNO: </b>
    <bean:write name="chapterSearchActionForm" property="chapterIDno"/>
  </logic:notEmpty>
 </logic:greaterThan>
   </span></h1>
  
 </logic:notEmpty>
 
 </div>
 
   <div style="margin:7px 0;float:right">
   <logic:notEqual name="chapterSearchActionForm" property="pageRangeTotal" value="1">
      <logic:equal name="chapterSearchActionForm" property="isLess" value="yes">
         <a href="noValidationChapterSearch.do?operation=prev">Prev</a> 
      </logic:equal>
   <logic:iterate name="chapterSearchActionForm" property="pageRange" id="pp" indexId="index">
    <% if (index.intValue() > 0) { %>
            ,
    <% } %>
    <logic:equal name="pp" value="<%= chapterSearchActionForm.getPageNo() %>">
      <logic:greaterThan name="pp" value="0">
        <bean:write name="pp" />
      </logic:greaterThan>    
    </logic:equal>
    <logic:notEqual name="pp" value="<%= chapterSearchActionForm.getPageNo() %>">
       <logic:greaterThan name="pp" value="0">
           <a href="noValidationChapterSearch.do?operation=toPage&pno=<bean:write name='pp' />"><bean:write name="pp" /></a>
       </logic:greaterThan> 
    </logic:notEqual>
   </logic:iterate>
      <logic:equal name="chapterSearchActionForm" property="isMore" value="yes">
          <a href="noValidationChapterSearch.do?operation=next"> Next</a>
      </logic:equal>
  </logic:notEqual>

  </div>
  </div>
  
<logic:notEmpty name="chapterSearchActionForm" property="chapters">
 <div class="clearer"></div>
<div style="border-width:0px;border-style:solid;"></div>
</logic:notEmpty>
<logic:iterate name="chapterSearchActionForm" property="chapters" id="chapter" indexId="index">
  <bean:define id="number"><%= index.intValue() + chapterSearchActionForm.getLowItem() %>.</bean:define>
  <div class="search-result-basic">
  
    <table style="width:100%">
    <col width="20px"/><col /><col width="175px"/>
  <tr><td><h2><bean:write name="number" /></h2></td><td><h2><bean:write name="chapter" property="mainTitle"/></h2></td>
  <td style="width:175px;text-alignment:right;">
   <h2>  <a href="noValidationChapterSearch.do?operation=continuePurchase&chapterIndex=<%= index %>">
           <img src="/media/images/btn_continue.gif" alt="Continue" class="floatright"/>
      </a>
      </h2>
  </td></tr>
    </table>
  
  <div class="item-list-details"  style="padding-left: 25px;">
	  <p>
		 <strong> Author(s): </strong><bean:write name="chapter" property="authorName"/><br/>
		 <logic:notEmpty name="chapter" property="idnoTypeCd" >
		    <strong><bean:write name="chapter" property="idnoTypeCd" />: </strong><bean:write name="chapter" property="idno"/><br/>
		 </logic:notEmpty>
		 <strong> Date: </strong><bean:write name="chapter" property="runPubStartDate"/><br/>
		
	  </p>
	  <p>
		  <logic:notEmpty name="chapter" property="itemPageRange">
		      <strong> Page Range: </strong><bean:write name="chapter" property="itemPageRange"/><br/>
		  </logic:notEmpty>   
		  <logic:empty name="chapter" property="itemPageRange">  
		     <logic:notEmpty name="chapter" property="itemStartPage">
		        <strong> Start Page: </strong><bean:write name="chapter" property="itemStartPage"/><br/>
		     </logic:notEmpty>   
		     <logic:notEmpty name="chapter" property="itemEndPage">
		        <strong> End Page: </strong><bean:write name="chapter" property="itemEndPage"/><br/>
		     </logic:notEmpty>   	  
		  </logic:empty> 
		  <logic:notEmpty name="chapter" property="country">
		      <strong> Country: </strong><bean:write name="chapter" property="country"/><br/>
		  </logic:notEmpty>  
		  <logic:notEmpty name="chapter" property="country">
		     <strong>       Language: </strong><bean:write name="chapter" property="language"/><br/>
		  </logic:notEmpty>
	  </p>
	  <div class="clearer"></div>
  </div>
</div>
</logic:iterate>

<div style="float:right">
 <logic:notEqual name="chapterSearchActionForm" property="pageRangeTotal" value="1">
    <logic:equal name="chapterSearchActionForm" property="isLess" value="yes">
       <a href="noValidationChapterSearch.do?operation=prev">Prev</a> 
    </logic:equal>
 <logic:iterate name="chapterSearchActionForm" property="pageRange" id="pp" indexId="index">
  <% if (index.intValue() > 0) { %>
          ,
  <% } %>
  <logic:equal name="pp" value="<%= chapterSearchActionForm.getPageNo() %>">
    <logic:greaterThan name="pp" value="0">
        <bean:write name="pp" />
    </logic:greaterThan>
  </logic:equal>
  <logic:notEqual name="pp" value="<%= chapterSearchActionForm.getPageNo() %>">
       <logic:greaterThan name="pp" value="0">
           <a href="noValidationChapterSearch.do?operation=toPage&pno=<bean:write name='pp' />"><bean:write name="pp" /></a>
       </logic:greaterThan> 
  </logic:notEqual>
 </logic:iterate>
  <logic:equal name="chapterSearchActionForm" property="isMore" value="yes">
     <a href="noValidationChapterSearch.do?operation=next"> Next</a>
  </logic:equal>
</logic:notEqual>

</div>
