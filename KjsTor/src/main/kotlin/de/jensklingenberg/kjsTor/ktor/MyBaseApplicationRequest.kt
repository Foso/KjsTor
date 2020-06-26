package de.jensklingenberg.kjsTor.ktor

import io.ktor.application.ApplicationCall
import io.ktor.request.ApplicationReceivePipeline
import io.ktor.request.ApplicationRequest

abstract class MyBaseApplicationRequest(override val call: ApplicationCall) :
    ApplicationRequest {
    override val pipeline = ApplicationReceivePipeline().apply {
        //merge(call.application.receivePipeline)
    }
}

