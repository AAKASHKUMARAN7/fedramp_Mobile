package com.vtmis.fedramp.thadam.export

import android.content.Context
import android.content.Intent
import android.os.Build
import com.vtmis.fedramp.thadam.agent.DeviceProfiler
import com.vtmis.fedramp.thadam.model.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Generates an HTML compliance report from live trust evaluation data
 * and shares it via Android's share intent.
 */
object ComplianceReportExporter {

    fun generateAndShare(
        context: Context,
        result: ChainResult,
        signals: Map<String, Double>,
        deviceInfo: DeviceProfiler.DeviceInfo?
    ) {
        val html = generateHTML(result, signals, deviceInfo)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            putExtra(Intent.EXTRA_SUBJECT, "Thadam C3 Compliance Report — ${formatDate(result.timestamp)}")
            putExtra(Intent.EXTRA_TEXT, html)
            putExtra(Intent.EXTRA_HTML_TEXT, html)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Export Compliance Report"))
    }

    private fun generateHTML(
        result: ChainResult,
        signals: Map<String, Double>,
        deviceInfo: DeviceProfiler.DeviceInfo?
    ): String {
        val date = formatDate(result.timestamp)
        val levelColor = result.globalTrustLevel.colorHex
        val device = deviceInfo ?: DeviceProfiler.DeviceInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            brand = Build.BRAND,
            device = Build.DEVICE,
            hardware = Build.HARDWARE,
            board = Build.BOARD,
            androidVersion = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            securityPatch = if (Build.VERSION.SDK_INT >= 23) Build.VERSION.SECURITY_PATCH else "Unknown",
            kernelVersion = System.getProperty("os.version") ?: "Unknown",
            buildFingerprint = Build.FINGERPRINT,
            totalStorageGB = 0.0,
            freeStorageGB = 0.0,
            totalRamMB = 0L,
            screenDensity = "Unknown",
            abiList = Build.SUPPORTED_ABIS.joinToString(","),
            radioVersion = Build.getRadioVersion() ?: "Unknown"
        )

        return """
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thadam C3 Compliance Report</title>
<style>
body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;
background:#0D1117;color:#E6EDF3;margin:0;padding:20px;max-width:800px;margin:0 auto}
h1{color:#58A6FF;border-bottom:1px solid #30363D;padding-bottom:10px}
h2{color:#8B949E;font-size:14px;letter-spacing:2px;margin-top:28px}
.card{background:#161B22;border-radius:12px;padding:16px;margin:10px 0;border:1px solid #21262D}
.score{font-size:36px;font-weight:bold}
.green{color:#4CAF50}.yellow{color:#FFC107}.orange{color:#FF9800}.red{color:#F44336}
table{width:100%;border-collapse:collapse;margin:8px 0}
td,th{text-align:left;padding:6px 8px;border-bottom:1px solid #21262D}
th{color:#8B949E;font-size:11px;letter-spacing:1px}
.bar{display:inline-block;height:8px;border-radius:4px;background:#4CAF50}
.mono{font-family:monospace;font-size:12px;color:#7D8590}
.tag{display:inline-block;padding:2px 8px;border-radius:4px;font-size:11px;font-weight:bold}
footer{text-align:center;margin-top:30px;color:#484F58;font-size:11px}
</style>
</head>
<body>
<h1>Thadam C3 Trust — Compliance Report</h1>
<p class="mono">Generated: $date | System: VTMIS FedRAMP Low Baseline</p>

<h2>DEVICE PROFILE</h2>
<div class="card">
<table>
<tr><td>Device</td><td><strong>${escapeHtml(device.displayName)}</strong></td></tr>
<tr><td>Manufacturer</td><td>${escapeHtml(device.manufacturer)}</td></tr>
<tr><td>Android</td><td>${escapeHtml(device.androidVersion)} (SDK ${device.sdkInt})</td></tr>
<tr><td>Security Patch</td><td>${escapeHtml(device.securityPatch)}</td></tr>
<tr><td>Hardware</td><td>${escapeHtml(device.hardware)}</td></tr>
<tr><td>Supply Chain Risk</td><td class="orange">${escapeHtml(device.supplyChainRisk)}</td></tr>
</table>
</div>

<h2>GLOBAL TRUST ASSESSMENT</h2>
<div class="card">
<div class="score ${result.globalTrustLevel.name.lowercase()}">${result.globalTrustLevel.label}</div>
<p>Score: ${"%.1f".format(result.globalTrustScore * 100)}% | Chain Floor: ${result.chainFloor.name}</p>
<table>
<tr><th>CIA Dimension</th><th>Score</th></tr>
<tr><td>Confidentiality</td><td>${"%.3f".format(result.ciaScore.confidentiality)}</td></tr>
<tr><td>Integrity</td><td>${"%.3f".format(result.ciaScore.integrity)}</td></tr>
<tr><td>Availability</td><td>${"%.3f".format(result.ciaScore.availability)}</td></tr>
</table>
</div>

<h2>GATE CHAIN RESULTS</h2>
${result.gateResults.joinToString("\n") { gate -> """
<div class="card">
<strong>${escapeHtml(gate.gateId)}: ${escapeHtml(gate.gateName)}</strong>
<span class="tag ${gate.decision.name.lowercase()}">${gate.decision.name}</span>
<p>Score: ${"%.3f".format(gate.trustScore)} | C3: [${"%.2f".format(gate.c3.context)}, ${"%.2f".format(gate.c3.control)}, ${"%.2f".format(gate.c3.carrier)}]</p>
${if (gate.reasons.isNotEmpty()) "<p class='mono'>${gate.reasons.joinToString("<br>") { escapeHtml(it) }}</p>" else ""}
</div>
"""}}

<h2>SIGNAL INVENTORY (${signals.size} SENSORS)</h2>
<div class="card">
<table>
<tr><th>Signal</th><th>Score</th><th>Status</th></tr>
${signals.entries.joinToString("\n") { (name, score) ->
    val status = if (score >= 0.7) "PASS" else "FINDING"
    val cls = if (score >= 0.7) "green" else "orange"
    "<tr><td>${escapeHtml(name)}</td><td>${"%.2f".format(score)}</td><td class='$cls'>$status</td></tr>"
}}
</table>
</div>

<h2>COMPLIANCE DETERMINATION</h2>
<div class="card">
<p>Standard: NIST SP 800-53 Rev. 5 — FedRAMP Low Baseline</p>
<p>Assessment Result: <strong class="${if (result.globalTrustScore >= 0.70) "yellow" else "red"}">${"%.1f".format(result.globalTrustScore * 100)}% Compliance</strong></p>
<p>Recommendation: ${when (result.globalTrustLevel) {
    TrustLevel.GREEN -> "Conditional ATO — Full Operations Authorized"
    TrustLevel.YELLOW -> "Conditional ATO — Remediation Required Within 30 Days"
    TrustLevel.ORANGE -> "Conditional ATO — Remediation Required Within 14 Days"
    TrustLevel.RED -> "ATO DENIED — Immediate Remediation Required"
}}</p>
</div>

<footer>
Thadam C3 Gate Adaptive Trust Architecture v3.0<br>
VTMIS FedRAMP Authorization Package — Educational/Portfolio Project
</footer>
</body>
</html>
""".trimIndent()
    }

    private fun formatDate(ts: Long): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US).format(Date(ts))

    private fun escapeHtml(text: String): String =
        text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("\"", "&quot;").replace("'", "&#39;")
}
