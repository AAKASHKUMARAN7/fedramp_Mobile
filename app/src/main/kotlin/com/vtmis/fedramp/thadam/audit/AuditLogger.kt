package com.vtmis.fedramp.thadam.audit

import android.content.Context
import android.util.Log
import com.vtmis.fedramp.thadam.model.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Audit Logger for Thadam C3 Gate System
 *
 * Logs all gate decisions, trust level changes, and security events.
 * Stores events locally (SOC-compliant) since SIEM is not yet configured (SAR-MOD-010).
 *
 * Log format follows NIST AU-3 requirements:
 *   timestamp | event_type | gate_id | decision | trust_score | details
 */
class AuditLogger(private val context: Context) {

    companion object {
        private const val TAG = "ThadamAudit"
        private const val LOG_FILE = "thadam_audit.log"
        private const val MAX_MEMORY_EVENTS = 200
        private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024  // 5MB rotation
    }

    // In-memory event buffer for dashboard display
    private val recentEvents = CopyOnWriteArrayList<AuditEvent>()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    /** Log a gate evaluation result */
    fun logGateResult(result: GateResult) {
        val event = AuditEvent(
            eventId = generateEventId(),
            gateId = result.gateId,
            decision = result.decision,
            trustScore = result.trustScore,
            details = "${result.gateName}: ${result.decision.name} " +
                    "(score=${formatScore(result.trustScore)}) " +
                    "C3=[${formatScore(result.c3.context)},${formatScore(result.c3.control)},${formatScore(result.c3.carrier)}] " +
                    if (result.reasons.isNotEmpty()) "reasons=${result.reasons.joinToString("; ")}" else ""
        )
        writeEvent(event)
    }

    /** Log a full chain evaluation */
    fun logChainResult(chain: ChainResult) {
        for (gateResult in chain.gateResults) {
            logGateResult(gateResult)
        }
        // Log the chain summary
        val summary = AuditEvent(
            eventId = generateEventId(),
            gateId = "CHAIN",
            decision = chain.chainFloor,
            trustScore = chain.globalTrustScore,
            details = "Chain complete: floor=${chain.chainFloor.name} " +
                    "global=${formatScore(chain.globalTrustScore)} " +
                    "level=${chain.globalTrustLevel.name} " +
                    "CIA=[C=${formatScore(chain.ciaScore.confidentiality)}," +
                    "I=${formatScore(chain.ciaScore.integrity)}," +
                    "A=${formatScore(chain.ciaScore.availability)}]"
        )
        writeEvent(summary)
    }

    /** Log a trust level change */
    fun logTrustLevelChange(previous: TrustLevel, current: TrustLevel, score: Double) {
        val event = AuditEvent(
            eventId = generateEventId(),
            gateId = "CIA-AGENT",
            decision = when (current) {
                TrustLevel.GREEN -> TrustDecision.ALLOW
                TrustLevel.YELLOW -> TrustDecision.DELAY
                TrustLevel.ORANGE -> TrustDecision.RESTRICT
                TrustLevel.RED -> TrustDecision.BLOCK
            },
            trustScore = score,
            details = "Trust level changed: ${previous.name} -> ${current.name} (score=${formatScore(score)})"
        )
        writeEvent(event)
    }

    /** Log a security incident */
    fun logSecurityEvent(gateId: String, description: String) {
        val event = AuditEvent(
            eventId = generateEventId(),
            gateId = gateId,
            decision = TrustDecision.BLOCK,
            trustScore = 0.0,
            details = "SECURITY EVENT: $description"
        )
        writeEvent(event)
    }

    /** Get recent events for dashboard display */
    fun getRecentEvents(count: Int = 50): List<AuditEvent> {
        return recentEvents.takeLast(count.coerceAtMost(MAX_MEMORY_EVENTS))
    }

    /** Get all events from log file */
    fun readLogFile(): String = try {
        val file = File(context.filesDir, LOG_FILE)
        if (file.exists()) file.readText() else "No audit log entries yet."
    } catch (e: Exception) {
        "Error reading audit log: ${e.message}"
    }

    // ================================================================
    // Private helpers
    // ================================================================

    private fun writeEvent(event: AuditEvent) {
        // Memory buffer
        recentEvents.add(event)
        if (recentEvents.size > MAX_MEMORY_EVENTS) {
            recentEvents.removeAt(0)
        }

        // Format log line
        val line = "${dateFormat.format(Date(event.timestamp))} | " +
                "${event.eventId} | ${event.gateId} | ${event.decision.name} | " +
                "${formatScore(event.trustScore)} | ${event.details}"

        // Android logcat
        when (event.decision) {
            TrustDecision.BLOCK -> Log.e(TAG, line)
            TrustDecision.RESTRICT -> Log.w(TAG, line)
            TrustDecision.DELAY -> Log.i(TAG, line)
            TrustDecision.ALLOW -> Log.d(TAG, line)
        }

        // File-based log (persistent)
        try {
            val file = File(context.filesDir, LOG_FILE)
            // Rotate if too large
            if (file.exists() && file.length() > MAX_FILE_SIZE_BYTES) {
                val rotated = File(context.filesDir, "${LOG_FILE}.1")
                if (rotated.exists()) rotated.delete()
                file.renameTo(rotated)
            }
            file.appendText(line + "\n")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write audit log: ${e.message}")
        }
    }

    private fun generateEventId(): String {
        val ts = System.currentTimeMillis()
        val rand = (Math.random() * 9999).toInt()
        return "EVT-$ts-$rand"
    }

    private fun formatScore(d: Double) = "%.3f".format(d)
}
