//@file:Suppress("JAVA_CLASS_ON_COMPANION")

package com.besaba.anvarov.orenclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import java.text.DateFormat
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class ClockWidget : AppWidgetProvider() {

//    private val myLogs = "myLogs"
    private var service: PendingIntent? = null

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
//        Log.d(myLogs, "onUpdate")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
//        Log.d(myLogs, "onReceive")

//        Получаем объект AlarmManager и установим время начала отсчёта интервала
// (в данном случае отсчёт начнётся сразу после запуска задачи)
        val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val startTime = Calendar.getInstance()
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.SECOND, 0)
        startTime.set(Calendar.MILLISECOND, 0)
//        Получаем созданную ранее вспомогательную службу
        val i = Intent(context, ClockWidgetUpdateService::class.java)
        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT)
        }
//        Далее запускаем задачу
        manager.setInexactRepeating(AlarmManager.RTC, startTime.time.time, 60000, service)
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            // делаю RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.clock_widget)
            views.setTextViewText(R.id.tvDate, getCurDate())
            views.setTextViewText(R.id.tvTime, getCurTime())
            views.setTextViewText(R.id.tvAlarm, appWidgetId.toString())
            views.setViewVisibility(R.id.ivAlarm, View.INVISIBLE)

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

