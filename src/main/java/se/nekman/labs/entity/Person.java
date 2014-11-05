package se.nekman.labs.entity;


/**
 * Simple demo Person POJO.
 */
public class Person {
	
	public static final Person EMPTY = new Person("", 0);

	private final String name;
	private final int age;
	
	private final Details details;

	private boolean selected;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;	
		
		this.details = new Details();
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUserId() {
		return name;
	}
	
	public int getAge() {
		return age;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public class Details {
		public int getShoeSize() {
			return age + 20;
		}
		public int getPostalCode() {
			return 55500 + age;
		}
	}
}
