//@Grab('org.anc.lapps:serialization:0.12.0')
//import org.anc.lapps.serialization.*
import groovy.json.JsonSlurper
import javax.servlet.http.HttpServletResponse

if (!params.text) {
	response.sendError(400, 'Missing text parameter.')
	return
}
if (!params.json) {
	response.sendError(400, 'Missing json parameter.')
	return
}

def json = new JsonSlurper().parseText(params.json)
def text = params.text
int step = 0
if (params.step) {
	step = params.step as int
}

html.html {
	head {
		title 'Alignment'
	}
	body {
		p "Aligned step $step"
		p "$text"
	}
}

	