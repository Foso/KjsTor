package io.ktor.routing

import Routi
import io.ktor.application.ApplicationCall
import io.ktor.http.Parameters
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.PipelineInterceptor
import de.jensklingenberg.kjsTor.MyNodeJsAppCall
import de.jensklingenberg.kjsTor.ktor.MyApplicationCall
import io.ktor.application.MyApplicationCallPipeline
import kotlinx.coroutines.CoroutineScope

typealias dui = suspend MyNodeJsAppCall.() -> Unit

open class Route(val parent: Route?, val selector: RouteSelector) : MyApplicationCallPipeline() {
    /**
     * List of child routes for this node
     */
    val children: List<Route> get() = childList

    private val childList: MutableList<Route> = ArrayList()
    val bodies = ArrayList<dui>()

    private var cachedPipeline: MyApplicationCallPipeline? = null

    internal val handlers = ArrayList<PipelineInterceptor<Unit, MyApplicationCall>>()
    var routiList : MutableList<Routi> = mutableListOf()
    private val tracers = mutableListOf<(RoutingResolveTrace) -> Unit>()

    suspend fun interceptor(context: PipelineContext<Unit, ApplicationCall>) {
        //TODO

    }

    suspend fun interceptor(context: CoroutineScope,call: MyApplicationCall) {
        //TODO
        val resolveContext = RoutingResolveContext(this, call, tracers)
        val resolveResult = resolveContext.resolve()
        if (resolveResult is RoutingResolveResult.Success) {
            console.log("SUCCESS")
            //executeResult(context, resolveResult.route, resolveResult.parameters)
        }else {
            console.log(resolveResult)
            //executeResult(context, resolveResult.route, resolveResult.parameters)
        }
    }

    private suspend fun executeResult(
        context: PipelineContext<Unit, ApplicationCall>,
        route: Route,
        parameters: Parameters
    ) {

    }
    fun myAddChild(route: Routi){
        routiList.add(route)
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
    fun handle(handler: PipelineInterceptor<Unit, MyApplicationCall>) {
        handlers.add(handler)

        // Adding a handler invalidates only pipeline for this entry
        cachedPipeline = null
    }

    fun handle(handler : suspend MyNodeJsAppCall.() -> Unit) {
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
    internal fun buildPipeline(): MyApplicationCallPipeline {
        return cachedPipeline ?: run {
            var current: Route? = this
            val pipeline = MyApplicationCallPipeline()
            val routePipelines = mutableListOf<MyApplicationCallPipeline>()
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
               //TODO pipeline.intercept(Call, handlers[index])
            }
            cachedPipeline = pipeline
            pipeline
        }
    }
}