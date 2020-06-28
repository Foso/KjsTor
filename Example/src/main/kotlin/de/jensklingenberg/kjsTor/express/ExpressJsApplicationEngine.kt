package de.jensklingenberg.kjsTor.express

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.BaseApplicationEngine
external fun require(module: String): dynamic


class ExpressJsApplicationEngine(environment: ApplicationEngineEnvironment, val configure: ExpressJsApplicationEngine.Configuration.() -> Unit = {}) : BaseApplicationEngine(environment){

    class Configuration : BaseApplicationEngine.Configuration() {

    }

    private val port = environment.connectors.first().port
    private val hostname = environment.connectors.first().host

    private val configuration = Configuration()
        .apply(configure)


    override fun start(wait: Boolean): ApplicationEngine {
        console.log("START=============================================")

        val wsapp = ExpressWs( Express())
        val app = wsapp.app
        app.get("*"){req, res ->
            res.send("i am a beautiful butterfly"+req.path)
        }
        app.listen(port) { console.log("listening on port 3000") }


        app.ws("/echo"){ws, req ->
            ws.on("message"){msg->
                console.log("Message"+msg)
            }

        }

        return this
    }

    override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {


    }

}

/**
 *  val express = require("express")
val app = express()

app.get("/", { req, res ->
res.type("text/plain")
res.send("i am a beautiful butterfly")
})

app.listen(3000, {
println("Listening on port 3000")
})
 *
 * */