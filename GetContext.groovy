import util.Http

def http = new Http('http://vocab.lappsgrid.org')
http.accept 'application/json'

def url = null

if (args.size() > 0 && args[0] == 'lappsgrid') {
	url = 'http://vocab.lappsgrid.org/person.jsonld'
}
else {
	url = 'http://json-ld.org/contexts/person'
}
println "Getting ${url}"
def response = http.get(url)
response.headers.each { name, value ->
	println "${name}=${value}"
}
println response.body

