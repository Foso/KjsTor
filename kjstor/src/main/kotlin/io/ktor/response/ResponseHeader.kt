package io.ktor.response
open class ResponseHeader(val name: String, val value: String)

object DefaultHeader : ResponseHeader("Content-Type", "text/plain")