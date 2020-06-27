package de.jensklingenberg.kjsTor

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.response.ResponseHeaders
import io.ktor.server.engine.BaseApplicationResponse
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteWriteChannel

class NodeJsResponse(myApplicationCall: ApplicationCall) : BaseApplicationResponse(myApplicationCall) {
    var statusCode: HttpStatusCode =
        HttpStatusCode.OK
    var responseHeaders: MutableList<ResponseHeader> = mutableListOf()
    var content: OutgoingContent =
        EmptyTextContent()

    override fun setContent(statusCode: HttpStatusCode, header: ResponseHeader, content: OutgoingContent) {
        this.statusCode = statusCode
        this.content = content
    }

    override fun status(): HttpStatusCode? {
        return HttpStatusCode.OK //TODO
    }



    override suspend fun respondUpgrade(upgrade: io.ktor.http.content.OutgoingContent.ProtocolUpgrade) {
       console.log("NodeJsResponse: responseUpgrade")
    }

    override suspend fun responseChannel(): ByteWriteChannel {
        console.log("responseChannel()")
        val channel = ByteChannel()
        val chunked = headers[HttpHeaders.TransferEncoding] == "chunked"
        //sendResponse(chunked, content = channel)
        return channel
    }

    override fun setStatus(statusCode: HttpStatusCode) {
        this.statusCode = statusCode
    }

    override val headers: ResponseHeaders = object : ResponseHeaders() {
        override fun engineAppendHeader(name: String, value: String) {
            responseHeaders.add(ResponseHeader(name, value))
        }

        override fun get(name: String): String? = responseHeaders.find { it.name ==name }?.value
        override fun getEngineHeaderNames(): List<String> = responseHeaders.map { it.name }
        override fun getEngineHeaderValues(name: String): List<String> = responseHeaders.filter { it.name==name }.map { it.value }

    }

    companion object {
        private val EmptyByteArray = ByteArray(0)


    }
}