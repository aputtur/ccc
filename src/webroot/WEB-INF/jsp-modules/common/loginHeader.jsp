<%@ page contentType="text/html;charset=windows-1252"  language="java" %>
<%@ page errorPage="/jspError.do" %>

<%@ page import="com.copyright.ccc.business.security.UserContextService" %>
<%@ page import="com.copyright.ccc.config.CC2Configuration" %>
<%@ page import="com.copyright.ccc.web.WebConstants" %>
<%@ page import="com.copyright.workbench.util.StringUtils2" %>

<%@ taglib uri="/WEB-INF/tld/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld"  prefix="html" %>  
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="static" uri="/WEB-INF/tld/cc2-static.tld" %>

<link href="<html:rewrite page="/resources/commerce/css/topnav.css"/>" rel="stylesheet" type="text/css" />
<link href="/resources/commerce/css/jquery-ui.css" rel="stylesheet" type="text/css"/>

<script src="<html:rewrite page="/resources/commerce/js/dropdown.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/util.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page="/resources/commerce/js/jquery-ui.min.js"/>" type="text/javascript"></script>
    
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

function preloadImages()
{
    MM_preloadImages('<html:rewrite href="/media/images/nav_business_on.gif"/>','<html:rewrite href="/media/images/nav_academic_on.gif"/>','<html:rewrite href="/media/images/nav_publishers_on.gif"/>','<html:rewrite href="/media/images/nav_authors_on.gif"/>','<html:rewrite href="/media/images/nav_partners_on.gif"/>','<html:rewrite href="/media/images/nav_home_on.gif"/>');
}

//window.onload = preloadImages;
if( typeof addOnLoadEvent == 'function' && typeof preloadImages == 'function' ) addOnLoadEvent( preloadImages );

</script>
<bean:define id="help_url"><%= CC2Configuration.getInstance().getHelpURL() %></bean:define>
<% 
String logoutLink = "/ccc/do/logout";
%>

<%/* <tiles:insert template="/dcs_tag_js.jsp"/> */%>

<a  href="<html:rewrite page="/home.do"/>">
    <html:img src="/media/images/CCC_logo3_RGB_72dpi.jpg" alt="Copyright Clearance Center" style="border: 0; float: left; margin: 4px 0 6px 8px;" styleId="img_ccc_logo" />
</a>

<div id="ecom-nav">
    <div style="height: 28px;">
        <div class="tcol" style="text-align: right;"><span id="welcome">Welcome</span></div>
        <div class="tcol"><html:link action="/loginBounce.do">Log in</html:link></div>
        <div class="tcol">|</div>
        <div class="tcol"><html:link action="/cart.do" styleClass="icon-cart">Cart</html:link></div>
        <div class="tcol">|</div>
        <div class="tcol"><html:link action="/manageAccount.do">Manage</html:link><br /><html:link action="/manageAccount.do">Account</html:link></div>
        <div class="tcol">|</div>
        <div class="tcol"><a href="http://guest.cvent.com/d/bdqgxd" target="_blank">Feedback</a></div>
        <div class="tcol">|</div>
        <div class="tcol"><a href="<bean:write name="help_url" />" target="_blank">Help</a></div>
        <div class="tcol">|</div>
        <div class="tcol"><a href="http://chat.copyright.com/webchat/" target="_blank" class="icon-chat">&nbsp;</a></div>
        <div class="tcol"><a href="http://chat.copyright.com/webchat/" target="_blank">Live</a><br /><a href="http://chat.copyright.com/webchat/" target="_blank">Help</a></div>
    </div>
</div>

<% if(StringUtils2.isNullOrEmpty((String)request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO))){ %>
	<div id="navigation">
		<static:importURL url='<%= CC2Configuration.getInstance().getMainDropDownMenuURL() %>'  />
	</div>
<% } else {%>
	<% if(request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO).equals("0")){ %>
		<div id="navigation">
			<static:importURL url='<%= CC2Configuration.getInstance().getMainDropDownMenuURL() %>'  />
		</div>
	<% } %>
	  
	<% if(request.getSession().getAttribute(WebConstants.SessionKeys.IS_CEMPRO).equals("1")){ %>
		<div id="navigation">
		     <static:importURL url='<%= CC2Configuration.getInstance().getCemproMainMenuURL() %>'  />
		</div>
	<% } %> 
<% } %> 
  
  <div style="clear:both; margin: 2px 0 0 0; height: 68px; background: url(/media/images/banner_patt2.gif) bottom left repeat-x;">
    <tiles:insert page="/WEB-INF/jsp-modules/common/basicSearchFormDiv.jsp"/>
     
  </div>

<% //IE6 bug BEGIN %>
<iframe id="helperIFrame" src="<html:rewrite page="/pages/62167c1239i93851aee9451eff4728944fbf8356.html"/>" frameborder="0" scrolling="no" style="position: absolute; width: 0px; height; 0px; top: 0px; left: 0px;"></iframe>
<% //IE6 bug END %>

<!-- Begin Header -->

<script type="text/javascript">
 <%// Code to fix SCR 8718: The blue NAV bar is not fully available on the Manage Account Screen - IE only %>
 if( navigator.userAgent.indexOf("MSIE") > -1 ){ 
    var menuTable = document.getElementById( "menu" );
    var isMenuTableConsistent = menuTable && (/^table$/i).test( menuTable.tagName );
    
    if( isMenuTableConsistent ){
      menuTable.style.position = "absolute";
      menuTable.style.zIndex = new Number( 1000 );
    }
 }
 
  var cccLogo = document.getElementById("img_ccc_logo");
  if( cccLogo && (/^img$/i).test( cccLogo.tagName ) ) cccLogo.style.position = "absolute";

 
 var ecomNav = document.getElementById("ecom-nav");
 if( ecomNav && (/^div$/i).test( ecomNav.tagName ) ) ecomNav.style.paddingBottom = "5px";
</script>

<!-- End Header -->
