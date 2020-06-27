@file:JsModule("https")
@file:JsNonModule
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "EXTERNAL_DELEGATION")
package https

import kotlin.js.*
import org.w3c.dom.url.*
import tls.ConnectionOptions
import tls.SecureContextOptions
import http.RequestListener
import http.HttpBase
import http.RequestOptions
import http.IncomingMessage
import http.ClientRequest

external interface AgentOptions : http.AgentOptions, ConnectionOptions {
    override var rejectUnauthorized: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var maxCachedSessions: Number?
        get() = definedExternally
        set(value) = definedExternally
    override var timeout: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external open class Agent(options: AgentOptions = definedExternally) : http.Agent {
    open var options: AgentOptions
}

external open class Server(requestListener: http.RequestListener = definedExternally) : tls.Server, HttpBase {
    constructor(options: SecureContextOptions, requestListener: RequestListener)
    override fun setTimeout(msecs: Number, callback: () -> Unit): Server /* this */
    override fun setTimeout(callback: () -> Unit): Server /* this */
    override var maxHeadersCount: Number?
    override var timeout: Number
    override var headersTimeout: Number
    override var keepAliveTimeout: Number
}

external fun createServer(requestListener: RequestListener = definedExternally): Server

external fun createServer(options: SecureContextOptions /* tls.SecureContextOptions & tls.TlsOptions & ServerOptions */, requestListener: RequestListener = definedExternally): Server

external fun request(options: RequestOptions /* RequestOptions & tls.SecureContextOptions & `T$60` */, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun request(options: String, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun request(options: URL, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun request(url: String, options: RequestOptions /* RequestOptions & tls.SecureContextOptions & `T$60` */, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun request(url: URL, options: RequestOptions /* RequestOptions & tls.SecureContextOptions & `T$60` */, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun get(options: RequestOptions /* RequestOptions & tls.SecureContextOptions & `T$60` */, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun get(options: String, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun get(options: URL, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun get(url: String, options: RequestOptions /* RequestOptions & tls.SecureContextOptions & `T$60` */, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external fun get(url: URL, options: RequestOptions /* RequestOptions & tls.SecureContextOptions & `T$60` */, callback: (res: IncomingMessage) -> Unit = definedExternally): ClientRequest

external var globalAgent: Agent