package io.ktor.response

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.MyOutgoingContent

interface MyApplicationResponse : ApplicationResponse {

    /**
     * Set status for this response
     */
    fun setContent(statusCode: HttpStatusCode, header: ResponseHeader = DefaultHeader, content: MyOutgoingContent)
}

fun MyApplicationResponse.header(name: String, value: String): Unit = headers.append(name,value)
