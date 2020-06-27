package io.ktor.server.engine;


import io.ktor.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.cio.ChannelIOException
import io.ktor.util.pipeline.execute
import io.ktor.utils.io.discard

suspend inline fun ApplicationCall.respond(message: Any) {
    response.pipeline.execute(this, message)
}
/**
 * Default engine pipeline for all engines. Use it only if you are writing your own application engine implementation.
 */

fun defaultEnginePipeline(environment: ApplicationEnvironment): EnginePipeline {
    val pipeline = EnginePipeline()
    console.log("defaultEnginePipeline")


    pipeline.intercept(EnginePipeline.Call) {
        try {
            call.application.execute(call)
            if (call.response.status() == null) {
                call.respond(HttpStatusCode.NotFound)
            }
        } catch (error: ChannelIOException) {
            /**
             *   with(CallLogging.Internals) {
            withMDCBlock {
            call.application.environment.logFailure(call, error)
            }
            }
             */
        } catch (error: Throwable) {
            /**
             * with(CallLogging.Internals) {
            withMDCBlock {
            call.application.environment.logFailure(call, error)
            handleFailure(error)
            }
            }
             */
        } finally {
            try {
                call.request.receiveChannel().discard()
            } catch (ignore: Throwable) {
            }
        }

    }

    return pipeline
}

@KtorExperimentalAPI
abstract class BaseApplicationEngine(
    final override val environment: ApplicationEngineEnvironment,
    val pipeline: EnginePipeline = defaultEnginePipeline(environment)
) : ApplicationEngine {

    /**
     * Configuration for the [BaseApplicationEngine]
     */
    open class Configuration : ApplicationEngine.Configuration()


    init {
        console.log("BaseApplicationEngine")
        BaseApplicationResponse.setupSendPipeline(pipeline.sendPipeline)
        environment.monitor.subscribe(ApplicationStarting) {
            it.receivePipeline.merge(pipeline.receivePipeline)
            it.sendPipeline.merge(pipeline.sendPipeline)
            it.receivePipeline.installDefaultTransformations()
            //it.sendPipeline.installDefaultTransformations()
        }
        environment.monitor.subscribe(ApplicationStarted) {
            environment.connectors.forEach {
                console.log("Responding at ${it.type.name.toLowerCase()}://${it.host}:${it.port}\"")
                //environment.log.info("Responding at ${it.type.name.toLowerCase()}://${it.host}:${it.port}")
            }
        }
    }
}

