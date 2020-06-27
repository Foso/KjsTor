package io.ktor.server.engine

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.Parameters
import io.ktor.util.Attributes

abstract class BaseApplicationCall(final override val application: Application) : ApplicationCall {
    final override val attributes = Attributes()
    override val parameters: Parameters get() = request.queryParameters

    abstract override val request: BaseApplicationRequest
    abstract override val response: BaseApplicationResponse

    /**
     * Put engine response attribute. This is required for base implementation to work properly
     */
    protected fun putResponseAttribute(response: BaseApplicationResponse = this.response) {
        console.log("putResponseAttribute")
        attributes.put(BaseApplicationResponse.EngineResponseAtributeKey, response)
    }
}