package com.vtmis.fedramp.thadam.gate

import android.content.Context
import android.os.Build
import com.vtmis.fedramp.thadam.agent.CIAAgent
import com.vtmis.fedramp.thadam.model.GateIds
import com.vtmis.fedramp.thadam.model.GateNames

/**
 * Gate 4 — Execution Gate
 *
 * Position: Between storage (read) and RAM execution (code loaded into memory).
 * Addresses: SAR-CRIT-001, SAR-HIGH-005, SAR-LOW-004, SCR-001, SCR-002, SCTH-001
 *
 * Trust Score = 0.25×patch + 0.25×SELinux + 0.20×VerBoot + 0.15×APK_sig +
 *              0.10×supply_chain + 0.05×kernel_ver
 */
class ExecutionGate(
    ciaAgent: CIAAgent,
    private val appContext: Context
) : C3Gate(GateIds.EXECUTION, GateNames.EXECUTION, ciaAgent) {

    override fun evaluateContext(): Double {
        var score = 0.0

        // Patch freshness — SAR-CRIT-001 (40%)
        score += 0.40 * ciaAgent.checkPatchFreshness()

        // App from verified source (30%)
        score += 0.30 * ciaAgent.checkAPKSignatures()

        // Supply chain trust — SCR-001, SCR-002 (30%)
        score += 0.30 * evaluateSupplyChainTrust()

        return score
    }

    override fun evaluateControl(): Double {
        var score = 0.0

        // SELinux enforcing (35%)
        score += 0.35 * ciaAgent.checkSELinuxEnforcing()

        // Verified Boot green (30%)
        score += 0.30 * ciaAgent.checkVerifiedBoot()

        // Kernel integrity — SAR-HIGH-005 (20%)
        score += 0.20 * ciaAgent.checkKernelVersion()

        // OEM services within baseline (15%)
        score += 0.15 * ciaAgent.checkOEMServiceBaseline()

        return score
    }

    override fun evaluateCarrier(): Double {
        var score = 0.0

        // Process sandboxing — SELinux labels (45%)
        score += 0.45 * ciaAgent.checkSELinuxEnforcing()

        // Runtime memory protection — seccomp-bpf (30%)
        score += 0.30 * checkRuntimeProtection()

        // TEE availability for sensitive ops (25%)
        score += 0.25 * checkTEEAvailable()

        return score
    }

    /**
     * Supply chain trust evaluation
     * SCR-001: Vivo (BBK/China) — firmware-level access → HIGH risk
     * SCR-002: MediaTek — closed-source baseband → MODERATE risk
     */
    private fun evaluateSupplyChainTrust(): Double {
        val manufacturer = Build.MANUFACTURER.lowercase()
        val hardware = Build.HARDWARE.lowercase()

        var risk = 0.0
        // Vivo/BBK supply chain scoring
        risk += when {
            manufacturer.contains("vivo") || manufacturer.contains("bbk") -> 0.4
            manufacturer.contains("samsung") || manufacturer.contains("google") -> 0.9
            else -> 0.6
        }

        // SoC vendor trust
        risk += when {
            hardware.contains("mt") || hardware.contains("mediatek") -> 0.4  // MediaTek
            hardware.contains("qcom") || hardware.contains("qualcomm") -> 0.7
            hardware.contains("tensor") || hardware.contains("exynos") -> 0.8
            else -> 0.5
        }

        return (risk / 2.0).coerceIn(0.0, 1.0)
    }

    private fun checkRuntimeProtection(): Double =
        // Android 8+ has seccomp-bpf in the app sandbox
        if (Build.VERSION.SDK_INT >= 26) 0.8 else 0.4

    private fun checkTEEAvailable(): Double = try {
        // Check if AndroidKeyStore-backed keys can use TEE
        val ks = java.security.KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)
        0.8  // AndroidKeyStore available = TEE likely present
    } catch (_: Exception) {
        0.3
    }
}
