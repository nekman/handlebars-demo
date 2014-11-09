 (function(exports) {
  'use strict';
 
  /**
   * View. Interacts with the DOM. 
   */
  function PersonView(controller) {
	  this.controller = controller;	  
	  this.detailTemplateUrl = '/app/templates/person/details.html';
	  this.personTableTemplateUrl = '/app/templates/person/person-table.html';
	  
	  this.initialize();
  }
  
  var ViewProto = PersonView.prototype;
  
  /**
   * Gets the templates from the server.
   * Could be extended to be cached in e.g. localStorage.
   */
  ViewProto.fetchTemplates = function() {
	  var fetch = this.controller.fetchHandlebarsTemplate;
	  
	  return $.when(fetch(this.detailTemplateUrl), fetch(this.personTableTemplateUrl))
	  	.then(function(detailTmpl, tableTmpl) {
		  this.detailTemplate = detailTmpl;
		  this.tableTemplate = tableTmpl;
	  }.bind(this));
  };
  
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
	history.replaceState(null, '', exports.BASE_URL + '/' + person.userId + location.search);

	var html = this.detailTemplate({
	  selected: person
	});
	
	this.$details.html(html);
  };
  
  /**
   * Sorts the persons on the client.
   * Updates the url with the History API.
   * @param {Event} e
   */
  ViewProto.handleSort = function(e) {
	var sortName = e.target.dataset.sort,	
		sortedPersons = this.controller.sortPersons(sortName);
	
	var html = this.tableTemplate({
	  persons: sortedPersons
	});
	
	history.replaceState(null, '', 
		'?sort=' + sortName + '&asc=' + this.controller.isAscending(sortName));
	
	this.$el.find('tbody').html(html);
	
	e.preventDefault();
  };
  
  /**
   * Initialize view, fetch templates.
   */
  ViewProto.initialize = function() {
	  this.$el = $('#persons');
	  
	  this.$details = this.$el.find('#details');
	  this.$el.find('a[data-sort]').on('click', this.handleSort.bind(this));
	  this.$el.find('tbody').on('click', 'button[data-userid]', this.getPersonDetails.bind(this));

	  this.fetchTemplates();
  };
  
  exports.PersonView = PersonView;

}(window.App));
