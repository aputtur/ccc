/*
 * Pseudo-Constants
 */
 
  var PARAM_EVENT_TYPE = "eventType";
  var PARAM_COMMUNICATION_VALUE = "communicationValue";
 
  var EVENT_CONTACT_SME_MAIN = "EVENT_CONTACT_SME_MAIN";
  var EVENT_CONTACT_SME_DETAILS = "EVENT_CONTACT_SME_DETAILS";
  var EVENT_CONTACT_SME_DETAILS_SPECIAL = "EVENT_CONTACT_SME_DETAILS_SPECIAL";
  var EVENT_CONTACT_SME_CART = "EVENT_CONTACT_SME_CART";
  var EVENT_CONTACT_SME_CHECKOUT = "EVENT_CONTACT_SME_CHECKOUT";
  var EVENT_CONTACT_SME_SEARCH = "EVENT_CONTACT_SME_SEARCH";

/*
 * Constructor
 */
 
function EventLogger( eventLoggerServiceURL ) {
   
   this.eventLoggerServiceURL = eventLoggerServiceURL;
   
   this.ajaxHelper = new net.ContentLoader( this, eventLoggerServiceURL, "POST", [] );
         
}

/*
 * Methods
 */
EventLogger.prototype = {

	ajaxUpdate:  function( request ) {
  },
  
  handleError:  function( request ) {
    alert("Event Logging Error\n\nError code: " + request.status + "\nError message: " + request.statusText);
  },
  
  logEvent: function( eventType, communicationValue ){
    
    var requestString = PARAM_EVENT_TYPE + '=' + eventType + '&' + PARAM_COMMUNICATION_VALUE + '=' + communicationValue;
  
    this.ajaxHelper.sendRequest( requestString );	
  
  }
  
}