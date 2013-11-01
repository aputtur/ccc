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

<script language="JavaScript" type="text/JavaScript">
<!--

function initPage() {
  updt();
  disableRadioButtonsForEnterpriseUser();
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
function updt() {
  num = document.getElementById('num');
  if (num.checked) { 
     content = document.getElementById('dateRange');
     content.style.display="none";
     content = document.getElementById('invNum');
     content.style.display="";
  } else {
     orderRadio = document.getElementById('order');
     sotRadio   = document.getElementById('sot');
     ordLabel = document.getElementById('ordrLabel');
     inLabel = document.getElementById('invLabel');
     stLabel = document.getElementById('sotLabel');
     if (orderRadio.checked) {
       ordLabel.style.display="";
       inLabel.style.display="none";
       stLabel.style.display="none";
     }
     else if (sotRadio.checked) {
        stLabel.style.display="";
        ordLabel.style.display="none";
        inLabel.style.display="none";     
     }
     else {
       ordLabel.style.display="none";
       stLabel.style.display="none";
       inLabel.style.display="";
     }
     content = document.getElementById('invNum');
     content.style.display="none";
     content = document.getElementById('dateRange');
     content.style.display="";
  }
}
function process() {
  if (validate()) {
    // Suppress the error message first before processing
    errorDiv = document.getElementById('validationErrorsSection');
    if (errorDiv) {
      errorDiv.style.display="none";
    }
  
    document.viewReportsForm.action = '<html:rewrite action="/processReports.do"/>';
    document.viewReportsForm.submit(); 
  }
}
function showReport() {
   if (validate()) {
     var mainFrm = document.getElementById('mainFrm');
     if (mainFrm) {
       // Do a popup window here and call showReport.do 
       url = '<html:rewrite action="/showReport.do" />';
       //win=openPopup(url,"mywindow","menubar=1,toolbar=1,location=1,scrollbars=1,resizable=1,width=650,height=450");
       //form.target='mywindow';
       mainFrm.action=url; // submits the form
       mainFrm.submit();
     }
   }
}
function validate() {
  var rtnValue = false;
  var invNum = document.getElementById('num');
  var invDate = document.getElementById('inv');
  var ordrDate = document.getElementById('order');
  var soDate = document.getElementById('sot');
  var inputFld;
  if (invNum.checked) { 
     inputFld = document.getElementById('invNumFld');
     if (inputFld) {
        if (inputFld.value) { rtnValue = true; }
        else alert('Please enter the Invoice Number search criteria in the space provided');
     } else alert('Please enter the Invoice Number search criteria in the space provided');
     
     return rtnValue;
  }

  var beginValid = false;
  var endValid = false;
  var beginSotValid = false;
  var endSotValid = false;

  if (invDate.checked) { 
     
     inputFld = document.getElementById('beginDate');
     if (inputFld) {
        if (inputFld.value) { beginValid = true; }
     }

     inputFld = document.getElementById('endDate');
     if (inputFld) {
        if (inputFld.value) { endValid = true; }
     }
  } else if (ordrDate.checked ) { 
     inputFld = document.getElementById('beginDate');
     if (inputFld) {
        if (inputFld.value) { beginValid = true; }
     }

     inputFld = document.getElementById('endDate');
     if (inputFld) {
        if (inputFld.value) { endValid = true; }
     }
  } else if (soDate.checked ) { 
     inputFld = document.getElementById('beginDate');
     if (inputFld) {
        if (inputFld.value) { beginValid = true; }
     }

     inputFld = document.getElementById('endDate');
     if (inputFld) {
        if (inputFld.value) { endValid = true; }
     }
  }  

  if (ordrDate.checked) {
    if (!beginValid && !endValid) {
        alert('Please enter a valid begin and end date for the Order Date search criteria in the format mm/dd/yyyy');
    } else if (!beginValid && endValid) {
        alert('Please enter a valid begin date for the Order Date search criteria in the format mm/dd/yyyy');
    } else if (beginValid && !endValid) {
        alert('Please enter a valid end date for the Order Date search criteria in the format mm/dd/yyyy');
    } 
    
        
    }
    
    if (soDate.checked) {
    if (!beginValid && !endValid) {
        alert('Please enter a valid begin and end date for the Start of Term Date search criteria in the format mm/dd/yyyy');
    } else if (!beginValid && endValid) {
        alert('Please enter a valid begin date for the Start of Term Date search criteria in the format mm/dd/yyyy');
    } else if (beginValid && !endValid) {
        alert('Please enter a valid end date for the Start of Term Date search criteria in the format mm/dd/yyyy');
    } 
    
        
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
//-->
</script>
<div id="calendarBox" STYLE="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

 <!--body class="cc2"
       onload="MM_preloadImages('/media/images/nav_business_on2.gif','/media/images/nav_academic_on2.gif','/media/images/nav_publishers_on2.gif','/media/images/nav_authors_on2.gif','/media/images/nav_partners_on2.gif','/media/images/nav_home_on2.gif')"><div id="wrapper"-->
   <!-- Begin Main Content -->
    
   <div id="manage-wrapper">
    <div id="manage-center">
     <h2>Account Activity Reports</h2><br/>
     <div class="horiz-rule"></div><br/>
     <h3>Generate report by:</h3><br/>
      
     <div class="indent-1">
     <form id="mainFrm" name="viewReportsForm" action='<html:rewrite action="/processReports.do" />' target="_blank" method="post">
     <html:hidden name="viewReportsForm" property="enterpriseViewInd" styleId="enterpriseViewInd"/>
<html:radio name="viewReportsForm" property="radio" value="invNum" styleId="num" onclick="javascript:updt();"/>Invoice number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<html:radio name="viewReportsForm" property="radio" value="orderDate" styleId="order" onclick="javascript:updt();"/>Order date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<html:radio name="viewReportsForm" property="radio" value="invDate" styleId="inv" onclick="javascript:updt();"/>Invoice date
<html:radio name="viewReportsForm" property="radio" value="sotDate" styleId="sot" onclick="javascript:updt();"/>Start of Term date
       <br/><br/>
      
<!-- Begin main section table -->       
 <table id="invNum"> 
    <tr>
      <td width="120"><b>Invoice number:</b></td>
      <td><input class="normal" name="invoiceNum" type="text" id="invNumFld" /></td>
    </tr>
    <tr>
      <td width="120">&nbsp;</td>
      <td>(Up to ten numbers may be entered, separated by commas.)</td>
    </tr>
  </table>

  <table  id="dateRange" cellpadding="3"> 
    <tr>
      <td colspan="4" id="ordrLabel"><b>Order Date Range:</b></td>
      <td colspan="4" id="invLabel"><b>Invoice Date Range:</b></td>
      <td colspan="4" id="sotLabel"><b>Start of Term Date Range:</b></td>
    </tr>
    <tr><td width="40" align="right">From:&nbsp;</td>
       <td>
           <html:text styleId="beginDate" name="viewReportsForm" property="beginDate" size="10" maxlength="12"/>
       </td>
       <td width="40" align="right">To:&nbsp;</td>
        <td>
           <html:text styleId="endDate" name="viewReportsForm" property="endDate" size="10" maxlength="12"/>
        </td>
    </tr>
  </table>
  <!-- End main section table --> 
     <br/><br/>
<!-- TODO: Change this to logic equal for production -->
<!--logicequal name="viewReportsForm" property="enterpriseViewInd" value="T">  
Result Range Selection: 
<select name="range">
<option value="0">All Rows</option>
<option value="1">1 - 10,000</option>
<option value="2">10,001 - 20,000</option>
<option value="3">20,001 - 30,000</option>
<option value="4">30,001 - 40,000</option>
<option value="5">40,001 - 50,000</option>
<option value="6">50,001 - 60,000</option>
<option value="7">60,001 - 70,000</option>
<option value="8">70,001 - 80,000</option>
<option value="9">80,001 - 90,000</option>
<option value="10">90,001 - 100,000</option>
</select> 
</logicnotEqual-->
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
    <!-- 
    window.onload = initPage; 
    //-->
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
