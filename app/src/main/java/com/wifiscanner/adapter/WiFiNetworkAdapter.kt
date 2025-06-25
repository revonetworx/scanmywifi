package com.wifiscanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wifiscanner.R
import com.wifiscanner.model.WiFiNetwork

/**
 * RecyclerView adapter for displaying WiFi network details
 * @property networks List of WiFi networks to display
 */
class WiFiNetworkAdapter(private val networks: List<WiFiNetwork>) : 
    RecyclerView.Adapter<WiFiNetworkAdapter.WiFiNetworkViewHolder>() {

    /**
     * ViewHolder class for WiFi network items
     * @param itemView The view for each list item
     */
    class WiFiNetworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ssidTextView: TextView = itemView.findViewById(R.id.ssid_text_view)
        private val bssidTextView: TextView = itemView.findViewById(R.id.bssid_text_view)
        private val signalStrengthTextView: TextView = itemView.findViewById(R.id.signal_strength_text_view)
        private val securityTypeTextView: TextView = itemView.findViewById(R.id.security_type_text_view)

        /**
         * Bind WiFi network data to the view
         * @param network WiFiNetwork object to display
         */
        fun bind(network: WiFiNetwork) {
            ssidTextView.text = network.ssid
            bssidTextView.text = network.bssid
            signalStrengthTextView.text = "${network.signalStrength} dBm (${network.getSignalStrengthDescription()})"
            securityTypeTextView.text = network.securityType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WiFiNetworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wifi_network_item, parent, false)
        return WiFiNetworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: WiFiNetworkViewHolder, position: Int) {
        holder.bind(networks[position])
    }

    override fun getItemCount(): Int = networks.size
}