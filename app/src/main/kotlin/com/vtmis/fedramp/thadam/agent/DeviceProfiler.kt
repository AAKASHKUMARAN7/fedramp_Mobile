package com.vtmis.fedramp.thadam.agent

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.DisplayMetrics

/**
 * DeviceProfiler — Collects device-specific hardware and software info
 * so Thadam works on ANY Android phone, not just Vivo T2X 5G.
 */
object DeviceProfiler {

    data class DeviceInfo(
        val manufacturer: String,
        val model: String,
        val brand: String,
        val device: String,
        val hardware: String,
        val board: String,
        val androidVersion: String,
        val sdkInt: Int,
        val securityPatch: String,
        val kernelVersion: String,
        val buildFingerprint: String,
        val totalStorageGB: Double,
        val freeStorageGB: Double,
        val totalRamMB: Long,
        val screenDensity: String,
        val abiList: String,
        val radioVersion: String
    ) {
        val displayName: String
            get() = "$brand $model".trim()

        val supplyChainRisk: String
            get() {
                val mfr = manufacturer.lowercase()
                return when {
                    mfr.contains("google") -> "LOW — Google (US, transparent supply chain)"
                    mfr.contains("samsung") -> "LOW-MODERATE — Samsung (South Korea)"
                    mfr.contains("oneplus") || mfr.contains("oppo") ||
                    mfr.contains("realme") || mfr.contains("vivo") ||
                    mfr.contains("bbk") -> "HIGH — BBK Electronics (China, firmware access)"
                    mfr.contains("xiaomi") || mfr.contains("redmi") ||
                    mfr.contains("poco") -> "HIGH — Xiaomi (China, data concerns)"
                    mfr.contains("huawei") || mfr.contains("honor") ->
                        "CRITICAL — Huawei (China, entity listed)"
                    mfr.contains("motorola") || mfr.contains("lenovo") ->
                        "MODERATE — Lenovo/Motorola (China-owned)"
                    mfr.contains("sony") -> "LOW — Sony (Japan)"
                    mfr.contains("nokia") || mfr.contains("hmd") ->
                        "LOW-MODERATE — HMD/Nokia (Finland)"
                    mfr.contains("asus") -> "LOW-MODERATE — ASUS (Taiwan)"
                    else -> "UNKNOWN — $manufacturer"
                }
            }

        val socVendor: String
            get() {
                val hw = hardware.lowercase()
                return when {
                    hw.contains("mt") || hw.contains("mediatek") -> "MediaTek"
                    hw.contains("qcom") || hw.contains("qualcomm") -> "Qualcomm"
                    hw.contains("tensor") -> "Google Tensor"
                    hw.contains("exynos") || hw.contains("samsungexynos") -> "Samsung Exynos"
                    hw.contains("kirin") -> "HiSilicon Kirin"
                    hw.contains("unisoc") || hw.contains("spreadtrum") -> "Unisoc"
                    else -> hardware
                }
            }
    }

    fun profile(context: Context): DeviceInfo {
        val stat = StatFs(Environment.getDataDirectory().path)
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memInfo)

        return DeviceInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            brand = Build.BRAND,
            device = Build.DEVICE,
            hardware = Build.HARDWARE,
            board = Build.BOARD,
            androidVersion = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            securityPatch = Build.VERSION.SECURITY_PATCH,
            kernelVersion = System.getProperty("os.version") ?: "unknown",
            buildFingerprint = Build.FINGERPRINT,
            totalStorageGB = stat.totalBytes / (1024.0 * 1024.0 * 1024.0),
            freeStorageGB = stat.availableBytes / (1024.0 * 1024.0 * 1024.0),
            totalRamMB = memInfo.totalMem / (1024 * 1024),
            screenDensity = context.resources.displayMetrics.densityDpi.let { dpi ->
                when {
                    dpi <= DisplayMetrics.DENSITY_LOW -> "ldpi"
                    dpi <= DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
                    dpi <= DisplayMetrics.DENSITY_HIGH -> "hdpi"
                    dpi <= DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
                    dpi <= DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
                    else -> "xxxhdpi"
                }
            },
            abiList = Build.SUPPORTED_ABIS.joinToString(", "),
            radioVersion = Build.getRadioVersion() ?: "N/A"
        )
    }
}
