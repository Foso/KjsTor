/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("unused")

package io.ktor.routing





import io.ktor.application.*
import io.ktor.http.*
import io.ktor.util.pipeline.*
import io.ktor.request.*

/**
 * Builds a route to match specified [path]
 */
@ContextDsl
fun Route.route(path: String, build: Route.() -> Unit): Route = createRouteFromPath(path).apply(build)



/**
 * Builds a route to match specified [method]
 */
@ContextDsl
fun Route.method(method: HttpMethod, body: Route.() -> Unit): Route {
    val selector = HttpMethodRouteSelector(method)
    return createChild(selector).apply(body)
}

/**
 * Builds a route to match parameter with specified [name] and [value]
 */
@ContextDsl
fun Route.param(name: String, value: String, build: Route.() -> Unit): Route {
    val selector = ConstantParameterRouteSelector(name, value)
    return createChild(selector).apply(build)
}

/**
 * Builds a route to match parameter with specified [name] and capture its value
 */
@ContextDsl
fun Route.param(name: String, build: Route.() -> Unit): Route {
    val selector = ParameterRouteSelector(name)
    return createChild(selector).apply(build)
}

/**
 * Builds a route to optionally capture parameter with specified [name], if it exists
 */
@ContextDsl
fun Route.optionalParam(name: String, build: Route.() -> Unit): Route {
    val selector = OptionalParameterRouteSelector(name)
    return createChild(selector).apply(build)
}

/**
 * Builds a route to match header with specified [name] and [value]
 */
@ContextDsl
fun Route.header(name: String, value: String, build: Route.() -> Unit): Route {
    val selector = HttpHeaderRouteSelector(name, value)
    return createChild(selector).apply(build)
}

/**
 * Builds a route to match requests with [HttpHeaders.Accept] header matching specified [contentType]
 */
@ContextDsl
fun Route.accept(contentType: ContentType, build: Route.() -> Unit): Route {
    val selector = HttpAcceptRouteSelector(contentType)
    return createChild(selector).apply(build)
}

/**
 * Builds a route to match requests with [HttpHeaders.ContentType] header matching specified [contentType]
 */
@ContextDsl
fun Route.contentType(contentType: ContentType, build: Route.() -> Unit): Route {
    return header(HttpHeaders.ContentType, "${contentType.contentType}/${contentType.contentSubtype}", build)
}

/**
 * Builds a route to match `GET` requests with specified [path]
 */
@ContextDsl
fun Route.get2(path: String, body: PipelineInterceptor<Unit, ApplicationCall>): Route {

console.log("GETS2")
    val ro =  route(path, HttpMethod.Get) { handle(body) }
    this.application.apply {
        if(environment.route==null){
            environment.route = ro
        }
        environment.route?.handle(body)
    }

    return ro
}

/**
 * Builds a route to match `GET` requests
 */
@ContextDsl
fun Route.get2(body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return method(HttpMethod.Get) { handle(body) }
}

/**
 * Builds a route to match `POST` requests with specified [path]
 */
@ContextDsl
fun Route.post2(path: String, body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return route(path, HttpMethod.Post) { handle(body) }
}



/**
 * Builds a route to match `POST` requests
 */
@ContextDsl
fun Route.post(body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return method(HttpMethod.Post) { handle(body) }
}

/**
 * Builds a route to match `HEAD` requests with specified [path]
 */
@ContextDsl
fun Route.head(path: String, body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return route(path, HttpMethod.Head) { handle(body) }
}

/**
 * Builds a route to match `HEAD` requests
 */
@ContextDsl
fun Route.head(body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return method(HttpMethod.Head) { handle(body) }
}

/**
 * Builds a route to match `PUT` requests with specified [path]
 */
@ContextDsl
fun Route.put(path: String, body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return route(path, HttpMethod.Put) { handle(body) }
}

/**
 * Builds a route to match `PUT` requests
 */
@ContextDsl
fun Route.put(body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return method(HttpMethod.Put) { handle(body) }
}

/**
 * Builds a route to match `PATCH` requests with specified [path]
 */
@ContextDsl
fun Route.patch(path: String, body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return route(path, HttpMethod.Patch) { handle(body) }
}

/**
 * Builds a route to match `PATCH` requests
 */
@ContextDsl
fun Route.patch(body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return method(HttpMethod.Patch) { handle(body) }
}

/**
 * Builds a route to match `DELETE` requests with specified [path]
 */
@ContextDsl
fun Route.delete(path: String, body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return route(path, HttpMethod.Delete) { handle(body) }
}

/**
 * Builds a route to match `DELETE` requests
 */
@ContextDsl
fun Route.delete(body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return method(HttpMethod.Delete) { handle(body) }
}

/**
 * Builds a route to match `OPTIONS` requests with specified [path]
 */
@ContextDsl
fun Route.options(path: String, body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return route(path, HttpMethod.Options) { handle(body) }
}

/**
 * Builds a route to match `OPTIONS` requests
 */
@ContextDsl
fun Route.options(body: PipelineInterceptor<Unit, ApplicationCall>): Route {
    return method(HttpMethod.Options) { handle(body) }
}



/**
 * Helper object for building instances of [RouteSelector] from path segments
 */
object PathSegmentSelectorBuilder {
    /**
     * Builds a [RouteSelector] to match a path segment parameter with prefix/suffix and a name
     */
    fun parseParameter(value: String): RouteSelector {
        val prefixIndex = value.indexOf('{')
        val suffixIndex = value.lastIndexOf('}')

        val prefix = if (prefixIndex == 0) null else value.substring(0, prefixIndex)
        val suffix = if (suffixIndex == value.length - 1) null else value.substring(suffixIndex + 1)

        val signature = value.substring(prefixIndex + 1, suffixIndex)
        return when {
            signature.endsWith("?") -> PathSegmentOptionalParameterRouteSelector(signature.dropLast(1), prefix, suffix)
            signature.endsWith("...") -> {
                if (suffix != null && suffix.isNotEmpty()) {
                    throw IllegalArgumentException("Suffix after tailcard is not supported")
                }
                PathSegmentTailcardRouteSelector(signature.dropLast(3), prefix ?: "")
            }
            else -> PathSegmentParameterRouteSelector(signature, prefix, suffix)
        }
    }

    /**
     * Builds a [RouteSelector] to match a constant or wildcard segment parameter
     */
    fun parseConstant(value: String): RouteSelector = when (value) {
        "*" -> PathSegmentWildcardRouteSelector
        else -> PathSegmentConstantRouteSelector(value)
    }

    /**
     * Parses a name out of segment specification
     */
    fun parseName(value: String): String {
        val prefix = value.substringBefore('{', "")
        val suffix = value.substringAfterLast('}', "")
        val signature = value.substring(prefix.length + 1, value.length - suffix.length - 1)
        return when {
            signature.endsWith("?") -> signature.dropLast(1)
            signature.endsWith("...") -> signature.dropLast(3)
            else -> signature
        }
    }
}


fun Route.mymethod(path:String, method: HttpMethod, function: ApplicationCall.() -> Unit): Route {
    this.application.apply {
        if(environment.route==null){
            environment.route = Route(null, RootRouteSelector(""))
        }
        environment.route?.myAddChild(Routi(path, method,function))
    }

    return this.application.environment.route!!

}