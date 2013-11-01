<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">


	div#exceptionContainer{
		float: left; 
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

<div id="exceptionContainer" >	

	<h2>The session expired due to inactivity</h2>
	
	<p>
	
		click here to continue..<a onclick ="javascript:window.parent.location.href='<s:url namespace="/om" action="orderManagementLanding"  includeParams="none"/>';">Order Support and Management</a>
	</p>
		
</div>


<div class="clearer"></div>
