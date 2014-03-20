package util

class Wrappers {
	static Container wrap(Data data, String type) {
		return wrap(data, type, type)
	}

	static Container wrap(Data data, String idPrefix, String label) {
		int id = 0
		Container container = new Container()
		StringBuilder buffer = new StringBuilder()
		ProcessingStep step = new ProcessingStep()	
		step.metadata['contains'] = label
		boolean skip = false
		data.payload.split('\n+').each { line ->
			def string = line
			boolean add = true
			if (skip) {
				skip = false
				if (line == '.') {
					add = false
				}
			}
			if (add) {
				line = line.replaceAll('-LRB-', '(')
				line = line.replaceAll('-RRB-', ')')
				Annotation a = new Annotation()
				a.id = "${idPrefix}${++id}"	
				a.start = buffer.size()
				buffer.append(line)
				a.end = buffer.size()
				buffer.append('\n')
				a.label = label
				step.annotations.add(a)	
			}
			skip = line.endsWith('.')	
			//println "Wrapped \"${line}\""
		}
		container.steps.add(step)
		container.text = buffer.toString()
		//System.err.println "Container.text: ${container.text}"
		return container
	}

	static String unwrap(Container container) {
		return unwrap(container, 0)
	}

	static String unwrap(Container container, int step) {
		//StringBuilder buffer = new StringBuilder()
		def result = []
		def sorted = container.steps[step].annotations.sort { it.start }
		sorted.each { a ->
			String text = container.text.substring((int)a.start, (int)a.end)
			if (text == '(') {
				text = '-LRB-'
			}
			else if (text == ')') {
				text = '-RRB-'
			}
			result << text
			//buffer.append(a.id)
			//buffer.append(": ")
			//buffer.append(text)
			//buffer.append('\n')		
		}
		//return buffer.toString()
		return result.join('\n')
	}	
}