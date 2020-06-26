package io.ktor.server.engine;

import io.ktor.application.Application
import io.ktor.application.Logger
import io.ktor.server.EngineConnectorConfig
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class ApplicationEngineEnvironmentBuilder {

    /**
     * Parent coroutine context for an application
     */
    var parentCoroutineContext: CoroutineContext = EmptyCoroutineContext

    /**
     * Paths to wait for application reload
     */
    var watchPaths: List<String> = emptyList()

    /**
     * Application modules
     */
    val modules: MutableList<Application.() -> Unit> = mutableListOf()

    var rootPath: String = ""

    /**
     * Install application module
     */
    fun module(body: Application.() -> Unit) {
        modules.add(body)
    }

    /**
     * Application connectors list
     */
    val connectors: MutableList<EngineConnectorConfig> = mutableListOf()

    /**
     * Build an application engine environment
     */
    fun build(builder: ApplicationEngineEnvironmentBuilder.() -> Unit): ApplicationEngineEnvironment {
        builder(this)
        return ApplicationEngineEnvironmentReloading(connectors, parentCoroutineContext, rootPath,modules)
    }
}


class ApplicationEngineEnvironmentReloading(
    override val connectors: List<EngineConnectorConfig>,
    override val parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
    override val rootPath: String = "",
    override val modules: MutableList<Application.() -> Unit>
) : ApplicationEngineEnvironment {
    override var route: io.ktor.routing.Route?=null
    override fun start() {

    }

    override fun stop() {


    }

    override val application: Application= Application(this)


    override val log: Logger
        get() = Logger()


}