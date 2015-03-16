class SimpleClass implements WebService {
	long[] produces() { return [ Types.JSON ] as long[] }
	long[] requires() { return [ Types.JSON ] as long[] }
	Data configure(Data ignored) {
		return DataFactory.error("Unsupported operation.")
	}
	
	Data execute(Data input) {
		return DataFactory.text("Hello World")
	}	
}