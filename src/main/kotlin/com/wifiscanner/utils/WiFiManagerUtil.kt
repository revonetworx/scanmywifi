package com.wifiscanner.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Utility class for managing WiFi manager initialization and operations
 */
class WiFiManagerUtil(private val context: Context) {

    /**
     * Retrieves the WiFi system service with proper null and compatibility checks
     * @return WifiManager instance or null if not available
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun initializeWiFiManager(): WifiManager? {
        return try {
            // Safely retrieve WiFi system service
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            
            // Validate WiFi manager availability
            when {
                wifiManager == null -> {
                    logError("WiFi manager could not be initialized")
                    null
                }
                !isWiFiScanningSupported(wifiManager) -> {
                    logError("WiFi scanning is not supported on this device")
                    null
                }
                else -> wifiManager
            }
        } catch (e: Exception) {
            logError("Error initializing WiFi manager: ${e.message}")
            null
        }
    }

    /**
     * Check if WiFi scanning is supported on the device
     * @param wifiManager The WiFi manager instance
     * @return Boolean indicating WiFi scanning support
     */
    private fun isWiFiScanningSupported(wifiManager: WifiManager): Boolean {
        return try {
            wifiManager.isWifiEnabled || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        } catch (e: Exception) {
            logError("Error checking WiFi scanning support: ${e.message}")
            false
        }
    }

    /**
     * Logs error messages for debugging and tracking
     * @param message Error message to log
     */
    private fun logError(message: String) {
        // In a real app, replace with proper logging mechanism
        println("WiFiManagerUtil Error: $message")
    }

    /**
     * Checks if WiFi is enabled on the device
     * @return Boolean indicating WiFi enabled status
     */
    fun isWiFiEnabled(): Boolean {
        return try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            wifiManager?.isWifiEnabled == true
        } catch (e: Exception) {
            logError("Error checking WiFi status: ${e.message}")
            false
        }
    }
}