package com.besaba.anvarov.orenclock

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder

class ClockWidgetUpdateService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        updateInfoWidget()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateInfoWidget() {//Обновление виджета
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this.applicationContext.packageName,
                ClockWidget::class.java.name))
                ClockWidget.updateAppWidget(this.getApplicationContext(),appWidgetManager,ids[0])
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
