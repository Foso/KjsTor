package io.ktor.application;


import Routi
import io.ktor.routing.Route
import kotlin.coroutines.CoroutineContext

class Logger

interface ApplicationEnvironment {
    /**
     * Parent coroutine context for an application
     */
    val parentCoroutineContext: CoroutineContext

    /**
     * [ClassLoader] used to load application.
     *
     * Useful for various reflection-based services, like dependency injection.
     */
   // val classLoader: ClassLoader

    /**
     * Instance of [Logger] to be used for logging.
     */
    val log: Logger

    /**
     * Configuration for the [Application]
     */
    //   val config: ApplicationConfig

    /**
     * Provides events on Application lifecycle
     */
   // val monitor: ApplicationEvents

    /**
     * Application's root path (prefix, context path in servlet container).
     */
  //  @KtorExperimentalAPI
    val rootPath: String

    var route: Route?

}
