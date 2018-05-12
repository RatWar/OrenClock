//@file:Suppress("JAVA_CLASS_ON_COMPANION")

package com.besaba.anvarov.orenclock

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class ClockWidget : AppWidgetProvider() {

    private val updateWidget = "updateWidget"

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        val intent = Intent(context, ClockWidget::class.java)
        intent.action = updateWidget
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pIntent)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        val intent = Intent(context, ClockWidget::class.java)
        intent.action = updateWidget
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.cancel(pIntent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action.equals(updateWidget)) {
            val thisAppWidget = ComponentName(context?.packageName, ClockWidget::class.java.name)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(thisAppWidget)

            for (appWidgetId in ids) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }

    companion object {

        internal fun updateAppWidget(context: Context?, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            // делаю RemoteViews object
            val views = RemoteViews(context?.packageName, R.layout.clock_widget)
            views.setTextViewText(R.id.tvDate, getCurDate())
            views.setTextViewText(R.id.tvTime, getCurTime())

            val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val nextAlarm = manager.nextAlarmClock
            when {
                nextAlarm != null -> {
                    views.setOnClickPendingIntent(R.id.ivAlarm, manager.nextAlarmClock.showIntent)
                    views.setTextViewText(R.id.tvAlarm, getAlarmTime(manager.nextAlarmClock.triggerTime))
                    views.setViewVisibility(R.id.tvAlarm, View.VISIBLE)
                }
                else -> {
                    val alarmIntent = Intent("android.intent.action.SET_ALARM")
                    val pIntent = PendingIntent.getActivity(context, appWidgetId, alarmIntent, 0)
                    views.setOnClickPendingIntent(R.id.ivAlarm, pIntent)
                    views.setViewVisibility(R.id.tvAlarm, View.INVISIBLE)
                }
            }

            // обновляю виджет
            appWidgetManager.updateAppWidget(appWidgetId, views)

        }

        @SuppressLint("SimpleDateFormat")
        private fun getAlarmTime(alarmTime: Long): String {
            return SimpleDateFormat("E HH:mm").format(alarmTime)
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

