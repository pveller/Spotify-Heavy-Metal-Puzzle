package home.spotify.bilateral;

/**
 * A value object representing an employee
 */
public class Employee {

	private final int id;
	
	public Employee(final int id) {
		assert id > 0 : "Employee ID [" + id + "] is not valid";
		
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Employee [id=" + getId() + "]";
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final Employee other = (Employee) obj;
		return id == other.id;
	}
	
	
	
}
