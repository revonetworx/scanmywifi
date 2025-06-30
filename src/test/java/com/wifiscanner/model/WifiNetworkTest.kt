package com.wifiscanner.model

import android.net.wifi.ScanResult
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WifiNetworkTest {

    @Test
    fun `test WifiNetwork creation from ScanResult`() {
        // Mock ScanResult
        val mockScanResult = ScanResult().apply {
            SSID = "TestNetwork"
            BSSID = "00:11:22:33:44:55"
            level = -65
            frequency = 2437
            capabilities = "WPA2-PSK"
            channelWidth = ScanResult.CHANNEL_WIDTH_20MHZ
        }

        val wifiNetwork = WifiNetwork.fromScanResult(mockScanResult)

        assertEquals("TestNetwork", wifiNetwork.ssid)
        assertEquals("00:11:22:33:44:55", wifiNetwork.bssid)
        assertEquals(-65, wifiNetwork.signalStrength)
        assertEquals(2437, wifiNetwork.frequency)
        assertEquals(WifiNetwork.SecurityType.WPA2, wifiNetwork.securityType)
        assertFalse(wifiNetwork.isConnected)
    }

    @Test
    fun `test security type detection`() {
        val testCases = listOf(
            "WPA3-PSK" to WifiNetwork.SecurityType.WPA3,
            "WPA2-PSK" to WifiNetwork.SecurityType.WPA2,
            "WPA-PSK" to WifiNetwork.SecurityType.WPA,
            "WEP" to WifiNetwork.SecurityType.WEP,
            "Open" to WifiNetwork.SecurityType.OPEN,
            "" to WifiNetwork.SecurityType.UNKNOWN
        )

        testCases.forEach { (capabilities, expectedType) ->
            val mockScanResult = ScanResult().apply {
                this.capabilities = capabilities
                SSID = "TestNetwork"
                BSSID = "00:11:22:33:44:55"
                level = -65
                frequency = 2437
                channelWidth = ScanResult.CHANNEL_WIDTH_20MHZ
            }

            val wifiNetwork = WifiNetwork.fromScanResult(mockScanResult)
            assertEquals(expectedType, wifiNetwork.securityType)
        }
    }

    @Test
    fun `test signal strength description`() {
        val testCases = listOf(
            -40 to "Excellent",
            -55 to "Good",
            -65 to "Fair",
            -75 to "Weak",
            -85 to "Very Weak"
        )

        testCases.forEach { (signalStrength, expectedDescription) ->
            val description = WifiNetwork.getSignalStrengthDescription(signalStrength)
            assertEquals(expectedDescription, description)
        }
    }
}