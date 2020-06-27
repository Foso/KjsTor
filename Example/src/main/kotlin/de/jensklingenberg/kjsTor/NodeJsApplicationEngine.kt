package de.jensklingenberg.kjsTor

import Buffer


import http.Server
import http.ServerResponse
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.content.LocalFileContent
import io.ktor.http.content.TextContent
import io.ktor.response.ApplicationSendPipeline
import io.ktor.routing.*
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
        pipeline.intercept(EnginePipeline.Call) {
            console.log("HIR")
            // (call as? MyNodeJsAppCall)//.finish()
        }
        pipeline.sendPipeline.intercept(ApplicationSendPipeline.After) {
            console.log("AFTER")
        }

    }


    override fun start(wait: Boolean): NodeJsApplicationEngine {

        environment.modules.forEach { myfunc ->
            Application(environment).myfunc()
        }


        environment.start()
        server = http.createServer { req, res ->
            console.log("REQU" + req.rawHeaders)


            val myAppCall =
                MyNodeJsAppCall(environment.application, req, res)


            var bestRoute: Route? = null
            environment.route?.run {

                val routing = Routing(environment.application)

                this.routiList.forEach {
                    val ro = it
                    console.log("RO" + ro.path)
                    val selector = HttpMethodRouteSelector(ro.method)
                    routing.createRouteFromPath(ro.path).createChild(selector).myAddChild(it)
                }


                val resolveContext = RoutingResolveContext(routing, myAppCall, emptyList())

                val root = routing
                val rootResult = root.selector.evaluate(resolveContext, 0)

                console.log("INCR " + rootResult.segmentIncrement)
                val result = resolveContext.resolve(root, rootResult.segmentIncrement)

                if (rootResult.succeeded) {
                    bestRoute = result.route
                    bestRoute?.routiList!!.forEach {
                        console.log("HEYROUTE " + it.path)
                    }
                }
                console.log("SUCS" + result.route)
                console.log(result)

            }

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
            console.log("SIZE" + environment.route?.routiList?.size)
            environment.route?.routiList?.forEach {
                console.log("NodeJsApplicationEngine " + it.path + " " + it.method)
            }




            environment.route?.routiList?.firstOrNull {
                (myAppCall.request.method == it.method) &&
                        (it.path == bestRoute?.routiList?.first()?.path)
            }.run {
                val routi = this
                if (routi == null) {
                    res.end()
                } else {


                    GlobalScope.launch(environment.parentCoroutineContext) {
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


fun ServerResponse.toServerResponse(nodeJsResponse: NodeJsResponse) {
    this.statusCode = nodeJsResponse.statusCode.value
    nodeJsResponse.headers.allValues().forEach { s, list ->
        this.setHeader(s, list.joinToString { it })
    }

    when (val content = nodeJsResponse.content) {
        is TextContent -> {
            console.log("toServerResponse" + content.message)
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