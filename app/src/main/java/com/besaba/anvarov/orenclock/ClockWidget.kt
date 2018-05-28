//@file:Suppress("JAVA_CLASS_ON_COMPANION")

package com.besaba.anvarov.orenclock

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class ClockWidget : AppWidgetProvider() {

    private val TAG = "ClockWidget"

    //    вызывается системой при создании первого экземпляра виджета
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.d(TAG, "onEnabled: ClockWidget")
        val intentUpdate = Intent(context, UpdateService::class.java)
        context?.startService(intentUpdate)
    }

    //    вызывается при удалении последнего экземпляра виджета.
    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled: ClockWidget")
        val intentUpdate = Intent(context, UpdateService::class.java)
        context?.stopService(intentUpdate)
    }

    //    вызывается при обновлении виджета. На вход, кроме контекста, метод получает объект
    //    AppWidgetManager и список ID экземпляров виджетов, которые обновляются. Именно этот метод
    //    обычно содержит код, который обновляет содержимое виджета. Для этого нам нужен будет
    //    AppWidgetManager, который мы получаем на вход.
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate: ClockWidget")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {

        fun updateAppWidget(context: Context?, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            // делаю RemoteViews object
            val views = RemoteViews(context?.packageName, R.layout.clock_widget)
            views.setTextViewText(R.id.tvDate, getCurDate())
            views.setTextViewText(R.id.tvTime, getCurTime())

            val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val nextAlarm = manager.nextAlarmClock
            when {
                nextAlarm != null -> {
                    views.setOnClickPendingIntent(R.id.llAlarm, manager.nextAlarmClock.showIntent)
                    views.setTextViewText(R.id.tvAlarm, getAlarmTime(manager.nextAlarmClock.triggerTime))
                    views.setImageViewResource(R.id.ivAlarm, R.drawable.ic_alarm_on)
                }
                else -> {
                    val alarmIntent = Intent("android.intent.action.SET_ALARM")
                    val pIntent = PendingIntent.getActivity(context, appWidgetId, alarmIntent, 0)
                    views.setOnClickPendingIntent(R.id.llAlarm, pIntent)
                    views.setTextViewText(R.id.tvAlarm, "")
                    views.setImageViewResource(R.id.ivAlarm, R.drawable.ic_alarm_off)
                }
            }

            val timeIntent = Intent("android.settings.DATE_SETTINGS")
            val pIntent = PendingIntent.getActivity(context, appWidgetId, timeIntent, 0)
            views.setOnClickPendingIntent(R.id.tvTime, pIntent)

            // обновляю виджет
            appWidgetManager.updateAppWidget(appWidgetId, views)

        }

        @SuppressLint("SimpleDateFormat")
        private fun getAlarmTime(alarmTime: Long): String {
            return SimpleDateFormat("E HH:mm").format(alarmTime)
        }

        private fun getCurDate(): String {
            return DateFormat.getDateInstance(DateFormat.FULL)
                    .format(Calendar.getInstance().time)
        }

        private fun getCurTime(): String {
            val cal = Calendar.getInstance()
            val min = cal.get(Calendar.MINUTE)
            val hour = cal.get(Calendar.HOUR_OF_DAY)

            return when {
                (min < 10) -> hour.toString() + ":" + "0" + min
                else -> hour.toString() + ":" + min
            }
        }
    }

}

