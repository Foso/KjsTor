package de.jensklingenberg

import de.jensklingenberg.kjsTor.NodeJsServer
import io.ktor.ext.appCall
import io.ktor.ext.respondText
import de.jensklingenberg.showdown.model.MyTest
import io.ktor.application.Application
import io.ktor.routing.get
import io.ktor.routing.post

import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer


fun main() {
    start()
}

//"/home/jens/Code/2020/NodeJsKtor/kjsTor/package.json"
//curl -d "param1=value1&param2=value2" -X POST http:ost:3006/hello
fun start() {

    embeddedServer(NodeJsServer, port = 3008, module = Application::MainModule).start(true)


}

fun Application.MainModule() {


    routing {
        get("/hallo") {
            console.log("HALLO")
            appCall.respondText(MyTest.test)
        }

        post("/hallo") {

            console.log("HALLO")
            appCall.respondText("posthallo")

        }
        get("/") {

            console.log("HALLO")
            appCall.respondText("HEy")

        }


        get("/a") {

            console.log("HALLO")
            appCall.respondText("a")

        }

        get("/room/{roomName}") {
            val name = appCall.parameters["roomName"]
            console.log("HALLO")
            appCall.respondText("HERE IST MY ROOM " + name)
        }


    }

}