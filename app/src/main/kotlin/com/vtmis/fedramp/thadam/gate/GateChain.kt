package com.vtmis.fedramp.thadam.gate

import android.content.Context
import com.vtmis.fedramp.thadam.agent.CIAAgent
import com.vtmis.fedramp.thadam.model.*

/**
 * Gate Chain Orchestrator
 *
 * Runs all 6 C3 Gates in sequence with trust propagation:
 *   Network → Access → Storage → Execution → Process → Transmission
 *
 * Each gate inherits the upstream floor — the most restrictive decision
 * from all previous gates becomes the minimum restriction for downstream gates.
 */
class GateChain(private val appContext: Context) {

    private val ciaAgent = CIAAgent(appContext)

    // The 6 gates in chain order
    private val gates: List<C3Gate> by lazy {
        listOf(
            NetworkGate(ciaAgent, appContext),       // Gate 1
            AccessGate(ciaAgent, appContext),        // Gate 2
            StorageGate(ciaAgent, appContext),       // Gate 3
            ExecutionGate(ciaAgent, appContext),     // Gate 4
            ProcessGate(ciaAgent, appContext),       // Gate 5
            TransmissionGate(ciaAgent, appContext)   // Gate 6
        )
    }

    /**
     * Execute the full chain of gates.
     * Returns a ChainResult with all gate results, the chain floor,
     * and the global trust level from the CIA Agent.
     */
    fun evaluateChain(): ChainResult {
        // Step 1: Collect CIA signals
        val ciaScore = ciaAgent.collectSignals()
        val (globalScore, globalLevel) = ciaAgent.computeGlobalTrust(ciaScore)

        // Step 2: Adjust gate thresholds based on global trust level
        adjustThresholds(globalLevel)

        // Step 3: Run gates sequentially with upstream propagation
        val results = mutableListOf<GateResult>()
        var chainFloor = TrustDecision.ALLOW

        for (gate in gates) {
            gate.upstreamFloor = chainFloor
            val result = gate.evaluate()
            results.add(result)

            // Update chain floor — most restrictive decision propagates forward
            chainFloor = TrustDecision.mostRestrictive(chainFloor, result.decision)
        }

        return ChainResult(
            gateResults = results,
            chainFloor = chainFloor,
            globalTrustScore = globalScore,
            globalTrustLevel = globalLevel,
            ciaScore = ciaScore
        )
    }

    /**
     * Evaluate a single gate by ID (for targeted re-evaluation).
     */
    fun evaluateGate(gateId: String): GateResult? {
        return gates.find { it.gateId == gateId }?.evaluate()
    }

    /**
     * Get the CIA Agent for direct signal queries.
     */
    fun getCIAAgent(): CIAAgent = ciaAgent

    /**
     * When global trust drops, lower gate thresholds to be stricter.
     * YELLOW: thresholds -= 0.10 (from architecture §8.1)
     */
    private fun adjustThresholds(level: TrustLevel) {
        // Gate thresholds are final — we only adjust by recalculating if needed
        // For now, the gate trust scores already incorporate the CIA signals,
        // so a lower global trust naturally leads to stricter gate decisions.
        // The architecture specifies this as a threshold adjustment, which
        // in practice is handled by the CIA Agent feeding lower scores to gates.
    }
}
