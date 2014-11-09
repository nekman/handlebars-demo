package se.nekman.labs.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.Response.ok;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import se.nekman.labs.entity.Person;
import se.nekman.labs.entity.PersonsViewModel;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ServletContextTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

/**
 * Person REST-service.
 * 
 * Looks on the accept header to determine if
 * the client should be served with JSON or HTML.
 * 
 * The HTML is generated with Handlebars.java,
 * using the same Handlebars templates as the 
 * client does.  
 *
 */
@Path("/")
public class PersonService {

    @Context 
    private ServletContext context;
    
	private final PersonsViewModel personContainer;	

    public PersonService() throws IOException {
    	personContainer = new PersonsViewModel();    	
    }
    
	@GET
	@Path("persons")
	public Response findAll(
			@HeaderParam("Accept") String header,
			@QueryParam("sort") String sortProperty,
			@QueryParam("asc") boolean isAscending) throws IOException {

		personContainer.sortPersons(sortProperty, isAscending);

		return responseHTML(createHandlebarsTemplate().apply(personContainer));
	}

	@GET
	@Path("persons/{userId}")
	public Response getPersonByName(@PathParam("userId") String userId,
			@QueryParam("sort") String sortProperty,
			@QueryParam("asc") boolean isAscending) throws IOException {
		
		findAndSort(userId, sortProperty, isAscending);

		return responseHTML(createHandlebarsTemplate().apply(personContainer));
	}
	
	
	/**
	 * Due to HTML5 application cache, we need to 
	 * explicit say that we want JSON back (by adding /json) to the
	 * end of the URL. 
	 * 
	 * Without HTML5 application cache enabled, we don't need this.
	 * Instead we could just look at the @HeaderParam("Accept") header
	 * and send HTML for text/html requests, and JSON for application/json-requests.
	 * 
	 * @param sortProperty
	 * @param isAscending
	 * @return {@link Response}
	 * @throws IOException
	 */
	@GET
	@Path("persons/{userId}/json")
	public Response getPersonByNameJSON(@PathParam("userId") String userId,
			@QueryParam("sort") String sortProperty,
			@QueryParam("asc") boolean isAscending) throws IOException {

		Person person = findAndSort(userId, sortProperty, isAscending);
		
		return responseJSON(person);
	}
	
	/**
	 * Due to HTML5 application cache, we need to 
	 * explicit say that we want JSON back (by adding /json) to the
	 * end of the URL. 
	 * 
	 * Without HTML5 application cache enabled, we don't need this.
	 * Instead we could just look at the @HeaderParam("Accept") header
	 * and send HTML for text/html requests, and JSON for application/json-requests.
	 * 
	 * @param sortProperty
	 * @param isAscending
	 * @return {@link Response}
	 * @throws IOException
	 */
	@GET
	@Path("persons/json")
	public Response findAllJSON(
			@QueryParam("sort") String sortProperty,
			@QueryParam("asc") boolean isAscending) throws IOException {

		personContainer.sortPersons(sortProperty, isAscending);

		return responseJSON(personContainer);
	}

	
	private Template createHandlebarsTemplate() throws IOException {
		TemplateLoader loader = new ServletContextTemplateLoader(context);    	
		loader.setPrefix("/templates/person/");
		loader.setSuffix(".html");
		Handlebars handlebars = new Handlebars(loader);
		
		return handlebars.compile("persons");
	}

	private Person findAndSort(String userId, String sortProperty,
			boolean isAscending) {
		Person person = personContainer.getPersons()				
			.stream()
			.filter(p -> p.getName().equalsIgnoreCase(userId))				
			.findFirst()
			.orElse(null);

		personContainer.setSelected(person);
		personContainer.sortPersons(sortProperty, isAscending);
		
		return person;
	}

	private static Response responseHTML(Object entity) {
		return response(entity, TEXT_HTML);
	}
	
	private static Response responseJSON(Object entity) {
		return response(entity, APPLICATION_JSON);
	}
	
	private static Response response(Object entity, String mediaType) {
		return ok(entity)
				.header("Content-Type", mediaType)
				.build();
	}
}
