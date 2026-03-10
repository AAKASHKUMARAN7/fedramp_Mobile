package com.vtmis.fedramp.thadam.gate

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.vtmis.fedramp.thadam.agent.CIAAgent
import com.vtmis.fedramp.thadam.model.GateIds
import com.vtmis.fedramp.thadam.model.GateNames

/**
 * Gate 5 — Process Gate
 *
 * Position: Between code execution in RAM and application-level data operations.
 * Addresses: SAR-HIGH-002, SAR-MOD-001, SAR-MOD-002, SAR-MOD-004,
 *            SAR-MOD-008, SAR-LOW-003, SAR-LOW-006, SAR-LOW-009
 *
 * Trust Score = 0.25×work_profile + 0.20×allowlisted + 0.20×perms_minimal +
 *              0.15×input_trusted + 0.10×clipboard_iso + 0.10×IPC_secured
 */
class ProcessGate(
    ciaAgent: CIAAgent,
    private val appContext: Context
) : C3Gate(GateIds.PROCESS, GateNames.PROCESS, ciaAgent) {

    // Known high-risk app packages (SAR-MOD-002, SAR-MOD-004, SAR-MOD-008)
    private val highRiskPackages = setOf(
        // AI/LLM apps — SAR-MOD-004
        "com.openai.chatgpt", "com.google.android.apps.bard",
        "com.microsoft.copilot", "com.anthropic.claude",
        // Social media — SAR-MOD-002
        "com.instagram.android", "com.facebook.katana", "com.twitter.android",
        "com.snapchat.android", "com.zhiliaoapp.musically",
        // File transfer apps — SAR-MOD-003
        "com.lenovo.anyshare.gps", "shareit.lite",
        // Payment apps — SAR-MOD-008
        "com.phonepe.app", "net.one97.paytm", "com.google.android.apps.nbu.paisa.user"
    )

    override fun evaluateContext(): Double {
        var score = 0.0

        // Work Profile active — SAR-MOD-001 (40%)
        score += 0.40 * ciaAgent.checkWorkProfileActive()

        // High-risk app inventory (35%)
        score += 0.35 * checkHighRiskAppExposure()

        // Install source verification (25%)
        score += 0.25 * checkInstallSourceTrusted()

        return score
    }

    override fun evaluateControl(): Double {
        var score = 0.0

        // App allowlist enforcement — SAR-LOW-003 (30%)
        score += 0.30 * 0.3  // No allowlisting currently (SAR-LOW-003)

        // Input method trusted — SAR-LOW-009 (25%)
        score += 0.25 * ciaAgent.checkInputMethodTrusted()

        // Permissions minimal (25%)
        score += 0.25 * checkPermissionsMinimal()

        // MDM policy enforcement — SAR-HIGH-002 (20%)
        score += 0.20 * 0.0  // No MDM (SAR-HIGH-002)

        return score
    }

    override fun evaluateCarrier(): Double {
        var score = 0.0

        // IPC secured via SELinux labels (40%)
        score += 0.40 * ciaAgent.checkSELinuxEnforcing()

        // Clipboard isolation (30%)
        score += 0.30 * ciaAgent.checkClipboardIsolation()

        // App sandbox integrity (30%)
        score += 0.30 * checkAppSandbox()

        return score
    }

    /** Check how many high-risk apps are installed */
    private fun checkHighRiskAppExposure(): Double = try {
        val pm = appContext.packageManager
        var riskyCount = 0
        for (pkg in highRiskPackages) {
            try {
                pm.getPackageInfo(pkg, 0)
                riskyCount++
            } catch (_: Exception) {
                // Package not installed — good
            }
        }
        when {
            riskyCount == 0 -> 1.0   // No high-risk apps — ideal
            riskyCount <= 2 -> 0.6   // A few — moderate risk
            riskyCount <= 5 -> 0.3   // Many — elevated risk
            else -> 0.1              // Too many high-risk apps
        }
    } catch (_: Exception) {
        0.5
    }

    /** Check that apps come from Play Store */
    private fun checkInstallSourceTrusted(): Double = try {
        val pm = appContext.packageManager
        val installer = if (Build.VERSION.SDK_INT >= 30) {
            pm.getInstallSourceInfo(appContext.packageName).installingPackageName
        } else {
            @Suppress("DEPRECATION")
            pm.getInstallerPackageName(appContext.packageName)
        }
        val trustedInstallers = setOf(
            "com.android.vending",  // Google Play Store
            "com.google.android.packageinstaller"
        )
        if (installer in trustedInstallers) 1.0 else 0.3
    } catch (_: Exception) {
        0.5
    }

    /** Check this app's own permission count */
    private fun checkPermissionsMinimal(): Double = try {
        val pm = appContext.packageManager
        val info = pm.getPackageInfo(appContext.packageName,
            android.content.pm.PackageManager.GET_PERMISSIONS)
        val permCount = info.requestedPermissions?.size ?: 0
        when {
            permCount <= 5 -> 1.0
            permCount <= 10 -> 0.7
            permCount <= 20 -> 0.5
            else -> 0.3
        }
    } catch (_: Exception) {
        0.5
    }

    /** App sandbox — SELinux + seccomp on modern Android */
    private fun checkAppSandbox(): Double =
        if (Build.VERSION.SDK_INT >= 28) 0.9 else 0.6
}
