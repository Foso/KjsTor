package de.jensklingenberg.kjsTor.node


import Buffer
import de.jensklingenberg.mapEntriesOf
import http.IncomingMessage
import io.ktor.http.Headers
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.RequestConnectionPoint
import io.ktor.server.engine.BaseApplicationRequest
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel


class NodeJsRequest(private val callNodeJs: MyNodeJsAppCall, val incomingMessage: IncomingMessage, val contentByteChannel: ByteChannel = ByteChannel()) :
    BaseApplicationRequest(callNodeJs) {
    override val local: RequestConnectionPoint =
        NodeJsConnectionPoint(incomingMessage)

    override fun receiveChannel(): ByteReadChannel = contentByteChannel


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
        get() = "NICHT "
    override val method: HttpMethod
        get() = HttpMethod.parse(incomingMessage.method)
    override val port: Int
        get() = -1
    override val remoteHost: String
        get() = "HERE"
    override val scheme: String
        get() = "HERE"
    override val uri: String
        get() = "/"+incomingMessage.url
    override val version: String
        get() = incomingMessage.httpVersion

}