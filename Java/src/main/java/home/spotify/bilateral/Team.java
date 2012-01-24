package home.spotify.bilateral;

/*
 * A value object representing a team of two employees working on a project 
 */
public class Team {

	private final Employee stockholmEmployee;
	private final Employee londonEmployee;
	private final int hashcode;
	
	public Team(final Employee stockholmEmployee, final Employee londonEmployee) {
		this.stockholmEmployee = stockholmEmployee;
		this.londonEmployee = londonEmployee;
		
		final StringBuilder hashCode = new StringBuilder();
		hashCode.append(stockholmEmployee.getId()).append(londonEmployee.getId());
		
		this.hashcode = Integer.parseInt(hashCode.toString()); 
	}

	public Employee getStockholmEmployee() {
		return stockholmEmployee;
	}

	public Employee getLondonEmployee() {
		return londonEmployee;
	}

	@Override
	public String toString() {
		return "Team [" + hashCode() + "] -> [" + stockholmEmployee + " : " + londonEmployee + "]";
	}

	@Override
	public int hashCode() {
		return hashcode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final Team other = (Team) obj;
		if (londonEmployee == null) {
			if (other.londonEmployee != null) {
				return false;
			}
		} else if (!londonEmployee.equals(other.londonEmployee)) {
			return false;
		}
		
		if (stockholmEmployee == null) {
			if (other.stockholmEmployee != null) {
				return false;
			}
		} else if (!stockholmEmployee.equals(other.stockholmEmployee)) {
			return false;
		}
		
		return true;
	}


	
}
