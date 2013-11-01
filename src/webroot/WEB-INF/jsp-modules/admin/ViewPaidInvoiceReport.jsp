<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>
<style type="text/css">
#manage-center {
	float: left;
	width: 554px;
	padding: 20px 10px;
}
#manage-center h2 {
	color: #316DB7;
	font-size: 14px;
	font-family: Arial, Helvetica, sans-serif;
	padding-bottom: 0px;
}
</style>

<title>CCC - Academic</title>
<link href="resources/commerce/css/default.css" rel="stylesheet" type="text/css" />
<link href="resources/commerce/css/ccc-new.css" rel="stylesheet" type="text/css" />
<script src="resources/commerce/js/dropdown.js" type="text/javascript"></script>
<script type="text/javascript" src="resources/commerce/js/util.js"></script>
<!--
<script src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-1.3.2.min.js"/>" type="text/javascript"></script> 
<script src="<html:rewrite page="/resources/ordermgmt/scripts/jquery-ui-1.7.2.custom.min.js"/>" type="text/javascript"></script> 
<link href="<html:rewrite page="/resources/ordermgmt/style/smoothness/jquery-ui-1.7.2.custom.css"/>" rel="stylesheet" type="text/css" />
-->
<script language="JavaScript" type="text/JavaScript"><!--


function initPage() {
  
}
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
function process() {
  if (validate()) 
  {
    var mainFrm = document.getElementById('mainFrm');

    mainFrm.action = '<html:rewrite action="saveReportsByPaidInvoiceDate.do"/>';
    mainFrm.target="_blank";
    mainFrm.method="POST";	
    mainFrm.submit(); 
  }
}
function showReport() {
  if (validate()) {
       var mainFrm = document.getElementById('mainFrm');
     if (mainFrm) {
       // Do a popup window here and call showReport.do 
       url = '<html:rewrite action="/showPaidInvoiceReport.do" />';
       //win=openPopup(url,"mywindow","menubar=1,toolbar=1,location=1,scrollbars=1,resizable=1,width=650,height=450");
       //form.target='mywindow';
       mainFrm.action=url; // submits the form
       mainFrm.target="_blank";
       mainFrm.method="POST";
       mainFrm.submit();
     }
   }
}
function validate() 
{
  var rtnValue = false;
  var inputFld;
  var beginValid = false;
  var endValid = false;

  inputFld = document.getElementById('beginDate');
  if (inputFld) 
  {
     if (inputFld.value) { beginValid = true; }
  }

  inputFld = document.getElementById('endDate');
  if (inputFld) 
  {
     if (inputFld.value) { endValid = true; }
  }

 if (!beginValid && !endValid) 
 {
     alert('Please enter a valid begin and end date for the Order Date search criteria in the format mm/dd/yyyy');
 }
 else if (!beginValid && endValid) 
 {
     alert('Please enter a valid begin date for the search criteria in the format mm/dd/yyyy');
 }
 else if (beginValid && !endValid) 
 {
     alert('Please enter a valid end date for the search criteria in the format mm/dd/yyyy');
 } 
    
  rtnValue = beginValid & endValid;

  return rtnValue;
}
function disableRadioButtonsForEnterpriseUser() {
    enterpriseViewInd = document.getElementById('enterpriseViewInd');
    
    if (enterpriseViewInd.value == 'T') {
//        orderDateRB = document.getElementById('order');
//        orderDateRB.disabled=true;
        InvoiceDateRB = document.getElementById('inv');
        InvoiceDateRB.disabled=true;
    };
}
//
--></script>

 <!--body class="cc2"
       onload="MM_preloadImages('/media/images/nav_business_on2.gif','/media/images/nav_academic_on2.gif','/media/images/nav_publishers_on2.gif','/media/images/nav_authors_on2.gif','/media/images/nav_partners_on2.gif','/media/images/nav_home_on2.gif')"><div id="wrapper"-->
   <!-- Begin Main Content -->
    
   <div id="manage-wrapper">
    <div id="manage-center">
     <h2>Paid Invoice Report</h2><br/>
     <div class="horiz-rule"></div><br/>
     <h3>Generate report:</h3><br/>
      
     <div class="indent-1">
     <form id="mainFrm" name="viewPaidInvoiceReportForm">
     <html:hidden name="viewPaidInvoiceReportForm" property="enterpriseViewInd" styleId="enterpriseViewInd"/>
       <br/><br/>
      
<!-- Begin main section table -->       
  <table  id="dateRange" cellpadding="3"> 
    <tr>
      <td colspan="4" id="ordrLabel"><b>Paid Date Range:</b></td>
    </tr>
    <tr><td width="40" align="right">From:&nbsp;</td>
       <td>
           <html:text styleId="beginDate" name="viewPaidInvoiceReportForm" property="beginDate" size="10" maxlength="12"/>
       </td>
       <td width="40" align="right">To:&nbsp;</td>
        <td>
           <html:text styleId="endDate" name="viewPaidInvoiceReportForm" property="endDate" size="10" maxlength="12"/>
        </td>
    </tr>
  </table>
     <br/><br/>
     <br/><br/>
      <a href="javascript:process();">
        <img src="<html:rewrite href="/media/images/btn_save.gif"/>" alt="Save" align="right"/>
     </a>

     <!--input name="show" type="image" src="<html:rewrite href="/media/images/btn_show_report.gif"/>" align="right" onclick="showReport(this.form)"-->
     <a href="javascript:showReport();">
     <img src="<html:rewrite href="/media/images/btn_show_report.gif"/>" align="right">
     </a>
    </form>  
    </div>

    </div>
     
    <div class="clearer"></div>
   </div>
   <!-- End Main Content -->
   
  <script language="JavaScript" type="text/JavaScript">
   
 	$(document).ready(function(){

	 	$("#beginDate").datepicker( {
	 			changeMonth: true,
				changeYear: true,
				yearRange: '-4:+4',
			    showAnim : 'fadeIn',
				showOn: 'button',
				buttonImage: '<html:rewrite href='/media/images/calendar.gif'/>',
				buttonImageOnly: true,
				buttonText: 'Calendar...'		});
	 	$("#endDate").datepicker( {
	 		    changeMonth: true,
			    changeYear: true,
			    yearRange: '-4:+4',
			    showAnim : 'fadeIn',
				showOn: 'button',
				buttonImage: '<html:rewrite href='/media/images/calendar.gif'/>',
				buttonImageOnly: true,
				buttonText: 'Calendar...'		});
	});
    
  </script>   
