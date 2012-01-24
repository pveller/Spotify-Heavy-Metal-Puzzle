#ruby generator.rb 100 20 10 0

STOCKHOLM = (1000..1999).entries.shuffle
LONDON = (2000..2999).entries.shuffle

projects = []

teams = ARGV[0].to_i
factor_stockholm = ARGV[1].to_i
factor_london = ARGV[2].to_i

#generate project teams
teams.times {|i| projects << [STOCKHOLM[0 + rand(factor_stockholm)], LONDON[0 + rand(factor_london)]]}

#add our friend a few times here and there i
[ARGV[3].to_i, teams - 1].min.times {|i| projects[i][0] = 1099}

projects.uniq!
projects.shuffle!

filename = "dataset_#{ARGV[0]}_#{ARGV[1]}_#{ARGV[2]}_#{ARGV[3]}.txt"

#input file for Java and Ruby solvers
File.open(filename, "w+") do |file|
	file.puts projects.length
	projects.each {|p| file.puts p.join(" ")}
end

#input string for Prolog
File.open(filename + ".pl", "w+") do |file|
	command = ""
	projects.each {|p| command += "(" + p.join(",") + "),"}
	file.puts ("solve([" + command.chop + "], Solution)")
end
