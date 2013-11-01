// execute your scripts when DOM is ready. this is a good habit
$(function() {

	// initialize scrollable
	$("div.scrollable").scrollable({
	  size:1,
      loop: true,
      interval: 8000,
      speed: 1000
      });

});
