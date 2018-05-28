package com.besaba.anvarov.orenclock

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log

class UpdateService : Service() {

    private val TAG = "ClockWidget"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: UpdateService")

        val mTimeChanged: TimeTickReceiver? = null
        this.registerReceiver(mTimeChanged, IntentFilter("android.intent.action.TIME_TICK"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: UpdateService")
        updateClockWidget()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateClockWidget() {//Обновление виджета
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this.applicationContext.packageName,
                ClockWidget::class.java.name))
        ClockWidget.updateAppWidget(this.applicationContext, appWidgetManager, ids[0])
    }
    
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
