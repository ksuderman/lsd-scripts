package util

class RemoteAlignmentService implements WebService {
	final static String endpoint = 'http://stanfordneralign.appspot.com/api'
	Http http = new Http(endpoint)
	
	long[] requires() { return [ Types.JSON ] as long[] }
	long[] produces() { return [ Types.JSON ] as long[] }
	Data configure(Data ignored) {
		throw new UnsupportedOperationException("The configuration option is not supported.")
	}
	
	Data execute(Data input) {
		if (input.discriminator != Types.JSON) {
			String type = DiscriminatorRegistry.get(input.discriminator)
			return DataFactory.error("Invalid input type. Expected JSON but found ${type}")
		}
		
		Container container = new Container(input.payload)
		def params = container.metadata.alignParams
		if (!params) {
			return DataFactory.error("No alignment parameters provided with the Data.")
		}
		
		String originalText = params.originalText
		if (!originalText) {
			return DataFactory.error("The original text was not included in the alignment parameters.")
		}
		
		String step = params.step ?: '0'
		println "Invoking remote HTTP alignment service."
		def response = http.post(text:originalText, json:container.toJson(), step:step)
		if (response.code != 200) {
			String message = "Alignment service returned error code ${response.code}\n\n${response.body}"
			return DataFactory.error(response.body)
		}
		
		Container aligned = new Container(response.body)
		aligned.text = originalText
		return DataFactory.json(aligned.toJson())
	}
}