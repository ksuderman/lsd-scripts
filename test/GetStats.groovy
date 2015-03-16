import java.text.SimpleDateFormat

if (args.size() == 0) {
	prinltn "No input file provided."
	return
}

File file = new File(args[0])
if (!file.exists()) {
	println "Input file not found."
	return
}

int parseTime(String time) {
	(minutes, seconds, millis) = time.split(":")
	return millis.toInteger() + (seconds.toInteger() * 1000) + (minutes.toInteger() * 60000)
}

int docs = 0
int totalWords = 0
int totalTime = 0
int longest = -1
int shortest = Integer.MAX_VALUE
float fastest = -1.0
float slowest = Float.MAX_VALUE
boolean running = true
file.eachLine { line ->
	if (running) {
		if (line.startsWith("The following")) {
			running = false
		}
		else {
			String[] parts = line.split("\t")
			int count = Integer.parseInt(parts[2])
			int time = parseTime(parts[3])
			if (time > longest) longest = time
			if (time < shortest) shortest = time;
			float wps = count / (time / 1000)
			if (wps > fastest) fastest = wps
			if (wps < slowest) slowest = wps
			totalWords += count
			totalTime += time
			++docs
		}
	}
}


def df = new SimpleDateFormat("mm:ss:SSS")
df.setTimeZone(TimeZone.getTimeZone("UTC"));
def withHours = new SimpleDateFormat("HH:mm:ss:SSS")
withHours.setTimeZone(TimeZone.getTimeZone("UTC"));

println "Longest time\t" + df.format(longest)
println "Shortest time\t" + df.format(shortest)
println "Average time\t" + df.format((int)(totalTime / docs))
println "Total words\t" + totalWords
println "Total time\t" + withHours.format(totalTime) 
println "Words/Second\t" + (totalWords / (totalTime/1000))
println "Fastest\t" + fastest
println "Slowest\t" + slowest