package de.jensklingenberg

external fun require(module: String): dynamic

fun entriesOf(jsObject: dynamic): List<Pair<String, Any?>> =
    (js("Object.entries") as (dynamic) -> Array<Array<Any?>>)
        .invoke(jsObject)
        .map { entry -> entry[0] as String to entry[1] }

fun mapEntriesOf(jsObject: dynamic): Map<String, Any?> =
    entriesOf(jsObject).toMap()
