<%@ page contentType="text/html" %>
<%@ page language="java" %>
<%@ page errorPage="/jspError.do" %>

<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<div id="ecom-content-wrapper">

 <div class="clearer"></div>

  <div id="ecom-content">

   <div id="ecom-leftcontent-search" class="tab-contentbox"> 
    <div class="floatleft">
     <br />
     <h1>Search</h1>
    </div>
    <ul id="navlinks" class="floatright top">
     <li><a href="#">View search tips</a></li>
     <li><a href="search.do?operation=addSpecialFromScratch">Can't find the publication you're looking for?</a></li>
    </ul>
    <form>

<!-- LOGIC:  Based on type of search, include one of two tiles. -->

<tiles:insert attribute="searchFormSpecific"/>

<tiles:insert attribute="searchResultStatus"/>

<!-- END LOGIC: search type. -->

    </form>
   </div>
   <div id="ecom-rightcontent">
    <p class="smalltype icon-alert"><strong>Note:</strong> Copyright.com supplies<br />
     permissions but not the copyrighted<br />
     content itself.</p>
    <div class="calloutbox">
     <h2>Need More Information?
     </h2>
     <p>See an overview of our<br>services for:<br />
      <a href="#">Business Use</a><br />
      <a href="#">Academic Use</a><br />
     </p>
    </div>
   </div>
   <div class="clearer"></div>
  </div>

 <div class="clearer"></div>
</div>