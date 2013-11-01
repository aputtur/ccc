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

<h1>Error page</h1>

<div id="leftShade" ></div>

<div id="exceptionContainer" class="mainContent" style="min-height: 500px;">	

	<h2>The application encountered a problem</h2>
	
	<p>
		Please contact Customer Service at 888-888-8888 or <a href="javascript:void(0)">orderManagement@copyright.com</a>
	</p>
		
	<div id="exceptionDetails" style="margin-top: 10px;" >
		<h4>Exception Name: </h4>
		<span><s:property value="exception" /></span>
		
		<h4>Stacktrace:</h4>
		<span><s:property value="exceptionStack" /></span>
	</div>
	
</div>


<div class="clearer"></div>
