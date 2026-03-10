package com.vtmis.fedramp.thadam.gate

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.vtmis.fedramp.thadam.agent.CIAAgent
import com.vtmis.fedramp.thadam.model.GateIds
import com.vtmis.fedramp.thadam.model.GateNames

/**
 * Gate 6 — Transmission Gate
 *
 * Position: Between application process and outbound data transmission (network egress).
 * Addresses: SAR-CRIT-002, SAR-MOD-003, SAR-MOD-010, SAR-LOW-008
 *
 * Trust Score = 0.30×VPN + 0.25×TLS + 0.20×dest_auth + 0.15×SIEM + 0.10×DLP
 */
class TransmissionGate(
    ciaAgent: CIAAgent,
    private val appContext: Context
) : C3Gate(GateIds.TRANSMISSION, GateNames.TRANSMISSION, ciaAgent) {

    override fun evaluateContext(): Double {
        var score = 0.0

        // What type of data is being transmitted? (35%)
        // TLS enforcement on outbound
        score += 0.35 * ciaAgent.checkTLSEnforcement()

        // Is logging active for audit? — SAR-MOD-010 (35%)
        score += 0.35 * checkSIEMStatus()

        // Destination validation capability (30%)
        score += 0.30 * 0.4  // No DLP/destination validation currently

        return score
    }

    override fun evaluateControl(): Double {
        var score = 0.0

        // VPN active — SAR-CRIT-002 (40%)
        score += 0.40 * ciaAgent.checkVPNActive()

        // TLS 1.2+ enforced (30%)
        score += 0.30 * ciaAgent.checkTLSEnforcement()

        // Cloud sync controls — SAR-LOW-008 (15%)
        score += 0.15 * checkCloudSyncControlled()

        // DLP capability (15%)
        score += 0.15 * 0.0  // No DLP currently

        return score
    }

    override fun evaluateCarrier(): Double {
        var score = 0.0

        // End-to-end encryption on channel (50%)
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val caps = cm.activeNetwork?.let { cm.getNetworkCapabilities(it) }
        val vpnActive = caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
        score += 0.50 * if (vpnActive) 1.0 else 0.3

        // Certificate verification available (30%)
        score += 0.30 * 0.7  // Android has system certificate store

        // File transfer app blocked — SAR-MOD-003 (20%)
        score += 0.20 * checkFileTransferAppsBlocked()

        return score
    }

    /** SAR-MOD-010: No centralized logging/SIEM */
    private fun checkSIEMStatus(): Double {
        // VTMIS has no SIEM configured — local-only logging
        return 0.2  // Local audit only (SAR-MOD-010)
    }

    /** SAR-LOW-008: Zoho suite cloud sync uncontrolled */
    private fun checkCloudSyncControlled(): Double {
        // Without MDM, can't control cloud sync
        return 0.2
    }

    /** SAR-MOD-003: File transfer app installed */
    private fun checkFileTransferAppsBlocked(): Double = try {
        val pm = appContext.packageManager
        val fileTransferApps = listOf(
            "com.lenovo.anyshare.gps",      // SHAREit
            "shareit.lite",                   // SHAREit Lite
            "com.estmob.android.sendanywhere", // Send Anywhere
            "com.xiaomi.midrop"               // Mi Share
        )
        val installed = fileTransferApps.count { pkg ->
            try { pm.getPackageInfo(pkg, 0); true } catch (_: Exception) { false }
        }
        if (installed == 0) 1.0 else 0.0
    } catch (_: Exception) {
        0.5
    }
}
