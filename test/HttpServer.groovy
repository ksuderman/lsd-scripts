/*
 * Run this script to start a Jetty server that serves up the scriptlets from the webapp directory.
 */

@Grab('org.eclipse.jetty.aggregate:jetty-all-server:8.1.0.v20120127')
import org.eclipse.jetty.servlet.ServletContextHandler
import groovy.servlet.GroovyServlet
import org.eclipse.jetty.server.Server

def params = [:]
args.each { arg ->
	def parts = arg.split('=')
	if (parts.size() == 1) {
		params[parts[0]] = true
	}
	else {
		params[parts[0]] = parts[1]
	}	
}

def path = params.path ?: '/'
def base = params.base ?: 'webapp'
int port = 8094
if (params.port) {
	port = params.port as int
}

println "String Jetty server on port ${port}"
ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS)
context.with {
    contextPath = path
    resourceBase = base
    addServlet(GroovyServlet, '*.groovy')
}

jettyServer = new Server(port)
jettyServer.with {
    setHandler(context)
    start()
}