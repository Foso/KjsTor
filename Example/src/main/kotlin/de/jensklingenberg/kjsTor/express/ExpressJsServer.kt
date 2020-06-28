package de.jensklingenberg.kjsTor.express

import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.ApplicationEngineFactory

/**
 * An [ApplicationEngineFactory] providing a Netty-based [ApplicationEngine]
 */
object ExpressJsServer : ApplicationEngineFactory<ExpressJsApplicationEngine, ExpressJsApplicationEngine.Configuration> {
    override fun create(environment: ApplicationEngineEnvironment, configure: ExpressJsApplicationEngine.Configuration.() -> Unit): ExpressJsApplicationEngine {
        return ExpressJsApplicationEngine(environment, configure)
    }
}