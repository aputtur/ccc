<br />
<br />
<br />
<div id="adminHomeGreeting" class="adminNavSection" style="width: inherit; z-index: 10000000">
    <span style="font-size: 3em">Copyright.com Administration home</span>
</div>

<script type="text/javascript">
  <%//Element does not display properly in Firefox%>
  if( navigator.userAgent.indexOf("Firefox") > -1 ){
    var adminHomeGreeting = document.getElementById("adminHomeGreeting");
    if( adminHomeGreeting ) adminHomeGreeting.style.marginTop = "40px";
  }
</script>