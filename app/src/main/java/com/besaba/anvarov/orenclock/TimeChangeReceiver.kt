package com.besaba.anvarov.orenclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TimeChangeReceiver : BroadcastReceiver() {
    private val TAG = "ClockWidget"

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
//        val thisAppWidget = ComponentName(context.packageName, ClockWidget::class.java.name)
//        val appWidgetManager = AppWidgetManager.getInstance(context)
//        val ids = appWidgetManager.getAppWidgetIds(thisAppWidget)
//        for (appWidgetId in ids) {
//            ClockWidget.updateAppWidget(context, appWidgetManager, appWidgetId)
//        }
        Log.d(TAG, "onReceive: ACTION_TIME_TICK")
    }
}
