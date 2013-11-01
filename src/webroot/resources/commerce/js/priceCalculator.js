//AJAX PriceFetcher

function PriceCalculator(url, pricingErrorSectionId)
{
    this.ajaxHelper = new net.ContentLoader( this, url, "POST", [] );
    this.onCalculateSuccess = "";
    this.pricingErrorSectionId = pricingErrorSectionId;
}

PriceCalculator.prototype = 
{
    calculatePrice: function( requestParameters, onCalculateSuccess )
    {
        this.onCalculateSuccess = onCalculateSuccess;
        this.ajaxHelper.sendRequest( requestParameters );
    },
    
    ajaxUpdate: function( request )
    {
        var ajaxXMLResponse = request.responseXML.getElementsByTagName("ajaxResponse")[0];
        var statusCode = this.getTextContent( ajaxXMLResponse.getElementsByTagName("status")[0] );
        
        document.getElementById("publicationYearRange").style.display = "";
        document.getElementById("publicationYearRangeValue").innerHTML = 
            this.getTextContent( ajaxXMLResponse.getElementsByTagName("publicationYearRange")[0] );
        
        if(statusCode == "success")
        {
            var price = this.getTextContent( ajaxXMLResponse.getElementsByTagName("price")[0] );
            this.onCalculateSuccess( price );
        }
        else if(statusCode == "validationError")
        {
            var validationErrorMessages = ajaxXMLResponse.getElementsByTagName("validationErrorMessage");
            var validationErrors = [];
            for(var i = 0; i < validationErrorMessages.length; i++)
            {
                validationErrors[i] = this.getTextContent( validationErrorMessages[i] );
            }
            showValidationErrors( validationErrors );
        }
        else
        {
            this.showPricingError( ajaxXMLResponse );
        }
    },
    
    showPricingError: function( ajaxXMLResponse )
    {
        var errorIcon = this.getTextContent( ajaxXMLResponse.getElementsByTagName("errorIcon")[0] );
        var errorMessage = this.getTextContent( ajaxXMLResponse.getElementsByTagName("errorMessage")[0] );
        var errorAction = this.getTextContent( ajaxXMLResponse.getElementsByTagName("errorAction")[0] );

        document.getElementById(this.pricingErrorSectionId).className = errorIcon;
        document.getElementById(this.pricingErrorSectionId).innerHTML = errorMessage;
        
        showErrorMode( errorAction );
    },
    
    getTextContent: function( element )
    {
        return element.firstChild.nodeValue;
    },
    
    handleError: function( request )
    {
        alert("Error in calculating price");
    }
}
