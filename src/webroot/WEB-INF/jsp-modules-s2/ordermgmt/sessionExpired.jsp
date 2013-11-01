<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">

	div#leftShade {
		background-position: 0 156px;
	}
	
	div#rightShade {
		background-position: 0 6px;
	}
	
	div#exceptionContainer{
		float: left; 
		width: 950px; 
		background-color: #FFFFFF; 
		padding-left: 5px;
		padding-top: 5px;
	}
	
	div#exceptionContainer a:hover{
		text-decoration: underline;
	}
	
	#exceptionContainer, #exceptionContainer .sb-inner { background: #FFFFFF; }
	#exceptionContainer .sb-border { background: #FFFFFF; }
		
</style>

<h1>Session Expired</h1>

<div id="leftShade" ></div>

<div id="exceptionContainer" class="mainContent" style="min-height: 500px;">	

	<h2>The session expired due to inactivity</h2>
	
	<p>
	
				click here to continue..<a href='<s:url namespace="/om" action="orderManagementLanding"  includeParams="none"/>'>Order Support and Management</a>
	</p>
		
</div>


<div class="clearer"></div>
