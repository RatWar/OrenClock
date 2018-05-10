package com.besaba.anvarov.orenclock

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder

class ClockWidgetUpdateService : Service() {
//    private val myLogs = "myLogs"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        updateInfoWidget()
        return super.onStartCommand(intent, flags, startId)
    }

    //Обновление виджета
    private fun updateInfoWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        // получаю id виджетов
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this.applicationContext.packageName,
                ClockWidget::class.java.name))

//        Log.d(myLogs, "onStartCommand " + ids[0].toString())
        if (ids != null)
            ClockWidget.updateAppWidget(this.applicationContext, appWidgetManager, ids[0])  // обновляю только один виджет
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
