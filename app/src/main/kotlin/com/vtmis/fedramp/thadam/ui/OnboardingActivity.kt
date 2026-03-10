package com.vtmis.fedramp.thadam.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.vtmis.fedramp.R

class OnboardingActivity : AppCompatActivity() {

    companion object {
        private const val PREF_ONBOARDING_DONE = "onboarding_done"

        fun shouldShow(context: Context): Boolean {
            return !PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_ONBOARDING_DONE, false)
        }

        fun markDone(context: Context) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(PREF_ONBOARDING_DONE, true).apply()
        }
    }

    private data class Step(val title: String, val subtitle: String, val body: String)

    private val steps = listOf(
        Step("THADAM", "C3 Gate Adaptive Trust",
            "A real-time security architecture that continuously evaluates your device through 6 trust gates and 22 security signals based on NIST SP 800-53 Rev. 5 controls."),
        Step("6 GATES", "Network → Access → Storage → Execution → Process → Transmission",
            "Each gate evaluates Context (is the device trustworthy?), Control (are security controls in place?), and Carrier (is the communication channel secure?). Trust flows through the chain — the weakest gate limits the overall level."),
        Step("CIA TRIAD", "Confidentiality • Integrity • Availability",
            "22 security signals feed into three CIA dimensions. Your global trust score is a weighted composite. GREEN (≥85%) = full trust, YELLOW (≥70%) = caution, ORANGE (≥50%) = degraded, RED (<50%) = minimal trust."),
        Step("LIVE MONITORING", "Continuous Assessment",
            "Thadam runs as a background service — scanning every 30 seconds. You get real-time trust updates, alert notifications on trust drops, and a full compliance report you can export and share."),
        Step("READY", "Let's secure your device",
            "Tap START to begin your first trust evaluation. Review findings, apply remediation actions, and monitor your device's FedRAMP compliance posture in real-time.")
    )

    private var currentStep = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        findViewById<View>(R.id.btnOnboardNext).setOnClickListener {
            if (currentStep < steps.size - 1) {
                currentStep++
                showStep(currentStep)
            } else {
                finishOnboarding()
            }
        }

        findViewById<View>(R.id.btnOnboardSkip).setOnClickListener {
            finishOnboarding()
        }

        showStep(0)
    }

    private fun showStep(index: Int) {
        val step = steps[index]
        findViewById<TextView>(R.id.tvOnboardTitle).text = step.title
        findViewById<TextView>(R.id.tvOnboardSubtitle).text = step.subtitle
        findViewById<TextView>(R.id.tvOnboardBody).text = step.body

        val btn = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnOnboardNext)
        btn.text = if (index == steps.size - 1) "START" else "NEXT"

        // Update step indicators
        val indicators = findViewById<LinearLayout>(R.id.stepIndicators)
        indicators.removeAllViews()
        for (i in steps.indices) {
            val dot = View(this).apply {
                val size = if (i == index) 12 else 8
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    setMargins(6, 0, 6, 0)
                    gravity = Gravity.CENTER_VERTICAL
                }
                background = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.OVAL
                    setColor(Color.parseColor(if (i == index) "#58A6FF" else "#30363D"))
                }
            }
            indicators.addView(dot)
        }
    }

    private fun finishOnboarding() {
        markDone(this)
        startActivity(Intent(this, ThadamDashboardActivity::class.java))
        finish()
    }
}
