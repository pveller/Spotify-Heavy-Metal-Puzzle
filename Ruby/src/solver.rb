class Solver
	attr_accessor :projects, :employees, :attendees
	
	def initialize(fileName)
		# a project is a team of two. collect all projects into an array
		@projects = File.readlines(fileName).grep(/^\d+\s\d+$/).collect {|line| line.split(/\s/)}

		# employee ID as a key and project participation index as a value, default to 0
		@employees = Hash.new(0)

		# calculate participation index
		@projects.flatten.each {|e| @employees[e] += 1 }
		@attendees = []
	end
	
	def optimize()
		project = @projects.sort_by! {|p| (@employees[p.first] - @employees[p.last]).abs}.last

		@attendees << 
			if (@employees[project.first] != @employees[project.last])
				# clean win
				(@employees[project.first] > @employees[project.last]) ? project.first : project.last
			else
				# a tie with a prefernece to "a friend"
				project.include?("1099") ? "1099" : project[rand(2)]
			end
		
		# discard projects that the attendee participates in 
		# and reduce  participation index of the individuals on those teams
		@projects.reject! {|p| p.include?(@attendees.last) && p.each {|e| @employees[e] -= 1}} 
		
		optimize() if @projects.length > 0
	end
end

solver = Solver.new(ARGV[0])
solver.optimize()

puts solver.attendees.length
solver.attendees.each {|attendee| puts attendee}