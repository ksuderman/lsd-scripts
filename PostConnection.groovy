@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
@GrabExclude('commons-beanutils:commons-beanutils') 
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
 

url = 'http://stanfordneralign.appspot.com/api'
//url='http://localhost:9999/echo.groovy'

String encode (File file) {
	return URLEncoder.encode(file.getText('UTF-8'), 'UTF-8')
}

def json_data = encode(new File('examples/bartok1.json'))
def text_data = encode(new File('examples/bartok1.txt'))

def charset = 'UTF-8'
def connection = new URL(url).openConnection()
connection.setDoOutput(true)
connection.setRequestProperty("Accept", "text/plain, text/xml, text/html, application/json")
connection.setRequestProperty("Accept-Charset", charset);
connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
connection.outputStream.withWriter { writer ->
	//writer << body
	writer << "json=${json_data}&text=${text_data}"//.getBytes(charset)
}

String response = connection.inputStream.withReader { reader -> reader.text }
println connection.responseCode
println response

connection.headerFields.each { name, list ->
	println "Header: ${name}: ${list.join(', ')}"
}