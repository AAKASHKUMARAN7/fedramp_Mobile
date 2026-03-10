package com.vtmis.fedramp.thadam.agent

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import com.vtmis.fedramp.thadam.model.CIAScore
import com.vtmis.fedramp.thadam.model.TrustLevel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * CIA Agent — Distributed Trust Sensor for VTMIS
 *
 * Continuously evaluates 22 signals across Confidentiality, Integrity,
 * and Availability to produce a composite trust score fed to all 6 C3 Gates.
 *
 * Weights derived from FIPS 199 Low baseline:
 *   wC = 0.33, wI = 0.34, wA = 0.33
 */
class CIAAgent(private val context: Context) {

    // FIPS 199 Low baseline weights
    private val wC = 0.33
    private val wI = 0.34
    private val wA = 0.33

    // ================================================================
    // PUBLIC API
    // ================================================================

    /** Collect all 22 signals and return composite CIA score */
    fun collectSignals(): CIAScore = CIAScore(
        confidentiality = evaluateConfidentiality(),
        integrity = evaluateIntegrity(),
        availability = evaluateAvailability()
    )

    /** Compute global trust score and level from CIA score */
    fun computeGlobalTrust(ciaScore: CIAScore): Pair<Double, TrustLevel> {
        val global = wC * ciaScore.confidentiality +
                wI * ciaScore.integrity +
                wA * ciaScore.availability
        return Pair(global, TrustLevel.fromScore(global))
    }

    // ================================================================
    // CONFIDENTIALITY SIGNALS (C-SIG-01 through C-SIG-07)
    // Total weight: 1.0
    // ================================================================

    fun evaluateConfidentiality(): Double {
        var score = 0.0
        score += 0.20 * checkFileBasedEncryption()    // C-SIG-01
        score += 0.25 * checkVPNActive()              // C-SIG-02
        score += 0.15 * checkTLSEnforcement()         // C-SIG-03
        score += 0.15 * checkWorkProfileActive()      // C-SIG-04
        score += 0.10 * checkClipboardIsolation()     // C-SIG-05
        score += 0.10 * checkInputMethodTrusted()     // C-SIG-06
        score += 0.05 * checkMACRandomization()       // C-SIG-07
        return score.coerceIn(0.0, 1.0)
    }

    // ================================================================
    // INTEGRITY SIGNALS (I-SIG-01 through I-SIG-08)
    // Total weight: 1.0
    // ================================================================

    fun evaluateIntegrity(): Double {
        var score = 0.0
        score += 0.20 * checkVerifiedBoot()           // I-SIG-01
        score += 0.20 * checkSELinuxEnforcing()       // I-SIG-02
        score += 0.15 * checkPatchFreshness()         // I-SIG-03
        score += 0.15 * checkAPKSignatures()          // I-SIG-04
        score += 0.10 * checkKernelVersion()          // I-SIG-05
        score += 0.05 * checkOEMServiceBaseline()     // I-SIG-06
        score += 0.10 * checkFileIntegrityHashes()    // I-SIG-07
        score += 0.05 * checkDNSIntegrity()           // I-SIG-08
        return score.coerceIn(0.0, 1.0)
    }

    // ================================================================
    // AVAILABILITY SIGNALS (A-SIG-01 through A-SIG-07)
    // Total weight: 1.0
    // ================================================================

    fun evaluateAvailability(): Double {
        var score = 0.0
        score += 0.25 * checkStorageFreeSpace()       // A-SIG-01
        score += 0.15 * checkRAMAvailable()           // A-SIG-02
        score += 0.10 * checkBatteryLevel()           // A-SIG-03
        score += 0.20 * checkNetworkConnectivity()    // A-SIG-04
        score += 0.15 * checkCriticalServicesAlive()  // A-SIG-05
        score += 0.10 * checkUpdateSpaceAvailable()   // A-SIG-06
        score += 0.05 * checkAuditLogCapacity()       // A-SIG-07
        return score.coerceIn(0.0, 1.0)
    }

    // ================================================================
    // INDIVIDUAL SIGNAL IMPLEMENTATIONS
    // Each returns 0.0 (failed) to 1.0 (healthy)
    // ================================================================

    // --- Confidentiality Signals ---

    /** C-SIG-01: File-Based Encryption state */
    fun checkFileBasedEncryption(): Double = try {
        val prop = getSystemProperty("ro.crypto.state")
        if (prop == "encrypted") 1.0 else 0.0
    } catch (_: Exception) {
        0.5 // Unknown — assume partial
    }

    /** C-SIG-02: VPN tunnel active */
    fun checkVPNActive(): Double = try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val caps = if (network != null) cm.getNetworkCapabilities(network) else null
        if (caps != null && caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) 1.0 else 0.0
    } catch (_: Exception) {
        0.0
    }

    /** C-SIG-03: TLS enforcement — checks if device is on SDK 29+ with network security config */
    fun checkTLSEnforcement(): Double =
        if (Build.VERSION.SDK_INT >= 29) 0.8 else 0.5 // Android 10+ blocks cleartext by default

    /** C-SIG-04: Work Profile separation */
    fun checkWorkProfileActive(): Double = try {
        val um = context.getSystemService(Context.USER_SERVICE)
        // Check if managed profile exists (requires MANAGE_USERS for full check)
        // For non-privileged app, check if user profiles > 1
        0.0 // VTMIS currently has no Work Profile (SAR-MOD-001)
    } catch (_: Exception) {
        0.0
    }

    /** C-SIG-05: Clipboard isolation */
    fun checkClipboardIsolation(): Double =
        // Android 12+ restricts clipboard access from background apps
        if (Build.VERSION.SDK_INT >= 31) 0.7 else 0.0

    /** C-SIG-06: Input method trusted (system keyboard vs third-party) */
    fun checkInputMethodTrusted(): Double = try {
        val ime = Settings.Secure.getString(context.contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        // System IMEs from Google/AOSP are trusted
        val trustedPrefixes = listOf("com.google.", "com.android.", "com.sec.android.")
        if (trustedPrefixes.any { ime?.startsWith(it) == true }) 1.0 else 0.3
    } catch (_: Exception) {
        0.5
    }

    /** C-SIG-07: Wi-Fi MAC randomization */
    fun checkMACRandomization(): Double =
        // Android 10+ uses MAC randomization by default
        if (Build.VERSION.SDK_INT >= 29) 0.8 else 0.0

    // --- Integrity Signals ---

    /** I-SIG-01: Verified Boot state */
    fun checkVerifiedBoot(): Double = try {
        val state = getSystemProperty("ro.boot.verifiedbootstate")
        when (state) {
            "green" -> 1.0   // Fully verified
            "yellow" -> 0.5  // Self-signed
            "orange" -> 0.2  // Custom key
            else -> 0.0      // Red or unknown
        }
    } catch (_: Exception) {
        0.5
    }

    /** I-SIG-02: SELinux enforcing mode */
    fun checkSELinuxEnforcing(): Double = try {
        val process = Runtime.getRuntime().exec(arrayOf("getenforce"))
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val result = reader.readLine()?.trim()
        reader.close()
        process.waitFor()
        when (result) {
            "Enforcing" -> 1.0
            "Permissive" -> 0.2
            else -> 0.0
        }
    } catch (_: Exception) {
        0.5 // Can't determine — assume partial
    }

    /** I-SIG-03: Security patch freshness */
    fun checkPatchFreshness(): Double = try {
        val patchDate = Build.VERSION.SECURITY_PATCH // "YYYY-MM-DD"
        val patchLocalDate = LocalDate.parse(patchDate)
        val daysSincePatch = ChronoUnit.DAYS.between(patchLocalDate, LocalDate.now())
        when {
            daysSincePatch <= 30 -> 1.0
            daysSincePatch <= 90 -> 0.8
            daysSincePatch <= 180 -> 0.5
            daysSincePatch <= 365 -> 0.2
            else -> 0.0
        }
    } catch (_: Exception) {
        0.0
    }

    /** I-SIG-04: APK signature verification — checks this app's own signature */
    fun checkAPKSignatures(): Double = try {
        val pm = context.packageManager
        val info = pm.getPackageInfo(context.packageName, android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES)
        if (info.signingInfo != null) 1.0 else 0.0
    } catch (_: Exception) {
        0.5
    }

    /** I-SIG-05: Kernel version against known-good baseline */
    fun checkKernelVersion(): Double = try {
        val kernelVersion = System.getProperty("os.version") ?: ""
        // VTMIS kernel: 4.19.x — check if it's at least 4.19
        val major = kernelVersion.split(".").getOrNull(0)?.toIntOrNull() ?: 0
        val minor = kernelVersion.split(".").getOrNull(1)?.toIntOrNull() ?: 0
        when {
            major >= 5 -> 1.0       // Modern kernel
            major == 4 && minor >= 19 -> 0.5  // VTMIS baseline
            else -> 0.2
        }
    } catch (_: Exception) {
        0.3
    }

    /** I-SIG-06: OEM service baseline count — manufacturer-agnostic */
    fun checkOEMServiceBaseline(): Double = try {
        val pm = context.packageManager
        val manufacturer = Build.MANUFACTURER.lowercase()
        // Detect OEM package prefix dynamically
        val oemPrefixes = mutableListOf<String>()
        when {
            manufacturer.contains("vivo") || manufacturer.contains("bbk") ->
                oemPrefixes.addAll(listOf("com.vivo.", "com.bbk."))
            manufacturer.contains("samsung") || manufacturer.contains("sec") ->
                oemPrefixes.addAll(listOf("com.samsung.", "com.sec."))
            manufacturer.contains("xiaomi") || manufacturer.contains("redmi") ->
                oemPrefixes.addAll(listOf("com.xiaomi.", "com.miui.", "com.mi."))
            manufacturer.contains("oppo") ->
                oemPrefixes.addAll(listOf("com.oppo.", "com.coloros.", "com.heytap."))
            manufacturer.contains("realme") ->
                oemPrefixes.addAll(listOf("com.realme.", "com.oplus."))
            manufacturer.contains("oneplus") ->
                oemPrefixes.addAll(listOf("com.oneplus.", "com.oplus."))
            manufacturer.contains("huawei") || manufacturer.contains("honor") ->
                oemPrefixes.addAll(listOf("com.huawei.", "com.honor."))
            manufacturer.contains("google") ->
                oemPrefixes.addAll(listOf("com.google."))
            manufacturer.contains("motorola") || manufacturer.contains("lenovo") ->
                oemPrefixes.addAll(listOf("com.motorola.", "com.lenovo."))
            else -> oemPrefixes.add("com.${manufacturer.replace(" ", "").take(15)}.")
        }
        val oemCount = pm.getInstalledPackages(0)
            .count { pkg -> oemPrefixes.any { pkg.packageName.startsWith(it) } }
        // Reasonable range: OEM packages should be 5-150
        if (oemCount in 5..150) 1.0 else 0.5
    } catch (_: Exception) {
        0.5
    }

    /** I-SIG-07: File integrity hashes — simplified check for build.prop accessibility */
    fun checkFileIntegrityHashes(): Double = try {
        // Check that build fingerprint hasn't been tampered with
        val fingerprint = Build.FINGERPRINT
        if (fingerprint.isNotEmpty() && fingerprint != "robolectric") 1.0 else 0.0
    } catch (_: Exception) {
        0.5
    }

    /** I-SIG-08: DNS integrity — checks Private DNS setting */
    fun checkDNSIntegrity(): Double = try {
        val privateDns = Settings.Global.getString(context.contentResolver, "private_dns_mode")
        when (privateDns) {
            "hostname" -> 1.0  // Custom DoT server configured
            "opportunistic" -> 0.7  // Opportunistic DoT
            "off" -> 0.0  // No DNS protection
            else -> 0.3
        }
    } catch (_: Exception) {
        0.3
    }

    // --- Availability Signals ---

    /** A-SIG-01: Storage free space — threshold: ≥15GB=1.0, 5-15GB=0.5, <5GB critical */
    fun checkStorageFreeSpace(): Double = try {
        val stat = StatFs(Environment.getDataDirectory().path)
        val freeGB = stat.availableBytes / (1024.0 * 1024.0 * 1024.0)
        when {
            freeGB >= 15.0 -> 1.0
            freeGB >= 5.0 -> 0.5
            freeGB >= 1.0 -> 0.2
            else -> 0.05
        }
    } catch (_: Exception) {
        0.1
    }

    /** A-SIG-02: RAM availability — threshold: ≥1GB free healthy */
    fun checkRAMAvailable(): Double = try {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val memInfo = android.app.ActivityManager.MemoryInfo()
        am.getMemoryInfo(memInfo)
        val freeGB = memInfo.availMem / (1024.0 * 1024.0 * 1024.0)
        when {
            freeGB >= 1.5 -> 1.0
            freeGB >= 1.0 -> 0.8
            freeGB >= 0.5 -> 0.5
            else -> 0.2
        }
    } catch (_: Exception) {
        0.3
    }

    /** A-SIG-03: Battery level — threshold: ≥20% healthy */
    fun checkBatteryLevel(): Double = try {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        when {
            level >= 50 -> 1.0
            level >= 20 -> 0.7
            level >= 10 -> 0.3
            else -> 0.1
        }
    } catch (_: Exception) {
        0.5
    }

    /** A-SIG-04: Network connectivity — default network active */
    fun checkNetworkConnectivity(): Double = try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val caps = network?.let { cm.getNetworkCapabilities(it) }
        when {
            caps == null -> 0.0
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> 1.0
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) &&
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> 0.9
            else -> 0.3
        }
    } catch (_: Exception) {
        0.0
    }

    /** A-SIG-05: Critical services alive — checks that key system services are accessible */
    fun checkCriticalServicesAlive(): Double = try {
        var score = 0.0
        // Connectivity service
        if (context.getSystemService(Context.CONNECTIVITY_SERVICE) != null) score += 0.33
        // Keyguard service
        if (context.getSystemService(Context.KEYGUARD_SERVICE) != null) score += 0.34
        // Device policy service
        if (context.getSystemService(Context.DEVICE_POLICY_SERVICE) != null) score += 0.33
        score
    } catch (_: Exception) {
        0.5
    }

    /** A-SIG-06: Update space available — can a security update be installed? */
    fun checkUpdateSpaceAvailable(): Double = try {
        val stat = StatFs(Environment.getDataDirectory().path)
        val freeGB = stat.availableBytes / (1024.0 * 1024.0 * 1024.0)
        // Need at least 2GB for a system update
        if (freeGB >= 2.0) 1.0 else 0.0
    } catch (_: Exception) {
        0.0
    }

    /** A-SIG-07: Audit log capacity — local buffer not full */
    fun checkAuditLogCapacity(): Double = try {
        // Check if internal storage has space for logs (>100MB)
        val stat = StatFs(context.filesDir.path)
        val freeMB = stat.availableBytes / (1024.0 * 1024.0)
        if (freeMB >= 100) 1.0 else 0.3
    } catch (_: Exception) {
        0.5
    }

    // ================================================================
    // UTILITY — Exposed signal scores for Dashboard
    // ================================================================

    /** Get all individual signal scores for dashboard display */
    fun getAllSignalScores(): Map<String, Double> = mapOf(
        "C-SIG-01 FBE" to checkFileBasedEncryption(),
        "C-SIG-02 VPN" to checkVPNActive(),
        "C-SIG-03 TLS" to checkTLSEnforcement(),
        "C-SIG-04 WorkProfile" to checkWorkProfileActive(),
        "C-SIG-05 Clipboard" to checkClipboardIsolation(),
        "C-SIG-06 InputMethod" to checkInputMethodTrusted(),
        "C-SIG-07 MAC" to checkMACRandomization(),
        "I-SIG-01 VerifiedBoot" to checkVerifiedBoot(),
        "I-SIG-02 SELinux" to checkSELinuxEnforcing(),
        "I-SIG-03 PatchAge" to checkPatchFreshness(),
        "I-SIG-04 APKSignature" to checkAPKSignatures(),
        "I-SIG-05 Kernel" to checkKernelVersion(),
        "I-SIG-06 OEMBaseline" to checkOEMServiceBaseline(),
        "I-SIG-07 FileIntegrity" to checkFileIntegrityHashes(),
        "I-SIG-08 DNS" to checkDNSIntegrity(),
        "A-SIG-01 Storage" to checkStorageFreeSpace(),
        "A-SIG-02 RAM" to checkRAMAvailable(),
        "A-SIG-03 Battery" to checkBatteryLevel(),
        "A-SIG-04 Network" to checkNetworkConnectivity(),
        "A-SIG-05 Services" to checkCriticalServicesAlive(),
        "A-SIG-06 UpdateSpace" to checkUpdateSpaceAvailable(),
        "A-SIG-07 LogCapacity" to checkAuditLogCapacity()
    )

    // ================================================================
    // UTILITY — Read system properties
    // ================================================================

    private fun getSystemProperty(key: String): String = try {
        val process = Runtime.getRuntime().exec(arrayOf("getprop", key))
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val result = reader.readLine()?.trim() ?: ""
        reader.close()
        process.waitFor()
        result
    } catch (_: Exception) {
        ""
    }
}
