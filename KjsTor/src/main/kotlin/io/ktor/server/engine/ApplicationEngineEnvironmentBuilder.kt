package io.ktor.server.engine;

import io.ktor.application.*
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
        return ApplicationEngineEnvironmentReloading(connectors, parentCoroutineContext, rootPath,modules,watchPaths)
    }
}


class ApplicationEngineEnvironmentReloading(
    override val connectors: List<EngineConnectorConfig>,
    override val parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
    override val rootPath: String = "",
    override val modules: MutableList<Application.() -> Unit>,
    watchPaths: List<String>
) : ApplicationEngineEnvironment {
    override var route: io.ktor.routing.Route?=null
    override fun start() {
        instantiateAndConfigureApplication()
    }
    private val watchPatterns: List<String> = emptyList()
        //(config.propertyOrNull("ktor.deployment.watch")?.getList() ?: listOf()) + watchPaths
    private fun instantiateAndConfigureApplication(): Application {
        val application = Application(this)
        safeRiseEvent(ApplicationStarting, application)
            console.log("START")
            /**
             * avoidingDoubleStartup {
            moduleFunctionNames?.forEach { fqName ->
            avoidingDoubleStartupFor(fqName) {
            executeModuleFunction(classLoader, fqName, application)
            }
            }
            }
             */

        if (watchPatterns.isEmpty()) {
            modules.forEach { it(application) }
        }

        safeRiseEvent(ApplicationStarted, application)
        return application
    }

    private fun safeRiseEvent(event: EventDefinition<Application>, application: Application) {
        try {
            monitor.raise(event, application)
        } catch (e: Throwable) {
            //log.error("One or more of the handlers thrown an exception", e)
        }
    }

    override val monitor = ApplicationEvents()

    override fun stop() {


    }

    override val application: Application= Application(this)


    override val log: Logger
        get() = Logger()


}