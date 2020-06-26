package de.jensklingenberg.kjsTor

import Buffer
import http.Server
import http.ServerResponse
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.content.LocalFileContent
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.BaseApplicationEngine
import io.ktor.server.engine.EnginePipeline
import io.ktor.util.pipeline.PipelinePhase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NodeJsApplicationEngine(environment: ApplicationEngineEnvironment, val configure: Configuration.() -> Unit = {}) :
    BaseApplicationEngine(environment) {
    class Configuration : BaseApplicationEngine.Configuration() {

    }

    private val configuration = Configuration().apply(configure)
    lateinit var server: Server

    private val port = environment.connectors.first().port
    private val hostname = environment.connectors.first().host

    init {
        val afterCall = PipelinePhase("After")
        pipeline.insertPhaseAfter(EnginePipeline.Call, afterCall)
        pipeline.intercept(afterCall) {
            //(call as? NodeJsApplicationCall)//.finish()
        }
    }


    override fun start(wait: Boolean): NodeJsApplicationEngine {
        environment.modules.forEach { myfunc ->
            Application(environment).myfunc()
        }


        environment.start()
        server = http.createServer { req, res ->
            console.log("REQU" + req.rawHeaders)

            console.log("REQU" + req.rawTrailers)
            // val stream = fs.createWriteStream("test.png")

            val myAppCall =
                MyNodeJsAppCall(environment.application, req, res)

            var buffer: Buffer = Buffer.alloc(10)
            when (myAppCall.request.method) {
                HttpMethod.Post, HttpMethod.Put -> {

                    req.on("data") { data ->
                        buffer = Buffer.from(data)
                    }
                    req.on("end") { data ->
                        fs.writeFileSync("tt.png", buffer)
                    }
                }
            }

            console.log("HANDLER" + environment.route?.handlers?.size)
            environment.route?.routiList?.firstOrNull {
                (myAppCall.request.method == it.method) &&
                        (myAppCall.url.startsWith(it.path))
            }.run {
                val routi = this
                if (routi == null) {
                    res.end()
                } else {
                    console.log("HHHIERII")

                    GlobalScope.launch {
                        routi.routeHandler(myAppCall)
                        when (myAppCall.request.method) {
                            HttpMethod.Post -> {
                                if (buffer.length != 10) {
                                    myAppCall.request.setData(buffer)
                                }
                            }
                        }
                        console.log("Request: " + myAppCall.method)
                        myAppCall.response.let {
                            res.toServerResponse(it)
                        }
                    }
                }

            }


        }

        server.listen(port) { println("Se rver running at http://${hostname}:${port}/") }
        return this
    }

    override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {
        server.close { }
    }


}


fun ServerResponse.toServerResponse(myResponse: MyResponse) {
    this.statusCode = myResponse.statusCode.value
    myResponse.headers.allValues().forEach { s, list ->
        this.setHeader(s, list.joinToString { it })
    }

    when (val content = myResponse.content) {
        is TextContent -> {
            console.log(content.message)
            this.end(content.message)
        }
        is LocalFileContent -> {
            val filePath = content.filePath
            val contentType = content.contentType.contentType + "/" + content.contentType.contentSubtype

            fs.readFile(filePath) { error, data ->

                this.end(data, "utf-8")
            }
        }

    }

}