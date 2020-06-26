package de.jensklingenberg.kjsTor

import http.IncomingMessage
import http.ServerResponse
import io.ktor.application.Application
import io.ktor.http.*
import de.jensklingenberg.kjsTor.ktor.MyBaseApplicationCall

class MyNodeJsAppCall(application: Application, incomingMessage: IncomingMessage, res: ServerResponse) : MyBaseApplicationCall(application) {

    val url = incomingMessage.url
    val method = incomingMessage.method
    override val response: MyResponse =
        MyResponse(this)
    override val request = MyRequest(this, incomingMessage)

}

class EmptyTextContent : TextContent("")


open class OutgoingContent() {
    open val contentType: ContentType? get() = null

}

open class TextContent(val message: String) : OutgoingContent()

open class ResponseHeader(val name: String, val value: String)

object DefaultHeader : ResponseHeader("Content-Type", "text/plain")