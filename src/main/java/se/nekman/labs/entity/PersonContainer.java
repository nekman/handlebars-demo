package se.nekman.labs.entity;

import static java.util.Arrays.asList;

import java.util.List;

public class PersonContainer {

	private List<Person> persons;
	private Person selected;
	
	public PersonContainer() {
		persons = asList(
    			new Person("Adam", 21),
    			new Person("Ben", 22),
    			new Person("Ceasar", 23));    
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
