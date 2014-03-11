package concurrent

@Grab(group='org.codehaus.gpars', module='gpars', version='1.1.0')
import groovyx.gpars.*
import groovyx.gpars.actor.*

import java.util.concurrent.BlockingQueue
import java.util.concurrent.SynchronousQueue

import static groovyx.gpars.actor.Actors.*
import java.util.concurrent.BlockingQueue


class ActorPool {
    public start(BlockingQueue queue, List closures) {
        println "Staring actors pool."
        GParsPool.withPool {
            def actors = []
            closures.each { cl ->
                actors = actor {
                    loop {
                        def packet = queue.take()
                        if (packet.poison) {
                            println "Actor halting."
                            terminate()
                        }
                        else {
                            cl(packet)
                        }
                    }
                }
            }
            actors*.join()
        }
        println "Actors pool finished."
    }
}

