package de.jensklingenberg.kjsTor

import Buffer
import de.jensklingenberg.mapEntriesOf
import http.IncomingMessage
import io.ktor.application.ApplicationCall
import io.ktor.http.Headers
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.RequestConnectionPoint
import io.ktor.request.ApplicationReceivePipeline
import io.ktor.request.ApplicationRequest
import de.jensklingenberg.kjsTor.ktor.MyApplicationCall
import de.jensklingenberg.kjsTor.ktor.MyBaseApplicationRequest


interface MyApplicationRequest {
    /**
     * [ApplicationCall] instance this ApplicationRequest is attached to
     */
    val call: MyApplicationCall

    /**
     * Pipeline for receiving content
     */
    val pipeline: ApplicationReceivePipeline

    /**
     * Parameters provided in an URL
     */
    val queryParameters: Parameters

    /**
     * Headers for this request
     */
    val headers: Headers

    /**
     * Contains http request and connection details such as a host name used to connect, port, scheme and so on.
     * No proxy headers could affect it. Use [ApplicationRequest.origin] if you need override headers support
     */
    val local: RequestConnectionPoint

    /**
     * Cookies for this request
     */
    // val cookies: RequestCookies

    /**
     * Request's body channel (for content only)
     */
    fun setData(buffer: Buffer)

    fun receiveData(callback: (Buffer) -> Unit)
}


class MyRequest(private val callNodeJs: MyNodeJsAppCall, val incomingMessage: IncomingMessage) :
    MyBaseApplicationRequest(callNodeJs) {
    override val local: RequestConnectionPoint =
        NodeJsConnectionPoint(incomingMessage)
    var dataCallback: (Buffer) -> Unit={
        console.log("FUNCTION NOT SET")
    }
    override fun setData(buffer: Buffer) {
        dataCallback(buffer)
    }

    override fun receiveData(callback: (Buffer) -> Unit) {
        dataCallback=callback
    }

    val method: HttpMethod =
        HttpMethod.parse(incomingMessage.method)

    override val headers: Headers by lazy {

        Headers.build {
            mapEntriesOf(incomingMessage.headers).forEach {
                val name = it.key
                val value = it.value
                if (name.isNotEmpty() && name[0] != ':') {
                    append(
                        name.toString(),
                        value.toString()
                    )
                }
            }
        }
    }

    override val queryParameters: Parameters = object :
        Parameters {
        private val decoder = QueryStringDecoder(incomingMessage.url)
        override val caseInsensitiveName: Boolean get() = true
        override fun getAll(name: String) = decoder.parameters()[name]
        override fun names() = decoder.parameters().keys
        override fun entries() = decoder.parameters().entries
        override fun isEmpty() = decoder.parameters().isEmpty()
    }

}

class NodeJsConnectionPoint(val incomingMessage: IncomingMessage) : RequestConnectionPoint {
    override val host: String
        get() = TODO("Not yet implemented")
    override val method: HttpMethod
        get() = HttpMethod.parse(incomingMessage.method)
    override val port: Int
        get() = TODO("Not yet implemented")
    override val remoteHost: String
        get() = TODO("Not yet implemented")
    override val scheme: String
        get() = TODO("Not yet implemented")
    override val uri: String
        get() = incomingMessage.url
    override val version: String
        get() = incomingMessage.httpVersion

}