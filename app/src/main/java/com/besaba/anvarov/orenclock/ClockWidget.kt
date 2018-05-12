//@file:Suppress("JAVA_CLASS_ON_COMPANION")

package com.besaba.anvarov.orenclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import java.text.DateFormat
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class ClockWidget : AppWidgetProvider() {

    private val myLogs = "myLogs"
    private val updateWidget = "updateWidget"

    override fun onEnabled(context: Context?) {
        Log.d(myLogs, "onEnabled")
        super.onEnabled(context)
        val intent = Intent(context, ClockWidget::class.java)
        intent.action = updateWidget
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pIntent)
    }

    override fun onDisabled(context: Context?) {
        Log.d(myLogs, "onDisabled")
        super.onDisabled(context)
        val intent = Intent(context, ClockWidget::class.java)
        intent.action = updateWidget
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.cancel(pIntent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        Log.d(myLogs, "onUpdate")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, 0)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(myLogs, "onReceive")

        if (intent?.action.equals(updateWidget)) {
            val thisAppWidget = ComponentName(context?.packageName, ClockWidget::class.java.name)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(thisAppWidget)

            val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmInfo = manager.nextAlarmClock.triggerTime

            for (appWidgetId in ids) {
                updateAppWidget(context, appWidgetManager, appWidgetId, alarmInfo)
            }
        }
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int, triggerTime: Long) {

            // делаю RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.clock_widget)
            views.setTextViewText(R.id.tvDate, getCurDate())
            views.setTextViewText(R.id.tvTime, getCurTime())

            val alarmUp = triggerTime > 0

            when {
                alarmUp -> {
                    views.setViewVisibility(R.id.ivAlarm, View.VISIBLE)
                    views.setTextViewText(R.id.tvAlarm, triggerTime.toString())
                }
                else -> {
                    views.setViewVisibility(R.id.ivAlarm, View.INVISIBLE)
                    views.setViewVisibility(R.id.tvAlarm, View.INVISIBLE)
                }
            }

            // обновляю виджет
            appWidgetManager.updateAppWidget(appWidgetId, views)

        }

        private fun getCurDate(): String {
            return DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time)
        }

        private fun getCurTime(): String {
            val cal: Calendar = Calendar.getInstance()
            val min = cal.get(Calendar.MINUTE)
            val hour = cal.get(Calendar.HOUR_OF_DAY)

            return when {
                (cal.get(Calendar.MINUTE) <= 9) -> {
                    hour.toString() + ":" + "0" + min
                }
                else -> hour.toString() + ":" + min
            }
        }
    }

}

