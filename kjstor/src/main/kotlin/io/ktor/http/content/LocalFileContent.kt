package io.ktor.http.content

import io.ktor.http.ContentType


class LocalFileContent(val filePath:String, override val contentType : ContentType):
    MyOutgoingContent()


open class MyOutgoingContent() {
    open val contentType: ContentType? get() = null

}
class EmptyTextContent : TextContent("")

open class TextContent(val message: String) : MyOutgoingContent()