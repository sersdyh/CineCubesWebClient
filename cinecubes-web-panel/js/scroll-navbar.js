/**
	This function handles hiding and toggling bootstrap .navbar
	element when the user scrolls down.
  */
$(function () {
	var lastScrollTop = 0;
	var $navbar = $('.navbar');
	$(window).scroll(function(event){
		var st = $(this).scrollTop();
				
		if (st > lastScrollTop) { // scroll down
			$navbar.fadeOut()
		} else {
			$navbar.fadeIn() // scroll up
		}
		lastScrollTop = st;
	});
});