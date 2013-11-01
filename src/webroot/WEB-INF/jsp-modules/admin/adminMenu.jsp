<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>  
<%@ taglib prefix="security" uri="/WEB-INF/tld/cc2-security.tld" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/cc2-util.tld" %>


<link rel="stylesheet" type="text/css" href="<html:rewrite page="/resources/commerce/css/adminMenu.css"/>" />

<script type="text/javascript" src="<html:rewrite page="/resources/commerce/js/adminMenu.js"/>"></script>

<iframe src="<html:rewrite page="/pages/62167c1239i93851aee9451eff4728944fbf8356.html"/>" id="helperIFrame" frameborder="0" scrolling="no" style="position: absolute; visibility: hidden; z-index:1000000000; overflow: hidden;" ></iframe>

<style type="text/css">
  .emulationBar {
    text-align: right; 
    background-color: #DDDD77; 
    border-bottom: 1px solid #c0c0c0;
  }
  
  .emulatedUserDetailsArea {
    font-weight: normal;
    font-style: italic;
    color: #600;
  }
  
  .adminMenuTitleBar {
    text-align: center; 
    font-weight: bold; 
    background-color: #DDDD77; 
    border-bottom: 1px solid #c0c0c0;
  }
</style>

<table class="menuComponentTable" id="topMenuTable"> 
	
  <security:ifUserEmulating>
    <tr class="emulationBar" >
          <td class="menuCellInactive emulationBar" colspan="5">
            <span class="emulatedUserDetailsArea">Emulating: <util:userInfo userType="cc" propertyName="username"/></span> &nbsp;|&nbsp; <html:link action="/admin/emulation?operation=stop">Stop Emulation</html:link>
          </td>
    </tr>
    
    <script type="text/javascript">
        /*
        Simple Image Trail script- By JavaScriptKit.com
        Visit http://www.javascriptkit.com for this script and more
        This notice must stay intact
        */

        var trailimage=["<html:rewrite page="/resources/commerce/images/emulation.png"/>", 150, 30] //image path, plus width and height
        var offsetfrommouse=[10,20] //image x,y offsets from cursor position in pixels. Enter 0,0 for no offset
        var displayduration=0 //duration in seconds image should remain visible. 0 for always.

        if (document.getElementById || document.all)
        document.write('<div id="trailimageid" style="position:absolute;visibility:visible;left:0px;top:0px;width:1px;height:1px;z-index: 11;"><img src="'+trailimage[0]+'" border="0" width="'+trailimage[1]+'px" height="'+trailimage[2]+'px"></div>')

        function gettrailobj(){
          if (document.getElementById)
            return document.getElementById("trailimageid").style
          else if (document.all)
            return document.all.trailimagid.style
        }

        function truebody(){
          return (!window.opera && document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
        }

        function hidetrail(){
          gettrailobj().visibility="hidden"
          document.onmousemove=""
        }

        function followmouse(e){
          var xcoord=offsetfrommouse[0]
          var ycoord=offsetfrommouse[1]
          if (typeof e != "undefined"){
            xcoord+=e.pageX
            ycoord+=e.pageY
          }
          else if (typeof window.event !="undefined"){
            xcoord+=truebody().scrollLeft+event.clientX
            ycoord+=truebody().scrollTop+event.clientY
          }
          var docwidth=document.all? truebody().scrollLeft+truebody().clientWidth : pageXOffset+window.innerWidth-15
          var docheight=document.all? Math.max(truebody().scrollHeight, truebody().clientHeight) : Math.max(document.body.offsetHeight, window.innerHeight)
          if (xcoord+trailimage[1]+3>docwidth || ycoord+trailimage[2]> docheight)
            gettrailobj().display="none"
          else 
            gettrailobj().display=""
          gettrailobj().left=xcoord+"px"
          gettrailobj().top=ycoord+"px"
        }

        document.onmousemove=followmouse

        if (displayduration>0)
        setTimeout("hidetrail()", displayduration*1000)
    </script>
  </security:ifUserEmulating>

  <security:ifUserHasPrivilege code="any">
  
    <tr>
          <td class="menuCellInactive adminMenuTitleBar" colspan="5" style="">
            CCC Administration Tools
          </td>
    </tr>
    
    <tr>
          <td class="menuCellInactive" style="border-bottom-width: 0px; border-left-width: 0px; padding: 0px;" width="1 px">
          </td>
              
          <td class="menuCellInactive" style="width: 150px"  onmouseover="javascript:doOnMouseOver(this); dropdownmenu(this, event, 'menuOrderManagement');"  nowrap>
              <a href="#" style="text-decoration: none;" >Order Management</a>
          </td>
              
          <td class="menuCellInactive" style="width: 100px;" onmouseover="javascript:doOnMouseOver(this); dropdownmenu(this, event, 'menuCSR')" nowrap>
              <a href="#" style="text-decoration: none;">CSR</a>
          </td>
  
          <td class="menuCellInactive" style="width: 100px;" onmouseover="javascript:doOnMouseOver(this); dropdownmenu(this, event, 'menuRoles')" nowrap >
              <a href="#" style="text-decoration: none;">Roles</a>
          </td>
          
          <td class="menuCellInactive" style="width: 100px;" onmouseover="javascript:doOnMouseOver(this); dropdownmenu(this, event, 'menuUserAdmin')" nowrap >
              <a href="#" style="text-decoration: none;">User Admin</a>
          </td>
          
          <td class="menuCellInactive" style="width: 100px;" onmouseover="javascript:doOnMouseOver(this); dropdownmenu(this, event, 'menuPublisherMaintenance')" nowrap >
              <a href="#" style="text-decoration: none;">Publisher Maintenance</a>
          </td>
          <td  class="menuCellInactive">
              &nbsp;
          </td>
    </tr>
    
  </security:ifUserHasPrivilege>

</table>

<security:ifUserHasPrivilege code="any">

  <div id="menuOrderManagement" class="menuBody">
      <a href="<html:rewrite page="/admin/orderHistory.do"/>" style="text-decoration: none;">Adjust Order</a>
      <a href="<html:rewrite page="/adjustment/adjustment.do?operation=displayAdjustmentsForUser"/>" style="text-decoration: none;">Pending Adjustments</a>
      <a href="<html:rewrite page="/admin/viewReports.do"/>" style="text-decoration: none;">Activity Report</a>
      <a href="<html:rewrite page="/admin/autoDunning.do"/>" style="text-decoration: none;">Autodunning Adhoc</a>
      <a href="<html:rewrite page="/admin/viewPaidInvoiceReports.do"/>" style="text-decoration: none;">View Paid Invoice Report</a>
  </div>
  
  <div id="menuCSR" class="menuBody">
      <a href="<html:rewrite page="/admin/startEmulationForm.do"/>" style="text-decoration: none;">Emulate User</a>
  </div>
  
  <div id="menuRoles" class="menuBody">
      <a href="<html:rewrite page="/admin/userRolesForm.do"/>" style="text-decoration: none;">View/Edit User Roles</a>
  </div>
  
  <div id="menuUserAdmin" class="menuBody">
      <a href="<html:rewrite page="/admin/userAdmin.do"/>" style="text-decoration: none;">User Administration</a>
  </div>
  
  <div id="menuPublisherMaintenance" class="menuBody">
      <a href="<html:rewrite page="/admin/publisherMaintenance.do?operation=findPublishers"/>" style="text-decoration: none;">Publisher Maintenance</a>
  </div>
</security:ifUserHasPrivilege>