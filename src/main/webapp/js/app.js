(function(exports) {
	'use strict';
	
	exports.App = {
	  BASE_URL: '/persons'
	};
	
	$(function() {
		new App.PersonView(new App.PersonController);		
	});

}(window));
