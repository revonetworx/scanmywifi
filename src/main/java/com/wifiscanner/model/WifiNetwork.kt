package com.wifiscanner.model

import android.net.wifi.ScanResult
import java.io.Serializable

/**
 * Data class representing a WiFi network with comprehensive details.
 *
 * @property ssid The network's SSID (Service Set Identifier)
 * @property bssid The network's BSSID (Basic Service Set Identifier)
 * @property signalStrength Signal strength in dBm
 * @property frequency Network frequency in MHz
 * @property securityType Network security type
 * @property isConnected Whether the device is currently connected to this network
 * @property channelWidth The width of the channel
 * @property capabilities Raw capabilities string from ScanResult
 */
data class WifiNetwork(
    val ssid: String,
    val bssid: String,
    val signalStrength: Int,
    val frequency: Int,
    val securityType: SecurityType,
    val isConnected: Boolean = false,
    val channelWidth: Int,
    val capabilities: String
) : Serializable {

    /**
     * Enum representing different WiFi security types
     */
    enum class SecurityType {
        OPEN,
        WEP,
        WPA,
        WPA2,
        WPA3,
        UNKNOWN
    }

    companion object {
        /**
         * Convert a ScanResult to a WifiNetwork object
         *
         * @param scanResult Source ScanResult from Android WiFi scan
         * @return WifiNetwork representation of the scan result
         */
        fun fromScanResult(scanResult: ScanResult, isConnected: Boolean = false): WifiNetwork {
            return WifiNetwork(
                ssid = scanResult.SSID ?: "Unknown",
                bssid = scanResult.BSSID ?: "Unknown",
                signalStrength = scanResult.level,
                frequency = scanResult.frequency,
                securityType = determineSecurityType(scanResult.capabilities),
                isConnected = isConnected,
                channelWidth = scanResult.channelWidth,
                capabilities = scanResult.capabilities
            )
        }

        /**
         * Determine the security type based on network capabilities
         *
         * @param capabilities Raw capabilities string from ScanResult
         * @return Corresponding SecurityType
         */
        private fun determineSecurityType(capabilities: String): SecurityType {
            return when {
                capabilities.contains("WPA3") -> SecurityType.WPA3
                capabilities.contains("WPA2") -> SecurityType.WPA2
                capabilities.contains("WPA") -> SecurityType.WPA
                capabilities.contains("WEP") -> SecurityType.WEP
                capabilities.contains("Open") -> SecurityType.OPEN
                else -> SecurityType.UNKNOWN
            }
        }

        /**
         * Get a human-readable signal strength description
         *
         * @param signalStrength Signal strength in dBm
         * @return Descriptive signal strength level
         */
        fun getSignalStrengthDescription(signalStrength: Int): String {
            return when {
                signalStrength >= -50 -> "Excellent"
                signalStrength >= -60 -> "Good"
                signalStrength >= -70 -> "Fair"
                signalStrength >= -80 -> "Weak"
                else -> "Very Weak"
            }
        }
    }
}