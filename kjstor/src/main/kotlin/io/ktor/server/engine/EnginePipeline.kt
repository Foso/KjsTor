package io.ktor.server.engine

import io.ktor.request.ApplicationReceivePipeline
import io.ktor.response.ApplicationSendPipeline
import io.ktor.util.pipeline.Pipeline
import io.ktor.util.pipeline.PipelinePhase

import io.ktor.application.ApplicationCall

/**
 * Application engine pipeline. One usually don't need to install interceptors here unless your are writing
 * your own engine implementation
 */
class EnginePipeline : Pipeline<Unit, ApplicationCall>(Before, Call) {
    /**
     * Pipeline for receiving content
     */
    val receivePipeline = ApplicationReceivePipeline()

    /**
     * Pipeline for sending content
     */
    val sendPipeline = ApplicationSendPipeline()

    companion object {
        /**
         * Before call phase
         */
        val Before = PipelinePhase("before")

        /**
         * Application call pipeline phase
         */
        val Call = PipelinePhase("call")
    }
}
