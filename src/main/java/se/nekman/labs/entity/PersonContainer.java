package se.nekman.labs.entity;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * Holds the list of persons
 * and the selected person (if any).
 */
public class PersonContainer {

	private final List<Person> persons;
	private Person selected;
	
	public PersonContainer() {
		persons = asList(
			new Person("Julia", 21),
			new Person("Sarah", 22),    			
			new Person("Adam", 22),
			new Person("Ben", 23),
			new Person("Ceasar", 23)
		)
		.stream()
		.sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
		.collect(toList());
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
