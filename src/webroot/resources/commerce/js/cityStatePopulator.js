function CityStatePopulator( channel )
{
    this.ajaxHelper = new net.ContentLoader( this, "cityStatePopulator.do?operation=fetchCityAndState", "POST", [] );
    this.cityTextBoxId = "";
    this.stateTextBoxId = "";
    this.phoneTextBoxId = "";
    this.channel = channel;
}

CityStatePopulator.prototype = 
{
    populateCityAndState: function( zipCode, cityTextBoxId, stateTextBoxId, phoneTextBoxId)
    {
        this.cityTextBoxId = cityTextBoxId;
        this.stateTextBoxId = stateTextBoxId;
        this.phoneTextBoxId = phoneTextBoxId;
        //this.channel = channel;
        
        this.ajaxHelper.sendRequest( ["zipCode="+zipCode] );
    },
    
    ajaxUpdate: function( request )
    {
        var ajaxXMLResponse = request.responseXML.getElementsByTagName("ajaxResponse")[0];
        var city = this.getTextContent( ajaxXMLResponse.getElementsByTagName("city")[0] );
        var state = this.getTextContent( ajaxXMLResponse.getElementsByTagName("state")[0] );
       // var channel = this.getTextContent( ajaxXMLResponse.getElementsByTagName("channel")[0] );
        
        //alert("Zip Code: " + zipCode);
        //alert("Channel: " + this.channel);
        
        if (this.channel == "IND")
        {
            if (city != "N/A")
            {
                document.getElementById(this.cityTextBoxId).value = city;
                document.getElementById(this.phoneTextBoxId).select();
            }
            else
            {
                document.getElementById(this.cityTextBoxId).value = "";
                document.getElementById(this.cityTextBoxId).select();
            }
            
            if (state != "NA")
            {
                document.getElementById(this.stateTextBoxId).value = state;
                document.getElementById(this.stateTextBoxId).disabled = true;                            
            }
            else
            {
                document.getElementById(this.stateTextBoxId).value = "  ";
                document.getElementById(this.stateTextBoxId).disabled = false;
            }
        
        }
        else if (this.channel == "ORG")
        { 
        
            var flagTrue = document.getElementById("sameBill").checked;
        
            //alert("flag in JS: " + flagTrue);
        
            if (city != "N/A")
            {
                document.getElementById(this.cityTextBoxId).value = city;
                document.getElementById(this.phoneTextBoxId).select();
                            
                if (flagTrue)
                {
                    document.getElementById("billing" + this.cityTextBoxId).value = city;
                }
            }
            else
            {
                document.getElementById(this.cityTextBoxId).value = "";
                document.getElementById(this.cityTextBoxId).select();
            
                if (flagTrue)
                {
                    document.getElementById("billing" + this.cityTextBoxId).value = "";
                }
            }
        
            if (state != "NA")
            {
                document.getElementById(this.stateTextBoxId).value = state;
                document.getElementById(this.stateTextBoxId).disabled = true;
            
                if (flagTrue)
                {
                    document.getElementById("billing" + this.stateTextBoxId).value = state;
                    document.getElementById("billing" + this.stateTextBoxId).disabled = true;
                }
            }
            else
            {
                document.getElementById(this.stateTextBoxId).value = "  ";
                document.getElementById(this.stateTextBoxId).disabled = false;
            
                if (flagTrue)
                {
                    document.getElementById("billing" + this.stateTextBoxId).value = "  ";
                    document.getElementById("billing" + this.stateTextBoxId).disabled = true;
                }
            }
        }
        
    },
    
    getTextContent: function( element )
    {
        return element.firstChild.nodeValue;
    },
    
    handleError: function( request )
    {
        alert("Error in getting City/State");
    }
}
