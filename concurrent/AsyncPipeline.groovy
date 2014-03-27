package concurrent

@Grab(group='org.codehaus.gpars', module='gpars', version='1.1.0')
import groovyx.gpars.*
import groovyx.gpars.dataflow.*

import java.util.concurrent.atomic.AtomicInteger

class AsyncPipeline {

	DataflowQueue queue = new DataflowQueue()
	DataflowReadChannel channel
	def id = new AtomicInteger()
		
	public AsyncPipeline() {
	}
	
	public AsyncPipeline(List services) {
		services.each { add it }
	}
	
	void leftShift(service) {
		add(service)
	}
	
	void add(service) {
		Closure cl
		if (service instanceof WebService) {
			println "Adding ${service.endpoint}"
			cl = invoke(service)
		}
		else if (service instanceof Closure) {
			println "Adding closure."
			cl = service
		}
		else {
			throw LappsException("Cannot add ${service.class.name} to an AsyncPipeline.")
		}
		
		// Add this service to the end the channel.		
		if (channel) {
			channel = channel.chainWith(cl)
		}
		else {
			channel = queue.chainWith(cl)		
		}
	}
	
	void leftShift(Packet packet) {
		run(packet)
	}
	
	void run(Packet packet) {
		packet.id = id.incrementAndGet()
		queue << packet
	}
	
	Object value() {
		return channel.val
	}

    void waitFor(String value) {
        boolean done = false
        while (!done) {
            //sleep(100)
            done = value.equals(channel.val)
        }
    }

	Closure invoke(WebService service) {
		def invoker = { Packet packet ->
			println "${packet.id}: Calling ${service.endpoint}"
			packet.data = service.execute(packet.data)
			return packet
		}
		return invoker
	}
}
	
