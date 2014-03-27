package util

class LocalAlignmentService implements WebService {
	long[] requires() { return [ Types.JSON ] as long[] }
	long[] produces() { return [ Types.JSON ] as long[] }
	Data configure(Data ignore) {
		return DataFactory.error("Unsupported operation.")
	}
	
	Data execute(Data input) {
		if (input.discriminator == Types.ERROR) {
			return input
		}
		
		if (input.discriminator != Types.JSON) {
			String type = DiscriminatorRegistry.get(input.discriminator)
			return DataFactory.error("Invalid input type. Expected json but found ${type}")
		}
		
		Container container = new Container(input.payload)
		def params = container.metadata.alignParams
		if (!params) {
			return DataFactory.error("No alignment parameters found.")
		}
		
		def text = params.originalText
		int step = 0
		if (params.step) {
			step = params.step as int
		}
		int index = 0
		def sorted = container.steps[step].annotations.sort { it.start }
		// non-breaking space character.
		String nbspace = "${160 as char}"
		sorted.each {
			int start = it.start as int
			int end = it.end as int
			def string = container.text.substring(start,end)
			string = string.replaceAll(space, ' ')
			//println "${start}-${end} ${string} (${index})"
			def before = index
			index = text.indexOf(string, index)
			if (index < 0) {
				println "Actual ${before} '${actual}'"
				throw new LappsException("Unable to match '${string}' at offset ${before}")
			}
			it.start = index
			it.end = it.start + string.length()
			def matched = text.substring(it.start as int, it.end as int)
			//println "${it.start}-${it.end} ${string} -> ${matched}"
			index = it.end 
		}
		container.text = text
		return container
	}
}
