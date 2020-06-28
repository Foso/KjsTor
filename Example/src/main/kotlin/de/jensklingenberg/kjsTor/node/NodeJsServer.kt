package de.jensklingenberg.kjsTor.node

import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.ApplicationEngineFactory

/**
 * An [ApplicationEngineFactory] providing a Netty-based [ApplicationEngine]
 */
object NodeJsServer : ApplicationEngineFactory<NodeJsApplicationEngine, NodeJsApplicationEngine.Configuration> {
    override fun create(environment: ApplicationEngineEnvironment, configure: NodeJsApplicationEngine.Configuration.() -> Unit): NodeJsApplicationEngine {
        return NodeJsApplicationEngine(environment, configure)
    }
}