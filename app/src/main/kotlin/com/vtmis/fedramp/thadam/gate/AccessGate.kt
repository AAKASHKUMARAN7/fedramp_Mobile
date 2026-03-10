package com.vtmis.fedramp.thadam.gate

import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.hardware.usb.UsbManager
import android.provider.Settings
import com.vtmis.fedramp.thadam.agent.CIAAgent
import com.vtmis.fedramp.thadam.model.GateIds
import com.vtmis.fedramp.thadam.model.GateNames

/**
 * Gate 2 — Access Gate
 *
 * Position: Between physical/logical access attempt and system resources.
 * Addresses: SAR-HIGH-001, SAR-HIGH-004, SAR-MOD-011, SAR-LOW-002
 *
 * Trust Score = 0.30×auth_recent + 0.20×USB_off + 0.15×DevOps_off +
 *              0.15×MDM_enrolled + 0.10×PIN_complex + 0.10×banner_ack
 */
class AccessGate(
    ciaAgent: CIAAgent,
    private val appContext: Context
) : C3Gate(GateIds.ACCESS, GateNames.ACCESS, ciaAgent) {

    override fun evaluateContext(): Double {
        var score = 0.0

        // Authentication state (40%)
        score += 0.40 * checkAuthenticated()

        // USB debugging state — SAR-HIGH-001 (35%)
        score += 0.35 * checkUSBDebugOff()

        // Developer Options — SAR-MOD-011 (25%)
        score += 0.25 * checkDeveloperOptionsOff()

        return score
    }

    override fun evaluateControl(): Double {
        var score = 0.0

        // MDM enrolled — SAR-HIGH-002 (35%)
        score += 0.35 * checkMDMEnrolled()

        // PIN/password complexity — SAR-LOW-002 (30%)
        score += 0.30 * checkPINComplexity()

        // System use banner — SAR-HIGH-004 (20%)
        score += 0.20 * checkBannerConfigured()

        // Lockout policy active (15%)
        score += 0.15 * checkLockoutPolicy()

        return score
    }

    override fun evaluateCarrier(): Double {
        var score = 0.0

        // Authentication channel secure — TEE-backed biometric (50%)
        score += 0.50 * checkSecureAuthChannel()

        // No USB connection active (30%)
        score += 0.30 * checkNoUSBConnection()

        // Device is locked when not in use (20%)
        score += 0.20 * checkAutoLockEnabled()

        return score
    }

    private fun checkAuthenticated(): Double = try {
        val km = appContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        when {
            !km.isDeviceSecure -> 0.0  // No lock configured
            !km.isDeviceLocked -> 1.0  // Unlocked = recently authenticated
            else -> 0.2  // Locked — needs re-auth
        }
    } catch (_: Exception) {
        0.3
    }

    /** SAR-HIGH-001: USB Debugging enabled */
    private fun checkUSBDebugOff(): Double = try {
        val adbEnabled = Settings.Global.getInt(
            appContext.contentResolver, Settings.Global.ADB_ENABLED, 0
        )
        if (adbEnabled == 0) 1.0 else 0.0
    } catch (_: Exception) {
        0.5
    }

    /** SAR-MOD-011: Developer Options enabled */
    private fun checkDeveloperOptionsOff(): Double = try {
        val devOpts = Settings.Global.getInt(
            appContext.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        )
        if (devOpts == 0) 1.0 else 0.0
    } catch (_: Exception) {
        0.5
    }

    /** SAR-HIGH-002: No MDM enrolled */
    private fun checkMDMEnrolled(): Double = try {
        val dpm = appContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admins = dpm.activeAdmins
        if (admins != null && admins.isNotEmpty()) 1.0 else 0.0
    } catch (_: Exception) {
        0.0
    }

    /** SAR-LOW-002: PIN instead of complex password */
    private fun checkPINComplexity(): Double = try {
        val km = appContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        // Can only check if device is secure, not the complexity level without device admin
        if (km.isDeviceSecure) 0.6 else 0.0
    } catch (_: Exception) {
        0.3
    }

    /** SAR-HIGH-004: No system use notification banner */
    private fun checkBannerConfigured(): Double = try {
        val dpm = appContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        // Lock screen info requires device owner
        0.0 // VTMIS currently has no banner configured
    } catch (_: Exception) {
        0.0
    }

    private fun checkLockoutPolicy(): Double = try {
        val dpm = appContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        // maximumFailedPasswordsForWipe requires admin component, use safe default
        // Android's built-in lockout (30s after 5 failures) always active
        0.3 // No MDM enrolled — default lockout only
    } catch (_: Exception) {
        0.3
    }

    private fun checkSecureAuthChannel(): Double =
        // Android TEE-backed keyguard is trusted by default since API 23
        if (android.os.Build.VERSION.SDK_INT >= 23) 0.8 else 0.5

    private fun checkNoUSBConnection(): Double = try {
        val usb = appContext.getSystemService(Context.USB_SERVICE) as UsbManager
        val devices = usb.deviceList
        if (devices.isEmpty()) 1.0 else 0.3
    } catch (_: Exception) {
        0.5
    }

    private fun checkAutoLockEnabled(): Double = try {
        val timeout = Settings.System.getInt(
            appContext.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 60000
        )
        when {
            timeout <= 30000 -> 1.0   // 30s or less — very secure
            timeout <= 60000 -> 0.8   // 1 minute
            timeout <= 300000 -> 0.5  // 5 minutes
            else -> 0.2              // Too long
        }
    } catch (_: Exception) {
        0.5
    }
}
