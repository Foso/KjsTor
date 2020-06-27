package io.ktor.request

import io.ktor.application.ApplicationCall
import io.ktor.application.log
import io.ktor.http.Parameters
import io.ktor.http.content.MultiPartData
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.Pipeline
import io.ktor.util.pipeline.PipelinePhase
import io.ktor.utils.io.ByteReadChannel
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Represents a subject for [ApplicationReceivePipeline]
 * @param typeInfo specifies the desired type for a receiving operation
 * @param value specifies current value being processed by the pipeline
 * @param reusableValue indicates whether the [value] instance can be reused. For example, a stream can't.
 */
class ApplicationReceiveRequest @KtorExperimentalAPI constructor(

) {
//TODO
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

