package com.wifiscanner.model

import org.junit.Assert.assertEquals
import org.junit.Test

class WiFiNetworkTest {

    @Test
    fun `test getSignalStrengthDescription returns correct description`() {
        val excellentSignal = WiFiNetwork(
            "TestNet", 
            "00:11:22:33:44:55", 
            -40, 
            "WPA2", 
            2400
        )
        assertEquals("Excellent", excellentSignal.getSignalStrengthDescription())

        val goodSignal = WiFiNetwork(
            "TestNet", 
            "00:11:22:33:44:55", 
            -55, 
            "WPA2", 
            2400
        )
        assertEquals("Good", goodSignal.getSignalStrengthDescription())

        val fairSignal = WiFiNetwork(
            "TestNet", 
            "00:11:22:33:44:55", 
            -65, 
            "WPA2", 
            2400
        )
        assertEquals("Fair", fairSignal.getSignalStrengthDescription())

        val poorSignal = WiFiNetwork(
            "TestNet", 
            "00:11:22:33:44:55", 
            -80, 
            "WPA2", 
            2400
        )
        assertEquals("Poor", poorSignal.getSignalStrengthDescription())
    }
}