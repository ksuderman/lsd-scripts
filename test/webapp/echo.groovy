import javax.servlet.http.HttpServletResponse

println "HEADERS"
request.headerNames.each { name ->
	println "${name}=${request.getHeader(name)}"
}
println "END HEADER"
println()

println "PARAMETERS"
request.parameterMap.each { name, value ->
	println "${name}=${value}"
}
println "END PARAMETER"
println()

println "BODY"
def input = request.inputStream
println input.text
println "END BODY"
