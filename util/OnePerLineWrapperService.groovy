package util

class OnePerLineWrapperService implements WebService {
	
	long[] requires() { return [ Types.JSON ] as long[] }
	long[] produces() { return [ Types.JSON ] as long[] }
	
	Data configure(Data ignored) {
		//throw new UnsupportedOperationException("The configuration option is not supported.")
		return DataFactory.error("Unsupported operation.")
	}
	
	Data execute(Data input) {
		if (input.discriminator != Types.JSON) {
			String type = DiscriminatorRegistry.get(input.discriminator)
			return DataFactory.error("Invalid input type. Expected JSON but found ${type}")
		}
		Container container = new Container(input.payload)
		def params = container.metadata.params
		if (!params) {
			return DataFactory.error("No wrapping parameters provided with the Data.")
		}		
		if (!params.prefix) {
			return DataFactory.error("No ID prefix specified.")
		}
		if (!params.label) {
			return DataFactory.error("No annotation label specified.")
		}
		
		Data result = null
		try {
			Container c = Wrapper.wrap(params.prefix, params.label, container.text)
			c.metadata = container.metadata
			result = DataFactory.json(c.toJson())
		}
		catch (Exception e) {
			result = DataFactory.error("Unable to wrap the input text", e)
		}
		return result
	}
}