package com.besaba.anvarov.orenclock

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

class TimeTickReceiver : BroadcastReceiver() {

    private val TAG = "ClockWidget"

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(TAG, "onReceive: TimeTickReceiver")
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(context.packageName,
                ClockWidget::class.java.name))
        ClockWidget.updateAppWidget(context, appWidgetManager, ids[0])
    }
}
