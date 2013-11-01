// creates a datepicker for the dateFieldId input field 
// with a calendar button using imageURL 

// for help see http://docs.jquery.com/UI/Datepicker

function jqCalendarDatepickerCreate(dateFieldId,imageURL) {

	var jqDate = '#'+dateFieldId;
	var jqValue = $(jqDate).val();
 	$(jqDate).datepicker( {
 		defaultDate: '',
 		selectDefaultDate: false,
		changeMonth: true,  
		changeYear: true,
		yearRange: '-2:+2',
	    showAnim : 'fadeIn',
		showOn: 'button',
		buttonImage: imageURL,
		buttonImageOnly: true,
		buttonText: 'Calendar...'});
 	$(jqDate).val(jqValue);
 
}

function jqCalendarDatepickerSetYearMenuRange(dateFieldId,yrRage) {
	// yearRange='-2:+2' list 2 years before current year and 2 after
	// yearRange='-0:+10' list 10 years after current year 
	// yearRange='-10:+0' list 10 years before current year 
	var jqDate = '#'+dateFieldId;
	var jqValue = $(jqDate).val();
	$( jqDate ).datepicker( "option", "yearRange", yrRage );
 	$(jqDate).val(jqValue);
}

function jqCalendarDatepickerSetMinDate(dateFieldId,mnDate) {
	// minDate='+0y' dates current year or later
	var jqDate = '#'+dateFieldId;
	var jqValue = $(jqDate).val();
	$( jqDate ).datepicker( "option", "minDate", mnDate );
 	$(jqDate).val(jqValue);
}

function jqCalendarDatepickerSetMaxDate(dateFieldId,mxDate) {
	// maxDate='+0y' dates up to current year
	var jqDate = '#'+dateFieldId;
	var jqValue = $(jqDate).val();
	$( jqDate ).datepicker( "option", "maxDate", mxDate );
 	$(jqDate).val(jqValue);
}
	
