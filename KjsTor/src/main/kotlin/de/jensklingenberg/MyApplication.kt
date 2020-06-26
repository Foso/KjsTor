package de.jensklingenberg

import de.jensklingenberg.kjsTor.NodeJsServer
import de.jensklingenberg.kjsTor.ktor.appCall
import de.jensklingenberg.kjsTor.ktor.receiveData
import de.jensklingenberg.kjsTor.ktor.respondFile
import de.jensklingenberg.kjsTor.ktor.respondText
import get
import io.ktor.application.ApplicationCallPipeline
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.response.header
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import myRouting
import post


fun main() {
    start()
}

//"/home/jens/Code/2020/NodeJsKtor/kjsTor/package.json"
//curl -d "param1=value1&param2=value2" -X POST http:ost:3006/hello
fun start() {


    embeddedServer(NodeJsServer, 3008) {
        intercept(ApplicationCallPipeline.Features) {

        }

        routing {
            get("/hey") {
                val roomName = appCall.parameters["room"] ?: "emptyList()"
                appCall.respondText("huhu"+roomName+ " "+ appCall.request.local.uri)
            }
        }

            myRouting {


                get("/hey") {
                    val roomName = appCall.parameters["room"] ?: "emptyList()"
                    appCall.respondText("Hallo"+roomName+ " "+ appCall.request.local.uri)
                }

                get("/room") {
                    val roomName = appCall.parameters.getAll("room") ?: emptyList()

                    appCall.response.header(HttpHeaders.ContentDisposition, ContentDisposition.Attachment.withParameter( ContentDisposition.Parameters.FileName,"test.mp3").toString())
                    appCall.respondFile("/home/jens/Desktop/hello.m4a", ContentType.Audio.MP4)
                }
                post("/upload") {

                    appCall.receiveData {
                        fs.writeFileSync("tet.png", it)
                    }
                }


            }

    }.start(true)

}
