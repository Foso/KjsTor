package io.ktor.request

import io.ktor.application.ApplicationCall
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.Pipeline
import io.ktor.util.pipeline.PipelinePhase
import kotlin.reflect.KType

/**
 * Represents a subject for [ApplicationReceivePipeline]
 * @param typeInfo specifies the desired type for a receiving operation
 * @param value specifies current value being processed by the pipeline
 * @param reusableValue indicates whether the [value] instance can be reused. For example, a stream can't.
 */
class ApplicationReceiveRequest @KtorExperimentalAPI constructor(
    @KtorExperimentalAPI val typeInfo: KType,
    val value: Any,
    @KtorExperimentalAPI val reusableValue: Boolean = false
) {

}

/**
 * Pipeline for processing incoming content
 *
 * When executed, this pipeline starts with an instance of [ByteReadChannel] and should finish with the requested type.
 */
open class ApplicationReceivePipeline : Pipeline<ApplicationReceiveRequest, ApplicationCall>(Before, Transform, After) {
    /**
     * Pipeline phases
     */
    @Suppress("PublicApiImplicitType")
    companion object Phases {
        /**
         * Executes before any transformations are made
         */
        val Before = PipelinePhase("Before")

        /**
         * Executes transformations
         */
        val Transform = PipelinePhase("Transform")

        /**
         * Executes after all transformations
         */
        val After = PipelinePhase("After")
    }
}