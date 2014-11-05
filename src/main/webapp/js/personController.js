(function() {
  'use strict';
 
  //
  // PersonController
  //
  function PersonController() {
	this.url = '/app/persons';

	this.fetchPersons();
  }

  var ControllerProto = PersonController.prototype;
  
  ControllerProto.fetchPersons = function() {
	return $.get(this.url).then(function(persons) {
	  this.persons = persons;
	}.bind(this));	
  };
  
  ControllerProto.fetchHandlebarsTemplate = function(url) {
	  return $.get(url).then(function(source) {
		return Handlebars.compile(source);
	  });
  };
  
  ControllerProto.fetchDetails = function(userId) {
	var dfd = $.Deferred();
	
	$.get(this.url + '/' + userId).then(dfd.resolve);
	
	return dfd.promise();
  };
  
  //
  // PersonView
  //
  function PersonView(controller) {
	  this.controller = controller;	  
	  this.detailTemplateUrl = '/app/templates/person/details.html';

	  this.controller.fetchHandlebarsTemplate(this.detailTemplateUrl).then(function(tmpl) {
		  this.compiledTemplate = tmpl;
	  }.bind(this));
	  
	  this.$details = $('#details');	  
	  $('button[data-userid]').on('click', this.handleClick.bind(this));
  }
  
  PersonView.prototype.handleClick = function(e) {	
	var promise = this.controller.fetchDetails(e.target.dataset.userid);
	
	promise.then(function(person) {
		history.replaceState(null, '', '/app/persons/' + person.name);

		var html = this.compiledTemplate({
		  selected: person
		});
		
		this.$details.html(html);
	  
	}.bind(this));
  };
  
  new PersonView(new PersonController);
}());
