package de.jensklingenberg.kjsTor

import http.IncomingMessage
import http.ServerResponse
import io.ktor.application.Application
import io.ktor.http.*
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

class EmptyTextContent : TextContent("")


open class OutgoingContent() {
    open val contentType: ContentType? get() = null

}

open class TextContent(val message: String) : OutgoingContent()

open class ResponseHeader(val name: String, val value: String)

object DefaultHeader : ResponseHeader("Content-Type", "text/plain")