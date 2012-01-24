package home.spotify.bilateral;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Designed to solve the Spotify's "Bilateral Projects" puzzle 
 * using a brute force algorithm.
 *  
 * More at: https://www.spotify.com/us/jobs/tech/bilateral-projects 
 */
public class Solver {

	// SPEC: Your friend has ID 1009
	private final static int MY_FRIEND_ID = 1009;
	
	private File inputFile;
	private int numberOfTeams;
	
	/*
	 * A list of projects (a project is represented by a Team object)
	 */
	private Set<Team> projects;
	

	private Solver () {
	}
	

	public void solve(final File inputFile) {
		initialize(inputFile);
		
		collect();
		
		optimize();
	}
	
	private void initialize(final File inputFile) {
		this.inputFile = inputFile;
		this.numberOfTeams = 0;
		this.projects = new HashSet<Team>();
	}
	
	/**
	 * Reads the input file, runs all assertions to make sure no breaking format errors,
	 * and populates the projects list for further optimization.
	 */
	private void collect() {
		FileReader reader = null;
		try {
			reader = new FileReader(inputFile);
			final LineNumberReader byLineReader = new LineNumberReader(reader);

			String nextLine = null;
			while ((nextLine = byLineReader.readLine()) != null) {
				// don't consider leading and trailing spaces a breaking format issue 
				nextLine = nextLine.trim();
				
				if (byLineReader.getLineNumber() == 1) {
					// SPEC: The first line of input contains an integer 1 <= m <= 10 000, the number of teams
					try {
						numberOfTeams = Integer.parseInt(nextLine);
					} catch (NumberFormatException ex) {
						assert false : 
							"First line [" + nextLine + "] isn't properly formatted. Numeric value expected"; 
					}

					assert numberOfTeams >= 1 : "Number of teams is expected to be greater than zero";
					assert numberOfTeams <= 10000 : "Number of teams is expected to be not greater than 10,000";
					
				} else if ((byLineReader.getLineNumber() - 1) > numberOfTeams) {
					/* we could report it as a breaking format failure but the task description
					 * didn't say there's nothing else in the file after M lines so let's just stop here.
					 * Rewind the line number back by 1 to make it look like we only read as much as we needed  
					 */
					byLineReader.setLineNumber(byLineReader.getLineNumber() - 1);
					
					break;
					
				} else {
					// SPEC: The following m lines each contain two integers, i, j separated by a space, 
					//       being the employee IDs of the two employees in that team
					
					final String[] employeeIDs = nextLine.split("\\s");
					assert (nextLine.matches("^\\d+\\s\\d+$")) && (employeeIDs.length == 2) : 
						"Line [" + byLineReader.getLineNumber() + "] isn't properly formatted. ^\\d+\\s\\d+$ expected";
					
					try {
						final int projectID = byLineReader.getLineNumber();
						
						// SPEC: The first one is from Stockholm and the second one is from London
						final int stockholmEmployeeID = Integer.parseInt(employeeIDs[0]);
						final int londonEmployeeID = Integer.parseInt(employeeIDs[1]);
						
						// SPEC: Stockholm employees have IDs in the range 1000 to 1999
						assert (stockholmEmployeeID >= 1000 && stockholmEmployeeID <= 1999) : 
							"stockholm employee [" + stockholmEmployeeID + "] on line [" + projectID + "] is out of [1000:1999] range";
						
						// SPEC: London employees have IDs in the range 2000 to 2999
						assert (londonEmployeeID >= 2000 && londonEmployeeID <= 2999) : 
							"stockholm employee [" + londonEmployeeID + "] on line [" + projectID + "] is out of [2000:2999] range";
						
						final Employee stockholmEmployee = new Employee(stockholmEmployeeID);
						final Employee londonEmployee = new Employee(londonEmployeeID);

						final Team team = new Team(stockholmEmployee, londonEmployee);
						
						// SPEC: An employee can be a member of several teams, 
						//       but there cannot be several teams consisting of the same pair of employees
						if (projects.contains(team)) {
							assert false : "Team [" + team + "] already participates in another project";
						}
						
						projects.add(team);
						
					} catch (NumberFormatException e) {
						assert false : 
							"One of the employee IDs on line [" + byLineReader.getLineNumber() + "]:[" + nextLine + "] isn't a numeric value";
					}
				}
			}
			
			assert (numberOfTeams == (byLineReader.getLineNumber() - 1)) : 
				"Number of teams doesn't match number of lines processed from the input file";
			
		} catch (IOException ex) {
			assert false : 
				"Can't read [" + inputFile + "] due to an error: " + ex.getMessage();
			
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
					// do nothing
				}
			}
		}
	}
	
	/**
	 * SPEC: computes the smallest number of people that must be invited in order to get at least one person from each project, 
	 * as well as a list of people to invite. If possible (subject to the set of people being smallest possible), 
	 * the list of invitees should include your friend.
	 */
	private void optimize() {
		// sort by length
		final List<Set<Employee>> allPossibleSolutions = generateAllSolutionsList();
		Collections.sort(allPossibleSolutions, 
				new Comparator<Set<Employee>>() {
					@Override
					public int compare(Set<Employee> set1, Set<Employee> set2) {
						return set1.size() - set2.size();
					}
				});
		
		final int smallest = allPossibleSolutions.get(0).size();
		
		// reduce to only optimal solutions
		final List<Set<Employee>> optimalSolutions = new ArrayList<Set<Employee>>();
		for (Set<Employee> s : allPossibleSolutions) {
			if (s.size() > smallest) {
				break; // sorted list so we know all others are even larger in size
			} else {
				optimalSolutions.add(s);
			}
		}
		
		// SPEC: If possible (subject to the set of people being smallest possible), 
		//       the list of invitees should include your friend
		final Employee myFriend = new Employee(MY_FRIEND_ID);
		
		// reduce to those having our "friend"
		final List<Set<Employee>> bestSolutions = new ArrayList<Set<Employee>>();
		for (Set<Employee> s : optimalSolutions) {
			if (s.contains(myFriend)) {
				bestSolutions.add(s);
			}
		}
		
		final List<Set<Employee>> solutions = !bestSolutions.isEmpty() ? bestSolutions : optimalSolutions; 
		final Set<Employee> solution = solutions.get(0); 
		
		// SPEC: Output first a single line with an integer k indicating 
		//       the smallest number of employees that must be invited
		System.out.println(solution.size());
		
		// SPEC: Then output k lines giving the IDs of employees to invite. 
		for (Employee e : solution) {
			System.out.println(e.getId());
		}
	}
	
	/**
	 * Builds a tree of all possible solutions using List<Set<Employee>> to represent all combinations.
	 * 
	 * @return
	 */
	private List<Set<Employee>> generateAllSolutionsList() {
		final List<Set<Employee>> accummulatedSolutions = new ArrayList<Set<Employee>>();
		accummulatedSolutions.add(new HashSet<Employee>()); // root of the solutions tree
		
		for (final Team team : projects) {
			final List<Set<Employee>> newBranch = new ArrayList<Set<Employee>>();
					
			for (Set<Employee> solution : accummulatedSolutions){
				final Set<Employee> solutionSibling = new HashSet<Employee>(solution);

				solution.add(team.getLondonEmployee());  // "left" leaf node (existing branch)
				solutionSibling.add(team.getStockholmEmployee()); // "right" leaf node (new branch)
				
				newBranch.add(solutionSibling);
			}
			
			accummulatedSolutions.addAll(newBranch);
		}
		
		return accummulatedSolutions;
	}
	
	/**
	 * @param args full path to the input file
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("Please provide a full path to the input file. \n " + 
							   "Read more about file format requirements at https://www.spotify.com/us/jobs/tech/bilateral-projects");
			return;
		}
		
		final String inputFilePath = args[0];
		final File inputFile = new File(inputFilePath);

		assert inputFile.exists() : "File [" + inputFilePath + " => " + inputFile.getAbsolutePath() + "] cannot be found";

		final Solver solver = new Solver();
		solver.solve(inputFile);
	}

}
