package com.vtmis.fedramp.thadam.engine

import com.vtmis.fedramp.thadam.model.*

/**
 * Adaptive Response Engine
 *
 * Maps trust levels to concrete enforcement recommendations
 * based on FedRAMP Low baseline requirements.
 */
object AdaptiveResponseEngine {

    data class ResponsePolicy(
        val level: TrustLevel,
        val actions: List<String>,
        val restrictions: List<String>,
        val urgency: String
    )

    fun getPolicy(result: ChainResult): ResponsePolicy {
        return when (result.globalTrustLevel) {
            TrustLevel.GREEN -> ResponsePolicy(
                level = TrustLevel.GREEN,
                actions = listOf(
                    "All operations authorized",
                    "Continue monitoring at standard 30s interval"
                ),
                restrictions = emptyList(),
                urgency = "ROUTINE"
            )
            TrustLevel.YELLOW -> ResponsePolicy(
                level = TrustLevel.YELLOW,
                actions = listOf(
                    "Increase scan frequency to 15s",
                    "Flag for security review within 30 days",
                    "Recommend enabling VPN if not active"
                ),
                restrictions = listOf(
                    "Federal HIGH data access: DELAYED — require additional auth",
                    "External network connections: MONITORED"
                ),
                urgency = "ELEVATED"
            )
            TrustLevel.ORANGE -> ResponsePolicy(
                level = TrustLevel.ORANGE,
                actions = listOf(
                    "Increase scan frequency to 10s",
                    "Generate incident report for review",
                    "Require immediate remediation of top findings",
                    "Notify device owner of degraded status"
                ),
                restrictions = listOf(
                    "Federal data access: RESTRICTED — read-only pending review",
                    "App installations: BLOCKED until remediated",
                    "Network services: MONITORED with logging"
                ),
                urgency = "HIGH"
            )
            TrustLevel.RED -> ResponsePolicy(
                level = TrustLevel.RED,
                actions = listOf(
                    "Continuous monitoring at 5s interval",
                    "Trigger full security incident",
                    "Recommend immediate device isolation",
                    "Escalate to security officer"
                ),
                restrictions = listOf(
                    "ALL federal data access: BLOCKED",
                    "Network communications: QUARANTINED",
                    "App execution: SUSPEND non-essential",
                    "USB/Bluetooth: DISABLE recommended"
                ),
                urgency = "CRITICAL"
            )
        }
    }

    fun getGateRemediation(result: GateResult): List<String> = when (result.decision) {
        TrustDecision.ALLOW -> listOf("No action required — gate passed")
        TrustDecision.DELAY -> result.reasons.map { "Fix: $it" } + listOf(
            "Score ${formatPercent(result.trustScore)} — remediate within 14 days"
        )
        TrustDecision.RESTRICT -> result.reasons.map { "URGENT: $it" } + listOf(
            "Score ${formatPercent(result.trustScore)} — remediate within 48 hours"
        )
        TrustDecision.BLOCK -> result.reasons.map { "CRITICAL: $it" } + listOf(
            "Score ${formatPercent(result.trustScore)} — immediate remediation required"
        )
    }

    private fun formatPercent(d: Double) = "%.1f%%".format(d * 100)
}
