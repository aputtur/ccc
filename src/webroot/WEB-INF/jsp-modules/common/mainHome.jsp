<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@ page errorPage="/jspError.do" %>

<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  
<%@ taglib uri="/WEB-INF/tld/cc2-security.tld" prefix="security" %>  

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>CCC - Commerce Sample</title>

<link href="<html:rewrite page="/resources/commerce/css/default.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/resources/commerce/css/ccc-new.css"/>" rel="stylesheet" type="text/css" />

<script src="<html:rewrite page="/resources/commerce/js/dropdown.js"/>" type="text/javascript"></script>

<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  //var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  //var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
  // if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
</head>

<body class="cc2 home" onload="MM_preloadImages('<html:rewrite href="/media/images/nav_business_on.gif"/>','<html:rewrite href="/media/images/nav_academic_on.gif" />','<html:rewrite href="/media/images/nav_publishers_on.gif" />','<html:rewrite href="/media/images/nav_authors_on.gif" />','<html:rewrite href="/media/images/nav_partners_on.gif" />','<html:rewrite href="/media/images/nav_home_on.gif" />','<html:rewrite href="/media/images/home_splash-photo-business.jpg"/>','<html:rewrite href="/media/images/home-splash-academic-on.gif" />','<html:rewrite href="/media/images/home_splash-photo-academic.jpg"/>','<html:rewrite href="/media/images/home-splash-copyright-on.gif" />','<html:rewrite href="/media/images/home_splash-photo-copyright.jpg"/>','<html:rewrite href="/media/images/home-splash-business-on.gif" />')">
<div id="wrapper">

<!-- Begin Header -->
<div id="logo-bar">

<html:img src="/media/images/home-headerlogo.gif" alt="Copyright Clearance Center" width="211" height="34" styleClass="floatleft" />
<div id="ecom-nav">
 <span id="welcome">Welcome</span>
 <security:ifUserAnonymous><a href="#">Log in</a></security:ifUserAnonymous> | <a href="CC-ID1+2" class="icon-cart">Cart</a> | <a href="/manageAccount.do">Manage Account</a> | <a href="#">Help</a>
</div>
<div class="clearer"></div>

</div>


<html:img src="/media/images/home-logo.gif" alt="Copyright.com" width="364" height="82" styleClass="floatleft" />

<!-- Get Permission / Find Title -->
<div id="titlesearch">
<html:img src="/media/images/home-search-getpermission.gif" alt="Get Permission / Find Title" width="149" height="14" /><br />
 <form name="form1" id="form1" method="post" action="">
  <a href="SR-ID4.html"><input id="titlesearchbox" value="Publication Title or ISBN/ISSN" name="textfield" type="text" class="input01" onfocus="if (this.value=='Publication Title or ISBN/ISSN') {this.value=''}" onblur="if (this.value=='') {this.value='Publication Title or ISBN/ISSN'}"  /><html:img src="/media/images/go.jpg"/></a>
 </form>
   <a href="SR-ID2.html"><html:img src="/media/images/search.jpg"/></a></div>
<br clear="all" />
<!-- Get Permission / Find Title -->

<div id="home-splash-nav">
<a href="#" onmouseover="MM_swapImage('splash_business','','images/home-splash-business-on.gif','splash_photo','','images/home_splash-photo-business.jpg',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/home-splash-business.gif" alt="Business" width="255" height="89" border="0" styleId="splash_business" /></a>
<a href="#" onmouseover="MM_swapImage('splash_academic','','images/home-splash-academic-on.gif','splash_photo','','images/home_splash-photo-academic.jpg',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/home-splash-academic.gif" alt="Academic" width="255" height="88" border="0" styleId="splash_academic" /></a>
<a href="#" onmouseover="MM_swapImage('splash_copyright','','images/home-splash-copyright-on.gif','splash_photo','','images/home_splash-photo-copyright.jpg',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/home-splash-copyright.gif" alt="Copyright Central" width="255" height="86" border="0" styleId="splash_copyright" /></a></div>
<div id="home-splash-photo">
<html:img src="/media/images/home_splash-photo.jpg" alt="Share, Innovate & Collaborate" width="494" height="263" styleId="splash_photo" />
</div>
<div class="clearer"></div>


<table border="0" cellpadding="0" cellspacing="0" id="hmmenu">

<tr>

<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('business','','images/nav_business_on.gif',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_business.gif" alt="Business"  width="105" height="34" border="0" styleId="business" /></a>
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


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('academic','','images/nav_academic_on.gif',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_academic.gif" alt="Academic" width="117" height="34" border="0" styleId="academic" /></a>
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


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('publishers','','images/nav_publishers_on.gif',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_publishers.gif" alt="Publishers" width="128" height="34" border="0" styleId="publishers" /></a>
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


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('authors','','images/nav_authors_on.gif',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_authors.gif" alt="Authors" width="106" height="34" border="0" styleId="authors" /></a>
<table style="display: none" border="0" id="menu_authors" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Licensing Services</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Beyond The Book</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Grant Permission Online</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Compliance &amp; Education</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('partners','','images/nav_partners_on.gif',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_partners.gif" alt="Partners" width="113" height="34" border="0" styleId="partners" /></a>
<table style="display: none" border="0" id="menu_partners" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Partner Directory</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Become a Partner</a></td></tr>
<tr onclick='window.location.href="#";' ><td><a href="page_link_here">Integration Technology</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="#" onmouseover="MM_swapImage('copyright','','images/nav_home_on.gif',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_home.gif" alt="Copyright Central"  width="180" height="34" border="0" styleId="copyright" /></a>
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



<!-- End Header -->
<!-- Begin Main Content -->

<div id="home-content">
<!-- Column 1 -->
<div><img class="home-title" src="images/title-intro-newsevents.gif" alt="News &amp; Events" width="213" height="19" />
<p>Scopus now offers CCC permissions<br /><a href="#">Read more</a></p>
<p>Nature Publishing Selects Rightslink<br /><a href="#">Read more </a></p>
<p>See us at the ACC&rsquo;s Annual Meeting<br /><a href="#">Learn more </a></p>
<p>CCC Spring Newsletters Now Online<br /><a href="#">See more</a></p>
<p><a href="#"><strong>More News</strong></a> | <strong><a href="#">More Events</a></strong></p>
</div>
<!-- Column 1 -->
<!-- Column 2 -->
<div style="margin-left: 19px; margin-right: 19px;"><img class="home-title" src="images/title-intro-quicklinks.gif" alt="Quick Links" width="213" height="19" />
<p>New to copyright.com? <a href="#">Get started.</a></p>
  <p>Annual Licensees: <a href="#">Verify Coverage by Title.</a></p>
  <p><a href="#">Purchase Copyright Permission Now.</a></p>
  <p><a href="#">Learn</a> about copyright.</p>
  <p><a href="#">Register</a> a copyright.</p>
</div>
<!-- Column 2 -->
<!-- Column 3 -->
<div>
<img class="home-title" src="images/title-intro-rightsphere.gif" alt="Introducing Rightsphere" width="213" height="19" />
<p><a href="#"><html:img src="/media/images/logo_rightsphere.gif" alt="rightsphere" width="213" height="51" border="0" /></a></p>
<p style="line-height:16px;">Rightsphere is a revolutionary Web-based rights advisory and management service for corporations that enables users to share information with the confidence of knowing they are copyright compliant. <a href="#">Learn more</a></p>
</div>
<!-- Column 3 -->
<br clear="all" />
</div>


</div>


<div id="bottomcorners"></div>

<!-- End Main Content -->

<!-- Begin Footer -->
<div id="footer-wrap">
 <div id="footer-content">
		<p><a href="#">About Us</a> | <a href="#">Contact Us</a> | <a href="#">Careers</a> | <a href="#">Privacy Policy</a> | <a href="#">Terms &amp; Conditions</a> | <a href="#">Site Index<br />
		</a><a href="#">Copyright</a> &copy; 1995-2006 </p>
		
		<div class="clearer"></div>
</div>
</div>
<!-- End Footer -->

<!-- POPUP -->
<script language="javascript" type="text/javascript">
   // popup_new_site_message();
</script>

</body>
</html>
