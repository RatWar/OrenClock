package com.besaba.anvarov.orenclock

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ClockWidgetUpdateService : Service() {
    private val myLogs = "myLogs"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        updateInfoWidget()
        Log.d(myLogs, "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateInfoWidget() {//Обновление виджета
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this.applicationContext.packageName,
                ClockWidget::class.java.name))
                ClockWidget.updateAppWidget(this.applicationContext,appWidgetManager,ids[0])
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
