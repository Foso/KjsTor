import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.routing.*
import io.ktor.util.pipeline.ContextDsl


@ContextDsl
fun Routing.get(path:String, function: ApplicationCall.() -> Unit) : Route {
    method(path,HttpMethod.Get,function)
    return route(path, HttpMethod.Get) { handle(function) }
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
            signature.endsWith("?") -> PathSegmentOptionalParameterRouteSelector(
                signature.dropLast(1),
                prefix,
                suffix
            )
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





