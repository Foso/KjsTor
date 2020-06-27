package de.jensklingenberg.kjsTor.ktor

import Buffer
import de.jensklingenberg.kjsTor.*
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.content.LocalFileContent
import io.ktor.request.ApplicationRequest
import io.ktor.response.ApplicationResponse
import io.ktor.util.Attributes

/**
 * Represents a single act of communication between client and server.
 */



inline fun ApplicationCall.respond(status: HttpStatusCode, message: String) {
   // this.response.pipeline.execute(this,message)
    (response as MyApplicationResponse).setContent(HttpStatusCode.OK, content = TextContent(message))

}

/**
 * Responds to a client with a plain text response, using specified [text]
 * @param contentType is an optional [ContentType], default is [ContentType.Text.Plain]
 * @param status is an optional [HttpStatusCode], default is [HttpStatusCode.OK]
 */
fun ApplicationCall.respondText(
    text: String,
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    configure: OutgoingContent.() -> Unit = {}
) {
    respond(HttpStatusCode.OK, text)
}



fun ApplicationCall.respondFile(filePath: String, contentType: ContentType) {
   val message = LocalFileContent(
       filePath,
       contentType
   )
    respondFile(
        message
    )
}



fun ApplicationCall.respondFile(content: LocalFileContent) {
    (response as MyApplicationResponse).setContent(HttpStatusCode.OK, content = content)
}

fun ApplicationCall.respondRedirect(message: String) {
    (response as MyApplicationResponse).setContent(HttpStatusCode.Found,
        ResponseHeader("Location", message), content = TextContent(
            message
        )
    )
}

val ApplicationCall.appCall: ApplicationCall get() = this


fun ApplicationCall.receiveData(function: (Buffer) -> Unit) {
    this.request.receiveData(function)
}
