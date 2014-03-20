import javax.servlet.http.HttpServletResponse

println "HEADERS"
request.headerNames.each { name ->
	println "${name}=${request.getHeader(name)}"
}

println "PARAMETERS"
request.parameterMap.each { name, value ->
	println "${name}=${value}"
}

println "BODY"
def input = request.inputStream
println input.text
println "END"
