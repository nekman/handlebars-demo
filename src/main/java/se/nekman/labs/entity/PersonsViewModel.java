package se.nekman.labs.entity;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Holds the list of persons
 * and the selected person (if any).
 */
public class PersonsViewModel {

    private final Map<String, Function<Boolean, Comparator<Person>>> map = new HashMap<>();

	private List<Person> persons;
	private Person selected;
	
	public PersonsViewModel() {
		// Create mock persons
		persons = asList(
			new Person("Julia", 21),
			new Person("Sarah", 22),    			
			new Person("Adam", 22),
			new Person("Ben", 23),
			new Person("Ceasar", 23)
		);
		
		map.put("age", asc -> asc ? comparing(Person::getAge) : comparing(Person::getAge).reversed());
    	map.put("name", asc -> asc ? comparing(Person::getName) : comparing(Person::getName).reversed());
	}

	/**
	 * Sorts persons by the given sortProperty (if any).
	 *
	 * @param sortProperty
	 * @param isAscending
	 */
	public void sortPersons(String sortProperty, boolean isAscending) {
		if (sortProperty != null && map.containsKey(sortProperty)) {			
			persons = persons
			 .stream()
			 .sorted(map.get(sortProperty).apply(isAscending))
			 .collect(toList());
		}
	}
	
	public List<Person> getPersons() {		
		return persons;
	}

	public void setSelected(Person person) {
		this.selected = person;
	}

	public Person getSelected() {
		return selected;
	}
}
