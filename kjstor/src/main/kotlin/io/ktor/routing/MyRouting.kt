/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.routing



import io.ktor.application.*
import io.ktor.http.HttpMethod
import io.ktor.http.decodeURLPart
import io.ktor.request.ApplicationRequest


import io.ktor.util.pipeline.*

/**
 * Returns request HTTP method possibly overridden via header X-Http-Method-Override
 */
val ApplicationRequest.httpMethod: HttpMethod get() = local.method


class RootRouteSelector(rootPath: String) :RouteSelector(RouteSelectorEvaluation.qualityConstant) {
    private val parts = RoutingPath.parse(rootPath).parts.map {
        require(it.kind == RoutingPathSegmentKind.Constant) {
            "rootPath should be constant, no wildcards supported."
        }
        it.value
    }
    private val successEvaluationResult = RouteSelectorEvaluation(
        true, RouteSelectorEvaluation.qualityConstant,
        segmentIncrement = parts.size
    )

    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        check(segmentIndex == 0) { "Root selector should be evaluated first." }
        if (parts.isEmpty()) {
            return RouteSelectorEvaluation.Constant
        }

        val parts = parts
        val segments = context.segments
        if (segments.size < parts.size) {
            return RouteSelectorEvaluation.Failed
        }

        for (index in segmentIndex until segmentIndex + parts.size) {
            if (segments[index] != parts[index]) {
                return RouteSelectorEvaluation.Failed
            }
        }

        return successEvaluationResult
    }

    override fun toString(): String = parts.joinToString("/")
}





/**
 * Builds a route to match specified [method] and [path]
 */
@ContextDsl
fun Route.route(path: String, method: HttpMethod, build: Route.() -> Unit): Route {
    val selector = HttpMethodRouteSelector(method)

    return createRouteFromPath(path).createChild(selector).apply(build)
}


@ContextDsl
fun Routing.get(path:String, function: ApplicationCall.() -> Unit) : Route {
    method(path,HttpMethod.Get,function)
    return route(path, HttpMethod.Get) { handle(function) }
}






@ContextDsl
fun Routing.post(path:String, function: ApplicationCall.() -> Unit) {
    method(path,HttpMethod.Post,function)
}

@ContextDsl
fun Routing.put(path:String, function: ApplicationCall.() -> Unit) {
    method(path,HttpMethod.Put,function)
}

@ContextDsl
fun Routing.delete(path:String, function: ApplicationCall.() -> Unit) {
    method(path,HttpMethod.Delete,function)
}


fun Route.method(path:String, method: HttpMethod, function: ApplicationCall.() -> Unit): Route {
    this.application.apply {
        if(environment.route==null){
            environment.route = Route(null, RootRouteSelector(""))
        }
        environment.route?.myAddChild(Routi(path, method,function))
    }

    return this.application.environment.route!!

}







/**
 * Create a routing entry for specified path
 */
fun Route.createRouteFromPath(path: String): Route {
    val parts = RoutingPath.parse(path).parts
    var current: Route = this
    for ((value, kind) in parts) {
        val selector = when (kind) {
            RoutingPathSegmentKind.Parameter -> PathSegmentSelectorBuilder.parseParameter(value)
            RoutingPathSegmentKind.Constant -> PathSegmentSelectorBuilder.parseConstant(value)
        }
        // there may already be entry with same selector, so join them
        current = current.createChild(selector)
    }
    return current
}

/**
 * Represents a parsed routing path. Consist of number of segments [parts]
 *
 * @property parts contains parsed routing path segments
 */
class RoutingPath private constructor(val parts: List<RoutingPathSegment>) {
    companion object {
        /**
         * A constant for root routing path
         */
        val root: RoutingPath = RoutingPath(listOf())

        /**
         * Parse the specified [path] and create an instance of [RoutingPath].
         * It handles wildcards and decodes escape characters properly.
         */
        fun parse(path: String): RoutingPath {
            if (path == "/") return root
            val segments = path.splitToSequence("/").filter { it.isNotEmpty() }.map { segment ->
                when {
                    segment.contains('{') && segment.contains('}') -> RoutingPathSegment(segment, RoutingPathSegmentKind.Parameter)
                    else -> RoutingPathSegment(segment.decodeURLPart(), RoutingPathSegmentKind.Constant)
                }
            }

            return RoutingPath(segments.toList())
        }
    }

    override fun toString(): String = parts.joinToString("/") { it.value }
}

/**
 * Represent a single routing path segment
 * @property value - segment text value
 * @property kind - segment kind (constant or parameter)
 */
data class RoutingPathSegment(val value: String, val kind: RoutingPathSegmentKind)

/**
 * Possible routing path segment kinds
 */
enum class RoutingPathSegmentKind {
    /**
     * Corresponds to constant path segment
     */
    Constant,

    /**
     * Corresponds to a parameter path segment (wildcard or named parameter or both)
     */
    Parameter
}

