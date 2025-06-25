package com.wifiscanner.network

/**
 * Sealed class representing different types of network retrieval errors.
 * Provides a comprehensive way to handle various error scenarios in WiFi network scanning.
 */
sealed class NetworkError(val message: String) {
    /**
     * Represents an error where WiFi scanning is not permitted due to insufficient permissions.
     */
    class PermissionDenied : NetworkError("WiFi scanning permission not granted")

    /**
     * Represents an error where WiFi is disabled on the device.
     */
    class WiFiDisabled : NetworkError("WiFi is currently disabled")

    /**
     * Represents an error during network scanning process.
     * @param details Additional details about the scanning failure
     */
    class ScanFailure(details: String = "Unknown scanning error") : NetworkError(details)

    /**
     * Represents an error where network details are incomplete or invalid.
     */
    class InsufficientNetworkDetails : NetworkError("Network details are incomplete")

    /**
     * Represents a system-level error during network retrieval.
     * @param cause The underlying exception that caused the error
     */
    class SystemError(cause: Throwable) : NetworkError("System error: ${cause.localizedMessage}")

    // Utility method to provide user-friendly error messages
    fun toUserMessage(): String = when (this) {
        is PermissionDenied -> "Please grant WiFi scanning permission"
        is WiFiDisabled -> "Please enable WiFi to scan networks"
        is ScanFailure -> "Network scan failed: $message"
        is InsufficientNetworkDetails -> "Unable to retrieve complete network information"
        is SystemError -> "A system error occurred while scanning networks"
    }
}