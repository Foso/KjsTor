package io.ktor.server.engine

import io.ktor.application.ApplicationCall
import io.ktor.request.ApplicationReceivePipeline
import io.ktor.request.ApplicationRequest

/**
 * Base class for implementing [ApplicationRequest]
 */
abstract class BaseApplicationRequest(override val call: ApplicationCall) : ApplicationRequest {
    override val pipeline = ApplicationReceivePipeline().apply {
        //merge(call.application.receivePipeline)
    }
}

