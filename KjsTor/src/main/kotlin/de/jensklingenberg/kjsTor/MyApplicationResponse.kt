package de.jensklingenberg.kjsTor

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.ApplicationSendPipeline
import io.ktor.response.ResponseHeaders
import de.jensklingenberg.kjsTor.ktor.MyApplicationCall
import de.jensklingenberg.kjsTor.ktor.MyBaseApplicationResponse

interface MyApplicationResponse {

    /**
     * [ApplicationCall] instance this ApplicationResponse is attached to
     */
    val call: MyApplicationCall

    /**
     * Set status for this response
     */
    fun setContent(statusCode: HttpStatusCode, header: ResponseHeader = DefaultHeader, content: OutgoingContent)

    /**
     * Currently set status code for this response, or null if none was set
     */
    fun status(): HttpStatusCode?

    /**
     * Pipeline for sending content
     */
    val pipeline: ApplicationSendPipeline

    /**
     * Headers for this response
     */
    val headers: ResponseHeaders
}

class MyResponse(myApplicationCall: MyApplicationCall) : MyBaseApplicationResponse(myApplicationCall) {
    var statusCode: HttpStatusCode = HttpStatusCode.OK
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

    override val headers: ResponseHeaders = object : ResponseHeaders() {
        override fun engineAppendHeader(name: String, value: String) {
            responseHeaders.add(ResponseHeader(name, value))
        }

        override fun get(name: String): String? = responseHeaders.find { it.name ==name }?.value
        override fun getEngineHeaderNames(): List<String> = responseHeaders.map { it.name }
        override fun getEngineHeaderValues(name: String): List<String> = responseHeaders.filter { it.name==name }.map { it.value }

    }
}