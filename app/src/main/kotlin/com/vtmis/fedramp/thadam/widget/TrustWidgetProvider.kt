package com.vtmis.fedramp.thadam.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import com.vtmis.fedramp.R
import com.vtmis.fedramp.thadam.model.TrustLevel
import com.vtmis.fedramp.thadam.service.ThadamService
import com.vtmis.fedramp.thadam.ui.ThadamDashboardActivity
import java.text.SimpleDateFormat
import java.util.*

class TrustWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ThadamService.ACTION_TRUST_UPDATE) {
            val mgr = AppWidgetManager.getInstance(context)
            val ids = mgr.getAppWidgetIds(ComponentName(context, TrustWidgetProvider::class.java))
            for (id in ids) {
                updateWidget(context, mgr, id)
            }
        }
    }

    companion object {
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_trust)

            val result = ThadamService.latestChainResult
            if (result != null) {
                views.setTextViewText(R.id.widgetTrustLevel, result.globalTrustLevel.label.uppercase())
                views.setTextColor(R.id.widgetTrustLevel, Color.parseColor(result.globalTrustLevel.colorHex))
                views.setTextViewText(R.id.widgetScore, "%.1f%%".format(result.globalTrustScore * 100))
                views.setTextViewText(R.id.widgetTime,
                    SimpleDateFormat("HH:mm", Locale.US).format(Date(result.timestamp)))
            } else {
                views.setTextViewText(R.id.widgetTrustLevel, "SCANNING...")
                views.setTextColor(R.id.widgetTrustLevel, Color.parseColor(TrustLevel.YELLOW.colorHex))
                views.setTextViewText(R.id.widgetScore, "—")
            }

            // Open dashboard on tap
            val pendingIntent = PendingIntent.getActivity(
                context, 0,
                Intent(context, ThadamDashboardActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetTrustLevel, pendingIntent)
            views.setOnClickPendingIntent(R.id.widgetScore, pendingIntent)

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }
}
