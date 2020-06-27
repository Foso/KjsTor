package de.jensklingenberg

import de.jensklingenberg.kjsTor.NodeJsServer
import de.jensklingenberg.kjsTor.ktor.appCall
import de.jensklingenberg.kjsTor.ktor.respondText
import de.jensklingenberg.showdown.model.MyTest
import get
import io.ktor.application.ApplicationCallPipeline
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import post


fun main() {
    start()
}

//"/home/jens/Code/2020/NodeJsKtor/kjsTor/package.json"
//curl -d "param1=value1&param2=value2" -X POST http:ost:3006/hello
fun start() {


    embeddedServer(NodeJsServer, 3008) {


        intercept(ApplicationCallPipeline.Call) {
            console.log("FEATURE")
        }

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
                console.log("HALLO")
                appCall.respondText("HERE IST MY ROOM")
            }



        }



    }.start(true)

}
