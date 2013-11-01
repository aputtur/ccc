<%@ page contentType="text/html;charset=windows-1252" %>
<%@ page errorPage="/jspError.do" %>

<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>  

<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>

<style type="text/css">
    .menu {

	background-color: #1B549A;

	position: absolute;

	top: 34px;

	left: 0px;

	font-weight: bold;

	filter:alpha(opacity=95);

	filter:progid:DXImageTransform.Microsoft.Alpha(opacity=95);

	-moz-opacity:0.95; 

	}
table.menu tr td a {
display:block;
}
</style>

<script src="<html:rewrite page="/resources/commerce/js/dropdown.js"/>" type="text/javascript"></script>
<!--<script src="<html:rewrite page="/resources/commerce/js/dcs_js.js"/>" type="text/javascript"></script>-->
<script language="JavaScript" type="text/JavaScript">

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

</script>

<security:ifUserHasPrivilege code="any">
</security:ifUserHasPrivilege>
<util:else>
    <security:ifUserEmulating>
        <tiles:insert page="/WEB-INF/jsp-modules/admin/adminMenu.jsp"/>
    </security:ifUserEmulating>
</util:else>

<!-- Begin Header -->
<div id="logo-bar">

<html:img src="/media/images/home-headerlogo.gif" alt="Copyright Clearance Center" width="211" height="34" styleClass="floatleft" />
<div id="ecom-nav" style="width:520px">
    <tiles:insert page="/WEB-INF/jsp-modules/common/dynamicHeaderLinks.jsp"/>
</div>
<div class="clearer"></div>

</div>


<img src="/media/images/home-logo.gif" alt="Copyright.com" width="364" height="82" style="float:left" />

	<!-- insert header basic search form -->
    <tiles:insert page="/common/basicSearchFormDiv.jsp" flush="true"/>

<br clear="all" />
<!-- Get Permission / Find Title -->

<static:import file="home-page/above-nav.html" />
<!--<div id="home-splash-nav"><a href="#" onmouseover="MM_swapImage('splash_business','','<html:rewrite href="/media/images/home-splash-business-on.gif" />', 'splash_photo','','<html:rewrite href="/media/images/home_splash-photo-business.jpg" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/home-splash-business.gif" alt="Business" width="255" height="89" border="0" styleId="splash_business" /></a><a href="#" onmouseover="MM_swapImage('splash_academic','','<html:rewrite href="/media/images/home-splash-academic-on.gif" />','splash_photo','','<html:rewrite href="/media/images/home_splash-photo-academic.jpg" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/home-splash-academic.gif" alt="Academic" width="255" height="88" border="0" styleId="splash_academic" /></a><a href="#" onmouseover="MM_swapImage('splash_copyright','','<html:rewrite href="/media/images/home-splash-copyright-on.gif" />','splash_photo','','<html:rewrite href="/media/images/home_splash-photo-copyright.jpg" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/home-splash-copyright.gif" alt="Copyright Central" width="255" height="86" border="0" styleId="splash_copyright" /></a></div>
<div id="home-splash-photo"><html:img src="/media/images/home_splash-photo.jpg" alt="Share, Innovate & Collaborate" width="494" height="263" styleId="splash_photo" /></div>
<div class="clearer"></div>-->


<table border="0" cellpadding="0" cellspacing="0" id="menu">

<tr>

<td align="center" valign="top" class="menuover"><div><a href="<html:rewrite page='/viewPage.do?pageCode=bu1-n' />" onmouseover="MM_swapImage('business','','<html:rewrite href="/media/images/nav_business_on.gif" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_business.gif" alt="Business" width="105" height="34" border="0" styleId="business" /></a>
<table style="display:none" border="0" id="menu_business" class="menu" width="150" cellpadding="5" cellspacing="0" >
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=bu1-n' />">Licensing &amp; Permissions Services</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=bu11' />">Rightsphere</a></td></tr>
<tr><td><a href="../readyimages/">ReadyImages</a></td></tr>
<tr><td><a href="<html:rewrite page='/search.do?operation=show&page=annual' />">Verify Coverage Under Annual License</a></td></tr>
<tr><td><a href="<html:rewrite page='/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/are_you_licensed.html' />">Are You Licensed?</a></td></tr>
<tr><td><a href="<html:rewrite page='/search.do?operation=show&page=advanced' />">Purchase Permissions</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr2-n' />">Tools &amp; Guidelines</a></td></tr></table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="<html:rewrite page='/viewPage.do?pageCode=ac1-n' />" onmouseover="MM_swapImage('academic','','<html:rewrite href="/media/images/nav_academic_on.gif" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_academic.gif" alt="Academic" width="117" height="34" border="0" styleId="academic" /></a>
    <table style="display: none" border="0" id="menu_academic" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=ac1-n' />">Licensing &amp; Permissions Services</a></td></tr>
<tr><td><a href="<html:rewrite page='/search.do?operation=show&page=academic' />">Verify Coverage Under Annual License</a></td></tr>
<tr><td><a href="<html:rewrite page='/content/cc3/en/toolbar/productsAndSolutions/payPerUsePermissionServices/are_you_licensed.html' />">Are You Licensed?</a></td></tr>
<tr><td><a href="<html:rewrite page='/search.do?operation=show&page=advanced' />">Purchase Permissions</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=ci5-n' />">Partners</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr2-n' />">Tools &amp; Guidelines</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="<html:rewrite page='/viewPage.do?pageCode=pu1-n' />" onmouseover="MM_swapImage('publishers','','<html:rewrite href="/media/images/nav_publishers_on.gif" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_publishers.gif" alt="Publishers" width="128" height="34" border="0" styleId="publishers" /></a>
<table style="display: none" border="0" id="menu_publishes" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=pu1-n' />">Licensing &amp; Permissions Services</a></td></tr>
<tr><td><a href="<html:rewrite page='/content/cc3/en/toolbar/productsAndSolutions/rightslink.html' />">Rightslink</a></td></tr>
<tr><td><a href="<html:rewrite page='/content/cc3/en/toolbar/partners/webServices.html' />">Rightsconnect for Publishers</a></td></tr>
<tr><td><a href="<%= CC2Configuration.getInstance().getGPOURL() %>" target="_blank">Rights Central</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=pu10' />">Grant Permission Online</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=ma17-n' />">Manage Titles &amp; Fees</a></td></tr>
<tr><td><a href="<html:rewrite page='/search.do?operation=show&page=advanced' />">Purchase Permissions</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=rh5' />">International</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr2-n' />">Tools &amp; Guidelines</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="<html:rewrite page='/viewPage.do?pageCode=a1-n' />" onmouseover="MM_swapImage('authors','','<html:rewrite href="/media/images/nav_authors_on.gif" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_authors.gif" alt="Authors" width="106" height="34" border="0" styleId="authors" /></a>
<table style="display: none" border="0" id="menu_authors" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr><td><a href="<html:rewrite page='/content/cc3/en/toolbar/aboutUs.html' />">Licensing &amp; Permissions Services</a></td></tr>
<tr><td><a href="../ozmo">Ozmo</a></td></tr>
<tr><td><a href="http://www.beyondthebookcast.com/" target="_blank">Beyond The Book</a></td></tr>
<tr><td><a href="<%= CC2Configuration.getInstance().getGPOURL() %>" target="_blank">Rights Central</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=pu10' />">Grant Permission Online</a></td></tr>
<tr><td><a href="<html:rewrite page='/search.do?operation=show&page=advanced' />">Purchase Permissions</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=pu3-n' />">Education and Awareness</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr2-n' />">Tools &amp; Guidelines</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="<html:rewrite page='/viewPage.do?pageCode=ci1-n' />" onmouseover="MM_swapImage('partners','','<html:rewrite href="/media/images/nav_partners_on.gif" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_partners.gif" alt="Partners" width="113" height="34" border="0" styleId="partners" /></a>
<table style="display: none;" border="0" id="menu_partners" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr><td><a href="<html:rewrite page="/viewPage.do?pageCode=ci1-n"/>">Opportunities</a></td></tr>
<tr><td><a href="<html:rewrite page="/viewPage.do?pageCode=ci12"/>">Rightsconnect</a></td></tr>
<tr><td><a href="../ozmo">Ozmo</a></td></tr>
</table>
</div>
</td>


<td align="center" valign="top" class="menuover"><div><a href="<html:rewrite page='/content/cc3/en/toolbar/education.html' />" onmouseover="MM_swapImage('copyright','','<html:rewrite href="/media/images/nav_home_on.gif" />',1)" onmouseout="MM_swapImgRestore()"><html:img src="/media/images/nav_home.gif" alt="Copyright Central" width="180" height="34" border="0" styleId="copyright" /></a>
<table style="display: none;" border="0" id="menu_copyrightcentral" class="menu" width="150" cellpadding="5" cellspacing="0">
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=pu3-n' />">Copyright Education</a></td></tr>
<tr><td><a href="<html:rewrite page='/content/cc3/en/toolbar/education.html' />">Copyright News</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr2-n' />">Tools and Guidelines</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr10-n' />">Copyright Basics</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr4-n' />">Reprints &amp; Reports</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr20' />">Web sites</a></td></tr>
<tr><td><a href="<html:rewrite page='/viewPage.do?pageCode=cr3' />">Register a Copyright</a></td></tr>
</table>
</div>
</td>

</tr>

</table>

<script>
definemenu();

</script>

<!-- End Header -->
