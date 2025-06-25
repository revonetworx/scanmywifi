package com.wifiscanner.model

/**
 * Data class representing a WiFi network with its details
 * @property ssid The name of the WiFi network
 * @property bssid The MAC address of the WiFi access point
 * @property signalStrength The signal strength in dBm
 * @property securityType The type of security (e.g., WPA2, Open)
 * @property frequency The frequency of the WiFi network in MHz
 */
data class WiFiNetwork(
    val ssid: String,
    val bssid: String,
    val signalStrength: Int,
    val securityType: String,
    val frequency: Int
) {
    /**
     * Get a human-readable signal strength description
     * @return String describing the signal strength
     */
    fun getSignalStrengthDescription(): String = when {
        signalStrength >= -50 -> "Excellent"
        signalStrength >= -60 -> "Good"
        signalStrength >= -70 -> "Fair"
        else -> "Poor"
    }
}