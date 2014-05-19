package util

class Http {
	private static final String UTF8 = 'UTF-8'
	URL url
	List accept = []
	
	public Http(URL url) {
		this.url = url
	}
	
	public Http(String url) {
		this.url = new URL(url)
	}
	
	String encode(input) {
		if (input instanceof String) {
			return URLEncoder.encode(input, UTF8)
		}
		return input
	}
	
	void accept(String contentType) {
		accept << contentType
	}
	
	Response post(Map query) {
		List params = []
		query.each { name, value ->
			params << "${name}=${encode(value)}"
		}
		return post(params.join('&'))
		
	}
	
	Response post(String query) {
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", UTF8);
        if (accept.size() > 0) {
        	connection.setRequestProperty('Accept', accept.join(','))
        }
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + UTF8);
        OutputStream output = connection.getOutputStream();
        try {
            output.write(query.getBytes(UTF8));
        } finally {
            try { output.close(); } catch (IOException ignored) {}
        }
        
        Response response = new Response()
        connection.headerFields.each { name,value ->
        	if (name) {
        		response.headers[name] = value
        	}
        }
        
        response.code = connection.responseCode
        response.encoding = connection.contentEncoding
        response.contentType = connection.contentType
        response.message = connection.responseMessage
        if (response.code == 200) {
        	response.body = connection.inputStream.text
        }
        else {
        	response.body = connection.errorStream.text
        }
        /*
        if (response.code != 200) {
        	response.body = connection.responseMessage
        }
        else {
	        response.body = connection.inputStream.text;			
	    }
	    */
        return response
	}

	Response get(String query) {
		HttpURLConnection connection = (HttpURLConnection) new URL(query).openConnection()
		connection.setRequestMethod("GET")
		
		Response response = new Response()
		response.code = connection.responseCode
		connection.headerFields.each { name,value ->
			if (name) {
				response.headers[name] = value
			}
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.inputStream))
		StringBuilder buffer = new StringBuilder()
		String line = reader.readLine()
		while (line != null) {
			buffer << line
			buffer << "\n"
			line = reader.readLine()
		}
		response.body = buffer.toString()
		return response
	}	
	
	public class Response {
		int code
		String encoding
		String contentType
		String message
		Map headers = [:]
		String body
	}
} 
/**
class Http {
	def http
	ContentType requestType
	List<String> acceptTypes
	
	public Http(String url) {
		this(url, ContentType.URLENC, [])
	}
	
	public Http(String url, ContentType type) {
		this(url, type, [])
	}
	
	public Http(String url, ContentType type, String accept) {
		this(url, type, [ accept ])
	}
	
	public Http(String url, ContentType type, List accept) {
		this.http = new HTTPBuilder(url)
		this.requestType = type
		this.acceptTypes = accept // as List
	}
	
	void accept(String type) {
		acceptTypes << type
	}
	
	String encode(String input) {
		return URLEncoder.encode(input, 'UTF-8')
	}
	
	Response post(Map parameters) {
		//StringBuilder body = new StringBuilder()
		def params = []
		parameters.each { name,value ->
			params << "${name}=${encode(value)}"
		}
		return post(params.join('&'))
	}
	
	Response post(String content) {
		http.request(POST, ContentType.JSON) {
			body = content
			requestContentType = requestType
			headers['Accept'] = acceptTypes.join(',')
			//requestAccept = acceptTypes.join(',')
			response.success = { resp, json ->
				println "resp is a ${resp.class.name}"
				def text = resp.entity.content.text
				println text
				return new Response(code:200, body:text)
			}
			response.'400' = { resp ->
				return new Response(code:400, body:resp.statusLine)
			}
			response.'500' = { resp ->
				return new Response(code:500, body:resp.statusLine)
			}
		}
	}
	
	
	Response get(Map query) {
		return get(query, ContentType.TEXT)
	}

	Response get(Map query, ContentType type) {
		http.get(contentType:type, query:query) { resp, reader ->
			Response response = new Response()
			response.code = resp.statusLine.statusCode
			response.body = read(reader)
			resp.headers.each {
				println it
			}
			println resp.data ?: 'No Data'
			
			return response
		}
	}
	
	Response get(String path, Map query) {
		return get(path, query, ContentType.TEXT)
	}
	
	String read(reader) {
		if (!reader) {
			return "Nothing."
		}
		def lines = []
		reader.eachLine { lines << it }
		return lines.join('\n')
	}

	Response get(String path, Map query, ContentType type) {
		http.get(path:path, contentType:type, query:query) { resp,reader ->
			Response response = new Response()
			response.code = resp.statusLine.statusCode
			response.body = read(reader)
			return response
		}
	}
	
}
**/