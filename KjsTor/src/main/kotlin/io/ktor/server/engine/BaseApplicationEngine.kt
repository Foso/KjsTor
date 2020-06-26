package io.ktor.server.engine;

import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationEnvironment


/**
 * Default engine pipeline for all engines. Use it only if you are writing your own application engine implementation.
 */

fun defaultEnginePipeline(environment: ApplicationEnvironment): EnginePipeline {
    val pipeline = EnginePipeline()


    pipeline.intercept(EnginePipeline.Call) {


    }

    return pipeline
}

abstract class BaseApplicationEngine(
    final override val environment: ApplicationEngineEnvironment,
    val pipeline: EnginePipeline = defaultEnginePipeline(environment)
) : ApplicationCallPipeline(), ApplicationEngine{

    /**
     * Configuration for the [BaseApplicationEngine]
     */
    open class Configuration : ApplicationEngine.Configuration()

    /**
     *
     *  init {
    BaseApplicationResponse.setupSendPipeline(pipeline.sendPipeline)
    environment.monitor.subscribe(ApplicationStarting) {
    it.receivePipeline.merge(pipeline.receivePipeline)
    it.sendPipeline.merge(pipeline.sendPipeline)
    it.receivePipeline.installDefaultTransformations()
    it.sendPipeline.installDefaultTransformations()
    }
    environment.monitor.subscribe(ApplicationStarted) {
    environment.connectors.forEach {
    environment.log.info("Responding at ${it.type.name.toLowerCase()}://${it.host}:${it.port}")
    }
    }
    }
     */
}