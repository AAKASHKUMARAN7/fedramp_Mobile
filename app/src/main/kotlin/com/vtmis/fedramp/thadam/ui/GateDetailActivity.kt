package com.vtmis.fedramp.thadam.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.vtmis.fedramp.R
import com.vtmis.fedramp.thadam.model.*
import com.vtmis.fedramp.thadam.service.ThadamService

class GateDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_GATE_INDEX = "gate_index"

        fun launch(context: Context, gateIndex: Int) {
            context.startActivity(Intent(context, GateDetailActivity::class.java).apply {
                putExtra(EXTRA_GATE_INDEX, gateIndex)
            })
            if (context is android.app.Activity) {
                context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        // SAR findings mapped to each gate
        val sarFindings = mapOf(
            "GATE-1" to listOf(
                "SAR-01: No VPN/IPsec configured",
                "SAR-03: Wi-Fi auto-connect enabled",
                "SAR-10: No network-level IDS/IPS"
            ),
            "GATE-2" to listOf(
                "SAR-04: Biometric auth not mandatory",
                "SAR-05: No MDM enrollment",
                "SAR-07: USB debugging enabled"
            ),
            "GATE-3" to listOf(
                "SAR-02: Storage encryption AES-256-XTS",
                "SAR-06: No DLP solution deployed",
                "SAR-12: Clipboard data exposure risk"
            ),
            "GATE-4" to listOf(
                "SAR-08: Unknown sources sideloading risk",
                "SAR-11: No app allowlisting policy",
                "SAR-14: Supply chain risk — BBK Electronics"
            ),
            "GATE-5" to listOf(
                "SAR-09: Excessive app permissions",
                "SAR-13: Background app data leakage",
                "SAR-16: No runtime integrity verification"
            ),
            "GATE-6" to listOf(
                "SAR-15: DNS queries unencrypted",
                "SAR-17: Certificate pinning not enforced",
                "SAR-18: Bluetooth pairing exposure"
            )
        )

        // Remediation actions per gate with settings intents
        val remediations = mapOf(
            "GATE-1" to listOf(
                RemediationAction("Enable VPN", "Configure always-on VPN for encrypted traffic", Settings.ACTION_VPN_SETTINGS),
                RemediationAction("Review Wi-Fi Networks", "Remove untrusted saved networks", Settings.ACTION_WIFI_SETTINGS),
                RemediationAction("Enable Private DNS", "Set DNS over TLS provider", "android.settings.PRIVATE_DNS_SETTINGS")
            ),
            "GATE-2" to listOf(
                RemediationAction("Configure Screen Lock", "Set PIN/password/biometric lock", Settings.ACTION_SECURITY_SETTINGS),
                RemediationAction("Disable USB Debugging", "Turn off developer options", Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                RemediationAction("Review Device Admin", "Check enrolled device administrators", Settings.ACTION_SECURITY_SETTINGS)
            ),
            "GATE-3" to listOf(
                RemediationAction("Verify Encryption", "Check device encryption status", Settings.ACTION_SECURITY_SETTINGS),
                RemediationAction("Review App Permissions", "Audit storage access permissions", Settings.ACTION_APPLICATION_SETTINGS),
                RemediationAction("Check Backup Settings", "Ensure backups are encrypted", Settings.ACTION_PRIVACY_SETTINGS)
            ),
            "GATE-4" to listOf(
                RemediationAction("Disable Unknown Sources", "Block sideloading from untrusted sources", Settings.ACTION_APPLICATION_SETTINGS),
                RemediationAction("Review Installed Apps", "Check for suspicious applications", Settings.ACTION_APPLICATION_SETTINGS),
                RemediationAction("Update System", "Install latest security patches", Settings.ACTION_DEVICE_INFO_SETTINGS)
            ),
            "GATE-5" to listOf(
                RemediationAction("Review Permissions", "Audit app runtime permissions", "android.settings.APPLICATION_DETAILS_SETTINGS"),
                RemediationAction("Check Battery Optimization", "Review background activity", Settings.ACTION_BATTERY_SAVER_SETTINGS),
                RemediationAction("Manage Notifications", "Control app notification access", Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            ),
            "GATE-6" to listOf(
                RemediationAction("Review Bluetooth", "Disable Bluetooth when not in use", Settings.ACTION_BLUETOOTH_SETTINGS),
                RemediationAction("Check Network Settings", "Review active connections", Settings.ACTION_WIRELESS_SETTINGS),
                RemediationAction("Manage NFC", "Control NFC when not needed", Settings.ACTION_NFC_SETTINGS)
            )
        )
    }

    data class RemediationAction(val title: String, val description: String, val settingsAction: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gate_detail)

        val gateIndex = intent.getIntExtra(EXTRA_GATE_INDEX, 0)
        val result = ThadamService.latestChainResult
        val gateResult = result?.gateResults?.getOrNull(gateIndex) ?: run {
            finish()
            return
        }

        findViewById<View>(R.id.btnBackGate).setOnClickListener { finish() }
        populateGateDetails(gateResult)
    }

    private fun populateGateDetails(gate: GateResult) {
        // Title
        findViewById<TextView>(R.id.tvGateTitle).text = "${gate.gateId}: ${gate.gateName}"

        // Decision & Score
        val tvDecision = findViewById<TextView>(R.id.tvGateDecisionLabel)
        tvDecision.text = gate.decision.name
        tvDecision.setTextColor(Color.parseColor(decisionColor(gate.decision)))

        findViewById<TextView>(R.id.tvGateScoreDetail).text =
            "Trust Score: ${"%.3f".format(gate.trustScore)}"
        findViewById<ProgressBar>(R.id.progressGateScore).progress =
            (gate.trustScore * 100).toInt()

        // C3 breakdown
        findViewById<TextView>(R.id.tvC3Context).text =
            "Context:  ${"%.3f".format(gate.c3.context)}"
        findViewById<TextView>(R.id.tvC3Control).text =
            "Control:  ${"%.3f".format(gate.c3.control)}"
        findViewById<TextView>(R.id.tvC3Carrier).text =
            "Carrier:  ${"%.3f".format(gate.c3.carrier)}"

        // SAR Findings
        val sarContainer = findViewById<LinearLayout>(R.id.sarFindingsContainer)
        sarFindings[gate.gateId]?.forEach { finding ->
            val tv = TextView(this).apply {
                text = "• $finding"
                setTextColor(Color.parseColor("#F0883E"))
                textSize = 12f
                setPadding(0, 4, 0, 4)
            }
            sarContainer.addView(tv)
        }

        // Reasons
        val tvReasons = findViewById<TextView>(R.id.tvReasons)
        if (gate.reasons.isNotEmpty()) {
            tvReasons.text = gate.reasons.joinToString("\n") { "→ $it" }
        } else {
            tvReasons.text = "No issues detected."
        }

        // Remediation actions
        val remContainer = findViewById<LinearLayout>(R.id.gateRemediationContainer)
        remediations[gate.gateId]?.forEach { action ->
            val btn = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
                text = action.title
                setTextColor(Color.parseColor("#58A6FF"))
                textSize = 12f
                strokeColor = android.content.res.ColorStateList.valueOf(Color.parseColor("#30363D"))
                setOnClickListener {
                    try {
                        startActivity(Intent(action.settingsAction))
                    } catch (_: Exception) {
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                    }
                }
            }
            val desc = TextView(this).apply {
                text = action.description
                setTextColor(Color.parseColor("#7D8590"))
                textSize = 11f
                setPadding(0, 0, 0, 12)
            }
            remContainer.addView(btn)
            remContainer.addView(desc)
        }
    }

    private fun decisionColor(decision: TrustDecision): String = when (decision) {
        TrustDecision.ALLOW -> "#4CAF50"
        TrustDecision.DELAY -> "#FFC107"
        TrustDecision.RESTRICT -> "#FF9800"
        TrustDecision.BLOCK -> "#F44336"
    }
}
