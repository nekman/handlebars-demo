(function(exports) {
  'use strict';
 
  /**
   * Gets person model(s) from the server. 
   */
  function PersonController() {
	this.url = exports.BASE_URL;
	this.sortProperties = {};

	this.fetchAll();
  }

  var ControllerProto = PersonController.prototype;

  /**
   * Get all persons.
   */
  ControllerProto.fetchAll = function(url) {
	  return $.get(this.url  + '/json').then(function(persons) {
		this.personsContainer = persons;		
		return persons;
	  }.bind(this));
  };

  ControllerProto.toQueryString = function() {	  
	var qs = location.href.split('?')[1];
	
	return qs && qs.split('&').map(function(part) {
		var parts = part.split('='),
			query = {}
		
		query[parts[0]] = parts[1];
		
		return query;		
	}) || [];
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
	
	$.get(this.url + '/' + userId + '/json').then(dfd.resolve);
	
	return dfd.promise();
  };
  
  ControllerProto.isAscending = function(sortName) {
	  return (this.sortProperties[sortName] || {}).asc;
  };

  /**
   * Client side sorting of persons.
   * @param {String} sortName
   */
  ControllerProto.sortPersons = function(sortName) {
	  if (!this.sortProperties[sortName]) {
		  this.sortProperties[sortName] = { asc: this.toQueryString().asc };
	  }	  
	  var asc = this.sortProperties[sortName].asc = !this.sortProperties[sortName].asc;
	  
	  return this.personsContainer.persons.sort(function(p1, p2) {		  
		 return asc ? p1[sortName] > p2[sortName] : p2[sortName] > p1[sortName];
	  }.bind(this));
  };

  exports.PersonController = PersonController;

}(window.App));
