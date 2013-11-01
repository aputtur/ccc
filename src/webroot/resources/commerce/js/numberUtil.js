	function isNumeric( sText )
	{
	  var numericRegex = /^-?[0-9]*\.?[0-9]+$/;
	  
	  return numericRegex.test( sText );
	 
	}

	function formatNumber( value, currencyCode ){
						
		if ( isNumeric( value ) ){
			if (currencyCode != null && currencyCode != '' && currencyCode == 'JPY'){
				return new Number(value).toFixed( 0 );				
			} else {
				return new Number(value).toFixed( 2 );
			}
		}
		
		return value;
	}
	
	function formatNumberWoutCurrency( value){
		
		if ( isNumeric( value ) ){
			return new Number(value).toFixed( 2 );
		}
		
		return value;
	}