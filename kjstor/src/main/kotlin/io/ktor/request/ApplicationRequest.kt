package io.ktor.request

import Buffer
import io.ktor.application.ApplicationCall
import io.ktor.http.Headers
import io.ktor.http.Parameters
import io.ktor.http.RequestConnectionPoint
import io.ktor.utils.io.ByteReadChannel


/**
 * Request's path without query string
 */
fun ApplicationRequest.path(): String = local.uri.substringBefore('?')


/**
 * Represents client's request
 */
interface ApplicationRequest {
    /**
     * [ApplicationCall] instance this ApplicationRequest is attached to
     */
    val call: ApplicationCall

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
    fun receiveChannel(): ByteReadChannel

    /**
     * Request's body channel (for content only)
     */
    fun setData(buffer: Buffer)

    fun receiveData(callback: (Buffer) -> Unit)
}
