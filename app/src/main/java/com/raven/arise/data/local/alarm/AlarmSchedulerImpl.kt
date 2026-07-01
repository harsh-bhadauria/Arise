package com.raven.arise.data.local.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.raven.arise.data.local.wakeUpCheck.WakeUpCheckReceiver
import com.raven.arise.domain.models.Alarm
import com.raven.arise.domain.models.AlarmScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.util.Calendar


@SuppressLint("ScheduleExactAlarm")
class AlarmSchedulerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(alarm: Alarm) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("label", alarm.title)
            putExtra("id", alarm.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            val now = Calendar.getInstance()
            if (alarm.repeatDays.isNotEmpty()) {
                var daysToAdd = 7
                for (repeatDay in alarm.repeatDays) {
                    val targetCalendarDay = repeatDay + 1 // 0=Sun to 1=Sun
                    val currentCalendarDay = get(Calendar.DAY_OF_WEEK)
                    
                    var diff = targetCalendarDay - currentCalendarDay
                    if (diff < 0 || (diff == 0 && before(now))) {
                        diff += 7
                    }
                    if (diff < daysToAdd) {
                        daysToAdd = diff
                    }
                }
                add(Calendar.DATE, daysToAdd)
            } else {
                if (before(now)) {
                    add(Calendar.DATE, 1)
                }
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            //System.currentTimeMillis() + 5 * 1000, // For testing purposes
            pendingIntent
        )
    }

    override fun cancel(alarm: Alarm) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun scheduleWakeUpCheck() {
        val intent = Intent(context, WakeUpCheckReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            222,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("WakeUp", "Scheduling wake-up check in 3 minutes")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 2 * 60 * 1000,
            pendingIntent
        )
    }

}