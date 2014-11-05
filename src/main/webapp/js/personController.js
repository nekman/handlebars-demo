(function($) {
  'use strict';
 
  var BASE_URL = '/persons';
  
  /**
   * Gets person model(s) from the server. 
   */
  function PersonController() {
	this.url = BASE_URL;
	this.fetchPersons();
  }

  var ControllerProto = PersonController.prototype;

  /**
   * Gets all persons.
   */
  ControllerProto.fetchPersons = function() {
	return $.get(this.url).then(function(persons) {
	  this.persons = persons;
	}.bind(this));	
  };

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
  
  /**
   * Gets a person from the controller.
   * Displays person details.
   *
   * @param {Event} e - Click event.  
   */
  PersonView.prototype.getPersonDetails = function(e) {	
	var promise = this.controller.fetchDetails(e.target.dataset.userid);
	
	promise.then(function(person) {
		history.replaceState(null, '', BASE_URL + '/' + person.userId);
		var html = this.compiledTemplate({
		  selected: person
		});
		
		this.$details.html(html);
	  
	}.bind(this));
  };
  
  // Start when DOM is ready.
  $(function() {
	  new PersonView(new PersonController);
  });

}(jQuery));
