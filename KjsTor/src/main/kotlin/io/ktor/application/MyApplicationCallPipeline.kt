/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.application


import io.ktor.request.ApplicationReceivePipeline
import io.ktor.response.ApplicationSendPipeline
import io.ktor.util.pipeline.*
import de.jensklingenberg.kjsTor.ktor.MyApplicationCall

/**
 * Pipeline configuration for executing [ApplicationCall] instances
 */
@Suppress("PublicApiImplicitType")
open class MyApplicationCallPipeline : Pipeline<Unit, MyApplicationCall>(
    Setup,
    Monitoring,
    Features,
    Call,
    Fallback
) {
    /**
     * Pipeline for receiving content
     */
    val receivePipeline = ApplicationReceivePipeline()

    /**
     * Pipeline for sending content
     */
   val sendPipeline = ApplicationSendPipeline()

    /**
     * Standard phases for application call pipelines
     */
    companion object ApplicationPhase {
        /**
         * Phase for preparing call and it's attributes for processing
         */
        val Setup = PipelinePhase("Setup")

        /**
         * Phase for tracing calls, useful for logging, metrics, error handling and so on
         */
        val Monitoring = PipelinePhase("Monitoring")

        /**
         * Phase for features. Most features should intercept this phase.
         */
        val Features = PipelinePhase("Features")

        /**
         * Phase for processing a call and sending a response
         */
        val Call = PipelinePhase("Call")

        /**
         * Phase for handling unprocessed calls
         */
        val Fallback = PipelinePhase("Fallback")
    }
}



/**
 * Current call for the context
 */
inline val PipelineContext<*, MyApplicationCall>.suscall: MyApplicationCall get() = context