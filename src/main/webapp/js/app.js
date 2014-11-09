(function(exports) {
	'use strict';
	
	exports.App = {
	  BASE_URL: '/persons',
	  BASE_TEMPLATE_URL: '/'
	};
	
	$(function() {
		new App.PersonView(new App.PersonController);		
	});

}(window));
