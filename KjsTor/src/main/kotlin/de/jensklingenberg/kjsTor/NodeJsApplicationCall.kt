package de.jensklingenberg.kjsTor

import io.ktor.application.Application
import io.ktor.response.ApplicationResponse
import io.ktor.server.engine.BaseApplicationCall
import io.ktor.server.engine.BaseApplicationRequest

class NodeJsApplicationCall(application: Application) : BaseApplicationCall(application) {
    override val request: BaseApplicationRequest
        get() = TODO("Not yet implemented")
    override val response: ApplicationResponse
        get() = TODO("Not yet implemented")
}