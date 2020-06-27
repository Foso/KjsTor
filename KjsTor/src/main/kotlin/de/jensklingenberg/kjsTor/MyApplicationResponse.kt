package de.jensklingenberg.kjsTor

import io.ktor.http.HttpStatusCode


import io.ktor.response.ApplicationResponse

interface MyApplicationResponse : ApplicationResponse {

    /**
     * Set status for this response
     */
    fun setContent(statusCode: HttpStatusCode, header: ResponseHeader = DefaultHeader, content: OutgoingContent)
}

fun MyApplicationResponse.header(name: String, value: String): Unit = headers.append(name,value)
