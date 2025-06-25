package com.wifiscanner.network

import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.ScanResult

class NetworkDetailsRetrieverTest {

    @Test
    fun `test network details retrieval with valid networks`() {
        // Mock context and WiFi manager
        val context = mock(Context::class.java)
        val wifiManager = mock(WifiManager::class.java)

        // Setup test scenario
        `when`(context.applicationContext.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiManager)
        `when`(wifiManager.isWifiEnabled).thenReturn(true)
        `when`(wifiManager.startScan()).thenReturn(true)

        // Create a mock scan result
        val scanResult = mock(ScanResult::class.java)
        `when`(scanResult.SSID).thenReturn("TestNetwork")
        `when`(scanResult.BSSID).thenReturn("00:11:22:33:44:55")
        `when`(scanResult.level).thenReturn(-55)
        `when`(scanResult.capabilities).thenReturn("WPA2")
        `when`(wifiManager.scanResults).thenReturn(listOf(scanResult))

        val retriever = NetworkDetailsRetriever(context)
        val result = retriever.retrieveNetworkDetails()

        assertTrue(result.isSuccess)
        val networks = result.getOrNull()
        assertNotNull(networks)
        assertEquals(1, networks?.size)
        assertEquals("TestNetwork", networks?.first()?.ssid)
        assertEquals("WPA2", networks?.first()?.securityType)
    }

    @Test
    fun `test error handling when WiFi is disabled`() {
        val context = mock(Context::class.java)
        val wifiManager = mock(WifiManager::class.java)

        `when`(context.applicationContext.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiManager)
        `when`(wifiManager.isWifiEnabled).thenReturn(false)

        val retriever = NetworkDetailsRetriever(context)
        val result = retriever.retrieveNetworkDetails()

        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertTrue(error is NetworkError.WiFiDisabled)
    }

    @Test
    fun `test handling of incomplete network details`() {
        val context = mock(Context::class.java)
        val wifiManager = mock(WifiManager::class.java)

        `when`(context.applicationContext.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiManager)
        `when`(wifiManager.isWifiEnabled).thenReturn(true)
        `when`(wifiManager.startScan()).thenReturn(true)

        // Create an invalid scan result
        val scanResult = mock(ScanResult::class.java)
        `when`(scanResult.SSID).thenReturn(null)
        `when`(wifiManager.scanResults).thenReturn(listOf(scanResult))

        val retriever = NetworkDetailsRetriever(context)
        val result = retriever.retrieveNetworkDetails()

        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertTrue(error is NetworkError.InsufficientNetworkDetails)
    }
}