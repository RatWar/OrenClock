@file:Suppress("JAVA_CLASS_ON_COMPANION")

package com.besaba.anvarov.orenclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.DateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class ClockWidget : AppWidgetProvider() {

    private val updateWidgets = "updateWidgets"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        val intent = Intent(context, ClockWidget.javaClass)
        intent.action = updateWidgets
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pIntent)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        val intent = Intent(context, ClockWidget.javaClass)
        intent.action = updateWidgets
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pIntent)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action.equals(updateWidgets, true)) {
            val thisAppWidget = ComponentName(context?.packageName, javaClass.name)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            for (appWidgetId in appWidgetManager.getAppWidgetIds(thisAppWidget)) {
                updateAppWidget(context!!, appWidgetManager, appWidgetId)
            }
        }
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.clock_widget)
            views.setTextViewText(R.id.tvDate, getCurDate())
            views.setTextViewText(R.id.tvTime, getCurTime())

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)

        }

        private fun getCurDate(): String {
            return DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time)
        }

        private fun getCurTime(): String {
            return DateFormat.getTimeInstance(DateFormat.SHORT).format(Calendar.getInstance().time)
        }
    }

}

