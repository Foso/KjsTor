package de.jensklingenberg

import de.jensklingenberg.kjsTor.express.ExpressJsServer
import de.jensklingenberg.kjsTor.node.NodeJsServer
import de.jensklingenberg.kjstor.appCall
import de.jensklingenberg.kjstor.respondText
import de.jensklingenberg.showdown.model.MyTest
import io.ktor.application.call
import io.ktor.routing.*

import io.ktor.server.engine.embeddedServer


fun main() {
    start()
}

//"/home/jens/Code/2020/NodeJsKtor/kjsTor/package.json"
//curl -d "param1=value1&param2=value2" -X POST http:ost:3006/hello
fun start() {

    embeddedServer(ExpressJsServer, port = 3008){

        routing {
            get2("/hallo") {
                console.log("COUR")
                call.respondText(MyTest.test)
            }

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
    }.start(true)


}
