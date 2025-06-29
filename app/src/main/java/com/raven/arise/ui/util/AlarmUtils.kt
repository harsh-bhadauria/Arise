package com.raven.arise.ui.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.raven.arise.data.local.alarm.AlarmReceiver
import com.raven.arise.data.local.alarm.AlarmService

fun stopAlarmService(context: Context) {
    val intent = Intent(context, AlarmService::class.java)
    context.stopService(intent)
}

fun pauseAlarmService(context: Context) {
    val intent = Intent(context, AlarmService::class.java).apply {
        action = AlarmService.ACTION_PAUSE
    }
    context.startService(intent)
}

fun resumeAlarmService(context: Context) {
    val intent = Intent(context, AlarmService::class.java).apply {
        action = AlarmService.ACTION_RESUME
    }
    context.startService(intent)
}

fun stopReRing(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val reRingIntent = Intent(context, AlarmReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        101,
        reRingIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.cancel(pendingIntent)
}
