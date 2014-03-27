import javax.servlet.http.HttpServletResponse
import groovy.json.JsonBuilder

if (!params.who) {
	response.sendError(400, 'Missing who parameter.')
	return
}

html.html {
	head {
		title 'Greetings'
	}
	body {
		p("Hello ${params.who ?: 'World'}")
	}
}


/*
out.println "Request is a ${request.class.name}"
out.println "Parameter map."
request.parameterMap.each { name,value ->
	out.println "${name}=${value}"
}

out.println "params."
params.each { name,value ->
	out.println "${name}=${value}"
}

out.println "headers."
headers.each { name,value ->
	out.println "${name}=${value}"
}
*/
