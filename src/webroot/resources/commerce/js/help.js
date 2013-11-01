  protocolSeparator = "://";

 function setUpHelp( pathToHelpDispatcher, helpLinkId ){
 
      var helpLink = (helpLinkId == null) ? document.getElementById("helpLink") : document.getElementById(helpLinkId);
      
      if( helpLink ){
      
        var protocol = getProtocol();
        var fullPathToHelpDispatcher =  protocol + protocolSeparator + pathToHelpDispatcher;
           
        helpLink.href = "javascript:launchHelp(\"" + fullPathToHelpDispatcher + "\")";
      }
      
    }

  function launchHelp( pathToHelpDispatcher ){
  
      openPopup( pathToHelpDispatcher, "_blank", "");
  
  }
  
  function getProtocol(){
    return document.location.href.split( protocolSeparator )[0];
  }