package de.jensklingenberg.kjsTor.node

import http.IncomingMessage
import http.ServerResponse
import io.ktor.application.Application
import io.ktor.server.engine.BaseApplicationCall


class MyNodeJsAppCall(application: Application, incomingMessage: IncomingMessage, res: ServerResponse) : BaseApplicationCall(application) {

    val url = incomingMessage.url
    val method = incomingMessage.method
    override val response: NodeJsResponse =
        NodeJsResponse(this)
    override val request = NodeJsRequest(this, incomingMessage)
    init {
        putResponseAttribute()
    }
}



