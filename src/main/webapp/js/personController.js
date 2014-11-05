(function($) {
  'use strict';
 
  var BASE_URL = '/persons';
  
  /**
   * Gets person model(s) from the server. 
   */
  function PersonController() {
	this.url = BASE_URL;
  }

  var ControllerProto = PersonController.prototype;

  /**
   * Get a template by url.
   * @param {String} url.
   */
  ControllerProto.fetchHandlebarsTemplate = function(url) {
	  return $.get(url).then(function(source) {
		return Handlebars.compile(source);
	  });
  };
  
  /**
   * Get user details.
   * @param {String} userId.
   */
  ControllerProto.fetchDetails = function(userId) {
	var dfd = $.Deferred();
	
	$.get(this.url + '/' + userId).then(dfd.resolve);
	
	return dfd.promise();
  };
  
  /**
   * View. Interacts with the DOM. 
   */
  function PersonView(controller) {
	  this.controller = controller;	  
	  this.detailTemplateUrl = '/templates/person/details.html';

	  this.controller.fetchHandlebarsTemplate(this.detailTemplateUrl).then(function(tmpl) {
		  this.compiledTemplate = tmpl;
	  }.bind(this));
	  
	  this.$details = $('#details');	  
	  $('button[data-userid]').on('click', this.getPersonDetails.bind(this));
  }
  
  var ViewProto = PersonView.prototype;
  
  /**
   * Gets a person from the controller.
   * Displays person details.
   *
   * @param {Event} e - Click event.  
   */
  ViewProto.getPersonDetails = function(e) {	
	var promise = this.controller.fetchDetails(e.target.dataset.userid);
	
	promise.then(this.displayDetails.bind(this));
  };
  
  /**
   * Displays details of selected person.
   *
   * Uses HTML5-history to show userId in 
   * in the address-bar. If user refresh the page, then
   * the REST-API will respond with the selected user
   * rendered in HTML.
   * 
   * @param {Object} person
   */
  ViewProto.displayDetails = function(person) {
	history.replaceState(null, '', BASE_URL + '/' + person.userId);

	var html = this.compiledTemplate({
	  selected: person
	});
	
	this.$details.html(html);
  }
  
  // Start when DOM is ready.
  $(function() {
	  new PersonView(new PersonController);
  });

}(jQuery));
