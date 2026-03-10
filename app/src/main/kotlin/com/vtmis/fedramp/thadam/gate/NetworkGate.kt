package com.vtmis.fedramp.thadam.gate

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.telephony.TelephonyManager
import com.vtmis.fedramp.thadam.agent.CIAAgent
import com.vtmis.fedramp.thadam.model.GateIds
import com.vtmis.fedramp.thadam.model.GateNames

/**
 * Gate 1 — Network Gate
 *
 * Position: Between external network interfaces and device internal bus.
 * Addresses: SAR-CRIT-002, SAR-MOD-005, SAR-MOD-006, SAR-MOD-007,
 *            SAR-MOD-009, SAR-LOW-005, SCTH-002, SCTH-006
 *
 * Trust Score = 0.35×VPN + 0.25×carrier_enc + 0.15×MAC + 0.10×DNS + 0.10×single_if + 0.05×BT
 */
class NetworkGate(
    ciaAgent: CIAAgent,
    private val appContext: Context
) : C3Gate(GateIds.NETWORK, GateNames.NETWORK, ciaAgent) {

    override fun evaluateContext(): Double {
        var score = 0.0
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val caps = network?.let { cm.getNetworkCapabilities(it) }

        // VPN active? (40% of context)
        val vpnActive = caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
        score += 0.40 * if (vpnActive) 1.0 else 0.0

        // Known carrier? (30% of context)
        try {
            val tm = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val carrier = tm.networkOperatorName ?: ""
            score += 0.30 * if (carrier.isNotEmpty()) 1.0 else 0.3
        } catch (_: Exception) {
            score += 0.30 * 0.3
        }

        // Network type (30% of context) — cellular preferred over Wi-Fi for Federal
        score += 0.30 * when {
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> 1.0
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> 0.6
            else -> 0.0
        }

        return score
    }

    override fun evaluateControl(): Double {
        var score = 0.0

        // VPN provides encryption control (35%)
        score += 0.35 * ciaAgent.checkVPNActive()

        // MAC randomization (20%)
        score += 0.20 * ciaAgent.checkMACRandomization()

        // DNS protection (25%)
        score += 0.25 * ciaAgent.checkDNSIntegrity()

        // TLS enforcement (20%)
        score += 0.20 * ciaAgent.checkTLSEnforcement()

        return score
    }

    override fun evaluateCarrier(): Double {
        var score = 0.0

        // Carrier encryption — cellular has inherent encryption (50%)
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val caps = cm.activeNetwork?.let { cm.getNetworkCapabilities(it) }
        score += 0.50 * when {
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> 0.9
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> 0.5
            else -> 0.0
        }

        // Bluetooth state — off or paired-only is secure (25%)
        score += 0.25 * checkBluetoothSafety()

        // Single interface operation (25%) — multiple active interfaces increase attack surface
        score += 0.25 * checkSingleInterface()

        return score
    }

    private fun checkBluetoothSafety(): Double = try {
        val bm = appContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val adapter = bm?.adapter
        when {
            adapter == null -> 1.0  // No Bluetooth = safe
            !adapter.isEnabled -> 1.0  // BT off = safe
            adapter.bondedDevices.isNotEmpty() -> 0.7  // Paired devices only
            else -> 0.3  // Discoverable with no bonds
        }
    } catch (_: SecurityException) {
        0.5
    } catch (_: Exception) {
        0.5
    }

    private fun checkSingleInterface(): Double = try {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = cm.allNetworks
        // Single active non-VPN network = 1.0
        val nonVpnNetworks = networks.count { net ->
            val c = cm.getNetworkCapabilities(net)
            c != null && !c.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        }
        when {
            nonVpnNetworks <= 1 -> 1.0
            nonVpnNetworks == 2 -> 0.5  // Dual-cellular (SAR-MOD-006)
            else -> 0.2
        }
    } catch (_: Exception) {
        0.5
    }
}
