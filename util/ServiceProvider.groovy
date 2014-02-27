package util

import org.lappsgrid.api.Data
import org.lappsgrid.client.ServiceClient

class ServiceProvider {
		
	Map services
	Pipeline pipeline
	
	public ServiceProvider(server, provider, version, names) {
		services = [:]
		pipeline = new Pipeline()
		
		def id = 'picard' // The grid id on localhost
		if (server.url == "http://grid.anc.org:8080") {
			id = 'anc'
		}
		names.each { name ->
			def url = "${server.url}/service_manager/invoker/${id}:${provider}.${name}_${version}"
			def client = new ServiceClient(url, server.username, server.password)
			services[name] = client
			pipeline.add(client)
		}
	}
	
	WebService get(String name) {
		return services[name]
	}
	
	Object propertyMissing(String name) {
		if (services[name]) {
			return services[name]
		}
		throw new MissingPropertyException("No service named ${name} has been defined.")
	}
	
	Data run(Data input) {
		return pipeline.execute(input, false)
	}
}