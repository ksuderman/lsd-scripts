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
		
		def originalText = params.originalText
		int index = -1
		if (params.step) {
			index = params.step as int
		}
		if (index < 0) {
			container.steps.each { ProcessingStep step ->
				align(originalText, container.text, step.annotations)
			}
		}
		else {
			align(originalText, container.text, step.annotations)
		}
		container.text = originalText
		return DataFactory.json(container.toJson())
	}
	
	void align(String originalText, String text, List annotations) {
		def sorted = annotations.sort { it.start }
		// non-breaking space character.
		String nbspace = "${160 as char}"
		int index = 0
		sorted.each {
			int start = it.start as int
			int end = it.end as int
			def string = text.substring(start,end)
			string = string.replaceAll(nbspace, ' ')
			//println "${start}-${end} ${string} (${index})"
			def before = index
			index = originalText.indexOf(string, index)
			if (index < 0) {
				println "Actual ${before} '${actual}'"
				throw new LappsException("Unable to match '${string}' at offset ${before}")
			}
			it.start = index
			it.end = it.start + string.length()
			index = it.end 
		}
	}
}
