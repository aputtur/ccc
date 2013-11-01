//AJAX PriceFetcher

function ContentsChecker(url)
{
    this.ajaxHelper = new net.ContentLoader( this, url, "POST", [] );
}

ContentsChecker.prototype = 
{
    checkContents: function( requestParameters )
    {
        this.ajaxHelper.sendRequest( requestParameters );
    },
    
    ajaxUpdate: function( request )
    {
        var ajaxXMLResponse = request.responseXML.documentElement;
        var status = this.getTextContent( ajaxXMLResponse.getElementsByTagName("status")[0] );
        
        if(status == "success")
        {
            document.getElementById("specialPermissionTypeForm").submit();
        }
        else
        {
            alert("Permission for use in courses (e.g. courspacks, classroom handouts, e-coursepacks, etc) cannot be purchased at the same time as permission for other kinds of use (e.g. photocopying for general use, e-mail, republication, etc). \n\nPlease checkout or clear the current items in your cart.");
        }
    },
    
    getTextContent: function( element )
    {
        return element.firstChild.nodeValue;
    },
    
    handleError: function( request )
    {
        alert("Error in checking for content");
    }
}
