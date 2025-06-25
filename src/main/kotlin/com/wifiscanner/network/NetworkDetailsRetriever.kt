package com.wifiscanner.network

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.ScanResult

/**
 * Responsible for retrieving and processing WiFi network details with robust error handling.
 * @param context Application context for accessing system services
 */
class NetworkDetailsRetriever(private val context: Context) {

    private val wifiManager: WifiManager by lazy { 
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager 
    }

    /**
     * Retrieves available WiFi networks with comprehensive error handling.
     * @return Result containing either a list of network details or a NetworkError
     */
    fun retrieveNetworkDetails(): Result<List<NetworkDetails>> {
        // Check if WiFi is enabled
        if (!wifiManager.isWifiEnabled) {
            return Result.failure(NetworkError.WiFiDisabled())
        }

        return try {
            // Trigger WiFi scan
            val scanSuccess = wifiManager.startScan()
            
            if (!scanSuccess) {
                return Result.failure(NetworkError.ScanFailure("Scan initiation failed"))
            }

            // Get scan results
            val scanResults = wifiManager.scanResults

            // Process and validate network details
            val processedNetworks = scanResults
                .mapNotNull { processNetworkResult(it) }
                .also { networks ->
                    if (networks.isEmpty()) {
                        throw NetworkError.InsufficientNetworkDetails()
                    }
                }

            Result.success(processedNetworks)

        } catch (e: SecurityException) {
            Result.failure(NetworkError.PermissionDenied())
        } catch (e: NetworkError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(NetworkError.SystemError(e))
        }
    }

    /**
     * Processes individual scan results with validation.
     * @param scanResult Raw WiFi scan result
     * @return Processed NetworkDetails or null if invalid
     */
    private fun processNetworkResult(scanResult: ScanResult): NetworkDetails? {
        return try {
            NetworkDetails(
                ssid = scanResult.SSID ?: return null,
                bssid = scanResult.BSSID ?: return null,
                signalStrength = scanResult.level,
                securityType = determineSecurityType(scanResult)
            )
        } catch (e: Exception) {
            null  // Silently drop invalid network entries
        }
    }

    /**
     * Determines the security type of a WiFi network.
     * @param scanResult Raw WiFi scan result
     * @return Security type as a string
     */
    private fun determineSecurityType(scanResult: ScanResult): String {
        return when {
            scanResult.capabilities.contains("WPA3") -> "WPA3"
            scanResult.capabilities.contains("WPA2") -> "WPA2"
            scanResult.capabilities.contains("WPA") -> "WPA"
            scanResult.capabilities.contains("WEP") -> "WEP"
            else -> "Open"
        }
    }
}

/**
 * Data class representing processed network details.
 * Ensures that only valid network information is stored.
 */
data class NetworkDetails(
    val ssid: String,
    val bssid: String,
    val signalStrength: Int,
    val securityType: String
)