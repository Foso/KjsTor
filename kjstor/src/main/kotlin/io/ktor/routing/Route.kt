package io.ktor.routing


import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.http.HttpMethod
import io.ktor.util.pipeline.PipelineInterceptor

typealias dui = ApplicationCall.() -> Unit


class Routi(val path: String, val method: HttpMethod, val routeHandler: ApplicationCall.() -> Unit)

open class Route(val parent: Route?, val selector: RouteSelector) : ApplicationCallPipeline() {
    /**
     * List of child routes for this node
     */
    val children: List<Route> get() = childList

    private val childList: MutableList<Route> = ArrayList()
    val bodies = ArrayList<dui>()

    private var cachedPipeline: ApplicationCallPipeline? = null

    internal val handlers = ArrayList<PipelineInterceptor<Unit, ApplicationCall>>()
    var routiList : MutableList<Routi> = mutableListOf()


    var path =""

    fun addPath(path:String){
        this.path= path
    }
    fun myAddChild(route: Routi){
       if( routiList.none() { it.path == route.path }){
           console.log("Myaddchild")
           routiList.add(route)
       }

    }

    /**
     * Creates a child node in this node with a given [selector] or returns an existing one with the same selector
     */
    fun createChild(selector: RouteSelector): Route {
        val existingEntry = childList.firstOrNull { it.selector == selector }
        if (existingEntry == null) {
            val entry = Route(this, selector)
            childList.add(entry)
            return entry
        }
        return existingEntry
    }

    /**
     * Installs a handler into this route which will be called when the route is selected for a call
     */
    fun handle(handler: PipelineInterceptor<Unit, ApplicationCall>) {
        handlers.add(handler)

        // Adding a handler invalidates only pipeline for this entry
        cachedPipeline = null
    }

    fun handle(handler : ApplicationCall.() -> Unit) {
        bodies.add(handler)
    }

    /**
     * Allows using route instance for building additional routes
     */
    operator fun invoke(body: Route.() -> Unit): Unit = body()

    private fun invalidateCachesRecursively() {
        cachedPipeline = null
        childList.forEach { it.invalidateCachesRecursively() }
    }

    override fun toString(): String = when {
        parent == null -> "/$selector"
        parent.parent == null -> parent.toString().let { parentText ->
            when {
                parentText.endsWith('/') -> "$parentText$selector"
                else -> "$parentText/$selector"
            }
        }
        else -> "$parent/$selector"
    }

    override fun afterIntercepted() {
        // Adding an interceptor invalidates pipelines for all children
        // We don't need synchronisation here, because order of intercepting and acquiring pipeline is indeterminate
        // If some child already cached its pipeline, it's ok to execute with outdated pipeline
        invalidateCachesRecursively()
    }
    internal fun buildPipeline(): ApplicationCallPipeline {
        console.log("BuildPipeline")
        return cachedPipeline ?: run {
            var current: Route? = this
            val pipeline = ApplicationCallPipeline()
            val routePipelines = mutableListOf<ApplicationCallPipeline>()
            while (current != null) {
                routePipelines.add(current)
                current = current.parent
            }

            for (index in routePipelines.lastIndex downTo 0) {
                val routePipeline = routePipelines[index]
                pipeline.merge(routePipeline)
                pipeline.receivePipeline.merge(routePipeline.receivePipeline)
                pipeline.sendPipeline.merge(routePipeline.sendPipeline)
            }

            val handlers = handlers
            for (index in 0..handlers.lastIndex) {
                pipeline.intercept(Call, handlers[index])
            }
            cachedPipeline = pipeline
            pipeline
        }
    }
}