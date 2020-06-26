package io.ktor.http.content

import io.ktor.http.ContentType
import de.jensklingenberg.kjsTor.OutgoingContent

class LocalFileContent(val filePath:String, override val contentType : ContentType):
    OutgoingContent()