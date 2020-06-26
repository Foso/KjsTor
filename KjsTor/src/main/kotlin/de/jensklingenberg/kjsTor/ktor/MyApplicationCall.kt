package de.jensklingenberg.kjsTor.ktor

import Buffer
import de.jensklingenberg.kjsTor.*
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.content.LocalFileContent
import io.ktor.util.Attributes

/**
 * Represents a single act of communication between client and server.
 */
interface MyApplicationCall {
    /**
     * Application being called
     */
    val application: Application

    /**
     * Client request
     */
    val request: MyApplicationRequest

    /**
     * Server response
     */
    val response: MyApplicationResponse

    /**
     * Attributes attached to this instance
     */
    val attributes: Attributes

    /**
     * Parameters associated with this call
     */
    val parameters: Parameters
}


fun MyApplicationCall.respond(status: HttpStatusCode, message: String) {
    response.setContent(statusCode = status, content = TextContent(message))
}

/**
 * Responds to a client with a plain text response, using specified [text]
 * @param contentType is an optional [ContentType], default is [ContentType.Text.Plain]
 * @param status is an optional [HttpStatusCode], default is [HttpStatusCode.OK]
 */
fun MyApplicationCall.respondText(
    text: String,
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    configure: OutgoingContent.() -> Unit = {}
) {
    respond(HttpStatusCode.OK, text)
}

suspend inline fun MyApplicationCall.susrespondText(
    text: String,
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    configure: OutgoingContent.() -> Unit = {}
) {
    console.log("Suspend")
    //respond(HttpStatusCode.OK,text)
}

fun MyApplicationCall.respondFile(filePath: String, contentType: ContentType) {
   val message = LocalFileContent(
       filePath,
       contentType
   )
    respondFile(
        message
    )
}



fun MyApplicationCall.respondFile(content: LocalFileContent) {
    response.setContent(HttpStatusCode.OK, content = content)
}

fun MyApplicationCall.respondRedirect(message: String) {
    response.setContent(HttpStatusCode.Found,
        ResponseHeader("Location", message), content = TextContent(
            message
        )
    )
}

val MyApplicationCall.appCall: MyApplicationCall get() = this


fun MyApplicationCall.receiveData(function: (Buffer) -> Unit) {
    this.request.receiveData(function)
}
