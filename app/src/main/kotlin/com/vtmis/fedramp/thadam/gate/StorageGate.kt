package com.vtmis.fedramp.thadam.gate

import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.vtmis.fedramp.thadam.agent.CIAAgent
import com.vtmis.fedramp.thadam.model.GateIds
import com.vtmis.fedramp.thadam.model.GateNames

/**
 * Gate 3 — Storage Gate
 *
 * Position: Between data ingestion and persistent storage write.
 * Addresses: SAR-HIGH-003, SAR-LOW-001, SAR-LOW-007, SCTH-005
 *
 * Trust Score = 0.30×FBE + 0.25×sufficient_space + 0.20×backup_enc +
 *              0.15×data_classified + 0.10×work_profile_sep
 */
class StorageGate(
    ciaAgent: CIAAgent,
    private val appContext: Context
) : C3Gate(GateIds.STORAGE, GateNames.STORAGE, ciaAgent) {

    override fun evaluateContext(): Double {
        var score = 0.0

        // Data classification awareness (40%) — is the app aware of data types?
        score += 0.40 * 0.7  // Thadam-enabled = partial classification

        // Sufficient free space — SAR-HIGH-003 (40%)
        score += 0.40 * checkSufficientSpace()

        // Destination encrypted partition (20%)
        score += 0.20 * ciaAgent.checkFileBasedEncryption()

        return score
    }

    override fun evaluateControl(): Double {
        var score = 0.0

        // File-Based Encryption active (35%)
        score += 0.35 * ciaAgent.checkFileBasedEncryption()

        // Backup encryption verified — SAR-LOW-001 (30%)
        score += 0.30 * checkBackupEncryption()

        // Storage not critically full (20%)
        score += 0.20 * checkStorageHealthy()

        // Data classification tagging (15%)
        score += 0.15 * 0.5  // Partially implemented via Thadam metadata

        return score
    }

    override fun evaluateCarrier(): Double {
        var score = 0.0

        // Write path through encrypted storage CE/DE (50%)
        score += 0.50 * ciaAgent.checkFileBasedEncryption()

        // Work Profile separation — SAR-LOW-007 (30%)
        score += 0.30 * ciaAgent.checkWorkProfileActive()

        // Backup transport encrypted (20%)
        score += 0.20 * checkBackupTransportEncrypted()

        return score
    }

    /** SAR-HIGH-003: Storage 97% full */
    private fun checkSufficientSpace(): Double = try {
        val stat = StatFs(Environment.getDataDirectory().path)
        val freeGB = stat.availableBytes / (1024.0 * 1024.0 * 1024.0)
        when {
            freeGB >= 15.0 -> 1.0  // Healthy
            freeGB >= 5.0 -> 0.5   // Degraded
            freeGB >= 1.0 -> 0.2   // Critical — SAR-HIGH-003 triggers here
            else -> 0.05           // Severe
        }
    } catch (_: Exception) {
        0.1
    }

    /** SAR-LOW-001: Backup encryption unverified */
    private fun checkBackupEncryption(): Double {
        // Without device admin or backup API access, we can't fully verify
        // Google backup is encrypted in transit by default
        return 0.5  // Partially trusted — SAR-LOW-001 acknowledged
    }

    private fun checkStorageHealthy(): Double = try {
        val stat = StatFs(Environment.getDataDirectory().path)
        val freeGB = stat.availableBytes / (1024.0 * 1024.0 * 1024.0)
        val totalGB = stat.totalBytes / (1024.0 * 1024.0 * 1024.0)
        val usedPercent = ((totalGB - freeGB) / totalGB) * 100
        when {
            usedPercent < 70 -> 1.0
            usedPercent < 85 -> 0.7
            usedPercent < 95 -> 0.3
            else -> 0.05  // >95% full — critical
        }
    } catch (_: Exception) {
        0.1
    }

    private fun checkBackupTransportEncrypted(): Double {
        // Google backup uses TLS by default
        return 0.6
    }
}
