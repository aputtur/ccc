<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page errorPage="/jspError.do" %>


<script src="<html:rewrite page="/resources/commerce/js/dcs_js.js"/>" type="text/javascript"></script>

  <style type="text/css">
    a {
        font-size: 12px;
        font-family: verdana,sans-serif;
        color : #10468A;
        line-height: 12px;
    }
    p {
		color: black;
		margin-left: 5px;
        font-family: verdana,sans-serif;
		font-size: 12px;
    }
    body {
		width: 450px;
    }

    .headline {
        font-size: 23px;
		margin-left: 5px;
        font-family: Arial, Helvetica;
        font-weight: bold;
        color : #00357A;
    }

    .body {
        font-size: 12px;
        font-family: verdana,sans-serif;
		margin: 10px;
        line-height: 14px;
    }

    A.nav:link{font-family : Verdana;font-size : 12px;line-height : 19px;color : #FFFFFF;text-decoration : underline;}
    A.nav:hover{font-family : Verdana;font-size : 12px;line-height : 19px;color : #FFFFFF;text-decoration : none;}  
    A.nav:visited{font-family : Verdana;font-size : 12px;line-height : 19px;color : #FFFFFF;}       
    A.smalllink:link{font-family : Verdana;font-size : 9px;line-height : 10px;color : #10468A;text-decoration : underline;}
    A.smalllink:hover{font-family : Verdana;font-size : 9px;line-height : 10px;color : #10468A;text-decoration : none;}     
    A.smalllink:visited{font-family : Verdana;font-size : 9px;line-height : 10px;color : #10468A;}
    A.smalllink0:hover{text-decoration : none;}    
    A.close:link{font-family : Verdana;font-size : 10px;line-height : 19px;color : #961138;text-decoration : underline;}
    A.close:hover{font-family : Verdana;font-size : 10px;line-height : 19px;color : #961138;text-decoration : none;}        
    A.close:visited{font-family : Verdana;font-size : 10px;line-height : 19px;color : #961138;}
    A.indexlink:link{font-family : verdana,sans-serif;font-size : 11px;line-height : 12px;color : #00357A;text-decoration : underline;}
    A.indexlink:hover{font-family : verdana,sans-serif;font-size : 11px;line-height : 12px;color : #00357A;text-decoration : none;} 
    A.indexlink:visited{font-family : verdana,sans-serif;font-size : 11px;line-height : 11px;color : #00357A;}      
    A.bodylink2:link{font-family : verdana,sans-serif;font-size : 11px;line-height : 12px;color : #00357A;text-decoration : underline;}
    A.bodylink2:hover{font-family : verdana,sans-serif;font-size : 11px;line-height : 12px;color : #00357A;text-decoration : none;} 
    A.bodylink2:visited{font-family : verdana,sans-serif;font-size : 11px;line-height : 13px;color : #00357A;}      
    .error {
        color: red;
        font-weight: bold;
    }
  </style>


<br><br>
  
  <table align="left" width="450" border="0" cellpadding="0" cellspacing="0">
    <tr>
    
<td valign="top" align="left"><br>
                    <span class="headline"> 
                    Google Book Preview
                    </span> 
                    <br></br>
                    
                    <p><strong>Google Preview results are provided and hosted by Google, Inc.</strong><br></p>

                    </span>
                    

<style type="text/css">

body {
        font-family: Arial, sans-serif;
      }

      /* Google Preview: Basic configuration */
      /* !! CUSTOMIZE THIS #1: Set dimensions and border */
      #viewport {
        width: 420px;
        height: 500px;
        margin-left: 15px;
        margin-bottom: 10px;
      }

	#logoheader {
 	 width: 400px;
 	 margin-left: 20px;
	padding-top:5px
	}
	#footer {
	font:8px;
	text-align:right;
	}

       /* Google Preview: Boilerplate styling */
       #viewport { font-size: 16px; line-height: 1; }
       #viewport img, #viewport table, #viewport div, #viewport td
       { border: 0;  padding: 0; margin: 0; background: none }
       #viewport td { vertical-align: middle }
    </style>
    
    <script src="<html:rewrite page="/resources/commerce/js/dcs_js.js"/>" type="text/javascript"></script>

    <%
        //  Find out if we came into this page via a secure
        //  connection.  Change our link to GOOGLE depending
        //  on what we find.
        
        if (pageContext.getRequest().isSecure()) {
    %>
    <script type="text/javascript" src="https://www.google.com/jsapi">
    alert("In Preview");
    </script>
    <%
        }
        else {
    %>
    <script type="text/javascript" src="http://www.google.com/jsapi">
    alert("In Preview");
    </script>    
    <%
        }
    %>
    
    <div id="preview_section">
      <div id="viewport" align="center"></div>
    </div>
    
    <script type="text/javascript">

	function getQueryVariable(variable) {
  	var query = window.location.search.substring(1);
  	var vars = query.split("&");
  	for (var i=0;i<vars.length;i++) { 
   	 var pair = vars[i].split("="); 
    	if (pair[0] == variable) { 
      	return pair[1]; 
    } 
  } 
} 

    /**
     * Loads the viewport.
     * @param {string} isbn The ISBN to load.
     */
    function load_viewport(isbn) {
      var viewportDiv = document.getElementById("viewport");
      var viewer = new google.books.DefaultViewer(viewportDiv,
          {showLinkChrome: false});
      viewer.load(isbn, handle_not_found);
    }

    /**
     * This function will be called if the viewer was unable to load
     * the given ISBN.  For demonstration, this function hides the viewport
     * section of the page, but users could instead take some other relevant
     * action.
     */
    function handle_not_found() {
      var viewportSection = document.getElementById("preview_section");
      viewportSection.style.display = 'none';
    }

    google.load("books", "0");

	/* If you wish to display the interface in another language */
	// var params = { language: "fr" };
	// google.load("books", "0", params);


    // !! CUSTOMIZE THIS #2: Enter your ISBN
    google.setOnLoadCallback(function() { 
      //load_viewport("ISBN:" + getQueryVariable("isbn"))
      load_viewport("ISBN:" + <bean:write name="googleBookPreviewForm" property='isbn'/>);
    });

    </script>
    
    </td>
    </tr>
    </table>
    
    <jsp:include page="/WEB-INF/jsp-modules/common/dcs_tag_js.jsp" />
    
    </body>
    
    

