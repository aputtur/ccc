<%@ page contentType="text/html;charset=windows-1252"
language="java"
import="java.io.*"
import="java.net.*"
import="java.lang.Boolean"
%>
<%@ page errorPage="/jspError.do" %>
<%--import="com.copyright.ccc.util.SSOProperties"
import="com.copyright.ccc.util.CCCPropsConfig"
%-->
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%--@ taglib uri="/WEB-INF/tld/taglibs-string.tld" prefix="str" --%>

<link href="/ccc/default.css" rel="stylesheet" type="text/css" />
<link href="/ccc/ccc-new.css" rel="stylesheet" type="text/css" />
<script src="/ccc/dropdown.js" type="text/javascript"></script>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>

<% 
String logoutLink = "/ccc/do/logout";
%>
<tiles:insert template="/dcs_tag_js.jsp"/>

  <bean:cookie id="firstName" name="firstNameCookie" value="Undefined"/>


<body class="cc2" onload="MM_preloadImages('/ccc/images/nav_business_on.gif','/ccc/images/nav_academic_on.gif','/ccc/images/nav_publishers_on.gif','/ccc/images/nav_authors_on.gif','/ccc/images/nav_partners_on.gif','/ccc/images/nav_home_on.gif')">
<div id="wrapper">

<!-- Begin Header -->
<a href="/"><img src="/media/images/CCC_logo3_RGB_72dpi.jpg" alt="Copyright Clearance Center" title="Copyright Clearance Center" style="float: left;margin: 4px 0 6px 8px;" /></a>

<div id="ecom-nav">
 <span id="welcome">Welcome</span> <a href="#">Log in</a> | <a href="CC-ID1+2" class="icon-cart">Cart</a> | <a href="#">Manage Account</a> | <a href="#">Help</a>
</div>
</div>

<div id="navigation">
<table border="0" cellpadding="0" cellspacing="0" id="menu">

<tr>

<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('business','','/ccc/images/nav_business_on.gif',1)" onmouseout="MM_swapImgRestore()"><img src="/ccc/images/nav_business.gif" alt="Business" name="business" width="105" height="34" border="0" id="business" /></a>
<table style="display: none;" border="0" id="menu_business" class="menu" width="150" cellpadding="5" cellspacing="0" >
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Licensing Services</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Rightsphere</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Are You Licensed?</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Verify Titles Under Annual License</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Purchase Permission</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Tools for Business</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('academic','','/ccc/images/nav_academic_on.gif',1)" onmouseout="MM_swapImgRestore()"><img src="/ccc/images/nav_academic.gif" alt="Academic" name="academic" width="117" height="34" border="0" id="academic" /></a>
    <table style="display: none" border="0" id="menu_academic" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Licensing Services</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Partners</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Are You Licensed?</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Verify Titles Under Annual License</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Purchase Permission</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Tools for Academia</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('publishers','','/ccc/images/nav_publishers_on.gif',1)" onmouseout="MM_swapImgRestore()"><img src="/ccc/images/nav_publishers.gif" alt="Publishers" name="publishers" width="128" height="34" border="0" id="publishers" /></a>
<table style="display: none" border="0" id="menu_publishes" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Licensing Services</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Rightslink</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Grant Permission Online</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Manage Titles &amp; Fees</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Compliance &amp; Education</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">International Licensing</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('authors','','/ccc/images/nav_authors_on.gif',1)" onmouseout="MM_swapImgRestore()"><img src="/ccc/images/nav_authors.gif" alt="Authors" name="authors" width="106" height="34" border="0" id="authors" /></a>
<table style="display: none" border="0" id="menu_authors" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Licensing Services</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Beyond The Book</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Grant Permission Online</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Compliance &amp; Education</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('partners','','/ccc/images/nav_partners_on.gif',1)" onmouseout="MM_swapImgRestore()"><img src="/ccc/images/nav_partners.gif" alt="Partners" name="partners" width="113" height="34" border="0" id="partners" /></a>
<table style="display: none" border="0" id="menu_partners" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Partner Directory</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Become a Partner</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Integration Technology</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('copyright','','/ccc/images/nav_home_on.gif',1)" onmouseout="MM_swapImgRestore()"><img src="/ccc/images/nav_home.gif" alt="Copyright Central" name="copyright" width="180" height="34" border="0" id="copyright" /></a>
<table style="display: none" border="0" id="menu_copyrightcentral" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">News</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Using Copyright.com</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Tools &amp; Guidelines</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">CCC Newsletters</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Articles &amp; Reports</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Register a Copyright</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Other Resources</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">FAQs</a></td></tr>
</table>
</div>
</td>

</tr>

</table>

<script>
definemenu();
</script>

</div>

<div id="titlebar" style="clear:both"><img src="/ccc/images/banner_advancedsearch2.jpg" alt="Copyright.com"  style="float:left;" />
<div id="titlesearch"><img src="/ccc/images/search_getpermission.gif" alt="Get Permission / Find Title"  title="Get Permission / Find Title" width="147" height="14" /><br />
 <form name="form1" id="form1" method="post" action="">
  <input id="titlesearchbox" value="Publication Title or ISBN/ISSN" name="textfield" type="text" class="input01" onfocus="if (this.value=='Publication Title or ISBN/ISSN') {this.value=''}" onblur="if (this.value=='') {this.value='Publication Title or ISBN/ISSN'}"  />
  <input name="imageField" type="image" id="titlesearchbutton" src="/ccc/images/search_go.gif" alt="Go" title="Go" width="25" height="19" border="0" />
 </form>
 <a href="SR-ID2">Advanced Search Options </a></div>
</div>
<!-- End Header -->

  
  
<!-- the following images must abut -->
  <logic:equal name="cccForm" property="currentSegment" value="generic">
     <script language="javascript" type="text/javascript" src="/WebPageLayout/js/navRolloversGeneric.js"></script>  
     
  </logic:equal>

  <logic:equal name="cccForm" property="currentSegment" value="BUS">
    <script language="javascript" type="text/javascript" src="/WebPageLayout/js/navRolloversBusiness.js"></script>
    
  </logic:equal>

  <logic:equal name="cccForm" property="currentSegment"   value="ACAD">
    <script language="javascript" type="text/javascript" src="/WebPageLayout/js/navRolloversAcademic.js"></script>
    
  </logic:equal>

  <logic:equal name="cccForm" property="currentSegment"  value="AUTH">
    <script language="javascript" type="text/javascript" src="/WebPageLayout/js/navRolloversAuthors.js"></script>
    
  </logic:equal>

  <logic:equal name="cccForm" property="currentSegment" value="PUB">
    <script language="javascript" type="text/javascript" src="/WebPageLayout/js/navRolloversPublisher.js"></script>
    
  </logic:equal>

  <logic:equal name="cccForm" property="currentSegment"  value="SERV">
    <script language="javascript" type="text/javascript" src="/WebPageLayout/js/navRolloversServiceProvider.js"></script>
    
  </logic:equal>


