package de.jensklingenberg.kjsTor.ktor

import io.ktor.application.Application
import io.ktor.http.Parameters
import io.ktor.util.Attributes

abstract class MyBaseApplicationCall(final override val application: Application) :
    MyApplicationCall {
    final override val attributes = Attributes()
    override val parameters: Parameters get() = request.queryParameters
    abstract override val request: MyBaseApplicationRequest
    //abstract override val response: BaseApplicationResponse
}