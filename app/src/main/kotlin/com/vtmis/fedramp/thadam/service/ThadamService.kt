package com.vtmis.fedramp.thadam.service

import android.app.*
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.vtmis.fedramp.R
import com.vtmis.fedramp.thadam.agent.DeviceProfiler
import com.vtmis.fedramp.thadam.audit.AuditLogger
import com.vtmis.fedramp.thadam.db.ThadamDatabase
import com.vtmis.fedramp.thadam.db.TrustHistoryEntry
import com.vtmis.fedramp.thadam.gate.GateChain
import com.vtmis.fedramp.thadam.model.*
import com.vtmis.fedramp.thadam.ui.ThadamDashboardActivity
import com.vtmis.fedramp.thadam.widget.TrustWidgetProvider
import kotlinx.coroutines.*

/**
 * Thadam Foreground Service
 *
 * Continuously evaluates the C3 Gate chain at regular intervals.
 * Posts a persistent notification showing the current trust level.
 * Broadcasts trust updates to the Dashboard UI.
 */
class ThadamService : Service() {

    companion object {
        const val CHANNEL_ID = "thadam_trust_channel"
        const val ALERT_CHANNEL_ID = "thadam_alert_channel"
        const val NOTIFICATION_ID = 1001
        const val ALERT_NOTIFICATION_ID = 1002
        const val ACTION_TRUST_UPDATE = "com.vtmis.fedramp.thadam.TRUST_UPDATE"
        const val EXTRA_TRUST_LEVEL = "trust_level"
        const val EXTRA_TRUST_SCORE = "trust_score"
        const val EXTRA_CHAIN_FLOOR = "chain_floor"
        const val EVAL_INTERVAL_MS = 30_000L  // 30 seconds
        const val MAX_HISTORY_ENTRIES = 2000

        // Shared state for dashboard access
        @Volatile var latestChainResult: ChainResult? = null
        @Volatile var latestSignals: Map<String, Double>? = null
        @Volatile var latestDeviceInfo: DeviceProfiler.DeviceInfo? = null
    }

    private lateinit var gateChain: GateChain
    private lateinit var auditLogger: AuditLogger
    private lateinit var db: ThadamDatabase
    private var evaluationJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var previousTrustLevel: TrustLevel? = null

    override fun onCreate() {
        super.onCreate()
        gateChain = GateChain(applicationContext)
        auditLogger = AuditLogger(applicationContext)
        db = ThadamDatabase.get(applicationContext)
        createNotificationChannel()
        createAlertChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification("Initializing...", TrustLevel.YELLOW))

        // Start periodic evaluation
        evaluationJob?.cancel()
        evaluationJob = serviceScope.launch {
            while (isActive) {
                try {
                    runEvaluation()
                } catch (e: Exception) {
                    auditLogger.logSecurityEvent("SERVICE",
                        "Evaluation error: ${e.message}")
                }
                delay(EVAL_INTERVAL_MS)
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        evaluationJob?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun runEvaluation() {
        // Collect device info (once on first run, cached after)
        if (latestDeviceInfo == null) {
            latestDeviceInfo = DeviceProfiler.profile(applicationContext)
        }
        val deviceInfo = latestDeviceInfo!!

        // Run full chain evaluation
        val result = gateChain.evaluateChain()
        latestChainResult = result

        // Collect individual signals for dashboard
        latestSignals = gateChain.getCIAAgent().getAllSignalScores()

        // Log the full chain
        auditLogger.logChainResult(result)

        // Store in Room database for history graph
        serviceScope.launch(Dispatchers.IO) {
            try {
                val gateScores = result.gateResults.map { it.trustScore }
                db.trustHistoryDao().insert(
                    TrustHistoryEntry(
                        timestamp = result.timestamp,
                        globalScore = result.globalTrustScore,
                        trustLevel = result.globalTrustLevel.name,
                        confidentiality = result.ciaScore.confidentiality,
                        integrity = result.ciaScore.integrity,
                        availability = result.ciaScore.availability,
                        chainFloor = result.chainFloor.name,
                        gate1Score = gateScores.getOrElse(0) { 0.0 },
                        gate2Score = gateScores.getOrElse(1) { 0.0 },
                        gate3Score = gateScores.getOrElse(2) { 0.0 },
                        gate4Score = gateScores.getOrElse(3) { 0.0 },
                        gate5Score = gateScores.getOrElse(4) { 0.0 },
                        gate6Score = gateScores.getOrElse(5) { 0.0 },
                        deviceName = deviceInfo.displayName,
                        manufacturer = deviceInfo.manufacturer,
                        androidVersion = deviceInfo.androidVersion,
                        securityPatch = deviceInfo.securityPatch
                    )
                )
                // Keep DB size bounded
                val count = db.trustHistoryDao().count()
                if (count > MAX_HISTORY_ENTRIES) {
                    db.trustHistoryDao().deleteOldest(count - MAX_HISTORY_ENTRIES)
                }
            } catch (_: Exception) { }
        }

        // Detect trust level changes
        val currentLevel = result.globalTrustLevel
        previousTrustLevel?.let { prev ->
            if (prev != currentLevel) {
                auditLogger.logTrustLevelChange(prev, currentLevel, result.globalTrustScore)
                // Send alert notification if trust dropped
                if (currentLevel.ordinal > prev.ordinal) {
                    sendTrustAlert(prev, currentLevel, result.globalTrustScore)
                }
            }
        }
        previousTrustLevel = currentLevel

        // Update notification
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, buildNotification(
            "${currentLevel.label} — ${formatScore(result.globalTrustScore)}",
            currentLevel
        ))

        // Broadcast update for dashboard
        val updateIntent = Intent(ACTION_TRUST_UPDATE).apply {
            setPackage(packageName)
            putExtra(EXTRA_TRUST_LEVEL, currentLevel.name)
            putExtra(EXTRA_TRUST_SCORE, result.globalTrustScore)
            putExtra(EXTRA_CHAIN_FLOOR, result.chainFloor.name)
        }
        sendBroadcast(updateIntent)

        // Update home screen widget
        val widgetMgr = AppWidgetManager.getInstance(applicationContext)
        val widgetIds = widgetMgr.getAppWidgetIds(
            ComponentName(applicationContext, TrustWidgetProvider::class.java)
        )
        for (id in widgetIds) {
            TrustWidgetProvider.updateWidget(applicationContext, widgetMgr, id)
        }
    }

    private fun sendTrustAlert(from: TrustLevel, to: TrustLevel, score: Double) {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(
            this, 1,
            Intent(this, ThadamDashboardActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, ALERT_CHANNEL_ID)
            .setContentTitle("Trust Level Dropped!")
            .setContentText("${from.label} → ${to.label} (${formatScore(score)})")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()
        nm.notify(ALERT_NOTIFICATION_ID, notification)
    }

    private fun buildNotification(statusText: String, level: TrustLevel): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, ThadamDashboardActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val icon = when (level) {
            TrustLevel.GREEN -> android.R.drawable.presence_online
            TrustLevel.YELLOW -> android.R.drawable.presence_away
            TrustLevel.ORANGE -> android.R.drawable.presence_busy
            TrustLevel.RED -> android.R.drawable.presence_offline
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Thadam C3 Trust Monitor")
            .setContentText(statusText)
            .setSmallIcon(icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Thadam Trust Monitor",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Continuous trust evaluation for VTMIS FedRAMP compliance"
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun createAlertChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ALERT_CHANNEL_ID,
                "Trust Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when device trust level drops"
                enableVibration(true)
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun formatScore(d: Double) = "%.1f%%".format(d * 100)
}
