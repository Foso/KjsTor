package io.ktor.server



/**
 * Represents a type of a connector, e.g HTTP or HTTPS.
 * @param name name of the connector.
 *
 * Some engines can support other connector types, hence not a enum.
 */
data class ConnectorType(val name: String) {
    companion object {
        /**
         * Non-secure HTTP connector.
         * 1
         */
        val HTTP = ConnectorType("HTTP")

        /**
         * Secure HTTP connector.
         */
        val HTTPS = ConnectorType("HTTPS")
    }
}

/**
 * Represents a connector configuration.
 */
interface EngineConnectorConfig {
    /**
     * Type of the connector, e.g HTTP or HTTPS.
     */
    val type: ConnectorType

    /**
     * The network interface this host binds to as an IP address or a hostname.  If null or 0.0.0.0, then bind to all interfaces.
     */
    val host: String

    /**
     * The port this application should be bound to.
     */
    val port: Int
}

/**
 * Mutable implementation of EngineConnectorConfig for building connectors programmatically
 */
open class EngineConnectorBuilder(override val type: ConnectorType = ConnectorType.HTTP) :
    EngineConnectorConfig {
    override var host: String = "0.0.0.0"
    override var port: Int = 80

    override fun toString(): String {
        return "${type.name} $host:$port"
    }
}



