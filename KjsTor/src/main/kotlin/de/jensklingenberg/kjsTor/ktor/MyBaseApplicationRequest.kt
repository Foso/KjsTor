package de.jensklingenberg.kjsTor.ktor

import io.ktor.request.ApplicationReceivePipeline
import de.jensklingenberg.kjsTor.MyApplicationRequest

abstract class MyBaseApplicationRequest(override val call: MyApplicationCall) :
    MyApplicationRequest {
    override val pipeline = ApplicationReceivePipeline().apply {
      //  merge(call.application.receivePipeline)
    }
}

