package io.ktor.routing

/**
 * Base type for all routing selectors
 *
 * @param quality indicates how good this selector is compared to siblings
 */
abstract class RouteSelector(val quality: Double) {
    /**
     * Evaluates this selector against [context] and a path segment at [segmentIndex]
     */
    abstract fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation
}