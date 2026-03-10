package com.vtmis.fedramp.thadam.gate

import com.vtmis.fedramp.thadam.agent.CIAAgent
import com.vtmis.fedramp.thadam.model.*

/**
 * Abstract base for all C3 Gates.
 *
 * Each gate evaluates Context, Control, and Carrier to produce a trust decision.
 * Gates inherit restrictions from upstream gates in the chain.
 */
abstract class C3Gate(
    val gateId: String,
    val gateName: String,
    protected val ciaAgent: CIAAgent
) {
    // C3 component weights (subclasses may override)
    open val contextWeight: Double = 0.40
    open val controlWeight: Double = 0.35
    open val carrierWeight: Double = 0.25

    // Decision thresholds
    open val allowThreshold: Double = 0.80
    open val restrictThreshold: Double = 0.60
    open val delayThreshold: Double = 0.40

    // Upstream gate floor — chain propagation
    var upstreamFloor: TrustDecision = TrustDecision.ALLOW

    abstract fun evaluateContext(): Double
    abstract fun evaluateControl(): Double
    abstract fun evaluateCarrier(): Double

    /** Evaluate this gate and return a decision */
    fun evaluate(): GateResult {
        val c3 = C3Evaluation(
            context = evaluateContext().coerceIn(0.0, 1.0),
            control = evaluateControl().coerceIn(0.0, 1.0),
            carrier = evaluateCarrier().coerceIn(0.0, 1.0)
        )

        val score = c3.weighted(contextWeight, controlWeight, carrierWeight)

        var decision = when {
            score >= allowThreshold -> TrustDecision.ALLOW
            score >= restrictThreshold -> TrustDecision.RESTRICT
            score >= delayThreshold -> TrustDecision.DELAY
            else -> TrustDecision.BLOCK
        }

        // Chain propagation: can only be as permissive as upstream
        decision = TrustDecision.mostRestrictive(decision, upstreamFloor)

        val reasons = buildReasons(c3, score, decision)

        return GateResult(
            gateId = gateId,
            gateName = gateName,
            decision = decision,
            trustScore = score,
            c3 = c3,
            reasons = reasons
        )
    }

    private fun buildReasons(c3: C3Evaluation, score: Double, decision: TrustDecision): List<String> {
        val reasons = mutableListOf<String>()
        if (c3.context < 0.5) reasons.add("Context degraded (${formatScore(c3.context)})")
        if (c3.control < 0.5) reasons.add("Controls insufficient (${formatScore(c3.control)})")
        if (c3.carrier < 0.5) reasons.add("Carrier unreliable (${formatScore(c3.carrier)})")
        if (upstreamFloor.severity > TrustDecision.ALLOW.severity) {
            reasons.add("Upstream chain restriction applied: ${upstreamFloor.name}")
        }
        return reasons
    }

    private fun formatScore(d: Double) = "%.2f".format(d)
}
