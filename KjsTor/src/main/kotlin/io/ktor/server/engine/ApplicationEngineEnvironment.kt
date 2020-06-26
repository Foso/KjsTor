package io.ktor.server.engine

import io.ktor.application.Application
import io.ktor.application.ApplicationEnvironment
import io.ktor.routing.Route
import io.ktor.server.EngineConnectorConfig
import de.jensklingenberg.kjsTor.MyNodeJsAppCall

/**
 * Represents an environment in which engine runs
 */
interface ApplicationEngineEnvironment : ApplicationEnvironment {

    override var route: Route?
    val modules: MutableList<Application.() -> Unit>

    fun handle(myNodeJsAppCall: MyNodeJsAppCall) : MyNodeJsAppCall


    /**
     * Connectors that describers where and how server should listen
     */
    val connectors: List<EngineConnectorConfig>

    /**
     * Starts [ApplicationEngineEnvironment] and creates an application
     */
    fun start()

    /**
     * Stops [ApplicationEngineEnvironment] and destroys any running application
     */
    fun stop()

    /**
     * Running [Application]
     *
     * Throws an exception if environment has not been started
     */
     val application: Application
}

/**
 * Creates [ApplicationEngineEnvironment] using [ApplicationEngineEnvironmentBuilder]
 */
fun applicationEngineEnvironment(builder: ApplicationEngineEnvironmentBuilder.() -> Unit): ApplicationEngineEnvironment {
    return ApplicationEngineEnvironmentBuilder().build(builder)
}