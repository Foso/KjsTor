package io.ktor.application

import io.ktor.http.Parameters
import io.ktor.request.ApplicationRequest
import io.ktor.response.ApplicationResponse
import io.ktor.util.Attributes

/**
 * Represents a single act of communication between client and server.
 */
interface ApplicationCall {
    /**
     * Application being called
     */
    val application: Application

    /**
     * Client request
     */
    val request: ApplicationRequest

    /**
     * Server response
     */
    val response: ApplicationResponse

    /**
     * Attributes attached to this instance
     */
    val attributes: Attributes

    /**
     * Parameters associated with this call
     */
    val parameters: Parameters
}