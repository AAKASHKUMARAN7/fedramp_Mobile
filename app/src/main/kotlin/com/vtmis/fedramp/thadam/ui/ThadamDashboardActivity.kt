package com.vtmis.fedramp.thadam.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.vtmis.fedramp.R
import com.vtmis.fedramp.thadam.agent.DeviceProfiler
import com.vtmis.fedramp.thadam.audit.AuditLogger
import com.vtmis.fedramp.thadam.db.ThadamDatabase
import com.vtmis.fedramp.thadam.engine.AdaptiveResponseEngine
import com.vtmis.fedramp.thadam.export.ComplianceReportExporter
import com.vtmis.fedramp.thadam.gate.GateChain
import com.vtmis.fedramp.thadam.model.*
import com.vtmis.fedramp.thadam.service.ThadamService
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class ThadamDashboardActivity : AppCompatActivity() {

    private lateinit var gateChain: GateChain
    private lateinit var auditLogger: AuditLogger
    private lateinit var db: ThadamDatabase
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Views
    private lateinit var tvTrustLevel: TextView
    private lateinit var tvGlobalScore: TextView
    private lateinit var progressTrust: ProgressBar
    private lateinit var tvChainFloor: TextView
    private lateinit var tvLastScan: TextView
    private lateinit var gateResultsContainer: LinearLayout
    private lateinit var signalDetailsContainer: LinearLayout
    private lateinit var remediationContainer: LinearLayout
    private lateinit var tvAuditLog: TextView
    private lateinit var trustHistoryChart: TrustHistoryChart
    private lateinit var tvDeviceName: TextView
    private lateinit var tvDeviceDetails: TextView
    private lateinit var tvSupplyChainRisk: TextView

    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    // Polling handler for reliable live updates
    private val handler = Handler(Looper.getMainLooper())
    private var lastResultTimestamp = 0L
    private val pollRunnable = object : Runnable {
        override fun run() {
            val result = ThadamService.latestChainResult
            if (result != null && result.timestamp != lastResultTimestamp) {
                lastResultTimestamp = result.timestamp
                updateFromServiceState()
            }
            handler.postDelayed(this, 5_000L)
        }
    }

    private val trustUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ThadamService.ACTION_TRUST_UPDATE) {
                updateFromServiceState()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thadam_dashboard)

        gateChain = GateChain(applicationContext)
        auditLogger = AuditLogger(applicationContext)
        db = ThadamDatabase.get(applicationContext)

        bindViews()
        setupButtons()
        loadDeviceInfo()
        loadTrustHistory()

        runEvaluation()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ThadamService.ACTION_TRUST_UPDATE)
        if (Build.VERSION.SDK_INT >= 34) {
            registerReceiver(trustUpdateReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(trustUpdateReceiver, filter)
        }
        handler.post(pollRunnable)
        updateFromServiceState()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(pollRunnable)
        unregisterReceiver(trustUpdateReceiver)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun bindViews() {
        tvTrustLevel = findViewById(R.id.tvTrustLevel)
        tvGlobalScore = findViewById(R.id.tvGlobalScore)
        progressTrust = findViewById(R.id.progressTrust)
        tvChainFloor = findViewById(R.id.tvChainFloor)
        tvLastScan = findViewById(R.id.tvLastScan)
        gateResultsContainer = findViewById(R.id.gateResultsContainer)
        signalDetailsContainer = findViewById(R.id.signalDetailsContainer)
        remediationContainer = findViewById(R.id.remediationContainer)
        tvAuditLog = findViewById(R.id.tvAuditLog)
        trustHistoryChart = findViewById(R.id.trustHistoryChart)
        tvDeviceName = findViewById(R.id.tvDeviceName)
        tvDeviceDetails = findViewById(R.id.tvDeviceDetails)
        tvSupplyChainRisk = findViewById(R.id.tvSupplyChainRisk)
    }

    private fun setupButtons() {
        findViewById<View>(R.id.btnRefresh).setOnClickListener {
            runEvaluation()
            loadTrustHistory()
        }
        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<View>(R.id.btnExport).setOnClickListener {
            val result = ThadamService.latestChainResult ?: return@setOnClickListener
            val signals = ThadamService.latestSignals ?: return@setOnClickListener
            ComplianceReportExporter.generateAndShare(
                this, result, signals, ThadamService.latestDeviceInfo
            )
        }
        // Theme toggle
        val btnTheme = findViewById<MaterialButton>(R.id.btnThemeToggle)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isDark = prefs.getBoolean("dark_mode", true)
        btnTheme.text = if (isDark) "\u2600" else "\uD83C\uDF19"
        btnTheme.setOnClickListener {
            val nowDark = prefs.getBoolean("dark_mode", true)
            prefs.edit().putBoolean("dark_mode", !nowDark).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (nowDark) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
            )
        }
    }

    private fun loadDeviceInfo() {
        scope.launch(Dispatchers.IO) {
            val info = ThadamService.latestDeviceInfo ?: DeviceProfiler.profile(applicationContext)
            withContext(Dispatchers.Main) {
                tvDeviceName.text = info.displayName
                tvDeviceDetails.text = "Android ${info.androidVersion} | Patch: ${info.securityPatch} | ${info.socVendor}"
                tvSupplyChainRisk.text = "Supply Chain: ${info.supplyChainRisk}"
                val riskColor = when {
                    info.supplyChainRisk.contains("CRITICAL") -> "#F44336"
                    info.supplyChainRisk.contains("HIGH") -> "#FF9800"
                    info.supplyChainRisk.contains("MODERATE") -> "#FFC107"
                    else -> "#4CAF50"
                }
                tvSupplyChainRisk.setTextColor(Color.parseColor(riskColor))
            }
        }
    }

    private fun loadTrustHistory() {
        scope.launch(Dispatchers.IO) {
            try {
                val entries = db.trustHistoryDao().getRecent(200)
                if (entries.isNotEmpty()) {
                    val sorted = entries.sortedBy { it.timestamp }
                    val mainPoints = sorted.map {
                        TrustHistoryChart.DataPoint(it.timestamp, it.globalScore)
                    }
                    val confPoints = sorted.map {
                        TrustHistoryChart.DataPoint(it.timestamp, it.confidentiality)
                    }
                    val integPoints = sorted.map {
                        TrustHistoryChart.DataPoint(it.timestamp, it.integrity)
                    }
                    val availPoints = sorted.map {
                        TrustHistoryChart.DataPoint(it.timestamp, it.availability)
                    }
                    withContext(Dispatchers.Main) {
                        trustHistoryChart.setData(mainPoints)
                        trustHistoryChart.setCIAData(confPoints, integPoints, availPoints)
                    }
                }
            } catch (_: Exception) { }
        }
    }

    private fun runEvaluation() {
        val result = gateChain.evaluateChain()
        val signals = gateChain.getCIAAgent().getAllSignalScores()
        auditLogger.logChainResult(result)
        updateUI(result, signals)
    }

    private fun updateFromServiceState() {
        val result = ThadamService.latestChainResult ?: return
        val signals = ThadamService.latestSignals ?: return
        updateUI(result, signals)
        loadTrustHistory()
    }

    private fun updateUI(result: ChainResult, signals: Map<String, Double>) {
        // Global trust
        tvTrustLevel.text = result.globalTrustLevel.label.uppercase()
        tvTrustLevel.setTextColor(Color.parseColor(result.globalTrustLevel.colorHex))
        tvGlobalScore.text = "%.1f%%".format(result.globalTrustScore * 100)
        progressTrust.progress = (result.globalTrustScore * 100).toInt()
        tvChainFloor.text = "Chain Floor: ${result.chainFloor.name}"
        tvLastScan.text = "Last: ${dateFormat.format(Date(result.timestamp))}"

        // CIA breakdown
        updateCIARow(R.id.rowConfidentiality, "Confidentiality", result.ciaScore.confidentiality)
        updateCIARow(R.id.rowIntegrity, "Integrity", result.ciaScore.integrity)
        updateCIARow(R.id.rowAvailability, "Availability", result.ciaScore.availability)

        // Gate results (tappable for detail)
        gateResultsContainer.removeAllViews()
        for ((index, gateResult) in result.gateResults.withIndex()) {
            addGateResultCard(gateResult, index)
        }

        // Signal details
        signalDetailsContainer.removeAllViews()
        for ((name, score) in signals) {
            addSignalRow(name, score)
        }

        // Remediation actions based on adaptive response
        updateRemediation(result)

        // Audit log
        val events = auditLogger.getRecentEvents(10)
        if (events.isNotEmpty()) {
            tvAuditLog.text = events.reversed().joinToString("\n") { event ->
                "${dateFormat.format(Date(event.timestamp))} ${event.gateId} ${event.decision.name} " +
                        "%.3f".format(event.trustScore)
            }
        }
    }

    private fun updateRemediation(result: ChainResult) {
        remediationContainer.removeAllViews()
        val policy = AdaptiveResponseEngine.getPolicy(result)

        // Urgency banner
        val urgencyColor = when (policy.urgency) {
            "CRITICAL" -> "#F44336"
            "HIGH" -> "#FF9800"
            "ELEVATED" -> "#FFC107"
            else -> "#4CAF50"
        }
        val tvUrgency = TextView(this).apply {
            text = "RESPONSE: ${policy.urgency}"
            setTextColor(Color.parseColor(urgencyColor))
            textSize = 12f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            setPadding(0, 0, 0, 8)
        }
        remediationContainer.addView(tvUrgency)

        // Quick fix buttons for common findings
        val quickFixes = mutableListOf<Triple<String, String, String>>()
        result.gateResults.forEach { gate ->
            gate.reasons.forEach { reason ->
                when {
                    reason.contains("VPN", true) ->
                        quickFixes.add(Triple("Enable VPN", reason, Settings.ACTION_VPN_SETTINGS))
                    reason.contains("USB", true) || reason.contains("debug", true) ->
                        quickFixes.add(Triple("Disable USB Debug", reason, Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
                    reason.contains("Wi-Fi", true) || reason.contains("wifi", true) ->
                        quickFixes.add(Triple("Review Wi-Fi", reason, Settings.ACTION_WIFI_SETTINGS))
                    reason.contains("lock", true) || reason.contains("biometric", true) ->
                        quickFixes.add(Triple("Set Screen Lock", reason, Settings.ACTION_SECURITY_SETTINGS))
                    reason.contains("Bluetooth", true) ->
                        quickFixes.add(Triple("Check Bluetooth", reason, Settings.ACTION_BLUETOOTH_SETTINGS))
                }
            }
        }

        // De-duplicate and add buttons
        val seen = mutableSetOf<String>()
        for ((title, reason, action) in quickFixes) {
            if (!seen.add(title)) continue
            val btn = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
                text = title
                setTextColor(Color.parseColor("#58A6FF"))
                textSize = 11f
                strokeColor = android.content.res.ColorStateList.valueOf(Color.parseColor("#30363D"))
                setOnClickListener {
                    try { startActivity(Intent(action)) }
                    catch (_: Exception) { startActivity(Intent(Settings.ACTION_SETTINGS)) }
                }
            }
            remediationContainer.addView(btn)
        }

        if (quickFixes.isEmpty()) {
            val tv = TextView(this).apply {
                text = "No critical remediation needed."
                setTextColor(Color.parseColor("#4CAF50"))
                textSize = 12f
            }
            remediationContainer.addView(tv)
        }
    }

    private fun updateCIARow(viewId: Int, label: String, score: Double) {
        val row = findViewById<View>(viewId)
        row.findViewById<TextView>(R.id.tvCiaLabel).text = label
        row.findViewById<TextView>(R.id.tvCiaScore).text = "%.2f".format(score)
        row.findViewById<ProgressBar>(R.id.progressCia).progress = (score * 100).toInt()

        val scoreColor = when {
            score >= 0.85 -> "#4CAF50"
            score >= 0.70 -> "#FFC107"
            score >= 0.50 -> "#FF9800"
            else -> "#F44336"
        }
        row.findViewById<TextView>(R.id.tvCiaScore).setTextColor(Color.parseColor(scoreColor))
    }

    private fun addGateResultCard(result: GateResult, index: Int) {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.item_gate_result, gateResultsContainer, false)

        view.findViewById<TextView>(R.id.tvGateName).text =
            "${result.gateId}: ${result.gateName}"
        view.findViewById<TextView>(R.id.tvGateScore).text =
            "Score: ${"%.3f".format(result.trustScore)}"
        view.findViewById<TextView>(R.id.tvGateC3).text =
            "C3: [${"%.2f".format(result.c3.context)}, ${"%.2f".format(result.c3.control)}, ${"%.2f".format(result.c3.carrier)}]"

        val tvDecision = view.findViewById<TextView>(R.id.tvGateDecision)
        tvDecision.text = result.decision.name
        tvDecision.setTextColor(Color.parseColor(decisionColor(result.decision)))

        val indicator = view.findViewById<View>(R.id.viewGateIndicator)
        indicator.setBackgroundColor(Color.parseColor(decisionColor(result.decision)))

        val tvReasons = view.findViewById<TextView>(R.id.tvGateReasons)
        if (result.reasons.isNotEmpty()) {
            tvReasons.visibility = View.VISIBLE
            tvReasons.text = result.reasons.joinToString("\n")
        }

        // Tap to open gate detail
        view.setOnClickListener { GateDetailActivity.launch(this, index) }

        gateResultsContainer.addView(view)
    }

    private fun addSignalRow(name: String, score: Double) {
        val tv = TextView(this).apply {
            text = "%-28s  %.2f  %s".format(name, score, signalBar(score))
            setTextColor(Color.parseColor(if (score >= 0.7) "#7D8590" else "#F0883E"))
            textSize = 11f
            typeface = android.graphics.Typeface.MONOSPACE
            setPadding(0, 2, 0, 2)
        }
        signalDetailsContainer.addView(tv)
    }

    private fun signalBar(score: Double): String {
        val filled = (score * 10).toInt().coerceIn(0, 10)
        return "\u2588".repeat(filled) + "\u2591".repeat(10 - filled)
    }

    private fun decisionColor(decision: TrustDecision): String = when (decision) {
        TrustDecision.ALLOW -> "#4CAF50"
        TrustDecision.DELAY -> "#FFC107"
        TrustDecision.RESTRICT -> "#FF9800"
        TrustDecision.BLOCK -> "#F44336"
    }
}
