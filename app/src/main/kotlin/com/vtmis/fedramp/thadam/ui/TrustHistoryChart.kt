package com.vtmis.fedramp.thadam.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

/**
 * Custom chart view for drawing trust score history as a line graph.
 * No external chart library needed — uses Canvas directly.
 */
class TrustHistoryChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    data class DataPoint(val timestamp: Long, val score: Double)

    private var dataPoints: List<DataPoint> = emptyList()
    private var ciaData: Triple<List<DataPoint>, List<DataPoint>, List<DataPoint>>? = null

    // Paints
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#21262D")
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8B949E")
        textSize = 24f
        typeface = Typeface.MONOSPACE
    }

    private val thresholdPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val paddingLeft = 80f
    private val paddingRight = 20f
    private val paddingTop = 30f
    private val paddingBottom = 40f

    fun setData(points: List<DataPoint>) {
        dataPoints = points
        invalidate()
    }

    fun setCIAData(conf: List<DataPoint>, integ: List<DataPoint>, avail: List<DataPoint>) {
        ciaData = Triple(conf, integ, avail)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dataPoints.isEmpty()) {
            labelPaint.textSize = 28f
            labelPaint.textAlign = Paint.Align.CENTER
            canvas.drawText("Collecting trust history...", width / 2f, height / 2f, labelPaint)
            labelPaint.textAlign = Paint.Align.LEFT
            return
        }

        val chartW = width - paddingLeft - paddingRight
        val chartH = height - paddingTop - paddingBottom

        drawGrid(canvas, chartW, chartH)
        drawThresholds(canvas, chartW, chartH)

        // Draw CIA lines if available
        ciaData?.let { (conf, integ, avail) ->
            drawLine(canvas, conf, chartW, chartH, Color.parseColor("#58A6FF"), 2f)  // Blue
            drawLine(canvas, integ, chartW, chartH, Color.parseColor("#A371F7"), 2f) // Purple
            drawLine(canvas, avail, chartW, chartH, Color.parseColor("#3FB950"), 2f) // Green
        }

        // Draw main trust line (thicker, on top)
        drawLine(canvas, dataPoints, chartW, chartH, Color.parseColor("#F0883E"), 4f)

        // Draw current score dot
        if (dataPoints.isNotEmpty()) {
            val last = dataPoints.last()
            val x = paddingLeft + chartW
            val y = paddingTop + chartH * (1.0 - last.score).toFloat()
            dotPaint.color = colorForScore(last.score)
            canvas.drawCircle(x, y, 8f, dotPaint)
        }

        drawLabels(canvas, chartW, chartH)
    }

    private fun drawGrid(canvas: Canvas, chartW: Float, chartH: Float) {
        // Horizontal grid lines at 0.0, 0.25, 0.50, 0.75, 1.0
        for (i in 0..4) {
            val y = paddingTop + chartH * (1.0f - i / 4f)
            canvas.drawLine(paddingLeft, y, paddingLeft + chartW, y, gridPaint)
        }
    }

    private fun drawThresholds(canvas: Canvas, chartW: Float, chartH: Float) {
        // GREEN threshold at 0.85
        thresholdPaint.color = Color.parseColor("#3FB950")
        val yGreen = paddingTop + chartH * (1.0f - 0.85f)
        canvas.drawLine(paddingLeft, yGreen, paddingLeft + chartW, yGreen, thresholdPaint)

        // YELLOW threshold at 0.70
        thresholdPaint.color = Color.parseColor("#FFC107")
        val yYellow = paddingTop + chartH * (1.0f - 0.70f)
        canvas.drawLine(paddingLeft, yYellow, paddingLeft + chartW, yYellow, thresholdPaint)

        // RED threshold at 0.50
        thresholdPaint.color = Color.parseColor("#F44336")
        val yRed = paddingTop + chartH * (1.0f - 0.50f)
        canvas.drawLine(paddingLeft, yRed, paddingLeft + chartW, yRed, thresholdPaint)
    }

    private fun drawLine(
        canvas: Canvas, points: List<DataPoint>,
        chartW: Float, chartH: Float,
        color: Int, strokeW: Float
    ) {
        if (points.size < 2) return

        val minT = dataPoints.first().timestamp
        val maxT = dataPoints.last().timestamp
        val timeRange = max(maxT - minT, 1L)

        linePaint.color = color
        linePaint.strokeWidth = strokeW

        val path = Path()
        val fillPath = Path()
        var first = true

        for (point in points) {
            val x = paddingLeft + chartW * ((point.timestamp - minT).toFloat() / timeRange)
            val y = paddingTop + chartH * (1.0 - point.score).toFloat()
            if (first) {
                path.moveTo(x, y)
                fillPath.moveTo(x, paddingTop + chartH)
                fillPath.lineTo(x, y)
                first = false
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }

        // Fill gradient under main line only
        if (strokeW >= 4f) {
            val lastX = paddingLeft + chartW * ((points.last().timestamp - minT).toFloat() / timeRange)
            fillPath.lineTo(lastX, paddingTop + chartH)
            fillPath.close()
            fillPaint.shader = LinearGradient(
                0f, paddingTop, 0f, paddingTop + chartH,
                Color.argb(40, Color.red(color), Color.green(color), Color.blue(color)),
                Color.argb(5, Color.red(color), Color.green(color), Color.blue(color)),
                Shader.TileMode.CLAMP
            )
            canvas.drawPath(fillPath, fillPaint)
            fillPaint.shader = null
        }

        canvas.drawPath(path, linePaint)
    }

    private fun drawLabels(canvas: Canvas, chartW: Float, chartH: Float) {
        labelPaint.textSize = 22f
        labelPaint.textAlign = Paint.Align.RIGHT
        for (i in 0..4) {
            val y = paddingTop + chartH * (1.0f - i / 4f) + 6f
            canvas.drawText("%.0f%%".format(i * 25.0), paddingLeft - 8f, y, labelPaint)
        }

        // Time labels
        if (dataPoints.size >= 2) {
            labelPaint.textAlign = Paint.Align.CENTER
            labelPaint.textSize = 20f
            val fmt = java.text.SimpleDateFormat("HH:mm", java.util.Locale.US)
            // Start
            canvas.drawText(
                fmt.format(java.util.Date(dataPoints.first().timestamp)),
                paddingLeft, paddingTop + chartH + 30f, labelPaint
            )
            // End
            canvas.drawText(
                fmt.format(java.util.Date(dataPoints.last().timestamp)),
                paddingLeft + chartW, paddingTop + chartH + 30f, labelPaint
            )
        }
        labelPaint.textAlign = Paint.Align.LEFT
    }

    private fun colorForScore(score: Double): Int = when {
        score >= 0.85 -> Color.parseColor("#4CAF50")
        score >= 0.70 -> Color.parseColor("#FFC107")
        score >= 0.50 -> Color.parseColor("#FF9800")
        else -> Color.parseColor("#F44336")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.makeMeasureSpec((w * 0.5f).toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, h)
    }
}
