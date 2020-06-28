package de.jensklingenberg.kjstor

import Buffer

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.LocalFileContent
import io.ktor.http.content.MyOutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.response.MyApplicationResponse
import io.ktor.response.ResponseHeader


fun ApplicationCall.respond(status: HttpStatusCode, message: String) {
   // this.response.pipeline.execute(this,message)
    (response as MyApplicationResponse).setContent(HttpStatusCode.OK, content = TextContent(message))

}


fun ApplicationCall.respondText(
    text: String,
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    configure: MyOutgoingContent.() -> Unit = {}
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
