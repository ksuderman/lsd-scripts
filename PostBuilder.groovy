@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
@GrabExclude('commons-beanutils:commons-beanutils') 
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
 

url = 'http://stanfordneralign.appspot.com/api'
//api='http://localhost:9999/echo.groovy'

String encode (File file) {
	return URLEncoder.encode(file.getText('UTF-8'), 'UTF-8')
}

def json_data = encode(new File('examples/bartok1.json'))
def text_data = encode(new File('examples/bartok1.txt'))

def http = new HTTPBuilder(url)
http.request( POST ) { 
	body = "json=${json_data}&text=${text_data}"
	requestContentType = URLENC
	requestAccept = "text/plain"
	response.success = { resp, json ->
		// response handling here
		println "POST response status: ${resp.statusLine}"
		println "${resp.class.name}"
		println json
	}
	response.'400' = { resp ->
		println "Bad Request."
		println resp.statusLine
	}
	response.'500' = { resp ->
		println "Internal server error."
		println resp.statusLine
	}
}
