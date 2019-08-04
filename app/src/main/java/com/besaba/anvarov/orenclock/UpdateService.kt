package com.besaba.anvarov.orenclock

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.*
import android.os.IBinder

class UpdateService : Service() {

//    private val TAG = "ClockWidget"

    private val mTimeChanged = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            Log.d(TAG, "TimeTickReceiver")
            updateClockWidget()
        }
    }

    override fun onCreate() {
        super.onCreate()
//        Log.d(TAG, "onCreate: UpdateService")
        updateClockWidget()
        this.registerReceiver(mTimeChanged, IntentFilter("android.intent.action.TIME_TICK"))
    }

    override fun onDestroy() {
//        Log.d(TAG, "onDestroy: UpdateService")
        this.unregisterReceiver(mTimeChanged)
        super.onDestroy()
    }

    private fun updateClockWidget() {//Обновление виджета
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(
            ComponentName(
                this.applicationContext.packageName,
                ClockWidget::class.java.name
            )
        )
        ClockWidget.updateAppWidget(this.applicationContext, appWidgetManager, ids[0])
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
