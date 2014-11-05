package se.nekman.labs.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.Response.ok;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import se.nekman.labs.entity.Person;
import se.nekman.labs.entity.PersonContainer;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ServletContextTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

@Path("/")
public class PersonService {

    @Context private ServletContext context;
	private PersonContainer personContainer;

    public PersonService() throws IOException {
    	personContainer = new PersonContainer();
    }

	@GET
	@Path("persons")
	public Response sendModel(@HeaderParam("Accept") String header) throws IOException {
		if (isTextPlain(header)) {	
			return responseHTML(createHandlebarsTemplate().apply(personContainer));			
		}
		
		return responseJSON(personContainer);
	}

	@GET
	@Path("persons/{userId}")
	public Response getPersonByName(final @PathParam("userId") String userId,
			@HeaderParam("Accept") String header) throws IOException {
		
		List<Person> persons = personContainer.getPersons();		
		
		Person person = persons				
			.stream()
			.map(p -> {
				p.setSelected(false);
				return p;
			})
			.filter(p -> p.getName().equalsIgnoreCase(userId))				
			.findFirst()
			.orElse(null);
		
		
		personContainer.setSelected(person);

		if (isTextPlain(header)) {	
			return responseHTML(createHandlebarsTemplate().apply(personContainer));			
		}
		
		return responseJSON(person);
	}
	
	private Template createHandlebarsTemplate() throws IOException {
		TemplateLoader loader = new ServletContextTemplateLoader(context);    	
		loader.setPrefix("/templates/person/");
		loader.setSuffix(".html");
		Handlebars handlebars = new Handlebars(loader);
		
		return handlebars.compile("persons");
	}
	
	
	private static boolean isTextPlain(String header) {
		return header != null && header.contains(TEXT_HTML);
	}

	private static Response responseHTML(Object entity) {
		return response(entity, TEXT_HTML);
	}
	
	private static Response responseJSON(Object entity) {
		return response(entity, APPLICATION_JSON);
	}
	
	private static Response response(Object entity, String mediaType) {
		return ok(entity)
				.header("Content-Disposition", TEXT_HTML)
				.build();
	}
	
	/*var xhr = new XMLHttpRequest();
	xhr.open('GET', document.location.href);
	xhr.onload = function() {
	  console.log(xhr.responseText);
	};
	xhr.send();*/
}