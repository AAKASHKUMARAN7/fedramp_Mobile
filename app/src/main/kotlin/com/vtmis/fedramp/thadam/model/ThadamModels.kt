package com.vtmis.fedramp.thadam.model

/**
 * Thadam C3 Gate Adaptive Trust Architecture — Core Data Models
 * Mapped to VTMIS FedRAMP Authorization Package (27 SAR findings)
 */

// Gate decision output
enum class TrustDecision(val severity: Int) {
    ALLOW(0),
    DELAY(1),
    RESTRICT(2),
    BLOCK(3);

    companion object {
        /** Returns the more restrictive of two decisions */
        fun mostRestrictive(a: TrustDecision, b: TrustDecision): TrustDecision =
            if (a.severity >= b.severity) a else b
    }
}

// Global trust level derived from CIA score
enum class TrustLevel(val label: String, val colorHex: String) {
    GREEN("Full Trust", "#4CAF50"),
    YELLOW("Elevated Caution", "#FFC107"),
    ORANGE("Degraded Trust", "#FF9800"),
    RED("Minimal Trust", "#F44336");

    companion object {
        fun fromScore(score: Double): TrustLevel = when {
            score >= 0.85 -> GREEN
            score >= 0.70 -> YELLOW
            score >= 0.50 -> ORANGE
            else -> RED
        }
    }
}

// FedRAMP data classification
enum class DataClassification {
    FEDERAL_HIGH,
    FEDERAL_MODERATE,
    FEDERAL_LOW,
    PERSONAL,
    PUBLIC
}

// C3 = Context + Control + Carrier evaluation
data class C3Evaluation(
    val context: Double,
    val control: Double,
    val carrier: Double
) {
    fun weighted(contextW: Double, controlW: Double, carrierW: Double): Double =
        contextW * context + controlW * control + carrierW * carrier
}

// CIA Agent composite score
data class CIAScore(
    val confidentiality: Double,
    val integrity: Double,
    val availability: Double
) {
    fun global(wC: Double = 0.33, wI: Double = 0.34, wA: Double = 0.33): Double =
        wC * confidentiality + wI * integrity + wA * availability
}

// Individual gate evaluation result
data class GateResult(
    val gateId: String,
    val gateName: String,
    val decision: TrustDecision,
    val trustScore: Double,
    val c3: C3Evaluation,
    val reasons: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)

// Full chain evaluation result (all 6 gates)
data class ChainResult(
    val gateResults: List<GateResult>,
    val chainFloor: TrustDecision,
    val globalTrustScore: Double,
    val globalTrustLevel: TrustLevel,
    val ciaScore: CIAScore,
    val timestamp: Long = System.currentTimeMillis()
) {
    val weakestGate: GateResult?
        get() = gateResults.maxByOrNull { it.decision.severity }
}

// Audit event for logging every gate decision
data class AuditEvent(
    val eventId: String,
    val gateId: String,
    val decision: TrustDecision,
    val trustScore: Double,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)

// Trust snapshot for history tracking
data class TrustSnapshot(
    val ciaScore: CIAScore,
    val globalScore: Double,
    val trustLevel: TrustLevel,
    val chainResult: ChainResult?,
    val timestamp: Long = System.currentTimeMillis()
)

// Gate identifiers matching the architecture document
object GateIds {
    const val NETWORK = "GATE-1"
    const val ACCESS = "GATE-2"
    const val STORAGE = "GATE-3"
    const val EXECUTION = "GATE-4"
    const val PROCESS = "GATE-5"
    const val TRANSMISSION = "GATE-6"
}

// Gate names for display
object GateNames {
    const val NETWORK = "Network Gate"
    const val ACCESS = "Access Gate"
    const val STORAGE = "Storage Gate"
    const val EXECUTION = "Execution Gate"
    const val PROCESS = "Process Gate"
    const val TRANSMISSION = "Transmission Gate"
}
