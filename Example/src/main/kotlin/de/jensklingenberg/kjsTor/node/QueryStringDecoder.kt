package de.jensklingenberg.kjsTor.node

import de.jensklingenberg.mapEntriesOf

class QueryStringDecoder(val uri: String) {


    /**
     * Returns the decoded key-value parameter pairs of the URI.
     */
    fun parameters(): Map<String, List<String>> {
        return decode(uri)
    }

    private fun decode(uri: String): Map<String, List<String>> {
        val myQuery = url.parse(uri,true).query

        return mapEntriesOf(myQuery).mapValues {
          val newValue =  "${it.value}"
            newValue.split(",")
        }
    }
}